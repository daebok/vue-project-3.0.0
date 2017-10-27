package com.rd.ifaes.core.sys.domain;

import java.util.Date;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import com.rd.ifaes.common.entity.BaseEntity;

/**
 * entity:LogTemplate
 * 
 * @author lh
 * @version 3.0
 * @date 2016-5-18
 */
public class LogTemplate extends BaseEntity<LogTemplate> {
	
	private static final long serialVersionUID = 1L;
	
	/** 日志类型:1资金日志，2积分日志 */ 
	@NotNull 
	private Integer	logType;
	/** 模板标识 */ 
	@NotBlank
	private String	code;	
	/** 模板名称 */ 
	@NotBlank
	private String	templateName;    
	/** 模板信息 */ 
	@NotBlank
	private String	templateContent;  
	/** 删除标识 0未删除，1 已删除 */ 
	private String	deleteFlag="0";	 
	/** 添加时间 */ 
	private Date	createTime;	
	/** 备注 */ 
	private String	remark;		       

	//其他自定义属性
	/** 资金日志 **/
	public static final int LOG_TYPE_ACCOUNT = 1;
	/** 积分日志 **/
	public static final int LOG_TYPE_SCORE = 2;

	/**
	 * 空构造函数
	 */
	public LogTemplate() {
		super();
	}

	/**
	 * full Constructor
	 */
	public LogTemplate(final String uuid, final Integer logType,final String code,final String templateName, final String templateContent, final String deleteFlag, final Date createTime, final String remark) {
		super();
		setUuid(uuid);
		this.logType = logType;
		this.code = code;
		this.templateName = templateName;
		this.templateContent = templateContent;
		this.deleteFlag = deleteFlag;
		this.createTime = createTime;
		this.remark = remark;
	}

	/**
	 * 获取日志类型:1资金日志，2积分日志
	 * @return logType
	 */
	public Integer getLogType() {
		return logType;
	}

	/**
	 * 设置日志类型:1资金日志，2积分日志
	 * @param  logType
	 */
	public void setLogType(final Integer logType) {
		this.logType = logType;
	}

	/**
	 * 获取模板标识
	 * @return code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * 设置模板标识
	 * @param  code
	 */
	public void setCode(final String code) {
		this.code = code;
	}

	/**
	 * 获取模板名称
	 * @return templateName
	 */
	public String getTemplateName() {
		return templateName;
	}

	/**
	 * 设置模板名称
	 * @param  templateName
	 */
	public void setTemplateName(final String templateName) {
		this.templateName = templateName;
	}

	/**
	 * 获取模板信息
	 * @return templateContent
	 */
	public String getTemplateContent() {
		return templateContent;
	}

	/**
	 * 设置模板信息
	 * @param  templateContent
	 */
	public void setTemplateContent(final String templateContent) {
		this.templateContent = templateContent;
	}

	/**
	 * 获取删除标识0未删除，1已删除
	 * @return deleteFlag
	 */
	public String getDeleteFlag() {
		return deleteFlag;
	}

	/**
	 * 设置删除标识0未删除，1已删除
	 * @param  deleteFlag
	 */
	public void setDeleteFlag(final String deleteFlag) {
		this.deleteFlag = deleteFlag;
	}

	/**
	 * 获取添加时间
	 * @return createTime
	 */
	public Date getCreateTime() {
		return createTime;
	}

	/**
	 * 设置添加时间
	 * @param  createTime
	 */
	public void setCreateTime(final Date createTime) {
		this.createTime = createTime;
	}

	/**
	 * 获取备注
	 * @return remark
	 */
	public String getRemark() {
		return remark;
	}

	/**
	 * 设置备注
	 * @param  remark
	 */
	public void setRemark(final String remark) {
		this.remark = remark;
	}

	/**
	 * 重写toString() 
	 */
	@Override
	public String toString() {
		return "LogTemplate [" + "uuid=" + uuid + ", logType=" + logType + ", code=" + code + ", templateName=" + templateName + ", templateContent=" + templateContent + ", deleteFlag=" + deleteFlag + ", createTime=" + createTime + ", remark=" + remark +  "]";
	}

	/**
	 * 重写preInsert()
	 */
	@Override
	public void preInsert() {
		super.preInsert();
		this.createTime = new Date();
	}
	
	
}
