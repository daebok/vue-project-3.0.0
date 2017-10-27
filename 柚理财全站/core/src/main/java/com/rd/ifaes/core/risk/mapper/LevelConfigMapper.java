package com.rd.ifaes.core.risk.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.rd.ifaes.core.base.mapper.BaseMapper;
import com.rd.ifaes.core.risk.domain.LevelConfig;

/**
 * 风险等级类
 * @version 3.0
 * @author QianPengZhan
 * @date 2016年8月5日
 */
public interface LevelConfigMapper extends BaseMapper<LevelConfig> {
	
	/**
	 * 判断风险等级是否重复存在
	 * @author QianPengZhan
	 * @date 2016年10月21日
	 * @param riskLevelVal
	 * @return
	 */
	int isExistsConfig(@Param("riskLevelVal") final String riskLevelVal, @Param("uuid") final String uuid);
	
	/**
	 * 获取当前最大值得产品等级
	 * @author QianPengZhan
	 * @date 2016年9月30日
	 * @return
	 */
	LevelConfig getMaxConfig();
	
	/**
	 * 查询不包含这些uuids的对象组 并且是未删除的 或者删除的
	 * @author QianPengZhan
	 * @date 2016年8月5日
	 * @param map  传递 list 和 逻辑删除标识
	 * @return
	 */
	List<LevelConfig> findNoClude(@Param("deleteFlag") String deleteFlag,@Param("list") List<String> list);
	
	/**
	 * 根据试卷ID查询试卷等级列表
	 * @author QianPengZhan
	 * @date 2016年8月5日
	 * @param map  传递不同对象的多个参数  主要是试卷ID  和  删除标识 
	 * @return
	 */
	List<LevelConfig> findLevelListByPapersId(@Param("deleteFlag") String deleteFlag,@Param("papersId") String papersId);
	
	/**
	 * 根据风险类型顺序排列得到的对象
	 * @author QianPengZhan
	 * @date 2016年8月5日
	 * @param deleteFlag
	 * @return
	 */
	List<LevelConfig> findLevelListByOrder(String deleteFlag);
}