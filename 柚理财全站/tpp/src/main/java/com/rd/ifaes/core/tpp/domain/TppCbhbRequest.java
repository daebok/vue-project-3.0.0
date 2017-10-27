package com.rd.ifaes.core.tpp.domain;

import java.util.Date;

import com.rd.ifaes.common.entity.BaseEntity;

/**
 * entity:TppCbhbRequest
 *  渤海银行请求记录实体类
 * @author qpz
 * @version 3.0
 * @date 2017-3-23
 */
public class TppCbhbRequest extends BaseEntity<TppCbhbRequest> {
	
	private static final long serialVersionUID = 1L;
	
	private String	requestType;		 /* 请求类型 */ 
	private String	requestMerbillno;		 /* 请求流水号 */ 
	private String	requestMap;		 /* 请求数据包（包含明文、密文、证书信息） */ 
	private String	requestFileUrl;		 /* 请求文件的路径 */ 
	private Date	requestTime;		 /* 请求时间 */ 

	//其他自定义属性
	

	// Constructor
	public TppCbhbRequest() {
	}

	/**
	 * full Constructor
	 */
	public TppCbhbRequest(String uuid, String requestType, String requestMerbillno, String requestMap, String requestFileUrl, Date requestTime) {
		setUuid(uuid);
		this.requestType = requestType;
		this.requestMerbillno = requestMerbillno;
		this.requestMap = requestMap;
		this.requestFileUrl = requestFileUrl;
		this.requestTime = requestTime;
	}


	public String getRequestType() {
		return requestType;
	}

	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}

	public String getRequestMerbillno() {
		return requestMerbillno;
	}

	public void setRequestMerbillno(String requestMerbillno) {
		this.requestMerbillno = requestMerbillno;
	}

	public String getRequestMap() {
		return requestMap;
	}

	public void setRequestMap(String requestMap) {
		this.requestMap = requestMap;
	}

	public String getRequestFileUrl() {
		return requestFileUrl;
	}

	public void setRequestFileUrl(String requestFileUrl) {
		this.requestFileUrl = requestFileUrl;
	}

	public Date getRequestTime() {
		return requestTime;
	}

	public void setRequestTime(Date requestTime) {
		this.requestTime = requestTime;
	}
	
	@Override
	public String toString() {
		return "TppCbhbRequest [" + "uuid=" + uuid + ", requestType=" + requestType + ", requestMerbillno=" + requestMerbillno + ", requestMap=" + requestMap + ", requestFileUrl=" + requestFileUrl + ", requestTime=" + requestTime +  "]";
	}
}
