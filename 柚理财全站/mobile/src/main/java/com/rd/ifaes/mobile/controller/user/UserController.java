package com.rd.ifaes.mobile.controller.user;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.rd.ifaes.common.annotation.TokenValid;
import com.rd.ifaes.common.constant.TokenConstants;
import com.rd.ifaes.common.dict.CommonEnum;
import com.rd.ifaes.common.exception.BussinessException;
import com.rd.ifaes.common.rabbit.RabbitProducer;
import com.rd.ifaes.common.util.CommonConstants;
import com.rd.ifaes.common.util.DateUtils;
import com.rd.ifaes.common.util.FreemarkerUtil;
import com.rd.ifaes.common.util.MD5Utils;
import com.rd.ifaes.common.util.SessionUtils;
import com.rd.ifaes.common.util.SpringContextHolder;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.common.util.ip.IPUtil;
import com.rd.ifaes.common.util.resource.ProtocolResource;
import com.rd.ifaes.common.util.resource.UserResource;
import com.rd.ifaes.common.validator.BeanValidators;
import com.rd.ifaes.core.core.constant.CacheConstant;
import com.rd.ifaes.core.core.constant.ConfigConstant;
import com.rd.ifaes.core.core.constant.Constant;
import com.rd.ifaes.core.core.constant.MessageConstant;
import com.rd.ifaes.core.core.constant.MqConstant;
import com.rd.ifaes.core.core.constant.ResourceConstant;
import com.rd.ifaes.core.core.sms.VerifCodeForSMSUtils;
import com.rd.ifaes.core.core.util.CacheUtils;
import com.rd.ifaes.core.core.util.ConfigUtils;
import com.rd.ifaes.core.core.util.DictUtils;
import com.rd.ifaes.core.core.util.ResourceUtils;
import com.rd.ifaes.core.protocol.domain.Protocol;
import com.rd.ifaes.core.protocol.service.ProtocolService;
import com.rd.ifaes.core.sys.domain.Config;
import com.rd.ifaes.core.sys.domain.Message;
import com.rd.ifaes.core.sys.domain.MessageType;
import com.rd.ifaes.core.sys.domain.Operator;
import com.rd.ifaes.core.sys.service.ConfigService;
import com.rd.ifaes.core.sys.service.MessageService;
import com.rd.ifaes.core.sys.service.MessageTypeService;
import com.rd.ifaes.core.sys.service.OperatorService;
import com.rd.ifaes.core.user.domain.User;
import com.rd.ifaes.core.user.domain.UserCache;
import com.rd.ifaes.core.user.domain.UserIdentify;
import com.rd.ifaes.core.user.model.RetrievePwdModel;
import com.rd.ifaes.core.user.model.UserModel;
import com.rd.ifaes.core.user.model.UserOpinionModel;
import com.rd.ifaes.core.user.service.UserCacheService;
import com.rd.ifaes.core.user.service.UserFreezeService;
import com.rd.ifaes.core.user.service.UserIdentifyService;
import com.rd.ifaes.core.user.service.UserOpinionService;
import com.rd.ifaes.core.user.service.UserService;
import com.rd.ifaes.mobile.common.AppException;
import com.rd.ifaes.mobile.common.AppRequestParam;
import com.rd.ifaes.mobile.common.AppResultCode;
import com.rd.ifaes.mobile.common.AppValidateUtil;
import com.rd.ifaes.mobile.controller.WebController;
import com.rd.ifaes.mobile.model.token.OauthAccessToken;
import com.rd.ifaes.mobile.model.token.OauthRefreshToken;
import com.rd.ifaes.mobile.model.token.OauthTokenManager;

/**
 * 用户账户
 * 
 * @author LGX
 * @version 3.0
 * @since 2016年10月10日
 */
@Controller
public class UserController extends WebController {
	@Resource
	private transient UserOpinionService userOpinionService;
	/**
	 * WEB_NAME
	 */
	private static final String WEB_NAME = "web_name";
	/**
	 * 用户基础类
	 */
	@Resource
	private transient UserService userService;
	/**
	 * 用户关联信息服务
	 */
	@Resource
	private transient UserCacheService userCacheService;
	/**
	 * 短信类型
	 */
	@Resource
	private transient MessageTypeService msgTypeService;
	/**
	 * 短信业务处理类
	 */
	@Resource
	private transient MessageService messageService;
	/**
	 * 配置信息处理类
	 */
	@Resource
	private transient ConfigService configService;
	/**
	 * 注册协议处理类
	 */
	@Resource
	private transient ProtocolService protocolService;
	/**
	 * Rabbit操作类
	 */
	@Autowired
	private transient RabbitProducer rabbitProducer;
	/**
	 * Rabbit操作类
	 */
	@Autowired
	private transient UserFreezeService userFreezeService;
	/**
	 * 用户认证信息处理类
	 */
	@Resource
	private transient UserIdentifyService identifyService;
	String apptoken;

	public String getApptoken() {
		return apptoken;
	}

	public void setApptoken(String apptoken) {
		this.apptoken = apptoken;
	}
	 /**
		 * 后台用户类
		 */    
	    @Resource
	    private transient OperatorService operatorService;
	/**
	 * 移动端判断手机号码是否已被使用
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/app/open/user/checkMobile")
	@ResponseBody
	public Object checkMobile(final String mobile) {
		Object obj=null;
		try {
			// String mobile = paramString("phone");
			if (StringUtils.isBlank(mobile)) {
				throw new AppException(AppResultCode.ERROR, "手机号不能为空");
			}
			checkPhone(mobile);
			obj=createSuccessAppResponse("可以注册");
		} catch (Exception e) {
			obj=dealException(e);
		}
		return obj;
	}

	/**
	 * 移动端判断用户名是否已被使用
	 */
	@RequestMapping(value = "/app/open/user/checkUserName")
	@ResponseBody
	public Object checkUsernameAvailable(final String username) {
		Object obj=null;
		try {
			checkUsername(username);
			obj=createSuccessAppResponse("可以注册");
		} catch (Exception e) {
			obj=dealException(e);
		}
		return obj;
	}

	/**
	 * 移动端注册-获取验证码 redis 发送短信时间redis缓存跟pc是同一个
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/app/open/user/registerPhoneCode")
	@ResponseBody
	public Object registerPhoneCode(final String mobile,
			final HttpServletRequest request) {
		Object obj=null;
		try {
			final UserModel model = new UserModel();
			model.setMobile(mobile);
			checkappMobile(mobile);//移动端校验手机号
			// 校验发送时间是否在间隔时间以内
			final String functionType = MessageConstant.MESSAGE_USER_REGISTER_PHONECODE;
			final String userIp= IPUtil.getRemortIP(request) ;
			checkSpaceTimeByRegister(mobile,userIp);
			// 获取验证码
			final String verifCode = VerifCodeForSMSUtils.buildVerifCode(
					functionType, mobile);

			final Map<String, Object> sendData = new HashMap<>();
			//sendData.put("webname", ConfigUtils.getValue(WEB_NAME));
			//sendData.put("phone", mobile);
			sendData.put("code", verifCode);

			// 获取 注册验证码 短信模版内容
			final MessageType msgType = msgTypeService.find(functionType,
					MessageConstant.MESSAGE_SMS);
			if (msgType != null && CommonEnum.YES.eq(msgType.getSend())) {

				final Message msg = new Message();
				msg.setMessageType(functionType);
				msg.setCreateTime(DateUtils.getNow());
				msg.setSendType(String.valueOf(MessageConstant.MESSAGE_SMS));
				msg.setSendUser(MessageConstant.USER_ID_ADMIN);
				msg.setTitle(msgType.getMessageTitle());
				msg.setContent(FreemarkerUtil.renderTemplate(
						msgType.getMessageContent(), sendData));
				msg.setTemplateCode(msgType.getTemplateCode());//短信平台需要的模板
				msg.setTemplateContent(JSON.toJSONString(sendData));//短信平台模板需要的json数据
				msg.setReceiveAddr(mobile);

				// 内容不为空
				if (StringUtils.isNotBlank(msg.getReceiveAddr())
						&& StringUtils.isNotBlank(msg.getContent())) {
					final Config config = configService
							.findByCode("open_rabbitmq");
					if (config != null
							&& CommonConstants.YES.equals(config
									.getConfigValue())) {
						// 加入队列
						rabbitProducer
								.send(MqConstant.ROUTING_KEY_MESSAGE, msg);
					} else {
						// 不加入队列
						messageService.sendMessage(msg);
					}
				}
			}
			//保存验证码发送时间
			saveSendTimeByRegister(mobile,userIp);
//          obj=createSuccessAppResponse("验证码已发送");
          Map<String, Object> data = new HashMap<String, Object>();
          data.put("__sid", SessionUtils.getSession().getId());
          obj = createPhoneEmailSuccessAppResponse(data);
		} catch (Exception e) {
			obj=dealException(e);
		}
		return obj;
	}
	public void checkappMobile(String mobile){
		if(StringUtils.isBlank(mobile)){
			throw new BussinessException(ResourceUtils.get(ResourceConstant.USER_MOBILE_NOTEMPTY), BussinessException.TYPE_JSON);
		}
		
		if (!StringUtils.isPhone(mobile)) {
			throw new BussinessException(ResourceUtils.get(ResourceConstant.MOBILE_STYLE_ERROR), BussinessException.TYPE_JSON);
		}
		
	/*	if(StringUtils.isNotBlank(getUuid())){
			User user = SessionUtils.getSessionUser();
			if(getMobile().equals(user.getMobile())){
				throw new BussinessException(ResourceUtils.get(ResourceConstant.USER_MOBILE_NEED_NEW), BussinessException.TYPE_JSON);
			}
		}*/
		
		UserService userService = (UserService)SpringContextHolder.getBean("userService");
		int count = userService.checkRepeat("", null, mobile, null);
		if (count > 0 ) {
			throw new AppException(AppResultCode.USER_REGISTER_ERROR, "该手机号码已注册，请直接登录");
		}
	}
	
	/**
	 *移动端--注册协议
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/app/open/user/registerProtocol")
	@ResponseBody
	public Object registerProtocol() {
		Object obj=null;
		Map<String, Object> data = new HashMap<String, Object>();
		try {
			List<Protocol> protocolList = protocolService.getProtocolListByType(Constant.PROTOCOL_TYPE_REGISTER);
			data.put("list", CollectionUtils.isEmpty(protocolList)? null:protocolList);
			obj=createSuccessAppResponse(data);
			} catch (Exception e) {
				obj=dealException(e);
			}
			return obj;
		}
	
	/**
	 * 注册协议
	 * @author xhf
	 * @date 2016年9月11日
	 * @param protocolId
	 * @return
	 */
	@RequestMapping(value = "/app/open/user/registerProtocolDetail")
	public Object registerProtocol(final String protocolId,final Model model) {
		try{
		final Map<String, Object> paramMap = Maps.newHashMap();
		paramMap.put(WEB_NAME, ConfigUtils.getValue(WEB_NAME));
		String content = protocolService.getProtocolContent(protocolId, paramMap);
		final Protocol protocol = protocolService.get(protocolId);
		if(protocol == null){
			throw new BussinessException(ResourceUtils.get(ProtocolResource.PROTOCL_NOT_EXISTS),BussinessException.TYPE_JSON);
		}
		model.addAttribute(RESULT_NAME, true);
		model.addAttribute("content", content);
		model.addAttribute("protocolType",DictUtils.getData("protocolType", Constant.PROTOCOL_TYPE_REGISTER).getItemName());
		model.addAttribute("protocolName", protocol.getName());
		model.addAttribute("titleName", protocol.getName());
		return "/app/protocol/registerProtocol";
	} catch (Exception e) {
		model.addAttribute("r_msg", e.getMessage());
		return ConfigUtils.getTppName()+"/retresult";
	}
	}
	
	/**
	 * 注册协议--微信
	 * @author xhf
	 * @date 2016年9月11日
	 * @param protocolId
	 * @return
	 */
	@RequestMapping(value = "/app/open/user/wxRegisterProtocolDetail")
	@ResponseBody
	public Object wxRegisterProtocolDetail(final String protocolId,final Model model) {
		Object obj=null;
		try{
			Map<String, Object> data = new HashMap<String, Object>();
		final Map<String, Object> paramMap = Maps.newHashMap();
		paramMap.put(WEB_NAME, ConfigUtils.getValue(WEB_NAME));
		String content = protocolService.getProtocolContent(protocolId, paramMap);
		final Protocol protocol = protocolService.get(protocolId);
		if(protocol == null){
			throw new BussinessException(ResourceUtils.get(ProtocolResource.PROTOCL_NOT_EXISTS),BussinessException.TYPE_JSON);
		}
		data.put(RESULT_NAME, true);
		data.put("content", content);
		data.put("protocolType",DictUtils.getData("protocolType", Constant.PROTOCOL_TYPE_REGISTER).getItemName());
		data.put("protocolName", protocol.getName());
		data.put("titleName", "注册协议");
		obj = createSuccessAppResponse(data);
		} catch (Exception e) {
			obj = dealException(e);
		}
		return obj;
	}
	
	/**
	 * 移动端 个人注册第一步：提交基本信息 手机号、密码、确认密码、图片验证码
	 * 
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/app/open/user/registerFirst")
	@ResponseBody
	public Object registerFirst(final String mobile,
			String pwd, final String inviter,int agree,final HttpServletRequest request) {
		Object obj=null;
		Map<String, Object> data = new HashMap<String, Object>();
		try {
			pwd = decodeBase64param(URLDecoder.decode(pwd, "UTF-8"));
			if(!"".equals(inviter)&&inviter!=null){
				//校验手机号码长度和格式
				if (!StringUtils.isPhone(inviter))
					throw new AppException(AppResultCode.ERROR, "您输入的邀请人手机号码格式不正确");
			}
			User userInviter = getInviter(inviter);
			UserModel model = new UserModel();
			model.setUserNature("1");// APP注册默认个人用户
			model.setMobile(mobile);
			model.setPwd(pwd);
			model.setAgree(agree);// 是否勾选协议
			model.setConfirmPwd(pwd);// app端默认把 密码和确认密码设为一致 因为直接用的 pc注册方法
			//model.validRegRule();//校验图形验证码  app不用 微信可能用到
			model.setInviteUser(userInviter);
			if (userInviter != null) {
				model.setUi(userInviter.getUuid());// 设置邀请人uuid
			}
			
			String userAgent = request.getHeader("User-Agent").toLowerCase();//判断访问来源 是否是微信浏览器
			int source=0;//来源  
			if("android".equals(userAgent)||"iphone".equals(userAgent)){//安卓
				model.setRegistChannel("2");//app
			}else if(userAgent.contains("android")||userAgent.contains("iphone")||userAgent.contains("micromessenger")){// 手机浏览器
				model.setRegistChannel("3");//微信
			}
			// 1、校验用户信息
			checkappUserRegister(model,userInviter);
			// 2、初步填写的user信息存入session中
			SessionUtils.setSessionAttr("userModel", model);
			data.put("__sid", SessionUtils.getSession().getId());
			obj=createSuccessAppResponse(data);// 基本信息提交成功 直接返回标识 sid
		} catch (Exception e) {
			obj=dealException(e);
		}
		return obj;
	}

	public void checkappUserRegister(final UserModel model,User inviteUser){
		// validate
		BeanValidators.validate(model);
		model.validAgree();
		model.validRegPwdModel();

		// check mobile
		final int hasMobilePhone = userService.checkRepeat(null, null, model.getMobile(), null);
		if (hasMobilePhone > 0) {
			throw new AppException(AppResultCode.USER_REGISTER_ERROR, "该手机号码已注册，请直接登录");
		}
		// inviteUser
		final String inviteUserParams = model.getUi();
			if(inviteUser != null){
				model.setInviteUser(inviteUser);
			}else{
				final Operator inviteOperator = operatorService.get(inviteUserParams);
				if(inviteOperator != null){
					model.setInviteOperator(inviteOperator);
				}
			}
			
	}
	/**
	 * 移动端 注册提交： 1、获取验证码(验证码忽略)， 2、用户注册成功 3、手机号码自动认证
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/app/open/user/doRegister")
	@ResponseBody
	public Object doRegister(final String code,final HttpServletRequest request) {
		Object obj=null;
		try {
			User u = null;
			// 注册用户信息
			u = userService.doRegister(code);
			setApptoken((String) SessionUtils.getSession().getId());
			String userAgent = request.getHeader("User-Agent").toLowerCase();//判断访问来源 是否是微信浏览器
			OauthAccessToken token=null;
			if("android".equals(userAgent)||"iphone".equals(userAgent)){//app端
				token = getToken(u);
			}else if(userAgent.contains("android")||userAgent.contains("iphone")||userAgent.contains("micromessenger")){//微信端
				token= wxgetToken(u);
			}else{
				throw new AppException(AppResultCode.ERROR, "请在手机端操作");
			}
			if(u!=null){ //登录成功，自动登录
				userService.loginSucess(u);
			}
			 obj=createSuccessAppResponse(token);
		} catch (Exception e) {
			obj=dealException(e);
		}
		return obj;
	}

	/**
	 * 用户登录
	 * 
	 * @param redirectURL
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/app/open/user/doLogin", method = RequestMethod.POST)
	@ResponseBody
	public Object doLogin(String loginName,String loginPwd,final HttpServletRequest request) throws Exception {
	 Object obj=null;
		try {
			UserModel user=new UserModel();
			user.setLoginPwd(decodeBase64param(URLDecoder.decode(loginPwd, "UTF-8")));
			user.setLoginName(loginName);
			// 校验
			user.validLoginModel();
			user.setIp(IPUtil.getRemortIP(request) );
			user.setSessionId(request.getSession().getId());
			/*start*/
			// 登录 ： 1、校验参数；2、用户是否存在；3、登录状态合法性校验
			final Map<String, Object> rtnMap = userService.doLogin(user);
			final String resultCode = (String)rtnMap.get("resultCode");
			if(UserService.LOGIN_SUCCESS.equals(resultCode)){
				User users = userService.getUserByLoginName(user.getLoginName());
				if(!"1".equals(users.getUserNature())){
					throw new AppException(AppResultCode.ERROR, "手机客户端暂不支持企业用户登录请至网页登录。");
				}
				Boolean pwdResetFlag=(Boolean) rtnMap.get("pwdResetFlag");
				setApptoken((String) SessionUtils.getSession().getId());
				String userAgent = request.getHeader("User-Agent").toLowerCase();//判断访问来源 是否是微信浏览器
				OauthAccessToken token=null;
				if("android".equals(userAgent)||"iphone".equals(userAgent)){//app端
					token = getToken(users);
				}else if(userAgent.contains("android")||userAgent.contains("iphone")||userAgent.contains("micromessenger")){//微信端
					token= wxgetToken(users);
				}else{
					throw new AppException(AppResultCode.ERROR, "请在手机端操作");
				}
				if(pwdResetFlag!=null){
					token.setPwdResetFlag("1");
				}else{
					token.setPwdResetFlag("0");
				}
				  obj=createSuccessAppResponse(token);
			}else{
				if(UserService.LOGIN_FAIL_LOCK.equals(resultCode)){
					throw new AppException(AppResultCode.USER_LOCK, "您的账户已被锁定,请联系客服");
				} else if(UserService.LOGIN_FAIL_PASS.equals(resultCode)||UserService.LOGIN_FAIL_NAME.equals(resultCode)){
					//显示错误提示语句
					String msg = ResourceConstant.LOGIN_NAME_OR_PASS_IS_ERROR;
					if(UserService.LOGIN_FAIL_PASS.equals(resultCode)){
						String userId = (String)rtnMap.get("userId");
						int errorCount = CacheUtils.getInt(CacheConstant.KEY_PREFIX_USER_LOGIN_FAIL_NUM + userId);
						int maxErrorCount = ConfigUtils.getInt(Constant.LOGIN_LOCK_NUMBER);
						
						if(maxErrorCount>2 && (maxErrorCount-2)==errorCount){
							msg = ResourceConstant.LOGIN_FAIL_WITH_TWO_CHANCE; 
						}
						if(maxErrorCount>1 && (maxErrorCount-1)==errorCount){
							msg = ResourceConstant.LOGIN_FAIL_WITH_LAST_CHANCE; 
						}
						if(maxErrorCount<=errorCount){
							msg = ResourceConstant.LOGIN_USER_IS_LOCK;
							throw new AppException(AppResultCode.USER_LOCK, "您的账户已被锁定,请联系客服");
						}
						if("登录失败，用户名或密码错误".equals(ResourceUtils.get(msg))){
							throw new AppException(AppResultCode.ERROR_SPECIAL, "手机号/登录密码有误");
						}else if("用户名或密码错误，您还可输入2次".equals(ResourceUtils.get(msg))){
							throw new AppException(AppResultCode.ERROR_SPECIAL, "手机号/登录密码有误，您还可以输入2次");
						}else if("还有最后一次重试机会".equals(ResourceUtils.get(msg))){
							throw new AppException(AppResultCode.ERROR_SPECIAL, "手机号/登录密码有误，您还可以输入1次");
						}else{
						throw new AppException(AppResultCode.ERROR_SPECIAL, ResourceUtils.get(msg));
						}
					}else{
						int countMobile = userService.checkRepeat(null, null, loginName, null);// 绑定该手机的用户
						if(countMobile ==0){
							throw new AppException(AppResultCode.USER_DOLOGIN_ERROR, "账户不存在，请重新输入或注册新账号");
						}
					throw new AppException(AppResultCode.ERROR_SPECIAL, "手机号/登录密码有误");
					}
				}
			}
			/*end*/
		} catch (Exception e) {
			obj=dealException(e);
		}
		return obj;
	}

	/**
	 * 移动端 忘记密码 发送验证码 该方法无需用户在线，但可通过控制图形验证码和手机号码或邮箱地址防止刷验证码
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/app/open/user/security/sendValidCode")
	@ResponseBody
	public Object sendValidCode(final RetrievePwdModel model) {
		Object obj=null;
		try {
			model.setType("1");
			model.checkBasicInfo();
			//业务校验：在点击发送短信时，再检查短信或邮箱是否已经注册
			checkPathWay(model);
			if (RetrievePwdModel.TYPE_PHONE.equals(model.getType())) {
				sendMessage(model.getPathWay(), MessageConstant.MESSAGE_RETRIEVE_PASS_PHONECODE, model.getType());
			}
			if (RetrievePwdModel.TYPE_EMAIL.equals(model.getType())) {
				sendMessage(model.getPathWay(), MessageConstant.MESSAGE_RETRIEVE_PASS_EMAILCODE, model.getType());
			}
			 obj=createSuccessAppResponse("验证码已发送");
		} catch (Exception e) {
			obj=dealException(e);
		}
		return obj;
	}
	/**
	 * 检查手机或邮箱是否存在(该方法需要访问service不宜放在model中)
	 * @return
	 * @throws Exception
	 */
	//@RequestMapping(value = "/user/security/checkPathWay")
	//@ResponseBody
	public void checkPathWay(final RetrievePwdModel model){
		if (!RetrievePwdModel.TYPE_PHONE.equals(model.getType())
				&& !RetrievePwdModel.TYPE_EMAIL.equals(model.getType())) {
			// 提示参数非法
			throw new BussinessException(ResourceUtils.get(ResourceConstant.PASS_RETRIEVE_TYPE_ERROR, 
								    BussinessException.TYPE_JSON));
		}
		if (RetrievePwdModel.TYPE_PHONE.equals(model.getType())) {
			int count = userService.checkRepeat(null, null, model.getPathWay(), null);
			if (count == Constant.INT_ZERO) {
				throw new BussinessException(ResourceUtils.get(UserResource.USER_MOBILE_NOT_EXIST),
						BussinessException.TYPE_JSON);
			}
		} else {
			int count = userService.checkRepeat(null, null, null, model.getPathWay());
			if (count == Constant.INT_ZERO) {
				throw new BussinessException(ResourceUtils.get(UserResource.USER_EMAIL_NOT_EXIST),
						BussinessException.TYPE_JSON);
			}
		}
		//return renderResult(true, StringUtils.EMPTY);
	}
	/**
	 * 
	 * 点击下一步（index页提交身份验证）
	 * @param  
	 * @return Object
	 * @author xxb
	 * @date 2016年10月17日
	 */
	@RequestMapping(value = "/app/open/user/retrievepwd/validation")
	@ResponseBody
	public Object validation(final RetrievePwdModel model){
		Map<String, Object> data = new HashMap<String, Object>();
		Object obj=null;
		try {
		model.setType("1");
		//验证
		model.checkBasicInfo();
		//model.checkImgValidCode();
		model.checkDynamicValidCode();
		//生成找回密码标识
		SessionUtils.setSessionAttr(Constant.RETRIEVEPASS_SIGN, MD5Utils.encode(model.getPathWay() + MessageConstant.MESSAGE_RETRIEVE_PASS_PHONECODE));
		data.put("pathWay", model.getPathWay());
		data.put("__sid", SessionUtils.getSession().getId());
		obj=createSuccessAppResponse(data);
		} catch (Exception e) {
			obj=dealException(e);
		}
	return obj;
	}
	/**
	 * 移动端 确认找回登录密码 确认提交
	 * 
	 * @param
	 * @return Object
	 * @author lgx
	 * @date 2016年10月17日
	 */
	@RequestMapping(value = "/app/open/user/retrievepwd/confirm")
	@ResponseBody
	public Object confirm(final RetrievePwdModel model) {
		Object obj=null;
		try {
			model.setPassword(decodeBase64param(URLDecoder.decode(model.getPassword(), "UTF-8")));
			model.setConfirmPassword(decodeBase64param(URLDecoder.decode(model.getConfirmPassword(), "UTF-8")));
			model.setType("1");
			//校验
			model.checkPassword();
			model.checkRetrievepwdSign();
			//业务
			userService.retrievePassword(model.getPathWay(), model.getPassword());
			//去除缓存中的标识
			SessionUtils.removeAttribute(Constant.RETRIEVEPASS_SIGN);
			obj=createSuccessAppResponse("登录密码重置成功！");
		} catch (Exception e) {
			obj=dealException(e);
		}
	return obj;
	}

	private User getInviter(String inviter) throws IOException {
		try {
			if ("".equals(inviter)||inviter==null) {
				return null;
			} else {
				User inviteUser;
				inviteUser = userService.getUserByLoginName(inviter);
				if (inviteUser == null) {
					throw new AppException(AppResultCode.ERROR, "您输入的邀请人手机号码不存在");
				}
				return inviteUser;
			}
		} catch (Exception e) {
			throw new AppException(AppResultCode.ERROR, "您输入的邀请人手机号码不存在");
		}
	}

	/*
	 * 根据用户获取token
	 * 
	 * @param user
	 * 
	 * @return
	 * 
	 * @throws Exception
	 */
	private OauthAccessToken getToken(User user) throws Exception {
		OauthTokenManager oauthTokenManager = new OauthTokenManager();
		oauthTokenManager.removeOldToken(user.getUuid());
		OauthAccessToken accessToken = oauthTokenManager.saveToken(null,
				getApptoken(), user);
		accessToken.setUsername(user.getUserName());
		final UserIdentify userIdentify = identifyService.findByUserId(user.getUuid());
		if(!"1".equals(userIdentify.getRealNameStatus())){
			/*accessToken.setHideUserName(user.getUserName().charAt(0) + "******"
					+ user.getUserName().charAt(user.getUserName().length() - 1));*/
			accessToken.setHideUserName(user.getHideMobile());
			}else{
				accessToken.setHideUserName(getHideRealName(user.getRealName()));
			}
		UserCache usercache=userCacheService.findByUserId(user.getUuid());
		if(usercache.getAvatarPhoto()==null||"".equals(usercache.getAvatarPhoto())){
			//accessToken.setAvatarPhoto(ConfigUtils.getValue(ConfigConstant.IMAGE_SERVER_URL)+"/data/img/avatar/default_portrait.jpg");
			accessToken.setAvatarPhoto("");
		}else{
			accessToken.setAvatarPhoto(ConfigUtils.getValue(ConfigConstant.IMAGE_SERVER_URL)+usercache.getAvatarPhoto());
		}
		return accessToken;
	}
	private OauthAccessToken wxgetToken(User user) throws Exception {
		OauthAccessToken accessToken = new OauthAccessToken();
		accessToken.setUsername(user.getUserName());
		/*String hideName = user.getUserName().charAt(0) + "******"
				+ user.getUserName().charAt(user.getUserName().length() - 1);*/
		final UserIdentify userIdentify = identifyService.findByUserId(user.getUuid());
		if(!"1".equals(userIdentify.getRealNameStatus())){
			/*accessToken.setHideUserName(user.getUserName().charAt(0) + "******"
					+ user.getUserName().charAt(user.getUserName().length() - 1));*/
			accessToken.setHideUserName(user.getHideMobile());
			}else{
				accessToken.setHideUserName(getHideRealName(user.getRealName()));
			}
		accessToken.set__sid(getApptoken());
		accessToken.setUserId(String.valueOf(user.getUuid()));
		
		UserCache usercache=userCacheService.findByUserId(user.getUuid());
		if(usercache.getAvatarPhoto()==null||"".equals(usercache.getAvatarPhoto())){
			/*accessToken.setAvatarPhoto(ConfigUtils.getValue(ConfigConstant.IMAGE_SERVER_URL)+"/data/img/avatar/default_portrait.jpg");*/
			accessToken.setHideUserName(user.getHideMobile());
		}else{
			accessToken.setAvatarPhoto(ConfigUtils.getValue(ConfigConstant.IMAGE_SERVER_URL)+usercache.getAvatarPhoto());
		}
		return accessToken;
	}

	public String getHideRealName(String userName) {
		if(userName.length()<3){
			return "*"+userName.substring(userName.length()-1, userName.length());
		}else{
		return userName.substring(0, 1)+"*"+userName.substring(userName.length()-1, userName.length());
		}
}
	/*
	 * 验证手机格式及是否被占用
	 * 
	 * @param phone
	 */
	private void checkPhone(String mobile) {
		// 校验手机号码长度和格式
		if (!StringUtils.isPhone(mobile)) {
			throw new AppException(AppResultCode.ERROR, "手机号格式错误");
		} else {
			int countMobile = userService.checkRepeat(null, null, mobile, null);// 绑定该手机的用户
			if (countMobile > 0) {// 手机号重复
				throw new AppException(AppResultCode.ERROR, "手机号被占用");
			}
			int countMobiles = userService
					.checkRepeat(null, mobile, null, null);// 以手机用作为的用户名的账户
			if (countMobiles > 0) {// 手机号重复
				throw new AppException(AppResultCode.ERROR, "手机号被占用");
			}
		}
	}

	/*
	 * 校验用户名格式及是否被占用
	 * 
	 * @param username
	 */
	private void checkUsername(String username) {
		if ("".equals(username)) {
			return;
		}
		String usernameFormat = "^(?![0-9]+$)[0-9A-Za-z\u0391-\uFFE5]{4,16}$";
		Pattern p = Pattern.compile(usernameFormat);
		Matcher m = p.matcher(username);
		if (m.matches()) {
			int countMobile = userService.checkRepeat(null, username, null,
					null);
			if (countMobile > 0) {// 手机号重复
				throw new AppException(AppResultCode.ERROR, "用户名已存在");
			}
		} else {
			throw new AppException(AppResultCode.ERROR, "用户名格式不正确");
		}
	}

	/**
	 * (前台)校验身份证是否已使用
	 * 
	 * @author fxl
	 * @date 2016年9月26日
	 * @param cardId
	 * @return
	 */
	@RequestMapping(value = "/app/open/user/checkCardId")
	@ResponseBody
	public Object checkCardId(String cardId) {
		boolean flag = false;
		if (!StringUtils.isBlank(cardId)) {
			flag = userCacheService.checkCardId(cardId);
		}
		return renderResult(
				flag,
				flag ? MSG_SUCCESS : ResourceUtils
						.get(ResourceConstant.IDCARD_IS_EXIST));
	}

	/**
	 * 刷新token
	 */
	@RequestMapping("/app/open/user/refreshToken")
	@ResponseBody
	public Object refreshToken(String refresh_token,String appkey,HttpServletRequest request) {
		Object obj=null;
		try {
			OauthAccessToken token = refreshTokens(refresh_token,appkey,request);
			obj=createSuccessAppResponse(token);
		} catch (Exception e) {
			dealException(e);
		}
		return obj;
	}

	/*
	 * 根据refresh_token获取新的token
	 * 
	 * @param refresh_token
	 * 
	 * @return
	 */
	private OauthAccessToken refreshTokens(String refresh_token,String appkey,HttpServletRequest request) {
		OauthTokenManager oauthTokenManager = new OauthTokenManager();
		String userId = request.getParameter(AppRequestParam.USER_ID);
		@SuppressWarnings("unused")
		OauthRefreshToken oldToken = oauthTokenManager
				.getRefreshTokenStore(refresh_token,userId);
		if (!AppValidateUtil.checkStrNull(userId)) {
			throw new AppException(AppResultCode.TOKEN_NOT_EXIT, "无效的用户id");
		}
		User user  = userService.get(userId);
		if (user == null) {
			throw new AppException(AppResultCode.TOKEN_NOT_EXIT, "用户不存在");
		}
		oauthTokenManager.removeOldToken(user.getUuid());
		OauthAccessToken accessToken = oauthTokenManager.saveToken(
				null, "refresh",user);
		if (user != null) {
			/*CacheUtils.setJson(user.getUserNo() + AppCommons.LOGIN_APP_USER,
					user, ExpireTime.match(1200));// 移动端登录 缓存 20分钟
*/		
			 SessionUtils.setSessionAttr(Constant.SESSION_USER, user);//重新插入redis缓存
			accessToken.setUsername(user.getUserName());
			final UserIdentify userIdentify = identifyService.findByUserId(user.getUuid());
			if(!"1".equals(userIdentify.getRealNameStatus())){
				/*accessToken.setHideUserName(user.getUserName().charAt(0) + "******"
						+ user.getUserName().charAt(user.getUserName().length() - 1));*/
				accessToken.setHideUserName(user.getHideMobile());
				}else{
					accessToken.setHideUserName(getHideRealName(user.getRealName()));
				}
		} else {
			accessToken.setUsername("");
			accessToken.setHideUserName("");
		}

		return accessToken;
	}
	
	/**
	 * 获取sessionId
	 * @author fxl
	 * @date 2017年6月28日
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/app/open/user/getSessionId")
	@ResponseBody
	public Object getSessionId(HttpServletRequest request){
		Map<String, Object> data = new HashMap<String, Object>();
		Object obj=null;
		try {
			User user = (User) SessionUtils.getSessionAttr(Constant.SESSION_USER);
			if(null==user){
				String userId = request.getParameter(AppRequestParam.USER_ID);
				user = userService.get(userId);
				user.setSessionId(request.getSession().getId());
				user.setIp(IPUtil.getRemortIP(request));
				UserCache userCache = userCacheService.findByUserId(user.getUuid());
				SessionUtils.setSessionAttr(Constant.SESSION_USER, user);
				SessionUtils.setSessionAttr(Constant.SESSION_USER_IDENTIFY, identifyService.findByUserId(user.getUuid()));
				SessionUtils.setSessionAttr(Constant.SESSION_USER_CACHE, userCache);
			}
			data.put("sessionId", user.getSessionId());
			obj=createSuccessAppResponse(data);
		} catch (Exception e) {
			obj=dealException(e);
		}
		return obj;
	}

	/**移动端
	 * @Description: 保存头像信息
	 * @author lgx
	 *  2016年11月15日 
	 */
	@RequestMapping("/app/member/uploadAvatar")
	@ResponseBody
	public Object face(String imgUrl,HttpServletRequest request) throws Exception {
		Object obj=null;
		try{
		User user=getAppSessionUser(request);
		imgUrl=URLDecoder.decode(imgUrl);
		if (!"".equals(imgUrl)||imgUrl!=null){
			userCacheService.updateAvaPic(user, imgUrl);
		}else{
			throw new AppException(AppResultCode.ERROR, "imgUrl参数不能为空！");
		}
		obj=createSuccessAppResponse("头像上传成功");
	 } catch (Exception e) {
		dealException(e);
	}
	return obj;
	}
	
	
	/**移动端
	 * @Description: 问题反馈
	 * @author lgx
	 *  2017年1月5日 
	 */
	@RequestMapping("/app/member/myAssistant/opinionAddSave")
	@ResponseBody
	@TokenValid(value=TokenConstants.TOKEN_ASSISTANT_COMMIT,dispatch=true)
	public Object opinionAddSave(final HttpServletRequest request,final UserOpinionModel userOpinion){
		Object obj=null;
		try{
		//检验验证码
		//userOpinion.checkImgValidCode();
			User user=getAppSessionUser(request);
		/*String[] attachmentPaths = request.getParameterValues("attachmentPaths[]");
		userOpinion.setAttachmentPath(StringUtils.contact(attachmentPaths));*/
		userOpinion.setTitle("无");
		userOpinion.setAddIp(IPUtil.getRemortIP(request) );
		userOpinion.setCreateTime(DateUtils.getNow());
		userOpinion.setUserId(user.getUuid());
		userOpinion.setRemark(URLDecoder.decode(userOpinion.getRemark()));
		userOpinion.setStatus("0");//未处理
		//TokenUtils.validShiroToken(TokenConstants.TOKEN_ASSISTANT_COMMIT);//录入信息前进行token的校验
		userOpinionService.insert(userOpinion);
		obj=createSuccessAppResponse("问题反馈成功");
		 } catch (Exception e) {
			dealException(e);
		}
		return obj;
		}
}
