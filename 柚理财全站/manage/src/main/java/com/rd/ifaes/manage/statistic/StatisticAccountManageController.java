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

import com.google.common.collect.Maps;
import com.rd.ifaes.common.annotation.SystemLog;
import com.rd.ifaes.common.util.DateUtils;
import com.rd.ifaes.core.account.model.TppMerchantLogModel;
import com.rd.ifaes.core.account.service.CashService;
import com.rd.ifaes.core.account.service.RechargeService;
import com.rd.ifaes.core.account.service.TppMerchantLogService;
import com.rd.ifaes.core.core.constant.SysLogConstant;
import com.rd.ifaes.core.statistic.constant.StatisticConstant;
import com.rd.ifaes.core.statistic.model.StatisticModel;
import com.rd.ifaes.core.statistic.service.StatisticAccountService;
import com.rd.ifaes.core.statistic.service.StatisticBadDebtService;
import com.rd.ifaes.core.statistic.service.StatisticEarnService;
import com.rd.ifaes.core.statistic.service.StatisticService;
import com.rd.ifaes.manage.sys.SystemController;

/**
 * 资金分析
 * @version 3.0
 * @date 2017年2月21日
 */
@Controller
public class StatisticAccountManageController extends SystemController{
	
	@Resource
	private RechargeService rechargeService;
	@Resource
	private CashService cashService;
	@Resource
	private StatisticService statisticService;
	@Resource
	private StatisticAccountService statisticAccountService;
	@Resource
	private TppMerchantLogService tppMerchantLogService;
	@Resource
	private StatisticBadDebtService statisticBadDebtService;
	@Resource
	private StatisticEarnService statisticEarnService;
	
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
	}
	
	/**
	 * 出入金分析页面
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/statistic/account/accountCirculateManage")
	public String accountCirculateManage(Model model){
		initDate(model);
		return "/statistic/account/accountCirculateManage";
	}
	
	/**
	 * 获取出入金信息-二维图
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/statistic/account/getAccountCirculate")
	@RequiresPermissions("statistic:account:circulate:query")
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.STATISTIC_ACCOUNT_CIRCULATE)
	@ResponseBody
	public Object getAccountCirculate(StatisticModel model){
		final Map<String, Object> map = Maps.newHashMap();
		map.put("result", true);
		//日期横坐标
		map.put(StatisticConstant.STAT_CHART_DATE, statisticService.getStatChartDate(model));
		//提现
		map.put("cash", cashService.findByStatDate(model));
		//充值
		map.put("recharge", rechargeService.findByStatDate(model));
		return map;
	}
	
	/**
	 * 获取出入金信息-饼状图
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/statistic/account/getAccountByType")
	@RequiresPermissions("statistic:account:circulate:query")
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.STATISTIC_ACCOUNT_CIRCULATE)
	@ResponseBody
	public Object getAccountByType(StatisticModel model){
		final Map<String, Object> map = Maps.newHashMap();
		map.put("result", true);
		//提现
		map.put("cash", cashService.findByStatType(model));
		//充值
		map.put("recharge", rechargeService.findByStatType(model));
		return map;
	}
	
	/**
	 * 账户可用余额分析页面
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/statistic/account/userUseMoneyManage")
	public String userUseMoneyManage(Model model){
		initDate(model);
		return "/statistic/account/userUseMoneyManage";
	}
	
	/**
	 * 用户可用余额统计
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/statistic/account/statisticUseMoney")
	@RequiresPermissions("statistic:account:useMoney:query")
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.STATISTIC_ACCOUNT_USEMONEY)
	@ResponseBody
	public Object statisticUseMoney(StatisticModel model){
		return statisticAccountService.findByStatType(model);
	}
	
	/**
	 * 投资收益分析页面
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/statistic/account/investEarnManage")
	public String investEarnManage(Model model){
		initDate(model);
		return "/statistic/account/investEarnManage";
	}
	
	/**
	 * 投资收益统计-二维图
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/statistic/account/statisticInvestEarn")
	@RequiresPermissions("statistic:account:investEarn:query")
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.STATISTIC_ACCOUNT_EARN)
	@ResponseBody
	public Object statisticInvestEarn(StatisticModel model){
		return statisticEarnService.findByStatDate(model);
	}
	
	/**
	 * 投资收益统计-饼状图
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/statistic/account/statisticInvestEarnByType")
	@RequiresPermissions("statistic:account:investEarn:query")
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.STATISTIC_ACCOUNT_EARN)
	@ResponseBody
	public Object statisticInvestEarnByType(StatisticModel model){
		return statisticEarnService.findByStatType(model);
	}
	
	/**
	 * 平台盈亏分析页面
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/statistic/account/platProfitManage")
	public String platIncomeManage(Model model){
		initDate(model);
		return "/statistic/account/platProfitManage";
	}
	
	/**
	 * 平台收入统计-二维图
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/statistic/account/statisticPlatIncome")
	@RequiresPermissions("statistic:account:platProfit:query")
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.STATISTIC_ACCOUNT_PLANT)
	@ResponseBody
	public Object statisticPlatIncome(StatisticModel model){
		model.setStatType(StatisticConstant.STAT_TYPE_AMOUNT);
		model.setTypeArr(TppMerchantLogModel.INCOME_ARR.split(","));
		
		final Map<String,Object> map = Maps.newHashMap();
		map.put("result", true);
		map.put(StatisticConstant.STAT_CHART_AMNT, tppMerchantLogService.findByStatDate(model));
		return map;
	}
	
	/**
	 * 平台收入统计-饼状图
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/statistic/account/statisticPlatIncomeByType")
	@RequiresPermissions("statistic:account:platProfit:query")
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.STATISTIC_ACCOUNT_PLANT)
	@ResponseBody
	public Object statisticPlatIncomeByType(StatisticModel model){
		model.setStatType(StatisticConstant.STAT_TYPE_AMOUNT);
		model.setTypeArr(TppMerchantLogModel.INCOME_ARR.split(","));
		
		final Map<String,Object> map = Maps.newHashMap();
		map.put("result", true);
		map.put(StatisticConstant.STAT_PIE_CHART, tppMerchantLogService.findByStatType(model));
		return map;
	}
	
	/**
	 * 平台支出统计-二维图
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/statistic/account/statisticPlatPay")
	@RequiresPermissions("statistic:account:platProfit:query")
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.STATISTIC_ACCOUNT_PLANT)
	@ResponseBody
	public Object statisticPlatPay(StatisticModel model){
		model.setStatType(StatisticConstant.STAT_TYPE_AMOUNT);
		model.setTypeArr(TppMerchantLogModel.PAY_ARR.split(","));
		
		final Map<String,Object> map = Maps.newHashMap();
		map.put("result", true);
		map.put(StatisticConstant.STAT_CHART_AMNT, tppMerchantLogService.findByStatDate(model));
		return map;
	}
	
	/**
	 * 平台支出统计-饼状图
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/statistic/account/statisticPlatPayByType")
	@RequiresPermissions("statistic:account:platProfit:query")
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.STATISTIC_ACCOUNT_PLANT)
	@ResponseBody
	public Object statisticPlatPayByType(StatisticModel model){
		model.setStatType(StatisticConstant.STAT_TYPE_AMOUNT);
		model.setTypeArr(TppMerchantLogModel.PAY_ARR.split(","));
		
		final Map<String,Object> map = Maps.newHashMap();
		map.put("result", true);
		map.put(StatisticConstant.STAT_PIE_CHART, tppMerchantLogService.findByStatType(model));
		return map;
	}
	
	
	/**
	 * 平台盈亏统计
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/statistic/account/statisticPlatProfit")
	@RequiresPermissions("statistic:account:platProfit:query")
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.STATISTIC_ACCOUNT_PLANT)
	@ResponseBody
	public Object statisticPlatProfit(StatisticModel model){
		return tppMerchantLogService.statisticPlatProfit(model);
	}
	
	/**
	 * 平台盈亏列表
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/statistic/account/getStatisticMertLogList")
	@RequiresPermissions("statistic:account:platProfit:query")
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.STATISTIC_ACCOUNT_PLANT)
	@ResponseBody
	public Object getStatisticMertLogList(StatisticModel model){
		return tppMerchantLogService.getStatisticMertLogList(model);
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/statistic/account/exportMertLogExcel")
	@RequiresPermissions("statistic:account:platProfit:export")
	@SystemLog(method=SysLogConstant.EXPORT,content=SysLogConstant.STATISTIC_ACCOUNT_PLANT)
	public void exportMertLogExcel(final StatisticModel model,final HttpServletRequest request,final HttpServletResponse response) throws IOException {
		final Map<String, Object> map = tppMerchantLogService.getStatisticMertLogList(model);
		final List<Object> dataList = (List<Object>) map.get("rows");
		statisticService.exportExcel(model, dataList, request, response);
	}
	
	/**
	 * 平台坏账统计
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/statistic/account/statisticBadDebt")
	@RequiresPermissions("statistic:account:platProfit:query")
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.STATISTIC_ACCOUNT_PLANT)
	@ResponseBody
	public Object statisticBadDebt(StatisticModel model){
		return statisticBadDebtService.findByStatDate(model);
	}
	
	/**
	 * 平台坏账统计
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/statistic/account/statisticBadDebtByType")
	@RequiresPermissions("statistic:account:platProfit:query")
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.STATISTIC_ACCOUNT_PLANT)
	@ResponseBody
	public Object statisticBadDebtByType(StatisticModel model){
		return statisticBadDebtService.findByStatType(model);
	}
	
}
