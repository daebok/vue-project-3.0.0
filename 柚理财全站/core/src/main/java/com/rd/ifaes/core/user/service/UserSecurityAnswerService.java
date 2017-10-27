package com.rd.ifaes.core.user.service;

import java.util.List;

import com.rd.ifaes.core.sys.domain.DictData;
import com.rd.ifaes.core.user.domain.UserSecurityAnswer;
import com.rd.ifaes.core.user.model.UserSecurityAnswerModel;
import com.rd.ifaes.core.base.service.BaseService;

/**
 * Service Interface:UserSecurityAnswerService
 * @author xhf
 * @version 3.0
 * @date 2016-7-21
 */
public interface UserSecurityAnswerService extends BaseService<UserSecurityAnswer>{

	/**
	 * 根据userId查询记录
	 * @param userId
	 * @return
	 */
	List<UserSecurityAnswer> findByUserId(String userId);
	
	/**
	 * 设置密保问题
	 * @param model
	 */
	void doSetPwdQuestion(UserSecurityAnswerModel model);
	
	/**
	 * 
	 * 根据问题标识和用户ID查询答案
	 * @author xhf
	 * @date 2016年7月28日
	 * @param userId
	 * @param questionFlag
	 * @return
	 */
	UserSecurityAnswer findByQuestion(String userId, String questionFlag);
	
	/**
	 * 
	 * 获得一条随机回答过的问题
	 * @author xhf
	 * @date 2016年7月28日
	 * @param userId
	 * @param questionFlag
	 * @return
	 */
	UserSecurityAnswer getRandomQuestion(String userId, String questionFlag);
	
	/**
	 * 
	 * 获得用户设置过的密保问题
	 * @author xhf
	 * @date 2016年7月28日
	 * @param userId
	 * @return
	 */
	List<DictData> findUserPwdQuestion(String userId);
	
	/**
	 * 
	 * 回答密保问题
	 * @author xhf
	 * @date 2016年7月28日
	 * @param model
	 */
	void doAnswerPwdQuestion(UserSecurityAnswerModel model);
	
	/**
	 * 
	 * 重置密保问题
	 * @author xhf
	 * @date 2016年7月28日
	 * @param model
	 */
	void doResetPwdQuestion(UserSecurityAnswerModel model);
}