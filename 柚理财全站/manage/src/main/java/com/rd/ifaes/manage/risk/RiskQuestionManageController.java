/**
* 互联网金融交易系统ifaes (Internet Finance  Assets  Exchange  System)
 * Copyright (c) 2016 杭州融都科技股份有限公司 版权所有
 */
package com.rd.ifaes.manage.risk;

import java.util.List;

import javax.annotation.Resource;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rd.ifaes.common.annotation.SystemLog;
import com.rd.ifaes.common.annotation.TokenValid;
import com.rd.ifaes.common.constant.TokenConstants;
import com.rd.ifaes.common.dict.RiskEnum;
import com.rd.ifaes.common.orm.Page;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.core.core.constant.Constant;
import com.rd.ifaes.core.core.constant.SysLogConstant;
import com.rd.ifaes.core.risk.domain.Answer;
import com.rd.ifaes.core.risk.domain.Question;
import com.rd.ifaes.core.risk.service.AnswerService;
import com.rd.ifaes.core.risk.service.QuestionService;
import com.rd.ifaes.manage.sys.SystemController;

/**
 * 试题管理类
 * @version 3.0
 * @author QianPengZhan
 * @date 2016年7月26日
 */
@Controller
public class RiskQuestionManageController extends SystemController{
	/**
	 * 单选
	 */
	private static final String SINGLE = RiskEnum.QUESTION_IS_SINGLE.getValue();
	
	/**
	 * 非单选
	 */
	private static final String NO_SINGLE = RiskEnum.QUESTION_IS_NOT_SINGLE.getValue();
	/**
	 * 问题业务类
	 */
	@Resource
	private transient QuestionService questionService;

	/**
	 * 答案业务类
	 */
	@Resource
	private transient AnswerService answerService;

	
	/**
	 * 试题的列表(右边列表)
	 * @author QianPengZhan
	 * @date 2016年7月26日
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/risk/riskQuestionList")
	@ResponseBody
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.RISK_QUESTION)
	public Object riskQuestionList(final Question model){
		//默认采用自定义排序号 排序 add xxb 2016-09-28
		Page<Question> page = new Page<Question>();
		page.setSort(RiskEnum.CUSTOM_PAGE_SORT_SORT.getValue());
		page.setOrder(Constant.ASC);
		model.setPage(page);
		
		if(StringUtils.isNotBlank(model.getKeywords())){
			model.setQuestionName(model.getKeywords());//问卷名称
		}
		if(StringUtils.isBlank(model.getIsSingle())){
			model.setIsSingleSet(new String []{SINGLE,NO_SINGLE});
		}
		return questionService.findPage(model);
	}
	
	/**
	 * 进入试题编辑页面
	 * @author QianPengZhan
	 * @date 2016年7月26日
	 * @param id
	 * @param request
	 * @return
	 */
	@RequiresPermissions("set:riskTpl:question:answeredit")
	@RequestMapping(value="/risk/riskQuestionEditPage")
	@SystemLog(method=SysLogConstant.EDIT,content=SysLogConstant.RISK_QUESTION)
	public String riskQuestionEditPage(final String id,final Model model) {
		if(StringUtils.isNotBlank(StringUtils.isNull(id))){
			 model.addAttribute("question", questionService.get(id));
			final Answer answer = new Answer();
			answer.setQuestionId(id);
			final List<Answer> aList = answerService.findList(answer);
			 model.addAttribute("aList", aList);
		}
		return "/risk/riskQuestionEditPage";
	}
	
	/**
	 * 编辑试题
	 * @author QianPengZhan
	 * @date 2016年7月26日
	 * @param model
	 * @param answerContent
	 * @return
	 */
	@RequiresPermissions("set:riskTpl:question:answeredit")
	@RequestMapping(value="/risk/riskQuestionEdit",method = RequestMethod.POST)
	@ResponseBody
	@TokenValid(value = TokenConstants.TOKEN_PAPERS_QA_EDIT, dispatch = true)
	@SystemLog(method=SysLogConstant.EDIT,content=SysLogConstant.RISK_QUESTION)
	public Object edit(final Question model){
		questionService.updateQuestionAndAnswer(model,model.getAnswerContent(),model.getAnswerContentAdd()) ;
		return renderResult(true, MSG_SUCCESS);
	}
	
	/**
	 * 批量逻辑删除
	 * @author QianPengZhan
	 * @date 2016年7月26日
	 * @param ids
	 * @return
	 */
	@RequiresPermissions("set:riskTpl:question:answerdel")
	@RequestMapping(value = "/risk/questionDelLogic")
	@ResponseBody
	@SystemLog(method=SysLogConstant.DEL,content=SysLogConstant.RISK_QUESTION)
	public Object delLogic(final String[] ids) {
		questionService.deleteBatch(ids);
		return  renderResult(true, MSG_SUCCESS);
	}
	
	
	/**
	 * 进入试题添加答案的界面
	 * @author QianPengZhan
	 * @date 2016年7月26日
	 * @param id
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/risk/riskAnswerAddPage.html")
	@SystemLog(method=SysLogConstant.ADD,content=SysLogConstant.RISK_QUESTION)
	public String riskAnswerAddPage(final String id,final Model model) {
		if(StringUtils.isNotBlank(StringUtils.isNull(id))){
			 model.addAttribute("question", questionService.get(id));
		}
		return "/risk/riskAnswerAddPage";
	}
	
	/**
	 * 答案添加操作
	 * @author QianPengZhan
	 * @date 2016年7月26日
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/risk/riskAnswerAdd.html",method = RequestMethod.POST)
	@ResponseBody
	@TokenValid(value = TokenConstants.TOKEN_ANSWER_ADD, dispatch = true)
	@SystemLog(method=SysLogConstant.ADD,content=SysLogConstant.RISK_QUESTION)
	public Object riskAnswerAdd(final Answer model){
		answerService.insert(model) ;
		return  renderResult(true, MSG_SUCCESS); 
	}
	
	
	/**
	 * 问题预览(没有使用)
	 * @author QianPengZhan
	 * @date 2016年7月26日
	 * @param id
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/risk/riskQuestionSearch")
	public String questionSearch(final String id,final Model model) {
		if(StringUtils.isNotBlank(StringUtils.isNull(id))){
			final Question question = questionService.getQuestionById(id);
			 model.addAttribute("question", question);
		}
		return "/risk/riskQuestionSearch";
	}
	
	
	/**
	 * 问题管理界面(没有使用)
	 * @author QianPengZhan
	 * @date 2016年7月26日
	 * @return
	 */
	@RequestMapping(value="/risk/riskQuestionManage")
	public String riskPapersManage(){
		return "/risk/riskQuestionManage";
	}
}
