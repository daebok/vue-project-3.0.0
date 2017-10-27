package com.rd.ifaes.core.user.service.impl;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import com.google.common.collect.Maps;
import com.rd.account.domain.Account;
import com.rd.account.domain.AccountLog;
import com.rd.account.model.AccountQueryModel;
import com.rd.account.model.AccountRegisterModel;
import com.rd.account.service.AccountLogService;
import com.rd.account.service.AccountService;
import com.rd.ifaes.common.annotation.Cacheable;
import com.rd.ifaes.common.constant.CbhbConstant;
import com.rd.ifaes.common.constant.JxbankConstant;
import com.rd.ifaes.common.constant.TokenConstants;
import com.rd.ifaes.common.constant.TppServiceEnum;
import com.rd.ifaes.common.dict.CacheKeys;
import com.rd.ifaes.common.dict.CommonEnum;
import com.rd.ifaes.common.dict.ExpireTime;
import com.rd.ifaes.common.dict.ProjectCollectionEnum;
import com.rd.ifaes.common.dict.ScoreEnum;
import com.rd.ifaes.common.dict.UserEnum;
import com.rd.ifaes.common.exception.BussinessException;
import com.rd.ifaes.common.orm.Page;
import com.rd.ifaes.common.rabbit.RabbitUtils;
import com.rd.ifaes.common.rabbit.model.MqActPlanModel;
import com.rd.ifaes.common.resource.CbhbResource;
import com.rd.ifaes.common.util.BigDecimalUtils;
import com.rd.ifaes.common.util.DateUtils;
import com.rd.ifaes.common.util.FileUtil;
import com.rd.ifaes.common.util.FormDecryptUtils;
import com.rd.ifaes.common.util.MD5Utils;
import com.rd.ifaes.common.util.PasswordHelper;
import com.rd.ifaes.common.util.SessionUtils;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.common.util.TokenUtils;
import com.rd.ifaes.common.util.TppUtil;
import com.rd.ifaes.common.util.excel.ExcelUtil;
import com.rd.ifaes.common.util.ip.IPUtil;
import com.rd.ifaes.common.util.resource.UserResource;
import com.rd.ifaes.common.validator.BeanValidators;
import com.rd.ifaes.core.account.domain.AccountBankCard;
import com.rd.ifaes.core.account.domain.AccountCheck;
import com.rd.ifaes.core.account.domain.Cash;
import com.rd.ifaes.core.account.model.AccountBankModel;
import com.rd.ifaes.core.account.service.AccountBankCardService;
import com.rd.ifaes.core.account.service.AccountCheckService;
import com.rd.ifaes.core.base.service.BaseServiceImpl;
import com.rd.ifaes.core.core.constant.CacheConstant;
import com.rd.ifaes.core.core.constant.ConfigConstant;
import com.rd.ifaes.core.core.constant.Constant;
import com.rd.ifaes.core.core.constant.MessageConstant;
import com.rd.ifaes.core.core.constant.MqConstant;
import com.rd.ifaes.core.core.constant.ResourceConstant;
import com.rd.ifaes.core.core.sms.msg.AccountCheckWarnMsg;
import com.rd.ifaes.core.core.util.CacheUtils;
import com.rd.ifaes.core.core.util.ConfigUtils;
import com.rd.ifaes.core.core.util.DictUtils;
import com.rd.ifaes.core.core.util.OrderNoUtils;
import com.rd.ifaes.core.core.util.ResourceUtils;
import com.rd.ifaes.core.core.util.ValidateUtils;
import com.rd.ifaes.core.operate.domain.UserVip;
import com.rd.ifaes.core.operate.service.ActivityPlanService;
import com.rd.ifaes.core.operate.service.UserVipLevelService;
import com.rd.ifaes.core.operate.service.UserVipService;
import com.rd.ifaes.core.project.domain.ProjectCollection;
import com.rd.ifaes.core.project.service.ProjectCollectionService;
import com.rd.ifaes.core.score.service.UserScoreService;
import com.rd.ifaes.core.statistic.domain.StatisticUser;
import com.rd.ifaes.core.statistic.service.StatisticUserService;
import com.rd.ifaes.core.sys.domain.Operator;
import com.rd.ifaes.core.sys.domain.OperatorCustomer;
import com.rd.ifaes.core.sys.service.OperatorCustomerService;
import com.rd.ifaes.core.sys.service.OperatorService;
import com.rd.ifaes.core.tpp.exception.CbhbException;
import com.rd.ifaes.core.tpp.model.cbhb.CbhbBindMobileNoModel;
import com.rd.ifaes.core.tpp.model.cbhb.CbhbExistUserRegisterModel;
import com.rd.ifaes.core.tpp.model.cbhb.chk.CbhbChkBaseModel;
import com.rd.ifaes.core.tpp.model.ufx.UfxModel;
import com.rd.ifaes.core.tpp.model.ufx.UfxQueryBalanceModel;
import com.rd.ifaes.core.tpp.service.TppService;
import com.rd.ifaes.core.tpp.service.tpp.UfxService;
import com.rd.ifaes.core.user.domain.User;
import com.rd.ifaes.core.user.domain.UserBaseInfo;
import com.rd.ifaes.core.user.domain.UserCache;
import com.rd.ifaes.core.user.domain.UserCompanyInfo;
import com.rd.ifaes.core.user.domain.UserIdentify;
import com.rd.ifaes.core.user.domain.UserInvite;
import com.rd.ifaes.core.user.domain.UserLoginLog;
import com.rd.ifaes.core.user.mapper.UserMapper;
import com.rd.ifaes.core.user.model.UserCacheModel;
import com.rd.ifaes.core.user.model.UserIdentifyModel;
import com.rd.ifaes.core.user.model.UserModel;
import com.rd.ifaes.core.user.service.UserBaseInfoService;
import com.rd.ifaes.core.user.service.UserCacheService;
import com.rd.ifaes.core.user.service.UserCompanyInfoService;
import com.rd.ifaes.core.user.service.UserFreezeService;
import com.rd.ifaes.core.user.service.UserIdentifyService;
import com.rd.ifaes.core.user.service.UserInviteService;
import com.rd.ifaes.core.user.service.UserLoginLogService;
import com.rd.ifaes.core.user.service.UserService;


/**
 * 用户信息处理类
 *
 * @author xhf
 * @version 3.0
 * @date 2016-6-8
 */
@Service("userService")
public class UserServiceImpl extends BaseServiceImpl<UserMapper, User> implements UserService {

    /**
     * 日志
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);
    /**
     * accountLogService 业务
     */
    @Resource
    private transient AccountLogService accountLogService;
    /**
     * accountCheckService业务处理
     */
    @Resource
    private transient AccountCheckService accountCheckService;
    /**
     * UFX处理类
     */
    @Resource
    private transient UfxService ufxService;
    /**
     * 资金处理类
     */
    @Resource
    private transient AccountService accountService;
    /**
     * 邀请好友处理类
     */
    @Resource
    private transient UserInviteService userInviteService;
    /**
     * 用户附属信息类
     */
    @Resource
    private transient UserCacheService userCacheService;
    /**
     * 用户认证信息类
     */
    @Resource
    private transient UserIdentifyService identifyService;
    /**
     * 用户基础信息类
     */
    @Resource
    private transient UserBaseInfoService baseInfoService;
    /**
     * 企业用户信息类
     */
    @Resource
    private transient UserCompanyInfoService companyService;
    /**
     * 用户登录日志类
     */
    @Resource
    private transient UserLoginLogService loginLogService;
    /**
     * 活动计划处理类
     */
    @Resource
    private transient ActivityPlanService actPlanService;
    /**
     * 用户积分类
     */
    @Resource
    private transient UserScoreService userScoreService;
    /**
     * 用户VIP信息类
     */
    @Resource
    private transient UserVipService userVipService;
    /**
     * 用户VIP等级信息类
     */
    @Resource
    private transient UserVipLevelService vipLevelService;
    /**
     * 后台用户类
     */
    @Resource
    private transient OperatorService operatorService;
    /**
     * 经纪人邀请管理处理
     */
    @Resource
    private OperatorCustomerService operatorCustomerService;
    /**
     * 用户公司信息
     */
    @Resource
    private transient UserCompanyInfoService userCompanyInfoService;

    @Resource
    private StatisticUserService statisticUserService;

    @Resource
    private transient AccountBankCardService accountBankCardService;

    @Resource
    private UserFreezeService userFreezeService;
    
    @Resource
    private ProjectCollectionService projectCollectionService;

    /**
     * 从缓存中获取对象
     */
    @Override
    @Cacheable(key = CacheConstant.KEY_PREFIX_USER_USER_ID + "{uuid}", expire = ExpireTime.ONE_HOUR)
    public User get(final String uuid) {
        return dao.get(uuid);
    }

    /**
     * 更新缓存对象
     */
    @Override
    public void update(final User entity) {
        CacheUtils.del(CacheConstant.KEY_PREFIX_USER_USER_ID.concat(entity.getUuid()));
        super.update(entity);
    }

    /**
     * 根据用户编号查询对象
     */
    @Override
    public User findByUserNo(final String userNo) {
        return dao.findByUserNo(userNo);
    }

    /**
     * 前台用户注册
     */
    @Override
    @Transactional
    public User doRegister(final String code) {
        // 获取session
        final UserModel model = (UserModel) SessionUtils.getSessionAttr("userModel");
        if (model == null) {
            throw new BussinessException(ResourceUtils.get(UserResource.USER_INFO_IS_RIGHT), BussinessException.TYPE_JSON);
        }
        /**
         *  校验手机号码  start
         */
        //校验手机号码长度和格式
        if (!StringUtils.isPhone(model.getMobile())) {
            throw new BussinessException(ResourceUtils.get(UserResource.USER_REGISTER_MOBILE_STYLE_ERROR), BussinessException.TYPE_JSON);
        }
        //校验手机号码是否已经被使用
        long time1 = System.currentTimeMillis();
        final int hasMobilePhone = checkRepeat(null, null, model.getMobile(), null);
        long time2 = System.currentTimeMillis();
        LOGGER.info("校验手机号码是否已经被使用,耗时：{}", (time2 - time1));
        if (hasMobilePhone > 0) {
            throw new BussinessException(ResourceUtils.get(UserResource.USER_MOBILE_IS_USED), BussinessException.TYPE_JSON);
        }
        /**
         *  校验手机号码 end
         */
        //验证码校验
        if (!ValidateUtils.checkCode(model.getMobile(), MessageConstant.MESSAGE_USER_REGISTER_PHONECODE, code)) {
            throw new BussinessException(ResourceUtils.get(UserResource.USER_VALIDCODE_ERROR), BussinessException.TYPE_JSON);
        }
        return addUserForRegister(model);
    }

    /**
     * 添加担保用户
     */
    @Override
    @Transactional
    public void addVouchUser(final UserModel userModel) {
        //校验密码
        userModel.checkPwd();
        //校验参数
        userModel.checkVouchManage();
        //校验新增企业参数是否重复
        checkAddCompanyRepeat(userModel);
        //校验token
        TokenUtils.validShiroToken(TokenConstants.TOKEN_ADD_USER_VOUCH);

        //默认参数
        userModel.setRealName(userModel.getCompanyName());
        userModel.setUserNature(UserCache.USER_NATURE_VOUCH);
        userModel.setRegMode(Constant.FLAG_YES);
        userModel.setMobilePhoneStatus(Constant.FLAG_YES);
        // 插入数据库
        final User user = addUserForRegister(userModel);

        //企业信息
        final UserCompanyInfo userCompanyInfo = companyService.findByUserId(user.getUuid());
        userCompanyInfo.setAddress(userModel.getAddress());
        userCompanyInfo.setEmail(userModel.getEmail());
        userCompanyInfo.setTaxRegNo(userModel.getTaxRegNo());
        userCompanyInfo.setLogo(userModel.getLogo());
        userCompanyInfo.setContacts(userModel.getContacts());
        companyService.update(userCompanyInfo);

        // 邮箱认证
        if (StringUtils.isNotBlank(userModel.getEmail())) {
            final UserIdentify userIdentify = identifyService.findByUserId(user.getUuid());
            userIdentify.setEmailStatus(Constant.FLAG_YES);
            userIdentify.setEmailVerifyTime(DateUtils.getNow());
            identifyService.update(userIdentify);
        }

        //第三方开户信息
        if (StringUtils.isNotBlank(userModel.getTppUserCustId())) { //线下开户
            manageCompanyIdentify(userModel, user);
        }
    }

    private User addUserForRegister(final UserModel model) {
        long startTime = System.currentTimeMillis();
        // 保存用户信息
        final User user = model.prototype();
        if (StringUtils.isBlank(model.getUserNo())) {
            user.setUserNo(OrderNoUtils.getSerialNumber());
        }
        user.setUserName(Constant.USER_NAME_PREFIX + user.getUserNo());
        user.setPwd(PasswordHelper.entryptPassword(user.getPwd().trim()));
        user.setCreateTime(DateUtils.getNow());
        user.setStatus(User.USER_STATUS_NORMAL);
        user.setUserNature(model.getUserNature());//冗余字段方便查询
        user.setTppStatus(Constant.FLAG_NO);
        user.setAccountType(User.ACCOUNT_TYPE_YOU);//账号类型默认柚账户
        user.setIsStock(Constant.FLAG_NO);//非存量用户
        save(user);

        // 用户附属信息
        final UserCache uc = new UserCache();
        uc.setUserId(user.getUuid());
        uc.setLoginFailTimes(0);
        uc.setIdNo(model.getIdNo());
        uc.setCertType("01");
        uc.setUserNature(model.getUserNature());
        if (StringUtils.isNotBlank(uc.getIdNo())) {
            uc.setSex(StringUtils.getSex(uc.getIdNo()));
        }
        // 设置需要修改密码标识
        if (Constant.FLAG_YES.equals(model.getRegMode())) {
            uc.setResetPwdFlag(Constant.FLAG_YES);
        } else {
            uc.setResetPwdFlag(Constant.FLAG_NO);
        }
        uc.setRegMode(model.getRegMode());
        uc.setProvince(model.getProvince());
        uc.setCity(model.getCity());
        uc.setArea(model.getArea());
        uc.setFirstAwardFlag(CommonEnum.NO.getValue());
        userCacheService.save(uc);

        // 企业信息：企业或担保机构
        if (UserCache.USER_NATURE_COMPANY.equals(uc.getUserNature()) || UserCache.USER_NATURE_VOUCH.equals(uc.getUserNature())) {
            final UserCompanyInfo companyInfo = new UserCompanyInfo();
            companyInfo.setUserId(user.getUuid());
            companyInfo.setCompanyName(user.getCompanyName());
            companyInfo.setAuditStatus(UserCompanyInfo.AUDIT_STATUS_WAIT);
            companyInfo.setTelephone(model.getTelephone());
            if (Constant.FLAG_NO.equals(model.getThreeCertificate())) {
                companyInfo.setOrgCode(user.getOrgCode());
                companyInfo.setBussinessCode(user.getBussinessCode());
            } else {
                companyInfo.setCreditCode(model.getCreditCode());
            }
            companyInfo.setThreeCertificate(model.getThreeCertificate());
            companyInfo.setLegalDelegate(model.getLegalDelegate());
            companyInfo.setCertNo(model.getCertNo());
            companyService.save(companyInfo);

            //个人信息
        } else {
            final UserBaseInfo userBaseInfo = new UserBaseInfo();
            userBaseInfo.setUserId(user.getUuid());
            if (StringUtils.isNotBlank(user.getIdNo())) {
                final String birthday = StringUtils.getBirthday(user.getIdNo());
                userBaseInfo.setBirthday(DateUtils.valueOf(birthday, "yyyyMMdd"));
            }
            baseInfoService.save(userBaseInfo);
        }

        // 用户认证信息(自动手机认证)
        final UserIdentify identify = new UserIdentify();
        identify.setUserId(user.getUuid());
        identify.setEmailStatus(Constant.FLAG_NO);
        identify.setRealNameStatus(Constant.FLAG_NO);
        identify.setMobilePhoneStatus(Constant.FLAG_YES);
        identify.setMobilePhoneVerifyTime(DateUtils.getNow());
        identifyService.save(identify);

        //用户VIP
        UserVip userVip = new UserVip(null, user, BigDecimal.ZERO);
        userVipService.save(userVip);
        // 用户统计
        StatisticUser statisticUser = new StatisticUser();
        statisticUser.setUserId(user.getUuid());
        statisticUser.setRegistDate(DateUtils.getNow());
        statisticUser.setUserNature(uc.getUserNature());
        statisticUser.setIsBorrower(UserEnum.USER_TYPE_NORMAL.getValue());
        statisticUser.setIsInvestor(UserEnum.USER_TYPE_NORMAL.getValue());
        statisticUser.setArea(IPUtil.getAddressByIP(user.getIp()));// 地区
        statisticUser.setRealNameStatus(UserEnum.REAL_NAME_STATUS_WAIT.getValue());
        statisticUser.setRegistChannel(model.getRegistChannel() != null ? model.getRegistChannel() : UserEnum.REGISTER_CHANNEL_PC.getValue());
        statisticUserService.save(statisticUser);

        // 邀请用户
        final User inviteUser = model.getInviteUser();
        if (inviteUser != null) {
            UserInvite iu = new UserInvite();
            iu.setInviteTime(DateUtils.getNow());
            iu.setInviteeUserId(user.getUuid());
            iu.setInviteUserId(inviteUser.getUuid());
            userInviteService.save(iu);
        }
        // 邀请经纪人
        final Operator inviteOperator = model.getInviteOperator();
        if (inviteOperator != null) {
            OperatorCustomer oc = new OperatorCustomer();
            oc.setCreateTime(DateUtils.getNow());
            oc.setUserId(user.getUuid());
            oc.setOperatorId(inviteOperator.getUuid());
            oc.setAddType(OperatorCustomer.ADD_TYPE_REGISTER);
            operatorCustomerService.save(oc);
        }
        //个人积分初始化
        userScoreService.initUserScore(user.getUuid());
        long userEnd = System.currentTimeMillis();
        // 资金账户专门放到账户中心处理
        AccountRegisterModel arm = new AccountRegisterModel(user.getUuid(), ConfigUtils.getValue(Constant.ACCOUNT_CODE), ConfigUtils.getValue(Constant.ACCOUNT_TYPE), null);
        accountService.register(arm);
        long accountEnd = System.currentTimeMillis();
        LOGGER.info("user register take time:{}, account register take time:{}", userEnd - startTime, accountEnd - userEnd);
        //活动方案入队列    注册的时候拆分队列
        MqActPlanModel redActModel = new MqActPlanModel(MqConstant.OPERATE_ACTPLAN_REGISTER_GIFT_REDPACKET, user, null, null);
        RabbitUtils.actPlan(redActModel);//活动大类：注册 -- 注册送好礼--送红包
        MqActPlanModel rateActModel = new MqActPlanModel(MqConstant.OPERATE_ACTPLAN_REGISTER_GIFT_RATECOUPON, user, null, null);
        RabbitUtils.actPlan(rateActModel);//活动大类：注册 -- 注册送好礼--送加息券
        MqActPlanModel friendRedActModel = new MqActPlanModel(MqConstant.OPERATE_ACTPLAN_REGISTER_FRIEND_GIFT_REDPACKET, user, null, null);
        RabbitUtils.actPlan(friendRedActModel);//活动大类：注册 -- 注册好友送好礼--送红包
        MqActPlanModel friendRateActModel = new MqActPlanModel(MqConstant.OPERATE_ACTPLAN_REGISTER_FRIEND_GIFT_RATECOUPON, user, null, null);
        RabbitUtils.actPlan(friendRateActModel);//活动大类：注册 -- 注册好友送好礼--送加息券
        MqActPlanModel scoreActModel = new MqActPlanModel(MqConstant.OPERATE_ACTPLAN_REGISTER_SCORE, user, null, null);
        RabbitUtils.actPlan(scoreActModel);//活动大类：注册 --送积分
        return user;
    }

    /**
     * 修改手机号
     */
    @Override
    @Transactional
    public void modifyPhone(final String userId, final String mobile, final String status) {
        CacheUtils.del(CacheConstant.KEY_PREFIX_USER_USER_ID.concat(userId));
        // change mobile
        dao.update(new User(userId, null, mobile, null));
        //change mobile status
        identifyService.modifyMobileStatus(userId, status, UserIdentifyModel.STATUS_MOBILE_WAIT);
    }

    /**
     * 根据userCustId查询对象
     */
    @Override
    public User findByUserCustId(final String userCustId) {
        return dao.findByUserCustId(userCustId);
    }

    /**
     * 根据Status查询对象
     */
    @Override
    public List<User> findByStatus(final String status) {
        return dao.findByStatus(status);
    }

    /**
     * 检查重复性
     */
    @Override
    public int checkRepeat(final String uuid, final String userName, final String mobile, final String email) {
        final User model = new User(uuid, userName, mobile, email);
        return dao.checkRepeat(model);
    }

    /**
     * 校对用户注册信息
     */
    @Override
    public void checkUserRegister(final UserModel model) {
        // validate
        BeanValidators.validate(model);
        model.validAgree();
        model.validRegPwdModel();

        // check mobile
        final int hasMobilePhone = checkRepeat(null, null, model.getMobile(), null);
        if (hasMobilePhone > 0) {
            throw new BussinessException(ResourceUtils.get(ResourceConstant.USER_MOBILE_REPEAT), BussinessException.TYPE_JSON);
        }
        // inviteUser
        final String inviteUserParams = model.getUi();
        if (StringUtils.isNotBlank(inviteUserParams)) {
            final User inviteUser = dao.get(inviteUserParams);
            if (inviteUser != null) {
                model.setInviteUser(inviteUser);
            } else {
                final Operator inviteOperator = operatorService.get(inviteUserParams);
                if (inviteOperator != null) {
                    model.setInviteOperator(inviteOperator);
                }
            }

        }
    }

    /**
     * 根据loginName查询对象
     */
    @Override
    public User getUserByLoginName(final String loginName) {
        //判空检查
        if (StringUtils.isBlank(loginName)) {
            return null;
        }
        User model = new User();
        //手机号码
        if (StringUtils.isPhone(loginName)) {
            model.setMobile(loginName);
        } else if (StringUtils.isEmail(loginName)) {
            //邮箱格式
            model.setEmail(loginName);
        } else {
            //默认用户名格式
            model.setUserName(loginName);
        }

        return dao.getUserByLoginName(model);
    }

    /**
     * 用户登录
     *
     * @throws Exception
     */
    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public Map<String, Object> doLogin(UserModel model) {
        int formEncryptEnable = ConfigUtils.getInt(ConfigConstant.USER_LOGIN_FORM_ENCRYPT_ENABLE);
        //对页面加密密码进行解密
        if (formEncryptEnable == Constant.INT_ONE && model.getEncrypt() == Constant.INT_ONE) {
            model.setLoginPwd(FormDecryptUtils.decrypt(model.getLoginPwd(), ConfigUtils.getValue(ConfigConstant.USER_LOGIN_FORM_ENCRYPT_PRIVATE)));
        }
        final User user = this.getUserByLoginName(model.getLoginName());
        final Map<String, Object> rtnMap = Maps.newHashMap();
        if (user != null) {
            //判断用户是否被锁定
            if (User.USER_STATUS_LOCKED.equals(user.getStatus())) { //锁定
                rtnMap.put("resultCode", LOGIN_FAIL_LOCK);
            } else { //未锁定

            	UserCache userCache = userCacheService.findByUserId(user.getUuid());
            	if (Constant.FLAG_YES.equals(user.getIsStock()) && Constant.FLAG_YES.equals(userCache.getResetPwdFlag())) {
            		if (user.getPwd().equals(MD5Utils.encode(model.getLoginPwd()))){
            			 // 判断是否需要首先修改密码
                        if (Constant.FLAG_YES.equals(userCache.getResetPwdFlag())) {
                            rtnMap.put("pwdResetFlag", true);
                        }
                        user.setIp(model.getIp());
                        user.setSessionId(model.getSessionId());
                        this.loginSucess(user);
                        
                        rtnMap.put("resultCode", LOGIN_SUCCESS);
            		} else {  //密码错误
                        this.loginFail(user);
                        addSessionErrorCount(model.getSessionId());
                        
                        rtnMap.put("resultCode", LOGIN_FAIL_PASS);
                        rtnMap.put("userId", user.getUuid());
                    }
            	} else if (PasswordHelper.validatePassword(model.getLoginPwd(), user.getPwd())) {//登陆成功
                    // 判断是否需要首先修改密码
                    if (Constant.FLAG_YES.equals(userCache.getResetPwdFlag())) {
                        rtnMap.put("pwdResetFlag", true);
                    }
                    user.setIp(model.getIp());
                    user.setSessionId(model.getSessionId());
                    this.loginSucess(user);

                    rtnMap.put("resultCode", LOGIN_SUCCESS);

                } else {  //密码错误
                    this.loginFail(user);
                    addSessionErrorCount(model.getSessionId());

                    rtnMap.put("resultCode", LOGIN_FAIL_PASS);
                    rtnMap.put("userId", user.getUuid());
                }
            }

        } else {  // 用户名错误
            rtnMap.put("resultCode", LOGIN_FAIL_NAME);
            addSessionErrorCount(model.getSessionId());
        }
        return rtnMap;
    }

    /**
     * 新增 sessionId计算登录次数 ,用来判断图形验证码是否显示
     *
     * @param sessionId
     * @author xhf
     * @date 2016年10月21日
     */
    private void addSessionErrorCount(String sessionId) {
        String loginSessionKey = CacheConstant.KEY_PREFIX_USER_LOGIN_FAIL_NUM + sessionId;
        Integer loginFailCount = CacheUtils.getInt(loginSessionKey);
        if (loginFailCount == null) {
            loginFailCount = 0;
        }
        CacheUtils.set(loginSessionKey, ++loginFailCount, ConfigUtils.getInt(Constant.LOGIN_ERROR_FALSH_TIME));
    }

    @Override
    public User checkUser() {
        final User user = (User) SessionUtils.getSessionUser(BussinessException.TYPE_CLOSE);
        final UserIdentify userIdentify = identifyService.findByUserId(user.getUuid());

        if (!UserIdentifyModel.STATUS_REALNAME_SUCC.equals(userIdentify.getRealNameStatus())) {
            throw new BussinessException(ResourceUtils.get(ResourceConstant.USER_TPP_STATUS_NOT_OPEN), Constant.USER_REAL_NAME_URL, "马上开通");
        }
        SessionUtils.setSessionAttr(Constant.SESSION_USER_IDENTIFY, userIdentify);

        return user;
    }

    @Override
    public Page<User> findBorrowerPage(User model) {
        final Page<User> page = model.getPage();
        page.setRows(dao.findBorrower(model));
        return page;
    }

    @Override
    public List<User> autoCompleteBorrower(final User model) {
        model.setQueryStyle(User.QUERY_STYLE_AUTO_COMPLETE);
        return dao.findBorrower(model);
    }

    @Override
    public List<User> findBorrowerList(final User model) {
        return dao.findBorrower(model);
    }

    @Override
    public List<AccountBankModel> getBankList() {
        //校验用户是否登录
        final User user = SessionUtils.getSessionUser();
        final UserIdentify userIdentify = identifyService.findByUserId(user.getUuid());

        //查询用户银行卡列表
        List<AccountBankModel> bankList = null;
        if (UserIdentifyModel.STATUS_REALNAME_SUCC.equals(userIdentify.getRealNameStatus())) {
            final UfxModel tppModel = new UfxModel();
            tppModel.setUserNo(user.getUserNo());
            tppModel.setTppUserCustId(user.getTppUserCustId());
            bankList = ufxService.tppQueryBank(tppModel);
        }
        return bankList;
    }

    @Override
    public Object addBank() {
        final User user = this.checkUser();
        final UserCache userCache = userCacheService.findByUserId(user.getUuid());
        //接口参数
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("userCustId", user.getTppUserCustId());
        map.put("userId", user.getUserNo());
        map.put("realName", user.getRealName());
        map.put("idNo", userCache.getIdNo());
        TppService tppService = (TppService) TppUtil.tppService();
        return tppService.tppUpdateBankCard(map);
    }

    @Override
    public Object addBank(Map<String, Object> map) {
        final User user = this.checkUser();
        final UserCache userCache = userCacheService.findByUserId(user.getUuid());
        //接口参数
        map.put("accountId", user.getTppUserCustId());
        map.put("idNo", userCache.getIdNo());
        map.put("idType", JxbankConstant.JXBANK_ID_TYPE_ONE);
        map.put("name", user.getRealName());
        map.put("mobile", user.getMobile());
        map.put("lastSrvAuthCode", SessionUtils.getSessionAttr("srvAuthCode:" + user.getMobile()));


        TppService tppService = (TppService) TppUtil.tppService();
        Object boject = tppService.tppAddBankCard(map);

        SessionUtils.removeAttribute("srvAuthCode:" + user.getMobile());
        return boject;
    }

    @Override
    public Object deleteBank(final String cardId, final String bankCode) {
        final User user = this.checkUser();
        if (StringUtils.isBlank(cardId)) {
            throw new BussinessException(ResourceUtils.get(ResourceConstant.BANK_CARDID_EMPTY), BussinessException.TYPE_CLOSE);
        }
        Account account = accountService.getByUserId(new AccountQueryModel(user.getUuid(), ConfigUtils.getValue(Constant.ACCOUNT_CODE)));
        if (account.getUseMoney().compareTo(BigDecimal.ZERO) > 0) {
            throw new BussinessException("账户余额不为0，不能解除绑定", BussinessException.TYPE_CLOSE);
        }
        
        final ProjectCollection collectionModel = new ProjectCollection();
		collectionModel.setUserId(user.getUuid());
		collectionModel.setStatus(ProjectCollectionEnum.STATUS_NOT_REPAY.getValue());
		List<ProjectCollection> pcList = projectCollectionService.findPcByPIdAndUserId(collectionModel);
		if (pcList != null && pcList.size() > 0) {
			throw new BussinessException("有未还的记录，不能解除绑定", BussinessException.TYPE_CLOSE);
		}
        

        final UserCache userCache = userCacheService.findByUserId(user.getUuid());

        //接口参数
        final Map<String, Object> map = new HashMap<String, Object>();
        map.put("accountId", user.getTppUserCustId());
        map.put("idNo", userCache.getIdNo());
        map.put("idType", JxbankConstant.JXBANK_ID_TYPE_ONE);
        map.put("name", user.getRealName());
        map.put("mobile", user.getMobile());
        map.put("cardNo", cardId);
        TppService tppService = (TppService) TppUtil.tppService();
        return tppService.tppDeleteBankBank(map);
    }

    @Override
    @Transactional
    public Boolean modifyPwd(final UserModel userModel) {
        // 坚持是否登录
        final User user = SessionUtils.getSessionUser();
        UserCache userCache = userCacheService.findByUserId(user.getUuid());
        // 检查数据有效性
        userModel.validSetNewPwd();
        // 检查原始密码是否输入正确
        if (Constant.FLAG_YES.equals(user.getIsStock()) && Constant.FLAG_YES.equals(userCache.getResetPwdFlag())) {
    		if (!user.getPwd().equals(MD5Utils.encode(userModel.getLoginPwd()))){
    			throw new BussinessException(ResourceUtils.get(ResourceConstant.OPERATOR_FIRST_OLD_PWD_ERROR), BussinessException.TYPE_JSON);
    		}
        }
        else if (!PasswordHelper.validatePassword(userModel.getLoginPwd(), user.getPwd())) {
            if (Constant.FLAG_YES.equals(userCache.getResetPwdFlag())) {
                throw new BussinessException(ResourceUtils.get(ResourceConstant.OPERATOR_FIRST_OLD_PWD_ERROR), BussinessException.TYPE_JSON);
            } else {
                throw new BussinessException(ResourceUtils.get(ResourceConstant.OPERATOR_OLD_PWD_ERROR), BussinessException.TYPE_JSON);
            }
        }
        // 新密码不能和原始密码一致
        if (Constant.FLAG_YES.equals(user.getIsStock()) && Constant.FLAG_YES.equals(userCache.getResetPwdFlag())) {
    		if (user.getPwd().equals(MD5Utils.encode(userModel.getNewLoginPwd()))){
    			throw new BussinessException(ResourceUtils.get(ResourceConstant.OPERATOR_FIRST_NEWPWD_AGAIN_ERROR), BussinessException.TYPE_JSON);
    		}
        }
        else if (PasswordHelper.validatePassword(userModel.getNewLoginPwd(), user.getPwd())) {
            if (Constant.FLAG_YES.equals(userCache.getResetPwdFlag())) {
                throw new BussinessException(ResourceUtils.get(ResourceConstant.OPERATOR_FIRST_NEWPWD_AGAIN_ERROR), BussinessException.TYPE_JSON);
            } else {
                throw new BussinessException(ResourceUtils.get(ResourceConstant.OPERATOR_NEWPWD_AGAIN_ERROR), BussinessException.TYPE_JSON);
            }
        }
        // 修改密码
        userModel.setNewLoginPwd(PasswordHelper.entryptPassword(userModel.getNewLoginPwd()));
        userModel.setUuid(user.getUuid());
        CacheUtils.del(CacheConstant.KEY_PREFIX_USER_USER_ID.concat(user.getUuid()));

        //返回结果
        boolean result = false;
        if (dao.updatePwd(userModel) > 0) {
            // 判断是否修改密码标识
            if (Constant.FLAG_YES.equals(userCache.getResetPwdFlag())) {
                userCache.setResetPwdFlag(Constant.FLAG_NO);
                userCacheService.update(userCache);
                CacheUtils.del(CacheConstant.KEY_PREFIX_USER_CACHE_USER_ID.concat(user.getUuid()));
            }
            result = true;
        }

        //更新session
        SessionUtils.setSessionAttr(Constant.SESSION_USER, dao.get(user.getUuid()));
        return result;
    }

    @Override
    @Transactional
    public void modifyEmail(final UserModel model) {
        // 检查是否登录
        final User user = SessionUtils.getSessionUser();
        final UserCache userCache = userCacheService.findByUserId(user.getUuid());
        final UserIdentify userIdentify = (UserIdentify) SessionUtils.getSessionAttr(Constant.SESSION_USER_IDENTIFY);
        // 检查是否非法操作
        final String modifyEmailSign = model.getModifyEmailSign();
        if (StringUtils.isNotBlank(user.getEmail())) { //修改手机
            if (Constant.EMAIL_VALID_PASS.equals(model.getCheckType())) { //密保问题
                if (StringUtils.isBlank(modifyEmailSign) || !modifyEmailSign.equals(MD5Utils.encode(user.getEmail() + userCache.getIdNo()))) {
                    throw new BussinessException(ResourceUtils.get(ResourceConstant.GLOBAL_MSG_ILLEGAL), BussinessException.TYPE_JSON);
                }
            } else if (Constant.EMAIL_VALID_EMAIL.equals(model.getCheckType())) { //邮箱认证
                if (StringUtils.isBlank(modifyEmailSign) || !modifyEmailSign.equals(MD5Utils.encode(user.getEmail() + MessageConstant.MESSAGE_MODIFY_EMAIL_EMAILCODE))) {
                    throw new BussinessException(ResourceUtils.get(ResourceConstant.GLOBAL_MSG_ILLEGAL), BussinessException.TYPE_JSON);
                }
            } else if (Constant.EMAIL_VALID_MOBILE.equals(model.getCheckType())) { //手机认证
                if (StringUtils.isBlank(modifyEmailSign) || !modifyEmailSign.equals(MD5Utils.encode(user.getEmail() + MessageConstant.MESSAGE_MODIFY_EMAIL_PHONECODE))) {
                    throw new BussinessException(ResourceUtils.get(ResourceConstant.GLOBAL_MSG_ILLEGAL), BussinessException.TYPE_JSON);
                }
            }
        }

        if (StringUtils.isNotBlank(model.getModifyType())) {
            if (Constant.MODIFYTYPE_BIND.equals(model.getModifyType()) && UserIdentifyModel.STATUS_EMAIL_SUCC.equals(userIdentify.getEmailStatus())) {
                throw new BussinessException(ResourceUtils.get(ResourceConstant.GLOBAL_EMAIL_STATUS_ERROR), BussinessException.TYPE_JSON);
            }
            if (Constant.MODIFYTYPE_EDIT.equals(model.getModifyType()) && user.getEmail().equals(model.getEmail())) {
                throw new BussinessException(ResourceUtils.get(ResourceConstant.EMAIL_CANNOTBE_SAME), BussinessException.TYPE_JSON);
            }
        }
        //校验绑定邮箱验证码
        model.setUuid(user.getUuid());
        model.setUserName(user.getUserName());
        model.validBindEmail();
        TokenUtils.validShiroToken(TokenConstants.TOKEN_EMAIL_BIND);//录入信息前进行token的校验
        //更新数据库
        //1、更新认证信息
        identifyService.modifyEmailStatus(user.getUuid(), UserIdentifyModel.STATUS_EMAIL_SUCC, UserIdentifyModel.STATUS_EMAIL_WAIT);
        //2、更新邮件
        dao.update(new User(user.getUuid(), null, null, model.getEmail()));
        CacheUtils.del(CacheConstant.KEY_PREFIX_USER_USER_ID.concat(user.getUuid()));

        //更新缓存
        //1、更新session_user
        user.setEmail(model.getEmail());
        SessionUtils.setSessionAttr(Constant.SESSION_USER, user);
        //2、更新SESSION_USER_IDENTIFY
        userIdentify.setEmailStatus(UserIdentifyModel.STATUS_EMAIL_SUCC);
        SessionUtils.setSessionAttr(Constant.SESSION_USER_IDENTIFY, userIdentify);
        // 邮箱绑定送积分
        userScoreService.grantScore(user, ScoreEnum.SCORE_EMAIL.getValue(), null);
    }


    @Override
    @Transactional
    public void modifyMobile(final UserModel model) {
        // 检查是否登录
        final User user = SessionUtils.getSessionUser();
        final UserCache userCache = userCacheService.findByUserId(user.getUuid());
        final UserIdentify userIdentify = (UserIdentify) SessionUtils.getSessionAttr(Constant.SESSION_USER_IDENTIFY);

        // 检查是否非法操作
        final String modifyPhoneSign = model.getModifyPhoneSign();
        if (StringUtils.isNotBlank(user.getMobile())) { //修改手机
            if (Constant.MOBILE_VALID_PASS_PERSON.equals(model.getCheckType())) { //密保问题
                if (StringUtils.isBlank(modifyPhoneSign) || !modifyPhoneSign.equals(MD5Utils.encode(user.getMobile() + userCache.getIdNo()))) {
                    throw new BussinessException(ResourceUtils.get(ResourceConstant.GLOBAL_MSG_FAIL), BussinessException.TYPE_JSON);
                }
            } else if (Constant.MOBILE_VALID_MOBILE.equals(model.getCheckType())) { //手机认证
                if (StringUtils.isBlank(modifyPhoneSign) || !modifyPhoneSign.equals(MD5Utils.encode(user.getMobile() + MessageConstant.MESSAGE_MODIFY_PHONE_PHONECODE))) {
                    throw new BussinessException(ResourceUtils.get(ResourceConstant.GLOBAL_MSG_FAIL), BussinessException.TYPE_JSON);
                }
            } else { //邮箱+身份证(个人)/密保(企业)
                if (StringUtils.isBlank(modifyPhoneSign) || !modifyPhoneSign.equals(MD5Utils.encode(user.getMobile() + MessageConstant.MESSAGE_MODIFY_PHONE_EMAILCODE))) {
                    throw new BussinessException(ResourceUtils.get(ResourceConstant.GLOBAL_MSG_FAIL), BussinessException.TYPE_JSON);
                }
            }
        }

        //校验绑定手机验证码
        model.setUuid(user.getUuid());
        model.setUserName(user.getUserName());
//		model.validBindPhone();改成江西银行接口

        TppService tppService = (TppService) TppUtil.tppService();

        final Map<String, Object> map = Maps.newHashMap();

        map.put("accountId", user.getTppUserCustId());
        map.put("mobile", model.getMobile());
        map.put("smsCode", model.getCode());
        map.put("lastSrvAuthCode", SessionUtils.getSessionAttr("srvAuthCode:" + model.getMobile()));

        tppService.tppUpdateMobileNo(map);
        SessionUtils.removeAttribute("lastSrvAuthCode:" + user.getMobile());

        //TokenUtils.validShiroToken(TokenConstants.TOKEN_MOBILE_BIND);//录入信息前进行token的校验

        //更新数据库
        //1、更新认证信息
        identifyService.modifyMobileStatus(user.getUuid(), UserIdentifyModel.STATUS_EMAIL_SUCC, UserIdentifyModel.STATUS_EMAIL_WAIT);
        //2、更新手机
        dao.update(new User(user.getUuid(), null, model.getMobile(), null));
        CacheUtils.del(CacheConstant.KEY_PREFIX_USER_USER_ID.concat(user.getUuid()));
        //更新缓存
        //1、更新session_user
        user.setMobile(model.getMobile());
        SessionUtils.setSessionAttr(Constant.SESSION_USER, user);
        //2、更新SESSION_USER_IDENTIFY
        userIdentify.setMobilePhoneStatus(UserIdentifyModel.STATUS_EMAIL_SUCC);
        SessionUtils.setSessionAttr(Constant.SESSION_USER_IDENTIFY, userIdentify);
    }

    @Override
    public Page<User> findPersonUser(UserModel model) {
        final Page<User> page = model.getPage();
        List<User> list = dao.findPersonUser(model);
        if (!org.springframework.util.CollectionUtils.isEmpty(list)) {
            List<Map<String, String>> ucList = userCacheService.getUserCacheByUserList(list);
            String[] userFreezeIds = userFreezeService.getUserFreezeByUserList(list);
            for (User user : list) {
                Iterator<Map<String, String>> its = ucList.iterator();
                while (its.hasNext()) {
                    Map<String, String> uc = its.next();
                    if (user.getUuid().equals(uc.get("user_id"))) {
                        user.setSex(uc.get("sex"));
                        user.setIdNo(uc.get("id_no"));
                        its.remove();
                    }
                }
                for (String userId : userFreezeIds) {
                    if (user.getUuid().equals(userId)) {
                        user.setStatus(User.USER_STATUS_LOCKED);
                    }
                }
            }
        }
        page.setRows(list);
        return page;
    }

    @Override
    public Page<User> findCompanyUser(final UserModel model) {
        final Page<User> page = model.getPage();
        page.setRows(dao.findCompanyUser(model));
        return page;
    }

    @Override
    public List<User> exportPersonUser(final UserModel model) {
        return dao.findPersonUser(model);
    }

    @Override
    public List<User> exportCompanyUser(final UserModel model) {
        return dao.findCompanyUser(model);
    }

    @Override
    public List<User> exportVouchUser(final UserModel model) {
        return dao.findCompanyUser(model);
    }

    @Override
    @Transactional
    public User addUserByManage(final UserModel model) {
        final int countUserName = checkRepeat(null, model.getUserName(), null, null);
        if (countUserName > 0) {
            throw new BussinessException(ResourceUtils.get(ResourceConstant.USER_NAME_REPEAT), BussinessException.TYPE_JSON);
        }
        final int countMobile = checkRepeat(null, null, model.getMobile(), null);
        if (countMobile > 0) {
            throw new BussinessException(ResourceUtils.get(ResourceConstant.USER_MOBILE_REPEAT), BussinessException.TYPE_JSON);
        }
        TokenUtils.validShiroToken(TokenConstants.TOKEN_ADD_USER_PERSON);
        //后台录入
        model.setRegMode(Constant.FLAG_YES);
        model.setUserNature(UserCache.USER_NATURE_PERSON);
        //插入数据库
        return addUserForRegister(model);
    }

    @Override
    @Transactional
    public void addCompanyByManage(UserModel model) {
        //校验密码
        model.checkPwd();
        //校验新增参数
        model.checkCompanyManage();
        //校验新增企业参数是否重复
        checkAddCompanyRepeat(model);
        //校验token
        TokenUtils.validShiroToken(TokenConstants.TOKEN_ADD_USER_COMPANY);

        //后台录入
        model.setRegMode(Constant.FLAG_YES);
        model.setUserNature(UserCache.USER_NATURE_COMPANY);
        model.setRealName(model.getCompanyName());
        //插入数据库
        final User user = addUserForRegister(model);
        if (!StringUtils.isBlank(model.getTppUserCustId())) { //企业为线下开户
            manageCompanyIdentify(model, user);
        }
    }

    /**
     * 编辑后台录入用户信息
     */
    @Override
    @Transactional
    public void editUserByManage(final UserModel model) {
        final int countUserName = checkRepeat(model.getUuid(), model.getUserName(), null, null);
        if (countUserName > 0) {
            throw new BussinessException(ResourceUtils.get(ResourceConstant.USER_NAME_REPEAT), BussinessException.TYPE_JSON);
        }
        final int countMobile = checkRepeat(model.getUuid(), null, model.getMobile(), null);
        if (countMobile > 0) {
            throw new BussinessException(ResourceUtils.get(ResourceConstant.USER_MOBILE_REPEAT), BussinessException.TYPE_JSON);
        }

        //插入数据库
        final User dbUser = dao.get(model.getUuid());
        final UserCache dbCache = userCacheService.findByUserId(dbUser.getUuid());
        if (Constant.FLAG_NO.equals(dbCache.getRegMode())) {
            throw new BussinessException(ResourceUtils.get(ResourceConstant.USER_NOT_CREATE_BY_MANAGE), BussinessException.TYPE_JSON);
        }
        final User user = model.prototype();
        CacheUtils.del(CacheConstant.KEY_PREFIX_USER_USER_ID.concat(user.getUuid()));
        if (Constant.FLAG_INT_NO == dao.update(user)) {
            throw new BussinessException(ResourceUtils.get(ResourceConstant.GLOBAL_MSG_FAIL), BussinessException.TYPE_JSON);
        }
        TokenUtils.validShiroToken(TokenConstants.TOKEN_EDIT_USER_PERSON);
    }

    /**
     * 编辑后台录入企业信息
     */
    @Override
    @Transactional
    public void editCompanyByManage(final UserModel model) {
        final User dbUser = dao.get(model.getUuid());
        //商户已开户
        if (StringUtils.isNotBlank(dbUser.getTppUserCustId())) {
            return;
        }
        //校验新增参数
        model.checkCompanyManage();
        //校验新增企业参数是否重复
        checkAddCompanyRepeat(model);
        //非后台添加用户不可编辑
        final UserCache dbCache = userCacheService.findByUserId(dbUser.getUuid());
        if (Constant.FLAG_NO.equals(dbCache.getRegMode())) {
            throw new BussinessException(ResourceUtils.get(ResourceConstant.USER_NOT_CREATE_BY_MANAGE), BussinessException.TYPE_JSON);
        }
        //校验token
        TokenUtils.validShiroToken(TokenConstants.TOKEN_EDIT_USER_COMPANY);

        //更新用户信息
        final User user = model.prototype();
        if(StringUtils.isNotBlank(model.getCompanyName())){
        	user.setRealName(model.getCompanyName());
        }
        this.update(user);

        //更新企业信息
        final UserCompanyInfo dbUserCompanyInfo = companyService.findByUserId(model.getUuid());
        final UserCompanyInfo userCompanyInfo = new UserCompanyInfo();
        userCompanyInfo.setUuid(dbUserCompanyInfo.getUuid());
        userCompanyInfo.setCompanyName(model.getCompanyName());
        userCompanyInfo.setThreeCertificate(model.getThreeCertificate());
        userCompanyInfo.setLegalDelegate(model.getLegalDelegate());
        userCompanyInfo.setCertNo(model.getCertNo());
        if (Constant.FLAG_NO.equals(model.getThreeCertificate())) {
            userCompanyInfo.setOrgCode(model.getOrgCode());
            userCompanyInfo.setBussinessCode(model.getBussinessCode());
            userCompanyInfo.setCreditCode("");
        } else {
            userCompanyInfo.setOrgCode("");
            userCompanyInfo.setBussinessCode("");
            userCompanyInfo.setCreditCode(model.getCreditCode());
        }
        companyService.update(userCompanyInfo);

        //商户线下已开户
        if (!StringUtils.isBlank(model.getTppUserCustId())) {
            manageCompanyIdentify(model, dbUser);
        }
    }

    /**
     * 登陆成功后续处理
     *
     * @param
     * @return void
     * @author xxb
     * @date 2016年8月8日
     */
    @Override
    public void loginSucess(final User user) {
        final UserCache userCache = userCacheService.findByUserId(user.getUuid());
        SessionUtils.setSessionAttr(Constant.SESSION_USER, user);
        SessionUtils.setSessionAttr(Constant.SESSION_USER_IDENTIFY, identifyService.findByUserId(user.getUuid()));
        SessionUtils.setSessionAttr(Constant.SESSION_USER_CACHE, userCache);
        SessionUtils.setSessionAttr(Constant.LOGIN_TIME, System.currentTimeMillis());
        // 存储企业logo
        if (UserCache.USER_NATURE_VOUCH.equals(userCache.getUserNature())) {
            final UserCompanyInfo userCompanyInfo = userCompanyInfoService.findByUserId(user.getUuid());
            if (!ObjectUtils.isEmpty(userCompanyInfo) && StringUtils.isNotBlank(userCompanyInfo.getLogo())) {
                SessionUtils.setSessionAttr(Constant.SESSION_COMPANY_LOGO_PATH, userCompanyInfo.getLogo());
            }
        }

        // 登录日志
        final UserLoginLog loginLog = new UserLoginLog();
        loginLog.setUserId(user.getUuid());
        loginLog.setLoginTime(DateUtils.getNow());
        loginLog.setLoginIp(user.getIp());
        loginLogService.save(loginLog);

        //重置登录次数
        CacheUtils.del(CacheConstant.KEY_PREFIX_USER_LOGIN_FAIL_NUM + user.getUuid());
        if (StringUtils.isNotBlank(user.getSessionId())) {
            CacheUtils.del(CacheConstant.KEY_PREFIX_USER_LOGIN_FAIL_NUM + user.getSessionId());
        }

        // 登陆送积分
        MqActPlanModel loginActModel = new MqActPlanModel(MqConstant.OPERATE_ACTPLAN_LOGIN, user, null, null);
        RabbitUtils.actPlan(loginActModel);
    }


    /**
     * 登录失败后续处理
     *
     * @param
     * @return void
     * @author xxb
     * @date 2016年8月8日
     */
    @Override
    public int loginFail(User user) {
        //1、记录错误次数；
        String loginUserIdKey = CacheConstant.KEY_PREFIX_USER_LOGIN_FAIL_NUM + user.getUuid();
        Integer loginFailCount = CacheUtils.getInt(loginUserIdKey);
        if (loginFailCount == null) {
            loginFailCount = 0;
        }
        CacheUtils.set(loginUserIdKey, ++loginFailCount, ConfigUtils.getInt(Constant.LOGIN_ERROR_FALSH_TIME));

        //2、如果登录次数超过锁定次数，则锁定用户
        final int loginLockNum = ConfigUtils.getInt(Constant.LOGIN_LOCK_NUMBER);
        if (loginLockNum != 0 && loginFailCount >= loginLockNum) {
            //更新账户锁定状态
            final User model = new User();
            model.setUuid(user.getUuid());
            model.setStatus(User.USER_STATUS_LOCKED);
            dao.updateStatus(model);
            //记录锁定时间
            UserCache userCache = userCacheService.findByUserId(user.getUuid());
            userCache.setLockType(Constant.LOCK_TYPE_LOGIN_FAIL);
            userCache.setLockTime(DateUtils.getNow());
            userCache.setLockRemark(ResourceUtils.get(ResourceConstant.ACCOUNT_IS_LOCKED));
            userCacheService.updateLockRemark(userCache);
            //清除redis缓存
            CacheUtils.del(CacheConstant.KEY_PREFIX_USER_USER_ID.concat(model.getUuid()));
        }

        return loginFailCount;
    }

    /**
     * 获得失败登录次数
     */
    @Override
    public int getLoginFailCount(final String loginName) {
        int loginFailCount = 0;
        final User user = this.getUserByLoginName(loginName);
        if (user != null) {
            final Integer temp = CacheUtils.getInt(user.getUuid());
            if (temp != null) {
                loginFailCount = temp;
            }

        }
        return loginFailCount;
    }

    /**
     * 校验登录图形验证码
     */
    @Override
    public void checkLoginCaptcha(UserModel model) {
        final User user = this.getUserByLoginName(model.getLoginName());
        if (user != null) {
            final Integer loginFailCount = CacheUtils.getInt(user.getUuid());
            if (loginFailCount != null && loginFailCount >= Constant.USER_LOGIN_SHOW_CAPTCHA_TIMES) {
                model.validRegRule();
            }
        }

    }

    /**
     * 修改担保用户
     */
    @Override
    public void editVouchUser(final UserModel model) {
        //校验参数
        model.checkVouchManage();
        //校验新增企业参数是否重复
        final User dbUser = dao.get(model.getUuid());
        if (StringUtils.isBlank(dbUser.getTppUserCustId())) {
            checkAddCompanyRepeat(model);
        }
        //校验TOKEN
        TokenUtils.validShiroToken(TokenConstants.TOKEN_EDIT_USER_VOUCH);

        //用户信息
        final User user = model.prototype();
        if(StringUtils.isNotBlank(model.getCompanyName())){
        	user.setRealName(model.getCompanyName());
        }
        this.update(user);

        // 邮箱认证
        if (StringUtils.isBlank(dbUser.getEmail()) && StringUtils.isNotBlank(model.getEmail())) {
            final UserIdentify userIdentify = identifyService.findByUserId(dbUser.getUuid());
            userIdentify.setEmailStatus(Constant.FLAG_YES);
            userIdentify.setEmailVerifyTime(DateUtils.getNow());
            identifyService.update(userIdentify);
        }

        //企业信息
        final UserCompanyInfo dbUserCompanyInfo = companyService.findByUserId(model.getUuid());
        final UserCompanyInfo userCompanyInfo = new UserCompanyInfo();
        userCompanyInfo.setUuid(dbUserCompanyInfo.getUuid());
        userCompanyInfo.setContacts(model.getContacts());
        userCompanyInfo.setEmail(model.getEmail());
        userCompanyInfo.setLogo(StringUtils.isNull(model.getLogo()));
        userCompanyInfo.setTelephone(model.getTelephone());
        userCompanyInfo.setAddress(model.getAddress());
        userCompanyInfo.setTaxRegNo(model.getTaxRegNo());

        //新增的时候未添加第三方商户号
        if (StringUtils.isBlank(dbUser.getTppUserCustId())) {
            //企业信息
            userCompanyInfo.setCompanyName(model.getCompanyName());
            userCompanyInfo.setThreeCertificate(model.getThreeCertificate());
            userCompanyInfo.setLegalDelegate(model.getLegalDelegate());
            userCompanyInfo.setCertNo(model.getCertNo());
            if (Constant.FLAG_NO.equals(model.getThreeCertificate())) {
                userCompanyInfo.setOrgCode(model.getOrgCode());
                userCompanyInfo.setBussinessCode(model.getBussinessCode());
                userCompanyInfo.setCreditCode("");
            } else {
                userCompanyInfo.setOrgCode("");
                userCompanyInfo.setBussinessCode("");
                userCompanyInfo.setCreditCode(model.getCreditCode());
            }

            //创建账户签章
            if (!StringUtils.isBlank(model.getTppUserCustId())) {
                manageCompanyIdentify(model, dbUser);
            }
        }
        companyService.update(userCompanyInfo);

        //附属信息
        UserCache userCache = userCacheService.findByUserId(model.getUuid());
        userCache.setProvince(model.getProvince());
        userCache.setCity(model.getCity());
        userCache.setArea(model.getArea());
        userCacheService.update(userCache);
    }

    /**
     * 查询用户信息
     *
     * @param model
     * @return
     * @author xhf
     * @date 2016年9月18日
     */
    @Override
    public Page<User> findUserAccount(UserModel model) {
        List<User> userList = dao.findAccountUser(model);
        for (User user : userList) {
            BigDecimal total = BigDecimal.ZERO;
            BigDecimal useMoney = BigDecimal.ZERO;
            BigDecimal noUseMoney = BigDecimal.ZERO;
            BigDecimal cashFeeMark = BigDecimal.ZERO;
            int advanceCount = 0;
            if (StringUtils.isNotBlank(user.getTppStatus())) {
                //账户中心表独立于业务表，此处采用接口方式来组装数据，而不能直接采用表关联
                Account account = accountService.getByUserId(new AccountQueryModel(user.getUuid(), ConfigUtils.getValue(Constant.ACCOUNT_CODE)));
                if (account != null) {
                    total = account.getTotal();
                    useMoney = account.getUseMoney();
                    noUseMoney = account.getNoUseMoney();
                    cashFeeMark = account.getCashFeeMark();
                }
                advanceCount = CacheUtils.getInt(String.format(Cash.KEY_ADVANCE_CASH_FEE_MONTH_COUNT, DateUtils.getMonth(), user.getUuid()));
            }
            user.setTotal(total);
            user.setUseMoney(useMoney);
            user.setNoUseMoney(noUseMoney);
            user.setCashFeeMark(cashFeeMark);
            user.setAdvanceCount(advanceCount);
        }
        final Page<User> page = model.getPage();
        page.setRows(userList);
        return page;
    }

    @Override
    public int getAccountUserCount(UserModel model) {
        return dao.getAccountUserCount(model);
    }

    @Override
    public Page<User> findUserForRed(UserModel model) {
        final Page<User> page = model.getPage();
        page.setRows(dao.findUserForRed(model));
        return page;
    }

    @Override
    public void scannerAccountCompareError() {
        //1、清除对账表中的数据
        accountCheckService.deleteAccountCheck();

        //2、查询出昨日17点30 ~ 今日17点30 的所有有资金操作的用户
        final AccountLog model = new AccountLog();
        model.setTppStatus(CommonEnum.YES.getValue());//开户成功的
        model.setEndTime(DateUtils.formatDate(DateUtils.getNow(), DateUtils.DATEFORMAT_STR_001));
        model.setStartTime(DateUtils.formatDate(DateUtils.rollDay(DateUtils.getNow(), Constant.INT_ONE_NEGA), DateUtils.DATEFORMAT_STR_001));
        final List<String> logList = accountLogService.findListForScanner(model);
        //3、循环对开户的用户资金进行对比查询处理
        if (CollectionUtils.isNotEmpty(logList)) {
            if (ConfigUtils.isOpenMq()) {
                RabbitUtils.scannerAccountCompareError(logList);
            } else {
                this.handleLogList(logList);
            }
        }
    }

    /**
     * 对有操作用户的记录进行循环处理
     *
     * @param logList
     * @author QianPengZhan
     * @date 2016年11月3日
     */
    @Override
    public void handleLogList(final List<String> logList) {
        LOGGER.info("循环对开户的用户资金进行对比查询处理");
        StringBuilder userNameString = new StringBuilder();
        userNameString.append("[");
        for (int i = Constant.INT_ZERO; i < logList.size(); i++) {
            final User user = dao.get(logList.get(i));
            final Account account = accountService.getMoneyByUserId(new AccountQueryModel(user.getUuid(), ConfigUtils.getValue(Constant.ACCOUNT_CODE)));
            if (StringUtils.isBlank(user.getTppUserCustId())
                    || StringUtils.isBlank(user.getTppUserAccId())
                    || account == null) {
                continue;
            }
            LOGGER.info("scannerAccountCompareError()  本地和第三方资金的对比   循环放入队列   用户uuid{}，用户名：{}........", user.getUuid(), user.getUserName());
            boolean lastFalg = false;
            if (i == logList.size() - 1) {
                lastFalg = true;
            }
            userNameString = userCompareAccount(user, userNameString, lastFalg);
        }
        userNameString.append("]");
        LOGGER.info("---------{}的资金正在对比,END----------", userNameString.toString());
        //短信提醒运维人员    资金异常
        Map<String, Object> params = Maps.newHashMap();
        params.put("operUserName", ConfigUtils.getValue(ConfigConstant.ACCOUNT_WARN_CONTACTS));
        params.put("errorUserName", userNameString.toString());
        params.put("web_name", ConfigUtils.getValue(ConfigConstant.WEB_NAME));
        AccountCheckWarnMsg message = new AccountCheckWarnMsg(MessageConstant.ACCOUNT_COMPARE_WARN, params);
        message.doEvent();
    }

    /**
     * 处理每一笔的不平衡资金对账
     *
     * @param userModel
     * @param userNameString
     * @return
     * @author QianPengZhan
     * @date 2016年11月3日
     */
    private StringBuilder userCompareAccount(final User userModel, final StringBuilder userNameString, final boolean lastFalg) {
        LOGGER.info("---------{}的资金正在对比BEGIN----------", userModel.getUuid());

        //1、去第三方查询第三方的资金
        final Map<String, Object> ufxMap = new HashMap<String, Object>();
        ufxMap.put("userCustId", userModel.getTppUserCustId());
        final UserCache userCache = userCacheService.findByUserId(userModel.getUuid());
        if (UserCache.USER_NATURE_PERSON.equals(userCache.getUserNature())) {
            ufxMap.put("userType", UfxQueryBalanceModel.USER_TYPE_PERSONAL);
        } else {
            ufxMap.put("userType", UfxQueryBalanceModel.USER_TYPE_COMPANY);
        }
        TppService tppService = (TppService) TppUtil.tppService();
        final UfxQueryBalanceModel queryBalanceModel = (UfxQueryBalanceModel) tppService.tppQueryBalance(ufxMap);
        // 第三方可用余额
        final String payUseMoney = (String) queryBalanceModel.getAvlBal().replaceAll(",", "");
        // 第三方冻结金额
        final String payNoUseMoney = (String) queryBalanceModel.getFrzBal().replaceAll(",", "");

        //2、得到本地的资金
        final Account account = accountService.getMoneyByUserId(new AccountQueryModel(userModel.getUuid(), ConfigUtils.getValue(Constant.ACCOUNT_CODE)));
        final BigDecimal useMoney = BigDecimalUtils.defaultIfNull(account.getUseMoney());
        final BigDecimal noUseMoney = BigDecimalUtils.defaultIfNull(account.getNoUseMoney());
        //计算可用金额差额
        final BigDecimal diffUserMoney = useMoney.subtract(BigDecimalUtils.valueOf(payUseMoney));
        final BigDecimal diffNoUserMoney = noUseMoney.subtract(BigDecimalUtils.valueOf(payNoUseMoney));

        //3、录入对账表
        final AccountCheck accountCheck = new AccountCheck();
        accountCheck.setTppUserCustId(userModel.getTppUserCustId());
        accountCheck.setUserId(userModel.getUuid());
        if (StringUtils.isNotBlank(payUseMoney)) {
            accountCheck.setTppUseMoney(new BigDecimal(payUseMoney));
        }
        if (StringUtils.isNotBlank(payNoUseMoney)) {
            accountCheck.setTppNoUseMoney(new BigDecimal(payNoUseMoney));
        }
        accountCheck.setUseMoney(useMoney);
        accountCheck.setNoUseMoney(noUseMoney);
        accountCheck.setUseMoneyDiff(diffUserMoney);
        accountCheck.setNoUseMoneyDiff(diffNoUserMoney);

        //4、对比本地资金和第三方资金  如果有异常 存入对账记录表  然后发送短信给运维人员
        if (diffUserMoney.compareTo(BigDecimal.ZERO) != 0 || diffNoUserMoney.compareTo(BigDecimal.ZERO) != 0) {
            LOGGER.info("---------{}的资金正在对比发现不平衡----------", userModel.getUuid());
            accountCheckService.insert(accountCheck);
            if (lastFalg) {
                userNameString.append(userModel.getUserName());
            } else {
                userNameString.append(userModel.getUserName()).append(",");
            }
        }
        return userNameString;
    }

    @Transactional(readOnly = false)
    @Override
    public void retrievePassword(String pathWay, String password) {
        User user = this.getUserByLoginName(pathWay);
        if (user == null) {
            throw new BussinessException(ResourceUtils.get(ResourceConstant.USER_NOT_EXIST), BussinessException.TYPE_JSON);
        }
        UserCache userCache = userCacheService.findByUserId(user.getUuid());
        if (Constant.FLAG_YES.equals(user.getIsStock()) && Constant.FLAG_YES.equals(userCache.getResetPwdFlag())) {
    		if (user.getPwd().equals(MD5Utils.encode(password))){
    			 throw new BussinessException(ResourceUtils.get(ResourceConstant.RETRIEVE_PASS_NOT_CHANGE), BussinessException.TYPE_JSON);
    		}
        }
        else if (PasswordHelper.validatePassword(password, user.getPwd())) {
            throw new BussinessException(ResourceUtils.get(ResourceConstant.RETRIEVE_PASS_NOT_CHANGE), BussinessException.TYPE_JSON);
        }
        user.setPwd(PasswordHelper.entryptPassword(password));  //该逻辑与用户注册保持一致
        this.update(user);
        if (Constant.FLAG_YES.equals(userCache.getResetPwdFlag())) {
            userCache.setResetPwdFlag(Constant.FLAG_NO);
            userCacheService.update(userCache);
            CacheUtils.del(CacheConstant.KEY_PREFIX_USER_CACHE_USER_ID.concat(user.getUuid()));
        }
        //更新session
//        SessionUtils.setSessionAttr(Constant.SESSION_USER, dao.get(user.getUuid()));
    }

    /**
     * 校验新增企业参数是否重复
     *
     * @author xhf
     * @date 2016年11月3日
     */
    private void checkAddCompanyRepeat(UserModel model) {
        //用户名是否重复
        final int countUserName = checkRepeat(model.getUuid(), model.getUserName(), null, null);
        if (countUserName > 0) {
            throw new BussinessException(ResourceUtils.get(ResourceConstant.USER_NAME_REPEAT), BussinessException.TYPE_JSON);
        }
        //手机号是否重复
        final int countMobile = checkRepeat(model.getUuid(), null, model.getMobile(), null);
        if (countMobile > 0) {
            throw new BussinessException(ResourceUtils.get(ResourceConstant.USER_MOBILE_REPEAT), BussinessException.TYPE_JSON);
        }
        //邮箱是否重复
        if (StringUtils.isNotBlank(model.getEmail())) {
            final int countEmail = checkRepeat(model.getUuid(), null, null, model.getEmail());
            if (countEmail > 0) {
                throw new BussinessException(ResourceUtils.get(ResourceConstant.EMAIL_IS_USED), BussinessException.TYPE_JSON);
            }
        }

        //第三方商户号
        if (StringUtils.isNotBlank(model.getTppUserCustId())) {
            //校验商户号是否重复
            User user = new User();
            user.setUuid(model.getUuid());
            user.setTppUserCustId(model.getTppUserCustId());
            int countTppCustId = dao.checkRepeat(user);
            if (countTppCustId > 0) {
                throw new BussinessException(ResourceUtils.get(ResourceConstant.TPP_USER_CUST_ID_REPEAT), BussinessException.TYPE_JSON);
            }
            //校验组织机构代码、社会信用代码或者信用代码是否存在（开户失败不算）
            UserCacheModel userCacheModel = new UserCacheModel();
            userCacheModel.setThreeCertificate(model.getThreeCertificate());
            userCacheModel.setOrgCode(model.getOrgCode());
            userCacheModel.setBussinessCode(model.getBussinessCode());
            userCacheModel.setCreditCode(model.getCreditCode());
            userCacheModel.checkRegCode(model.getUuid());
        }
    }

    /**
     * 后台添加的线下企业用户开通第三方账户
     *
     * @param model
     * @param user
     * @author xhf
     * @date 2016年11月4日
     */
    private void manageCompanyIdentify(UserModel model, User user) {
        //用户
        user.setTppStatus(CommonEnum.YES.getValue());//企业实名
        user.setRealName(model.getCompanyName());
        user.setTppUserAccId(model.getTppUserAccId());
        user.setTppUserCustId(model.getTppUserCustId());
        this.update(user);
        //认证信息
        final UserIdentify userIdentify = identifyService.findByUserId(user.getUuid());
        userIdentify.setRealNameStatus(Constant.FLAG_YES);
        userIdentify.setRealNameVerifyTime(DateUtils.getNow());
        identifyService.update(userIdentify);
        //企业信息
        final UserCompanyInfo userCompanyInfo = companyService.findByUserId(user.getUuid());
        userCompanyInfo.setAuditStatus(UserCompanyInfo.AUDIT_STATUS_SUCCESS);
        userCompanyInfo.setAuditTime(DateUtils.getNow());
        companyService.update(userCompanyInfo);
        //协议注册签名
        boolean createSign = userCacheService.createCompanySign(userCompanyInfo, user);
        if (!createSign) {
            LOGGER.info("创建E签宝企业账户失败，userId:{}", user.getUuid());
        }
    }

    @Override
    public User getByInvitee(String userId) {
        return dao.getByInvitee(userId);
    }

    @Override
    public int getCompanyUserCount(UserModel model) {
        return dao.getCompanyUserCount(model);
    }

    @Override
    public int getPersonUserCount(UserModel model) {
        return dao.getPersonUserCount(model);
    }

    @Override
    public String getInviteUserMobile(String ui) {
        if (StringUtils.isNotBlank(ui)) {
            final User inviteUser = dao.get(ui);
            if (inviteUser != null) {
                return inviteUser.getMobile();
            } else {
                final Operator inviteOperator = operatorService.get(ui);
                if (inviteOperator != null) {
                    return inviteOperator.getMobile();
                }
            }

        }
        return null;
    }

    /**
     * 自动解冻用户
     */
    @Override
    public void unFreezeAccountByTimer() {
        final long accountLockTime = ConfigUtils.getInt(Constant.ACCOUNT_LOCK_TIME) * 1000;
        List<User> userList = dao.findByStatus(User.USER_STATUS_LOCKED);
        for (User user : userList) {
            unFreezeItemAccountByTimer(user, accountLockTime);
        }
    }

    /**
     * 自动解冻用户-单项操作
     *
     * @param user
     * @param accountLockTime
     */
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    private void unFreezeItemAccountByTimer(User user, final long accountLockTime) {
        final long now = DateUtils.getNow().getTime();
        UserCache userCache = userCacheService.findByUserId(user.getUuid());
        if (userCache != null) {
            if (!Constant.LOCK_TYPE_MAN_HANDLE.equals(userCache.getLockType())) { // 非后台人工锁定
                if (userCache.getLockTime() != null && (now - userCache.getLockTime().getTime()) >= accountLockTime) {
                    // 重置失败登录次数
                    CacheUtils.set(CacheConstant.KEY_PREFIX_USER_LOGIN_FAIL_NUM + user.getUuid(), 0,
                            ConfigUtils.getInt(Constant.LOGIN_ERROR_FALSH_TIME));
                    // 更新用户锁定状态
                    user.setStatus(User.USER_STATUS_NORMAL);
                    this.update(user);
                    // 更新用户锁定信息
                    userCache.setLockType(null);
                    userCache.setLockTime(null);
                    userCache.setLockRemark(null);
                    userCacheService.updateLockRemark(userCache);
                }
            }
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public Map<String, Object> checkExcelUser(File excel) {
        Map<String, Object> map = Maps.newHashMap();

        StringBuffer errorMobile = new StringBuffer(""); // 平台不存在的手机号
        List<String> userIds = new ArrayList<String>();
        try {
            List[] userInfoSet = ExcelUtil.readExcelUserInfo(excel);
            for (List<Object> mobileList : userInfoSet) {
                if (mobileList != null && mobileList.size() > Constant.INT_ZERO) {
                    String mobile = mobileList.get(0).toString();
                    if (mobile != null && StringUtils.isNotBlank(mobile.toString())) {
                        if (!ValidateUtils.isPhone(mobile.toString())) {
                            errorMobile.append(mobile).append("、");
                            continue;
                        }
                        User user = dao.getUserByMobile(mobile.toString());
                        if (user == null) {
                            errorMobile.append(mobile).append("、");
                        } else {
                            userIds.add(user.getUuid());
                        }
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw new BussinessException(e.getMessage());
        }
        map.put("result", true);
        map.put("userIds", userIds);
        if (errorMobile.length() > 0) {
            errorMobile.deleteCharAt(errorMobile.length() - 1);
            map.put("errorMobile", errorMobile.toString());
            map.put("result", false);
        }
        return map;
    }
    
    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public Map<String, Object> checkExcelUserScore(File excel) {
        Map<String, Object> map = Maps.newHashMap();

        StringBuffer errorMobile = new StringBuffer(""); // 平台不存在的手机号
        StringBuffer errorScore = new StringBuffer(""); // 错误格式的积分
        List<String> userIds = new ArrayList<String>();
        List<String> scores = new ArrayList<String>();
        try {
            List[] userInfoSet = ExcelUtil.readExcelUserInfo2(excel);
            for (List<Object> mobileList : userInfoSet) {
                if (mobileList != null && mobileList.size() > Constant.INT_ZERO) {
                    String mobile = mobileList.get(0).toString();
                    LOGGER.info("mobileList:{}",mobileList.size());
                    if (mobile != null && StringUtils.isNotBlank(mobile.toString())) {
                        if (!ValidateUtils.isPhone(mobile.toString())) {
                            errorMobile.append(mobile).append("、");
                            continue;
                        }
                        User user = dao.getUserByMobile(mobile.toString());
                        if (user == null) {
                            errorMobile.append(mobile).append("、");
                        } else {
                            userIds.add(user.getUuid());
                        }
                    }
                    String score = mobileList.get(1).toString() ;
                    if(score != null && StringUtils.isNotBlank(score.toString())) {
                    	if (!ValidateUtils.isInteger(score.toString())) {
                            errorScore.append(score).append("、");
                            continue;
                        }else {
                        	scores.add(score) ;
                        }
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw new BussinessException(e.getMessage());
        }
        map.put("result", true);
        map.put("userIds", userIds);
        map.put("scores", scores) ;
        if (errorMobile.length() > 0  || errorScore.length() > 0) {
            errorMobile.deleteCharAt(errorMobile.length() - 1);
            errorScore.deleteCharAt(errorScore.length() - 1) ;
            map.put("errorMobile", errorMobile.toString());
            map.put("result", false);
        }
        return map;
    }

    @Override
    public BigDecimal getValidMoneyByUser(final User user, final UserCache userCache) {
        BigDecimal result = BigDecimal.ZERO;
        final String tppName = ConfigUtils.getTppName();
        if (TppServiceEnum.CHINAPNR.getName().equals(tppName)) {
            final Map<String, Object> ufxMap = new HashMap<>();
            ufxMap.put("userCustId", user.getTppUserCustId());
            ufxMap.put("userType", userCache.getUserNature());
            TppService tppService = (TppService) TppUtil.tppService();
            final UfxQueryBalanceModel queryBalanceModel = (UfxQueryBalanceModel) tppService.tppQueryBalance(ufxMap);
            result = StringUtils.toBigDecimal(StringUtils.isNull(queryBalanceModel.getAcctBal()));
        } else if (TppServiceEnum.CBHB.getName().equals(tppName)) {
            final Map<String, Object> cbhbMap = new HashMap<>();
            cbhbMap.put("plaCustId", user.getTppUserCustId());
            TppService tppCbhbService = (TppService) TppUtil.tppService();
            @SuppressWarnings("unchecked")
            Map<String, Object> map = (HashMap<String, Object>) tppCbhbService.tppQueryBalance(cbhbMap);
            result = BigDecimalUtils.valueOf(StringUtils.isNull(map.get("acctBal")));
        } else if (TppServiceEnum.JXBANK.getName().equals(tppName)) {
            final Map<String, Object> jxMap = new HashMap<>();
            jxMap.put("accountId", user.getTppUserCustId());
            TppService tppService = (TppService) TppUtil.tppService();
            @SuppressWarnings("unchecked")
            Map<String, Object> map = (HashMap<String, Object>) tppService.tppQueryBalance(jxMap);
            result = BigDecimalUtils.valueOf(StringUtils.isNull(map.get("avlBal")));
        }
        return result;
    }

    @Override
    public List<User> findUserByIds(String[] userIds) {
        return dao.findUserByIds(userIds);
    }

    @Override
    public List<User> findByUserName(User model) {
        return dao.findByUserName(model);
    }

    @Override
    public int getCountByUserName(String keywords) {
        return dao.getCountByUserName(keywords);
    }

    @Override
    public User getUserByMobile(String mobileNo) {
        return dao.getUserByMobile(mobileNo);
    }

    @Override
    public Object modifyPayPass(User user) {
        user = dao.get(user.getUuid());
        if ("1".equals(user.getPayPwd())) {
        	throw new BussinessException("你已设置过密码，请退出后刷新查看!", BussinessException.TYPE_CLOSE);
        }
        if(passwordSetQuery(user.getTppUserCustId())){
        	User qUser = get(user.getUuid());
        	qUser.setPayPwd("1");//已设置密码
            update(qUser);
            SessionUtils.setSessionAttr(Constant.SESSION_USER, qUser);
        	throw new BussinessException("你已设置过密码，请退出后刷新查看!", BussinessException.TYPE_CLOSE);
        }
        UserCache userCache = userCacheService.findByUserId(user.getUuid());
        TppService tppService = (TppService) TppUtil.tppService();
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("accountId", user.getTppUserCustId());
        params.put("mobile", user.getMobile());
        params.put("payPwd", "0");
        params.put("name", user.getRealName());
        if(UserCache.USER_NATURE_PERSON.equals(user.getUserNature())){
        	//个人用户
        	params.put("idType", JxbankConstant.JXBANK_ID_TYPE_ONE);
        	params.put("idNo", userCache.getIdNo());
        }else{
        	//非个人用户
        	if(StringUtils.isBlank(user.getRealName())){
        		throw new BussinessException("请先联系客服人员设置企业名称！", BussinessException.TYPE_CLOSE);
        	}
        	final UserCompanyInfo companyInfo = userCompanyInfoService.findByUserId(user.getUuid());
        	if(StringUtils.isNotBlank(companyInfo.getCreditCode())){
        		params.put("idType", JxbankConstant.JXBANK_ID_TYPE_FIVE);
            	params.put("idNo", companyInfo.getCreditCode());
        	}else if(StringUtils.isNotBlank(companyInfo.getOrgCode())){
        		params.put("idType", JxbankConstant.JXBANK_ID_TYPE_TWO);
            	params.put("idNo", companyInfo.getOrgCode());
        	}else{
        		throw new BussinessException("请先联系客服人员设置企业社会信用代码或者组织机构代码！", BussinessException.TYPE_CLOSE);
        	}
        }
        
        return tppService.tppUpdatePayPass(params);
    }


    @Override
    public Object resetPwd(User user, final String smsCode) {
        user = dao.get(user.getUuid());
        if (!"1".equals(user.getPayPwd())) {
        	throw new BussinessException("你未设置过密码，请退出后刷新查看", BussinessException.TYPE_CLOSE);
        }
        UserCache userCache = userCacheService.findByUserId(user.getUuid());
        TppService tppService = (TppService) TppUtil.tppService();
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("accountId", user.getTppUserCustId());
        params.put("name", user.getRealName());
        params.put("mobile", user.getMobile());
        params.put("payPwd", "1");
        params.put("lastSrvAuthCode", SessionUtils.getSessionAttr("srvAuthCode:" + user.getMobile()));
        params.put("smsCode", smsCode);
        if(UserCache.USER_NATURE_PERSON.equals(user.getUserNature())){
        	//个人用户
        	params.put("idType", JxbankConstant.JXBANK_ID_TYPE_ONE);
        	params.put("idNo", userCache.getIdNo());
        }else{
        	//非个人用户
        	if(StringUtils.isBlank(user.getRealName())){
        		throw new BussinessException("请先联系客服人员设置企业名称！", BussinessException.TYPE_CLOSE);
        	}
        	final UserCompanyInfo companyInfo = userCompanyInfoService.findByUserId(user.getUuid());
        	if(StringUtils.isNotBlank(companyInfo.getCreditCode())){
        		params.put("idType", JxbankConstant.JXBANK_ID_TYPE_FIVE);
            	params.put("idNo", companyInfo.getCreditCode());
        	}else if(StringUtils.isNotBlank(companyInfo.getOrgCode())){
        		params.put("idType", JxbankConstant.JXBANK_ID_TYPE_TWO);
            	params.put("idNo", companyInfo.getOrgCode());
        	}else{
        		//测试阶段可以用个人的身份证号，方便测试
        		if(StringUtils.isNotBlank(userCache.getIdNo())){
        			params.put("idType", JxbankConstant.JXBANK_ID_TYPE_ONE);
                	params.put("idNo", userCache.getIdNo());
        		}else{
        			throw new BussinessException("请先联系客服人员设置企业社会信用代码或者组织机构代码！", BussinessException.TYPE_CLOSE);
        		}
        	}
        }
        Object object = tppService.tppUpdatePayPass(params);
        SessionUtils.removeAttribute("srvAuthCode:" + user.getMobile());
        return object;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public Map<String, Object> cbhbRealName(File excel) {
        Map<String, Object> map = Maps.newHashMap();

        StringBuffer errorParam = new StringBuffer(""); // 平台不存在的手机号
        Map<String, Object> mapCbhb = new HashMap<String, Object>(); // 提交渤海银行第三方信息
        List<User> userUpdateList = new ArrayList<User>(); // 修改真实姓名
        List<UserCache> userCacheUpdateList = new ArrayList<UserCache>(); // 修改身份证号
        List<AccountBankCard> accountBankCardList = new ArrayList<AccountBankCard>(); // 银行卡信息
        try {
            List[] userInfoSet = ExcelUtil.readExcelUserInfo(excel); // 读取用户数据
            mapCbhb.put("userInfo", userInfoSet);
            for (List<Object> userList : userInfoSet) {
                checkParam(userList, userUpdateList, userCacheUpdateList, accountBankCardList, errorParam);
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
        map.put("result", true);
        if (errorParam.length() > 0) {
            errorParam.deleteCharAt(errorParam.length() - 1);
            map.put("errorParam", errorParam.toString());
            map.put("result", false);
        } else {
            // 执行第三方注册
            TppService tppservice = (TppService) TppUtil.tppService();
            CbhbExistUserRegisterModel responseModel = (CbhbExistUserRegisterModel) tppservice.tppExperRealeName(mapCbhb);
            if (!CbhbConstant.CBHB_CODE_SUCCESS.equals(responseModel.getRespCode())) {
                map.put("errorParam", responseModel.getRespDesc());
                map.put("result", false);
            } else {
                // 修改账户信息
                dao.updateBatch(userUpdateList);
                userCacheService.updateBatch(userCacheUpdateList);
                accountBankCardService.insertBatch(accountBankCardList);
            }
        }
        return map;
    }

    /**
     * 校验上传数据
     *
     * @param userList
     * @param mapCbhb
     * @param accountBankCardList
     * @param userCacheUpdateList
     * @param userUpdateList
     * @param errorParam
     * @author ZhangBiao
     * @date 2017年3月15日
     */
    private void checkParam(List<Object> userList, List<User> userUpdateList, List<UserCache> userCacheUpdateList,
                            List<AccountBankCard> accountBankCardList, StringBuffer errorParam) {
        String idCard = userList.get(1).toString();
        if (!StringUtils.isCard(idCard)) {
            errorParam.append("身份证号码：").append(idCard).append("、");
            return;
        }
        String realName = userList.get(2).toString();
        if (!StringUtils.isChinese(realName)) {
            errorParam.append("姓名：").append(realName).append("、");
            return;
        }
        String iphone = userList.get(3).toString();
        if (!ValidateUtils.isPhone(iphone)) {
            errorParam.append("手机号码：").append(iphone).append("、");
            return;
        }
        // 判断手机号用户是否存在
        User user = dao.getUserByMobile(iphone.toString());
        if (user == null) {
            errorParam.append("手机号码：").append(iphone).append("、");
            return;
        }
        UserCache userCache = userCacheService.findByUserId(user.getUuid());
        // 更新真实姓名 身份证号
        user.setRealName(realName);
        userCache.setIdNo(idCard);
        userCache.setSex(StringUtils.getSex(idCard));
        // 保存银行卡信息
        AccountBankCard card = accountBankCardService.getCardByUserIdAndCardId(user.getUuid(),
                userList.get(5).toString(), CommonEnum.YES.getValue());
        if (null == card) {
            // 封装cbhb渤海银行参数
            card = new AccountBankCard();
            card.preInsert();
            card.setUserId(user.getUuid());
            card.setCreateTime(DateUtils.getNow());
            card.setBankCode(userList.get(4).toString());
            card.setBankAccount(userList.get(5).toString());
            card.setBankName(DictUtils.getItemName(CbhbConstant.CBHB_BANK_CARD, userList.get(4).toString()));
            accountBankCardList.add(card);
        }
        userUpdateList.add(user);
        userCacheUpdateList.add(userCache);
    }

    @Override
    public void CbhbExistUserRegister(CbhbExistUserRegisterModel cbhbModel) {
        //1、获取下载的result文件然后处理信息放入对象
        String fileName = cbhbModel.getResultFileName();
        String localFilePath = cbhbModel.getResultFileNamePath();//相对路径
        String remotePath = new CbhbChkBaseModel().getRemotePath(DateUtils.getNow());
        Map<String, Object> map = FileUtil.getResultFileContent(localFilePath, remotePath, fileName);
        String[] otherContent = (String[]) map.get("otherContent");
        List<User> userList = new ArrayList<User>();
        List<UserIdentify> userIdentifyList = new ArrayList<UserIdentify>();
        for (String contents : otherContent) {
            String[] content = contents.split(StringUtils.VERTICAL_LINE);
            String transId = content[0];
            String mobileNo = content[1];
            String respCode = content[2];
            String plaCustId = content[3];
            String respDesc = content[4];
            if (!CbhbConstant.CBHB_CODE_SUCCESS.equals(respCode)) {
                LOGGER.info("批量注册返回错误，流水号：{},手机号：{},返回码：{},错误信息:{}", transId, mobileNo, respCode, respDesc);
            } else {
                User user = dao.getUserByMobile(mobileNo);
                UserIdentify userIdentify = identifyService.findByUserId(user.getUuid());
                user.setTppUserCustId(plaCustId);
                user.setTppStatus(Constant.TPP_STATUS_ACTIVE);
                userList.add(user);
                userIdentify.setRealNameVerifyTime(DateUtils.getNow());
                userIdentify.setRealNameStatus(UserIdentifyModel.STATUS_REALNAME_SUCC);
                userIdentifyList.add(userIdentify);
            }
        }
        if (userList != null && userList.size() > 0) {
            dao.updateBatch(userList);
            identifyService.updateBatch(userIdentifyList);
        }

    }


    @Override
    public void cbhbUpdatePhone(final CbhbBindMobileNoModel mobileModel) {
        //1、验签
        if (mobileModel.validSign(mobileModel)) {
            String resultFlag = String.format(CacheKeys.PREFIX_TPP_ORDER_HANDLE_KEY.getKey(), mobileModel.getMerBillNo());
            if (CbhbConstant.CBHB_CODE_SUCCESS.equals(mobileModel.getRespCode())) {
                LOGGER.info("修改手机号进入本地回调处理，订单号userNo={}，回调参数：{}", mobileModel.getMobileNo(), mobileModel.toString());
                //重复处理判断(缓存标记) 开户回调处理 计数 失效时间： 1天
                final String handleKey = String.format(CacheKeys.PREFIX_TPP_REGISTER_HANDLE_NUM.getKey(), mobileModel.getMerBillNo());
                if (CacheUtils.incr(handleKey, Constant.DOUBLE_ONE) > Constant.DOUBLE_ONE) {
                    LOGGER.info("该修改手机号订单号已做处理", StringUtils.isNull(mobileModel.getMerBillNo()));
                    return;
                }
                // 更新失效时间
                CacheUtils.expire(handleKey, ExpireTime.ONE_DAY);

                String result = Constant.TPP_ORDER_HANDLE_SUCCESS;
                try {
                    //修改手机号    手机号变化，真实姓名和身份证号码不变， 银行卡可能换
                    final User user = dao.findByUserCustId(mobileModel.getPlaCustId());
                    user.setMobile(mobileModel.getMobileNo());
                    dao.update(user);
                    /*//根据回调的银行卡号查询是否和原来的一致   --  不作处理
					AccountBankCard card = accountBankCardService.getCardByUserIdAndCardId(user.getUuid(),
							mobileModel.getOpenBankId(), CommonEnum.YES.getValue());
					//查询个人所有的银行卡
					List<AccountBankCard> cardList = accountBankCardService.getCardByUserId(user.getUuid());
					if(card == null){//银行卡号和当前拥有的都不一致 说明已经变更了  以前拥有的卡号全部作废处理
						for (int i = 0; i < cardList.size(); i++) {
							AccountBankCard bCard = cardList.get(i);
							bCard.setStatus(CommonEnum.NO.getValue());
							bCard.setUpdateTime(DateUtils.getNow());
							accountBankCardService.update(bCard);
						}
						card = new AccountBankCard();
						card.preInsert();
						card.setUserId(user.getUuid());
						card.setCreateTime(DateUtils.getNow());
						card.setBankCode(mobileModel.getOpenBankId());
						card.setBankAccount(mobileModel.getOpenAcctId());
						card.setBankName(DictUtils.getItemName(CbhbConstant.CBHB_BANK_CARD, mobileModel.getOpenBankId()));
						accountBankCardService.insert(card);
					}else{//和其中一张的银行卡保持一致  那么原有的其他卡作废
						for (int i = 0; i < cardList.size(); i++) {
							AccountBankCard bCard = cardList.get(i);
							if(!bCard.getBankAccount().equals(card.getBankAccount())){
								bCard.setStatus(CommonEnum.NO.getValue());
								bCard.setUpdateTime(DateUtils.getNow());
								accountBankCardService.update(bCard);
							}
						}
					}*/
                } catch (Exception e) {
                    if (e instanceof BussinessException) {// 业务异常，保存业务处理信息
                        result = e.getMessage();
                    } else {
                        result = ResourceUtils.get(ResourceConstant.ORDER_HANDLE_SYSTEM_EXCEPTION);
                    }
                    throw e;
                } finally {
                    CacheUtils.set(resultFlag, result, ExpireTime.ONE_DAY);
                }
            } else {
                throw new CbhbException(ResourceUtils.get(CbhbResource.CBHB_RESOURCE_ERROR_BINDMOBILE, mobileModel.getRespCode(), mobileModel.getRespDesc()), BussinessException.TYPE_JSON);
            }
        } else {
            throw new CbhbException(ResourceUtils.get(ResourceConstant.TPP_NOTIFY_SIGN_FAIL), BussinessException.TYPE_JSON);
        }
    }

    @Override
    public void smsCodeApply(String mobile, String reqType, String srvTxCode, String cardId) {
        LOGGER.info("mobile:{}, reqType:{}, srvTxCode:{}, cardId:{}", mobile, reqType, srvTxCode, cardId);
        Map<String, Object> data = Maps.newHashMap();
        data.put("mobile", mobile == null ? mobile = SessionUtils.getSessionUser().getMobile() : mobile);
        data.put("reqType", reqType == null ? JxbankConstant.JXBANK_REQTYPE_ONE : reqType);
        data.put("cardNo", cardId == null ? "" : cardId);
        data.put("acqRes", "");
        data.put("srvTxCode", srvTxCode);

        TppService tppservice = (TppService) TppUtil.tppService();
        @SuppressWarnings({"unchecked"})
        Map<String, Object> resp = (Map<String, Object>) tppservice.tppSmsCodeApply(data);

        if (JxbankConstant.JXBANK_REQTYPE_ONE.equals(reqType)) {
            String srvAuthCode = (String) resp.get("srvAuthCode");
            SessionUtils.setSessionAttr("srvAuthCode:" + mobile, srvAuthCode);
        } else {
            String smsSeq = (String) resp.get("smsSeq");
            SessionUtils.setSessionAttr("smsSeq:" + mobile, smsSeq);
        }
//		String validTime = (String) resp.get("validTime");
    }

    @Override
    public Object queryCorpration(Map<String, Object> map) {
        TppService tppservice = (TppService) TppUtil.tppService();
        @SuppressWarnings("unchecked")
        Map<String, Object> resp = (Map<String, Object>) tppservice.queryCorpration(map);
        return resp;
    }

    @Override
    public Object fundTransQuery(Map<String, Object> map) {
        TppService tppservice = (TppService) TppUtil.tppService();
        @SuppressWarnings("unchecked")
        Map<String, Object> resp = (Map<String, Object>) tppservice.fundTransQuery(map);
        return resp;
    }

    @Override
    public User queryInvestDetails(String id) {
        return dao.queryInvestDetails(id);
    }

    @Override
    public User queryBorrowDetails(String id) {
        return dao.queryBorrowDetails(id);
    }

	@Override
	public Object bidApplyQuery(Map<String, Object> map) {
		TppService tppservice = (TppService)TppUtil.tppService();
		@SuppressWarnings("unchecked")
		Map<String,Object> resp = (Map<String, Object>) tppservice.bidApplyQuery(map);
		return resp;
	}

	@Override
	public Object creditDetailsQuery(Map<String, Object> map) {
		TppService tppservice = (TppService)TppUtil.tppService();
		@SuppressWarnings("unchecked")
		Map<String,Object> resp = (Map<String, Object>) tppservice.creditDetailsQuery(map);
		return resp;
	}

	@Override
	public void editUserAccountTypeByManage(UserModel model) {
		dao.editUserAccountTypeByManage(model);
	}

	@Override
	public void delUserCacheDataByType(final User user, final String type) {
		if(user != null){
			if(CacheConstant.KEY_PREFIX_USER_BANK_CARD.equals(type)){
				//解绑成功删除当前用户银行卡的缓存
				CacheUtils.del(type.concat(user.getUuid()));
			}
		}
		
	}

	@Override
	public boolean passwordSetQuery(String accountId) {
		Map<String, Object> map = new HashMap<>();
		map.put("accountId", accountId);//6212462380000800047
		TppService tppservice = (TppService)TppUtil.tppService();
		@SuppressWarnings("unchecked")
		Map<String,Object> resp = (Map<String, Object>) tppservice.passwordSetQuery(map);
		String pinFlag = (String) resp.get("pinFlag");//0-无密码 1-有密码
		if(Constant.FLAG_YES.equals(pinFlag)){
			return true;//有密码，已经设置过
		}
		return false;
	}
}