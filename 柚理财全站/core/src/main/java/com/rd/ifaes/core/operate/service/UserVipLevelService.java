package com.rd.ifaes.core.operate.service;

import java.math.BigDecimal;
import java.util.List;

import com.rd.ifaes.core.base.service.BaseService;
import com.rd.ifaes.core.operate.domain.UserVipLevel;
import com.rd.ifaes.core.operate.model.UserVipLevelModel;

/**
 * 
 * VIP 等级设置业务接口
 * @version 3.0
 * @author wyw
 * @date 2016-8-1
 */
public interface UserVipLevelService extends BaseService<UserVipLevel>{
    /**
     * vip等级保存
     * @author wyw
     * @date 2016-8-1
     * @param userVipLevelModel
     * @return
     */
	 boolean saveVipLevel(UserVipLevelModel  userVipLevelModel);
	/**
	 * 
	 * 获取等级更高的vip等级设置
	 * @author wyw
	 * @date 2016-8-2
	 * @param vipLevel
	 * @return
	 */
	List<UserVipLevel> findHighLever(UserVipLevel  vipLevel);
	/**
	 * 
	 * 根据vip等级获取设置
	 * @author wyw
	 * @date 2016-8-2
	 * @param vipLevel
	 * @return
	 */
	UserVipLevel getByVipLevel(String vipLevel);
	
	/**
	 * 
	 * 获取用户满足的vip等级
	 * @author xhf
	 * @date 2016年9月18日
	 * @param growthValue
	 * @param vipLevel
	 * @return
	 */
	UserVipLevel getUserVipLevel(final BigDecimal growthValue,final String vipLevel);
	
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
	 * @return
	 */
	List<String> getLevelByStatus(String deleteFlag);
	
	/**
	 * 修改vip配置
	 * @author ZhangBiao
	 * @date 2016年10月27日
	 * @param model
	 */
	void updateVipLevel(UserVipLevel model);

	/**
	 * 校验当前有没有VIP等级大于0的用户 有就不予许修改或删除VIP等级
	 * @author fxl
	 * @date 2017年1月3日
	 */
	void checkVipUser();
}