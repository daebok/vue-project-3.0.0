package com.rd.ifaes.core.account.domain;

import java.math.BigDecimal;
import java.util.Date;

import com.rd.ifaes.common.entity.BaseEntity;
import com.rd.ifaes.core.core.constant.Constant;

/**
 * 提现手续费标记表
 * @author zhangxj
 * @version 3.0
 * @date 2017-09-18
 */
public class CashFeeMarkLog extends BaseEntity<CashFeeMarkLog>  {

	
	private static final long serialVersionUID = 1L;
	private String userId;/* 用户主键 */
	private String accountType;/* 交易的金额类型 */
	private String cashId;/* 投资操作变动，保存得提现记录id*/
	private String investId;/* 投资操作变动，保存得投资记录id*/
	private String markType;/* 标记类型：收入earn 支出 expense */
	private BigDecimal money;/* 交易金额 */
	private String remark;/* 备注 */
	private Date createTime;/* 创建时间 */
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	public String getAccountType() {
		return accountType;
	}
	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}
	
	public String getCashId() {
		return cashId;
	}
	public void setCashId(String cashId) {
		this.cashId = cashId;
	}
	public String getInvestId() {
		return investId;
	}
	public void setInvestId(String investId) {
		this.investId = investId;
	}
	public String getMarkType() {
		return markType;
	}
	public void setMarkType(String markType) {
		this.markType = markType;
	}
	public BigDecimal getMoney() {
		return money;
	}
	public void setMoney(BigDecimal money) {
		this.money = money;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	@Override
	public String toString() {
		return "CashFeeMarkLog [userId=" + userId + ", accountType=" + accountType + ", investId=" + investId
				+ ", markType=" + markType + ", money=" + money + ", remark=" + remark + ", createTime=" + createTime
				+ "]";
	}
	public CashFeeMarkLog() {
		// TODO Auto-generated constructor stub
	}
	public CashFeeMarkLog(String userId, String accountType, String relativeId) {
		super();
		this.userId = userId;
		this.accountType = accountType;
		if(Constant.CASH_PROCCESS.equals(accountType) || Constant.CASH_FAIL.equals(accountType)){
			this.cashId = relativeId;
		}else{
			this.investId = relativeId;
		}
	}

}
