package com.rd.ifaes.core.risk.mapper;

import java.util.List;

import com.rd.ifaes.core.base.mapper.BaseMapper;
import com.rd.ifaes.core.risk.domain.PapersScore;

/**
 * 风险等级、试卷中间表  Dao 
 * @version 3.0
 * @author QianPengZhan
 * @date 2016年8月5日
 */
public interface PapersScoreMapper extends BaseMapper<PapersScore> {
	
	/**
	 * 根据试卷ID查询等级设置   
	 * @author QianPengZhan
	 * @date 2016年8月5日
	 * @param papersId
	 * @return
	 */
	 List<PapersScore> findPapersScoreByPapersId(String papersId);
 }	