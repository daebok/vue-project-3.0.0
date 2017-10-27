package com.rd.ifaes.common.rabbit.model;

import com.rd.ifaes.core.account.model.CashModel;
import com.rd.ifaes.core.core.constant.MqConstant;
import com.rd.ifaes.core.tpp.model.ufx.TppRechargeModel;
import com.rd.ifaes.core.tpp.model.ufx.UfxCashAuditModel;

/**
 * 充值取现
 * @version 3.0
 * @author lh
 * @date 2016年8月6日
 */
public class MqCashModel extends MqBaseModel {
	
	/**
	 * 取现
	 */
	private CashModel cashModel;
	/**
	 * 充值
	 */
	private TppRechargeModel rechargeModel;
	
	/**
	 * 取现复核
	 */
	private UfxCashAuditModel cashAuditModel;

	/**
	 * 无参构造方法
	 */
	public MqCashModel() {
		super();
	}
	
	/**
	 * 
	 * @param operate
	 */
	public MqCashModel(String operate) {
		this.operate = operate;
	}
	
	/**
	 * 
	 * @param cashModel
	 */
	public MqCashModel(CashModel cashModel) {
		this.operate = MqConstant.OPERATE_CASH;
		this.cashModel = cashModel;
	}
	
	/**
	 * 
	 * @param operate
	 * @param rechargeModel
	 */
	public MqCashModel(String operate, TppRechargeModel rechargeModel) {
		this.operate = operate;
		this.rechargeModel = rechargeModel;
	}

	/**
	 * 获取取现Model
	 * @return cashModel
	 */
	public CashModel getCashModel() {
		return cashModel;
	}

	/**
	 * 设置取现Model
	 * @param  cashModel
	 */
	public void setCashModel(CashModel cashModel) {
		this.cashModel = cashModel;
	}

	/**
	 * 获取充值Model
	 * @return rechargeModel
	 */
	public TppRechargeModel getRechargeModel() {
		return rechargeModel;
	}

	/**
	 * 设置充值Model
	 * @param  rechargeModel
	 */
	public void setRechargeModel(TppRechargeModel rechargeModel) {
		this.rechargeModel = rechargeModel;
	}

	/**
	 * 获取取现复核Model
	 * @return cashAuditModel
	 */
	public UfxCashAuditModel getCashAuditModel() {
		return cashAuditModel;
	}

	/**
	 * 设置取现复核Model
	 * @param  cashAuditModel
	 */
	public void setCashAuditModel(UfxCashAuditModel cashAuditModel) {
		this.cashAuditModel = cashAuditModel;
	}

	@Override
	public String toString() {
		return "MqCashModel [operate="+operate+", cashModel=" + cashModel + ", rechargeModel=" + rechargeModel + "]";
	}
	
	
	
}
