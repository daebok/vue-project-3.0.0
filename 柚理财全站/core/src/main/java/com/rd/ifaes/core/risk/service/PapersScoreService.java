package com.rd.ifaes.core.risk.service;

import java.util.List;

import com.rd.ifaes.core.base.service.BaseService;
import com.rd.ifaes.core.risk.domain.PapersScore;

/**
 *  试卷等级业务接口类
 * @version 3.0
 * @author QianPengZhan
 * @date 2016年8月6日
 */
public interface PapersScoreService extends BaseService<PapersScore>{
	
	/**
	 * 批量插入对象
	 * @author QianPengZhan
	 * @date 2016年8月6日
	 * @param list
	 * @return
	 */
	int insertBatchPapersScores(List<PapersScore> list);
	
	
	/**
	 * 插入对象
	 * @author QianPengZhan
	 * @date 2016年8月6日
	 * @param ps
	 * @return
	 */
	int insertPapersScore(PapersScore ps);
	
	/**
	 * 根据试卷ID查询等级设置
	 * @author QianPengZhan
	 * @date 2016年8月6日
	 * @param papersId
	 * @return
	 */
	  List<PapersScore> findPapersScoreByPapersId(String papersId);
	
	/**
	 * 根据对象修改对象
	 * @author QianPengZhan
	 * @date 2016年8月6日
	 * @param papersScore
	 * @return
	 */
	int updatePapersScore(PapersScore papersScore);
}