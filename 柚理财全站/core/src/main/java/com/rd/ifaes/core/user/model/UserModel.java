package com.rd.ifaes.core.user.model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.BeanUtils;

import com.rd.ifaes.common.exception.BussinessException;
import com.rd.ifaes.common.util.HtmlUtils;
import com.rd.ifaes.common.util.MD5Utils;
import com.rd.ifaes.common.util.NumberUtils;
import com.rd.ifaes.common.util.SessionUtils;
import com.rd.ifaes.common.util.SpringContextHolder;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.common.util.resource.UserResource;
import com.rd.ifaes.core.core.constant.Constant;
import com.rd.ifaes.core.core.constant.MessageConstant;
import com.rd.ifaes.core.core.constant.ResourceConstant;
import com.rd.ifaes.core.core.util.ResourceUtils;
import com.rd.ifaes.core.core.util.ValidateUtils;
import com.rd.ifaes.core.sys.domain.Operator;
import com.rd.ifaes.core.user.domain.User;
import com.rd.ifaes.core.user.domain.UserCache;
import com.rd.ifaes.core.user.domain.UserSecurityAnswer;
import com.rd.ifaes.core.user.service.UserCacheService;
import com.rd.ifaes.core.user.service.UserSecurityAnswerService;
import com.rd.ifaes.core.user.service.UserService;

/**
 * 用户Model
 * 
 * @author xhf
 * @version 3.0
 * @since 2016年6月6日
 */
public class UserModel extends User {
	
	private static final long serialVersionUID = 1L;
	
	/****************注册 start*****************/
	/** 同意协议 */
	private int agree;       
	/** 短信验证码 */
	private String code;  
	/** 登录密码  */
	private String loginPwd;
	/** 新登录密码 */
	private String newLoginPwd; 
	/** 确认密码 */
	private String confirmPwd; 
	/** 图形验证码 */
	private String validCode;
	/** 邀请链接参数 */
	private String ui;
	/** 邀请人 */
	private User inviteUser;
	/** 邀请经纪人 */
	private Operator inviteOperator;
	/** 重定向地址  */
	private String redirectURL;
	/** 登录名  */
	private String loginName;
	/** 是否加密  */
	private int isRsa;
	/** 用户私有域 */
	private String merPriv;
	/****************注册 end*****************/
	
	/****************密保问题 start*****************/
	/** 验证方式 01：邮箱认证  02：手机认证 **/
	private String checkType;
	/** 修改邮箱标识 **/
	private String modifyEmailSign; 
	/** 修改手机标识**/
	private String modifyPhoneSign; 
	/** 问题标识**/
	private String questionFlag; 
	/** 答案 **/
	private String answer;     
	/****************密保问题 end*****************/
	
		
	/****************其它属性 start*****************/	
	
	/** 用户注册方式 0 用户注册 1 后台录入 **/ 
	private String	regMode;		 
	
	/** 修改类型 0绑定  1 修改**/ 
	private String	modifyType;	
	/**锁定备注**/
	String lockRemark;
	
	/**操作列表**/
	String[] operation;
	
	/**审核状态**/
	private String auditStat; 
	
	/**审核状态描述**/
	private String auditDesc;
	
	/**流水号, 查询企业开户结果时需要使用 徽商必传**/
	private String tradeNo;
	
	/**手机认证状态**/
	private String mobilePhoneStatus;
	
	/**担保企业区域-省**/
	private String province;
	/**担保企业区域-市**/
	private String city;
	/**担保企业区域-区**/
	private String area;
	/** 三证合一用户 */ 
	private String threeCertificate;
	
	/** 法人代表 */ 
	private String	legalDelegate;
	
	/** 法人证件号码 */ 
	private String	certNo;

	/** 注册渠道 1 PC  2 APP 3 微信  */ 
	private String registChannel;
	
	/**冻结功能点**/
	String freezeOperation;
	
	/****************其它属性 end*****************/
	/**
	 * 页面是否对密码加密：0 不加密，1 加密
	 */
	private int encrypt;
	
	// Constructor
	public UserModel() {
	}
	
	public UserModel(final String loginName, String loginPwd) {
		super();
		this.loginName = loginName;
		this.loginPwd = loginPwd;
	}
		
	/**
	 * 获取"是否同意协议"
	 * @return agree
	 */
	public int getAgree() {
		return agree;
	}

	/**
	 * 设置"是否同意协议"
	 * @param  agree
	 */
	public void setAgree(int agree) {
		this.agree = agree;
	}

	/**
	 * 获取短信验证码
	 * @return code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * 设置短信验证码
	 * @param  code
	 */
	public void setCode(final String code) {
		this.code = code;
	}

	/**
	 * 获取登录密码
	 * @return loginPwd
	 */
	public String getLoginPwd() {
		return loginPwd;
	}

	/**
	 * 设置登录密码
	 * @param  loginPwd
	 */
	public void setLoginPwd(final String loginPwd) {
		this.loginPwd = loginPwd;
	}

	/**
	 * 获取新登录密码
	 * @return newLoginPwd
	 */
	public String getNewLoginPwd() {
		return newLoginPwd;
	}

	/**
	 * 设置新登录密码
	 * @param  newLoginPwd
	 */
	public void setNewLoginPwd(final String newLoginPwd) {
		this.newLoginPwd = newLoginPwd;
	}

	/**
	 * 获取确认密码
	 * @return confirmPwd
	 */
	public String getConfirmPwd() {
		return confirmPwd;
	}

	/**
	 * 设置确认密码
	 * @param  confirmPwd
	 */
	public void setConfirmPwd(final String confirmPwd) {
		this.confirmPwd = confirmPwd;
	}

	/**
	 * 获取图形验证码
	 * @return validCode
	 */
	public String getValidCode() {
		return validCode;
	}

	/**
	 * 设置图形验证码
	 * @param  validCode
	 */
	public void setValidCode(final String validCode) {
		this.validCode = validCode;
	}

	/**
	 * 获取邀请链接参数
	 * @return ui
	 */
	public String getUi() {
		return ui;
	}

	/**
	 * 设置邀请链接参数
	 * @param  ui
	 */
	public void setUi(final String ui) {
		this.ui = ui;
	}

	/**
	 * 获取邀请人
	 * @return inviteUser
	 */
	public User getInviteUser() {
		return inviteUser;
	}

	/**
	 * 设置邀请人
	 * @param  inviteUser
	 */
	public void setInviteUser(User inviteUser) {
		this.inviteUser = inviteUser;
	}

	/**
	 * 获取重定向地址
	 * @return redirectURL
	 */
	public String getRedirectURL() {
		return redirectURL;
	}

	/**
	 * 设置重定向地址
	 * @param  redirectURL
	 */
	public void setRedirectURL(final String redirectURL) {
		this.redirectURL = redirectURL;
	}

	/**
	 * 获取登录名
	 * @return loginName
	 */
	public String getLoginName() {
		return loginName;
	}

	/**
	 * 设置登录名
	 * @param  loginName
	 */
	public void setLoginName(final String loginName) {
		this.loginName = loginName;
	}

	/**
	 * 获取是否加密
	 * @return isRsa
	 */
	public int getIsRsa() {
		return isRsa;
	}

	/**
	 * 设置是否加密
	 * @param  isRsa
	 */
	public void setIsRsa(int isRsa) {
		this.isRsa = isRsa;
	}

	/**
	 * 获取用户私有域
	 * @return merPriv
	 */
	public String getMerPriv() {
		return merPriv;
	}

	/**
	 * 设置用户私有域
	 * @param  merPriv
	 */
	public void setMerPriv(final String merPriv) {
		this.merPriv = merPriv;
	}

	/**
	 * 获取注册end
	 * @return checkType
	 */
	public String getCheckType() {
		return checkType;
	}

	/**
	 * 设置注册end
	 * @param  checkType
	 */
	public void setCheckType(final String checkType) {
		this.checkType = checkType;
	}

	/**
	 * 获取修改邮箱标识
	 * @return modifyEmailSign
	 */
	public String getModifyEmailSign() {
		return modifyEmailSign;
	}

	/**
	 * 设置修改邮箱标识
	 * @param  modifyEmailSign
	 */
	public void setModifyEmailSign(final String modifyEmailSign) {
		this.modifyEmailSign = modifyEmailSign;
	}

	/**
	 * 获取修改手机标识
	 * @return modifyPhoneSign
	 */
	public String getModifyPhoneSign() {
		return modifyPhoneSign;
	}

	/**
	 * 设置修改手机标识
	 * @param  modifyPhoneSign
	 */
	public void setModifyPhoneSign(final String modifyPhoneSign) {
		this.modifyPhoneSign = modifyPhoneSign;
	}

	/**
	 * 获取问题标识
	 * @return questionFlag
	 */
	public String getQuestionFlag() {
		return questionFlag;
	}

	/**
	 * 设置问题标识
	 * @param  questionFlag
	 */
	public void setQuestionFlag(final String questionFlag) {
		this.questionFlag = questionFlag;
	}

	/**
	 * 获取答案
	 * @return answer
	 */
	public String getAnswer() {
		return answer;
	}

	/**
	 * 设置答案
	 * @param  answer
	 */
	public void setAnswer(final String answer) {
		this.answer = answer;
	}

	/**
	 * 获取用户注册方式0用户注册1后台录入
	 * @return regMode
	 */
	public String getRegMode() {
		return regMode;
	}

	/**
	 * 设置用户注册方式0用户注册1后台录入
	 * @param  regMode
	 */
	public void setRegMode(final String regMode) {
		this.regMode = regMode;
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
	 * 获取操作列表
	 * @return operation
	 */
	public String[] getOperation() {
		return operation;
	}

	/**
	 * 设置操作列表
	 * @param  operation
	 */
	public void setOperation(final String[] operation) {
		this.operation = operation;
	}

	/**
	 * 获取审核状态
	 * @return auditStat
	 */
	public String getAuditStat() {
		return auditStat;
	}

	/**
	 * 设置审核状态
	 * @param  auditStat
	 */
	public void setAuditStat(final String auditStat) {
		this.auditStat = auditStat;
	}

	/**
	 * 获取审核状态描述
	 * @return auditDesc
	 */
	public String getAuditDesc() {
		return auditDesc;
	}

	/**
	 * 设置审核状态描述
	 * @param  auditDesc
	 */
	public void setAuditDesc(final String auditDesc) {
		this.auditDesc = auditDesc;
	}

	/**
	 * 获取流水号查询企业开户结果时需要使用徽商必传
	 * @return tradeNo
	 */
	public String getTradeNo() {
		return tradeNo;
	}

	/**
	 * 设置流水号查询企业开户结果时需要使用徽商必传
	 * @param  tradeNo
	 */
	public void setTradeNo(final String tradeNo) {
		this.tradeNo = tradeNo;
	}

	/**
	 * 获取手机认证状态
	 * @return mobilePhoneStatus
	 */
	public String getMobilePhoneStatus() {
		return mobilePhoneStatus;
	}

	/**
	 * 设置手机认证状态
	 * @param  mobilePhoneStatus
	 */
	public void setMobilePhoneStatus(final String mobilePhoneStatus) {
		this.mobilePhoneStatus = mobilePhoneStatus;
	}
	
	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public void validRegPhoneCode(){
		if (!ValidateUtils.checkCode(getMobile(), MessageConstant.MESSAGE_REG, getCode())) {
			throw new BussinessException(ResourceUtils.get(ResourceConstant.VALID_CODE_ERROR), BussinessException.TYPE_JSON);
		}
	}
		
	/**
	 * 注册 检查提交的密码数据格式
	 * @return
	 */
	public void validRegPwdModel() {
		if (StringUtils.isBlank(getPwd())) {
			throw new BussinessException(ResourceUtils.get(ResourceConstant.USER_PWD_EMPTY), BussinessException.TYPE_JSON);
		}
		if (StringUtils.isBlank(getConfirmPwd())) {
			throw new BussinessException(ResourceUtils.get(ResourceConstant.CONFIRM_PWD_IS_EMPTY), BussinessException.TYPE_JSON);
		}
		if (!getPwd().equals(getConfirmPwd())) {
			throw new BussinessException(ResourceUtils.get(ResourceConstant.OPERATOR_PWD_AGAIN_ERROR), BussinessException.TYPE_JSON);
		}
		// 密码不能含有空格
		if(!(getPwd().replace(" ", "")).equals(getPwd())){
			throw new BussinessException(ResourceUtils.get(UserResource.USER_PASS_HAVA_SPACE), BussinessException.TYPE_JSON);
		}
		// 密码不能含有中文
		if(StringUtils.ishaveChinese(getPwd())){
			throw new BussinessException(ResourceUtils.get(UserResource.USER_PASS_HAVA_CHINESE), BussinessException.TYPE_JSON);
		}
		// 密码规则正则校验
		if(!StringUtils.isPwd(HtmlUtils.htmlUnescape(getPwd()))){
			throw new BussinessException(ResourceUtils.get(UserResource.USER_PASS_ERROR), BussinessException.TYPE_JSON);
		}
	}
	
	/**
	 * 校验图形验证码
	 * 
	 * @return
	 */
	public int validRegRule() {
		String vCode = StringUtils.isNull(getValidCode());
		if (vCode.isEmpty()) {
			throw new BussinessException(ResourceUtils.get(ResourceConstant.VALID_CODE_NOTEMPTY), BussinessException.TYPE_JSON);
		}

		if (!ValidateUtils.checkValidCode(vCode)) {
			throw new BussinessException(ResourceUtils.get(ResourceConstant.VALID_CODE_ERROR), BussinessException.TYPE_JSON);
		}
		return -1;
	}
	
	/**
	 * 是否同意协议
	 * @return
	 */
	public void validAgree(){
		if(Constant.FLAG_INT_YES != getAgree()){
			throw new BussinessException(ResourceUtils.get(ResourceConstant.PROTOCOL_SHOULD_BE_AGREE), BussinessException.TYPE_JSON);
		}
	}
	
	/**
	 * 登录数据， 非空校验
	 * 
	 * @return
	 */
	public int validLoginModel() {
		if (StringUtils.isBlank(getLoginName())) {
			throw new BussinessException(ResourceUtils.get(ResourceConstant.USER_NAME_EMPTY), BussinessException.TYPE_JSON);
		}
		if (StringUtils.isBlank(getLoginPwd())) {
			throw new BussinessException(ResourceUtils.get(ResourceConstant.USER_PWD_EMPTY), BussinessException.TYPE_JSON);
		}
		return -1;
	}
	/**
	 * 修改登录密码数据验证，
	 * @return
	 */
	public int validSetNewPwd(){
		//不为空 判断
		if (StringUtils.isBlank(getLoginPwd())) {
			throw new BussinessException(ResourceUtils.get(ResourceConstant.OLD_PWD_IS_EMPTY), BussinessException.TYPE_JSON);
		}else if(StringUtils.isBlank(getNewLoginPwd())){
			throw new BussinessException(ResourceUtils.get(ResourceConstant.NEW_PWD_IS_EMPTY), BussinessException.TYPE_JSON);
		}else if(StringUtils.isBlank(getConfirmPwd())){
			throw new BussinessException(ResourceUtils.get(ResourceConstant.CONFIRM_PWD_IS_EMPTY), BussinessException.TYPE_JSON);
		}
		//判断新密码格式
		if(!isLoginPwd(getNewLoginPwd())){
			throw new BussinessException(ResourceUtils.get(ResourceConstant.NEW_PWD_FORMAT_ERROR), BussinessException.TYPE_JSON);
		}
		//判断确认密码是否一致
		if(!getNewLoginPwd().equals(getConfirmPwd())){
			throw new BussinessException(ResourceUtils.get(ResourceConstant.OPERATOR_PWD_AGAIN_ERROR), BussinessException.TYPE_JSON);
		}
		return -1;
	}
	
	public User prototype() {
		User user = new User();
		BeanUtils.copyProperties(this, user);
		return user;
	}

	/**
	 * 修改绑定邮箱校验
	 * @throws Exception 
	 */
	public void validModifyEmail(){
		if(StringUtils.isBlank(getCheckType())){
			throw new BussinessException(ResourceUtils.get(ResourceConstant.IDENTIFY_METHOD_NEED_SELECTED), BussinessException.TYPE_JSON);
		}
		
		String signType="";
		if(Constant.EMAIL_VALID_PASS.equals(getCheckType())){ //身份证+密保问题
			//身份证号
			UserCacheService userCacheService = SpringContextHolder.getBean("userCacheService");
			UserCache userCache = userCacheService.findByUserId(getUuid());
			if(StringUtils.isBlank(super.getIdNo())){
				throw new BussinessException(ResourceUtils.get(ResourceConstant.IDCARD_IS_EMPTY), BussinessException.TYPE_JSON);
			} else if(!super.getIdNo().equals(userCache.getIdNo())){
				throw new BussinessException(ResourceUtils.get(ResourceConstant.IDCARD_IS_ERROR), BussinessException.TYPE_JSON);
			}
			//密保问题
			UserSecurityAnswerService answerService = SpringContextHolder.getBean("userSecurityAnswerService");
			UserSecurityAnswer userAnswer = answerService.findByQuestion(getUuid(), getQuestionFlag());
			if(StringUtils.isBlank(getAnswer())){
				throw new BussinessException(ResourceUtils.get(ResourceConstant.ANSWER_IS_EMPTY), BussinessException.TYPE_JSON);
			}else if(userAnswer==null){
				throw new BussinessException(ResourceUtils.get(ResourceConstant.PWD_QUESTION_IS_NOT_EXIST), BussinessException.TYPE_JSON);
			} else if(!getAnswer().equals(userAnswer.getAnswer())){
				throw new BussinessException(ResourceUtils.get(ResourceConstant.ANSWER_IS_ERROR), BussinessException.TYPE_JSON);
			}
			
			signType = super.getIdNo();
			
		} else{
			String addr="";
			if(Constant.EMAIL_VALID_EMAIL.equals(getCheckType())){  //邮箱认证
				signType = MessageConstant.MESSAGE_MODIFY_EMAIL_EMAILCODE;
				addr = getEmail();
				
			} else if(Constant.EMAIL_VALID_MOBILE.equals(getCheckType())){ //手机认证
				signType = MessageConstant.MESSAGE_MODIFY_EMAIL_PHONECODE;
				addr = getMobile();
			}
			if (!ValidateUtils.checkCode(addr, signType, getCode())) {
				throw new BussinessException(ResourceUtils.get(ResourceConstant.VALID_CODE_ERROR), BussinessException.TYPE_JSON);
			}
		} 
		this.setModifyEmailSign(MD5Utils.encode(getEmail() + signType));
	}
	
	/**
	 * 修改绑定手机校验
	 * @throws Exception 
	 */
	public void validModifyMobile(){
		if(StringUtils.isBlank(getCheckType())){
			throw new BussinessException(ResourceUtils.get(ResourceConstant.IDENTIFY_METHOD_NEED_SELECTED), BussinessException.TYPE_JSON);
		}
		
		String signType="";
		if(Constant.MOBILE_VALID_PASS_PERSON.equals(getCheckType())){ //身份证+密保问题
			//身份证号
			UserCache userCache = (UserCache)SessionUtils.getSessionAttr(Constant.SESSION_USER_CACHE);
			if(StringUtils.isBlank(getIdNo())){
				throw new BussinessException(ResourceUtils.get(ResourceConstant.IDCARD_IS_EMPTY), BussinessException.TYPE_JSON);
			} else if(!getIdNo().equals(userCache.getIdNo())){
				throw new BussinessException(ResourceUtils.get(ResourceConstant.IDCARD_IS_ERROR), BussinessException.TYPE_JSON);
			}
			//密保问题
			UserSecurityAnswerService answerService = SpringContextHolder.getBean("userSecurityAnswerService");
			UserSecurityAnswer userAnswer = answerService.findByQuestion(getUuid(), getQuestionFlag());
			if(StringUtils.isBlank(getAnswer())){
				throw new BussinessException(ResourceUtils.get(ResourceConstant.ANSWER_IS_EMPTY), BussinessException.TYPE_JSON);
			}else if(userAnswer==null){
				throw new BussinessException(ResourceUtils.get(ResourceConstant.PWD_QUESTION_IS_NOT_EXIST), BussinessException.TYPE_JSON);
			} else if(!getAnswer().equals(userAnswer.getAnswer())){
				throw new BussinessException(ResourceUtils.get(ResourceConstant.ANSWER_IS_ERROR), BussinessException.TYPE_JSON);
			}
			
			signType = getIdNo();
			
		} else{
			String addr="";
			if(Constant.MOBILE_VALID_MOBILE.equals(getCheckType())){ //手机认证
				signType = MessageConstant.MESSAGE_MODIFY_PHONE_PHONECODE;
				addr = getMobile();
				
			}else{ //邮箱+身份证/密保
				signType = MessageConstant.MESSAGE_MODIFY_PHONE_EMAILCODE;
				addr = getEmail();
				
				if(Constant.MOBILE_VALID_EMAIL_PERSON.equals(getCheckType())){ //邮箱+身份证
					//身份证号
					UserCache userCache = (UserCache)SessionUtils.getSessionAttr(Constant.SESSION_USER_CACHE);
					if(StringUtils.isBlank(getIdNo())){
						throw new BussinessException(ResourceUtils.get(ResourceConstant.IDCARD_IS_EMPTY), BussinessException.TYPE_JSON);
					} else if(!getIdNo().equals(userCache.getIdNo())){
						throw new BussinessException(ResourceUtils.get(ResourceConstant.IDCARD_IS_ERROR), BussinessException.TYPE_JSON);
					}
					
				}else if(Constant.MOBILE_VALID_EMAIL_COMPANY.equals(getCheckType())){ //邮箱+密保
					//密保问题
					UserSecurityAnswerService answerService = SpringContextHolder.getBean("userSecurityAnswerService");
					UserSecurityAnswer userAnswer = answerService.findByQuestion(getUuid(), getQuestionFlag());
					if(StringUtils.isBlank(getAnswer())){
						throw new BussinessException(ResourceUtils.get(ResourceConstant.ANSWER_IS_EMPTY), BussinessException.TYPE_JSON);
					}else if(userAnswer==null){
						throw new BussinessException(ResourceUtils.get(ResourceConstant.PWD_QUESTION_IS_NOT_EXIST), BussinessException.TYPE_JSON);
					} else if(!getAnswer().equals(userAnswer.getAnswer())){
						throw new BussinessException(ResourceUtils.get(ResourceConstant.ANSWER_IS_ERROR), BussinessException.TYPE_JSON);
					}
				}
				
			}
			if (!ValidateUtils.checkCode(addr, signType, getCode())) {
				throw new BussinessException(ResourceUtils.get(ResourceConstant.VALID_CODE_ERROR), BussinessException.TYPE_JSON);
			}
		} 
		this.setModifyPhoneSign(MD5Utils.encode(getMobile() + signType));
	}
	
	/**
	 * 绑定邮箱校验
	 * @throws Exception 
	 */
	public void validBindEmail(){
        //校验邮箱
		checkEmail();
		
		//校验验证码
		if (!ValidateUtils.checkCode(getEmail(), MessageConstant.MESSAGE_BIND_EMAIL, this.code)) {
			throw new BussinessException(ResourceUtils.get(ResourceConstant.VALID_CODE_ERROR), BussinessException.TYPE_JSON);
		}
	}
	
	/**
	 * 校验邮箱
	 * @author xhf
	 * @date 2016年8月20日
	 */
	public void checkEmail(){
		if (StringUtils.isBlank(this.getEmail())) {
			throw new BussinessException(ResourceUtils.get(ResourceConstant.EAMIL_IS_EMPTY), BussinessException.TYPE_JSON);
		}
		if (!StringUtils.isEmail(this.getEmail())) {
			throw new BussinessException(ResourceUtils.get(ResourceConstant.EMAIL_FORMAT_ERROR), BussinessException.TYPE_JSON);
		}
		if (this.getEmail().length()>Constant.MAIL_MAX_LENGTH) {
			throw new BussinessException(ResourceUtils.get(ResourceConstant.EMAIL_OVER_MAX_LENGTH), BussinessException.TYPE_JSON);
		}
		
		UserService userService = (UserService)SpringContextHolder.getBean("userService");
		int count = userService.checkRepeat(getUuid(), null, null, getEmail());
		if (count > 0) {
			throw new BussinessException(ResourceUtils.get(ResourceConstant.EMAIL_IS_USED), BussinessException.TYPE_JSON);
		}
	}
	
	/**
	 * 绑定手机校验
	 */
	public void validBindPhone() {
		//校验手机号
		checkMobile();
		
		//校验验证码
		if (!ValidateUtils.checkCode(getMobile(), MessageConstant.MESSAGE_BIND_PHONE, getCode())) {
			throw new BussinessException(ResourceUtils.get(ResourceConstant.VALID_CODE_ERROR), BussinessException.TYPE_JSON);
		}
	}
	
	/**
	 * 
	 * 校验手机号码
	 * @author xhf
	 * @date 2016年8月20日
	 */
	public void checkMobile(){
		if(StringUtils.isBlank(getMobile())){
			throw new BussinessException(ResourceUtils.get(ResourceConstant.USER_MOBILE_NOTEMPTY), BussinessException.TYPE_JSON);
		}
		
		if (!StringUtils.isPhone(this.getMobile())) {
			throw new BussinessException(ResourceUtils.get(ResourceConstant.MOBILE_STYLE_ERROR), BussinessException.TYPE_JSON);
		}
		
		if(StringUtils.isNotBlank(getUuid())){
			User user = SessionUtils.getSessionUser();
			if(getMobile().equals(user.getMobile())){
				throw new BussinessException(ResourceUtils.get(ResourceConstant.USER_MOBILE_NEED_NEW), BussinessException.TYPE_JSON);
			}
		}
		
		UserService userService = (UserService)SpringContextHolder.getBean("userService");
		int count = userService.checkRepeat(getUuid(), null, getMobile(), null);
		if (count > 0 ) {
			throw new BussinessException(ResourceUtils.get(UserResource.USER_MOBILE_IS_USED), BussinessException.TYPE_JSON);
		}
	}
	
	/**
	 * 校验用户名
	 * 
	 * @param userName
	 * @return
	 */
	public static boolean isUserName(String userName) {
		userName = StringUtils.isNull(userName);
		Pattern regex = Pattern.compile("(?![0-9]+$)[0-9A-Za-z]{6,20}");
		Matcher matcher = regex.matcher(userName);
		boolean isMatched = matcher.matches();
		return isMatched;
	}
	
	/**
	 * 校验登陆密码
	 * 
	 * @param loginPwd
	 * @return
	 */
	public static boolean isLoginPwd(String loginPwd) {
		loginPwd = StringUtils.isNull(loginPwd);
		Pattern regex = Pattern.compile("(?![^a-zA-Z]+$)(?!\\D+$)^(?!.*\\s).{8,16}");
		Matcher matcher = regex.matcher(loginPwd);
		boolean isMatched = matcher.matches();
		return isMatched;
	}
	
	/**
	 * 
	 * 后台添加企业用户校验密码
	 * @author xhf
	 * @date 2016年11月4日
	 */
	public void checkPwd(){
		// 默认密码不能为空
		if(StringUtils.isBlank(getPwd())){
			throw new BussinessException(ResourceUtils.get(ResourceConstant.USER_PWD_ISEMPTY), BussinessException.TYPE_JSON);
		}
		// 默认密码格式错误
		if(!isLoginPwd(getPwd())) {
			throw new BussinessException(ResourceUtils.get(ResourceConstant.USER_PWD_MANAGE_ERROR), BussinessException.TYPE_JSON);
		}
	}

	/**
	 * 后台添加或编辑企业用户字段校验
	 * 
	 * @author xhf
	 * @date 2016年9月26日
	 */
	public void checkCompanyManage() {
		// 手机号不能为空
		if(StringUtils.isBlank(getMobile())){
			throw new BussinessException(ResourceUtils.get(ResourceConstant.USER_MOBILE_ISEMPTY), BussinessException.TYPE_JSON);
		}
		// 手机号格式错误
		if(!StringUtils.isPhone(this.getMobile())) {
			throw new BussinessException(ResourceUtils.get(ResourceConstant.USER_MOBILE_ERROR), BussinessException.TYPE_JSON);
		}
		
		if(StringUtils.isNotBlank(getTppUserCustId())){
			//第三方商户号
			if(getTppUserCustId().length() > Constant.INT_FIFTY){
				throw new BussinessException(ResourceUtils.get(ResourceConstant.USER_TPP_USER_CUST_LENGHT_ERROR), BussinessException.TYPE_JSON);
			}
			//企业名称
			if(!StringUtils.isCompanyName(getCompanyName())){
				throw new BussinessException(ResourceUtils.get(ResourceConstant.COMPANY_NAME_ERROR), BussinessException.TYPE_CLOSE);
			}
			//法人代表
			if(!StringUtils.isChinese(this.getLegalDelegate())){
				throw new BussinessException(ResourceUtils.get(ResourceConstant.USER_REALNAME_ERROR), BussinessException.TYPE_CLOSE);
			}
			//法人身份证号
			if(!StringUtils.isCard(getCertNo())){
				throw new BussinessException(ResourceUtils.get(ResourceConstant.USER_IDNO_ERROR), BussinessException.TYPE_CLOSE);
			}
			//三证
			if(Constant.THREE_CERTIFICATE_YES.equals(getThreeCertificate())){//三证合一
				//统一社会信用代码
				if(!StringUtils.isIntAndChar(getCreditCode(), 18)){
					throw new BussinessException(ResourceUtils.get(ResourceConstant.CREDIT_CODE_ERROR), BussinessException.TYPE_CLOSE);
				}
			}else{//非三证合一
				//营业执照号
				if(!StringUtils.isIntAndChar(getBussinessCode(), 15)){
					throw new BussinessException(ResourceUtils.get(ResourceConstant.BUSSINESS_CODE_ERROR), BussinessException.TYPE_CLOSE);
				}
				//组织机构代码
				if(!StringUtils.isOrgCode(getOrgCode())){
					throw new BussinessException(ResourceUtils.get(ResourceConstant.ORG_CODE_ERROR), BussinessException.TYPE_CLOSE);
				}
			}
		}else{
			//企业名称
			if(StringUtils.isNotBlank(getCompanyName()) && !StringUtils.isCompanyName(getCompanyName())){
				throw new BussinessException(ResourceUtils.get(ResourceConstant.COMPANY_NAME_ERROR), BussinessException.TYPE_CLOSE);
			}
			//法人代表
			if(StringUtils.isNotBlank(getLegalDelegate()) && !StringUtils.isChinese(getLegalDelegate())){
				throw new BussinessException(ResourceUtils.get(ResourceConstant.USER_REALNAME_ERROR), BussinessException.TYPE_CLOSE);
			}
			//法人身份证号
			if(StringUtils.isNotBlank(getCertNo()) && !StringUtils.isCard(getCertNo())){
				throw new BussinessException(ResourceUtils.get(ResourceConstant.USER_IDNO_ERROR), BussinessException.TYPE_CLOSE);
			}
			//三证
			if(Constant.THREE_CERTIFICATE_YES.equals(getThreeCertificate())){//三证合一
				//统一社会信用代码
				if(StringUtils.isNotBlank(getCreditCode()) && !StringUtils.isIntAndChar(getCreditCode(), 18)){
					throw new BussinessException(ResourceUtils.get(ResourceConstant.CREDIT_CODE_ERROR), BussinessException.TYPE_CLOSE);
				}
			}else{//非三证合一
				//营业执照号
				if(StringUtils.isNotBlank(getBussinessCode()) && !StringUtils.isIntAndChar(getBussinessCode(), 15)){
					throw new BussinessException(ResourceUtils.get(ResourceConstant.BUSSINESS_CODE_ERROR), BussinessException.TYPE_CLOSE);
				}
				//组织机构代码
				if(StringUtils.isNotBlank(getOrgCode()) && !StringUtils.isOrgCode(getOrgCode())){
					throw new BussinessException(ResourceUtils.get(ResourceConstant.ORG_CODE_ERROR), BussinessException.TYPE_CLOSE);
				}
			}
		}
		
	}

	/**
	 * 后台添加担保用户字段校验
	 * 
	 * @author ZhangBiao
	 * @date 2016年9月26日
	 */
	public void checkVouchManage() {
		// 手机号不能为空
		if(StringUtils.isBlank(getMobile())){
			throw new BussinessException(ResourceUtils.get(ResourceConstant.USER_MOBILE_ISEMPTY), BussinessException.TYPE_JSON);
		}
		// 手机号格式错误
		if(!StringUtils.isPhone(this.getMobile())) {
			throw new BussinessException(ResourceUtils.get(ResourceConstant.USER_MOBILE_ERROR), BussinessException.TYPE_JSON);
		}
		// 联系人姓名长度错误
		if(StringUtils.isNotBlank(getContacts()) && (getContacts().length() < Constant.INT_TWO || getContacts().length() > Constant.INT_TEN)){
			throw new BussinessException(ResourceUtils.get(ResourceConstant.USER_CONTACTS_LENGHT_ERROR,Constant.INT_TWO,Constant.INT_TEN), BussinessException.TYPE_JSON);
		}
		// 联系邮箱格式错误
		if(StringUtils.isNotBlank(getEmail()) && !StringUtils.isEmail(getEmail())){
			throw new BussinessException(ResourceUtils.get(ResourceConstant.USER_EMAIL_ERROR,Constant.INT_TWO,Constant.INT_TEN), BussinessException.TYPE_JSON);
		}
		// 税务登记证号格式错误
		if(StringUtils.isNotBlank(getTaxRegNo()) && (!NumberUtils.isDigits(getTaxRegNo()) || getTaxRegNo().length() > Constant.INT_FIFTEEN)){
			throw new BussinessException(ResourceUtils.get(ResourceConstant.USER_TAXREGNO_LENGHT_ERROR), BussinessException.TYPE_JSON);
		}
		// 企业固定电话长度错误
		if(StringUtils.isNotBlank(getTelephone()) && getTelephone().length() > Constant.INT_THIRTEEN){
			throw new BussinessException(ResourceUtils.get(ResourceConstant.USER_TELEPHONE_LENGHT_ERROR), BussinessException.TYPE_JSON);
		}
		// 企业固定电话格式错误
		if (StringUtils.isNotBlank(getTelephone()) && !CheckTelephone()) {
			throw new BussinessException(ResourceUtils.get(ResourceConstant.USER_TELEPHONE_FORMAT_ERROR));
		}
		// 企业地址长度错误
		if(StringUtils.isNotBlank(getAddress()) && getAddress().length() > Constant.INT_FIFTY){
			throw new BussinessException(ResourceUtils.get(ResourceConstant.USER_ADDRESS_LENGHT_ERROR), BussinessException.TYPE_JSON);
		}
		
		//第三方商户号和组织机构代码
		if(StringUtils.isNotBlank(getTppUserCustId())){
			//第三方商户号
			if(getTppUserCustId().length() > Constant.INT_FIFTY){
				throw new BussinessException(ResourceUtils.get(ResourceConstant.USER_TPP_USER_CUST_LENGHT_ERROR), BussinessException.TYPE_JSON);
			}
			//企业名称
			if(!StringUtils.isCompanyName(getCompanyName())){
				throw new BussinessException(ResourceUtils.get(ResourceConstant.COMPANY_NAME_ERROR), BussinessException.TYPE_CLOSE);
			}
			//法人代表
			if(!StringUtils.isChinese(this.getLegalDelegate())){
				throw new BussinessException(ResourceUtils.get(ResourceConstant.USER_REALNAME_ERROR), BussinessException.TYPE_CLOSE);
			}
			//法人身份证号
			if(!StringUtils.isCard(getCertNo())){
				throw new BussinessException(ResourceUtils.get(ResourceConstant.USER_IDNO_ERROR), BussinessException.TYPE_CLOSE);
			}
			//三证
			if(Constant.THREE_CERTIFICATE_YES.equals(getThreeCertificate())){//三证合一
				//统一社会信用代码
				if(!StringUtils.isIntAndChar(getCreditCode(), 18)){
					throw new BussinessException(ResourceUtils.get(ResourceConstant.CREDIT_CODE_ERROR), BussinessException.TYPE_CLOSE);
				}
			}else{//非三证合一
				//营业执照号
				if(!StringUtils.isIntAndChar(getBussinessCode(), 15)){
					throw new BussinessException(ResourceUtils.get(ResourceConstant.BUSSINESS_CODE_ERROR), BussinessException.TYPE_CLOSE);
				}
				//组织机构代码
				if(!StringUtils.isOrgCode(getOrgCode())){
					throw new BussinessException(ResourceUtils.get(ResourceConstant.ORG_CODE_ERROR), BussinessException.TYPE_CLOSE);
				}
			}
		}else{
			//企业名称
			if(StringUtils.isNotBlank(getCompanyName()) && !StringUtils.isCompanyName(getCompanyName())){
				throw new BussinessException(ResourceUtils.get(ResourceConstant.COMPANY_NAME_ERROR), BussinessException.TYPE_CLOSE);
			}
			//法人代表
			if(StringUtils.isNotBlank(getLegalDelegate()) && !StringUtils.isChinese(getLegalDelegate())){
				throw new BussinessException(ResourceUtils.get(ResourceConstant.USER_REALNAME_ERROR), BussinessException.TYPE_CLOSE);
			}
			//法人身份证号
			if(StringUtils.isNotBlank(getCertNo()) && !StringUtils.isCard(getCertNo())){
				throw new BussinessException(ResourceUtils.get(ResourceConstant.USER_IDNO_ERROR), BussinessException.TYPE_CLOSE);
			}
			//三证
			if(Constant.THREE_CERTIFICATE_YES.equals(getThreeCertificate())){//三证合一
				//统一社会信用代码
				if(StringUtils.isNotBlank(getCreditCode()) && !StringUtils.isIntAndChar(getCreditCode(), 18)){
					throw new BussinessException(ResourceUtils.get(ResourceConstant.CREDIT_CODE_ERROR), BussinessException.TYPE_CLOSE);
				}
			}else{//非三证合一
				//营业执照号
				if(StringUtils.isNotBlank(getBussinessCode()) && !StringUtils.isIntAndChar(getBussinessCode(), 15)){
					throw new BussinessException(ResourceUtils.get(ResourceConstant.BUSSINESS_CODE_ERROR), BussinessException.TYPE_CLOSE);
				}
				//组织机构代码
				if(StringUtils.isNotBlank(getOrgCode()) && !StringUtils.isOrgCode(getOrgCode())){
					throw new BussinessException(ResourceUtils.get(ResourceConstant.ORG_CODE_ERROR), BussinessException.TYPE_CLOSE);
				}
			}
		}
	}
	public boolean CheckTelephone(){
		return ValidateUtils.checkTelephone(getTelephone());
	}
	
	public String getThreeCertificate() {
		return threeCertificate;
	}

	public void setThreeCertificate(String threeCertificate) {
		this.threeCertificate = threeCertificate;
	}

	public Operator getInviteOperator() {
		return inviteOperator;
	}

	public void setInviteOperator(Operator inviteOperator) {
		this.inviteOperator = inviteOperator;
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
	public void setLegalDelegate(String legalDelegate) {
		this.legalDelegate = legalDelegate;
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
	public void setCertNo(String certNo) {
		this.certNo = certNo;
	}

	/**
	 * 获取属性modiftType的值
	 * @return modiftType属性值
	 */
	public String getModifyType() {
		return modifyType;
	}

	/**
	 * 设置属性modiftType的值
	 * @param  modiftType属性值
	 */
	public void setModifyType(String modifyType) {
		this.modifyType = modifyType;
	}

	/**
	 * 获取属性registChannel的值
	 * @return registChannel属性值
	 */
	public String getRegistChannel() {
		return registChannel;
	}

	/**
	 * 设置属性registChannel的值
	 * @param  registChannel属性值
	 */
	public void setRegistChannel(String registChannel) {
		this.registChannel = registChannel;
	}

	/**
	 * @return the 冻结功能点
	 */
	public String getFreezeOperation() {
		return freezeOperation;
	}

	/**
	 * @param 冻结功能点 the freezeOperation to set
	 */
	public void setFreezeOperation(String freezeOperation) {
		this.freezeOperation = freezeOperation;
	}

	public int getEncrypt() {
		return encrypt;
	}

	public void setEncrypt(int encrypt) {
		this.encrypt = encrypt;
	}
	
	
}
