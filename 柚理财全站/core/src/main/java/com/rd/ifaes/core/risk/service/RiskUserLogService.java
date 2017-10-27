package com.rd.ifaes.core.risk.service;

import java.util.List;

import com.rd.ifaes.core.risk.domain.Question;
import com.rd.ifaes.core.risk.domain.RiskUserLog;
import com.rd.ifaes.core.base.service.BaseService;

/**
 * Service Interface:UserLogService
 * @author QianPengZhan
 * @version 3.0
 * @date 2016-7-14
 */
public interface RiskUserLogService extends BaseService<RiskUserLog>{

	/**
	 * 根据userId查询记录
	 * @param userId
	 * @return
	 */
	int getCountByUserId(final String userId);
	
	/**
	 * 根据用户评测记录来列出所有问题和答案内容
	 * @author QianPengZhan
	 * @date 2016年9月12日
	 * @param userLog
	 * @return
	 */
	List<Question> getQuestionAndAnswers(final RiskUserLog userLog);
}