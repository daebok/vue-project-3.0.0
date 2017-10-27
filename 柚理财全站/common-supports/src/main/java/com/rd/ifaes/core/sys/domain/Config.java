package com.rd.ifaes.core.sys.domain;

import java.util.Date;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import com.rd.ifaes.common.annotation.DictType;
import com.rd.ifaes.common.entity.BaseEntity;
import com.rd.ifaes.common.exception.BussinessException;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.core.core.constant.Constant;
import com.rd.ifaes.core.core.constant.ResourceConstant;
import com.rd.ifaes.core.core.util.ResourceUtils;

/**
 * entity:Config
 * 
 * @author lh
 * @version 3.0
 * @date 2016-5-17
 */
public class Config extends BaseEntity<Config> {
	
	private static final long serialVersionUID = 1L;
	
	@Length(min=5,max=50,message="{sys.config.code.lengthError}")
	private String	code;		 /* 配置项标识 */ 
	@Length(min=2,max=50,message="{sys.config.configName.lengthError}")
	private String	configName;		 /* 配置项名称 */ 
	@NotEmpty(message="{sys.config.configValue.emptyMsg}")
	private String	configValue;		 /* 值 */ 
	@DictType(type="configType")
	private String	configType;		 /* 类型 */ 
	private Date	createTime;		 /* 添加时间 */ 
	@DictType(type="enable")
	private String	status;		 /* 启用状态：0 未启用 1 启用 */ 
	@Length(max=512,message="{sys.config.configName.remark}")
	private String	remark;		 /* 备注 */ 
	private boolean editEnable;  /*是否可编辑*/
	
	// Constructor
	public Config() {
	}
	
	public Config(String id, String code){
		setUuid(id);
		this.code = code;
	}

	

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getConfigName() {
		return configName;
	}

	public void setConfigName(String configName) {
		this.configName = configName;
	}

	public String getConfigValue() {
		return configValue;
	}

	public void setConfigValue(String configValue) {
		this.configValue = configValue;
	}

	public String getConfigType() {
		return configType;
	}

	public void setConfigType(String configType) {
		this.configType = configType;
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

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	public boolean isEditEnable() {
		return editEnable;
	}

	public void setEditEnable(boolean editEnable) {
		this.editEnable = editEnable;
	}

	public void checkConfig(){
		if (StringUtils.isBlank(this.getConfigValue())
				|| this.getConfigValue().length() > Constant.INT_ONE_HUNDRED_TWENY_EIGHT) {
			throw new BussinessException(
					ResourceUtils.get(ResourceConstant.CONFIG_VALUE_MAX_LENTH, Constant.INT_ONE_HUNDRED_TWENY_EIGHT),
					BussinessException.TYPE_JSON);
		}
	}
	
	@Override
	public void preInsert() {
		super.preInsert();
		this.createTime = new Date();
		this.status = StringUtils.isBlank(this.status)?(DictData.DEFAULT_STATUS_ENABLE+""):this.status;
	}

	@Override
	public String toString() {
		return "Config [uuid="+ getUuid()+", code=" + code + ", configName=" + configName + ", configValue=" + configValue + ", configType="
				+ configType + ", createTime=" + createTime + ", status=" + status + ", remark=" + remark + "]";
	}
	
}
