package com.rd.ifaes.core.tpp.service.tpp;

import com.rd.ifaes.core.bond.model.BondInvestOtherModel;
import com.rd.ifaes.core.tpp.model.jx.JxCreditInvestModel;

public interface JxbankBondInvestService {

    /**
     * 债权投资回调处理
     * @param model
     */
    void bondInvestHandle(JxCreditInvestModel model);

    /**
     * 债权投资成功
     * @param model
     */
    void bondInvestSuccess(JxCreditInvestModel model);

    /**
     * 债权投资失败
     * @param model
     */
    void bondInvestFail(JxCreditInvestModel model);

    /**
     * 债权投资其他业务处理
     * @param model
     */
    void doBondInvestOtherService(final BondInvestOtherModel model);

    /**
     * 转让后更新bond_invest和bond_collection的investId;
     * 因为转让成功后会生成新的project_invest记录 原先的对应不上了
     * @param projectId
     */
    void updateInvestIdAfterBond(String projectId);
}
