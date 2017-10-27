package com.rd.ifaes.core.user.domain;

import java.util.Date;

import com.rd.ifaes.common.entity.BaseEntity;
import com.rd.ifaes.core.tpp.model.ufx.UfxAuthSignModel;

/**
 * entity:UserAuthSignLog
 * 
 * @author zb
 * @version 3.0
 * @date 2016-7-22
 */
public class UserAuthSignLog extends BaseEntity<UserAuthSignLog> {
	
	private static final long serialVersionUID = 1L;
	
	private String	userId;		 /* 用户 */ 
	private String	authOption;		 /* 签约类型 0：授权解约 1：授权签约  */ 
	private String	serviceTypes;		 /* 业务类型 */ 
	private String	orderNo;		 /* 订单号 */ 
	private String	respDesc;		 /* 0.未处理 1.成功 2.失败 */ 
	private Date	createTime;		 /* 添加时间 */ 
	private String	addIp;		 /* 操作ip */ 

	//其他自定义属性
	public static final String RESP_DESC = "0";// 未处理
	public static final String RESP_DESC_SUCC = "1";// 成功
	public static final String RESP_DESC_FAIL = "2";// 失败
	
	public static final String SERVICE_TYPE_AUTO_INVEST = "1";//自动投资
	public static final String SERVICE_TYPE_AUTO_BOND = "2";//自动债权

	// Constructor
	public UserAuthSignLog() {
	}

	/**
	 * full Constructor
	 */
	public UserAuthSignLog(String uuid, String userId, String authOption, String serviceTypes, String orderNo, String respDesc, Date createTime, String addIp) {
		setUuid(uuid);
		this.userId = userId;
		this.authOption = authOption;
		this.serviceTypes = serviceTypes;
		this.orderNo = orderNo;
		this.respDesc = respDesc;
		this.createTime = createTime;
		this.addIp = addIp;
	}
	
	public UserAuthSignLog(UfxAuthSignModel authSign) {
		this.authOption = authSign.getAuthOption();
		this.orderNo = authSign.getOrderNo();
		this.respDesc = RESP_DESC;
		this.serviceTypes = authSign.getServiceTypes();
		this.createTime = new Date();
	}

	public UserAuthSignLog(String respDesc, String orderNo) {
		this.respDesc = respDesc;
		this.orderNo = orderNo;
	}
	
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getAuthOption() {
		return authOption;
	}

	public void setAuthOption(String authOption) {
		this.authOption = authOption;
	}

	public String getServiceTypes() {
		return serviceTypes;
	}

	public void setServiceTypes(String serviceTypes) {
		this.serviceTypes = serviceTypes;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getRespDesc() {
		return respDesc;
	}

	public void setRespDesc(String respDesc) {
		this.respDesc = respDesc;
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

	@Override
	public String toString() {
		return "UserAuthSignLog [" + "uuid=" + uuid + ", userId=" + userId + ", authOption=" + authOption + ", serviceTypes=" + serviceTypes + ", orderNo=" + orderNo + ", respDesc=" + respDesc + ", createTime=" + createTime + ", addIp=" + addIp +  "]";
	}
}
