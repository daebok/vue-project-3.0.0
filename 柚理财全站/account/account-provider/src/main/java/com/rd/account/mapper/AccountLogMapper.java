package com.rd.account.mapper;

import java.util.List;

import com.rd.account.domain.AccountLog;
import com.rd.ifaes.core.base.mapper.BaseMapper;

/**
 * Dao Interface:AccountLogMapper
 * @author lh
 * @version 3.0
 * @date 2016-6-14
 */
public interface AccountLogMapper extends BaseMapper<AccountLog> {
	
	
	/**
	 * 查询处昨日17点30~今日17点30 所有有资金操作的用户
	 * @author QianPengZhan
	 * @date 2016年10月11日
	 * @param model
	 * @return
	 */
	List<String> findListForScanner(final AccountLog model);
	/**
	 * 
	 * @Title: findManageList 
	 * @Description:后台查询用户资金
	 * @param @param model
	 * @param @return 
	 * @return List<Account>
	 * @throws
	 */
	List<AccountLog> findManageList(AccountLog model);
	
	/**
	 * 根据日期查询记录
	 * @param model
	 * @return
	 */
	List<AccountLog> findByDate(AccountLog model);
	
	/**
	 * 统计昨日收益
	 * @param model
	 * @return
	 */
	List<AccountLog> yesterdayEarnAmount(AccountLog model);
	
	/**
	 * 扩展新增
	 * @param model
	 * @return
	 */
	int insertBySelect(AccountLog model);
	
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
}