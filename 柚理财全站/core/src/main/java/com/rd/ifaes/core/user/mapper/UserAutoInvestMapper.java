package com.rd.ifaes.core.user.mapper;

import java.util.List;

import com.rd.ifaes.core.user.domain.UserAutoInvest;
import com.rd.ifaes.core.user.model.AutoInvestRuleLogModel;
import com.rd.ifaes.core.base.mapper.BaseMapper;

/**
 * Dao Interface:UserAutoInvestMapper
 * @author zb
 * @version 3.0
 * @date 2016-7-25
 */
public interface UserAutoInvestMapper extends BaseMapper<UserAutoInvest> {

	/**
	 * 根据用户id获取自动投资设置对象
	 * @param uuid
	 * @return
	 */
	UserAutoInvest getAutoInvestByUserId(String userId);

	/**
	 * 
	 * 获取自动投标排序
	 * @author zb
	 * @param mdoel 
	 * @date 2016年7月27日
	 * @return
	 */
	List<UserAutoInvest> findSortList(AutoInvestRuleLogModel mdoel);

	/**
	 * 
	 * 获取某种状态的自动投资总人数
	 * @author ZhangBiao
	 * @date 2016年8月4日
	 * @return
	 */
	int getSumByStatus(String status);

	/**
	 * 
	 * 获取某种状态所在排名
	 * @author ZhangBiao
	 * @param userId 
	 * @date 2016年8月4日
	 * @return
	 */
	int getNumByStatus(UserAutoInvest userAutoInvest);

}