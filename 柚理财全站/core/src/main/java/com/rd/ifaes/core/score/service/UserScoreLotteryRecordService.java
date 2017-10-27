package com.rd.ifaes.core.score.service;

import com.rd.ifaes.core.base.service.BaseService;
import com.rd.ifaes.core.score.domain.ScoreLottery;
import com.rd.ifaes.core.score.domain.UserScoreLotteryRecord;

import java.util.List;

public interface UserScoreLotteryRecordService extends BaseService<UserScoreLotteryRecord> {
	/**
	 * 
	 * 最新的记录
	 * @author jxx
	 * @param userScoreLotteryRecord 
	 * @date 2017年6月12日
	 * @param limit
	 * @return
	 */
	List<UserScoreLotteryRecord> findNewList(UserScoreLotteryRecord userScoreLotteryRecord);
	
	ScoreLottery lottety(UserScoreLotteryRecord model);
}
