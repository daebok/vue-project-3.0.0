package com.rd.ifaes.web.controller.member;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rd.ifaes.common.dict.DeleteFlagEnum;
import com.rd.ifaes.common.orm.Page;
import com.rd.ifaes.common.util.SessionUtils;
import com.rd.ifaes.core.base.web.BaseController;
import com.rd.ifaes.core.core.constant.Constant;
import com.rd.ifaes.core.operate.domain.UserRateCoupon;
import com.rd.ifaes.core.operate.domain.UserRedenvelope;
import com.rd.ifaes.core.operate.service.UserRateCouponService;
import com.rd.ifaes.core.operate.service.UserRedenvelopeService;
import com.rd.ifaes.core.project.domain.Project;
import com.rd.ifaes.core.project.domain.ProjectType;
import com.rd.ifaes.core.project.service.ProjectService;
import com.rd.ifaes.core.project.service.ProjectTypeService;
import com.rd.ifaes.core.user.domain.User;

/**
 * 
 *  我的优惠
 * @version 3.0
 * @author wyw
 * @date 2016-8-2
 */
@Controller
public class MyCouponController extends BaseController {
	/**用户红包业务处理*/
	@Resource
	private transient UserRedenvelopeService userRedenvelopeService;
	/**用户加息券业务处理*/
	@Resource
	private transient UserRateCouponService userRateCouponService;
	/**项目业务处理*/
	@Resource
	private transient ProjectService projectService;
	/**项目分类业务服务*/
	@Resource
	private transient ProjectTypeService projectTypeService;
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
	 * 获取用户红包
	 * @author wyw
	 * @date 2016-8-2
	 * @param userRedenvelope
	 * @return
	 */
	@RequestMapping(value = "/member/coupon/userRedenvelopeList")
	@ResponseBody
	public Object userRedenvelopeList(final UserRedenvelope userRedenvelope,final String dateType){
		final User user = SessionUtils.getSessionUser();
		userRedenvelope.setStatus(dateType);
		userRedenvelope.setUserId(user.getUuid());
		final Page<UserRedenvelope> page = userRedenvelopeService.findWebPage(userRedenvelope);
	    ProjectType projectType = new ProjectType();
	    projectType.setDeleteFlag(DeleteFlagEnum.NO.getValue());
	    Page<ProjectType> typePage = projectTypeService.findPage(projectType);
	    
	    final Map<String, Object> data =   this.renderSuccessResult();
		data.put("data", page);
		data.put("typePage", typePage);
		return data;
	}

	/**
	 * 
	 * 获取用户加息券
	 * @author wyw
	 * @date 2016-8-2
	 * @param userRateCoupon
	 * @return
	 */
	@RequestMapping(value = "/member/coupon/userRateCouponList")
	@ResponseBody
	public Object userRateCouponList(final UserRateCoupon userRateCoupon,final String dateType){
		final User user = SessionUtils.getSessionUser();
		userRateCoupon.setStatus(dateType);
		userRateCoupon.setUserId(user.getUuid());
		final Page<UserRateCoupon> page = userRateCouponService.findWebPage(userRateCoupon);
		final Map<String, Object> data =   this.renderSuccessResult();
		data.put("data", page);
		return data;
	}
	/**
	 * 
	 * 获取用户可用红包
	 * @author ywt
	 * @date 2016-10-17
	 * @param projectId,tenderMoney,userRedenvelope
	 * @return
	 */
	@RequestMapping(value = "/member/coupon/availableRedList")
	@ResponseBody
	public Object availableRedList(final String projectId,final BigDecimal tenderMoney,UserRedenvelope userRedenvelope ){
		final User user = SessionUtils.getSessionUser();
		Project project=projectService.get(projectId);
		userRedenvelope.setUserId(user.getUuid());
		final Page<UserRedenvelope> page=userRedenvelope.getPage();
		page.setSort("use_expire_time");
		page.setOrder(Constant.DESC);
		userRedenvelope.setPage(page);
		final List<UserRedenvelope>  list=userRedenvelopeService.findAvailableRedenvelope(userRedenvelope, project, tenderMoney);
		page.setRows(list);
		final Map<String, Object> data =  this.renderSuccessResult();
		data.put("data", page);
		return data;
	}
	/**
	 * 
	 * 获取用户可用加息券
	 * @author ywt
	 * @date 2016-10-17
	 * @param projectId,tenderMoney,userRateCoupon
	 * @return
	 */
	@RequestMapping(value = "/member/coupon/availableRateList")
	@ResponseBody
	public Object availableRateList(final String projectId,final BigDecimal tenderMoney,final UserRateCoupon userRateCoupon){
		final User user = SessionUtils.getSessionUser();
		Project project=projectService.get(projectId);
		userRateCoupon.setUserId(user.getUuid());
		final Page<UserRateCoupon> page=userRateCoupon.getPage();
		page.setSort("use_expire_time");
		page.setOrder(Constant.DESC);
		userRateCoupon.setPage(page);
		final List<UserRateCoupon>  list=userRateCouponService.findAvailableRateCoupon(userRateCoupon, project, tenderMoney);
		page.setRows(list);
		final Map<String, Object> data =  this.renderSuccessResult();
		data.put("data", page);
		return data;
	}
}
