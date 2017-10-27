package com.rd.ifaes.core.tpp.model.jx;

import com.rd.ifaes.core.core.util.OrderNoUtils;
import com.rd.ifaes.core.tpp.util.JxConfig;

public class JxBidCancelModel extends JxBaseModel {
    //电子账号	A	19	M	存管平台分配的账号
    private String accountId;
    //订单号	A	30	M	由P2P生成，必须保证唯一
    private String orderId;
    //交易金额	N	12,2	M	原投标金额
    private String txAmount;
    //标的号	A	40	M	原标的号
    private String productId;
    //原订单号	A	30	M	原投标的订单号
    private String orgOrderId;

    /**
     * 请求参数
     */
    private String[] directRequestParamNames = new String[]{"accountId", "orderId", "txAmount", "productId", "orgOrderId"};
    /**
     * 响应参数
     */
    private String[] directResponseParamNames = new String[]{"accountId", "orderId", "txAmount", "productId", "name"};

    public JxBidCancelModel(){
        super();
        super.setTxCode(JxConfig.BID_CANCEL);
        this.orderId = OrderNoUtils.getSerialNumber();
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

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getOrgOrderId() {
        return orgOrderId;
    }

    public void setOrgOrderId(String orgOrderId) {
        this.orgOrderId = orgOrderId;
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
