package com.rd.ifaes.core.account.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.google.common.collect.Maps;
import com.rd.account.domain.Account;
import com.rd.account.domain.AccountLog;
import com.rd.account.model.AccountModel;
import com.rd.account.model.AccountQueryModel;
import com.rd.account.service.AccountService;
import com.rd.ifaes.common.constant.CbhbConstant;
import com.rd.ifaes.common.constant.JxbankConstant;
import com.rd.ifaes.common.constant.TppServiceEnum;
import com.rd.ifaes.common.constant.UfxConstant;
import com.rd.ifaes.common.dict.CacheKeys;
import com.rd.ifaes.common.dict.CommonEnum;
import com.rd.ifaes.common.dict.DictTypeEnum;
import com.rd.ifaes.common.dict.ExpireTime;
import com.rd.ifaes.common.exception.BussinessException;
import com.rd.ifaes.common.orm.Page;
import com.rd.ifaes.common.security.shiro.IdGen;
import com.rd.ifaes.common.util.BigDecimalUtils;
import com.rd.ifaes.common.util.DateUtils;
import com.rd.ifaes.common.util.SessionUtils;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.common.util.TppUtil;
import com.rd.ifaes.core.account.domain.Cash;
import com.rd.ifaes.core.account.domain.CashFeeMarkLog;
import com.rd.ifaes.core.account.domain.FreezeTradeLog;
import com.rd.ifaes.core.account.mapper.CashMapper;
import com.rd.ifaes.core.account.model.CashModel;
import com.rd.ifaes.core.account.model.TppMerchantLogModel;
import com.rd.ifaes.core.account.service.AccountBankCardService;
import com.rd.ifaes.core.account.service.CashFeeMarkLogService;
import com.rd.ifaes.core.account.service.CashService;
import com.rd.ifaes.core.account.service.FreezeTradeLogService;
import com.rd.ifaes.core.account.service.TppMerchantLogService;
import com.rd.ifaes.core.base.service.BaseServiceImpl;
import com.rd.ifaes.core.core.constant.ConfigConstant;
import com.rd.ifaes.core.core.constant.Constant;
import com.rd.ifaes.core.core.constant.MessageConstant;
import com.rd.ifaes.core.core.constant.ResourceConstant;
import com.rd.ifaes.core.core.sms.msg.BaseMsg;
import com.rd.ifaes.core.core.sms.msg.WarnMsg;
import com.rd.ifaes.core.core.util.CacheUtils;
import com.rd.ifaes.core.core.util.ConfigUtils;
import com.rd.ifaes.core.core.util.LogTemplateUtils;
import com.rd.ifaes.core.core.util.ResourceUtils;
import com.rd.ifaes.core.statistic.constant.StatisticConstant;
import com.rd.ifaes.core.statistic.model.StatisticModel;
import com.rd.ifaes.core.statistic.service.StatisticService;
import com.rd.ifaes.core.tpp.exception.UfxException;
import com.rd.ifaes.core.tpp.model.cbhb.CbhbDrawingsModel;
import com.rd.ifaes.core.tpp.model.cbhb.CbhbHelper;
import com.rd.ifaes.core.tpp.model.jx.JxWithdrawModel;
import com.rd.ifaes.core.tpp.model.ufx.UfxAdditionalOrderModel;
import com.rd.ifaes.core.tpp.model.ufx.UfxCashAuditModel;
import com.rd.ifaes.core.tpp.model.ufx.UfxCashModel;
import com.rd.ifaes.core.tpp.model.ufx.UfxModel;
import com.rd.ifaes.core.tpp.model.ufx.UfxQueryBalanceModel;
import com.rd.ifaes.core.tpp.model.ufx.UfxUnFreezeModel;
import com.rd.ifaes.core.tpp.service.TppService;
import com.rd.ifaes.core.tpp.service.tpp.UfxService;
import com.rd.ifaes.core.tpp.util.JxConfig;
import com.rd.ifaes.core.user.domain.User;
import com.rd.ifaes.core.user.domain.UserCache;
import com.rd.ifaes.core.user.service.UserCacheService;
import com.rd.ifaes.core.user.service.UserService;


/**
 * 提现业务处理类
 * @author xhf
 * @version 3.0
 * @date 2016-7-2
 */
@Service("cashService") 
public class CashServiceImpl extends BaseServiceImpl<CashMapper, Cash> implements CashService{
	
	/**
	 * 日志跟踪器
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(CashServiceImpl.class);
	@Resource
	public transient AccountBankCardService accountBankCardService;
    /**
     * 用户基本类
     */
	@Resource
	private transient UserService userService;
	/**
	 * 资金处理类
	 */
    @Resource
    private transient AccountService accountService;
	/**
	 * ufx业务处理类
	 */
	@Resource
	private transient UfxService ufxService;
	/**
	 * 冻结交易日志处理类
	 */
	@Resource
	private transient FreezeTradeLogService freezeLogService;
	
	/**
	 * 附属信息类
	 */
	@Resource
	private transient UserCacheService userCacheService;
	
	@Resource
    private transient PlatformTransactionManager transactionManager;
    
	/**
	 * 统计信息处理类
	 */
	@Resource
	private transient StatisticService statisticService;
	
	@Resource
	private transient TppMerchantLogService tppMerchantLogService;
	
	@Resource
	private transient CashFeeMarkLogService cashFeeMarkLogService;
	
	/**
	 * 
	 * 取现-前期业务处理
	 * @author xhf
	 * @date 2016年8月29日
	 * @param model
	 * @return
	 */
	@Transactional(readOnly = false)
	@Override
	public Object doCash(final CashModel model){
		//校验用户是否登录
		final User loginUser = model.getUser();
		if(loginUser==null){
			throw new BussinessException(ResourceUtils.get(ResourceConstant.USER_NOT_LOGIN), BussinessException.TYPE_CLOSE);
		}
		User user = userService.get(loginUser.getUuid());
		UserCache userCache = userCacheService.findByUserId(loginUser.getUuid());
		
		//校验提现
		model.checkCash();

		//参数封装
		final Map<String,Object> map = Maps.newHashMap();
		map.put("accountId", user.getTppUserCustId());
		map.put("idType", JxbankConstant.JXBANK_ID_TYPE_ONE);
		map.put("idNo", userCache.getIdNo());
		map.put("name", user.getRealName());
		map.put("mobile", user.getMobile());
		map.put("cardNo", model.getCardId());
		map.put("routeCode", model.getRouteCode());
		if(CashModel.ROUTE_CODE_WHOLESALE.equals(model.getRouteCode())){
			//人行通道
			map.put("cardBankCnaps", model.getCardBankCnaps().trim());
		}
		String tppName = ConfigUtils.getTppName();
		TppService tppService  = (TppService)TppUtil.tppService();
		final Cash cash = model.prototype();
		cash.setUuid(IdGen.uuid());
		cash.setUserId(user.getUuid());
		cash.setStatus(Constant.CASH_STATUS_APPLY);
		cash.setAddTime(DateUtils.getNow());
		cash.setCardId(model.getCardId());
		cash.setBankCode(model.getBankCode());
		cash.setAddIp(model.getAddIp());
		cash.setAmount(model.getAmount());
		cash.setIsAdvance(CommonEnum.NO.getValue());//默认平台不垫付
		cash.setAdvanceFee(BigDecimal.ZERO);//默认0
		if(TppServiceEnum.CHINAPNR.getName().equals(tppName)){
			map.put("servFee", BigDecimalUtils.mul(ConfigUtils.getBigDecimal(Constant.CASH_SERV_FEE, Constant.INT_FOUR), model.getAmount()));
			UfxCashModel cashModel = (UfxCashModel)tppService.tppCash(map);
			//保存数据
			cash.setOrderNo(cashModel.getOrderNo());
			cash.setServFee(BigDecimalUtils.valueOf(cashModel.getServFee()));
			cash.setNeedAudit(ConfigUtils.getValue(Constant.CASH_NEED_AUDIT));
			cash.setIsAdvance(UfxCashModel.IS_ADVANCE_Y.equals(cashModel.getIsAdvance())?CommonEnum.YES.getValue():CommonEnum.NO.getValue());
			this.save(cash);
			return cashModel;
		}else if(TppServiceEnum.CBHB.getName().equals(tppName)){
			map.put("servFee", CbhbHelper.getBigDecimalMoneyUpper(BigDecimalUtils.mul(ConfigUtils.getBigDecimal(Constant.CASH_SERV_FEE, Constant.INT_FOUR), model.getAmount()).toString()));
			map.put("notifyUrl", "");
			CbhbDrawingsModel drawingsModel = (CbhbDrawingsModel)tppService.tppCash(map);
			
			//保存数据
			cash.setOrderNo(drawingsModel.getMerBillNo());
			cash.setServFee(CbhbHelper.getBigDecimalMoneyLow(drawingsModel.getMerFeeAmt()));
			cash.setNeedAudit(CommonEnum.NO.getValue());//渤海银行 无需审核
			cash.setIsAdvance(drawingsModel.getFeeType());
			this.save(cash);
			return drawingsModel;
		}else if(TppServiceEnum.JXBANK.getName().equals(tppName)){
		    BigDecimal txAmount = BigDecimalUtils.round(model.getAmount(), 2);
			
		    //BigDecimal txFee = BigDecimalUtils.mul(ConfigUtils.getBigDecimal(Constant.CASH_SERV_FEE, Constant.INT_FOUR), model.getAmount());
		    final Account account = accountService.getByUserId(new AccountQueryModel(user.getUuid(), ConfigUtils.getValue(ConfigConstant.ACCOUNT_CODE)));
		    BigDecimal currCashFeeMark = account.getCashFeeMark();//当前账户的提现资金手续费金额，用作后续是否修改保存判断
		    BigDecimal txFee = getCashFee(model.getAmount(), user.getUuid(), cash, account);
			//可用余额重新判断
			if (txFee.compareTo(BigDecimal.ZERO) > Constant.INT_ZERO) {
				if(account.getUseMoney().compareTo(BigDecimalUtils.add(txAmount, txFee))<0){
					txAmount = BigDecimalUtils.sub(account.getUseMoney(), txFee);//手续费不变，还是以前的
				}
			}

			map.put("txAmount", BigDecimalUtils.round(txAmount, 2));
			map.put("txFee", txFee);
			
			
			JxWithdrawModel jxWithdrawModel = (JxWithdrawModel)tppService.tppCash(map);
//			Map<String, Object> rstMap = jxWithdrawModel.getMapReqData();
			
			String orderNo = jxWithdrawModel.getOrderNo();

			cash.setRealAmount(txAmount);
			cash.setMoney(BigDecimalUtils.add(txAmount, txFee));
			cash.setServFee(txFee);
			//保存数据
			cash.setOrderNo(orderNo);
			cash.setNeedAudit(CommonEnum.NO.getValue());//渤海银行 无需审核
			this.save(cash);
			if(currCashFeeMark.compareTo(account.getCashFeeMark()) != 0){
				//更新用户提现手续费金额值
				accountService.updateOneByModel(account);
				//保存更改日志
				BigDecimal money = model.getAmount();//默认为提现的金额
				if(BigDecimal.ZERO.compareTo(account.getCashFeeMark()) == 0){
					money = currCashFeeMark;
				}
				cashFeeMarkLogService.preSave(cash.getUserId(), Constant.CASH_PROCCESS, money.negate(), cash.getUuid(), null);
				LOGGER.debug("本次提现扣除的手续费的金额部分为：{}", money);
			}
			if(CommonEnum.YES.getValue().equals(cash.getIsAdvance())){
				//平台垫付，缓存加+1
				cash.changeAdvanceCount(1);
			}
			return jxWithdrawModel;
		}else{
			return null;
		}
	}
	
	
	/**
	 * 查询提现状态处理业务
	 */
	@SuppressWarnings({ "unchecked"})
	@Override
	public void queryCashStatus() {
		String[] statusSet = {Constant.CASH_STATUS_APPLY, Constant.CASH_STATUS_PROCESS};
		List<Cash> cashList = dao.findCashList(statusSet);
		if (CollectionUtils.isNotEmpty(cashList)) {
			LOGGER.info("平台在提现交易发起的20分钟后，仍未收到银行的响应，则可以通过提现的单笔金融交易查询确认提现是否成功，" + "如果查询提现成功时（响应码为00000000但冲正/撤销标志不为1时），则扣减平台本地余额");
			TppService tppService = (TppService) TppUtil.tppService();
			String orderId = null;
			Map<String, Object> params = new HashMap<String, Object>();
			Map<String, Object> rst = null;
			String type = null;
			for (Cash cash : cashList) {
				LOGGER.info("用户：{}", cash.getUserId());
				User user = userService.get(cash.getUserId());
				orderId = cash.getOrderNo();
				params.put("accountId", user.getTppUserCustId());
				params.put("orgTxDate", orderId.substring(0, 8));
				params.put("orgTxTime", orderId.substring(8, 14));
				params.put("orgSeqNo", orderId.substring(14, 20));
				try {
					rst = (Map<String, Object>) tppService.fundTransQuery(params);
				} catch (Exception e) {
					LOGGER.error("接口查询错误;{}", e.getMessage());
				}
				if (rst != null) {
					String retCode = StringUtils.isNull(rst.get("retCode"));
					String retMsg = StringUtils.isNull(rst.get("retMsg"));
					String orFlag = StringUtils.isNull(rst.get("orFlag"));
					// 扣除金额
					String paymentsType = ""; // 收支类型
					BigDecimal money = cash.getMoney(); // 交易金额
					BigDecimal useMoney = BigDecimal.ZERO; // 新增可用余额(可为负数)
					BigDecimal noUseMoney = BigDecimal.ZERO; // 新增冻结金额(可为负数)
					if (Constant.CASH_STATUS_APPLY.equals(cash.getStatus())) {
						if (JxConfig.SUCCESS.equals(retCode) && JxConfig.CASH_OR_FLAG_NORMAL.equals(orFlag)) {
							LOGGER.info("提现资金与日志-------------成功:{}", cash.toString());
							type = Constant.CASH_SUCCESS;
							// 更改提现状态
							cash.setStatus(Constant.CASH_STATUS_SUCCESS);
							cash.setRemark(retMsg);
							cash.setHandleReason("主动查询提现交易状态，处理提现。");
							cash.setHandleTime(DateUtils.getNow());
							updateByTppResult(cash);
							LOGGER.info("提现更新状态:{}", cash.toString());

							// 资金和日志处理
							final Map<String, Object> param = new HashMap<String, Object>();
							// 支出
							paymentsType = AccountLog.PAYMENTS_TYPE_EXPEND;
							// 解冻金额
							useMoney = money.negate();
							noUseMoney = BigDecimal.ZERO;
							// 参数
							param.put("amount", cash.getAmount());
							param.put("realAmount", cash.getRealAmount());
							param.put("cashFee", BigDecimalUtils.add(cash.getServFee()));
							if (money.compareTo(BigDecimal.ZERO) > 0) {
								LOGGER.info("提现更新资金记录：{}", param);
								final String content = LogTemplateUtils.getAccountTemplate(type, param);
								final AccountLog accountLog = new AccountLog(ConfigUtils.getValue(Constant.ACCOUNT_CODE), type,
										cash.getUserId(), money, null, paymentsType, cash.getCashNo(), cash.getOrderNo(), content);
								AccountModel accountModel = new AccountModel(cash.getUserId(),
										ConfigUtils.getValue(Constant.ACCOUNT_CODE), useMoney, noUseMoney);
								accountService.saveAccountChange(accountModel, accountLog);
							}
						} else {
							LOGGER.info("提现失败，更新提现记录状态，不需要更新资金，{}", cash);
							// 更改提现状态
							cash.setStatus(Constant.CASH_STATUS_FAIL);
							cash.setRemark(new StringBuffer("错误代码：").append(retCode).append(",错误信息").append(retMsg)
									.toString());
							cash.setHandleReason("主动查询提现交易状态，处理提现。");
							cash.setHandleTime(DateUtils.getNow());
							updateByTppResult(cash);
							type = Constant.CASH_FAIL;
							/*----提现失败，返还提现需要手续费的金额和缓存中的平台垫付次数 start ----*/
				            repayCashFeeAmount(cash);
				            /*----提现失败，处理需要提现手续费金额的值和用户垫付次数 end ----*/
						}
					} else {
						if (JxConfig.SUCCESS.equals(retCode) && JxConfig.CASH_OR_FLAG_NORMAL.equals(orFlag)) {
							type = Constant.CASH_SUCCESS;
							// 更改提现状态
							cash.setStatus(Constant.CASH_STATUS_SUCCESS);
							cash.setRemark(retMsg);
							cash.setHandleReason("主动查询提现交易状态，处理提现。");
							cash.setHandleTime(DateUtils.getNow());
							updateByTppResult(cash);
							LOGGER.info("提现成功，更新状态:{}", cash.toString());
						} else {
							LOGGER.info("提现失败，更新状态和资金:{}", cash.toString());
							// 更改提现状态
							cash.setStatus(Constant.CASH_STATUS_FAIL);
							cash.setRemark(new StringBuffer("错误代码：").append(retCode).append(",错误信息").append(retMsg)
									.toString());
							cash.setHandleReason("主动查询提现交易状态，处理提现。");
							cash.setHandleTime(DateUtils.getNow());
							updateByTppResult(cash);
							type = Constant.CASH_FAIL;
							// 资金和日志处理
							final Map<String, Object> param = new HashMap<String, Object>();
							// 支出
							paymentsType = AccountLog.PAYMENTS_TYPE_EXPEND;
							// 解冻金额
							useMoney = money;
							noUseMoney = BigDecimal.ZERO;
							// 参数
							param.put("amount", cash.getAmount());
							param.put("realAmount", cash.getRealAmount());
							param.put("cashFee", BigDecimalUtils.add(cash.getServFee()));
							if (money.compareTo(BigDecimal.ZERO) > 0) {
								LOGGER.info("提现更新资金记录：{}", param);
								final String content = LogTemplateUtils.getAccountTemplate(type, param);
								final AccountLog accountLog = new AccountLog(ConfigUtils.getValue(Constant.ACCOUNT_CODE), type,
										cash.getUserId(), money, null, paymentsType, cash.getCashNo(), cash.getOrderNo(), content);
								AccountModel accountModel = new AccountModel(cash.getUserId(),
										ConfigUtils.getValue(Constant.ACCOUNT_CODE), useMoney, noUseMoney);
								accountService.saveAccountChange(accountModel, accountLog);
							}
							/*----提现失败，返还提现需要手续费的金额和缓存中的平台垫付次数 start ----*/
				            repayCashFeeAmount(cash);
				            /*----提现失败，处理需要提现手续费金额的值和用户垫付次数 end ----*/
						}
					}

					if (Constant.CASH_SUCCESS.equals(type)) {
						// 提现成功发送消息
						try {
							HashMap<String, Object> baseMap = new HashMap<String, Object>();
							baseMap.put("user", userService.get(cash.getUserId()));
							baseMap.put("amount", cash.getRealAmount().doubleValue());
							baseMap.put("fee", BigDecimalUtils.add(cash.getServFee()).doubleValue());
							baseMap.put("cashTime", DateUtils.getDateTime());
							BaseMsg baseMsg = new BaseMsg(MessageConstant.CASH_SUCC, baseMap);
							baseMsg.doEvent();
						} catch (Exception e) {
							LOGGER.error("提现成功通知处理失败,{}", e);
						}
					} else if (Constant.CASH_FAIL.equals(type)) {
						try {
							HashMap<String, Object> baseMap = new HashMap<String, Object>();
							baseMap.put("user", userService.get(cash.getUserId()));
							baseMap.put("amount", money.doubleValue());
							baseMap.put("cashTime", DateUtils.getDateTime());
							LOGGER.info("params:{}", params.toString());
							BaseMsg baseMsg = new BaseMsg(MessageConstant.CASH_FAIL, baseMap);
							baseMsg.doEvent();
						} catch (Exception e) {
							LOGGER.error("提现失败通知处理失败,{}", e);
						}
					}
				}
			}
		}
	}
	
	/**
	 * 渤海银行提现
	 */
	@Override
	public void doCbhbCashNotify(CbhbDrawingsModel model) {
		final boolean ret = model.validSign(model);
		LOGGER.debug("取现验签结果：" + ret);
		if (ret) {
			LOGGER.info("取现进入异步回调处理，订单号OrderNo={}，回调参数：{}",model.getMerBillNo(), model.toString());
			
			//重复处理判断(缓存标记) 投资回调处理 计数 失效时间： 1天
			final String handleKey = String.format(CacheKeys.PREFIX_CASH_TPP_HANDLE_NUM.getKey(),
					model.getMerBillNo(), model.getBizType(), model.getRespCode());
			if (CacheUtils.incr(handleKey, Constant.DOUBLE_ONE) > Constant.DOUBLE_ONE) {
				LOGGER.info("{}----提现订单已处理该状态",StringUtils.isNull(model.getMerBillNo()));
				return;
			}
			// 更新失效时间
			CacheUtils.expire(handleKey, ExpireTime.ONE_HOUR);
			
			//封装参数
			final Cash cashModel = dao.findByOrderNo(model.getMerBillNo());//根据提现订单号查询提现记录
			final User user = userService.get(cashModel.getUserId());
			LOGGER.info("用户user_id:{},{}",cashModel.getUserId(),user.getUuid());
			cashModel.setCashNo(model.getMerBillNo());
			cashModel.setServFee(CbhbHelper.getBigDecimalMoneyLow(model.getMerFeeAmt()));
			cashModel.setCashFee(CbhbHelper.getBigDecimalMoneyLow(model.getFeeAmt()));
			cashModel.setRealAmount(CbhbHelper.getBigDecimalMoneyLow(model.getTransAmt()));
			
			if(CbhbConstant.CBHB_CODE_SUCCESS.equals(model.getRespCode())){
				//成功回调  
				//更改提现状态 处理中
				cashModel.setStatus(Constant.CASH_STATUS_PROCESS);
				cashModel.setRemark("提现"+CbhbHelper.getBigDecimalMoneyLow(model.getTransAmt())+"元处理中");
				dao.updateByTppResult(cashModel);
				//提现处理中,冻结金额
				doCashLogByType(Constant.CASH_PROCCESS, cashModel);
			}else{
				//失败回调
				LOGGER.info("提现订单{}，银行处理失败，解冻金额：{}",cashModel.getOrderNo(),cashModel.getAmount() );
				
				//设置状态为取现失败
				cashModel.setStatus(Constant.CASH_STATUS_FAIL);
				cashModel.setRemark("提现"+CbhbHelper.getBigDecimalMoneyLow(model.getTransAmt())+"元交易失败");
				dao.updateByTppResult(cashModel);
				//提现申请失败
				doCashLogByType(Constant.CASH_SUBMIT_FAIL, cashModel);
			}
		} else {
			LOGGER.info("取现验签失败，订单号：{}" , model.getMerBillNo());
			throw new UfxException(ResourceUtils.get(ResourceConstant.TPP_NOTIFY_SIGN_FAIL));
		}
	}
	
	
	
	
	/**
	 * 根据订单号查询记录
	 */
	@Override
	public Cash findByOrderNo(final String orderNo){
		return dao.findByOrderNo(orderNo);
	}
	
	/**
	 * 根据取现订单号查询记录
	 */
	@Override
	public Cash findByCashNo(final String orderNo){
		return dao.findByCashNo(orderNo);
	}

	/**
	 * 后台查询提现记录(含分页)
	 */
	@Override
	public Page<Cash> findManagePage(final CashModel entity) {
		final Page<Cash> page = entity.getPage();
		final List<Cash> list = dao.findManageList(entity);
		page.setRows(list);
		return page;
	}
	
	/**
	 * 后台查询提现记录
	 */
	@Override
	public List<Cash> findManageList(final CashModel entity) {
		return dao.findManageList(entity);
	}

   /**
    * 提现信息
    */
	@Override
	public CashModel getCashModel(final String uuid) {
		return dao.getCashModel(uuid);
	}
	
	/**
	 * 根据条件查询提现记录
	 */
	@Override
	public Page<Cash> findByDate(final CashModel model) {
		final User user = SessionUtils.getSessionUser();
		model.setUserId(user.getUuid());
		Page<Cash> page = model.getPage();
		if(page==null){
			page = new Page<Cash>();
		}
		page.setRows(dao.findByDate(model));
		return page;
	}
	
	/**
	 * 提现审核-校验和解冻第三方资金
	 */
	@Transactional(readOnly = false)
	@Override
	public void checkAndunFreeze(final CashModel model){
		final Cash cash = dao.get(model.getUuid());
		//订单是否存在
		if(cash==null){
			throw new BussinessException(ResourceUtils.get(ResourceConstant.ORDER_NOT_EXIST), BussinessException.TYPE_JSON);
		}
		//防止并发操作
		model.checkCashAudit(cash.getCashNo());
		//审核状态
		if(!Constant.CASH_STATUS_AUDIT.equals(cash.getStatus())||cash.getVerifyTime()!=null){
			throw new BussinessException(ResourceUtils.get(ResourceConstant.CASH_HAS_AUDITED), BussinessException.TYPE_JSON);
		}
		
		// 取现审核前需先将取现冻结金额解冻
		final User user = userService.get(cash.getUserId());
		unFreezeTppCash(cash, user);
	}
	
	/**
	 * 提现审核
	 */
	@Transactional(readOnly = false)
	@Override
	public void doAudit(final CashModel model){
		final Cash dbCash = dao.get(model.getUuid());
		final User user = userService.get(dbCash.getUserId());
		
		//参数封装
		final Map<String,Object> map = Maps.newHashMap();
		map.put("userCustId", user.getTppUserCustId());
		map.put("auditFlag", Constant.FLAG_YES.equals(model.getVerifyStatus())? UfxCashAuditModel.AUDIT_FLAG_Y:UfxCashAuditModel.AUDIT_FLAG_N);
		map.put("cashNo", dbCash.getCashNo());
		map.put("amount", dbCash.getAmount());
		TppService tppService = (TppService)TppUtil.tppService();
		tppService.tppVerifyCash(map);
		
		//记录审核信息
		Cash cash = new Cash();
		cash.setUuid(dbCash.getUuid());
		cash.setVerifyTime(DateUtils.getNow());
		cash.setVerifyRemark(model.getVerifyRemark());
		cash.setVerifyUserName(model.getOperatorName());
		dao.updateAuditMsg(cash);
	}
	
	/**
	 * 提现状态
	 */
	@Transactional(readOnly = false)
	@Override
	public void updateByTppResult(final Cash cash){
		int result = dao.updateByTppResult(cash);
		if(result!=1){
			throw new BussinessException(ResourceUtils.get(ResourceConstant.CASH_UPDATE_FAIL));
		}
	}
	
	/**
	 * 解冻取现金额
	 * @param cash
	 */
	@Override
	public void unFreezeTppCash(final Cash cash, final User user) {
		final FreezeTradeLog freezeLog = freezeLogService.findByOrderNo(FreezeTradeLog.FREEZE_TYPE_CASH, cash.getOrderNo());
		if(!FreezeTradeLog.FREEZE_STATUS_UNFREEZE.equals(freezeLog.getStatus())){
			//调用冻结接口
			final UfxModel tppModel = new UfxModel();
			tppModel.setTrxId(freezeLog.getFreezeNo());
			tppModel.setUserId(user.getUserNo());
			tppModel.setTppUserCustId(user.getTppUserCustId());
			tppModel.setFreezeType(UfxUnFreezeModel.FREEZE_CASH);
			tppModel.setMoney(cash.getMoney());
			ufxService.unFreeze(tppModel);
			//更新订单状态
			freezeLog.setStatus(FreezeTradeLog.FREEZE_STATUS_UNFREEZE);
			freezeLogService.update(freezeLog);
		}
	}
	
	/**
	 * 获得单日已提现次数(不含提现失败)
	 */
	@Override
	public int getSingleDaytime(final String userId) {
		return dao.getSingleDaytime(getSingleCashModel(userId));
	}
	
	/**
	 * 获取当日已提现金额(不含提现失败)
	 */
	@Override
	public BigDecimal getSingleDayAmount(final String userId) {
		final BigDecimal singleDayAmount = dao.getSingleDayAmount(getSingleCashModel(userId));
		return singleDayAmount==null? BigDecimal.ZERO:singleDayAmount;
	}
	
	/**
	 * 获取当日剩余提现金额
	 */
	@Override
	public BigDecimal getSingleDayRemainCashAmount(final String userId) {
		BigDecimal remainCashAmount = BigDecimal.ZERO;
		final BigDecimal dayMaxAmount = ConfigUtils.getBigDecimal(Constant.CASH_DAY_MAX_AMNT);
		final BigDecimal singleDayAmount = getSingleDayAmount(userId);
		//每日最大提现金额>今日已提现金额
		if(dayMaxAmount.compareTo(singleDayAmount)>0){
			remainCashAmount = BigDecimalUtils.sub(dayMaxAmount,singleDayAmount);
		}
		return remainCashAmount;
	}
	
	/**
	 * 获取第三方可提现金额
	 */
	@Override
	public BigDecimal getTppCashAmount(final String userId) {
		BigDecimal tppCashAmount = BigDecimal.ZERO;
		User user = userService.get(userId);
		if(Constant.FLAG_YES.equals(user.getTppStatus())){
			final String tppName = ConfigUtils.getTppName();
			if(TppServiceEnum.CHINAPNR.getName().equals(tppName)){
				Map<String, Object> ufxMap = new HashMap<String, Object>();
				ufxMap.put("userCustId",user.getTppUserCustId());
				UserCache userCache = userCacheService.findByUserId(userId);
				if(UserCache.USER_NATURE_PERSON.equals(userCache.getUserNature())){
					ufxMap.put("userType", UfxQueryBalanceModel.USER_TYPE_PERSONAL);
					ufxMap.put("idNo", userCache.getIdNo());
				}else{
					ufxMap.put("userType", UfxQueryBalanceModel.USER_TYPE_COMPANY);
				}
				TppService tppService = (TppService)TppUtil.tppService();
				UfxQueryBalanceModel queryBalanceModel = (UfxQueryBalanceModel)tppService.tppQueryBalance(ufxMap);
				if(StringUtils.isNotBlank(queryBalanceModel.getWithdrawBal())){
					tppCashAmount = new BigDecimal(queryBalanceModel.getWithdrawBal());
				}
			}else if(TppServiceEnum.CBHB.getName().equals(tppName)){
				//第三方可提现余额  暂时无   默认可用余额
				final Map<String, Object> cbhbMap = new HashMap<>();
				cbhbMap.put("plaCustId",user.getTppUserCustId());
				TppService tppCbhbService = (TppService)TppUtil.tppService();
				@SuppressWarnings("unchecked")
				Map<String,Object> map = (HashMap<String,Object>)tppCbhbService.tppQueryBalance(cbhbMap);
				tppCashAmount = BigDecimalUtils.valueOf(StringUtils.isNull(map.get("acctBal")));
			}else if(TppServiceEnum.JXBANK.getName().equals(tppName)){
				//第三方可提现余额  暂时无   默认可用余额
				final Map<String, Object> jxMap = new HashMap<>();
				jxMap.put("accountId",user.getTppUserCustId());
				TppService tppCbhbService = (TppService)TppUtil.tppService();
				@SuppressWarnings("unchecked")
				Map<String,Object> map = (HashMap<String,Object>)tppCbhbService.tppQueryBalance(jxMap);
				tppCashAmount = BigDecimalUtils.valueOf(StringUtils.isNull(map.get("acctBal")));
			}else{
				
			}
		}
		
		return tppCashAmount;
	}
	
	/**
	 * 
	 * 提现记录信息（添加状态列表）
	 * @return
	 */
	private CashModel getSingleCashModel(final String userId){
		final CashModel model = new CashModel();
		model.setUserId(userId);
		
		final List<String> statusList = new ArrayList<String>();
		statusList.add(Constant.CASH_STATUS_APPLY);
		statusList.add(Constant.CASH_STATUS_AUDIT);
		statusList.add(Constant.CASH_STATUS_PROCESS);
		statusList.add(Constant.CASH_STATUS_SUCCESS);
		model.setStatusList(statusList);
		
		return model;
	}
	
	/**
	 * 更新超时提现订单，以30分钟为准
	 */
	@Override
	//@Transactional(readOnly = false)
	public void updateOverTimeOrderStatus() {
		final Date nowDate = DateUtils.getNow();
		long now = nowDate.getTime();
		int dayTime = 1800 * 1000;
		try {
			final List<Cash> list = dao.findByStatus(Constant.CASH_STATUS_APPLY);
			for (final Cash cash : list) {
				if ((now - cash.getAddTime().getTime()) >= dayTime) {
					updateItemOverTimeOrderStatus(cash);
				}
			}
		} catch (Exception e) {
			LOGGER.error(e.getLocalizedMessage(), e);
		}
	}
	
	private void updateItemOverTimeOrderStatus(final Cash cash) {
		// 手动控制提交事务，避免关闭队列，同步发送报文太久，TPP未提交，异步回调先到，查询不到TPP
		TransactionDefinition definition = new DefaultTransactionDefinition();
		TransactionStatus ts = transactionManager.getTransaction(definition);
		try{
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("loanNo", cash.getOrderNo());
			map.put("loanDate",DateUtils.dateStr7(cash.getAddTime()));
			map.put("isRepeatSend", UfxAdditionalOrderModel.REPEAT_SEND_YES);
			TppService tppService = (TppService)TppUtil.tppService();
			final UfxAdditionalOrderModel model = (UfxAdditionalOrderModel)tppService.tppAdditionOrder(map);
			if(model!=null){
				if(!UfxConstant.UFX_CODE_DOING.equals(model.getRespCode()) && 
						!UfxConstant.UFX_CODE_VERIFY.equals(model.getRespCode()) && 
						!UfxConstant.UFX_CODE_SUCCESS.equals(model.getRespCode())){
					//更改提现记录
					cash.setStatus(Constant.CASH_STATUS_FAIL);
					cash.setRemark(ResourceUtils.get(ResourceConstant.ORDER_IS_TIMEOUT));
					dao.updateByTppResult(cash);
				}
			}			
			transactionManager.commit(ts);
		} catch (Exception e) {
			transactionManager.rollback(ts);
			LOGGER.error(e.getMessage(),e);
			throw e;
		}
	
	}
	
	/**
	 * 
	 * 提现资金和日志统一处理
	 * @author xhf
	 * @date 2016年9月23日
	 * @param type
	 * @param cash
	 */
	@Override
	@Transactional(readOnly = false)
	public void doCashLogByType(String type, final Cash cash){
		//check
		if(StringUtils.isBlank(type)||null==cash){
			throw new BussinessException(ResourceUtils.get(ResourceConstant.GLOBAL_MSG_FAIL));
		}
		
		//资金和日志处理
		final Map<String, Object> param = new HashMap<String, Object>();
		String paymentsType = "";                //收支类型
		BigDecimal money = cash.getMoney();      //交易金额
		BigDecimal useMoney = BigDecimal.ZERO;   //新增可用余额(可为负数)
		BigDecimal noUseMoney = BigDecimal.ZERO; //新增冻结金额(可为负数)
		
		//提现待审核或处理中，冻结提现金额
		if(Constant.CASH_PROCCESS.equals(type)||Constant.CASH_AUDIT.equals(type)){ 
			//收支不变
			paymentsType = AccountLog.PAYMENTS_TYPE_REMAIN;
			//冻结金额
			useMoney = money.negate();
			noUseMoney = money;
			//参数：金额
			param.put("money", money);
		
		}else if(Constant.CASH_FAIL.equals(type)){ //提现审核失败，退还冻结金额
			//收支不变
			paymentsType = AccountLog.PAYMENTS_TYPE_REMAIN;
			//解冻金额
			useMoney = money;
			noUseMoney = money.negate();
			//参数：金额
			param.put("money", money);			
			
		}else if(Constant.CASH_BANK_FAIL.equals(type)){ //异步回调成功，银行对账失败，返回扣除金额
			//收支不变
			paymentsType = AccountLog.PAYMENTS_TYPE_INCOME;
			//退还金额
			useMoney = money;
			noUseMoney = BigDecimal.ZERO;
			//参数
			param.put("money", money);

		}else if(Constant.CASH_SUCCESS.equals(type)){ //提现成功，扣除可用金额
			//支出
			paymentsType = AccountLog.PAYMENTS_TYPE_EXPEND;
			//解冻金额
			useMoney = money.negate();
			noUseMoney = BigDecimal.ZERO;
			//参数
			param.put("amount", cash.getAmount());
			param.put("realAmount", cash.getRealAmount());
			param.put("cashFee", BigDecimalUtils.add(cash.getCashFee(),cash.getServFee()));
			
		}else if(Constant.CASH_AUDIT_SUCCESS.equals(type)){ //提现审核成功，扣除冻结金额
			//为了前台查询，这里用同一个状态
			type = Constant.CASH_SUCCESS;
			//支出
			paymentsType = AccountLog.PAYMENTS_TYPE_EXPEND;
			//解冻金额
			useMoney = BigDecimal.ZERO;
			noUseMoney = money.negate();
			//参数
			param.put("amount", cash.getAmount());
			param.put("realAmount", cash.getRealAmount());
			param.put("cashFee", BigDecimalUtils.add(cash.getCashFee(),cash.getServFee()));

		}else if(Constant.CASH_SUBMIT_FAIL.equals(type)){//提现申请失败   资金不变 记录变为失败
			//收支不变
			paymentsType = AccountLog.PAYMENTS_TYPE_REMAIN;
			//冻结金额
			noUseMoney = BigDecimal.ZERO;
			//参数：金额
			param.put("money", money);
		}
		
		if(money.compareTo(BigDecimal.ZERO)>0){
			final String content = LogTemplateUtils.getAccountTemplate(type, param);
			final AccountLog accountLog = new AccountLog(ConfigUtils.getValue(Constant.ACCOUNT_CODE), type, cash.getUserId(), money,
					null, paymentsType, cash.getCashNo(), cash.getOrderNo(), content);
			AccountModel accountModel = new AccountModel(cash.getUserId(), ConfigUtils.getValue(Constant.ACCOUNT_CODE), useMoney, noUseMoney);
			accountService.saveAccountChange(accountModel, accountLog);			
		}
		
		//提现成功，新增商户服务费记录日志
		if(Constant.CASH_SUCCESS.equals(type)){
			if(BigDecimalUtils.validAmount(cash.getServFee())){
			    tppMerchantLogService.saveMerchantLog(TppMerchantLogModel.TYPE_CUSTOMER_CASH_SERVER, cash.getUserId(),cash.getServFee(), cash.getOrderNo());
			}
			// 渤海平台垫付提现手续费
			if(CbhbConstant.CBHB_TPP_NAME.equals(ConfigUtils.getTppName()) &&
					BigDecimalUtils.validAmount(cash.getCashFee())){
			    tppMerchantLogService.saveMerchantLog(TppMerchantLogModel.TYPE_CUSTOMER_CASH, cash.getUserId(),cash.getCashFee(), cash.getOrderNo());
			}
		}
		
		if(Constant.CASH_SUCCESS.equals(type)){
			// 提现成功发送消息
			try {
				HashMap<String, Object> params = new HashMap<String, Object>();
				params.put("user", userService.get(cash.getUserId()));
				params.put("amount", cash.getRealAmount().doubleValue());
				params.put("fee",  BigDecimalUtils.add(cash.getCashFee(),cash.getServFee()).doubleValue());
				params.put("cashTime", DateUtils.getDateTime());
				BaseMsg baseMsg = new BaseMsg(MessageConstant.CASH_SUCC, params);
				baseMsg.doEvent();
			} catch (Exception e) {
				LOGGER.error("提现成功通知处理失败", e);
			}
		}else if(Constant.CASH_FAIL.equals(type) || Constant.CASH_BANK_FAIL.equals(type)){
			try {
				HashMap<String, Object> params = new HashMap<String, Object>();
				params.put("user", userService.get(cash.getUserId()));
				params.put("amount", money.doubleValue());
				params.put("cashTime", DateUtils.getDateTime());
				LOGGER.info("params:{}",params.toString());
				BaseMsg baseMsg = new BaseMsg(MessageConstant.CASH_FAIL, params);
				baseMsg.doEvent();
			} catch (Exception e) {
				LOGGER.error("提现失败通知处理失败", e);
			}
		}
	}
	
	/**
	 * 提现人工核查
	 */
	@Transactional(readOnly = false)
	@Override
	public void doCheck(final CashModel model){
		final Cash dbCash = dao.get(model.getUuid());
		if(dbCash==null){
			throw new BussinessException(ResourceUtils.get(ResourceConstant.ORDER_NOT_EXIST));
		}
		if(Constant.MAN_HANDLE_CHECKED.equals(model.getManHandle())){
			final Cash cash = new Cash();
			cash.setUuid(model.getUuid());
			cash.setManHandle(model.getManHandle());
			cash.setHandleTime(DateUtils.getNow());
			cash.setHandleUser(model.getOperatorName());
			dao.update(cash);
		}
	}
	
	/**
	 * 提现订单需要人工处理
	 * @param dbCash
	 * @param handleReason
	 */
	@Override
	public void cashNeedManHandle(Cash dbCash,String handleReason){
		final Cash cash = new Cash();
		cash.setUuid(dbCash.getUuid());
		cash.setStatus(Constant.CASH_STATUS_PROCESS);
		cash.setManHandle(Constant.FLAG_YES);
		cash.setHandleReason(handleReason);
		this.update(cash);
		
		//发送预警短信
		Map<String, Object> params = Maps.newHashMap();
		params.put("orderType", ResourceUtils.get(ResourceConstant.ORDER_TYPE_CASH));
		params.put("orderNo", dbCash.getOrderNo());
		WarnMsg message = new WarnMsg(MessageConstant.ACCOUNT_WARN, params);
		message.doEvent();
	}
	
	@Override
	public int getManageCount(CashModel model) {
		return dao.getManageCount(model);
	}
	
	/**
	 * 统计充值人数/金额-二维图
	 * @return
	 * @throws ParseException 
	 */
	@Override
	public Map<String, Object> findByStatDate(StatisticModel model){
		final Map<String, Object> map = Maps.newHashMap();
		//校验时间
		model.checkQueryDate();
		//订单状态为成功
		model.setStatus(Constant.CASH_STATUS_SUCCESS);
		//总计
		List<String> totalNumList = statisticService.statByDate(dao.findByStatType(model), model); 
		map.put(StatisticConstant.STAT_CHART_TOTAL, totalNumList);
		//环比值
		BigDecimal beforeCount = dao.getCountBeforeStartDate(model);
		map.put(StatisticConstant.STAT_CHART_RATIO, statisticService.statisticRatio(totalNumList, beforeCount));
		return map;
	}
	
	/**
	 * 统计充值人数/金额-饼状图
	 * @return
	 * @throws ParseException 
	 */
	@Override
	public Map<String, Object> findByStatType(StatisticModel model){
		final Map<String, Object> map = Maps.newHashMap();
		//校验时间
		model.checkQueryDate();
		//订单状态为成功
		model.setStatus(Constant.CASH_STATUS_SUCCESS);
		//提现来源
		model.fillDictList(DictTypeEnum.SALE_CHANNEL.getValue());
		if(StatisticConstant.STAT_TYPE_CUSTOMERS.equals(model.getStatType())){
			map.put(StatisticConstant.STAT_PIE_CHART, dao.getCountByUsers(model));
		}else{
			map.put(StatisticConstant.STAT_PIE_CHART, dao.getCountByAmnt(model));
		}
		
		return map;
	}


	@Override
	public BigDecimal getCashFee(final BigDecimal cashAmount, final String userId, final Cash cash, final Account account) {
		//1、获取提现需要交手续费的部分金额
		BigDecimal needCashFeeAmount = cashAmount;//需要计算手续费部分的提现金额
		if(BigDecimal.ZERO.compareTo(account.getCashFeeMark()) < 0){
			//账号中需要手续费提现金额大于等于0
			if(cashAmount.compareTo(account.getCashFeeMark()) >= 0){
				//当提现的金额大于等于   账号中需要手续费提现的金额
				needCashFeeAmount = account.getCashFeeMark();//需要计算手续费部分的提现金额直接等于账号中剩余需要手续的提现金额
				account.setCashFeeMark(BigDecimal.ZERO);
			}else{
				account.setCashFeeMark(BigDecimalUtils.sub(account.getCashFeeMark(), cashAmount));
			}
		}else{
			//不需要手续费
			return BigDecimal.ZERO;
		}
		//2、计算手续费
		BigDecimal cashFee = BigDecimalUtils.mul(ConfigUtils.getBigDecimal(Constant.CASH_SERV_FEE, Constant.INT_FOUR), needCashFeeAmount);
		//3、是否需要垫付手续费
		if (cashFee.compareTo(BigDecimal.ZERO) > 0) {
			//平台每月可垫付次数
			int platformAdvanceMonthCount = ConfigUtils.getInt(ConfigConstant.CASH_FEE_PLATFORM_ADVANCE_MONTH_COUNT);
			//当前用户本月已垫付次数
			int advanceCount = CacheUtils.getInt(String.format(Cash.KEY_ADVANCE_CASH_FEE_MONTH_COUNT, DateUtils.getMonth(), userId));
			if(platformAdvanceMonthCount > 0 && platformAdvanceMonthCount > advanceCount){
				cash.setIsAdvance(CommonEnum.YES.getValue());//设置为平台垫付
				cash.setAdvanceFee(cashFee);//垫付的手续费
				cashFee = BigDecimal.ZERO;//支付的手续费设置为0
			}
		}
		return cashFee;
	}


	@Override
	public void repayCashFeeAmount(Cash dbCash) {
		//查询之前扣除时的记录
		CashFeeMarkLog cashFeeMarkLog = cashFeeMarkLogService.findOne(
        		new CashFeeMarkLog(dbCash.getUserId(), Constant.CASH_PROCCESS, dbCash.getUuid()));
        if(cashFeeMarkLog != null && cashFeeMarkLog.getMoney() != null){
    		final Account account = accountService.getByUserId(new AccountQueryModel(dbCash.getUserId(), ConfigUtils.getValue(ConfigConstant.ACCOUNT_CODE)));
    		if(account != null){
    			//归还之前扣除的提现手续费金额
    			BigDecimal repayMoney = cashFeeMarkLog.getMoney().negate();
    			account.setCashFeeMark(account.getCashFeeMark()==null?repayMoney:BigDecimalUtils.add(account.getCashFeeMark(), repayMoney));
    			accountService.updateOneByModel(account);
    			//提现手续费金额变动记录
    			cashFeeMarkLogService.preSave(dbCash.getUserId(), Constant.CASH_FAIL, repayMoney, dbCash.getUuid(), null);
    		}
    	}
        //是否平台垫付，如垫付需要返还缓存中的提现手续费垫付次数
        if(CommonEnum.YES.getValue().equals(dbCash.getIsAdvance())){
        	//平台垫付提现时后续费，释放之前添加的次数 -1
        	dbCash.changeAdvanceCount(-1);
        }
		
	}
}