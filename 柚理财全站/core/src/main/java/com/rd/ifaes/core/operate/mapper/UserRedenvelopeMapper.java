package com.rd.ifaes.core.operate.mapper;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.rd.ifaes.core.base.mapper.BaseMapper;
import com.rd.ifaes.core.operate.domain.UserRedenvelope;
import com.rd.ifaes.core.statistic.model.StatisticModel;
import com.rd.ifaes.core.statistic.model.StatisticResultModel;

/**
 * Dao Interface:UserRedenvelopeMapper
 * @author 
 * @version 3.0
 * @date 2016-7-22
 */
public interface UserRedenvelopeMapper extends BaseMapper<UserRedenvelope> {
	/**
	 * 批量更新状态
	 * @author QianPengZhan
	 * @date 2017年1月10日
	 * @param list
	 */
	void updateBatchStatus(List<UserRedenvelope> list);
	
	/**
	 * 查询过期加息券
	 * @author QianPengZhan
	 * @date 2017年1月10日
	 * @param preStatus
	 * @return
	 */
	List<String> findExpireList(@Param("startTime") Date startTime, @Param("endTime") Date endTime, @Param("preStatus") String preStatus);
	
	/**
	 *  批量修改指定用户红包状态
	 * @author  FangJun
	 * @date 2016年8月14日
	 * @param uuids 红包ID字符串拼接
	 * @param status  红包当前状态
	 * @param investId  投资记录ID
	 * @param preStatus 红包前一状态
	 * @return  修改记录条数
	 */
	int  updateStatusBatch(@Param("uuids")String uuids, @Param("investId") String investId,
			@Param("useTime") Date useTime,@Param("status")String status,@Param("preStatus")String preStatus);
	
	/**
	 * 修改使用红包的投资ID
	 * @author fxl
	 * @date 2016年8月24日
	 */
	void updateRedenvelopeTenderId(@Param("oldInvestId")String oldInvestId,@Param("newInvestId")String newInvestId);
	
	/**
	 * 
	 * 获取可使用的红包
	 * @author ywt
	 * @date 2016-10-12
	 * @param status
	 * @param preStatus
	 * @return
	 */
	List<UserRedenvelope> viableUserRedenvelope (UserRedenvelope userRedenvelope);
	
	/**
	 * 根据统计日期获取记录
	 * @param model
	 * @return
	 */
	List<StatisticResultModel> findByStatDate(StatisticModel model);
	
	/**
	 * 根据状态获取记录
	 * @param model
	 * @return
	 */
	List<StatisticResultModel> findByStatStatus(StatisticModel model);	
	
	/**
	 * 根据tenderId计数
	 * @param tenderId
	 * @return
	 */
	int getCountByTenderId(String tenderId);
	
	/**
	 * 
	 * 根据投资订单号查找红包
	 * @author jxx
	 * @date 2017年9月20日
	 * @param tenderId
	 * @return
	 */
	UserRedenvelope getByTenderId(String tenderId);
}