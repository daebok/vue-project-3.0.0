package com.rd.ifaes.core.account.domain;

import java.math.BigDecimal;
import java.util.Date;

import com.rd.ifaes.common.annotation.DictType;
import com.rd.ifaes.common.entity.BaseEntity;
import com.rd.ifaes.common.util.DateUtils;
import com.rd.ifaes.core.account.model.TppMerchantLogModel;

public class TppMerchantLog  extends BaseEntity<TppMerchantLog> {

	/** 
	* @Fields serialVersionUID : 
	*/ 
	private static final long serialVersionUID = 1L;
	/**
	 * 操作员ID
	 */
	private String operatorId;

	/**
	 * 付款账户
	 */
	private String account;

	/**
	 * 收款账户
	 * 
	 */
	private String toAccount;

	/**
	 * 操作金额
	 */
	private BigDecimal money;

	/**
	 * 操作手续费
	 */
	private BigDecimal fee;

	/**
	 * 操作状态
	 */
	@DictType(type="platAccountStatus")
	private String status;

	/**
	 * 操作类型 
	 */
	@DictType(type="platAccountType")
	private String operateType;

	/**
	 * 订单号
	 */
	private String orderNo;

	/**
	 * 操作时间
	 */
	private Date createTime;

	/**
	 * 添加ip
	 */
	private String addIp;

	/**
	 * 备注
	 */
	private String remark;
	
	
	//自定义字段
	/**
	 * 用户名
	 */
	private String loginName;
	
	/**
	 * 真实姓名
	 */
	private String realName;
	
	/**
	 * 渤海充值商户类型
	 */
	private String merAccTyp;
	
	public TppMerchantLog() {
		super();
	}
	
	public TppMerchantLog(String operateType,String account,String toAccount,BigDecimal money,String orderNo,String remark) {
		super();
		this.operateType = operateType;
		this.account = account;
		this.toAccount = toAccount;
		this.money = money;
		this.orderNo = orderNo;
		this.remark = remark;
	}	
	
	@Override
	public void preInsert(){
		super.preInsert();
		this.setFee(BigDecimal.ZERO);
		this.setCreateTime(DateUtils.getNow());
	}
	
	public String getOperatorId() {
		return operatorId;
	}

	public void setOperatorId(String operatorId) {
		this.operatorId = operatorId;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getToAccount() {
		return toAccount;
	}

	public void setToAccount(String toAccount) {
		this.toAccount = toAccount;
	}

	public BigDecimal getMoney() {
		return money;
	}

	public void setMoney(BigDecimal money) {
		this.money = money;
	}

	public BigDecimal getFee() {
		return fee;
	}

	public void setFee(BigDecimal fee) {
		this.fee = fee;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getOperateType() {
		return operateType;
	}

	public void setOperateType(String operateType) {
		this.operateType = operateType;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getAddIp() {
		return addIp;
	}

	public void setAddIp(String addIp) {
		this.addIp = addIp;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}
	
	public String getMerAccTyp() {
		return merAccTyp;
	}

	public void setMerAccTyp(String merAccTyp) {
		this.merAccTyp = merAccTyp;
	}

	/**
	 * 带符号的金额
	 */
	public String getMoneyStr() {
		if (operateType.equals(TppMerchantLogModel.TYPE_MERCHANT_RECHARGE)
				|| operateType.equals(TppMerchantLogModel.TYPE_INTEREST_FEE)
				|| operateType.equals(TppMerchantLogModel.TYPE_BORROW_FEE)
				|| operateType.equals(TppMerchantLogModel.TYPE_BOND_FEE)
				|| operateType.equals(TppMerchantLogModel.TYPE_REALIZE_FEE)
				|| operateType.equals(TppMerchantLogModel.TYPE_OVERDUE_MERCHANT)
				|| operateType.equals(TppMerchantLogModel.TYPE_CUSTOMER_CASH_SERVER)) {
			return "+" + money;
		}else if(operateType.equals(TppMerchantLogModel.TYPE_MERCHANT_CASH) 
				|| operateType.equals(TppMerchantLogModel.TYPE_TRANSFER) 
				|| operateType.equals(TppMerchantLogModel.TYPE_CUSTOMER_RECHARGE)
				|| operateType.equals(TppMerchantLogModel.TYPE_CUSTOMER_CASH)
				|| operateType.equals(TppMerchantLogModel.TYPE_RATECOUPON) 
				|| operateType.equals(TppMerchantLogModel.TYPE_REDENVELOPE)) {
			return  "-"+money;
		}else{
			return ""+money;
		}
	}
}
