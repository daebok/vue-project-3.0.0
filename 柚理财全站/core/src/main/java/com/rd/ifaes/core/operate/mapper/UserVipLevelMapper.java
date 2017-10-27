package com.rd.ifaes.core.operate.mapper;

import java.util.List;

import com.rd.ifaes.core.operate.domain.UserVipLevel;
import com.rd.ifaes.core.base.mapper.BaseMapper;

/**
 * Dao Interface:UserVipLevelMapper
 * @author 
 * @version 3.0
 * @date 2016-8-1
 */
public interface UserVipLevelMapper extends BaseMapper<UserVipLevel> {
 
	/**
	 * 
	 * 获取高级的vip等级设置集合
	 * @author wyw
	 * @date 2016-8-2
	 * @param vipLevel
	 * @return
	 */
	List<UserVipLevel> findHighLever(UserVipLevel vipLevel);
	/**
	 * 
	 * 根据vip等级获取 设置
	 * @author wyw
	 * @date 2016-8-2
	 * @param vipLevel
	 * @return
	 */
	UserVipLevel  getByVipLevel(String vipLevel);

	/**
	 * 查询所有VIP等级
	 * 
	 * @author FangJun
	 * @date 2016年9月26日
	 * @return 所有VIP等级列表
	 */
	List<String> getAllLevel();
	
	/**
	 * 根据状态查询所有VIP等级
	 * 
	 * @author ZhangBiao
	 * @date 2016年10月13日
	 * @param deleteFlag
	 * @return
	 */
	List<String> getLevelByStatus(String deleteFlag);

	
	/**
	 * 统计VIP等级大于0的用户数
	 * @author fxl
	 * @date 2017年1月3日
	 * @return
	 */
	int countVipUser();

	
	/**
	 * 查询所有正常状态vip等级
	 * @author ZhangBiao
	 * @return
	 */
	List<UserVipLevel> findTrueList();

}