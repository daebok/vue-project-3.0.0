package com.rd.ifaes.manage.operate;

import javax.annotation.Resource;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rd.ifaes.common.annotation.SystemLog;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.core.core.constant.SysLogConstant;
import com.rd.ifaes.core.operate.domain.ActivityPlan;
import com.rd.ifaes.core.operate.service.ActivityPlanService;
import com.rd.ifaes.manage.sys.SystemController;


/**
 * 
 *  活动方案
 * @version 3.0
 * @author wyw
 * @date 2016-7-27
 */
@Controller
public class ActivityManageController  extends SystemController {
	/**
	 * 活动方案业务
	 */
	@Resource
	private transient  ActivityPlanService activityPlanService;
	/**
	 * 
	 * 活动方案管理页面
	 * @author wyw
	 * @date 2016-7-27
	 * @return
	 */
	@RequestMapping(value = "/operate/activity/activityManage")
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.ACTIVITY)
	public String activityManage(){
		return "/operate/activity/activityManage";
	}
	
	

	/**
	 * 
	 * 活动方案数据
	 * @author wyw
	 * @date 2016-7-27
	 * @param activityPlan
	 * @return
	 */
	@RequestMapping(value = "/operate/activity/activityList")
	@ResponseBody
	@RequiresPermissions("oper:actPlan:query")
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.ACTIVITY)
	public Object activityList(final ActivityPlan activityPlan){
		activityPlan.getPage().setSort("sort");
		activityPlan.getPage().setOrder("asc");
		return activityPlanService.findPage(activityPlan);
	}
	/**
	 * 
	 * 启用或者禁用方案
	 * @author wyw
	 * @date 2016-7-27
	 * @param activityPlan
	 * @param id
	 * @return
	 */
	@RequiresPermissions("oper:actPlan:cancel")
	@RequestMapping(value = "/operate/activity/activityStatus")
	@ResponseBody
	@SystemLog(method=SysLogConstant.EDIT,content=SysLogConstant.ACTIVITY)
	public Object activityStatus(final ActivityPlan activityPlan, final String id){
		activityPlan.setUuid(id);
	    activityPlanService.update(activityPlan);
		return renderSuccessResult();
	}
	/**
	 * 
	 * 选择用户
	 * @author wyw
	 * @date 2016-8-29
	 * @return
	 */
	@RequestMapping(value = "/operate/activity/selectGrantUsers")
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.USER_LIST)
	public String selectGrantUsers(final String userIds ,final Model model) {
		if(StringUtils.isNotBlank(userIds)){
			  model.addAttribute("userIds", userIds);
		}
		return "/operate/activity/selectGrantUsers";
	}
}
