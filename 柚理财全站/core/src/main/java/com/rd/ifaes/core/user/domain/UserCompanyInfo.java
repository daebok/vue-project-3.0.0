package com.rd.ifaes.core.user.domain;

import java.util.Date;

import javax.validation.constraints.Pattern;

import com.rd.ifaes.common.entity.BaseEntity;
import com.rd.ifaes.common.exception.BussinessException;
import com.rd.ifaes.common.util.SpringContextHolder;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.core.core.constant.Constant;
import com.rd.ifaes.core.core.constant.ResourceConstant;
import com.rd.ifaes.core.core.util.ResourceUtils;
import com.rd.ifaes.core.core.util.ValidateUtils;
import com.rd.ifaes.core.sys.service.AreaService;

/**
 * entity:UserCompanyInfo
 * 
 * @author xhf
 * @version 3.0
 * @date 2016-6-15
 */
public class UserCompanyInfo extends BaseEntity<UserCompanyInfo> {
	
	private static final long serialVersionUID = 1L;
	/** 用户ID */ 
	private String	userId;		
	/** 公司名称 */ 
	@Pattern(regexp = "^([\u4e00-\u9fa5]+(（[\u4e00-\u9fa5]+）)?){2,30}$", message="{"+ResourceConstant.USER_COMPANYNAME_IRREGULAR+"}")
	private String	companyName;	
	/** 企业简称 */ 
	private String	simpleName;	
	/** 成立时间 */ 
	private Date	establishDate;		 
	/** 注册本金 */
	private String	registeredCapital;	
	/** 企业地址 */ 
	private String	address;	
	/** 执照号 */ 
	private String	bussinessCode;	
	/** 组织机构代码 */ 
	@Pattern(regexp = "^[0-9A-Z]{9}$", message="{"+ResourceConstant.USER_ORGCODE_IRREGULAR+"}")
	private String	orgCode;	
	/** 统一社会信用代码 */ 
	private String	creditCode;	
	/** 营业执照所在地 */ 
	private String	licenseAddress;	
	/** 企业LOGO */ 
	private String	logo;		 
	/** 执照过期日（营业期限），格式为"YYYYMMDD" */ 
	private Date	licenseExpireDate;	
	/** 营业范围 */
	private String	businessScope;	
	/** 联系电话 */ 
	private String	telephone;		
	/** 联系人 */ 
	private String	contacts;	
	/** 联系Email */ 
	private String	email;	
	/** 企业简介 */ 
	private String	summary;	
	/** 自然人 */ 
	private String	naturalPerson;	
	/** 企业法人 */ 
	private String	legalPerson;
	/** 法人证件号码 */ 
	private String	certNo;		
	/** 证件类型，默认身份证 */ 
	private String	certType;		
	/** 法人手机号码 */ 
	private String	legalPersonPhone;
	/** 银行编号 */ 
	private String	bankCode;		 
	/** 银行卡号 */ 
	private String	bankAccountNo;		
	/** 开户行省份 */ 
	private String	bankProvince;		
	/** 开户行城市 */ 
	private String	bankCity;		
	/** 支行名称 */ 
	private String	bankBranch;		
	/** 扩展信息 */ 
	private String	extendParam;	
	/** 请求审核订单号 */
	private String	auditOrderNo;	
	/** 审核状态  0：待审核，1：审核成功，2：审核失败，3：审核处理中 */ 
	private String	auditStatus;	
	/** 审核时间 */
	private Date	auditTime;		
	/** 审核信息 */ 
	private String	auditMessage;	
	/** 法人代表 */ 
	private String	legalDelegate;		
	/** 税务登记号 */ 
	private String	taxRegNo;		 
	/** 工作地省份 */ 
	private String officeProvince;
	/** 工作地市 */ 
	private String officeCity;
	/** 工作地地区 */ 
	private String officeArea;
	/** 工作地点 */ 
	private String officeAddress;
	/** 三证合一用户 */ 
	private String threeCertificate;
	
	/*公司行业*/
	private String companyIndustry;
	/*公司规模*/
	private String companyScale;
	/*还款来源*/
	private String repaySource;
	
	//其他自定义属性
	/** 用户名*/
	private String  userName;
	/** 第三方登录账户 */
	private String	tppUserAccId;
	/** 密码 */
	private String	pwd;     
	/** 是否担保机构,供查询使用*/
	private String  isVouch;	
	/** 添加时间 显示使用 */
	private Date	createTime;		
	/** 成立时间显示值 */ 
	private String	establishDateStr;	
	
	
	/** 担保用户默认密码*/
	public static final String VOUCH_DEFAULT_PASS = "3";   
	
	/**开户状态-待审核 */
	public static final String AUDIT_STATUS_WAIT = "0";    
	/**开户状态-审核成功 */
	public static final String AUDIT_STATUS_SUCCESS = "1"; 
	/**开户状态-审核失败 */
	public static final String AUDIT_STATUS_FAIL = "2";    
	/**开户状态-审核处理中 */
	public static final String AUDIT_STATUS_PROCESS = "3"; 
	
	/**
	 * 证件类型：身份证
	 */
	public static final String CERT_TYPE_ID_CARD = "IC"; 
	
	/**
	 *  构造函数
	 */
	public UserCompanyInfo() {
		super();
	}
	
	/**
	 *  构造函数
	 */
	public UserCompanyInfo(final String userId) {
		super();
		this.userId = userId;
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
	 * 获取企业简称
	 * @return simpleName
	 */
	public String getSimpleName() {
		return simpleName;
	}

	/**
	 * 设置企业简称
	 * @param  simpleName
	 */
	public void setSimpleName(final String simpleName) {
		this.simpleName = simpleName;
	}

	/**
	 * 获取成立时间
	 * @return establishDate
	 */
	public Date getEstablishDate() {
		return establishDate;
	}

	/**
	 * 设置成立时间
	 * @param  establishDate
	 */
	public void setEstablishDate(final Date establishDate) {
		this.establishDate = establishDate;
	}

	/**
	 * 获取注册本金
	 * @return registeredCapital
	 */
	public String getRegisteredCapital() {
		return registeredCapital;
	}

	/**
	 * 设置注册本金
	 * @param  registeredCapital
	 */
	public void setRegisteredCapital(final String registeredCapital) {
		this.registeredCapital = registeredCapital;
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
	 * 获取执照号
	 * @return bussinessCode
	 */
	public String getBussinessCode() {
		return bussinessCode;
	}

	/**
	 * 设置执照号
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
	 * 获取营业执照所在地
	 * @return licenseAddress
	 */
	public String getLicenseAddress() {
		return licenseAddress;
	}

	/**
	 * 设置营业执照所在地
	 * @param  licenseAddress
	 */
	public void setLicenseAddress(final String licenseAddress) {
		this.licenseAddress = licenseAddress;
	}

	/**
	 * 获取企业LOGO
	 * @return logo
	 */
	public String getLogo() {
		return logo;
	}

	/**
	 * 设置企业LOGO
	 * @param  logo
	 */
	public void setLogo(final String logo) {
		this.logo = logo;
	}

	/**
	 * 获取执照过期日（营业期限），格式为"YYYYMMDD"
	 * @return licenseExpireDate
	 */
	public Date getLicenseExpireDate() {
		return licenseExpireDate;
	}

	/**
	 * 设置执照过期日（营业期限），格式为"YYYYMMDD"
	 * @param  licenseExpireDate
	 */
	public void setLicenseExpireDate(final Date licenseExpireDate) {
		this.licenseExpireDate = licenseExpireDate;
	}

	/**
	 * 获取营业范围
	 * @return businessScope
	 */
	public String getBusinessScope() {
		return businessScope;
	}

	/**
	 * 设置营业范围
	 * @param  businessScope
	 */
	public void setBusinessScope(final String businessScope) {
		this.businessScope = businessScope;
	}

	/**
	 * 获取联系电话
	 * @return telephone
	 */
	public String getTelephone() {
		return telephone;
	}

	/**
	 * 设置联系电话
	 * @param  telephone
	 */
	public void setTelephone(final String telephone) {
		this.telephone = telephone;
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
	 * 获取联系Email
	 * @return email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * 设置联系Email
	 * @param  email
	 */
	public void setEmail(final String email) {
		this.email = email;
	}

	/**
	 * 获取企业简介
	 * @return summary
	 */
	public String getSummary() {
		return summary;
	}

	/**
	 * 设置企业简介
	 * @param  summary
	 */
	public void setSummary(final String summary) {
		this.summary = summary;
	}

	/**
	 * 获取自然人
	 * @return naturalPerson
	 */
	public String getNaturalPerson() {
		return naturalPerson;
	}

	/**
	 * 设置自然人
	 * @param  naturalPerson
	 */
	public void setNaturalPerson(final String naturalPerson) {
		this.naturalPerson = naturalPerson;
	}

	/**
	 * 获取企业法人
	 * @return legalPerson
	 */
	public String getLegalPerson() {
		return legalPerson;
	}

	/**
	 * 设置企业法人
	 * @param  legalPerson
	 */
	public void setLegalPerson(final String legalPerson) {
		this.legalPerson = legalPerson;
	}

	/**
	 * 获取法人证件号码
	 * @return certNo
	 */
	public String getCertNo() {
		return certNo;
	}

	/**
	 * 设置法人证件号码
	 * @param  certNo
	 */
	public void setCertNo(final String certNo) {
		this.certNo = certNo;
	}

	/**
	 * 获取证件类型，默认身份证
	 * @return certType
	 */
	public String getCertType() {
		return certType;
	}

	/**
	 * 设置证件类型，默认身份证
	 * @param  certType
	 */
	public void setCertType(final String certType) {
		this.certType = certType;
	}

	/**
	 * 获取法人手机号码
	 * @return legalPersonPhone
	 */
	public String getLegalPersonPhone() {
		return legalPersonPhone;
	}

	/**
	 * 设置法人手机号码
	 * @param  legalPersonPhone
	 */
	public void setLegalPersonPhone(final String legalPersonPhone) {
		this.legalPersonPhone = legalPersonPhone;
	}

	/**
	 * 获取银行编号
	 * @return bankCode
	 */
	public String getBankCode() {
		return bankCode;
	}

	/**
	 * 设置银行编号
	 * @param  bankCode
	 */
	public void setBankCode(final String bankCode) {
		this.bankCode = bankCode;
	}

	/**
	 * 获取银行卡号
	 * @return bankAccountNo
	 */
	public String getBankAccountNo() {
		return bankAccountNo;
	}

	/**
	 * 设置银行卡号
	 * @param  bankAccountNo
	 */
	public void setBankAccountNo(final String bankAccountNo) {
		this.bankAccountNo = bankAccountNo;
	}


	
	/**
	 * 获取支行名称
	 * @return bankBranch
	 */
	public String getBankBranch() {
		return bankBranch;
	}

	/**
	 * 设置支行名称
	 * @param  bankBranch
	 */
	public void setBankBranch(final String bankBranch) {
		this.bankBranch = bankBranch;
	}

	/**
	 * 获取扩展信息
	 * @return extendParam
	 */
	public String getExtendParam() {
		return extendParam;
	}

	/**
	 * 设置扩展信息
	 * @param  extendParam
	 */
	public void setExtendParam(final String extendParam) {
		this.extendParam = extendParam;
	}

	/**
	 * 获取请求审核订单号
	 * @return auditOrderNo
	 */
	public String getAuditOrderNo() {
		return auditOrderNo;
	}

	/**
	 * 设置请求审核订单号
	 * @param  auditOrderNo
	 */
	public void setAuditOrderNo(final String auditOrderNo) {
		this.auditOrderNo = auditOrderNo;
	}

	/**
	 * 获取审核状态0：待审核，1：审核成功，2：审核失败
	 * @return auditStatus
	 */
	public String getAuditStatus() {
		return auditStatus;
	}

	/**
	 * 设置审核状态0：待审核，1：审核成功，2：审核失败
	 * @param  auditStatus
	 */
	public void setAuditStatus(final String auditStatus) {
		this.auditStatus = auditStatus;
	}

	/**
	 * 获取审核时间
	 * @return auditTime
	 */
	public Date getAuditTime() {
		return auditTime;
	}

	/**
	 * 设置审核时间
	 * @param  auditTime
	 */
	public void setAuditTime(Date auditTime) {
		this.auditTime = auditTime;
	}

	/**
	 * 获取审核信息
	 * @return auditMessage
	 */
	public String getAuditMessage() {
		return auditMessage;
	}

	/**
	 * 设置审核信息
	 * @param  auditMessage
	 */
	public void setAuditMessage(final String auditMessage) {
		this.auditMessage = auditMessage;
	}

	/**
	 * 获取法人代表
	 * @return legalDelegate
	 */
	public String getLegalDelegate() {
		return legalDelegate;
	}

	/**
	 * 设置法人代表
	 * @param  legalDelegate
	 */
	public void setLegalDelegate(final String legalDelegate) {
		this.legalDelegate = legalDelegate;
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
	 * 获取第三方登录账户
	 * @return tppUserAccId
	 */
	public String getTppUserAccId() {
		return tppUserAccId;
	}

	/**
	 * 设置第三方登录账户
	 * @param  tppUserAccId
	 */
	public void setTppUserAccId(final String tppUserAccId) {
		this.tppUserAccId = tppUserAccId;
	}

	/**
	 * 获取密码
	 * @return pwd
	 */
	public String getPwd() {
		return pwd;
	}

	/**
	 * 设置密码
	 * @param  pwd
	 */
	public void setPwd(final String pwd) {
		this.pwd = pwd;
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
	 * 获取添加时间显示使用
	 * @return createTime
	 */
	public Date getCreateTime() {
		return createTime;
	}

	/**
	 * 设置添加时间显示使用
	 * @param  createTime
	 */
	public void setCreateTime(final Date createTime) {
		this.createTime = createTime;
	}

	/**
	 * 获取成立时间显示值
	 * @return establishDateStr
	 */
	public String getEstablishDateStr() {
		return establishDateStr;
	}

	/**
	 * 设置成立时间显示值
	 * @param  establishDateStr
	 */
	public void setEstablishDateStr(final String establishDateStr) {
		this.establishDateStr = establishDateStr;
	}

	/**
	 * 获取属性bankProvince的值
	 * @return bankProvince属性值
	 */
	public String getBankProvince() {
		return bankProvince;
	}

	/**
	 * 设置属性bankProvince的值
	 * @param  bankProvince属性值
	 */
	public void setBankProvince(String bankProvince) {
		this.bankProvince = bankProvince;
	}

	/**
	 * 获取属性bankCity的值
	 * @return bankCity属性值
	 */
	public String getBankCity() {
		return bankCity;
	}

	/**
	 * 设置属性bankCity的值
	 * @param  bankCity属性值
	 */
	public void setBankCity(String bankCity) {
		this.bankCity = bankCity;
	}

	/**
	 * 获取属性officeProvince的值
	 * @return officeProvince属性值
	 */
	public String getOfficeProvince() {
		return officeProvince;
	}

	/**
	 * 设置属性officeProvince的值
	 * @param  officeProvince属性值
	 */
	public void setOfficeProvince(String officeProvince) {
		this.officeProvince = officeProvince;
	}

	/**
	 * 获取属性officeCity的值
	 * @return officeCity属性值
	 */
	public String getOfficeCity() {
		return officeCity;
	}

	/**
	 * 设置属性officeCity的值
	 * @param  officeCity属性值
	 */
	public void setOfficeCity(String officeCity) {
		this.officeCity = officeCity;
	}

	/**
	 * 获取属性officeArea的值
	 * @return officeArea属性值
	 */
	public String getOfficeArea() {
		return officeArea;
	}

	/**
	 * 设置属性officeArea的值
	 * @param  officeArea属性值
	 */
	public void setOfficeArea(String officeArea) {
		this.officeArea = officeArea;
	}

	/**
	 * 获取属性officeAddress的值
	 * @return officeAddress属性值
	 */
	public String getOfficeAddress() {
		return officeAddress;
	}

	/**
	 * 设置属性officeAddress的值
	 * @param  officeAddress属性值
	 */
	public void setOfficeAddress(String officeAddress) {
		this.officeAddress = officeAddress;
	}

	public String getCreditCode() {
		return creditCode;
	}

	public void setCreditCode(String creditCode) {
		this.creditCode = creditCode;
	}
	/**
	 * 获取属性threeCertificate的值
	 * @return threeCertificate属性值
	 */
	public String getThreeCertificate() {
		return threeCertificate;
	}

	/**
	 * 设置属性threeCertificate的值
	 * @param  threeCertificate属性值
	 */
	public void setThreeCertificate(String threeCertificate) {
		this.threeCertificate = threeCertificate;
	}

	/**
	 * 重写toString()方法
	 */
	@Override
	public String toString() {
		return "UserCompanyInfo [userId=" + userId + ", companyName="
				+ companyName + ", simpleName=" + simpleName
				+ ", establishDate=" + establishDate + ", registeredCapital="
				+ registeredCapital + ", address=" + address
				+ ", bussinessCode=" + bussinessCode + ", orgCode=" + orgCode
				+ ", licenseAddress=" + licenseAddress + ", licenseExpireDate="
				+ licenseExpireDate + ", businessScope=" + businessScope
				+ ", telephone=" + telephone + ", contacts=" + contacts
				+ ", email=" + email + ", summary=" + summary
				+ ", naturalPerson=" + naturalPerson + ", legalPerson="
				+ legalPerson + ", certNo=" + certNo + ", certType=" + certType
				+ ", legalPersonPhone=" + legalPersonPhone + ", bankCode="
				+ bankCode + ", bankAccountNo=" + bankAccountNo + ", bankProvince="
				+ bankProvince + ", bankCity=" + bankCity + ", bankBranch=" + bankBranch
				+ ", extendParam=" + extendParam + ", auditOrderNo="
				+ auditOrderNo + ", auditStatus=" + auditStatus
				+ ", auditTime=" + auditTime + ", auditMessage=" + auditMessage
				+ "]";
	}
	
	public void validate(){
		/*if(!isConpanyName()){
			throw new BussinessException(ResourceUtils.get(ResourceConstant.COMPANY_NAME_ERROR));
		}*/
		if (StringUtils.isNotBlank(getTelephone()) &&!CheckTelephone()) {
			throw new BussinessException(ResourceUtils.get(ResourceConstant.USER_TELEPHONE_FORMAT_ERROR));
		}
		if (StringUtils.isNotBlank(getCreditCode()) && getCreditCode().length()!=Constant.INT_EIGHTEEN ) {
			throw new BussinessException(ResourceUtils.get(ResourceConstant.CREDIT_CODE_ERROR));
		}
	}
	
	//校验公司名称只能2-30个中文字符，包括()
	/*public boolean isConpanyName(){
		Pattern regex=Pattern.compile("^[\u4e00-\u9fa5\\(\\)]{2,30}$");
		Matcher matcher=regex.matcher(StringUtils.isNull(getCompanyName()));
		return matcher.matches();
	}*/
	public boolean CheckTelephone(){
		return ValidateUtils.checkTelephone(getTelephone());
	}
	
	
	/**
	 * 办公省显示值
	 * @return
	 */
	public String getOfficeProvinceStr() {
		String officeProvinceStr = "";
		if(StringUtils.isNotBlank(getOfficeProvince())){
			final AreaService areaService = SpringContextHolder.getBean("areaService");
			officeProvinceStr = areaService.getNameByCode(getOfficeProvince());
		}
		return officeProvinceStr;
	}
	
	/**
	 * 办公市显示值
	 * @return
	 */
	public String getOfficeCityStr() {
		String officeCityStr = "";
		if(StringUtils.isNotBlank(getOfficeCity())){
			final AreaService areaService = SpringContextHolder.getBean("areaService");
			officeCityStr = areaService.getNameByCode(getOfficeCity());
		}
		return officeCityStr;
	}
	
	
	/**
	 * 办公区显示值
	 * @return
	 */
	public String getOfficeAreaStr() {
		String officeAreaStr = "";
		if(StringUtils.isNotBlank(getOfficeArea())){
			final AreaService areaService = SpringContextHolder.getBean("areaService");
			officeAreaStr = areaService.getNameByCode(getOfficeArea());
		}
		return officeAreaStr;
	}

	public String getCompanyIndustry() {
		return companyIndustry;
	}

	public void setCompanyIndustry(String companyIndustry) {
		this.companyIndustry = companyIndustry;
	}

	public String getCompanyScale() {
		return companyScale;
	}

	public void setCompanyScale(String companyScale) {
		this.companyScale = companyScale;
	}

	public String getRepaySource() {
		return repaySource;
	}

	public void setRepaySource(String repaySource) {
		this.repaySource = repaySource;
	}
	
}
