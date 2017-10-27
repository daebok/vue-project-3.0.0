package com.rd.ifaes.core.user.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.rd.ifaes.core.user.domain.UserQualificationApply;
import com.rd.ifaes.core.base.mapper.BaseMapper;

/**
 * Dao Interface:UserQualificationApplyMapper
 * @author FangJun
 * @version 3.0
 * @date 2016-7-21
 */
public interface UserQualificationApplyMapper extends BaseMapper<UserQualificationApply> {

	/**
	 * 
	 * 查询用户某项资质类型是否申请中或者审核成功
	 * @author ZhangBiao
	 * @date 2016年8月2日
	 * @param userQualificationApply
	 * @return
	 */
	int countByType(@Param("userId")String uuid,@Param("qualificationType") String qualificationType,@Param("status") String status);
	
	/**
	 * 
	 * 获得最新审核资质文件
	 * @author xhf
	 * @date 2016年8月3日
	 * @param model
	 * @return
	 */
	UserQualificationApply findNewestApply(UserQualificationApply model);

	/**
	 * 
	 * 获取记录列表
	 * @author xhf
	 * @date 2016年8月3日
	 * @param model
	 * @return
	 */
	List<UserQualificationApply> getLogList(UserQualificationApply model);

}