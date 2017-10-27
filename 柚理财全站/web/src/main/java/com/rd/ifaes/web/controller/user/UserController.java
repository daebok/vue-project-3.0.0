package com.rd.ifaes.web.controller.user;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.rd.ifaes.common.dict.CommonEnum;
import com.rd.ifaes.common.exception.BussinessException;
import com.rd.ifaes.common.rabbit.RabbitProducer;
import com.rd.ifaes.common.util.BigDecimalUtils;
import com.rd.ifaes.common.util.DateUtils;
import com.rd.ifaes.common.util.FreemarkerUtil;
import com.rd.ifaes.common.util.JsonMapper;
import com.rd.ifaes.common.util.MD5Utils;
import com.rd.ifaes.common.util.SessionUtils;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.common.util.TppUtil;
import com.rd.ifaes.common.util.ip.IPUtil;
import com.rd.ifaes.common.util.resource.ProtocolResource;
import com.rd.ifaes.common.util.resource.UserResource;
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
import com.rd.ifaes.core.core.util.OrderNoUtils;
import com.rd.ifaes.core.core.util.ResourceUtils;
import com.rd.ifaes.core.core.util.ValidateUtils;
import com.rd.ifaes.core.protocol.domain.Protocol;
import com.rd.ifaes.core.protocol.service.ProtocolService;
import com.rd.ifaes.core.sys.domain.Message;
import com.rd.ifaes.core.sys.domain.MessageType;
import com.rd.ifaes.core.sys.service.ConfigService;
import com.rd.ifaes.core.sys.service.MessageService;
import com.rd.ifaes.core.sys.service.MessageTypeService;
import com.rd.ifaes.core.tpp.model.jx.JxVoucherPayModel;
import com.rd.ifaes.core.tpp.service.TppService;
import com.rd.ifaes.core.user.domain.User;
import com.rd.ifaes.core.user.domain.UserCache;
import com.rd.ifaes.core.user.model.RetrievePwdModel;
import com.rd.ifaes.core.user.model.UserModel;
import com.rd.ifaes.core.user.service.UserCacheService;
import com.rd.ifaes.core.user.service.UserService;
import com.rd.ifaes.web.controller.WebController;

/**
 * 用户账户
 * 
 * @author xhf
 * @version 3.0
 * @since 2016年6月7日
 */
@Controller
public class UserController extends WebController{
	/**
	 * WEB_NAME
	 */
	private static final String WEB_NAME  = "web_name";
	/**
	 * USER_NATURE
	 */
	private static final String USER_NATURE = "userNature";
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
	 * 首页注册
	 * 个人注册，担保机构不需要前台注册
	 * 默认跳转到个人注册页面
	 * @return
	 */
	@RequestMapping({"/user/register", "/user/userRegister"})
	public String register( final String ui ,final Model model) {
		LOGGER.info("进入注册页面....start");
		String  GO_PAGE = StringUtils.EMPTY;
		if (getSessionUser() != null) { // 在线用户点击用户注册，直接跳转到主页
			GO_PAGE = "redirect:/member/account/main.html";
		}else{	
			model.addAttribute(USER_NATURE, UserCache.USER_NATURE_PERSON);
			String inviteUserMobile = userService.getInviteUserMobile(ui);
			if(StringUtils.isNotBlank(inviteUserMobile)){
				model.addAttribute("inviteUserMobile", inviteUserMobile);
			}
			 model.addAttribute("ui", ui);
			List<Protocol> protocolList = protocolService.getProtocolListByType(Constant.PROTOCOL_TYPE_REGISTER);
			 model.addAttribute("protocolList", CollectionUtils.isEmpty(protocolList)? null:JsonMapper.toJsonString(protocolList));
			GO_PAGE = "/user/userRegister";
		}
		return GO_PAGE;
	}
	
	/**
	 * 
	 * 企业注册
	 * @param  
	 * @date 2016年8月8日
	 */
	@RequestMapping(value = "/user/companyRegister")
	public String companyRegister( final String ui ,final Model model) {
		String  GO_PAGE = StringUtils.EMPTY;
		if (getSessionUser() != null) { // 在线用户点击用户注册，直接跳转到主页
			GO_PAGE = "redirect:/member/account/main.html";
		}else{
			model.addAttribute(USER_NATURE, UserCache.USER_NATURE_COMPANY );
			String inviteUserMobile = userService.getInviteUserMobile(ui);
			if(StringUtils.isNotBlank(inviteUserMobile)){
				model.addAttribute("inviteUserMobile", inviteUserMobile);
			}
			model.addAttribute("ui", ui);
			List<Protocol> protocolList = protocolService.getProtocolListByType(Constant.PROTOCOL_TYPE_REGISTER);
			 model.addAttribute("protocolList", CollectionUtils.isEmpty(protocolList)? null:JsonMapper.toJsonString(protocolList));
			GO_PAGE = "/user/companyRegister";
		}
		return GO_PAGE;
	}
	
	/**
	 * 注册协议
	 * @author xhf
	 * @date 2016年9月11日
	 * @param protocolId
	 * @return
	 */
	@RequestMapping(value = "/user/registerProtocol")
	@ResponseBody
	public Object registerProtocol(final String protocolId) {
		final Map<String, Object> paramMap = Maps.newHashMap();
		paramMap.put(WEB_NAME, ConfigUtils.getValue(WEB_NAME));
		String content = protocolService.getProtocolContent(protocolId, paramMap);
		final Protocol protocol = protocolService.get(protocolId);
		if(protocol == null){
			throw new BussinessException(ResourceUtils.get(ProtocolResource.PROTOCL_NOT_EXISTS),BussinessException.TYPE_JSON);
		}
		final Map<String, Object> data = Maps.newHashMap();
		data.put(RESULT_NAME, true);
		data.put("content", content);
		data.put("protocolType",DictUtils.getData("protocolType", Constant.PROTOCOL_TYPE_REGISTER).getItemName());
		data.put("protocolName", protocol.getName());
		return data;
	}
	
	
	/**
	 * 个人注册第一步：提交基本信息
	 * 手机号、密码、确认密码、图片验证码
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/user/registerFirst")
	@ResponseBody
	public Object registerFirst(final UserModel model, final String inviteUserMobile) {
		// 1、校验用户信息
		model.validRegRule();//校验验证码
		userService.checkUserRegister(model);

		//邀请修改
        if (inviteUserMobile != null && !"".equals(inviteUserMobile)) {
            User userInviter = userService.getUserByLoginName(inviteUserMobile);
            if (userInviter == null) {
            	throw new BussinessException("邀请人手机号码有误，请仔细核对。", BussinessException.TYPE_JSON);
            }
            model.setUi(userInviter.getUuid());
            model.setInviteUser(userInviter);
        } else {
        	 model.setUi("");
             model.setInviteUser(null);
        }
		
		// 2、初步填写的user信息存入session中
		SessionUtils.setSessionAttr("userModel", model);

		// 3、返回注册手机号
		final Map<String, Object> data = Maps.newHashMap();
		data.put(RESULT_NAME, true);
		data.put("mobile", model.getMobile());
		return data;
	}
	
	/**
	 * 注册提交：
	 *  1、获取验证码(验证码忽略)，
	 *  2、用户注册成功
	 *  3、手机号码自动认证
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/user/doRegister")
	@ResponseBody
	public Object doRegister(final String code){
		LOGGER.info("开始正式注册....start,code:{}",code);
		// 注册用户信息
		User user = userService.doRegister(code);
		if(user!=null){ //登录成功，自动登录
			userService.loginSucess(user);
		}
		SessionUtils.removeAttribute("userModel");
		// 返回结果
		return renderResult(true, "操作成功");
	}
	
	/**
	 * 用户登录
	 * @param redirectURL
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/user/login")
	public String login(final String redirectURL ,final Model model) {
		// 在线用户点击登录，跳转到账户中心
		final User user = getSessionUser();
		String goPage = "/user/login";
		if (user != null) {
			goPage = "redirect:/member/account/main.html";
		}
		 model.addAttribute("redirectURL", redirectURL);
		initRSAME(model);
		return goPage;
	}
	
	protected void initRSAME(Model model) {
		int formEncryptEnable = ConfigUtils.getInt(ConfigConstant.USER_LOGIN_FORM_ENCRYPT_ENABLE);
		if (Constant.INT_ONE == formEncryptEnable) {
			model.addAttribute("pubKey", ConfigUtils.getValue(ConfigConstant.USER_LOGIN_FORM_ENCRYPT_PUBLIC));
			model.addAttribute("isRsa", formEncryptEnable);
		}
		model.addAttribute("encrypt", formEncryptEnable);
	}

	@RequestMapping(value = "/user/doLogin", method = RequestMethod.POST)
	@ResponseBody
	public Object doLogin(final UserModel user ,final HttpServletRequest request ,final Model model) {
		final Map<String, Object> data = Maps.newHashMap();
		// 校验
		user.validLoginModel();
		user.setIp(IPUtil.getRemortIP(request) );
		user.setSessionId(request.getSession().getId());
		//图形验证码校验
		Integer loginFailCount = CacheUtils.getInt(CacheConstant.KEY_PREFIX_USER_LOGIN_FAIL_NUM + user.getSessionId());
		if(loginFailCount != null && loginFailCount >= Constant.USER_LOGIN_SHOW_CAPTCHA_TIMES){
			user.validRegRule();
		}
		
		try {
			// 登录 ： 1、校验参数；2、用户是否存在；3、登录状态合法性校验
			final Map<String, Object> rtnMap = userService.doLogin(user);
			final String resultCode = (String)rtnMap.get("resultCode");
			if(UserService.LOGIN_SUCCESS.equals(resultCode)){
				data.put("pwdResetFlag", rtnMap.get("pwdResetFlag"));
				data.put(RESULT_NAME, true);
				data.put(MSG_NAME, ResourceUtils.get(ResourceConstant.LOGIN_SUCCESS));
			}else{
				data.put(RESULT_NAME, false);
				if(UserService.LOGIN_FAIL_LOCK.equals(resultCode)){
					data.put(MSG_NAME, ResourceUtils.get(ResourceConstant.LOGIN_USER_IS_LOCK));
					data.put("showCaptcha", true);
					
				} else if(UserService.LOGIN_FAIL_PASS.equals(resultCode)||
						UserService.LOGIN_FAIL_NAME.equals(resultCode)){	
					//图形验证码
					boolean showCaptcha = false;
					Integer sessionLoginFailCount = CacheUtils.getInt(CacheConstant.KEY_PREFIX_USER_LOGIN_FAIL_NUM + user.getSessionId());
					if(sessionLoginFailCount != null &&
							sessionLoginFailCount >= Constant.USER_LOGIN_SHOW_CAPTCHA_TIMES){
						showCaptcha = true;
					}
					data.put("showCaptcha", showCaptcha);
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
						}
					}
					data.put(MSG_NAME, ResourceUtils.get(msg));
				}
			}
		} catch (Exception e) {
			data.put(RESULT_NAME, false);
			data.put(MSG_NAME, e.getMessage());
			throw e ;
		}

		return data;
	}
	
	
	/**
	 * 
	 * 用户登出
	 * @author  FangJun
	 * @date 2016年7月26日
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/user/logout")
	public String logout(){
		//清空session
		SessionUtils.logout();
		return "redirect:/user/login.html";
	}
	
	
	/**
	 * 判断手机号码是否已被使用
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/user/checkMobile")
	@ResponseBody
	public Object checkMobile(String mobile ,final HttpServletRequest request ) {
		//请求是否是异步请求
		if(!ValidateUtils.isAjaxRequest(request)){
			throw new BussinessException(ResourceUtils.get(ResourceConstant.GLOBAL_MSG_ILLEGAL),BussinessException.TYPE_JSON);
		}
		//手机号是否为空
		if(StringUtils.isBlank(mobile)){
			throw new BussinessException(ResourceUtils.get("check.mobile.is.empty"), BussinessException.TYPE_JSON);
		}
		
		String userId = getSessionUser() == null ? null : getSessionUser().getUuid();
		int countMobile = userService.checkRepeat(userId, null, mobile, null);
		if(countMobile>0){//手机号重复
			throw new BussinessException(ResourceUtils.get("user.mobile.repeat"), BussinessException.TYPE_JSON);
		}
		return renderSuccessResult();
	}
	
	/**
	 * 判断用户名是否已被使用
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/user/checkUserName")
	@ResponseBody
	public Object checkUserName(String userName ,final HttpServletRequest request ) {
		//请求是否是异步请求
		if(!ValidateUtils.isAjaxRequest(request)){
			throw new BussinessException(ResourceUtils.get(ResourceConstant.GLOBAL_MSG_ILLEGAL),BussinessException.TYPE_JSON);
		}
		//用户名是否为空
		if(StringUtils.isBlank(userName)){
			throw new BussinessException(ResourceUtils.get("check.userName.is.empty"), BussinessException.TYPE_JSON);
		}
		//校验用户格式
		if (!UserModel.isUserName(userName)) {   
			throw new BussinessException(ResourceUtils.get(ResourceConstant.USER_NAME_ERROR),BussinessException.TYPE_JSON);
	    }
		//校验用户名是否重复
		final String userId = getSessionUser() == null ? null : getSessionUser().getUuid();
		final int count = userService.checkRepeat(userId, userName, null, null);
		final Map<String, Object> data = Maps.newHashMap();
		if(count>0){
			data.put(RESULT_NAME, false);
			data.put(MSG_NAME, ResourceUtils.get(ResourceConstant.USER_NAME_REPEAT));
		}else{
			data.put(RESULT_NAME, true);
		}
		return data;
	}
	
	/**
	 * 注册-获取验证码
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/user/registerPhoneCode")
	@ResponseBody
	public Object registerPhoneCode(final String mobilePhone,final HttpServletRequest request){
		final UserModel userModel = (UserModel) SessionUtils.getSessionAttr("userModel");
		if (userModel == null) {
			throw new BussinessException(ResourceUtils.get(UserResource.USER_INFO_IS_RIGHT), BussinessException.TYPE_JSON);
		}
		if(!userModel.getMobile().equals(mobilePhone)){
			throw new BussinessException(ResourceUtils.get(UserResource.MOBILE_IS_NOT_RIGHT), BussinessException.TYPE_JSON);
		}
		final UserModel model = new UserModel();
		model.setMobile(mobilePhone);
		model.checkMobile();
		//校验发送时间是否在间隔时间以内
		final String functionType = MessageConstant.MESSAGE_USER_REGISTER_PHONECODE;
		checkPhoneCodeSpaceTime(mobilePhone);
		//获取验证码
		final String verifCode = VerifCodeForSMSUtils.buildVerifCode(functionType, mobilePhone);
		
		final Map<String, Object> sendData = new HashMap<>();
		//sendData.put("webname", ConfigUtils.getValue(WEB_NAME));
		//sendData.put("phone", mobilePhone);
		sendData.put("code", verifCode);
		
		//获取 注册验证码 短信模版内容
		final MessageType msgType = msgTypeService.find(functionType, MessageConstant.MESSAGE_SMS);
		if (msgType != null && CommonEnum.YES.eq(msgType.getSend())) {
			
			final Message msg = new Message();
			msg.setMessageType(functionType);
			msg.setCreateTime(DateUtils.getNow());
			msg.setSendType(String.valueOf(MessageConstant.MESSAGE_SMS));
			msg.setSendUser(MessageConstant.USER_ID_ADMIN);
			msg.setTitle(FreemarkerUtil.renderTemplate(msgType.getMessageTitle(), sendData));
			msg.setContent(FreemarkerUtil.renderTemplate(
						msgType.getMessageContent(), sendData));//系统模板的内容
			msg.setTemplateCode(msgType.getTemplateCode());//短信平台需要的模板
			msg.setTemplateContent(JSON.toJSONString(sendData));//短信平台模板需要的json数据
			msg.setReceiveAddr(mobilePhone);
			
			//内容不为空
			if (StringUtils.isNotBlank(msg.getContent())) {
				if(ConfigUtils.isOpenMq()){
					//加入队列
					rabbitProducer.send(MqConstant.ROUTING_KEY_MESSAGE, msg);						
				}else{
					//不加入队列
					messageService.sendMessage(msg);
				}
			}
		}
		//保存验证码发送时间
		savePhoneCodeSendTime(mobilePhone);
		
		return renderResult(true, "获取成功");
	}
	
	/**
	 * 
	 * 是否需要显示图形验证码
	 * @author xhf
	 * @date 2016年8月9日
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/user/showCaptcha")
	@ResponseBody
	public Object showCaptcha(final HttpServletRequest request){
		boolean showCaptcha = false;
		Integer loginFailCount = CacheUtils.getInt(CacheConstant.KEY_PREFIX_USER_LOGIN_FAIL_NUM + request.getSession().getId());
		if(loginFailCount != null && loginFailCount >= Constant.USER_LOGIN_SHOW_CAPTCHA_TIMES){
			showCaptcha = true;
		}
		return  renderResult(showCaptcha, StringUtils.EMPTY);
	}
	 /**
	  * 当前用户信息
	  * isLogin	Integer	登录状态	N	1 已登录 0 未登录
	  *	userNature	Integer	当前用户类型	Y	1个人用户、2 企业用户、3 担保机构
	  * tppStatus  	Integer	三方开户状态	Y	1 已开户 0 未开户
	  *	novice	Integer	是否新手	Y	1 新手 0 非新手
	  *	riskLevel	Integer	当前用户风险等级	Y	风险等级数值：1,2,3…
	  * @author  FangJun
	  * @date 2016年8月26日
	  */
	@RequestMapping(value = "/user/info")
	@ResponseBody
	public Object userInfo(){
		final Map<String, Object> data = renderSuccessResult();
		User user = getSessionUser();
		if (user != null) {
			data.put("isLogin", CommonEnum.YES.getValue());
			user=userService.get(user.getUuid());//从缓存重查询，避免session中tppStatus无效
			data.put("userId",user.getUuid());
			data.put("tppStatus",StringUtils.toInteger(user.getTppStatus()));
			UserCache userCache = (UserCache)  userCacheService.findByUserId(user.getUuid());
			if(userCache!=null){
				data.put(USER_NATURE,userCache.getUserNature());
				data.put("novice",userCache.getInvestNum()==null ? Constant.INT_ONE : (userCache.getInvestNum()>Constant.INT_ZERO ?Constant.INT_ZERO:Constant.INT_ONE  ) );
				data.put("riskLevel", userCache.getRiskLevel());
			}
			data.put("payPwdStatus", "1".equals(user.getPayPwd()) ? "1" : "0");//1为已设置，0未设置
		}else{
			data.put("isLogin", CommonEnum.NO.getValue());
		}
		
		return data;
	}
	
	
	/**
	 * (前台)校验身份证是否已使用
	 * @author fxl
	 * @date 2016年9月26日
	 * @param cardId
	 * @return
	 */
	@RequestMapping(value = "/user/checkCardId")
	@ResponseBody
	public Object checkCardId(String cardId){
		boolean flag = false;
		if(!StringUtils.isBlank(cardId)){
			flag = userCacheService.checkCardId(cardId);
		}
		return  renderResult(flag, flag?MSG_SUCCESS:ResourceUtils.get(ResourceConstant.IDCARD_IS_EXIST));
	}
	
	/**
	 * 
	 * 进入找回密码index页
	 * @param  
	 * @return String
	 * @author xxb
	 * @date 2016年10月17日
	 */
	@RequestMapping(value = "/user/retrievepwd/index")
	public String passwordIndex(){
		return "/user/retrievepwd/index";
	}
	
	/**
	 * 后台导入用户首次登录修改密码页面
	 * @author ZhangBiao
	 * @date 2016年10月22日
	 * @return
	 */
	@RequestMapping(value = "/user/pwdReset")
	public String pwdReset(){
		return "/user/pwdReset";
	}
	
	/**
	 * 
	 * 点击下一步（index页提交身份验证）
	 * @param  
	 * @return Object
	 * @author xxb
	 * @date 2016年10月17日
	 */
	@RequestMapping(value = "/user/retrievepwd/validation")
	@ResponseBody
	public Object validation(final RetrievePwdModel model){
		//验证
		model.checkBasicInfo();
		model.checkImgValidCode();
		model.checkDynamicValidCode();
		
		//生成找回密码标识
		SessionUtils.setSessionAttr(Constant.RETRIEVEPASS_SIGN, MD5Utils.encode(model.getPathWay() + MessageConstant.MESSAGE_RETRIEVE_PASS_PHONECODE));
		
		//回传参数,在确认页面带回
		final Map<String, Object> data = new HashMap<>();
		data.put(RESULT_NAME, true);
		data.put("pathWay", model.getPathWay());
		return data;
	}
	
	/**
	 * 
	 * 最后一步确认提交
	 * @param  
	 * @return Object
	 * @author xxb
	 * @date 2016年10月17日
	 */
	@RequestMapping(value = "/user/retrievepwd/confirm")
	@ResponseBody
	public Object confirm(final RetrievePwdModel model){
		//校验
		model.checkPassword();
		model.checkRetrievepwdSign();
		//业务
		userService.retrievePassword(model.getPathWay(), model.getPassword());
		//去除缓存中的标识
		SessionUtils.removeAttribute(Constant.RETRIEVEPASS_SIGN);
		
		return renderResult(true, StringUtils.EMPTY); 
	}
	
	
	/**
	 * 点击获取短信验证码（发送验证码）
	 * 该方法无需用户在线，但可通过控制图形验证码和手机号码或邮箱地址防止刷验证码
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/user/security/sendValidCode")
	@ResponseBody
	public Object sendValidCode(final RetrievePwdModel model){
		//请求值校验
		model.checkBasicInfo();
		model.checkImgValidCode();
		//图形验证码验证成功后重新放到session中
		SessionUtils.setSessionAttr(Constant.VALID_CODE, model.getImgValidCode());
		//业务校验：在点击发送短信时，再检查短信或邮箱是否已经注册
		checkPathWay(model);
		if (RetrievePwdModel.TYPE_PHONE.equals(model.getType())) {
			sendMessage(model.getPathWay(), MessageConstant.MESSAGE_RETRIEVE_PASS_PHONECODE, model.getType());
		}
		if (RetrievePwdModel.TYPE_EMAIL.equals(model.getType())) {
			sendMessage(model.getPathWay(), MessageConstant.MESSAGE_RETRIEVE_PASS_EMAILCODE, model.getType());
		}
		return renderResult(true, StringUtils.EMPTY);
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
	 * 单笔资金类业务交易查询
	 * @author jxx
	 * @date 2017年9月5日
	 * @param orderId
	 * @return
	 */
	@RequestMapping(value = "/user/fundTransQuery")
	@ResponseBody
	public Object fundTransQuery(final String orderId, final String accountId){
		Map<String, Object> map = new HashMap<>();
		if(StringUtils.isBlank(orderId)) {
			renderResult(false, StringUtils.EMPTY); 
		}
//		map.put("accountId", user.getTppUserCustId());6212462380000800047
		map.put("accountId", accountId);
		map.put("orgTxDate", orderId.substring(0, 8));
		map.put("orgTxTime", orderId.substring(8, 14));
		map.put("orgSeqNo", orderId.substring(14, 20));
		
//		userService.fundTransQuery(map);
		return userService.fundTransQuery(map);
	}
	
	
	/**
	 * 
	 * 投资人投标申请查询
	 * @author jxx
	 * @date 2017年9月8日
	 * @param accountId
	 * @param orgOrderId
	 * @return
	 */
	@RequestMapping(value = "/user/bidApplyQuery")
	@ResponseBody
	public Object bidApplyQuery(final String accountId, final String orgOrderId){
		Map<String, Object> map = new HashMap<>();
		if(StringUtils.isBlank(orgOrderId)) {
			renderResult(false, StringUtils.EMPTY); 
		}
//		User user = SessionUtils.getSessionUser();
		map.put("accountId", accountId);//6212462380000800047
		map.put("orgOrderId", orgOrderId);
		
//		userService.fundTransQuery(map);
		return userService.bidApplyQuery(map);
	}
	
	/**
	 * 
	 * 投资人债权明细查询
	 * @author jxx
	 * @date 2017年9月8日
	 * @param accountId
	 * @param projectId
	 * @param state
	 * @param startDate
	 * @param endDate
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(value = "/user/creditDetailsQuery")
	@ResponseBody
	public Object creditDetailsQuery(final String accountId, final String projectId, final String state, final String startDate, 
			final String endDate, final String pageNum, final String pageSize){
		Map<String, Object> map = new HashMap<>();
//		User user = SessionUtils.getSessionUser();
		map.put("accountId", accountId);//6212462380000800047
		map.put("productId", projectId);//no
		map.put("state", state);
		map.put("startDate", startDate);
		map.put("endDate", endDate);
		map.put("pageNum", pageNum);
		map.put("pageSize", pageSize);
		
		TppService tppservice = (TppService)TppUtil.tppService();
		@SuppressWarnings("unchecked")
		Map<String,Object> resp = (Map<String, Object>) tppservice.creditDetailsQuery(map);
		return resp;
	}
	
	/**
	 * 
	 * 借款人标的信息查询
	 * @author jxx
	 * @date 2017年9月8日
	 * @param accountId
	 * @param projectId
	 * @param startDate
	 * @param endDate
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(value = "/user/debtDetailsQuery")
	@ResponseBody
	public Object debtDetailsQuery(final String accountId, final String projectId, final String startDate, 
			final String endDate, final String pageNum, final String pageSize){
		Map<String, Object> map = new HashMap<>();
//		User user = SessionUtils.getSessionUser();
		map.put("accountId", accountId);//6212462380000800047
		map.put("productId", projectId);//no
		map.put("startDate", startDate);//no
		map.put("endDate", endDate);//no
		map.put("pageNum", pageNum);
		map.put("pageSize", pageSize);
		
		TppService tppservice = (TppService)TppUtil.tppService();
		@SuppressWarnings("unchecked")
		Map<String,Object> resp = (Map<String, Object>) tppservice.debtDetailsQuery(map);
		return resp;
	}
	
	/**
	 * 
	 * 投资人购买债权查询
	 * @author jxx
	 * @date 2017年9月8日
	 * @param accountId
	 * @param orgOrderId
	 * @return
	 */
	@RequestMapping(value = "/user/creditInvestQuery")
	@ResponseBody
	public Object creditInvestQuery(final String accountId, final String orgOrderId){
		Map<String, Object> map = new HashMap<>();
//		User user = SessionUtils.getSessionUser();
		map.put("accountId", accountId);//6212462380000800047
		map.put("orgOrderId", orgOrderId);
		TppService tppservice = (TppService)TppUtil.tppService();
		@SuppressWarnings("unchecked")
		Map<String,Object> resp = (Map<String, Object>) tppservice.creditInvestQuery(map);
		return resp;
	}
	
	/**
	 * 
	 * 投资人签约状态查询
	 * @author jxx
	 * @date 2017年9月8日
	 * @param accountId
	 * @return
	 */
	@RequestMapping(value = "/user/creditAuthQuery")
	@ResponseBody
	public Object creditAuthQuery(final String accountId){
		Map<String, Object> map = new HashMap<>();
//		User user = SessionUtils.getSessionUser();
		map.put("accountId", accountId);//6212462380000800047
		TppService tppservice = (TppService)TppUtil.tppService();
		@SuppressWarnings("unchecked")
		Map<String,Object> resp = (Map<String, Object>) tppservice.creditAuthQuery(map);
		return resp;
	}
	
	/**
	 * 
	 * 账户资金冻结明细查询
	 * @author jxx
	 * @date 2017年9月8日
	 * @param accountId
	 * @param state
	 * @param startDate
	 * @param endDate
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(value = "/user/freezeDetailsQuery")
	@ResponseBody
	public Object freezeDetailsQuery(final String accountId, final String state, final String startDate, 
			final String endDate, final String pageNum, final String pageSize){
		Map<String, Object> map = new HashMap<>();
//		User user = SessionUtils.getSessionUser();
		map.put("accountId", accountId);//6212462380000800047
		map.put("state", state);//no
		map.put("startDate", startDate);//no
		map.put("endDate", endDate);//no
		map.put("pageNum", pageNum);
		map.put("pageSize", pageSize);
		
		TppService tppservice = (TppService)TppUtil.tppService();
		@SuppressWarnings("unchecked")
		Map<String,Object> resp = (Map<String, Object>) tppservice.freezeDetailsQuery(map);
		return resp;
	}
	
	/**
	 * 
	 * 电子账户密码是否设置查询
	 * @author jxx
	 * @date 2017年9月8日
	 * @param accountId
	 * @return
	 */
	@RequestMapping(value = "/user/passwordSetQuery")
	@ResponseBody
	public Object passwordSetQuery(final String accountId){
		Map<String, Object> map = new HashMap<>();
//		User user = SessionUtils.getSessionUser();
		map.put("accountId", accountId);//6212462380000800047
		TppService tppservice = (TppService)TppUtil.tppService();
		@SuppressWarnings("unchecked")
		Map<String,Object> resp = (Map<String, Object>) tppservice.passwordSetQuery(map);
		return resp;
	}
	
	/**
	 * 
	 * 发红包
	 * @author jxx
	 * @date 2017年9月11日
	 * @param accountId
	 * @param amount
	 * @return
	 */
	@RequestMapping(value = "/user/experBonus")
	@ResponseBody
	public Object experBonus(final String accountId, final String forAccountId, final BigDecimal amount){
		JxVoucherPayModel jxVoucherPayModel = null;
		final Map<String, Object> map = new HashMap<String, Object>();
        map.put("accountId", accountId);
        map.put("txAmount", BigDecimalUtils.round(amount, 2));
        map.put("forAccountId", forAccountId);
        map.put("desLineFlag", "1");
        map.put("desLine", "红包使用");
        TppService tppService = (TppService) TppUtil.tppService();
        jxVoucherPayModel = (JxVoucherPayModel) tppService.experBonus(map);
		return jxVoucherPayModel;
	}
	
	//http://localhost:9080/user/voucherPayCancel.html?accountId=6212462380000800047&amount=20&test=test&orderNo=20170911162052121435
	/**
	 * 
	 * 解冻红包
	 * @author jxx
	 * @date 2017年9月11日
	 * @param accountId
	 * @param orderNo
	 * @param amount
	 * @param test
	 * @return
	 */
	@RequestMapping(value = "/user/voucherPayCancel")
	@ResponseBody
	public Object voucherPayCancel(final String accountId, final String forAccountId, final String orderNo, final BigDecimal amount, final String test){
		final Map<String, Object> map = new HashMap<String, Object>();
		map.put("accountId", accountId);
		map.put("txAmount", amount);
		map.put("forAccountId", forAccountId);
		map.put("orgTxDate", orderNo.substring(0, 8));
		map.put("orgTxTime", orderNo.substring(8, 14));
		map.put("orgSeqNo", orderNo.substring(14, 20));
		if ("test".equals(test)) {
			map.put("delay", "0");
		} else {
			map.put("delay", "1");
		}
		
		TppService tppService = (TppService) TppUtil.tppService();
		return tppService.voucherPayCancel(map);
	}
	
	/**
	 * 
	 * 结束债权
	 * @author jxx
	 * @date 2017年9月13日
	 * @param accountId
	 * @param forAccountId
	 * @param productId
	 * @param authCode
	 * @return
	 */
	@RequestMapping(value = "/user/batchCreditEnd")
	@ResponseBody
	public Object batchCreditEnd(final String accountId, final String forAccountId, final String productId, final String authCode){
		final Map<String, Object> map = new HashMap<String, Object>();
        List<Map> _subPacks = new ArrayList<>();//加息请求数组
		 Map<String, Object> subPack = new HashMap<>();

         subPack.put("accountId", accountId);
         subPack.put("orderId", OrderNoUtils.getSerialNumber());
         subPack.put("forAccountId", forAccountId);
         subPack.put("productId", productId);
         subPack.put("authCode", authCode);
         _subPacks.add(subPack);
         
//         subPack = new HashMap<>();
//         subPack.put("accountId", "6212462380000750051");
//         subPack.put("orderId", OrderNoUtils.getSerialNumber());
//         subPack.put("forAccountId", "6212462380000800047");
//         subPack.put("productId", "ef0f09a46d0c46c4a49f4159a66c6274");
//         subPack.put("authCode", "20161019160359665171");
//         _subPacks.add(subPack);
//         
//         subPack = new HashMap<>();
//         subPack.put("accountId", "6212462380000750051");
//         subPack.put("orderId", OrderNoUtils.getSerialNumber());
//         subPack.put("forAccountId", "6212462380000800047");
//         subPack.put("productId", "54909857ffdf400d9d51be3bae91c3ee");
//         subPack.put("authCode", "20161019170036665183");
//         _subPacks.add(subPack);
        
        String batchNo = OrderNoUtils.getRandomStr(6);
		map.put("batchNo", batchNo);
		map.put("txCounts", _subPacks.size());
        map.put("subPacks", _subPacks);
		
		TppService tppService = (TppService) TppUtil.tppService();
		return tppService.batchCreditEnd(map);
	}
	
	/**
	 * 
	 * 批量红包
	 * @author jxx
	 * @date 2017年9月13日
	 * @param forAccountId
	 * @param name
	 * @return
	 */
	@RequestMapping(value = "/user/batchVoucherPay")
	@ResponseBody
	public Object batchVoucherPay(final String forAccountId, final String name){
		final Map<String, Object> map = new HashMap<String, Object>();

		String[] forAccountIds = forAccountId.split(",");
		String[] names = name.split(",");
        List<Map> _subPacks = new ArrayList<>();//加息请求数组
		 Map<String, Object> subPack = new HashMap<>();
         subPack.put("orderId", OrderNoUtils.getSerialNumber());
         subPack.put("txAmount", BigDecimal.TEN);
         subPack.put("forAccountId", forAccountIds[0]);
         subPack.put("voucherType", "002");
         subPack.put("name", names[0]);
         _subPacks.add(subPack);
         
         subPack = new HashMap<>();
         subPack.put("orderId", OrderNoUtils.getSerialNumber());
         subPack.put("txAmount", BigDecimal.TEN);
         subPack.put("forAccountId", forAccountIds[1]);
         subPack.put("voucherType", "002");
         subPack.put("name", names[1]);
         _subPacks.add(subPack);
         
         subPack = new HashMap<>();
         subPack.put("orderId", OrderNoUtils.getSerialNumber());
         subPack.put("txAmount", BigDecimal.TEN);
         subPack.put("forAccountId", forAccountIds[2]);
         subPack.put("voucherType", "002");
         subPack.put("name", names[2]);
         _subPacks.add(subPack);
		

         String batchNo = OrderNoUtils.getRandomStr(6);
         final Date now = DateUtils.getNow();
         map.put("txAmount", BigDecimalUtils.add(BigDecimal.TEN, BigDecimal.TEN, BigDecimal.TEN));
         map.put("txCounts", "3");
         map.put("subPacks", _subPacks);
         map.put("batchNo", batchNo);
         map.put("tradeNo", DateUtils.dateStr7(now).concat(batchNo));
		
		TppService tppService = (TppService) TppUtil.tppService();
		return tppService.batchVoucherPay(map);
	}
	
	/**
	 * http://localhost:9080/user/batchDetailsQuery.html?batchNo=131607&batchTxDate=20171010&type=9
	 * 
	 * 交易状态查询
	 * @author jxx
	 * @date 2017年9月13日
	 * @param batchNo
	 * @return
	 */
	@RequestMapping(value = "/user/batchDetailsQuery")
	@ResponseBody
	public Object batchDetailsQuery(final String batchTxDate, final String batchNo, final String type){
		final Map<String, Object> map = new HashMap<String, Object>();
		map.put("batchTxDate", batchTxDate);
		map.put("batchNo", batchNo);//143472  131607
		map.put("type", type);
		map.put("pageNum", 1);
	    map.put("pageSize", 10);
		
		TppService tppService = (TppService) TppUtil.tppService();
		return tppService.batchDetailsQuery(map);
	}
	
	/**
	 * 
	 * http://localhost:9080/user/tppInvestFail.html?accountId=6212462380000800047&txAmount=1000&
	 * productId=810ff162e2a14a39813f2d5def0cecc2&orgOrderId=1709070985967339
	 * 手动撤标
	 * @author jxx
	 * @date 2017年9月13日
	 * @param accountId
	 * @param txAmount
	 * @param productId
	 * @param orgOrderId
	 * @return
	 */
	@RequestMapping(value = "/user/tppInvestFail")
	@ResponseBody
	public Object tppInvestFail(final String accountId, final String txAmount, final String productId, final String orgOrderId){
		final Map<String, Object> map = new HashMap<String, Object>();
		map.put("accountId", accountId);
		map.put("txAmount", txAmount);//143472  131607
		map.put("productId", productId);
		map.put("orgOrderId", orgOrderId);
	    
		TppService tppService = (TppService) TppUtil.tppService();
		return tppService.tppInvestFail(map);
	}
	
	@RequestMapping(value = "/user/downloadTardeDetailAll")
	@ResponseBody
	public Object downloadTardeDetailAll(final String date){
		final Map<String, Object> map = new HashMap<String, Object>();
		map.put("date", date);
		TppService tppService = (TppService) TppUtil.tppService();
		return tppService.downTradeDetailAll(map);
	}
	
	@RequestMapping(value = "/user/downloadTardeDetail")
	@ResponseBody
	public Object downloadTardeDetail(final String date){
		final Map<String, Object> map = new HashMap<String, Object>();
		map.put("date", date);
		TppService tppService = (TppService) TppUtil.tppService();
		return tppService.downTradeDetail(map);
	}
}
