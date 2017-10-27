package com.rd.ifaes.core.score.mapper;

import java.util.List;

import com.rd.ifaes.core.base.mapper.BaseMapper;
import com.rd.ifaes.core.score.domain.UserScoreLog;

/**
 * Dao Interface:UserScoreLogMapper
 * @author 
 * @version 3.0
 * @date 2016-8-4
 */
public interface UserScoreLogMapper extends BaseMapper<UserScoreLog> {
	  /**
	   * 
	   * 根据时间段，积分类型查询等查询用户积分记录
	   * @author wyw
	   * @date 2016-8-6
	   * @param project
	   * @return
	   */
	  List<UserScoreLog> findByDate(UserScoreLog userScoreLog);
	  
	  /**
	   * 取得导出记录数
	   * @param model
	   * @return
	   */
	  int getListCount(UserScoreLog model);
}