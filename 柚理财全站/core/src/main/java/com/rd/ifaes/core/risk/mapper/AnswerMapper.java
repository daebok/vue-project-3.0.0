package com.rd.ifaes.core.risk.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.rd.ifaes.core.base.mapper.BaseMapper;
import com.rd.ifaes.core.risk.domain.Answer;

/**
 * Dao Interface:AnswerMapper
 * @author QianPengZhan
 * @version 3.0
 * @date 2016-7-12
 */
public interface AnswerMapper extends BaseMapper<Answer> {
	
	/**
	 * 根据问题的ID查询答案集合
	 * @author QianPengZhan
	 * @date 2016年8月5日
	 * @param map中传递 quuid  和 deleteFlag
	 * @return
	 */
	List<Answer> findListByQId(@Param("questionId") String questionId,@Param("deleteFlag") String deleteFlag);
	
	/**
	 * 根据问题ID,答案ID答案集合
	 * @author QianPengZhan
	 * @date 2016年8月5日
	 * @param map中传递 quuid
	 * @return
	 */
	List<Answer> findListByQIdAndAId(@Param("questionId") String questionId,@Param("answerIds") String[] answerIds);
}	