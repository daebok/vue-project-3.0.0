package com.rd.ifaes.core.score.service;


import com.rd.ifaes.core.base.service.BaseService;
import com.rd.ifaes.core.score.domain.ScoreGoodsPic;

/**
 * Service Interface:商品图片管理Service
 * @author ywt
 * @version 3.0
 * @date 2017-2-20
 */
public interface ScoreGoodsPicService extends BaseService<ScoreGoodsPic>{
	
	/**
	 * 初始化商品
	 * @param goodsId
	 */
	void initPic(String goodsId);
}