/**
* 互联网金融交易系统ifaes (Internet Finance  Assets  Exchange  System)
 * Copyright (c) 2016 杭州融都科技股份有限公司 版权所有
 */
package com.rd.ifaes.manage.risk;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rd.ifaes.common.annotation.SystemLog;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.core.core.constant.SysLogConstant;
import com.rd.ifaes.core.risk.domain.LevelConfig;
import com.rd.ifaes.core.risk.domain.Papers;
import com.rd.ifaes.core.risk.domain.Question;
import com.rd.ifaes.core.risk.domain.RiskUserLog;
import com.rd.ifaes.core.risk.service.LevelConfigService;
import com.rd.ifaes.core.risk.service.PapersService;
import com.rd.ifaes.core.risk.service.RiskUserLogService;
import com.rd.ifaes.core.user.domain.User;
import com.rd.ifaes.core.user.service.UserService;
import com.rd.ifaes.manage.sys.SystemController;

/**
 * 用户评测记录类
 * @version 3.0
 * @author QianPengZhan
 * @date 2016年7月26日
 */
@Controller
public class RiskUserLogManageController extends SystemController{
	
	/**
	 * 用户评测记录类
	 */
	@Resource
	private transient RiskUserLogService riskUserLogService;
	
	/**
	 * 用户业务类
	 */
	@Resource
	private transient UserService userService;
	
	/**
	 *试卷业务 
	 */
	@Resource
	private transient PapersService papersService;
	
	/**
	 * LevelConfigServic业务
	 */
	@Resource
	private transient LevelConfigService levelConfigService;
	
	/**
	 *  评测管理界面
	 * @author QianPengZhan
	 * @date 2016年7月26日
	 * @return
	 */
	@RequestMapping(value="/risk/riskUserLogManage")
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.RISK_USER_LOG)
	public String riskUserLogManage(final Model model) {
		final List<LevelConfig> configList = levelConfigService.findByOrder();
		 model.addAttribute("configList", configList);
		return "/risk/riskUserLogManage";
	}
	
	
	/**
	 *  用户评测记录列表
	 * @author QianPengZhan
	 * @date 2016年7月26日
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/risk/userLogList")
	@ResponseBody
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.RISK_USER_LOG)
	public Object userLogList(final RiskUserLog model){
		return riskUserLogService.findPage(model);
	}
	
	/**
	 * 查看答题内容
	 * @author QianPengZhan
	 * @date 2016年9月12日
	 * @param id
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/risk/riskUserLogLookPage")
	@SystemLog(method=SysLogConstant.VIEW,content=SysLogConstant.RISK_USER_LOG)
	public String questionSearch(final String id,final Model model) {
		if(StringUtils.isNotBlank(StringUtils.isNull(id))){
			final RiskUserLog userLog = riskUserLogService.get(id);
			final User user = userService.get(userLog.getUserId());
			userLog.setUserName(user.getUserName());
			 model.addAttribute("userLog", userLog);
			//答题试卷
			final Papers papers = papersService.get(userLog.getPapersId());
			 model.addAttribute("papers", papers);
			//获取问题和答案
			final List<Question> list = riskUserLogService.getQuestionAndAnswers(userLog);
			 model.addAttribute("qList", list);
			
		}
		return "/risk/riskUserLogLookPage";
	}
}
