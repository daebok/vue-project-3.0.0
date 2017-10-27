package com.rd.ifaes.core.tpp.service.tpp.impl;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.rd.account.service.AccountLogService;
import com.rd.account.service.AccountService;
import com.rd.ifaes.common.constant.UfxConstant;
import com.rd.ifaes.common.dict.CacheKeys;
import com.rd.ifaes.common.dict.ExpireTime;
import com.rd.ifaes.common.rabbit.RabbitUtils;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.core.account.domain.Cash;
import com.rd.ifaes.core.account.service.CashService;
import com.rd.ifaes.core.account.service.FreezeTradeLogService;
import com.rd.ifaes.core.core.constant.Constant;
import com.rd.ifaes.core.core.constant.ResourceConstant;
import com.rd.ifaes.core.core.util.CacheUtils;
import com.rd.ifaes.core.core.util.ConfigUtils;
import com.rd.ifaes.core.core.util.ResourceUtils;
import com.rd.ifaes.core.tpp.exception.UfxException;
import com.rd.ifaes.core.tpp.model.ufx.UfxCashAuditModel;
import com.rd.ifaes.core.tpp.service.tpp.UfxCashAuditService;
import com.rd.ifaes.core.tpp.service.tpp.UfxService;
import com.rd.ifaes.core.user.service.UserService;

/**
 * 取现复核业务回调
 */
@Service("ufxCashAuditService")
@Transactional(propagation=Propagation.SUPPORTS)
public class UfxCashAuditServiceImpl implements UfxCashAuditService {
	/**
	 * 日志跟踪器
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(UfxCashAuditServiceImpl.class);

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
	 * 提现复核异步回调
	 */
	@Override
	public void doTppCashAuditService(final UfxCashAuditModel model) {
		//回调日志
			LOGGER.info("取现复核订单OrderNo={}进入回调业务方法，原取现订单号：{}回调结果：{}" , model.getOrderNo(), model.getCashNo(), model.getRespCode() );
		
		//根据订单获取取现记录
		Cash dbCash = cashService.findByCashNo(model.getCashNo()); 
		if (dbCash == null) {
			LOGGER.info("取现订单CashNo={}不存在", model.getCashNo());
			return;
		}
		
		//处理返回码
		if(UfxConstant.UFX_CODE_DOING.equals(model.getRespCode())){//处理中0002
			if(Constant.CASH_STATUS_AUDIT.equals(dbCash.getStatus())){ //待审核->处理中
				//设置状态为取现处理中
				dbCash.setStatus(Constant.CASH_STATUS_PROCESS);
				dbCash.setPreStatus(Constant.CASH_STATUS_AUDIT);
				cashService.updateByTppResult(dbCash);
			}
			
		}else if(UfxConstant.UFX_CODE_SUCCESS.equals(model.getRespCode())){//处理成功0000
			
			if(Constant.CASH_STATUS_PROCESS.equals(dbCash.getStatus())||
					Constant.CASH_STATUS_AUDIT.equals(dbCash.getStatus())){ //待审核或者处理中
				
				if(UfxCashAuditModel.AUDIT_FLAG_Y.equals(model.getAuditFlag())){ //审核通过
					LOGGER.info("提现订单CashNo={}审核通过，扣除冻结金额：{}元。",dbCash.getCashNo(),dbCash.getMoney());
					//更改提现状态
					dbCash.setStatus(Constant.CASH_STATUS_SUCCESS);
					cashService.updateByTppResult(dbCash);
					//更改资金
					cashService.doCashLogByType(Constant.CASH_AUDIT_SUCCESS, dbCash);
					
				}else{ //审核不通过
					LOGGER.info("提现订单{}审核不通过，解冻金额：{}元。",dbCash.getCashNo(),dbCash.getMoney());
					//更改提现状态
					dbCash.setStatus(Constant.CASH_STATUS_FAIL);
					dbCash.setRemark(ResourceUtils.get(ResourceConstant.CASH_AUDIT_FAIL));
					cashService.updateByTppResult(dbCash);
					//更改资金
					cashService.doCashLogByType(Constant.CASH_FAIL, dbCash);
				}

			}
			
		}else{//处理失败0001或其它返回码XXXX
			if(Constant.CASH_STATUS_PROCESS.equals(dbCash.getStatus())||
					Constant.CASH_STATUS_AUDIT.equals(dbCash.getStatus())){ //待审核或者处理中
				if(UfxCashAuditModel.AUDIT_FLAG_Y.equals(model.getAuditFlag())){ //审核通过
					LOGGER.info("提现订单CashNo={}审核通过，但处理失败，退还冻结金额：{}元。",dbCash.getCashNo(),dbCash.getMoney());
					//更改提现状态
					dbCash.setStatus(Constant.CASH_STATUS_FAIL);
					dbCash.setRemark(model.getRespDesc());
					cashService.updateByTppResult(dbCash);
					//审核通过，但处理失败，退还冻结金额
					cashService.doCashLogByType(Constant.CASH_FAIL, dbCash);
					
				}else{ //审核不通过
					LOGGER.info("提现订单CashNo={},发生错误异常，审核不通过但又处理失败。",dbCash.getCashNo());
					cashService.cashNeedManHandle(dbCash, ResourceUtils.get(ResourceConstant.CASH_AUDIT_TPP_ERROR));
				}
			}
			
			//如果是异步对账回调，则还要处理"处理成功->处理失败"这种情况
			if(UfxConstant.CASH.equals(model.getRespType())){
				if(Constant.CASH_STATUS_SUCCESS.equals(dbCash.getStatus())){
					if(UfxCashAuditModel.AUDIT_FLAG_Y.equals(model.getAuditFlag())){ //审核通过
						LOGGER.info("提现订单CashNo={}异步回调处理成功，但是异步对账失败，返还可用余额",dbCash.getCashNo());
						//更改提现状态
						dbCash.setStatus(Constant.CASH_STATUS_FAIL);
						dbCash.setRemark(model.getRespDesc());
						cashService.updateByTppResult(dbCash);
						//提现对账失败，退回可用余额
						cashService.doCashLogByType(Constant.CASH_BANK_FAIL, dbCash);
						
					}else{ //审核不通过
						LOGGER.info("提现订单CashNo={}异步回调处理成功，但是异步对账失败，需进行人工处理",dbCash.getCashNo());
						cashService.cashNeedManHandle(dbCash, ResourceUtils.get(ResourceConstant.CASH_AUDIT_TPP_ERROR));
					}
				}
			}
		}
		
	}

	/**
	 * 处理取现回调业务
	 * @param resultFlag
	 */
	@Override
	public void doCashAudit(final UfxCashAuditModel model) {
		final boolean ret = model.validSign(model);
		LOGGER.debug("取现验签结果：{}" , ret);
		
		if (ret) {
			LOGGER.info("取现复核进入异步回调处理，订单号CashNo={}，回调参数：{}",model.getCashNo(), model.toString());
			
			//重复处理判断(缓存标记) 投资回调处理 计数 失效时间： 1天
			final String handleKey = String.format(CacheKeys.PREFIX_CASH_AUDIT_TPP_HANDLE_NUM.getKey(),
					model.getOrderNo(), model.getRespType(), model.getRespCode());
			if (CacheUtils.incr(handleKey, Constant.DOUBLE_ONE) > Constant.DOUBLE_ONE) {
				LOGGER.info("{}----提现复核订单已处理该状态",StringUtils.isNull(model.getOrderNo()));
				return;
			}
			// 更新失效时间
			CacheUtils.expire(handleKey, ExpireTime.ONE_HOUR);
			
			if (UfxConstant.CASH.equals(model.getRespType())) {
				LOGGER.info("取现审核异步对账回调进入本地处理，返回状态：{}，订单号：{}" ,model.getRespCode(), model.getOrderNo());
			} else {
				LOGGER.info("取现审核异步回调进入本地处理，返回状态：{}，订单号：{}" ,model.getRespCode(), model.getOrderNo());
			}
			if(ConfigUtils.isOpenMq()){
				RabbitUtils.cashAudit(model);
			}else{
				doTppCashAuditService(model);
			}
		} else {
			LOGGER.info("取现复核验签失败，订单号：{}" , model.getOrderNo());
			throw new UfxException(ResourceUtils.get(ResourceConstant.TPP_NOTIFY_SIGN_FAIL));
		}
	}
	
}
