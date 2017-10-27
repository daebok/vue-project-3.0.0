/**
* 互联网金融交易系统ifaes (Internet Finance  Assets  Exchange  System)
 * Copyright (c) 2016 杭州融都科技股份有限公司 版权所有
 */
package com.rd.ifaes.mq.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.rd.ifaes.common.rabbit.model.MqLoanModel;
import com.rd.ifaes.core.core.constant.MqConstant;
import com.rd.ifaes.core.tpp.service.tpp.UfxLoanService;
import com.rd.ifaes.mq.MqListener;

/**
 * 放款队列监听
 * @version 3.0
 * @author FangJun
 * @date 2016年10月17日
 */
@Component
public class LoanListener implements MqListener<MqLoanModel> {
	private static final Logger LOGGER = LoggerFactory.getLogger(LoanListener.class);

	@Autowired
	private UfxLoanService ufxLoanService;

	@Override
	public void listen(MqLoanModel model) {
		LOGGER.info("放款队列监听");
		if (MqConstant.OPERATE_LOAN.equals(model.getOperate())) {
			ufxLoanService.successLoanHandle(model.getTppTrade());
		}
	}

}
