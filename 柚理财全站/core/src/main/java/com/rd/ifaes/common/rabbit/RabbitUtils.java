package com.rd.ifaes.common.rabbit;

import com.rd.ifaes.common.rabbit.model.*;
import com.rd.ifaes.common.util.SpringContextHolder;
import com.rd.ifaes.core.account.model.CashModel;
import com.rd.ifaes.core.bond.model.BondInvestOtherModel;
import com.rd.ifaes.core.core.constant.MqConstant;
import com.rd.ifaes.core.protocol.util.bond.BondProtocol;
import com.rd.ifaes.core.protocol.util.invest.InvestProtocol;
import com.rd.ifaes.core.sys.domain.Message;
import com.rd.ifaes.core.tpp.domain.TppTrade;
import com.rd.ifaes.core.tpp.model.TppTradeModel;
import com.rd.ifaes.core.tpp.model.jx.JxBidApplyModel;
import com.rd.ifaes.core.tpp.model.jx.JxCreditInvestModel;
import com.rd.ifaes.core.tpp.model.ufx.*;
import com.rd.ifaes.core.tpp.service.tpp.JxbankBondInvestService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * rabbit队列服务
 *
 * @author lh
 * @version 3.0
 * @date 2016年8月8日
 */
public class RabbitUtils {

    private static RabbitProducer rabbitProducer = SpringContextHolder.getBean(RabbitProducer.class);

    private RabbitUtils() {
    }
    /************************************** 投资相关 **************************************/
    /**
     * 投资成功
     *
     * @param investModel
     * @author lh
     * @date 2016年8月8日
     */
    public static void investSuccess(final UfxInvestModel investModel) {
        final MqInvestModel model = new MqInvestModel(MqConstant.OPERATE_INVEST_SUCCESS);
        model.setInvestModel(investModel);
        rabbitProducer.send(MqConstant.ROUTING_KEY_INVEST, model);
    }

    /**
     * 投资成功
     *
     * @param jxModel
     * @author lh
     * @date 2016年8月8日
     */
    public static void investSuccessJx(final JxBidApplyModel jxModel) {
        final MqInvestModel model = new MqInvestModel(MqConstant.OPERATE_INVEST_SUCCESS);
        model.setJxBidApplyModel(jxModel);
        rabbitProducer.send(MqConstant.ROUTING_KEY_INVEST, model);
    }


    /**
     * 投资超时（下架、撤销等，支付成功回调处理）
     *
     * @param investModel
     * @author lh
     * @date 2016年8月8日
     */
    public static void investTimeout(final UfxInvestModel investModel) {
        final MqInvestModel model = new MqInvestModel(MqConstant.OPERATE_INVEST_TIMEOUT);
        model.setInvestModel(investModel);
        rabbitProducer.send(MqConstant.ROUTING_KEY_INVEST, model);
    }

    /**
     * 投资超时（下架、撤销等，支付成功回调处理）
     *
     * @param jxModel
     * @author lh
     * @date 2016年8月8日
     */
    public static void investTimeoutJx(final JxBidApplyModel jxModel) {
        final MqInvestModel model = new MqInvestModel(MqConstant.OPERATE_INVEST_TIMEOUT);
        model.setJxBidApplyModel(jxModel);
        rabbitProducer.send(MqConstant.ROUTING_KEY_INVEST, model);
    }

    /**
     * 开始执行自动投资业务
     *
     * @param autoInvestModel
     * @author ZhangBiao
     * @date 2016年9月13日
     */
    public static void startAutoInvest(MqAutoInvestModel autoInvestModel) {
        final MqInvestModel model = new MqInvestModel(MqConstant.OPERATE_START_AUTO_INVEST);
        model.setAutoInvestModel(autoInvestModel);
        rabbitProducer.send(MqConstant.ROUTING_KEY_AUTO_INVEST, model);
    }

    /**
     * 投资失败，解冻资金
     *
     * @param investFailModel
     * @author lh
     * @date 2016年8月8日
     */
    public static void investFail(final UfxInvestFailModel investFailModel) {
        final MqInvestModel model = new MqInvestModel(MqConstant.OPERATE_INVEST_FAIL);
        model.setInvestFailModel(investFailModel);
        rabbitProducer.send(MqConstant.ROUTING_KEY_INVEST, model);
    }

    /**
     * 投资支付失败
     *
     * @param investFailModel
     * @author lh
     * @date 2016年8月8日
     */
    public static void investPayFail(final UfxInvestModel investModel) {
        final MqInvestModel model = new MqInvestModel(MqConstant.OPERATE_INVEST_PAY_FAIL);
        model.setInvestModel(investModel);
        rabbitProducer.send(MqConstant.ROUTING_KEY_INVEST, model);
    }

    /**
     * 投资支付失败
     *
     * @param investFailModel
     * @author lh
     * @date 2016年8月8日
     */
    public static void investPayFailJx(final JxBidApplyModel jxModel) {
        final MqInvestModel model = new MqInvestModel(MqConstant.OPERATE_INVEST_PAY_FAIL);
        model.setJxBidApplyModel(jxModel);
        rabbitProducer.send(MqConstant.ROUTING_KEY_INVEST, model);
    }

    /**
     * 放款回调处理
     *
     * @param loanModel
     * @author FangJun
     * @date 2016年10月17日
     */
    public static void loan(final TppTrade trade) {
        final MqLoanModel model = new MqLoanModel(MqConstant.OPERATE_LOAN);
        model.setTppTrade(trade);
        rabbitProducer.send(MqConstant.ROUTING_KEY_LOAN, model);
    }

    /**
     * 还款回调处理
     *
     * @param loanModel
     * @author FangJun
     * @date 2016年10月21日
     */
    public static void repay(final UfxRepaymentModel repayModel) {
        final MqRepayModel model = new MqRepayModel(MqConstant.OPERATE_REPAY);
        model.setRepayModel(repayModel);
        rabbitProducer.send(MqConstant.ROUTING_KEY_REPAY, model);
    }
    /**
     * 还款回调处理
     *
     * @param loanModel
     * @author FangJun
     * @date 2016年10月21日
     */
    public static void repay(final TppTrade tppTrade) {
        final MqRepayModel model = new MqRepayModel(MqConstant.OPERATE_REPAY);
        model.setTppTrade(tppTrade);
        rabbitProducer.send(MqConstant.ROUTING_KEY_REPAY, model);
    }

    /**
     * 还款结束批量结束债权
     *
     * @param loanModel
     * @author FangJun
     * @date 2016年10月21日
     */
    public static void batchCreditEnd(String projectId) {
        final MqRepayModel model = new MqRepayModel(MqConstant.OPERATE_BATCH_CREDIT_END);
        model.setProjectId(projectId);
        rabbitProducer.send(MqConstant.ROUTING_KEY_REPAY, model);
    }

    /**
     * 债权转让
     *
     * @param creditTransferModel
     * @author lh
     * @date 2016年8月8日
     */
    public static void bondInvest(final UfxCreditTransferModel creditTransferModel) {
        final MqInvestModel model = new MqInvestModel(MqConstant.OPERATE_BOND_INVEST);
        model.setCreditTransferModel(creditTransferModel);
        rabbitProducer.send(MqConstant.ROUTING_KEY_INVEST, model);
    }
    /**
     * 债权转让
     *
     * @param creditTransferModel
     * @author lh
     * @date 2016年8月8日
     */
    public static void bondInvestSuccess(final JxCreditInvestModel jxCreditInvestModel) {
        final MqInvestModel model = new MqInvestModel(MqConstant.OPERATE_BOND_INVEST);
        model.setJxCreditInvestModel(jxCreditInvestModel);
        rabbitProducer.send(MqConstant.ROUTING_KEY_INVEST, model);
    }

    /**
     * 债权投资失败
     *
     * @param creditTransferModel
     * @author QianPengZhan
     * @date 2016年12月7日
     */
    public static void bondInvestFail(final UfxCreditTransferModel creditTransferModel) {
        final MqInvestModel model = new MqInvestModel(MqConstant.OPERATE_BOND_INVEST_FAIL);
        model.setCreditTransferModel(creditTransferModel);
        rabbitProducer.send(MqConstant.ROUTING_KEY_INVEST, model);
    }

    /**
     * 债权投资失败
     *
     * @param creditTransferModel
     * @author QianPengZhan
     * @date 2016年12月7日
     */
    public static void bondInvestFail(final JxCreditInvestModel jxCreditInvestModel) {
        final MqInvestModel model = new MqInvestModel(MqConstant.OPERATE_BOND_INVEST_FAIL);
        model.setJxCreditInvestModel(jxCreditInvestModel);
        rabbitProducer.send(MqConstant.ROUTING_KEY_INVEST, model);
    }

    /**
     * 债权转让其他业务的异步处理
     *
     * @param bondInvestOtherModel
     * @author QianPengZhan
     * @date 2016年12月7日
     */
    public static void bondInvestOtherService(final BondInvestOtherModel bondInvestOtherModel) {
        final MqInvestModel model = new MqInvestModel(MqConstant.OPERATE_BOND_INVEST_OTHER);
        model.setBondInvestOtherModel(bondInvestOtherModel);
        rabbitProducer.send(MqConstant.ROUTING_KEY_INVEST, model);
    }
    /************************************** 第三方交易 **************************************/

    /**
     * 第三方交易
     *
     * @param tradeList
     * @author lh
     * @date 2016年8月8日
     */
    public static void tppTrade(final List<TppTradeModel> tradeList) {
        final MqTradeModel model = new MqTradeModel(tradeList);
        rabbitProducer.send(MqConstant.ROUTING_KEY_TRADE, model);
    }

    /**
     * 第三方交易--放款
     *
     * @param tradeList
     * @author lh
     * @date 2016年8月8日
     */
    public static void tppLoan(final List<TppTradeModel> tradeList) {
        final MqTradeModel model = new MqTradeModel(tradeList);
        rabbitProducer.send(MqConstant.ROUTING_KEY_TRADE_LOAN, model);
    }

    /**
     * 第三方交易--还款
     *
     * @param tradeList
     * @author lh
     * @date 2016年8月8日
     */
    public static void tppRepay(final List<TppTradeModel> tradeList) {
        final MqTradeModel model = new MqTradeModel(tradeList);
        rabbitProducer.send(MqConstant.ROUTING_KEY_TRADE_REPAY, model);
    }

    /**
     * 第三方交易--项目撤销
     *
     * @param tradeList
     * @author lh
     * @date 2016年8月8日
     */
    public static void tppInvestFail(final List<TppTradeModel> tradeList) {
        final MqTradeModel model = new MqTradeModel(tradeList);
        rabbitProducer.send(MqConstant.ROUTING_KEY_TRADE_INVESTFAIL, model);
    }

    /************************************** 用户注册 **************************************/

    /**
     * 个人注册
     *
     * @author lh
     * @date 2016年8月8日
     */
    public static void register(final UfxRegisterModel registerModel) {
        final MqUserModel model = new MqUserModel(MqConstant.OPERATE_REGISGER);
        model.setRegisterModel(registerModel);
        rabbitProducer.send(MqConstant.ROUTING_KEY_USER, model);
    }

    /**
     * 企业注册
     *
     * @param userModel
     * @author lh
     * @date 2016年8月8日
     */
    public static void companyRegister(final UfxCompanyRegisterModel companyRegisterModel) {
        final MqUserModel model = new MqUserModel(MqConstant.OPERATE_COMPANY_REGISGER);
        model.setCompanyRegisterModel(companyRegisterModel);
        rabbitProducer.send(MqConstant.ROUTING_KEY_USER, model);
    }

    /************************************** 充值取现 **************************************/

    /**
     * 取现-同步异步回调
     *
     * @param cashModel
     * @author lh
     * @date 2016年8月8日
     */
    public static void cash(final CashModel cashModel) {
        final MqCashModel model = new MqCashModel(MqConstant.OPERATE_CASH);
        model.setCashModel(cashModel);
        rabbitProducer.send(MqConstant.ROUTING_KEY_CASH, model);
    }

    /**
     * 取现-异步对账回调
     *
     * @param cashModel
     * @author xhf
     * @date 2016年8月12日
     */
    public static void cashBankReturn(final CashModel cashModel) {
        final MqCashModel model = new MqCashModel(MqConstant.OPERATE_CASH_BANK_RETURN);
        model.setCashModel(cashModel);
        rabbitProducer.send(MqConstant.ROUTING_KEY_CASH, model);
    }

    /**
     * 取现复核-异步或异步对账回调
     *
     * @param cashModel
     * @author xhf
     * @date 2016年8月8日
     */
    public static void cashAudit(final UfxCashAuditModel cashAuditModel) {
        final MqCashModel model = new MqCashModel(MqConstant.OPERATE_CASH_AUDIT);
        model.setCashAuditModel(cashAuditModel);
        rabbitProducer.send(MqConstant.ROUTING_KEY_CASH, model);
    }

    /**
     * 充值成功
     *
     * @param rechargeModel
     * @author lh
     * @date 2016年8月8日
     */
    public static void rechargeSuccess(final TppRechargeModel rechargeModel) {
        final MqCashModel model = new MqCashModel(MqConstant.OPERATE_RECHARGE_SUCCESS);
        model.setRechargeModel(rechargeModel);
        rabbitProducer.send(MqConstant.ROUTING_KEY_CASH, model);
    }

    /**
     * 充值失败
     *
     * @author lh
     * @date 2016年8月8日
     */
    public static void rechargeFail(final TppRechargeModel rechargeModel) {
        final MqCashModel model = new MqCashModel(MqConstant.OPERATE_RECHARGE_FAIL);
        model.setRechargeModel(rechargeModel);
        rabbitProducer.send(MqConstant.ROUTING_KEY_CASH, model);
    }

    /**
     * 发送消息
     *
     * @param message
     * @author xhf
     * @date 2016年8月19日
     */
    public static void sendMessage(final Message message) {
        rabbitProducer.send(MqConstant.ROUTING_KEY_MESSAGE, message);
    }


    /**
     * 普通投资生成借款协议
     *
     * @author QianPengZhan
     * @date 2016年10月19日
     */
    public static void buildProtocol(final InvestProtocol investProtocol) {
        final MqInvestModel model = new MqInvestModel(MqConstant.BUILD_PROTOCOL);
        model.setInvestProtocol(investProtocol);
        rabbitProducer.send(MqConstant.ROUTING_KEY_PROTOCOl, model);
    }

    /**
     * 生成债权协议
     *
     * @param bondProcotol
     * @author QianPengZhan
     * @date 2016年9月30日
     */
    public static void buildBondProtocol(final BondProtocol bondProcotol) {
        final MqInvestModel model = new MqInvestModel(MqConstant.BUILD_BOND_PROTOCOL);
        model.setBondProtocol(bondProcotol);
        rabbitProducer.send(MqConstant.ROUTING_KEY_PROTOCOl, model);
    }

    /**
     * 每天下午17点30扫描昨天17点30~今天17点30  所有有资金操作的用户的本地和第三方资金的对比  找出差异的用户 并发短信通知运维人员
     *
     * @param registerModel
     * @author QianPengZhan
     * @date 2016年10月11日
     */
    public static void scannerAccountCompareError(final List<String> logList) {
        final MqUserModel model = new MqUserModel(MqConstant.OPERATE_USER_COMPARE_ACCOUNT);
        model.setLogList(logList);
        rabbitProducer.send(MqConstant.ROUTING_KEY_USER, model);
    }

    /**
     * 第三方交易失败  发送预警短信
     *
     * @author QianPengZhan
     * @date 2016年10月12日
     */
    public static void tppWarn(final Map<String, Object> params) {
        final MqUserModel model = new MqUserModel(MqConstant.TPP_WARN);
        model.setParams(params);
        rabbitProducer.send(MqConstant.ROUTING_KEY_USER, model);
    }

    /**
     * 活动方案
     *
     * @param model
     * @author lh
     * @since 2016-10-18
     */
    public static void actPlan(final MqActPlanModel model) {
        rabbitProducer.send(MqConstant.ROUTING_KEY_ACTPLAN, model);
    }

    /**
     * 计算用户投资记录数
     *
     * @param userId
     */
    public static void addUserInvestNum(String userId) {
        final MqUserModel model = new MqUserModel(MqConstant.OPERATE_USER_INVEST_NUM);
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        model.setParams(map);
        rabbitProducer.send(MqConstant.ROUTING_KEY_USER, model);
    }

    /**
     * 转让后更新bond_invest和bond_collection的investId;
     * 因为转让成功后会生成新的project_invest记录 原先的对应不上了
     * @param projectId
     */
    public static void updateInvestIdAfterBond(String projectId) {
        final MqInvestModel model = new MqInvestModel(MqConstant.OPERATE_UPDATE_INVEST_ID);
        model.setProjectId(projectId);
        rabbitProducer.send(MqConstant.ROUTING_KEY_INVEST, model);
    }

    /**
     * 还垫付款回调处理
     * @param trade
     */
    public static void repayBail(TppTrade tppTrade) {
        final MqRepayModel model = new MqRepayModel(MqConstant.OPERATE_REPAY_BAIL);
        model.setTppTrade(tppTrade);
        rabbitProducer.send(MqConstant.ROUTING_KEY_REPAY, model);
    }
}
