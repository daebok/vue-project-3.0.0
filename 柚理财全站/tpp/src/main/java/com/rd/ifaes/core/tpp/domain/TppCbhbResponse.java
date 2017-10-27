package com.rd.ifaes.core.tpp.domain;


import java.util.Date;

import com.rd.ifaes.common.entity.BaseEntity;

/**
 * entity:TppCbhbResponse
 * 渤海银行回调响应记录实体类
 * @author qpz
 * @version 3.0
 * @date 2017-3-23
 */
public class TppCbhbResponse extends BaseEntity<TppCbhbResponse> {
	
	private static final long serialVersionUID = 1L;
	
	private String	respType;		 /* 响应类型 */ 
	private String	requestMerbillno;		 /* 请求流水号 */ 
	private String	respTransid;		 /* 回调响应流水号 */ 
	private String	respMap;		 /* 回调响应数据包（包含明文、密文、证书） */ 
	private String	respFileUrl;		 /* 回调响应文件地址 */ 
	private String	respCode;		 /* 实时回调码 */ 
	private String	respDesc;		 /* 实时回调码解析 */ 
	private Date	respTime;		 /* 实时响应时间 */ 
	private String	returnRespCode;		 /* 同步回调码 */ 
	private String	returnRespDesc;		 /* 同步回调码对应解析 */ 
	private Date	returnRespTime;		 /* 同步响应时间 */ 
	private String	notifyRespCode;		 /* 异步回调码 */ 
	private String	notifyRespDesc;		 /* 异步回调码解析 */ 
	private Date	notifyRespTime;		 /* 异步响应时间 */ 

	//其他自定义属性
	

	// Constructor
	public TppCbhbResponse() {
	}

	/**
	 * full Constructor
	 */
	public TppCbhbResponse(String uuid, String respType, String requestMerbillno, String respTransid, String respMap, String respFileUrl, String respCode, String respDesc, Date respTime, String returnRespCode, String returnRespDesc, Date returnRespTime, String notifyRespCode, String notifyRespDesc, Date notifyRespTime) {
		setUuid(uuid);
		this.respType = respType;
		this.requestMerbillno = requestMerbillno;
		this.respTransid = respTransid;
		this.respMap = respMap;
		this.respFileUrl = respFileUrl;
		this.respCode = respCode;
		this.respDesc = respDesc;
		this.respTime = respTime;
		this.returnRespCode = returnRespCode;
		this.returnRespDesc = returnRespDesc;
		this.returnRespTime = returnRespTime;
		this.notifyRespCode = notifyRespCode;
		this.notifyRespDesc = notifyRespDesc;
		this.notifyRespTime = notifyRespTime;
	}

	public String getRespType() {
		return respType;
	}

	public void setRespType(String respType) {
		this.respType = respType;
	}

	public String getRequestMerbillno() {
		return requestMerbillno;
	}

	public void setRequestMerbillno(String requestMerbillno) {
		this.requestMerbillno = requestMerbillno;
	}

	public String getRespTransid() {
		return respTransid;
	}

	public void setRespTransid(String respTransid) {
		this.respTransid = respTransid;
	}

	public String getRespMap() {
		return respMap;
	}

	public void setRespMap(String respMap) {
		this.respMap = respMap;
	}

	public String getRespFileUrl() {
		return respFileUrl;
	}

	public void setRespFileUrl(String respFileUrl) {
		this.respFileUrl = respFileUrl;
	}

	public String getRespCode() {
		return respCode;
	}

	public void setRespCode(String respCode) {
		this.respCode = respCode;
	}

	public String getRespDesc() {
		return respDesc;
	}

	public void setRespDesc(String respDesc) {
		this.respDesc = respDesc;
	}

	public Date getRespTime() {
		return respTime;
	}

	public void setRespTime(Date respTime) {
		this.respTime = respTime;
	}

	public String getReturnRespCode() {
		return returnRespCode;
	}

	public void setReturnRespCode(String returnRespCode) {
		this.returnRespCode = returnRespCode;
	}

	public String getReturnRespDesc() {
		return returnRespDesc;
	}

	public void setReturnRespDesc(String returnRespDesc) {
		this.returnRespDesc = returnRespDesc;
	}

	public Date getReturnRespTime() {
		return returnRespTime;
	}

	public void setReturnRespTime(Date returnRespTime) {
		this.returnRespTime = returnRespTime;
	}

	public String getNotifyRespCode() {
		return notifyRespCode;
	}

	public void setNotifyRespCode(String notifyRespCode) {
		this.notifyRespCode = notifyRespCode;
	}

	public String getNotifyRespDesc() {
		return notifyRespDesc;
	}

	public void setNotifyRespDesc(String notifyRespDesc) {
		this.notifyRespDesc = notifyRespDesc;
	}

	public Date getNotifyRespTime() {
		return notifyRespTime;
	}

	public void setNotifyRespTime(Date notifyRespTime) {
		this.notifyRespTime = notifyRespTime;
	}

	@Override
	public String toString() {
		return "TppCbhbResponse [" + "uuid=" + uuid + ", respType=" + respType + ", requestMerbillno=" + requestMerbillno + ", respTransid=" + respTransid + ", respMap=" + respMap + ", respFileUrl=" + respFileUrl + ", respCode=" + respCode + ", respDesc=" + respDesc + ", respTime=" + respTime + ", returnRespCode=" + returnRespCode + ", returnRespDesc=" + returnRespDesc + ", returnRespTime=" + returnRespTime + ", notifyRespCode=" + notifyRespCode + ", notifyRespDesc=" + notifyRespDesc + ", notifyRespTime=" + notifyRespTime +  "]";
	}
}
