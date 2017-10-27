package com.rd.ifaes.core.user.service.impl;


import org.springframework.stereotype.Service;
import com.rd.ifaes.core.base.service.BaseServiceImpl;
import com.rd.ifaes.core.user.domain.UserOpinion;
import com.rd.ifaes.core.user.mapper.UserOpinionMapper;
import com.rd.ifaes.core.user.service.UserOpinionService;


/**
 * 用户意见反馈处理类
 * @author xxb
 * @version 3.0
 * @date 2016-10-10
 */
@Service("userOpinionService") 
public class UserOpinionServiceImpl extends BaseServiceImpl<UserOpinionMapper, UserOpinion> implements UserOpinionService{
	
	@Override
	public void  updateById(UserOpinion uo) {
		dao.updateById(uo);
	}
	
}