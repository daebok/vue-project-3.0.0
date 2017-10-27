/**
* 互联网金融交易系统ifaes (Internet Finance  Assets  Exchange  System)
 * Copyright (c) 2016 杭州融都科技股份有限公司 版权所有
 */
package com.rd.ifaes.core.home.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.rd.ifaes.common.annotation.Cacheable;
import com.rd.ifaes.common.dict.ExpireTime;
import com.rd.ifaes.common.dict.InvestEnum;
import com.rd.ifaes.core.home.model.SureProfitModel;
import com.rd.ifaes.core.home.service.HomeService;
import com.rd.ifaes.core.project.domain.ProjectInvest;
import com.rd.ifaes.core.project.mapper.ProjectMapper;
import com.rd.ifaes.core.project.service.ProjectInvestBespeakService;
import com.rd.ifaes.core.project.service.ProjectInvestService;
import com.rd.ifaes.core.project.service.ProjectTypeService;
import com.rd.ifaes.core.sys.service.OperatorService;
import com.rd.ifaes.core.user.domain.User;
import com.rd.ifaes.core.user.service.UserEarnLogService;
import com.rd.ifaes.core.user.service.UserService;

/**
 * 首页数据查询--服务
 * 
 * @version 3.0
 * @author FangJun
 * @date 2016年11月8日
 */
@Service("homeService")
public class HomeServiceImpl implements HomeService {
	/**
	 * 用户业务
	 */
	@Resource
	private transient UserService userService;
	/**
	 * 项目业务
	 */
	@Resource
	private transient ProjectMapper projectMapper;
	/**
	 * userEarnLogService 业务
	 */
	@Resource
	private transient UserEarnLogService userEarnLogService;
	/**
	 * 项目投资业务
	 */
	@Resource
	private transient ProjectInvestService projectInvestService;
	/**
	 * 项目预约投资业务
	 */
	@Resource
	private transient ProjectInvestBespeakService projectInvestBespeakService;
	@Resource
	private transient OperatorService operatorService;
	@Resource
	private transient ProjectTypeService projectTypeService;

	@Override
	@Cacheable(expire = ExpireTime.FIVE_SEC)
	public Map<String, Object> webCountInfo() {
		final Map<String, Object> data = new HashMap<String, Object>();
		ProjectInvest projectInvest = new ProjectInvest();
		projectInvest.setStatus(InvestEnum.STATUS_SUCCESS.getValue());
		User queryUser = new User();
		queryUser.setIsVouch(User.USER_NATURE_NOT_VOUCH);
		int registerTotal = userService.getCount(queryUser) - 1;// 排除uuid=1的平台用户
		data.put("registerTotal", registerTotal<0?0:registerTotal);// 注册总数
		data.put("totalEarnAmount", userEarnLogService.allEarnAmount(null));// 赚取的收益
		data.put("investTotal", projectInvestService.sumInvest(projectInvest));// 投资总额
		// 获取在线客服列表
		data.put("onlineServer", operatorService.findOnlineServer());
		return data;
	}
 
	@Override
	@Cacheable(expire = ExpireTime.FIVE_SEC)
	public List<SureProfitModel> getSureProfit() {
		return projectMapper.getSureProfit();
	}
}
