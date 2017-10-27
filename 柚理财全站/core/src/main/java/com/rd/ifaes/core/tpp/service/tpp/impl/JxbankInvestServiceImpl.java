package com.rd.ifaes.core.tpp.service.tpp.impl;

import com.rd.ifaes.common.annotation.CacheEvict;
import com.rd.ifaes.common.dict.*;
import com.rd.ifaes.common.exception.BussinessException;
import com.rd.ifaes.common.rabbit.RabbitUtils;
import com.rd.ifaes.common.rabbit.model.MqActPlanModel;
import com.rd.ifaes.common.util.BigDecimalUtils;
import com.rd.ifaes.common.util.DateUtils;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.common.util.TppUtil;
import com.rd.ifaes.common.util.resource.LoanResource;
import com.rd.ifaes.core.cache.InvestCache;
import com.rd.ifaes.core.cache.ProjectCache;
import com.rd.ifaes.core.cache.RealizeCache;
import com.rd.ifaes.core.core.constant.CacheConstant;
import com.rd.ifaes.core.core.constant.Constant;
import com.rd.ifaes.core.core.constant.MessageConstant;
import com.rd.ifaes.core.core.constant.MqConstant;
import com.rd.ifaes.core.core.sms.msg.BaseMsg;
import com.rd.ifaes.core.core.util.CacheUtils;
import com.rd.ifaes.core.core.util.ConfigUtils;
import com.rd.ifaes.core.core.util.ResourceUtils;
import com.rd.ifaes.core.project.domain.Project;
import com.rd.ifaes.core.project.domain.ProjectInvest;
import com.rd.ifaes.core.project.service.ProjectInvestService;
import com.rd.ifaes.core.project.service.ProjectService;
import com.rd.ifaes.core.statistic.service.StatisticUserService;
import com.rd.ifaes.core.tpp.model.jx.JxBidApplyModel;
import com.rd.ifaes.core.tpp.service.TppService;
import com.rd.ifaes.core.tpp.service.tpp.JxbankInvestService;
import com.rd.ifaes.core.tpp.util.JxConfig;
import com.rd.ifaes.core.user.domain.UserCache;
import com.rd.ifaes.core.user.service.UserCacheService;
import com.rd.ifaes.core.user.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service("jxbankInvestService")
public class JxbankInvestServiceImpl implements JxbankInvestService {

    private static final Logger LOGGER = LoggerFactory.getLogger(JxbankInvestServiceImpl.class);
    @Resource
    private ProjectService projectService;
    @Resource
    private ProjectInvestService projectInvestService;
    @Resource
    private PlatformTransactionManager transactionManager;
    @Resource
    private UserService userService;
    @Resource
    private UserCacheService userCacheService;
    @Resource
    private StatisticUserService statisticUserService;

    @Override
    public void investHandle(JxBidApplyModel jxModel) {
        String handleKey = String.format(CacheKeys.PREFIX_INVEST_ORDER_NO_HANDLE_NUM.getKey(), jxModel.getOrderId());
        if (CacheUtils.incr(handleKey, Constant.DOUBLE_ONE) > Constant.DOUBLE_ONE) {
            LOGGER.info("订单号：{} ----投资订单回调, 已处理", jxModel.getOrderId());
            return;
        }
        LOGGER.info("投资回调进入本地处理，订单号：{}，返回状态：{}", jxModel.getOrderId(), jxModel.getRetCode());
        CacheUtils.expire(handleKey, ExpireTime.ONE_MIN);

        if (StringUtils.isBlank(jxModel.getOrderId())) {
            throw new BussinessException(ResourceUtils.get(LoanResource.INVEST_ORDER_NO_NOT_EMPTY));
        }
        ProjectInvest cachedInvest = projectInvestService.getInvestByOrderNo(jxModel.getOrderId());
        Project pmodel = new Project();
        pmodel.setUuid(cachedInvest.getProjectId());
        pmodel.setStatus(LoanEnum.STATUS_RAISING.getValue());// 募集中

        if (InvestEnum.STATUS_TIMEOUT.eq(cachedInvest.getStatus())) {
            // 被定时器处理为超时
            if (JxConfig.SUCCESS.equals(jxModel.getRetCode())) {
                this.invalidInvestHandle(jxModel, cachedInvest.getUuid(), InvestEnum.STATUS_TIMEOUT.getValue()); // 超时-支付成功
            } else {
                // 超时-支付失败
                projectInvestService.updateStatus(cachedInvest.getUuid(), InvestEnum.STATUS_FAIL.getValue(),
                        InvestEnum.STATUS_TIMEOUT.getValue());
            }
        } else if (InvestEnum.STATUS_CANCEL.eq(cachedInvest.getStatus())) {
            if (JxConfig.SUCCESS.equals(jxModel.getRetCode())) {
                // 再支付，取消的前一订单 -支付成功
                this.invalidInvestHandle(jxModel, cachedInvest.getUuid(), InvestEnum.STATUS_CANCEL.getValue());
            } else {
                // 再支付，取消的前一订单 -支付失败
                projectInvestService.updateStatus(cachedInvest.getUuid(), InvestEnum.STATUS_FAIL.getValue(),
                        InvestEnum.STATUS_CANCEL.getValue());
            }
        } else {
            if (JxConfig.SUCCESS.equals(jxModel.getRetCode())) {
                if (projectService.getCount(pmodel) < Constant.INT_ONE) {
                    // 项目不在募集中状态（撤销、下架等处理），成功投资的视为超时处理
                    projectInvestService.updateStatus(cachedInvest.getUuid(), InvestEnum.STATUS_TIMEOUT.getValue(),
                            InvestEnum.STATUS_NEW.getValue());
                    if (ConfigUtils.isOpenMq()) {
                        RabbitUtils.investTimeoutJx(jxModel);
                    } else {
                        this.investTimeout(jxModel);
                    }
                } else { // 成功投资
                    if (ConfigUtils.isOpenMq()) {
                        RabbitUtils.investSuccessJx(jxModel);
                    } else {
                        this.investSuccess(jxModel);
                    }
                }
            } else {
                LOGGER.error("投标第三方处理失败，原因：{}-{}", jxModel.getRetCode(), jxModel.getRetMsg());
                // 未超时-支付失败
                projectInvestService.updateStatus(cachedInvest.getUuid(), InvestEnum.STATUS_FAIL.getValue(),
                        InvestEnum.STATUS_NEW.getValue());
                // 失败业务处理
                if (ConfigUtils.isOpenMq()) {
                    RabbitUtils.investPayFailJx(jxModel);
                } else {
                    this.investPayFail(jxModel);
                }
            }
        }
    }

    /**
     * 投资超时、取消-支付成功-回调处理
     *
     * @param jxModel   投资回调信息
     * @param investId  投资记录ID
     * @param preStatus 修改前状态
     */
    @Override
    @Transactional
    public void invalidInvestHandle(JxBidApplyModel jxModel, String investId, String preStatus) {
        LOGGER.info("投资回调处理 -超时/取消-orderNo={} ", jxModel.getOrderId());

        // 1更新投资记录
        final ProjectInvest investTemp = new ProjectInvest();
        investTemp.setUuid(investId);
        investTemp.setInvestNo(jxModel.getOrderId());
        investTemp.setInvestDate(DateUtils.getDateTime());
        investTemp.setFreezeNo(jxModel.getOrderId());
        projectInvestService.update(investTemp);

        // 用户未支付投资订单数 -1
        if (InvestEnum.STATUS_TIMEOUT.eq(preStatus) && !InvestCache.decrUserInvestUnpay(jxModel.getAccountId())) {
            LOGGER.error("投资成功回调-用户(userCustId:{} 未支付投资订单数-处理错误!", jxModel.getAccountId());
        }

        // 2 调用接口： 投资失败资金退回（investFail），新网撤销用户投资（解冻资金）预处理取消
        Map<String, Object> map = new HashMap<>();
        map.put("accountId", jxModel.getAccountId());
        map.put("txAmount", jxModel.getTxAmount());
        map.put("productId", jxModel.getProductId());
        map.put("orgOrderId", jxModel.getOrderId());
        TppService tppService = (TppService) TppUtil.tppService();
        tppService.tppInvestFail(map);

        LOGGER.info("投资回调业务处理 （超时、取消）-orderNo={} ----处理结束", jxModel.getOrderId());
    }

    /**
     * 投资超时后支付成功-业务处理
     * 1）投资记录修改（退款中）
     * 2）发送投资失败请求（InvestFail)
     * 3）回退优惠劵
     * 4）退回用户投资金额（本地）
     * 5）退回项目可投金额
     *
     * @param jxModel
     */
    @Override
    @Transactional
    public void investTimeout(JxBidApplyModel jxModel) {
        LOGGER.info("投资回调处理 -超时（非定时器处理）-orderNo={}----", jxModel.getOrderId());

        // 1更新投资记录
        ProjectInvest invest = projectInvestService.getInvestByOrderNo(jxModel.getOrderId());
        invest.setInvestNo(jxModel.getOrderId());
        invest.setInvestDate(DateUtils.getDateTime());
        invest.setFreezeNo(jxModel.getOrderId());
        invest.setStatus(InvestEnum.STATUS_REFUND.getValue());
        projectInvestService.update(invest);

        // 2 调用接口： investFail ,回调处理优惠劵、修改资金
        Map<String, Object> map = new HashMap<>();
        map.put("accountId", jxModel.getAccountId());
        map.put("txAmount", jxModel.getTxAmount());
        map.put("productId", jxModel.getProductId());
        map.put("orgOrderId", jxModel.getOrderId());
        TppService tppService = (TppService) TppUtil.tppService();
        tppService.tppInvestFail(map);

        //3 归项目还剩余可投金额 (redis)
        double investAmount = Double.valueOf(jxModel.getTxAmount());
        double remainAmount = ProjectCache.incrRemainAccount(jxModel.getProductId(), investAmount);
        if (remainAmount == investAmount) {//退回之前无剩余可投，项目的投资阶段改为可投
            LOGGER.info("updateStage >>:投资超时后支付成功");
            projectService.updateStage(LoanEnum.STAGE_SALE.getInt(), jxModel.getProductId());
        }
    }

    @Override
    @CacheEvict(keys = CacheConstant.KEY_PREFIX_PROJECT_LIST, interval = ExpireTime.ONE_SEC)
    public void investSuccess(JxBidApplyModel jxModel) {
        LOGGER.info("投资回调业务处理 （成功）: {} ", jxModel.getOrderId());

        ProjectInvest invest = projectInvestService.getInvestByOrderNo(jxModel.getOrderId());
        LOGGER.info("投资回调业务处理 （成功）,投资记录ID：{}, DB中状态：{} ", invest.getUuid(), invest.getStatus());

        TransactionDefinition definition = new DefaultTransactionDefinition();
        TransactionStatus status = transactionManager.getTransaction(definition);
        try {
            projectInvestService.updateStatus(invest.getUuid(), InvestEnum.STATUS_SUCCESS.getValue(),
                    InvestEnum.STATUS_NEW.getValue());
            // 1更新投资记录
            invest.setInvestNo(jxModel.getOrderId());
            invest.setInvestDate(DateUtils.dateStr7(new Date()));
            invest.setFreezeNo(jxModel.getOrderId());
            invest.setStatus(InvestEnum.STATUS_SUCCESS.getValue());
            invest.setAuthCode(jxModel.getAuthCode());
            projectInvestService.update(invest);

            // 判断是否执行发放首投奖励
            if (!ConfigUtils.firstInvestRewardInVerify()) {
                // 用户首次投资逻辑处理
                UserCache userCache = userCacheService.findByUserId(invest.getUserId());
                if (!CommonEnum.YES.eq(StringUtils.isNull(userCache.getFirstAwardFlag()))
                        && userCacheService.userFirstInvest(invest.getUserId()) > 0) {
                    invest.setUserFirstInvest(CommonEnum.YES.getValue());
                    LOGGER.info("进入首投,userId = {}", invest.getUserId());
                    // 首投活动入队列
                    MqActPlanModel actModel = new MqActPlanModel(MqConstant.OPERATE_ACTPLAN_INVEST_FIRST,
                            userService.get(invest.getUserId()), invest, null);
                    RabbitUtils.actPlan(actModel);
                }
                // 标记用户为投资人
                statisticUserService.updateUserType(invest.getUserId(), UserEnum.USER_TYPE_INVESTOR.getValue());
            }

            // 投资成功- 处理项目已投、投资进度
            this.handleProjectAmount(invest);

            transactionManager.commit(status);
        } catch (Exception e) {
            transactionManager.rollback(status);
            throw e;
        }
        // 用户未支付投资订单数 -1
        if (!InvestCache.decrUserInvestUnpay(jxModel.getAccountId())) {
            LOGGER.error("投资成功回调-用户(UserCustId：{})未支付投资订单数-处理错误!", jxModel.getAccountId());
        }

        Project project = projectService.get(invest.getProjectId());
        // 发送通知
        try {
            HashMap<String, Object> params = new HashMap<String, Object>();
            params.put("user", userService.get(invest.getUserId()));
            params.put("investMoney", invest.getRealAmount().doubleValue());
            params.put("investTime", DateUtils.dateStr4(invest.getCreateTime()));
            params.put("projectName", project.getProjectName().length() > 10
                    ? project.getProjectName().substring(0, 10) + "..." : project.getProjectName());
            BaseMsg baseMsg = new BaseMsg(MessageConstant.INVEST, params);
            baseMsg.doEvent();
        } catch (Exception e) {
            LOGGER.error("给投资人发送投资消息处理失败，investId:{}", invest.getUuid(), e);
        }

        if (ProjectCache.getRemainAccount(project.getUuid()) == 0) {
            LOGGER.info("updateStage >>:投资回调处理");
            projectService.updateStage(LoanEnum.STAGE_FULL.getInt(), project.getProjectNo());
        }
        // 即息理财-开始放款
        if (project.isInterestFinancing() && ConfigUtils.supportInterestFinancing()) {
            projectService.handleInvestLoan(invest);
        }

        LOGGER.info("投资回调业务处理 （investSuccess）  {} ----处理结束", jxModel.getOrderId());

    }

    @Override
    @Transactional
    public void investPayFail(JxBidApplyModel jxModel) {
        ProjectInvest cachedInvest = projectInvestService.getInvestByOrderNo(jxModel.getOrderId());
        // 1退还红包、加息券
        projectInvestService.backVoucher(cachedInvest.getUuid());
        // 1.2 在投记录-1
        userCacheService.subUserInvestNum(cachedInvest.getUserId());

        // 2 退还用户投资实际支付金额 (dubbo)
        if (BigDecimalUtils.validAmount(cachedInvest.getRealAmount())) {
            projectInvestService.handleFailInvesterAccount(cachedInvest.getAmount(), cachedInvest.getRealAmount(), cachedInvest.getUserId(), cachedInvest.getInvestOrderNo());
        }

        double investAmount = Double.valueOf(jxModel.getTxAmount());
        //3、归项目还剩余可投金额 (redis)
        double remainAmount = ProjectCache.incrRemainAccount(jxModel.getProductId(), investAmount);
        if (remainAmount == investAmount) {//退回之前无剩余可投，项目的投资阶段改为可投
            LOGGER.info("updateStage >>:投资支付失败");
            projectService.updateStage(LoanEnum.STAGE_SALE.getInt(), jxModel.getProductId());
        }
    }

    /**
     * 投资成功- 处理项目已投、投资进度
     *
     * @param model
     * @author FangJun
     * @date 2016年8月19日
     */
    private void handleProjectAmount(final ProjectInvest invest) {
        // 1.2 修改项目的已投金额，投资进度
        //递增修改项目已投
        projectService.updateAccountYes(invest.getAmount().doubleValue(), invest.getProjectId());
        Project project = projectService.getProjectByUuid(invest.getProjectId());

        //清理项目详情缓存
        CacheUtils.del(CacheConstant.KEY_PREFIX_PROJECT_DETAIL + project.getUuid());

        // 判断是否满标，修改项目状态
        if (project.getAccount().compareTo(project.getAccountYes()) == Constant.INT_ZERO) {
            //更新募集结束时间
            final Project projectTemp = new Project();
            projectTemp.setUuid(project.getUuid());
            //即息理财-投满 显示-直接还款中
            if (project.isInterestFinancing() && ConfigUtils.supportInterestFinancing()) {
                projectTemp.setStatus(LoanEnum.STATUS_REPAY_START.getValue());
            } else {
                projectTemp.setStatus(LoanEnum.STATUS_RAISE_FINISHED.getValue());
            }
            projectTemp.setRaiseEndTime(DateUtils.getNow());
            projectTemp.setStage(LoanEnum.STAGE_FULL.getInt());//满标
            projectService.update(projectTemp);

            //满标，清理所有项目列表缓存
            CacheUtils.batchDel(CacheConstant.KEY_PREFIX_PROJECT_LIST);
        }
        // 变现借款删除详情缓存
        if (LoanEnum.REALIZE_FLAG_REALIZE.eq(project.getRealizeFlag())) {
            RealizeCache.delRealizeAndList(project.getUuid());
        }
    }

}
