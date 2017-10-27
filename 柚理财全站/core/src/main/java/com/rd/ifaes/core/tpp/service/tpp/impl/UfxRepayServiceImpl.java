/**
 * 互联网金融交易系统ifaes (Internet Finance  Assets  Exchange  System)
 * Copyright (c) 2016 杭州融都科技股份有限公司 版权所有
 */
package com.rd.ifaes.core.tpp.service.tpp.impl;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.rd.account.domain.AccountLog;
import com.rd.account.model.AccountBatchModel;
import com.rd.account.model.AccountModel;
import com.rd.account.service.AccountService;
import com.rd.ifaes.common.constant.CbhbConstant;
import com.rd.ifaes.common.constant.UfxConstant;
import com.rd.ifaes.common.dict.InvestEnum;
import com.rd.ifaes.common.dict.LoanEnum;
import com.rd.ifaes.common.dict.OperateEnum;
import com.rd.ifaes.common.dict.ProjectCollectionEnum;
import com.rd.ifaes.common.dict.TppEnum;
import com.rd.ifaes.common.dict.UfxRepaymentModelEnum;
import com.rd.ifaes.common.exception.BussinessException;
import com.rd.ifaes.common.util.BigDecimalUtils;
import com.rd.ifaes.common.util.DateUtils;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.common.util.TppUtil;
import com.rd.ifaes.core.account.model.TppMerchantLogModel;
import com.rd.ifaes.core.account.service.TppMerchantLogService;
import com.rd.ifaes.core.bond.domain.Bond;
import com.rd.ifaes.core.bond.domain.BondInvest;
import com.rd.ifaes.core.bond.service.BondInvestService;
import com.rd.ifaes.core.bond.service.BondService;
import com.rd.ifaes.core.core.constant.ConfigConstant;
import com.rd.ifaes.core.core.constant.Constant;
import com.rd.ifaes.core.core.constant.MessageConstant;
import com.rd.ifaes.core.core.constant.MqConstant;
import com.rd.ifaes.core.core.constant.ResourceConstant;
import com.rd.ifaes.core.core.constant.URLConstant;
import com.rd.ifaes.core.core.sms.msg.BaseMsg;
import com.rd.ifaes.core.core.util.ConfigUtils;
import com.rd.ifaes.core.core.util.LogTemplateUtils;
import com.rd.ifaes.core.core.util.ResourceUtils;
import com.rd.ifaes.core.operate.domain.UserRedenvelope;
import com.rd.ifaes.core.operate.service.UserRedenvelopeService;
import com.rd.ifaes.core.project.domain.Project;
import com.rd.ifaes.core.project.domain.ProjectCollection;
import com.rd.ifaes.core.project.domain.ProjectInvest;
import com.rd.ifaes.core.project.domain.ProjectRepayment;
import com.rd.ifaes.core.project.domain.ProjectType;
import com.rd.ifaes.core.project.domain.RealizeFreeze;
import com.rd.ifaes.core.project.service.ProjectCollectionService;
import com.rd.ifaes.core.project.service.ProjectInvestService;
import com.rd.ifaes.core.project.service.ProjectRepaymentService;
import com.rd.ifaes.core.project.service.ProjectService;
import com.rd.ifaes.core.project.service.ProjectTypeService;
import com.rd.ifaes.core.project.service.RealizeFreezeService;
import com.rd.ifaes.core.project.service.RealizeService;
import com.rd.ifaes.core.tpp.domain.TppTrade;
import com.rd.ifaes.core.tpp.model.cbhb.CbhbExperCapModel;
import com.rd.ifaes.core.tpp.model.cbhb.CbhbFileRepaymentModel;
import com.rd.ifaes.core.tpp.model.jx.JxVoucherPayModel;
import com.rd.ifaes.core.tpp.model.ufx.UfxRepaymentModel;
import com.rd.ifaes.core.tpp.service.TppService;
import com.rd.ifaes.core.tpp.service.tpp.TppTradeService;
import com.rd.ifaes.core.tpp.service.tpp.UfxRepayService;
import com.rd.ifaes.core.tpp.util.JxConfig;
import com.rd.ifaes.core.user.domain.User;
import com.rd.ifaes.core.user.service.UserService;

/**
 * 还款异步处理
 *
 * @author FangJun
 * @version 3.0
 * @date 2016年10月24日
 */
@Service("ufxRepayService")
public class UfxRepayServiceImpl implements UfxRepayService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UfxRepayServiceImpl.class);

    @Resource
    private TppTradeService tppTradeService;
    @Resource
    private AccountService accountService;
    @Resource
    private UserService userService;
    @Resource
    private ProjectService projectService;
    @Resource
    private RealizeService realizeService;
    @Resource
    private ProjectInvestService projectInvestService;
    @Resource
    private ProjectCollectionService projectCollectionService;
    @Resource
    private TppMerchantLogService tppMerchantLogService;
    @Resource
    private RealizeFreezeService realizeFreezeService;
    @Resource
    private BondInvestService bondInvestService;
    @Resource
    private BondService bondService;
    @Resource
    private ProjectTypeService projectTypeService;
    @Resource
    private ProjectRepaymentService projectRepaymentService;
	@Resource
	private UserRedenvelopeService userRedenvelopeService;

    @Override
    @Transactional
    public void ufxRepayHandle(UfxRepaymentModel model) {
        LOGGER.info("还款异步回调处理,orderNo：{}", model.getOrderNo());
        final TppTrade trade = tppTradeService.findByOrderNo(model.getOrderNo());
        if (trade == null) {
            throw new BussinessException(ResourceUtils.get(ResourceConstant.GLOBAL_RECORD_NO_EXISTS));
        }
        int updateNum = 0;
        if (UfxConstant.UFX_CODE_SUCCESS.equals(model.getRespCode())) {
            updateNum = tppTradeService.callbackHandle(trade.getUuid(), TppEnum.STATUS_SUCEESS.getValue(), model.getRespDesc());
            if (updateNum == Constant.INT_ONE) {
                this.successRepayHandle(trade);
            }
        } else {
            // 失败处理
            updateNum = tppTradeService.callbackHandle(trade.getUuid(), TppEnum.STATUS_FAIL.getValue(), model.getRespDesc());
        }
        if (updateNum != Constant.INT_ONE) {
            LOGGER.warn("还款异步回调处理--更新状态失败,orderNo：{}", model.getOrderNo());
        }
    }

    /**
     * 获取项目还款备注
     *
     * @param trade
     * @param project
     * @return
     * @author QianPengZhan
     * @date 2016年12月2日
     */
    private String getRemarkInfo(final String repayType, final TppTrade trade, final Project project) {
        final StringBuffer infoBuffer = new StringBuffer();
        final ProjectType projectType = projectTypeService.get(project.getProjectTypeId());
        if (UfxRepaymentModelEnum.REPAY_TYPE_SIX.eq(StringUtils.isNull(repayType))) {//还款类型若为债权还款换为债权项目信息
            final String investId = StringUtils.isNull(trade.getInvestId());//投资ID信息
            final ProjectInvest invest = projectInvestService.get(investId);//原始标投资记录信息
            final BondInvest bondInvest = invest == null ? null : bondInvestService.getBondInvestByOrderNo(invest.getInvestOrderNo());//获取对应的债权投资信息
            final Bond bond = bondInvest == null ? null : bondService.get(bondInvest.getBondId());
            if (bond != null) {
                infoBuffer.append("【<a href=\"")
                        .append(URLConstant.BOND_DETAIL_PAGE_PREFIX).append(bond.getUuid()).append("\"  target=\"_blank\">")
                        .append(bond.getBondName()).append("</a>】债权");
            } else {
                infoBuffer.append("债权");
            }
        } else if (LoanEnum.REALIZE_FLAG_REALIZE.eq(project.getRealizeFlag())) {//变现
            infoBuffer.append("【<a href=\"")
                    .append(URLConstant.REALIZE_DETAIL_PAGE_PREFIX).append(project.getUuid()).append("\"  target=\"_blank\">")
                    .append(project.getProjectName()).append("</a>】");
        } else {
            infoBuffer.append("【<a href=\"")
                    .append(URLConstant.INVEST_DETAIL_PAGE_PREFIX).append(project.getUuid()).append("\"  target=\"_blank\">")
                    .append(projectType.getTypeName()).append(StringUtils.HYPHEN).append(project.getProjectNo()).append("</a>】");
        }
        LOGGER.info("还款备注：{}", infoBuffer.toString());
        return infoBuffer.toString();//项目还款备注
    }

    /**
     * 还款成功-处理投资人资金
     *
     * @param trade 还款报文记录
     * @author FangJun
     * @date 2016年11月3日
     */
    @Override
    public void successRepayHandle(TppTrade trade) {
        // 投资人账户金额处理
        final List<AccountModel> accountList = new ArrayList<AccountModel>();
        // 投资人资金日志
        final List<AccountLog> accountLogList = new ArrayList<AccountLog>();
        //投资用户ID
        final String investUserId = trade.getToUserId();
        JSONObject paramsObj = JSON.parseObject(trade.getParams());
        final BigDecimal capital = paramsObj.getBigDecimal("capital");
        final BigDecimal interest = paramsObj.getBigDecimal("interest");
        final BigDecimal raiseInterest = paramsObj.getBigDecimal("raiseInterest");
        final BigDecimal lateInterest = paramsObj.getBigDecimal("lateInterest");
        final BigDecimal merchantLateInterest = paramsObj.getBigDecimal("merchantLateInterest");
        final String accountType = paramsObj.getString("accountType");// 资金类型
        final String repayType = paramsObj.getString("repayType");//  还款类型
        LOGGER.info("[还款类型(01：融资人还款 02：商户平台代偿 03：融资人还代偿给商户平台 04：担保方代偿  05：融资人还代偿给担保方, 06：债权还款 )：{},"
                        + "资金类型(collect_capital 本金  collect_interest 利息  collect_add_interest 加息 collect_late_interest 逾期利息)：{},项目ID:{}]"
                , StringUtils.isNull(repayType), StringUtils.isNull(accountType), StringUtils.isNull(trade.getInvestProjectId()));
        final String projectId = StringUtils.isNull(trade.getInvestProjectId());//项目ID
        final Project project = projectService.getProjectByUuid(projectId);//获取项目信息
        final String info = this.getRemarkInfo(repayType, trade, project);//项目还款备注
        final Boolean isOverdue = paramsObj.getBoolean("isOverdue");//是否垫付
        final BigDecimal interestFee = trade.getServFee();

        LOGGER.info("isOverdue: {}, : merchantLateInterest{}, orderNo: {}", isOverdue, merchantLateInterest, trade.getOrderNo());
        //如果不是垫付 逾期罚息给平台 记录平台资金日志
        if (isOverdue != null && !isOverdue && merchantLateInterest != null && merchantLateInterest.compareTo(BigDecimal.ZERO) > 0) {
            tppMerchantLogService.saveMerchantLog(TppMerchantLogModel.TYPE_OVERDUE_MERCHANT, trade.getUserId(), merchantLateInterest, trade.getOrderNo());
        }
        
        User investUser = userService.get(investUserId);
		
        //自动投标红包处理
        ProjectInvest projectInvest = projectInvestService.get(trade.getInvestId());
        if (projectInvest != null && "1".equals(projectInvest.getInvestType()) && Constant.COLLECT_CAPITAL_INTEREST.equals(accountType)) {
        	//自动投资
        	LOGGER.info("自动投标还款，红包返回------------investId:{}", trade.getInvestId());
        	UserRedenvelope userRedenvelope = userRedenvelopeService.getByTenderId(trade.getInvestId());
    		if (userRedenvelope != null ) {
    			//使用了红包
        		ProjectRepayment repay = projectRepaymentService.get(trade.getProjectRepaymentId());
    			if (repay!= null ) {
    				if (repay.getPeriod() == repay.getPeriods()) {
    					//最后一期
    					JxVoucherPayModel jxVoucherPayModel = null;
    					//最后一期还款发红包
    					final Map<String, Object> map = new HashMap<String, Object>();
    			        map.put("accountId", ConfigUtils.getValue(ConfigConstant.JXBANK_REDENVELOPE));
    			        map.put("txAmount", BigDecimalUtils.round(userRedenvelope.getAmount(), 2));
    			        map.put("forAccountId", investUser.getTppUserCustId());
    			        map.put("desLineFlag", "1");
    			        map.put("desLine", "自动投资红包使用");
    			        TppService tppService = (TppService) TppUtil.tppService();
    			        jxVoucherPayModel = (JxVoucherPayModel) tppService.experBonus(map);
    			        if (!JxConfig.SUCCESS.equals(jxVoucherPayModel.getRetCode())) {
    			        	LOGGER.info("发放红包失败, userRedenvelope.getUuid:{}", userRedenvelope.getUuid());
    			        	 userRedenvelopeService.updateStatusBatch("'" + userRedenvelope.getUuid() + "'", null,
    			                     OperateEnum.STATUS_NO_USE.getValue(), OperateEnum.STATUS_USE.getValue());
    			        } else {
        			        userRedenvelope.setOrderNo(jxVoucherPayModel.getOrderNo());
        			        userRedenvelopeService.update(userRedenvelope);
        			        this.updateAccountAndLog(trade, userRedenvelope.getAmount(), project, accountList, accountLogList, Constant.COLLECT_REDENVELOPE, info);
    			        }
    				} else {
    					LOGGER.info("ProjectRepayment不是最后一期, repay.getPeriod():{},repay.getPeriods():{}", 
    							repay.getPeriod(), repay.getPeriods());
    				}
    			} else {
    				LOGGER.info("ProjectRepayment为空, trade.getProjectRepaymentId:{}", trade.getProjectRepaymentId());
    			}
    		} else {
    			LOGGER.info("没有红包, investId:{}", trade.getInvestId());
    		}
        } else {
        	LOGGER.info("不是自动投资, investId:{}", trade.getInvestId());
        }
		
        
        // 本息罚息
        if (Constant.COLLECT_CAPITAL_INTEREST.equals(accountType)) {
            this.updateAccountAndLog(trade, capital, project, accountList, accountLogList, Constant.COLLECT_CAPITAL, info);
            this.updateAccountAndLog(trade, interest, project, accountList, accountLogList, Constant.COLLECT_INTEREST, info);
            this.updateAccountAndLog(trade, lateInterest, project, accountList, accountLogList, Constant.COLLECT_LATE_INTEREST, info);
            if (interestFee.compareTo(BigDecimal.ZERO) > 0) {
                this.updateAccountAndLogForFee(trade, interestFee, project, accountList, accountLogList, Constant.INTEREST_MANAGE_FEE, info);
            }
        } else {
        	LOGGER.info("accountType, {}", accountType);
            this.updateAccountAndLog(trade, trade.getMoney(), project, accountList, accountLogList, accountType, info);
        }

        if (Constant.COLLECT_CAPITAL_INTEREST.equals(accountType) && BigDecimalUtils.validAmount(trade.getMoney())) {
            LOGGER.error("给投资人发送还款通知strat");
            try {
                HashMap<String, Object> params = new HashMap<>();
                params.put("user", investUser);
                params.put("projectName", project.getProjectName().length() > 10 ? project.getProjectName().substring(0, 10) + "..." : project.getProjectName());
                params.put("period", project.getRepayedPeriod() != null ? project.getRepayedPeriod() : 1);
                params.put("repayTime", DateUtils.getDateTime());
                final StringBuffer moneyBuffer = new StringBuffer();
                if (BigDecimalUtils.validAmount(capital)) {
                    moneyBuffer.append("本金").append(BigDecimalUtils.round(capital).toString()).append("元，");
                }
                if (BigDecimalUtils.validAmount(interest)) {
                    moneyBuffer.append("利息").append(BigDecimalUtils.round(interest).toString()).append("元，");
                }
                if (BigDecimalUtils.validAmount(raiseInterest)) {
                    moneyBuffer.append("加息").append(BigDecimalUtils.round(raiseInterest).toString()).append("元，");
                }
                if (BigDecimalUtils.validAmount(lateInterest)) {
                    moneyBuffer.append("逾期利息").append(BigDecimalUtils.round(lateInterest).toString()).append("元，");
                }
                if (moneyBuffer.length() > 1) {
                    params.put("repayStr", moneyBuffer.deleteCharAt(moneyBuffer.length() - 1).toString());
                    LOGGER.debug("发送投资人的还款短信 repayStr：{}", params.get("repayStr"));
                    BaseMsg baseMsg = new BaseMsg(MessageConstant.RECEIVE_REPAY, params);
                    baseMsg.doEvent();
                }
            } catch (Exception e) {
                LOGGER.error("给投资人发送还款通知处理失败，orderNo:{}", trade.getOrderNo(), e);
            }
        }
        ProjectInvest invest = projectInvestService.get(trade.getInvestId());
        // 变现冻结
        final List<TppTrade> realizeFreeTaskList = new ArrayList<TppTrade>();
        if (invest != null && InvestEnum.REALIZE_FLAG_TENDER.eq(invest.getRealizeFlag())
                && Constant.COLLECT_CAPITAL_INTEREST.equals(accountType)) {
            doRealizeFreeze(invest, trade, paramsObj.getString("collectionId"), accountType,
                    investUserId, project, accountList, accountLogList, realizeFreeTaskList);
            // 第三方调用
            tppTradeService.sendTppTrade(realizeFreeTaskList, MqConstant.ROUTING_KEY_TRADE);
        }
        // 保存投资人资金日志
        accountService.saveBatch(new AccountBatchModel(accountList, accountLogList));
    }

    @Override
    public void successRepayBailHandle(TppTrade trade) {
        //担保人账户
        final String userId = trade.getToUserId();
        JSONObject paramsObj = JSON.parseObject(trade.getParams());
        final BigDecimal money = trade.getMoney();
        final BigDecimal merchantLateInterest = paramsObj.getBigDecimal("txFeeOut");
        //如果不是垫付 逾期罚息给平台 记录平台资金日志
        if (merchantLateInterest != null && merchantLateInterest.compareTo(BigDecimal.ZERO) > 0) {
            tppMerchantLogService.saveMerchantLog(TppMerchantLogModel.TYPE_OVERDUE_MERCHANT, trade.getUserId(), merchantLateInterest, trade.getOrderNo());
        }
        final StringBuffer infoBuffer = new StringBuffer();
        final Project project = projectService.get(trade.getProjectId());
        final ProjectType projectType = projectTypeService.get(project.getProjectTypeId());
        infoBuffer.append("【<a href=\"").append(URLConstant.INVEST_DETAIL_PAGE_PREFIX).append(project.getUuid()).append("\"  target=\"_blank\">")
                .append(projectType.getTypeName()).append(StringUtils.HYPHEN).append(project.getProjectNo()).append("</a>】");
        final String info = infoBuffer.toString();
        //资金处理
        AccountModel accountModel = new AccountModel(userId, ConfigUtils.getValue(ConfigConstant.ACCOUNT_CODE), money, BigDecimal.ZERO);
        Map<String, Object> remarkData = new HashMap<String, Object>(2);
        remarkData.put("info", info);
        remarkData.put("money", money.doubleValue());
        String remark = LogTemplateUtils.getAccountTemplate(Constant.REPAY_BAIL_BACK, remarkData);
        //资金日志
        AccountLog accountLog = new AccountLog(ConfigUtils.getValue(ConfigConstant.ACCOUNT_CODE), Constant.REPAY_BAIL_BACK, userId,
                money, remark);
        accountLog.setToUser(project.getUserId());
        accountLog.setOrderNo(trade.getOrderNo());
        accountLog.setPaymentsType(AccountLog.PAYMENTS_TYPE_INCOME);
        accountService.saveAccountChange(accountModel, accountLog);
    }

    /**
     * 平台收取费用
     *
     * @param trade
     * @param money
     * @param project
     * @param accountList
     * @param accountLogList
     * @param accountType
     * @param info
     */
    private void updateAccountAndLogForFee(TppTrade trade, BigDecimal money, Project project, List<AccountModel> accountList, List<AccountLog> accountLogList, String accountType, String info) {
        if (money.doubleValue() <= 0.0) {
            LOGGER.info("trade uuid: {} , accountType:{} 的金额为零!", trade.getUuid(), accountType);
            return;
        }
        //投资用户ID
        final String investUserId = trade.getToUserId();
        accountList.add(new AccountModel(investUserId, ConfigUtils.getValue(ConfigConstant.ACCOUNT_CODE), money.negate(), BigDecimal.ZERO));
        Map<String, Object> remarkData = new HashMap<String, Object>(2);
        remarkData.put("info", info);
        remarkData.put("amount", money.doubleValue());
        String remark = LogTemplateUtils.getAccountTemplate(accountType, remarkData);
        LOGGER.info("{}的日志：{}", accountType, remark);
        AccountLog accountLog = new AccountLog(ConfigUtils.getValue(ConfigConstant.ACCOUNT_CODE), accountType, investUserId,
                money, remark);
        accountLog.setToUser(ConfigUtils.getValue(Constant.ADMIN_ID));
        accountLog.setOrderNo(trade.getOrderNo());
        accountLog.setPaymentsType(AccountLog.PAYMENTS_TYPE_EXPEND);
        accountLogList.add(accountLog);
    }

    /**
     * 生成还款日志明细
     *
     * @param trade          交易报文记录
     * @param money          交易金额
     * @param accountList    账户修改收集列表
     * @param accountLogList 资金日志收集列表
     * @param accountType    资金日志类型（collect_capital 本金  collect_interest 利息  collect_add_interest 加息 collect_late_interest 逾期利息）
     * @param info           资金日志备注
     */
    private void updateAccountAndLog(TppTrade trade, BigDecimal money, final Project project, final List<AccountModel> accountList,
                                     final List<AccountLog> accountLogList, final String accountType, final String info) {
        if (money.doubleValue() <= 0.0) {
            LOGGER.info("trade uuid: {} , accountType:{} 的金额为零!", trade.getUuid(), accountType);
            return;
        }
        //投资用户ID
        final String investUserId = trade.getToUserId();

        accountList.add(new AccountModel(investUserId, ConfigUtils.getValue(ConfigConstant.ACCOUNT_CODE), money, BigDecimal.ZERO));
        Map<String, Object> remarkData = new HashMap<String, Object>(2);
        remarkData.put("info", info);
        remarkData.put("amount", money.doubleValue());
        String remark = LogTemplateUtils.getAccountTemplate(accountType, remarkData);
        LOGGER.info("{}的日志：{}", accountType, remark);
        AccountLog accountLog = new AccountLog(ConfigUtils.getValue(ConfigConstant.ACCOUNT_CODE), accountType, investUserId,
                money, remark);
        accountLog.setToUser(project.getUserId());
        accountLog.setOrderNo(trade.getOrderNo());
        accountLog.setPaymentsType(AccountLog.PAYMENTS_TYPE_INCOME);

        if (Constant.COLLECT_CAPITAL.equals(accountType)) {
            final ProjectInvest invest = projectInvestService.get(trade.getInvestId());
            BigDecimal redPacket = BigDecimalUtils.sub(invest.getAmount(), invest.getRealAmount());//红包收益
            if (redPacket.doubleValue() == 0.0) {//未使用红包
                accountLog.setEarn(BigDecimal.ZERO);
            } else if (LoanEnum.STYLE_INSTALLMENT_REPAYMENT.eq(project.getRepayStyle())) { //等额  每期红包=本期本金*使用红包额度/投资金额
                accountLog.setEarn(BigDecimalUtils.div(redPacket.multiply(trade.getMoney()), invest.getAmount()));
            } else if (LoanEnum.STYLE_AVERAGE_CAPITAL_REPAYMENT.eq(project.getRepayStyle())) {//等本 每期红包= 使用红包额度/总期数
                accountLog.setEarn(BigDecimalUtils.div(redPacket, BigDecimal.valueOf(project.getTotalPeriod())));
            } else {
                //一次性还本息、按月付息到期还本、按季等都在最后还款本金的一期，计算红包收益
                accountLog.setEarn(redPacket);
            }
        } else if (Constant.COLLECT_INTEREST.equals(accountType) || Constant.COLLECT_LATE_INTEREST.equals(accountType)) {
            accountLog.setEarn(accountLog.getMoney());// 标识为收益
        } else if (Constant.COLLECT_ADD_INTEREST.equals(accountType)) {
            accountLog.setEarn(accountLog.getMoney());// 标识为收益
            //平台资金日志-加息利息发放
            tppMerchantLogService.saveMerchantLog(TppMerchantLogModel.TYPE_RATECOUPON, investUserId, trade.getMoney(), trade.getOrderNo());
        } else if (Constant.COLLECT_REDENVELOPE.equals(accountType)) {
            accountLog.setEarn(accountLog.getMoney());// 标识为收益
            //平台资金日志-自动投资红包发放
            tppMerchantLogService.saveMerchantLog(TppMerchantLogModel.TYPE_REDENVELOPE, investUserId, trade.getMoney(), trade.getOrderNo());
        } else {
            accountLog.setEarn(BigDecimal.ZERO);
        }

        accountLogList.add(accountLog);

    }

    /**
     * 处理变现冻结金额
     *
     * @author fxl
     * @date 2016年12月9日
     */
    private void doRealizeFreeze(ProjectInvest invest, TppTrade trade, String collectionId, String accountType, String investUserId,
                                 Project project, List<AccountModel> accountList, List<AccountLog> accountLogList, List<TppTrade> taskList) {
        LOGGER.info("还款变现冻结进来,investId:{}", invest.getUuid());
        //根据userId,collectionId 状态为0的  统计冻结本金总和//
        List<RealizeFreeze> realizeFreezeList = realizeFreezeService.getFreezeListByCollection(collectionId);
        if (CollectionUtils.isNotEmpty(realizeFreezeList)) {
            BigDecimal freezeMoney = realizeService.fillFreezeTaskList(taskList, invest, trade.getToTppUserCustId(), realizeFreezeList, trade.getProjectId(), collectionId);
            LOGGER.info("冻结已变现金额" + freezeMoney + "元");
            // 资金处理暂时未放入异步
            String type = StringUtils.EMPTY;
            if (Constant.COLLECT_CAPITAL_INTEREST.equals(accountType)) {
                type = Constant.FREEZE_REALIZE_CAPITAL;
            }
            accountList.add(new AccountModel(investUserId, ConfigUtils.getValue(ConfigConstant.ACCOUNT_CODE), freezeMoney.negate(), freezeMoney));
            // 备注
            Map<String, Object> freezeRemarkData = new HashMap<String, Object>(1);
            final StringBuffer infoBuffer = new StringBuffer();
            infoBuffer.append("【<a href=\"").append(URLConstant.REALIZE_DETAIL_PAGE_PREFIX).append(project.getUuid()).append("\"  target=\"_blank\">")
                    .append(project.getProjectName()).append("</a>】");
            freezeRemarkData.put("projectName", infoBuffer.toString());
            freezeRemarkData.put("amount", freezeMoney.doubleValue());
            String feeremark = LogTemplateUtils.getAccountTemplate(type, freezeRemarkData);
            AccountLog freezeLog = new AccountLog(ConfigUtils.getValue(ConfigConstant.ACCOUNT_CODE), type, investUserId, freezeMoney, feeremark);
            freezeLog.setToUser(project.getUserId());
            freezeLog.setOrderNo(trade.getOrderNo());
            freezeLog.setPaymentsType(AccountLog.PAYMENTS_TYPE_REMAIN);
            accountLogList.add(freezeLog);
            // 三方业务入库 --还款
            tppTradeService.insertBatch(taskList);
        }
    }

    @Override
    public void cbhbRepayHandle(CbhbFileRepaymentModel model) {
        LOGGER.info("渤海银行---还款异步回调处理,orderNo：{}", model.getMerBillNo());
        final TppTrade trade = tppTradeService.findByOrderNo(model.getMerBillNo());
        try {
            model.setRespDesc(URLDecoder.decode(model.getRespDesc(), "gbk"));
        } catch (UnsupportedEncodingException e) {
            LOGGER.error(e.getMessage(), e);
        }
        if (trade == null) {
            throw new BussinessException(ResourceUtils.get(ResourceConstant.GLOBAL_RECORD_NO_EXISTS));
        }
        int updateNum = 0;
        if (CbhbConstant.CBHB_CODE_SUCCESS.equals(model.getRespCode())) {
            updateNum = tppTradeService.callbackHandle(trade.getUuid(), TppEnum.STATUS_SUCEESS.getValue(), model.getRespDesc());
            if (updateNum == Constant.INT_ONE) {
                this.successCbhbRepayHandle(trade);
            }
        } else {
            // 失败处理
            updateNum = tppTradeService.callbackHandle(trade.getUuid(), TppEnum.STATUS_FAIL.getValue(), model.getRespDesc());
        }
        if (updateNum != Constant.INT_ONE) {
            LOGGER.warn("还款异步回调处理--更新状态失败,orderNo：{}", model.getMerBillNo());
        }
    }


    private void successCbhbRepayHandle(TppTrade trade) {
        JsonObject paramsObj = new JsonParser().parse(trade.getParams()).getAsJsonObject();
        final String repaymentId = paramsObj.get("repaymentId").getAsString();// 还款信息id
        final String repayType = paramsObj.get("repayType").getAsString();//  还款类型
        final boolean isOverdue = paramsObj.get("isOverdue").getAsBoolean();//  是否垫付

        ProjectRepayment repay = projectRepaymentService.get(repaymentId);
        LOGGER.info("渤海银行还款投资人代收查询--还款期数：{}-项目Id：{}", repay.getPeriod(), repay.getProjectId());
        final List<ProjectCollection> collectionList = projectCollectionService.findByProjectIdPeriodStatus(repay.getProjectId(),
                repay.getPeriod(), ProjectCollectionEnum.STATUS_PAID.getValue());
        final User repayUser = userService.get(repay.getUserId());
        Map<String, Object> mapExperCap = new HashMap<String, Object>(); // 加息发放
        String[][] detailExperCap = new String[collectionList.size()][];
        BigDecimal tasteTolAmt = BigDecimal.ZERO; // 加息
        int experCapNum = 0;
        final String projectId = StringUtils.isNull(trade.getProjectId());//项目ID
        final Project project = projectService.getProjectByUuid(projectId);//获取项目信息
        for (ProjectCollection collection : collectionList) {
            // 投资人账户金额处理
            final List<AccountModel> accountList = new ArrayList<AccountModel>();
            // 投资人资金日志
            final List<AccountLog> accountLogList = new ArrayList<AccountLog>();
            //投资用户ID
            final String investUserId = collection.getUserId();
            LOGGER.info("[还款类型(01：融资人还款       02：商户平台代偿 03：融资人还代偿给商户平台 04：担保方代偿       05：融资人还代偿给担保方, 06：债权还款 )：{},项目ID:{}]"
                    , StringUtils.isNull(repayType), StringUtils.isNull(trade.getProjectId()));
            final String info = this.getRemarkInfo(repayType, trade, project);//项目还款备注
            final User user = userService.get(investUserId);
            //逾期罚息给平台 记录平台资金日志
            if (BigDecimalUtils.validAmount(collection.getMerchantLateInterest())) {
                final String repayUserId = isOverdue ? project.getVouchId() : project.getUserId();
                tppMerchantLogService.saveMerchantLog(TppMerchantLogModel.TYPE_OVERDUE_MERCHANT, repayUserId, collection.getMerchantLateInterest(), trade.getOrderNo());
            }
            // 本金处理
            if (BigDecimalUtils.validAmount(collection.getCapital())) {
                accountList.add(new AccountModel(investUserId, ConfigUtils.getValue(ConfigConstant.ACCOUNT_CODE), collection.getCapital(), BigDecimal.ZERO));

                AccountLog accountLog = createAccountLog(Constant.COLLECT_CAPITAL, info, collection.getCapital(), investUserId, project.getUserId(), trade, AccountLog.PAYMENTS_TYPE_INCOME);
                final ProjectInvest invest = projectInvestService.get(collection.getInvestId());
                BigDecimal redPacket = BigDecimalUtils.sub(invest.getAmount(), invest.getRealAmount());//红包收益
                if (redPacket.doubleValue() == 0.0) {//未使用红包
                    accountLog.setEarn(BigDecimal.ZERO);
                } else if (LoanEnum.STYLE_INSTALLMENT_REPAYMENT.eq(project.getRepayStyle())) { //等额  每期红包=本期本金*使用红包额度/投资金额
                    accountLog.setEarn(BigDecimalUtils.div(redPacket.multiply(collection.getCapital()), invest.getAmount()));
                } else if (LoanEnum.STYLE_AVERAGE_CAPITAL_REPAYMENT.eq(project.getRepayStyle())) {//等本 每期红包= 使用红包额度/总期数
                    accountLog.setEarn(BigDecimalUtils.div(redPacket, BigDecimal.valueOf(project.getTotalPeriod())));
                } else {
                    //一次性还本息、按月付息到期还本、按季等都在最后还款本金的一期，计算红包收益
                    accountLog.setEarn(redPacket);
                }
                accountLogList.add(accountLog);

            }
            // 利息处理
            if (BigDecimalUtils.validAmount(collection.getInterest())) {
                accountList.add(new AccountModel(investUserId, ConfigUtils.getValue(ConfigConstant.ACCOUNT_CODE), collection.getInterest(), BigDecimal.ZERO));
                AccountLog accountLog = createAccountLog(Constant.COLLECT_INTEREST, info, collection.getInterest(), investUserId, project.getUserId(), trade, AccountLog.PAYMENTS_TYPE_INCOME);
                accountLog.setEarn(collection.getInterest());// 标识为收益
                accountLogList.add(accountLog);
            }
            // 逾期利息处理
            if (BigDecimalUtils.validAmount(collection.getLateInterest())) {
                accountList.add(new AccountModel(investUserId, ConfigUtils.getValue(ConfigConstant.ACCOUNT_CODE), collection.getLateInterest(), BigDecimal.ZERO));
                AccountLog accountLog = createAccountLog(Constant.COLLECT_LATE_INTEREST, info, collection.getLateInterest(), investUserId, project.getUserId(), trade, AccountLog.PAYMENTS_TYPE_INCOME);
                accountLog.setEarn(collection.getLateInterest());// 标识为收益
                accountLogList.add(accountLog);
            }
            // 加息利息
            if (BigDecimalUtils.validAmount(collection.getRaiseInterest())) {
                accountList.add(new AccountModel(investUserId, ConfigUtils.getValue(ConfigConstant.ACCOUNT_CODE), collection.getRaiseInterest(), BigDecimal.ZERO));
                AccountLog accountLog = createAccountLog(Constant.COLLECT_ADD_INTEREST, info, collection.getRaiseInterest(), investUserId, project.getUserId(), trade, AccountLog.PAYMENTS_TYPE_INCOME);
                accountLog.setEarn(accountLog.getMoney());// 标识为收益
                //平台资金日志-加息利息发放
                tppMerchantLogService.saveMerchantLog(TppMerchantLogModel.TYPE_RATECOUPON, investUserId, collection.getRaiseInterest(), trade.getOrderNo());
                accountLogList.add(accountLog);

                // 封装渤海银行发放加息参数
                String[] arrayExperCap = new String[5];
                arrayExperCap[0] = String.valueOf(experCapNum + 1);
                arrayExperCap[1] = user.getTppUserCustId();
                arrayExperCap[2] = collection.getRaiseInterest().toString();
                arrayExperCap[3] = CbhbConstant.STRING_ZERO;
                arrayExperCap[4] = CbhbConstant.STRING_ZERO;
                detailExperCap[experCapNum] = arrayExperCap;
                experCapNum++;
                tasteTolAmt = BigDecimalUtils.add(tasteTolAmt, collection.getRaiseInterest());
            }
            // 利息管理费
            if (BigDecimalUtils.validAmount(collection.getManageFee())) {
                accountList.add(new AccountModel(investUserId, ConfigUtils.getValue(ConfigConstant.ACCOUNT_CODE), collection.getManageFee().negate(),
                        BigDecimal.ZERO));
                AccountLog accountLog = createAccountLog(Constant.INTEREST_MANAGE_FEE, info, collection.getManageFee(), investUserId, ConfigUtils.getValue(Constant.ADMIN_ID), trade, AccountLog.PAYMENTS_TYPE_EXPEND);
                accountLogList.add(accountLog);

                //平台资金日志
                tppMerchantLogService.saveMerchantLog(TppMerchantLogModel.TYPE_INTEREST_FEE, investUserId, collection.getManageFee(), trade.getOrderNo());
            }
            try {
                HashMap<String, Object> params = new HashMap<String, Object>();
                params.put("user", userService.get(investUserId));
                params.put("projectName", project.getProjectName().length() > 10 ? project.getProjectName().substring(0, 10) + "..." : project.getProjectName());
                params.put("period", project.getRepayedPeriod() != null ? project.getRepayedPeriod() : 1);
                params.put("repayTime", DateUtils.getDateTime());
                final StringBuffer moneyBuffer = new StringBuffer();
                if (BigDecimalUtils.validAmount(collection.getCapital())) {
                    moneyBuffer.append("本金" + collection.getCapital().doubleValue() + "元，");
                }
                if (BigDecimalUtils.validAmount(collection.getInterest())) {
                    moneyBuffer.append("利息" + collection.getInterest().doubleValue() + "元，");
                }
                if (BigDecimalUtils.validAmount(collection.getRaiseInterest())) {
                    moneyBuffer.append("加息" + collection.getRaiseInterest().doubleValue() + "元，");
                }
                if (BigDecimalUtils.validAmount(collection.getLateInterest())) {
                    moneyBuffer.append("逾期利息" + collection.getLateInterest().doubleValue() + "元，");
                }
                params.put("repayStr", moneyBuffer.toString());
                BaseMsg baseMsg = new BaseMsg(MessageConstant.RECEIVE_REPAY, params);
                baseMsg.doEvent();
            } catch (Exception e) {
                LOGGER.error("给投资人发送还款通知处理失败，orderNo:{}", trade.getOrderNo(), e);
            }
            // TODO 变现冻结
            /*final List<TppTrade> realizeFreeTaskList = new ArrayList<TppTrade>();
            ProjectInvest invest = projectInvestService.get(trade.getInvestId());
			if (invest != null && InvestEnum.REALIZE_FLAG_TENDER.eq(invest.getRealizeFlag())) {
				doRealizeFreeze(invest, trade, collection.getUuid(), investUserId, project, accountList, accountLogList,realizeFreeTaskList);
				// 第三方调用
				LOGGER.info("渤海银行执行变现冻结操作，collectionId:{}",collection.getUuid());
				TppService tppService = (TppService)TppUtil.tppService();
				for(TppTrade tppTrade : realizeFreeTaskList){
					Map<String,Object> params = new HashMap<String,Object>();
					params.put("customerId", tppTrade.getTppUserCustId());
					params.put("holdAmt", tppTrade.getMoney());
					CbhbHoldAmtModel model = (CbhbHoldAmtModel)tppService.tppFreeze(params, null);
					if(CbhbConstant.CBHB_CODE_SUCCESS.equals(model.getRespCode())){
						JsonObject paramsObjRealize = new JsonParser().parse(tppTrade.getParams()).getAsJsonObject();
						realizeFreezeService.updateFreeze(paramsObjRealize.get("collectionId").getAsString(),paramsObjRealize.get("realizeId").getAsString(),paramsObj.get("type").getAsString(), model.getFreezeId(), model.getMerBillNo());
					}
				}
//				tppTradeService.sendTppTrade(realizeFreeTaskList, MqConstant.ROUTING_KEY_TRADE);
			}*/
            // 保存投资人资金日志
            accountService.saveBatch(new AccountBatchModel(accountList, accountLogList));

        }
        mapExperCap.put("totalNum", experCapNum);
        mapExperCap.put("detailList", detailExperCap);
        mapExperCap.put("tasteTolAmt", tasteTolAmt);
        mapExperCap.put("borrowId", project.getProjectNo());
        mapExperCap.put("repaymentId", repaymentId);
        mapExperCap.put("transAmt", CbhbConstant.STRING_ZERO);
        mapExperCap.put("merFeeAmt", CbhbConstant.STRING_ZERO);
        mapExperCap.put("capTyp", CbhbConstant.STRING_TWO);
        if (tasteTolAmt.doubleValue() > 0) {
            TppService tppService = (TppService) TppUtil.tppService();
            CbhbExperCapModel modelExperCap = (CbhbExperCapModel) tppService.tppExperCap(mapExperCap);
            TppTrade tppTradeExperCap = new TppTrade(TppEnum.TPP_TYPE_EXPERCAP.getValue(), TppEnum.TPP_TYPE_EXPERCAP.getValue(), null, repayUser.getTppUserCustId(),
                    "", "", tasteTolAmt, "");
            mapExperCap.remove("detailList");
            tppTradeExperCap.setParams(JSON.toJSONString(mapExperCap));
            tppTradeExperCap.setInvestProjectId(project.getUuid());
            tppTradeExperCap.setProjectId(project.getUuid());// 项目UUID
            if (modelExperCap != null) {
                tppTradeExperCap.setOrderNo(modelExperCap.getMerBillNo());
                tppTradeExperCap.setReturnUrl(modelExperCap.getBgRetUrl());
            }
            tppTradeService.insert(tppTradeExperCap);
        }
    }

    /**
     * 生成资金日志
     *
     * @param accountType
     * @param info
     * @param amount
     * @param investUserId
     * @param toUser
     * @param trade
     * @param paymentsType
     * @return
     */
    private AccountLog createAccountLog(String accountType, String info,
                                        BigDecimal amount, String investUserId, String toUser, TppTrade trade, String paymentsType) {
        Map<String, Object> remarkData = new HashMap<String, Object>(1);
        remarkData.put("info", info);
        remarkData.put("amount", amount.doubleValue());
        String remark = LogTemplateUtils.getAccountTemplate(accountType, remarkData);
        AccountLog accountLog = new AccountLog(ConfigUtils.getValue(ConfigConstant.ACCOUNT_CODE), accountType, investUserId,
                amount, remark);
        accountLog.setToUser(toUser);
        accountLog.setOrderNo(trade.getOrderNo());
        accountLog.setPaymentsType(paymentsType);
        return accountLog;
    }
}
