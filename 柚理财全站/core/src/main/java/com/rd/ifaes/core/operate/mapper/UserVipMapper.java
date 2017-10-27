package com.rd.ifaes.core.operate.mapper;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.rd.ifaes.core.operate.domain.UserVip;
import com.rd.ifaes.core.base.mapper.BaseMapper;

/**
 * Dao Interface:UserVipMapper
 * @author 
 * @version 3.0
 * @date 2016-8-1
 */
public interface UserVipMapper extends BaseMapper<UserVip> {
	/**
	 * 
	 * 根据用户id获取userVip
	 * @author wyw
	 * @date 2016-8-2
	 * @param userId
	 * @return
	 */
	UserVip getUserVip(String userId);
	/**
	 * 
	 * 更新vip成长值
	 * @author wyw
	 * @date 2016-8-3
	 * @param userVip
	 */
	void updateVipGrowth(UserVip userVip);
	/**
	 * 
	 * 更新vi等级
	 * @author wyw
	 * @date 2016-8-3
	 * @param userVip
	 */
	void updateVipLevel(UserVip userVip);
	
	/**
	 * 查询需要扣除成长值的用户记录
	 * @author ZhangBiao
	 * @date 2016年11月23日
	 * @param date
	 * @return
	 */
	List<UserVip> needDeductionUser(Date date);
	
	/**
	 * 查询本月生日的用户
	 * @author fxl
	 * @date 2017年5月10日
	 * @return
	 */
	List<UserVip> findBirthVipUser();
	
}