package com.rd.ifaes.core.sys.domain;

import java.util.Date;

import com.rd.ifaes.common.entity.BaseEntity;

/**
 * entity:Log
 * 
 * @author lh
 * @version 3.0
 * @date 2016-6-2
 */
public class Log extends BaseEntity<Log> {
	
	private static final long serialVersionUID = 1L;
	
	private String	logType;		 /* 日志类型 */ 
	private String	ip;		 /* 操作IP地址 */ 
	private String	requestUri;		 /* 请求URI */ 
	private String	requestMethod;		 /* 操作方式 */ 
	private String	params;		 /* 操作提交的数据 */ 
	private String	exception;		 /* 异常信息 */ 
	private Date	createTime;		 /* 添加时间 */ 
	private String	remark;		 /* 备注 */ 
	private String userId;		/*操作人员id */
	private double takeTime;	/* 耗时(s)	*/
	private String operationContent;/* 操作内容 */ 

	//其他自定义属性
	private String loginName;

	// Constructor
	public Log() {
	}


	public Log(String logType, String ip, String requestUri, String requestMethod,String operationContent, String params, String exception,
			Date createTime, String remark, String userId, double takeTime) {
		super();
		this.logType = logType;
		this.ip = ip;
		this.requestUri = requestUri;
		this.requestMethod = requestMethod;
		this.params = params;
		this.exception = exception;
		this.createTime = createTime;
		this.remark = remark;
		this.userId = userId;
		this.operationContent = operationContent;
		this.takeTime = takeTime;
	}


	public String getLogType() {
		return logType;
	}

	public void setLogType(String logType) {
		this.logType = logType;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getRequestUri() {
		return requestUri;
	}

	public void setRequestUri(String requestUri) {
		this.requestUri = requestUri;
	}

	public String getRequestMethod() {
		return requestMethod;
	}

	public void setRequestMethod(String requestMethod) {
		this.requestMethod = requestMethod;
	}

	public String getParams() {
		return params;
	}

	public void setParams(String params) {
		this.params = params;
	}

	public String getException() {
		return exception;
	}

	public void setException(String exception) {
		this.exception = exception;
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
	
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public double getTakeTime() {
		return takeTime;
	}

	public void setTakeTime(double takeTime) {
		this.takeTime = takeTime;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	/**
	 * 获取属性operationContent的值
	 * @return operationContent属性值
	 */
	public String getOperationContent() {
		return operationContent;
	}


	/**
	 * 设置属性operationContent的值
	 * @param  operationContent属性值
	 */
	public void setOperationContent(String operationContent) {
		this.operationContent = operationContent;
	}


	@Override
	public void preInsert() {
		super.preInsert();
		this.createTime = new Date();
	}

	@Override
	public String toString() {
		return "Log [" + "uuid=" + uuid + ", logType=" + logType + ", ip=" + ip + ", requestUri=" + requestUri + ", requestMethod=" + requestMethod + ", params=" + params + ", exception=" + exception + ", createTime=" + createTime + ", remark=" + remark +  "]";
	}
}
