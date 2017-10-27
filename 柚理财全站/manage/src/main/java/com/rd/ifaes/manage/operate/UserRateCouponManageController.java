package com.rd.ifaes.manage.operate;

import java.util.List;

import javax.annotation.Resource;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rd.ifaes.common.annotation.SystemLog;
import com.rd.ifaes.common.annotation.TokenValid;
import com.rd.ifaes.common.constant.TokenConstants;
import com.rd.ifaes.common.dict.DeleteFlagEnum;
import com.rd.ifaes.common.dict.OperateEnum;
import com.rd.ifaes.common.exception.BussinessException;
import com.rd.ifaes.core.core.constant.Constant;
import com.rd.ifaes.core.core.constant.ResourceConstant;
import com.rd.ifaes.core.core.constant.SysLogConstant;
import com.rd.ifaes.core.core.util.ResourceUtils;
import com.rd.ifaes.core.operate.domain.ActivityPlan;
import com.rd.ifaes.core.operate.domain.RateCouponRule;
import com.rd.ifaes.core.operate.domain.UserRateCoupon;
import com.rd.ifaes.core.operate.service.ActivityPlanService;
import com.rd.ifaes.core.operate.service.RateCouponRuleService;
import com.rd.ifaes.core.operate.service.UserRateCouponService;
import com.rd.ifaes.manage.sys.SystemController;


/**
 * 
 * 用户加息券Controller
 * @version 3.0
 * @author wyw
 * @date 2016-8-31
 */
@Controller
public class UserRateCouponManageController extends SystemController{
	
	/** 用户加息券业务处理 */
	@Resource
	private transient UserRateCouponService userRateCouponService;
	/** 加息券规则业务处理 */
	@Resource
	private transient RateCouponRuleService rateCouponRuleService;
	@Resource
	private transient ActivityPlanService activityPlanService;
	/**
	 * 用户加息券管理
	 * @author wyw
	 * @date 2016-7-29
	 * @return
	 */
	@RequestMapping(value = "/operate/rateCoupon/userRateCouponManage")
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.USER_RATE)
	public String userRateCouponManage(){
		return "/operate/rateCoupon/userRateCouponManage";
	}

	/**
	 * 
	 * 用户加息券列表
	 * @author wyw
	 * @date 2016-7-29
	 * @param userRateCoupon
	 * @return
	 */
	@RequestMapping(value = "/operate/rateCoupon/userRateCouponList")
	@ResponseBody
	@RequiresPermissions("oper:addApr:addAprList:query")
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.USER_RATE)
	public Object userRateCouponList(final UserRateCoupon  userRateCoupon){
		return userRateCouponService.findPage(userRateCoupon);
	}
	/**
	 * 作废加息券
	 * @return
	 */
	@RequestMapping(value = "/operate/rateCoupon/rateCouponStatus")
	@ResponseBody
	@RequiresPermissions("oper:addApr:addAprList:cancel")
	@SystemLog(method=SysLogConstant.EDIT,content=SysLogConstant.USER_RATE)
	public Object userRedenvelopeStatus(final String id){
	    userRateCouponService.cancellationRateConpon(id);
		return renderSuccessResult();
	}
	
	/**
	 * 
	 * 发放用户加息券页面
	 * @author wyw
	 * @date 2016-8-28
	 * @return
	 */
	@RequestMapping(value = "/operate/rateCoupon/grantUserRate")
    @RequiresPermissions("oper:addApr:addAprList:rate_grant")
	@SystemLog(method=SysLogConstant.GRANT,content=SysLogConstant.USER_RATE)
	public String grantUserRate(final Model model) {
		ActivityPlan actp=activityPlanService.findActivityPlanByCode(OperateEnum.ACTIVITYPLAN_SELECT_USERS.getValue());
		if (actp==null || actp.getStatus().equals(OperateEnum.STATUS_FORBIDDEN.getValue())) {
			throw new BussinessException(ResourceUtils.get(ResourceConstant.ACTIVITY_GRANT_BAN));
		}
		//加载自定义加息全规则
		final RateCouponRule queryRate = new RateCouponRule();
		queryRate.setActivityCode(OperateEnum.ACTIVITYPLAN_SELECT_USERS.getValue());
		queryRate.setDeleteFlag(DeleteFlagEnum.NO.getValue());
		queryRate.setStatus(OperateEnum.STATUS_SERVICE.getValue());
		final List<RateCouponRule> rateRuleList = rateCouponRuleService.findListForGrant(queryRate);
		 model.addAttribute("rateRuleList", rateRuleList);
		return "/operate/rateCoupon/grantUserRate";
	}
	/**
	 * 加息券发放操作
	 * @author wyw
	 * @date 2016-8-28
	 * @param userIds
	 * @param redenvelopeRuleId
	 * @return
	 */
	@RequiresPermissions("oper:addApr:addAprList:rate_grant")
	@RequestMapping(value = "/operate/rateCoupon/doUserRateCoupon")
	@ResponseBody
	@SystemLog(method=SysLogConstant.GRANT,content=SysLogConstant.USER_RATE)
	@TokenValid(value = TokenConstants.TOKEN_GRANT_RATECOUPON, dispatch = true)
	public Object doUserRateCoupon(final String[] userIds,final String rateCouponRuleId){
		// 调用加息券发放规则 发放红包
		rateCouponRuleService.grantSelectUserRate(userIds, rateCouponRuleId,Constant.INT_ONE);
		return renderSuccessResult();
	}
	
}
