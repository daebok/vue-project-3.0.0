package com.rd.ifaes.web.activity.controller;


import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rd.ifaes.common.util.SessionUtils;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.core.base.web.BaseController;
import com.rd.ifaes.core.operate.service.RateCouponRuleService;
import com.rd.ifaes.core.operate.service.RedenvelopeRuleService;
import com.rd.ifaes.core.operate.service.UserRateCouponService;
import com.rd.ifaes.core.operate.service.UserRedenvelopeService;
import com.rd.ifaes.core.user.domain.User;

@Controller
public class ActivityController  extends BaseController{
	
	/** 用户红包业务处理 */
	@Resource
	private transient UserRedenvelopeService userRedenvelopeService;
	/** 红包规则业务处理 */
	@Resource
	private transient RedenvelopeRuleService redenvelopeRuleService;
	/** 用户加息券业务处理 */
	@Resource
	private transient UserRateCouponService userRateCouponService;
	/** 加息券规则业务处理 */
	@Resource
	private transient RateCouponRuleService rateCouponRuleService;
	
	
	@RequestMapping(value="/platformActivity/*")
	public String activityRedirct(final HttpServletRequest request ,final Model model) {
		String RequestUrl=request.getRequestURL().toString();
		String goPage="/";
		goPage+=RequestUrl.substring(RequestUrl.indexOf("platformActivity"),RequestUrl.lastIndexOf('.'));
		 model.addAttribute("redenvelopeId", StringUtils.isNull(redenvelopeRuleService.findRedenvelopeRuleIdByUrl(goPage+".html")));
		 model.addAttribute("rateCouponId",StringUtils.isNull(rateCouponRuleService.findRateCouponRuleIdByUrl(goPage+".html")));
		return goPage;
	}
	
	/**
	 * 自定义红包获取
	 * 
	 * @author ywt
	 * @date 2016-10-20
	 * @return
	 */
	@RequestMapping(value = "/activity/gainActivityRedenvelope")
	@ResponseBody
	public Object gainActivityRedenvelope(final String RedenvelopeRuleId){
		User user = SessionUtils.getSessionUser();
		userRedenvelopeService.gainActivityRedenvelope(user, RedenvelopeRuleId);
		return renderSuccessResult();
	}
	
	/**
	 * 自定义加息券获取
	 * 
	 * @author ywt
	 * @date 2016-10-20
	 * @return
	 */
	@RequestMapping(value = "/activity/gainActivityRateCoupon")
	@ResponseBody
	public Object gainActivityRateCoupon(final String RateCouponId){
		User user = SessionUtils.getSessionUser();
		userRateCouponService.gainActivityRateCoupon(user, RateCouponId);
		return renderSuccessResult();
	}
}
