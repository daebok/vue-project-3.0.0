package com.rd.ifaes.core.risk.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.rd.ifaes.common.dict.CommonEnum;
import com.rd.ifaes.common.orm.Page;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.core.base.service.BaseServiceImpl;
import com.rd.ifaes.core.core.constant.Constant;
import com.rd.ifaes.core.risk.domain.Answer;
import com.rd.ifaes.core.risk.domain.Question;
import com.rd.ifaes.core.risk.domain.RiskUserLog;
import com.rd.ifaes.core.risk.mapper.RiskUserLogMapper;
import com.rd.ifaes.core.risk.service.AnswerService;
import com.rd.ifaes.core.risk.service.QuestionService;
import com.rd.ifaes.core.risk.service.RiskUserLogService;
import com.rd.ifaes.core.user.domain.UserCache;
import com.rd.ifaes.core.user.domain.UserCompanyInfo;
import com.rd.ifaes.core.user.service.UserCacheService;
import com.rd.ifaes.core.user.service.UserCompanyInfoService;

/**
 * ServiceImpl:UserLogServiceImpl
 * @author QianPengZhan
 * @version 3.0
 * @date 2016-7-14
 */
@Service("riskUserLogService") 
public class RiskUserLogServiceImpl  extends BaseServiceImpl<RiskUserLogMapper, RiskUserLog> implements RiskUserLogService{
	
	/**
	 * 问题业务
	 */
	@Resource
	private transient QuestionService questionService;
	/**
	 * 答案业务
	 */
	@Resource
	private transient AnswerService answerService;
	/**
	 * UserCacheService
	 */
	@Resource
	private transient UserCacheService userCacheService;
	/**
	 * UserCompanyInfoService
	 */
	@Resource
	private transient UserCompanyInfoService userCompanyInfoService;
	/**
	 * 根据用户查记录
	 */
	@Override
	public int getCountByUserId(final String userId){
		return dao.getCount(new RiskUserLog(userId,null));
	}
	
	@Override
	public Page<RiskUserLog> findPage(RiskUserLog entity) {
		Page<RiskUserLog> page = entity.getPage();
		if(page==null){
			page=new Page<RiskUserLog>();
		}
		final List<RiskUserLog> list = dao.findList(entity);
		for(final RiskUserLog riskUserLog : list){
			final UserCache userCache = userCacheService.findByUserId(riskUserLog.getUserId());
			if(!UserCache.USER_NATURE_PERSON.equals(userCache.getUserNature())){
				final UserCompanyInfo userCompanyInfo = userCompanyInfoService.findByUserId(riskUserLog.getUserId());
				riskUserLog.setRealName(userCompanyInfo.getCompanyName());
			}
		}
		page.setRows(list);
		return page;
	}



	/**
	 * 查出问题和答案
	 */
	@Override
	public List<Question> getQuestionAndAnswers(final RiskUserLog userLog) {
		final List<Question> list = new ArrayList<>();
		final String content = userLog.getRiskAnswers();
		final String[] arrOne = content.split("concat");
		if(arrOne==null || arrOne.length == 0){
			return list;
		}
		for (int i = Constant.INT_ZERO; i < arrOne.length; i++) {
			final String[] arrTwo = arrOne[i].replace("{", StringUtils.EMPTY).replace("}", StringUtils.EMPTY).split(",");
			final String qUuid = arrTwo[Constant.INT_ZERO];
			final Question question = questionService.get(qUuid);
			final List<Answer> answerList = answerService.findListByQIdAndAId(question.getUuid(), arrTwo);
			for (int j = Constant.INT_ONE; j < arrTwo.length; j++) {
				final String aUuid = arrTwo[j];
				for(Answer anser :answerList){
					if(anser.getUuid().equals(aUuid)){
						anser.setIsSelected(Constant.INT_ONE);
					}
				}
			}
			question.setList(answerList);
			list.add(question);
		}
		return list;
	}
}