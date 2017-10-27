package com.rd.ifaes.core.score.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.rd.ifaes.core.base.mapper.BaseMapper;
import com.rd.ifaes.core.score.domain.ScoreGoodsOrder;

/**
 * Mapper Interface:商品管理Mapper
 * @author ywt
 * @version 3.0
 * @date 2017-03-03
 */
public interface ScoreGoodsOrderMapper extends BaseMapper<ScoreGoodsOrder>{
	
	/**
	 * 获取最新的兑换记录
	 * @return
	 */
	List<ScoreGoodsOrder> getNewOrders();
	
	/**
	 * 通过状态统计数量
	 * @param status
	 * @return
	 */
	Integer countOrdersByStatus(@Param("userId")String userId,@Param("status")String status);
	
	/**
	 * 统计用户兑换某商品的数量
	 * @param status
	 * @return
	 */
	Integer countUserExchangeOrders(@Param("userId")String userId,@Param("goodsId")String goodsId);
	
	/**
	 * 查询除待审核状态之外的订单
	 * @param keywords
	 * @return
	 */
	List<ScoreGoodsOrder> findListExceptWaitVerify(@Param("keywords")String keywords);
}
