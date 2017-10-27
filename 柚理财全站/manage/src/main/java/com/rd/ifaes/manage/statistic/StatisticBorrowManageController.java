/**
* 互联网金融交易系统ifaes (Internet Finance  Assets  Exchange  System)
 * Copyright (c) 2016 杭州融都科技股份有限公司 版权所有
 */
package com.rd.ifaes.manage.statistic;

import java.io.IOException;
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
import org.springframework.web.bind.annotation.ResponseBody;

import com.rd.ifaes.common.annotation.SystemLog;
import com.rd.ifaes.common.dict.RepaymentEnum;
import com.rd.ifaes.common.util.DateUtils;
import com.rd.ifaes.core.core.constant.SysLogConstant;
import com.rd.ifaes.core.statistic.constant.StatisticConstant;
import com.rd.ifaes.core.statistic.model.StatisticModel;
import com.rd.ifaes.core.statistic.service.StatisticBorrowService;
import com.rd.ifaes.core.statistic.service.StatisticOverdueService;
import com.rd.ifaes.core.statistic.service.StatisticRepayInfoService;
import com.rd.ifaes.core.statistic.service.StatisticRepayService;
import com.rd.ifaes.core.statistic.service.StatisticService;
import com.rd.ifaes.core.statistic.service.StatisticUserService;
import com.rd.ifaes.manage.sys.SystemController;


/**
 * 借款分析
 * @version 3.0
 * @date 2017年2月21日
 */
@Controller
public class StatisticBorrowManageController extends SystemController{
	
	@Resource
	private StatisticBorrowService statisticBorrowService;
	@Resource
	private StatisticRepayService statisticRepayService;
	@Resource
	private StatisticOverdueService statisticOverdueService;
	@Resource
	private StatisticUserService statisticUserService;
	@Resource
	private StatisticService statisticService;
	@Resource
	private StatisticRepayInfoService statisticRepayInfoService;
	
	/**
	 * 日期初始化
	 * @author fxl
	 * @date 2017年3月1日
	 * @param model
	 */
	private void initDate(Model model){
		Date endDate = DateUtils.rollDay(DateUtils.getToday(),-1);
		Date startDate = DateUtils.rollDay(endDate, -6);
		model.addAttribute("startDate",DateUtils.formatDate(startDate));
		model.addAttribute("endDate",DateUtils.formatDate(endDate));
	}
	/**
	 * 借款人数页面
	 * @author fxl
	 * @date 2017年2月21日
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/statistic/borrow/borrowerNumManage")
	@RequiresPermissions("statistic:borrow:borrowerNum:query")
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.STATISTIC_BORROWER_NUM)
	public String borrowerNumManage(Model model,HttpServletRequest request){
		initDate(model);
		return "/statistic/borrow/borrowerNumManage";
	}
	
	/**
	 * 借款人数接口
	 * @author fxl
	 * @date 2017年2月21日
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/statistic/borrow/getBorrowerNum")
	@RequiresPermissions("statistic:borrow:borrowerNum:query")
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.STATISTIC_BORROWER_NUM)
	@ResponseBody
	public Object getBorrowerNum(StatisticModel model) {
		model.setStatType(StatisticConstant.STAT_TYPE_CUSTOMERS);
		return statisticBorrowService.getBorrowStatistic(model);
	}
	
	/**
	 * 借款金额页面
	 * @author fxl
	 * @date 2017年2月21日
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/statistic/borrow/borrowAccountManage")
	@RequiresPermissions("statistic:borrow:borrowAccount:query")
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.STATISTIC_BORROW_ACCOUNT)
	public String borrowAccountManage(Model model){
		initDate(model);
		return "/statistic/borrow/borrowAccountManage";
	}
	
	/**
	 * 借款金额接口
	 * @author fxl
	 * @date 2017年2月21日
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/statistic/borrow/getBorrowAccount")
	@RequiresPermissions("statistic:borrow:borrowAccount:query")
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.STATISTIC_BORROW_ACCOUNT)
	@ResponseBody
	public Object getBorrowAccount(StatisticModel model) {
		model.setStatType(StatisticConstant.STAT_TYPE_AMOUNT);
		return statisticBorrowService.getBorrowStatistic(model);
	}
	
	/**
	 * 借款笔数页面
	 * @author fxl
	 * @date 2017年2月21日
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/statistic/borrow/borrowNumManage")
	@RequiresPermissions("statistic:borrow:borrowNum:query")
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.STATISTIC_BORROW_NUM)
	public String borrowNumManage(Model model){
		initDate(model);
		return "/statistic/borrow/borrowNumManage";
	}
	
	/**
	 * 借款笔数接口
	 * @author fxl
	 * @date 2017年2月21日
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/statistic/borrow/getBorrowNum")
	@RequiresPermissions("statistic:borrow:borrowNum:query")
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.STATISTIC_BORROW_NUM)
	@ResponseBody
	public Object getBorrowNum(StatisticModel model) {
		model.setStatType(StatisticConstant.STAT_TYPE_NUMBERS);
		return statisticBorrowService.getBorrowStatistic(model);
	}
	
	/**
	 * 对比时段
	 * @author fxl
	 * @date 2017年3月15日
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/statistic/borrow/compareData")
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.STATISTIC_COMPARE_DATA)
	@ResponseBody
	public Object compareData(StatisticModel model){
		return statisticBorrowService.compareDataByDate(model);
	}

	/**
	 * 借款列表接口
	 * @author fxl
	 * @date 2017年3月15日
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/statistic/borrow/getBorrowList")
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.STATISTIC_BORROW_LIST)
	@ResponseBody
	public Object getBorrowList(StatisticModel model){
		return statisticBorrowService.getBorrowList(model);
	}
	
	/**
	 * 导出借款人数明细
	 */
	@RequestMapping(value = "/statistic/borrow/exportBorrowerNumExcel")
	@RequiresPermissions("statistic:borrow:borrowerNum:export")
	@SystemLog(method=SysLogConstant.EXPORT,content=SysLogConstant.STATISTIC_BORROW_LIST)
	public void exportBorrowerNumExcel(final StatisticModel model,final HttpServletRequest request,final HttpServletResponse response) throws IOException {
		model.setExportTitle("借款人数明细");
		exportBorrowExcel(model, request, response);
	}
	
	/**
	 * 导出借款金额明细
	 */
	@RequestMapping(value = "/statistic/borrow/exportBorrowAccountExcel")
	@RequiresPermissions("statistic:borrow:borrowAccount:export")
	@SystemLog(method=SysLogConstant.EXPORT,content=SysLogConstant.STATISTIC_BORROW_LIST)
	public void exportBorrowAccountExcel(final StatisticModel model,final HttpServletRequest request,final HttpServletResponse response) throws IOException {
		model.setExportTitle("借款金额明细");
		exportBorrowExcel(model, request, response);
	}
	
	/**
	 * 导出借款笔数明细
	 */
	@RequestMapping(value = "/statistic/borrow/exportBorrowNumExcel")
	@RequiresPermissions("statistic:borrow:borrowNum:export")
	@SystemLog(method=SysLogConstant.EXPORT,content=SysLogConstant.STATISTIC_BORROW_LIST)
	public void exportBorrowNumExcel(final StatisticModel model,final HttpServletRequest request,final HttpServletResponse response) throws IOException {
		model.setExportTitle("借款笔数明细");
		//设置列名和字段
		exportBorrowExcel(model, request, response);
	}
	
	/**
	 * 导出借款
	 */
	@SuppressWarnings("unchecked")
	private void exportBorrowExcel(final StatisticModel model,final HttpServletRequest request,final HttpServletResponse response) throws IOException {
		//设置列名和字段
		String headers = StatisticConstant.BORROW_EXCEL_HEADER;
		String fields = StatisticConstant.BORROW_EXCEL_FIELDS;
		model.setHearders(headers);
		model.setFields(fields);
		final Map<String, Object> map = statisticBorrowService.getBorrowList(model);
		final List<Object> dataList = (List<Object>) map.get("rows");
		statisticService.exportExcel(model, dataList, request, response);
	}
	
	/**
	 * 借款地区页面
	 * @author fxl
	 * @date 2017年2月21日
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/statistic/borrow/borrowAreaManage")
	@RequiresPermissions("statistic:borrow:borrowArea:query")
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.STATISTIC_BORROW_AREA)
	public String borrowAreaManage(Model model){
		initDate(model);
		return "/statistic/borrow/borrowAreaManage";
	}
	
	/**
	 * 借款地区接口
	 * @author fxl
	 * @date 2017年2月21日
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/statistic/borrow/getBorrowArea")
	@RequiresPermissions("statistic:borrow:borrowArea:query")
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.STATISTIC_BORROW_AREA)
	@ResponseBody
	public Object getBorrowArea(StatisticModel model) {
		return statisticBorrowService.getBorrowArea(model);
	}
	
	/**
	 * 借款地区列表接口
	 * @author fxl
	 * @date 2017年3月15日
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/statistic/borrow/getBorrowAreaList")
	@RequiresPermissions("statistic:borrow:borrowArea:query")
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.STATISTIC_BORROW_AREA)
	@ResponseBody
	public Object getBorrowAreaList(StatisticModel model){
		return statisticBorrowService.getBorrowAreaList(model);
	}
	
	/**
	 * 导出借款笔数明细
	 */
	@RequestMapping(value = "/statistic/borrow/exportBorrowAreaExcel")
	@RequiresPermissions("statistic:borrow:borrowArea:export")
	@SystemLog(method=SysLogConstant.EXPORT,content=SysLogConstant.STATISTIC_BORROW_AREA)
	public void exportBorrowAreaExcel(final StatisticModel model,final HttpServletRequest request,final HttpServletResponse response) throws IOException {
		model.setExportTitle("地区明细");
		//设置列名和字段
		String headers = StatisticConstant.AREA_EXCEL_HEADER;
		String fields = StatisticConstant.AREA_EXCEL_FIELDS;
		model.setHearders(headers);
		model.setFields(fields);
		final Map<String, Object> map = statisticBorrowService.getBorrowAreaList(model);
		final List<Object> dataList = (List<Object>) map.get("rows");
		statisticService.exportExcel(model, dataList, request, response);
	}
	
	/**
	 * 借款情况页面
	 * @author fxl
	 * @date 2017年2月21日
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/statistic/borrow/borrowInfoManage")
	@RequiresPermissions("statistic:borrow:borrowInfo:query")
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.STATISTIC_BORROW_INFO)
	public String borrowInfoManage(Model model){
		initDate(model);
		return "/statistic/borrow/borrowInfoManage";
	}
	
	/**
	 * 借款情况接口
	 * @author fxl
	 * @date 2017年2月21日
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/statistic/borrow/getBorrowInfo")
	@RequiresPermissions("statistic:borrow:borrowInfo:query")
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.STATISTIC_BORROW_INFO)
	@ResponseBody
	public Object getBorrowInfo(StatisticModel model) {
		return statisticBorrowService.getBorrowInfo(model);
	}
	
	/**
	 * 待还分析页面
	 * @author fxl
	 * @date 2017年3月01日
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/statistic/borrow/borrowNotRepayManage")
	@RequiresPermissions("statistic:borrow:collection:query")
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.STATISTIC_BORROW_NOTREPAY)
	public String borrowNotRepayManage(Model model){
		initDate(model);
		return "/statistic/borrow/borrowNotRepayManage";
	}
	
	/**
	 * 待还分析接口
	 * @author fxl
	 * @date 2017年3月01日
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/statistic/borrow/getBorrowNotRepay")
	@RequiresPermissions("statistic:borrow:collection:query")
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.STATISTIC_BORROW_NOTREPAY)
	@ResponseBody
	public Object getBorrowNotRepay(StatisticModel model) {
		model.setRepayStatus(RepaymentEnum.STATUS_NOT_REPAY.getValue());
		return statisticRepayService.getBorrowRepayment(model);
	}

	/**
	 * 待还数据接口
	 * @author fxl
	 * @date 2017年3月01日
	 * @return
	 */
	@RequestMapping(value = "/statistic/borrow/getRepayInfo")
	@RequiresPermissions("statistic:borrow:collection:query")
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.STATISTIC_BORROW_NOTREPAY)
	@ResponseBody
	public Object getRepayInfo() {
		return statisticRepayInfoService.getRepayInfo();
	}
	
	/**
	 * 正常还款分析页面
	 * @author fxl
	 * @date 2017年3月01日
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/statistic/borrow/borrowRepaidManage")
	@RequiresPermissions("statistic:borrow:repay:query")
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.STATISTIC_BORROW_REPAID)
	public String borrowRepaidManage(Model model){
		initDate(model);
		return "/statistic/borrow/borrowRepaidManage";
	}
	
	/**
	 * 正常还款接口
	 * @author fxl
	 * @date 2017年3月01日
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/statistic/borrow/getBorrowRepaid")
	@RequiresPermissions("statistic:borrow:repay:query")
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.STATISTIC_BORROW_REPAID)
	@ResponseBody
	public Object getBorrowRepaid(StatisticModel model) {
		model.setRepayStatus(RepaymentEnum.STATUS_REPAID.getValue());
		return statisticRepayService.getBorrowRepayment(model);
	}
	
	/**
	 * 逾期分析页面
	 * @author fxl
	 * @date 2017年3月01日
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/statistic/borrow/borrowOverdueManage")
	@RequiresPermissions("statistic:borrow:overdue:query")
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.STATISTIC_BORROW_OVERDUE)
	public String borrowOverdueManage(Model model){
		initDate(model);
		return "/statistic/borrow/borrowOverdueManage";
	}
	
	/**
	 * 逾期信息接口
	 * @author fxl
	 * @date 2017年3月01日
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/statistic/borrow/getOverdueInfo")
	@RequiresPermissions("statistic:borrow:overdue:query")
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.STATISTIC_BORROW_OVERDUE)
	@ResponseBody
	public Object getOverdueInfo() {
		return statisticRepayInfoService.getRepayInfo();
	}
	
	/**
	 * 逾期列表接口
	 * @author fxl
	 * @date 2017年3月01日
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/statistic/borrow/getBorrowOverdue")
	@RequiresPermissions("statistic:borrow:overdue:query")
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.STATISTIC_BORROW_OVERDUE)
	@ResponseBody
	public Object getBorrowOverdue(StatisticModel model) {
		return statisticOverdueService.getOverdueCount(model);
	}

	/**
	 * 逾期率接口
	 * @author fxl
	 * @date 2017年3月01日
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/statistic/borrow/getBorrowOverdueRate")
	@RequiresPermissions("statistic:borrow:overdue:query")
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.STATISTIC_BORROW_OVERDUE)
	@ResponseBody
	public Object getBorrowOverdueRate(StatisticModel model) {
		return statisticOverdueService.getOverdueRate(model);
	}
	
	/**
	 * 逾期借款人信息
	 * @author fxl
	 * @date 2017年3月7日
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/statistic/borrow/borrowOverdueInfoManage")
	@RequiresPermissions("statistic:borrow:overdue:query")
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.STATISTIC_BORROW_OVERDUE)
	public String borrowOverdueInfoManage(Model model){
		initDate(model);
		return "/statistic/borrow/borrowOverdueInfoManage";
	}
	
	/**
	 * 逾期借款人信息
	 * @author fxl
	 * @date 2017年3月7日
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/statistic/borrow/getBorrowOverdueInfo")
	@RequiresPermissions("statistic:borrow:overdue:query")
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.STATISTIC_BORROW_OVERDUE)
	@ResponseBody
	public Object getBorrowOverdueInfo(StatisticModel model) {
		model.setUserType(StatisticConstant.USER_TYPE_OVERDUE);//逾期
		return statisticUserService.getUserPortrait(model);
	}
	
	/**
	 * 逾期借款人地区
	 * @author fxl
	 * @date 2017年3月7日
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/statistic/borrow/getBorrowOverdueArea")
	@RequiresPermissions("statistic:borrow:overdue:query")
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.STATISTIC_BORROW_OVERDUE)
	@ResponseBody
	public Object getBorrowOverdueArea(StatisticModel model) {
		return statisticOverdueService.getBorrowOverdueArea(model);
	}
	
}
