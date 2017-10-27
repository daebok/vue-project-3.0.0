package com.rd.ifaes.core.risk.service;

import java.util.List;

import com.rd.ifaes.core.base.service.BaseService;
import com.rd.ifaes.core.risk.domain.Answer;

/**
 * 答案接口
 * @version 3.0
 * @author QianPengZhan
 * @date 2016年8月5日
 */
public interface AnswerService extends BaseService<Answer>{
		
	/**
	 * 根据对象修改答案
	 * @author QianPengZhan
	 * @date 2016年8月6日
	 * @param answer
	 * @return
	 */
	int updateAnswer(Answer answer);
	
	/**
	 * 批量添加答案集合
	 * @author QianPengZhan
	 * @date 2016年8月6日
	 * @param list
	 * @return
	 */
	int insertBatchAnswers(List<Answer> list);
	
	/**
	 *  根据问题ID和删除标识查询答案集合
	 * @author QianPengZhan
	 * @date 2016年8月6日
	 * @param questionId
	 * @param deleteFlag
	 * @return
	 */
	List<Answer> findListByQId(String questionId,String deleteFlag);
	
	/**
	 * 根据问题ID,答案ID答案集合
	 * @author QianPengZhan
	 * @date 2016年8月6日
	 * @param questionId
	 * @param deleteFlag
	 * @return
	 */
	List<Answer> findListByQIdAndAId(String questionId,String[] answerIds);
}