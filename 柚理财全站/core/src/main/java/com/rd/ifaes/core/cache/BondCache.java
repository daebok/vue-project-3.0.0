/**
* 互联网金融交易系统ifaes (Internet Finance  Assets  Exchange  System)
 * Copyright (c) 2016 杭州融都科技股份有限公司 版权所有
 */
package com.rd.ifaes.core.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rd.ifaes.common.dict.CacheKeys;
import com.rd.ifaes.common.dict.ExpireTime;
import com.rd.ifaes.common.util.DateUtils;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.core.core.constant.ConfigConstant;
import com.rd.ifaes.core.core.constant.Constant;
import com.rd.ifaes.core.core.constant.ProjectConstant;
import com.rd.ifaes.core.core.util.CacheUtils;
import com.rd.ifaes.core.core.util.ConfigUtils;

/**
 * 债权投资缓存处理类
 * @version 3.0
 * @author QianPengZhan
 * @date 2016年8月19日
 */
public  class BondCache{
	/**
	 * 日志类
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(BondCache.class);
	
	/** 债权投资 剩余可投处理    start  **/
	/**
	 * 1、债权剩余可投金额加入缓存
	 * 使用时间： 在债权发布成功之后 加入缓存
	 * @author QianPengZhan
	 * @date 2016年8月19日
	 * @param bondNo   债权编号
	 * @param amount   剩余债权金额
	 */
	public static void setBondRemainAccount(final String bondNo, final double amount) {
		final String bondRemainAccountKey = String.format(CacheKeys.BOND_REMAIN_ACCOUNT.getKey(), bondNo);
		LOGGER.info("【债权剩余可投金额加入缓存】："+"缓存key:"+bondRemainAccountKey+",缓存value:"+amount);
		CacheUtils.setDouble(bondRemainAccountKey, amount, ExpireTime.NONE);
	}
	
	/**
	 * 2、根据债权编号查询该债权标剩余可投债权金额
	 * 使用时间：
	 * 1、进入订单页面  信息校验
	 * @author QianPengZhan
	 * @date 2016年8月19日
	 * @param bondNo  债权编号
	 * @return   剩余债权可投金额
	 */
	public static double getBondRemainAccount(final String bondNo){
		final String bondRemainAccountKey = String.format(CacheKeys.BOND_REMAIN_ACCOUNT.getKey(), bondNo);
		return CacheUtils.getDouble(bondRemainAccountKey);
	}
	
	/**
	 *  3、归还剩余可投金额<br>
	 * 	哪些条件下需要归还剩余可投金额：<br>
	 * 	1、取消订单；
	 *  2、第三方投资回调失败；
	 *  3、超投回调成功；
	 * @author QianPengZhan
	 * @date 2016年8月19日
	 * @param bondNo
	 * @param amount
	 */
	public static double incrBondRemainAccount(final String bondNo,final double amount) {
		final String bondRemainAccountKey = String.format(CacheKeys.BOND_REMAIN_ACCOUNT.getKey(), bondNo);//获取当前剩余债权金额
		return CacheUtils.incr(bondRemainAccountKey, amount);
	}
	
	/**
	 * 4、扣除剩余可投金额<br>
	 * 哪些条件下需要扣除剩余可投金额：<br>
	 * 1、下单成功；
	 * @param model
	 */
	public static void decrBondRemainAccount(final String bondNo,final  double amount) {
		final String bondRemainAccountKey = String.format(CacheKeys.BOND_REMAIN_ACCOUNT.getKey(), bondNo);
		CacheUtils.decr(bondRemainAccountKey, amount);
	}
	
	/**
	 * 5、删除剩余金额<br>
	 * @param Id
	 * @param amount
	 */
	public static void delBondRemainAccount(final String bondNo) {
		final String key = String.format(CacheKeys.BOND_REMAIN_ACCOUNT.getKey(), bondNo);
		CacheUtils.del(key);
	}
	
	/**
	 * 6、债权投资检验是否超投
	 * 1）进入订单页面需要校验
	 * 2）债权投资的时候需要校验
	 * @author QianPengZhan
	 * @date 2016年8月19日
	 * @param bondNo
	 * @param amount
	 * @return
	 */
	public static double checkOverBondInvest(final String bondNo,final double amount) {
		double  currentBondAmount;
		final String key = String.format(CacheKeys.BOND_REMAIN_ACCOUNT.getKey(), bondNo);
		if ((currentBondAmount=CacheUtils.decr(key, amount)) < Constant.INT_ZERO) { // 超投
			CacheUtils.incr(key, amount);
			LOGGER.error("---overBondInvest---bondNo:{},amount: {}" ,bondNo, amount);
			LOGGER.info("【用户债权投资时进行超投校验】是否超投：{}",true);
			currentBondAmount =Constant.DOUBLE_ONE_NEGE;//超投默认值
		}
		return currentBondAmount;
	}
	/** 债权投资 剩余可投处理    end **/
	
	/**
	 * 取得下一个项目编号
	 * @param today
	 * @return
	 */
	public static String getNextBondNo(String today){
		if(StringUtils.isBlank(today)){
			today = DateUtils.getDate("yyyyMMdd");
		}else{
			today = today.replaceAll("-", "");			
		}
		int initBondNo = ConfigUtils.getInt(ConfigConstant.BOND_NO_INIT_VALUE);
		String bondNoKey = StringUtils.format(ProjectConstant.KEY_TODAY_PROJECT_NO, today);
		int bondNo = (int)CacheUtils.incr(bondNoKey, 1d);
		if(bondNo < initBondNo){
			bondNo = initBondNo;
			CacheUtils.setDouble(bondNoKey, initBondNo, ExpireTime.ONE_DAY);
		}
		return today + String.format("%05d", bondNo);
	}
	 
}
