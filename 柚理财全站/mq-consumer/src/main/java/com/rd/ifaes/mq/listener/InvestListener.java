package com.rd.ifaes.mq.listener;

import com.rd.ifaes.common.rabbit.model.MqInvestModel;
import com.rd.ifaes.core.core.constant.MqConstant;
import com.rd.ifaes.core.tpp.service.tpp.JxbankBondInvestService;
import com.rd.ifaes.core.tpp.service.tpp.JxbankInvestService;
import com.rd.ifaes.mq.MqListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 投资队列监听
 *
 * @author lh
 * @version 3.0
 * @since 2016-7-14
 */
@Component
public class InvestListener implements MqListener<MqInvestModel> {

    private static final Logger LOGGER = LoggerFactory.getLogger(InvestListener.class);

    @Autowired
    private JxbankInvestService jxbankInvestService;
    @Autowired
    private JxbankBondInvestService jxbankBondInvestService;

    @Override
    public void listen(MqInvestModel model) {
        LOGGER.info("投资队列监听，处理：{}", model.toString());

        if (MqConstant.OPERATE_INVEST_SUCCESS.equals(model.getOperate())) {
            jxbankInvestService.investSuccess(model.getJxBidApplyModel());
        } else if (MqConstant.OPERATE_INVEST_PAY_FAIL.equals(model.getOperate())) {
            jxbankInvestService.investPayFail(model.getJxBidApplyModel());
        } else if (MqConstant.OPERATE_INVEST_FAIL.equals(model.getOperate())) {
//            ufxInvestService.ufxInvestFailHandle(model.getInvestFailModel());
        } else if (MqConstant.OPERATE_INVEST_TIMEOUT.equals(model.getOperate())) {
            jxbankInvestService.investTimeout(model.getJxBidApplyModel());
        } else if (MqConstant.OPERATE_BOND_INVEST.equals(model.getOperate())) {
            jxbankBondInvestService.bondInvestSuccess(model.getJxCreditInvestModel());
        } else if (MqConstant.OPERATE_BOND_INVEST_FAIL.equals(model.getOperate())) {
            jxbankBondInvestService.bondInvestFail(model.getJxCreditInvestModel());
        } else if (MqConstant.OPERATE_BOND_INVEST_OTHER.equals(model.getOperate())) {
            jxbankBondInvestService.doBondInvestOtherService(model.getBondInvestOtherModel());
        } else if (MqConstant.OPERATE_UPDATE_INVEST_ID.equals(model.getOperate())) {
            jxbankBondInvestService.updateInvestIdAfterBond(model.getProjectId());
        }
    }
}
