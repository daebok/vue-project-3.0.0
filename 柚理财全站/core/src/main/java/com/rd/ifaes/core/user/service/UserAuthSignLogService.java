package com.rd.ifaes.core.user.service;

import java.util.Map;

import com.rd.ifaes.core.base.service.BaseService;
import com.rd.ifaes.core.tpp.model.jx.JxAutoBidAuthPlusModel;
import com.rd.ifaes.core.tpp.model.jx.JxAutoCreditInvestAuthPlusModel;
import com.rd.ifaes.core.tpp.model.jx.JxCreditAuthQueryModel;
import com.rd.ifaes.core.tpp.model.ufx.UfxAuthSignModel;
import com.rd.ifaes.core.user.domain.User;
import com.rd.ifaes.core.user.domain.UserAuthSignLog;

/**
 * Service Interface:UserAuthSignLogService
 * @author zb
 * @version 3.0
 * @date 2016-7-22
 */
public interface UserAuthSignLogService extends BaseService<UserAuthSignLog>{

	/**
	 * 授权申请并加入记录
	 * @param sessionUUserser 
	 * @param ip 
	 * @param authOptionClose 
	 * @return
	 */
	UfxAuthSignModel auth(User sessionUUserser, String addIp, String authOption);
	
	Object auth(Map<String,Object> map);

	/**
	 * 授权回调处理
	 * @param authModel
	 */
	void doAuth(UfxAuthSignModel authModel);

	void doAuth(JxAutoBidAuthPlusModel authModel);
	
	String getOrderNo(String serviceTypes, String userId);

	/**
	 * 判断是否需要授权
	 * @author ZhangBiao
	 * @date 2016年9月11日
	 * @return
	 */
	boolean checkAuthSign();

	/**
	 * 投资人自动债权转让签约增强
	 * @param smsCode
	 * @param uuid
	 * @param addIp IP地址
	 * @return
	 */
	JxAutoCreditInvestAuthPlusModel autoCreditInvestAuthPlus(String smsCode, String uuid, String addIp);
	/**
	 * 投资人撤销自动债权转让签约增强
	 * @param addIp IP地址
	 * @return
	 */
	void autoCreditInvestAuthCancel(String addIp);

	/**
	 * 投资人自动债权转让签约增强回调处理
	 * @param model
	 */
	void doAutoCreditInvestAuth(JxAutoCreditInvestAuthPlusModel model);
	/**
	 * 去第三方查询是否签约过
	 * @param type 1 自动投标签约  2 自动债转签约
	 * @param accountId 投资人电子账号
	 */
	JxCreditAuthQueryModel creditAuthQuery(final String type, final String accountId);
}