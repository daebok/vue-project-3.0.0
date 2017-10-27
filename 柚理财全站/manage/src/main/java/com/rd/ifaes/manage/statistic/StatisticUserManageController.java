/**
* 互联网金融交易系统ifaes (Internet Finance  Assets  Exchange  System)
 * Copyright (c) 2016 杭州融都科技股份有限公司 版权所有
 */
package com.rd.ifaes.manage.statistic;

import java.util.Date;

import javax.annotation.Resource;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rd.ifaes.common.annotation.SystemLog;
import com.rd.ifaes.common.util.DateUtils;
import com.rd.ifaes.core.core.constant.SysLogConstant;
import com.rd.ifaes.core.statistic.model.StatisticModel;
import com.rd.ifaes.core.statistic.service.StatisticUserService;
import com.rd.ifaes.manage.sys.SystemController;

/**
 * 
 * 用户分析
 * @version 3.0
 * @date 2017年3月6日
 */
@Controller
public class StatisticUserManageController extends SystemController{
	
	@Resource
	private StatisticUserService statisticUserService;
	
	
	
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
	 * 用户统计页面
	 * @author fxl
	 * @date 2017年3月6日
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/statistic/user/userStatisticManage")
	@RequiresPermissions("statistic:user:userStatistic:query")
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.STATISTIC_USER)
	public String userStatisticManage(Model model){
		initDate(model);
		return "/statistic/user/userStatisticManage";
	}
	
	/**
	 * 用户统计数据
	 * @author fxl
	 * @date 2017年3月6日
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/statistic/user/getUserStatistic")
	@RequiresPermissions("statistic:user:userStatistic:query")
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.STATISTIC_USER)
	@ResponseBody
	public Object getUserStatistic(StatisticModel model) {
		return statisticUserService.getUserStatistic(model);
	}
	
	/**
	 * 用户地区
	 * @author fxl
	 * @date 2017年3月7日
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/statistic/user/getUserArea")
	@RequiresPermissions("statistic:user:userStatistic:query")
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.STATISTIC_USER)
	@ResponseBody
	public Object getUserArea(StatisticModel model) {
		return statisticUserService.getUserArea(model);
	}
	
	
	/**
	 * 用户总数列表
	 * @author fxl
	 * @date 2017年3月7日
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/statistic/user/getUserCountList")
	@RequiresPermissions("statistic:user:userStatistic:query")
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.STATISTIC_USER)
	@ResponseBody
	public Object getUserCountList(StatisticModel model) {
		return statisticUserService.getUserCountList(model);
	}
	
	/**
	 * 用户活跃页面
	 * @author fxl
	 * @date 2017年3月7日
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/statistic/user/userActiveManage")
	@RequiresPermissions("statistic:user:userActive:query")
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.STATISTIC_USER_ACTIVE)
	public String userActiveManage(Model model){
		initDate(model);
		return "/statistic/user/userActiveManage";
	}
	
	/**
	 * 用户活跃列表接口
	 * @author fxl
	 * @date 2017年3月7日
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/statistic/user/getUserActiveList")
	@RequiresPermissions("statistic:user:userActive:query")
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.STATISTIC_USER_ACTIVE)
	@ResponseBody
	public Object getUserActiveList(StatisticModel model) {
		return statisticUserService.getUserActiveList(model);
	}
	
	/**
	 * 用户活跃地区接口
	 * @author fxl
	 * @date 2017年3月7日
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/statistic/user/getUserActiveArea")
	@RequiresPermissions("statistic:user:userActive:query")
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.STATISTIC_USER_ACTIVE)
	@ResponseBody
	public Object getUserActiveArea(StatisticModel model) {
		return statisticUserService.getUserActiveArea(model);
	}
	
	/**
	 * 用户活跃总计接口
	 * @author fxl
	 * @date 2017年3月7日
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/statistic/user/getUserActiveCountList")
	@RequiresPermissions("statistic:user:userActive:query")
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.STATISTIC_USER_ACTIVE)
	@ResponseBody
	public Object getUserActiveCountList(StatisticModel model) {
		return statisticUserService.getUserActiveCountList(model);
	}
	
	/**
	 * 用户画像页面
	 * @author fxl
	 * @date 2017年3月7日
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/statistic/user/userPortraitManage")
	@RequiresPermissions("statistic:user:userPortrait:query")
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.STATISTIC_USER_PORTRAIT)
	public String userPortraitManage(){
		return "/statistic/user/userPortraitManage";
	}
	
	/**
	 * 用户画像接口
	 * @author fxl
	 * @date 2017年3月7日
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/statistic/user/getUserPortrait")
	@RequiresPermissions("statistic:user:userPortrait:query")
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.STATISTIC_USER_PORTRAIT)
	@ResponseBody
	public Object getUserPortrait(StatisticModel model) {
		return statisticUserService.getUserPortrait(model);
	}
}
