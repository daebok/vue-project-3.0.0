package com.rd.ifaes.core.score.service;

import com.rd.ifaes.common.orm.Page;
import com.rd.ifaes.core.base.service.BaseService;
import com.rd.ifaes.core.score.domain.UserScoreLog;
import com.rd.ifaes.core.user.domain.User;

/**
 * Service Interface:UserScoreLogService
 * @author 
 * @version 3.0
 * @date 2016-8-4
 */
public interface UserScoreLogService extends BaseService<UserScoreLog>{
	 /**
	  * 根据时间查询积分日志
	  * @author wyw
	  * @date 2016-8-31
	  * @param userScoreLog
	  * @param user
	  * @return
	  */
	 Page<UserScoreLog> findByDate(UserScoreLog userScoreLog,User user);
	 
	  /**
	   * 取得导出记录数
	   * @param model
	   * @return
	   */
	  int getListCount(UserScoreLog model);
}