package com.rd.ifaes.core.risk.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.rd.ifaes.core.base.mapper.BaseMapper;
import com.rd.ifaes.core.risk.domain.Question;

/**
 *  问题类Dao 数据接口
 * @version 3.0
 * @author QianPengZhan
 * @date 2016年8月5日
 */
public interface QuestionMapper extends BaseMapper<Question> {
	
	/**
	 *  根据试卷UUID查询该试卷已经有多少未删除的问题了
	 * @author QianPengZhan
	 * @date 2016年8月6日
	 * @param papersId
	 * @param deleteFlag
	 * @return
	 */
	int getQuestionNumByPapersId(@Param("papersId")String papersId,@Param("deleteFlag") String deleteFlag);
	
	/**
	 * 根据组合属性查询问题列表
	 * @author QianPengZhan
	 * @date 2016年8月5日
	 * @param map  此处传递试卷papersId和未删除的标识deleteFlag
	 * @return 
	 */
	List<Question> findListByProperties(Question question);
	
	
	/**
	 * 获取试卷总分
	 * @author QianPengZhan
	 * @date 2016年8月6日
	 * @param papersId
	 * @param deleteFlag
	 * @return
	 */
	int getPapersTotalScore(@Param("papersId")String papersId,@Param("deleteFlag") String deleteFlag);
	
	/**
	 * 根据序号查询问题 是否存在
	 * @author QianPengZhan
	 * @date 2016年8月5日
	 * @param question  传递题号和是否删除
	 * @return
	 */
	int getQuestionNumBySort(Question question);
}