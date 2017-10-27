package com.rd.ifaes.manage.loan;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rd.ifaes.common.annotation.SystemLog;
import com.rd.ifaes.common.util.excel.ExportModel;
import com.rd.ifaes.common.util.excel.ExportUtil;
import com.rd.ifaes.core.core.constant.Constant;
import com.rd.ifaes.core.core.constant.SysLogConstant;
import com.rd.ifaes.core.project.domain.ProjectInvest;
import com.rd.ifaes.core.project.service.ProjectInvestService;
import com.rd.ifaes.manage.sys.SystemController;

/**
 * 投资管理
 * @author lh
 * @version 3.0
 * @since 2016-7-29
 *
 */
@Controller
public class InvestManageController extends SystemController {
	
	private static final String SORT_NAME = "pi.create_time desc, pi.uuid";
	@Autowired
	private ProjectInvestService projectInvestService;
	
	/****************************** 投资记录 ******************************/
	
	/**
	 * 投资记录页面
	 * @return
	 */
	@RequestMapping(value = "/loan/invest/investRecord")
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.INVEST_RECORD)
	public String record(Model model,String borrowFlag){
		model.addAttribute("borrowFlag", borrowFlag);		
		return "/loan/invest/investRecord";
	}
	
	
	/**
	 * 投资记录页面(借贷记录、产品记录子页面)
	 * @param model
	 * @param projectId
	 * @return
	 */
	@RequestMapping(value = "/loan/invest/investList")
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.INVEST_RECORD)
	public String list(Model model, String projectId){
		model.addAttribute("projectId", projectId);
		return "/loan/invest/investList";
	}

	/**
	 * 投资列表
	 * @return
	 */
	@RequestMapping(value = "/loan/invest/investRecordList")
	@ResponseBody
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.INVEST_RECORD)
	public Object recordList(ProjectInvest model){
		model.getPage().setSort(SORT_NAME);
		model.getPage().setOrder(Constant.ASC);
		return projectInvestService.findRecordPage(model);
	}
	
	/**
	 * export 投资列表
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping(value = "/loan/invest/exportInvestRecord")
	@SystemLog(method=SysLogConstant.EXPORT,content=SysLogConstant.INVEST_RECORD)
	public void exportInvestRecord(final ProjectInvest model,final HttpServletRequest request,
			               final HttpServletResponse response) throws IOException{
		ExportUtil.exportExcel(new ExportModel<ProjectInvest>() {
			@Override
			public String getCacheKey() {
				return  InvestManageController.this.getUserId();
			}
			@Override
			public ProjectInvest getModel() {
				return model;
			}
			@Override
			public int getTotal(ProjectInvest entity) {
				return projectInvestService.getCount(entity);
			}
			@Override
			public List<ProjectInvest> getPageRecords(ProjectInvest entity) {
				return projectInvestService.findPage(entity).getRows();
			}
		}, request, response);
	 
	}

}
