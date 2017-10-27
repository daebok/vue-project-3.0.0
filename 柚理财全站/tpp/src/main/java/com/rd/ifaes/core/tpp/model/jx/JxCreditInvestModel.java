package com.rd.ifaes.core.tpp.model.jx;

import com.rd.ifaes.core.core.util.ConfigUtils;
import com.rd.ifaes.core.core.util.OrderNoUtils;
import com.rd.ifaes.core.tpp.util.JxConfig;

/**
 * 2.5.2投资人购买债权
 *
 * @author jxx
 * @version 3.0
 * @date 2017年8月18日
 */
public class JxCreditInvestModel extends JxBaseModel {
    private String accountId; //电子账号	A	19	M	购买方账号
    private String orderId; //订单号	A	30	M	由P2P生成，必须保证唯一
    private String txAmount; //交易金额	N	12,2	M	成交价格，购买方实际付出金额
    private String txFee; //手续费	N	12,2	C	向卖出方收取的手续费
    private String tsfAmount; //转让金额	N	12,2	M	卖出的债权金额
    private String forAccountId; //对手电子账号	A	19	M	卖出方账号
    private String orgOrderId; //原订单号	A	30	M	卖出方投标的原订单号（或卖出方购买债权的原订单号）
    private String orgTxAmount; //原交易金额	N	12,2	M	卖出方投标的原交易金额（或卖出方购买债权的原转让金额）
    private String productId; //标的号	A	40	M	原标的号
    private String remark; //备注	A	200	C
    private String forgotPwdUrl; //忘记密码跳转	A	200	C	忘记密码的跳转URL
    private String retUrl; //前台跳转链接	A	200	C	交易后台跳转的前台URL
    private String notifyUrl; //后台通知链接	A	200	M	【响应参数】会返回到该URL，平台收到后请返回“success”


    private String name; //姓名	A	60	C	买入方姓名
    private String authCode; //授权码	A	20	C	买入方获得的债权授权码


    /**
     * 请求参数
     */
    private String[] directRequestParamNames = new String[]{"accountId", "orderId", "txAmount", "txFee", "tsfAmount",
            "forAccountId", "orgOrderId", "orgTxAmount", "productId", "remark", "forgotPwdUrl", "retUrl", "notifyUrl"};

    /**
     * 响应参数
     */
    private String[] directResponseParamNames = new String[]{"accountId", "orderId", "txAmount", "tsfAmount", "productId",
            "name", "authCode"};

    public JxCreditInvestModel() {
        super();
        super.setTxCode(JxConfig.CREDIT_INVEST);
        this.orderId = OrderNoUtils.getSerialNumber();
        this.retUrl = ConfigUtils.getValue("notify_url") + "/jxbank/creditInvest/return.html";
        this.forgotPwdUrl = ConfigUtils.getValue("notify_url") + "/member/security/modifyTransactionPwd.html";
        this.notifyUrl = ConfigUtils.getValue("notify_url") + "/jxbank/creditInvest/notify.html";
        setSummitUrl(getBaseUrl() + "/p2p/page/creditInvest");
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getTxAmount() {
        return txAmount;
    }

    public void setTxAmount(String txAmount) {
        this.txAmount = txAmount;
    }

    public String getTxFee() {
        return txFee;
    }

    public void setTxFee(String txFee) {
        this.txFee = txFee;
    }

    public String getTsfAmount() {
        return tsfAmount;
    }

    public void setTsfAmount(String tsfAmount) {
        this.tsfAmount = tsfAmount;
    }

    public String getForAccountId() {
        return forAccountId;
    }

    public void setForAccountId(String forAccountId) {
        this.forAccountId = forAccountId;
    }

    public String getOrgOrderId() {
        return orgOrderId;
    }

    public void setOrgOrderId(String orgOrderId) {
        this.orgOrderId = orgOrderId;
    }

    public String getOrgTxAmount() {
        return orgTxAmount;
    }

    public void setOrgTxAmount(String orgTxAmount) {
        this.orgTxAmount = orgTxAmount;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getForgotPwdUrl() {
        return forgotPwdUrl;
    }

    public void setForgotPwdUrl(String forgotPwdUrl) {
        this.forgotPwdUrl = forgotPwdUrl;
    }

    public String getRetUrl() {
        return retUrl;
    }

    public void setRetUrl(String retUrl) {
        this.retUrl = retUrl;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthCode() {
        return authCode;
    }

    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }

    @Override
    public String[] getDirectRequestParamNames() {
        return directRequestParamNames;
    }

    @Override
    public void setDirectRequestParamNames(String[] directRequestParamNames) {
        this.directRequestParamNames = directRequestParamNames;
    }

    @Override
    public String[] getDirectResponseParamNames() {
        return directResponseParamNames;
    }

    @Override
    public void setDirectResponseParamNames(String[] directResponseParamNames) {
        this.directResponseParamNames = directResponseParamNames;
    }


}
