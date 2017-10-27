package com.rd.ifaes.core.project.domain;

import java.util.Date;

import com.rd.ifaes.common.entity.BaseEntity;

/**
 * 
 *  催收记录
 * @version 3.0
 * @author wyw
 * @date 2016-8-17
 */
public class UrgeRepayLog extends BaseEntity<UrgeRepayLog> {
	
	private static final long serialVersionUID = 1L;
	
	private String	repaymentId;		 /* 代还id */ 
	private String	remark;		 /* 备注 */ 
	private Date	createTime;		 /* 催收时间 */ 
	private String	operatorId;		 /* 操作员id */ 

	//其他自定义属性
	

	// Constructor
	public UrgeRepayLog() {
	}

	/**
	 * full Constructor
	 */
	public UrgeRepayLog(String uuid, String repaymentId, String remark, Date createTime, String operatorId) {
		setUuid(uuid);
		this.repaymentId = repaymentId;
		this.remark = remark;
		this.createTime = createTime;
		this.operatorId = operatorId;
	}

	public String getRepaymentId() {
		return repaymentId;
	}

	public void setRepaymentId(String repaymentId) {
		this.repaymentId = repaymentId;
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

	public String getOperatorId() {
		return operatorId;
	}

	public void setOperatorId(String operatorId) {
		this.operatorId = operatorId;
	}

	@Override
	public String toString() {
		return "UrgeRepayLog [" + "uuid=" + uuid + ", repaymentId=" + repaymentId + ", remark=" + remark + ", createTime=" + createTime + ", operatorId=" + operatorId +  "]";
	}
}
