/**  
 * All rights Reserved, Designed By QianPengZhan   
 * @Title:  RiskPapersWebController.java   
 * @Package com.rd.ifaes.web.controller.risk   
 * @date:   2016年7月15日 下午3:31:55   
 * @version V3.0     
 */
package com.rd.ifaes.web.controller.risk;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rd.ifaes.core.risk.domain.Papers;
import com.rd.ifaes.core.risk.domain.Question;
import com.rd.ifaes.core.risk.model.PapersModel;
import com.rd.ifaes.core.risk.service.PapersService;
import com.rd.ifaes.core.risk.service.QuestionService;
import com.rd.ifaes.core.user.domain.User;
import com.rd.ifaes.web.controller.WebController;

/**
 *  试卷前台控制层
 * @version 3.0
 * @author QianPengZhan
 * @date 2016年8月30日
 */
@Controller
public class RiskPapersWebController extends WebController{
	
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
	 * 构造函数
	 */
	public RiskPapersWebController() {
		super();
	}
	
	/**
	 * 进入风险评测
	 * @author QianPengZhan
	 * @date 2016年9月10日
	 * @return
	 */
	@RequestMapping(value = "/member/risk/userRiskPapers")
	public String userRiskPapers(final Model model) {
		final Papers papers = papersService.findPublishPapers();//找出发布了的试卷
		final List<Question> list = questionService.findQuestionAndAnswer(papers.getUuid());
		 model.addAttribute("papers", papers);
		 model.addAttribute("list", list);
		 model.addAttribute("questionNum", list.size());
		return "/member/risk/userRiskPapers";
	}
	
	/**
	 * @Title: doUserRiskPapers   
	 * @Description:用户评测逻辑处理
	 * @param: @return      
	 * @return: Object      
	 * @throws
	 */
	@RequestMapping(value = "/risk/doUserRiskPapers")
	@ResponseBody
	public Object doUserRiskPapers(final PapersModel papersModel){
		final String[] questionContents = papersModel.getQuestionContent().split("concat");
		final User user = checkUserNotLogin();
		final Papers papers = papersService.get(papersModel.getPapersId());
		final Map<String,Object> map = papersService.doUserRiskPapers(papers,questionContents,user);
		final String levelName = String.valueOf(map.get("levelName"));
		return renderResult(true, levelName);	
	}
}
