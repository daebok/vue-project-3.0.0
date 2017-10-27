/**
* 互联网金融交易系统ifaes (Internet Finance  Assets  Exchange  System)
 * Copyright (c) 2016 杭州融都科技股份有限公司 版权所有
 */
package com.rd.ifaes.manage.statistic;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
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

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.rd.ifaes.common.annotation.SystemLog;
import com.rd.ifaes.common.util.DateUtils;
import com.rd.ifaes.core.core.constant.SysLogConstant;
import com.rd.ifaes.core.statistic.model.StatisticModel;
import com.rd.ifaes.core.statistic.model.StatisticResultModel;
import com.rd.ifaes.core.statistic.service.StatisticInvestService;
import com.rd.ifaes.core.statistic.service.StatisticService;
import com.rd.ifaes.manage.sys.SystemController;


/**
 * 投资分析
 * @version 3.0
 * @date 2017年2月21日
 */
@Controller
public class StatisticInvestManageController extends SystemController{
	
	@Resource
	private StatisticInvestService statisticInvestService;
	@Resource
	private StatisticService statisticService;
	
	/**
	 * 日期初始化
	 * @author xhf
	 * @date 2017年3月1日
	 * @param model
	 */
	private void initDate(Model model){
		Date endDate = DateUtils.rollDay(DateUtils.getToday(),-1);
		Date startDate = DateUtils.rollDay(endDate, -6);
		model.addAttribute("startDate",DateUtils.formatDate(startDate));
		model.addAttribute("endDate",DateUtils.formatDate(endDate));
		model.addAttribute("projectTypeMap", JSON.toJSONString(statisticInvestService.getProjectTypeMap()));
	}
	
	/**
	 * 投资人数分析
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/statistic/invest/investCustomersManage")
	@RequiresPermissions("statistic:invest:customers:query")
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.STATISTIC_INVEST_CUSTOMERS)
	public String investCustomersManage(final Model model){
		initDate(model);
		return "/statistic/invest/investCustomersManage";
	}
	
	/**
	 * 投资金额分析
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/statistic/invest/investAmntManage")
	@RequiresPermissions("statistic:invest:amount:query")
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.STATISTIC_INVEST_AMOUNT)
	public String investAmntManage(final Model model){
		initDate(model);
		return "/statistic/invest/investAmntManage";
	}
	
	/**
	 * 投资笔数分析
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/statistic/invest/investNumManage")
	@RequiresPermissions("statistic:invest:times:query")
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.STATISTIC_INVEST_TIMES)
	public String investNumManage(final Model model){
		initDate(model);
		return "/statistic/invest/investNumManage";
	}
	
	/**
	 * 投资地区分析
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/statistic/invest/investAreaManage")
	@RequiresPermissions("statistic:invest:area:query")
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.STATISTIC_INVEST_AREA)
	public String investAreaManage(final Model model){
		initDate(model);
		return "/statistic/invest/investAreaManage";
	}
	
	/**
	 * 投资情况分析
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/statistic/invest/investConditionManage")
	@RequiresPermissions("statistic:invest:condition:query")
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.STATISTIC_INVEST_CONDITION)
	public String investConditionManage(final Model model){
		initDate(model);
		return "/statistic/invest/investConditionManage";
	}
	
	/**
	 * 统计人数、金额、笔数接口
	 * @throws ParseException 
	 */
	@RequestMapping(value = "/statistic/invest/findInvestByStatType")
	@ResponseBody
	public Object findInvestByStatType(StatisticModel model){
		return statisticInvestService.findInvestByStatType(model);
	}
	
	/**
	 * 统计情况接口
	 * @throws ParseException 
	 */
	@RequestMapping(value = "/statistic/invest/getInvestCondition")
	@RequiresPermissions("statistic:invest:condition:query")
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.STATISTIC_INVEST_CONDITION)
	@ResponseBody
	public Object getInvestCondition(StatisticModel model){
		final Map<String,Object> map = Maps.newHashMap();
		map.put("result", true);
		map.put("data", statisticInvestService.getInvestCondition(model));
		return map;
	}

	/**
	 * 地区投资统计接口
	 * @throws ParseException 
	 */
	@RequestMapping(value = "/statistic/invest/getInvestArea")
	@RequiresPermissions("statistic:invest:area:query")
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.STATISTIC_INVEST_AREA)
	@ResponseBody
	public Object getInvestArea(StatisticModel model){
		return statisticInvestService.getInvestArea(model);
	}
	
	/**
	 * 地区投资统计列表接口
	 * @throws ParseException 
	 */
	@RequestMapping(value = "/statistic/invest/getInvestAreaList")
	@RequiresPermissions("statistic:invest:area:query")
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.STATISTIC_INVEST_AREA)
	@ResponseBody
	public Object getInvestAreaList(StatisticModel model){
		return statisticInvestService.getInvestArea(model);
	}
	
	/**
	 * 统计列表数据接口
	 * @throws ParseException 
	 */
	@RequestMapping(value = "/statistic/invest/getInvestList")
	@ResponseBody
	public Object getInvestList(StatisticModel model){
		return statisticInvestService.getInvestList(model);
	}
	
	/**
	 * 统计列表数据接口
	 * @throws ParseException 
	 */
	@RequestMapping(value = "/statistic/invest/compareData")
	@ResponseBody
	public Object compareData(StatisticModel model){
		return statisticInvestService.compareDataByDate(model);
	}
	
	/**
	 * 
	 * 导出人数明细
	 */
	@RequestMapping(value = "/statistic/invest/exportCustomersExcel")
	@RequiresPermissions("statistic:invest:customers:export")
	@SystemLog(method=SysLogConstant.EXPORT,content=SysLogConstant.STATISTIC_INVEST_CUSTOMERS)
	public void exportCustomersExcel(final StatisticModel model,final HttpServletRequest request,final HttpServletResponse response) throws IOException {
		exportStatisticInvest(model, request, response);
	}
	
	/**
	 * 
	 * 导出金额明细
	 */
	@RequestMapping(value = "/statistic/invest/exportAmountExcel")
	@RequiresPermissions("statistic:invest:amount:export")
	@SystemLog(method=SysLogConstant.EXPORT,content=SysLogConstant.STATISTIC_INVEST_AMOUNT)
	public void exportAmountExcel(final StatisticModel model,final HttpServletRequest request,final HttpServletResponse response) throws IOException {
		exportStatisticInvest(model, request, response);
	}
	
	/**
	 * 
	 * 导出笔数明细
	 */
	@RequestMapping(value = "/statistic/invest/exportNumExcel")
	@RequiresPermissions("statistic:invest:times:export")
	@SystemLog(method=SysLogConstant.EXPORT,content=SysLogConstant.STATISTIC_INVEST_TIMES)
	public void exportNumExcel(final StatisticModel model,final HttpServletRequest request,final HttpServletResponse response) throws IOException {
		exportStatisticInvest(model, request, response);
	}
	
	@SuppressWarnings("unchecked")
	private void exportStatisticInvest(final StatisticModel model,final HttpServletRequest request,final HttpServletResponse response) throws IOException{
		final Map<String, Object> map = statisticInvestService.getInvestList(model);
		final List<Object> dataList = (List<Object>) map.get("dataList");
		statisticService.exportExcel(model, dataList, request, response);
	}
	
	/**
	 * 
	 * 导出地区投资明细
	 */
    @SuppressWarnings("unchecked")
	@RequestMapping(value = "/statistic/invest/exportInvestAreaExcel")
    @RequiresPermissions("statistic:invest:area:export")
	@SystemLog(method=SysLogConstant.EXPORT,content=SysLogConstant.STATISTIC_INVEST_AREA)
	public void exportInvestAreaExcel(final StatisticModel model,final HttpServletRequest request,final HttpServletResponse response) throws IOException {
		final Map<String, Object> map = statisticInvestService.getInvestAreaList(model);
		final List<Object> dataList = (List<Object>) map.get("dataList");
		//最后一行总计
		StatisticResultModel resultModel = new StatisticResultModel();
		resultModel.setArea("总计");
		resultModel.setUserNum((Integer)map.get("totalUsers"));
		resultModel.setTotalAmnt((BigDecimal)map.get("totalAmnt"));
		resultModel.setTotalCount((BigDecimal)map.get("totalNums"));
		dataList.add(resultModel);
		
		statisticService.exportExcel(model,dataList,request,response);
	}
	
}
