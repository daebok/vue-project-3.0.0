package com.rd.ifaes.core.tpp.model.jx.json;

public class JxBatchVoucherDetailsQuerySubPacksModel {

    //电子账号	A	19	M	电子账号
    private String accountId;
    //订单号	A	30	M
    private String orderId;
    //交易金额	N	12,2	M
    private String txAmount;
    //对手电子账号	A	19	M
    private String forAccountId;
    //红包类型	A	3	M	001-红包发放 002-加息券收益发放
    private String voucherType;
    //持卡人姓名	A	30	M
    private String name;
    //自定义交易描述	A	20	C
    private String tradeDesc;
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

    public String getVoucherType() {
        return voucherType;
    }

    public void setVoucherType(String voucherType) {
        this.voucherType = voucherType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTradeDesc() {
        return tradeDesc;
    }

    public void setTradeDesc(String tradeDesc) {
        this.tradeDesc = tradeDesc;
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
