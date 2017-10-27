package com.rd.ifaes.core.user.domain;

import java.math.BigDecimal;
import java.util.Date;

import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Email;

import com.rd.ifaes.common.annotation.DictType;
import com.rd.ifaes.common.dict.DictTypeEnum;
import com.rd.ifaes.common.entity.BaseEntity;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.core.core.constant.ResourceConstant;
import com.rd.ifaes.core.core.util.DictUtils;

/**
 * entity:User
 * 
 * @author xhf
 * @version 3.0
 * @date 2016-6-15
 */
public class User extends BaseEntity<User> {
	
	private static final long serialVersionUID = 1L;
	/** 用户唯一标识 */ 
	private String	userNo;		
	/** 登录用户名 */ 
	@Pattern(regexp = "^(?![0-9]+$)[0-9A-Za-z]{6,20}$", message="{"+ResourceConstant.USER_NAME_ERROR+"}")
	private String	userName;	
	/** 登陆密码 */ 
	//@Pattern(regexp = "(?![^a-zA-Z]+$)(?!\\D+$)^(?!.*\\s).{8,16}", message="{user.pwd.error}")
	private String	pwd;	
	/** 姓名 */ 
	@Pattern(regexp = "^[\u4e00-\u9fa5]{2,10}$", message="{user.realName.error}")
	private String	realName;	
	/** 手机号 */ 
	@Pattern(regexp = "^((13[0-9])|(15[0-9])|(18[0-9])|145|147|170|171|173|175|176|177|178)\\d{8}$", message="{"+ResourceConstant.USER_MOBILE_ERROR+"}")
	private String	mobile;		
	/** 邮箱 */ 
	@Email(message = "{"+ResourceConstant.EMAIL_FORMAT_ERROR+"}")	
	private String	email;		
	/** 用户状态:(-1 锁定,0 正常,默认0) */ 
	private String	status;		
	/** 添加时间 */ 
	private Date	createTime;	
	/** 交易密码 */ 
	private String	payPwd;		
	/** 第三方账号是否激活 1 激活， 0未激活 */
	private String	tppStatus;		
	/** 用户资金存管平台账户号(汇付天下账号如：xxx_2016051150021 ) */ 
	private String	tppUserAccId;	
	/** 用户资金存管平台资金账户(uid) */ 
	private String	tppUserCustId;		 
	/** 用户类型:  1个人用户、2 企业用户、3 担保机构 **/
	@DictType(type="userType")
	private String userNature;
	/*用户账号类型*/
	@DictType(type="userAccountType")
	private String accountType;
	/*注册渠道，用户前台注册自定义内容*/
	private String registerChannel;
	//其他自定义属性
	/** 用户状态-正常*/
	public static final String USER_STATUS_NORMAL = "0";
	/** 用户状态-锁定*/
	public static final String USER_STATUS_LOCKED = "-1";
	//柚账户
	public static final String ACCOUNT_TYPE_YOU = "1";
	//京账户
	public static final String ACCOUNT_TYPE_JING = "2";
	//用户未登录是用这个替代，部分代码有用到，无其他意义
	public static final String ACCOUNT_TYPE_NULL = "3";
	/**
	 * 是否存量用户 0否 1是
	 */
	private String isStock;
	
	/**是否担保机构,供查询使用*/
	private String isVouch;	
	
	/** 是否是担保机构-是*/
	public static final String USER_NATURE_VOUCH = "1";
	/** 是否是担保机构-否*/
	public static final String USER_NATURE_NOT_VOUCH = "0";
	/**查询方式*/
	private String queryStyle;
	public static final String QUERY_STYLE_AUTO_COMPLETE = "1";     //自动补全
	
	/** 实名认证状态 **/
	private String realNameStatus;
	
	/** 授权认证状态 **/
	private String authSignStatus;
	
	/** 性别**/ 
	private String sex;
	
	/** 身份证号**/ 
	private String idNo; 
	
	/** 营业执照 **/
	private String bussinessCode;
	
	/** 组织机构代码 **/ 
	private String	orgCode;
	
	/** 统一社会信用代码 */ 
	private String	creditCode;	
	
	/** 公司名称 **/ 
	private String companyName;
	
	/** 联系人 **/ 
	private String contacts;
	
	/** 企业联系固定电话 **/ 
	private String telephone;	
	/** 税务登记号 **/ 
	private String taxRegNo;
	/** 企业地址 **/ 
	private String address;
	/** 企业logo **/ 
	private String logo;
	
	/** 登录IP **/ 
	private String ip;
	/** 查询的userid **/ 
	private String[] uuids;
	
	/** 账户总额 **/ 
	private BigDecimal	total;	
	/** 可用余额 **/ 
	private BigDecimal	useMoney;
	/** 冻结金额 **/ 
	private BigDecimal	noUseMoney;
	/* >=0，提现收取手续费标记（如果大于0说明需要手续费） */
	private BigDecimal cashFeeMark;
	private int advanceCount;//已减免手续费（次/月）
	/**排除某个功能冻结的用户，仅用于查询**/
	private String exceptFreeze;
	/**sessionId，用于登录次数校验**/
	private String sessionId;
	/**邀请人名字**/
	private String inviteName;
	/**地区所在地，只到市一级**/
	private String provinceCity;
	/**注册渠道**/
	private String registChannel;
	/**待收金额*/
	private BigDecimal collectionMoney;
	/**待收金额*/
	private BigDecimal paymentMoney;
	/**投资次数**/
	private int investNum;
	/**是否投过资**/
	private String investStatus;
	/**借款次数**/
	private int borrowNum;
	/**是否借过款**/
	private String borrowStatus;
	/**
	 * 构造函数
	 */
	public User() {
		super();
	}

	/**
	 * 构造函数
	 */
	public User(final String uuid) {
		super();
		this.uuid=uuid;
	}
	
	/**
	 * 构造函数
	 */
	public User(final String uuid, final String userName, final String mobile, final String email) {
		super();
		this.uuid=uuid;
		this.userName=userName;
		this.mobile=mobile;
		this.email=email;
	}
	/**
	 * 获取邀请人名称
	 * @return inviteName
	 */
	public String getInviteName() {
		return inviteName;
	}
	/**
	 * 设置邀请人名称
	 * @param inviteName
	 */
	public void setInviteName(String inviteName) {
		this.inviteName = inviteName;
	}
	
	/**
	 * 获取地区，只到市一级
	 * @return provinceCity
	 */
	public String getProvinceCity() {
		return provinceCity;
	}
	/**
	 * 设置地区，只到市一级
	 * @param provinceCity
	 */
	public void setProvinceCity(String provinceCity) {
		this.provinceCity = provinceCity;
	}
	/**
	 * 获取用户唯一标识
	 * @return userNo
	 */
	public String getUserNo() {
		return userNo;
	}

	/**
	 * 设置用户唯一标识
	 * @param  userNo
	 */
	public void setUserNo(final String userNo) {
		this.userNo = userNo;
	}

	/**
	 * 获取登录用户名
	 * @return userName
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * 设置登录用户名
	 * @param  userName
	 */
	public void setUserName(final String userName) {
		this.userName = userName;
	}

	/**
	 * 获取登陆密码
	 * @return pwd
	 */
	public String getPwd() {
		return pwd;
	}

	/**
	 * 设置登陆密码
	 * @param  pwd
	 */
	public void setPwd(final String pwd) {
		this.pwd = pwd;
	}

	/**
	 * 获取姓名
	 * @return realName
	 */
	public String getRealName() {
		return realName;
	}

	/**
	 * 设置姓名
	 * @param  realName
	 */
	public void setRealName(final String realName) {
		this.realName = realName;
	}

	/**
	 * 获取手机号
	 * @return mobile
	 */
	public String getMobile() {
		return mobile;
	}

	/**
	 * 设置手机号
	 * @param  mobile
	 */
	public void setMobile(final String mobile) {
		this.mobile = mobile;
	}

	/**
	 * 获取邮箱
	 * @return email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * 设置邮箱
	 * @param  email
	 */
	public void setEmail(final String email) {
		this.email = email;
	}

	/**
	 * 获取用户状态:(-1锁定0正常默认0)
	 * @return status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * 设置用户状态:(-1锁定0正常默认0)
	 * @param  status
	 */
	public void setStatus(final String status) {
		this.status = status;
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

	public String getAuthSignStatus() {
		return authSignStatus;
	}

	public void setAuthSignStatus(String authSignStatus) {
		this.authSignStatus = authSignStatus;
	}

	/**
	 * 获取交易密码
	 * @return payPwd
	 */
	public String getPayPwd() {
		return payPwd;
	}

	/**
	 * 设置交易密码
	 * @param  payPwd
	 */
	public void setPayPwd(final String payPwd) {
		this.payPwd = payPwd;
	}

	/**
	 * 获取第三方账号是否激活1激活，0未激活
	 * @return tppStatus
	 */
	public String getTppStatus() {
		return tppStatus;
	}

	/**
	 * 设置第三方账号是否激活1激活，0未激活
	 * @param  tppStatus
	 */
	public void setTppStatus(final String tppStatus) {
		this.tppStatus = tppStatus;
	}

	/**
	 * 获取用户资金存管平台账户号(汇付天下账号如：xxx_2016051150021)
	 * @return tppUserAccId
	 */
	public String getTppUserAccId() {
		return tppUserAccId;
	}

	/**
	 * 设置用户资金存管平台账户号(汇付天下账号如：xxx_2016051150021)
	 * @param  tppUserAccId
	 */
	public void setTppUserAccId(final String tppUserAccId) {
		this.tppUserAccId = tppUserAccId;
	}

	/**
	 * 获取用户资金存管平台资金账户(uid)
	 * @return tppUserCustId
	 */
	public String getTppUserCustId() {
		return tppUserCustId;
	}

	/**
	 * 设置用户资金存管平台资金账户(uid)
	 * @param  tppUserCustId
	 */
	public void setTppUserCustId(final String tppUserCustId) {
		this.tppUserCustId = tppUserCustId;
	}

	/**
	 * 获取是否担保机构供查询使用
	 * @return isVouch
	 */
	public String getIsVouch() {
		return isVouch;
	}

	/**
	 * 设置是否担保机构供查询使用
	 * @param  isVouch
	 */
	public void setIsVouch(final String isVouch) {
		this.isVouch = isVouch;
	}

	/**
	 * 获取查询方式
	 * @return queryStyle
	 */
	public String getQueryStyle() {
		return queryStyle;
	}

	/**
	 * 设置查询方式
	 * @param  queryStyle
	 */
	public void setQueryStyle(final String queryStyle) {
		this.queryStyle = queryStyle;
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
	 * 获取实名认证状态
	 * @return realNameStatus
	 */
	public String getRealNameStatus() {
		return realNameStatus;
	}

	/**
	 * 设置实名认证状态
	 * @param  realNameStatus
	 */
	public void setRealNameStatus(final String realNameStatus) {
		this.realNameStatus = realNameStatus;
	}

	/**
	 * 获取性别
	 * @return sex
	 */
	public String getSex() {
		return sex;
	}

	/**
	 * 设置性别
	 * @param  sex
	 */
	public void setSex(final String sex) {
		this.sex = sex;
	}

	/**
	 * 获取身份证号
	 * @return idNo
	 */
	public String getIdNo() {
		return idNo;
	}

	/**
	 * 设置身份证号
	 * @param  idNo
	 */
	public void setIdNo(final String idNo) {
		this.idNo = idNo;
	}

	/**
	 * 获取营业执照
	 * @return bussinessCode
	 */
	public String getBussinessCode() {
		return bussinessCode;
	}

	/**
	 * 设置营业执照
	 * @param  bussinessCode
	 */
	public void setBussinessCode(final String bussinessCode) {
		this.bussinessCode = bussinessCode;
	}

	/**
	 * 获取组织机构代码
	 * @return orgCode
	 */
	public String getOrgCode() {
		return orgCode;
	}

	/**
	 * 设置组织机构代码
	 * @param  orgCode
	 */
	public void setOrgCode(final String orgCode) {
		this.orgCode = orgCode;
	}

	/**
	 * 获取公司名称
	 * @return companyName
	 */
	public String getCompanyName() {
		return companyName;
	}

	/**
	 * 设置公司名称
	 * @param  companyName
	 */
	public void setCompanyName(final String companyName) {
		this.companyName = companyName;
	}

	/**
	 * 获取联系人
	 * @return contacts
	 */
	public String getContacts() {
		return contacts;
	}

	/**
	 * 设置联系人
	 * @param  contacts
	 */
	public void setContacts(final String contacts) {
		this.contacts = contacts;
	}

	/**
	 * 获取联系人电话
	 * @return telephone
	 */
	public String getTelephone() {
		return telephone;
	}

	/**
	 * 设置联系人电话
	 * @param  telephone
	 */
	public void setTelephone(final String telephone) {
		this.telephone = telephone;
	}

	/**
	 * 获取税务登记号
	 * @return taxRegNo
	 */
	public String getTaxRegNo() {
		return taxRegNo;
	}

	/**
	 * 设置税务登记号
	 * @param  taxRegNo
	 */
	public void setTaxRegNo(final String taxRegNo) {
		this.taxRegNo = taxRegNo;
	}

	/**
	 * 获取企业地址
	 * @return address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * 设置企业地址
	 * @param  address
	 */
	public void setAddress(final String address) {
		this.address = address;
	}

	/**
	 * 获取企业logo
	 * @return 企业logo
	 */
	public String getLogo() {
		return logo;
	}

	/**
	 * 设置企业logo
	 * @param  企业logo
	 */
	public void setLogo(final String logo) {
		this.logo = logo;
	}

	/**
	 * 获取登录IP
	 * @return ip
	 */
	public String getIp() {
		return ip;
	}

	/**
	 * 设置登录IP
	 * @param  ip
	 */
	public void setIp(final String ip) {
		this.ip = ip;
	}

	/**
	 * 获取查询的userid
	 * @return uuids
	 */
	public String[] getUuids() {
		return uuids;
	}

	/**
	 * 设置查询的userid
	 * @param  uuids
	 */
	public void setUuids(final String[] uuids) {
		this.uuids = uuids;
	}

	/**
	 * 获得性别显示值
	 * @return
	 */
	public String getSexStr(){
		return DictUtils.getItemName(DictTypeEnum.SEX.getValue(), getSex());
	}
	
	/**
	 * 获得实名认证显示值
	 * @return
	 */
	public String getRealNameStatusStr(){
		return DictUtils.getItemName(DictTypeEnum.REAL_NAME_STATUS.getValue(), getRealNameStatus());
	}
	
	/**
	 * 用户类型显示值
	 * @return
	 */
	public String getUserNatureStr(){
		return DictUtils.getItemName(DictTypeEnum.USER_NATURE.getValue(), getUserNature());
	}
	/**
	 * 获取账户总额
	 * @return total
	 */
	public BigDecimal getTotal() {
		return total;
	}

	/**
	 * 设置账户总额
	 * @param  total
	 */
	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	/**
	 * 获取可用余额
	 * @return useMoney
	 */
	public BigDecimal getUseMoney() {
		return useMoney;
	}

	/**
	 * 设置可用余额
	 * @param  useMoney
	 */
	public void setUseMoney(BigDecimal useMoney) {
		this.useMoney = useMoney;
	}

	/**
	 * 获取冻结金额
	 * @return noUseMoney
	 */
	public BigDecimal getNoUseMoney() {
		return noUseMoney;
	}

	/**
	 * 设置冻结金额
	 * @param  noUseMoney
	 */
	public void setNoUseMoney(BigDecimal noUseMoney) {
		this.noUseMoney = noUseMoney;
	}

	public String getCreditCode() {
		return creditCode;
	}

	public void setCreditCode(String creditCode) {
		this.creditCode = creditCode;
	}
	
	/**
	 * 获取sessionId
	 * @return sessionId
	 */
	public String getSessionId() {
		return sessionId;
	}

	/**
	 * 设置sessionId
	 * @param  sessionId
	 */
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	/**
	 * 获取(隐藏一定位数的)电子邮件
	 * 
	 * @return 电子邮件
	 */
	public String getHideEmail() {
		if (StringUtils.isNotBlank(email) && email.indexOf('@') != -1) {
			return email.substring(0, 1) + "****"
					+ email.substring(email.indexOf('@') - 1, email.length());
		}
		return email;
	}

	/**
	 * 获取(隐藏一定位数的)手机号码
	 * 
	 * @return 手机号码
	 */
	public String getHideMobile() {
		if (StringUtils.isNotBlank(mobile) && mobile.length() == 11) {
			return mobile.substring(0, 3) + "****"
					+ mobile.substring(7, 11);
		}
		return mobile;
	}

	/**
	 * 获取(隐藏一定位数的)用户名
	 * 
	 * @return
	 */
	public String getHideUserName() {
		if (StringUtils.isNotBlank(userName) && userName.length() == 18) {
			return userName.substring(0, 2) + "****" + userName.substring(14, 18);
		} else {
			return userName.substring(0, 1)+"***"+userName.substring(userName.length()-1, userName.length());
		}
	}
	
	/**
	 * 
	 * 获取(隐藏一定位数的)第三方商户号
	 * @author xhf
	 * @date 2016年11月5日
	 * @return
	 */
	public String getHideTppUserCustId(){
		String hideTppUserCustId = tppUserCustId;
		if(StringUtils.isNotBlank(tppUserCustId) && tppUserCustId.length() >= 10){
			hideTppUserCustId = tppUserCustId.substring(0, 3)+"****"+tppUserCustId.substring(tppUserCustId.length()-4, tppUserCustId.length());
		}
		return hideTppUserCustId;
	}

	public String getExceptFreeze() {
		return exceptFreeze;
	}

	public void setExceptFreeze(String exceptFreeze) {
		this.exceptFreeze = exceptFreeze;
	}

	/**
	 * 重写toString()方法
	 */
	@Override
	public String toString() {
		return "User [" + "uuid=" + uuid + ", userNo=" + userNo + ", userName=" + userName + ", pwd=" + pwd + ", realName=" + realName + ", mobile=" + mobile + ", email=" + email + ", status=" + status + ", createTime=" + createTime + ", payPwd=" + payPwd + ", tppStatus=" + tppStatus + ", tppUserAccId=" + tppUserAccId + ", tppUserCustId=" + tppUserCustId + "]";
	}

	public String getRegistChannel() {
		return registChannel;
	}

	public void setRegistChannel(String registChannel) {
		this.registChannel = registChannel;
	}
	/**
	 * 获得待收金额
	 * @return
	 */
	public BigDecimal getCollectionMoney() {
		return collectionMoney;
	}
	/**
	 * 设置待收金额
	 * @param collectionMoney
	 */
	public void setCollectionMoney(BigDecimal collectionMoney) {
		this.collectionMoney = collectionMoney;
	}
	/**
	 * 获得待还金额
	 * @return
	 */
	public BigDecimal getPaymentMoney() {
		return paymentMoney;
	}
	/**
	 * 设置待还金额
	 * @param payment
	 */
	public void setPaymentMoney(BigDecimal paymentMoney) {
		this.paymentMoney = paymentMoney;
	}
	/**
	 * 获得个人用户投资次数
	 * @return
	 */
	public int getInvestNum() {
		return investNum;
	}
	/**
	 * 设置个人用户投资次数
	 * @param investNum
	 */
	public void setInvestNum(int investNum) {
		this.investNum = investNum;
	}
	/**
	 * 获得投资的状态
	 * @return
	 */
	public String getInvestStatus() {
		return investStatus;
	}
	/**
	 * 设置投资的状态
	 * @param investStatus
	 */
	public void setInvestStatus(String investStatus) {
		this.investStatus = investStatus;
	}
	/**
	 * 获得借款次数
	 * @return
	 */
	public int getBorrowNum() {
		return borrowNum;
	}
	/**
	 * 设置借款次数
	 * @param borrowNum
	 */
	public void setBorrowNum(int borrowNum) {
		this.borrowNum = borrowNum;
	}
	/**
	 * 获得借款状态
	 * @return
	 */
	public String getBorrowStatus() {
		return borrowStatus;
	}
	/**
	 * 设置借款状态
	 * @param borrowStatus
	 */
	public void setBorrowStatus(String borrowStatus) {
		this.borrowStatus = borrowStatus;
	}

	public String getRegisterChannel() {
		return registerChannel;
	}

	public void setRegisterChannel(String registerChannel) {
		this.registerChannel = registerChannel;
	}

	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	public String getIsStock() {
		return isStock;
	}

	public void setIsStock(String isStock) {
		this.isStock = isStock;
	}

	public BigDecimal getCashFeeMark() {
		return cashFeeMark;
	}

	public void setCashFeeMark(BigDecimal cashFeeMark) {
		this.cashFeeMark = cashFeeMark;
	}

	public int getAdvanceCount() {
		return advanceCount;
	}

	public void setAdvanceCount(int advanceCount) {
		this.advanceCount = advanceCount;
	}

	
	
}
