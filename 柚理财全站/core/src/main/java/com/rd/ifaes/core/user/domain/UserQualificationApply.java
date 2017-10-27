package com.rd.ifaes.core.user.domain;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import com.rd.ifaes.common.dict.DictTypeEnum;
import com.rd.ifaes.common.entity.BaseEntity;
import com.rd.ifaes.core.core.util.DictUtils;

/**
 * entity:UserQualificationApply
 * 
 * @author FangJun
 * @version 3.0
 * @date 2016-7-21
 */
public class UserQualificationApply extends BaseEntity<UserQualificationApply> {
	
	private static final long serialVersionUID = 1L;
	
	/** 用户ID */ 
	private String	userId;		 
	/** 资质类型 */ 
	private String	qualificationType;	
	/** 添加时间 */ 
	private Date	createTime;		 
	/** 审核备注 */ 
	private String	verifyRemark;	
	/** 审核人 */ 
	private String	verifyUser;		 
	/** 申请状态(待审核 0 ，审核成功 1 ，审核失败2 ，默认 0 ) */
	private String	status;		  
	/** 审核时间 */
	private Date	verifyTime;	
	/** 申请IP */
	private String	addIp;		  
	
	//其他自定义属性
	/**用户名**/
	private String userName;
	/**用户类型**/
	private String userNature;
	/** 认证名称**/
	private String certificateName;
	
	/** 审核状态-待审核 */
	public static final String STATUS_AUDIT = "0"; 
	/** 审核状态-审核成功 */
	public static final String STATUS_SUCCESS = "1"; 
	/** 审核状态-审核失败*/
	public static final String STATUS_FAIL = "2";    

	/**
	 * 构造函数
	 */
	public UserQualificationApply() {
		super();
	}

	/**
	 * 构造函数
	 */
	public UserQualificationApply(final String userId, final String qualificationType) {
		super();
		this.userId = userId;
		this.qualificationType = qualificationType;
	}

	/**
	 * 构造函数
	 */
	public UserQualificationApply(final String uuid, final String userId, final String qualificationType, final Date createTime, 
			final String verifyRemark, final String verifyUser, final String status, final Date verifyTime, final String addIp) {
		super();
		setUuid(uuid);
		this.userId = userId;
		this.qualificationType = qualificationType;
		this.createTime = createTime;
		this.verifyRemark = verifyRemark;
		this.verifyUser = verifyUser;
		this.status = status;
		this.verifyTime = verifyTime;
		this.addIp = addIp;
	}

	/**
	 * 获取用户ID
	 * @return userId
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * 设置用户ID
	 * @param  userId
	 */
	public void setUserId(final String userId) {
		this.userId = userId;
	}

	/**
	 * 获取资质类型
	 * @return qualificationType
	 */
	public String getQualificationType() {
		return qualificationType;
	}

	/**
	 * 设置资质类型
	 * @param  qualificationType
	 */
	public void setQualificationType(final String qualificationType) {
		this.qualificationType = qualificationType;
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
	 * 获取审核备注
	 * @return verifyRemark
	 */
	public String getVerifyRemark() {
		return verifyRemark;
	}

	/**
	 * 设置审核备注
	 * @param  verifyRemark
	 */
	public void setVerifyRemark(final String verifyRemark) {
		this.verifyRemark = verifyRemark;
	}

	/**
	 * 获取审核人
	 * @return verifyUser
	 */
	public String getVerifyUser() {
		return verifyUser;
	}

	/**
	 * 设置审核人
	 * @param  verifyUser
	 */
	public void setVerifyUser(final String verifyUser) {
		this.verifyUser = verifyUser;
	}

	/**
	 * 获取申请状态(待审核0，审核成功1，审核失败2，默认0)
	 * @return status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * 设置申请状态(待审核0，审核成功1，审核失败2，默认0)
	 * @param  status
	 */
	public void setStatus(final String status) {
		this.status = status;
	}

	/**
	 * 获取审核时间
	 * @return verifyTime
	 */
	public Date getVerifyTime() {
		return verifyTime;
	}

	/**
	 * 设置审核时间
	 * @param  verifyTime
	 */
	public void setVerifyTime(final Date verifyTime) {
		this.verifyTime = verifyTime;
	}

	/**
	 * 获取申请IP
	 * @return addIp
	 */
	public String getAddIp() {
		return addIp;
	}

	/**
	 * 设置申请IP
	 * @param  addIp
	 */
	public void setAddIp(final String addIp) {
		this.addIp = addIp;
	}

	/**
	 * 获取其他自定义属性
	 * @return userName
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * 设置其他自定义属性
	 * @param  userName
	 */
	public void setUserName(final String userName) {
		this.userName = userName;
	}

	/**
	 * 获取用户类型
	 * @return userNature
	 */
	public String getUserNature() {
		return userNature;
	}

	/**
	 * 设置用户类型
	 * @param  userNature
	 */
	public void setUserNature(final String userNature) {
		this.userNature = userNature;
	}

	/**
	 * 获取认证名称
	 * @return certificateName
	 */
	public String getCertificateName() {
		return certificateName;
	}

	/**
	 * 设置认证名称
	 * @param  certificateName
	 */
	public void setCertificateName(final String certificateName) {
		this.certificateName = certificateName;
	}

	/**
	 * 获得证明资料类型显示值
	 * @return
	 */
	public String getQualificationTypeStr() {
		String qualificationTypeStr = "";
		if(StringUtils.isNotBlank(getUserNature())){
			if(UserCache.USER_NATURE_PERSON.equals(getUserNature())){
				qualificationTypeStr = DictUtils.getItemName(DictTypeEnum.QUALIFICATION_TYPE.getValue(), getQualificationType());
			}else{
				qualificationTypeStr = DictUtils.getItemName(DictTypeEnum.COMPANY_QUALIFICATION_TYPE.getValue(), getQualificationType());
			}
		}
		return qualificationTypeStr;
	}

	/**
	 * 重写toString()方法
	 */
	@Override
	public String toString() {
		return "UserQualificationApply [" + "uuid=" + uuid + ", userId=" + userId + ", qualificationType=" + qualificationType + ", createTime=" + createTime + ", verifyRemark=" + verifyRemark + ", verifyUser=" + verifyUser + ", status=" + status + ", verifyTime=" + verifyTime + ", addIp=" + addIp +  "]";
	}
}
