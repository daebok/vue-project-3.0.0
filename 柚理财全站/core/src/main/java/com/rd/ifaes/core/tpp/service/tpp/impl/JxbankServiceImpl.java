package com.rd.ifaes.core.tpp.service.tpp.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.rd.account.domain.AccountLog;
import com.rd.account.log.AccountLogback;
import com.rd.account.model.AccountModel;
import com.rd.account.service.AccountService;
import com.rd.ifaes.common.constant.JxbankConstant;
import com.rd.ifaes.common.dict.CacheKeys;
import com.rd.ifaes.common.dict.ExpireTime;
import com.rd.ifaes.common.dict.InvestEnum;
import com.rd.ifaes.common.dict.LoanEnum;
import com.rd.ifaes.common.dict.OperateEnum;
import com.rd.ifaes.common.dict.TppEnum;
import com.rd.ifaes.common.orm.Page;
import com.rd.ifaes.common.rabbit.RabbitUtils;
import com.rd.ifaes.common.rabbit.model.MqAutoInvestModel;
import com.rd.ifaes.common.util.BigDecimalUtils;
import com.rd.ifaes.common.util.DateUtils;
import com.rd.ifaes.common.util.SessionUtils;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.common.util.TppUtil;
import com.rd.ifaes.core.account.domain.Cash;
import com.rd.ifaes.core.account.service.CashService;
import com.rd.ifaes.core.cache.ProjectCache;
import com.rd.ifaes.core.core.constant.CacheConstant;
import com.rd.ifaes.core.core.constant.Constant;
import com.rd.ifaes.core.core.constant.MessageConstant;
import com.rd.ifaes.core.core.constant.ResourceConstant;
import com.rd.ifaes.core.core.sms.msg.BaseMsg;
import com.rd.ifaes.core.core.util.CacheUtils;
import com.rd.ifaes.core.core.util.ConfigUtils;
import com.rd.ifaes.core.core.util.LogTemplateUtils;
import com.rd.ifaes.core.core.util.OrderNoUtils;
import com.rd.ifaes.core.core.util.ResourceUtils;
import com.rd.ifaes.core.interest.InterestCalculator;
import com.rd.ifaes.core.operate.service.UserRateCouponService;
import com.rd.ifaes.core.operate.service.UserRedenvelopeService;
import com.rd.ifaes.core.project.domain.Project;
import com.rd.ifaes.core.project.domain.ProjectInvest;
import com.rd.ifaes.core.project.service.ProjectInvestService;
import com.rd.ifaes.core.project.service.ProjectService;
import com.rd.ifaes.core.project.worker.ProjectWorker;
import com.rd.ifaes.core.statistic.service.StatisticUserService;
import com.rd.ifaes.core.tpp.domain.TppTrade;
import com.rd.ifaes.core.tpp.model.jx.JxBatchBailRepayModel;
import com.rd.ifaes.core.tpp.model.jx.JxPasswordResetPlusModel;
import com.rd.ifaes.core.tpp.model.jx.JxPasswordSetModel;
import com.rd.ifaes.core.tpp.model.jx.JxWithdrawModel;
import com.rd.ifaes.core.tpp.model.jx.json.JxAccountDetailsQuerySubPacksModel;
import com.rd.ifaes.core.tpp.model.jx.json.JxBailRepaySubPacksModel;
import com.rd.ifaes.core.tpp.model.jx.json.JxBatchDetailsQuerySubPacksModel;
import com.rd.ifaes.core.tpp.model.jx.json.JxBatchVoucherDetailsQuerySubPacksModel;
import com.rd.ifaes.core.tpp.service.TppService;
import com.rd.ifaes.core.tpp.service.tpp.JxbankService;
import com.rd.ifaes.core.tpp.service.tpp.TppTradeService;
import com.rd.ifaes.core.tpp.service.tpp.UfxLoanService;
import com.rd.ifaes.core.tpp.service.tpp.UfxRepayService;
import com.rd.ifaes.core.tpp.util.JxConfig;
import com.rd.ifaes.core.user.domain.User;
import com.rd.ifaes.core.user.domain.UserAutoInvest;
import com.rd.ifaes.core.user.domain.UserCache;
import com.rd.ifaes.core.user.domain.UserInvestAutoLog;
import com.rd.ifaes.core.user.mapper.UserAutoInvestMapper;
import com.rd.ifaes.core.user.service.UserAuthSignLogService;
import com.rd.ifaes.core.user.service.UserCacheService;
import com.rd.ifaes.core.user.service.UserInvestAutoLogService;
import com.rd.ifaes.core.user.service.UserService;

@Service("jxbankService")
public class JxbankServiceImpl implements JxbankService {

    private static final Logger LOGGER = LoggerFactory.getLogger(JxbankServiceImpl.class);
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
    @Resource
    private TppTradeService tppTradeService;
    @Resource
    private UfxLoanService ufxLoanService;
    @Resource
    private UfxRepayService ufxRepayService;
    @Resource
    private CashService cashService;
    @Resource
    private transient UserRedenvelopeService userRedenvelopeService;
    @Resource
    private transient UserRateCouponService userRateCouponService;

    /**
     * 账户资金处理类
     */
    @Resource
    private transient AccountService accountService;
    @Resource
    private UserAutoInvestMapper userAutoInvestMapper;
    @Resource
    private UserInvestAutoLogService userInvestAutoLogService;
    @Resource
    private UserAuthSignLogService userAuthSignLogService;

    @Override
    public void LoanSuccess(TppTrade tppTrade) {
        List<TppTrade> list = tppTradeService.getListByTradeNoAndType(tppTrade.getTradeNo(), TppEnum.SERVICE_TYPE_LOAN.getValue(), TppEnum.STATUS_UNDO.getValue());
        for (TppTrade trade : list) {
            int updateNum = tppTradeService.callbackHandle(trade.getUuid(), TppEnum.STATUS_SUCEESS.getValue(), "success");
            if (updateNum == Constant.INT_ONE) {
                if (ConfigUtils.isOpenMq()) {
                    RabbitUtils.loan(trade);
                } else {
                    ufxLoanService.successLoanHandle(trade);
                }
            } else {
                LOGGER.warn("放款异步回调处理--更新状态失败,orderNo：{}", trade.getOrderNo());
            }
        }
        doLoanSuccessOtherHandle(tppTrade);
    }

    private void doLoanSuccessOtherHandle(TppTrade tppTrade) {
        //更新为已成功
        tppTradeService.updateStatus(tppTrade.getUuid(), TppEnum.STATUS_SUCEESS.getValue(), TppEnum.STATUS_UNDO.getValue());
    }

    @Override
    public String jxBatchDetailsHandle(TppTrade tppTrade) {
        String tppType = "";
        if (TppEnum.TPP_TYPE_BATCH_LOAN.getValue().equals(tppTrade.getTppType())) {
            tppType = TppEnum.TPP_TYPE_LOAN.getValue();//放款
        } else if (TppEnum.TPP_TYPE_BATCH_REPAY.getValue().equals(tppTrade.getTppType())) {
            tppType = TppEnum.TPP_TYPE_REPAY.getValue();//还款
        } else if (TppEnum.TPP_TYPE_BATCH_BAIL_REPAY.getValue().equals(tppTrade.getTppType())) {
            tppType = TppEnum.TPP_TYPE_BAIL_REPAY.getValue();//担保垫付
        } else if (TppEnum.TPP_TYPE_BATCH_CREDIT_END.getValue().equals(tppTrade.getTppType())) {
            tppType = TppEnum.TPP_TYPE_CREDIT_END.getValue();//结束债权
        } else if (TppEnum.TPP_TYPE_BATCH_REPAY_BAIL.getValue().equals(tppTrade.getTppType())) {
            tppType = TppEnum.TPP_TYPE_REPAY_BAIL.getValue();//还垫付款
        } else {
            return "无法查询该交易！";
        }
        int countUndo = tppTradeService.countByTradeNoAndType(tppTrade.getTradeNo(), tppType, TppEnum.STATUS_UNDO.getValue());
        if (countUndo == 0) {
            return "全部交易已处理，无需再查询！";
        }
        int pageNum = 1;
        int pageSize = 50;
        //查询批量交易详情
        batchDetailsQuery(tppTrade.getTradeNo().substring(0, 8), tppTrade.getTradeNo().substring(8), pageNum, pageSize);
        //如果全部成功 更新状态
        int countUnSuccess = tppTradeService.countUnSuccessByTradeNoAndType(tppTrade.getTradeNo(), tppType);
        if (countUnSuccess == 0) {
            //全部都成功了
            if (tppType.equals(TppEnum.TPP_TYPE_LOAN.getValue())) {
                doLoanSuccessOtherHandle(tppTrade);
            } else if (tppType.equals(TppEnum.TPP_TYPE_REPAY.getValue()) || tppType.equals(TppEnum.TPP_TYPE_BAIL_REPAY.getValue())) {
                doRepaySuccessOtherHandle(tppTrade);
            } else if (tppType.equals(TppEnum.TPP_TYPE_CREDIT_END.getValue())) {
                //更新为已成功
                tppTradeService.updateStatus(tppTrade.getUuid(), TppEnum.STATUS_SUCEESS.getValue(), TppEnum.STATUS_UNDO.getValue());
            } else if (tppType.equals(TppEnum.TPP_TYPE_REPAY_BAIL.getValue())) {
                doRepayBailSuccessOtherHandle(tppTrade);
            }
        }
        return null;
    }

    private void batchDetailsQuery(String batchTxDate, String batchNo, int pageNum, int pageSize) {
        Map<String, Object> params = new HashMap<>();
        params.put("batchTxDate", batchTxDate);
        params.put("batchNo", batchNo);
        params.put("type", "0");
        params.put("pageNum", pageNum);
        params.put("pageSize", pageSize);
        TppService tppService = (TppService) TppUtil.tppService();
        //noinspection unchecked
        Map<String, Object> map = (Map<String, Object>) tppService.batchDetailsQuery(params);
        int totalItems = Integer.valueOf(StringUtils.isNull(map.get("totalItems")));
        List<JxBatchDetailsQuerySubPacksModel> list = JSON.parseArray(StringUtils.isNull(map.get("subPacks")), JxBatchDetailsQuerySubPacksModel.class);
        if (CollectionUtils.isEmpty(list)) {
            LOGGER.warn("subPacks转换失败！");
            return;
        }
        for (JxBatchDetailsQuerySubPacksModel packsModel : list) {
            TppTrade trade = tppTradeService.findByOrderNo(packsModel.getOrderId());
            if (trade == null || TppEnum.STATUS_SUCEESS.getValue().equals(trade.getStatus())) {
                continue;
            }
            int updateNum = 0;
            String status = packsModel.getTxState();
            if ("S".equals(status)) {
                updateNum = tppTradeService.callbackHandle(trade.getUuid(), TppEnum.STATUS_SUCEESS.getValue(), status);
                if (updateNum == Constant.INT_ONE) {
                    if (TppEnum.SERVICE_TYPE_LOAN.getValue().equals(trade.getServiceType())) {
                        if (ConfigUtils.isOpenMq()) {
                            RabbitUtils.loan(trade);
                        } else {
                            ufxLoanService.successLoanHandle(trade);
                        }
                    } else if (TppEnum.SERVICE_TYPE_REPAY.getValue().equals(trade.getServiceType())) {
                        if (ConfigUtils.isOpenMq()) {
                            RabbitUtils.repay(trade);
                        } else {
                            ufxRepayService.successRepayHandle(trade);
                        }
                    } else if (TppEnum.SERVICE_TYPE_BAIL_REPAY.getValue().equals(trade.getServiceType())) {
                        tppTradeService.bailRepaySuccessHandle(trade.getUuid(), packsModel.getAuthCode());
                        if (ConfigUtils.isOpenMq()) {
                            RabbitUtils.repay(trade);
                        } else {
                            ufxRepayService.successRepayHandle(trade);
                        }
                    } else if (TppEnum.SERVICE_TYPE_REPAY_BAIL.getValue().equals(trade.getServiceType())) {
                        if (ConfigUtils.isOpenMq()) {
                            RabbitUtils.repayBail(trade);
                        } else {
                            ufxRepayService.successRepayBailHandle(trade);
                        }
                    }
                }
            } else if ("F".equals(status) || "C".equals(status)) {
                // 失败处理
                updateNum = tppTradeService.callbackHandle(trade.getUuid(), TppEnum.STATUS_FAIL.getValue(), packsModel.getFailMsg());
            }
            if (updateNum != Constant.INT_ONE) {
                LOGGER.warn("批次异步回调处理--更新状态失败,orderNo：{},status:{}", packsModel.getOrderId(), status);
            }
        }
        if (totalItems > (pageNum * pageSize)) {
            pageNum++;
            batchDetailsQuery(batchTxDate, batchNo, pageNum, pageSize);
        }
    }

    @Override
    public void resetPwd(JxPasswordResetPlusModel model) {
//		User user = userService.findByUserCustId(model.getAccountId());
//		if (user != null) {
//			user.setPayPwd("1");//已设置密码
//			userService.update(user);
//		}

    	if (JxConfig.SUCCESS.equals(model.getRetCode())) {
    		LOGGER.info("异步通知处理重置密码成功，{}", model.getRetMsg());
    	} else {
    		LOGGER.info("异步通知处理重置密码失败，{}", model.getRetMsg());
    	}
    }

    @Override
    public void setPwd(JxPasswordSetModel model) {
    	if (JxConfig.SUCCESS.equals(model.getRetCode())) {
    		LOGGER.info("异步通知处理密码设置成功,{}, accountId:{}", model.getRetMsg(), model.getAccountId());
            User user = userService.findByUserCustId(model.getAccountId());
            if (user != null) {
                user.setPayPwd("1");//已设置密码
                userService.update(user);
                SessionUtils.setSessionAttr(Constant.SESSION_USER, user);
            }
    	} else {
    		LOGGER.info("异步通知处理密码设置失败，{}", model.getRetMsg());
    	}
    }

    @Override
    public void doCash(JxWithdrawModel model) {
        LOGGER.info("取现进入异步回调处理，订单号OrderNo={}，回调参数：{}", model.getOrderNo(), model.toString());

        //重复处理判断(缓存标记) 投资回调处理 计数 失效时间： 1天
        final String handleKey = String.format(CacheKeys.PREFIX_CASH_TPP_HANDLE_NUM.getKey(),
                model.getOrderNo(), model.getTxCode(), model.getRetCode());
        if (CacheUtils.incr(handleKey, Constant.DOUBLE_ONE) > Constant.DOUBLE_ONE) {
            LOGGER.info("{}----提现订单已处理该状态", StringUtils.isNull(model.getOrderNo()));
            return;
        }
        // 更新失效时间
        CacheUtils.expire(handleKey, ExpireTime.ONE_HOUR);

        Cash dbCash = cashService.findByOrderNo(model.getOrderNo());
        if (dbCash == null) {
            LOGGER.info("订单{}不存在", model.getOrderNo());
            return;
        }

        String type = null;
        if (JxConfig.SUCCESS.equals(model.getRetCode())) {
            //更改提现状态
            dbCash.setStatus(Constant.CASH_STATUS_SUCCESS);
            if ("success".equals(model.getRetMsg())) {
            	dbCash.setRemark("提现成功");
            } else {
            	dbCash.setRemark(model.getRetMsg());
            }
            
            cashService.updateByTppResult(dbCash);

            type = Constant.CASH_SUCCESS;
        } else if (JxConfig.PROCESS.equals(model.getRetCode()) || JxConfig.TIME_OUT_ONE.equals(model.getRetCode())
                || JxConfig.TIME_OUT_TWO.equals(model.getRetCode()) || JxConfig.TIME_OUT_THREE.equals(model.getRetCode())
                || JxConfig.ERROR_ONE.equals(model.getRetCode()) || JxConfig.ERROR_TWO.equals(model.getRetCode())
                || JxConfig.ERROR_THREE.equals(model.getRetCode())) {
            // 更改提现状态
            dbCash.setStatus(Constant.CASH_STATUS_PROCESS);
            dbCash.setRemark(model.getRetMsg());
            cashService.updateByTppResult(dbCash);

            type = Constant.CASH_PROCCESS;
        } else {
            //更改提现状态
            dbCash.setStatus(Constant.CASH_STATUS_FAIL);
            dbCash.setRemark(new StringBuffer("错误代码：").append(model.getRetCode()).append(",错误信息").append(model.getRetMsg()).toString());
            cashService.updateByTppResult(dbCash);
            type = Constant.CASH_FAIL;
            /*----提现失败，返还提现需要手续费的金额和缓存中的平台垫付次数 start ----*/
            cashService.repayCashFeeAmount(dbCash);
            /*----提现失败，处理需要提现手续费金额的值和用户垫付次数 end ----*/
        }

        //扣除金额
        String paymentsType = "";                //收支类型
        BigDecimal money = dbCash.getMoney();      //交易金额
        BigDecimal useMoney = BigDecimal.ZERO;   //新增可用余额(可为负数)
        BigDecimal noUseMoney = BigDecimal.ZERO; //新增冻结金额(可为负数)

        //资金和日志处理
        final Map<String, Object> param = new HashMap<String, Object>();
        if (Constant.CASH_FAIL.equals(type)) { //提现审核失败，退还冻结金额
            LOGGER.info("提现资金与日志-------------失败:{}", dbCash.toString());

        } else { //提现成功，扣除可用金额
            LOGGER.info("提现资金与日志-------------成功:{}", dbCash.toString());
            //支出
            paymentsType = AccountLog.PAYMENTS_TYPE_EXPEND;
            //解冻金额
            useMoney = money.negate();
            noUseMoney = BigDecimal.ZERO;
            //参数
            param.put("amount", dbCash.getAmount());
            param.put("realAmount", dbCash.getRealAmount());
            param.put("cashFee", BigDecimalUtils.add(dbCash.getServFee()));

        }

        if (money.compareTo(BigDecimal.ZERO) > 0 && (Constant.CASH_SUCCESS.equals(type) || Constant.CASH_PROCCESS.equals(type))) {
            final String content = LogTemplateUtils.getAccountTemplate(type, param);
            final AccountLog accountLog = new AccountLog(ConfigUtils.getValue(Constant.ACCOUNT_CODE), type, dbCash.getUserId(), money,
                    null, paymentsType, dbCash.getCashNo(), dbCash.getOrderNo(), content);
            AccountModel accountModel = new AccountModel(dbCash.getUserId(), ConfigUtils.getValue(Constant.ACCOUNT_CODE), useMoney, noUseMoney);
            accountService.saveAccountChange(accountModel, accountLog);
        }

        if (Constant.CASH_SUCCESS.equals(type)) {
            // 提现成功发送消息
            try {
                HashMap<String, Object> params = new HashMap<String, Object>();
                params.put("user", userService.get(dbCash.getUserId()));
                params.put("amount", dbCash.getRealAmount().doubleValue());
                params.put("fee", BigDecimalUtils.add(dbCash.getServFee()).doubleValue());
                params.put("cashTime", DateUtils.getDateTime());
                BaseMsg baseMsg = new BaseMsg(MessageConstant.CASH_SUCC, params);
                baseMsg.doEvent();
            } catch (Exception e) {
                LOGGER.error("提现成功通知处理失败,{}", e);
            }
        } else if (Constant.CASH_FAIL.equals(type) || Constant.CASH_BANK_FAIL.equals(type)) {
            try {
                HashMap<String, Object> params = new HashMap<String, Object>();
                params.put("user", userService.get(dbCash.getUserId()));
                params.put("amount", money.doubleValue());
                params.put("cashTime", DateUtils.getDateTime());
                LOGGER.info("params:{}", params.toString());
                BaseMsg baseMsg = new BaseMsg(MessageConstant.CASH_FAIL, params);
                baseMsg.doEvent();
            } catch (Exception e) {
                LOGGER.error("提现失败通知处理失败,{}", e);
            }
        }

    }

    @Override
    @Transactional(readOnly = false)
    public boolean doAutoInvest(final MqAutoInvestModel model) {
        final Project project = projectService.getProjectByUuid(model.getProject().getUuid());
        // 执行自动投资
        ProjectInvest invest = doAutoInvest(model, project);
        //即息理财-开始放款
        if (invest != null && project.isInterestFinancing() && ConfigUtils.supportInterestFinancing()) {
            projectService.handleInvestLoan(invest);
        }
        return true;
    }

    @SuppressWarnings("unchecked")
    @Transactional(readOnly = false)
    public ProjectInvest doAutoInvest(final MqAutoInvestModel model, final Project base) {
        LOGGER.info("进入自动投资.................");
        final User user = model.getUser();
        final Project project = projectService.get(base.getUuid());

        BigDecimal tempAmount = model.getAmount();
        final String userId = user.getUuid();
        // 获取自动投资
        final UserAutoInvest autoInvest = userAutoInvestMapper.getAutoInvestByUserId(userId);
        autoInvest.setSortTime(DateUtils.getNow().getTime());
        final UserInvestAutoLog investLog = new UserInvestAutoLog(base.getUuid(), userId);
        // 剩余可投 小于 产品最低投资金额，跳出循环
        BigDecimal lowestAccount = base.getLowestAccount();
        // 自动投资时可投资额度小于两倍最底投资额度，直接排序到队尾
        if (tempAmount.compareTo(lowestAccount.multiply(BigDecimal.valueOf(Constant.DOUBLE_TWO))) < Constant.INT_ZERO) {
            // 更新auto时间
            userAutoInvestMapper.update(autoInvest);
            // 生成自动投资记录
            investLog.setInvestMoney(tempAmount);
            investLog.setStatus(InvestEnum.STATUS_FAIL.getValue());
            investLog.setRemark(ResourceUtils.get(ResourceConstant.AUTO_INVEST_REMAINACCOUNT_LTDOUBLELOWESTACCOUNT));
            userInvestAutoLogService.save(investLog);
            LOGGER.info("自动投资所剩额度已不足两倍最底投资额度");
            return null;
        }
        // 自动投标第三方调用
        Map<String, Object> retMap = null;
        // 查看自动投资调用是否正常，异常直接返回
        try {
            retMap = (Map<String, Object>) doAutoInvest(project, user, tempAmount);
        } catch (Exception e) {
            LOGGER.error("自动投资第三方调用错误, {}", e);
            LOGGER.error("auto Invest failed [investOrderNo={}, money={}]", retMap == null ? null : retMap.get("orderId"), tempAmount);
            return null;
        }
        String orderId = StringUtils.isNull(retMap.get("orderId"));
        // 处理投资人资金及更新产品信息
        doProjectAccount(tempAmount, model.getRealAmount(), model.getRedEnvelope(), model.getUpApr(), project, user, orderId);

        retMap.put("realAmount", model.getRealAmount());
        // 生成投资记录
        ProjectInvest invest = createInvest(retMap, tempAmount, user, project, model.getUserCache());

        // 更新auto时间
        userAutoInvestMapper.update(autoInvest);

        // 生成自动投资记录
        investLog.setInvestMoney(tempAmount);
        investLog.setStatus(InvestEnum.STATUS_SUCCESS.getValue());
        investLog.setRemark(ResourceUtils.get(ResourceConstant.AUTO_INVEST_SUCCESS));
        userInvestAutoLogService.save(investLog);

        // 更新红包使用状态为已使用
        if (model.getRedUuid() != null) {
            LOGGER.info("自动投资使用了红包.................{},更新红包状态", model.getRedUuid());
            int updateNum = userRedenvelopeService.updateStatusBatch("'" + model.getRedUuid() + "'", invest.getUuid(),
                    OperateEnum.STATUS_USE.getValue(), OperateEnum.STATUS_NO_USE.getValue());
            if (updateNum != 1) {
                LOGGER.error("自动投资使用了红包.................{},更新红包状态失败", model.getRedUuid());
//               throw new BussinessException(ResourceUtils.get(LoanResource.INVEST_USE_INVALID_REDENVELOPE), BussinessException.TYPE_CLOSE);
            }
        }

        // 更新加息劵使用状态为已使用
        if (model.getRateUuid() != null) {
            LOGGER.info("自动投资使用了加息券.................{},更新加息券状态", model.getRateUuid());
            userRateCouponService.updateStatus(model.getRateUuid(), invest.getUuid(), OperateEnum.STATUS_USE.getValue(),
                    OperateEnum.STATUS_NO_USE.getValue());
        }
        LOGGER.info("自动投资成功.................");
        return invest;
    }

    private Object doAutoInvest(final Project project, final User user, final BigDecimal amount) {
        final Map<String, Object> map = new HashMap<String, Object>();
        map.put("accountId", user.getTppUserCustId());
        map.put("txAmount", BigDecimalUtils.round(amount));
        map.put("productId", project.getUuid());
        map.put("frzFlag", JxbankConstant.JXBANK_FRZFLAG_FREEZE);
        map.put("contOrderId", userAuthSignLogService.getOrderNo("1", user.getUuid()));
        TppService tppService = (TppService) TppUtil.tppService();
        return tppService.tppAutoTender(map);//TODO
    }

    /**
     * 处理投资人资金及产品信息
     *
     * @param amount
     * @param project
     * @param accountYes
     * @param user
     * @author ZhangBiao
     * @date 2016年8月6日
     */
    private void doProjectAccount(final BigDecimal amount, BigDecimal realAmount, BigDecimal redEnvelope, BigDecimal upApr, final Project project, final User user, final String orderNo) {
        // 冻结投资人本金
        final Map<String, Object> remarkData = new HashMap<String, Object>(2);
        remarkData.put("amount", amount.doubleValue());
        remarkData.put("realAmount", realAmount == null ? (realAmount = amount).doubleValue() : realAmount.doubleValue());
        remarkData.put("info", projectService.getProjectInfo(project.getUuid(), project.getProjectName(), project.getRealizeFlag()));
        remarkData.put("redEnvelope", redEnvelope == null ? Constant.INT_ZERO : redEnvelope.doubleValue());
        remarkData.put("upApr", upApr != null ? upApr.doubleValue() : BigDecimal.ZERO);
        String remark = LogTemplateUtils.getAccountTemplate(Constant.INVEST_FREEZE, remarkData);
        final AccountLog log = new AccountLog(ConfigUtils.getValue("account_code"), Constant.INVEST_FREEZE, user.getUuid(), amount,
                remark);
        log.setToUser(ConfigUtils.getValue(Constant.ADMIN_ID));
        log.setPaymentsType(AccountLog.PAYMENTS_TYPE_REMAIN);
        log.setOrderNo(orderNo);
        accountService.saveAccountChange(
                new AccountModel(user.getUuid(), ConfigUtils.getValue("account_code"), realAmount.negate(), amount), log);//realAmount.negate() 修改，默认减去了红包使用的金额

        projectService.updateAccountYes(amount.doubleValue(), project.getUuid());
    }

    /**
     * 自动投资生成投资记录
     *
     * @param autoInvestModel
     * @param amount
     * @param user
     * @param project
     * @author ZhangBiao
     * @date 2016年8月6日
     */
    private ProjectInvest createInvest(final Map<String, Object> map, final BigDecimal amount,
                                       final User user, final Project project, final UserCache userCache) {
        final ProjectInvest invest = new ProjectInvest(map, amount);
        invest.setUserId(user.getUuid());
        invest.setProjectId(project.getUuid());
        invest.setBorrowFlag(project.getBorrowFlag());
        projectInvestService.insert(invest);

        // 保存第三方投资记录start
        AccountLogback.LOGGER.info("auto Invest successed [investOrderNo={}, money={}]", StringUtils.isNull(map.get("orderId")), amount);
        // 如果是T+n计息保存计息
        if (LoanEnum.INTEREST_STYLE_TN.eq(project.getInterestStyle())) {
            final ProjectWorker worker = new ProjectWorker(project);
            final InterestCalculator ic = worker.interestCalculator(amount, null, null, invest.getCreateTime());
            final BigDecimal repayTotal = ic.repayTotal().add(ic.repayInterest());
            invest.setPayment(repayTotal);
            invest.setInterest(ic.repayInterest());
            invest.setRaiseInterest(ic.raiseInterest());
            invest.setWaitAmount(repayTotal);
            invest.setWaitInterest(ic.collectInterest());
            invest.setWaitRaiseInterest(ic.raiseInterest());
            projectInvestService.update(invest);
        }
        //扣除可投金额
        ProjectCache.decrRemainAccount(project.getUuid(), amount.doubleValue());
        // 扣除自动投资可投
        ProjectCache.decrRemainAccount(project.getUuid() + CacheConstant.KEY_AUTOINVEST_REMAINACCOUNT, amount.doubleValue());
        // 判断是否满标，修改项目状态
        Project proj = projectService.getProjectByUuid(project.getUuid());
        if (proj.getAccount().compareTo(proj.getAccountYes()) == Constant.INT_ZERO) {
            //更新募集结束时间
            final Project projectTemp = new Project();
            projectTemp.setUuid(project.getUuid());
            projectTemp.setStage(LoanEnum.STAGE_FULL.getInt());
            projectTemp.setStatus(LoanEnum.STATUS_RAISE_FINISHED.getValue());
            projectTemp.setRaiseEndTime(DateUtils.getNow());
            projectService.update(projectTemp);

            //满标，清理所有项目列表缓存
            CacheUtils.batchDel(CacheConstant.KEY_PREFIX_PROJECT_LIST);
        }
        return invest;
    }

    @Override
    public void repaySuccess(TppTrade tppTrade) {
        List<TppTrade> list = tppTradeService.getListByTradeNoAndType(tppTrade.getTradeNo(), TppEnum.SERVICE_TYPE_REPAY.getValue(), TppEnum.STATUS_UNDO.getValue());
        for (TppTrade trade : list) {
            int updateNum = tppTradeService.callbackHandle(trade.getUuid(), TppEnum.STATUS_SUCEESS.getValue(), "success");
            if (updateNum == Constant.INT_ONE) {
                if (ConfigUtils.isOpenMq()) {
                    RabbitUtils.repay(trade);
                } else {
                    ufxRepayService.successRepayHandle(trade);
                }
            } else {
                LOGGER.warn("还款异步回调处理--更新状态失败,orderNo：{}", trade.getOrderNo());
            }
        }
        doRepaySuccessOtherHandle(tppTrade);
    }

    private void doRepaySuccessOtherHandle(TppTrade tppTrade) {
        //更新为已成功
        tppTradeService.updateStatus(tppTrade.getUuid(), TppEnum.STATUS_SUCEESS.getValue(), TppEnum.STATUS_UNDO.getValue());
        //结束全部债权
        if (ConfigUtils.isOpenMq()) {
            RabbitUtils.batchCreditEnd(tppTrade.getProjectId());
        } else {
            batchCreditEnd(tppTrade.getProjectId());
        }
    }

    @Override
    public String jxBatchVoucherDetailsHandle(TppTrade tppTrade) {
        if (!TppEnum.SERVICE_TYPE_BATCH_VOUCHER.getValue().equals(tppTrade.getTppType())) {
            return "无法查询该交易！";
        }
        int countUndo = tppTradeService.countByTradeNoAndType(tppTrade.getTradeNo(), TppEnum.SERVICE_TYPE_VOUCHER.getValue(), TppEnum.STATUS_UNDO.getValue());
        if (countUndo == 0) {
            return "全部交易已处理，无需再查询！";
        }
        int pageNum = 1;
        int pageSize = 50;
        //查询批量交易详情
        batchVoucherDetailsQuery(tppTrade.getTradeNo().substring(0, 8), tppTrade.getTradeNo().substring(8), pageNum, pageSize);
        //如果全部成功 更新状态
        int countUnSuccess = tppTradeService.countUnSuccessByTradeNoAndType(tppTrade.getTradeNo(), TppEnum.SERVICE_TYPE_VOUCHER.getValue());
        if (countUnSuccess == 0) {
            //全部都成功了
            tppTradeService.updateStatus(tppTrade.getUuid(), TppEnum.STATUS_SUCEESS.getValue(), TppEnum.STATUS_UNDO.getValue());
        }
        return null;
    }

    @Override
    public void repayVoucherSuccess(String tradeNo) {
        List<TppTrade> list = tppTradeService.getListByTradeNoAndType(tradeNo, TppEnum.SERVICE_TYPE_VOUCHER.getValue(), TppEnum.STATUS_UNDO.getValue());
        for (TppTrade trade : list) {
            int updateNum = tppTradeService.callbackHandle(trade.getUuid(), TppEnum.STATUS_SUCEESS.getValue(), "success");
            if (updateNum == Constant.INT_ONE) {
                if (ConfigUtils.isOpenMq()) {
                    RabbitUtils.repay(trade);
                } else {
                    ufxRepayService.successRepayHandle(trade);
                }
            } else {
                LOGGER.warn("批次红包发放异步回调处理--更新状态失败,orderNo：{}", trade.getOrderNo());
            }
        }
        tppTradeService.updateStatusByTradeNoAndType(tradeNo, TppEnum.SERVICE_TYPE_BATCH_VOUCHER.getValue(), TppEnum.STATUS_SUCEESS.getValue());
    }

    @Override
    public Page<JxAccountDetailsQuerySubPacksModel> accountDetailsQuery(Map<String, Object> params) {
        Page<JxAccountDetailsQuerySubPacksModel> page = new Page<>();
        TppService tppService = (TppService) TppUtil.tppService();
        //noinspection unchecked
        Map<String, Object> map = (Map<String, Object>) tppService.accountDetailsQuery(params);
        page.setCount(Integer.valueOf(StringUtils.isNull(map.get("totalItems"))));
        page.setPage(Integer.valueOf(StringUtils.isNull(map.get("pageNum"))));
        page.setPageSize(Integer.valueOf(StringUtils.isNull(map.get("pageSize"))));
        List<JxAccountDetailsQuerySubPacksModel> list = JSON.parseArray(StringUtils.isNull(map.get("subPacks")), JxAccountDetailsQuerySubPacksModel.class);
        if (CollectionUtils.isEmpty(list)) {
            LOGGER.warn("subPacks转换失败！");
        }
        page.setRows(list);
        return page;
    }

    @SuppressWarnings("unchecked")
    @Override
    @Transactional
    public String batchCreditEnd(String projectId) {
        Project project = projectService.get(projectId);
        if (("9".equals(project.getStatus()) || "91".equals(project.getStatus())) && project.getRealLastRepayTime() != null) {
            //全部还款已完成
            int countDo = tppTradeService.countByTypeAndProjectId(TppEnum.SERVICE_TYPE_BATCH_CREDIT_END.getValue(), projectId);
            if (countDo > 0) {
                LOGGER.info("已存在批量结束债权记录，请在调度任务中查询--projectId={}", projectId);
                return "已存在批量结束债权记录，请在调度任务中查询";
            }
            //判断存管方还款是否结束
    		int repayed = tppTradeService.countByTypeAndProjectIdAndStatus(TppEnum.SERVICE_TYPE_BATCH_REPAY.getValue(), projectId);
    		LOGGER.info("看放款是否到账:{}" , repayed > 0 ? "否" : "是");
    		if (repayed > 0 ) {
    			LOGGER.info("还款未结束，可能还款多次操作，前期还款进行了回调--projectId={}", projectId);
    			return "还款未结束，可能还款多次操作，前期还款进行了回调";
    		}
            
            LOGGER.info("批量结束债权--projectId={}", projectId);
            User projectUser = userService.get(project.getUserId());
            List<Map> list = projectInvestService.getNeedCreditEndList(project.getUuid());
            if (list != null && list.size() > 0) {
                List<TppTrade> tppTradeList = new ArrayList<>();
                Map<String, Object> jxMap = new HashMap<>();
                String batchNo = OrderNoUtils.getRandomStr(6);
                final Date now = DateUtils.getNow();
                String tradeNo = DateUtils.dateStr7(now).concat(batchNo);
                for (Map map : list) {
                    TppTrade tpp = new TppTrade(TppEnum.SERVICE_TYPE_CREDIT_END.getValue(), TppEnum.TPP_TYPE_CREDIT_END.getValue(),
                            projectUser.getUuid(), projectUser.getTppUserCustId(),
                            StringUtils.isNull(map.get("userId")), StringUtils.isNull(map.get("forAccountId")),
                            null, StringUtils.isNull(map.get("investId")));
                    tpp.setProjectId(project.getUuid());
                    tpp.setInvestProjectId(project.getUuid());
                    tpp.setTradeNo(tradeNo);
                    tpp.setTradeDate(now);
                    map.put("orderId", tpp.getOrderNo());
                    map.put("accountId", projectUser.getTppUserAccId());
                    map.put("productId", project.getUuid());
        			tpp.setParams(JSON.toJSONString(map));
                    tppTradeList.add(tpp);
                }
                jxMap.put("batchNo", batchNo);
                jxMap.put("txCounts", list.size());
                jxMap.put("subPacks", list);

                TppTrade tpp = new TppTrade(TppEnum.SERVICE_TYPE_BATCH_CREDIT_END.getValue(), TppEnum.TPP_TYPE_BATCH_CREDIT_END.getValue(),
                        projectUser.getUuid(), projectUser.getTppUserCustId(),
                        null, null, null, null);
                tpp.setProjectId(project.getUuid());
                tpp.setInvestProjectId(project.getUuid());
                tpp.setTradeNo(tradeNo);
                tpp.setTradeDate(now);
                tppTradeList.add(tpp);

                tppTradeService.insertBatch(tppTradeList);
                TppService tppService = (TppService) TppUtil.tppService();
                Map<String, Object> map = (Map<String, Object>) tppService.batchCreditEnd(jxMap);
                String received = StringUtils.isNull(map.get("received"));
                if (!"success".equals(received)) {
                    tppTradeService.updateStatusByTradeNo(tradeNo, TppEnum.STATUS_FAIL.getValue(), TppEnum.STATUS_UNDO.getValue(), "存管方接收失败！");
                    return "存管方接收失败！";
                }
            }
        }
        return null;
    }

    @Override
    public void creditEndSuccess(TppTrade tppTrade) {
        tppTradeService.updateStatusByTradeNoAndType(tppTrade.getTradeNo(), TppEnum.SERVICE_TYPE_CREDIT_END.getValue(), TppEnum.STATUS_SUCEESS.getValue());
        tppTradeService.updateStatusByTradeNoAndType(tppTrade.getTradeNo(), TppEnum.SERVICE_TYPE_BATCH_CREDIT_END.getValue(), TppEnum.STATUS_SUCEESS.getValue());
    }

    @Override
    public void bailRepayHandle(JxBatchBailRepayModel model) {
        TppTrade tpp = tppTradeService.findByTradeNoAndType(model.getTxDate().concat(model.getBatchNo()), TppEnum.SERVICE_TYPE_BATCH_BAIL_REPAY.getValue(), TppEnum.STATUS_UNDO.getValue());
        List<JxBailRepaySubPacksModel> list = JSON.parseArray(model.getSubPacks(), JxBailRepaySubPacksModel.class);
        if (CollectionUtils.isEmpty(list)) {
            LOGGER.warn("subPacks转换失败！");
            return;
        }
        for (JxBailRepaySubPacksModel packsModel : list) {
            TppTrade trade = tppTradeService.findByOrderNo(packsModel.getOrderId());
            if (trade == null || TppEnum.STATUS_SUCEESS.getValue().equals(trade.getStatus())) {
                continue;
            }
            int updateNum = tppTradeService.bailRepaySuccessHandle(trade.getUuid(), packsModel.getAuthCode());
            if (updateNum == Constant.INT_ONE) {
                if (ConfigUtils.isOpenMq()) {
                    RabbitUtils.repay(trade);
                } else {
                    ufxRepayService.successRepayHandle(trade);
                }
            } else {
                LOGGER.warn("担保代偿TppTrade更新状态失败,orderNo：{}", packsModel.getOrderId());
            }
        }
        int countUnSuccess = tppTradeService.countUnSuccessByTradeNoAndType(tpp.getTradeNo(), TppEnum.SERVICE_TYPE_BAIL_REPAY.getValue());
        if (countUnSuccess == 0) {
            //如果全部都成功了
            doRepaySuccessOtherHandle(tpp);
        } else {
            //对于没处理的已失败处理
            tppTradeService.updateStatusByTradeNo(tpp.getTradeNo(), TppEnum.STATUS_FAIL.getValue(), TppEnum.STATUS_UNDO.getValue(), "交易失败");
        }
    }

    @Override
    public void repayBailSuccess(TppTrade tppTrade) {
        List<TppTrade> list = tppTradeService.getListByTradeNoAndType(tppTrade.getTradeNo(), TppEnum.SERVICE_TYPE_REPAY_BAIL.getValue(), TppEnum.STATUS_UNDO.getValue());
        for (TppTrade trade : list) {
            int updateNum = tppTradeService.callbackHandle(trade.getUuid(), TppEnum.STATUS_SUCEESS.getValue(), "success");
            if (updateNum == Constant.INT_ONE) {
                if (ConfigUtils.isOpenMq()) {
                    RabbitUtils.repayBail(trade);
                } else {
                    ufxRepayService.successRepayBailHandle(trade);
                }
            } else {
                LOGGER.warn("还担保垫付款异步回调处理--更新状态失败,orderNo：{}", trade.getOrderNo());
            }
        }
        doRepayBailSuccessOtherHandle(tppTrade);
    }

    private void doRepayBailSuccessOtherHandle(TppTrade tppTrade) {
        //更新为已成功
        tppTradeService.updateStatus(tppTrade.getUuid(), TppEnum.STATUS_SUCEESS.getValue(), TppEnum.STATUS_UNDO.getValue());
        //结束全部债权 todo
//        if (ConfigUtils.isOpenMq()) {
//            RabbitUtils.batchCreditEnd(tppTrade.getProjectId());
//        } else {
//            batchCreditEnd(tppTrade.getProjectId());
//        }
    }

    private void batchVoucherDetailsQuery(String batchTxDate, String batchNo, int pageNum, int pageSize) {
        Map<String, Object> params = new HashMap<>();
        params.put("batchTxDate", batchTxDate);
        params.put("batchNo", batchNo);
        params.put("type", "0");
        params.put("pageNum", pageNum);
        params.put("pageSize", pageSize);
        TppService tppService = (TppService) TppUtil.tppService();
        //noinspection unchecked
        Map<String, Object> map = (Map<String, Object>) tppService.batchVoucherDetailsQuery(params);
        int totalItems = Integer.valueOf(StringUtils.isNull(map.get("totalItems")));
        List<JxBatchVoucherDetailsQuerySubPacksModel> list = JSON.parseArray(StringUtils.isNull(map.get("subPacks")), JxBatchVoucherDetailsQuerySubPacksModel.class);
        if (CollectionUtils.isEmpty(list)) {
            LOGGER.warn("subPacks转换失败！");
            return;
        }
        for (JxBatchVoucherDetailsQuerySubPacksModel packsModel : list) {
            TppTrade trade = tppTradeService.findByOrderNo(packsModel.getOrderId());
            if (trade == null || TppEnum.STATUS_SUCEESS.getValue().equals(trade.getStatus())) {
                continue;
            }
            int updateNum = 0;
            String status = packsModel.getTxState();
            if ("S".equals(status)) {
                updateNum = tppTradeService.callbackHandle(trade.getUuid(), TppEnum.STATUS_SUCEESS.getValue(), status);
                if (updateNum == Constant.INT_ONE) {
                    if (ConfigUtils.isOpenMq()) {
                        RabbitUtils.repay(trade);
                    } else {
                        ufxRepayService.successRepayHandle(trade);
                    }
                }
            } else if ("F".equals(status) || "C".equals(status)) {
                // 失败处理
                updateNum = tppTradeService.callbackHandle(trade.getUuid(), TppEnum.STATUS_FAIL.getValue(), packsModel.getFailMsg());
            }
            if (updateNum != Constant.INT_ONE) {
                LOGGER.warn("批次红包发放异步回调处理--更新状态失败,orderNo：{},status:{}", packsModel.getOrderId(), status);
            }
        }
        if (totalItems > (pageNum * pageSize)) {
            pageNum++;
            batchVoucherDetailsQuery(batchTxDate, batchNo, pageNum, pageSize);
        }
    }

}
