package com.rd.ifaes.core.project.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.rd.ifaes.common.orm.Page;
import com.rd.ifaes.core.base.service.BaseService;
import com.rd.ifaes.core.project.domain.ProjectCollection;
import com.rd.ifaes.core.project.model.PeriodCollectionModel;

/**
 * Service Interface:ProjectCollectionService
 * @author FangJun
 * @version 3.0
 * @date 2016-7-13
 */
public interface ProjectCollectionService extends BaseService<ProjectCollection>{
	
	/**
	 * 获取未还的加息利息之和
	 * @author QianPengZhan
	 * @date 2016年12月20日
	 * @param investId
	 * @return
	 */
	BigDecimal getNoRepayRaiseInterest(final  String investId);
	
	/**
	 * 批量更新待收逾期利息
	 * @author QianPengZhan
	 * @date 2016年11月25日
	 * @param list
	 */
	void updateBatchLateInterest(final List<ProjectCollection> list);
	
	/**
	 * 根据用户和项目投资ID获取待收列表
	 * @author QianPengZhan
	 * @date 2016年8月6日
	 * @param pcModel
	 * @return
	 */
	List<ProjectCollection> findPcByPIdAndUserId(ProjectCollection pcModel);

	/**
	 * 根据userId查看一年12月的所有代收记录
	 * @param userId
	 * @return
	 */
	Object[] getYearCollectDataByUserId(String userId,String status);
	/**
	 * 取得一年的月份集合
	 * @return
	 */
	String[] getYearCollectMonths();
	/**
	 * 取得每期待收的汇总信息
	 * @param projectId
	 * @return
	 */
	List<PeriodCollectionModel> findPeriodCollectionList(String projectId);
	
	/**
	 * 根据项目ID和期数查询未还待收列表
	 * @author fxl
	 * @date 2016年8月1日
	 * @param projectId 项目UUID
	 * @param period 期数
	 * @param investId 投资记录UUID
	 * @return
	 */
	List<ProjectCollection> findByProjectIdAndPeriod(String projectId,Integer period,String investId);
	
	
	/**
	 * 根据投标ID查询未还待收列表
	 * @author fxl
	 * @date 2016年8月11日
	 * @param investId
	 * @return
	 */
	List<ProjectCollection> findByInvestId(String investId,String status);
	
	/**
	 * 根据投资ID获取距离最后还款所剩的天数
	 * @author fxl
	 * @date 2016年8月2日
	 * @return
	 */
	int getReypayDays(String investId);
	
	
	/**
	 * 根据投资ID获取最后还款时间
	 * @author fxl
	 * @date 2016年8月9日
	 * @param investId
	 * @return
	 */
	Date getlastRepayTime(String investId);
	
	
	/**
	 * 根据投资ID和期数查询单笔对应的待收记录
	 * @author QianPengZhan
	 * @date 2016年8月11日
	 * @param investId
	 * @param period
	 * @return
	 */
	ProjectCollection getProjectCollectionByInvestIdAndPeriod(String investId,int period);
	
	/**
	  *  根据投资记录和状态查询该笔投资对应的待收的已还和未还期数
	  * @author QianPengZhan
	  * @date 2016年8月15日
	  * @param investId
	  * @param status
	  * @return
	  */
	  int getProjectPeriodByInvestId(String investId,String status);
	  
	  
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
	BigDecimal getRepayedAccountTotal(String projectId, String status,String investId,String[] collectionTypeSet);
	
	/**
	 * 
	 * 更新逾期相关信息
	 * @author wyw
	 * @date 2016-8-25
	 * @param projectId
	 * @param period
	 * @param lateDays
	 * @param lateRate
	 */
	int updateOverdueInfo(String projectId, Integer period, Integer lateDays, BigDecimal lateRate);
	
	
	/**
	 * 待收分页数据
	 * @author fxl
	 * @date 2016年9月27日
	 * @param model
	 * @return
	 */
	Page<ProjectCollection> findForPage(ProjectCollection model);
	
	/**
	 * 查询已还
	 * @param  
	 * @return BigDecimal
	 * @author xxb
	 * @date 2016年11月10日
	 */
	BigDecimal getRepayedAmount(@Param("userId") String userId, @Param("projectId") String projectId);
	
	/**
	 * 查询债权转让最后还款日期
	 * @param bondInvestId
	 * @return
	 */
	Date getLastRepayTimeByBondInvestId(String bondInvestId);

	/**
	 * 获得坏账代收信息
	 * @return
	 */
	List<ProjectCollection> findBadDebtCollection();
	/**
	 * 还款回调查询本期需修改资金代收列表
	 * @author ZhangBiao
	 * @date 2017年3月10日
	 * @param projectId
	 * @param period
	 * @param value
	 * @return
	 */
	List<ProjectCollection> findByProjectIdPeriodStatus(String projectId,
			Integer period, String value);
}