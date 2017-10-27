package com.rd.ifaes.manage.loan;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rd.ifaes.common.annotation.SystemLog;
import com.rd.ifaes.common.dict.LoanEnum;
import com.rd.ifaes.common.dict.RepaymentEnum;
import com.rd.ifaes.common.exception.BussinessException;
import com.rd.ifaes.common.orm.Page;
import com.rd.ifaes.common.util.DateUtils;
import com.rd.ifaes.common.util.PrincipalUtils;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.common.util.excel.ExportModel;
import com.rd.ifaes.common.util.excel.ExportUtil;
import com.rd.ifaes.core.core.constant.Constant;
import com.rd.ifaes.core.core.constant.ResourceConstant;
import com.rd.ifaes.core.core.constant.SysLogConstant;
import com.rd.ifaes.core.core.util.CacheUtils;
import com.rd.ifaes.core.core.util.ResourceUtils;
import com.rd.ifaes.core.project.domain.Project;
import com.rd.ifaes.core.project.domain.ProjectRepayment;
import com.rd.ifaes.core.project.domain.UrgeRepayLog;
import com.rd.ifaes.core.project.service.ProjectRepaymentService;
import com.rd.ifaes.core.project.service.ProjectService;
import com.rd.ifaes.core.project.service.UrgeRepayLogService;
import com.rd.ifaes.core.sys.domain.Operator;
import com.rd.ifaes.core.user.service.UserService;
import com.rd.ifaes.manage.sys.SystemController;
/**
 * 还款管理
 * @author lh
 * @version 3.0
 * @since 2016-7-29
 *
 */
@Controller
public class RepaymentManageController extends SystemController {
	
	/**
	 * 还款列表排序方式
	 */
	private static final String SORT_ORDER = "pr.repay_time"; 
	private static final String SORT_ORDER_REPAY = "pr.repay_time,pr.uuid"; 
	@Resource
	private ProjectRepaymentService projectRepaymentService;
	@Resource
	private UrgeRepayLogService urgeRepayLogService;
	@Resource
	private ProjectService projectService;
	@Resource
	private UserService userService;
	
/****************************** 还款记录 ******************************/
	
	/**
	 * 还款记录页面
	 * @return
	 */
	@RequestMapping(value = "/loan/repayment/repaymentRecord")
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.REPAYMENT_RECORD)
	public String record(){	
		return "/loan/repayment/repaymentRecord";
	}

	/**
	 * 还款列表
	 * @return
	 */
	@RequestMapping(value = "/loan/repayment/repaymentRecordList")
	@ResponseBody
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.REPAYMENT_RECORD)
	public Object recordList(ProjectRepayment model){		
		model.getPage().setSort(SORT_ORDER_REPAY);
		model.getPage().setOrder(Constant.ASC);
		return projectRepaymentService.findDetail(model);
	}
	
	/**
	 * 根据不同类新获取还款记录总计
	 * @Author hhtao
	 * @date 2017/3/2 13:48
	 * @param
	 */
	@RequestMapping(value = "/loan/repayment/getPaymentSum")
	@ResponseBody
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.REPAYMENT_RECORD)
	public Object getPaymentSum(){
        String sumPayment=CacheUtils.getStr("repaymentMoneySum");
		return sumPayment;
	}
	
	/**
	 * 借款頁面彈出還款頁面
	 * @return
	 */
	@RequestMapping(value = "/loan/repayment/repaymentList")
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.INVEST_RECORD)
	public String list(Model model, String projectId){
		model.addAttribute("projectId", projectId);
		return "/loan/repayment/repaymentList";
	}
	
	/**
	 * 
	 * 逾期管理页面
	 * @author wyw
	 * @date 2016-8-18
	 * @return
	 */
	@RequestMapping(value = "/loan/repayment/repaymentOverdue")
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.REPAYMENT_OVERDUE)
	public String repaymentOverdue(){		
		return "/loan/repayment/repaymentOverdue";
	}
	/**
	 * 
	 * 逾期还款数据
	 * @author wyw
	 * @date 2016-8-18
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/loan/repayment/overdueRecordList")
	@ResponseBody
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.REPAYMENT_OVERDUE)
	public Object overdueRecordList(ProjectRepayment model){
		//逾期 
		model.getPage().setSort(SORT_ORDER);
		model.getPage().setOrder(Constant.ASC);
		model.setIsOverdue(String.valueOf(Constant.INT_ONE));
		//未还
		model.setStatus(RepaymentEnum.STATUS_NOT_REPAY.getValue());
		return projectRepaymentService.findPage(model);
	}
	
	/**
	 * 导出逾期还款记录
	 * @param model
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value = "/loan/repayment/exportOverdueRecordList")
	@RequiresPermissions("project:borrow:overdue:export")
	@SystemLog(method=SysLogConstant.EXPORT,content=SysLogConstant.REPAYMENT_OVERDUE)
	public void exportBorrowRecord(ProjectRepayment model,HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		//逾期 
		model.setIsOverdue(String.valueOf(Constant.INT_ONE));
		//未还
		model.setStatus(RepaymentEnum.STATUS_NOT_REPAY.getValue());
		
		exportExcel(model, request, response);
	}
	
	/**
	 * 
	 * 催收页面
	 * @author wyw
	 * @date 2016-8-18
	 * @return
	 */
	@RequestMapping(value = "/loan/repayment/repayUrge")
	@SystemLog(method=SysLogConstant.VIEW,content=SysLogConstant.REPAY_URGE)
	public String repayUrge(String id, final Model model){
		if (StringUtils.isBlank(id)) {
			throw new BussinessException(ResourceUtils.get(ResourceConstant.GLOBAL_NO_RECORD));
		}
		// 借款标信息
		ProjectRepayment repayment = projectRepaymentService.get(id);
		 model.addAttribute("repayment", repayment);
		return "/loan/repayment/repayUrge";
	}
	/**
	 * 
	 * 催收操作
	 * @author wyw
	 * @date 2016-8-18
	 * @param urgeRepayLog
	 * @return
	 */
	@RequestMapping(value = "/loan/repayment/doRepayUrge", method = RequestMethod.POST)
	@ResponseBody
	@SystemLog(method=SysLogConstant.AUDIT,content=SysLogConstant.REPAY_URGE)
	public Map<String, Object> doRepayUrge(UrgeRepayLog urgeRepayLog) {
		Operator operator = (Operator)PrincipalUtils.getPrincipal();
		urgeRepayLog.setOperatorId(operator.getUuid());
		urgeRepayLog.setCreateTime(DateUtils.getNow());
		urgeRepayLogService.save(urgeRepayLog);
		return renderSuccessResult();
	}
	/**
	 * 
	 * 催收纪录页面
	 * @author wyw
	 * @date 2016-9-13
	 * @return
	 */
	@RequestMapping(value = "/loan/repayment/repaymentUrgeLog")
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.REPAY_URGE)
	public String repaymentUrgeLog(String repaymentId,final Model model){	
		 model.addAttribute("repaymentId", repaymentId);
		return "/loan/repayment/repaymentUrgeLog";
	}
	
	/**
	 * 催收纪录 数据
	 * @author wyw
	 * @date 2016-9-13
	 * @param urgeRepayLog
	 * @return
	 */
	@RequestMapping(value = "/loan/repayment/repaymentUrgeLogList", method = RequestMethod.POST)
	@ResponseBody
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.REPAY_URGE)
	public Object repaymentUrgeLogList(UrgeRepayLog urgeRepayLog) {
		return urgeRepayLogService.findPage(urgeRepayLog);
	}
	
	/**
	 * 短信催收还款
	 * @param mobilePhone 电话号码
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/loan/repayment/urgeRpaymentPhoneCode")
	@ResponseBody
	public Object repaymentPhoneCode(final String id) {
		projectRepaymentService.repaymentPhoneCode(id);
		return renderResult(true, "发送成功");
	}
	
	/**
	 * 
	 * 坏账处理
	 * @author wyw
	 * @date 2016-9-13
	 * @param projectId
	 * @return
	 */
	@RequestMapping(value = "/loan/repayment/doRepayBadDebt", method = RequestMethod.POST)
	@ResponseBody
	@SystemLog(method=SysLogConstant.AUDIT,content=SysLogConstant.REPAY_BAD_DEBT)
	public Map<String, Object> doRepayBadDebt(String id) {
		Project project= projectService.get(id);
		if(project!=null){
			projectService.updateStatus(id, LoanEnum.STATUS_BAD_DEBT.getValue(), project.getStatus());
		}
		return renderSuccessResult();
	}
	/**
	 * 
	 * 垫付记录
	 * @author wyw
	 * @date 2016-8-23
	 * @return
	 */
	@RequestMapping(value = "/loan/repayment/repaymentAdvance")
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.REPAYMENT_ADVANCE)
	public String repaymentAdvance(){		
		return "/loan/repayment/repaymentAdvance";
	}
	/**
	 * 
	 * 垫付列表数据
	 * @author wyw
	 * @date 2016-8-24
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/loan/repayment/repaymentAdvanceList")
	@ResponseBody
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.REPAYMENT_ADVANCE)
	public Object repaymentAdvanceList(ProjectRepayment model){
		model.getPage().setSort("real_repay_time");
		model.getPage().setOrder(Constant.DESC);
		Page<ProjectRepayment> advancePage = projectRepaymentService.findAdvance(model);
		return advancePage;
	}
	
	/**
	 * 导出还款列表
	 * @param model
	 * @throws IOException 
	 */
	@RequestMapping(value = "/loan/repayment/exportRepaymentRecord")
	@SystemLog(method=SysLogConstant.EXPORT,content=SysLogConstant.REPAYMENT_RECORD)
	public void exportRepaymentRecord(ProjectRepayment model,final HttpServletRequest request,
			final HttpServletResponse response) throws IOException{
		exportExcel(model, request, response);
	}
	
	/**
	 * 导出垫付记录列表
	 * @param model
	 * @throws IOException 
	 */
	@RequestMapping(value = "/loan/repayment/exportRepaymentAdvance")
	@SystemLog(method=SysLogConstant.EXPORT,content=SysLogConstant.REPAYMENT_ADVANCE)
	public void exportRepaymentAdvance(final ProjectRepayment model,final HttpServletRequest request,
			final HttpServletResponse response) throws IOException{
 
		ExportUtil.exportExcel(new ExportModel<ProjectRepayment>() {
			@Override
			public String getCacheKey() {
				return  RepaymentManageController.this.getUserId();
			}
			@Override
			public ProjectRepayment getModel() {
				return model;
			}
			@Override
			public int getTotal(ProjectRepayment entity) {
				return projectRepaymentService.getCount(entity);
			}
			@Override
			public List<ProjectRepayment> getPageRecords(ProjectRepayment entity) {
				model.getPage().setSort("real_repay_time");
				model.getPage().setOrder(Constant.DESC);
				List<ProjectRepayment> list = projectRepaymentService.findAdvance(model).getRows();
				return list;
			}
		}, request, response);
	}
	
	/**
	 * 导出excel
	 * @param model
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	private void exportExcel(final ProjectRepayment model, final HttpServletRequest request, final HttpServletResponse response)throws IOException{
		
		ExportUtil.exportExcel(new ExportModel<ProjectRepayment>() {
			@Override
			public String getCacheKey() {
				return  RepaymentManageController.this.getUserId();
			}
			@Override
			public ProjectRepayment getModel() {
				return model;
			}
			@Override
			public int getTotal(ProjectRepayment entity) {
				return projectRepaymentService.getCount(entity);
			}
			@Override
			public List<ProjectRepayment> getPageRecords(ProjectRepayment entity) {
				model.getPage().setSort(SORT_ORDER);
				model.getPage().setOrder(Constant.DESC);
				List<ProjectRepayment> list = projectRepaymentService.findPage(model).getRows();
				return list;
			}
		}, request, response);
	}
	
	/**
	 * 定时发送3天内的还款信息
	 */
	public void repaymentTimeoutHandle(ProjectRepayment model) { 
		List<ProjectRepayment> list = projectRepaymentService.findPage(model).getRows() ;
		for (ProjectRepayment projectRepayment : list) {
			LOGGER.info("待处理:{}", projectRepayment);
			Date repayDate = projectRepayment.getRepayTime() ;
			Calendar c1 = Calendar.getInstance() ;
			Calendar c2 = Calendar.getInstance() ;
			c1.setTime(repayDate);
			Date nowDate = DateUtils.getToday() ;
			c2.setTime(nowDate);
			LOGGER.info("待处理:{}", nowDate);
			int days = c1.get(Calendar.DAY_OF_YEAR) - c2.get(Calendar.DAY_OF_YEAR);
			LOGGER.info("待处理:{}",days);
			//三天内还款信息
			if(days <=2 && days >= 0) {
				Object obj = repaymentPhoneCode(projectRepayment.getUuid()) ;
			}
		}
	}
	
}
