package com.rd.ifaes.web.controller;

import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import com.google.common.collect.Maps;
import com.rd.ifaes.common.constant.UfxConstant;
import com.rd.ifaes.common.exception.BussinessException;
import com.rd.ifaes.common.util.DateUtils;
import com.rd.ifaes.common.util.EncodeUtils;
import com.rd.ifaes.common.util.NumberUtils;
import com.rd.ifaes.common.util.SessionUtils;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.common.util.TokenUtils;
import com.rd.ifaes.common.util.resource.UserResource;
import com.rd.ifaes.core.base.web.BaseController;
import com.rd.ifaes.core.core.constant.CacheConstant;
import com.rd.ifaes.core.core.constant.Constant;
import com.rd.ifaes.core.core.constant.ResourceConstant;
import com.rd.ifaes.core.core.sms.msg.GetCodeLog;
import com.rd.ifaes.core.core.util.CacheUtils;
import com.rd.ifaes.core.core.util.ConfigUtils;
import com.rd.ifaes.core.core.util.ResourceUtils;
import com.rd.ifaes.core.core.util.ValidateUtils;
import com.rd.ifaes.core.tpp.exception.UfxException;
import com.rd.ifaes.core.tpp.model.ufx.UfxBaseModel;
import com.rd.ifaes.core.user.domain.User;
import com.rd.ifaes.core.user.domain.UserIdentify;

public abstract class WebController extends BaseController {
	
	/**
	 * 校验异常信息
	 */
	public String getBindErrors(final BindingResult bindingResult){
		final StringBuilder errorStr = new StringBuilder();
		final List<ObjectError> errors = bindingResult.getAllErrors();
		final int errorLen = errors.size()-1;
		for (int i = errorLen; i >=0 ; i--) {
			errorStr.append(errors.get(i).getDefaultMessage()).append("<br>");
		}
		return errorStr.substring(0, errorStr.length()-4);		
	}
	
	/**
	 * 校验出现错误，返回错误信息
	 * @param bindingResult
	 * @param response
	 */
	public Map<String, Object> renderBindingResult(final BindingResult bindingResult){
		Map<String, Object> result=null;
		if (bindingResult.hasErrors()) {  
			result = renderResult(false, getBindErrors(bindingResult));
	    }
		return result;		
	}
	
	
	/**
	 * 获取Session中的用户对象
	 * 
	 * @return
	 */
	protected User getSessionUser() {
		return (User) SessionUtils.getSessionAttr(Constant.SESSION_USER);
	}
	
	@Override
	public String getUserId() {
		return getSessionUser().getUuid();
	}
	
	/**
	 * 获取Session中的用户认证信息
	 * 
	 * @return
	 */
	protected UserIdentify getSessionUserIdentify() {
		return (UserIdentify) SessionUtils.getSessionAttr(Constant.SESSION_USER_IDENTIFY);
	}

	/**
	 * 获取Session中的用户对象
	 * 
	 * @return
	 */
	protected String getUserName() {
		String userName=null;
		final User user = getSessionUser();
		if(user != null){
			userName = user.getUserName();
		}
		return userName;
	}
	
	/**
	 * 
	 * 获得map参数对象
	 * @author xhf
	 * @date 2016年8月26日
	 * @param request
	 * @return
	 */
	protected Map<String,Object> getParamsMap(final HttpServletRequest request){
		final Map<String, Object> params = Maps.newHashMap();
		final Enumeration<String> paramsName = request.getParameterNames();
		while (paramsName.hasMoreElements()) {
			final String name = paramsName.nextElement();
			params.put(name, request.getParameter(name));
		}
		return params;
	}
		
	/**
	 * 
	 * 发送消息(短信、邮件、站内信)
	 * 验证码校验是type+addr，而获取时间验证为addr+type
	 * @author xhf
	 * @date 2016年8月29日
	 * @param user
	 * @param receiveAddr
	 * @param messageType
	 */
	protected void sendMessage(final User user, final String receiveAddr, final String messageType){
		if(StringUtils.isNotBlank(receiveAddr) && StringUtils.isNotBlank(messageType)){
			checkSpaceTime("sendType:"+receiveAddr+messageType);
			final GetCodeLog msg = new GetCodeLog(user, messageType, receiveAddr);
			msg.doEvent();
			saveSendTime("sendType:"+receiveAddr+messageType);
		}
	}
	
	protected void sendRepayMessage(final User user, final String receiveAddr, final String messageType,String repaymentId){
		if(StringUtils.isNotBlank(messageType)){
			checkSpaceTimeByRepay(receiveAddr, repaymentId);
			final GetCodeLog msg = new GetCodeLog(user, messageType, receiveAddr);
			msg.doEvent();
			saveSendTimeByRepay(receiveAddr, repaymentId);
		}
	}
	
	/**
	 * 
	 * 非在线用户发送消息(短信、邮件) 
	 * @author xxb
	 * @date 2016年10月17日
	 * @param receiveAddr
	 * @param messageType
	 */
	protected void sendMessage(final String receiveAddr, final String messageType, final String route){
		if(StringUtils.isNotBlank(receiveAddr) && StringUtils.isNotBlank(messageType)){
			checkSpaceTime("sendType:"+receiveAddr+messageType);
			final GetCodeLog msg = new GetCodeLog();
			msg.sendMsg(receiveAddr, messageType, route);
			saveSendTime("sendType:"+receiveAddr+messageType);
		}
	}
	
	/**
	 * 
	 * 根据日期类型获得相应的时间
	 * @author xhf
	 * @date 2016年8月29日
	 * @param dateType
	 * @return
	 */
	protected String getDateTypeTime(final String dateType){
		Date dateTypeTime=null; //默认全部
		if(StringUtils.isNotBlank(dateType)){
			final Date now = DateUtils.getNow();
			if(Constant.DATE_TYPE_WEEK.equals(dateType)){  //近7天
				dateTypeTime = DateUtils.rollDay(now, -7);
			} else if(Constant.DATE_TYPE_MONTH.equals(dateType)){   //近1月
				dateTypeTime = DateUtils.rollMon(now, -1);
			} else if(Constant.DATE_TYPE_QUARTER.equals(dateType)){ //近3月
				dateTypeTime = DateUtils.rollMon(now, -3);
			} else if(Constant.DATE_TYPE_YEAR.equals(dateType)){    //近1年
				dateTypeTime = DateUtils.rollYear(now, -1);
			}
		}
		return dateTypeTime==null? null:DateUtils.getDateEnd(dateTypeTime);
	}
	
	/**
	 * 获得来源网址
	 * @author xhf
	 * @date 2016年8月30日
	 * @return
	 */
	protected String getResourceUrl(final HttpServletRequest request){
		String resourceUrl=null;
		String requestUrl = request.getHeader("Referer");
		if (requestUrl != null){
			String hostUrl = null;
			Pattern p = Pattern.compile("(http://|ftp://|https://|www){0,1}[^\u4e00-\u9fa5\\s]*?\\.(com|net|cn|me|tw|fr)");
			Matcher m = p.matcher(requestUrl);
			if(m.find()){
				hostUrl = m.group(0);
			}else {
				Pattern  p1 = Pattern.compile("((http://|ftp://|https://){0,1}[localhost|\\d|\\.|:]*?)/");
				Matcher  m1 = p1.matcher(requestUrl);
				if(m1.find()){
					hostUrl =m1.group(1);
				}
			}
			if(hostUrl != null){
				resourceUrl = requestUrl.substring(hostUrl.length());
			}
		}
		return resourceUrl;
	}
	
	/**
	 * 同步回调异常处理
	 * @author fxl
	 * @date 2016年10月24日
	 */
	protected void checkErrorReturn(UfxBaseModel model) {
		boolean ret = model.validSign(model);
		if (ret) {// 验签成功
			LOGGER.info("同步回调进来，订单号：{}，返回状态：{}", model.getOrderNo(), EncodeUtils.urlDecode(model.getRespCode()));
			if (!UfxConstant.UFX_CODE_SUCCESS.equals(model.getRespCode())) {
				throw new UfxException(model.getReqExt(), BussinessException.TYPE_CLOSE);
			}
		} else {// 验签失败
			LOGGER.error("同步回调验签失败，订单号：{}", model.getOrderNo());
			throw new UfxException(ResourceUtils.get(ResourceConstant.TPP_NOTIFY_SIGN_FAIL), BussinessException.TYPE_CLOSE);
		}
	}
	
	/**
	 * 
	 * 保存验证码发送时间
	 * @param name
	 */
	protected void saveSendTime(final String name) {
		SessionUtils.setSessionAttr(name, DateUtils.getNowTime());
	}
	
	/**
	 * 
	 * 校验 验证码发送时间 是否在间隔时间内
	 * @param name
	 * @return
	 */
	protected void checkSpaceTime(final String name) {
		final int sendTime = NumberUtils.getInt(StringUtils.isNull(SessionUtils.getSessionAttr(name)));
		ValidateUtils.checkSpaceTime(sendTime);
		SessionUtils.removeAttribute(name);
	}
	/**
	 * 检查用户有没有登录私有方法
	 * @author QianPengZhan
	 * @date 2016年8月23日
	 */
	protected User  checkUserNotLogin(){
		final User user = SessionUtils.getSessionUser();
		if (user == null) {
			throw new BussinessException(ResourceUtils.get(UserResource.USER_NOT_LOGIN), BussinessException.TYPE_CLOSE);
		}
		return user;
	}
	
	/**
	 * 前台设置页面token  返回<input type='hidden' name='%s' value='%s'/>
	 * @author QianPengZhan
	 * @date 2016年9月13日
	 */
	protected String setToken(final String tokenName,final HttpServletRequest request){
		String token = StringUtils.EMPTY;
		if (StringUtils.isNotBlank(tokenName)) {
			TokenUtils.setToken(request.getSession(), tokenName);
			token = (String)request.getSession().getAttribute(tokenName);
			LOGGER.debug("TOKEN[{}]发放成功:{}" ,tokenName, token);
		}
		request.setAttribute("tokenName", tokenName);
		request.setAttribute("tokenValue", token);
		return token;
	}
	

	/**
	 * 注册短信保存发送时间(缓存)
	 * @author fxl
	 * @date 2016年9月11日
	 * @param phoneNum
	 * @param sendIp
	 */
	protected void savePhoneCodeSendTime(final String phoneNum) {
		int nowTime = DateUtils.getNowTime();
		int spaceTime = ConfigUtils.getInt("send_code_space_time");
		// 号码缓存
		CacheUtils.setIntForPhone(CacheConstant.KEY_PHONECODE_PHONENUM.concat(phoneNum), nowTime, spaceTime);
		// 发送ip缓存
		//CacheUtils.setIntForPhone(CacheConstant.KEY_PHONECODE_SENDIP.concat(sendIp), nowTime, spaceTime);
	}
	
	/**
	 * 还款短信保存发送时间(缓存)
	 * @author fxl
	 * @date 2016年11月3日
	 * @param phoneNum
	 * @param repaymentId
	 */
	protected void saveSendTimeByRepay(final String phoneNum,final String repaymentId) {
		int nowTime = DateUtils.getNowTime();
		int spaceTime = ConfigUtils.getInt("send_code_space_time");
		// 号码缓存
		CacheUtils.setIntForPhone(CacheConstant.KEY_PHONECODE_PHONENUM.concat(phoneNum).concat(repaymentId), nowTime, spaceTime);
	}
	
	protected void checkSpaceTimeByRepay(final String phoneNum,final String repaymentId) {
		int nowTime = DateUtils.getNowTime();
		// 号码缓存
		int phoneNumTime = CacheUtils.getInt(CacheConstant.KEY_PHONECODE_PHONENUM.concat(phoneNum).concat(repaymentId));
		if(phoneNumTime > 0){
			int overplusTime = ConfigUtils.getInt("send_code_space_time") - (nowTime - phoneNumTime);  //下次发送验证码的倒计时
			throw new BussinessException("验证码已发送，"+ overplusTime +"秒内不可再次获取", BussinessException.TYPE_JSON);
		}
	}
	
	/**
	 * 注册短信间隔校验(缓存)
	 * @author fxl
	 * @date 2016年9月11日
	 * @param name
	 */
	protected void checkPhoneCodeSpaceTime(final String phoneNum) {
		int nowTime = DateUtils.getNowTime();
		// 号码缓存
		int phoneNumTime = CacheUtils.getInt(CacheConstant.KEY_PHONECODE_PHONENUM.concat(phoneNum));
		if(phoneNumTime > 0){
			int overplusTime = ConfigUtils.getInt("send_code_space_time") - (nowTime - phoneNumTime);  //下次发送验证码的倒计时
			throw new BussinessException("验证码已发送，"+ overplusTime +"秒内不可再次获取", BussinessException.TYPE_JSON);
		}
	}
	
}
