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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rd.ifaes.common.annotation.SystemLog;
import com.rd.ifaes.common.annotation.TokenValid;
import com.rd.ifaes.common.constant.TokenConstants;
import com.rd.ifaes.common.dict.RiskEnum;
import com.rd.ifaes.common.exception.BussinessException;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.common.util.resource.RiskResource;
import com.rd.ifaes.core.core.constant.SysLogConstant;
import com.rd.ifaes.core.core.util.ResourceUtils;
import com.rd.ifaes.core.risk.domain.LevelConfig;
import com.rd.ifaes.core.risk.domain.Papers;
import com.rd.ifaes.core.risk.domain.PapersScore;
import com.rd.ifaes.core.risk.domain.Question;
import com.rd.ifaes.core.risk.service.LevelConfigService;
import com.rd.ifaes.core.risk.service.PapersScoreService;
import com.rd.ifaes.core.risk.service.PapersService;
import com.rd.ifaes.core.risk.service.QuestionService;
import com.rd.ifaes.manage.sys.SystemController;

/**
 * 后台试卷管理类
 * @version 3.0
 * @author QianPengZhan
 * @date 2016年7月26日
 */
@Controller
public class RiskPapersManageController extends SystemController{
	/**
	 * 传递到前台参数PAPERS
	 */
	private static final String PAPERS = "papers";
	
	/**
	 * 传递到前台参数LEVELLIST
	 */
	private static final String LEVELLIST = "levelList";
	/**
	 * 试卷是否发布 
	 */
	private static final String NO_PUBLISH = RiskEnum.PAPERS_NO_PUBLISH.getValue();
	/**
	 * 试卷发布时 
	 */
	private static final String PUBLISH = RiskEnum.PAPERS_PUBLISH.getValue();
	/**
	 * 试卷类型 1 
	 */
	private static final String PTYPE_ONE = RiskEnum.PAPERS_TYPE_ONE.getValue();
	/**
	 * 试卷类型 2
	 */
	private static final String PTYPE_TWO = RiskEnum.PAPERS_TYPE_TWO.getValue();
	
	/**
	 * 试卷业务类
	 */
	@Resource
	private transient PapersService papersService;
	
	/**
	 * 问题业务类
	 */
	@Resource
	private transient QuestionService questionService;
	
	/**
	 * 等级业务类
	 */
	@Resource
	private transient LevelConfigService levelConfigService;
	
	/**
	 * 试卷分数业务类
	 */
	@Resource
	private transient PapersScoreService papersScoreService;
	

	/**
	 * 问卷的管理界面
	 * @author QianPengZhan
	 * @date 2016年7月26日
	 * @return
	 */
	@RequestMapping(value="/risk/riskPapersManage")
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.RISK_PAPER)
	public String riskPapersManage(){
		return "/risk/riskPapersManage";
	}
	
	
	/**
	 * 问卷管理（ 试卷 试题  左右2栏 展示）
	 * @author QianPengZhan
	 * @date 2016年7月26日
	 * @return
	 */
	@RequestMapping(value="/risk/riskPapersSingleManage")
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.RISK_PAPER)
	public String riskPapersSingleManage(){
		return "/risk/riskPapersSingleManage";
	}
		
	
	/**
	 * 问卷管理的列表（左边列表）
	 * @author QianPengZhan
	 * @date 2016年7月26日
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/risk/riskPapersList")
	@ResponseBody
	@RequiresPermissions("set:riskTpl:question:query")
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.RISK_PAPER)
	public Object riskPapersList(final Papers model){
		if(StringUtils.isNotBlank(model.getKeywords())){
			model.setPapersName(model.getKeywords());//问卷名称
		}
		if(StringUtils.isBlank(model.getPapersType())){
			model.setPapersTypeSet(new String[]{PTYPE_ONE,PTYPE_TWO});
		}
		if(StringUtils.isBlank(model.getStatus())){
			model.setStatusSet(new String[]{NO_PUBLISH,PUBLISH});
		}
		return papersService.findPage(model);
	}
	
	/**
	 * 进入问卷的添加界面
	 * @author QianPengZhan
	 * @date 2016年7月26日
	 * @return
	 */
	@RequestMapping(value = "/risk/riskPapersAddPage")
	@RequiresPermissions("set:riskTpl:question:add")
	@SystemLog(method=SysLogConstant.ADD,content=SysLogConstant.RISK_PAPER)
	public String addPage(final Model model) {
		final List<LevelConfig> levelConfig  = levelConfigService.findByOrder();
		model.addAttribute(LEVELLIST, levelConfig);
		return "/risk/riskPapersAddPage";
	}
	
	/**
	 * 问卷的添加操作
	 * @author QianPengZhan
	 * @date 2016年7月26日
	 * @param model
	 * @param scoreConfig
	 * @return
	 */
	@RequestMapping(value = "/risk/riskPapersAdd", method = RequestMethod.POST)
	@ResponseBody
	@RequiresPermissions("set:riskTpl:question:add")
	@TokenValid(value = TokenConstants.TOKEN_PAPERS_ADD, dispatch = true)
	@SystemLog(method=SysLogConstant.ADD,content=SysLogConstant.RISK_PAPER)
	public Object add(final Papers model,@RequestParam final String[][] scoreConfig){		
		papersService.insertPapersAndConfig(model,scoreConfig) ;
		return renderResult(true,MSG_SUCCESS);	 
	}
	
	
	/**
	 * 进入问卷的编辑界面
	 * @author QianPengZhan
	 * @date 2016年7月26日
	 * @param id
	 * @param request
	 * @return
	 */
	@RequiresPermissions("set:riskTpl:question:edit")
	@RequestMapping(value="/risk/riskPapersEditPage")
	@SystemLog(method=SysLogConstant.EDIT,content=SysLogConstant.RISK_PAPER)
	public String editPage(final String id,final Model model) {
		if(StringUtils.isNotBlank(id)){
			model.addAttribute(PAPERS, papersService.get(id));
			final List<PapersScore> psList  = papersScoreService.findPapersScoreByPapersId(id);
			 model.addAttribute("psList", psList);
		}
		return "/risk/riskPapersEditPage";
	}
	
	/**
	 * 编辑问卷操作
	 * @author QianPengZhan
	 * @date 2016年7月26日
	 * @param model
	 * @param scoreConfig
	 * @return
	 */
	@RequestMapping(value="/risk/riskPapersEdit",method = RequestMethod.POST)
	@ResponseBody
	@RequiresPermissions("set:riskTpl:question:edit")
	@SystemLog(method=SysLogConstant.EDIT,content=SysLogConstant.RISK_PAPER)
	@TokenValid(value = TokenConstants.TOKEN_PAPERS_EDIT, dispatch = true)
	public Object edit(final Papers model,@RequestParam final String[][] scoreConfig){
		papersService.updatePapersAndConfig(model,scoreConfig) ;
		return renderResult(true,MSG_SUCCESS);	 
	}
	
	/**
	 * 批量逻辑删除
	 * @author QianPengZhan
	 * @date 2016年7月26日
	 * @param ids
	 * @return
	 */
	@RequestMapping(value = "/risk/PapersDelLogic")
	@ResponseBody
	@SystemLog(method=SysLogConstant.DEL,content=SysLogConstant.RISK_PAPER)
	public Object delLogic(final String[] ids) {
		papersService.deleteBatch(ids);
		return renderResult(true,MSG_SUCCESS);	 
	}
	
	/**
	 * 问卷预览
	 * @author QianPengZhan
	 * @date 2016年7月26日
	 * @param id
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/risk/riskPapersSearch")
	@RequiresPermissions("set:riskTpl:question:query")
	@SystemLog(method=SysLogConstant.VIEW,content=SysLogConstant.RISK_PAPER)
	public String riskPapersSearch(final String id,final Model model) {
		if(id == null){
			throw new BussinessException(ResourceUtils.get(RiskResource.RISK_PAPERS_ID_IS_NULL),BussinessException.TYPE_JSON);
		}
		 model.addAttribute("papers", papersService.get(id));
		final List<Question> qList = questionService.findQuestionAndAnswer(id);
		 model.addAttribute("qList", qList);
		final List<PapersScore> levelConfig  = papersScoreService.findPapersScoreByPapersId(id);
		 model.addAttribute("levelList", levelConfig);
		final int totalScore = questionService.getTotalScore(id);
		 model.addAttribute("totalScore", totalScore);
		return "/risk/riskPapersSearch";
	}
	
	
	
	/**
	 * 进入问卷试题添加界面
	 * @author QianPengZhan
	 * @date 2016年7月26日
	 * @param id
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/risk/riskQuestionAddPage")
	@RequiresPermissions("set:riskTpl:question:answeradd")
	@SystemLog(method=SysLogConstant.ADD,content=SysLogConstant.RISK_QUESTION)
	public String addQuestion(final String id,final Model model) {
		if(StringUtils.isNotBlank(id)){
			 model.addAttribute("papers", papersService.get(id));
		}
		return "/risk/riskQuestionAddPage";
	}

	/**
	 * 问卷问题单个添加操作
	 * @author QianPengZhan
	 * @date 2016年7月26日
	 * @param question
	 * @param answerContent
	 * @return
	 */
	@RequestMapping(value="/risk/riskQuestionAdd",method = RequestMethod.POST)
	@ResponseBody
	@RequiresPermissions("set:riskTpl:question:add")
	@TokenValid(value = TokenConstants.TOKEN_SINGLE_QUESTION_ADD, dispatch = true)
	@SystemLog(method=SysLogConstant.ADD,content=SysLogConstant.RISK_QUESTION)
	public Object questionAdd(final Question question,@RequestParam final String[][] answerContent){
		questionService.insertQuestionAndAnswer(question,answerContent) ;
		return renderResult(true,MSG_SUCCESS);	 
	}
	
	
	
	/**
	 * 直接执行发布
	 * @author QianPengZhan
	 * @date 2016年7月26日
	 * @param ids
	 * @param publish
	 * @return
	 */
	@RequestMapping(value="/risk/riskPapersDoPublish")
	@ResponseBody
	@SystemLog(method=SysLogConstant.GRANT,content=SysLogConstant.RISK_QUESTION)
	public Object riskPapersDoPublish(final String[] ids,final String publish){
		papersService.doPapersPublish(ids,publish);
		return renderResult(true,MSG_SUCCESS);	 
	}
}
