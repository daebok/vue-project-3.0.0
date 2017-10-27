package com.rd.ifaes.core.user.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.rd.account.domain.AccountLog;
import com.rd.account.service.AccountLogService;
import com.rd.ifaes.common.annotation.Cacheable;
import com.rd.ifaes.common.dict.ExpireTime;
import com.rd.ifaes.common.util.BigDecimalUtils;
import com.rd.ifaes.common.util.DateUtils;
import com.rd.ifaes.core.base.service.BaseServiceImpl;
import com.rd.ifaes.core.core.constant.CacheConstant;
import com.rd.ifaes.core.user.domain.UserEarnLog;
import com.rd.ifaes.core.user.mapper.UserEarnLogMapper;
import com.rd.ifaes.core.user.service.UserEarnLogService;
import com.rd.ifaes.core.user.service.UserService;

/**
 * 收益处理类
 * @author xhf
 * @version 3.0
 * @date 2016-7-21
 */
@Service("userEarnLogService") 
public class UserEarnLogServiceImpl  extends BaseServiceImpl<UserEarnLogMapper, UserEarnLog> implements UserEarnLogService{
	
	/**
	 * 用户基础类
	 */
	@Resource
	private UserService userService;
	/**
	 * 资金日志类
	 */
	@Resource
	private AccountLogService accountLogService;

	/**
	 * 获取昨日收益
	 */
	@Override
	@Cacheable(key=CacheConstant.KEY_USER_YESTERDAY_EARN_AMOUNT, expire = ExpireTime.ONE_HOUR)
	public BigDecimal yesterdayEarnAmount(final String userId){
		BigDecimal earnAmount = dao.getUserEarnAmountByDate(new UserEarnLog(userId,DateUtils.rollDay(DateUtils.getNow(), -1)));
		return earnAmount == null ? BigDecimal.ZERO : earnAmount;
	}
	
	/**
	 * 统计所有收益
	 */
	@Override
	public BigDecimal allEarnAmount(final String userId){
		final BigDecimal allEarnAmount = dao.getAllEarnAmount(new UserEarnLog(userId,null));
		return allEarnAmount==null? BigDecimal.ZERO:allEarnAmount;
	}
	
	/**
	 * 计算昨日收益
	 */
	@Override
	//@Transactional(readOnly=false)
	public void calcuYesterdayEarn(){
		final Date earnDate = DateUtils.rollDay(DateUtils.getNow(), -1);
		final AccountLog model = new AccountLog();
		model.setStartTime(DateUtils.getDateStart(earnDate));
		model.setEndTime(DateUtils.getDateEnd(earnDate));
		
		final List<AccountLog> accountLogList = accountLogService.yesterdayEarnAmount(model);
		List<UserEarnLog> logs = new ArrayList<>();
		for (AccountLog accountLog : accountLogList) {
			UserEarnLog userEarnLog = new UserEarnLog();
			userEarnLog.setUserId(accountLog.getUserId());
			userEarnLog.setEarn(accountLog.getMoney());
			userEarnLog.setEarnDate(earnDate);
			//统计上一次
			final UserEarnLog lastLog = dao.getLastLog(accountLog.getUserId());
			if(lastLog!=null){
				userEarnLog.setTotal(BigDecimalUtils.add(lastLog.getTotal(),accountLog.getMoney()));
			}else{
				userEarnLog.setTotal(accountLog.getMoney());
			}
			logs.add(userEarnLog);
		}
		this.insertBatch(logs);
	}

	@Override
	public BigDecimal getAllEarnAmountByDate(String dateStart) {
		return dao.getAllEarnAmountByDate(dateStart);
	}
}