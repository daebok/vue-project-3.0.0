package com.rd.ifaes.core.account.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.rd.account.domain.Account;
import com.rd.ifaes.common.orm.Page;
import com.rd.ifaes.core.account.domain.Cash;
import com.rd.ifaes.core.account.model.CashModel;
import com.rd.ifaes.core.base.service.BaseService;
import com.rd.ifaes.core.tpp.model.cbhb.CbhbDrawingsModel;
import com.rd.ifaes.core.statistic.model.StatisticModel;
import com.rd.ifaes.core.user.domain.User;

/**
 * Service Interface:CashService
 * @author xhf
 * @version 3.0
 * @date 2016-7-2
 */
public interface CashService extends BaseService<Cash>{
	/**
	 * 查询提现状态处理业务
	 * @author QianPengZhan
	 * @date 2017年3月2日
	 */
	void queryCashStatus();
	/**
	 * 提现回调业务处理
	 * @author QianPengZhan
	 * @date 2017年3月1日
	 */
	void doCbhbCashNotify(final CbhbDrawingsModel model);
	/**
	 * 取现
	 * @param cash
	 * @return
	 */
	Object doCash(CashModel model);
	
	/**
	 * 根据订单号查询记录
	 * @param orderNo
	 * @return
	 */
	Cash findByOrderNo(String orderNo);
	
	/**
	 * 通过取现订单号查询记录
	 * @param orderNo
	 * @return
	 */
	Cash findByCashNo(String orderNo);
	
	/**
	 * 
	 * @Title: findManagePage 
	 * @Description: 后台提现记录
	 * @param @param entity
	 * @param @return 
	 * @return Page<Cash>
	 * @throws
	 */
	Page<Cash> findManagePage(CashModel entity);
	
	/**
	 * 
	 * 后台查询提现记录
	 * @author jxx
	 * @date 2016年8月4日
	 * @param entity
	 * @return
	 */
	List<Cash> findManageList(CashModel entity);
	
	/**
	 * 
	 * 提现信息
	 * @author jxx
	 * @date 2016年7月29日
	 * @param id
	 * @return
	 */
	CashModel getCashModel(String uuid);
	
	/**
	 * 根据日期获取提现记录
	 * @param model
	 * @return
	 */
	Page<Cash> findByDate(CashModel model);
	
	/**
	 * 取现复核
	 * @param model
	 */
	void doAudit(CashModel model);
	
	/**
	 * 更新提现状态
	 * @param model
	 */
	void updateByTppResult(Cash cash);
	
    /**
	 * 获取当日已提现次数(不含提现失败)
	 * @param userId
	 * @return
	 */
	int getSingleDaytime(String userId);
	
    /**
	 * 获取当日已提现金额(不含提现失败)
	 * @param userId
	 * @return
	 */
	BigDecimal getSingleDayAmount(String userId);
	
	/**
	 * 今日剩余可提现金额
	 * @param userId
	 * @return
	 */
	BigDecimal getSingleDayRemainCashAmount(String userId);
	
	/**
	 * 
	 * 更新超时订单状态
	 * @author xhf
	 * @date 2016年8月20日
	 */
	void updateOverTimeOrderStatus();
	
	/**
	 * 
	 * 提现资金和日志统一处理
	 * @author xhf
	 * @date 2016年9月23日
	 * @param type
	 * @param cash
	 */
	void doCashLogByType(String type, Cash cash);
	
	/**
	 * 校验和解冻第三方资金
	 * @author xhf
	 * @date 2016年9月27日
	 * @param model
	 */
	void checkAndunFreeze(CashModel model);
	
	/**
	 * 
	 * 提现人工核查
	 * @author xhf
	 * @date 2016年9月29日
	 * @param model
	 */
	void doCheck(CashModel model);
	
	/**
	 * 
	 * 获取第三方提现金额
	 * @author xhf
	 * @date 2016年11月3日
	 * @param userId
	 * @return
	 */
	BigDecimal getTppCashAmount(String userId);
	
	/**
	 * 后台提现记录数
	 * @param model
	 * @return
	 */
	int getManageCount(CashModel model);
	
	/**
	 * 解冻提现金额
	 * @param cash
	 * @param user
	 */
	void unFreezeTppCash(Cash cash, User user);
	
	/**
	 * 提现订单需要人工处理
	 * @param dbCash
	 * @param handleReason
	 */
	void cashNeedManHandle(Cash dbCash,String handleReason);

	/**
	 * 统计提现人数/金额-二维图
	 * @param model
	 * @return
	 */
	Map<String, Object> findByStatDate(StatisticModel model);
	
	/**
	 * 统计提现人数/金额-饼状图
	 * @param model
	 * @return
	 */
	Map<String, Object> findByStatType(StatisticModel model);
	/**
	 * 获取提现手续费
	 */
	BigDecimal getCashFee(final BigDecimal cashAmount, final String userId, final Cash cash, final Account account);
	/**
	 * 提现失败之后，处理提现金额需要手续费相关的数据
	 * @param dbCash
	 */
	void repayCashFeeAmount(Cash dbCash);
}