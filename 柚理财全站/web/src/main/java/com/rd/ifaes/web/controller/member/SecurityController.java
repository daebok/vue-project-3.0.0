package com.rd.ifaes.web.controller.member;

import com.google.common.collect.Maps;
import com.rd.ifaes.common.annotation.TokenValid;
import com.rd.ifaes.common.constant.TokenConstants;
import com.rd.ifaes.common.constant.TppServiceEnum;
import com.rd.ifaes.common.dict.CacheKeys;
import com.rd.ifaes.common.dict.DictTypeEnum;
import com.rd.ifaes.common.exception.BussinessException;
import com.rd.ifaes.common.util.MD5Utils;
import com.rd.ifaes.common.util.SessionUtils;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.common.util.TppUtil;
import com.rd.ifaes.common.util.ip.IPUtil;
import com.rd.ifaes.core.core.constant.CallbackConstant;
import com.rd.ifaes.core.core.constant.Constant;
import com.rd.ifaes.core.core.constant.MessageConstant;
import com.rd.ifaes.core.core.constant.ResourceConstant;
import com.rd.ifaes.core.core.sms.msg.GetCodeLog;
import com.rd.ifaes.core.core.util.ConfigUtils;
import com.rd.ifaes.core.core.util.DictUtils;
import com.rd.ifaes.core.core.util.ResourceUtils;
import com.rd.ifaes.core.credit.service.UserCreditService;
import com.rd.ifaes.core.tpp.model.cbhb.CbhbBindMobileNoModel;
import com.rd.ifaes.core.tpp.model.jx.JxAutoBidAuthPlusModel;
import com.rd.ifaes.core.tpp.model.jx.JxAutoCreditInvestAuthPlusModel;
import com.rd.ifaes.core.tpp.model.jx.JxCreditAuthQueryModel;
import com.rd.ifaes.core.tpp.model.jx.JxPasswordResetPlusModel;
import com.rd.ifaes.core.tpp.model.jx.JxPasswordSetModel;
import com.rd.ifaes.core.tpp.model.ufx.UfxLoginModel;
import com.rd.ifaes.core.tpp.service.TppService;
import com.rd.ifaes.core.tpp.util.JxConfig;
import com.rd.ifaes.core.user.domain.*;
import com.rd.ifaes.core.user.model.UserCacheModel;
import com.rd.ifaes.core.user.model.UserModel;
import com.rd.ifaes.core.user.model.UserSecurityAnswerModel;
import com.rd.ifaes.core.user.service.*;
import com.rd.ifaes.web.controller.WebController;
import org.apache.shiro.util.CollectionUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
     * 额度信息处理类
     */
    @Resource
    private transient UserCreditService userCreditService;
    @Resource 
    transient UserAuthSignLogService userAuthSignLogService;
    @Resource
    private transient UserIdentifyService userIdentifyService;

    /**
     * 前往本地开户页面
     *
     * @return
     */
    @RequestMapping(value = "/member/security/realNameIdentify")
    public String realNameIdentify(final Model model) {
        String tppName = ConfigUtils.getTppName();
        final UserCache userCache = (UserCache) SessionUtils.getSessionAttr(Constant.SESSION_USER_CACHE);
        final UserCacheModel userCacheModel = userCache.prototype();
        if (TppServiceEnum.CBHB.getName().equals(tppName)) {//渤海银行的开户无需校验直接跳入第三方界面
            Object object = userCacheService.tppRealName(userCacheModel);
            if (object instanceof Boolean) {
                if (true == (boolean) object) {
                    model.addAttribute("msg", "存管已经开户过,数据录入成功!");
                } else if (false == (boolean) object) {
                    model.addAttribute("msg", "存管无法登陆");
                }
                model.addAttribute("resultFlag", String.format(CacheKeys.PREFIX_TPP_ORDER_HANDLE_KEY.getKey(), System.currentTimeMillis()));
                model.addAttribute("picType", CallbackConstant.REGISTER_PICTYPE);
                model.addAttribute("requestType", CallbackConstant.REGISTER_REQUEST_TYPE);
                model.addAttribute("resultName", CallbackConstant.REGISTER_REQUEST_NAME);
                model.addAttribute("resultUrl", CallbackConstant.REGISTER_REQUEST_CBHB_URL);
                return "/retRegResult";
            } else {
                model.addAttribute("reg", object);
                return ConfigUtils.getTppName() + "/register";
            }
        } else if (TppServiceEnum.CHINAPNR.getName().equals(tppName)) {
            if (UserCache.USER_NATURE_VOUCH.equals(userCache.getUserNature())) {
                model.addAttribute(IS_VOUCH, userCache.getUserNature());
            }

            //跳转页面，默认企业开户页面
            String goPage = "member/security/companyIdentify";
            if (UserCache.USER_NATURE_PERSON.equals(userCache.getUserNature())) {  //个人开户页面
                goPage = "member/security/userIdentify";
            }
            return goPage;
        } else if (TppServiceEnum.JXBANK.getName().equals(tppName)) {
            if (UserCache.USER_NATURE_VOUCH.equals(userCache.getUserNature())) {
                model.addAttribute(IS_VOUCH, userCache.getUserNature());
            }
            User user = SessionUtils.getSessionUser();
            model.addAttribute("mobile", user.getMobile());
            //跳转页面，默认企业开户页面
            String goPage = "member/security/companyIdentify";
            if (UserCache.USER_NATURE_PERSON.equals(userCache.getUserNature())) {  //个人开户页面
                goPage = "member/security/userIdentify";
            }
            return goPage;
        } else {
            return "404";
        }

    }

    /**
     * 获得认证结果-针对开户业务
     *
     * @return
     */
    @RequestMapping(value = "/member/security/getIdentifyResult")
    @ResponseBody
    public Object getIdentifyResult() {
        final User sessionUser = SessionUtils.getSessionUser(BussinessException.TYPE_JSON);
        final User user = userService.get(sessionUser.getUuid());
        final UserCache userCache = userCacheService.findByUserId(sessionUser.getUuid());
        final UserIdentify userIdentify = identifyService.findByUserId(sessionUser.getUuid());

        //更新缓存
        SessionUtils.setSessionAttr(Constant.SESSION_USER, user);
        SessionUtils.setSessionAttr(Constant.SESSION_USER_IDENTIFY, userIdentify);

        //页面参数
        final Map<String, Object> data = Maps.newHashMap();
        data.put(RESULT_NAME, true);
        data.put("tppUserCustId", user.getTppUserCustId());
        data.put("tpp_login_url", Constant.TPP_LOGIN_URL);

        //审核信息
        if (UserCache.USER_NATURE_PERSON.equals(userCache.getUserNature())) {
            data.put(REALNAME_STATUS, userIdentify.getRealNameStatus());
            data.put("realNameVerifyTime", userIdentify.getRealNameVerifyTime());
        } else {
            final UserCompanyInfo userCompanyInfo = companyService.findByUserId(sessionUser.getUuid());
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
        final User sessionUser = SessionUtils.getSessionUser(BussinessException.TYPE_JSON);
        final UserIdentify userIdentify = identifyService.findByUserId(sessionUser.getUuid());
        SessionUtils.setSessionAttr(Constant.SESSION_USER_IDENTIFY, userIdentify);

        //页面参数
        final Map<String, Object> data = Maps.newHashMap();
        data.put(RESULT_NAME, true);
        data.put(REALNAME_STATUS, userIdentify.getRealNameStatus());
        data.put("authSignStatus", userIdentify.getAuthSignStatus());
        data.put("emailStatus", userIdentify.getEmailStatus());
        return data;
    }

    /**
     * 资金托管账户开户-个人
     *
     * @return
     * @throws Exception
     */

    @RequestMapping(value = "/member/security/tppRegister")
    @ResponseBody
    public Object tppRegisger(final UserCacheModel cache, final Model model, final HttpServletRequest request) {
        LOGGER.info("[实名的信息,姓名：{},身份证号码：{}]", cache.getRealName(), cache.getIdNo());
        cache.setIp(IPUtil.getRemortIP(request));
        userCacheService.tppRealName(cache);
        return renderResult(true, ResourceUtils.get(ResourceConstant.USER_TPP_REGISTER_SUCCESS));
    }

    /**
     * 资金托管账户开户-企业
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/member/security/tppCompanyRegister")
    public String tppCompanyRegister(final UserCacheModel cache, final Model model) {
        model.addAttribute("reg", userCacheService.tppCompanyRealName(cache));
        return ConfigUtils.getTppName() + "/companyRegister";
    }

    /**
     * 资金托管账户登录
     *
     * @return
     */
    @RequestMapping(value = "/member/security/apiLogin")
    public String tppLogin(final Model model) {
        final User user = SessionUtils.getSessionUser(BussinessException.TYPE_CLOSE);
        final UserIdentify userIdentify = identifyService.findByUserId(user.getUuid());
        if (!Constant.FLAG_YES.equals(userIdentify.getRealNameStatus())) {
            throw new BussinessException(ResourceUtils.get(ResourceConstant.USER_TPP_STATUS_NOT_OPEN), "/member/security/realNameIdentify.html", "马上开通");
        }
        //参数封装
        final Map<String, Object> map = Maps.newHashMap();
        map.put("userCustId", user.getTppUserCustId());
        TppService tppService = (TppService) TppUtil.tppService();
        final UfxLoginModel loginModel = (UfxLoginModel) tppService.tppUserLogin(map);
        model.addAttribute("login", loginModel);
        return ConfigUtils.getTppName() + "/login";
    }

    //***********************************************登录密码********************************************************

    /**
     * 修改登录密码页
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/member/security/modifyPwd")
    public String modifyPwd(final Model model) {
        initUserNature(null, model);
        return "/member/security/modifyPwd";
    }

    /**
     * 修改登录密码
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/member/security/doModifyPwd")
    @ResponseBody
    public Object doModifyPwd(final UserModel userModel) {
        Map<String, Object> data = Maps.newHashMap();
        data.put(RESULT_NAME, userService.modifyPwd(userModel));
        UserIdentify userIdentify = (UserIdentify) SessionUtils.getSessionAttr(Constant.SESSION_USER_IDENTIFY);
        data.put("realNameStatus", userIdentify.getRealNameStatus());
        return data;
    }

    //**********************************************邮箱********************************************************

    /**
     * 绑定邮箱页面
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/member/security/bindEmail")
    public String bindEmail(final Model model, final HttpServletRequest request) {
        initUserNature(null, model);
        setToken(TokenConstants.TOKEN_EMAIL_BIND, request);
        return "/member/security/bindEmail";
    }

    /**
     * 绑定邮箱-获取验证码
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/member/security/bindEmailCode")
    @ResponseBody
    public Object bindEmailCode(final String email) {
        final User user = SessionUtils.getSessionUser();
        final UserModel model = new UserModel();
        model.setUuid(user.getUuid());
        model.setEmail(email);
        model.checkEmail();

        sendMessage(getSessionUser(), email, MessageConstant.MESSAGE_BIND_EMAIL);
        return renderResult(true, "获取成功");
    }

    /**
     * 绑定邮箱
     *
     * @return
     */
    @RequestMapping(value = "/member/security/doBindEmail")
    @ResponseBody
    @TokenValid(value = TokenConstants.TOKEN_EMAIL_BIND, dispatch = true)
    public Object doBindEmail(final UserModel model) {
        userService.modifyEmail(model);
        return renderResult(true, "绑定成功");
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
        final User user = userService.get(SessionUtils.getSessionUser().getUuid());
        final UserIdentify userIdentify = identifyService.findByUserId(user.getUuid());
        final UserCache userCache = (UserCache) SessionUtils.getSessionAttr(Constant.SESSION_USER_CACHE);

        final Map<String, Object> data = Maps.newHashMap();
        data.put(RESULT_NAME, true);
        data.put("mobile", user.getHideMobile());
        data.put("email", user.getHideEmail());
        data.put("userNature", userCache.getUserNature());
        data.put(REALNAME_STATUS, userIdentify.getRealNameStatus());
        data.put(QUESTION_LIST, secAnswerService.findUserPwdQuestion(user.getUuid()));
        return data;
    }

    /**
     * 修改邮箱页面
     *
     * @return
     */
    @RequestMapping(value = "/member/security/modifyEmail")
    public String modifyEmail(final Model model, final HttpServletRequest request) {
        initUserNature(null, model);
        setToken(TokenConstants.TOKEN_EMAIL_BIND, request);
        return "/member/security/modifyEmail";
    }

    /**
     * 修改邮箱-获取验证码
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/member/security/modifyEmailCode")
    @ResponseBody
    public Object modifyEmailCode(final String checkType) {
        final User user = SessionUtils.getSessionUser();
        String messageType = MessageConstant.MESSAGE_MODIFY_EMAIL_PHONECODE;
        String addr = user.getMobile();
        if (Constant.EMAIL_VALID_EMAIL.equals(checkType)) {
            messageType = MessageConstant.MESSAGE_MODIFY_EMAIL_EMAILCODE;
            addr = user.getEmail();
        }
        sendMessage(user, addr, messageType);
        return renderResult(true, "获取成功");
    }

    /**
     * 修改邮箱
     *
     * @return
     */
    @RequestMapping(value = "/member/security/doModifyEmail")
    @ResponseBody
    public Object doModifyEmail(final UserModel model) {
        final User user = getSessionUser();
        model.setUserName(user.getUserName());
        model.setUuid(user.getUuid());
        model.setEmail(user.getEmail());
        model.setMobile(user.getMobile());
        model.validModifyEmail();

        final Map<String, Object> data = Maps.newHashMap();
        data.put(RESULT_NAME, true);
        data.put("modifyEmailSign", model.getModifyEmailSign());
        return data;
    }

    //**********************************************邮箱********************************************************
    //**********************************************手机********************************************************

    /**
     * 绑定手机-获取验证码
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/member/security/bindPhoneCode")
    @ResponseBody
    public Object bindPhoneCode(final String mobile) {
        final User user = SessionUtils.getSessionUser();
        final UserModel model = new UserModel();
        model.setUuid(user.getUuid());
        model.setMobile(mobile);
        model.checkMobile();

        sendMessage(getSessionUser(), mobile, MessageConstant.MESSAGE_BIND_PHONE);
        return renderResult(true, "获取成功");
    }

    /**
     * 绑定手机
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/member/security/doBindPhone")
    @ResponseBody
    @TokenValid(value = TokenConstants.TOKEN_MOBILE_BIND, dispatch = true)
    public Object doBindPhone(final UserModel model) {
        userService.modifyMobile(model);
        return renderResult(true, "获取成功");
    }

    /**
     * 前往修改手机页面
     *
     * @return
     */
    @RequestMapping(value = "/member/security/modifyPhone")
    public String modifyPhone(final Model model, final HttpServletRequest request) {
        //返回页面
        String goPage = "/member/security/modifyCompanyPhone";
        final User user = SessionUtils.getSessionUser(BussinessException.TYPE_CLOSE);
        if (TppServiceEnum.CHINAPNR.getName().equals(ConfigUtils.getTppName())) {
            final UserCache userCache = userCacheService.findByUserId(user.getUuid());
            if (UserCache.USER_NATURE_VOUCH.equals(userCache.getUserNature())) {
                model.addAttribute(IS_VOUCH, userCache.getUserNature());
            }
            initUserNature(userCache, model);
            setToken(TokenConstants.TOKEN_MOBILE_BIND, request);
            if (UserCache.USER_NATURE_PERSON.equals(userCache.getUserNature())) {
                goPage = "/member/security/modifyUserPhone";
            }
        } else if (TppServiceEnum.CBHB.getName().equals(ConfigUtils.getTppName())) {
            TppService tppService = (TppService) TppUtil.tppService();
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("oldMobileNo", user.getMobile());
            map.put("plaCustId", user.getTppUserCustId());
            CbhbBindMobileNoModel bindMobileNoModel = (CbhbBindMobileNoModel) tppService.tppUpdateMobileNo(map);
            model.addAttribute("reg", bindMobileNoModel);
            goPage = ConfigUtils.getTppName() + "/bindMobileWeb";
        } else if (TppServiceEnum.JXBANK.getName().equals(ConfigUtils.getTppName())) {
            final UserCache userCache = userCacheService.findByUserId(user.getUuid());
            if (UserCache.USER_NATURE_VOUCH.equals(userCache.getUserNature())) {
                model.addAttribute(IS_VOUCH, userCache.getUserNature());
            }
            initUserNature(userCache, model);
            setToken(TokenConstants.TOKEN_MOBILE_BIND, request);
            if (UserCache.USER_NATURE_PERSON.equals(userCache.getUserNature())) {
                goPage = "/member/security/modifyUserPhone";
            }
        } else {

        }
        return goPage;
    }

    /**
     * 修改支付密码
     *
     * @return
     * @author QianPengZhan
     * @date 2017年3月4日
     */
    @RequestMapping(value = "/member/security/modifyCbhbPass")
    public String modifyPayPass(final Model model, final HttpServletRequest request) {
        final User user = SessionUtils.getSessionUser(BussinessException.TYPE_CLOSE);
        model.addAttribute("reg", userService.modifyPayPass(user));
        return ConfigUtils.getTppName() + "/bindPass";
    }


    /**
     * 修改绑定手机-获取验证码
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/member/security/modifyPhoneCode")
    @ResponseBody
    public Object modifyPhoneCode(final String checkType) {
        final User user = SessionUtils.getSessionUser();
        String messageType = MessageConstant.MESSAGE_MODIFY_PHONE_EMAILCODE;
        if (Constant.MOBILE_VALID_MOBILE.equals(checkType)) {
            String mobile = user.getMobile();
            checkPhoneCodeSpaceTime(mobile);
            new GetCodeLog(user, MessageConstant.MESSAGE_MODIFY_PHONE_PHONECODE, mobile).doEvent();
            savePhoneCodeSendTime(mobile);
        } else {
            sendMessage(user, user.getEmail(), messageType);
        }
        return renderResult(true, "获取成功");
    }

    /**
     * 修改绑定手机 校验密码和密保
     *
     * @return
     */
    @RequestMapping(value = "/member/security/doModifyPhone")
    @ResponseBody
    public Object doModifyPhone(final UserModel model) {
        final User user = getSessionUser();
        model.setUuid(user.getUuid());
        model.setUserName(user.getUserName());
        model.setMobile(user.getMobile());
        model.setEmail(user.getEmail());
        model.validModifyMobile();

        final Map<String, Object> data = Maps.newHashMap();
        data.put(RESULT_NAME, true);
        data.put("modifyPhoneSign", model.getModifyPhoneSign());
        return data;
    }

    //**********************************************手机  end********************************************************

    //**********************************************密保问题  start**************************************************

    /**
     * 密保问题展示页面
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/member/security/pwdQuestion")
    public String pwdQuestion(final String answerPwdQuestSign, final Model model) {
        final User user = SessionUtils.getSessionUser(BussinessException.TYPE_CLOSE);
        final List<UserSecurityAnswer> list = secAnswerService.findByUserId(user.getUuid());
        initUserNature(null, model);
        String goPage = "/member/security/setPwdQuestion";
        if (!CollectionUtils.isEmpty(list)) {
            goPage = "/member/security/answerPwdQuestion";
            if (StringUtils.isNotBlank(answerPwdQuestSign)
                    && answerPwdQuestSign.equals(MD5Utils.encode(user.getMobile() + Constant.RESET_PASS_QUEST))) {
                goPage = "/member/security/setPwdQuestion";
            }
        }
        return goPage;
    }

    /**
     * 获取当前用户的密保问题
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/member/security/getUserPwdQuestion")
    @ResponseBody
    public Object getUserPwdQuestion(final String answerPwdQuestSign) {
        final User user = SessionUtils.getSessionUser();
        Map<String, Object> questionList = new HashMap<>();
        questionList.put("result", true);
        questionList.put("questionList", secAnswerService.findUserPwdQuestion(user.getUuid()));
        return questionList;
    }


    /**
     * 获得密保问题
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/member/security/getPwdQuestion")
    @ResponseBody
    public Object getPwdQuestion(final String answerFlag) {
        final Map<String, Object> data = Maps.newHashMap();
        data.put(RESULT_NAME, true);
        //如果answerFlag=1，则表示获取的是回答过的问题
        if (Constant.FLAG_YES.equals(answerFlag)) {
            User user = SessionUtils.getSessionUser();
            data.put(QUESTION_LIST, secAnswerService.findUserPwdQuestion(user.getUuid()));
        } else {
            data.put(QUESTION_LIST, DictUtils.list(DictTypeEnum.SECURITY_QUESTION.getValue()));
        }

        return data;
    }

    /**
     * 调整设置密保问题
     */
    @RequestMapping(value = "/member/security/setPwdQuestion.html")
    public String goSetPwdQuestionPage() {
        return "/member/security/setPwdQuestion";
    }


    /**
     * 设置密保问题
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/member/security/doSetPwdQuestion")
    @ResponseBody
    public Object doSetPwdQuestion(final UserSecurityAnswerModel answer, final HttpServletRequest request) {
        answer.setAddIp(IPUtil.getRemortIP(request));
        secAnswerService.doSetPwdQuestion(answer);
        return renderResult(true, "设置成功");
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
        data.put("answerPwdQuestSign", MD5Utils.encode(user.getMobile() + Constant.RESET_PASS_QUEST));
        return renderResult(true, "提交成功");
    }

    /**
     * 重置密保问题页面
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/member/security/resetPwdQuestion")
    public String resetPwdQuestion(final Model model) {
        initUserNature(null, model);
        return "/member/security/pwdQuestion";
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
    //**********************************************密保问题  end**************************************************

    @RequestMapping(value = "/member/security/bindAuthorization")
    public String bindAuthorization(final Model model) {
        final User sessionUser = SessionUtils.getSessionUser(BussinessException.TYPE_CLOSE);
        model.addAttribute("mobile", sessionUser.getMobile());
        model.addAttribute("payPwdStatus", sessionUser.getPayPwd());
        return "/member/security/bindAuthorization";
    }

    /**
     * 授权
     */
    @RequestMapping(value = "/member/security/authSign")
    public String authSign(final HttpServletRequest request, final Model model, final String smsCode) {
        final User sessionUser = SessionUtils.getSessionUser(BussinessException.TYPE_CLOSE);
        // 封装授权参数及记录
        final String addIp = StringUtils.getRemoteAddr(request);

        // 封装授权参数
        final Map<String, Object> map = new HashMap<String, Object>();
        map.put("smsCode", smsCode);
        map.put("addIp", addIp);
        map.put("authOption", JxConfig.JXBANK_AUTH_OPEN);
        map.put("userId", sessionUser.getUuid());
        final JxAutoBidAuthPlusModel authSign = (JxAutoBidAuthPlusModel) authSignService.auth(map);
        model.addAttribute("authSign", authSign);
        model.addAttribute("sign", authSign.getSign());
        return ConfigUtils.getTppName() + "/authSign";
    }

    /**
     * 解除授权
     */
    @RequestMapping(value = "/member/security/authSignCancel")
    @ResponseBody
    public Object authSignCancel(final HttpServletRequest request, final Model model) {
        final User sessionUser = SessionUtils.getSessionUser(BussinessException.TYPE_CLOSE);
        // 封装授权参数及记录
        final String addIp = StringUtils.getRemoteAddr(request);
        final Map<String, Object> map = new HashMap<String, Object>();
        map.put("addIp", addIp);
        map.put("authOption", JxConfig.JXBANK_AUTH_CLOSE);
        map.put("userId", sessionUser.getUuid());
        authSignService.auth(map);

        return renderResult(true, "撤销成功");
    }

    private void initUserNature(final UserCache userInfo, final Model model) {
        if (userInfo == null) {
            model.addAttribute(IS_VOUCH, ((UserCache) SessionUtils.getSessionAttr(Constant.SESSION_USER_CACHE)).getUserNature());
        } else {
            model.addAttribute(IS_VOUCH, userInfo.getUserNature());
        }
    }

    /**
     * 跳转忘记密保页面
     */
    @RequestMapping(value = "/member/security/reSetPwdQuestion")
    public String reSetPwdQuestion() {
        return "/member/security/pwdQuestion";
    }

    /**
     * 校验机构代码是否可使用
     *
     * @param orgCode
     * @param creditCode
     * @return true 可使用   false不能使用
     * @author xhf
     * @date 2016年9月26日
     */
    @RequestMapping(value = "/member/security/checkRegCode")
    @ResponseBody
    public Object checkRegCode(UserCacheModel model) {
        boolean flag = true;
        String msg = MSG_SUCCESS;
        User user = SessionUtils.getSessionUser();

        UserCompanyInfo codeModel = null;

        if (StringUtils.isNotBlank(model.getOrgCode())) {
            codeModel = new UserCompanyInfo();
            codeModel.setUserId(user.getUuid());
            codeModel.setOrgCode(model.getOrgCode());

            int orgCodecount = companyService.countRegisterCode(codeModel);
            if (orgCodecount > 0) {
                flag = false;
                msg = ResourceUtils.get(ResourceConstant.ORG_CODE_IS_USED);
            }
        } else if (StringUtils.isNotBlank(model.getBussinessCode())) {
            codeModel = new UserCompanyInfo();
            codeModel.setUserId(user.getUuid());
            codeModel.setBussinessCode(model.getBussinessCode());

            int bussCodecount = companyService.countRegisterCode(codeModel);
            if (bussCodecount > 0) {
                flag = false;
                msg = ResourceUtils.get(ResourceConstant.BUSSINESS_CODE_IS_USED);
            }
        } else if (StringUtils.isNotBlank(model.getCreditCode())) {
            codeModel = new UserCompanyInfo();
            codeModel.setUserId(user.getUuid());
            codeModel.setCreditCode(model.getCreditCode());

            int count = companyService.countRegisterCode(codeModel);
            if (count > 0) {
                flag = false;
                msg = ResourceUtils.get(ResourceConstant.CREDIT_CODE_IS_USED);
            }
        }

        return renderResult(flag, msg);
    }

    /**
     * 存管发送验证
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/member/security/smsCodeApply")
    @ResponseBody
    public Object smsCodeApply(String mobile, String reqType, String srvTxCode, String cardId) {
        userService.smsCodeApply(mobile, reqType, srvTxCode, cardId);
        return renderResult(true, "发送成功");
    }


    /**
     * 根据身份证查询存管id
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/member/security/accountIdQuery")
    @ResponseBody
    public Object accountIdQuery(String idNo, String realName) {
        userCacheService.accountIdQuery(idNo, realName);
        return renderResult(true, "发送成功");
    }

    /**
     * 重置密码
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/member/security/resetPwd")
    public String resetPwd(final Model model, final String smsCode) {
        final User user = SessionUtils.getSessionUser(BussinessException.TYPE_CLOSE);
        JxPasswordResetPlusModel jxPasswordResetPlusModel = (JxPasswordResetPlusModel) userService.resetPwd(user, smsCode);
        model.addAttribute("obj", jxPasswordResetPlusModel);
        model.addAttribute("sign", jxPasswordResetPlusModel.getSign());
        return ConfigUtils.getTppName() + "/resetPwd";
    }

    /**
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/member/security/modifyTransactionPwd")
    public String modifyTransactionPwd(final Model model) {
        final User user = SessionUtils.getSessionUser(BussinessException.TYPE_CLOSE);
        model.addAttribute("mobile", user.getMobile());
        return "/member/security/modifyTransactionPwd";
    }

    /**
     * 设置密码
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/member/security/setPwd")
    public String setPwd(final Model model) {
        final User user = SessionUtils.getSessionUser(BussinessException.TYPE_CLOSE);
        JxPasswordSetModel jxPasswordSetModel = (JxPasswordSetModel) userService.modifyPayPass(user);
        
        model.addAttribute("obj", jxPasswordSetModel);
        model.addAttribute("sign", jxPasswordSetModel.getSign());
        return ConfigUtils.getTppName() + "/setPwd";
    }


    /**
     * 额度申请界面
     */
    @RequestMapping(value = "/member/credit/index")
    public String applyCredit(final Model model) {
    	final User sessionUser = SessionUtils.getSessionUser(BussinessException.TYPE_JSON);
    	final UserIdentify userIdentify = identifyService.findByUserId(sessionUser.getUuid());
    	model.addAttribute(REALNAME_STATUS, userIdentify.getRealNameStatus());
        return "/member/credit/index";
    }

    /**
     * 额度申请
     *
     * @return
     */
    @RequestMapping(value = "/member/security/applyCredit")
    @ResponseBody
    public Object doApplyCredit(String account, String content) {
        final User user = SessionUtils.getSessionUser(BussinessException.TYPE_CLOSE);
        String userId = user.getUuid();
        userCreditService.applyCredit(userId, account, content);
        return renderResult(true, "申请成功");
    }

    /**
     * 去签约自动债权转让页面
     */
    @RequestMapping(value = "/member/security/autoCreditInvestAuth.html")
    public String autoCreditInvestAuth(final Model model) {
    	final User sessionUser = SessionUtils.getSessionUser(BussinessException.TYPE_CLOSE);
    	final User user = userService.get(sessionUser.getUuid());
    	//查询第三方是否签约过
        final JxCreditAuthQueryModel jxCreditAuthQueryModel = userAuthSignLogService.creditAuthQuery(UserAuthSignLog.SERVICE_TYPE_AUTO_BOND, user.getTppUserCustId());
        if(JxConfig.JXBANK_AUTH_OPEN.equals(jxCreditAuthQueryModel.getState())){
        	userIdentifyService.setAutoCreditInvestAuthStatus(user.getUuid(), JxConfig.JXBANK_AUTH_OPEN);//已签约
        	throw new BussinessException("你已签约自动债权，请退出后刷新查看!", BussinessException.TYPE_JSON);
        }
    	UserIdentify userIdentify = (UserIdentify) SessionUtils.getSessionAttr(Constant.SESSION_USER_IDENTIFY);
    	model.addAttribute("mobile", user.getMobile());
    	model.addAttribute("realNameStatus", userIdentify.getRealNameStatus());
        return "/member/security/autoCreditInvestAuth";
    }
    
    
    /**
     * 签约自动债权转让
     * @param smsCode 短信验证码
     */
    @RequestMapping(value = "/member/security/doAutoCreditInvestAuth")
    public String autoCreditInvestAuth(final HttpServletRequest request, final Model model, final String smsCode) {
    	if(StringUtils.isBlank(smsCode)){
    		throw new BussinessException("短信验证码不能为空", BussinessException.TYPE_CLOSE);
    	}
        final User sessionUser = SessionUtils.getSessionUser(BussinessException.TYPE_CLOSE);
        // 封装授权参数
        final JxAutoCreditInvestAuthPlusModel jxmodel = authSignService.autoCreditInvestAuthPlus(smsCode, sessionUser.getUuid(), StringUtils.getRemoteAddr(request));
        model.addAttribute("model", jxmodel);
        model.addAttribute("sign", jxmodel.getSign());
        return "/jxbank/autoCreditInvestAuth";
    }
    
    /**
     * 撤销自动债权转让签约
     */
    @RequestMapping(value = "/member/security/doAutoCreditInvestAuthCancel")
    @ResponseBody
    public Object doAutoCreditInvestAuthCancel(final HttpServletRequest request, final Model model) {
        authSignService.autoCreditInvestAuthCancel(StringUtils.getRemoteAddr(request));
        return renderResult(true, "撤销自动债权转让签约增强成功！");
    }
}
