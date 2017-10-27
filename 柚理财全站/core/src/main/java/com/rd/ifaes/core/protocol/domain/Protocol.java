package com.rd.ifaes.core.protocol.domain;

import java.util.Date;

import com.rd.ifaes.common.entity.BaseEntity;
import com.rd.ifaes.common.util.DateUtils;
import com.rd.ifaes.core.protocol.model.ProtocolModel;

/**
 * 
 *  协议模板表
 * @version 3.0
 * @author jxx
 * @date 2016年8月1日
 */
public class Protocol extends BaseEntity<Protocol> {

	/** 
	* @Fields serialVersionUID : 
	*/ 
	private static final long serialVersionUID = 1L;
	
	/**
	 * 协议类型
	 */
	private String protocolType;
	
	/**
	 * 协议模板名称
	 */
	private String name;
	
	/**
	 * 协议模板内容
	 */
	private String content;
	
	/**
	 * 添加时间
	 */
	private Date createTime;
	
	/**
	 * 状态
	 */
	private String status;
	
	@Override
	public void preInsert(){
		super.preInsert();
		this.setCreateTime(DateUtils.getNow());
		this.setStatus(ProtocolModel.STATUS_VALID);
	}

	public String getProtocolType() {
		return protocolType;
	}

	public void setProtocolType(String protocolType) {
		this.protocolType = protocolType;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
