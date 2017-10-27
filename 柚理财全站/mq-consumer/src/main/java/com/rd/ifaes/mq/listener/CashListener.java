package com.rd.ifaes.mq.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.rd.ifaes.common.rabbit.model.MqCashModel;
import com.rd.ifaes.core.account.model.CashModel;
import com.rd.ifaes.core.core.constant.MqConstant;
import com.rd.ifaes.core.tpp.model.ufx.TppRechargeModel;
import com.rd.ifaes.core.tpp.model.ufx.UfxCashAuditModel;
import com.rd.ifaes.core.tpp.service.tpp.UfxCashAuditService;
import com.rd.ifaes.core.tpp.service.tpp.UfxCashService;
import com.rd.ifaes.core.tpp.service.tpp.UfxRechargeService;
import com.rd.ifaes.mq.MqListener;

/**
 * 充值取现队列监听
 * 
 * @version 3.0
 * @author lh
 * @date 2016年8月4日
 */
@Component
public class CashListener implements MqListener<MqCashModel>{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CashListener.class);

	@Autowired
	private UfxCashService ufxCashService;
	
	@Autowired
	private UfxRechargeService ufxRechargeService;
	
	@Autowired
	private UfxCashAuditService ufxCashAuditService;

	@Override
	public void listen(MqCashModel model) {
		LOGGER.info("充值提现队列监听已开启");
		LOGGER.info(model.toString());
		if (MqConstant.OPERATE_CASH.equals(model.getOperate())) {
			cash(model.getCashModel());
		} else if (MqConstant.OPERATE_CASH_BANK_RETURN.equals(model.getOperate())) {
			cashBankReturn(model.getCashModel());
			
		} else if(MqConstant.OPERATE_CASH_AUDIT.equals(model.getOperate())){
			cashAudit(model.getCashAuditModel());
			
		} else if (MqConstant.OPERATE_RECHARGE_SUCCESS.equals(model.getOperate())) {
			rechargeSuccess(model.getRechargeModel());
		} else if (MqConstant.OPERATE_RECHARGE_FAIL.equals(model.getOperate())) {
			rechargeFail(model.getRechargeModel());
		}
	}

	/**
	 * 取现-同步异步回调业务处理
	 * 
	 * @author lh
	 * @date 2016年8月4日
	 * @param model
	 */
	public void cash(CashModel model) {
		ufxCashService.doTppCashService(model);
	}
	
	/**
	 * 取现-异步对账回调业务处理
	 * 
	 * @author xhf
	 * @date 2016年8月12日
	 * @param model
	 */
	public void cashBankReturn(CashModel model) {
		ufxCashService.doCashBankReturn(model);
	}
	
	/**
	 * 取现复核回调业务处理
	 * 
	 * @author lh
	 * @date 2016年8月4日
	 * @param model
	 */
	public void cashAudit(UfxCashAuditModel model) {
		ufxCashAuditService.doTppCashAuditService(model);
	}

	/**
	 * 充值回调处理
	 * 
	 * @author lh
	 * @date 2016年8月4日
	 * @param model
	 */
	public void rechargeSuccess(TppRechargeModel model) {
		ufxRechargeService.tppRechargeSucc(model);
	}

	/**
	 * 充值失败回调处理
	 * 
	 * @author lh
	 * @date 2016年8月4日
	 * @param model
	 */
	public void rechargeFail(TppRechargeModel model) {
		ufxRechargeService.tppRechargeFail(model);
	}

}
