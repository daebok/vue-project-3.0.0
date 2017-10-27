package com.rd.ifaes.core.score.service.impl;

import org.springframework.stereotype.Service;
import com.rd.ifaes.core.base.service.BaseServiceImpl;
import com.rd.ifaes.core.score.domain.ScoreType;
import com.rd.ifaes.core.score.mapper.ScoreTypeMapper;
import com.rd.ifaes.core.score.service.ScoreTypeService;

/**
 * 
 * 积分发放类型
 * @version 3.0
 * @author wyw
 * @date 2016-8-4
 */
@Service("scoreTypeService") 
public class ScoreTypeServiceImpl  extends BaseServiceImpl<ScoreTypeMapper, ScoreType> implements ScoreTypeService{

	/**
	 * 根据typeCode获取积分类型
	 */
	@Override
	public ScoreType getScoreTypeByCode(final String typeCode) {
		return dao.getScoreTypeByCode(typeCode);
	}
	
    //@Resource
    //private ScoreTypeMapper scoreTypeMapper;

}