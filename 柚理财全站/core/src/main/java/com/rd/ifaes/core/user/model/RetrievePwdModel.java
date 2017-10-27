package com.rd.ifaes.core.user.model;

import com.rd.ifaes.common.exception.BussinessException;
import com.rd.ifaes.common.util.MD5Utils;
import com.rd.ifaes.common.util.SessionUtils;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.common.util.resource.UserResource;
import com.rd.ifaes.core.core.constant.Constant;
import com.rd.ifaes.core.core.constant.MessageConstant;
import com.rd.ifaes.core.core.constant.ResourceConstant;
import com.rd.ifaes.core.core.util.ResourceUtils;
import com.rd.ifaes.core.core.util.ValidateUtils;

/**
 * entity:RetrievePwdModel
 * 找回密码数据对象
 * @author xxb
 * @version 3.0
 * @date 2016-10-17
 */
public class RetrievePwdModel {
	
	public static final String TYPE_PHONE = "1";
	public static final String TYPE_EMAIL = "2";
	/**
	 * 找回类型
	 */
	private String type;
	/**
	 * 途径（手机号码、邮箱地址）
	 */
	private String pathWay;
	/**
	 * 图形验证码
	 */
	private String imgValidCode;
	/**
	 * 动态验证码
	 */
	private String dynamicValidCode;
	/**
	 * 密码
	 */
	private String password;
	/**
	 * 确认密码
	 */
	private String confirmPassword;	
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getPathWay() {
		return pathWay;
	}

	public void setPathWay(String pathWay) {
		this.pathWay = pathWay;
	}

	public String getDynamicValidCode() {
		return dynamicValidCode;
	}

	public void setDynamicValidCode(String dynamicValidCode) {
		this.dynamicValidCode = dynamicValidCode;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	public String getImgValidCode() {
		return imgValidCode;
	}

	public void setImgValidCode(String imgValidCode) {
		this.imgValidCode = imgValidCode;
	}

	/**
	 * 
	 * 手机验证码校验
	 * @param  
	 * @return void
	 * @author xxb
	 * @date 2016年10月17日
	 */
	public void checkDynamicValidCode(){
		final String dynamicValidCode = this.getDynamicValidCode();
		final String pathWay = this.getPathWay();  //手机号码或邮箱地址
		//手机、邮箱验证码校验
		if(TYPE_EMAIL.equals(this.getType())){
			if (!ValidateUtils.checkCode(pathWay, MessageConstant.MESSAGE_RETRIEVE_PASS_EMAILCODE, dynamicValidCode)) {
				throw new BussinessException(ResourceUtils.get(UserResource.DYNAMIC_VALIDCODE_ERROR), BussinessException.TYPE_JSON);
			}
		}else if(TYPE_PHONE.equals(this.getType())){
			if (!ValidateUtils.checkCode(pathWay, MessageConstant.MESSAGE_RETRIEVE_PASS_PHONECODE, dynamicValidCode)) {
				throw new BussinessException(ResourceUtils.get(UserResource.DYNAMIC_VALIDCODE_ERROR), BussinessException.TYPE_JSON);
			}
		}else{
			throw new BussinessException(ResourceUtils.get(UserResource.DYNAMIC_VALIDCODE_ERROR), BussinessException.TYPE_JSON);
		}
	}
	
	/**
	 * 
	 * 基本信息校验
	 * @param  
	 * @return void
	 * @author xxb
	 * @date 2016年10月17日
	 */
	public void checkBasicInfo(){
		if(!TYPE_PHONE.equals(type) && !TYPE_EMAIL.equals(type) ){
			//提示参数非法
			throw new BussinessException(ResourceUtils.get(ResourceConstant.PASS_RETRIEVE_TYPE_ERROR), 
					BussinessException.TYPE_JSON);
		}
		//手机号码校验
		if (TYPE_PHONE.equals(type)) {
			if (StringUtils.isBlank(this.getPathWay())) {
				throw new BussinessException(ResourceUtils.get(ResourceConstant.USER_MOBILE_ISEMPTY),
						BussinessException.TYPE_JSON);
			}
			if (!StringUtils.isPhone(pathWay)) {
				throw new BussinessException(ResourceUtils.get(ResourceConstant.MOBILE_STYLE_ERROR),
						BussinessException.TYPE_JSON);
			}
		}
		//邮箱校验
		if(TYPE_EMAIL.equals(type)){
			if (StringUtils.isBlank(this.getPathWay())) {
				throw new BussinessException(ResourceUtils.get(ResourceConstant.EAMIL_IS_EMPTY),
						BussinessException.TYPE_JSON);
			}
			if (!StringUtils.isEmail(pathWay)) {
				throw new BussinessException(ResourceUtils.get(ResourceConstant.EMAIL_FORMAT_ERROR),
						BussinessException.TYPE_JSON);
			}
		}
		
	}
	
	/**
	 * 图片验证码
	 * @param  
	 * @return void
	 * @author xxb
	 * @date 2016年10月17日
	 */
	public void checkImgValidCode() {

		if (StringUtils.isBlank(this.getImgValidCode())) {
			throw new BussinessException(ResourceUtils.get(ResourceConstant.VALID_CODE_NOTEMPTY),
					BussinessException.TYPE_JSON);
		}
		if (!ValidateUtils.checkValidCode(this.getImgValidCode())) {
			throw new BussinessException(ResourceUtils.get(ResourceConstant.VALID_CODE_ERROR),
					BussinessException.TYPE_JSON);
		}
	}
	
	/**
	 * 校验密码(密码校验规则参考用户注册时密码规则实现)
	 * @param  
	 * @return void
	 * @author xxb
	 * @date 2016年10月17日
	 */
	public void checkPassword(){
		if (StringUtils.isBlank(this.getPassword())) {
			throw new BussinessException(ResourceUtils.get(ResourceConstant.USER_PWD_EMPTY),
					BussinessException.TYPE_JSON);
		}
		if (StringUtils.isBlank(this.getConfirmPassword())) {
			throw new BussinessException(ResourceUtils.get(ResourceConstant.CONFIRM_PWD_IS_EMPTY),
					BussinessException.TYPE_JSON);
		}
		// 密码不能含有空格
		if (!(this.getPassword().replace(" ", "")).equals(getPassword())) {
			throw new BussinessException(ResourceUtils.get(UserResource.USER_PASS_HAVA_SPACE),
					BussinessException.TYPE_JSON);
		}
		// 密码不能含有中文
		if (StringUtils.ishaveChinese(getPassword())) {
			throw new BussinessException(ResourceUtils.get(UserResource.USER_PASS_HAVA_CHINESE),
					BussinessException.TYPE_JSON);
		}
		// 密码规则正则校验
		if (!StringUtils.isPwd(getPassword())) {
			throw new BussinessException(ResourceUtils.get(UserResource.USER_PASS_ERROR), BussinessException.TYPE_JSON);
		}
		// 判断确认密码是否一致
		if (!this.getPassword().equals(this.getConfirmPassword())) {
			throw new BussinessException(ResourceUtils.get(ResourceConstant.OPERATOR_PWD_AGAIN_ERROR),
					BussinessException.TYPE_JSON);
		}
	}
	
	/**
	 * 校验找回密码标识
	 */
	public void checkRetrievepwdSign(){
		String retrievepwdSign = (String)SessionUtils.getSessionAttr(Constant.RETRIEVEPASS_SIGN);
		if(StringUtils.isBlank(retrievepwdSign) || !retrievepwdSign.equals(MD5Utils.encode(this.getPathWay() + MessageConstant.MESSAGE_RETRIEVE_PASS_PHONECODE))){
			throw new BussinessException(ResourceUtils.get(ResourceConstant.GLOBAL_MSG_ILLEGAL), BussinessException.TYPE_JSON);
		}
	}
	
}
