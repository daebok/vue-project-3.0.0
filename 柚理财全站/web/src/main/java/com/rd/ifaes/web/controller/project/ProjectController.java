/**
* 互联网金融交易系统ifaes (Internet Finance  Assets  Exchange  System)
 * Copyright (c) 2016 杭州融都科技股份有限公司 版权所有
 */
package com.rd.ifaes.web.controller.project;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rd.ifaes.common.dict.LoanEnum;
import com.rd.ifaes.common.exception.BussinessException;
import com.rd.ifaes.common.util.SessionUtils;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.common.util.resource.LoanResource;
import com.rd.ifaes.core.base.web.BaseController;
import com.rd.ifaes.core.core.constant.Constant;
import com.rd.ifaes.core.core.constant.ProjectConstant;
import com.rd.ifaes.core.core.util.ResourceUtils;
import com.rd.ifaes.core.project.domain.Project;
import com.rd.ifaes.core.project.domain.ProjectRepayment;
import com.rd.ifaes.core.project.exception.ProjectException;
import com.rd.ifaes.core.project.service.BorrowUploadService;
import com.rd.ifaes.core.project.service.ProjectRepaymentService;
import com.rd.ifaes.core.project.service.ProjectService;
import com.rd.ifaes.core.project.service.ProjectTypeService;
import com.rd.ifaes.core.user.domain.User;

/**
 * 项目相关信息（详情、借款资料等）
 * 
 * @author FangJun
 * @date 2016年7月25日
 */
@Controller
public class ProjectController extends BaseController {
	@Resource
	private ProjectService projectService;
	@Resource
	private BorrowUploadService borrowUploadService;
	@Resource
	private ProjectRepaymentService projectRepaymentService;
	@Resource
	private transient ProjectTypeService projectTypeService;

	/**
	 * 项目详情(JSON）
	 * 
	 * @param projectId 项目UUID
	 * @return
	 */
	@RequestMapping(value = "/project/detail")
	@ResponseBody
	public Object detail(String projectId) {
		if (StringUtils.isNotBlank(projectId)) {
			final Map<String, Object> data = renderSuccessResult();
			final User user = (User) SessionUtils.getSessionAttr(Constant.SESSION_USER);
			if (user != null) {
				 data.putAll(projectService.getDetail(projectId,user.getUuid()));
			}else{
				 data.putAll(projectService.getDetail(projectId,null));
			}
			final Project project = projectService.get(projectId);
			if(project!=null){
				data.put("protocolId",project.getProtocolId());
			}
			return data;
		}
		throw new BussinessException(ResourceUtils.get(LoanResource.PROJECT_ID_IS_NULL), BussinessException.TYPE_JSON);
	}

	/**
	 * 项目详情、借款详情
	 * 
	 * @param projectId 项目UUID
	 * @return
	 */
	@RequestMapping(value = "/project/content")
	@ResponseBody
	public Object content(String projectId) {
		if (StringUtils.isNotBlank(projectId)) {
			final Map<String, Object> data = renderSuccessResult();
			data.putAll(projectService.getContent(projectId));
			return data;
		}
		throw new BussinessException(ResourceUtils.get(LoanResource.PROJECT_ID_IS_NULL), BussinessException.TYPE_JSON);
	}

	/**
	 * 借款人信息（基本信息、借款资质、担保机构）
	 * 
	 * @param projectId 项目UUID
	 * @return
	 */
	@RequestMapping(value = "/project/borrower")
	@ResponseBody
	public Object borrower(String projectId) {
		if (StringUtils.isNotBlank(projectId)) {
			final Map<String, Object> data = renderSuccessResult();
			data.putAll(projectService.getBorrowerInfo(projectId));
			return data;
		}
		throw new BussinessException(ResourceUtils.get(LoanResource.PROJECT_ID_IS_NULL), BussinessException.TYPE_JSON);
	}

	/**
	 * 借款资料
	 * 
	 * @param projectId 借款UUID
	 * @return
	 */
	@RequestMapping(value = "/project/borrowPic")
	@ResponseBody
	public Object borrowPic(String projectId) {
		if (StringUtils.isNotBlank(projectId)) {
			final Map<String, Object> data = renderSuccessResult();
			data.put("list", borrowUploadService.findBorrowPic(projectId));
			return data;
		}
		throw new BussinessException(ResourceUtils.get(LoanResource.PROJECT_ID_IS_NULL), BussinessException.TYPE_JSON);
	}
	 /**
	  * 指定项目的还款计划
	  * @author  FangJun
	  * @date 2016年8月29日
	  * @param projectId 项目UUID
	  * @return
	  */
	@RequestMapping(value = "/project/repaymentList")
	@ResponseBody
	public Object getProjectRepaymentList(ProjectRepayment model){
		Map<String,Object> data =new HashMap<>();
		data.put("result", true);
		data.put(RESULT_PAGE, projectRepaymentService.findByProjectId(model));
		return data;
	}
	
	
	/**
	 * 项目剩余可投金额
	 * @author  FangJun
	 * @param projectId 项目ID
	 * @return 含项目剩余可投金额Map
	 */
	@RequestMapping(value = "/project/remainAccount")
	@ResponseBody
	public Object remainAccount(String projectId) {
		if (StringUtils.isNotBlank(projectId)) {
			final Map<String, Object> data = renderSuccessResult();
			data.putAll(projectService.getRemainAccount(projectId));
			return data;
		}
		throw new BussinessException(ResourceUtils.get(LoanResource.PROJECT_ID_IS_NULL), BussinessException.TYPE_JSON);
	}
 
	/**
	 * 检查输入的定向密码正确性
	 * @author  FangJun
	 * @param projectId 项目UUID
	 * @param pwd 输入的定向密码
	 * @return 校验结果
	 */
	@RequestMapping(value = "/project/checkPwd")
	@ResponseBody
	public Object checkPwd(String projectId,String pwd,final HttpServletRequest request) {
		if (StringUtils.isBlank(pwd)) {
			throw new BussinessException(ResourceUtils.get(LoanResource.SALE_PASS_ERROR), BussinessException.TYPE_JSON);
		}
		if (StringUtils.isNotBlank(projectId)) {
			final Map<String, Object> data = renderSuccessResult();
			final Project project=projectService.get(projectId);
			if (project == null) {
				throw new BussinessException(ResourceUtils.get(LoanResource.PROJECT_NOT_EXISTS), BussinessException.TYPE_JSON);
			}
			if (LoanEnum.SPECIFIC_SALE_PASSWORD.eq(project.getSpecificSale())) {
				//校验定向密码
				  if(!project.getSpecificSaleRule().equals(pwd) ){
					 request.getSession().removeAttribute(String.format(ProjectConstant.PROJECT_PASS_RIGHT_PROJECT_ID, projectId));
					 throw new ProjectException(ResourceUtils.get(LoanResource.SALE_PASS_ERROR), BussinessException.TYPE_JSON);
				}
				
				request.getSession().setAttribute(String.format(ProjectConstant.PROJECT_PASS_RIGHT_PROJECT_ID, projectId), true);
			}
			return data;
		}
		throw new BussinessException(ResourceUtils.get(LoanResource.PROJECT_ID_IS_NULL), BussinessException.TYPE_JSON);
	}
}
