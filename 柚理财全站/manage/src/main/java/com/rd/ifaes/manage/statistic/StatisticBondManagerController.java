package com.rd.ifaes.manage.statistic;

import java.util.Date;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rd.ifaes.common.annotation.SystemLog;
import com.rd.ifaes.common.util.DateUtils;
import com.rd.ifaes.core.core.constant.SysLogConstant;
import com.rd.ifaes.core.statistic.model.StatisticModel;
import com.rd.ifaes.core.statistic.service.StatisticBondService;
import com.rd.ifaes.manage.sys.SystemController;

/**
 * 债权转让分析
 * @version 3.0
 * @date 2017年2月21日
 */
@Controller
public class StatisticBondManagerController extends SystemController {
	
	@Autowired
	private StatisticBondService statisticBondService;
	
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
	 * 债权转让分析
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/statistic/bond/bondInvestManage")
	public String borrowerNumManage(Model model){
		initDate(model);
		return "/statistic/bond/bondInvestManage";
	}
	
	/**
	 * 根据查询类型（金额、人数、笔数）获得统计信息
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/statistic/bond/findInvestByType")
	@RequiresPermissions("statistic:bond:query")
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.STATISTIC_BOND)
	@ResponseBody
	public Object getInvestByType(StatisticModel model){
		return statisticBondService.findInvestByStatType(model);
	}
	
	/**
	 * 获得债权期限统计信息
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/statistic/bond/getBondTimeLimit")
	@RequiresPermissions("statistic:bond:query")
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.STATISTIC_BOND)
	@ResponseBody
	public Object getBondTimeLimit(StatisticModel model){
		return statisticBondService.getBondTimeLimit(model);
	}
}
