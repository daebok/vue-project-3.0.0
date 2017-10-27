package com.rd.ifaes.core.user.mapper;

import com.rd.ifaes.core.user.domain.UserSecurityAnswer;
import com.rd.ifaes.core.base.mapper.BaseMapper;

/**
 * Dao Interface:UserSecurityAnswerMapper
 * @author xhf
 * @version 3.0
 * @date 2016-7-21
 */
public interface UserSecurityAnswerMapper extends BaseMapper<UserSecurityAnswer> {

	/**
	 * 
	 * 获得一条随机答案
	 * @author xhf
	 * @date 2016年7月28日
	 * @param questionFlag
	 * @return
	 */
	UserSecurityAnswer getRandomQuestion(UserSecurityAnswer securityAnswer);
}