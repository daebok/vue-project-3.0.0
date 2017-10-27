package com.rd.ifaes.core.tpp.service.tpp.impl;

import java.math.BigDecimal;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.rd.account.domain.Account;
import com.rd.account.model.AccountQueryModel;
import com.rd.account.service.AccountLogService;
import com.rd.account.service.AccountService;
import com.rd.ifaes.common.constant.UfxConstant;
import com.rd.ifaes.common.dict.CacheKeys;
import com.rd.ifaes.common.dict.CommonEnum;
import com.rd.ifaes.common.dict.DictTypeEnum;
import com.rd.ifaes.common.dict.ExpireTime;
import com.rd.ifaes.common.rabbit.RabbitUtils;
import com.rd.ifaes.common.util.BigDecimalUtils;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.core.account.domain.Cash;
import com.rd.ifaes.core.account.domain.FreezeTradeLog;
import com.rd.ifaes.core.account.model.CashModel;
import com.rd.ifaes.core.account.service.CashService;
import com.rd.ifaes.core.account.service.FreezeTradeLogService;
import com.rd.ifaes.core.core.constant.Constant;
import com.rd.ifaes.core.core.constant.ResourceConstant;
import com.rd.ifaes.core.core.util.CacheUtils;
import com.rd.ifaes.core.core.util.ConfigUtils;
import com.rd.ifaes.core.core.util.DictUtils;
import com.rd.ifaes.core.core.util.ResourceUtils;
import com.rd.ifaes.core.tpp.exception.UfxException;
import com.rd.ifaes.core.tpp.model.ufx.UfxCashModel;
import com.rd.ifaes.core.tpp.model.ufx.UfxFreezeModel;
import com.rd.ifaes.core.tpp.model.ufx.UfxModel;
import com.rd.ifaes.core.tpp.service.tpp.UfxCashService;
import com.rd.ifaes.core.tpp.service.tpp.UfxService;
import com.rd.ifaes.core.user.domain.User;
import com.rd.ifaes.core.user.service.UserService;

/**
 * 取现业务回调
 */
@Service("ufxCashService")
@Transactional(propagation=Propagation.SUPPORTS)
public class UfxCashServiceImpl implements UfxCashService {
	/**
	 * 日志跟踪器
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(UfxCashServiceImpl.class);

	/**
	 * UFX处理类
	 */
	@Resource
	private transient UfxService ufxService;
	/**
	 * 用户基础类
	 */
	@Resource
	private transient UserService userService;
	/**
	 * 取现操作类
	 */
	@Resource
	private transient CashService cashService;
	/**
	 * 账户资金处理类
	 */
	@Resource
	private transient AccountService accountService;
	/**
	 * 交易冻结操作类
	 */
	@Resource
	private transient FreezeTradeLogService freezeLogService;
	/**
	 * 资金日志操作类
	 */
	@Resource
	private transient AccountLogService accountLogService;
	
	/**
	 * 提现异步回调
	 */
	@Override
	@Transactional(readOnly=false)
	public void doTppCashService(final CashModel model) {
		//回调日志
		LOGGER.info("取现订单OrderNo={}进入回调业务方法，回调结果：{}" ,model.getOrderNo(), model.getReturnCode());
		
		//根据订单获取取现记录
		Cash dbCash = cashService.findByOrderNo(model.getOrderNo()); 
		if (dbCash == null) {
			LOGGER.info("订单{}不存在",model.getOrderNo());
			return;
		}
		
		// 提现是否需要审核
		final String cashNeedAudit = ConfigUtils.getValue(Constant.CASH_NEED_AUDIT);
		dbCash.setCashNo(model.getCashNo());          //取现订单号（第三方）
		dbCash.setCardId(model.getCardId());          //银行卡ID
		dbCash.setCashFee(model.getCashFee());        //取现手续费
		dbCash.setRealAmount(model.getRealAmount());  //实际到账金额
		
		// 将第三方取现手续费与平台服务费单独放到两个字段，只针对UFX
		if(dbCash.getCashFee()!=null && dbCash.getServFee()!=null){ 
			dbCash.setCashFee(BigDecimalUtils.sub(dbCash.getCashFee(),dbCash.getServFee()));
		}
		
		//实际操作金额
		if(dbCash.getRealAmount()!=null && dbCash.getCashFee()!=null){
			if(CommonEnum.YES.eq(dbCash.getIsAdvance())){ //是否垫付手续费,如果垫付手续费，则只收服务费
				dbCash.setMoney(BigDecimalUtils.add(dbCash.getRealAmount(), dbCash.getServFee()));
			}else{//否则收取第三方收取的手续费和平台收取的服务费
				dbCash.setMoney(BigDecimalUtils.add(dbCash.getRealAmount(), dbCash.getCashFee(), dbCash.getServFee()));
			}
		}
				
		//处理返回码
		//处理中
		if(UfxConstant.UFX_CODE_DOING.equals(model.getReturnCode())){//处理中0002
			if(Constant.CASH_STATUS_APPLY.equals(dbCash.getStatus())){ //申请提现->处理中
				//设置状态为取现处理中
				dbCash.setStatus(Constant.CASH_STATUS_PROCESS);
				dbCash.setPreStatus(Constant.CASH_STATUS_APPLY);
				cashService.updateByTppResult(dbCash);
				//冻结本地可用资金
				cashService.doCashLogByType(Constant.CASH_PROCCESS, dbCash);
			}
			
		}else if(UfxConstant.UFX_CODE_VERIFY.equals(model.getReturnCode())){//待审核(双乾/浙商)0003
			if(Constant.CASH_STATUS_APPLY.equals(dbCash.getStatus())){ //申请提现 ->待审核
				doNeedAuditCash(model, dbCash);
			}
			
		}else if(UfxConstant.UFX_CODE_SUCCESS.equals(model.getReturnCode())){//处理成功0000
			if (Constant.FLAG_YES.equals(cashNeedAudit)) {// 提现需要审核
				LOGGER.info("提现需要审核----");
				if (Constant.CASH_STATUS_APPLY.equals(dbCash.getStatus())){
					doNeedAuditCash(model, dbCash);
				}
				
			}else{// 提现不需要审核
				LOGGER.info("提现不需要审核----");
				if (Constant.CASH_STATUS_APPLY.equals(dbCash.getStatus())){
					LOGGER.info("提现订单{}处理成功，扣除冻结金额：{}",dbCash.getOrderNo(),dbCash.getMoney());
					//更改提现状态
					dbCash.setStatus(Constant.CASH_STATUS_SUCCESS);
					dbCash.setRemark(doCashRemark(model, dbCash));
					cashService.updateByTppResult(dbCash);
					//扣除金额
					Account account = accountService.getMoneyByUserId(new AccountQueryModel(dbCash.getUserId(), ConfigUtils.getValue(Constant.ACCOUNT_CODE)));
					if(BigDecimalUtils.add(account.getUseMoney(),dbCash.getMoney().negate()).compareTo(BigDecimal.ZERO)>=0){
						//扣除实际提现操作金额
						cashService.doCashLogByType(Constant.CASH_SUCCESS, dbCash);
					}else{
						//预警短信
						cashService.cashNeedManHandle(dbCash, ResourceUtils.get(ResourceConstant.CASH_ACCOUNT_ERROR));
					}
				}
			}
			
		}else{//处理失败0001或其它返回码XXXX
			if(Constant.CASH_STATUS_APPLY.equals(dbCash.getStatus())){
				LOGGER.info("提现订单{}处理失败，解冻金额：{}",dbCash.getOrderNo(),dbCash.getAmount());
				//设置状态为取现失败
				dbCash.setStatus(Constant.CASH_STATUS_FAIL);
				dbCash.setRemark(doCashRemark(model, dbCash));
				cashService.updateByTppResult(dbCash);
			}
		}
		
	}
	
	@Override
	@Transactional(readOnly=false)
	public void doCashBankReturn(final CashModel model) {
		LOGGER.info("取现订单{}进入异步对账业务，回调结果：{}" , model.getOrderNo(), model.getReturnCode() );
		
		//根据订单获取取现记录
		Cash dbCash = cashService.findByOrderNo(model.getOrderNo()); 
		if (dbCash == null) {
			LOGGER.info("订单{}不存在",model.getOrderNo());
			return;
		}
		
		dbCash.setCashNo(model.getCashNo());          //取现订单号（第三方）
		dbCash.setCardId(model.getCardId());          //银行卡ID
		dbCash.setCashFee(model.getCashFee());        //取现手续费
		dbCash.setRealAmount(model.getRealAmount());  //实际到账金额
		
		// 将第三方取现手续费与平台服务费单独放到两个字段，只针对UFX
		if(dbCash.getCashFee()!=null && dbCash.getServFee()!=null){ 
			dbCash.setCashFee(BigDecimalUtils.sub(dbCash.getCashFee(),dbCash.getServFee()));
		}
		
		//实际操作金额
		if(dbCash.getRealAmount()!=null && dbCash.getCashFee()!=null){
			if(CommonEnum.YES.eq(dbCash.getIsAdvance())){ //是否垫付手续费,如果垫付手续费，则只收服务费
				dbCash.setMoney(BigDecimalUtils.add(dbCash.getRealAmount(), dbCash.getServFee()));
			}else{//否则收取第三方收取的手续费和平台收取的服务费
				dbCash.setMoney(BigDecimalUtils.add(dbCash.getRealAmount(), dbCash.getCashFee(), dbCash.getServFee()));
			}
		}
		
		if(UfxConstant.UFX_CODE_SUCCESS.equals(model.getReturnCode())){//处理成功0000
			LOGGER.info("提现订单{}，银行处理成功，扣除金额：{}",dbCash.getOrderNo(),dbCash.getMoney());
			
			if(Constant.CASH_STATUS_PROCESS.equals(dbCash.getStatus())){
				//更改提现状态
				dbCash.setStatus(Constant.CASH_STATUS_SUCCESS);
				dbCash.setRemark(doCashRemark(model, dbCash));
				cashService.updateByTppResult(dbCash);
				
				//扣除冻结金额
				Account account = accountService.getMoneyByUserId(new AccountQueryModel(dbCash.getUserId(), ConfigUtils.getValue(Constant.ACCOUNT_CODE)));
				if(BigDecimalUtils.add(account.getNoUseMoney(),dbCash.getMoney().negate()).compareTo(BigDecimal.ZERO)>=0){
					//扣除实际提现操作金额,CASH_AUDIT_SUCCESS因为是扣除冻结金额，所以选这个状态
					cashService.doCashLogByType(Constant.CASH_AUDIT_SUCCESS, dbCash);
				}else{
					cashService.cashNeedManHandle(dbCash, ResourceUtils.get(ResourceConstant.CASH_ACCOUNT_ERROR));
				}
			}	
			
		} else if (UfxConstant.UFX_CODE_FAIL.equals(model.getReturnCode())) {	//取现处理失败
			LOGGER.info("提现订单{}，银行处理失败，解冻金额：{}",dbCash.getOrderNo(),dbCash.getAmount() );
			
			//设置状态为取现失败
			final String oldDbStatus = dbCash.getStatus();
			dbCash.setStatus(Constant.CASH_STATUS_FAIL);
			dbCash.setRemark(doCashRemark(model, dbCash));
			cashService.updateByTppResult(dbCash);
			
			//处理资金
			if(Constant.CASH_STATUS_PROCESS.equals(oldDbStatus)){ //提现处理中,解冻金额
				cashService.doCashLogByType(Constant.CASH_FAIL, dbCash);
				
			}else if(Constant.CASH_STATUS_SUCCESS.equals(oldDbStatus)){ //处理成功
				cashService.doCashLogByType(Constant.CASH_BANK_FAIL, dbCash);
			}
			

		}
	}

	/**
	 * 冻结用户取现金额
	 * @param cash
	 */
	private void freezeTppCash(final Cash cash) {
		final FreezeTradeLog existFreezeLog = freezeLogService.findByOrderNo(FreezeTradeLog.FREEZE_TYPE_CASH, cash.getOrderNo());
		if(existFreezeLog==null){
			final UfxModel tppModel = new UfxModel();
			final User user = userService.get(cash.getUserId());
			tppModel.setUserId(user.getUserNo());
			tppModel.setTppUserCustId(user.getTppUserCustId());
			tppModel.setMoney(cash.getMoney());
			tppModel.setFreezeType(UfxFreezeModel.FREEZE_CASH);
			final String trxId = ufxService.freeze(tppModel).getFreezeNo();
			LOGGER.info("冻结资金订单号：{}",trxId);
			if (StringUtils.isNotBlank(trxId)) {
				final FreezeTradeLog freezeLog = new FreezeTradeLog(FreezeTradeLog.FREEZE_TYPE_CASH, cash.getOrderNo(), trxId);
				freezeLog.setAmount(tppModel.getMoney());
				freezeLog.setUserId(user.getUuid());
				freezeLogService.insert(freezeLog);
			}
		}
	}
	
	public String doCashRemark(final CashModel model,final Cash cash){
		StringBuffer remark = new StringBuffer();
		if(UfxConstant.UFX_CODE_SUCCESS.equals(model.getReturnCode())){
			String bankName = DictUtils.getItemName(DictTypeEnum.ACCOUNT_BANK.getValue(), cash.getBankCode());
			int length = model.getCardId().length();
			String cardId = model.getCardId().substring(length-4, length);
			remark.append(bankName+"(尾号"+cardId+")");
		}else{
			remark.append(model.getReturnDesc());
		}
		return remark.toString();
	}
	
	/**
	 * 处理取现回调业务
	 * @param resultFlag
	 */
	@Override
	public void doCash(final UfxCashModel model) {
		final boolean ret = model.validSign(model);
		LOGGER.debug("取现验签结果：" + ret);
		
		if (ret) {
			LOGGER.info("取现进入异步回调处理，订单号OrderNo={}，回调参数：{}",model.getOrderNo(), model.toString());
			
			//重复处理判断(缓存标记) 投资回调处理 计数 失效时间： 1天
			final String handleKey = String.format(CacheKeys.PREFIX_CASH_TPP_HANDLE_NUM.getKey(),
					model.getOrderNo(), model.getRespType(), model.getRespCode());
			if (CacheUtils.incr(handleKey, Constant.DOUBLE_ONE) > Constant.DOUBLE_ONE) {
				LOGGER.info("{}----提现订单已处理该状态",StringUtils.isNull(model.getOrderNo()));
				return;
			}
			// 更新失效时间
			CacheUtils.expire(handleKey, ExpireTime.ONE_HOUR);
			
			//封装参数
			final CashModel cashModel = new CashModel();
			cashModel.setOrderNo(model.getOrderNo());
			cashModel.setCashNo(model.getCashNo());
			cashModel.setUser(userService.findByUserCustId(model.getUserCustId()));
			cashModel.setServFee(BigDecimalUtils.valueOf(model.getServFee()));
			cashModel.setCashFee(BigDecimalUtils.valueOf(model.getCashFee()));
			cashModel.setAmount(BigDecimalUtils.valueOf(model.getAmount()));
			cashModel.setRealAmount(BigDecimalUtils.valueOf(model.getRealAmount()));
			cashModel.setIsAdvance(model.getIsAdvance());
			cashModel.setReturnCode(model.getRespCode());
			cashModel.setReturnDesc(model.getRespDesc());
			cashModel.setCardId(model.getCardId());
			
			// 异步对账回调
			if (UfxConstant.CASH.equals(model.getRespType())) {
				LOGGER.info("取现异步对账回调》进入本地处理，返回状态：{}，订单号：{}" ,model.getRespCode(), model.getOrderNo());
				if(ConfigUtils.isOpenMq()){
					RabbitUtils.cashBankReturn(cashModel);
				}else{
					doCashBankReturn(cashModel);
				}
				
			//同步异步回调
			} else { 
				LOGGER.info("取现同步异步回调》进入本地处理，返回状态：{}，订单号：{}" ,model.getRespCode(), model.getOrderNo());
				if(ConfigUtils.isOpenMq()){
		            RabbitUtils.cash(cashModel);
		    	}else{
		    		doTppCashService(cashModel);
		    	}
				
			}
		} else {
			LOGGER.info("取现验签失败，订单号：{}" , model.getOrderNo());
			throw new UfxException(ResourceUtils.get(ResourceConstant.TPP_NOTIFY_SIGN_FAIL));
		}
	}
	
	/**
	 * 
	 * 有取现复核，同时第三方返回提现成功或者提现审核
	 * @author xhf
	 * @date 2016年9月26日
	 * @param dbCash
	 */
	private void doNeedAuditCash(CashModel cashModel, Cash dbCash){
		LOGGER.info("提现订单{}处理成功，提现状态改为待审核状态",dbCash.getOrderNo());
		//更改提现状态
		dbCash.setStatus(Constant.CASH_STATUS_AUDIT);
		dbCash.setRemark(doCashRemark(cashModel, dbCash));
		cashService.updateByTppResult(dbCash);
		
		//如果取现需要审核，第三方不会冻结用户这部分资金
		//为了避免在审核之前该笔资金被用户动用，所以需要先冻结该笔资金
		LOGGER.info("调用冻结资金接口，订单号：{}",dbCash.getOrderNo());
		try{
			freezeTppCash(dbCash);
		}catch(UfxException e){
			dbCash.setStatus(Constant.CASH_STATUS_PROCESS);
			cashService.cashNeedManHandle(dbCash, "提现第三方冻结失败："+e.getMessage());
		}
		
		//冻结实际提现操作金额
		if(Constant.CASH_STATUS_AUDIT.equals(dbCash.getStatus())){
			//冻结金额
			Account account = accountService.getMoneyByUserId(new AccountQueryModel(dbCash.getUserId(), ConfigUtils.getValue(Constant.ACCOUNT_CODE)));
			if(BigDecimalUtils.add(account.getUseMoney(),dbCash.getMoney().negate()).compareTo(BigDecimal.ZERO)>=0){
				cashService.doCashLogByType(Constant.CASH_AUDIT, dbCash);
			}else{
				cashService.cashNeedManHandle(dbCash, ResourceUtils.get(ResourceConstant.CASH_ACCOUNT_ERROR));
			}
		}
	}

}
