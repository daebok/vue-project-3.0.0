package com.rd.ifaes.core.account.service.impl;

import java.math.BigDecimal;
import java.text.ParseException;
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
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.google.common.collect.Maps;
import com.rd.account.domain.AccountLog;
import com.rd.account.model.AccountModel;
import com.rd.account.service.AccountService;
import com.rd.ifaes.common.constant.CbhbConstant;
import com.rd.ifaes.common.constant.JxbankConstant;
import com.rd.ifaes.common.constant.TppServiceEnum;
import com.rd.ifaes.common.constant.UfxConstant;
import com.rd.ifaes.common.dict.CacheKeys;
import com.rd.ifaes.common.dict.CommonEnum;
import com.rd.ifaes.common.dict.DictTypeEnum;
import com.rd.ifaes.common.dict.ExpireTime;
import com.rd.ifaes.common.dict.InvestEnum;
import com.rd.ifaes.common.exception.BussinessException;
import com.rd.ifaes.common.orm.Page;
import com.rd.ifaes.common.util.BigDecimalUtils;
import com.rd.ifaes.common.util.DateUtils;
import com.rd.ifaes.common.util.EncodeUtils;
import com.rd.ifaes.common.util.NumberUtils;
import com.rd.ifaes.common.util.SessionUtils;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.common.util.TppUtil;
import com.rd.ifaes.core.account.domain.Recharge;
import com.rd.ifaes.core.account.mapper.RechargeMapper;
import com.rd.ifaes.core.account.model.RechargeModel;
import com.rd.ifaes.core.account.service.CashFeeMarkLogService;
import com.rd.ifaes.core.account.service.RechargeService;
import com.rd.ifaes.core.base.service.BaseServiceImpl;
import com.rd.ifaes.core.core.constant.Constant;
import com.rd.ifaes.core.core.constant.MessageConstant;
import com.rd.ifaes.core.core.constant.ResourceConstant;
import com.rd.ifaes.core.core.sms.msg.BaseMsg;
import com.rd.ifaes.core.core.util.CacheUtils;
import com.rd.ifaes.core.core.util.ConfigUtils;
import com.rd.ifaes.core.core.util.ResourceUtils;
import com.rd.ifaes.core.statistic.constant.StatisticConstant;
import com.rd.ifaes.core.statistic.model.StatisticModel;
import com.rd.ifaes.core.statistic.service.StatisticService;
import com.rd.ifaes.core.sys.domain.LogTemplate;
import com.rd.ifaes.core.sys.service.LogTemplateService;
import com.rd.ifaes.core.tpp.exception.CbhbException;
import com.rd.ifaes.core.tpp.exception.UfxException;
import com.rd.ifaes.core.tpp.model.cbhb.CbhbWebRechargeModel;
import com.rd.ifaes.core.tpp.model.ufx.TppRechargeModel;
import com.rd.ifaes.core.tpp.model.ufx.UfxAdditionalOrderModel;
import com.rd.ifaes.core.tpp.model.ufx.UfxRechargeModel;
import com.rd.ifaes.core.tpp.service.TppService;
import com.rd.ifaes.core.tpp.service.tpp.UfxRechargeService;
import com.rd.ifaes.core.tpp.util.JxConfig;
import com.rd.ifaes.core.user.domain.User;
import com.rd.ifaes.core.user.domain.UserCache;
import com.rd.ifaes.core.user.service.UserCacheService;
import com.rd.ifaes.core.user.service.UserService;

/**
 * 充值业务处理类
 * @author xhf
 * @version 3.0
 * @date 2016-6-30
 */
@Service("rechargeService") 
public class RechargeServiceImpl  extends BaseServiceImpl<RechargeMapper, Recharge> implements RechargeService{
	
	/**
	 * 日志跟踪器
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(RechargeServiceImpl.class);

	/**
	 * 用户附属信息类
	 */
	@Resource
	private transient UserCacheService userCacheService;
	/**
	 * 用户信息类
	 */
	@Resource
	private transient UserService userService;
	@Resource
	private UfxRechargeService ufxRechargeService;
	@Resource
    private PlatformTransactionManager transactionManager;
	@Resource
	private StatisticService statisticService;
	/**
	 * 账户资金处理类
	 */
	@Resource
	private transient AccountService accountService;
	/**
	 * 日志模板处理类
	 */
	@Resource
	private transient LogTemplateService logTemplateService;
	@Resource
	private CashFeeMarkLogService cashFeeMarkLogService;
	
	/**
	 * 充值
	 */
	@Override
	public Object doRecharge(final RechargeModel model){
		//校验用户是否登录
		if(model.getUser()==null){
			throw new BussinessException(ResourceUtils.get(ResourceConstant.USER_NOT_LOGIN), BussinessException.TYPE_CLOSE);
		}
		User user = userService.get(model.getUser().getUuid());
		//校验用户充值功能是否冻结
		model.checkFreeze(user.getUuid());
	    //认证信息校验
		model.checkIdentify(user.getUuid());
		//金额校验
        model.checkRechargeMoney();
		
		//封装参数
        final UserCache userCache = userCacheService.findByUserId(user.getUuid());
        final Map<String, Object> map = Maps.newHashMap();
//		map.put("userCustId", user.getTppUserCustId());
//		map.put("amount", BigDecimalUtils.round(model.getAmount()));
//		map.put("idNo", userCache.getIdNo());
//		map.put("userId", user.getUserNo());
//		map.put("realName", user.getRealName());
//		map.put("feeType", CommonEnum.NO.getValue());//写死垫付

        String mobile = model.getMobile() != null ? model.getMobile() : user.getMobile();
		map.put("accountId", user.getTppUserCustId());
		map.put("idType", JxbankConstant.JXBANK_ID_TYPE_ONE);
		map.put("idNo", userCache.getIdNo());
		map.put("name", user.getRealName());
		map.put("mobile", mobile);
		map.put("cardNo", model.getCardId());
		map.put("txAmount", BigDecimalUtils.round(model.getAmount()));
		map.put("currency", JxbankConstant.JXBANK_CURRENCY_RMB);
		map.put("smsCode", model.getSmsCode());
		map.put("smsSeq", SessionUtils.getSessionAttr("smsSeq:" + mobile));
		
		// 保存充值记录
		final Recharge recharge = new Recharge();
		String tppName = ConfigUtils.getTppName();
		TppService tppService  = (TppService)TppUtil.tppService();
		Object object = tppService.tppRecharge(map);
    	recharge.setStatus(Constant.RECHARGE_STATUS_SUCCESS);
    	recharge.setCreateTime(DateUtils.getNow());
    	recharge.setChannel(model.getChannel());
		if(TppServiceEnum.CHINAPNR.getName().equals(tppName)){
			UfxRechargeModel newRecharge = (UfxRechargeModel)object;
			recharge.setFeeType(Constant.FEE_TYPE_MERCHANT);
			recharge.setOrderNo(StringUtils.isNull(newRecharge.getOrderNo()));
		}else if(TppServiceEnum.CBHB.getName().equals(tppName)){
			CbhbWebRechargeModel reModel = (CbhbWebRechargeModel)object;
			recharge.setFeeType(CommonEnum.NO.eq(reModel.getFeeType())?Constant.FEE_TYPE_MERCHANT:Constant.FEE_TYPE_USER);
			recharge.setOrderNo(StringUtils.isNull(reModel.getMerBillNo()));
		}else if(TppServiceEnum.JXBANK.getName().equals(tppName)){
			@SuppressWarnings("unchecked")
			Map<String, Object> rstMap = (Map<String, Object>) object;
			recharge.setFeeType(Constant.FEE_TYPE_MERCHANT);
			recharge.setOrderNo(StringUtils.isNull((String) rstMap.get("txDate") + (String) rstMap.get("txTime")
					+ (String) rstMap.get("seqNo")));

			String retCode = (String) rstMap.get("retCode");
			String retMsg = (String) rstMap.get("retMsg");
			if (!JxConfig.SUCCESS.equals(retCode)){
		    	recharge.setStatus(Constant.RECHARGE_STATUS_FAIL);
		    	if(StringUtils.isBlank(retMsg)){
		    		retMsg = "充值失败！";
		    	}
		    	recharge.setRemark(retMsg);
			} else {
				SessionUtils.removeAttribute("smsSeq:" + mobile);
				//日志
				LOGGER.info("用户userId={}充值成功，增加可用金额：{}",user.getUuid(),model.getAmount());			
				
				//添加资金记录
				final Map<String, Object> param = new HashMap<String, Object>();
				param.put("money", model.getAmount());
				final String content = logTemplateService.getTemplateContent(LogTemplate.LOG_TYPE_ACCOUNT, Constant.RECHARGE_SUCCESS_LOG, param);
				
				final AccountLog accountLog = new AccountLog(ConfigUtils.getValue(Constant.ACCOUNT_CODE), Constant.RECHARGE_SUCCESS_LOG, user.getUuid(), model.getAmount(),
						null, AccountLog.PAYMENTS_TYPE_INCOME, recharge.getTradeNo(), recharge.getOrderNo(), content);
				AccountModel accountModel = new AccountModel(user.getUuid(), ConfigUtils.getValue(Constant.ACCOUNT_CODE), model.getAmount(), BigDecimal.ZERO, model.getAmount());
				accountService.saveAccountChange(accountModel, accountLog);
				/*---提现手续费标记资金有修改，保存修改记录 start--*/
				cashFeeMarkLogService.preSave(accountModel.getUserId(), accountLog.getAccountType(), 
						accountModel.getCashFeeMark() == null? BigDecimal.ZERO:accountModel.getCashFeeMark(), null, null);
				/*---提现手续费标记资金有修改，保存修改记录 end--*/
				
				// 发送充值成功信息
				try {
					HashMap<String, Object> params = new HashMap<String, Object>();
					params.put("user", user);
					params.put("amount", model.getAmount().doubleValue());
					params.put("rechargeTime", DateUtils.dateStr4(recharge.getCreateTime()));
					BaseMsg baseMsg = new BaseMsg(MessageConstant.RECHARGE_SUCC, params);
					baseMsg.doEvent();
				} catch (Exception e) {
					LOGGER.error("充值成功通知处理失败", e);
				}
			}
		}else{
			throw new BussinessException("后台第三方支付配置出错!",BussinessException.TYPE_JSON);
		}
    	recharge.setUserId(user.getUuid());
    	recharge.setAmount(model.getAmount());
    	recharge.setAddIp(model.getAddIp());
    	recharge.setCreateTime(DateUtils.getNow());
    	recharge.setChannel(model.getChannel());
		save(recharge);
		return recharge;
	}
	
	/**
	 * 根据交易流水号获取充值记录
	 */
	@Override
	public Recharge getRechargeByOrderNo(final String orderNo){
		Recharge recharge = null;
		if(StringUtils.isNotBlank(orderNo)){
			final List<Recharge> rechargeList = dao.findList(new Recharge(null,orderNo,null));
			if(!rechargeList.isEmpty()){
				recharge = rechargeList.get(0);
			}
		}

		return recharge;
	}

	/**
	 * 后台查询充值订单记录
	 */
	@Override
	public Page<Recharge> findManagePage(final RechargeModel model) {
		final Page<Recharge> page = model.getPage();
		final List<Recharge> list = dao.findManageList(model);
		page.setRows(list);
		return page;
	}
	
	/**
	 * 根据日志查询充值记录
	 */
	@Override
	public Page<Recharge> findByDate(final RechargeModel model) {
		final User user = SessionUtils.getSessionUser();
		model.setUserId(user.getUuid());
		
		final Page<Recharge> page = model.getPage();
		page.setRows(dao.findByDate(model));
		return page;
	}

	/**
	 * 查询后台充值记录(不含分页)
	 */
	@Override
	public List<Recharge> findManageList(final RechargeModel model) {
		return dao.findManageList(model);
	}
	
	/**
	 * 更新状态
	 */
	@Override
	public void updateByTppResult(final Recharge recharge){
		if(dao.updateByTppResult(recharge)!=Constant.FLAG_INT_YES){
			throw new BussinessException(ResourceUtils.get(ResourceConstant.RECHARGE_UPDATE_FAIL));
		}
	}
	
	/**
	 * 更新超时充值订单状态
	 */
	@Override
	//@Transactional(readOnly = false)
	public void updateOverTimeOrderStatus(){
		final Date nowDate = DateUtils.getNow();
		final long now = nowDate.getTime();
		final int dayTime = 1 * 24 * 3600 * 1000;
		
		try {
			final List<Recharge> list = dao.findByStatus(Constant.RECHARGE_STATUS_APPLY);
			for (final Recharge c : list) {
				if ((now - c.getCreateTime().getTime()) >= dayTime) {
					updateItemOverTimeOrderStatus(c);
				}
			}
		} catch (Exception e) {
			LOGGER.error(e.getLocalizedMessage(), e);
		}
	}
	
	private void updateItemOverTimeOrderStatus(final Recharge c ){
		// 手动控制提交事务，避免关闭队列，同步发送报文太久，TPP未提交，异步回调先到，查询不到TPP
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("loanNo", c.getOrderNo());
		map.put("loanDate",DateUtils.dateStr7(c.getCreateTime()));
		map.put("isRepeatSend", UfxAdditionalOrderModel.REPEAT_SEND_YES);
		TppService tppService = (TppService)TppUtil.tppService();
		final UfxAdditionalOrderModel model = (UfxAdditionalOrderModel)tppService.tppAdditionOrder(map);
		
		if(model!=null){
    		LOGGER.info("订单{}超时，第三方状态：{}",c.getOrderNo(),model.getRespCode());
    		if(!UfxConstant.UFX_CODE_DOING.equals(model.getRespCode()) && 
    			!UfxConstant.UFX_CODE_VERIFY.equals(model.getRespCode()) && 
    			!UfxConstant.UFX_CODE_SUCCESS.equals(model.getRespCode())){
    			
    			TransactionDefinition definition = new DefaultTransactionDefinition();
    			TransactionStatus ts = transactionManager.getTransaction(definition);
    			try{
	    			//更新记录状态
					final Recharge recharge = new Recharge();
					recharge.setUuid(c.getUuid());
					recharge.setStatus(Constant.RECHARGE_STATUS_FAIL);
					recharge.setRemark(ResourceUtils.get(ResourceConstant.ORDER_IS_TIMEOUT));
					dao.updateOverTimeOrderStatus(recharge);
					transactionManager.commit(ts);
    			} catch (Exception e) {
    				transactionManager.rollback(ts);
    				LOGGER.error(e.getMessage(),e);
    				throw e;
    			}
    		}
		}		
	}
	
	
	/**
	 * 充值回调业务处理
	 * @param flag
	 */
	@Override
	public void doTppRecharge(final UfxRechargeModel model){
		final boolean isValidSign = model.validSign(model);
		if (isValidSign) {
			// 重复处理判断(缓存标记)
			String handleKey = String.format(CacheKeys.PREFIX_RECHARGE_ORDER_NO_HANDLE_NUM.getKey(), model.getOrderNo());
			if (CacheUtils.incr(handleKey, Constant.DOUBLE_ONE) > Constant.DOUBLE_ONE) {
				LOGGER.info("订单号：{} ----充值订单回调, 已处理", model.getOrderNo());
				return;
			}
			CacheUtils.expire(handleKey, ExpireTime.ONE_HOUR);
			
			LOGGER.info("充值回调进入本地处理，订单号：{}，返回状态：{}", model.getOrderNo(), EncodeUtils.urlDecode(model.getRespCode()));
			if (UfxConstant.UFX_CODE_SUCCESS.equals(model.getRespCode())) {
				final TppRechargeModel rechargeModel = new TppRechargeModel();
				rechargeModel.setAdvanceFee(true);
				rechargeModel.setRechargeFee(NumberUtils.getBigDecimal(model.getRechargeFee()));
				rechargeModel.setRealAmount(NumberUtils.getBigDecimal(model.getAmount()));
				rechargeModel.setRechargeMoney(NumberUtils.getBigDecimal(model.getAmount()));
				rechargeModel.setUserCustId(model.getUserCustId());
				rechargeModel.setOrdNo(model.getOrderNo());
				rechargeModel.setTradeNo(model.getTradeNo());
				rechargeModel.setPayway(model.getPayWay());
				ufxRechargeService.tppRechargeSucc(rechargeModel);
				
			}else{
				//失败，修改订单状态
				final TppRechargeModel rechargeModel = new TppRechargeModel();
				rechargeModel.setOrdNo(model.getOrderNo());
				rechargeModel.setRespDesc(model.getRespDesc());
				ufxRechargeService.tppRechargeFail(rechargeModel);
			}
		}else{
			LOGGER.error("充值回调验签失败，订单号：{}", model.getOrderNo());
			throw new UfxException(ResourceUtils.get(ResourceConstant.TPP_NOTIFY_SIGN_FAIL));
		}
	}
	
	
	@Override
	public int getManageCount(RechargeModel model) {
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
		model.setStatus(Constant.RECHARGE_STATUS_SUCCESS);
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
		model.setStatus(Constant.RECHARGE_STATUS_SUCCESS);
        //充值来源
		model.fillDictList(DictTypeEnum.SALE_CHANNEL.getValue());
		if(StatisticConstant.STAT_TYPE_CUSTOMERS.equals(model.getStatType())){
			map.put(StatisticConstant.STAT_PIE_CHART, dao.getCountByUsers(model));
		}else{
			map.put(StatisticConstant.STAT_PIE_CHART, dao.getCountByAmnt(model));
		}
		return map;
	}
	
	@Override
	public void queryCbhbRechargeStatus() {
		String tppName = ConfigUtils.getTppName();
		if(TppServiceEnum.CBHB.getName().equals(tppName)){//渤海银行支付 才需要处理
			//查询充值申请中的充值记录
			List<Recharge> list = dao.findByStatus(Constant.RECHARGE_STATUS_APPLY);
			//调用交易状态接口查询充值的实际状态值
			if(CollectionUtils.isNotEmpty(list)){
				TppService tppService = (TppService)TppUtil.tppService();
				for(Recharge recharge :list){
					LOGGER.info("用户：{}",recharge.getUserId());
					Map<String,Object> params = new HashMap<String, Object>();
					params.put("merBillNo", recharge.getOrderNo());
					params.put("queryTransType", CbhbConstant.BIZ_TYPE_WEB_RECHARGE);
					String status = (String)tppService.tppQueryTransStat(params);
					LOGGER.info("交易状态：{}",status);
					if("S1".equals(status) || "W4".equals(status)){//交易成功 已清分 ；银行受理成功
						LOGGER.info("充值成功 ...");
						final TppRechargeModel rechargeModel = new TppRechargeModel();
						rechargeModel.setAdvanceFee(true);
						rechargeModel.setRechargeFee(BigDecimal.ZERO);
						rechargeModel.setRealAmount(recharge.getAmount());
						rechargeModel.setRechargeMoney(recharge.getAmount());
						rechargeModel.setOrdNo(recharge.getOrderNo());
						rechargeModel.setTradeNo(recharge.getOrderNo());
						ufxRechargeService.tppRechargeSucc(rechargeModel);
					}else if("W3".equals(status)){//请求处理中  不做处理 
						LOGGER.info("请求处理中...");
					}else if("C1".equals(status)){//充值初始化 。。不做处理  等待回调
						LOGGER.info("充值初始化 ...");
					}else{//其他的全是充值失败
						LOGGER.info("充值失败 ...");
						//失败，修改订单状态
						final TppRechargeModel rechargeModel = new TppRechargeModel();
						rechargeModel.setOrdNo(recharge.getOrderNo());
						rechargeModel.setRespDesc("查询交易状态：交易失败");
						ufxRechargeService.tppRechargeFail(rechargeModel);
					}
				}
			}
		}
	}
	
	
	
	@Override
	public void doTppCbhbRecharge(CbhbWebRechargeModel model) {
		if (model.validSign(model)) {
			// 重复处理判断(缓存标记)
			String handleKey = String.format(CacheKeys.PREFIX_RECHARGE_ORDER_NO_HANDLE_NUM.getKey(), model.getMerBillNo());
			if (CacheUtils.incr(handleKey, Constant.DOUBLE_ONE) > Constant.DOUBLE_ONE) {
				LOGGER.info("订单号：{} ----充值订单回调, 已处理", model.getMerBillNo());
				return;
			}
			CacheUtils.expire(handleKey, ExpireTime.ONE_HOUR);
			
			LOGGER.info("充值回调进入本地处理，订单号：{}，返回状态：{}", model.getMerBillNo(), EncodeUtils.urlDecode(model.getRespCode()));
			if (CbhbConstant.CBHB_CODE_SUCCESS.equals(model.getRespCode())) {
				final TppRechargeModel rechargeModel = new TppRechargeModel();
				rechargeModel.setAdvanceFee(true);
				rechargeModel.setRechargeFee(BigDecimalUtils.div(NumberUtils.getBigDecimal(model.getMerFeeAmt()), new BigDecimal(Constant.INT_ONE_HUNDRED)));
				rechargeModel.setRealAmount(BigDecimalUtils.div(NumberUtils.getBigDecimal(model.getTransAmt()), new BigDecimal(Constant.INT_ONE_HUNDRED)));
				rechargeModel.setRechargeMoney(BigDecimalUtils.div(NumberUtils.getBigDecimal(model.getTransAmt()), new BigDecimal(Constant.INT_ONE_HUNDRED)));
				rechargeModel.setAdvanceFeeAmount(BigDecimalUtils.div(NumberUtils.getBigDecimal(model.getFeeAmt()), new BigDecimal(Constant.INT_ONE_HUNDRED)));
				rechargeModel.setOrdNo(model.getMerBillNo());
				rechargeModel.setTradeNo(model.getTransId());
				ufxRechargeService.tppRechargeSucc(rechargeModel);
				
			}else{
				//失败，修改订单状态
				final TppRechargeModel rechargeModel = new TppRechargeModel();
				rechargeModel.setOrdNo(model.getMerBillNo());
				rechargeModel.setRespDesc(model.getRespDesc());
				ufxRechargeService.tppRechargeFail(rechargeModel);
			}
		}else{
			LOGGER.error("充值回调验签失败，订单号：{}", model.getMerBillNo());
			throw new CbhbException(ResourceUtils.get(ResourceConstant.TPP_NOTIFY_SIGN_FAIL),BussinessException.TYPE_JSON);
		}
	}

	
}