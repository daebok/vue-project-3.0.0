package com.rd.ifaes.core.operate.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.rd.ifaes.core.base.mapper.BaseMapper;
import com.rd.ifaes.core.operate.domain.RedenvelopeRule;

/**
 * 
 * 红包规则mapper接口
 * @version 3.0
 * @author wyw
 * @date 2016-7-28
 */
public interface RedenvelopeRuleMapper extends BaseMapper<RedenvelopeRule> {
	 /**
	  * 
	  * 启用或则禁用规则
	  * @author wyw
	  * @date 2016-7-28
	  * @param redenvelopeRule
	  * @return
	  */
	  int updateStatus(RedenvelopeRule redenvelopeRule);
	 /**
	  * 
	  * 更新已经发放的数量
	  * @author wyw
	  * @date 2016-7-28
	  * @param redenvelopeRule
	  * @return
	  */
	  int updateLssueNum(RedenvelopeRule redenvelopeRule);
	  
	  /**
		 * 查询可发放的红包（选择用户发放红包时调用）
		 * @author ywt
		 * @param redenvelopeRule
		*/
	  List<RedenvelopeRule> findListForGrant(RedenvelopeRule redenvelopeRule);
	  
	  /**
	   * 查询可用发放条件的红包规则
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
	  String findRedenvelopeRuleIdByUrl(String grantUrl);
	  
	  
	  /**
	   * 统计要删除的规则中是否有启用的规则
	   * @author ywt
	   * @date 2016-11-2
	   * @param ids
	   * @return
	   */
	  int checkRedenvelopeRuleBeforeDelete(String[] ids);
	  
	  /**
	   * 根据项目类型ID查找可用的红包规则
	   * @author ywt
	   * @date 2016-11-05
	   * @param ProjectTypeId
	   * @return
	   */
	  RedenvelopeRule findInvestRedenvelopeRuleByProjectTypeId(String ProjectTypeId);
	  
	  /**
	   * 根据红包规则id查询该红包的调用
	   * @author ywt
	   * @date 2016-12-05
	   * @param RedenvelopeRuleId 红包规则ID
	   * @param tableName 表名
	   * @return
	   */
	  String[] findRedenvelopeRuleCall(@Param("RedenvelopeRuleId")String RedenvelopeRuleId,@Param("tableName")String tableName);
	  
	/**
	 * 统计发放类型
	 * @author fxl
	 * @date 2017年1月4日
	 * @param projectTypeId
	 * @return
	 */
	int countGrantProjectType(@Param("projectTypeId")String projectTypeId);

}