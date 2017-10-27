package com.rd.ifaes.core.tpp.service.tpp;

import com.rd.ifaes.core.tpp.model.jx.JxBidApplyModel;

public interface JxbankInvestService {

    /**
     * 投资回调处理
     * @param jxModel
     */
    void investHandle(JxBidApplyModel jxModel);

    /**
     * 投资无效
     * @param jxModel
     * @param investId
     * @param preStatus
     */
    void invalidInvestHandle(JxBidApplyModel jxModel, String investId, String preStatus);

    /**
     * 投资超时
     * @param jxModel
     */
    void investTimeout(JxBidApplyModel jxModel);

    /**
     * 投资成功
     * @param jxModel
     */
    void investSuccess(JxBidApplyModel jxModel);

    /**
     * 投资失败
     * @param jxModel
     */
    void investPayFail(JxBidApplyModel jxModel);

}
