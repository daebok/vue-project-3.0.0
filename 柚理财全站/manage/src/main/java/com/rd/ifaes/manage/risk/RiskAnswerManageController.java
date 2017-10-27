/**
* 互联网金融交易系统ifaes (Internet Finance  Assets  Exchange  System)
 * Copyright (c) 2016 杭州融都科技股份有限公司 版权所有
 */
package com.rd.ifaes.manage.risk;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rd.ifaes.common.annotation.SystemLog;
import com.rd.ifaes.common.annotation.TokenValid;
import com.rd.ifaes.common.constant.TokenConstants;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.core.core.constant.SysLogConstant;
import com.rd.ifaes.core.risk.domain.Answer;
import com.rd.ifaes.core.risk.service.AnswerService;
import com.rd.ifaes.manage.sys.SystemController;

/**
 *  答案管理类
 * @version 3.0
 * @author QianPengZhan
 * @date 2016年7月26日
 */
@Controller
public class RiskAnswerManageController extends SystemController{
	
	/**
	 * 答案业务
	 */
	@Resource
	private transient AnswerService answerService;
	
	/**
	 * 答案管理界面
	 * @author QianPengZhan
	 * @date 2016年7月26日
	 * @return
	 */
	@RequestMapping(value="/risk/riskAnswerManage")
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.RISK_ANSWER)
	public String riskAnswerManage(){
		return "/risk/riskAnswerManage";
	}
	
	/**
	 * 答案管理界面列表
	 * @author QianPengZhan
	 * @date 2016年7月26日
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/risk/riskAnswerList")
	@ResponseBody
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.RISK_ANSWER)
	public Object riskAnswerList(final Answer model){
		if(StringUtils.isNotBlank(model.getKeywords())){
			model.setContent(model.getKeywords());
		}
		return answerService.findPage(model);
	}
	
	
	/**
	 * 进入答案的编辑界面
	 * @author QianPengZhan
	 * @date 2016年7月26日
	 * @param id
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/risk/riskAnswerEditPage")
	@SystemLog(method=SysLogConstant.EDIT,content=SysLogConstant.RISK_ANSWER)
	public String riskAnswerEditPage(final String id,final Model model) {
		if(StringUtils.isNotBlank(StringUtils.isNull(id))){
			 model.addAttribute("answer", answerService.get(id));
		}
		return "/risk/riskAnswerEditPage";
	}
	
	/**
	 * 编辑答案
	 * @author QianPengZhan
	 * @date 2016年7月26日
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/risk/riskAnswerEdit.html",method = RequestMethod.POST)
	@ResponseBody
	@TokenValid(value = TokenConstants.TOKEN_ANSWER_EDIT, dispatch = true)
	@SystemLog(method=SysLogConstant.EDIT,content=SysLogConstant.RISK_ANSWER)
	public Object riskAnswerEdit(final Answer model){
		answerService.update(model) ;
		return renderResult(true,MSG_SUCCESS);	 
	}
	
	
	/**
	 * 批量逻辑删除
	 * @author QianPengZhan
	 * @date 2016年7月26日
	 * @param ids
	 * @return
	 */
	@RequestMapping(value = "/risk/answerDelLogic")
	@ResponseBody
	@SystemLog(method=SysLogConstant.DEL,content=SysLogConstant.RISK_ANSWER)
	public Object delLogic(final String[] ids) {
		answerService.deleteBatch(ids);
		return renderResult(true,MSG_SUCCESS);
	}
}
