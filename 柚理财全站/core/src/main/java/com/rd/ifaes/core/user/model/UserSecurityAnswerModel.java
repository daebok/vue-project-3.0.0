package com.rd.ifaes.core.user.model;


import com.rd.ifaes.common.exception.BussinessException;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.core.core.constant.Constant;
import com.rd.ifaes.core.core.constant.MessageConstant;
import com.rd.ifaes.core.core.constant.ResourceConstant;
import com.rd.ifaes.core.core.util.ResourceUtils;
import com.rd.ifaes.core.core.util.ValidateUtils;
import com.rd.ifaes.core.user.domain.UserSecurityAnswer;

/**
 * entity:UserSecurityAnswer
 * 
 * @author xhf
 * @version 3.0
 * @date 2016-7-28
 */
public class UserSecurityAnswerModel extends UserSecurityAnswer{
	
	private static final long serialVersionUID = 1L;
	//其他自定义属性
	/** 新增密保 */
	/** 密保问题1*/
	private String question1;
	/** 密保问题2*/
	private String question2;
	/** 密保问题3*/
	private String question3;
	/** 密保答案1*/
	private String answer1;
	/** 密保答案2*/
	private String answer2;
	/** 密保答案3*/
	private String answer3;
	
	/**** 重置密保****/
	/** 验证方式*/
	private String checkType;  
	/** 验证码*/
	private String code;       
	/** 用户名*/
	private String userName; 
	/** 手机号*/
	private String mobile;   
	/** 邮箱*/
	private String email;    

	/**
	 * 获取其他自定义属性
	 * @return question1
	 */
	public String getQuestion1() {
		return question1;
	}

	/**
	 * 设置其他自定义属性
	 * @param  question1
	 */
	public void setQuestion1(final String question1) {
		this.question1 = question1;
	}

	/**
	 * 获取密保问题2
	 * @return question2
	 */
	public String getQuestion2() {
		return question2;
	}

	/**
	 * 设置密保问题2
	 * @param  question2
	 */
	public void setQuestion2(final String question2) {
		this.question2 = question2;
	}

	/**
	 * 获取密保问题3
	 * @return question3
	 */
	public String getQuestion3() {
		return question3;
	}

	/**
	 * 设置密保问题3
	 * @param  question3
	 */
	public void setQuestion3(final String question3) {
		this.question3 = question3;
	}

	/**
	 * 获取密保答案1
	 * @return answer1
	 */
	public String getAnswer1() {
		return answer1;
	}

	/**
	 * 设置密保答案1
	 * @param  answer1
	 */
	public void setAnswer1(final String answer1) {
		this.answer1 = answer1;
	}

	/**
	 * 获取密保答案2
	 * @return answer2
	 */
	public String getAnswer2() {
		return answer2;
	}

	/**
	 * 设置密保答案2
	 * @param  answer2
	 */
	public void setAnswer2(final String answer2) {
		this.answer2 = answer2;
	}

	/**
	 * 获取密保答案3
	 * @return answer3
	 */
	public String getAnswer3() {
		return answer3;
	}

	/**
	 * 设置密保答案3
	 * @param  answer3
	 */
	public void setAnswer3(final String answer3) {
		this.answer3 = answer3;
	}

	/**
	 * 获取重置密保
	 * @return checkType
	 */
	public String getCheckType() {
		return checkType;
	}

	/**
	 * 设置重置密保
	 * @param  checkType
	 */
	public void setCheckType(final String checkType) {
		this.checkType = checkType;
	}

	/**
	 * 获取验证码
	 * @return code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * 设置验证码
	 * @param  code
	 */
	public void setCode(final String code) {
		this.code = code;
	}

	/**
	 * 获取用户名
	 * @return userName
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * 设置用户名
	 * @param  userName
	 */
	public void setUserName(final String userName) {
		this.userName = userName;
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
	 * 验证设置密保问题的数据
	 */
	public void validForm() {
		if (StringUtils.isBlank(this.answer1)) {
			throw new BussinessException(ResourceUtils.get("answer.first.is.empty"), BussinessException.TYPE_JSON);
		} else if (StringUtils.isBlank(this.answer2)) {
			throw new BussinessException(ResourceUtils.get("answer.two.is.empty"), BussinessException.TYPE_JSON);
		} else if (StringUtils.isBlank(this.answer3)) {
			throw new BussinessException(ResourceUtils.get("answer.three.is.empty"), BussinessException.TYPE_JSON);
		} else if (StringUtils.isBlank(question1)) {
			throw new BussinessException(ResourceUtils.get("question.first.is.empty"), BussinessException.TYPE_JSON);
		} else if (StringUtils.isBlank(question2)) {
			throw new BussinessException(ResourceUtils.get("question.two.is.empty"), BussinessException.TYPE_JSON);
		} else if (StringUtils.isBlank(question3)) {
			throw new BussinessException(ResourceUtils.get("question.three.is.empty"), BussinessException.TYPE_JSON);
		}else if (this.answer1.length() > Constant.INT_SIXTEN || this.answer2.length() > Constant.INT_SIXTEN || this.answer3.length() > Constant.INT_SIXTEN) {
			throw new BussinessException(ResourceUtils.get(ResourceConstant.SECURITYANSWER_LENGTH_ERROR), BussinessException.TYPE_JSON);
		}else if (question1.equals(question2) || question1.equals(question3) || question2.equals(question3)) {
			throw new BussinessException(ResourceUtils.get("question.is.repeat"), BussinessException.TYPE_JSON);
		}
	}
	
	/**
	 * 重置密保问题
	 * @author xhf
	 * @date 2016年7月28日
	 * @throws Exception
	 */
	public void validResetPwdQuest(){
		String todo="", addr="";
		if(Constant.PASS_VALID_EMAIL.equals(getCheckType())){  //邮箱认证
			todo = MessageConstant.MESSAGE_RESET_QUESTION_EMAILCODE;
			addr = getEmail();
			
		} else if(Constant.PASS_VALID_MOBILE.equals(getCheckType())){ //手机认证
			todo = MessageConstant.MESSAGE_RESET_QUESTION_PHONECODE;
			addr = getMobile();
		}
		
		if (!ValidateUtils.checkCode(addr, todo, getCode())) {
			throw new BussinessException(ResourceUtils.get(ResourceConstant.VALID_CODE_ERROR), BussinessException.TYPE_JSON);
		}
	}
}
