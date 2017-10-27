package com.rd.ifaes.core.user.mapper;

import com.rd.ifaes.core.base.mapper.BaseMapper;
import com.rd.ifaes.core.user.domain.UserOpinion;

/**
 * Dao Interface:UserMapper
 * @author xxb
 * @version 3.0
 * @date 2016-10-10
 */
public interface UserOpinionMapper extends BaseMapper<UserOpinion> {
	/**
	 * 根据id修改记录
	 * @param uo
	 */
	public void updateById(UserOpinion uo) ;
	
}