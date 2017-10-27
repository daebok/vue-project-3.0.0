package com.rd.ifaes.core.tpp.service.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.rd.ifaes.common.constant.JxbankConstant;
import com.rd.ifaes.common.util.BigDecimalUtils;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.core.tpp.exception.JxbankException;
import com.rd.ifaes.core.tpp.model.jx.JxAccountDetailsQueryModel;
import com.rd.ifaes.core.tpp.model.jx.JxAccountIdQueryModel;
import com.rd.ifaes.core.tpp.model.jx.JxAcountOpenPlusModel;
import com.rd.ifaes.core.tpp.model.jx.JxAutoBidAuthCancelModel;
import com.rd.ifaes.core.tpp.model.jx.JxAutoBidAuthPlusModel;
import com.rd.ifaes.core.tpp.model.jx.JxAutoCreditInvestAuthCancelModel;
import com.rd.ifaes.core.tpp.model.jx.JxAutoCreditInvestAuthPlusModel;
import com.rd.ifaes.core.tpp.model.jx.JxBalanceFreezeModel;
import com.rd.ifaes.core.tpp.model.jx.JxBalanceQueryModel;
import com.rd.ifaes.core.tpp.model.jx.JxBalanceUnfreezeModel;
import com.rd.ifaes.core.tpp.model.jx.JxBatchBailRepayModel;
import com.rd.ifaes.core.tpp.model.jx.JxBatchCreditEndModel;
import com.rd.ifaes.core.tpp.model.jx.JxBatchDetailsQueryModel;
import com.rd.ifaes.core.tpp.model.jx.JxBatchLendPayModel;
import com.rd.ifaes.core.tpp.model.jx.JxBatchQueryModel;
import com.rd.ifaes.core.tpp.model.jx.JxBatchRepayBailModel;
import com.rd.ifaes.core.tpp.model.jx.JxBatchRepayModel;
import com.rd.ifaes.core.tpp.model.jx.JxBatchVoucherDetailsQueryModel;
import com.rd.ifaes.core.tpp.model.jx.JxBatchVoucherPayModel;
import com.rd.ifaes.core.tpp.model.jx.JxBidApplyModel;
import com.rd.ifaes.core.tpp.model.jx.JxBidApplyQueryModel;
import com.rd.ifaes.core.tpp.model.jx.JxBidAutoApplyModel;
import com.rd.ifaes.core.tpp.model.jx.JxBidCancelModel;
import com.rd.ifaes.core.tpp.model.jx.JxCardBindDetailsQueryModel;
import com.rd.ifaes.core.tpp.model.jx.JxCardBindPlusModel;
import com.rd.ifaes.core.tpp.model.jx.JxCardUnbindModel;
import com.rd.ifaes.core.tpp.model.jx.JxCorprationQueryModel;
import com.rd.ifaes.core.tpp.model.jx.JxCreditAuthQueryModel;
import com.rd.ifaes.core.tpp.model.jx.JxCreditDetailsQueryModel;
import com.rd.ifaes.core.tpp.model.jx.JxCreditInvestModel;
import com.rd.ifaes.core.tpp.model.jx.JxCreditInvestQueryModel;
import com.rd.ifaes.core.tpp.model.jx.JxDebtDetailsQueryModel;
import com.rd.ifaes.core.tpp.model.jx.JxDebtRegisterCancelModel;
import com.rd.ifaes.core.tpp.model.jx.JxDebtRegisterModel;
import com.rd.ifaes.core.tpp.model.jx.JxDirectRechargeOnlineModel;
import com.rd.ifaes.core.tpp.model.jx.JxFundTransQueryModel;
import com.rd.ifaes.core.tpp.model.jx.JxMobileModifyPlusModel;
import com.rd.ifaes.core.tpp.model.jx.JxPasswordResetPlusModel;
import com.rd.ifaes.core.tpp.model.jx.JxPasswordSetModel;
import com.rd.ifaes.core.tpp.model.jx.JxPasswordSetQueryModel;
import com.rd.ifaes.core.tpp.model.jx.JxSmsCodeApplyModel;
import com.rd.ifaes.core.tpp.model.jx.JxVoucherPayCancelModel;
import com.rd.ifaes.core.tpp.model.jx.JxVoucherPayDelayCancelModel;
import com.rd.ifaes.core.tpp.model.jx.JxVoucherPayModel;
import com.rd.ifaes.core.tpp.model.jx.JxWithdrawModel;
import com.rd.ifaes.core.tpp.model.jx.file.JxFileTradeDetailAllModel;
import com.rd.ifaes.core.tpp.model.jx.file.JxFileTradeDetailModel;
import com.rd.ifaes.core.tpp.service.TppService;
import com.rd.ifaes.core.tpp.util.JxConfig;

@Service("tppJxbankServiceImpl")
public class TppJxbankServiceImpl implements TppService {
    protected static final Logger LOGGER = LoggerFactory.getLogger(TppJxbankServiceImpl.class);

    @Override
    public Object tppAppRegister(Map<String, Object> params) {
        // TODO Auto-generated method stub
        return null;
    }


    @Override
    public Object tppAccountIdQuery(Map<String, Object> params) {
        final JxAccountIdQueryModel model = new JxAccountIdQueryModel();
        model.setIdType(StringUtils.isNull(params.get("idType")));
        model.setIdNo(StringUtils.isNull(params.get("idNo")));
        model.setAcqRes(StringUtils.isNull(params.get("acqRes")));
        model.sign();
        model.directRequest();
        return model.getMapRespData();
    }

    @Override
    public Object tppSmsCodeApply(Map<String, Object> params) {
        final JxSmsCodeApplyModel model = new JxSmsCodeApplyModel();
        model.setMobile(StringUtils.isNull(params.get("mobile")));
        model.setReqType(StringUtils.isNull(params.get("reqType")));
        model.setCardNo(StringUtils.isNull(params.get("cardNo")));
        model.setSrvTxCode(StringUtils.isNull(params.get("srvTxCode")));
        model.setAcqRes(StringUtils.isNull(params.get("acqRes")));
        model.sign();
        model.directRequest();
        return model.getMapRespData();
    }

    @Override
    public Object tppRegister(Map<String, Object> params) {
        final JxAcountOpenPlusModel model = new JxAcountOpenPlusModel();
        model.setIdType(StringUtils.isNull(params.get("idType")));
        model.setIdNo(StringUtils.isNull(params.get("idNo")));
        model.setName(StringUtils.isNull(params.get("name")));
        model.setMobile(StringUtils.isNull(params.get("mobile")));
        model.setCardNo(StringUtils.isNull(params.get("cardNo")));
        model.setEmail(StringUtils.isNull(params.get("email")));
        model.setAcctUse(StringUtils.isNull(params.get("acctUse")));
        model.setLastSrvAuthCode(StringUtils.isNull(params.get("lastSrvAuthCode")));
        model.setSmsCode(StringUtils.isNull(params.get("smsCode")));
        model.setUserIP(StringUtils.isNull(params.get("userIP")));
        model.setAcqRes(StringUtils.isNull(params.get("acqRes")));
        model.sign();
        model.directRequest();
        return model.getMapRespData();
    }

    @Override
    public Object tppAppRecharge(Map<String, Object> params) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Object tppRecharge(Map<String, Object> params) {
        JxDirectRechargeOnlineModel model = new JxDirectRechargeOnlineModel();
        model.setAccountId(StringUtils.isNull(params.get("accountId")));
        model.setIdType(StringUtils.isNull(params.get("idType")));
        model.setIdNo(StringUtils.isNull(params.get("idNo")));
        model.setName(StringUtils.isNull(params.get("name")));
        model.setMobile(StringUtils.isNull(params.get("mobile")));
        model.setCardNo(StringUtils.isNull(params.get("cardNo")));
        model.setTxAmount(StringUtils.isNull(params.get("txAmount")));
        model.setCurrency(StringUtils.isNull(params.get("currency")));
        model.setSmsCode(StringUtils.isNull(params.get("smsCode")));
        model.setSmsSeq(StringUtils.isNull(params.get("smsSeq")));
        model.setAcqRes(StringUtils.isNull(params.get("acqRes")));
        model.sign();
        Map<String, Object> map = model.getMapRespData();
        try {
            model.directRequest();
        } catch (JxbankException e) {
            LOGGER.info("不抛出异常：{}", e.getMessage());
            map.put("retCode", JxConfig.ERROR);
            map.put("retMsg", "验签失败");
        }
        return model.getMapRespData();
    }

    @Override
    public Object tppAppCash(Map<String, Object> params) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Object tppCash(Map<String, Object> params) {
        JxWithdrawModel model = new JxWithdrawModel();
        model.setAccountId(StringUtils.isNull(params.get("accountId")));
        model.setIdType(StringUtils.isNull(params.get("idType")));
        model.setIdNo(StringUtils.isNull(params.get("idNo")));
        model.setName(StringUtils.isNull(params.get("name")));
        model.setMobile(StringUtils.isNull(params.get("mobile")));
        model.setCardNo(StringUtils.isNull(params.get("cardNo")));
        model.setTxAmount(StringUtils.isNull(params.get("txAmount")));
        model.setTxFee(StringUtils.isNull(params.get("txFee")));
        model.setRouteCode(StringUtils.isNull(params.get("routeCode")));
        model.setCardBankCnaps(StringUtils.isNull(params.get("cardBankCnaps")));
//		model.setForgotPwdUrl(StringUtils.isNull(params.get("forgotPwdUrl")));
//		model.setNotifyUrl(StringUtils.isNull(params.get("notifyUrl")));
        model.setAcqRes(StringUtils.isNull(params.get("acqRes")));
        model.sign();
        return model;
    }

    @Override
    public Object tppMerchantRecharge(Map<String, Object> params) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Object tppMerchantCash(Map<String, Object> params) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Object tppAddProject(Map<String, Object> params) {
        JxDebtRegisterModel model = new JxDebtRegisterModel();
        model.setAccountId(StringUtils.isNull(params.get("accountId")));
        model.setProductId(StringUtils.isNull(params.get("productId")));
        model.setProductDesc(StringUtils.isNull(params.get("productDesc")));
        model.setRaiseDate(StringUtils.isNull(params.get("raiseDate")));
        model.setRaiseEndDate(StringUtils.isNull(params.get("raiseEndDate")));
        model.setIntType(StringUtils.isNull(params.get("intType")));
        model.setIntPayDay(StringUtils.isNull(params.get("intPayDay")));
        model.setDuration(StringUtils.isNull(params.get("duration")));
        model.setTxAmount(StringUtils.isNull(params.get("txAmount")));
        model.setRate(StringUtils.isNull(params.get("rate")));
        model.setTxFee(StringUtils.isNull(params.get("txFee")));
        model.setBailAccountId(StringUtils.isNull(params.get("bailAccountId")));
        model.setNominalAccountId(StringUtils.isNull(params.get("nominalAccountId")));
        model.setAcqRes(StringUtils.isNull(params.get("acqRes")));
        model.sign();
        model.directRequest();
        return model.getMapRespData();
    }

    @Override
    public Object tppAddTender(Map<String, Object> params) {
        JxBidApplyModel model = new JxBidApplyModel();
        model.setAccountId(StringUtils.isNull(params.get("accountId")));
        model.setTxAmount(StringUtils.isNull(params.get("txAmount")));
        model.setProductId(StringUtils.isNull(params.get("productId")));
        model.setBonusFlag(StringUtils.isNull(params.get("bonusFlag")));
        model.setBonusAmount(StringUtils.isNull(params.get("bonusAmount")));
        model.setAcqRes(StringUtils.isNull(params.get("acqRes")));
        model.sign();
        return model;
    }

    @Override
    public Object tppLoan(Map<String, Object> params) {
        JxBatchLendPayModel model = new JxBatchLendPayModel();
        model.setBatchNo(StringUtils.isNull(params.get("batchNo")));
        model.setSeqNo(StringUtils.isNull(params.get("batchNo")));
        model.setTxAmount(StringUtils.isNull(params.get("txAmount")));
        model.setTxCounts(StringUtils.isNull(params.get("txCounts")));
        model.setSubPacks(JSON.toJSONString(params.get("subPacks")));
        model.sign();
        model.directRequestBatch();
        return model.getMapRespData();
    }

    @Override
    public Object tppRepayment(Map<String, Object> params) {
        JxBatchRepayModel model = new JxBatchRepayModel();
        model.setBatchNo(StringUtils.isNull(params.get("batchNo")));
        model.setSeqNo(StringUtils.isNull(params.get("batchNo")));
        model.setTxAmount(StringUtils.isNull(params.get("txAmount")));
        model.setTxCounts(StringUtils.isNull(params.get("txCounts")));
        model.setSubPacks(JSON.toJSONString(params.get("subPacks")));
        model.sign();
        model.directRequestBatch();
        return model.getMapRespData();
    }

    @Override
    public Object tppQueryBalance(Map<String, Object> params) {
        JxBalanceQueryModel model = new JxBalanceQueryModel();
        model.setAccountId(StringUtils.isNull(params.get("accountId")));
        model.setAcqRes(StringUtils.isNull(params.get("acqRes")));
        model.sign();
        model.directRequest();

        Map<String, Object> mapRespData = model.getMapRespData();
        final Map<String, Object> map = new HashMap<>();
        BigDecimal availBal = new BigDecimal((String) mapRespData.get("availBal"));
        BigDecimal currBal = new BigDecimal((String) mapRespData.get("currBal"));
        map.put("avlBal", availBal);
        map.put("acctBal", currBal);
        map.put("frzBal", BigDecimalUtils.sub(currBal, availBal));
        return map;
    }

    @Override
    public Object tppInvestFail(Map<String, Object> params) {
        JxBidCancelModel model = new JxBidCancelModel();
        model.setAccountId(StringUtils.isNull(params.get("accountId")));
        model.setTxAmount(StringUtils.isNull(params.get("txAmount")));
        model.setProductId(StringUtils.isNull(params.get("productId")));
        model.setOrgOrderId(StringUtils.isNull(params.get("orgOrderId")));
        model.sign();
        model.directRequest();
        return model.getMapRespData();
    }

    @Override
    public Object tppAdditionOrder(Map<String, Object> params) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Object tppQueryTransfer(Map<String, Object> params) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Object tppMerSubAccountTransfer(Map<String, Object> params) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Object tppProjectAttachInfo(Map<String, Object> params) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Object tppProjectRevoke(Map<String, Object> params) {
        JxDebtRegisterCancelModel model = new JxDebtRegisterCancelModel();
        model.setAccountId(StringUtils.isNull(params.get("accountId")));
        model.setProductId(StringUtils.isNull(params.get("productId")));
        model.setRaiseDate(StringUtils.isNull(params.get("raiseDate")));
        model.setTxAmount(StringUtils.isNull(params.get("txAmount")));
        model.setAcqRes(StringUtils.isNull(params.get("acqRes")));
        model.sign();
        model.directRequest();
        return model.getMapRespData();
    }

    @Override
    public Object tppUpdateProject(Map<String, Object> params) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Object tppCropRegister(Map<String, Object> params) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Object tppAuthSign(Map<String, Object> params) {
        String authOption = (String) params.get("authOption");
        if (JxConfig.JXBANK_AUTH_OPEN.equals(authOption)) {
            final JxAutoBidAuthPlusModel model = new JxAutoBidAuthPlusModel();
            model.setAccountId(StringUtils.isNull(params.get("accountId")));
            model.setOrderId(StringUtils.isNull(params.get("orderId")));
            model.setTxAmount(StringUtils.isNull(params.get("txAmount")));
            model.setTotAmount(StringUtils.isNull(params.get("totAmount")));
            model.setLastSrvAuthCode(StringUtils.isNull(params.get("lastSrvAuthCode")));
            model.setSmsCode(StringUtils.isNull(params.get("smsCode")));
            model.setAcqRes(StringUtils.isNull(params.get("acqRes")));
            model.sign();
            return model;
        } else {
            final JxAutoBidAuthCancelModel model = new JxAutoBidAuthCancelModel();
            model.setAccountId(StringUtils.isNull(params.get("accountId")));
            model.setOrderId(StringUtils.isNull(params.get("orderId")));
            model.setOrgOrderId(StringUtils.isNull(params.get("orgOrderId")));
            model.sign();
            model.directRequest();
            return model.getMapRespData();
        }
    }

    @Override
    public Object tppVerifyCash(Map<String, Object> params) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Object tppAddBankCard(Map<String, Object> params) {
        final JxCardBindPlusModel model = new JxCardBindPlusModel();
        model.setAccountId(StringUtils.isNull(params.get("accountId")));
        model.setIdType(StringUtils.isNull(params.get("idType")));
        model.setIdNo(StringUtils.isNull(params.get("idNo")));
        model.setName(StringUtils.isNull(params.get("name")));
        model.setMobile(StringUtils.isNull(params.get("mobile")));
        model.setCardNo(StringUtils.isNull(params.get("cardNo")));
        model.setLastSrvAuthCode(StringUtils.isNull(params.get("lastSrvAuthCode")));
        model.setSmsCode(StringUtils.isNull(params.get("smsCode")));
        model.setUserIP(StringUtils.isNull(params.get("userIP")));
        model.setAcqRes(StringUtils.isNull(params.get("acqRes")));
        model.sign();
        model.directRequest();
        return model.getMapRespData();
    }

    @Override
    public Object tppDeleteBankBank(Map<String, Object> params) {
        final JxCardUnbindModel model = new JxCardUnbindModel();
        model.setAccountId(StringUtils.isNull(params.get("accountId")));
        model.setIdType(StringUtils.isNull(params.get("idType")));
        model.setIdNo(StringUtils.isNull(params.get("idNo")));
        model.setName(StringUtils.isNull(params.get("name")));
        model.setMobile(StringUtils.isNull(params.get("mobile")));
        model.setCardNo(StringUtils.isNull(params.get("cardNo")));
        model.setAcqRes(StringUtils.isNull(params.get("acqRes")));
        model.sign();
        model.directRequest();
        return model.getMapRespData();
    }

    @Override
    public Object tppFreeze(Map<String, Object> params) {
        final JxBalanceFreezeModel model = new JxBalanceFreezeModel();
        model.setAccountId(StringUtils.isNull(params.get("accountId")));
        model.setOrderId(StringUtils.isNull(params.get("orderId")));
        model.setTxAmount(StringUtils.isNull(params.get("txAmount")));
        model.setAcqRes(StringUtils.isNull(params.get("acqRes")));
        model.sign();
        model.directRequest();
        return model.getMapRespData();
    }

    @Override
    public Object tppUnFreeze(Map<String, Object> params) {
        final JxBalanceUnfreezeModel model = new JxBalanceUnfreezeModel();
        model.setAccountId(StringUtils.isNull(params.get("accountId")));
        model.setOrderId(StringUtils.isNull(params.get("orderId")));
        model.setTxAmount(StringUtils.isNull(params.get("txAmount")));
        model.setOrgOrderId(StringUtils.isNull(params.get("orgOrderId")));
        model.setAcqRes(StringUtils.isNull(params.get("acqRes")));
        model.sign();
        model.directRequest();
        return model.getMapRespData();
    }

    @Override
    public Object tppAutoTenderPlan(Map<String, Object> params) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Object tppCloseAutoTenderPlan(Map<String, Object> params) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Object tppAutoTender(Map<String, Object> params) {
        final JxBidAutoApplyModel model = new JxBidAutoApplyModel();
        model.setAccountId(StringUtils.isNull(params.get("accountId")));
//        model.setOrderId(StringUtils.isNull(params.get("orderId")));
        model.setTxAmount(StringUtils.isNull(params.get("txAmount")));
        model.setProductId(StringUtils.isNull(params.get("productId")));
//        model.setFrzFlag(StringUtils.isNull(params.get("frzFlag")));
        model.setContOrderId(StringUtils.isNull(params.get("contOrderId")));
        model.setAcqRes(StringUtils.isNull(params.get("acqRes")));
        model.sign();
        model.directRequest();
        return model.getMapRespData();
    }

    @Override
    public Object tppQueryUserBankCard(Map<String, Object> params) {
        final JxCardBindDetailsQueryModel model = new JxCardBindDetailsQueryModel();
        model.setAccountId(StringUtils.isNull(params.get("accountId")));
        model.setState(StringUtils.isNull(params.get("state")));
        model.setAcqRes(StringUtils.isNull(params.get("acqRes")));
        model.sign();
        model.directRequest();
        return model.getMapRespData();
    }

    @Override
    public Object tppUserLogin(Map<String, Object> params) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Object tppFssTrans(Map<String, Object> params) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Object tppTransfer(Map<String, Object> params) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Object tppCreditAssign(Map<String, Object> params) {
        final JxCreditInvestModel model = new JxCreditInvestModel();
        model.setAccountId(StringUtils.isNull(params.get("accountId")));
        model.setTxAmount(StringUtils.isNull(params.get("txAmount")));
        model.setTxFee(StringUtils.isNull(params.get("txFee")));
        model.setTsfAmount(StringUtils.isNull(params.get("tsfAmount")));
        model.setForAccountId(StringUtils.isNull(params.get("forAccountId")));
        model.setOrgOrderId(StringUtils.isNull(params.get("orgOrderId")));
        model.setOrgTxAmount(StringUtils.isNull(params.get("orgTxAmount")));
        model.setProductId(StringUtils.isNull(params.get("productId")));
        model.sign();
        return model;
    }

    @Override
    public Object tppExpChk(Map<String, Object> params) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Object tppWdcChk(Map<String, Object> params) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Object tppPpdChk(Map<String, Object> params) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Object tppInvestChk(Map<String, Object> params) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Object tppBatInvestCancle(Map<String, Object> params) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Object tppInvestCancle(Map<String, Object> params) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Object tppQueryTransStat(Map<String, Object> params) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Object tppUpdatePayPass(Map<String, Object> params) {
        if ("0".equals(StringUtils.isNull(params.get("payPwd")))) {
            JxPasswordSetModel model = new JxPasswordSetModel();
            model.setAccountId(StringUtils.isNull(params.get("accountId")));
            model.setIdType(StringUtils.isNull(params.get("idType")));
            model.setIdNo(StringUtils.isNull(params.get("idNo")));
            model.setName(StringUtils.isNull(params.get("name")));
            model.setMobile(StringUtils.isNull(params.get("mobile")));
//			model.setNotifyUrl(StringUtils.isNull(params.get("notifyUrl")));
            model.setAcqRes(StringUtils.isNull(params.get("acqRes")));
            model.sign();
            return model;
        } else {
            JxPasswordResetPlusModel model = new JxPasswordResetPlusModel();
            model.setAccountId(StringUtils.isNull(params.get("accountId")));
            model.setIdType(StringUtils.isNull(params.get("idType")));
            model.setIdNo(StringUtils.isNull(params.get("idNo")));
            model.setName(StringUtils.isNull(params.get("name")));
            model.setMobile(StringUtils.isNull(params.get("mobile")));
//			model.setNotifyUrl(StringUtils.isNull(params.get("notifyUrl")));
            model.setLastSrvAuthCode(StringUtils.isNull(params.get("lastSrvAuthCode")));
            model.setSmsCode(StringUtils.isNull(params.get("smsCode")));
            model.setAcqRes(StringUtils.isNull(params.get("acqRes")));
            model.sign();
            return model;
        }
    }

    @Override
    public Object tppUpdateMobileNo(Map<String, Object> params) {
        JxMobileModifyPlusModel model = new JxMobileModifyPlusModel();
        model.setAccountId(StringUtils.isNull(params.get("accountId")));
        model.setMobile(StringUtils.isNull(params.get("mobile")));
        model.setOption(JxbankConstant.JXBANK_OPTION_MODIFY);
        model.setLastSrvAuthCode(StringUtils.isNull(params.get("lastSrvAuthCode")));
        model.setSmsCode(StringUtils.isNull(params.get("smsCode")));
        model.setAcqRes(StringUtils.isNull(params.get("acqRes")));
        model.sign();
        model.directRequest();
        return model.getMapRespData();
    }

    @Override
    public Object tppAppUpdateBankCard(Map<String, Object> params) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Object tppUpdateBankCard(Map<String, Object> params) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Object tppSendUapMsg(Map<String, Object> params) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Object queryMerchantAccts(Map<String, Object> params) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Object experBonus(Map<String, Object> params) {
        final JxVoucherPayModel model = new JxVoucherPayModel();
        model.setAccountId(StringUtils.isNull(params.get("accountId")));
        model.setTxAmount(StringUtils.isNull(params.get("txAmount")));
        model.setForAccountId(StringUtils.isNull(params.get("forAccountId")));
        model.setDesLineFlag(StringUtils.isNull(params.get("desLineFlag")));
        model.setDesLine(StringUtils.isNull(params.get("desLine")));
        model.setAcqRes(StringUtils.isNull(params.get("acqRes")));
        model.sign();
        model.directRequest();
        return model;
    }

    @Override
    public Object queryUserInf(Map<String, Object> params) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Object tppExperCap(Map<String, Object> params) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Object tppExperRealeName(Map<String, Object> params) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Object voucherPayCancel(Map<String, Object> params) {
        if ("0".equals(StringUtils.isNull(params.get("delay")))) {//当天撤销
            final JxVoucherPayCancelModel model = new JxVoucherPayCancelModel();
            model.setAccountId(StringUtils.isNull(params.get("accountId")));
            model.setTxAmount(StringUtils.isNull(params.get("txAmount")));
            model.setForAccountId(StringUtils.isNull(params.get("forAccountId")));
            model.setOrgTxDate(StringUtils.isNull(params.get("orgTxDate")));
            model.setOrgTxTime(StringUtils.isNull(params.get("orgTxTime")));
            model.setOrgSeqNo(StringUtils.isNull(params.get("orgSeqNo")));
            model.setAcqRes(StringUtils.isNull(params.get("acqRes")));
            model.sign();
            model.directRequest();
            return model.getMapRespData();
        } else {
            final JxVoucherPayDelayCancelModel model = new JxVoucherPayDelayCancelModel();
            model.setAccountId(StringUtils.isNull(params.get("accountId")));
            model.setTxAmount(StringUtils.isNull(params.get("txAmount")));
            model.setForAccountId(StringUtils.isNull(params.get("forAccountId")));
            model.setDesLineFlag(StringUtils.isNull(params.get("desLineFlag")));
            model.setOrgTxDate(StringUtils.isNull(params.get("orgTxDate")));
            model.setOrgTxTime(StringUtils.isNull(params.get("orgTxTime")));
            model.setOrgSeqNo(StringUtils.isNull(params.get("orgSeqNo")));
            model.setDesLine(StringUtils.isNull(params.get("desLine")));
            model.setAcqRes(StringUtils.isNull(params.get("acqRes")));
            model.sign();
            model.directRequest();
            return model.getMapRespData();
        }
    }

    @Override
    public Object batchDetailsQuery(Map<String, Object> params) {
        JxBatchDetailsQueryModel model = new JxBatchDetailsQueryModel();
        model.setBatchTxDate(StringUtils.isNull(params.get("batchTxDate")));
        model.setBatchNo(StringUtils.isNull(params.get("batchNo")));
        model.setType(StringUtils.isNull(params.get("type")));
        model.setPageNum(StringUtils.isNull(params.get("pageNum")));
        model.setPageSize(StringUtils.isNull(params.get("pageSize")));
        model.sign();
        model.directRequest();
        return model.getMapRespData();
    }


    @Override
    public Object queryCorpration(Map<String, Object> params) {
        JxCorprationQueryModel model = new JxCorprationQueryModel();
        model.setAccountId(StringUtils.isNull(params.get("accountId")));
        model.sign();
        model.directRequest();
        return model.getMapRespData();
    }


    @Override
    public Object fundTransQuery(Map<String, Object> params) {
        JxFundTransQueryModel model = new JxFundTransQueryModel();
        model.setAccountId(StringUtils.isNull(params.get("accountId")));
        model.setOrgTxDate(StringUtils.isNull(params.get("orgTxDate")));
        model.setOrgTxTime(StringUtils.isNull(params.get("orgTxTime")));
        model.setOrgSeqNo(StringUtils.isNull(params.get("orgSeqNo")));
        model.sign();
        model.directRequest();
        return model.getMapRespData();
    }

    @Override
    public Object batchVoucherPay(Map<String, Object> params) {
        JxBatchVoucherPayModel model = new JxBatchVoucherPayModel();
        model.setBatchNo(StringUtils.isNull(params.get("batchNo")));
        model.setSeqNo(StringUtils.isNull(params.get("batchNo")));
        model.setTxAmount(StringUtils.isNull(params.get("txAmount")));
        model.setTxCounts(StringUtils.isNull(params.get("txCounts")));
        model.setSubPacks(JSON.toJSONString(params.get("subPacks")));
        model.sign();
        model.directRequestBatch();
        return model.getMapRespData();
    }

    @Override
    public Object batchVoucherDetailsQuery(Map<String, Object> params) {
        JxBatchVoucherDetailsQueryModel model = new JxBatchVoucherDetailsQueryModel();
        model.setBatchTxDate(StringUtils.isNull(params.get("batchTxDate")));
        model.setBatchNo(StringUtils.isNull(params.get("batchNo")));
        model.setType(StringUtils.isNull(params.get("type")));
        model.setPageNum(StringUtils.isNull(params.get("pageNum")));
        model.setPageSize(StringUtils.isNull(params.get("pageSize")));
        model.sign();
        model.directRequest();
        return model.getMapRespData();
    }

    @Override
    public Object batchBailRepay(Map<String, Object> params) {
        JxBatchBailRepayModel model = new JxBatchBailRepayModel();
        model.setBatchNo(StringUtils.isNull(params.get("batchNo")));
        model.setSeqNo(StringUtils.isNull(params.get("batchNo")));
        model.setAccountId(StringUtils.isNull(params.get("accountId")));
        model.setProductId(StringUtils.isNull(params.get("productId")));
        model.setTxAmount(StringUtils.isNull(params.get("txAmount")));
        model.setTxCounts(StringUtils.isNull(params.get("txCounts")));
        model.setSubPacks(JSON.toJSONString(params.get("subPacks")));
        model.sign();
        model.directRequestBatch();
        return model.getMapRespData();
    }

    @Override
    public Object accountDetailsQuery(Map<String, Object> params) {
        JxAccountDetailsQueryModel model = new JxAccountDetailsQueryModel();
        model.setAccountId(StringUtils.isNull(params.get("accountId")));
        model.setStartDate(StringUtils.isNull(params.get("startDate")));
        model.setEndDate(StringUtils.isNull(params.get("endDate")));
        model.setType(StringUtils.isNull(params.get("type")));
        model.setTranType(StringUtils.isNull(params.get("tranType")));
        model.setPageNum(StringUtils.isNull(params.get("pageNum")));
        model.setPageSize(StringUtils.isNull(params.get("pageSize")));
        model.sign();
        model.directRequest();
        return model.getMapRespData();
    }

    @Override
    public Object creditDetailsQuery(Map<String, Object> map) {
        JxCreditDetailsQueryModel model = new JxCreditDetailsQueryModel();
        model.setAccountId(StringUtils.isNull(map.get("accountId")));
        model.setProductId(StringUtils.isNull(map.get("productId")));
        model.setState(StringUtils.isNull(map.get("state")));
        model.setStartDate(StringUtils.isNull(map.get("startDate")));
        model.setEndDate(StringUtils.isNull(map.get("endDate")));
        model.setPageNum(StringUtils.isNull(map.get("pageNum")));
        model.setPageSize(StringUtils.isNull(map.get("pageSize")));
        model.sign();
        model.directRequest();
        return model.getMapRespData();
    }


    @Override
    public Object bidApplyQuery(Map<String, Object> map) {
        JxBidApplyQueryModel model = new JxBidApplyQueryModel();
        model.setAccountId(StringUtils.isNull(map.get("accountId")));
        model.setOrgOrderId(StringUtils.isNull(map.get("orgOrderId")));
        model.sign();
        model.directRequest();
        return model.getMapRespData();
    }


    @Override
    public Object creditInvestQuery(Map<String, Object> map) {
        JxCreditInvestQueryModel model = new JxCreditInvestQueryModel();
        model.setAccountId(StringUtils.isNull(map.get("accountId")));
        model.setOrgOrderId(StringUtils.isNull(map.get("orgOrderId")));
        model.sign();
        model.directRequest();
        return model.getMapRespData();
    }


    @Override
    public Object creditAuthQuery(Map<String, Object> map) {
        JxCreditAuthQueryModel model = new JxCreditAuthQueryModel();
        model.setAccountId(StringUtils.isNull(map.get("accountId")));
        model.setType(StringUtils.isNull(map.get("type")));
        model.sign();
        model.directRequest();
        return model.getMapRespData();
    }


    @Override
    public Object freezeDetailsQuery(Map<String, Object> map) {
        // TODO Auto-generated method stub
        return null;
    }


    @Override
    public Object debtDetailsQuery(Map<String, Object> map) {
        JxDebtDetailsQueryModel model = new JxDebtDetailsQueryModel();
        model.setAccountId(StringUtils.isNull(map.get("accountId")));
        model.setProductId(StringUtils.isNull(map.get("productId")));
        model.setStartDate(StringUtils.isNull(map.get("startDate")));
        model.setEndDate(StringUtils.isNull(map.get("endDate")));
        model.setPageNum(StringUtils.isNull(map.get("pageNum")));
        model.setPageSize(StringUtils.isNull(map.get("pageSize")));
        model.sign();
        model.directRequest();
        return model.getMapRespData();
    }


    @Override
    public Object passwordSetQuery(Map<String, Object> map) {
        JxPasswordSetQueryModel model = new JxPasswordSetQueryModel();
        model.setAccountId(StringUtils.isNull(map.get("accountId")));
        model.sign();
        model.directRequest();
        return model.getMapRespData();
    }

    @Override
    public Object batchCreditEnd(Map<String, Object> params) {
        JxBatchCreditEndModel model = new JxBatchCreditEndModel();
        model.setBatchNo(StringUtils.isNull(params.get("batchNo")));
        model.setSeqNo(StringUtils.isNull(params.get("batchNo")));
        model.setTxCounts(StringUtils.isNull(params.get("txCounts")));
        model.setSubPacks(JSON.toJSONString(params.get("subPacks")));
        model.sign();
        model.directRequestBatch();
        return model.getMapRespData();
    }


    @Override
    public Object batchQuery(Map<String, Object> params) {
        JxBatchQueryModel model = new JxBatchQueryModel();
        model.setBatchTxDate(StringUtils.isNull(params.get("batchTxDate")));
        model.setBatchNo(StringUtils.isNull(params.get("batchNo")));
        model.sign();
        model.directRequestBatch();
        return model.getMapRespData();
    }

    @Override
    public Object autoCreditInvestAuthPlus(Map<String, Object> params) {
        final JxAutoCreditInvestAuthPlusModel model = new JxAutoCreditInvestAuthPlusModel();
        model.setAccountId(StringUtils.isNull(params.get("accountId")));
        model.setOrderId(StringUtils.isNull(params.get("orderId")));
        model.setLastSrvAuthCode(StringUtils.isNull(params.get("lastSrvAuthCode")));
        model.setSmsCode(StringUtils.isNull(params.get("smsCode")));
        model.setAcqRes(StringUtils.isNull(params.get("acqRes")));
        model.sign();
        return model;
    }
    
    

    @Override
    public Object batchRepayBail(Map<String, Object> params) {
        JxBatchRepayBailModel model = new JxBatchRepayBailModel();
        model.setBatchNo(StringUtils.isNull(params.get("batchNo")));
        model.setSeqNo(StringUtils.isNull(params.get("batchNo")));
        model.setTxAmount(StringUtils.isNull(params.get("txAmount")));
        model.setTxCounts(StringUtils.isNull(params.get("txCounts")));
        model.setSubPacks(JSON.toJSONString(params.get("subPacks")));
        model.sign();
        model.directRequestBatch();
        return model.getMapRespData();
    }


	@Override
	public Object downTradeDetail(Map<String, Object> params) {
		String date = StringUtils.isNull(params.get("date"));
		JxFileTradeDetailModel model = null;
		if (StringUtils.isBlank(date)) {
			model = new JxFileTradeDetailModel();
		} else {
			model = new JxFileTradeDetailModel(date);
		}
		model.sign();
		model.directRequest();
		return model.getMapRespData();
	}


	@Override
	public Object downTradeDetailAll(Map<String, Object> params) {
		String date = StringUtils.isNull(params.get("date"));
		JxFileTradeDetailAllModel model = null;
		if (StringUtils.isBlank(date)) {
			model = new JxFileTradeDetailAllModel();
		} else {
			model = new JxFileTradeDetailAllModel(date);
		}
		
		model.sign();
		model.directRequest();
		return model.getMapRespData();
	}


	@Override
	public Object autoCreditInvestAuthCancel(Map<String, Object> params) {
	    final JxAutoCreditInvestAuthCancelModel model = new JxAutoCreditInvestAuthCancelModel();
        model.setAccountId(StringUtils.isNull(params.get("accountId")));
        model.setOrderId(StringUtils.isNull(params.get("orderId")));
        model.setOrgOrderId(StringUtils.isNull(params.get("orgOrderId")));
        model.setAcqRes(StringUtils.isNull(params.get("acqRes")));
        model.sign();
        model.directRequest();
		return model.getMapRespData();
	}
}
