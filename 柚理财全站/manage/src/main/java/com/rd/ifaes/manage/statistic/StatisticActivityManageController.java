/**
* 互联网金融交易系统ifaes (Internet Finance  Assets  Exchange  System)
 * Copyright (c) 2016 杭州融都科技股份有限公司 版权所有
 */
package com.rd.ifaes.manage.statistic;

import java.util.Date;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Maps;
import com.rd.ifaes.common.annotation.SystemLog;
import com.rd.ifaes.common.util.DateUtils;
import com.rd.ifaes.core.account.service.CashService;
import com.rd.ifaes.core.account.service.RechargeService;
import com.rd.ifaes.core.core.constant.SysLogConstant;
import com.rd.ifaes.core.operate.service.UserRateCouponService;
import com.rd.ifaes.core.operate.service.UserRedenvelopeService;
import com.rd.ifaes.core.statistic.constant.StatisticConstant;
import com.rd.ifaes.core.statistic.model.StatisticModel;
import com.rd.ifaes.core.statistic.service.StatisticInvestService;
import com.rd.ifaes.core.statistic.service.StatisticService;
import com.rd.ifaes.manage.sys.SystemController;

/**
 * 活动运营分析
 * @version 3.0
 * @date 2017年2月21日
 */
@Controller
public class StatisticActivityManageController extends SystemController{
	
	@Resource
	private RechargeService rechargeService;
	@Resource
	private CashService cashService;
	@Resource
	private StatisticService statisticService;
	@Resource
	private UserRedenvelopeService userRedenvelopeService;
	@Resource
	private UserRateCouponService userRateCouponService;
	@Resource
	private StatisticInvestService statisticInvestService;
	
	/**
	 * 日期初始化
	 * @author xhf
	 * @date 2017年3月1日
	 * @param model
	 */
	private void initDate(Model model){
		Date endDate = DateUtils.rollDay(DateUtils.getToday(), -1);
		Date startDate = DateUtils.rollDay(endDate, -6);
		model.addAttribute("startDate",DateUtils.formatDate(startDate));
		model.addAttribute("endDate",DateUtils.formatDate(endDate));
	}
	
	/**
	 * 活动运营页面
	 * @return
	 */
	@RequestMapping(value = "/statistic/activity/activityOperManage")
	public String activityOperationManage(Model model){
		initDate(model);
		return "/statistic/activity/activityOperManage";
	}
		
	/**
	 * 根据日期获取红包统计信息-二维图
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/statistic/activity/findRedByStatDate")
	@RequiresPermissions("statistic:activity:query")
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.STATISTIC_ACTIVITY)
	@ResponseBody
	public Object findRedByStatDate(StatisticModel model){
		return userRedenvelopeService.findByStatDate(model);
	}
	
	/**
	 * 根据日期获取红包统计信-饼状图
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/statistic/activity/findRedByStatStatus")
	@RequiresPermissions("statistic:activity:query")
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.STATISTIC_ACTIVITY)
	@ResponseBody
	public Object findRedByStatStatus(StatisticModel model){
		return userRedenvelopeService.findByStatStatus(model);
	}

	/**
	 * 根据类型获取加息券统计信息-二维图
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/statistic/activity/findRateByStatDate")
	@RequiresPermissions("statistic:activity:query")
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.STATISTIC_ACTIVITY)
	@ResponseBody
	public Object findRateByStatDate(StatisticModel model){
		return userRateCouponService.findByStatDate(model);
	}
	
	/**
	 * 根据类型获取加息券统计信息-饼状图
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/statistic/activity/findRateByStatType")
	@RequiresPermissions("statistic:activity:query")
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.STATISTIC_ACTIVITY)
	@ResponseBody
	public Object findRateByStatType(StatisticModel model){
		final Map<String,Object> map = Maps.newHashMap();
		map.put("result", true);
		map.put(StatisticConstant.STAT_PIE_TYPE, userRateCouponService.findByStatType(model));
		map.put(StatisticConstant.STAT_PIE_STATUS, userRateCouponService.findByStatStatus(model));
		return map;
	}

	/**
	 * 活动投资金额分析-二维图
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/statistic/activity/findRealAmntByDate")
	@RequiresPermissions("statistic:activity:query")
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.STATISTIC_ACTIVITY)
	@ResponseBody
	public Object findRealAmntByDate(StatisticModel model){
		return statisticInvestService.findRealAmntByDate(model);
	}
	
	/**
	 * 活动投资金额分析-饼状图
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/statistic/activity/findRealAmntByType")
	@RequiresPermissions("statistic:activity:query")
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.STATISTIC_ACTIVITY)
	@ResponseBody
	public Object findRealAmntByType(StatisticModel model){
		final Map<String,Object> map = Maps.newHashMap();
		map.put("result", true);
		map.put("data", statisticInvestService.getRealAmntMap(model));
		return map;
	}
}
