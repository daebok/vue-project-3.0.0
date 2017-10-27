package com.rd.ifaes.common.dict;

/**
 * 三方交易记录相关
 *
 * @author FangJun
 * @date 2016年7月18日
 */
public enum TppEnum implements BaseEnum<String, String> {

    /**
     * 状态：未处理,0
     */
    STATUS_UNDO("未处理", "0"),
    /**
     * 状态：处理成功,1
     */
    STATUS_SUCEESS("处理成功", "1"),
    /**
     * 状态：处理失败,2
     */
    STATUS_FAIL("处理失败", "2"),
    /**
     * 状态：已查询,2
     */
    STATUS_QUERY("已查询", "3"),
    //----------SERVICE_TYPE 服务类型------------------------------------------------------
    /**
     * 服务类型：项目取消,projectCancel
     */
    SERVICE_TYPE_PROJECT_CANCEL("项目取消", "projectCancel"),
    /**
     * 服务类型：放款,loan
     */
    SERVICE_TYPE_LOAN("放款", "loan"),
    /**
     * 服务类型：批次放款,batch_loan
     */
    SERVICE_TYPE_BATCH_LOAN("批次放款", "batch_loan"),
    /**
     * 服务类型：还款,repay
     */
    SERVICE_TYPE_REPAY("还款", "repay"),
    /**
     * 服务类型：批次还款,repay
     */
    SERVICE_TYPE_BATCH_REPAY("批次还款", "batch_repay"),
    /**
     * 服务类型：担保垫付还款,bail_repay
     */
    SERVICE_TYPE_BAIL_REPAY("担保垫付", "bail_repay"),
    /**
     * 服务类型：批次担保垫付还款,batch_bail_repay
     */
    SERVICE_TYPE_BATCH_BAIL_REPAY("批次担保垫付", "batch_bail_repay"),
    /**
     * 服务类型：融资人还担保账户垫款,repay_bail
     */
    SERVICE_TYPE_REPAY_BAIL("还担保垫款", "repay_bail"),
    /**
     * 服务类型：批次融资人还担保账户垫款,batch_repay_bail
     */
    SERVICE_TYPE_BATCH_REPAY_BAIL("批次还担保垫款", "batch_repay_bail"),
    /**
     * 服务类型：冻结,freeze
     */
    SERVICE_TYPE_FREEZE("冻结", "freeze"),
    /**
     * 服务类型：解冻,unfreeze
     */
    SERVICE_TYPE_UNFREEZE("解冻", "unfreeze"),
    /**
     * 服务类型：红包发放(还款加息时使用)
     */
    SERVICE_TYPE_VOUCHER("红包发放", "voucher"),
    /**
     * 服务类型：批次红包发放(还款加息时使用)
     */
    SERVICE_TYPE_BATCH_VOUCHER("批次红包发放", "batch_voucher"),
    /**
     * 服务类型：结束债权
     */
    SERVICE_TYPE_CREDIT_END("结束债权", "credit_end"),
    /**
     * 服务类型：批次结束债权
     */
    SERVICE_TYPE_BATCH_CREDIT_END("批次结束债权", "batch_credit_end"),

    //---------TPP_TYPE 三方类型---------------------------------------------
    /**
     * 三方类型：撤销投资退回资金,investFail
     */
    TPP_TYPE_INVEST_FAIL("解冻", "investFail"),
    /**
     * 三方类型：批次放款,batch_loan
     */
    TPP_TYPE_BATCH_LOAN("批次放款", "batch_loan"),
    /**
     * 三方类型：放款,loan
     */
    TPP_TYPE_LOAN("放款", "loan"),
    /**
     * 三方类型： 冻结,freeze
     */
    TPP_TYPE_FREEZE("冻结", "freeze"),
    /**
     * 三方类型：解冻,loan
     */
    TPP_TYPE_UNFREEZE("解冻", "unFreeze"),
    /**
     * 三方类型：转账,transfer
     */
    TPP_TYPE_TRANSFER("转账", "transfer"),
    /**
     * 三方类型：红包,experCap
     */
    TPP_TYPE_EXPERCAP("红包", "experCap"),
    /**
     * 三方类型：还款,repay
     */
    TPP_TYPE_REPAY("还款", "repay"),
    /**
     * 三方类型：批次还款,repay
     */
    TPP_TYPE_BATCH_REPAY("批次还款", "batch_repay"),

    /**
     * 服务类型：担保垫付还款,repay
     */
    TPP_TYPE_BAIL_REPAY("担保垫付", "bail_repay"),
    /**
     * 服务类型：批次担保垫付还款,repay
     */
    TPP_TYPE_BATCH_BAIL_REPAY("批次担保垫付", "batch_bail_repay"),
    /**
     * 服务类型：融资人还担保账户垫款,repay_bail
     */
    TPP_TYPE_REPAY_BAIL("还担保垫款", "repay_bail"),
    /**
     * 服务类型：批次融资人还担保账户垫款,batch_repay_bail
     */
    TPP_TYPE_BATCH_REPAY_BAIL("批次还担保垫款", "batch_repay_bail"),
    /**
     * 服务类型：红包发放(还款加息时使用)
     */
    TPP_TYPE_VOUCHER("红包发放", "voucher"),
    /**
     * 服务类型：批次红包发放(还款加息时使用)
     */
    TPP_TYPE_BATCH_VOUCHER("批次红包发放", "batch_voucher"),
    /**
     * 服务类型：结束债权
     */
    TPP_TYPE_CREDIT_END("结束债权", "credit_end"),
    /**
     * 服务类型：批次结束债权
     */
    TPP_TYPE_BATCH_CREDIT_END("批次结束债权", "batch_credit_end"),
    ;

    // 字典项的中文名称
    private String name;
    // 字典项的值
    private String value;

    private TppEnum(String name, String value) {
        this.name = name;
        this.value = value;
    }

    /**
     * 获取label
     *
     * @return
     */
    @Override
    public String getName() {
        return this.name;
    }

    /**
     * 获取值
     *
     * @return
     */
    @Override
    public String getValue() {
        return this.value;
    }

    /**
     * 根据VALUE比较
     *
     * @param obj 目标值
     * @return
     */
    public boolean eq(Object obj) {
        return this.value.equals(obj);
    }
}