package com.rd.ifaes.core.user.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.shiro.util.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rd.ifaes.common.dict.DeleteFlagEnum;
import com.rd.ifaes.common.dict.DictTypeEnum;
import com.rd.ifaes.common.exception.BussinessException;
import com.rd.ifaes.common.util.SessionUtils;
import com.rd.ifaes.core.base.service.BaseServiceImpl;
import com.rd.ifaes.core.core.constant.Constant;
import com.rd.ifaes.core.core.constant.ResourceConstant;
import com.rd.ifaes.core.core.util.DictUtils;
import com.rd.ifaes.core.core.util.ResourceUtils;
import com.rd.ifaes.core.sys.domain.DictData;
import com.rd.ifaes.core.user.domain.User;
import com.rd.ifaes.core.user.domain.UserSecurityAnswer;
import com.rd.ifaes.core.user.mapper.UserSecurityAnswerMapper;
import com.rd.ifaes.core.user.model.UserSecurityAnswerModel;
import com.rd.ifaes.core.user.service.UserSecurityAnswerService;

/**
 * 密保问题业务处理类
 * @author xhf
 * @version 3.0
 * @date 2016-7-21
 */
@Service("userSecurityAnswerService") 
public class UserSecurityAnswerServiceImpl  extends BaseServiceImpl<UserSecurityAnswerMapper, UserSecurityAnswer> implements UserSecurityAnswerService{

	
	private static final Logger LOGGER = LoggerFactory.getLogger(UserSecurityAnswerServiceImpl.class);

	/**
	 * 根据userId查询密保问题答案
	 */
	@Override
	public List<UserSecurityAnswer> findByUserId(final String userId){
		return dao.findList(new UserSecurityAnswer(userId, "", DeleteFlagEnum.NO.getValue()));
	}
	
	/**
	 * 根据userId查询密保问题列表
	 */
	@Override
	public List<DictData> findUserPwdQuestion(final String userId){
		final List<DictData> dictDataList = new ArrayList<DictData>();
		final List<UserSecurityAnswer> answerList = findByUserId(userId);
		if(!CollectionUtils.isEmpty(answerList)){
			for (final UserSecurityAnswer answer : answerList) {
				dictDataList.add(DictUtils.getData(
						DictTypeEnum.SECURITY_QUESTION.getValue(), answer.getQuestionFlag()));
			}
		}

		return dictDataList;
	}
	
	/**
	 * 获得随机问题
	 */
	@Override
	public UserSecurityAnswer getRandomQuestion(final String userId, final String questionFlag){
		return dao.getRandomQuestion(new UserSecurityAnswer(userId, questionFlag, DeleteFlagEnum.NO.getValue()));
	}
	
	/**
	 * 获得指定问题答案
	 */
	@Override
	public UserSecurityAnswer findByQuestion(final String userId, final String questionFlag){
		final List<UserSecurityAnswer> list = dao.findList(new UserSecurityAnswer(userId, questionFlag, DeleteFlagEnum.NO.getValue()));
		UserSecurityAnswer answer = null;
		if(!CollectionUtils.isEmpty(list)){
			answer = list.get(0);
		}
		return answer;
	}
	
	/**
	 * 设置密保问题
	 */
	@Override
	@Transactional
	public void doSetPwdQuestion(final UserSecurityAnswerModel model){
		final User user = SessionUtils.getSessionUser();
		model.validForm();
		
		// 删除原来密保问题中的记录
		final List<UserSecurityAnswer> answerList = findByUserId(user.getUuid());
		for (UserSecurityAnswer answer : answerList) {
			dao.delete(answer.getUuid());
		}

		// 保存新增的数据
		final List<UserSecurityAnswer> dataList = new ArrayList<UserSecurityAnswer>();
		final UserSecurityAnswer securityAnswer1 = new UserSecurityAnswer(user.getUuid(),model.getAddIp());
		final UserSecurityAnswer securityAnswer2 = new UserSecurityAnswer(user.getUuid(),model.getAddIp());
		final UserSecurityAnswer securityAnswer3 = new UserSecurityAnswer(user.getUuid(),model.getAddIp());
		
		securityAnswer1.setQuestionFlag(model.getQuestion1());
		securityAnswer1.setAnswer(model.getAnswer1());
		securityAnswer1.setSort(1);
		securityAnswer1.preInsert();
		dataList.add(securityAnswer1);
		
		securityAnswer2.setQuestionFlag(model.getQuestion2());
		securityAnswer2.setAnswer(model.getAnswer2());
		securityAnswer2.setSort(2);
		securityAnswer2.preInsert();
		dataList.add(securityAnswer2);
		
		securityAnswer3.setQuestionFlag(model.getQuestion3());
		securityAnswer3.setAnswer(model.getAnswer3());
		securityAnswer3.setSort(3);
		securityAnswer3.preInsert();
		dataList.add(securityAnswer3);
		try {
			dao.insertBatch(dataList);
		} catch (Exception e) {
			LOGGER.error("密保问题插入失败",e);
			throw new BussinessException(ResourceUtils.get(ResourceConstant.SECURITYANSWER_SAVE_ERROR));
		}
	}
	
	/**
	 * 回答密保问题
	 */
	@Override
	public void doAnswerPwdQuestion(final UserSecurityAnswerModel model) {
		final User user =SessionUtils.getSessionUser();
		UserSecurityAnswerModel query=new UserSecurityAnswerModel();
		query.setUserId(user.getUuid());
		query.setDeleteFlag(Constant.FLAG_NO);
		List<UserSecurityAnswer> upqs= dao.findList(query);
		
		if (upqs.get(0)==null||!upqs.get(0).getAnswer().equals(model.getAnswer1())) {
			throw new BussinessException(ResourceUtils.get(ResourceConstant.ANSWER_FIRST_ERROR));
		}
		if (upqs.get(1)==null||!upqs.get(1).getAnswer().equals(model.getAnswer2())) {
			throw new BussinessException(ResourceUtils.get(ResourceConstant.ANSWER_TWO_ERROR));
		}
		if (upqs.get(2)==null||!upqs.get(2).getAnswer().equals(model.getAnswer3())) {
			throw new BussinessException(ResourceUtils.get(ResourceConstant.ANSWER_THREE_ERROR));
		}
	}
	
	/**
	 * 重置密保问题
	 */
	@Override
	public void doResetPwdQuestion(final UserSecurityAnswerModel model){
		final User user =SessionUtils.getSessionUser();
		model.setUserName(user.getUserName());
		model.setEmail(user.getEmail());
		model.setMobile(user.getMobile());
		model.validResetPwdQuest();
		
		//删除密保问题
		final List<UserSecurityAnswer> answerList = findByUserId(user.getUuid());
		for (UserSecurityAnswer answer : answerList) {
			dao.deleteLogic(answer.getUuid());
		}
	}

}