/**
 * 互联网金融交易系统ifaes (Internet Finance  Assets  Exchange  System)
 * Copyright (c) 2016 杭州融都科技股份有限公司 版权所有
 */
package com.rd.ifaes.core.project.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rd.account.domain.Account;
import com.rd.account.domain.AccountLog;
import com.rd.account.model.AccountModel;
import com.rd.account.model.AccountQueryModel;
import com.rd.account.service.AccountLogService;
import com.rd.account.service.AccountService;
import com.rd.ifaes.common.constant.CbhbConstant;
import com.rd.ifaes.common.constant.TppServiceEnum;
import com.rd.ifaes.common.dict.CacheKeys;
import com.rd.ifaes.common.dict.CommonEnum;
import com.rd.ifaes.common.dict.ExpireTime;
import com.rd.ifaes.common.dict.InvestEnum;
import com.rd.ifaes.common.dict.LoanEnum;
import com.rd.ifaes.common.dict.OperateEnum;
import com.rd.ifaes.common.exception.BussinessException;
import com.rd.ifaes.common.orm.Page;
import com.rd.ifaes.common.rabbit.RabbitUtils;
import com.rd.ifaes.common.resource.CbhbResource;
import com.rd.ifaes.common.security.shiro.IdGen;
import com.rd.ifaes.common.util.BigDecimalUtils;
import com.rd.ifaes.common.util.DateUtils;
import com.rd.ifaes.common.util.SessionUtils;
import com.rd.ifaes.common.util.SpringContextHolder;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.common.util.TppUtil;
import com.rd.ifaes.common.util.resource.AccountResource;
import com.rd.ifaes.common.util.resource.BondResource;
import com.rd.ifaes.common.util.resource.LoanResource;
import com.rd.ifaes.core.base.service.BaseServiceImpl;
import com.rd.ifaes.core.bond.service.BondService;
import com.rd.ifaes.core.cache.InvestCache;
import com.rd.ifaes.core.cache.ProjectCache;
import com.rd.ifaes.core.core.constant.CacheConstant;
import com.rd.ifaes.core.core.constant.ConfigConstant;
import com.rd.ifaes.core.core.constant.Constant;
import com.rd.ifaes.core.core.constant.ResourceConstant;
import com.rd.ifaes.core.core.util.CacheUtils;
import com.rd.ifaes.core.core.util.ConfigUtils;
import com.rd.ifaes.core.core.util.LogTemplateUtils;
import com.rd.ifaes.core.core.util.ResourceUtils;
import com.rd.ifaes.core.interest.InterestCalculator;
import com.rd.ifaes.core.operate.domain.UserRateCoupon;
import com.rd.ifaes.core.operate.domain.UserRedenvelope;
import com.rd.ifaes.core.operate.domain.UserVip;
import com.rd.ifaes.core.operate.service.UserRateCouponService;
import com.rd.ifaes.core.operate.service.UserRedenvelopeService;
import com.rd.ifaes.core.operate.service.UserVipService;
import com.rd.ifaes.core.project.domain.Project;
import com.rd.ifaes.core.project.domain.ProjectInvest;
import com.rd.ifaes.core.project.domain.Realize;
import com.rd.ifaes.core.project.exception.ProjectException;
import com.rd.ifaes.core.project.mapper.ProjectInvestMapper;
import com.rd.ifaes.core.project.model.MyProjectInvestModel;
import com.rd.ifaes.core.project.model.OutTimeProjectInvest;
import com.rd.ifaes.core.project.model.ProjectInvestModel;
import com.rd.ifaes.core.project.model.ProjectInvestRecord;
import com.rd.ifaes.core.project.service.ProjectCollectionService;
import com.rd.ifaes.core.project.service.ProjectInvestService;
import com.rd.ifaes.core.project.service.ProjectService;
import com.rd.ifaes.core.project.worker.ProjectWorker;
import com.rd.ifaes.core.protocol.service.ProtocolService;
import com.rd.ifaes.core.tpp.exception.CbhbException;
import com.rd.ifaes.core.tpp.model.cbhb.CbhbBackInvestModel;
import com.rd.ifaes.core.tpp.model.cbhb.CbhbInvestCancleModel;
import com.rd.ifaes.core.tpp.model.jx.JxBidApplyModel;
import com.rd.ifaes.core.tpp.model.ufx.UfxInvestModel;
import com.rd.ifaes.core.tpp.service.TppService;
import com.rd.ifaes.core.tpp.service.tpp.UfxInvestService;
import com.rd.ifaes.core.user.domain.User;
import com.rd.ifaes.core.user.domain.UserCache;
import com.rd.ifaes.core.user.domain.UserIdentify;
import com.rd.ifaes.core.user.model.UserFreezeModel;
import com.rd.ifaes.core.user.service.UserAuthSignLogService;
import com.rd.ifaes.core.user.service.UserCacheService;
import com.rd.ifaes.core.user.service.UserFreezeService;
import com.rd.ifaes.core.user.service.UserIdentifyService;
import com.rd.ifaes.core.user.service.UserService;

/**
 * ServiceImpl:ProjectInvestServiceImpl
 *
 * @author FangJun
 * @version 3.0
 * @date 2016-7-13
 */
@Service("projectInvestService")
public class ProjectInvestServiceImpl extends BaseServiceImpl<ProjectInvestMapper, ProjectInvest>
        implements ProjectInvestService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProjectInvestServiceImpl.class);
    @Resource
    private transient BondService bondService;
    @Resource
    private ProtocolService protocolService;
    @Resource
    private UserCacheService userCacheService;
    @Resource
    private UserIdentifyService userIdentifyService;
    @Resource
    private ProjectService projectService;
    @Resource
    private UserService userService;
    @Resource
    private UserVipService userVipService;
    @Resource
    private AccountService accountService;
    @Resource
    private AccountLogService accountLogService;
    @Resource
    private UserRateCouponService userRateCouponService;
    @Resource
    private UserRedenvelopeService userRedenvelopeService;
    @Resource
    private UserAuthSignLogService userAuthSignLogService;
    @Resource
    private ProjectCollectionService projectCollectionService;
    @Resource
    private UfxInvestService ufxInvestService;

    @Override
    public ProjectInvest getProjectInvest(final String id, final String backUrl) {
        if (StringUtils.isBlank(id)) {
            throw new BussinessException(ResourceUtils.get(BondResource.PROJECT_INVEST_ID_NOT_EXISTS), backUrl, BussinessException.TYPE_JSON);
        }
        final ProjectInvest projectInvest = dao.get(id);
        if (projectInvest == null) {
            throw new BussinessException(ResourceUtils.get(BondResource.PROJECT_INVEST_NOT_EXISTS), backUrl, BussinessException.TYPE_JSON);
        }
        return projectInvest;
    }

    @Override
    public ProjectInvest getInvestByOrderNo(String orderNo) {
        return dao.getInvestByOrderNo(orderNo);
    }

    /**
     * 检查投资数据完整性
     *
     * @param invest 投资数据
     * @author FangJun
     * @date 2016年7月29日
     */
    private void checkInvestParams(ProjectInvestModel invest) {
        if (StringUtils.isBlank(invest.getProjectId())) {
            throw new BussinessException(ResourceUtils.get(LoanResource.PROJECT_ID_IS_NULL), BussinessException.TYPE_CLOSE);
        }
        if (!BigDecimalUtils.validAmount(invest.getAmount())) {
            throw new BussinessException(ResourceUtils.get(LoanResource.INVEST_AMOUNT_IS_NOT_VAILD),
                    BussinessException.TYPE_CLOSE);
        }
    }

    /**
     * 根据冻结流水号查询投资记录
     *
     * @param freezeNo 冻结流水号
     * @return 对应投资记录
     */
    @Override
    public ProjectInvest getInvestByFreezeNo(String freezeNo) {
        return dao.getInvestByFreezeNo(freezeNo);
    }


    @Override
    @Transactional(readOnly = false)
    public Object doInvest(ProjectInvestModel model) {
        long startTime = System.currentTimeMillis();
        checkInvestParams(model);
        if (StringUtils.isBlank(model.getUserId())) {
            throw new BussinessException(ResourceUtils.get(ResourceConstant.USER_NOT_LOGIN), BussinessException.TYPE_CLOSE);
        }
        User user = userService.get(model.getUserId());
        // 投资项目信息
        model.setIsSelectedTip(1);
        Project project = checkSpecificSale(model, user);
        model.setBorrowFlag(project.getBorrowFlag());
        // 校验投资信息
        checkInvestInfo(project, user);
        //新手标校验
        incrInvestNumForNovice(project, user.getTppUserCustId());

        // 未支付订单数校验
        if (!InvestCache.incrUserInvestUnpay(user.getTppUserCustId())) {
            this.decrInvestNumForNovice(project, user.getTppUserCustId());
            throw new BussinessException(StringUtils.format(ResourceUtils.get(LoanResource.INVEST_UNPAY_TOO_MUCH), ConfigUtils.getValue(ConfigConstant.INVEST_UNPAY_MAX)), BussinessException.TYPE_JSON, "/member/myInvest/list.html?tab=2", "查看详情");
        }
        // 超投校验,同时扣除投资金额
        double currentProjectAmount = InvestCache.checkOverInvest(project.getUuid(), model.getAmount().doubleValue());

        try {
            // 投资金额校验
            checkInvestAmount(project, model, model.getAmount().add(BigDecimal.valueOf(currentProjectAmount)));
            // 生成投资ID,给加加息劵、红包使用
            model.setUuid(IdGen.uuid());

            long validateEnd = System.currentTimeMillis();
            LOGGER.info("projectInvestService doInvest validate take time:{}ms,userId:{}", (validateEnd - startTime), model.getUserId());

            // 优惠劵信息校验
            checkVoucher(model, project, user);
            // 用户资金判断
            Account account = accountService.getMoneyByUserId(new AccountQueryModel(user.getUuid(), ConfigUtils.getValue(Constant.ACCOUNT_CODE)));
            if (account.getUseMoney().compareTo(model.getRealAmount()) < 0) {
                throw new BussinessException(ResourceUtils.get(AccountResource.ACCOUNT_USEMONEY_NOTENOUGHERROR),
                        BussinessException.TYPE_CLOSE);
            }

            //封装第三方出借报文
            String tppName = ConfigUtils.getTppName();
            String orderNo = "";
            Object object = null;
            if (TppServiceEnum.CHINAPNR.getName().equals(tppName)) {
                object = createUfxInvestModel(model, project, user);
                orderNo = ((UfxInvestModel) object).getOrderNo();
            } else if (TppServiceEnum.JXBANK.getName().equals(tppName)) {
                object = createJxBidApplyModel(model, project, user);
                orderNo = ((JxBidApplyModel) object).getOrderId();
            }
            // 新建投资记录(投资订单)
            createInvestRecord(model, user, orderNo);

            //冻结投资金额
            handleInvesterAccount(model.getAmount(), model.getRealAmount(), model.getUpApr(), user, orderNo, project);

            //用户投资记录+1
            if (ConfigUtils.isOpenMq()) {
                RabbitUtils.addUserInvestNum(model.getUserId());
            } else {
                userCacheService.addUserInvestNum(model.getUserId());
            }
            return object;

        } catch (Exception e) {
            // 投资失败，归还剩余可投金额
            ProjectCache.incrRemainAccount(project.getUuid(), model.getAmount().doubleValue());
            //回退用户未支付次数
            InvestCache.decrUserInvestUnpay(user.getTppUserCustId());
            //修正新手投资笔数
            this.decrInvestNumForNovice(project, user.getTppUserCustId());
            throw e;
        } finally {
            long endTime = System.currentTimeMillis();
            LOGGER.info("projectInvestService doInvest take time:{}ms,userId:{}", (endTime - startTime), model.getUserId());
        }
    }

    private JxBidApplyModel createJxBidApplyModel(ProjectInvestModel model, Project project, User user) {
        Map<String, Object> map = new HashMap<>();
        map.put("accountId", user.getTppUserAccId());
        map.put("txAmount", BigDecimalUtils.round(model.getAmount(), 2));
        map.put("productId", project.getUuid());
        BigDecimal red = BigDecimalUtils.round(model.getAmount().subtract(model.getRealAmount()));
        if (red.compareTo(BigDecimal.ZERO) > 0) {
            map.put("bonusFlag", "1");
            map.put("bonusAmount", red.toString());
        }
        // 封装出借参数
        TppService tppService = (TppService) TppUtil.tppService();
        return (JxBidApplyModel) tppService.tppAddTender(map);
    }

    /**
     * 避免瞬间新手标重复投
     *
     * @param project    项目信息
     * @param userCustId 用户标识（三方账户）
     */
    private void incrInvestNumForNovice(final Project project, final String userCustId) {
        if (CommonEnum.YES.eq(project.getNovice())) {
            String key = String.format(CacheKeys.PREFIX_USER_INVEST_NUM.getKey(), userCustId);
            double currentNum = CacheUtils.incr(key, Constant.DOUBLE_ONE);
            if (currentNum <= Constant.DOUBLE_ONE) {
                CacheUtils.expire(key, ExpireTime.TEN_MIN);
            }
            if (currentNum > Constant.DOUBLE_ONE) {
                CacheUtils.decr(key, Constant.DOUBLE_ONE);
                LOGGER.error("---Invest novice again ---userId: {}", userCustId);
                throw new ProjectException(ResourceUtils.get(LoanResource.INVEST_NOVICE_AGAIN), BussinessException.TYPE_CLOSE);
            }
        }
    }

    /**
     * 新手标投资笔数修正
     *
     * @param project    项目信息
     * @param userCustId 用户标识（三方账户）
     */
    private void decrInvestNumForNovice(final Project project, final String userCustId) {
        if (CommonEnum.YES.eq(project.getNovice())) {
            String key = String.format(CacheKeys.PREFIX_USER_INVEST_NUM.getKey(), userCustId);
            double currentNum = CacheUtils.decr(key, Constant.DOUBLE_ONE);
            if (currentNum < Constant.DOUBLE_ZERO) {
                LOGGER.error("--- novice Invest number resume error ---userId: {}", userCustId);
            }
        }
    }

    private ProjectInvest createInvestRecordCbhb(ProjectInvestModel model, User user,
                                                 String merBillNo) {
        ProjectInvest invest = model.prototype();
        invest.setInvestOrderNo(merBillNo);
        invest.setStatus(InvestEnum.STATUS_NEW.getValue());
        invest.setCreateTime(DateUtils.getNow());
        invest.setMoney(invest.getAmount());
        invest.setRealAmount(model.getRealAmount());
        invest.setUserId(user.getUuid());
        invest.setRealizeFlag(InvestEnum.REALIZE_FLAG_NORMAL.getValue());
        invest.setBondFlag(InvestEnum.BOND_FLAG_NORMAL.getValue());
        invest.setInvestStyle(InvestEnum.INVEST_STYLE_NORMAL.getValue());
        invest.setRealizeAmount(BigDecimal.ZERO);
        invest.setRealizeInterest(BigDecimal.ZERO);
        invest.setFreezeInterest(BigDecimal.ZERO);
        invest.setFreezeCapital(BigDecimal.ZERO);
        invest.setPayment(BigDecimal.ZERO);
        invest.setInterest(BigDecimal.ZERO);
        invest.setRepayedAmount(BigDecimal.ZERO);
        invest.setRepayedInterest(BigDecimal.ZERO);
        invest.setWaitAmount(BigDecimal.ZERO);
        invest.setWaitInterest(BigDecimal.ZERO);
        invest.setWaitRaiseInterest(BigDecimal.ZERO);
        invest.setBorrowFlag(model.getBorrowFlag());
        return invest;
    }

    /**
     * 渤海银行投资后台方式提交
     *
     * @param model
     * @param project
     * @param user
     * @return
     * @author ZhangBiao
     * @date 2017年3月1日
     */
    private CbhbBackInvestModel createCbhbInvestModel(ProjectInvestModel model,
                                                      Project project, User user) {
        TppService tppservice = (TppService) TppUtil.tppService();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("busiType", Constant.INT_ONE);
        map.put("plaCustId", user.getTppUserCustId());
        map.put("smsCode", model.getSmsCode());
        map.put("transAmt", model.getAmount());
        map.put("marketAmt", BigDecimalUtils.sub(model.getAmount(), model.getRealAmount()));
        map.put("borrowId", project.getProjectNo());
        return (CbhbBackInvestModel) tppservice.tppAddTender(map);
    }


    /**
     * 投资用户资金处理
     *
     * @param amount     投资金额
     * @param realAmount 实投金额
     * @param upApr      加息券加息额度
     * @param user       投资用户
     * @param orderNo    投资订单号
     * @author FangJun
     * @date 2016年8月19日
     */
    protected void handleInvesterAccount(BigDecimal amount, BigDecimal realAmount, BigDecimal upApr, User user, String orderNo, final Project project) {
        // 日志模板备注处理
        Map<String, Object> remarkData = new HashMap<String, Object>(2);
        remarkData.put("info", projectService.getProjectInfo(project.getUuid(), projectService.getProjectName(project), project.getRealizeFlag()));
        remarkData.put("amount", amount.doubleValue());
        remarkData.put("realAmount", realAmount.doubleValue());
        remarkData.put("redEnvelope", amount.doubleValue() - realAmount.doubleValue());
        remarkData.put("upApr", upApr != null ? upApr.doubleValue() : BigDecimal.ZERO);
        String remark = LogTemplateUtils.getAccountTemplate(Constant.INVEST_FREEZE, remarkData);
        AccountLog log = new AccountLog(ConfigUtils.getValue(Constant.ACCOUNT_CODE), Constant.INVEST_FREEZE, user.getUuid(), realAmount, remark);
        log.setToUser(user.getUuid());
        log.setPaymentsType(AccountLog.PAYMENTS_TYPE_REMAIN);
        log.setOrderNo(orderNo);
        accountService.saveAccountChange(new AccountModel(user.getUuid(), ConfigUtils.getValue(Constant.ACCOUNT_CODE), realAmount.negate(), realAmount), log);
    }


    /**
     * 投资失败-用户资金解冻处理
     *
     * @param amount     投资金额
     * @param realAmount 实投金额
     * @param userId     投资用户ID
     * @param orderNo    投资订单号
     * @author FangJun
     * @date 2016年8月19日
     */
    public void handleFailInvesterAccount(BigDecimal amount, BigDecimal realAmount, String userId, String orderNo) {
        // 日志模板备注处理
        Map<String, Object> remarkData = new HashMap<String, Object>(2);
        remarkData.put("amount", amount.doubleValue());
        remarkData.put("realAmount", realAmount.doubleValue());
        String remark = LogTemplateUtils.getAccountTemplate(Constant.INVEST_UNFREEZE, remarkData);
        AccountLog log = new AccountLog(ConfigUtils.getValue(Constant.ACCOUNT_CODE), Constant.INVEST_UNFREEZE, userId, realAmount, remark);
        log.setToUser(userId);
        log.setPaymentsType(AccountLog.PAYMENTS_TYPE_REMAIN);
        log.setOrderNo(orderNo);
        accountService.saveAccountChange(new AccountModel(userId, ConfigUtils.getValue(Constant.ACCOUNT_CODE), realAmount, realAmount.negate()), log);
    }

    /**
     * 检查红包、加息券
     *
     * @param model   投资信息
     * @param project 投资项目
     * @param user    投资用户
     * @author FangJun
     * @date 2016年8月17日
     */
    @Override
    //@Transactional(readOnly = false)
    public void checkVoucher(final ProjectInvestModel model, final Project project, final User user) {
        // 1 检查红包总金额
        final String redEnvelopeIds = model.getRedEnvelopeIds();
        // 使用红包总额
        BigDecimal redEnvelopeTotal = BigDecimal.ZERO;
        if (StringUtils.isNotBlank(redEnvelopeIds)) {
            if (!CommonEnum.YES.eq(project.getRedEnvelopeUseful())) {
                throw new BussinessException(ResourceUtils.get(LoanResource.INVEST_CANNOT_USE_REDENVELOPE), BussinessException.TYPE_CLOSE);
            }
            BigDecimal redEnvelopeRateMax = project.getRedEnvelopeRate();
            if (CommonEnum.YES.eq(project.getRedEnvelopeUseful()) && !BigDecimalUtils.validAmount(redEnvelopeRateMax)) {
                redEnvelopeRateMax = ConfigUtils.getBigDecimal(ConfigConstant.INVEST_REDENVELOPE_RATE);
            }
            final BigDecimal userInvestRedpackageRate = redEnvelopeRateMax.divide(BigDecimal.valueOf(Constant.DOUBLE_ONE_HUNDRED));
            final StringBuffer redBuffer = new StringBuffer();
            final String[] redEnvelopeIdArr = redEnvelopeIds.split(",");

            for (String redEnvelopeId : redEnvelopeIdArr) {
                UserRedenvelope redEnvelope = userRedenvelopeService.get(redEnvelopeId);
                if (redEnvelope == null) {
                    throw new BussinessException(ResourceUtils.get(LoanResource.INVEST_REDENVELOPE_NOT_EXISTS),
                            BussinessException.TYPE_CLOSE);
                }
                if (!user.getUuid().equals(redEnvelope.getUserId())) {
                    throw new BussinessException(ResourceUtils.get(LoanResource.INVEST_USE_OTHER_REDENVELOPE),
                            BussinessException.TYPE_CLOSE);
                }
                if (!redEnvelope.isAvailable(project, model.getAmount())) {
                    throw new BussinessException(ResourceUtils.get(LoanResource.INVEST_USE_INVALID_REDENVELOPE), BussinessException.TYPE_CLOSE);
                }
                redBuffer.append("'").append(redEnvelopeId).append("',");
                redEnvelopeTotal = redEnvelopeTotal.add(redEnvelope.getAmount());
            }

            LOGGER.debug("用户(UUID:{}),投资使用红包总额：{}", user.getRealName(), redEnvelopeTotal);

            if (userInvestRedpackageRate.doubleValue() > Constant.DOUBLE_ZERO
                    && userInvestRedpackageRate.compareTo(BigDecimalUtils.div(redEnvelopeTotal, model.getAmount())) < Constant.INT_ZERO) {
                throw new BussinessException(ResourceUtils.get(LoanResource.INVEST_USE_TOO_MUCH_REDENVELOPE), BussinessException.TYPE_CLOSE);
            }

            // 更新红包使用状态为已使用
            int updateNum = userRedenvelopeService.updateStatusBatch(redBuffer.substring(0, redBuffer.length() - 1), model.getUuid(),
                    OperateEnum.STATUS_USE.getValue(), OperateEnum.STATUS_NO_USE.getValue());
            if (updateNum != redEnvelopeIdArr.length) {
                throw new BussinessException(ResourceUtils.get(LoanResource.INVEST_USE_INVALID_REDENVELOPE), BussinessException.TYPE_CLOSE);
            }
        }
        //设置用户实际支付金额
        model.setRealAmount(BigDecimalUtils.sub(model.getAmount(), redEnvelopeTotal));
        //实际支付不可为零
        if (!BigDecimalUtils.validAmount(model.getRealAmount())) {
            throw new BussinessException(ResourceUtils.get(LoanResource.INVEST_REAL_AMOUNT_NOT_EQ_ZERO), BussinessException.TYPE_CLOSE);
        }
        // 2 检查加息劵
        final String rateCouponId = model.getRateCouponId();
        if (StringUtils.isNotBlank(rateCouponId)) {
            //只有项目无加息 且 设置可用加息劵，才可使用
            if (!(!BigDecimalUtils.validAmount(project.getAddApr()) && CommonEnum.YES.eq(project.getAdditionalRateUseful()))) {
                throw new BussinessException(ResourceUtils.get(LoanResource.INVEST_CANNOT_USE_RATECOUPON), BussinessException.TYPE_CLOSE);
            }
            UserRateCoupon rateCoupon = userRateCouponService.get(rateCouponId);
            if (rateCoupon == null) {
                throw new BussinessException(ResourceUtils.get(LoanResource.INVEST_RATECOUPON_NOT_EXISTS), BussinessException.TYPE_CLOSE);
            }
            if (!user.getUuid().equals(rateCoupon.getUserId())) {
                throw new BussinessException(ResourceUtils.get(LoanResource.INVEST_USE_OTHER_RATECOUPON), BussinessException.TYPE_CLOSE);
            }
            if (!rateCoupon.isAvailable(project, model.getAmount())) {
                throw new BussinessException(ResourceUtils.get(LoanResource.INVEST_USE_INVALID_RATECOUPON), BussinessException.TYPE_CLOSE);
            }
            model.setUpApr(rateCoupon.getUpApr());
            // 更新加息劵使用状态为已使用
            userRateCouponService.updateStatus(rateCouponId, model.getUuid(), OperateEnum.STATUS_USE.getValue(),
                    OperateEnum.STATUS_NO_USE.getValue());
        }

    }

    /**
     * 新建投资记录
     *
     * @param model   投资信息
     * @param user    投资用户
     * @param orderNo 投资订单号
     * @author FangJun
     * @date 2016年7月30日
     */
    protected void createInvestRecord(ProjectInvestModel model, User user, String orderNo) {
        ProjectInvest invest = model.prototype();
        invest.setInvestOrderNo(orderNo);
        invest.setStatus(InvestEnum.STATUS_NEW.getValue());
        invest.setCreateTime(DateUtils.getNow());
        invest.setMoney(invest.getAmount());
        invest.setRealAmount(model.getRealAmount());
        invest.setUserId(user.getUuid());
        invest.setRealizeFlag(InvestEnum.REALIZE_FLAG_NORMAL.getValue());
        invest.setBondFlag(InvestEnum.BOND_FLAG_NORMAL.getValue());
        invest.setInvestStyle(InvestEnum.INVEST_STYLE_NORMAL.getValue());
        invest.setRealizeAmount(BigDecimal.ZERO);
        invest.setRealizeInterest(BigDecimal.ZERO);
        invest.setFreezeInterest(BigDecimal.ZERO);
        invest.setFreezeCapital(BigDecimal.ZERO);
        invest.setPayment(BigDecimal.ZERO);
        invest.setInterest(BigDecimal.ZERO);
        invest.setRepayedAmount(BigDecimal.ZERO);
        invest.setRepayedInterest(BigDecimal.ZERO);
        invest.setWaitAmount(BigDecimal.ZERO);
        invest.setWaitInterest(BigDecimal.ZERO);
        invest.setWaitRaiseInterest(BigDecimal.ZERO);
        invest.setBorrowFlag(model.getBorrowFlag());
        invest.setInvestChannel(model.getInvestChannel());
        this.insert(invest);
    }

    /**
     * 封装UFX投资报文
     *
     * @param model   投资信息
     * @param project 投资项目
     * @param user    投资用户
     * @return UFX投资报文模型
     * @author FangJun
     * @date 2016年7月30日
     */
    protected UfxInvestModel createUfxInvestModel(ProjectInvestModel model, final Project project, final User user) {
        // 借款人
        User projectUser = userService.get(project.getUserId());
        // 传递参数
        Map<String, Object> map = new HashMap<String, Object>();

        map.put("projectId", project.getProjectNo());
        map.put("sponsorer", projectUser.getTppUserCustId());
        map.put("projectAmount", BigDecimalUtils.round(project.getAccount()));
        map.put("userCustId", user.getTppUserCustId());
        map.put("amount", BigDecimalUtils.round(model.getAmount()));
        map.put("voucher", BigDecimalUtils.round(model.getAmount().subtract(model.getRealAmount())));
        map.put("userId", user.getUuid());
        // 封装投资参数
        UfxInvestModel ufxModel = null;
        TppService tppService = (TppService) TppUtil.tppService();
        if (LoanEnum.REALIZE_FLAG_REALIZE.eq(project.getRealizeFlag())) {
            map.put("flag", "realize");
        }
        ufxModel = (UfxInvestModel) tppService.tppAddTender(map);
        LOGGER.debug("投资报文组装完毕,({})", ufxModel);
        return ufxModel;
    }


    @Override
    public void checkInvestAmount(final Project project, final ProjectInvestModel model, final BigDecimal remainAccount) {
        BigDecimal amount = model.getAmount();
        // 用户需要购买剩余可投金额的2种场景
        if (BigDecimalUtils.validAmount(project.getLowestAccount()) && amount.compareTo(remainAccount) != Constant.INT_ZERO) {
            // 1 最后一笔，投资金额 < 起投，用户投资金额 == 剩余可投
            if (project.getLowestAccount().compareTo(amount) > Constant.INT_ZERO) {
                throw new BussinessException(ResourceUtils.get(LoanResource.INVEST_AMOUNT_LESS_LOWEST),
                        BussinessException.TYPE_CLOSE);
            }
            // 起投金额2倍
            final BigDecimal doubleLowest = project.getLowestAccount().multiply(BigDecimal.valueOf(Constant.DOUBLE_TWO));
            // 2 剩余可投小于起投金额2倍，投资用户需要购买全部剩余可投
            if (remainAccount.compareTo(doubleLowest) < Constant.INT_ZERO) {
                throw new BussinessException(ResourceUtils.get(LoanResource.INVEST_NOT_EQ_REMAIN_AMOUNT),
                        BussinessException.TYPE_CLOSE);
            }

            //判断递增金额
            if (BigDecimalUtils.validAmount(project.getStepAccount()) &&
                    !BigDecimalUtils.modZero(amount.subtract(project.getLowestAccount()), project.getStepAccount())) {
                throw new BussinessException(ResourceUtils.get(LoanResource.INVEST_AMOUNT_NOT_MATCH_STEP),
                        BussinessException.TYPE_CLOSE);
            }
        }
        // 最高可投，初步判断
        if (BigDecimalUtils.validAmount(project.getMostAccount())) {
            if (project.getMostAccount().compareTo(amount) < Constant.INT_ZERO) {
                throw new BussinessException(ResourceUtils.get(LoanResource.INVEST_AMOUNT_GREATER_MOST),
                        BussinessException.TYPE_CLOSE);
            } else {
                // 最高可投金额，累计投资金额
                BigDecimal investedAmount = dao.countUserInvestProject(model);
                if (project.getMostAccount().compareTo(investedAmount.add(amount)) < Constant.INT_ZERO) {
                    throw new BussinessException(ResourceUtils.get(LoanResource.INVEST_AMOUNT_GREATER_MOST),
                            BussinessException.TYPE_CLOSE);
                }
            }
        }
    }

    /**
     * 查询用户在指定项目上累计投资金额
     *
     * @param model 用户投资信息（投资项目ID，用户ID）
     * @return 累计投资金额
     * @author FangJun
     * @date 2016年8月1日
     */
    public BigDecimal countUserInvestProject(ProjectInvestModel model) {
        return dao.countUserInvestProject(model);
    }

    /**
     * 校验：项目、用户可投情况
     *
     * @param project 投资项目
     * @param user    当前投资用户
     */
    protected void checkInvestInfo(final Project project, final User user) {
        // 校验投资人准备情况（是否开户等）
        UserIdentify identify = userIdentifyService.findByUserId(user.getUuid());
        if (!Constant.FLAG_YES.equals(identify.getMobilePhoneStatus())) {
            CacheUtils.del(CacheConstant.KEY_PREFIX_USER_IDENTIFY_USER_ID + user.getUuid());
            throw new BussinessException(ResourceUtils.get(ResourceConstant.USER_MOBILE_NOT_AUTH), BussinessException.TYPE_CLOSE);
        }
        if (!Constant.FLAG_YES.equals(identify.getRealNameStatus())) {
            CacheUtils.del(CacheConstant.KEY_PREFIX_USER_IDENTIFY_USER_ID + user.getUuid());
            throw new BussinessException(ResourceUtils.get(ResourceConstant.USER_TPP_STATUS_NOT_OPEN), BussinessException.TYPE_CLOSE);
        }
        if (userAuthSignLogService.checkAuthSign() && !Constant.FLAG_YES.equals(identify.getAuthSignStatus())) {
            CacheUtils.del(CacheConstant.KEY_PREFIX_USER_IDENTIFY_USER_ID + user.getUuid());
            throw new BussinessException(ResourceUtils.get(ResourceConstant.USER_AUTH_SIGN_STATUS), BussinessException.TYPE_CLOSE);
        }
        // 1.项目状态检查
        if (!LoanEnum.STATUS_RAISING.eq(project.getStatus())) {
            throw new ProjectException(ResourceUtils.get(LoanResource.PROJECT_NOT_RAISING), BussinessException.TYPE_CLOSE);
        }
        // 2. 用户投资功能是否被冻结
        UserFreezeService userFreezeService = (UserFreezeService) SpringContextHolder.getBean("userFreezeService");
        if (userFreezeService.isFreezed(user.getUuid(), UserFreezeModel.STATUS_USER_FREEZE_INVEST)) {
            throw new BussinessException(ResourceUtils.get(ResourceConstant.USER_FREEZE_INVEST_FREEZED), BussinessException.TYPE_CLOSE);
        }
        // 3.1 自己不能投自己发的借款
        if (project.getUserId().equals(user.getUuid())) {
            throw new ProjectException(ResourceUtils.get(LoanResource.INVEST_SELF_BORROW), BussinessException.TYPE_CLOSE);
        }
        // 4 开售时间校验
        if (project.getSaleTime() != null && DateUtils.getNow().before(project.getSaleTime())) {
            throw new ProjectException(ResourceUtils.get(LoanResource.INVEST_BEFORE_SALE_TIME), BussinessException.TYPE_CLOSE);
        }
        //5 募集结束时间校验
        if (project.getSaleTime() != null && project.getRaiseTimeLimit() != null && project.getRaiseTimeLimit() > Constant.INT_ZERO
                && DateUtils.getNow().after(DateUtils.rollDay(project.getSaleTime(), project.getRaiseTimeLimit()))) {
            throw new ProjectException(ResourceUtils.get(LoanResource.INVEST_AFTER_SALE_END), BussinessException.TYPE_CLOSE);
        }
        // 6 重复投资新手标
        if (CommonEnum.YES.eq(project.getNovice())) {
            UserCache userCache = userCacheService.findByUserId(user.getUuid());
            if (userCache.getInvestNum() != null && userCache.getInvestNum() > Constant.INT_ZERO) {
                throw new ProjectException(ResourceUtils.get(LoanResource.INVEST_NOVICE_AGAIN), BussinessException.TYPE_CLOSE);
            }
        }

    }

    @Override
    public Page<ProjectInvest> findRecordPage(ProjectInvest entity) {
        return this.findPage(entity);
    }

    @Override
    public void updateOriginalInvest(Realize realize, String flag) {
        dao.updateOriginalInvest(realize.getInvestId(), realize.getRealizeAmount().negate(), realize.getRealizeInterest().negate(), flag);
    }

    /**
     * 根据投资信息，计算预期利息
     *
     * @param invest 投资信息（投资项目，投资金额）
     * @return 预期利息
     * @author FangJun
     * @date 2016年7月29日
     */
    @Override
    public BigDecimal calcInterest(ProjectInvestModel invest) {
        // 校验输入
        checkInvestParams(invest);
        // 投资信息
        Project project = projectService.get(invest.getProjectId());
        if (project == null) {
            throw new BussinessException(ResourceUtils.get(LoanResource.PROJECT_NOT_EXISTS),
                    BussinessException.TYPE_JSON);
        }
        project.setReviewTime(DateUtils.getNow());
        ProjectWorker projectWorker = new ProjectWorker(project);
        BigDecimal addApr = project.getAddApr();
        if (!BigDecimalUtils.validAmount(addApr)) {
            addApr = BigDecimalUtils.validAmount(invest.getUpApr()) ? invest.getUpApr() : BigDecimal.ZERO;
        }

        InterestCalculator ic = projectWorker.interestCalculator(invest.getAmount(), project.getApr(), addApr, DateUtils.getNow());
        return ic.collectInterest();
    }

    @Override
    public Page<ProjectInvestModel> findAbleBondList(ProjectInvestModel model) {
        // 查出列表
        if (model.getPage() == null) {
            model.setPage(new Page<ProjectInvestModel>());
        }
        final List<ProjectInvestModel> list = dao.findAbleBondList(model);
        final List<ProjectInvestModel> newList = new ArrayList<ProjectInvestModel>();
        if (CollectionUtils.isNotEmpty(list)) {
            for (ProjectInvestModel projectInvestModel : list) {
                if (projectInvestModel.judge() || bondService.judgeBondAprLimit(projectInvestModel)) {//judgeBondAprLimit  此方法中录入剩余期限的值
                    continue;
                }
                projectInvestModel.setRaiseInterest(BigDecimalUtils.defaultIfNull(projectCollectionService.getNoRepayRaiseInterest(projectInvestModel.getUuid())));//加息利息--防止null值空值报错
                if (LoanEnum.STYLE_INSTALLMENT_REPAYMENT.eq(projectInvestModel.getRepayStyle())
                        || LoanEnum.STYLE_AVERAGE_CAPITAL_REPAYMENT.eq(projectInvestModel.getRepayStyle())) {//等额本息或者等额本金才需要扣除已还本金
                    final BigDecimal repayedCapital = BigDecimalUtils.defaultIfNull(bondService.getRepayedMoney(projectInvestModel));
                    projectInvestModel.setAmount(BigDecimalUtils.sub(BigDecimalUtils.defaultIfNull(projectInvestModel.getAmount()), repayedCapital));//录入已还本金
                    LOGGER.info("转让金额：{},已还本金：{}元", BigDecimalUtils.defaultIfNull(projectInvestModel.getAmount()), repayedCapital);
                }
                newList.add(projectInvestModel);
            }
            model.getPage().setRows(newList);
            model.getPage().setCount(newList.size());
        }
        return model.getPage();
    }

    public Project checkSpecificSale(ProjectInvestModel invest, User user) {
        Project project = projectService.get(invest.getProjectId());
        if (project == null) {
            throw new BussinessException(ResourceUtils.get(LoanResource.PROJECT_NOT_EXISTS),
                    BussinessException.TYPE_CLOSE);
        }
        Project model = new Project();
        model.setUuid(invest.getProjectId());
        model.setStatus(LoanEnum.STATUS_RAISING.getValue());
        //项目状态校验
        if (projectService.getCount(model) == Constant.INT_ZERO) {
            throw new BussinessException(ResourceUtils.get(LoanResource.PROJECT_STATUS_NEQ_RAISING), BussinessException.TYPE_CLOSE);
        }

        // 风险评估判断
        final UserCache userCache = userCacheService.findByUserId(user.getUuid());
        if (StringUtils.isBlank(userCache.getRiskLevel())) {
            throw new BussinessException(ResourceUtils.get(ResourceConstant.USER_RISK_LEVEL_IS_EMPTY), Constant.USER_RISK_PAPERS_URL, Constant.USER_RISK_PAPERS_CONTENT);
        } else if (StringUtils.isNotBlank(project.getRiskLevel())
                && (Integer.parseInt(project.getRiskLevel()) - Constant.INT_ONE > Integer.parseInt(userCache.getRiskLevel()))
                && (invest.getIsSelectedTip() == Constant.INT_ZERO)) {
            // 风险承受能力不足
            throw new BussinessException(ResourceUtils.get(LoanResource.INVEST_RISK_LEVEL_TOO_LOW),
                    BussinessException.TYPE_CLOSE);
        }

        if (!LoanEnum.SPECIFIC_SALE_NONE.eq(project.getSpecificSale())) {
            //校验定向密码
            if (LoanEnum.SPECIFIC_SALE_PASSWORD.eq(project.getSpecificSale()) && !invest.getPwdHasVerify()) {
                throw new ProjectException(ResourceUtils.get(LoanResource.SALE_PASS_ERROR), BussinessException.TYPE_CLOSE);
            } else {
                final String msg = validMailAndVip(user, project);
                if (StringUtils.isNotBlank(msg)) {
                    throw new ProjectException(msg, BussinessException.TYPE_CLOSE);
                }
            }
        }
        return project;
    }

    /**
     * 校验定向销售的邮箱校验 或VIP等级校验
     *
     * @param user    当前用户
     * @param project 投资项目
     * @return 校验错误信息
     * @author FangJun
     * @date 2016年9月27日
     */
    public String validMailAndVip(User user, Project project) {
        String msg = null;
        //校验邮箱
        if (LoanEnum.SPECIFIC_SALE_MAIL.eq(project.getSpecificSale()) && !StringUtils.isBlank(project.getSpecificSaleRule())) {
            if (StringUtils.isBlank(user.getEmail())) {
                msg = ResourceUtils.get(ResourceConstant.PROJECT_SALE_MAIL_MSG);
            } else {
                boolean eamilFlag = false;
                // 邮箱数组
                String emailText[] = project.getSpecificSaleRule().split(",");
                for (String email : emailText) {
                    if (StringUtils.endsWith(user.getEmail(), email)) {
                        eamilFlag = true;
                    }
                }
                if (!eamilFlag) {
                    msg = ResourceUtils.get(ResourceConstant.PROJECT_SALE_MAIL_MSG);
                }
            }
        } else if (LoanEnum.SPECIFIC_SALE_VIP_LEVEL.eq(project.getSpecificSale())) {
            //校验VIP
            UserVip userVip = userVipService.getUserVip(user.getUuid());
            int userVipLevel = 0;
            if (userVip != null) {
                userVipLevel = Integer.valueOf(userVip.getVipLevel());
            }
            if (Integer.valueOf(project.getSpecificSaleRule()) > userVipLevel) {
                msg = ResourceUtils.get(LoanResource.SALE_RULE_VIP_ERROR, project.getSpecificSaleRule());
            }
        }

        return msg;
    }

    @Override
    public int updateBondFlagAndInvestStyle(String investStyle, String bondFlag, String uuid) {
        return dao.updateBondFlagAndInvestStyle(investStyle, bondFlag, uuid);
    }

    @Override
    public BigDecimal sumInvest(ProjectInvest model) {
        BigDecimal total = dao.sumInvest(model);
        if (total == null) {
            total = BigDecimal.ZERO;
        }
        return total;
    }

    @Override
    public Page<ProjectInvestRecord> findPageForDetail(ProjectInvestRecord entity) {
        if (StringUtils.isBlank(entity.getProjectId())) {
            throw new BussinessException(ResourceUtils.get(LoanResource.PROJECT_ID_IS_NULL), BussinessException.TYPE_JSON);
        }
        entity.setStatusSet(new String[]{InvestEnum.STATUS_SUCCESS.getValue(), InvestEnum.STATUS_DELETE.getValue()});
        entity.setInvestStyle(InvestEnum.INVEST_STYLE_BUY.getValue());//不显示受让投资的  此处是不等于
        List<ProjectInvestRecord> investList = dao.findListForDetail(entity);
        for (ProjectInvestRecord invest : investList) {
            invest.setUserName(StringUtils.hideUserName(invest.getUserName()));
        }
        Page<ProjectInvestRecord> page = entity.getPage();
        page.setRows(investList);

        return page;
    }

    public boolean updateStatus(String uuid, String status, String preStatus) {
        int result = dao.updateStatus(uuid, status, preStatus);
        if (result == Constant.INT_ZERO) {
            String msg = "投资记录状态更新失败 [uuid=" + uuid + ", status=" + status + ", preStatus=" + preStatus + "]";
            LOGGER.error(msg);
            throw new BussinessException(msg);
        }
        return true;
    }

    @Override
    //@Transactional(readOnly = false)
    public void investTimeoutHandle() {
        int minutes = ConfigUtils.getInt(ConfigConstant.INVEST_TIMEOUT, ConfigConstant.INVEST_DEFAULT_ORDER_TIME_OUT);
        if (minutes < Constant.INT_ONE) {
            throw new BussinessException(ResourceUtils.get(LoanResource.INVEST_TIMEOUT_TIME_ERROR));
        }
        LOGGER.info("投资订单-超时处理,超时时长:{}分钟", minutes);
        final List<OutTimeProjectInvest> timeOutList = dao.getTimeOutInvest(minutes);
        final StringBuffer buffer = new StringBuffer();
        for (OutTimeProjectInvest invest : timeOutList) {
            // 拼接投资记录UUID
            buffer.append("'").append(invest.getUuid()).append("',");
        }
        if (buffer.length() > Constant.INT_ONE) {
            LOGGER.info("投资订单-超时处理 订单ID:{}", buffer.toString());
            final int updateNum = dao.investTimeoutHandle(buffer.substring(0, buffer.length() - 1), InvestEnum.STATUS_TIMEOUT.getValue(),
                    InvestEnum.STATUS_NEW.getValue());
            if (updateNum != CollectionUtils.size(timeOutList)) {
                dao.investTimeoutHandle(buffer.substring(0, buffer.length() - 1), InvestEnum.STATUS_NEW.getValue(),
                        InvestEnum.STATUS_TIMEOUT.getValue());
                LOGGER.error(ResourceUtils.get(LoanResource.INVEST_TIMEOUT_HANDLE_STATUS_ERROR));
            } else {
                for (OutTimeProjectInvest invest : timeOutList) {
                    itemInvestTimeoutHandle(invest);
                }
            }
        }
    }

    /**
     * 投资超时处理
     *
     * @param invest
     */
    private void itemInvestTimeoutHandle(OutTimeProjectInvest invest) {
        try {
            // 1退回各项目的可投金额
            double remainAmount = ProjectCache.incrRemainAccount(invest.getProjectId(), invest.getAmount().doubleValue());
            if (remainAmount == invest.getAmount().doubleValue()) {//退回之前无剩余可投，项目的投资阶段改为可投
                LOGGER.info("updateStage >>:投资超投处理");
                projectService.updateStage(LoanEnum.STAGE_SALE.getInt(), invest.getProjectNo());
            }
            // 2 退还红包、加息券
            this.backVoucher(invest.getUuid());
            // 2.2 在投记录-1
            userCacheService.subUserInvestNum(invest.getUserId());
            //3退还用户投资实际支付金额
            if (BigDecimalUtils.validAmount(invest.getRealAmount())) {
                this.handleFailInvesterAccount(invest.getAmount(), invest.getRealAmount(), invest.getUserId(), invest.getInvestOrderNo());
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw e;
        }

    }

    /**
     * 撤销项目，解冻未支付订单的本地资金
     *
     * @param projectId 项目UUID
     */
    public void cancelProjectHandleUnpay(String projectId) {
        final ProjectInvest investTemp = new ProjectInvest();
        investTemp.setStatus(InvestEnum.STATUS_REFUND.getValue());
        investTemp.setProjectId(projectId);
        List<ProjectInvest> unpayList = this.findList(investTemp);
        final StringBuffer buffer = new StringBuffer();
        for (ProjectInvest invest : unpayList) {
            // 拼接投资记录UUID
            buffer.append("'").append(invest.getUuid()).append("',");
        }
        if (buffer.length() > Constant.INT_ONE) {
            final Project project = projectService.get(projectId);
            LOGGER.info("投资订单---项目撤销处理--未支付订单ID:{}", buffer.toString());
            final int updateNum = dao.investTimeoutHandle(buffer.substring(0, buffer.length() - 1), InvestEnum.STATUS_FAIL.getValue(),
                    InvestEnum.STATUS_REFUND.getValue());
            if (updateNum != CollectionUtils.size(unpayList)) {
                throw new BussinessException(ResourceUtils.get(LoanResource.INVEST_TIMEOUT_HANDLE_STATUS_ERROR));
            } else {
                for (ProjectInvest invest : unpayList) {
                    // 1退回各项目的可投金额
                    ProjectCache.incrRemainAccount(project.getUuid(), invest.getAmount().doubleValue());


                    // 2 退还红包、加息券
                    this.backVoucher(invest.getUuid());
                    // 2.2 在投记录-1
                    userCacheService.subUserInvestNum(invest.getUserId());

                    //3退还用户投资实际支付金额
                    if (BigDecimalUtils.validAmount(invest.getRealAmount())) {
                        this.handleFailInvesterAccount(invest.getAmount(), invest.getRealAmount(), invest.getUserId(),
                                invest.getInvestOrderNo());
                    }
                }//for
            }//updateNum
        }
    }

    @Override
    public ProjectInvest getInvestByOrderNoAndBondFlag(String orderNo, String bondFlag) {
        return dao.getInvestByOrderNoAndBondFlag(orderNo, bondFlag);
    }

    @Override
    public Page<MyProjectInvestModel> myProjectInvestList(MyProjectInvestModel model) {
        if (model.getPage() == null) {
            model.setPage(new Page<MyProjectInvestModel>());
        }
        List<MyProjectInvestModel> investList = dao.getMyProjectInvestList(model);
        final int investExpireSeconds = ConfigUtils.getInt(ConfigConstant.INVEST_TIMEOUT) * 60;
        for (MyProjectInvestModel myList : investList) {
            if (Constant.FLAG_NO.equals(myList.getStatus())) {
                final int pastSeconds = (int) DateUtils.pastSeconds(myList.getCreateTime());
                final int remainTimes = investExpireSeconds - pastSeconds;
                if (remainTimes > 0) {
                    myList.setRemainTimes(remainTimes);
                } else {
                    myList.setStatus(InvestEnum.STATUS_TIMEOUT.getValue());
                }
            }
        }

        model.getPage().setRows(investList);
        return model.getPage();
    }

    /**
     * 退还指定投资的优惠劵（红包、加息券）
     *
     * @param investId 投资ID
     * @author FangJun
     * @date 2016年8月19日
     */
    @Override
    public void backVoucher(String investId) {
        //2 退还使用的加息劵、红包
        UserRedenvelope userRedenvelopeModel = new UserRedenvelope();
        userRedenvelopeModel.setTenderId(investId);
        List<UserRedenvelope> userRedenvelopeList = userRedenvelopeService.findList(userRedenvelopeModel);
        if (CollectionUtils.isNotEmpty(userRedenvelopeList)) {
            final StringBuffer redBuffer = new StringBuffer();
            for (UserRedenvelope red : userRedenvelopeList) {
                redBuffer.append("'").append(red.getUuid()).append("',");
            }
            // 更新红包使用状态
            int updateNum = userRedenvelopeService.updateStatusBatch(redBuffer.substring(0, redBuffer.length() - 1), null,
                    OperateEnum.STATUS_NO_USE.getValue(), OperateEnum.STATUS_USE.getValue());
            if (updateNum != userRedenvelopeList.size()) {
                throw new BussinessException(ResourceUtils.get("core.redEnvelope.useError"), BussinessException.TYPE_JSON);
            }
        }

        // 加息劵退还
        UserRateCoupon userRateCouponModel = new UserRateCoupon();
        userRateCouponModel.setTenderId(investId);
        List<UserRateCoupon> userRateCouponList = userRateCouponService.findList(userRateCouponModel);
        if (CollectionUtils.isNotEmpty(userRateCouponList)) {
            if (userRateCouponList.size() > Constant.INT_ONE) {
                throw new BussinessException(ResourceUtils.get("core.rateCoupon.useError"), BussinessException.TYPE_JSON);
            }
            final String userRateCouponId = userRateCouponList.get(0).getUuid();
            userRateCouponService.updateStatus(userRateCouponId, null, OperateEnum.STATUS_NO_USE.getValue(), OperateEnum.STATUS_USE.getValue());
        }
    }


    @Override
    public Object doPay(final ProjectInvestModel investModel) {
        // 校验用户投资再支付信息，返回投资记录
        final ProjectInvest oldInvest = checkPayInvest(investModel);
        // 将原有订单置为失效
        updateStatus(oldInvest.getUuid(), InvestEnum.STATUS_CANCEL.getValue(), InvestEnum.STATUS_NEW.getValue());
        // 已添加缓存支持
        final User user = userService.get(investModel.getUserId());
        // 原有属性重新封装
        final ProjectInvestModel newModel = oldInvest.prototype();
        // 生成投资ID,给加加息劵、红包使用
        newModel.setUuid(IdGen.uuid());
        final Project project = projectService.get(newModel.getProjectId());
        // 封装第三方投资报文
        String tppName = ConfigUtils.getTppName();
        String orderNo = "";
        Object object = null;
        if (TppServiceEnum.CHINAPNR.getName().equals(tppName)) {
            object = createUfxInvestModel(newModel, project, user);
            orderNo = ((UfxInvestModel) object).getOrderNo();
        } else if (TppServiceEnum.JXBANK.getName().equals(tppName)) {
            object = createJxBidApplyModel(newModel, project, user);
            orderNo = ((JxBidApplyModel) object).getOrderId();
        }
        // 新建投资记录(投资订单)
        createNewInvest(newModel, orderNo);
        // 更新红包加息卷使用
        userRedenvelopeService.updateRedenvelopeTenderId(oldInvest.getUuid(), newModel.getUuid());
        userRateCouponService.updateRateCouponTenderId(oldInvest.getUuid(), newModel.getUuid());

        return object;
    }

    /**
     * 校验用户投资再支付信息，返回投资记录
     *
     * @param investModel
     * @return
     * @author fxl
     * @date 2016年9月28日
     */
    public ProjectInvest checkPayInvest(final ProjectInvestModel investModel) {
        if (investModel.getUuid() == null) {
            throw new BussinessException(ResourceUtils.get(LoanResource.PROJECT_ID_IS_NULL));
        }
        final ProjectInvest invest = dao.get(investModel.getUuid());
        if (invest == null) {
            throw new BussinessException(ResourceUtils.get(LoanResource.INVEST_NOT_FOUND));
        }
        if (!InvestEnum.STATUS_NEW.eq(invest.getStatus())) {
            throw new BussinessException(ResourceUtils.get(LoanResource.INVEST_STATUS_NOTRIGHT));
        }
        if (!investModel.getUserId().equals(invest.getUserId())) {
            throw new BussinessException(ResourceUtils.get(LoanResource.INVEST_USER_NOTRIGHT));
        }
        Project project = projectService.get(invest.getProjectId());
        if (project == null) {
            throw new BussinessException(ResourceUtils.get(LoanResource.PROJECT_NOT_EXISTS),
                    BussinessException.TYPE_CLOSE);
        }
        // 1.项目状态检查
        if (!LoanEnum.STATUS_RAISING.eq(project.getStatus())) {
            throw new ProjectException(ResourceUtils.get(LoanResource.PROJECT_NOT_RAISING), BussinessException.TYPE_CLOSE);
        }
        // 2. 用户投资功能是否被冻结
        UserFreezeService userFreezeService = (UserFreezeService) SpringContextHolder.getBean("userFreezeService");
        if (userFreezeService.isFreezed(invest.getUserId(), UserFreezeModel.STATUS_USER_FREEZE_INVEST)) {
            throw new BussinessException(ResourceUtils.get(ResourceConstant.USER_FREEZE_INVEST_FREEZED), BussinessException.TYPE_CLOSE);
        }
        // 汇付查询订单状态，有可能返回：投标记录不存在
      /*Map<String, Object> ufxMap = new HashMap<String, Object>();
        ufxMap.put("loanNo", invest.getInvestOrderNo());
		ufxMap.put("loanDate",DateUtils.dateStr7(invest.getCreateTime()));
    	ufxMap.put("transType", UfxQueryTransferModel.TRANS_TYPE_INVEST);
		UfxQueryTransferModel ufxModel;
		try {
			ufxModel = UfxHelper.queryTransfer(ufxMap);
			if (UfxConstant.UFX_CODE_SUCCESS.equals(ufxModel.getRespCode())) {
			     if("N".equals(ufxModel.getTransferMsg())){ 
			    	 //汇付支付成功，只是平台未收到回调   1 投标  I 初始  N成功 C 失败
			    		throw new BussinessException(ResourceUtils.get(LoanResource.INVEST_STATUS_NOTRIGHT));
			     }
			} 
		} catch (UfxException e) {
		  LOGGER.warn(e.getMessage(),e);
		} */

        return invest;
    }

    /**
     * 生成新的投资订单
     *
     * @param model
     * @param orderNo
     * @author fxl
     * @date 2016年8月24日
     */
    private void createNewInvest(ProjectInvestModel model, String orderNo) {
        ProjectInvest invest = model.prototype();
        invest.setInvestOrderNo(orderNo);
        invest.setStatus(InvestEnum.STATUS_NEW.getValue());
        this.insert(invest);
    }


    @Override
    public int refundByProject(String projectId) {
        return dao.refundByProject(projectId);
    }

    @Override
    public ProjectInvest getInvestByOriIdAndUuid(String oriId, String uuid) {
        return dao.getInvestByOriIdAndUuid(oriId, uuid);
    }

    @Override
    public void checkOrder(final Project project, final User user, final BigDecimal amount) {
        this.checkInvestInfo(project, user);
        final double remainAccount = ProjectCache.getRemainAccount(project.getUuid());
        final ProjectInvestModel model = new ProjectInvestModel();
        model.setProjectId(project.getUuid());
        model.setAmount(amount);
        this.checkInvestAmount(project, model, BigDecimal.valueOf(remainAccount));
    }

    @Override
    public void handleAllProtocol(final String projectId, final String projectInvestId) {
        final Project project = projectService.get(projectId);
        if (project == null) {
            throw new BussinessException(ResourceUtils.get(LoanResource.PROJECT_NOT_EXISTS));
        }
        if (projectInvestId.equals(projectId)) {
            final List<ProjectInvest> investList = dao.findSuccessInvestForProtocol(projectId);
            if (CollectionUtils.isNotEmpty(investList)) {
                for (final ProjectInvest pInvest : investList) {
                    protocolService.buildProtocol(pInvest.getUuid(), pInvest.getUserId());
                    protocolService.buildProtocol(pInvest.getUuid(), project.getUserId());
                }
            }
        } else {
            final ProjectInvest projectInvest = dao.get(projectInvestId);
            if (projectInvest == null) {
                throw new BussinessException(ResourceUtils.get(LoanResource.INVEST_NOT_FOUND));
            }
            protocolService.buildProtocol(projectInvestId, projectInvest.getUserId());
            protocolService.buildProtocol(projectInvestId, project.getUserId());
        }
    }

    @Override
    public List<ProjectInvest> findListByBondId(final String bondId) {
        return dao.findListByBondId(bondId);
    }

    @Override
    public List<ProjectInvest> findSuccessInvest(String projectId) {
        return dao.findSuccessInvest(projectId);
    }

    @Override
    public ProjectInvestModel getProjectInvestModel(String projectInvestId) {
        return dao.getProjectInvestModel(projectInvestId);
    }

    @Override
    public void updateRealizeFreeze(String investId, BigDecimal freezeCapital, BigDecimal freezeInterest) {
        dao.updateRealizeFreeze(investId, freezeCapital, freezeInterest);
    }

    @Override
    public List<ProjectInvest> findByInvestDate(String investDate) {
        return dao.findByInvestDate(investDate);
    }

    @Override
    @Transactional(readOnly = false)
    public boolean cbhbInvestCancle(ProjectInvestModel invest) {
        boolean flag = false;
        final User user = SessionUtils.getSessionUser();
        //投标取消 校验
        this.investCancleValid(invest);
        TppService tppService = (TppService) TppUtil.tppService();
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("PlaCustId", user.getTppUserCustId());
        params.put("FreezeId", invest.getFreezeNo());
        params.put("TransAmt", invest.getAmount());
        params.put("SmsCode", invest.getSmsCode());
        params.put("OldTransId", invest.getInvestNo());
        CbhbInvestCancleModel cancleModel = (CbhbInvestCancleModel) tppService.tppInvestCancle(params);
        if (CbhbConstant.CBHB_CODE_SUCCESS.equals(cancleModel.getRespCode())) {
            flag = true;
            //TODO rabbitMq  调用队列服务处理投资撤销
        }
        return flag;
    }

    @Override
    public List<Map> getNeedCreditEndList(String projectId) {
        return dao.getNeedCreditEndList(projectId);
    }

    @Override
    public List<ProjectInvest> findListAfterBond(String projectId) {
        return dao.findListAfterBond(projectId);
    }

    /**
     * 投标取消 校验
     *
     * @param invest
     * @author QianPengZhan
     * @date 2017年3月6日
     */
    private void investCancleValid(final ProjectInvestModel invest) {
        if (StringUtils.isBlank(invest.getProjectId())) {
            throw new CbhbException(ResourceUtils.get(LoanResource.PROJECT_NOT_EXISTS), BussinessException.TYPE_JSON);
        }
        if (StringUtils.isBlank(invest.getUuid())) {
            throw new CbhbException(ResourceUtils.get(LoanResource.INVEST_NOT_FOUND), BussinessException.TYPE_JSON);
        }
        if (StringUtils.isBlank(invest.getInvestNo())) {
            throw new CbhbException(ResourceUtils.get(CbhbResource.CBHB_RESOURCE_ERROR_REQUEST_MERBILLNO_EMPTY), BussinessException.TYPE_JSON);
        }
    }

    @Override
    @Transactional(readOnly = false)
    public CbhbBackInvestModel doCbhbInvest(ProjectInvestModel model) {
        long startTime = System.currentTimeMillis();
        checkInvestParams(model);
        if (StringUtils.isBlank(model.getUserId())) {
            throw new BussinessException(ResourceUtils.get(ResourceConstant.USER_NOT_LOGIN), BussinessException.TYPE_CLOSE);
        }
        User user = userService.get(model.getUserId());
        // 投资项目信息
        model.setIsSelectedTip(1);
        Project project = checkSpecificSale(model, user);
        model.setBorrowFlag(project.getBorrowFlag());
        // 校验投资信息
        checkInvestInfo(project, user);

        // 未支付订单数校验
        if (!InvestCache.incrUserInvestUnpay(user.getTppUserCustId())) {
            throw new BussinessException(StringUtils.format(ResourceUtils.get(LoanResource.INVEST_UNPAY_TOO_MUCH), ConfigUtils.getValue(ConfigConstant.INVEST_UNPAY_MAX)), BussinessException.TYPE_JSON, "/member/myInvest/list.html?tab=2", "查看详情");
        }
        // 超投校验,同时扣除投资金额
        double currentProjectAmount = InvestCache.checkOverInvest(project.getUuid(), model.getAmount().doubleValue());
        CbhbBackInvestModel cbhbModel = null;
        try {
            // 投资金额校验
            checkInvestAmount(project, model, model.getAmount().add(BigDecimal.valueOf(currentProjectAmount)));
            // 生成投资ID,给加加息劵、红包使用
            model.setUuid(IdGen.uuid());

            long validateEnd = System.currentTimeMillis();
            LOGGER.info("projectInvestService doInvest validate take time:{}ms,userId:{}", (validateEnd - startTime), model.getUserId());

            // 优惠劵信息校验
            checkVoucher(model, project, user);
            // 用户资金判断
            Account account = accountService.getMoneyByUserId(new AccountQueryModel(user.getUuid(), ConfigUtils.getValue(Constant.ACCOUNT_CODE)));
            if (account.getUseMoney().compareTo(model.getRealAmount()) < 0) {
                throw new BussinessException(ResourceUtils.get(AccountResource.ACCOUNT_USEMONEY_NOTENOUGHERROR),
                        BussinessException.TYPE_CLOSE);
            }

            // 封装UFX投资报文
            cbhbModel = createCbhbInvestModel(model, project, user);
            if (!CbhbConstant.CBHB_CODE_SUCCESS.equals(cbhbModel.getRespCode())) {
                throw new BussinessException("投资失败,原因:[" + cbhbModel.getRespDesc() + "]", BussinessException.TYPE_JSON);
            }

            // 新建投资记录(投资订单)
            ProjectInvest invest = createInvestRecordCbhb(model, user, cbhbModel.getMerBillNo());

            //冻结投资金额
            handleInvesterAccount(model.getAmount(), model.getRealAmount(), model.getUpApr(), user, cbhbModel.getMerBillNo(), project);

            //用户投资记录+1
            if (ConfigUtils.isOpenMq()) {
                RabbitUtils.addUserInvestNum(model.getUserId());
            } else {
                userCacheService.addUserInvestNum(model.getUserId());
            }

            // 投资成功之后处理流程
            if (CbhbConstant.CBHB_CODE_SUCCESS.equals(cbhbModel.getRespCode())) {
                LOGGER.info("渤海银行投资成功资金处理-------------订单号：" + cbhbModel.getMerBillNo());
                ufxInvestService.cbhbInvestSuccess(cbhbModel, invest);
            }

        } catch (Exception e) {
            // 投资失败，归还剩余可投金额
            ProjectCache.incrRemainAccount(project.getUuid(), model.getAmount().doubleValue());
            //回退用户未支付次数
            InvestCache.decrUserInvestUnpay(user.getTppUserCustId());
            throw e;
        } finally {
            long endTime = System.currentTimeMillis();
            LOGGER.info("projectInvestService doInvest take time:{}ms,userId:{}", (endTime - startTime), model.getUserId());
        }
        return cbhbModel;
    }
}