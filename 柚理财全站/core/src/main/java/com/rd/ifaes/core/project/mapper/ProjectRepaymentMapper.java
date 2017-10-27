package com.rd.ifaes.core.project.mapper;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.rd.ifaes.core.base.mapper.BaseMapper;
import com.rd.ifaes.core.project.domain.ProjectRepayment;

/**
 * Dao Interface:ProjectRepaymentMapper
 * @author FangJun
 * @version 3.0
 * @date 2016-7-13
 */
public interface ProjectRepaymentMapper extends BaseMapper<ProjectRepayment> {
	
	/**
	 * 批量更新待收逾期利息
	 * @author QianPengZhan
	 * @date 2016年11月25日
	 * @param list
	 */
	void updateBatchLateInterest(final List<ProjectRepayment> list);
	/**
	 * 根据项目ID和状态查询还款列表的总期数 未还期数 已还期数
	 * @author QianPengZhan
	 * @date 2016年8月31日
	 * @param projectId
	 * @param status
	 * @return
	 */
	int getRepayPeriodsByProjectIdAndStatus(@Param("projectId") final String projectId,@Param("status") final String status);


	/**
	 * 待还款总额
	 * @param userId
	 * @return
	 */
	BigDecimal getWaitRepayAccountTotal(String userId);

	/**
	 * 根据userId查询下一条还款记录
	 * @param userId
	 * @return
	 */
	ProjectRepayment getNextRepayByUserId(String userId);
	
	/**
	 * 下一次还款金额
	 * @return
	 */
	BigDecimal getNextRepayAccountByTime(ProjectRepayment projectRepayment);
	
	/**
	 *   根据项目ID找到此项目
	 * @author QianPengZhan
	 * @date 2016年7月28日
	 * @param projectId 项目ID
	 * @return
	 */
	ProjectRepayment getRepayByProject(String projectId);
	
	/**
	 * 根据项目ID和期数找到项目
	 * @author fxl
	 * @date 2016年8月3日
	 * @param projectId
	 * @param period
	 * @return
	 */
	ProjectRepayment findRepayByProjectIdAndPeriod(@Param("projectId") String projectId,@Param("period") Integer period,@Param("status") String status);
	
	/**
	 *  指定还款记录前，该项目的未还款记录数
	 * @author  FangJun
	 * @date 2016年8月2日
	 * @param repayment 指定还款记录
	 * @return 未还款记录数
	 */
    int   countUnpayRecord(ProjectRepayment repayment);
    /**
     * 更新指定还款记录状态
     * @author  FangJun
     * @date 2016年8月2日
     * @param uuid 记录ID
     * @param status 状态
     * @param preStatus 上一状态
     * @return 更新条数  1 成功 0 未成功
     */
    int updateStatus(@Param("uuid") String uuid, @Param("status") String status, @Param("preStatus") String preStatus);
    
	/**
	 * 
	 * 根据日期查询记记录列表
	 * @author xhf
	 * @date 2016年8月2日
	 * @return
	 */
	List<ProjectRepayment> findByDate(ProjectRepayment model);
	
	/**
	 * 
	 * 根据projectId和userId查询记录列表
	 * @author xhf
	 * @date 2016年8月2日
	 * @param model
	 * @return
	 */
	List<ProjectRepayment> findByProjectId(ProjectRepayment model);
	
	/**
	 * 
	 * 更新逾期信息
	 * @author wyw
	 * @date 2016-8-25
	 * @param model
	 * @return
	 */
	int updateOverdueInfo(ProjectRepayment model);
	   /**
     *  最后还款日（项目详情页使用）
     * @author  FangJun
     * @date 2016年9月12日
     * @param projectId
     * @return
     */
    Date getLastRepayDay( final String projectId);
    /**
	 * 统计借款人逾期项目个数
	 * @author fxl
	 * @date 2016年10月25日
	 * @param userId
	 * @return
	 */
	int countOverDueByUser(String userId);
    /**
     * 前台逾期列表
     * @param model
     * @return
     */
    List<ProjectRepayment> findOverdueList(ProjectRepayment model);
    /**
     * 查询单日需要自动还款的待还记录
     * @author  FangJun
     * @date 2016年10月24日
     * @return
     */
   List<ProjectRepayment> findTodayRepay();
   /**
    * 查询指定项目的下一个还款时间
    * @param model 当前还款的记录信息（项目UUID、还款日期）
    * @return 指定项目的下一个还款时间
    */
   Date findNextRepayTime(ProjectRepayment model);
   
   /**
    * 后台垫付数据
    * @author ywt
    * @date 2016-12-16
    * @param ProjectRepayment model
    */
   List<ProjectRepayment> findAdvance(ProjectRepayment model);
   
   /**
    *更新还款类型 
    * @author fxl
    * @date 2016年12月20日
    * @param uuid
    *  @param repayType 修改后的还款类型 
    * @param preRepayType 修正前的还款类型 
    */
   int updateRepayType(@Param("uuid") String uuid,@Param("repayType") String repayType, @Param("preRepayType") String preRepayType);
   
   /**
    * 查询待还总额
    * @return
    */
   BigDecimal getTotalCollection() ;
   
   /**
    * 根据日期查询待还总额
    * @param startDate
    * @param endDate
    * @return
    */
   BigDecimal sumTotalCollectionByDate(@Param("startDate") String startDate, @Param("endDate") String endDate) ;
}