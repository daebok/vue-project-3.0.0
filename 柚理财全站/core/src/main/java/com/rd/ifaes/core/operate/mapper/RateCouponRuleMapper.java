package com.rd.ifaes.core.operate.mapper;

import com.rd.ifaes.core.operate.domain.RateCouponRule;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.rd.ifaes.core.base.mapper.BaseMapper;

/**
 * 
 * 加息券规则mapper接口
 * @version 3.0
 * @author wyw
 * @date 2016-7-28
 */
public interface RateCouponRuleMapper extends BaseMapper<RateCouponRule> {
 /**
  * 
  * 启用或则禁用规则
  * @author wyw
  * @date 2016-7-28
  * @param ratecouponRule
  */
  int updateStatus(RateCouponRule ratecouponRule);
  /**
   * 
   * 更新已经发放的数量
   * @author wyw
   * @date 2016-7-28
   * @param ratecouponRule
   */
   int updateLssueNum(RateCouponRule ratecouponRule);
   
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
	 * 根据url获取自定义发放的加息券规则
	 * @param activityCode
	 * @return
	 */
	String findRateCouponRuleIdByUrl(String grantUrl);
	
	/**
	 * 统计要删除的规则中是否有启用的规则
	 * @author ywt
	 * @date 2016-11-2
	 * @param ids
	 * @return
	 */
	 int checkRateCouponRuleBeforeDelete(String[] ids);
	 
	 /**
	   * 根据项目类型ID查找可用的加息券规则
	   * @author ywt
	   * @date 2016-11-05
	   * @param ProjectTypeId
	   * @return
	   */
	 RateCouponRule  findInvestRateCouponRuleByProjectTypeId(String projectTypeId);
	
	/**
	   * 根据加息券规则id查询该加息券的调用
	   * @author ywt
	   * @date 2016-12-05
	   * @param rateCouponRuleId 红包规则ID
	   * @param tableName 表名
	   * @return
	   */
	  String[] findRateCouponRuleCall(@Param("rateCouponRuleId")String rateCouponRuleId,@Param("tableName")String tableName);
	  
	/**
	 * 统计发放类型
	 * @author fxl
	 * @date 2017年1月4日
	 * @param projectTypeId
	 * @return
	 */
	int countGrantProjectType(@Param("projectTypeId")String projectTypeId);

}