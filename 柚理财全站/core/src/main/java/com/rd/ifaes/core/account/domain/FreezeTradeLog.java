package com.rd.ifaes.core.account.domain;

import java.math.BigDecimal;
import java.util.Date;

import com.rd.ifaes.common.entity.BaseEntity;
import com.rd.ifaes.common.util.DateUtils;

/**
 * entity:FreezeTradeLog
 * 
 * @author FangJun
 * @version 3.0
 * @date 2016-7-18
 */
public class FreezeTradeLog extends BaseEntity<FreezeTradeLog> {
	
	private static final long serialVersionUID = 1L;
	
	private String	freezeType;		 /* 冻结类型（00 冻结, 01 取现冻结;02 还款冻结 ,默认00  ） */ 
	private String	orderNo;		 /* 订单号（平台本地生成订单号） */ 
	private String	userId;		 /* 用户号 */ 
	private BigDecimal	amount;		 /* 交易金额 */ 
	private String	projectNo;		 /* 项目编号(还款冻结) */ 
	private String	freezeNo;		 /* 冻结流水号 */ 
	private Date	createTime;		 /* 添加时间 */ 
	private String	remark;		 /* 备注 */ 
	private String	investId;		 /* 投资记录ID */ 
	private String	status;		 /* 状态 */

	//其他自定义属性
	/**
	 * 冻结操作类型：取现
	 */
	public static final String FREEZE_TYPE_CASH = "01";
	/**
	 * 冻结操作类型：还款
	 */
	public static final String FREEZE_TYPE_REPAY = "02";
	/**
	 * 冻结操作类型：变现
	 */
	public static final String FREEZE_TYPE_REALIZE = "03";
	/**
	 * 冻结状态,已冻结
	 */
	public static final String FREEZE_STATUS_FREEZE = "0";
	/**
	 * 冻结状态,已解冻
	 */
	public static final String FREEZE_STATUS_UNFREEZE = "1";

	// Constructor
	public FreezeTradeLog() {
	}
	
	

	public FreezeTradeLog(String freezeType, String orderNo, String freezeNo) {
		super();
		this.freezeType = freezeType;
		this.orderNo = orderNo;
		this.freezeNo = freezeNo;
		this.createTime = DateUtils.getNow();
	}



	/**
	 * full Constructor
	 */
	public FreezeTradeLog(String uuid, String freezeType, String orderNo, String userId, BigDecimal amount, String projectNo, String freezeNo, Date createTime, String remark) {
		setUuid(uuid);
		this.freezeType = freezeType;
		this.orderNo = orderNo;
		this.userId = userId;
		this.amount = amount;
		this.projectNo = projectNo;
		this.freezeNo = freezeNo;
		this.createTime = createTime;
		this.remark = remark;
	}

	public String getFreezeType() {
		return freezeType;
	}

	public void setFreezeType(String freezeType) {
		this.freezeType = freezeType;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getProjectNo() {
		return projectNo;
	}

	public void setProjectNo(String projectNo) {
		this.projectNo = projectNo;
	}

	public String getFreezeNo() {
		return freezeNo;
	}

	public void setFreezeNo(String freezeNo) {
		this.freezeNo = freezeNo;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	/**
	 * 获取属性investId的值
	 * @return investId属性值
	 */
	public String getInvestId() {
		return investId;
	}

	/**
	 * 设置属性investId的值
	 * @param  investId属性值
	 */
	public void setInvestId(String investId) {
		this.investId = investId;
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

	@Override
	public String toString() {
		return "FreezeTradeLog [" + "uuid=" + uuid + ", freezeType=" + freezeType + ", orderNo=" + orderNo + ", userId=" + userId + ", amount=" + amount + ", projectNo=" + projectNo + ", freezeNo=" + freezeNo + ", createTime=" + createTime + ", remark=" + remark +  "]";
	}
}
