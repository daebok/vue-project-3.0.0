package com.rd.ifaes.core.user.mapper;

import java.util.List;
import java.util.Map;

import com.rd.ifaes.core.base.mapper.BaseMapper;
import com.rd.ifaes.core.user.domain.User;
import com.rd.ifaes.core.user.domain.UserCache;

/**
 * Dao Interface:UserCacheMapper
 * @author xhf
 * @version 3.0
 * @date 2016-6-15
 */
public interface UserCacheMapper extends BaseMapper<UserCache> {

	/**
	 * 根据userId查找对下对象
	 * @param userId
	 * @return
	 */
	UserCache findByUserId(String userId);
	
	/**
	 * 
	 * 更新备注
	 * @author xhf
	 * @date 2016年7月30日
	 * @param userCache
	 * @return
	 */
	int updateLockRemark(UserCache userCache);
	
	/**
	 * 
	 * 更新用户信息
	 * @author xhf
	 * @date 2016年8月23日
	 * @param userCache
	 * @return
	 */
	int updateUserInfo(UserCache userCache);
	
	/**
	 * 查询身份证数量
	 * @author fxl
	 * @date 2016年9月26日
	 * @param cardId
	 * @return
	 */
	int countCardId(final String cardId);
	/**
	 * 用户首投处理（firstAwardFlag 0->1)
	 * @author  FangJun
	 * @date 2016年10月24日
	 * @param userId 用户ID
	 * @return 更新记录条数
	 */
	int userFirstInvest(String userId);
	/**
	 * 用户投资记录数+1
	 * @author  FangJun
	 * @date 2016年10月24日
	 * @param userId 用户ID
	 * @return 更新记录条数
	 */
	int addUserInvestNum(String userId);
	/**
	 * 用户投资记录数-1
	 * @author  FangJun
	 * @date 2016年10月24日
	 * @param userId 用户ID
	 * @return 更新记录条数
	 */
	int subUserInvestNum(String userId);
	
	/**
	 * 根据用户列表得到UserCache
	 * @author fxl
	 * @date 2017年1月10日
	 * @param list
	 * @return
	 */
	List<Map<String, String>> getUserCacheByUserList(List<User> list);
}