package com.rd.ifaes.core.tpp.service.tpp;

import com.rd.ifaes.common.orm.Page;
import com.rd.ifaes.common.rabbit.model.MqAutoInvestModel;
import com.rd.ifaes.core.tpp.domain.TppTrade;
import com.rd.ifaes.core.tpp.model.jx.*;
import com.rd.ifaes.core.tpp.model.jx.json.JxAccountDetailsQuerySubPacksModel;

import java.util.Map;

public interface JxbankService {

    /**
     * 放款全部成功
     * @param tradeNo
     */
    void LoanSuccess(TppTrade tppTrade);

    /**
     * 批次交易详情处理
     * @param tppTrade 批次交易主记录
     * @return
     */
    String jxBatchDetailsHandle(TppTrade tppTrade);

    /**
     *
     * 重置密码
     * @author jxx
     * @date 2017年8月31日
     * @param model
     */
    void resetPwd(JxPasswordResetPlusModel model);

    /**
     *
     * 设置密码
     * @author jxx
     * @date 2017年8月31日
     * @param model
     */
    void setPwd(JxPasswordSetModel model);

    /**
     *
     * 提现处理
     * @author jxx
     * @date 2017年8月31日
     * @param model
     */
    void doCash(JxWithdrawModel model);

    /**
     * 
     * 自动投标
     * @author jxx
     * @date 2017年8月31日
     * @param model
     * @return
     */
    boolean doAutoInvest(final MqAutoInvestModel model);

    /**
     * 还款全部成功
     * @param tradeNo
     */
    void repaySuccess(TppTrade tppTrade);

    /**
     * 批次红包详情处理(还款加息）
     * @param tppTrade 批次交易主记录
     * @return
     */
    String jxBatchVoucherDetailsHandle(TppTrade tppTrade);

    /**
     * 还款加息全部成功
     * @param tradeNo
     */
    void repayVoucherSuccess(String tradeNo);

    /**
     * 电子账户资金交易明细查询
     * @param map
     */
    Page<JxAccountDetailsQuerySubPacksModel> accountDetailsQuery(Map<String, Object> map);

    /**
     * 批次结束债权
     * @param projectId
     * @return
     */
    String batchCreditEnd(String projectId);

    /**
     * 结束债权全部成功
     * @param tpp
     */
    void creditEndSuccess(TppTrade tpp);

    /**
     * 担保垫付回调处理
     * @param model
     */
    void bailRepayHandle(JxBatchBailRepayModel model);

    /**
     * 还垫付款全部成功
     * @param tpp
     */
    void repayBailSuccess(TppTrade tpp);
}
