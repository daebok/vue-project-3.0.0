package com.rd.ifaes.core.bond.mapper;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.rd.ifaes.core.base.mapper.BaseMapper;
import com.rd.ifaes.core.bond.domain.Bond;
import com.rd.ifaes.core.bond.model.BondModel;

/**
 * Dao Interface:BondMapper
 * @author FangJun
 * @version 3.0
 * @date 2016-7-13
 */
public interface BondMapper extends BaseMapper<Bond> {
	/**
	 * 查询当前的原始标投资记录 对应的正在转让中或者转让成功的债权标的个数
	 * @author QianPengZhan
	 * @date 2016年11月29日
	 * @param uuid
	 * @return
	 */
	int	getBondCountByProjectInvestId(@Param("uuid")final String uuid);
	
	/**
	 * 修改债权转让投资阶段 
	 * @author QianPengZhan
	 * @date 2016年10月27日
	 */
	void updateStage(@Param("stage") final Integer stage,@Param("uuid") final String uuid);
	/**
	 * 递增修改持有利息和手续费
	 * @author QianPengZhan
	 * @date 2016年8月31日
	 * @param soldInterest
	 * @param bondFee
	 * @param uuid
	 */
	void  updateBondOtherInfo(@Param("soldInterest") final BigDecimal soldInterest,
			@Param("bondFee") final BigDecimal bondFee,@Param("successTime") final Date successTime,
			@Param("uuid") final String uuid);
	
	/**
	 * 根据旧状态修改新状泰
	 * @author QianPengZhan
	 * @date 2016年8月31日
	 * @param newStatus
	 * @param oldStatus
	 * @param uuid
	 */
	void updateBondStatusById(@Param("newStatus") final String newStatus,
			@Param("oldStatus") final String oldStatus,@Param("uuid") final String uuid);
	
	/**
	 * 递增修改已售金额
	 * @author QianPengZhan
	 * @date 2016年8月31日
	 * @param money
	 * @param uuid
	 */
	void updateSoldCapitalById(@Param("money")final  double money,@Param("uuid")final String uuid);
	
	/**
	 * 根据编号查询对象
	 * @author QianPengZhan
	 * @date 2016年8月28日
	 * @param bondNo
	 * @return
	 */
	Bond  getBondByBondNo(final String bondNo);
	
	/**
	 * 项目编号是否存在
	 * @param projectNo
	 * @return
	 */
	int bondNoExists(String projectNo);
	
	/**
	 * 取当日最大编号
	 * @param createDate yyyy-MM-dd
	 * @return
	 */
	String getMaxBondNo(@Param("startDate") String startDate,@Param("endDate") String endDate);
	
	/**
	 * 查詢分頁的債權列表
	 * @author QianPengZhan
	 * @date 2016年8月3日
	 * @param model
	 * @return
	 */
	List<BondModel> findModelList(BondModel model);
	
	/**
	 * 后台转让记录查询扩展属性的分页数据
	 * @author QianPengZhan
	 * @date 2016年7月28日
	 * @param model
	 * @return
	 */
	List<BondModel> findManagePage(BondModel bond);
	
	/**
	 * 批量修改状态
	 * @author QianPengZhan
	 * @date 2016年8月18日
	 * @param list
	 */
	int updateStatusBatch(List<Bond> list);
	
	
	/**
	 * 批量修改剩余期限
	 * @author QianPengZhan
	 * @date 2016年8月18日
	 * @param list
	 */
	int updateRemainDaysBatch(List<Bond> list);

	/**
	 * 根据项目id获取不同状态债权个数
	 * @author ZhangBiao
	 * @date 2016年10月17日
	 * @param projectId
	 * @param status
	 * @return
	 */
	int getCountByStatus(@Param("projectId")String projectId,@Param("status") String status);
}