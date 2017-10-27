package com.rd.ifaes.mobile.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.rd.ifaes.common.exception.BussinessException;
import com.rd.ifaes.common.exception.TokenException;
import com.rd.ifaes.common.orm.Page;
import com.rd.ifaes.common.util.DateUtils;
import com.rd.ifaes.common.util.NumberUtils;
import com.rd.ifaes.common.util.SessionUtils;
import com.rd.ifaes.common.util.SpringContextHolder;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.common.util.TokenUtils;
import com.rd.ifaes.common.util.code.BASE64Decoder;
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
import com.rd.ifaes.core.user.domain.User;
import com.rd.ifaes.core.user.domain.UserIdentify;
import com.rd.ifaes.core.user.model.UserFreezeModel;
import com.rd.ifaes.core.user.service.UserFreezeService;
import com.rd.ifaes.core.user.service.UserService;
import com.rd.ifaes.mobile.common.AppException;
import com.rd.ifaes.mobile.common.AppRequestParam;
import com.rd.ifaes.mobile.common.AppResultCode;
import com.rd.ifaes.mobile.common.AppValidateUtil;
import com.rd.ifaes.mobile.model.AppResponse;
import com.rd.ifaes.mobile.model.PagedData;

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
	 * @author xhf
	 * @date 2016年8月29日
	 * @param user
	 * @param receiveAddr
	 * @param messageType
	 */
	protected void sendMessage(final User user, final String receiveAddr, final String messageType){
		if(StringUtils.isNotBlank(messageType)){
			checkSpaceTime(messageType);
			final GetCodeLog msg = new GetCodeLog(user, messageType, receiveAddr);
			msg.doEvent();
			saveSendTime(messageType);
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
	
	
	
	//还款短信验证码获取
	protected void sendRepayMessage(final User user, final String receiveAddr, final String messageType,String repaymentId){
		if(StringUtils.isNotBlank(messageType)){
			checkSpaceTimeByRepay(receiveAddr, repaymentId);
			final GetCodeLog msg = new GetCodeLog(user, messageType, receiveAddr);
			msg.doEvent();
			saveSendTimeByRepay(receiveAddr, repaymentId);
		}
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
	protected void checkSpaceTimeByRegister(final String phoneNum,final String sendIp) {
		int nowTime = DateUtils.getNowTime();
		// 号码缓存
		int phoneNumTime = CacheUtils.getInt(CacheConstant.KEY_PHONECODE_PHONENUM.concat(phoneNum));
		if(phoneNumTime > 0){
			int overplusTime = ConfigUtils.getInt("send_code_space_time") - (nowTime - phoneNumTime);  //下次发送验证码的倒计时
			throw new BussinessException("验证码已发送，"+ overplusTime +"秒内不可再次获取", BussinessException.TYPE_JSON);
		}
	}
	

	/**
	 * 注册短信保存发送时间(缓存)
	 * @author fxl
	 * @date 2016年9月11日
	 * @param phoneNum
	 * @param sendIp
	 */
	protected void saveSendTimeByRegister(final String phoneNum,final String sendIp) {
		int nowTime = DateUtils.getNowTime();
		int spaceTime = ConfigUtils.getInt("send_code_space_time");
		// 号码缓存
		CacheUtils.setIntForPhone(CacheConstant.KEY_PHONECODE_PHONENUM.concat(phoneNum), nowTime, spaceTime);
		// 发送ip缓存
		//CacheUtils.setIntForPhone(CacheConstant.KEY_PHONECODE_SENDIP.concat(sendIp), nowTime, spaceTime);
	}
	/**
	 * 忘记密码保存发送时间(缓存)
	 * @author lgx
	 * @date 2017年02月08日
	 */
	protected void saveSendTimeByRetrievePwd(final String phoneNum) {
		int nowTime = DateUtils.getNowTime();
		int spaceTime = ConfigUtils.getInt("send_code_space_time");
		// 号码缓存
		CacheUtils.setIntForPhone(CacheConstant.KEY_PHONECODE_PHONENUM.concat(phoneNum), nowTime, spaceTime);
	}
	
	/**
	 * 忘记密码发送时间校验(缓存)
	 * @author lgx
	 * @date 2017年02月08日
	 */
	protected void checkSpaceTimeByRetrievePwd(final String phoneNum) {
		int nowTime = DateUtils.getNowTime();
		// 号码缓存
		int phoneNumTime = CacheUtils.getInt(CacheConstant.KEY_PHONECODE_PHONENUM.concat(phoneNum));
		if(phoneNumTime > 0){
			int overplusTime = ConfigUtils.getInt("send_code_space_time") - (nowTime - phoneNumTime);  //下次发送验证码的倒计时
			throw new BussinessException("验证码已发送，"+ overplusTime +"秒内不可再次获取", BussinessException.TYPE_JSON);
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
		//checkSpaceTime(messageType);
		checkSpaceTimeByRetrievePwd(receiveAddr);
		final GetCodeLog msg = new GetCodeLog();
		msg.sendMsg(receiveAddr, messageType, route);
		//saveSendTime(messageType);
		saveSendTimeByRetrievePwd(receiveAddr);
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

	@Resource
	private UserService userService;
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	
	protected boolean regenSessionId = false;

	//public FreezeRuleCheck freezeRuleCheck = (FreezeRuleCheck) Global.getRuleCheck("freeze");

	protected Object dealException(Exception e) {
		logger.info("**********************DEBUG START****************************");
		e.printStackTrace();
		StringWriter sw = new StringWriter();
		e.printStackTrace(new PrintWriter(sw, true));
		String str = sw.toString();
		logger.error("App Exception : " + str);
		logger.info("**********************DEBUG END****************************");
		logger.error(e.toString());
		if ((e instanceof AppException)) {
			return createFailureAppResponse((AppException)e);
		} else if (e instanceof BussinessException||e instanceof TokenException||e instanceof InvocationTargetException) {
			return createFailureAppResponse(AppResultCode.ERROR, e.getMessage());
		} else {
			return createFailureAppResponse(AppResultCode.ERROR, "系统异常");
		}
	}
	protected Object dealExceptiontobond(Exception e) {
		e.printStackTrace();
		StringWriter sw = new StringWriter();
		e.printStackTrace(new PrintWriter(sw, true));
		String str = sw.toString();
		logger.error("App Exception : " + str);
		logger.error(e.toString());
		if ((e instanceof AppException)) {
			return createFailureAppResponse((AppException)e);
		} else if (e instanceof BussinessException||e instanceof TokenException||e instanceof InvocationTargetException) {
			return createFailureAppResponse(AppResultCode.ERROR_BACK, e.getMessage());
		} else {
			return createFailureAppResponse(AppResultCode.ERROR, "系统异常");
		}
	}
	/**
	 * 获取App User对象
	 * 
	 * @return User对象
	 */
	protected User getAppSessionUser(HttpServletRequest request) {
		String userAgent = request.getHeader("User-Agent").toLowerCase();//判断访问来源 是否是微信浏览器
		int source=0;//来源 
		if("android".equals(userAgent)){//安卓
			source=1;
		}else if("iphone".equals(userAgent)){//ios
			source=2;
		}else if(userAgent.contains("android")||userAgent.contains("iphone")||userAgent.contains("micromessenger")){// 手机浏览器
			source=3;
		}
		if(source==3){//微信或者浏览器访问  直接从缓存里面取
			if ((User) SessionUtils.getSessionAttr(Constant.SESSION_USER) == null) {
				throw new AppException(AppResultCode.TOKEN_NOT_EXIT, "请重新登录！");
			}
		return (User) SessionUtils.getSessionAttr(Constant.SESSION_USER);
		}else{//app访问
			String userId = request.getParameter(AppRequestParam.USER_ID);
			if (!AppValidateUtil.checkStrNull(userId)) {
				throw new AppException(AppResultCode.TOKEN_NOT_EXIT, "无效的用户id");
			}
			User user =null;
					
			user=(User) SessionUtils.getSessionAttr(Constant.SESSION_USER);//先从redis缓存中取
		/*	if(user==null){//如果换redis缓存 没有 就查实体对象
			 user = userService.get(userId);
			 SessionUtils.setSessionAttr(Constant.SESSION_USER, user);//重新插入redis缓存
			}*/
			if (user==null) {
				throw new AppException(AppResultCode.TOKEN_NOT_EXIT, "用户不存在");
			}
			return user;
		}
	}
	/**
	 * 隐藏字符串若干位
	 * 
	 * @param str
	 *            源串
	 * @param begin
	 *            开始下标
	 * @param length
	 *            隐藏长度
	 * @return
	 */
	public static String hideStr(String str, int begin, int length) {
		if (str == null || "".equals(str)) {
			return "";
		}
		int size = str.length();
		if (size == 1) {
			return "";
		}
		if (begin > size) {
			begin = size / 2;
		}
		if (begin + length > size) {
			length = size / 3;
		}
		String s1 = str.substring(0, begin);
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < length; i++) {
			sb.append("*");
		}
		String s2 = sb.toString();
		String s3 = str.substring(begin + length);
		return s1 + s2 + s3;
	}

	protected String decodeBase64param(String str) throws IOException {
		String param = str;
		BASE64Decoder bd = new BASE64Decoder();
		byte[] pwd = bd.decodeBuffer(param);
		return new String(pwd, "utf-8");
	}
	@SuppressWarnings("rawtypes")
	protected void fillPageData( PagedData pagedData , Page page ) {
		pagedData.setDataCount(page.getCount());
		pagedData.setTotalPage(page.getTotalPage());
		pagedData.setPage(page.getPage());
		pagedData.setPageSize(page.getRows().size());
	}
	@SuppressWarnings("rawtypes")
	protected void fillPageDatas( PagedData pagedData , Page page ) {
		pagedData.setDataCount(page.getCount());
		pagedData.setTotalPage(page.getTotalPage());
		pagedData.setPage(page.getPage());
		pagedData.setPageSize(page.getPageSize());
	}
	protected Object createSuccessAppResponse( Object data ) {
		AppResponse appResponse = new AppResponse(AppResultCode.SUCCESS, "SUCCESS",data);
		LOGGER.info(JSONObject.toJSONString(appResponse));
		return appResponse;
		}
	protected Object createSuccessAutoCloseAppResponse( Object data ) {
		AppResponse appResponse = new AppResponse(AppResultCode.SUCCESS, "关闭成功",data);
		LOGGER.info(JSONObject.toJSONString(appResponse));
		return appResponse;
		}
	protected Object createSuccessAutoOpenAppResponse( Object data ) {
		AppResponse appResponse = new AppResponse(AppResultCode.SUCCESS, "开启成功",data);
		LOGGER.info(JSONObject.toJSONString(appResponse));
		return appResponse;
		}
	protected Object createPhoneEmailSuccessAppResponse( Object data ) {
		AppResponse appResponse = new AppResponse(AppResultCode.SUCCESS, "已发送验证码",data);
		LOGGER.info(JSONObject.toJSONString(appResponse));
		return appResponse;
		}
	
	protected Object createSuccessAppResponse( String msg ) {
		AppResponse appResponse = new AppResponse(AppResultCode.SUCCESS , msg );
		LOGGER.info(JSONObject.toJSONString(appResponse));
		return appResponse;
	}
	
	protected Object createFailureAppResponse( AppException e ) {
		AppResponse appResponse = new AppResponse(e);
		LOGGER.info(JSONObject.toJSONString(appResponse));
		return appResponse;
	}

	protected Object createFailureAppResponse( int code , String msg ) {
		AppResponse appResponse = new AppResponse(code,msg);
		LOGGER.info(JSONObject.toJSONString(appResponse));
		return appResponse;
	}

	protected Object createFailureAppResponse( int code , String msg , String sessionId ) {
		Map<String,Object> data = new HashMap<String,Object>();
		data.put("sessionId", sessionId);
		return new AppResponse(code,msg,data);
	}

	/**
	 * 校验冻结功能
	 * @param userId
	 */
	public void mobileCheckFreeze(String userId,String freeze){
		final UserFreezeService userFreezeService = (UserFreezeService)SpringContextHolder.getBean("userFreezeService");
		if("recharge".equals(freeze)){//充值
			if(userFreezeService.isFreezed(userId, UserFreezeModel.STATUS_USER_FREEZE_RECHARGE)) 
				throw new AppException(AppResultCode.USER_FREEZE_RECHARGE, ResourceUtils.get(ResourceConstant.RECHARGE_IS_FREEZE));
		}else if("cash".equals(freeze)){//提现
			if(userFreezeService.isFreezed(userId, UserFreezeModel.STATUS_USER_FREEZE_CASH))
			throw new AppException(AppResultCode.USER_FREEZE_CASH, ResourceUtils.get(ResourceConstant.CASH_IS_FREEZE));
		}else if("invest".equals(freeze)){//投资
			if(userFreezeService.isFreezed(userId, UserFreezeModel.STATUS_USER_FREEZE_INVEST))
			throw new AppException(AppResultCode.USER_FREEZE_INVEST, ResourceUtils.get(ResourceConstant.USER_FREEZE_INVEST_FREEZED));
		}else if("realize".equals(freeze)){//变现
			if(userFreezeService.isFreezed(userId, UserFreezeModel.STATUS_USER_FREEZE_REALIZE))
			throw new AppException(AppResultCode.USER_FREEZE_REALIZE, ResourceUtils.get(ResourceConstant.REALIZE_IS_FREEZE));
		}else if("bond".equals(freeze)){//债券转让
			if(userFreezeService.isFreezed(userId, UserFreezeModel.STATUS_USER_FREEZE_BOND))
			throw new AppException(AppResultCode.USER_FREEZE_BOND, ResourceUtils.get(ResourceConstant.BOND_IS_FREEZE));
		}else if("loan".equals(freeze)){//投资
			if(userFreezeService.isFreezed(userId, UserFreezeModel.STATUS_USER_FREEZE_LOAN)) 
			throw new AppException(AppResultCode.USER_FREEZE_LOAN, ResourceUtils.get(ResourceConstant.PRODUCT_BORROWER_LOAN_FREEZE));
		}
	}
}
