package com.rd.ifaes.core.account.service.impl;
import java.math.BigDecimal;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.rd.ifaes.common.rabbit.RabbitProducer;
import com.rd.ifaes.common.util.DateUtils;
import com.rd.ifaes.core.account.domain.CashFeeMarkLog;
import com.rd.ifaes.core.account.mapper.CashFeeMarkLogMapper;
import com.rd.ifaes.core.account.service.CashFeeMarkLogService;
import com.rd.ifaes.core.base.service.BaseServiceImpl;
import com.rd.ifaes.core.core.constant.Constant;
import com.rd.ifaes.core.core.constant.MqConstant;
import com.rd.ifaes.core.core.constant.ResourceConstant;
import com.rd.ifaes.core.core.util.ResourceUtils;

@Service("cashFeeMarkLogService")
public class CashFeeMarkLogServiceImpl extends BaseServiceImpl<CashFeeMarkLogMapper, CashFeeMarkLog> implements CashFeeMarkLogService {

	@Resource
	private RabbitProducer rabbitProducer;
	
	@Override
	public String getRemarkByAccountCode(CashFeeMarkLog log) {
		if(Constant.RECHARGE_SUCCESS_LOG.equals(log.getAccountType())){
			//充值，获取充值提示
			return ResourceUtils.get(ResourceConstant.CASH_FEE_MARK_LOG_RECHARGE, log.getMoney());
		}else if(Constant.CASH_PROCCESS.equals(log.getAccountType())){
			//提现，获取提现处理中提示
			return ResourceUtils.get(ResourceConstant.CASH_FEE_MARK_LOG_CASH_DISPOSE, log.getMoney());
		}else if(Constant.CASH_FAIL.equals(log.getAccountType())){
			return ResourceUtils.get(ResourceConstant.CASH_FEE_MARK_LOG_CASH_FAIL, log.getMoney());
		}else if(Constant.INVEST_SUCCESS.equals(log.getAccountType())){
			return ResourceUtils.get(ResourceConstant.CASH_FEE_MARK_LOG_INVEST, log.getMoney());
		}else if(Constant.BOND_INVEST_SUCCESS.equals(log.getAccountType())){
			return ResourceUtils.get(ResourceConstant.CASH_FEE_MARK_LOG_BOND, log.getMoney());
		}
		return null;
	}

	@Override
	public void preSave(String userId, String accountType, BigDecimal money,String cashId, String investId) {
		if(BigDecimal.ZERO.compareTo(money) != 0) {
			CashFeeMarkLog cashFeeMarkLog = new CashFeeMarkLog();
			cashFeeMarkLog.setUserId(userId);
			cashFeeMarkLog.setAccountType(accountType);
			cashFeeMarkLog.setMoney(money);
			cashFeeMarkLog.setCashId(cashId);
			cashFeeMarkLog.setInvestId(investId);
			if(BigDecimal.ZERO.compareTo(money) < 0){
				//收入
				cashFeeMarkLog.setMarkType(Constant.ACCOUNT_CHANGE_TYPE_EARN);
			}else{
				//支出
				cashFeeMarkLog.setMarkType(Constant.ACCOUNT_CHANGE_TYPE_EXPENSE);
			}
			cashFeeMarkLog.setCreateTime(DateUtils.getNow());
			cashFeeMarkLog.setRemark(getRemarkByAccountCode(cashFeeMarkLog));
			rabbitProducer.send(MqConstant.ROUTING_KEY_CASH_FEE_MARK_LOG, cashFeeMarkLog);
		}
	}

	@Override
	public CashFeeMarkLog findOne(CashFeeMarkLog cashFeeMarkLog) {
		return dao.findOne(cashFeeMarkLog);
	}

}
