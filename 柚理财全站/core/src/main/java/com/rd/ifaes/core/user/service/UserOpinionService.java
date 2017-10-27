package com.rd.ifaes.core.user.service;

import com.rd.ifaes.core.base.service.BaseService;
import com.rd.ifaes.core.user.domain.UserOpinion;

/**
 * Service Interface:UserOpinionService
 * @author xxb
 * @version 3.0
 * @date 2016-10-10
 */
public interface UserOpinionService extends BaseService<UserOpinion>{
	/**
	 * 根据id修改记录
	 * @param uo
	 */
	public void updateById(UserOpinion uo) ;
}