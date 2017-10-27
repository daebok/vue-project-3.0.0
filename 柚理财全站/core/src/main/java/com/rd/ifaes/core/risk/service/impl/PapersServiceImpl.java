package com.rd.ifaes.core.risk.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rd.ifaes.common.constant.TokenConstants;
import com.rd.ifaes.common.dict.CommonEnum;
import com.rd.ifaes.common.dict.RiskEnum;
import com.rd.ifaes.common.exception.BussinessException;
import com.rd.ifaes.common.orm.Page;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.common.util.TokenUtils;
import com.rd.ifaes.common.util.resource.RiskResource;
import com.rd.ifaes.common.validator.BeanValidators;
import com.rd.ifaes.core.base.service.BaseServiceImpl;
import com.rd.ifaes.core.core.constant.CacheConstant;
import com.rd.ifaes.core.core.constant.Constant;
import com.rd.ifaes.core.core.util.CacheUtils;
import com.rd.ifaes.core.core.util.OrderNoUtils;
import com.rd.ifaes.core.core.util.ResourceUtils;
import com.rd.ifaes.core.risk.domain.Answer;
import com.rd.ifaes.core.risk.domain.LevelConfig;
import com.rd.ifaes.core.risk.domain.Papers;
import com.rd.ifaes.core.risk.domain.PapersScore;
import com.rd.ifaes.core.risk.domain.Question;
import com.rd.ifaes.core.risk.domain.RiskUserLog;
import com.rd.ifaes.core.risk.mapper.PapersMapper;
import com.rd.ifaes.core.risk.service.AnswerService;
import com.rd.ifaes.core.risk.service.LevelConfigService;
import com.rd.ifaes.core.risk.service.PapersScoreService;
import com.rd.ifaes.core.risk.service.PapersService;
import com.rd.ifaes.core.risk.service.QuestionService;
import com.rd.ifaes.core.risk.service.RiskUserLogService;
import com.rd.ifaes.core.user.domain.User;
import com.rd.ifaes.core.user.domain.UserCache;
import com.rd.ifaes.core.user.service.UserCacheService;

/**
 * 试卷业务处理
 * 
 * @version 3.0
 * @author QianPengZhan
 * @date 2016年8月5日
 */
@Service("papersService")
public class PapersServiceImpl extends BaseServiceImpl<PapersMapper, Papers>
		implements PapersService {
	/**
	 * questionService业务
	 */
	@Resource
	private transient QuestionService questionService;
	/**
	 * answerService业务
	 */
	@Resource
	private transient AnswerService answerService;
	/**
	 * papersScoreService业务
	 */
	@Resource
	private transient PapersScoreService papersScoreService;
	/**
	 * levelConfigService业务
	 */
	@Resource
	private transient LevelConfigService levelConfigService;
	/**
	 * userLogService业务
	 */
	@Resource
	private transient RiskUserLogService userLogService;
	/**
	 * userCacheService业务
	 */
	@Resource
	private transient UserCacheService userCacheService;

	/**
	 * 批量删除
	 */
	@Override
	public void deleteBatch(final String[] ids) {
		if (ids == null || ids.length == Constant.INT_ZERO) {
			throw new BussinessException(ResourceUtils.get(RiskResource.RISK_SELECTED_DELETE_ERROR),BussinessException.TYPE_CLOSE);
		}
		for (int i = 0; i < ids.length; i++) {
			final Papers papers = dao.get(ids[i]);
			if(papers.getStatus() == Constant.INT_ONE){
				throw new BussinessException(ResourceUtils.get(RiskResource.RISK_PAPERS_ON_NOT_DELETE),BussinessException.TYPE_CLOSE);
			}
		}
		dao.deleteBatch(ids);
	}
	
	/**
	 * 获取papers
	 */
	@Override
	public Papers get(final String id) {
		if (StringUtils.isBlank(id)) {
			throw new BussinessException(ResourceUtils.get(RiskResource.RISK_PAPERS_ID_IS_EMPTY),BussinessException.TYPE_JSON);
		}
		return dao.get(id);
	}
	
	/**
	 * 添加
	 */
	@Override
	@Transactional(readOnly = false)
	public void insert(final Papers entity) {
		BeanValidators.validate(entity);// 校验
		TokenUtils.validShiroToken(TokenConstants.TOKEN_PAPERS_ADDSINGLE);
		entity.preInsert();
		dao.insert(entity);
	}
	
	/**
	 * 分页查询
	 */
	@Override
	public Page<Papers> findPage(final Papers entity) {
		entity.setDeleteFlag(Integer.parseInt(RiskEnum.CONFIG_IS_NOT_DELETE_FLAG.getValue()));
		final List<Papers> pList = new ArrayList<Papers>();
		final List<Papers> list = dao.findPageList(entity);
		for (final Papers p : list) {
			final int num = questionService.getQuestionNumByPapersId(p.getUuid());
			int totalScore = Constant.INT_ZERO;
			if (num > Constant.INT_ZERO) {
				totalScore = questionService.getPapersTotalScore(p.getUuid());
				p.setCanSearch(CommonEnum.YES.getValue());
			}else{
				p.setCanSearch(CommonEnum.NO.getValue());
			}
			p.setTotalScore(totalScore);
			pList.add(p);
		}
		final Page<Papers> page = entity.getPage();
		page.setRows(pList);
		return page;
	}
	
	/**
	 * 查询列表
	 */
	@Override
	public List<Papers> findList(final Papers model) {
		model.setDeleteFlag(Integer.parseInt(RiskEnum.CONFIG_IS_NOT_DELETE_FLAG.getValue()));
		return dao.findList(model);
	}
	
	/**
	 * 查询已经发布的试卷
	 */
	@Override
	public Papers findPublishPapers() {
		final Papers papers = dao.getPapersByStatusAndDeleteFlag(RiskEnum.PAPERS_PUBLISH.getValue(), CommonEnum.NO.getValue());
		if (papers == null) {
			throw new BussinessException(ResourceUtils.get(RiskResource.RISK_PAPER_NOT_PUBLISH), BussinessException.TYPE_JSON);
		}
		return papers;
	}

	/**
	 * 做试卷
	 */
	@Override
	@Transactional(readOnly=false)
	public Map<String, Object> doUserRiskPapers(final Papers papers,
			final String[] questionContent, final User user) {
		if(papers == null){
			throw new BussinessException(ResourceUtils.get(RiskResource.RISK_PAPERS_IS_NULL),BussinessException.TYPE_JSON);
		}
		if (questionContent == null|| questionContent.length == Constant.INT_ZERO) {
			throw new BussinessException(ResourceUtils.get(RiskResource.RISK_PAPER_ANSWER_IS_EMPTY),BussinessException.TYPE_JSON);
		}
		Map<String, Object> map = new HashMap<>();
		Answer answer = new Answer();
		String answers = StringUtils.EMPTY;
		int totalScore = Constant.INT_ZERO;
		int levelVal = Constant.INT_ZERO;
		String levelName= StringUtils.EMPTY;
		for (int i = Constant.INT_ZERO; i < questionContent.length; i++) {
			String content = questionContent[i];
			final String[] contentSplit = content.replace("{", "").replace("}", "").split(",");
			final String quuid = contentSplit[Constant.INT_ZERO];
			questionService.get(quuid);//试题校验是否真实存在
			answers += "{" + quuid+",";
			for(int j = Constant.INT_ONE;j < contentSplit.length;j++){
				final String auuid = contentSplit[j];
				answer = answerService.get(auuid);
				if (j == contentSplit.length - Constant.INT_ONE) {
					answers += auuid+"}concat";
				}else{
					answers += auuid+",";
				}
				totalScore += answer.getScore();
			}
		}
		final List<PapersScore> psList = papersScoreService.findPapersScoreByPapersId(papers.getUuid());
		for (int i = Constant.INT_ZERO; i < psList.size(); i++) {
			final PapersScore papersScore = psList.get(i);
			if (totalScore >= papersScore.getStartScore()
					&& totalScore <= papersScore.getEndScore()) {
				final LevelConfig levelConfig = levelConfigService.get(papersScore.getRiskId());
				levelVal = levelConfig.getUserRiskLevelVal();
				levelName = levelConfig.getUserRiskLevelName();
			}
		}
		final RiskUserLog log = new RiskUserLog();
		log.preInsert();
		log.setPapersId(papers.getUuid());
		log.setUserId(user.getUuid());
		log.setScore(totalScore);
		log.setRiskLevel(levelVal);
		log.setRiskLevelName(levelName);
		log.setStatus(Integer.parseInt(RiskEnum.USER_LOG_STATUS_DO.getValue()));
		log.setRiskAnswers(answers);
		userLogService.insert(log);		
		final UserCache userCache = userCacheService.findByUserId(user.getUuid());
		userCache.setRiskLevel(String.valueOf(levelVal));
		userCacheService.update(userCache);
		CacheUtils.del(CacheConstant.KEY_PREFIX_USER_CACHE_USER_ID +userCache.getUserId());
		map.put("levelName", levelName);
		map.put("totalScore", totalScore);
		return map;
	}

	/**
	 * 添加试卷和等级
	 */
	@Override
	@Transactional(readOnly = false)
	public void insertPapersAndConfig(final Papers entity,final  String[][] scoreConfig) {
		entity.preInsert();
		entity.setPapersNo(OrderNoUtils.getSerialNumber());//随机生成试卷编号
		final List<PapersScore> psList = handleScore(scoreConfig, entity);
		TokenUtils.validShiroToken(TokenConstants.TOKEN_PAPERS_ADD);
		dao.insert(entity);
		papersScoreService.insertBatchPapersScores(psList);
	}

	private List<PapersScore> handleScore(final String[][] scoreConfig,final  Papers entity) {
		final String papersId = entity.getUuid();
		final List<PapersScore> psList = new ArrayList<PapersScore>();
		// 上一级最高分
		int beforeEndScore = Constant.INT_ZERO;
		for (int i = Constant.INT_ZERO; i < scoreConfig.length; i++) {
			String[] array = scoreConfig[i];
			if(array.length == Constant.INT_THREE){
				if(array[Constant.INT_ZERO] == null || array[Constant.INT_ONE] == null  || array[Constant.INT_TWO] == null){
					throw new BussinessException(ResourceUtils.get(RiskResource.RISK_CONFIG_MAX_MIN_SCORE_EMPTY),BussinessException.TYPE_JSON);
				}
				final String riskId = array[Constant.INT_ZERO];
				final int startScore = Integer.parseInt(array[Constant.INT_ONE]);
				final int endScore = Integer.parseInt(array[Constant.INT_TWO]);
				if(startScore >= endScore){
					throw new BussinessException(ResourceUtils.get(RiskResource.RISK_CONFIG_MIN_UP_MAX),BussinessException.TYPE_JSON);
				}
				if(i > Constant.INT_ZERO && startScore < beforeEndScore){
					throw new BussinessException(ResourceUtils.get(RiskResource.RISK_CONFIG_NEXT_MIN_UP_MAX),BussinessException.TYPE_JSON);
				}
				final PapersScore papersScore = new PapersScore();
				papersScore.setPapersId(papersId);
				papersScore.setRiskId(riskId);
				papersScore.setStartScore(startScore);
				papersScore.setEndScore(endScore);
				papersScore.preInsert();
				psList.add(papersScore);
				beforeEndScore = endScore;
			}else{
				throw new BussinessException(ResourceUtils.get(RiskResource.RISK_CONFIG_MAX_MIN_SCORE_EMPTY),BussinessException.TYPE_JSON);
			}
		}
		return psList;
	}

	@Override
	@Transactional(readOnly = false)
	public void updatePapersAndConfig(final Papers entity, final String[][] scoreConfig) {
		final List<PapersScore> list = handleUpdateSocre(scoreConfig);
		TokenUtils.validShiroToken(TokenConstants.TOKEN_PAPERS_EDIT);
		dao.update(entity);
		papersScoreService.updateBatch(list);
	}
	
	/**
	 * 修改等级
	 * @author QianPengZhan
	 * @date 2016年8月30日
	 * @param scoreConfig
	 * @return
	 */
	public List<PapersScore> handleUpdateSocre(final String[][] scoreConfig) {
		final List<PapersScore> list = new ArrayList<>();
		// 上一级最高分
		int beforeEndScore = Constant.INT_ZERO;
		for (int i = Constant.INT_ZERO; i < scoreConfig.length; i++) {
			final String[] array = scoreConfig[i];
			if (array.length == Constant.INT_THREE) {
				//1、校验三个参数
				if(array[Constant.INT_ZERO] == null || array[Constant.INT_ONE] == null  || array[Constant.INT_TWO] == null){
					throw new BussinessException(ResourceUtils.get(RiskResource.RISK_CONFIG_MAX_MIN_SCORE_EMPTY),BussinessException.TYPE_JSON);
				}
				//2、校验获取的分值
				final String uuid = array[Constant.INT_ZERO];
				final int startScore = Integer.parseInt(array[ Constant.INT_ONE]);
				final int endScore = Integer.parseInt(array[ Constant.INT_TWO]);
				if(startScore >= endScore){
					throw new BussinessException(ResourceUtils.get(RiskResource.RISK_CONFIG_MIN_UP_MAX),BussinessException.TYPE_JSON);
				}
				if(i > Constant.INT_ZERO && startScore < beforeEndScore){
					throw new BussinessException(ResourceUtils.get(RiskResource.RISK_CONFIG_NEXT_MIN_UP_MAX),BussinessException.TYPE_JSON);
				}
				//3、添加分数等级
				final PapersScore papersScore = papersScoreService.get(uuid);
				papersScore.setStartScore(startScore);
				papersScore.setEndScore(endScore);
				list.add(papersScore);
				beforeEndScore = endScore;
			}else{
				throw new BussinessException(ResourceUtils.get(RiskResource.RISK_CONFIG_PAPERS_SCORE_ERROR),BussinessException.TYPE_JSON);
			}
		}
		return list;
	}

	@Override
	@Transactional(readOnly = false)
	public int modifyStatus(final String uuid, final String publish) {
		final Papers model = dao.get(uuid);
		model.setStatus(Integer.parseInt(publish));
		return dao.update(model);
	}

	@Override
	@Transactional(readOnly = false)
	public void insertAllPapers(final Papers papers,final  String[] questionContent,
			final String[] riskContent) {
		if (questionContent == null || riskContent == null
				|| questionContent.length == Constant.INT_ZERO|| riskContent.length == Constant.INT_ZERO) {
			throw new BussinessException(ResourceUtils.get(RiskResource.RISK_PAPER_CONFIG_ADD_FAIL),BussinessException.TYPE_JSON);
		}
		papers.setQuestionCount(questionContent.length);
		List<PapersScore> list = handleRisk(papers.getUuid(), riskContent);
		handlequestionContent(questionContent,papers.getUuid());
		TokenUtils.validShiroToken(TokenConstants.TOKEN_ALLPAPERS_ADD);
		dao.update(papers);
		papersScoreService.updateBatch(list);
	}

	/**
	 * 处理风险类型
	 * 
	 * @author QianPengZhan
	 * @date 2016年7月27日
	 * @param papersId
	 * @param riskContent
	 * @return
	 */
	private List<PapersScore> handleRisk(final String papersId,final  String[] riskContent) {
		List<PapersScore> list = new ArrayList<>();
		for (int i = Constant.INT_ZERO; i < riskContent.length; i++) {
			final String risk = riskContent[i];
			if (StringUtils.isNotBlank(risk)) {
				final String[] levelArr = StringUtils.trimFirstAndLastChar(StringUtils.trimFirstAndLastChar(risk, '['), ']').split(",");
				if (levelArr.length == Constant.INT_THREE) {
					final String uuid = levelArr[0];
					final int startScore = Integer.parseInt(levelArr[1]);
					final int endScore = Integer.parseInt(levelArr[2]);
					final PapersScore papersScore = new PapersScore();
					papersScore.setPapersId(papersId);
					papersScore.setRiskId(uuid);
					papersScore.setStartScore(startScore);
					papersScore.setEndScore(endScore);
					papersScore.preInsert();
					list.add(papersScore);
				}
			}
		}
		return list;
	}

	/**
	 * 处理前台传来的问题 答案数组
	 * 
	 * @author QianPengZhan
	 * @date 2016年7月27日
	 * @param questionContent
	 * @param papersId
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void handlequestionContent(final String[] questionContent, final String papersId) {
		for (int i = Constant.INT_ZERO; i < questionContent.length; i++) {
			final String question = questionContent[i];
			if (!StringUtils.isBlank(question)) {
				final Question newQuestion = new Question();
				final String[] qArr = StringUtils.trimFirstAndLastChar(
						StringUtils.trimFirstAndLastChar(question, '{'), '}')
						.split("=");
				final int qSort = Integer.parseInt(qArr[Constant.INT_ZERO]);
				final String qContent = qArr[Constant.INT_ONE];
				final int qSingle = Integer.parseInt(qArr[Constant.INT_TWO]);
				final String answers = qArr[Constant.INT_THREE];
				int qScore = Constant.INT_ZERO;
				int cScore = Constant.INT_ZERO;// 临时的最大分值
				final Set set = new HashSet();
				newQuestion.preInsert();
				newQuestion.setPapersId(papersId);
				newQuestion.setSort(qSort);
				newQuestion.setQuestionName(qContent);
				newQuestion.setIsSingle(qSingle);
				questionService.insert(newQuestion);
				final String[] aArr = StringUtils.trimFirstAndLastChar(
						StringUtils.trimFirstAndLastChar(answers, '['), ']')
						.split(":");
				for (int j = Constant.INT_ZERO; j < aArr.length; j++) {
					final Answer newAnswer = new Answer();
					final String[] answer = StringUtils
							.trimFirstAndLastChar(
									StringUtils.trimFirstAndLastChar(aArr[j],
											'{'), '}').split("-");
					final String aSort = answer[Constant.INT_ZERO];
					set.add(aSort);
					final String aContent = answer[Constant.INT_ONE];
					final int aScore = Integer.parseInt(answer[Constant.INT_TWO]);
					if (cScore <= aScore) {
						cScore = aScore;
					}
					if (qSingle == Integer.parseInt(RiskEnum.QUESTION_IS_NOT_SINGLE.getValue())) {
						// 多选的时候的试题分值
						qScore += cScore;
					} else {
						qScore = cScore;
					}
					newAnswer.preInsert();
					newAnswer.setQuestionId(newQuestion.getUuid());
					newAnswer.setContent(aContent);
					newAnswer.setSort(aSort);
					newAnswer.setScore(aScore);
					answerService.insert(newAnswer);
				}
				if (set.size() < aArr.length) {
					throw new BussinessException(ResourceUtils.get(RiskResource.RISK_ANSWER_SORT_IS_EXISTS),BussinessException.TYPE_JSON);
				}
				newQuestion.setScore(qScore);
				questionService.updateQuestion(newQuestion);
			}
		}
	}

	@Override
	public void updateAllPapers(final Papers papers, final String[] questionContent,
			final String[] riskContent) {
		if (questionContent == null || riskContent == null
				|| questionContent.length == Constant.INT_ZERO || riskContent.length == Constant.INT_ZERO) {
			throw new BussinessException(ResourceUtils.get(RiskResource.RISK_CONFIG_PAPERS_UPDATE_FAIL));
		}
		// 修改等级
		List<PapersScore> list = handlePapersScore(riskContent);
		// 修改或者添加问题和答案
		handleUpdateQuestion(questionContent,papers.getUuid());
		TokenUtils.validShiroToken(TokenConstants.TOKEN_ALLPAPERS_EDIT);
		dao.update(papers);
		papersScoreService.updateBatch(list);
	}

	/**
	 *  整卷修改问题和答案或者添加答案
	 * 
	 * @author QianPengZhan
	 * @date 2016年7月27日
	 * @param questionContent
	 * @return
	 */
	@Deprecated
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void handleUpdateQuestion(final String[] questionContent, final String papersId) {
		for (int i =Constant.INT_ZERO; i < questionContent.length; i++) {
			final String question = questionContent[i];
			if (!StringUtils.isBlank(question)) {
				final String[] qArr = StringUtils.trimFirstAndLastChar(
						StringUtils.trimFirstAndLastChar(question, '{'), '}')
						.split("`");
				final String quuid = qArr[Constant.INT_ZERO];
				Question newQuestion = null;
				if (StringUtils.isBlank(quuid)) {
					newQuestion = new Question();
				} else {
					newQuestion = questionService.get(quuid);
				}
				final int qSort = Integer.parseInt(qArr[Constant.INT_ONE]);
				final String qContent = qArr[Constant.INT_TWO];
				final int qSingle = Integer.parseInt(qArr[Constant.INT_THREE]);
				final int qDelete = Integer.parseInt(qArr[Constant.INT_FOUR]);
				final String answers = qArr[Constant.INT_FIVE];
				int qScore = Constant.INT_ZERO;
				int cScore = Constant.INT_ZERO;// 临时的最大分值
				final Set set = new HashSet();
				if (StringUtils.isBlank(quuid)) {
					newQuestion.preInsert();
					newQuestion.setPapersId(papersId);
				}
				newQuestion.setSort(qSort);
				newQuestion.setQuestionName(qContent);
				newQuestion.setIsSingle(qSingle);
				newQuestion.setDeleteFlag(qDelete);
				if (StringUtils.isBlank(quuid)) {
					questionService.insert(newQuestion);
				}
				if (answers.contains("[") && answers.contains("]")
						&& answers.contains("#")) {
					final String[] aArr = StringUtils
							.trimFirstAndLastChar(
									StringUtils.trimFirstAndLastChar(answers,
											'['), ']').split("\\#");
					if (aArr != null && aArr.length > Constant.INT_ZERO) {
						for (int j = 0; j < aArr.length; j++) {
							final String[] answer = StringUtils.trimFirstAndLastChar(
									StringUtils.trimFirstAndLastChar(aArr[j],
											'{'), '}').split("\\$");
							final String auuid = answer[Constant.INT_ZERO];
							Answer anaswer = null;
							if (StringUtils.isBlank(auuid)) {
								anaswer = new Answer();
							} else {
								anaswer = answerService.get(auuid);
							}
							final String aSort = answer[Constant.INT_ONE];
							set.add(aSort);
							final String aContent = answer[Constant.INT_TWO];
							final int aScore = Integer.parseInt(answer[Constant.INT_THREE]);
							final int aDelete = Integer.parseInt(answer[Constant.INT_FOUR]);
							if (cScore <= aScore) {
								cScore = aScore;
							}
							if (qSingle == Integer.parseInt(RiskEnum.QUESTION_IS_NOT_SINGLE.getValue())) {
								// 多选的时候的试题分值
								qScore += cScore;
							} else {
								qScore = cScore;
							}
							if (StringUtils.isBlank(auuid)) {
								anaswer.preInsert();
								anaswer.setQuestionId(newQuestion.getUuid());
							}
							anaswer.setContent(aContent);
							anaswer.setSort(aSort);
							anaswer.setScore(aScore);
							if (StringUtils.isBlank(auuid)) {
								answerService.insert(anaswer);
							} else {
								anaswer.setDeleteFlag(aDelete);
								answerService.update(anaswer);
							}
						}
					}
					if (aArr != null && set.size() < aArr.length) {
						throw new BussinessException(ResourceUtils.get(RiskResource.RISK_ANSWER_SORT_IS_EXISTS));
					}
				}
				newQuestion.setScore(qScore);
				questionService.updateQuestion(newQuestion);
			}
		}
	}

	/**
	 * 修改等级分数
	 * 
	 * @author QianPengZhan
	 * @date 2016年7月27日
	 * @param riskContent
	 * @return
	 */
	private List<PapersScore> handlePapersScore(final String[] riskContent) {
		List<PapersScore> list = new ArrayList<>();
		for (int i =  Constant.INT_ZERO; i < riskContent.length; i++) {
			final String risk = riskContent[i];
			if (!StringUtils.isBlank(risk)) {
				final String[] levelArr = StringUtils.trimFirstAndLastChar(StringUtils.trimFirstAndLastChar(risk, '['), ']').split(",");
				if (levelArr.length ==  Constant.INT_THREE) {
					final String uuid = levelArr[Constant.INT_ZERO];
					final 	int startScore = Integer.parseInt(levelArr[Constant.INT_ONE]);
					final int endScore = Integer.parseInt(levelArr[Constant.INT_TWO]);
					final PapersScore papersScore = papersScoreService.get(uuid);
					papersScore.setStartScore(startScore);
					papersScore.setEndScore(endScore);
					list.add(papersScore);
				}
			}
		}
		return list;
	}

	/**
	 * 直接执行发布
	 */
	@Override
	public void doPapersPublish(final String[] ids,final  String publish) {
		if (ids == null || ids.length == Constant.INT_ZERO) {
			throw new BussinessException(ResourceUtils.get(RiskResource.RISK_PAPERS_PUBLISH_ID_NULL),BussinessException.TYPE_CLOSE);
		}
		for (int i = Constant.INT_ZERO; i < ids.length; i++) {
			final Papers model = new Papers();
			model.setStatus(Integer.parseInt(RiskEnum.PAPERS_PUBLISH.getValue()));
			model.setDeleteFlag(Constant.INT_ZERO);
			final int count = dao.getCount(model);
			if (count != Constant.INT_ZERO  && publish.equals(RiskEnum.PAPERS_PUBLISH.getValue())) {
				throw new BussinessException(ResourceUtils.get(RiskResource.RISK_PAPERS_PUBLISH_AFTER_CANCLE),BussinessException.TYPE_CLOSE);
			}
			modifyStatus(ids[i], publish);
		}
	}
	
}