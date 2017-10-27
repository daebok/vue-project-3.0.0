package com.rd.ifaes.core.core.util;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import com.rd.ifaes.common.exception.BussinessException;
import com.rd.ifaes.common.util.DateUtils;
import com.rd.ifaes.common.util.SessionUtils;
import com.rd.ifaes.common.util.SpringContextHolder;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.core.core.constant.ConfigConstant;
import com.rd.ifaes.core.core.constant.Constant;
import com.rd.ifaes.core.core.sms.VerifCodeForSMSUtils;
import com.rd.ifaes.core.sys.domain.Message;
import com.rd.ifaes.core.sys.service.MessageService;
/**
 * 数据校验工具类
 * 
 * @author xx
 * @version 2.0
 * @since 2014年1月8日
 */
public class ValidateUtils {
	
	private ValidateUtils() {}
	
	/**
	 * 校验_图片验证码
	 * @param validCode 图片验证码
	 * @return 校验结果 true：通过 false：不通过
	 */
	public static boolean checkValidCode(String validCode) {
		
		String isValidTest = ConfigUtils.getValue(ConfigConstant.CONFIG_ONLINE);  //是否是在线生产环境
		if("0".equals(isValidTest) && ("9999".equals(validCode) )){
			return true;
		}
		
		Object obj = SessionUtils.getSessionAttr(Constant.VALID_CODE);
		if(obj == null){
			return false;
		}
		
		if(StringUtils.isNotBlank(validCode) && validCode.equalsIgnoreCase(obj.toString())){
			// 校验通过，移除session中的图片验证码
			SessionUtils.removeAttribute(Constant.VALID_CODE);
			return true;
		}
		return false;
	}

	/**
	 * 
	 * 校验验证码 发送 间隔时间
	 * @param sendTime
	 * @return
	 */
	public static boolean checkSpaceTime(int sendTime) {
		int nowTime = DateUtils.getNowTime();
		int spaceTime = ConfigUtils.getInt("send_code_space_time");
		if (sendTime > 0 && nowTime - sendTime < spaceTime) {
			throw new BussinessException("验证码已发送，"+(spaceTime - nowTime + sendTime)+"秒内不可再次获取", BussinessException.TYPE_JSON);
		}
		return true;
	}

	/**
	 * 校验_验证码
	 * @param key
	 * @param userName 用户名
	 * @param userId 用户ID
	 * @param email 电子邮件
	 * @param todo
	 * @param code
	 * @return 校验结果 true：通过 false：不通过
	 * @throws Exception 
	 */	
	public static boolean checkCode(String addr, String todo, String code){
		if (StringUtils.isBlank(code)) {
			throw new BussinessException(ResourceUtils.get("common.message.validCodeIsNull"));
		}
		String configOnline = ConfigUtils.getValue(ConfigConstant.CONFIG_ONLINE); 
		if ("0".equals(configOnline)&& ("9999".equals(code) || "E80114CEE8E3704866676F58850D81DA".equals(code))) {
			return true;
		}
		// 发送验证码时间取得
		MessageService messageService = SpringContextHolder.getBean("messageService");
		Message msg = messageService.getNewestSendTime(todo, addr);
		if(msg==null){
			throw new BussinessException("请先获取验证码！");
		}
		Date sendTime = msg.getCreateTime();
		int vtime=ConfigUtils.getInt("verify_code_time");
		if(System.currentTimeMillis() - sendTime.getTime() > vtime * 60 * 1000){
			throw new BussinessException("验证码已过期(有效期" + vtime + "分钟)，请重新获取！");
		}
		if(!msg.getStatus().equals(Constant.FLAG_YES)){
			throw new BussinessException("请输入正确的验证码");
		}

		return VerifCodeForSMSUtils.checkVerifCode(todo, addr, code);
	}

	/**
	 * 校验手机号格式
	 * 
	 * @param phone 手机号
	 * @return 校验结果 true：通过 false：不通过
	 */
	public static boolean isPhone(String phone) {
		Pattern regex = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0-9])|145|147|170|171|173|176|177|178|185)\\d{8}$");
		Matcher matcher = regex.matcher(StringUtils.isNull(phone));
		boolean isMatched = matcher.matches();
		return isMatched;
	}
	
	/**
	 * 判断是否是金额
	 * @param str
	 * @return
	 */
	public static boolean isAmount(Object obj) {
		if(obj==null){
			return false;
		}
		Pattern pattern = Pattern.compile("^((\\d{1,9})|([0]{1}))(\\.(\\d){0,2})?$");    
	    return pattern.matcher(obj.toString()).matches();
	}
	
	/**
	 * 判断是否是整数
	 * @param obj
	 * @return
	 */
	public static boolean isInteger(Object obj) {
		if(obj == null){
			return false;
		}
		Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");    
	    return pattern.matcher(obj.toString()).matches();  
	}
	
	/**
	 * 判断double是否是整数
	 * @param obj
	 * @return
	 */
	public static boolean isIntegerForDouble(double obj) {
		double eps = 1e-10;  // 精度范围
		return obj-Math.floor(obj) < eps;
	}
	
	/**
	 * 
	 * 判断请求是否是异步请求
	 * @param request
	 * @return
	 */
	public static boolean isAjaxRequest(HttpServletRequest request){
		boolean result = false;
		final String requestType = request.getHeader("X-Requested-With");
		if(("XMLHttpRequest").equals(requestType)){
			result = true;
		}
        return result;
	}
	
	/**
	 * 固定电话格式校验
	 * @param telephone
	 * @return
	 */
	public static boolean checkTelephone(String telephone){
		Pattern regex=Pattern.compile("((0\\d{2,3})-?)?(\\d{7,8})(-(\\d{3,}))?$");
		Matcher matcher=regex.matcher(StringUtils.isNull(telephone));
		return matcher.matches();
	}
	
}
