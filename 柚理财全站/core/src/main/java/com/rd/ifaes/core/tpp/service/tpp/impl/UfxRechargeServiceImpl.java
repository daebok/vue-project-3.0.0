package com.rd.ifaes.core.tpp.service.tpp.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rd.account.domain.AccountLog;
import com.rd.account.model.AccountModel;
import com.rd.account.service.AccountLogService;
import com.rd.account.service.AccountService;
import com.rd.ifaes.common.util.BigDecimalUtils;
import com.rd.ifaes.common.util.DateUtils;
import com.rd.ifaes.core.account.domain.Recharge;
import com.rd.ifaes.core.account.model.TppMerchantLogModel;
import com.rd.ifaes.core.account.service.RechargeService;
import com.rd.ifaes.core.account.service.TppMerchantLogService;
import com.rd.ifaes.core.core.constant.Constant;
import com.rd.ifaes.core.core.constant.MessageConstant;
import com.rd.ifaes.core.core.constant.ResourceConstant;
import com.rd.ifaes.core.core.sms.msg.BaseMsg;
import com.rd.ifaes.core.core.util.ConfigUtils;
import com.rd.ifaes.core.core.util.ResourceUtils;
import com.rd.ifaes.core.sys.domain.LogTemplate;
import com.rd.ifaes.core.sys.service.LogTemplateService;
import com.rd.ifaes.core.tpp.exception.UfxException;
import com.rd.ifaes.core.tpp.model.ufx.TppRechargeModel;
import com.rd.ifaes.core.tpp.service.tpp.UfxRechargeService;
import com.rd.ifaes.core.user.domain.User;
import com.rd.ifaes.core.user.service.UserService;

/**
 * 托管接口充值处理方法
 * @author xhf
 * @version 3.0
 * @date 2016年6月28日 下午4:26:08
 */
@Service("ufxRechargeService")
@Transactional
public class UfxRechargeServiceImpl implements UfxRechargeService {
	
	/**
	 * 日志跟踪器
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(UfxRechargeServiceImpl.class);
	
	/**
	 * 用户基础类
	 */
	@Resource
	private transient UserService userService;
	/**
	 * 充值业务处理类
	 */
	@Resource
	private transient RechargeService rechargeService;
	/**
	 * 账户资金处理类
	 */
	@Resource
	private transient AccountService accountService;
	/**
	 * 账户资金日志处理类
	 */
	@Resource
	private transient AccountLogService accountLogService;
	/**
	 * 日志模板处理类
	 */
	@Resource
	private transient LogTemplateService logTemplateService;
	@Resource
	private TppMerchantLogService tppMerchantLogService;
	

	@Override
	@Transactional(readOnly=false)
	public void tppRechargeSucc(final TppRechargeModel model) {	
		final Recharge recharge = rechargeService.getRechargeByOrderNo(model.getOrdNo());
		if (null==recharge) {
			throw new UfxException(ResourceUtils.get(ResourceConstant.ORDER_NOT_EXIST));
		}
		
		//校验订单状态
		if (Constant.RECHARGE_STATUS_SUCCESS.equals(recharge.getStatus())) {
			LOGGER.info("订单已经处理，订单号：{}",model.getOrdNo());
			return;
		}
		
		//处理充值业务
		final User user = userService.get(recharge.getUserId());
		recharge.setAmount(model.getRealAmount());
		if(model.getRechargeFee() != null && BigDecimalUtils.validAmount(model.getRechargeFee())){
			recharge.setRechargeFee(model.getRechargeFee());
		}else{
			recharge.setRechargeFee(BigDecimal.ZERO);
		}
		recharge.setStatus(Constant.RECHARGE_STATUS_SUCCESS);
		recharge.setPayWay(model.getPayway());
		recharge.setTradeNo(model.getTradeNo());
		rechargeService.updateByTppResult(recharge);
		
		//日志
		LOGGER.info("用户userId={}充值成功，增加可用金额：{}",user.getUuid(),model.getRealAmount());			
		
		//添加资金记录
		final Map<String, Object> param = new HashMap<String, Object>();
		param.put("money", model.getRealAmount());
		final String content = logTemplateService.getTemplateContent(LogTemplate.LOG_TYPE_ACCOUNT, Constant.RECHARGE_SUCCESS_LOG, param);
		
		final AccountLog accountLog = new AccountLog(ConfigUtils.getValue(Constant.ACCOUNT_CODE), Constant.RECHARGE_SUCCESS_LOG, user.getUuid(), model.getRealAmount(),
				null, AccountLog.PAYMENTS_TYPE_INCOME, model.getTradeNo(), model.getOrdNo(), content);
		AccountModel accountModel = new AccountModel(user.getUuid(), ConfigUtils.getValue(Constant.ACCOUNT_CODE), model.getRealAmount(), BigDecimal.ZERO);
		accountService.saveAccountChange(accountModel, accountLog);
		
		// 扣除充值垫付手续费
		if(BigDecimalUtils.validAmount(model.getAdvanceFeeAmount())){
			tppMerchantLogService.saveMerchantLog(TppMerchantLogModel.TYPE_CUSTOMER_RECHARGE, user.getUuid(),model.getAdvanceFeeAmount(), model.getOrdNo());
		}
		
		// 发送充值成功信息
		try {
			HashMap<String, Object> params = new HashMap<String, Object>();
			params.put("user", user);
			params.put("amount", model.getRealAmount().doubleValue());
			params.put("rechargeTime", DateUtils.dateStr4(recharge.getCreateTime()));
			BaseMsg baseMsg = new BaseMsg(MessageConstant.RECHARGE_SUCC, params);
			baseMsg.doEvent();
		} catch (Exception e) {
			LOGGER.error("充值成功通知处理失败", e);
		}
	}

	@Override
	@Transactional(readOnly=false)
	public void tppRechargeFail(final TppRechargeModel model) {
		final Recharge recharge = rechargeService.getRechargeByOrderNo(model.getOrdNo());
		if (null==recharge) {
			throw new UfxException(ResourceUtils.get(ResourceConstant.ORDER_NOT_EXIST));
		}
		
		//校验订单状态
		if (Constant.RECHARGE_STATUS_FAIL.equals(recharge.getStatus())) {
			LOGGER.info("订单已经处理，订单号：{}",model.getOrdNo());	
			return;
		}
		
		//更新数据库
		recharge.setStatus(Constant.RECHARGE_STATUS_FAIL);
		recharge.setRemark(model.getRespDesc());
		rechargeService.updateByTppResult(recharge);

	}
	
}
