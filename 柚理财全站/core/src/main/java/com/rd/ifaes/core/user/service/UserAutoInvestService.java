package com.rd.ifaes.core.user.service;

import com.rd.ifaes.core.user.domain.User;
import com.rd.ifaes.core.user.domain.UserAutoInvest;
import com.rd.ifaes.core.user.model.AutoInvestRuleLogModel;
import com.rd.ifaes.core.base.service.BaseService;

/**
 * Service Interface:UserAutoInvestService
 * @author zb
 * @version 3.0
 * @date 2016-7-25
 */
public interface UserAutoInvestService extends BaseService<UserAutoInvest>{

	/**
	 * 添加自动投标设置
	 * @param rule
	 * @param sessionUser
	 */
	void add(AutoInvestRuleLogModel model, User sessionUser);

	/**
	 * 
	 * 关闭自动投资
	 * @author ZhangBiao
	 * @date 2016年8月4日
	 * @param sessionUser
	 */
	Object close(User sessionUser);

	/**
	 * 
	 * 获取自动投资配置信息、排名、可用金额
	 * @author ZhangBiao
	 * @date 2016年8月4日
	 * @return
	 */
	Object getData();

	/**
	 * 
	 * 根据用户id获取自动投资状态
	 * @author ZhangBiao
	 * @date 2016年8月18日
	 * @param uuid
	 * @return
	 */
	String getAutoStatusByUserId(String uuid);

}