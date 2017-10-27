package com.rd.ifaes.core.account.service;

import java.util.List;
import java.util.Map;

import com.rd.ifaes.common.orm.Page;
import com.rd.ifaes.core.account.domain.Recharge;
import com.rd.ifaes.core.account.model.RechargeModel;
import com.rd.ifaes.core.base.service.BaseService;
import com.rd.ifaes.core.tpp.model.cbhb.CbhbWebRechargeModel;
import com.rd.ifaes.core.statistic.model.StatisticModel;
import com.rd.ifaes.core.tpp.model.ufx.UfxRechargeModel;

/**
 * Service Interface:RechargeService
 * @author xhf
 * @version 3.0
 * @date 2016-6-30
 */
public interface RechargeService extends BaseService<Recharge>{
	
	/**
	 * 查询充值状态
	 * @author QianPengZhan
	 * @date 2017年3月30日
	 */
	void queryCbhbRechargeStatus();
	
	/**
	 * 充值
	 * @param model
	 * @return
	 */
	Object doRecharge(RechargeModel model);
	
	/**
	 * 根据交易流水号获取充值记录
	 * @param tradeNo
	 * @return
	 */
	Recharge getRechargeByOrderNo(String tradeNo);
	
	/**
	 * 
	 * @Title: findManagePage 
	 * @Description: 后台充值记录
	 * @param @param entity
	 * @param @return 
	 * @return Page<Recharge>
	 * @throws
	 */
	Page<Recharge> findManagePage(RechargeModel entity);
	
	/**
	 * 
	 * 后台充值记录,不分页
	 * @author jxx
	 * @date 2016年8月4日
	 * @param model
	 * @return
	 */
	List<Recharge> findManageList(RechargeModel model);
	
	/**
	 * 通过日期查询记录
	 * @param model
	 * @return
	 */
	Page<Recharge> findByDate(RechargeModel model);
	
	/**
	 * 
	 * 更新充值状态
	 * @author xhf
	 * @date 2016年8月10日
	 * @param recharge
	 */
	void updateByTppResult(Recharge recharge);
	
	/**
	 * 
	 * 更新超时订单状态
	 * @author xhf
	 * @date 2016年8月20日
	 */
	void updateOverTimeOrderStatus();
	
	/**
	 * 充值回调业务处理
	 */
	void doTppRecharge(UfxRechargeModel model);
	
	/**
	 * 后台充值记录数
	 * @param model
	 * @return
	 */
	int getManageCount(RechargeModel model);
	
	/**
	 * 统计充值人数/金额——二维图
	 * @param model
	 * @return
	 */
	Map<String, Object> findByStatDate(StatisticModel model);

	/**
	 * 统计充值人数/金额——饼状图
	 * @param model
	 * @return
	 */
	Map<String, Object> findByStatType(StatisticModel model);
	
	/**
	 * 渤海银行充值
	 * @author ZhangBiao
	 * @date 2017年2月28日
	 * @param model
	 */
	void doTppCbhbRecharge(CbhbWebRechargeModel model);
 }