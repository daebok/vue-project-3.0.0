package com.rd.ifaes.core.score.mapper;

import org.apache.ibatis.annotations.Param;

import com.rd.ifaes.core.base.mapper.BaseMapper;
import com.rd.ifaes.core.score.domain.ScoreGoodsStore;

/**
 * Mapper Interface:商品库存Mapper
 * @author ywt
 * @version 3.0
 * @date 2017-2-20
 */
public interface ScoreGoodsStoreMapper extends BaseMapper<ScoreGoodsStore>{
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
	 * 修改商品库存
	 * @param scoreGoodsStore
	 * @return
	 */
	void updateGoodStoreTotal(@Param("goodsId")String goodsId,@Param("store")int store);
}
