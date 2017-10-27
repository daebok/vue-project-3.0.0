package com.rd.ifaes.core.operate.service;

import java.math.BigDecimal;
import java.util.List;

import com.rd.ifaes.core.base.service.BaseService;
import com.rd.ifaes.core.operate.domain.RedenvelopeRule;
import com.rd.ifaes.core.operate.domain.RedenvelopeRuleDetail;
import com.rd.ifaes.core.operate.domain.UserActivityAwardLog;
import com.rd.ifaes.core.operate.domain.UserRedenvelope;
import com.rd.ifaes.core.operate.model.RedenvelopeRuleModel;
import com.rd.ifaes.core.project.domain.ProjectInvest;
import com.rd.ifaes.core.user.domain.User;

/**
 * Service Interface:RedenvelopeRuleService
 * @author 
 * @version 3.0
 * @date 2016-7-22
 */
public interface RedenvelopeRuleService extends BaseService<RedenvelopeRule>{
	/**
	 *  根据红包规则发放红包 
	 * @author ywt
	 * @date 2016-11-05
	 * @param awardUser 发放对象
	 * @param redenvelopeRule 发放规则
	 * @param projectInvest  投资记录 
	 * @param inviter 邀请人
	 * @return 返回发放成功的红包金额列表
	 */
	List<BigDecimal>  grantRedenvelope(User awardUser,RedenvelopeRule redenvelopeRule,ProjectInvest projectInvest,User inviter
			,final List<UserRedenvelope> urvs,final List<UserActivityAwardLog> logs);
	/**
	 * 保存红包规则
	 * @author wyw
	 * @param model 红包规则
	 * @param detaillist 规则明细
	 * @return 是否保存成功
	 */
	Boolean saveRule(RedenvelopeRuleModel model, List<RedenvelopeRuleDetail> detaillist);
	/**
	 *  修改红包规则
	 * @author wyw
	 * @param model 红包规则
	 * @param detaillist 规则明细
	 * @return 是否修改成功
	 */
    Boolean updateRule(RedenvelopeRuleModel model,List<RedenvelopeRuleDetail> detaillist);
	/**
	 * 启用或则禁用规则
	 * @author wyw
	 * @date 2016-7-28
	 * @param ratecouponRule
	 * @return
	 */
	int updateStatus(RedenvelopeRule redenvelopeRule);
	/**
	 * 手动发放红包
	 * @author wyw
	 * @date 2016-8-28
	 * @param userIds
	 * @param redenvelopeRuleId
	 * @param type 1正常发放页面  2增加规则时候的发放
	 */
	void grantSelectUserRed(String[] userIds,String redenvelopeRuleId,int type);
	
	/**
	 * 查询可发放的红包（选择用户发放红包时调用）
	 * @author ywt
	 * @param redenvelopeRule
	 */
	List<RedenvelopeRule> findListForGrant(RedenvelopeRule redenvelopeRule);
	
	/**
	 * 查询可用发放条件的红包规则
	 * @author ywt
	 * @date 2016-11-05
	 * @param activityCode
	 * @return
	 */
	List<RedenvelopeRule> findByActivityCode(String activityCode);
	
	/**
	 * 根据URL获取自定义活动的红包Id
	 * @author ywt
	 * @date 2016-10-29
	 * @param url
	 * @return
	 */
	String findRedenvelopeRuleIdByUrl(String url);
	
	/**
	 * 批量删除
	 * @author ywt
	 * @date 2016-10-29
	 * @param url
	 * @return
	 */
	void checkAndDeleteBatch(String[] ids);
	
	 /**
	   * 根据项目类型ID查找唯一可用的红包规则
	   * @author ywt
	   * @date 2016-11-05
	   * @param ProjectTypeId
	   * @return
	   */
	RedenvelopeRule findInvestRedenvelopeRuleByProjectTypeId(String ProjectTypeId);
	
	/**
	 * 校验发放类型
	 * @author fxl
	 * @date 2017年1月4日
	 * @param projectTypeId
	 * @return
	 */
	boolean checkGrantProjectType(String projectTypeId);
}