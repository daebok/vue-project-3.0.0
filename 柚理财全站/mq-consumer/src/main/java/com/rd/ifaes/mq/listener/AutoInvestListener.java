package com.rd.ifaes.mq.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.rd.ifaes.common.rabbit.model.MqInvestModel;
import com.rd.ifaes.core.core.constant.MqConstant;
import com.rd.ifaes.core.user.service.UserInvestAutoLogService;
import com.rd.ifaes.mq.MqListener;

/**
 * 自动投资业务队列监听
 *  
 * @version 3.0
 * @author ZhangBiao
 * @date 2016年9月13日
 */
@Component
public class AutoInvestListener implements MqListener<MqInvestModel> {

	private static final Logger LOGGER = LoggerFactory.getLogger(AutoInvestListener.class);

	@Autowired
	private UserInvestAutoLogService userInvestAutoLogService;

	@Override
	public void listen(MqInvestModel model) {
		LOGGER.info("自动投资业务队列监听已开启");
		LOGGER.info(model.toString());
		if (MqConstant.OPERATE_START_AUTO_INVEST.equals(model.getOperate())) {
			userInvestAutoLogService.startAutoInvest(model.getAutoInvestModel());
		}
	}

}
