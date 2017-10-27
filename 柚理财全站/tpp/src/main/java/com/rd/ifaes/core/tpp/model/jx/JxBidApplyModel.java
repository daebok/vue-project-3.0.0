package com.rd.ifaes.core.tpp.model.jx;

import com.rd.ifaes.core.core.util.ConfigUtils;
import com.rd.ifaes.core.core.util.OrderNoUtils;
import com.rd.ifaes.core.tpp.util.JxConfig;

public class JxBidApplyModel extends JxBaseModel {

    //电子账号	A	19	M	存管平台分配的账号
    private String accountId;
    //订单号	A	30	M	由P2P生成，必须保证唯一
    private String orderId;
    //交易金额	N	12,2	M	投标金额
    private String txAmount;
    //标的号	A	40	M	标的号
    private String productId;
    //是否冻结金额	A	1	M	0-不冻结 1-冻结 为避免放款失败，应填1
    private String frzFlag;
    //是否使用红包	A	1	C	0-不使用红包（若不出现，默认为0）1-使用红包
    private String bonusFlag;
    //抵扣红包金额	N	9,2	C	若bonusFlag为1，必须出现
    private String bonusAmount;
    //备注	A	200	C
    private String remark;
    //忘记密码跳转	A	200	M	忘记密码的跳转URL
    private String forgotPwdUrl;
    //前台跳转链接	A	200	C	交易后台跳转的前台URL
    private String retUrl;
    //后台通知链接	A	200	M	后台通知URL，【响应参数】返回到该URL，收到后返回“success”
    private String notifyUrl;


    //姓名	A	60	C
    private String name;
    //授权码	A	20	C	债权授权码（后台响应）
    private String authCode;


    /**
     * 请求参数
     */
    private String[] directRequestParamNames = new String[]{"accountId", "orderId", "txAmount", "productId", "frzFlag", "bonusFlag",
            "bonusAmount", "remark", "forgotPwdUrl", "retUrl", "notifyUrl"};
    /**
     * 响应参数
     */
    private String[] directResponseParamNames = new String[]{"accountId","orderId","txAmount","productId","name","authCode"};

    public JxBidApplyModel() {
        super();
        super.setTxCode(JxConfig.BID_APPLY);
        this.orderId = OrderNoUtils.getSerialNumber();
        this.frzFlag = "1";
        this.retUrl = ConfigUtils.getValue("notify_url") + "/jxbank/bidApply/return.html";
        this.forgotPwdUrl = ConfigUtils.getValue("notify_url") + "/member/security/modifyTransactionPwd.html";
        this.notifyUrl = ConfigUtils.getValue("notify_url") + "/jxbank/bidApply/notify.html";
        setSummitUrl(getBaseUrl() + "/p2p/page/bidapply");
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

    public String getFrzFlag() {
        return frzFlag;
    }

    public void setFrzFlag(String frzFlag) {
        this.frzFlag = frzFlag;
    }

    public String getBonusFlag() {
        return bonusFlag;
    }

    public void setBonusFlag(String bonusFlag) {
        this.bonusFlag = bonusFlag;
    }

    public String getBonusAmount() {
        return bonusAmount;
    }

    public void setBonusAmount(String bonusAmount) {
        this.bonusAmount = bonusAmount;
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
