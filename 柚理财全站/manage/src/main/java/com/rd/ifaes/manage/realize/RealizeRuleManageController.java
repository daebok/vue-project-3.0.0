package com.rd.ifaes.manage.realize;

import java.math.BigDecimal;

import javax.annotation.Resource;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rd.ifaes.common.annotation.SystemLog;
import com.rd.ifaes.common.annotation.TokenValid;
import com.rd.ifaes.common.constant.TokenConstants;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.core.core.constant.ConfigConstant;
import com.rd.ifaes.core.core.constant.SysLogConstant;
import com.rd.ifaes.core.core.util.ConfigUtils;
import com.rd.ifaes.core.project.domain.RealizeRule;
import com.rd.ifaes.core.project.service.RealizeRuleService;
import com.rd.ifaes.manage.sys.SystemController;

/**
 * 变现规则管理
 * @version 3.0
 * @author fxl
 * @date 2016年8月1日
 */
@Controller
public class RealizeRuleManageController extends SystemController {
	
	@Resource
	private transient RealizeRuleService realizeRuleService;
	
	/**
	 * 变现规则管理
	 * @author fxl
	 * @date 2016年8月1日
	 * @return
	 */
	@RequestMapping(value = "/realize/rule/realizeRuleManage")
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.REALIZE_RULE)
	public String manage(final Model model){	
		final String daysOfYear = ConfigUtils.getValue(ConfigConstant.DAYS_OF_YEAR);
		final BigDecimal interestManageRate = ConfigUtils.getBigDecimal(ConfigConstant.INTEREST_MANAGE_RATE);
		model.addAttribute("interestManageRate", interestManageRate);
		model.addAttribute("daysOfYear", daysOfYear);
		model.addAttribute("hourList", StringUtils.getHourList());
		RealizeRule rule = realizeRuleService.getRule();
		model.addAttribute("rule", rule);
		if(rule!=null){
			model.addAttribute("issueTimeHour",rule.getIssueTime().split(":",3)[0]);
			model.addAttribute("issueTimeMin",rule.getIssueTime().split(":",3)[1]);
			model.addAttribute("raiseEndTimeHour",rule.getRaiseEndTime().split(":",3)[0]);
			model.addAttribute("raiseEndTimeMin",rule.getRaiseEndTime().split(":",3)[1]);
		}else{
			model.addAttribute("issueTimeHour","03");
			model.addAttribute("issueTimeMin","00");
			model.addAttribute("raiseEndTimeHour","04");
			model.addAttribute("raiseEndTimeMin","00");
		}
		return "/realize/rule/realizeRuleManage";
	}
	
	/**
	 * 查看历史记录页面
	 * @author fxl
	 * @date 2016年8月1日
	 * @return
	 */
	@RequestMapping(value = "/realize/rule/realizeRuleViewPage")
	@SystemLog(method=SysLogConstant.VIEW,content=SysLogConstant.REALIZE_RULE)
	public String realizeRuleViewPage() {
		return "/realize/rule/realizeRuleViewPage";
	}
	
	/**
	 * 变现规则列表
	 * @author fxl
	 * @date 2016年8月1日
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/realize/rule/list")
	@ResponseBody
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.REALIZE_RULE)
	public Object list(final RealizeRule model){		
		return realizeRuleService.findPage(model);
	}

	/**
	 * 保存规则
	 * @author fxl
	 * @date 2016年8月1日
	 * @param realizeRule
	 * @return
	 */
	@RequiresPermissions("project:realize:realizeRule:add")
	@RequestMapping(value = "/realize/rule/realizeRuleAdd", method = RequestMethod.POST)
	@ResponseBody
	@TokenValid(value=TokenConstants.TOKEN_REALIZE_RULE_ADD,dispatch=true)
	@SystemLog(method=SysLogConstant.ADD,content=SysLogConstant.REALIZE_RULE)
	public Object realizeRuleAdd(final RealizeRule realizeRule){		
		realizeRuleService.insert(realizeRule) ;
		return renderSuccessResult();
	}
}
