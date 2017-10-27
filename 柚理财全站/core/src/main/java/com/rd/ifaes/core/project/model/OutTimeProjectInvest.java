/**
* 互联网金融交易系统ifaes (Internet Finance  Assets  Exchange  System)
 * Copyright (c) 2016 杭州融都科技股份有限公司 版权所有
 */
package com.rd.ifaes.core.project.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 超时投资订单记录
 * @version 3.0
 * @author FangJun
 * @date 2016年8月16日
 */
public class OutTimeProjectInvest implements Serializable {
	 
	private static final long serialVersionUID = 8154644648922870378L;
	/**
	 * 主键:UUID
	 */
	private String uuid;
	private String	userId;		 /* 用户ID */ 
	private String projectId; /* 项目ID */
	private String projectNo; /* 项目ID */
	private String status; /* 投资状态 0 待支付，1成功，2失败 */
	private BigDecimal amount; /* 有效投资金额 */
	private BigDecimal	realAmount;		 /* 用户真实资金 */ 
	private Date createTime; /* 添加时间 */
	private String	investOrderNo;		 /* 投标时候的订单号 */ 
	
	public OutTimeProjectInvest() {
		super();
	}
	/**
	 * 获取属性uuid的值
	 * @return uuid属性值
	 */
	public String getUuid() {
		return uuid;
	}
	/**
	 * 设置属性uuid的值
	 * @param  uuid属性值
	 */
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	/**
	 * 获取属性projectId的值
	 * @return projectId属性值
	 */
	public String getProjectId() {
		return projectId;
	}
	/**
	 * 设置属性projectId的值
	 * @param  projectId属性值
	 */
	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
	/**
	 * 获取属性projectNo的值
	 * @return projectNo属性值
	 */
	public String getProjectNo() {
		return projectNo;
	}
	/**
	 * 设置属性projectNo的值
	 * @param  projectNo属性值
	 */
	public void setProjectNo(String projectNo) {
		this.projectNo = projectNo;
	}
	/**
	 * 获取属性status的值
	 * @return status属性值
	 */
	public String getStatus() {
		return status;
	}
	/**
	 * 设置属性status的值
	 * @param  status属性值
	 */
	public void setStatus(String status) {
		this.status = status;
	}
	/**
	 * 获取属性amount的值
	 * @return amount属性值
	 */
	public BigDecimal getAmount() {
		return amount;
	}
	/**
	 * 设置属性amount的值
	 * @param  amount属性值
	 */
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	/**
	 * 获取属性createTime的值
	 * @return createTime属性值
	 */
	public Date getCreateTime() {
		return createTime;
	}
	/**
	 * 设置属性createTime的值
	 * @param  createTime属性值
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	/**
	 * 获取属性realAmount的值
	 * @return realAmount属性值
	 */
	public BigDecimal getRealAmount() {
		return realAmount;
	}
	/**
	 * 设置属性realAmount的值
	 * @param  realAmount属性值
	 */
	public void setRealAmount(BigDecimal realAmount) {
		this.realAmount = realAmount;
	}
	/**
	 * 获取属性userId的值
	 * @return userId属性值
	 */
	public String getUserId() {
		return userId;
	}
	/**
	 * 设置属性userId的值
	 * @param  userId属性值
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}
	/**
	 * 获取属性investOrderNo的值
	 * @return investOrderNo属性值
	 */
	public String getInvestOrderNo() {
		return investOrderNo;
	}
	/**
	 * 设置属性investOrderNo的值
	 * @param  investOrderNo属性值
	 */
	public void setInvestOrderNo(String investOrderNo) {
		this.investOrderNo = investOrderNo;
	}
	
}
