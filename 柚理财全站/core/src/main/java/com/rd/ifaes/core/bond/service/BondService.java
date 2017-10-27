package com.rd.ifaes.core.bond.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

import com.rd.ifaes.common.orm.Page;
import com.rd.ifaes.core.base.service.BaseService;
import com.rd.ifaes.core.bond.domain.Bond;
import com.rd.ifaes.core.bond.model.BondInvestModel;
import com.rd.ifaes.core.bond.model.BondModel;
import com.rd.ifaes.core.project.domain.Project;
import com.rd.ifaes.core.project.domain.ProjectInvest;
import com.rd.ifaes.core.project.model.ProjectInvestModel;
import com.rd.ifaes.core.user.domain.User;

/**
 * 债权标业务接口类
 * @version 3.0
 * @author QianPengZhan
 * @date 2016年7月28日
 */
public interface BondService extends BaseService<Bond>{
	
	/**
	 * 添加债权
	 * @author QianPengZhan
	 * @date 2016年12月1日
	 * @param bond
	 * @param backUrl
	 */
	void insert(final Bond bond,final String backUrl);
	/**
	 * 判断折溢价率是否超出限制
	 * @author QianPengZhan
	 * @date 2016年11月28日
	 * @param projectInvestModel
	 * @return
	 */
	boolean judgeBondAprLimit(final ProjectInvestModel projectInvestModel);
	/**
	 *  根据转让时的对应的原始标下的投资记录来判断有几次
	 * @author QianPengZhan
	 * @param projectInvestId
	 * @param realCounts
	 * @return
	 */
	int calculateCounts(final String projectInvestId,final int realCounts);
	/**
	 * 由于剩余期限的利息每天变化导致最高折溢价率的改变 所以债权的折溢价率等于最高折溢价率的标 需要更新掉他的债权折溢价率
	 * @author QianPengZhan
	 * @date 2016年11月4日
	 */
	void bondMaxBondAprChangeByDay();
	/**
	 * 修改债权转让投资阶段 
	 * @author QianPengZhan
	 * @date 2016年10月27日
	 */
	void updateStage(final Integer stage,final String uuid);
	/**
	 * 获取对应投资记录的已还本金
	 * @author QianPengZhan
	 * @date 2016年11月28日
	 * @param pInvestInvestModel
	 * @return
	 */
	BigDecimal getRepayedMoney(final ProjectInvestModel pInvestInvestModel);
	/**
	 * 获取对应投资记录的已还本金
	 * @author QianPengZhan
	 * @date 2016年10月14日
	 * @param project
	 * @param pInvest
	 * @param isPart 是否部分
	 * @return
	 */
	 BigDecimal getRepayedMoney(final Project project,final ProjectInvest pInvest,final boolean isPart);
	 
	/**
	 * 处理协议
	 * @author QianPengZhan
	 * @date 2016年11月3日
	 * @param bondId
	 * @param bondInvestId
	 */
	 void handleAllBondProtocol(final String bondId,final String bondInvestId);
	/**
	 * 债权转让人下载债权协议 .zip
	 * @author QianPengZhan
	 * @date 2016年9月30日
	 * @param bondId
	 * @return
	 */
	String downloadAllBondProtocol(final String bondId);
	
	/**
	 * 债权投资人下载债权协议
	 * @author QianPengZhan
	 * @date 2016年9月29日
	 * @param bondInvestId
	 * @return
	 */
	String downloadBondProtocol(final String bondInvestId);
	
	/**
	 * 债权专区
	 * @author QianPengZhan
	 * @date 2016年8月31日
	 * @param bond
	 * @return
	 */
	Page<BondModel> findPageData(final BondModel bond);
	
	/**
	 * 递增修改持有利息和手续费
	 * @author QianPengZhan
	 * @date 2016年8月31日
	 * @param soldInterest
	 * @param bondFee
	 * @param successTime
	 * @param uuid
	 */
	void  updateBondOtherInfo(BigDecimal soldInterest,BigDecimal bondFee,Date successTime,String uuid);
	
	/**
	 * 根据旧状态修改新状泰
	 * @author QianPengZhan
	 * @date 2016年8月31日
	 * @param newStatus
	 * @param oldStatus
	 * @param uuid
	 */
	void updateBondStatusById(String newStatus,String oldStatus,String uuid);
	
	/**
	 * 递增修改已售金额
	 * @author QianPengZhan
	 * @date 2016年8月31日
	 * @param money
	 * @param uuid
	 */
	void updateSoldCapitalById(double money,String uuid);
	
	/**
	 * 获取缓存中的债权信息
	 * @author QianPengZhan
	 * @date 2016年8月31日
	 * @param bondId
	 * @param backUrl 校验返回的地址
	 * @return
	 */
	Bond getBond(final String bondId,final String backUrl);
	
	/**
	 * 根据编号查询对象
	 * @author QianPengZhan
	 * @date 2016年8月28日
	 * @param bondNo
	 * @return
	 */
	Bond  getBondByBondNo(final String bondNo);
	/**
	 * 获取回款计划
	 * @author QianPengZhan
	 * @date 2016年8月28日
	 * @param bondInvestId
	 * @param backUrl
	 * @return
	 */
	Map<String,Object>  getRepayPlan(String bondInvestId,final String backUrl);
	
	
	/**
	 * 查询扩展属性的分页数据
	 * @author QianPengZhan
	 * @date 2016年7月28日
	 * @param model
	 * @return
	 */
	Page<BondModel> findModelPage(BondModel bond);
	
	/**
	 * 后台转让记录查询扩展属性的分页数据
	 * @author QianPengZhan
	 * @date 2016年7月28日
	 * @param model
	 * @return
	 */
	Page<BondModel> findManagePage(BondModel bond);
	
	/**
	 * 转让设置
	 * @author QianPengZhan
	 * @date 2016年7月28日
	 * @param id
	 * @param backUrl
	 * @return
	 */
	Map<String,Object>  handleBondSet(final String id,final String backUrl);
	
	/**
	 * 取得当日下一个借款编号<br>
	 * 	生成的格式是: 2016062800001 前面几位为当前的日期,后面五位为系统自增长类型的编号<br>
	 *  原理: 1.获取当前日期格式化值; 2.读取数据库,上次编号的值+1即为当前此次编号的值<br>
	 *  (新的一天会重新从00001开始编号)
	 * @param createDate
	 * @return
	 */
	String nextBondNo(String createDate);
	
	/**
	 * 详情页面获取债权信息和项目信息
	 * @author QianPengZhan
	 * @date 2016年8月2日
	 * @param id
	 * @return
	 */
	Map<String,Object> getBondProjectDetail(String id,final String backUrl);
	
	
	/**
	 * 详情页面获取债权信息和项目信息
	 * @author QianPengZhan
	 * @date 2016年8月2日
	 * @param id
	 * @return
	 */
	Map<String,Object> getBondProjectDetail(String id,User user,final String backUrl);
	
	/**
	 * 根据输入金额获取预期收益和实际支付金额
	 * @author QianPengZhan
	 * @date 2016年8月2日
	 * @param amount
	 * @param id
	 * @return
	 */
	Map<String,Object> getInvestData(BigDecimal amount,String id);
	
	/**
	 * 根据债权的ID 撤销此笔债权
	 * @author QianPengZhan
	 * @date 2016年8月4日
	 * @param id
	 * @param backUrl
	 * @return
	 */
	void cancleBond(String id,boolean isAuto,final String backUrl);
	
	/**
	 * 定时任务 自动撤回
	 * @author QianPengZhan
	 * @date 2016年8月18日
	 */
	void autoCancleBond();
	
	/**
	 * 债权标每日更新剩余期限
	 * @author QianPengZhan
	 * @date 2016年8月18日
	 */
	void bondRemainDaysChangeByDay();
	
	
	/**
	 * 债权专区查询条件
	 * @author QianPengZhan
	 * @date 2016年8月18日
	 * @return
	 */
	Map<String,Object> queryBondCondition();
	
	
	/**
	 * 校验债权信息
	 * @author QianPengZhan
	 * @date 2016年8月19日
	 * @param invest
	 * @return
	 */
	Bond checkBondInfo(final BondInvestModel invest,final String backUrl);

	/**
	 * 根据项目id获取不同状态债权个数
	 * @author ZhangBiao
	 * @date 2016年10月17日
	 * @param projectId
	 * @param status
	 * @return
	 */
	int getCountByStatus(String projectId,String status);
}