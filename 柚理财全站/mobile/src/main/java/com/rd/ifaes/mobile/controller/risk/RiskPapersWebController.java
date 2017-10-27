/**  
 * All rights Reserved, Designed By QianPengZhan   
 * @Title:  RiskPapersWebController.java   
 * @Package com.rd.ifaes.web.controller.risk   
 * @date:   2016年7月15日 下午3:31:55   
 * @version V3.0     
 */
package com.rd.ifaes.mobile.controller.risk;

import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rd.ifaes.core.core.constant.Constant;
import com.rd.ifaes.core.core.util.ConfigUtils;
import com.rd.ifaes.core.risk.domain.LevelConfig;
import com.rd.ifaes.core.risk.domain.Papers;
import com.rd.ifaes.core.risk.domain.Question;
import com.rd.ifaes.core.risk.model.PapersModel;
import com.rd.ifaes.core.risk.service.LevelConfigService;
import com.rd.ifaes.core.risk.service.PapersService;
import com.rd.ifaes.core.risk.service.QuestionService;
import com.rd.ifaes.core.user.domain.User;
import com.rd.ifaes.core.user.domain.UserCache;
import com.rd.ifaes.core.user.service.UserCacheService;
import com.rd.ifaes.mobile.controller.WebController;

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
	 * levelConfigService业务
	 */
	@Resource
	private transient LevelConfigService levelConfigService;
	/**
	 * 问题业务类
	 */
	@Resource
	private transient QuestionService questionService;
	@Resource
	private transient UserCacheService userCacheService;
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
	@RequestMapping(value = "/app/member/risk/userRiskPapers")
	@ResponseBody
	public Object userRiskPapers(HttpServletRequest request) {
		Map<String, Object> data = new HashMap<String, Object>();
		Object obj=null;
		try{
			@SuppressWarnings("unused")
			User user = getAppSessionUser(request);
		final Papers papers = papersService.findPublishPapers();//找出发布了的试卷
		final List<Question> list = questionService.findQuestionAndAnswer(papers.getUuid());
		final List<LevelConfig> configList = levelConfigService.findByOrder();
				data.put("papers", papers);
				data.put("list", list);
				data.put("questionNum", list.size());
				data.put("configList", configList);
	      obj=createSuccessAppResponse(data);
			} catch (Exception e) {
				obj=dealException(e);
			}
			return obj;
	}
	
	
	/**
	 * 进入风险评测
	 * @author QianPengZhan
	 * @date 2016年9月10日
	 * @return
	 */
	@RequestMapping(value = "/app/member/risk/userappRiskPapers")
	public Object userappRiskPapers(HttpServletRequest request,final Model model) {
		try{
			@SuppressWarnings("unused")
			User user = getAppSessionUser(request);
		final Papers papers = papersService.findPublishPapers();//找出发布了的试卷
		final List<Question> list = questionService.findQuestionAndAnswer(papers.getUuid());
		 model.addAttribute("papers", papers);
		 model.addAttribute("list", list);
		 model.addAttribute("questionNum", list.size());
		 model.addAttribute("mobileUrl", ConfigUtils.getValue("mobile_url"));
		 final List<LevelConfig> configList = levelConfigService.findByOrder();
		 model.addAttribute("configList", configList);
		 
		return "/member/risk/mobileUserRiskPapers";
		} catch (Exception e) {
			model.addAttribute("r_msg", e.getMessage());
			return ConfigUtils.getTppName()+"/retresult";
		}
	}
	
	/**
	 * @Title: doUserRiskPapers   
	 * @Description:用户评测逻辑处理
	 * @param: @return      
	 * @return: Object      
	 * @throws
	 */
	@RequestMapping(value = "/app/member/risk/doUserRiskPapers", method = RequestMethod.POST)
	@ResponseBody
	public Object doUserRiskPapers(final PapersModel papersModel,HttpServletRequest request){
		Object obj=null;
		Map<String, Object> data = new HashMap<String, Object>();
		try{
			User user = getAppSessionUser(request);
		papersModel.setPapersId(URLDecoder.decode(papersModel.getPapersId()));
		papersModel.setQuestionContent(URLDecoder.decode(papersModel.getQuestionContent()));
		final String[] questionContents = papersModel.getQuestionContent().split("concat");
		final Papers papers = papersService.get(papersModel.getPapersId());
		final Map<String,Object> map = papersService.doUserRiskPapers(papers,questionContents,user);
		final String levelName = String.valueOf(map.get("levelName"));
		
		UserCache userCache=userCacheService.findByUserId(user.getUuid());
		final LevelConfig levelConfig = levelConfigService.findListByUserRiskLevelVal(NumberUtils.toInt(userCache.getRiskLevel()));
		
		data.put("level",userCache.getRiskLevel());//数字等级
		data.put("levelDesc",levelConfig.getUserRiskLevelDesc());//数字等级说明
		data.put("levelName",levelName);//数字等级名称
	    obj=createSuccessAppResponse(data);
				} catch (Exception e) {
					obj=dealException(e);
				}
				return obj;
	}
}
