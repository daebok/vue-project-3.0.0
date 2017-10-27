package com.rd.ifaes.core.score.service;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.rd.ifaes.common.annotation.Cacheable;
import com.rd.ifaes.common.dict.ExpireTime;
import com.rd.ifaes.common.orm.Page;
import com.rd.ifaes.core.base.service.BaseService;
import com.rd.ifaes.core.core.constant.CacheConstant;
import com.rd.ifaes.core.score.domain.ScoreGoods;
import com.rd.ifaes.core.score.model.ScoreGoodsModel;

/**
 * Service Interface:商品管理Service
 * @author ywt
 * @version 3.0
 * @date 2017-2-20
 */
public interface ScoreGoodsService extends BaseService<ScoreGoods>{
	
	/**
	 * 查询待审核的商品信息
	 * @param scoreGoodsModel
	 * @return
	 */
	Page<ScoreGoods> findVerifyGoods(ScoreGoodsModel scoreGoodsModel);
	
	/**
	 * 查询可上架、下架的商品信息
	 * @param scoreGoodsModel
	 * @return
	 */
	Page<ScoreGoods> findSaleGoods(ScoreGoodsModel scoreGoodsModel);
	
	void saveOrUpdate(ScoreGoodsModel scoreGoodsModel);
	
	void updateGoods(ScoreGoodsModel scoreGoodsModel);
	
	/**
	 * 查询畅销商品(先按推荐指数降序，再按销售数量降序)
	 * @param goodsCategoryId
	 * @return
	 */
	List<ScoreGoods> findBestseller(String goodsCategoryId,int countLimit);
	
	
	/**
	 * 获取列表页的查询配置
	 * @return
	 */
	Map<String,Object> queryCondition();
	
	
}