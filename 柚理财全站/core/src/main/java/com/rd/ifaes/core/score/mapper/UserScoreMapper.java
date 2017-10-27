package com.rd.ifaes.core.score.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.rd.ifaes.core.base.mapper.BaseMapper;
import com.rd.ifaes.core.score.domain.UserScore;
import com.rd.ifaes.core.user.domain.User;

/**
 * Dao Interface:UserScoreMapper
 * @author 
 * @version 3.0
 * @date 2016-8-4
 */
public interface UserScoreMapper extends BaseMapper<UserScore> {
  /**
   * 
   * 根据用户id获取用户积分
   * @author wyw
   * @date 2016-8-4
   * @param userId
   * @return
   */
   UserScore getUserScore(String userId);
  /**
   * 更新用户积分值
   * @author wyw
   * @return
   */
  
   int updateUserScore(@Param("useScore")int  useScore,@Param("noUseScore") int noUseScore,
		  					 @Param("expenseScore") int expenseScore,@Param("userId") String userId);

   /**
    * 根据UserIds查询UserScore
    * @param userIds
    * @return
    */
   List<UserScore> findByUserIds(List<User> userList);
}