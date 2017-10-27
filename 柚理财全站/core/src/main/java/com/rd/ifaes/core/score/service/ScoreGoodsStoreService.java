package com.rd.ifaes.core.score.service;

import com.rd.ifaes.core.base.service.BaseService;
import com.rd.ifaes.core.score.domain.ScoreGoodsStore;

/**
 * Service Interface:商品库存Service
 * @author ywt
 * @version 3.0
 * @date 2017-2-20
 */
public interface ScoreGoodsStoreService extends BaseService<ScoreGoodsStore>{
	
	/**
	 * 通过商品id查询商品库存
	 * @param scoreGoodsId
	 * @return
	 */
	ScoreGoodsStore findStoreByGoodsId(String scoreGoodsId);
	
	/**
	 * 修改商品库存
	 * @param scoreGoodsStore
	 * @return
	 */
	int updateGoodsStore(ScoreGoodsStore scoreGoodsStore);
	
	/**
	 * 查询商品剩余数量
	 * @param goodsId
	 * @return
	 */
	int queryGoodsLessNum(String goodsId);
	
	/**
	 * 更细库存数
	 * @param goodsId
	 * @param store
	 * @return
	 */
	void updateGoodStoreTotal(String goodsId,int store);
	
}