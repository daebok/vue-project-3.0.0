package com.rd.ifaes.core.score.service.impl;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rd.ifaes.common.util.DateUtils;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.core.base.service.BaseServiceImpl;
import com.rd.ifaes.core.core.constant.Constant;
import com.rd.ifaes.core.score.domain.ScoreGoodsCategory;
import com.rd.ifaes.core.score.mapper.ScoreGoodsCategoryMapper;
import com.rd.ifaes.core.score.model.ScoreGoodsCategoryModel;
import com.rd.ifaes.core.score.service.ScoreGoodsCategoryService;

/**
 * ServiceImpl:商品分类service
 * @author ywt
 * @version 3.0
 * @date 2017-2-20
 */
@Service("scoreGoodsCategoryService") 
public class ScoreGoodsCategoryServiceImpl extends BaseServiceImpl<ScoreGoodsCategoryMapper, ScoreGoodsCategory> implements ScoreGoodsCategoryService {
	
	@Transactional(readOnly = false)
	@Override
	public void saveOrUpdate(ScoreGoodsCategoryModel scoreGoodsCategoryModel) {
		if (StringUtils.isBlank(scoreGoodsCategoryModel.getUuid())) {
			scoreGoodsCategoryModel.setNewRecord(true);
			scoreGoodsCategoryModel.setCreateTime(DateUtils.getNow());
			scoreGoodsCategoryModel.setDeleteFlag(Constant.FLAG_NO);
		}
		super.save(scoreGoodsCategoryModel);
	}


}
