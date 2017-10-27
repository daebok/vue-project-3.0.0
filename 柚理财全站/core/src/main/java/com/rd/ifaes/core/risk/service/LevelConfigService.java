package com.rd.ifaes.core.risk.service;

import java.util.List;

import com.rd.ifaes.core.base.service.BaseService;
import com.rd.ifaes.core.risk.domain.LevelConfig;

/**
 * 风险类型接口
 * @version 3.0
 * @author QianPengZhan
 * @date 2016年8月6日
 */
public interface LevelConfigService extends BaseService<LevelConfig>{
	
	/**
	 *  获取当前最大值得产品等级
	 * @author QianPengZhan
	 * @date 2016年10月21日
	 * @return
	 */
	LevelConfig getMaxConfig();
	
	/**
	 * @Title: findByPapersId   
	 * @Description: 根据paperId查询设定等级然后处理一部分
	 * @param: @param papersId
	 * @param: @return      
	 * @return: List<LevelConfig>      
	 * @throws
	 */
	 List<LevelConfig> findByPapersId(String papersId);
	
	
	/**
	 * findListByPapersId:(根据试卷查询该试卷设定的等级). <br/> 
	 * @author QianPengZhan
	 * @param papersId
	 * @return
	 */
	 List<LevelConfig> findListByPapersId(String papersId);

	/**
	 * 根据用户风险等级值查询记录
	 * @param riskLevelVal
	 * @return
	 */
	LevelConfig findListByUserRiskLevelVal(int userRiskLevelVal);
	
	/**
	 * 按照类型顺序正序查询风险类型
	 * @author QianPengZhan
	 * @date 2016年7月27日
	 * @return
	 */
	 List<LevelConfig> findByOrder();
	
	/**
	 * 根据产品风险等级查询等级
	 * @author QianPengZhan
	 * @date 2016年8月2日
	 * @param riskLevel
	 * @return
	 */
	LevelConfig findByRiskLevel(String riskLevel);
}	