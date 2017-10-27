package com.rd.ifaes.mobile.controller.member;

import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.util.CollectionUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Maps;
import com.rd.ifaes.common.annotation.TokenValid;
import com.rd.ifaes.common.constant.TokenConstants;
import com.rd.ifaes.common.dict.DictTypeEnum;
import com.rd.ifaes.common.exception.BussinessException;
import com.rd.ifaes.common.util.MD5Utils;
import com.rd.ifaes.common.util.SessionUtils;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.common.util.TokenUtils;
import com.rd.ifaes.common.util.ip.IPUtil;
import com.rd.ifaes.core.core.constant.Constant;
import com.rd.ifaes.core.core.constant.MessageConstant;
import com.rd.ifaes.core.core.constant.ResourceConstant;
import com.rd.ifaes.core.core.util.ConfigUtils;
import com.rd.ifaes.core.core.util.DictUtils;
import com.rd.ifaes.core.core.util.ResourceUtils;
import com.rd.ifaes.core.tpp.model.jx.JxAutoBidAuthPlusModel;
import com.rd.ifaes.core.tpp.model.jx.JxPasswordResetPlusModel;
import com.rd.ifaes.core.tpp.model.jx.JxPasswordSetModel;
import com.rd.ifaes.core.tpp.model.ufx.UfxHelper;
import com.rd.ifaes.core.tpp.model.ufx.UfxLoginModel;
import com.rd.ifaes.core.tpp.util.JxConfig;
import com.rd.ifaes.core.user.domain.User;
import com.rd.ifaes.core.user.domain.UserCache;
import com.rd.ifaes.core.user.domain.UserCompanyInfo;
import com.rd.ifaes.core.user.domain.UserIdentify;
import com.rd.ifaes.core.user.domain.UserSecurityAnswer;
import com.rd.ifaes.core.user.model.UserCacheModel;
import com.rd.ifaes.core.user.model.UserModel;
import com.rd.ifaes.core.user.model.UserSecurityAnswerModel;
import com.rd.ifaes.core.user.service.UserAuthSignLogService;
import com.rd.ifaes.core.user.service.UserCacheService;
import com.rd.ifaes.core.user.service.UserCompanyInfoService;
import com.rd.ifaes.core.user.service.UserIdentifyService;
import com.rd.ifaes.core.user.service.UserSecurityAnswerService;
import com.rd.ifaes.core.user.service.UserService;
import com.rd.ifaes.mobile.controller.WebController;

/**
 * 账户管理-基本信息-安全设置
 * 
 * @author xhf
 * @version 3.0
 * @since 2016年6月7日
 */
@Controller
public class SecurityController extends WebController {
	/**
	 * 问题列表QUESTION_LIST
	 */
	private static final String QUESTION_LIST = "questionList";
	/**
	 * 实名认证状态 REALNAME_STATUS
	 */
	private static final String REALNAME_STATUS = "realNameStatus";
	/**
	 * 是否担保IS_VOUCH
	 */
	private static final String IS_VOUCH = "isVouch";
	/**
	 * 用户基础类
	 */
	@Resource
	private transient UserService userService;
	/**
	 * 用户关联信息服务类
	 */
	@Resource
	private transient UserCacheService userCacheService;
	/**
	 * 认证信息处理类
	 */
	@Resource
	private transient UserIdentifyService identifyService;
	/**
	 * 授权认证类
	 */
	@Resource
	private transient UserAuthSignLogService authSignService;
	/**
	 * 密保问题处理类
	 */
	@Resource
	private transient UserSecurityAnswerService secAnswerService;
	/**
	 * 企业信息处理类
	 */
	@Resource
	private transient UserCompanyInfoService companyService;

	/**
	 * 前往本地开户页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "/member/security/realNameIdentify")
	public String realNameIdentify(final HttpServletRequest request) {
		final UserCache userCache = (UserCache) SessionUtils
				.getSessionAttr(Constant.SESSION_USER_CACHE);
		if (!ObjectUtils.isEmpty(userCache)) {
			request.setAttribute(IS_VOUCH, userCache.getUserNature());
		}

		// 跳转页面，默认企业开户页面
		String goPage = "member/security/companyIdentify";
		if (UserCache.USER_NATURE_PERSON.equals(userCache.getUserNature())) { // 个人开户页面
			goPage = "member/security/userIdentify";
		}
		return goPage;
	}

	/**
	 * 获得认证结果-针对开户业务
	 * 
	 * @return
	 */
	@RequestMapping(value = "/member/security/getIdentifyResult")
	@ResponseBody
	public Object getIdentifyResult() {
		final User sessionUser = SessionUtils
				.getSessionUser(BussinessException.TYPE_JSON);
		final User user = userService.get(sessionUser.getUuid());
		final UserCache userCache = userCacheService.findByUserId(sessionUser
				.getUuid());
		final UserIdentify userIdentify = identifyService
				.findByUserId(sessionUser.getUuid());

		// 更新缓存
		SessionUtils.setSessionAttr(Constant.SESSION_USER, user);
		SessionUtils.setSessionAttr(Constant.SESSION_USER_IDENTIFY,
				userIdentify);

		// 页面参数
		final Map<String, Object> data = Maps.newHashMap();
		data.put(RESULT_NAME, true);
		data.put("tppUserCustId", user.getTppUserCustId());
		data.put("tpp_login_url", Constant.TPP_LOGIN_URL);

		// 审核信息
		if (UserCache.USER_NATURE_PERSON.equals(userCache.getUserNature())) {
			data.put(REALNAME_STATUS, userIdentify.getRealNameStatus());
			data.put("realNameVerifyTime", userIdentify.getRealNameVerifyTime());
		} else {
			final UserCompanyInfo userCompanyInfo = companyService
					.findByUserId(sessionUser.getUuid());
			data.put(REALNAME_STATUS, userCompanyInfo.getAuditStatus());
			data.put("realNameVerifyTime", userCompanyInfo.getAuditTime());
			data.put("auditDesc", userCompanyInfo.getAuditMessage());
		}

		return data;
	}

	/**
	 * 获得用户认证信息
	 * 
	 * @return
	 */
	@RequestMapping(value = "/member/security/getIdentifyInfo")
	@ResponseBody
	public Object getIdentifyInfo() {
		final User sessionUser = SessionUtils
				.getSessionUser(BussinessException.TYPE_JSON);
		final UserIdentify userIdentify = identifyService
				.findByUserId(sessionUser.getUuid());
		SessionUtils.setSessionAttr(Constant.SESSION_USER_IDENTIFY,
				userIdentify);

		// 页面参数
		final Map<String, Object> data = Maps.newHashMap();
		data.put(RESULT_NAME, true);
		data.put(REALNAME_STATUS, userIdentify.getRealNameStatus());
		data.put("authSignStatus", userIdentify.getAuthSignStatus());
		data.put("emailStatus", userIdentify.getEmailStatus());
		return data;
	}

	/**移动端
	 * 资金托管账户开户-个人
	 * 
	 * @return
	 * @throws Exception
	 */

	@RequestMapping(value = "/app/member/security/tppRegister")
	@ResponseBody
	public Object tppRegisger(final UserCacheModel cache,final HttpServletRequest request,final Model model) {
		Object obj=null;
		LOGGER.info("[实名的信息,姓名：{},身份证号码：{}]",cache.getRealName(),cache.getIdNo());
		try {
			cache.setIp(IPUtil.getRemortIP(request));
			userCacheService.tppRealName(cache);
			obj=createSuccessAppResponse("开户成功");
		} catch (Exception e) {
			obj=dealException(e);
		}
		return obj;
	}

	// ***********************************************登录密码********************************************************

	/**
	 * 修改登录密码页
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/member/security/modifyPwd")
	public String modifyPwd(final Model model){
		initUserNature(null,model);
		return "/member/security/modifyPwd";
	}

	/**
	 * 修改登录密码
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/app/member/security/doModifyPwd")
	@ResponseBody
	public Object doModifyPwd(UserModel model,final HttpServletRequest request) {
		Object obj=null;
		try {
			@SuppressWarnings("unused")
			User user = getAppSessionUser(request);
			model.setLoginPwd(decodeBase64param(URLDecoder.decode(model.getLoginPwd(), "UTF-8")));
			model.setNewLoginPwd(decodeBase64param(URLDecoder.decode(model.getNewLoginPwd(), "UTF-8")));
			model.setConfirmPwd(decodeBase64param(URLDecoder.decode(model.getConfirmPwd(), "UTF-8")));
			userService.modifyPwd(model);
		obj=createSuccessAppResponse("登录密码修改成功");
		} catch (Exception e) {
			obj=dealException(e);
		}
		return obj;
	}

	// **********************************************邮箱********************************************************
	/**
	 * 移动端 绑定邮箱-获取验证码
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/app/member/security/bindEmailCode")
	@ResponseBody
	public Object bindEmailCode(final String email,HttpServletRequest request) {
		Object obj=null;
		Map<String, Object> data = new HashMap<String, Object>();
		try {
			User user = getAppSessionUser(request);
			final UserModel model = new UserModel();
			model.setUuid(user.getUuid());
			model.setEmail(URLDecoder.decode(email));
			model.checkEmail();
			sendMessage(user, model.getEmail(),
					MessageConstant.MESSAGE_BIND_EMAIL);
			data.put("emailBindToken", TokenUtils.setToken(request.getSession(), TokenConstants.TOKEN_EMAIL_BIND));
			obj=createPhoneEmailSuccessAppResponse(data);
		} catch (Exception e) {
			obj=dealException(e);
		}
		return obj;
	}

	/**
	 * 移动端 绑定邮箱
	 * 
	 * @return
	 */
	@RequestMapping(value = "/app/member/security/doBindEmail")
	@ResponseBody
	@TokenValid(value=TokenConstants.TOKEN_EMAIL_BIND,dispatch=true)
	public Object doBindEmail(final UserModel model,HttpServletRequest request) {
		Object obj=null;
		try {
			model.setEmail(URLDecoder.decode(model.getEmail()));
			@SuppressWarnings("unused")
			User user = getAppSessionUser(request);
			userService.modifyEmail(model);
			obj=createSuccessAppResponse("邮箱绑定成功！");
		} catch (Exception e) {
			obj=dealException(e);
		}
		return obj;
	}

	/**
	 * 获得用户验证信息
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/member/security/getCheckUserInfo")
	@ResponseBody
	public Object getCheckUserInfo(final String questionFlag) {
		final User user = SessionUtils.getSessionUser();
		final UserCache userCache = (UserCache) SessionUtils
				.getSessionAttr(Constant.SESSION_USER_CACHE);
		final UserIdentify userIdentify = identifyService.findByUserId(user
				.getUuid());

		final Map<String, Object> data = Maps.newHashMap();
		data.put(RESULT_NAME, true);
		data.put("mobile", user.getHideMobile());
		data.put("email", user.getEmail());
		data.put("userNature", userCache.getUserNature());
		data.put(REALNAME_STATUS, userIdentify.getRealNameStatus());
		data.put(QUESTION_LIST,
				secAnswerService.findUserPwdQuestion(user.getUuid()));
		return data;
	}

	/**
	 * 移动端 修改邮箱-获取验证码
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/app/member/security/modifyEmailCode")
	@ResponseBody
	public Object modifyEmailCode(final String checkType,HttpServletRequest request) {//checkType 01 手机验证 02 邮箱验证
		Object obj=null;
		try {
			User user = getAppSessionUser(request);
			String messageType = MessageConstant.MESSAGE_MODIFY_EMAIL_PHONECODE;
			String addr = user.getMobile();
			if (Constant.EMAIL_VALID_EMAIL.equals(checkType)) {
				messageType = MessageConstant.MESSAGE_MODIFY_EMAIL_EMAILCODE;
				addr = user.getEmail();
			}
			sendMessage(user, addr, messageType);
			obj=createSuccessAppResponse("验证码已发送");
		} catch (Exception e) {
			obj=dealException(e);
		}
		return obj;
	}

	/**移动端
	 * 修改邮箱
	 * 
	 * @return
	 */
	@RequestMapping(value = "/app/member/security/doModifyEmail")
	@ResponseBody
	public Object doModifyEmail(final UserModel model,HttpServletRequest request) {//checkType 01 手机验证 02 邮箱验证
		Object obj=null;
		Map<String, Object> data = new HashMap<String, Object>();
		try {
			User user = getAppSessionUser(request);
			model.setUserName(user.getUserName());
			model.setUuid(user.getUuid());
			model.setEmail(user.getEmail());
			model.setCheckType(model.getCheckType());//默认邮箱认证
			model.validModifyEmail();
			data.put("modifyEmailSign", model.getModifyEmailSign());
			obj=createSuccessAppResponse(data);
		} catch (Exception e) {
			obj=dealException(e);
		}
		return obj;
	}

	// **********************************************邮箱********************************************************
	// **********************************************手机********************************************************

	/**移动端
	 * 绑定手机-获取验证码
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/app/member/security/bindPhoneCode")
	@ResponseBody
	public Object bindPhoneCode(final String mobile,HttpServletRequest request) {//mobile 01 手机验证 02 邮箱验证
		Object obj=null;
		try {
			Map<String, Object> data = new HashMap<String, Object>();
			User user = getAppSessionUser(request);
			final UserModel model = new UserModel();
			model.setUuid(user.getUuid());
			model.setMobile(mobile);
			model.checkMobile();
			sendMessage(getSessionUser(), mobile,
					MessageConstant.MESSAGE_BIND_PHONE);
			
			data.put("mobileBindToken",TokenUtils.setToken(request.getSession(), TokenConstants.TOKEN_MOBILE_BIND));
			obj=createPhoneEmailSuccessAppResponse(data);
		} catch (Exception e) {
			obj=dealException(e);
		}
		return obj;
	}

	/**移动端
	 * 绑定手机
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/app/member/security/doBindPhone")
	@ResponseBody
	@TokenValid(value=TokenConstants.TOKEN_MOBILE_BIND,dispatch=true)
	public Object doBindPhone(final UserModel model,HttpServletRequest request) {
		Object obj=null;
		try {
			@SuppressWarnings("unused")
			User user = getAppSessionUser(request);
			model.setAgree(0);// 同意协议
			model.setIsRsa(0);// 是否加密
			model.setCheckType("01");
			userService.modifyMobile(model);
			obj=createSuccessAppResponse("手机绑定成功！");
		} catch (Exception e) {
			obj=dealException(e);
		}
	return obj;
	}

	/**
	 * 移动端 修改绑定手机-获取验证码
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/app/member/security/modifyPhoneCode")
	@ResponseBody
	public Object modifyPhoneCode(HttpServletRequest request) {
		Object obj=null;
		try {
			String checkType = "01";// 默认值01
			User user = getAppSessionUser(request);
			String messageType = MessageConstant.MESSAGE_MODIFY_PHONE_EMAILCODE;
			String addr = user.getEmail();
			if (Constant.MOBILE_VALID_MOBILE.equals(checkType)) {
				messageType = MessageConstant.MESSAGE_MODIFY_PHONE_PHONECODE;
				addr = user.getMobile();
			}
			sendMessage(user, addr, messageType);
			obj=createSuccessAppResponse("验证码已发送");
		} catch (Exception e) {
			obj=dealException(e);
		}
		return obj;
	}

	/**移动端
	 * 修改绑定手机 校验密码和密保
	 * 
	 * @return
	 */
	@RequestMapping(value = "/app/member/security/doModifyPhone")
	@ResponseBody
	public Object doModifyPhone(final UserModel model,HttpServletRequest request) {
		Map<String, Object> data = new HashMap<String, Object>();
		Object obj=null;
		try {
			User user = getAppSessionUser(request);
			String checkType = "01";// 01 手机验证 默认
			model.setCheckType(checkType);
			model.setUuid(user.getUuid());
			model.setUserName(user.getUserName());
			model.setMobile(user.getMobile());
			model.setEmail(user.getEmail());
			model.validModifyMobile();
			
			data.put("modifyPhoneSign", model.getModifyPhoneSign());
			obj=createSuccessAppResponse(data);
		} catch (Exception e) {
			obj=dealException(e);
		}
		return obj;
	}

	// **********************************************手机
	// end********************************************************

	// **********************************************密保问题
	// start**************************************************
	/**
	 * 密保问题展示页面
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/member/security/pwdQuestion")
	public String pwdQuestion(final String answerPwdQuestSign,final Model model){
		final User user = SessionUtils.getSessionUser(BussinessException.TYPE_CLOSE);
		final List<UserSecurityAnswer> list = secAnswerService.findByUserId(user.getUuid());
		initUserNature(null,model);
		String goPage="/member/security/setPwdQuestion";
		if(!CollectionUtils.isEmpty(list)){
			goPage="/member/security/answerPwdQuestion";
			if(StringUtils.isNotBlank(answerPwdQuestSign)
					&& answerPwdQuestSign.equals(MD5Utils.encode(user.getMobile() + Constant.RESET_PASS_QUEST))){
				goPage="/member/security/setPwdQuestion";
			}
		}
		return goPage;
	}

	/**
	 * 获得密保问题--移动端
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/app/open/security/getPwdQuestion")
	@ResponseBody
	public Object getPwdQuestion(final String answerFlag,HttpServletRequest request) {
		Object obj=null;
		try{
			User user = getAppSessionUser(request);
		final Map<String, Object> data = new HashMap<String, Object>();
		// 如果answerFlag=1，则表示获取的是回答过的问题
		if (Constant.FLAG_YES.equals(answerFlag)) {
			data.put("questionList",
					secAnswerService.findUserPwdQuestion(user.getUuid()));
		} else {
			data.put("questionList",
					DictUtils.list(DictTypeEnum.SECURITY_QUESTION.getValue()));
		}
		obj=createSuccessAppResponse(data);
	} catch (Exception e) {
		obj=dealException(e);
	}
	return obj;
	}

	/**
	 * 设置密保问题--移动端
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/app/member/security/doSetPwdQuestion")
	@ResponseBody
	public Object doSetPwdQuestion(final UserSecurityAnswerModel model,HttpServletRequest request) {
		Object obj=null;
		try{
		model.setAddIp(IPUtil.getRemortIP(request));
		secAnswerService.doSetPwdQuestion(model);
		obj=createSuccessAppResponse("设置成功");
	} catch (Exception e) {
		obj=dealException(e);
	}
	return obj;
	}

	/**
	 * 回答密保问题
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/member/security/doAnswerPwdQuestion")
	@ResponseBody
	public Object doAnswerPwdQuestion(UserSecurityAnswerModel model) {
		final User user = SessionUtils.getSessionUser();
		final Map<String, Object> data = Maps.newHashMap();

		secAnswerService.doAnswerPwdQuestion(model);
		data.put(RESULT_NAME, true);
		data.put(
				"answerPwdQuestSign",
				MD5Utils.encode(user.getMobile()
						+ Constant.RESET_PASS_QUEST));
		return renderResult(true, "提交成功");
	}

	/**
	 * 重置密保问题页面
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/member/security/resetPwdQuestion")
	public String resetPwdQuestion() {
		return "/member/security/resetPwdQuestion";
	}

	/**
	 * 获得重置密保问题验证码
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/member/security/resetPwdQuestionCode")
	@ResponseBody
	public Object resetPwdQuestionCode(final String checkType) {
		final User user = SessionUtils.getSessionUser();
		String messageType = MessageConstant.MESSAGE_RESET_QUESTION_PHONECODE;
		String addr = user.getMobile();
		if (Constant.PASS_VALID_EMAIL.equals(checkType)) {
			messageType = MessageConstant.MESSAGE_RESET_QUESTION_EMAILCODE;
			addr = user.getEmail();
		}
		sendMessage(user, addr, messageType);
		return renderResult(true, "获取成功");
	}

	/**
	 * 重置密保问题
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/member/security/doResetPwdQuestion")
	@ResponseBody
	public Object doResetPwdQuestion(UserSecurityAnswerModel model) {
		secAnswerService.doResetPwdQuestion(model);
		return renderResult(true, "设置成功");
	}

	// **********************************************密保问题
	// end**************************************************

//	/**移动端
//	 * 授权
//	 */
//	@RequestMapping(value = "/app/member/security/authSign",method=RequestMethod.POST)
//	public String authSign(final HttpServletRequest request ,final Model model) {
//		try {
//			User user = getAppSessionUser(request);
//		// 封装授权参数及记录
//		final String addIp = StringUtils.getRemoteAddr(request);
//		final UfxAuthSignModel authSign = authSignService.auth(user,addIp,UfxAuthSignModel.AUTH_OPTION_OPEN);
//		 model.addAttribute("authSign", authSign);
//		} catch (Exception e) {
//			model.addAttribute("r_msg", e.getMessage());
//			return ConfigUtils.getTppName()+"/retresult";
//		}
//		return ConfigUtils.getTppName() +"/authSign";
//	}

//	/**移动端
//	 * 解除授权
//	 */
//	@RequestMapping(value = "/app/member/security/authSignCancle")
//	public String authSignCancle(final HttpServletRequest request,final Model model) {
//		try {
//			User user = getAppSessionUser(request);
//		// 封装授权参数及记录
//		final String addIp = StringUtils.getRemoteAddr(request);
//		final UfxAuthSignModel authSign = authSignService.auth(user,addIp,UfxAuthSignModel.AUTH_OPTION_CLOSE);
//		 model.addAttribute("authSign", authSign);
//		} catch (Exception e) {
//			model.addAttribute("r_msg", e.getMessage());
//			return ConfigUtils.getTppName()+"/retresult";
//		}
//		return ConfigUtils.getTppName() +"/authSign";
//	}

	private void initUserNature(final UserCache userInfo,final Model model){
		if(userInfo==null){
			 model.addAttribute(IS_VOUCH, ((UserCache) SessionUtils.getSessionAttr(Constant.SESSION_USER_CACHE)).getUserNature());	
		}else{
			 model.addAttribute(IS_VOUCH, userInfo.getUserNature());		
		}	
	}
	
	/**
	 * 资金托管账户登录
	 * @return
	 */
	@RequestMapping(value = "/app/member/security/apiLogin")
	public String tppLogin(final Model model,final HttpServletRequest request) {
		try {
			User user = getAppSessionUser(request);
		final UserIdentify userIdentify = identifyService.findByUserId(user.getUuid());
		if (!Constant.FLAG_YES.equals(userIdentify.getRealNameStatus())) {
			throw new BussinessException(ResourceUtils.get(ResourceConstant.USER_TPP_STATUS_NOT_OPEN),"/member/security/realNameIdentify.html", "马上开通");
		}
		//参数封装
		final Map<String,Object> map = Maps.newHashMap();
		map.put("userCustId", user.getTppUserCustId());
		final UfxLoginModel loginModel = UfxHelper.login(map);
		 model.addAttribute("login", loginModel);
		} catch (Exception e) {
			model.addAttribute("r_msg", e.getMessage());
			return ConfigUtils.getTppName()+"/retresult";
		}
		return ConfigUtils.getTppName() + "/login";
	}
	
	
	
	@RequestMapping(value = "/app/member/security/smsCodeApply")
	@ResponseBody
	public Object smsCodeApply(String mobile, String reqType, String srvTxCode, String cardId){
		Object obj = null;
		try {
			userService.smsCodeApply(mobile, reqType, srvTxCode, cardId);
			obj = createSuccessAppResponse("发送成功");
		} catch (Exception e) {
			obj = dealException(e);
		}
		return obj;
	}
	
	/**
	 * 设置密码
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/app/member/security/setPwd")
	public String setPwd(final HttpServletRequest request, final Model model){
		try {
			final User user = getAppSessionUser(request);
			JxPasswordSetModel jxPasswordSetModel = (JxPasswordSetModel) userService.modifyPayPass(user);
			model.addAttribute("obj", jxPasswordSetModel);
			model.addAttribute("sign", jxPasswordSetModel.getSign());
		} catch (Exception e) {
			model.addAttribute("r_msg", e.getMessage());
			return ConfigUtils.getTppName() + "/retresult";
		}
		return ConfigUtils.getTppName() + "/setPwd";
	}
	
	/**
	 * 重置密码
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/app/member/security/resetPwd")
	public String resetPwd(final HttpServletRequest request, final Model model, final String smsCode) {
		try {
			final User user = getAppSessionUser(request);
			JxPasswordResetPlusModel jxPasswordResetPlusModel = (JxPasswordResetPlusModel) userService.resetPwd(user, smsCode);
			model.addAttribute("obj", jxPasswordResetPlusModel);
			model.addAttribute("sign", jxPasswordResetPlusModel.getSign());
		} catch (Exception e) {
			model.addAttribute("r_msg", e.getMessage());
			return ConfigUtils.getTppName() + "/retresult";
		}
		return ConfigUtils.getTppName() + "/resetPwd";
	}
	
	/**
	 * 授权
	 */
	@RequestMapping(value = "/app/member/security/authSign")
	public String authSign(final HttpServletRequest request, final Model model, final String smsCode) {
		final User user = getAppSessionUser(request);
		// 封装授权参数及记录
		final String addIp = StringUtils.getRemoteAddr(request);
		try {
			// 封装授权参数
			final Map<String, Object> map = new HashMap<String, Object>();
			map.put("smsCode", smsCode);
			map.put("addIp", addIp);
			map.put("authOption", JxConfig.JXBANK_AUTH_OPEN);
			map.put("userId", user.getUuid());
			final JxAutoBidAuthPlusModel authSign = (JxAutoBidAuthPlusModel) authSignService.auth(map);
			model.addAttribute("authSign", authSign);
			model.addAttribute("sign", authSign.getSign());
		} catch (Exception e) {
			model.addAttribute("r_msg", e.getMessage());
			return ConfigUtils.getTppName() + "/retresult";
		}
		return ConfigUtils.getTppName() + "/authSign";
	}
	
	/**
	 * 解除授权
	 */
	@RequestMapping(value = "/app/member/security/authSignCancel")
	@ResponseBody
	public Object authSignCancel(final HttpServletRequest request, final Model model) {
		Object obj = null;
		try {
			final User sessionUser = SessionUtils.getSessionUser(BussinessException.TYPE_CLOSE);
			// 封装授权参数及记录
			final String addIp = StringUtils.getRemoteAddr(request);
			final Map<String, Object> map = new HashMap<String, Object>();
			map.put("addIp", addIp);
			map.put("authOption", JxConfig.JXBANK_AUTH_CLOSE);
			map.put("userId", sessionUser.getUuid());
			authSignService.auth(map);
			obj = createSuccessAppResponse("解除授权成功");
		} catch (Exception e) {
			obj = dealException(e);
		}
		return obj;
	}
}
