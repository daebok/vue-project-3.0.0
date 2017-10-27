package com.rd.ifaes.core.project.mapper;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.rd.ifaes.core.base.mapper.BaseMapper;
import com.rd.ifaes.core.project.domain.ProjectCollection;
import com.rd.ifaes.core.project.model.MonthCollection;
import com.rd.ifaes.core.project.model.PeriodCollectionModel;

/**
 * Dao Interface:ProjectCollectionMapper
 * @author FangJun
 * @version 3.0
 * @date 2016-7-13
 */
public interface ProjectCollectionMapper extends BaseMapper<ProjectCollection> {
	
	/**
	 * 获取未还的加息利息之和
	 * @author QianPengZhan
	 * @date 2016年12月20日
	 * @param investId
	 * @return
	 */
	BigDecimal getNoRepayRaiseInterest(@Param("investId")final  String investId);
	
	/**
	 * 批量更新待收逾期利息
	 * @author QianPengZhan
	 * @date 2016年11月25日
	 * @param list
	 */
	void updateBatchLateInterest(final List<ProjectCollection> list);

	/**
	 * 根据月份查询待还金额
	 * @param projectCollection
	 * @return
	 */
	BigDecimal getRepayAmountByMonth(ProjectCollection projectCollection);
	
	
	/**
	 * findPcByPIdAndUserId:(根据项目ID查询单个用户的待收信息). <br/> 
	 * @author QianPengZhan
	 * @param projectId
	 * @param userId
	 * @return
	 */
	List<ProjectCollection> findPcByPIdAndUserId(ProjectCollection projectCollection);
	
	/**
	 * 根据项目ID和期数查询未还待收列表
	 * @author fxl
	 * @date 2016年8月1日
	 * @param projectId
	 * @param period
	 * @return
	 */
	List<ProjectCollection> findByProjectIdAndPeriod(@Param("projectId") String projectId,
			@Param("period") Integer period,@Param("investId") String investId,@Param("status") String status);
	
	/**
	 * findByInvestId:(根据投资ID查询项目待收). <br/> 
	 * @author QianPengZhan
	 * @param uuid
	 * @return
	 */
	List<ProjectCollection> findByInvestId(@Param("investId") String investId,@Param("status") String status);
	
	/**
	 * 查询待收分页
	 * @author fxl
	 * @date 2016年9月27日
	 * @param model
	 * @return
	 */
	List<ProjectCollection> findForPage(ProjectCollection model);
	/**
	 * 取得每期待收的汇总信息
	 * @param projectId
	 * @return
	 */
	List<PeriodCollectionModel> findPeriodCollectionList(String projectId);
	
	/**
	 * 根据投资ID获取最后一期待收
	 * @author fxl
	 * @date 2016年8月2日
	 * @param investId
	 * @return
	 */
	ProjectCollection getLastCollectionByInvestId(String investId);
	
	
	/**
	 * 根据投资ID和期数查询单笔对应的待收记录
	 * @author QianPengZhan
	 * @date 2016年8月11日
	 * @param investId
	 * @param period
	 * @return
	 */
	ProjectCollection getProjectCollectionByInvestIdAndPeriod(@Param("investId") String investId,@Param("period") int period);
	
	
	 /**
	  *  根据投资记录和状态查询该笔投资对应的待收的已还和未还期数
	  * @author QianPengZhan
	  * @date 2016年8月15日
	  * @param investId
	  * @param status
	  * @return
	  */
	  int getProjectPeriodByInvestId(@Param("investId") String investId,@Param("status") String status);
	  
	/**
	 * 根据投资ID和
	 * @author QianPengZhan
	 * @date 2016年8月24日
	 * @param projectId
	 * @param status
	 * @param investId
	 * @param collectionTypeSet
	 * @return
	 */
	BigDecimal getRepayedAccountTotal(@Param("projectId") String projectId,@Param("status") String status
			,@Param("investId") String investId,@Param("collectionTypeSet") String[] collectionTypeSet);
	
	/**
	 * 
	 * 更新代收逾期信息
	 * @author wyw
	 * @date 2016-8-25
	 * @param projectId
	 * @param period
	 * @param lateDays
	 * @param lateRate
	 * @return
	 */
	int  updateOverdueInfo(@Param("projectId") String projectId,@Param("period") Integer period
			,@Param("lateDays") int lateDays,@Param("lateRate") BigDecimal lateRate,@Param("collectionTypeSet") String[] collectionTypeSet);
	
	
	/**
	 * 按月统计一定时间段内project的回款金额
	 * @param model
	 * @return
	 */
	List<MonthCollection> findMonthRepayByTime(ProjectCollection model);
	
	/**
	 * 按月统计一定时间段内bond的回款金额
	 * @param model
	 * @return
	 */
	List<MonthCollection> findBondMonthRepayByTime(ProjectCollection model);
	
	
	BigDecimal getRepayedAmount(@Param("userId") String userId, @Param("projectId") String projectId);
	
	/**
	 * 查询债权转让最后还款日期
	 * @param bondInvestId
	 * @return
	 */
	Date getLastRepayTimeByBondInvestId(String bondInvestId);
	
	/**
	 * 根据项目ID和期数查询未还待收列表
	 * @author fxl
	 * @date 2016年8月1日
	 * @param projectId
	 * @param period
	 * @return
	 */
	List<ProjectCollection> findBadDebtCollection(@Param("projectStatus") String projectStatus, @Param("collectionStatus") String collectionStatus);
}