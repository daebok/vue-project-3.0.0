package com.rd.ifaes.core.score.service;

import com.rd.ifaes.core.base.service.BaseService;
import com.rd.ifaes.core.score.domain.ScoreType;

/**
 * Service Interface:ScoreTypeService
 * @author 
 * @version 3.0
 * @date 2016-8-4
 */
public interface ScoreTypeService extends BaseService<ScoreType>{
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