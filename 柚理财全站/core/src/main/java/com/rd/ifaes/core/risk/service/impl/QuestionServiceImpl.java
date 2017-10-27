package com.rd.ifaes.core.risk.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.rd.ifaes.core.core.util.OrderNoUtils;
import com.rd.ifaes.core.core.util.ResourceUtils;
import com.rd.ifaes.core.risk.domain.Answer;
import com.rd.ifaes.core.risk.domain.Papers;
import com.rd.ifaes.core.risk.domain.Question;
import com.rd.ifaes.core.risk.mapper.QuestionMapper;
import com.rd.ifaes.core.risk.service.AnswerService;
import com.rd.ifaes.core.risk.service.PapersService;
import com.rd.ifaes.core.risk.service.QuestionService;

/**
 * ServiceImpl:QuestionServiceImpl
 * @author QianPengZhan
 * @version 3.0
 * @date 2016-7-12
 */
@Service("questionService") 
public class QuestionServiceImpl  extends BaseServiceImpl<QuestionMapper, Question> implements QuestionService{
	/**
	 * QuestionServiceImpl的日志
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(QuestionServiceImpl.class);
	/**
	 * papersService业务
	 */
	@Resource
	private transient PapersService papersService;
	/**
	 * answerService业务
	 */
	@Resource
	private transient AnswerService answerService;
	
	/**
	 * 批量删除
	 */
	@Override
	public void deleteBatch(final String[] ids) {
		if (ids == null || ids.length == 0) {
			throw new BussinessException(ResourceUtils.get(RiskResource.RISK_SELECTED_DELETE_ERROR),BussinessException.TYPE_CLOSE);
		}
		for (int i = 0; i < ids.length; i++) {
			final String id = ids[i];
			final Question question = dao.get(id);
			final Papers papers  = papersService.get(question.getPapersId());
			if(Constant.INT_ONE == papers.getStatus()){
				throw new BussinessException(ResourceUtils.get(RiskResource.RISK_QUESTION_ON_NOT_DELETE),BussinessException.TYPE_CLOSE);
			}
		}
		dao.deleteBatch(ids);
	}
	
	/**
	 * 添加
	 */
	@Override
	@Transactional(readOnly = false)
	public void insert(final Question entity) {
		BeanValidators.validate(entity);
		entity.preInsert();
		dao.insert(entity);
	}
	
	/**
	 * 单体获取
	 */
	@Override
	public Question get(final String id) {
		final Question question= dao.get(id);
		if(question == null){
			throw new BussinessException(ResourceUtils.get(RiskResource.RISK_QUESTION_IS_NULL),BussinessException.TYPE_CLOSE);
		}
		final Papers papers = papersService.get(question.getPapersId());
		question.setPapersName(papers.getPapersName());
		return question;
	}

	/**
	 * 查询列表
	 */
	@Override
	public List<Question> findList(final Question model) {
		model.setDeleteFlag(Integer.parseInt(RiskEnum.CONFIG_IS_NOT_DELETE_FLAG.getValue()));
		return super.findList(model);
	}
	
	/**
	 * 查数量
	 */
	@Override
	public int getCount(final Question model) {
		model.setDeleteFlag(Integer.parseInt(RiskEnum.CONFIG_IS_NOT_DELETE_FLAG.getValue()));
		return super.getCount(model);
	}
	
	/**
	 * 分页查询
	 */
	@Override	
	public Page<Question> findPage(final Question entity) {
		entity.setDeleteFlag(Integer.parseInt(RiskEnum.CONFIG_IS_NOT_DELETE_FLAG.getValue()));
		return super.findPage(entity);
	}
	
	/**
	 * 修改问题和答案
	 */
	@SuppressWarnings("rawtypes")
	@Override
	@Transactional(readOnly=false)
	public void updateQuestionAndAnswer(final Question model,final String[][] answerContent,final String[][] answerContentAdd) {
		//1检验传参
		if(answerContent == null || answerContent.length == Constant.INT_ZERO){
			throw new BussinessException(ResourceUtils.get(RiskResource.RISK_PAPER_ANSWER_IS_EMPTY),BussinessException.TYPE_CLOSE);
		}
		//2、 处理问题
		model.setSet(model.getSet()==null?new HashSet():model.getSet());
		model.setScore(model.getScore()==null?Constant.INT_ZERO:model.getScore());
		final Question question = handleAnswers(model,answerContent);
		
		Question questionAdd =new Question();
		if(answerContentAdd != null){
			questionAdd.setSet(model.getSet());
			questionAdd.setScore(model.getScore());
			questionAdd.setUuid(model.getUuid());
			questionAdd.setIsSingle(model.getIsSingle());
			questionAdd = handleAnswers(questionAdd,answerContentAdd);
		}
		//3、 token校验 然后数据库处理
		TokenUtils.validShiroToken(TokenConstants.TOKEN_PAPERS_QA_EDIT);
		dao.update(model);
		if(answerContentAdd ==null){
			dao.update(model);
		}else{
			dao.update(questionAdd);
		}
		answerService.updateBatch(question.getList());
		if(answerContentAdd != null){
			answerService.insertBatch(questionAdd.getList());
		}
	}
	
	/**
	 * 添加问题和答案
	 */
	@SuppressWarnings("rawtypes")
	@Override
	@Transactional(readOnly=false)
	public void insertQuestionAndAnswer(final Question model, final String[][] answerContent) {
		//0、校验参数信息
		model.validQuestion();
		//1、校验问题序号是否重复
		final Question ques = new Question();
		ques.setPapersId(model.getPapersId());
		ques.setSort(model.getSort());
		ques.setDeleteFlag(Constant.INT_ZERO);
		final int isSort = dao.getQuestionNumBySort(ques);
		if(isSort == Constant.INT_ONE){
			throw new BussinessException(ResourceUtils.get(RiskResource.RISK_ANSWER_SORT_IS_EXISTS),BussinessException.TYPE_JSON);
		}
		//2、添加问题uuid
		model.preInsert();
		
		//3、添加答案 和 问题分值
		model.setSet(model.getSet()==null?new HashSet():model.getSet());
		model.setScore(model.getScore()==null?Constant.INT_ZERO:model.getScore());
		final Question question = handleAnswers(model,answerContent);
		
		//4、查询该问题对应的试卷  然后问题数量+1
		final Papers papers = papersService.get(question.getPapersId());
		if(papers == null){
			throw new BussinessException(ResourceUtils.get(RiskResource.RISK_PAPERS_IS_NULL),BussinessException.TYPE_JSON);
		}
		final int num  = papers.getQuestionCount()==null?Constant.INT_ZERO:papers.getQuestionCount();
		papers.setQuestionCount(num+Constant.INT_ONE);
		
		//5、token 校验 之后处理数据库
		TokenUtils.validShiroToken(TokenConstants.TOKEN_SINGLE_QUESTION_ADD);
		
		papersService.update(papers);
		dao.insert(question);
		answerService.insertBatchAnswers(question.getList());
	}
	
	/**
	 * 处理答案
	 * @author QianPengZhan
	 * @date 2016年8月30日
	 * @param qUuid
	 * @param answerContent  答案集合
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private Question handleAnswers(final Question model,final  String[][] answerContent){
		final List<Answer>  aList=  new ArrayList<>();
		int qScore = model.getScore();
		int cScore = Constant.INT_ZERO;//临时的最大分值
		Set set= model.getSet();
		int length = set.size();
		if(answerContent == null){
			LOGGER.info("答案集合不存在");
		}else{
			if(answerContent[Constant.INT_ZERO].length == Constant.INT_ONE){
				final Answer newAnswer = new Answer();
				final String sort = answerContent[Constant.INT_ZERO][Constant.INT_ZERO];
				set.add(sort);
				final String content = answerContent[Constant.INT_ONE][Constant.INT_ZERO];
				final int score = Integer.parseInt(answerContent[Constant.INT_TWO][Constant.INT_ZERO]);
				newAnswer.preInsert();
				newAnswer.setAnswerNo(OrderNoUtils.getSerialNumber());
				newAnswer.setSort(sort);
				newAnswer.setContent(content);
				newAnswer.setScore(score);
				newAnswer.setQuestionId(model.getUuid());
				if(cScore <= score ){
					cScore = score;
				}
				if(model.getIsSingle() == Integer.parseInt(RiskEnum.QUESTION_IS_NOT_SINGLE.getValue())){
					//多选的时候的试题分值
					qScore += score;
				}else{
					qScore = cScore;
				}
				aList.add(newAnswer);
				if(set.size() < Constant.INT_ONE+length){
					throw new BussinessException(ResourceUtils.get(RiskResource.RISK_ANSWER_SORT_IS_EXISTS),BussinessException.TYPE_JSON);
				}
			}else{
				for (int i = Constant.INT_ZERO; i < answerContent.length; i++) {
					final String[] answers = answerContent[i];
					if(answers.length ==Constant.INT_THREE  || answers.length ==Constant.INT_FIVE){
						final Answer newAnswer = new Answer();
						if(answers.length ==Constant.INT_THREE){
							//添加答案参数
							final String sort = answers[Constant.INT_ZERO];
							set.add(sort);
							final String content = answers[Constant.INT_ONE];
							final int score = Integer.parseInt(answers[Constant.INT_TWO]);
							newAnswer.preInsert();
							newAnswer.setAnswerNo(OrderNoUtils.getSerialNumber());
							newAnswer.setSort(sort);
							newAnswer.setContent(content);
							newAnswer.setScore(score);
							newAnswer.setQuestionId(model.getUuid());
							if(cScore <= score ){
								cScore = score;
							}
							if(model.getIsSingle() == Integer.parseInt(RiskEnum.QUESTION_IS_NOT_SINGLE.getValue())){
								//多选的时候的试题分值
								qScore += score;
							}else{
								qScore = cScore;
							}
						}else{
							final String uuid = answers[Constant.INT_ZERO];
							final String sort = answers[Constant.INT_ONE];
							set.add(sort);
							final String content = answers[Constant.INT_TWO];
							final int score = Integer.parseInt(answers[Constant.INT_THREE]);
							final int deleteFlag = Integer.parseInt(answers[Constant.INT_FOUR]);
							newAnswer.setUuid(uuid);
							newAnswer.setSort(sort);
							newAnswer.setContent(content);
							newAnswer.setScore(score);
							newAnswer.setDeleteFlag(deleteFlag);
							if(cScore <= score && deleteFlag == Integer.parseInt(RiskEnum.CONFIG_IS_NOT_DELETE_FLAG.getValue())){
								cScore = score;
							}
							if(model.getIsSingle() == Integer.parseInt(RiskEnum.QUESTION_IS_NOT_SINGLE.getValue())
									&& deleteFlag == Integer.parseInt(RiskEnum.CONFIG_IS_NOT_DELETE_FLAG.getValue())){
								//多选的时候的试题分值
								qScore += score;
							}else if(deleteFlag == Integer.parseInt(RiskEnum.CONFIG_IS_NOT_DELETE_FLAG.getValue())){
								qScore = cScore;
							}
						}
						aList.add(newAnswer);
					}else{
						throw new BussinessException(ResourceUtils.get(RiskResource.RISK_CONFIG_PAPERS_SCORE_ERROR),BussinessException.TYPE_JSON);
					}
				}
				if(set.size() < answerContent.length+length){
					throw new BussinessException(ResourceUtils.get(RiskResource.RISK_ANSWER_SORT_IS_EXISTS),BussinessException.TYPE_JSON);
				}
			}
			model.setList(aList);//录入答案
		}
		model.setScore(qScore);//录分值
		return model;
	}
	
	/**
	 * 根据试卷ID查询问题及答案集合
	 */
	@Override
	public List<Question> findQuestionAndAnswer(final String papersId) {
		final List<Question> qlist = new ArrayList<Question>();
		final Question ques = new Question();
		ques.setPapersId(papersId);
		ques.setDeleteFlag(Constant.INT_ZERO);//未删除的问题
		Page<Question> page = new Page<Question>();
		page.setSort(RiskEnum.CUSTOM_PAGE_SORT_SORT.getValue());
		page.setOrder(Constant.ASC);
		ques.setPage(page);
		final List<Question> list = dao.findListByProperties(ques);
		if(CollectionUtils.isNotEmpty(list)){
			for(final Question question :list){
				final List<Answer> answers = answerService.findListByQId(question.getUuid(),CommonEnum.NO.getValue());
				question.setList(answers);
				qlist.add(question);
			}
		}else{
			throw new BussinessException(ResourceUtils.get(RiskResource.RISK_QUESTION_IS_WAIT_ADD),BussinessException.TYPE_CLOSE);
		}
		return qlist;
	}

	/**
	 * 获取试卷的总分
	 */
	@Override
	public int getTotalScore(final String papersId) {
		final int num = dao.getPapersTotalScore(papersId,CommonEnum.NO.getValue());
		return  num > Constant.INT_ZERO ? num : Constant.INT_ZERO;
	}
	
	/**
	 * 根据ID查询问题
	 */
	@Override
	public Question getQuestionById(final String id) {
		final Question question = dao.get(id);
		final List<Answer> list = answerService.findListByQId(id,CommonEnum.NO.getValue());
		question.setList(list);
		return question;
	}
	
	/**
	 * 根据试卷ID获取试卷有多少有用的题目
	 */
	@Override
	public int getQuestionNumByPapersId(final String papersId) {
		final int num = dao.getQuestionNumByPapersId(papersId,CommonEnum.NO.getValue());
		return  num;
	}
	
	/**
	 * 根据试卷ID查询试卷总分 
	 */
	@Override
	public int getPapersTotalScore(final String papersId) {
		return dao.getPapersTotalScore(papersId, CommonEnum.NO.getValue());
	}
	
	/**
	 * 根据问题对象修改问题对象
	 */
	@Override
	public int updateQuestion(final Question question) {
		return dao.update(question);
	}
}