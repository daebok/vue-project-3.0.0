package com.rd.ifaes.core.score.mapper;


import com.rd.ifaes.core.base.mapper.BaseMapper;
import com.rd.ifaes.core.score.domain.ScoreGoodsPic;

/**
 * Mapper Interface:商品图片管理Mapper
 * @author ywt
 * @version 3.0
 * @date 2017-2-20
 */
public interface ScoreGoodsPicMapper extends BaseMapper<ScoreGoodsPic>{
	
	/**
	 * 初始化商品
	 * @param goodsId
	 */
	void initPic(String goodsId);

}
