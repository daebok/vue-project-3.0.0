package com.rd.ifaes.core.user.service;

import com.rd.ifaes.common.rabbit.model.MqAutoInvestModel;
import com.rd.ifaes.core.base.service.BaseService;
import com.rd.ifaes.core.user.domain.UserInvestAutoLog;

/**
 * Service Interface:UserInvestAutoLogService
 * @author zb
 * @version 3.0
 * @date 2016-7-25
 */
public interface UserInvestAutoLogService extends BaseService<UserInvestAutoLog>{

	/**
	 * 
	 * 定时任务扫描需自动投资产品，并执行自动投资
	 * @author ZhangBiao
	 * @date 2016年8月18日
	 */
	void jobAutoInvest();

	/**
	 * 开始自动投资业务
	 * 
	 * @author ZhangBiao
	 * @date 2016年9月13日
	 * @param autoInvestModel
	 */
	void startAutoInvest(MqAutoInvestModel autoInvestModel);

}