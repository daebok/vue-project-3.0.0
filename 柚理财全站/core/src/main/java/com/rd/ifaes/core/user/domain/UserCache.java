package com.rd.ifaes.core.user.domain;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;

import com.rd.ifaes.common.entity.BaseEntity;
import com.rd.ifaes.common.util.SpringContextHolder;
import com.rd.ifaes.core.sys.service.AreaService;
import com.rd.ifaes.core.user.model.UserCacheModel;

/**
 * entity:UserCache
 * 
 * @author xhf
 * @version 3.0
 * @date 2016-6-15
 */
public class UserCache extends BaseEntity<UserCache> {
	
	private static final long serialVersionUID = 1L;
	/** 用户ID */ 
	private String	userId;		 
	/** 用户类型:  1个人用户、2 企业用户、3 担保机构 */ 
	private String	userNature;		 
	/** 证件类型，01 身份证18位，02 身份证15位；默认1 */ 
	private String	certType;	
	/** 证件号码 */ 
	private String	idNo;		
	/** 性别:M 男性，F女性 */
	private String	sex;	
	/** 邮编 */ 
	private String	postCode;	
	/** 省 */
	private String	province;	
	/** 市 */ 
	private String	city;		
	/** 区 */
	private String	area;		 
	/** 地址 */
	private String	address;		  
	/** 风险评估等级(数字等级) */ 
	private String	riskLevel;	
	/** 头像照片路径 */ 
	private String	avatarPhoto;	
	/** 登录失败次数 */
	private Integer	loginFailTimes;		
	/** 锁定类型  0-登录失败锁定 1-后台手动锁定 */ 
	private String lockType;	
	/** 锁定时间 */ 
	private Date lockTime;		
	/** 锁定备注 */ 
	private String lockRemark;		
	/** 用户名修改标识（默认0，修改过默认生成用户名置为1） */ 
	private String renameFlag;		
	/** 签名认证账号 */ 
	private String signAccount;	
	/** 签名电子印章数据 */ 
	private String	signSealData;		
	/** 用户注册方式 0 用户注册 1 后台录入 */ 
	private String regMode;	
	/** 用户投资记录数（在投、成功）*/
	private Integer investNum;
	/** 是否需要修改密码0.不需要1.需要*/
	private String resetPwdFlag;
	/** 首投奖励（0未发放，1已发放,默认0）*/
	private String firstAwardFlag;
	
	//其他自定义属性
    /** 个人用户 */
	public static final String USER_NATURE_PERSON = "1"; 
	/** 企业用户 */
	public static final String USER_NATURE_COMPANY = "2";
	/** 担保机构 */
	public static final String USER_NATURE_VOUCH = "3";   
	
	/**
	 *  空构造函数
	 */
	public UserCache() {
		super();
	}

	/**
	 * full Constructor
	 */
	public UserCache(final String userId, final String userNature, final String certType,
			final String idNo, final String sex, final String postCode, final String province,
			final String city, final String area, final String address, final String riskLevel,
			final String avatarPhoto, final Integer loginFailTimes, final Date lockTime,
			final String lockRemark, final String renameFlag, final String signAccount,
			final String signSealData) {
		super();
		this.userId = userId;
		this.userNature = userNature;
		this.certType = certType;
		this.idNo = idNo;
		this.sex = sex;
		this.postCode = postCode;
		this.province = province;
		this.city = city;
		this.area = area;
		this.address = address;
		this.riskLevel = riskLevel;
		this.avatarPhoto = avatarPhoto;
		this.loginFailTimes = loginFailTimes;
		this.lockTime = lockTime;
		this.lockRemark = lockRemark;
		this.renameFlag = renameFlag;
		this.signAccount = signAccount;
		this.signSealData = signSealData;
	}
	
	/**
	 * 复制本身属性给Model
	 * @author QianPengZhan
	 * @date 2016年8月29日
	 * @return
	 */
	public UserCacheModel prototype() {
		UserCacheModel userCacheModel = new UserCacheModel();
		BeanUtils.copyProperties(this, userCacheModel);
		return userCacheModel;
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
	 * 获取用户类型:1个人用户、2企业用户、3担保机构
	 * @return userNature
	 */
	public String getUserNature() {
		return userNature;
	}

	/**
	 * 设置用户类型:1个人用户、2企业用户、3担保机构
	 * @param  userNature
	 */
	public void setUserNature(final String userNature) {
		this.userNature = userNature;
	}

	/**
	 * 获取证件类型，01身份证18位，02身份证15位；默认1
	 * @return certType
	 */
	public String getCertType() {
		return certType;
	}

	/**
	 * 设置证件类型，01身份证18位，02身份证15位；默认1
	 * @param  certType
	 */
	public void setCertType(final String certType) {
		this.certType = certType;
	}

	/**
	 * 获取证件号码
	 * @return idNo
	 */
	public String getIdNo() {
		return idNo;
	}

	/**
	 * 设置证件号码
	 * @param  idNo
	 */
	public void setIdNo(final String idNo) {
		this.idNo = idNo;
	}

	/**
	 * 获取性别:M男性，F女性
	 * @return sex
	 */
	public String getSex() {
		return sex;
	}

	/**
	 * 设置性别:M男性，F女性
	 * @param  sex
	 */
	public void setSex(final String sex) {
		this.sex = sex;
	}

	/**
	 * 获取邮编
	 * @return postCode
	 */
	public String getPostCode() {
		return postCode;
	}

	/**
	 * 设置邮编
	 * @param  postCode
	 */
	public void setPostCode(final String postCode) {
		this.postCode = postCode;
	}

	/**
	 * 获取省
	 * @return province
	 */
	public String getProvince() {
		return province;
	}

	/**
	 * 设置省
	 * @param  province
	 */
	public void setProvince(final String province) {
		this.province = province;
	}

	/**
	 * 获取市
	 * @return city
	 */
	public String getCity() {
		return city;
	}

	/**
	 * 设置市
	 * @param  city
	 */
	public void setCity(final String city) {
		this.city = city;
	}

	/**
	 * 获取区
	 * @return area
	 */
	public String getArea() {
		return area;
	}

	/**
	 * 设置区
	 * @param  area
	 */
	public void setArea(final String area) {
		this.area = area;
	}

	/**
	 * 获取地址
	 * @return address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * 设置地址
	 * @param  address
	 */
	public void setAddress(final String address) {
		this.address = address;
	}

	/**
	 * 获取风险评估等级(数字等级)
	 * @return riskLevel
	 */
	public String getRiskLevel() {
		return riskLevel;
	}

	/**
	 * 设置风险评估等级(数字等级)
	 * @param  riskLevel
	 */
	public void setRiskLevel(final String riskLevel) {
		this.riskLevel = riskLevel;
	}

	/**
	 * 获取头像照片路径
	 * @return avatarPhoto
	 */
	public String getAvatarPhoto() {
		return avatarPhoto;
	}

	/**
	 * 设置头像照片路径
	 * @param  avatarPhoto
	 */
	public void setAvatarPhoto(final String avatarPhoto) {
		this.avatarPhoto = avatarPhoto;
	}

	/**
	 * 获取登录失败次数
	 * @return loginFailTimes
	 */
	public Integer getLoginFailTimes() {
		return loginFailTimes;
	}

	/**
	 * 设置登录失败次数
	 * @param  loginFailTimes
	 */
	public void setLoginFailTimes(final Integer loginFailTimes) {
		this.loginFailTimes = loginFailTimes;
	}

	/**
	 * 获取锁定时间
	 * @return lockTime
	 */
	public Date getLockTime() {
		return lockTime;
	}

	/**
	 * 设置锁定时间
	 * @param  lockTime
	 */
	public void setLockTime(final Date lockTime) {
		this.lockTime = lockTime;
	}

	/**
	 * 获取锁定备注
	 * @return lockRemark
	 */
	public String getLockRemark() {
		return lockRemark;
	}

	/**
	 * 设置锁定备注
	 * @param  lockRemark
	 */
	public void setLockRemark(final String lockRemark) {
		this.lockRemark = lockRemark;
	}

	/**
	 * 获取用户名修改标识（默认0，修改过默认生成用户名置为1）
	 * @return renameFlag
	 */
	public String getRenameFlag() {
		return renameFlag;
	}

	/**
	 * 设置用户名修改标识（默认0，修改过默认生成用户名置为1）
	 * @param  renameFlag
	 */
	public void setRenameFlag(final String renameFlag) {
		this.renameFlag = renameFlag;
	}

	/**
	 * 获取签名认证账号
	 * @return signAccount
	 */
	public String getSignAccount() {
		return signAccount;
	}

	/**
	 * 设置签名认证账号
	 * @param  signAccount
	 */
	public void setSignAccount(final String signAccount) {
		this.signAccount = signAccount;
	}

	/**
	 * 获取签名电子印章数据
	 * @return signSealData
	 */
	public String getSignSealData() {
		return signSealData;
	}

	/**
	 * 设置签名电子印章数据
	 * @param  signSealData
	 */
	public void setSignSealData(final String signSealData) {
		this.signSealData = signSealData;
	}

	/**
	 * 获取用户注册方式0用户注册1后台录入
	 * @return regMode
	 */
	public String getRegMode() {
		return regMode;
	}

	/**
	 * 设置用户注册方式   0用户注册1后台录入
	 * @param  regMode
	 */
	public void setRegMode(final String regMode) {
		this.regMode = regMode;
	}

	/**
	 * 获取用户投资记录数（在投、成功）
	 * @return investNum
	 */
	public Integer getInvestNum() {
		return investNum==null?0:investNum;
	}
 

	/**
	 * 省显示值
	 * @return
	 */
	public String getProvinceStr() {
		String provinceStr = "";
		if(StringUtils.isNotBlank(getProvince())){
			final AreaService areaService = SpringContextHolder.getBean("areaService");
			provinceStr = areaService.getNameByCode(getProvince());
		}
		return provinceStr;
	}
	
	/**
	 * 市显示值
	 * @return
	 */
	public String getCityStr() {
		String cityStr = "";
		if(StringUtils.isNotBlank(getProvince())){
			final AreaService areaService = SpringContextHolder.getBean("areaService");
			cityStr = areaService.getNameByCode(getCity());
		}
		return cityStr;
	}

	/**
	 * 区显示值
	 * @return
	 */
	public String getAreaStr() {
		String areaStr = "";
		if(StringUtils.isNotBlank(getProvince())){
			final AreaService areaService = SpringContextHolder.getBean("areaService");
			areaStr = areaService.getNameByCode(getArea());
		}
		return areaStr;
	}

	public String getResetPwdFlag() {
		return resetPwdFlag;
	}

	public void setResetPwdFlag(String resetPwdFlag) {
		this.resetPwdFlag = resetPwdFlag;
	}
	/**
	 * 获取属性firstAwardFlag的值
	 * @return firstAwardFlag属性值
	 */
	public String getFirstAwardFlag() {
		return firstAwardFlag;
	}

	/**
	 * 设置属性firstAwardFlag的值
	 * @param  firstAwardFlag属性值
	 */
	public void setFirstAwardFlag(String firstAwardFlag) {
		this.firstAwardFlag = firstAwardFlag;
	}
	
	/**
	 * @return 锁定类型 0-登录失败锁定 1-后台手动锁定
	 */
	public String getLockType() {
		return lockType;
	}

	/**
	 * @param 锁定类型 0-登录失败锁定 1-后台手动锁定 
	 */
	public void setLockType(String lockType) {
		this.lockType = lockType;
	}

	/**
	 * 重写toString()方法
	 */
	@Override
	public String toString() {
		return "UserCache [userId=" + userId + ", userNature=" + userNature
				+ ", certType=" + certType + ", idNo=" + idNo + ", sex=" + sex
				+ ", postCode=" + postCode + ", province=" + province
				+ ", city=" + city + ", area=" + area + ", address=" + address
				+ ", riskLevel=" + riskLevel + ", avatarPhoto=" + avatarPhoto
				+ ", loginFailTimes=" + loginFailTimes + ", lockTime="
				+ lockTime + ", lockRemark=" + lockRemark + ", renameFlag="
				+ renameFlag + ", signAccount=" + signAccount
				+ ", signSealData=" + signSealData + "]";
	}
}
