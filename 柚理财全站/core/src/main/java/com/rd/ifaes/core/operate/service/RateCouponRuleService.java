package com.rd.ifaes.core.operate.service;

import java.math.BigDecimal;
import java.util.List;

import com.rd.ifaes.core.base.service.BaseService;
import com.rd.ifaes.core.operate.domain.RateCouponRule;
import com.rd.ifaes.core.operate.domain.RateCouponRuleDetail;
import com.rd.ifaes.core.operate.domain.UserActivityAwardLog;
import com.rd.ifaes.core.operate.domain.UserRateCoupon;
import com.rd.ifaes.core.operate.model.RateCouponRuleModel;
import com.rd.ifaes.core.project.domain.ProjectInvest;
import com.rd.ifaes.core.user.domain.User;

/**
 * 
 * 加息券规则业务接口
 * @version 3.0
 * @author wyw
 * @date 2016-7-28
 */
public interface RateCouponRuleService extends BaseService<RateCouponRule>{
	/**
	 * 保存加息券规则
	 * @author wyw
	 * @param ruleModel
	 * @param detaillist
	 * @return
	 */
	Boolean saveRule(RateCouponRuleModel ruleModel, List<RateCouponRuleDetail> detaillist,String userIds);
	/**
	 * 修改加息券规则
	 * @author wyw
	 * @param model 
	 * @param detaillist
	 * @return
	 */
	Boolean updateRule(RateCouponRuleModel model, List<RateCouponRuleDetail> detaillist);
	/**
	 * 启用或则禁用规则
	 * @author wyw
	 * @date 2016-7-28
	 * @param ratecouponRule
	 * @return
	 */
	int updateStatus(RateCouponRule rateCouponRule);
	/**
	 * 根据加息券规则发放加息券
	 * @author wyw
	 * @param user
	 * @return
	 */
	List<BigDecimal>  grantRateCoupon(User user,RateCouponRule rateCouponRule,ProjectInvest projectInvest,User inviteUser,final List<UserRateCoupon> urcs,final List<UserActivityAwardLog> logs);
	/**
	 * 选择用户发放加息券
	 * @author wyw
	 * @param userIds
	 * @param rateCouponRuleId
	 * @param type 1 普通发放页面   2 增加时候的发放
	 */
	void grantSelectUserRate(String[] userIds,String rateCouponRuleId,int type);
	/**
	 * 查询可发放的加息卷（选择用户发放加息券时调用）
	 * @author ywt
	 * @param rateCouponRule
	 */
	List<RateCouponRule> findListForGrant(RateCouponRule rateCouponRule);
	
	/**
	 * 查询可用发放条件的加息券规则
	 * @param activityCode
	 * @return
	 */
	List<RateCouponRule> findByActivityCode(String activityCode);
	

	/**
	 * 根据URL获取自定义活动的加息券Id
	 * @author ywt
	 * @date 2016-10-29
	 * @param url
	 * @return
	 */
	String findRateCouponRuleIdByUrl(String url);
	
	
	/**
	 * 批量删除
	 * @author ywt
	 * @date 2016-10-29
	 * @param url
	 * @return
	 */
	void checkAndDeleteBatch(String[] ids);
	
	/**
	   * 根据项目类型ID查找可用的加息券规则
	   * @author ywt
	   * @date 2016-11-05
	   * @param ProjectTypeId
	   * @return
	   */
	RateCouponRule findInvestRateCouponRuleByProjectTypeId(String ProjectTypeId);
	
	/**
	 * 校验发放类型
	 * @author fxl
	 * @date 2017年1月4日
	 * @param projectTypeId
	 * @return
	 */
	boolean checkGrantProjectType(String projectTypeId);
}