package com.rd.ifaes.core.risk.service;

import java.util.List;

import com.rd.ifaes.core.base.service.BaseService;
import com.rd.ifaes.core.risk.domain.Question;

/**
 * 问题业务接口类
 * @version 3.0
 * @author QianPengZhan
 * @date 2016年8月6日
 */
public interface QuestionService extends BaseService<Question>{
	
	 /**
	  * 根据问题对象修改问题对象
	  * @author QianPengZhan
	  * @date 2016年8月6日
	  * @param question
	  * @return
	  */
	 int updateQuestion(Question question);
	/**
	 *  修改问题和答案
	 * @author QianPengZhan
	 * @date 2016年7月27日
	 * @param model
	 * @param answerContent
	 * @param answerContentAdd
	 * @return
	 */
	void updateQuestionAndAnswer(Question model,String[][] answerContent,final String[][] answerContentAdd);
	
	/**
	 *  添加问题和答案
	 * @author QianPengZhan
	 * @date 2016年7月27日
	 * @param model
	 * @param answerContent
	 * @return
	 */
	void insertQuestionAndAnswer(Question model,String[][] answerContent);
	
	/**
	 *  根据试卷ID查询问题及答案集合
	 * @author QianPengZhan
	 * @date 2016年7月27日
	 * @param papersId
	 * @return
	 */
	List<Question> findQuestionAndAnswer(String papersId);
	
	/**
	 *  获取试卷的总分
	 * @author QianPengZhan
	 * @date 2016年7月27日
	 * @param papersId
	 * @return
	 */
	int getTotalScore(String papersId);
	
	/**
	 *  根据ID查询问题
	 * @author QianPengZhan
	 * @date 2016年7月27日
	 * @param id
	 * @return
	 */
	Question getQuestionById(String id);
	
	/**
	 *  根据试卷ID获取试卷有多少有用的题目
	 * @author QianPengZhan
	 * @date 2016年7月27日
	 * @param papersId
	 * @return
	 */
	int  getQuestionNumByPapersId(String papersId);
	
	/**
	 * 根据试卷ID查询试卷总分  
	 * @author QianPengZhan
	 * @date 2016年8月6日
	 * @param papersId
	 * @return
	 */
	int getPapersTotalScore(String papersId);
	
	
}