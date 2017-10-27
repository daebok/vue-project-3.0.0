package com.rd.ifaes.core.user.mapper;

import java.util.Date;

import org.apache.ibatis.annotations.Param;

import com.rd.ifaes.core.base.mapper.BaseMapper;
import com.rd.ifaes.core.user.domain.UserIdentify;
import com.rd.ifaes.core.user.model.UserIdentifyModel;


/**
 * Dao Interface:UserIdentifyMapper
 * @author xhf
 * @version 3.0
 * @date 2016-6-15
 */
public interface UserIdentifyMapper extends BaseMapper<UserIdentify> {

	/**
	 * 根据userId查询对象
	 * @param userId
	 * @return
	 */
	UserIdentify findByUserId(String userId);
	
	/**
	 * 更新手机状态
	 * @param userId
	 * @param status
	 * @param preStatus
	 */
	int modifyMobileStatus(UserIdentifyModel model);
	
	/**
	 * 更新邮箱状态
	 * @param userId
	 * @param status
	 * @param preStatus
	 */
	int modifyEmailStatus(UserIdentifyModel model);

	/**
	 * 根据用户id修改授权状态
	 * @param userCustId
	 * @param authOptionOpen
	 * @param date 
	 */
	void setAuthSignStatus(@Param("userId")String userId,@Param("authOptionOpen") String authOptionOpen,@Param("date") Date date);

    void setAutoCreditInvestAuthStatus(@Param("userId")String userId, @Param("status")String status);
}