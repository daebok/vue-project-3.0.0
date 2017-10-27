package com.rd.ifaes.core.user.service.impl;

import com.google.common.collect.Maps;
import com.rd.ifaes.common.annotation.Cacheable;
import com.rd.ifaes.common.constant.CbhbConstant;
import com.rd.ifaes.common.constant.JxbankConstant;
import com.rd.ifaes.common.constant.TppServiceEnum;
import com.rd.ifaes.common.constant.UfxConstant;
import com.rd.ifaes.common.dict.*;
import com.rd.ifaes.common.exception.BussinessException;
import com.rd.ifaes.common.rabbit.RabbitUtils;
import com.rd.ifaes.common.util.*;
import com.rd.ifaes.core.account.domain.AccountBankCard;
import com.rd.ifaes.core.account.service.AccountBankCardService;
import com.rd.ifaes.core.base.service.BaseServiceImpl;
import com.rd.ifaes.core.core.constant.CacheConstant;
import com.rd.ifaes.core.core.constant.Constant;
import com.rd.ifaes.core.core.constant.ResourceConstant;
import com.rd.ifaes.core.core.sign.factory.SignFactory;
import com.rd.ifaes.core.core.util.CacheUtils;
import com.rd.ifaes.core.core.util.ConfigUtils;
import com.rd.ifaes.core.core.util.DictUtils;
import com.rd.ifaes.core.core.util.ResourceUtils;
import com.rd.ifaes.core.score.service.UserScoreService;
import com.rd.ifaes.core.statistic.domain.StatisticUser;
import com.rd.ifaes.core.statistic.service.StatisticUserService;
import com.rd.ifaes.core.tpp.exception.UfxException;
import com.rd.ifaes.core.tpp.model.cbhb.CbhbQueryUserInfModel;
import com.rd.ifaes.core.tpp.model.cbhb.CbhbRealNameWebModel;
import com.rd.ifaes.core.tpp.model.cbhb.app.CbhbNetLoanRegisterModel;
import com.rd.ifaes.core.tpp.model.ufx.UfxCompanyRegisterModel;
import com.rd.ifaes.core.tpp.model.ufx.UfxRegisterModel;
import com.rd.ifaes.core.tpp.service.TppService;
import com.rd.ifaes.core.user.domain.*;
import com.rd.ifaes.core.user.mapper.UserCacheMapper;
import com.rd.ifaes.core.user.model.UserCacheModel;
import com.rd.ifaes.core.user.model.UserModel;
import com.rd.ifaes.core.user.service.*;
import com.timevale.esign.sdk.tech.bean.seal.OrganizeTemplateType;
import com.timevale.esign.sdk.tech.bean.seal.PersonTemplateType;
import com.timevale.esign.sdk.tech.bean.seal.SealColor;
import com.timevale.esign.sdk.tech.impl.constants.OrganRegType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户附属信息处理类
 *
 * @author xhf
 * @version 3.0
 * @date 2016-6-15
 */
@Service("userCacheService")
@Transactional
public class UserCacheServiceImpl extends BaseServiceImpl<UserCacheMapper, UserCache> implements UserCacheService {

    /**
     * 日志记录器
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(UserCacheServiceImpl.class);

    /**
     * 用户类
     */
    @Resource
    private transient UserService userService;
    /**
     * 用户积分类
     */
    @Resource
    private transient UserScoreService userScoreService;
    /**
     * 用户认证信息类
     */
    @Resource
    private transient UserIdentifyService identifyService;
    /**
     * 个人用户信息类
     */
    @Resource
    private transient UserBaseInfoService baseInfoService;
    /**
     * 企业用户信息类
     */
    @Resource
    private transient UserCompanyInfoService companyService;

    @Resource
    private transient StatisticUserService statisticUserService;

    @Resource
    private UserCacheService userCacheService;

    /**
     * 渤海银行卡类
     */
    @Resource
    private transient AccountBankCardService accountBankCardService;

    /**
     * 根据userId查询用户附属信息
     */
    @Override
    @Cacheable(key = CacheConstant.KEY_PREFIX_USER_CACHE_USER_ID + "{userId}", expire = ExpireTime.ONE_HOUR)
    public UserCache findByUserId(final String userId) {
        return dao.findByUserId(userId);
    }

    /**
     * 个人开户-前期业务
     *
     * @return
     * @throws Exception
     */
    @Override
    public Object tppRealName(final UserCacheModel model) {
        LOGGER.info("[实名的信息,姓名：{},身份证号码：{}]", model.getRealName(), model.getIdNo());
        Object object = null;
        String tppName = ConfigUtils.getTppName();
        TppService tppService = (TppService) TppUtil.tppService();
        User user = SessionUtils.getSessionUser(BussinessException.TYPE_CLOSE);
        user = userService.get(user.getUuid());
        //封装参数
        final Map<String, Object> map = new HashMap<>();
        if (TppServiceEnum.CHINAPNR.getName().equals(tppName)) {
            //校验姓名和身份证号
            model.checkUserRegister();
            //校验身份证是否已存在
            if (!checkCardId(model.getIdNo())) {
                throw new BussinessException(ResourceUtils.get(ResourceConstant.IDCARD_IS_EXIST), BussinessException.TYPE_CLOSE);
            }
            //汇付开户传参
            map.put("userId", user.getUserNo());
            map.put("realName", model.getRealName());
            map.put("mobilePhone", user.getMobile());
            map.put("email", user.getEmail());
            map.put("idNo", model.getIdNo());
            map.put("sex", StringUtils.getSex(model.getIdNo()));
            object = tppService.tppRegister(map);
        } else if (TppServiceEnum.CBHB.getName().equals(tppName)) {
            if (CommonEnum.YES.eq(user.getTppStatus())) {//若是本地开户成功了
                object = false;
            } else {//本地没有开户成功
                if (StringUtils.isNotBlank(user.getTppUserCustId())) {
                    //开户前查询下手机号在三方是否已经开户
                    Map<String, Object> params = new HashMap<String, Object>();
                    params.put("mobileNo", user.getMobile());
                    params.put("plaCustId", user.getTppUserCustId());
                    CbhbQueryUserInfModel infModel = (CbhbQueryUserInfModel) tppService.queryUserInf(params);
                    if (CbhbConstant.CBHB_CODE_SUCCESS.equals(infModel.getRespCode())) {
                        //说明开过户，实名过   直接获取存管标识 录入数据库   银行卡无需处理
                        object = true;
                        this.regisHandle(user, infModel.getList().get(Constant.INT_ZERO).getUseName(), StringUtils.EMPTY, infModel.getPlaCustId(), infModel.getIdentNo());
                        LOGGER.info("已经在存管系统中开户成功,数据重新录入");
                    } else {
                        LOGGER.info("错误码：{},错误原因：{}", infModel.getRespCode(), infModel.getRespDesc());
                        //渤海银行存管开户传参
                        map.put("mobileNo", user.getMobile());
                        map.put("openType", CbhbConstant.STRING_ONE);
                        object = tppService.tppRegister(map);
                    }
                } else {
                    //渤海银行存管开户传参
                    map.put("mobileNo", user.getMobile());
                    map.put("openType", CbhbConstant.STRING_ONE);
                    object = tppService.tppRegister(map);
                }
            }
        } else if (TppServiceEnum.JXBANK.getName().equals(tppName)) {
            if (CommonEnum.YES.eq(user.getTppStatus())) {//若是本地开户成功了
                return true;
            } else {//本地没有开户成功
                //渤海银行存管开户传参
                map.put("userId", user.getUserNo());
                map.put("name", model.getRealName());
                map.put("mobile", user.getMobile());
                map.put("email", user.getEmail());
                map.put("idNo", model.getIdNo());
                map.put("idType", JxbankConstant.JXBANK_ID_TYPE_ONE);
                map.put("cardNo", model.getBankNo());
                map.put("acctUse", model.getAcctUse());
                map.put("lastSrvAuthCode", SessionUtils.getSessionAttr("srvAuthCode:" + user.getMobile()));
                map.put("smsCode", model.getSmsCode());
                map.put("userIP", model.getIp());
                map.put("acqRes", "");

                @SuppressWarnings("unchecked")
                Map<String, Object> rstMap = (Map<String, Object>) tppService.tppRegister(map);

                SessionUtils.removeAttribute("srvAuthCode:" + user.getMobile());
                regisHandle(user, model.getRealName(), (String) rstMap.get("accountId"), (String) rstMap.get("accountId"), model.getIdNo());
            }
        }
        return object;
    }

    /**
     * 企业开户-前期业务
     */
    @Override
    public UfxCompanyRegisterModel tppCompanyRealName(final UserCacheModel model) {
        final User user = SessionUtils.getSessionUser(BussinessException.TYPE_CLOSE);
        final UserCache userCache = dao.findByUserId(user.getUuid());
        //校验输入参数
        model.checkCompanyRegister();
        //校验用户开户状态
        model.checkAuditStatus(user.getUuid());
        //校验企业注册代码是否重复
        model.checkRegCode(user.getUuid());

        //是否是担保机构
        String guarType = UfxConstant.GUAR_TYPE_N;
        if (UserCache.USER_NATURE_VOUCH.equals(userCache.getUserNature())) {
            guarType = UfxConstant.GUAR_TYPE_Y;
        }
        // 是否三证合一
        String threeCertificate = Constant.THREE_CERTIFICATE_NO;
        String bussinessCode = StringUtils.isNull(model.getBussinessCode());
        String orgCode = StringUtils.isNull(model.getOrgCode());
        if (Constant.THREE_CERTIFICATE_YES.equals(model.getThreeCertificate())) {//三证合一
            threeCertificate = Constant.THREE_CERTIFICATE_YES;
            bussinessCode = StringUtils.isNull(model.getCreditCode());
            orgCode = "111111111";
        }

        //更新和保存法人身份证和姓名
        UserCompanyInfo userCompanyInfo = companyService.findByUserId(user.getUuid());
        final UserCompanyInfo companyInfo = new UserCompanyInfo();
        companyInfo.setUuid(userCompanyInfo.getUuid());
        companyInfo.setAuditStatus(UserCompanyInfo.AUDIT_STATUS_WAIT);
        companyInfo.setLegalDelegate(model.getLegalDelegate());
        companyInfo.setCertNo(model.getCertNo());
        companyInfo.setCertType(UserCompanyInfo.CERT_TYPE_ID_CARD);
        companyInfo.setThreeCertificate(threeCertificate);
        if (Constant.THREE_CERTIFICATE_YES.equals(threeCertificate)) {
            companyInfo.setCreditCode(model.getCreditCode());
        } else {
            companyInfo.setOrgCode(orgCode);
            companyInfo.setBussinessCode(bussinessCode);
        }
        companyService.updateLegalDelegate(companyInfo);

        //封装参数
        final Map<String, Object> map = new HashMap<>();
        map.put("userId", user.getUserNo());
        map.put("mobilePhone", user.getMobile());
        map.put("email", user.getEmail());
        map.put("orgCode", orgCode);
        map.put("companyName", model.getCompanyName());
        map.put("bussinessCode", bussinessCode);
        map.put("guarType", guarType);
        TppService tppService = (TppService) TppUtil.tppService();
        return (UfxCompanyRegisterModel) tppService.tppCropRegister(map);
    }

    /**
     * 开户-本地业务处理
     *
     * @return
     * @throws Exception
     */
    @Override
    @Transactional(readOnly = false)
    public void tppUfxUserRegis(final UfxRegisterModel model) {
        LOGGER.info("进入个人开户本地回调业务处理，用户userNo={}", model.getUserId());
        //check
        final User user = userService.findByUserNo(model.getUserId());
        if (null == user) {
            throw new BussinessException(ResourceUtils.get(ResourceConstant.USER_NOT_EXIST));
        }
        if (Constant.TPP_STATUS_ACTIVE.equals(user.getTppStatus())) { //用户已开户成功
            LOGGER.info("用户userNo={},已开户成功", user.getUserNo());
            return;
        }
        this.regisHandle(user, model.getRealName(), model.getUserAccId(), model.getUserCustId(), model.getIdNo());
    }

    @Override
    public void tppCbhbUserRegis(CbhbRealNameWebModel model) {
        LOGGER.info("进入个人开户本地回调业务处理，用户手机号={}", model.getMobileNo());
        //check
        final User user = userService.getUserByMobile(model.getMobileNo());
        if (null == user) {
            throw new BussinessException(ResourceUtils.get(ResourceConstant.USER_NOT_EXIST));
        }
        if (Constant.TPP_STATUS_ACTIVE.equals(user.getTppStatus())) { //用户已开户成功
            LOGGER.info("用户userNo={},已开户成功", user.getUserNo());
            return;
        }
        //渤海银行卡用用户信息查询接口来处理本地不再存入任何银行卡信息
        //insertAccountBankCard(user, model.getOpenBankId(), model.getOpenAcctId());
        regisHandle(user, model.getUsrName(), StringUtils.EMPTY, model.getPlaCustId(), model.getIdentNo());
    }

    @Override
    public void tppCbhbAppUserRegis(CbhbNetLoanRegisterModel model) {
        LOGGER.info("进入个人开户本地回调业务处理，用户手机号={}", model.getMobileNo());
        //check
        final User user = userService.getUserByMobile(model.getMobileNo());
        if (null == user) {
            throw new BussinessException(ResourceUtils.get(ResourceConstant.USER_NOT_EXIST));
        }
        if (Constant.TPP_STATUS_ACTIVE.equals(user.getTppStatus())) { //用户已开户成功
            LOGGER.info("用户userNo={},已开户成功", user.getUserNo());
            return;
        }
        //渤海银行卡用用户信息查询接口来处理本地不再存入任何银行卡信息
        regisHandle(user, model.getUsrName(), StringUtils.EMPTY, model.getPlaCustId(), model.getIdentNo());
    }

    /**
     * 渤海银行添加银行卡
     *
     * @param user
     * @param model
     */
    @SuppressWarnings("unused")
    @Deprecated
    private void insertAccountBankCard(final User user, final String bankType, final String bankCardId) {
        //insert account_bank_card
        AccountBankCard card = new AccountBankCard();
        card.setUserId(user.getUuid());
        card.setBankCode(bankType);
        card.setBankAccount(bankCardId);//第三方返回的就是带有*的银行卡
        card.setBankName(DictUtils.getItemName(CbhbConstant.CBHB_BANK_CARD, bankType));
        LOGGER.info("银行卡名称：{}", DictUtils.getItemName(CbhbConstant.CBHB_BANK_CARD, bankType));
        card.setCreateIp(StringUtils.EMPTY);
        card.setCreateTime(DateUtils.getNow());
        accountBankCardService.insertBankCard(card);
    }

    /**
     * 开户共同本地业务处理
     *
     * @param user
     * @param realName
     * @param tppUserAccId
     * @param tppCustId
     * @param cardId
     */
    private void regisHandle(final User user, final String realName, final String tppUserAccId, final String tppCustId,
                             final String cardId) {
        //update user
        user.setRealName(realName);
        user.setTppStatus(Constant.FLAG_YES);
        user.setTppUserAccId(tppCustId);
        user.setTppUserCustId(tppCustId);
        userService.update(user);

        //update user_cache
        final UserCache userCache = findByUserId(user.getUuid());
        if (null != userCache) {
            userCache.setIdNo(cardId);
            userCache.setSex(StringUtils.getSex(cardId));
        }

        //update user_identify
        final UserIdentify userIdentify = identifyService.findByUserId(user.getUuid());
        if (null != userIdentify) {
            userIdentify.setRealNameStatus(Constant.FLAG_YES);
            userIdentify.setRealNameVerifyTime(DateUtils.getNow());
            identifyService.update(userIdentify);
        }

        //update user_base_info
        final UserBaseInfo userBaseInfo = baseInfoService.findByUserId(user.getUuid());
        if (null != userBaseInfo) {
            String birthday = StringUtils.getBirthday(cardId);
            userBaseInfo.setBirthday(DateUtils.valueOf(birthday, DateUtils.DATEFORMAT_STR_012));
            baseInfoService.update(userBaseInfo);
        }

        //update statistic_user
        final StatisticUser statisticUser = statisticUserService.get(user.getUuid());
        if (null != statisticUser) {
            statisticUser.setYear(NumberUtils.getInt(StringUtils.getBirthYear(cardId)));
            statisticUser.setSex(StringUtils.getSex(cardId));
            statisticUser.setRealNameStatus(UserEnum.REAL_NAME_STATUS_SUCC.getValue());
            statisticUser.setRealNameDate(DateUtils.getNow());
            statisticUserService.update(statisticUser);
        }

        //协议注册签名
        final Map<String, Object> map = Maps.newHashMap();
        map.put("type", UserCache.USER_NATURE_PERSON);
        map.put("name", user.getRealName());
        map.put("idno", cardId);
        map.put("mobile", user.getMobile());

        //协议签章默认
        SignFactory factory = new SignFactory();
        userCache.setSignAccount(factory.register(map));
        if (StringUtils.isNotBlank(userCache.getSignAccount())) {
            map.put("accountId", userCache.getSignAccount());
            map.put("templateType", PersonTemplateType.SQUARE);
            map.put("color", SealColor.RED);
            userCache.setSignSealData(factory.createSeal(map));
        } else {
            LOGGER.error("创建E签宝企业账户失败，userId:{}", userCache.getUserId());
        }
        dao.update(userCache);

        //实名认证后 送积分
        userScoreService.grantScore(user, ScoreEnum.SCORE_REALNAME.getValue(), null);
        // 由于事物未提交可能导致缓存又被刷新 所以在方法最后再清一下
        CacheUtils.del(CacheConstant.KEY_PREFIX_USER_CACHE_USER_ID.concat(user.getUuid()));
        CacheUtils.del(CacheConstant.KEY_PREFIX_USER_IDENTIFY_USER_ID.concat(user.getUuid()));
        CacheUtils.del(CacheConstant.KEY_PREFIX_USER_USER_ID.concat(user.getUuid()));
    }

    /**
     * 企业开户-后期处理
     */
    @SuppressWarnings("unchecked")
    @Override
    @Transactional(readOnly = false)
    public void tppUfxCompanyRegis(final UfxCompanyRegisterModel model) {
        LOGGER.info("进入企业开户本地回调业务处理");
        //私有域不能为空
        if (StringUtils.isBlank(model.getMerPriv())) {
            throw new BussinessException(ResourceUtils.get(ResourceConstant.COMPANY_REGISTER_MERPRIV_NOT_EXIST));
        }
        //将私有域解密然后转化为map对象
        final String merPrivJson = Base64Utils.base64Decode(model.getMerPriv());
        LOGGER.info("私有域参数：{}", merPrivJson);
        final Map<String, Object> merPrivMap = JsonMapper.fromJsonString(merPrivJson, HashMap.class);

        //校验
        LOGGER.info("userNo：{}", merPrivMap.get("userId"));
        final User user = userService.findByUserNo(StringUtils.isNull(merPrivMap.get("userId")));
        if (null == user) {
            throw new BussinessException(ResourceUtils.get(ResourceConstant.USER_NOT_EXIST));
        }
        if (Constant.TPP_STATUS_ACTIVE.equals(user.getTppStatus())) { //用户已开户成功
            LOGGER.info("用户userNo={}已开户成功", user.getUserNo());
            return;
        }
        //附属信息
        final UserCompanyInfo userCompanyInfo = companyService.findByUserId(user.getUuid());
        userCompanyInfo.setCompanyName(StringUtils.isNull(merPrivMap.get("companyName")));
        userCompanyInfo.setAuditTime(DateUtils.getNow());
        userCompanyInfo.setAuditOrderNo(model.getTradeNo());

        //判断开户的状态
        if (UfxCompanyRegisterModel.REG_SUCCESS.equals(model.getAuditStat())) {  //开户成功
            //用户状态
            user.setRealName(userCompanyInfo.getCompanyName());
            user.setTppStatus(Constant.TPP_STATUS_ACTIVE);
            user.setTppUserAccId(model.getUserAccId());
            user.setTppUserCustId(model.getUserCustId());
            userService.update(user);

            //实名认证状态
            final UserIdentify identify = identifyService.findByUserId(user.getUuid());
            identify.setRealNameStatus(Constant.TPP_STATUS_ACTIVE);
            identify.setRealNameVerifyTime(DateUtils.getNow());
            identifyService.update(identify);

            //update statistic_user
            final StatisticUser statisticUser = statisticUserService.get(user.getUuid());
            if (null != statisticUser) {
                statisticUser.setRealNameDate(DateUtils.getNow());
                statisticUser.setRealNameStatus(UserEnum.REAL_NAME_STATUS_SUCC.getValue());
                statisticUserService.update(statisticUser);
            }

            //创建企业用户签章
            boolean createSign = createCompanySign(userCompanyInfo, user);
            if (!createSign) {
                LOGGER.info("创建E签宝企业账户失败，userId:{}", user.getUuid());
            }

            //清楚用户关联表缓存信息
            CacheUtils.del(CacheConstant.KEY_PREFIX_USER_CACHE_USER_ID.concat(user.getUuid()));

            userCompanyInfo.setAuditStatus(UserCompanyInfo.AUDIT_STATUS_SUCCESS);

        } else if (UfxCompanyRegisterModel.REG_FAIL.equals(model.getAuditStat()) ||
                UfxCompanyRegisterModel.REG_REJECT.equals(model.getAuditStat())) { //开户失败
            userCompanyInfo.setAuditStatus(UserCompanyInfo.AUDIT_STATUS_FAIL);
            userCompanyInfo.setAuditMessage(model.getAuditDesc());

        } else { //开户处理中
            userCompanyInfo.setAuditStatus(UserCompanyInfo.AUDIT_STATUS_PROCESS);
        }
        companyService.update(userCompanyInfo);
    }

    /**
     * 创建企业用户签章
     *
     * @param userCompanyInfo
     * @param user
     * @author xhf
     * @date 2016年11月3日
     */
    @Override
    @Transactional(readOnly = false)
    public boolean createCompanySign(UserCompanyInfo userCompanyInfo, User user) {
        boolean result = true;
        //协议注册签名
        final Map<String, Object> map = Maps.newHashMap();
        map.put("type", UserCache.USER_NATURE_COMPANY);
        map.put("name", userCompanyInfo.getCompanyName());
        map.put("mobile", user.getMobile());
        map.put("legalName", userCompanyInfo.getLegalDelegate());
        map.put("legalIdNo", userCompanyInfo.getCertNo());
        if (Constant.FLAG_YES.equals(userCompanyInfo.getThreeCertificate())) { //是否三证合一
            map.put("regType", OrganRegType.MERGE);
            map.put("organCode", userCompanyInfo.getCreditCode());
        } else {
            map.put("regType", OrganRegType.NORMAL);
            map.put("organCode", userCompanyInfo.getOrgCode());
        }
        //可选
        map.put("regCode", userCompanyInfo.getBussinessCode());

        //附属信息
        final UserCache cache = dao.findByUserId(user.getUuid());
        final SignFactory factory = new SignFactory();
        cache.setSignAccount(factory.register(map));
        if (StringUtils.isNotBlank(cache.getSignAccount())) {
            //协议签章默认
            map.put("accountId", cache.getSignAccount());
            map.put("templateType", OrganizeTemplateType.STAR);
            map.put("color", SealColor.RED);
            map.put("hText", Constant.SIGN_ACCOUNT_HTEXT);
            map.put("qText", Constant.SIGN_ACCOUNT_QTEXT);
            cache.setSignSealData(factory.createSeal(map));
            dao.update(cache);
        } else {
            result = false;
        }
        return result;
    }

    /**
     * 更新个人信息
     */
    @Override
    @Transactional(readOnly = false)
    public void modifyUserInfo(final String userName, final UserCache userCache, final UserBaseInfo userBaseInfo) {
        final String userId = SessionUtils.getSessionUser().getUuid();
        final User user = userService.get(userId);

        final UserCache dbUserCache = findByUserId(userId);
        if (!Constant.FLAG_YES.equals(dbUserCache.getRenameFlag())
                && StringUtils.isNotBlank(userName) && !user.getUserName().equals(userName)) {
            //校验用户格式
            if (!UserModel.isUserName(userName)) {
                throw new BussinessException(ResourceUtils.get(ResourceConstant.USER_NAME_ERROR), BussinessException.TYPE_JSON);
            }
            //校验用户名是否重复
            final int count = userService.checkRepeat(userId, userName, null, null);
            if (count > 0) {
                throw new BussinessException(ResourceUtils.get(ResourceConstant.USER_NAME_REPEAT), BussinessException.TYPE_JSON);
            }
            user.setUserName(userName);
            userService.update(user);
            SessionUtils.setSessionAttr(Constant.SESSION_USER, user);

            dbUserCache.setRenameFlag(Constant.FLAG_YES);
        }
        //附属信息
        dbUserCache.setProvince(userCache.getProvince());
        dbUserCache.setCity(userCache.getCity());
        dbUserCache.setArea(userCache.getArea());
        dbUserCache.setAddress(userCache.getAddress());
        dao.updateUserInfo(dbUserCache);
        CacheUtils.del(CacheConstant.KEY_PREFIX_USER_CACHE_USER_ID.concat(user.getUuid()));
        //基本信息
        final UserBaseInfo dbUserBaseInfo = baseInfoService.findByUserId(userId);
        dbUserBaseInfo.setEducation(userBaseInfo.getEducation());
        dbUserBaseInfo.setWorkExperience(userBaseInfo.getWorkExperience());
        dbUserBaseInfo.setMonthIncomeRange(userBaseInfo.getMonthIncomeRange());
        dbUserBaseInfo.setMaritalStatus(userBaseInfo.getMaritalStatus());
        dbUserBaseInfo.setCarStatus(userBaseInfo.getCarStatus());
        dbUserBaseInfo.setHouseStatus(userBaseInfo.getHouseStatus());
        dbUserBaseInfo.setProfession(userBaseInfo.getProfession());
        baseInfoService.update(dbUserBaseInfo);
        // 用户统计更新
        final StatisticUser statisticUser = statisticUserService.get(userId);
        statisticUser.setEducation(userBaseInfo.getEducation());
        statisticUser.setMonthIncomeRange(userBaseInfo.getMonthIncomeRange());
        statisticUser.setMaritalStatus(userBaseInfo.getMaritalStatus());
        statisticUserService.update(statisticUser);

    }

    /**
     * 修改企业信息
     */
    @Override
    @Transactional(readOnly = false)
    public void modifyCompanyInfo(final String userName, final UserCache userCache, final UserCompanyInfo userCompanyInfo) {
        final String userId = SessionUtils.getSessionUser().getUuid();
        final User user = userService.get(userId);

        final UserCache dbUserCache = findByUserId(userId);
        if (!Constant.FLAG_YES.equals(dbUserCache.getRenameFlag())
                && StringUtils.isNotBlank(userName) && !user.getUserName().equals(userName)) {
            //校验用户格式
            if (!UserModel.isUserName(userName)) {
                throw new BussinessException(ResourceUtils.get(ResourceConstant.USER_NAME_ERROR), BussinessException.TYPE_JSON);
            }
            //校验用户名是否重复
            final int count = userService.checkRepeat(userId, userName, null, null);
            if (count > 0) {
                throw new BussinessException(ResourceUtils.get(ResourceConstant.USER_NAME_REPEAT), BussinessException.TYPE_JSON);
            }
            user.setUserName(userName);
            userService.update(user);

            dbUserCache.setRenameFlag(Constant.FLAG_YES);
        }
        //附属信息
        dbUserCache.setProvince(userCache.getProvince());
        dbUserCache.setCity(userCache.getCity());
        dbUserCache.setArea(userCache.getArea());
        dbUserCache.setAddress(userCache.getAddress());
        dao.update(dbUserCache);
        CacheUtils.del(CacheConstant.KEY_PREFIX_USER_CACHE_USER_ID.concat(user.getUuid()));
        //基本信息
        final UserCompanyInfo dbUserCompanyInfo = companyService.findByUserId(userId);
        dbUserCompanyInfo.setSimpleName(userCompanyInfo.getSimpleName());
        dbUserCompanyInfo.setRegisteredCapital(userCompanyInfo.getRegisteredCapital());
        dbUserCompanyInfo.setEstablishDate(userCompanyInfo.getEstablishDate());
        dbUserCompanyInfo.setTaxRegNo(userCompanyInfo.getTaxRegNo());
        dbUserCompanyInfo.setTelephone(userCompanyInfo.getTelephone());
        dbUserCompanyInfo.setContacts(userCompanyInfo.getContacts());
        dbUserCompanyInfo.setNaturalPerson(userCompanyInfo.getNaturalPerson());
        dbUserCompanyInfo.setLegalPerson(userCompanyInfo.getLegalPerson());
        dbUserCompanyInfo.setOfficeProvince(userCompanyInfo.getOfficeProvince());
        dbUserCompanyInfo.setOfficeCity(userCompanyInfo.getOfficeCity());
        dbUserCompanyInfo.setOfficeArea(userCompanyInfo.getOfficeArea());
        dbUserCompanyInfo.setOfficeAddress(userCompanyInfo.getOfficeAddress());
        dbUserCompanyInfo.setCompanyIndustry(userCompanyInfo.getCompanyIndustry());
        dbUserCompanyInfo.setCompanyScale(userCompanyInfo.getCompanyScale());
        dbUserCompanyInfo.setRepaySource(userCompanyInfo.getRepaySource());
        dbUserCompanyInfo.setBusinessScope(userCompanyInfo.getBusinessScope());
        companyService.update(dbUserCompanyInfo);
    }

    /**
     * 更新头像
     */
    @Override
    @Transactional(readOnly = false)
    public void updateAvaPic(final User user, final String path) {
        final UserCache userCache = dao.findByUserId(user.getUuid());
        userCache.setAvatarPhoto(path);
        dao.update(userCache);
        CacheUtils.del(CacheConstant.KEY_PREFIX_USER_CACHE_USER_ID.concat(user.getUuid()));
    }

    /**
     * 更新备注
     */
    @Override
    @Transactional(readOnly = false)
    public int updateLockRemark(final UserCache userCache) {
        CacheUtils.del(CacheConstant.KEY_PREFIX_USER_CACHE_USER_ID.concat(userCache.getUserId()));
        return dao.updateLockRemark(userCache);
    }

    /**
     * 个人开户回调处理
     *
     * @param model
     * @author xhf
     * @date 2016年9月22日
     */
    @Override
    public void doRegisger(final UfxRegisterModel model) {
        final boolean ret = model.validSign(model);
        LOGGER.debug("用户开户验签结果：{}", ret);

        if (ret) { //验签通过
            String resultFlag = String.format(CacheKeys.PREFIX_TPP_ORDER_HANDLE_KEY.getKey(), model.getOrderNo());

            if (UfxConstant.UFX_CODE_SUCCESS.equals(model.getRespCode())) {
                LOGGER.info("个人开户进入本地回调处理，订单号userNo={}，回调参数：{}", model.getUserId(), model.toString());
                //重复处理判断(缓存标记) 开户回调处理 计数 失效时间： 1天
                final String handleKey = String.format(CacheKeys.PREFIX_TPP_REGISTER_HANDLE_NUM.getKey(), model.getOrderNo());
                if (CacheUtils.incr(handleKey, Constant.DOUBLE_ONE) > Constant.DOUBLE_ONE) {
                    LOGGER.info("该开户订单号已做处理", StringUtils.isNull(model.getOrderNo()));
                    return;
                }
                // 更新失效时间
                CacheUtils.expire(handleKey, ExpireTime.ONE_DAY);

                //调用接口
                if (ConfigUtils.isOpenMq()) {
                    RabbitUtils.register(model);

                } else {
                    String result = Constant.TPP_ORDER_HANDLE_SUCCESS;
                    try {
                        userCacheService.tppUfxUserRegis(model);
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
                }

            } else {
                String result = ResourceUtils.get(ResourceConstant.USER_TPP_REGISTER_FAIL) + ":" + model.getRespDesc();
                CacheUtils.set(resultFlag, result, ExpireTime.ONE_DAY);
            }
        } else {
            throw new UfxException(ResourceUtils.get(ResourceConstant.TPP_NOTIFY_SIGN_FAIL));
        }
    }

    @Override
    public void doCbhbAppRegisger(CbhbNetLoanRegisterModel model) {
        boolean ret = model.verifyMac(model.getDecryMac());
        if (ret) {
            String resultFlag = String.format(CacheKeys.PREFIX_TPP_ORDER_HANDLE_KEY.getKey(), model.getMerBillNo());

            if (CbhbConstant.CBHB_CODE_SUCCESS.equals(model.getRpCode())) {
                LOGGER.info("APP个人开户进入本地回调处理，订单号userNo={}，回调参数：{}", model.getMobileNo(), model.toString());
                //重复处理判断(缓存标记) 开户回调处理 计数 失效时间： 1天
                final String handleKey = String.format(CacheKeys.PREFIX_TPP_REGISTER_HANDLE_NUM.getKey(), model.getMerBillNo());
                if (CacheUtils.incr(handleKey, Constant.DOUBLE_ONE) > Constant.DOUBLE_ONE) {
                    LOGGER.info("该APP开户订单号已做处理", StringUtils.isNull(model.getMerBillNo()));
                    return;
                }
                // 更新失效时间
                CacheUtils.expire(handleKey, ExpireTime.ONE_DAY);

                String result = Constant.TPP_ORDER_HANDLE_SUCCESS;
                try {
                    userCacheService.tppCbhbAppUserRegis(model);
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
                String result = ResourceUtils.get(ResourceConstant.USER_TPP_REGISTER_FAIL) + ":" + model.getRpDesc();
                CacheUtils.set(resultFlag, result, ExpireTime.ONE_DAY);
            }
        } else {
            throw new UfxException(ResourceUtils.get(ResourceConstant.TPP_NOTIFY_SIGN_FAIL));
        }
    }

    @Override
    public void doCbhbRegisger(CbhbRealNameWebModel model) {
        final boolean ret = model.validSign(model);
        LOGGER.debug("用户开户验签结果：{}", ret);

        if (ret) { //验签通过
            String resultFlag = String.format(CacheKeys.PREFIX_TPP_ORDER_HANDLE_KEY.getKey(), model.getMerBillNo());

            if (CbhbConstant.CBHB_CODE_SUCCESS.equals(model.getRespCode())) {
                LOGGER.info("个人开户进入本地回调处理，订单号userNo={}，回调参数：{}", model.getMobileNo(), model.toString());
                //重复处理判断(缓存标记) 开户回调处理 计数 失效时间： 1天
                final String handleKey = String.format(CacheKeys.PREFIX_TPP_REGISTER_HANDLE_NUM.getKey(), model.getMerBillNo());
                if (CacheUtils.incr(handleKey, Constant.DOUBLE_ONE) > Constant.DOUBLE_ONE) {
                    LOGGER.info("该开户订单号已做处理", StringUtils.isNull(model.getMerBillNo()));
                    return;
                }
                // 更新失效时间
                CacheUtils.expire(handleKey, ExpireTime.ONE_DAY);

                //调用接口
//				if(ConfigUtils.isOpenMq()){
//					RabbitUtils.register(model);
//				}else{
                String result = Constant.TPP_ORDER_HANDLE_SUCCESS;
                try {
                    userCacheService.tppCbhbUserRegis(model);
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
//				}	

            } else {
                String result = ResourceUtils.get(ResourceConstant.USER_TPP_REGISTER_FAIL) + ":" + model.getRespDesc();
                CacheUtils.set(resultFlag, result, ExpireTime.ONE_DAY);
            }
        } else {
            throw new UfxException(ResourceUtils.get(ResourceConstant.TPP_NOTIFY_SIGN_FAIL));
        }
    }

    /**
     * 企业开户异步回调处理
     *
     * @param flag
     * @return
     */
    @Override
    public void doCompanyRegisger(final UfxCompanyRegisterModel model) {
        final boolean ret = model.validSign(model);
        LOGGER.info("企业用户开户验签结果：{}", ret);
        if (ret) { //验签通过
            if (ConfigUtils.isOpenMq()) {
                RabbitUtils.companyRegister(model);
            } else {
                userCacheService.tppUfxCompanyRegis(model);
            }
        } else {
            throw new UfxException(ResourceUtils.get(ResourceConstant.TPP_NOTIFY_SIGN_FAIL));
        }
    }


    @Override
    public boolean checkCardId(final String cardId) {
        final int count = dao.countCardId(cardId);
        if (count == 0) {
            return true;
        }
        return false;
    }

    @Override
    public int userFirstInvest(String userId) {
        return dao.userFirstInvest(userId);
    }

    @Override
    public int addUserInvestNum(String userId) {
        final int updateNum = dao.addUserInvestNum(userId);
        if (updateNum > 0) {
            CacheUtils.del(CacheConstant.KEY_PREFIX_USER_CACHE_USER_ID.concat(userId));
        }
        return updateNum;
    }

    @Override
    public int subUserInvestNum(String userId) {
        final int updateNum = dao.subUserInvestNum(userId);
        if (updateNum > 0) {
            CacheUtils.del(CacheConstant.KEY_PREFIX_USER_CACHE_USER_ID.concat(userId));
        }
        return updateNum;
    }

    @Override
    public List<Map<String, String>> getUserCacheByUserList(List<User> list) {
        return dao.getUserCacheByUserList(list);
    }

    @Override
    public void accountIdQuery(String idNo, String realName) {
        User user = userService.get(SessionUtils.getSessionUser().getUuid());
        Map<String, Object> data = Maps.newHashMap();
        data.put("idType", JxbankConstant.JXBANK_ID_TYPE_ONE);
        data.put("idNo", idNo);

        TppService tppservice = (TppService) TppUtil.tppService();
        @SuppressWarnings({"unchecked"})
        Map<String, Object> resp = (Map<String, Object>) tppservice.tppAccountIdQuery(data);
        regisHandle(user, realName, StringUtils.EMPTY, (String) resp.get("accountId"), idNo);
    }
}