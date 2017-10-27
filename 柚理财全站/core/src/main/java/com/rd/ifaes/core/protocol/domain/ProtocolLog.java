package com.rd.ifaes.core.protocol.domain;

import java.util.Date;

import com.rd.ifaes.common.entity.BaseEntity;

public class ProtocolLog extends BaseEntity<ProtocolLog> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 协议模板id
	 */
	private String protocolId;

	/**
	 * 模板类型（注册用户协议、借款协议、转让协议等）
	 */
	private String protocolType;

	/**
	 * 协议名称
	 */
	private String protocolName;

	/**
	 * 业务id，如投资，债权投资等id
	 */
	private String businessId;

	/**
	 * 生成的合同地址
	 */
	private String filePath;

	/**
	 * 添加时间
	 */
	private Date createTime;

	/**
	 * 备注
	 */
	private String remark;
	

	//自定义参数
	/**
	 * 用户名
	 */
	private String userName;
	
	/**
	 * 真实姓名
	 */
	private String realName;

	public String getProtocolId() {
		return protocolId;
	}

	public void setProtocolId(String protocolId) {
		this.protocolId = protocolId;
	}

	public String getProtocolType() {
		return protocolType;
	}

	public void setProtocolType(String protocolType) {
		this.protocolType = protocolType;
	}

	public String getProtocolName() {
		return protocolName;
	}

	public void setProtocolName(String protocolName) {
		this.protocolName = protocolName;
	}

	public String getBusinessId() {
		return businessId;
	}

	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
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

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}
	
	

}
