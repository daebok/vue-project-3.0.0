/**
* 互联网金融交易系统ifaes (Internet Finance  Assets  Exchange  System)
 * Copyright (c) 2016 杭州融都科技股份有限公司 版权所有
 */
package com.rd.ifaes.mq.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.rd.ifaes.common.rabbit.model.MqInvestModel;
import com.rd.ifaes.core.core.constant.MqConstant;
import com.rd.ifaes.core.protocol.service.ProtocolService;
import com.rd.ifaes.mq.MqListener;

/**
 * 协议监听
 * @version 3.0
 * @author QianPengZhan
 * @date 2016年9月30日
 */
@Component
public class ProtocolListener implements MqListener<MqInvestModel>{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ProtocolListener.class);
	
	@Autowired
	private transient ProtocolService protocolService;
	
	@Override
	public void listen(MqInvestModel model) {
		LOGGER.info("协议队列监听已开启");
		LOGGER.info(model.toString());
		
		if (MqConstant.BUILD_BOND_PROTOCOL.equals(model.getOperate())) {
			protocolService.buildBondProtocol(model.getBondProtocol().getBondInvestId(), model.getBondProtocol().getUserId());
		}else if(MqConstant.BUILD_PROTOCOL.equals(model.getOperate())){
			protocolService.buildProtocol(model.getInvestProtocol().getInvestId(), model.getInvestProtocol().getUserId());
		}
	}

}
