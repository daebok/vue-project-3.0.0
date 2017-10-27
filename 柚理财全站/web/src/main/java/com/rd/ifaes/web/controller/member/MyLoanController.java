package com.rd.ifaes.web.controller.member;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rd.ifaes.common.util.SessionUtils;
import com.rd.ifaes.core.project.domain.Project;
import com.rd.ifaes.core.project.domain.ProjectRepayment;
import com.rd.ifaes.core.project.service.ProjectRepaymentService;
import com.rd.ifaes.core.project.service.ProjectService;
import com.rd.ifaes.core.user.domain.User;
import com.rd.ifaes.web.controller.WebController;

/**
 * 
 *  我的借款-借款列表
 * @version 3.0
 * @author xhf
 * @date 2016年7月27日
 */
@Controller
public class MyLoanController extends WebController {

	@Resource
	private ProjectService projectService;
	@Resource
	private ProjectRepaymentService projectRepaymentService;
	
	/**
	 * 借款列表页面
	 * @author xhf
	 * @date 2016年7月27日
	 * @return
	 */
	@RequestMapping(value = "/member/myLoan/list")
	public String list(){
		return "/member/myLoan/list";
	}
	
	/**
	 * 获得借款列表记录
	 * @author xhf
	 * @date 2016年7月27日
	 * @return
	 */
	@RequestMapping(value = "/member/myLoan/getLogList")
	@ResponseBody
	public Object getLogList(Project model){
		User user = SessionUtils.getSessionUser();
		 model.setUserId(user.getUuid());
		Map<String,Object> data = new HashMap<>();
		data.put("result", true);
		data.put("data", projectService.findByDate(model));
		return data;
	}
	
	/**
	 * 还款计划页面
	 * @author xhf
	 * @date 2016年7月27日
	 * @return
	 */
	@RequestMapping(value = "/member/myLoan/projectRepayment")
	public String projectRepayment(){
		return "/member/myLoan/projectRepayment";
	}
	
	/**
	 * 获得还款计划列表记录
	 * @author xhf
	 * @date 2016年7月27日
	 * @return
	 */
	@RequestMapping(value = "/member/myLoan/getProjectRepaymentList")
	@ResponseBody
	public Object getProjectRepaymentList(ProjectRepayment model){
		Map<String,Object> data =new HashMap<>();
		data.put("result", true);
		data.put("data", projectRepaymentService.findByProjectId(model));
		data.put("projectName", projectService.get(model.getProjectId()).getProjectName());
		return data;
	}
	
}
