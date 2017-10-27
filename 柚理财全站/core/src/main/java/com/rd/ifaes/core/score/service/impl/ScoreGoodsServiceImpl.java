package com.rd.ifaes.core.score.service.impl;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rd.ifaes.common.annotation.Cacheable;
import com.rd.ifaes.common.dict.ExpireTime;
import com.rd.ifaes.common.exception.BussinessException;
import com.rd.ifaes.common.orm.Page;
import com.rd.ifaes.common.util.DateUtils;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.core.base.service.BaseServiceImpl;
import com.rd.ifaes.core.core.constant.CacheConstant;
import com.rd.ifaes.core.core.constant.Constant;
import com.rd.ifaes.core.core.constant.ResourceConstant;
import com.rd.ifaes.core.core.util.ResourceUtils;
import com.rd.ifaes.core.score.domain.ScoreGoods;
import com.rd.ifaes.core.score.domain.ScoreGoodsCategory;
import com.rd.ifaes.core.score.domain.ScoreGoodsPic;
import com.rd.ifaes.core.score.domain.ScoreGoodsStore;
import com.rd.ifaes.core.score.mapper.ScoreGoodsMapper;
import com.rd.ifaes.core.score.model.ScoreGoodsModel;
import com.rd.ifaes.core.score.service.ScoreGoodsCategoryService;
import com.rd.ifaes.core.score.service.ScoreGoodsPicService;
import com.rd.ifaes.core.score.service.ScoreGoodsService;
import com.rd.ifaes.core.score.service.ScoreGoodsStoreService;
import com.rd.ifaes.core.sys.service.DictDataService;

/**
 * ServiceImpl:商品管理service
 * @author ywt
 * @version 3.0
 * @date 2017-2-20
 */
@Service("scoreGoodsService") 
public class ScoreGoodsServiceImpl extends BaseServiceImpl<ScoreGoodsMapper, ScoreGoods> implements ScoreGoodsService {
	
	@Resource
	private transient ScoreGoodsStoreService scoreGoodsStoreService;
	@Resource
	private transient ScoreGoodsPicService scoreGoodsPicService;
	@Resource
	private transient DictDataService dictDataService;
	@Resource
	private transient ScoreGoodsCategoryService scoreGoodsCategoryService;
	
	
	@Override
	public Page<ScoreGoods> findVerifyGoods(ScoreGoodsModel scoreGoodsModel) {
		Page<ScoreGoods> page = scoreGoodsModel.getPage();
		if(page==null){
			page=new Page<ScoreGoods>();
		}
		page.setRows(dao.findVerifyGoods(scoreGoodsModel));
		return page;
	}

	@Override
	public Page<ScoreGoods> findSaleGoods(ScoreGoodsModel scoreGoodsModel) {
		Page<ScoreGoods> page = scoreGoodsModel.getPage();
		if(page==null){
			page=new Page<ScoreGoods>();
		}
		page.setRows(dao.findSaleGoods(scoreGoodsModel));
		return page;
	}
	
	@Transactional(readOnly = false)
	@Override
	public void saveOrUpdate(ScoreGoodsModel scoreGoodsModel) {
		if (scoreGoodsModel.isNewRecord()) {
			scoreGoodsModel.setCreateTime(DateUtils.getNow());
			scoreGoodsModel.setUpdateTime(DateUtils.getNow());
			//生成库存记录
			ScoreGoodsStore scoreGoodsStore= new ScoreGoodsStore();
			scoreGoodsStore.setGoodsId(scoreGoodsModel.getUuid());
			scoreGoodsStore.setNewRecord(true);
			//默认库存
			scoreGoodsStore.setStore(scoreGoodsModel.getStore());
			//默认冻结库存
			scoreGoodsStore.setFreezeStore(Constant.INT_ZERO);
			//默认已兑换数量
			scoreGoodsStore.setSoldAmount(Constant.INT_ZERO);
			scoreGoodsStoreService.save(scoreGoodsStore);
			// 生成图片记录
			if (StringUtils.isNotBlank(scoreGoodsModel.getPicPaths())) {
				initPic(scoreGoodsModel.getPicPaths(),scoreGoodsModel.getUuid());
			}
			else {
				throw new BussinessException(ResourceUtils.get(ResourceConstant.GOODS_PIC_NOT_NULL));
			}
			
		}
		else{
			if (StringUtils.isNotBlank(scoreGoodsModel.getPicPaths())) {
				initPic(scoreGoodsModel.getPicPaths(),scoreGoodsModel.getUuid());
			}
			if (Constant.GOODS_STATUS_SALE_ON.equals(scoreGoodsModel.getStatus())) {
				scoreGoodsModel.setSaleTime(DateUtils.getNow());
			}
			// 库存
			ScoreGoodsStore scoreGoodsStore = scoreGoodsStoreService.findStoreByGoodsId(scoreGoodsModel.getUuid());
			if(scoreGoodsStore!=null && scoreGoodsStore.getStore() != scoreGoodsModel.getStore()){
				if(scoreGoodsModel.getStore() < (scoreGoodsStore.getFreezeStore()+scoreGoodsStore.getSoldAmount())){
					throw new BussinessException(ResourceUtils.get(ResourceConstant.GOODS_STORE_MUSTGT_FREEZE_AND_SOLD));
				}
				scoreGoodsStoreService.updateGoodStoreTotal(scoreGoodsModel.getUuid(),scoreGoodsModel.getStore());
			}
			scoreGoodsModel.setUpdateTime(DateUtils.getNow());
		}
		super.save(scoreGoodsModel);
	}

	@Transactional(readOnly = false)
	@Override
	public void updateGoods(ScoreGoodsModel scoreGoodsModel) {
		if (StringUtils.isNotBlank(scoreGoodsModel.getPicPaths())) {
			initPic(scoreGoodsModel.getPicPaths(),scoreGoodsModel.getUuid());
		}
		if (Constant.GOODS_STATUS_SALE_ON.equals(scoreGoodsModel.getStatus())) {
			scoreGoodsModel.setSaleTime(DateUtils.getNow());
		}
		scoreGoodsModel.setUpdateTime(DateUtils.getNow());
		super.save(scoreGoodsModel);
	}

	
	
	@Override
	public List<ScoreGoods> findBestseller(String goodsCategoryId,int countLimit) {
		return dao.findBestseller(goodsCategoryId,countLimit);
	}
	
	@Override
	@Cacheable(key = CacheConstant.KEY_PREFIX_GOODS_QUERY_CONDITION , expire = ExpireTime.TEN_MIN)
	public  Map<String,Object> queryCondition() {
		Map<String, Object> conditionMap = new HashMap<String, Object>();
		conditionMap.put("goodsCategoryList", scoreGoodsCategoryService.findList(new ScoreGoodsCategory()));
		conditionMap.put("scoreCondition", dictDataService.findListByDictType("scoreCondition"));
		return conditionMap;
	}
	
	/**
	 * 生成图片记录
	 * @param picPath
	 * @param goodsId
	 */
	public void initPic(String picPath, String goodsId) {
		String[] paths = picPath.split(",");
		List<ScoreGoodsPic> list = new ArrayList<>();
		for (String path : paths) {
			ScoreGoodsPic scoreGoodsPic = new ScoreGoodsPic();
			scoreGoodsPic.setGoodsId(goodsId);
			scoreGoodsPic.setNewRecord(true);
			scoreGoodsPic.setPicPath(path);
			scoreGoodsPic.setCreateTime(DateUtils.getNow());
			scoreGoodsPic.setPicType(Constant.GOODS_PIC_TYPE_BIG);
			list.add(scoreGoodsPic);
		}
		// 设置第一张图片为缩略图
		list.get(Constant.INT_ZERO).setPicType(Constant.GOODS_PIC_TYPE_SMALL);
		// 初始化图片
		scoreGoodsPicService.initPic(goodsId);
		scoreGoodsPicService.insertBatch(list);
	}
}
