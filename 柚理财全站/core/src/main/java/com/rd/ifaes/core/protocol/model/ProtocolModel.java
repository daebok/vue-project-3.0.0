package com.rd.ifaes.core.protocol.model;

import org.springframework.beans.BeanUtils;

import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.core.protocol.domain.Protocol;

/**
 * 
 *  协议扩展类
 * @version 3.0
 * @author jxx
 * @date 2016年7月27日
 */
public class ProtocolModel extends Protocol {

	/** 
	* @Fields serialVersionUID : 
	*/ 
	private static final long serialVersionUID = 1L;
	
	public static final String STATUS_VALID = "1";
	
	public static final String STATUS_INVALID = "0";

	public static ProtocolModel instance(Protocol protocol) {
		ProtocolModel protocolModel = new ProtocolModel();
		BeanUtils.copyProperties(protocol, protocolModel);
		return protocolModel;
	}

	public Protocol prototype() {
		Protocol protocol = new Protocol();
		BeanUtils.copyProperties(this, protocol);
		return protocol;
	}
	
	/**
	 * 
	 * 添加修改验证
	 * @author jxx
	 * @date 2016年7月27日
	 * @return
	 */
	public String valid(){
		String str = null;
		if(StringUtils.isBlank(getProtocolType())){
			str = "类型不能为空";
		}
		if(StringUtils.isBlank(getName())){
			str = "名称不能为空";
		}
		if(StringUtils.isBlank(getContent())){
			str = "内容不能为空";
		}
		return str;
	}
}
