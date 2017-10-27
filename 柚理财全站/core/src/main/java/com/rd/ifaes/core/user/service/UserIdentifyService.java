package com.rd.ifaes.core.user.service;

import java.math.BigDecimal;

import com.rd.ifaes.core.base.service.BaseService;
import com.rd.ifaes.core.user.domain.User;
import com.rd.ifaes.core.user.domain.UserIdentify;

/**
 * Service Interface:UserIdentifyService
 * @author xhf
 * @version 3.0
 * @date 2016-6-15
 */
public interface UserIdentifyService extends BaseService<UserIdentify>{

	/**
	 * 根据userId查找对下对象
	 * @param userId
	 * @return
	 */
	UserIdentify findByUserId(String userId);
	
	/**
	 * 更新手机状态
	 * @param userId
	 * @param status
	 */
	void modifyMobileStatus(String userId, String status, String preStatus);
	
	/**
	 * 
	 * 更新邮箱状态
	 * @author xhf
	 * @date 2016年7月27日
	 * @param userId
	 * @param status
	 * @param preStatus
	 */
	void modifyEmailStatus(String userId, String status, String preStatus);
	
	/**
	 * 校验用户是否可充值
	 */
	void validIdentifyForRecharge();

	/**
	 * 获得用户安全系数值，以%为单位，返回类似80,60,40
	 * @param user
	 * @return
	 */
	BigDecimal getSecurityValue(User user);
	/**
	 * 用户安全系数级别
	 * @param user
	 * @return
	 */
	String getSecurityLevel(User user);

	/**
	 * 根据用户id修改授权状态
	 * @param userCustId
	 * @param authOptionOpen
	 */
	void setAuthSignStatus(String userId, String authOptionOpen);

	/**
	 * 
	 * 用户投资状态校验
	 * @author ZhangBiao
	 * @date 2016年9月11日
	 * @param user
	 * @param backUrl 校验错误 返回地址
	 */
	void validIdentifyForInvest(User user,final String backUrl);

	/**
	 * 根据用户id修改债权授权状态
	 * @param userId
	 * @param status
	 */
    void setAutoCreditInvestAuthStatus(String userId, String status);
}