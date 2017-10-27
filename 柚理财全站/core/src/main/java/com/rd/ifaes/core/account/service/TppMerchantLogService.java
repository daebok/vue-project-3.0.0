package com.rd.ifaes.core.account.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.rd.ifaes.common.orm.Page;
import com.rd.ifaes.core.account.domain.TppMerchantLog;
import com.rd.ifaes.core.account.model.TppMerchantLogModel;
import com.rd.ifaes.core.base.service.BaseService;
import com.rd.ifaes.core.statistic.model.StatisticModel;
import com.rd.ifaes.core.tpp.model.ufx.UfxCashModel;
import com.rd.ifaes.core.tpp.model.ufx.UfxRechargeModel;
import com.rd.ifaes.core.tpp.model.ufx.UfxSubAccountModel;

/**
 * 
 * @ClassName: TppMerchantLogService 
 * @Description: 
 * @author jxx 
 * @date 2016年7月13日 下午4:53:15 
 *
 */
public interface TppMerchantLogService extends BaseService<TppMerchantLog>{
	
	TppMerchantLog findByOrderNo(String ordId);
	
	/**
	 * 
	 * @Title: findManagePage 
	 * @Description: 平台资金记录
	 * @param @param entity
	 * @param @return 
	 * @return Page<TppMerchantLog>
	 * @throws
	 */
	Page<TppMerchantLog> findManagePage(TppMerchantLogModel entity);
	
	/**
	 * 
	 * 平台资金记录,不分页
	 * @author jxx
	 * @date 2016年8月4日
	 * @param entity
	 * @return
	 */
	List<TppMerchantLog> findManageList(TppMerchantLogModel entity);
	
	/**
	 * 
	 * 商户充值
	 * @author jxx
	 * @date 2016年8月9日
	 * @param tppMerchantLog
	 * @return
	 */
	UfxRechargeModel merchantRecharge(TppMerchantLog tppMerchantLog);
	
	/**
	 * 
	 * 商户提现
	 * @author jxx
	 * @date 2016年8月9日
	 * @param tppMerchantLog
	 */
	UfxCashModel merchantCash(TppMerchantLog tppMerchantLog);
	
	/**
	 * 
	 * 查询商户子账户列表
	 * @author jxx
	 * @date 2016年8月9日
	 * @return
	 */
	List<UfxSubAccountModel> getSubAccountList();
	
	/**
	 * 
	 * 商户转账给用户
	 * @author jxx
	 * @date 2016年8月9日
	 * @param tppMerchantLog
	 */
	String merchantTransfer(TppMerchantLog tppMerchantLog,String userName);
	
	/**
	 * 
	 * 商户子账户之间转账
	 * @author jxx
	 * @date 2016年8月9日
	 * @param tppMerchantLog
	 */
	String subMerchantTransfer(TppMerchantLog tppMerchantLog);
	
	/**
	 * 修改操作记录为失败状态
	 */
	void setTppMerchantLogFail();
	
	/**
	 * 
	 * 给商户充值
	 * @author jxx
	 * @date 2016年8月15日
	 * @param flag
	 * @param model
	 * @return
	 */
	boolean doMerchantRecharge(UfxRechargeModel model);
	
	/**
	 * 
	 * 给商户提现
	 * @author jxx
	 * @date 2016年8月15日
	 * @param flag
	 * @param model
	 * @return
	 */
	boolean doMerchantCash(UfxCashModel model);
	
	   /**
     * 保存放款、还款2个阶段的平台资金收支日志
     * @author  FangJun
     * @date 2016年11月3日
     * @param operateType  操作类型
     * @param userId 交易用户
     * @param money 交易金额
     * @param orderNo 交易订单号
     */
	void saveMerchantLog(final String operateType,final String userId,final BigDecimal money,final String orderNo);
	

	/**
	 * 取得商户交易记录数
	 * @param model
	 * @return
	 */
	int getManageCount(TppMerchantLogModel model);

	/**
	 * 根据统计日期统计平台收入信息
	 * @param model
	 * @return
	 */
	Map<String, Object> findByStatDate(StatisticModel model);

	/**
	 * 根据类型统计平台收入信息
	 * @param model
	 * @return
	 */
	Map<String, Object> findByStatType(StatisticModel model);

	/**
	 * 根据平台收入和支出统计盈亏
	 * @param model
	 * @return
	 */
	Map<String, Object> statisticPlatProfit(StatisticModel model);
	
    /**
     * 获得平台盈亏统计列表
     * @param model
     * @return
     */
	Map<String, Object> getStatisticMertLogList(StatisticModel model);
	
	/**
	 * 渤海银行商户充值
	 * @author ZhangBiao
	 * @date 2017年3月3日
	 * @param tppMerchantLog
	 */
	void cbhbMerchantRecharge(TppMerchantLog tppMerchantLog);

	/**
	 * 渤海银行商户提现
	 * @author ZhangBiao
	 * @date 2017年3月4日
	 * @param tppMerchantLog
	 */
	void merchantCbhbCash(TppMerchantLog tppMerchantLog);
	
}