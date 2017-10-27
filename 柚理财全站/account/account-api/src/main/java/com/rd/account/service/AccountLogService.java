package com.rd.account.service;

import java.util.List;

import com.rd.account.domain.AccountLog;
import com.rd.ifaes.common.orm.Page;

/**
 * Service Interface:AccountLogService
 * @author lh
 * @version 3.0
 * @date 2016-6-14
 */
public interface AccountLogService{
	/**
	 * 查询处昨日17点30~今日17点30 所有有资金操作的用户
	 * @author QianPengZhan
	 * @date 2016年10月11日
	 * @param model
	 * @return
	 */
	List<String> findListForScanner(final AccountLog model);
	
	/**
	 * 查询分页数据
	 * @param entity
	 * @return
	 */
	Page<AccountLog> findPage(AccountLog entity);
	
	/**
	 * 保存
	 * @param entity
	 * @return
	 */
	void save(AccountLog entity);

	/**
	 * 
	 * @Title: findManagePage 
	 * @Description: 后台查询用户资金记录
	 * @param @param entity
	 * @param @return 
	 * @return Page<Account>
	 * @throws
	 */
	Page<AccountLog> findManagePage(AccountLog entity);
	
	/**
	 * 
	 * 后台查询用户资金记录,不分页
	 * @author jxx
	 * @date 2016年8月4日
	 * @param model
	 * @return
	 */
	List<AccountLog> findManageList(AccountLog model);
	
	/**
	 * 根据日期查询用户资金记录
	 * @param entity
	 * @return
	 */
	Page<AccountLog> findByDate(AccountLog entity);
	
	/**
	 * 
	 * 统计昨日收益
	 * @author xhf
	 * @date 2016年8月20日
	 * @param model
	 * @return
	 */
	List<AccountLog> yesterdayEarnAmount(AccountLog model);

	/**
	 * 后台资金日志导出
	 * @author ZhangBiao
	 * @date 2016年10月21日
	 * @param model
	 * @return
	 */
	List<AccountLog> findManageListExportExcel(AccountLog model);
	
	/**
	 * 统计行数
	 * @param model
	 * @return
	 */
	int getManageCount(AccountLog model);

	/**
	 * 根据日期查询accountLog列表，未分页
	 * @param log
	 * @return
	 */
	List<AccountLog> findListByDate(AccountLog log);
}