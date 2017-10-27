package com.rd.ifaes.core.score.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.rd.ifaes.core.base.mapper.BaseMapper;
import com.rd.ifaes.core.score.domain.ScoreGoods;
import com.rd.ifaes.core.score.model.ScoreGoodsModel;

/**
 * Mapper Interface:商品管理Mapper
 * @author ywt
 * @version 3.0
 * @date 2017-2-20
 */
public interface ScoreGoodsMapper extends BaseMapper<ScoreGoods>{
	
	/**
	 * 查询待审核的商品信息
	 * @param scoreGoodsModel
	 * @return
	 */
	List<ScoreGoods> findVerifyGoods(ScoreGoodsModel scoreGoodsModel);
	
	/**
	 * 查询可上架、下架的商品信息
	 * @param scoreGoodsModel
	 * @return
	 */
	List<ScoreGoods> findSaleGoods(ScoreGoodsModel scoreGoodsModel);
	
	/**
	 * 查询畅销商品(先按推荐指数降序，再按销售数量降序)
	 * @param goodsCategoryId
	 * @return
	 */
	List<ScoreGoods> findBestseller(@Param("goodsCategoryId")String goodsCategoryId,@Param("countLimit")int countLimit);

}
