package com.rd.ifaes.core.risk.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rd.ifaes.common.constant.TokenConstants;
import com.rd.ifaes.common.dict.CommonEnum;
import com.rd.ifaes.common.dict.RiskEnum;
import com.rd.ifaes.common.exception.BussinessException;
import com.rd.ifaes.common.orm.Page;
import com.rd.ifaes.common.util.TokenUtils;
import com.rd.ifaes.common.util.resource.RiskResource;
import com.rd.ifaes.common.validator.BeanValidators;
import com.rd.ifaes.core.base.service.BaseServiceImpl;
import com.rd.ifaes.core.core.constant.Constant;
import com.rd.ifaes.core.core.util.ResourceUtils;
import com.rd.ifaes.core.risk.domain.Answer;
import com.rd.ifaes.core.risk.domain.Papers;
import com.rd.ifaes.core.risk.domain.Question;
import com.rd.ifaes.core.risk.mapper.AnswerMapper;
import com.rd.ifaes.core.risk.service.AnswerService;
import com.rd.ifaes.core.risk.service.PapersService;
import com.rd.ifaes.core.risk.service.QuestionService;

/**
 * 答案业务类
 * 
 * @version 3.0
 * @author QianPengZhan
 * @date 2016年8月6日
 */
@Service("answerService")
public class AnswerServiceImpl extends BaseServiceImpl<AnswerMapper, Answer>
		implements AnswerService {
	/**
	 * 问题业务类
	 */
	@Resource
	private transient QuestionService questionService;
	/**
	 * 试卷业务类
	 */
	@Resource
	private transient PapersService papersService;

	/**
	 * 批量删除
	 */
	@Override
	public void deleteBatch(final String[] ids) {
		if (ids == null || ids.length == Constant.INT_ZERO) {
			throw new BussinessException(ResourceUtils.get(RiskResource.RISK_SELECTED_DELETE_ERROR),BussinessException.TYPE_CLOSE);
		}
		dao.deleteBatch(ids);
	}

	/**
	 * 添加
	 */
	@Override
	@Transactional(readOnly = false)
	public void insert(final Answer entity) {
		BeanValidators.validate(entity);
		TokenUtils.validShiroToken(TokenConstants.TOKEN_ANSWER_ADD);
		// 根据entity的questionId查询此问题是否已经存在答案 判断序号是否相同
		final List<Answer> answers = dao.findListByQId(entity.getQuestionId(),CommonEnum.NO.getValue());
		for (final Answer a : answers) {
			if (entity.getSort().equals(a.getSort())) {
				throw new BussinessException(ResourceUtils.get(RiskResource.RISK_ANSWER_SORT_IS_EXISTS));
			}
		}
		entity.preInsert();
		dao.insert(entity);
	}

	/**
	 * 获取单条数据
	 */
	@Override
	public Answer get(final String id) {
		final Answer answer = dao.get(id);
		if(answer == null){
			throw new BussinessException(ResourceUtils.get(RiskResource.RISK_ANSWER_IS_NULL));
		}
		final Question question = questionService.get(answer.getQuestionId());
		final Papers papers = papersService.get(question.getPapersId());
		answer.setPapersName(papers.getPapersName());
		answer.setQuestionName(question.getQuestionName());
		return answer;
	}

	/**
	 * 查询数据列表，如果需要分页，请设置分页对象，如：model.setPage(new Page());
	 */
	@Override
	public List<Answer> findList(final Answer model) {
		model.setDeleteFlag(Constant.INT_ZERO);
		return dao.findList(model);
	}

	/**
	 * 查询分页数据
	 */
	@Override
	public Page<Answer> findPage(final Answer entity) {
		entity.setDeleteFlag(Integer.parseInt(RiskEnum.CONFIG_IS_NOT_DELETE_FLAG.getValue()));
		return super.findPage(entity);
	}

	/**
	 * 查询数据记录
	 */
	@Override
	public int getCount(final Answer model) {
		model.setDeleteFlag(Integer.parseInt(RiskEnum.CONFIG_IS_NOT_DELETE_FLAG.getValue()));
		return dao.getCount(model);
	}

	/**
	 * 根据对象修改答案
	 */
	@Override
	public int updateAnswer(final Answer answer) {
		return dao.update(answer);
	}

	/**
	 * 批量添加答案集合
	 */
	@Override
	public int insertBatchAnswers(final List<Answer> list) {
		return dao.insertBatch(list);
	}

	/**
	 * 根据问题ID和删除标识查询答案集合
	 */
	@Override
	public List<Answer> findListByQId(final String questionId,
			final String deleteFlag) {
		return dao.findListByQId(questionId, deleteFlag);
	}
	
	/**
	 * 根据问题ID,答案ID答案集合
	 */
	@Override
	public List<Answer> findListByQIdAndAId(final String questionId,final String[] answerIds) {
		return dao.findListByQIdAndAId(questionId,answerIds);
	}
	
	/**
	 * 修改answer
	 */
	@Override
	public void update(Answer entity) {
		TokenUtils.validShiroToken(TokenConstants.TOKEN_ANSWER_EDIT);
		dao.update(entity);
	}

	
	
}