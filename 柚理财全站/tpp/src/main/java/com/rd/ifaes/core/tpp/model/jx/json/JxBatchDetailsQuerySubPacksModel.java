package com.rd.ifaes.core.tpp.model.jx.json;

public class JxBatchDetailsQuerySubPacksModel {

    //电子账号	A	19	M	电子账号
    private String accountId;
    //订单号	A	30	M
    private String orderId;
    //交易金额	N	12,2	M
    private String txAmount;
    //对手电子账号	A	19	M
    private String forAccountId;
    //标的号	A	40	M
    private String productId;
    //授权码	A	20	C	批次交易代码为批次投资人购买债权、批次担保账户代偿时，该字段代表交易成功生成的买入方授权码
    private String authCode;
    //交易状态	A	1	C	S-成功F-失败A-待处理D-正在处理C-撤销
    private String txState;
    //失败描述	A	60	C	txState为F时有效
    private String failMsg;

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

    public String getForAccountId() {
        return forAccountId;
    }

    public void setForAccountId(String forAccountId) {
        this.forAccountId = forAccountId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getAuthCode() {
        return authCode;
    }

    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }

    public String getTxState() {
        return txState;
    }

    public void setTxState(String txState) {
        this.txState = txState;
    }

    public String getFailMsg() {
        return failMsg;
    }

    public void setFailMsg(String failMsg) {
        this.failMsg = failMsg;
    }
}
