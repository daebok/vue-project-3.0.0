package com.rd.ifaes.core.score.mapper;

import com.rd.ifaes.core.score.domain.ScoreType;
import com.rd.ifaes.core.base.mapper.BaseMapper;

/**
 * Dao Interface:ScoreTypeMapper
 * @author 
 * @version 3.0
 * @date 2016-8-4
 */
public interface ScoreTypeMapper extends BaseMapper<ScoreType> {
	/**
	 * 
	 * 根据typeCode获取积分类型
	 * @author wyw
	 * @date 2016-8-4
	 * @param typeCode
	 * @return
	 */
	 ScoreType getScoreTypeByCode(String typeCode);
}