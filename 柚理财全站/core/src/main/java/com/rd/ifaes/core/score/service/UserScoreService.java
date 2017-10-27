package com.rd.ifaes.core.score.service;

import com.rd.ifaes.core.base.service.BaseService;
import com.rd.ifaes.core.score.domain.UserScore;
import com.rd.ifaes.core.user.domain.User;

/**
 * Service Interface:UserScoreService
 * @author wyw
 * @version 3.0
 * @date 2016-8-4
 */
public interface UserScoreService extends BaseService<UserScore>{
	/**
	 * 
	 * 根据用户id获取用户积分
	 * @author wyw
	 * @date 2016-8-4
	 * @param userId
	 * @return
	 */
	 UserScore getUserScore(String userId);
	/**
	 * 
	 * 发放积分： 发放类型为固定值，固定值递增
	 * @author wyw
	 * @date 2016-8-4
	 * @param user
	 * @param scoreType
	 * @return 获取的积分值
	 */
	 int grantScore(User user,String typeCode);
	
	/**
	 * 发放积分：发放类型为投资比例，比例满返 金额满返
	 * @author wyw
	 * @param user
	 * @param scoreType
	 * @param uuid 投资ID
	 * @return 获取的积分值
	 */
	 int grantScore(User user,String typeCode,String uuid);
	/**
	 * 发放积分：其他发放积分渠道，需要传递操作积分值，以及操作备注
	 * @author wyw
	 * @return 获取的积分值
	 */
	 int grantScore(User user,String scoreType,String scoreTypeName,int optValue,String remark);
	
	/**
	 * 更新用户积分值,并返回最新的值
	 * @author wyw
	 * @return
	 */
	 UserScore updateUserScore(int useScore, int noUseScore, int expenseScore,String userId);
	//使用积分
		/**
		 * 初始化userScore 
		 * @author wyw
		 * @date 2016-8-4
		 * @param userId
		 * @return
		 */
	 void   initUserScore(final String userId);
	 
	 /**
	    * 取得记录数
	    * @param model
	    * @return
	    */
	   int getListCount(String keywords);
}