package com.rd.ifaes.core.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rd.ifaes.common.dict.ExpireTime;
import com.rd.ifaes.common.util.DateUtils;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.core.core.constant.ConfigConstant;
import com.rd.ifaes.core.core.constant.Constant;
import com.rd.ifaes.core.core.constant.ProjectConstant;
import com.rd.ifaes.core.core.util.CacheUtils;
import com.rd.ifaes.core.core.util.ConfigUtils;


/**
 * 项目缓存处理( 只存放项目剩余可投)
 * @author lh
 * @verison 3.0
 * @since 2016-7-19
 */
public class ProjectCache {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ProjectCache.class);
		
	/**
	 * 剩余可投加入缓存
	 * @param projectNo
	 * @param amount
	 */
	public static void setRemainAccount(String projectId, double amount) {
		String projectRemainAccountKey = String.format(ProjectConstant.KEY_PROJECT_REMAIN_ACCOUNT, projectId);
		CacheUtils.setDouble(projectRemainAccountKey, amount, ExpireTime.NONE);
	}
	/**
	 * 查询剩余可投
	 * @param projectNo
	 */
	public static double getRemainAccount(String projectId ) {
		String projectRemainAccountKey = String.format(ProjectConstant.KEY_PROJECT_REMAIN_ACCOUNT, projectId);
		return CacheUtils.getDouble(projectRemainAccountKey);
	}
	
	/**
	 * 归还剩余可投金额<br>
	 * 哪些条件下需要归还剩余可投金额：<br>
	 * 	1、取消订单；
	 *  2、第三方投资回调失败；
	 *  3、超投回调成功；
	 * @param model
	 */
	public static double incrRemainAccount(String projectNo, double amount) {
		return CacheUtils.incr( String.format(ProjectConstant.KEY_PROJECT_REMAIN_ACCOUNT, projectNo), amount);
	}
	
	/**
	 * 扣除剩余可投金额<br>
	 * 哪些条件下需要扣除剩余可投金额：<br>
	 * 1、下单成功；
	 * 2、自动投资回调成功；
	 * @param model
	 */
	public static double decrRemainAccount(String projectNo, double amount) {
		String projectRemainAccountKey = String.format(ProjectConstant.KEY_PROJECT_REMAIN_ACCOUNT, projectNo);
		double remainAccount = CacheUtils.decr(projectRemainAccountKey, amount);
		if(remainAccount<Constant.DOUBLE_ZERO){
			LOGGER.warn("项目(projectNo:{})扣除剩余金额(amount:{})失败!",projectNo,amount);
			remainAccount= CacheUtils.incr(projectRemainAccountKey, amount);
		}
		return remainAccount;
	}

	/**
	 * 删除剩余金额<br>
	 * 	成立审核时执行
	 * @param projectId
	 * @param amount
	 */
	public static void delRemainAccount(String projectNo) {
		String key = String.format(ProjectConstant.KEY_PROJECT_REMAIN_ACCOUNT, projectNo);
		CacheUtils.del(key);
	}
	
	/**
	 * 取得下一个项目编号
	 * @param today
	 * @return
	 */
	public static String getNextProjectNo(String today){
		if(StringUtils.isBlank(today)){
			today = DateUtils.getDate("yyMMdd");
		}else{
			today = today.replaceAll("-", "");
			today = today.substring(2,today.length());
		}
		int initProjectNo = ConfigUtils.getInt(ConfigConstant.PROJECT_NO_INIT_VALUE);
		String projectNoKey = StringUtils.format(ProjectConstant.KEY_TODAY_PROJECT_NO, today);
		int projectNo = (int)CacheUtils.incr(projectNoKey, 1d);
		if(projectNo < initProjectNo){
			projectNo = initProjectNo;
			CacheUtils.setDouble(projectNoKey, initProjectNo, ExpireTime.ONE_DAY);
		}
		return today + String.format("%04d", projectNo);
	}
	 
  
}
