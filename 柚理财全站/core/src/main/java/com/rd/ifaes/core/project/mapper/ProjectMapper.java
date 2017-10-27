package com.rd.ifaes.core.project.mapper;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.rd.ifaes.core.base.mapper.BaseMapper;
import com.rd.ifaes.core.home.model.SureProfitModel;
import com.rd.ifaes.core.project.domain.Project;
import com.rd.ifaes.core.project.model.ProjectRecord;

/**
 * Dao Interface:ProjectMapper
 * @author FangJun
 * @version 3.0
 * @date 2016-7-13
 */
public interface ProjectMapper extends BaseMapper<Project> {
	/**
	 * 查询还款中的项目它的成立审核时间在一定范围内的标
	 * @author QianPengZhan
	 * @date 2016年11月4日
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	List<Project> findListByReviewTime(@Param("startTime") final Date startTime,@Param("endTime") final Date endTime);
	/**
	 * 根据riskLevelVal 查询产品的个数
	 * @author QianPengZhan
	 * @date 2016年10月21日
	 * @param riskLevelVal
	 * @return
	 */
	int getProjectCount(@Param("riskLevelVal") final String riskLevelVal);
	
	/**
	 * 取当日最大编号
	 * @param createDate yyyy-MM-dd
	 * @return
	 */
	String getMaxProjectNo(@Param("startDate") String startDate,@Param("endDate") String endDate);
	
	/**
	 * 不可编辑数量
	 * @param ids
	 * @return
	 */
	int getNotEditableCount(@Param("uuids") String[] uuids,@Param("statusArray") String[] statusArray);
	
	/**
	 * 更新状态
	 * @param uuid 记录ID
     * @param status 状态
     * @param preStatus 上一状态
     * @return 更新条数  1 成功 0 未成功
	 */
	int updateStatus(@Param("uuid") String uuid, @Param("status") String status, @Param("preStatus") String preStatus);
	/**
	 * 根据项目编号
	 * @param projectNo 项目编号
	 * @return 指定编号的项目
	 */
	public  Project  getProjectByNo(String projectNo);
	
   /**
    *  更新已投金额
    * @param amount 新增投资金额
    * @param projectId 项目编号
    * @return 成功 1 失败0
    */
  int  updateAccountYes(@Param("amount") double amount, @Param("projectId") String projectId);
	 /**
	  * 查询项目
	  * @param project
	  * @return
	  */
	 List<Project> findProject(Project project);
	 /**
	  * 审核项目
	  */
	 int updateVouchStatus(Project project);
	 
	  /**
	   * 根据userId查询借款总额
	   * @param userId
	   * @return
	   */
	  BigDecimal getAccountTotalByUserId(@Param("userId") String userId, @Param("projectStatus") String[] projectStatus);
	  
	  /**
	   * 查询项目的产品详情或借款详情
	   * @param projectId  项目UUID
	   * @param borrowFlag  借款标识
	   * @return 产品详情或借款详情
	   */
	  Map<String,Object> getContent(@Param("projectId") String projectId,@Param("borrowFlag") String borrowFlag);
	  
	  /**
	   * 
	   * 根据日期状态查询列表
	   * @author xhf
	   * @date 2016年8月2日
	   * @param project
	   * @return
	   */
	  List<Project> findByDate(Project project);
	  /**
	   * 
	   * 计算担保中总额  查询未还款 并且审核通过的标的总额
	   * @author wyw
	   * @date 2016-8-8
	   * @param isVouch  是否为担保项目  1：（是）
	   * @param repayStatus 还款状态：0（未还）
	   * @param vouchStatus 项目担保审核状态  1（审核同意）
	   * @param vouchId 担保用户id
	   * @return 统计的金额
	   */
	  BigDecimal vouchAccount(@Param("isVouch") String isVouch,@Param("repayType") String repayType,
		  				@Param("vouchStatus") String vouchStatus,@Param("vouchId") String vouchId);
	  /**
	   * 
	   * 待审核项目 /已经审核项目个数
	   * @author wyw
	   * @date 2016-8-8  
	   * @param isVouch  是否为担保项目  1：（是） 0（否）
	   * @param vouchId 担保用户id
	   * @param vouchStatus 项目担保审核状态  0（待审核） 1 （审核同意） 2（审核失败）
	   * @return
	   */
	  BigDecimal toAuditCount(@Param("isVouch") String isVouch,@Param("vouchId") String vouchId,@Param("vouchStatus") String vouchStatus);
	  /**
	   * 
	   * 计算担保中项目 个数 -审核已经通过 并且未还款项目
	   * @author wyw
	   * @date 2016-8-8
	   * @param isVouch 是否为担保项目  1：（是） 0（否）
	   * @param vouchStatus 项目担保审核状态  0（待审核） 1 （审核同意） 2（审核失败）
	   * @param vouchId 担保用户id
	   * @param projectStatus  (4,5,6,8,9) 
	   * @return 
	   */
	  BigDecimal auditingCount(@Param("isVouch") String isVouch,@Param("vouchStatus") String vouchStatus,@Param("vouchId") String vouchId
		  					,@Param("projectStatus") String[] projectStatus);
	  /**
	   * 
	   * 计算待垫付项目项目  -审核已经通过 并且逾期项目
	   * @author wyw
	   * @date 2016-8-8
	   * @param isVouch 是否为担保项目  1：（是）
	   * @param repayStatus  0 未还
	   * @param vouchStatus 1 （审核同意）
	   * @param vouchId  担保用户id
	   * @return
	   */
	  BigDecimal  toAdvanceCount(@Param("isVouch") String isVouch,@Param("repayStatus") String repayStatus
						   ,@Param("vouchStatus") String vouchStatus,@Param("vouchId") String vouchId);
	  /**
	   * 
	   * 借款金额
	   * @author wyw
	   * @date 2016-8-10
	   * @param projectStatus
	   * @return
	   */
	  BigDecimal sumProjectAccount(@Param("projectStatus") String[] projectStatus);
	  
	/**
	 * 根据状态成立审核时间获取借款金额
	 * 
	 * @author ZhangBiao
	 * @date 2016年8月18日
	 * @param status
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	  BigDecimal sumProjectAccountByDate(@Param("projectStatus") String[] projectStatus,
			@Param("startDate") String startDate, @Param("endDate") String endDate);

	/**
	 * 根据状态开售时间获取产品列表
	 * @author ZhangBiao
	 * @date 2016年8月18日
	 * @param status
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	List<Project> findListByStatusTime(@Param("status") String status, @Param("startDate") Date startDate,
			@Param("endDate") Date endDate);
	  
	/**
	 *  产品列表查询
	 * @author  FangJun
	 * @date 2016年8月20日
	 * @param entity 产品查询条件
	 * @return  ProjectRecord分页列表数据
	 */ 
	  List<ProjectRecord> findProjectPage(ProjectRecord entity) ;	
	  
	  
	/**
	 * 查询募集到期的变现的项目
	 * @author fxl
	 * @date 2016年8月24日
	 * @param endTime
	 * @return
	 */
	List<Project> findExpireRealize(@Param("endTime") Date endTime);
	
	/**
	 * 返回待自动下架产品的uuid
	 * @return
	 */
	List<String> findAutoStopSaleUuids();
	
	/**
	 *  自动下架使用（修改项目状态为募集结束，已投为0改为未成立）
	 * @author  FangJun
	 * @date 2016年8月31日
	 * @return 修改条数
	 */
	int autoStopSale(final String[] ids);
	/**
	 * 
	 * 根据分类获取最小利率
	 * @author wyw
	 * @date 2016-9-8
	 * @param project
	 * @return
	 */
	BigDecimal getMinAprByProjectType(Project project);
	/**
	 * 
	 * 根据分类获取最大利率
	 * @author wyw
	 * @date 2016-9-8
	 * @param project
	 * @return
	 */
	BigDecimal getMaxAprByProjectType(Project project);
	
	/**
	 * 
	 * 查询首页项目
	 * @author wyw
	 * @date 2016-9-8
	 * @param project
	 * @return
	 */
	List<Project> findWebProjectList(Project project);
	
	/**
	 * 取得已上架待展示的项目数量
	 * @param delaySeconds
	 * @return
	 */
	int getShowingCount(@Param("delaySeconds") int delaySeconds);
	
	/**
	 * 获取担保项目个数
	 * @author fxl
	 * @date 2016年9月22日
	 * @param isVouch
	 * @param vouchId
	 * @param vouchStatus
	 * @param statusSet
	 * @return
	 */
	Integer countVouchProject(@Param("isVouch") String isVouch, @Param("vouchId") String vouchId,
			@Param("vouchStatus") String vouchStatus, @Param("statusSet") String[] statusSet);
	
	/**
	 * 根据状态获取对应数据
	 * 
	 * @author ZhangBiao
	 * @date 2016年9月29日
	 * @param status
	 * @return
	 */
	List<Project> findListByStatus(String status);
	/**
	 *  查询指定项目的预约信息（预约人数，指定用户是否预约）
	 * @author  FangJun
	 * @date 2016年10月11日
	 * @param projectId 项目ID
	 * @param userId 用户ID
	 * @return  预约信息
	 */
	Map<String,Object> getBespeakInfo(@Param("projectId")String projectId,@Param("userId")String userId);
	
	/**
	 * 项目详情（含项目类别名称）
	 * @author  FangJun
	 * @date 2016年10月18日
	 * @param uuid
	 * @return
	 */
	Project getDetail(String uuid);
	
	/**
	 * 查找可转让项目
	 * @param projectIds
	 * @return
	 */
	List<String> findBondUsefulByProjectIds(String projectIds);
	
	/**
	 * 查找可变现项目
	 * @param projectIds
	 * @return
	 */
	List<String> findRealizeUsefulByProjectIds(String projectIds);
	
	/**
	 * 更新项目投资阶段
	 * @param stage		项目阶段
	 * @param projectNo	项目编号	
	 * @return
	 */
	int updateStage(@Param("stage")int stage,@Param("projectNo")String projectNo);
	
	
	/**
	 * 产品下架后移除投资预约信息
	 * @author ywt
	 * @date 2016-10-25
	 * @param projectNo	项目编号	
	 * @return
	 */
	void removeInvestBespeak(String projectNo);
	
	/**
	 * 项目编号是否存在
	 * @param projectNo
	 * @return
	 */
	int projectNoExists(String projectNo);

	/**
	 * 根据用户id获取借款个数
	 * @author ZhangBiao
	 * @date 2016年10月27日
	 * @param userId
	 * @return
	 */
	int getProjectNumByUserId(String userId);
	/**
	 * 首页稳赚系列查询数据
	 * @author  FangJun
	 * @date 2016年11月9日
	 * @return 项目分类、年利率信息
	 */
	 List<SureProfitModel> getSureProfit();
	 
	 /**
	  * 担保用户统计逾期的项目数
	  */
	 int countOverdueProjectNum(String vouchId);
	 
	 /**
	  * 统计状态数
	  * @param borrowFlag
	  * @param statusSet
	  * @return
	  */
	 List<Project> getCountByStatus(@Param("borrowFlag") String borrowFlag, @Param("statusSet") String[] statusSet);
}