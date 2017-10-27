/**
 * 标投资记录列表条目
 */
package com.rd.ifaes.mobile.model.bond;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author lgx
 * 申购记录列表条目
 */
public class bondListItemModel {

	/**
	 * 受让人（马赛克）
	 */
	private String userName;
	
	/**
	 * 受让金额
	 */
	private BigDecimal money;
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public BigDecimal getMoney() {
		return money;
	}
	public void setMoney(BigDecimal money) {
		this.money = money;
	}
	/**
	 * 受让时间
	 */
	private Date createTime;
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	} 
	
}
