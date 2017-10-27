package com.rd.ifaes.core.score.service.impl;


import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rd.ifaes.common.annotation.CacheEvict;
import com.rd.ifaes.common.annotation.Cacheable;
import com.rd.ifaes.common.dict.ExpireTime;
import com.rd.ifaes.core.base.service.BaseServiceImpl;
import com.rd.ifaes.core.core.constant.CacheConstant;
import com.rd.ifaes.core.score.domain.ScoreGoodsStore;
import com.rd.ifaes.core.score.mapper.ScoreGoodsStoreMapper;
import com.rd.ifaes.core.score.service.ScoreGoodsService;
import com.rd.ifaes.core.score.service.ScoreGoodsStoreService;

/**
 * ServiceImpl:商品库存service
 * @author ywt
 * @version 3.0
 * @date 2017-2-20
 */
@Service("scoreGoodsStoreSerivce") 
public class ScoreGoodsStoreServiceImpl extends BaseServiceImpl<ScoreGoodsStoreMapper, ScoreGoodsStore> implements ScoreGoodsStoreService {
	private static final Logger LOGGER = LoggerFactory.getLogger(ScoreGoodsStoreServiceImpl.class);
	
	@Resource
	private transient ScoreGoodsService scoreGoodsService;
	
	@Override
	public ScoreGoodsStore findStoreByGoodsId(String scoreGoodsId) {
		return dao.findStoreByGoodsId(scoreGoodsId);
	}
	
	@Transactional(readOnly = false)
	@CacheEvict(keys=CacheConstant.KEY_PREFIX_SCORE_GOODS_LESS_NUM+"{scoreGoodsStore.goodsId}")
	@Override
	public int updateGoodsStore(ScoreGoodsStore scoreGoodsStore) {
		LOGGER.info("商品id：{},变更冻结数量：{}，变更售出总数：{}",scoreGoodsStore.getGoodsId(),scoreGoodsStore.getFreezeStore(),scoreGoodsStore.getSoldAmount());
		return dao.updateGoodsStore(scoreGoodsStore);
	}
	
	@Cacheable(key=CacheConstant.KEY_PREFIX_SCORE_GOODS_LESS_NUM+"{goodsId}",expire=ExpireTime.FIVE_MIN)
	@Override
	public int queryGoodsLessNum(String goodsId) {
		ScoreGoodsStore scoreGoodsStore= dao.findStoreByGoodsId(goodsId);
		return scoreGoodsStore.getStore()-scoreGoodsStore.getFreezeStore()-scoreGoodsStore.getSoldAmount();
	}

	@CacheEvict(keys=CacheConstant.KEY_PREFIX_SCORE_GOODS_LESS_NUM+"{scoreGoodsStore.goodsId}")
	@Override
	public void updateGoodStoreTotal(String goodsId,int store) {
		dao.updateGoodStoreTotal(goodsId,store);
	}
}
