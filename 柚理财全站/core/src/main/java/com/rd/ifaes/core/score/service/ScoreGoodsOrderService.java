package com.rd.ifaes.core.score.service;

import java.util.List;


import com.rd.ifaes.core.base.service.BaseService;
import com.rd.ifaes.core.score.domain.ScoreGoodsOrder;

public interface ScoreGoodsOrderService extends BaseService<ScoreGoodsOrder>{
	/**
	 * 获取最新的兑换记录
	 * @return
	 */
	List<ScoreGoodsOrder> getNewOrders();
	
	 /**
	  * 兑换商品
	  * @param scoreGoodsOrder
	  */
	void exchangeGoods(ScoreGoodsOrder scoreGoodsOrder);
	
	/**
	 * 通过状态统计数量
	 * @param status
	 * @return
	 */
	int countOrdersByStatus(String userId,String status);
	
	/**
	 * 统计用户兑换某商品的数量
	 * @param status
	 * @return
	 */
	int countUserExchangeOrders(String userId,String goodsId);
	
	/**
	 * 查询除待审核状态之外的订单
	 * @param keywords
	 * @return
	 */
	List<ScoreGoodsOrder> findListExceptWaitVerify(String keywords);
	
	/**
	 * 录入发货信息
	 */
	void enteringDeliverInfo(ScoreGoodsOrder order);
}
