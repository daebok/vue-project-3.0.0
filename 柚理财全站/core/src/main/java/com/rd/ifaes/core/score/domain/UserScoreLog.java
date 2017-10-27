package com.rd.ifaes.core.score.domain;

import java.util.Date;

import com.rd.ifaes.common.annotation.DictType;
import com.rd.ifaes.common.entity.BaseEntity;

/**
 * entity:UserScoreLog
 * 
 * @author 
 * @version 3.0
 * @date 2016-8-4
 */
public class UserScoreLog extends BaseEntity<UserScoreLog> {
		
	/**序列号*/
	private static final long serialVersionUID = -8507562642794020596L;
	/** 用户id*/
	private String	userId;		
	/** 总积分*/
	private Integer	totalScore;		
	/** 可用积分*/
	private Integer	useScore;		
	/** 操作积分*/
	private Integer	optValue;		
	/** 冻结积分*/
	private Integer	noUseScore;		
	/** 操作类型（0减少，1增加）*/
	@DictType(type="optType")
	private String	optType;		
	/** 积分类型名称 */
	private String	typeName;		
	/** 积分类型标识  */
	private String	typeCode;		
	/** 备注  */
	private String	remark;	
	/** 创建日期  */
	private Date	createTime;		

	//其他自定义属性
	/** 用户账号  */
	private String userName;
	/** 手机号码  */
	private String mobile; 
	/** 真实姓名  */
	private String realName;
	/** 创建开始日期  */
	private String createTimeBegin;
	/** 创建结束日期  */
	private String createTimeEnd;
	
	/** Constructor  */
	public UserScoreLog() {
		super();
	}

	/**
	 * full Constructor
	 */
	public UserScoreLog(final String uuid,final String userId, final Integer totalScore, final Integer useScore, final Integer optValue, final Integer noUseScore, final String optType,final  String typeName, final String typeCode,
			final String remark,final  Date createTime) {
		super();
		setUuid(uuid);
		this.userId = userId;
		this.totalScore = totalScore;
		this.useScore = useScore;
		this.optValue = optValue;
		this.noUseScore = noUseScore;
		this.optType = optType;
		this.typeName = typeName;
		this.typeCode = typeCode;
		this.remark = remark;
		this.createTime = createTime;
	}
	/** 获取用户id  */
	public String getUserId() {
		return userId;
	}
	/** 设置用户id  */
	public void setUserId(final String userId) {
		this.userId = userId;
	}
	/** 获取总的积分  */
	public Integer getTotalScore() {
		return totalScore;
	}
	/** 设置总的积分  */
	public void setTotalScore(final Integer totalScore) {
		this.totalScore = totalScore;
	}
	/** 获取可用积分  */
	public Integer getUseScore() {
		return useScore;
	}
	/** 设置可用积分  */
	public void setUseScore(final Integer useScore) {
		this.useScore = useScore;
	}
	/** 获取操作积分  */
	public Integer getOptValue() {
		return optValue;
	}
	/** 设置操作积分  */
	public void setOptValue(final Integer optValue) {
		this.optValue = optValue;
	}
	/** 获取冻结积分  */
	public Integer getNoUseScore() {
		return noUseScore;
	}
	/** 设置冻结积分  */
	public void setNoUseScore(final Integer noUseScore) {
		this.noUseScore = noUseScore;
	}
	/** 获取操作类型  */
	public String getOptType() {
		return optType;
	}
	/** 设置操作类型  */
	public void setOptType(final String optType) {
		this.optType = optType;
	}
	/** 获取积分类型名称  */
	public String getTypeName() {
		return typeName;
	}
	/** 设置积分类型名称  */
	public void setTypeName(final String typeName) {
		this.typeName = typeName;
	}
	/** 获取积分类型标识  */
	public String getTypeCode() {
		return typeCode;
	}
	/** 设置积分类型标识  */
	public void setTypeCode(final String typeCode) {
		this.typeCode = typeCode;
	}
	/** 获取备注  */
	public String getRemark() {
		return remark;
	}
	/** 设置备注  */
	public void setRemark(final String remark) {
		this.remark = remark;
	}
	/** 获取创建日期  */
	public Date getCreateTime() {
		return createTime;
	}
	/** 设置创建日期  */
	public void setCreateTime(final Date createTime) {
		this.createTime = createTime;
	}
	/** 获取用户名  */
	public String getUserName() {
		return userName;
	}
	/** 设置用户名  */
	public void setUserName(final String userName) {
		this.userName = userName;
	}
	/** 获取手机号  */
	public String getMobile() {
		return mobile;
	}
	/** 设置手机号  */
	public void setMobile(final String mobile) {
		this.mobile = mobile;
	}
	/** 获取真实姓名  */
	public String getRealName() {
		return realName;
	}
	/** 设置真实姓名  */
	public void setRealName(final String realName) {
		this.realName = realName;
	}
	
	public String getCreateTimeBegin() {
		return createTimeBegin;
	}

	public void setCreateTimeBegin(String createTimeBegin) {
		this.createTimeBegin = createTimeBegin;
	}

	public String getCreateTimeEnd() {
		return createTimeEnd;
	}

	public void setCreateTimeEnd(String createTimeEnd) {
		this.createTimeEnd = createTimeEnd;
	}

	/** toString  */
	@Override
	public String toString() {
		return "UserScoreLog [" + "uuid=" + uuid + ", userId=" + userId + ", totalScore=" + totalScore + ", useScore=" + useScore + ", optValue=" + optValue + ", noUseScore=" + noUseScore + ", optType=" + optType + ", typeName=" + typeName + ", typeCode=" + typeCode + ", remark=" + remark + ", createTime=" + createTime +  "]";
	}
}
