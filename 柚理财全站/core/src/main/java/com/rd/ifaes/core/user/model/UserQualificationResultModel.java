package com.rd.ifaes.core.user.model;

import java.io.Serializable;
/**
 * 借款详情页-平台认证信息：用户借款资质
 * 
 * @author  FangJun
 * @date 2016年7月25日
 */
public class UserQualificationResultModel implements Serializable{
 
	private static final long serialVersionUID = -3075449428411580325L;
	/** 资质类型 */ 
	private String	qualificationType;	
	/** 审核状态(待审核 0 ，审核成功 1 ，审核失败2 ，默认 0 ) */
	private String	status;		
	
	/**
	 * 空构造方法
	 */
	public UserQualificationResultModel() {
		super();
	}
	
	/**
	 * 构造方法
	 * @param qualificationType  资质类型
	 * @param status  审核状态
	 */
	public UserQualificationResultModel(final String qualificationType, final String status) {
		super();
		this.qualificationType = qualificationType;
		this.status = status;
	}

	/**
	 * 获取资质类型
	 * @return qualificationType
	 */
	public String getQualificationType() {
		return qualificationType;
	}

	/**
	 * 获取审核状态(待审核0，审核成功1，审核失败2，默认0)
	 * @return status
	 */
	public String getStatus() {
		return status;
	}

}
