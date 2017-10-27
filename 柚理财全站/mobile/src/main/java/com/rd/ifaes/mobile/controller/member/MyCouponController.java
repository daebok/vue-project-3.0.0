package com.rd.ifaes.mobile.controller.member;

import java.math.BigDecimal;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rd.ifaes.common.orm.Page;
import com.rd.ifaes.core.core.constant.Constant;
import com.rd.ifaes.core.operate.domain.UserRateCoupon;
import com.rd.ifaes.core.operate.domain.UserRedenvelope;
import com.rd.ifaes.core.operate.service.UserRateCouponService;
import com.rd.ifaes.core.operate.service.UserRedenvelopeService;
import com.rd.ifaes.core.project.domain.Project;
import com.rd.ifaes.core.project.service.ProjectService;
import com.rd.ifaes.core.user.domain.User;
import com.rd.ifaes.mobile.common.AppDataUtil;
import com.rd.ifaes.mobile.controller.WebController;
import com.rd.ifaes.mobile.model.PagedData;
import com.rd.ifaes.mobile.model.account.UserRateCouponModel;
import com.rd.ifaes.mobile.model.account.UserRedenvelopeModel;

/**
 * 
 *  我的优惠
 * @version 3.0
 * @author wyw
 * @date 2016-8-2
 */
@Controller
public class MyCouponController extends WebController {
	/**用户红包业务处理*/
	@Resource
	private transient UserRedenvelopeService userRedenvelopeService;
	/**用户加息券业务处理*/
	@Resource
	private transient UserRateCouponService userRateCouponService;
	/**项目业务处理*/
	@Resource
	private transient ProjectService projectService;
	/**
	 * 
	 * 我的优惠 页面
	 * @author wyw
	 * @date 2016-8-2
	 * @return
	 */
	@RequestMapping(value = "/member/coupon/myCoupon")
	public String myCoupon(){
		return "/member/coupon/myCoupon";
	}
	

	/**
	 * 
	 * 移动端获取用户红包
	 * @author xiaodingjiang
	 * @date 2016-10-21
	 * @param userRedenvelope
	 * @return
	 */
	@RequestMapping(value = "/app/member/coupon/userRedenvelopeList")
	@ResponseBody
	public Object userRedenvelopeList(UserRedenvelope userRedenvelope,HttpServletRequest request){
		Object obj=null;
		try{
		User user = getAppSessionUser(request);
		PagedData<UserRedenvelopeModel> pirlist=new PagedData<UserRedenvelopeModel>();
		String ruleRemark="";//规则说明
		//app默认查询所有的红包
		userRedenvelope.setStatus("-1");//查全部
		userRedenvelope.setUserId(user.getUuid());
		int page=userRedenvelope.getPage().getPage();
		Page<UserRedenvelope> userRed = userRedenvelopeService.findWebPage(userRedenvelope);
		userRed.setPage(page);
		List<UserRedenvelope>  redEnvelopeList=userRed.getRows();
		fillPageData(pirlist,userRed);
		for(UserRedenvelope userRedevelope:redEnvelopeList){
			//过滤掉平台作废的红包
		/*	if(userRedevelope.getStatus().equals("3")){
				continue;
			}*/
			UserRedenvelopeModel model=new UserRedenvelopeModel();
			model.setAmount(userRedevelope.getAmount().intValue());//红包金额
			model.setRuleName(userRedevelope.getRuleName());//规则名称
			model.setStatus(userRedevelope.getStatus());//红包状态：0未使用,1已使用，2已过期 3已作废
			/*if(userRedevelope.getUseExpireTime()==null){
				model.setUseExpireTime(DateUtils.parseDate("1970-01-01 08:00:01"));
			}else{*/
				model.setUseExpireTime(userRedevelope.getUseExpireTime());//过期时间
//			//}
			model.setUuid(userRedevelope.getUuid());//红包id
			model.setUseTenderMoney(userRedevelope.getUseTenderMoney());//最低起投金额
			model.setUseProjectType(userRedevelope.getUseProjectType()==null?"0":userRedevelope.getUseProjectType());//使用产品分类	0 全部可用 1部分产品可用	
			model.setUseProjectTypeName(userRedevelope.getUseProjectTypeName()==null?"":userRedevelope.getUseProjectTypeName());//使用产品名称
			ruleRemark="投资满"+userRedevelope.getUseTenderMoney().intValue()+"元可用";
			model.setRuleRemark(ruleRemark);
			pirlist.getList().add(model);
		}
        obj=createSuccessAppResponse(pirlist);
	} catch (Exception e) {
		obj=dealException(e);
	}
	return obj;
}
	
	
	/**
	 * 
	 * 移动端获取用户加息券
	 * @author xiaodingjiang
	 * @date 2016-10-21
	 * @param userRateCoupon
	 * @return
	 */
	@RequestMapping(value = "/app/member/coupon/userRateCouponList")
	@ResponseBody
	public Object userRateCouponList(UserRateCoupon userRateCoupon,HttpServletRequest request){
		Object obj=null;
		try{
			User user = getAppSessionUser(request);
			PagedData<UserRateCouponModel> pirlist=new PagedData<UserRateCouponModel>();
			String ruleRemark="";
			//app默认查询所有的加息劵
			userRateCoupon.setStatus("-1");//查全部
			userRateCoupon.setUserId(user.getUuid());
			int page=userRateCoupon.getPage().getPage();
			final Page<UserRateCoupon> userRatePage = userRateCouponService.findWebPage(userRateCoupon);
			List<UserRateCoupon>  userRateList=userRatePage.getRows();
			userRatePage.setPage(page);
			fillPageData(pirlist,userRatePage);
			for(UserRateCoupon userRate:userRateList){
				UserRateCouponModel model=new UserRateCouponModel();
				model.setUpApr(AppDataUtil.doubleFormat(userRate.getUpApr()));//加息利率
				model.setRuleName(userRate.getRuleName());//规则名称
				model.setStatus(userRate.getStatus());//加息劵状态：0未使用,1已使用，2已过期 3已作废
				model.setUseExpireTime(userRate.getUseExpireTime());//过期时间
			/*	if(userRate.getUseExpireTime()==null){
					model.setUseExpireTime(DateUtils.parseDate("1970-01-01 08:00:01"));
				}*/
				model.setUuid(userRate.getUuid());//加息劵id
				model.setUseTenderMoney(userRate.getUseTenderMoney());//最低起投金额
				model.setUseProjectType(userRate.getUseProjectType()==null?"0":userRate.getUseProjectType());//使用产品分类	0 全部可用 1部分产品可用	
				model.setUseProjectTypeName(userRate.getUseProjectTypeName()==null?"":userRate.getUseProjectTypeName());//使用产品名称
				ruleRemark="投资满"+userRate.getUseTenderMoney().intValue()+"元可用";
				model.setRuleRemark(ruleRemark);
				pirlist.getList().add(model);
			}
	     obj=createSuccessAppResponse(pirlist);
		} catch (Exception e) {
			obj=dealException(e);
		}
		return obj;
	}
	/**
	 * 
	 * 获取用户可用红包
	 * @author lgx
	 * @date 2016-11-03
	 * @return
	 */
	@RequestMapping(value = "/app/member/coupon/availableRedList")
	@ResponseBody
	public Object availableRedList(UserRedenvelope userRedenvelope,BigDecimal tenderMoney,String projectId,HttpServletRequest request){
		Object obj=null;
		try{
			User user=getAppSessionUser(request);
			PagedData<UserRedenvelopeModel> pirlist=new PagedData<UserRedenvelopeModel>();
			String ruleRemark="";
			Project project=projectService.get(projectId);
			userRedenvelope.setUserId(user.getUuid());
		  final Page<UserRedenvelope> pageu=userRedenvelope.getPage();
			pageu.setSort("use_expire_time");
			pageu.setOrder(Constant.DESC);
			userRedenvelope.setPage(pageu);                                      
			final List<UserRedenvelope>  availableRedList=userRedenvelopeService.findAvailableRedenvelope(userRedenvelope, project, tenderMoney);
			pageu.setRows(availableRedList);
			fillPageData(pirlist,pageu);
			for(UserRedenvelope userRedevelope:availableRedList){
				UserRedenvelopeModel model=new UserRedenvelopeModel();
				model.setAmount(userRedevelope.getAmount().intValue());//红包金额
				model.setRuleName(userRedevelope.getRuleName());//规则名称*
				model.setStatus(userRedevelope.getStatus());//红包状态：0未使用,1已使用，2已过期 3已作废*
				model.setUseExpireTime(userRedevelope.getUseExpireTime());//过期时间*
			/*	if(userRedevelope.getUseExpireTime()==null){
					model.setUseExpireTime(DateUtils.parseDate("1970-01-01 08:00:01"));
				}*/
				model.setUuid(userRedevelope.getUuid());//红包id
				model.setUseTenderMoney(userRedevelope.getUseTenderMoney());//最低起投金额
				model.setUseProjectType(userRedevelope.getUseProjectType()==null?"0":userRedevelope.getUseProjectType());//使用产品分类	0 全部可用 1部分产品可用	
				model.setUseProjectTypeName(userRedevelope.getUseProjectTypeName()==null?"":userRedevelope.getUseProjectTypeName());//使用产品名称
				ruleRemark="投资满"+userRedevelope.getUseTenderMoney().intValue()+"元可用";
				model.setRuleRemark(ruleRemark);
				pirlist.getList().add(model);
			}
			obj=createSuccessAppResponse(pirlist);
		}catch(Exception e){
			obj=dealException(e);
		}
		return obj;
	}
	/**
	 * 
	 * 获取用户可用加息券
	 * @author lgx
	 * @date 2016-10-21
	 * @param projectId,tenderMoney,userRateCoupon
	 * @return
	 */
	@RequestMapping(value = "/app/member/coupon/availableRateList")
	@ResponseBody
	public Object availableRateList(String projectId,BigDecimal tenderMoney,UserRateCoupon userRateCoupon,HttpServletRequest request){
		Object obj=null;
		try{
			User user=getAppSessionUser(request);
			PagedData<UserRateCouponModel> pirlist=new PagedData<UserRateCouponModel>();
			String ruleRemark="";
			Project project=projectService.get(projectId);
			userRateCoupon.setUserId(user.getUuid());
			final Page<UserRateCoupon> pages=userRateCoupon.getPage();
			  pages.setSort("use_expire_time");
				pages.setOrder(Constant.DESC);
				userRateCoupon.setPage(pages);
			final List<UserRateCoupon>  availableRateList=userRateCouponService.findAvailableRateCoupon(userRateCoupon, project, tenderMoney);
			pages.setRows(availableRateList);
			fillPageData(pirlist,pages);
			for(UserRateCoupon userRate:availableRateList){
				UserRateCouponModel model=new UserRateCouponModel();
				model.setUpApr(AppDataUtil.doubleFormat(userRate.getUpApr()));//加息利率
				model.setRuleName(userRate.getRuleName());//规则名称
				model.setStatus(userRate.getStatus());//加息劵状态：0未使用,1已使用，2已过期 3已作废
				model.setUseExpireTime(userRate.getUseExpireTime());//过期时间
				model.setUuid(userRate.getUuid());//加息劵id
				model.setUseTenderMoney(userRate.getUseTenderMoney());//最低起投金额
				model.setUseProjectType(userRate.getUseProjectType()==null?"0":userRate.getUseProjectType());//使用产品分类	0 全部可用 1部分产品可用	
				model.setUseProjectTypeName(userRate.getUseProjectTypeName()==null?"":userRate.getUseProjectTypeName());//使用产品名称
				ruleRemark="投资满"+userRate.getUseTenderMoney().intValue()+"元可用";
				model.setRuleRemark(ruleRemark);
				pirlist.getList().add(model);
			}
			obj=createSuccessAppResponse(pirlist);
		}catch(Exception e){
			obj=dealException(e);
		}
		
		return obj;
	}
}
