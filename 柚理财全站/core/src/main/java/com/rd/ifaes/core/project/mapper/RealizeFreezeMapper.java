package com.rd.ifaes.core.project.mapper;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.rd.ifaes.core.base.mapper.BaseMapper;
import com.rd.ifaes.core.project.domain.RealizeFreeze;

/**
 * 变现冻结信息
 * @version 3.0
 * @author fxl
 * @date 2016年8月1日
 */
public interface RealizeFreezeMapper extends BaseMapper<RealizeFreeze> {

	/**
	 * 统计冻结金额总和
	 * @author fxl
	 * @date 2016年11月22日
	 * @param userId
	 * @param collectionId
	 * @param freezeType
	 * @return
	 */
	BigDecimal getSumFreezeMoney(@Param("userId")String userId,@Param("collectionId")String collectionId,@Param("freezeType")String freezeType);
	
	/**
	 * 根据待收ID查询待冻结列表
	 * @author fxl
	 * @date 2016年12月5日
	 * @param collectionId
	 * @return
	 */
	List<RealizeFreeze> getFreezeListByCollection(@Param("collectionId")String collectionId);
	
	/**
	 * 更新冻结信息
	 * @author fxl
	 * @date 2016年11月23日
	 * @param collectionId
	 * @param realizeId
	 * @param freezeType
	 */
	void updateFreeze(@Param("collectionId") String collectionId, @Param("realizeId") String realizeId,
			@Param("freezeType") String freezeType, @Param("freezeNo") String freezeNo, @Param("orderNo") String orderNo);
	
	/**
	 * 撤销冻结
	 * @author fxl
	 * @date 2016年11月23日
	 * @param realizeId
	 */
	void cancelFreeze(@Param("realizeId")String realizeId);

	/**
	 * 根据变现ID和状态获取冻结列表
	 * @author fxl
	 * @date 2016年11月23日
	 * @param realizeId
	 * @return
	 */
	List<RealizeFreeze> getFreezedListByRealizeId(@Param("realizeId")String realizeId,@Param("status")String status);
}