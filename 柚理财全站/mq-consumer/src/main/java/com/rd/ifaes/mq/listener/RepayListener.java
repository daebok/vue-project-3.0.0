/**
 * 互联网金融交易系统ifaes (Internet Finance  Assets  Exchange  System)
 * Copyright (c) 2016 杭州融都科技股份有限公司 版权所有
 */
package com.rd.ifaes.mq.listener;

import com.rd.ifaes.common.rabbit.model.MqRepayModel;
import com.rd.ifaes.core.core.constant.MqConstant;
import com.rd.ifaes.core.tpp.service.tpp.JxbankService;
import com.rd.ifaes.core.tpp.service.tpp.UfxRepayService;
import com.rd.ifaes.mq.MqListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 还款队列监听
 * @version 3.0
 * @author FangJun
 * @date 2016年10月21日
 */
@Component
public class RepayListener implements MqListener<MqRepayModel> {
    private static final Logger LOGGER = LoggerFactory.getLogger(RepayListener.class);

    @Autowired
    private UfxRepayService ufxRepayService;
    @Autowired
    private JxbankService jxbankService;

    @Override
    public void listen(MqRepayModel model) {
        LOGGER.info("还款款队列监听，操作类型：{}", model.getOperate());
        if (MqConstant.OPERATE_REPAY.equals(model.getOperate())) {
            ufxRepayService.successRepayHandle(model.getTppTrade());
//			ufxRepayService.ufxRepayHandle(model.getRepayModel());
        }else if(MqConstant.OPERATE_BATCH_CREDIT_END.equals(model.getOperate())) {
            jxbankService.batchCreditEnd(model.getProjectId());
        }else if(MqConstant.OPERATE_REPAY_BAIL.equals(model.getOperate())) {
            ufxRepayService.successRepayBailHandle(model.getTppTrade());
        }
    }

}
