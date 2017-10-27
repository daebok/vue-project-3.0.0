package com.rd.ifaes.core.score.service.impl;



import org.springframework.stereotype.Service;

import com.rd.ifaes.core.base.service.BaseServiceImpl;
import com.rd.ifaes.core.score.domain.ScoreGoodsPic;
import com.rd.ifaes.core.score.mapper.ScoreGoodsPicMapper;
import com.rd.ifaes.core.score.service.ScoreGoodsPicService;

/**
 * ServiceImpl:商品图片管理service
 * @author ywt
 * @version 3.0
 * @date 2017-2-20
 */
@Service("scoreGoodsPicService") 
public class ScoreGoodsPicServiceImpl extends BaseServiceImpl<ScoreGoodsPicMapper, ScoreGoodsPic> implements ScoreGoodsPicService {

	@Override
	public void initPic(String goodsId) {
		dao.initPic(goodsId);
	}


}
