package com.rd.ifaes.core.score.service;

import com.rd.ifaes.core.base.service.BaseService;
import com.rd.ifaes.core.score.domain.ScoreGoodsCategory;
import com.rd.ifaes.core.score.model.ScoreGoodsCategoryModel;

/**
 * Service Interface:商品分类Service
 * @author ywt
 * @version 3.0
 * @date 2017-2-20
 */
public interface ScoreGoodsCategoryService extends BaseService<ScoreGoodsCategory>{
	
	void saveOrUpdate(ScoreGoodsCategoryModel scoreGoodsCategoryModel);
	
}