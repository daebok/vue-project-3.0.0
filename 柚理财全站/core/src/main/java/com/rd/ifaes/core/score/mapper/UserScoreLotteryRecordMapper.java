package com.rd.ifaes.core.score.mapper;

import com.rd.ifaes.core.base.mapper.BaseMapper;
import com.rd.ifaes.core.score.domain.UserScoreLotteryRecord;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface UserScoreLotteryRecordMapper extends BaseMapper<UserScoreLotteryRecord> {
	
	/**
	 * 
	 * 最新的记录
	 * @author jxx
	 * @date 2017年6月12日
	 * @param limit
	 * @return
	 */
	List<UserScoreLotteryRecord> findNewList(UserScoreLotteryRecord record);

}
