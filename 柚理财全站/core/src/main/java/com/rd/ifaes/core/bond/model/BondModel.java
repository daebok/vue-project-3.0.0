package com.rd.ifaes.core.bond.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.rd.ifaes.common.dict.BondRuleEnum;
import com.rd.ifaes.common.entity.BaseEntity;
import com.rd.ifaes.common.util.BigDecimalUtils;
import com.rd.ifaes.common.util.NumberUtils;
import com.rd.ifaes.common.util.SpringContextHolder;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.core.bond.domain.Bond;
import com.rd.ifaes.core.bond.domain.BondRule;
import com.rd.ifaes.core.bond.service.BondRuleService;
import com.rd.ifaes.core.cache.BondCache;
import com.rd.ifaes.core.core.constant.Constant;
import com.rd.ifaes.core.core.constant.DictConstant;
import com.rd.ifaes.core.core.util.DictUtils;
import com.rd.ifaes.core.core.util.ScriptEngineUtils;
import com.rd.ifaes.core.sys.domain.DictData;

/**
 * 债权专区展示类
 * @version 3.0
 * @author QianPengZhan
 * @date 2016年8月30日
 */
public class BondModel extends BaseEntity<BondModel> {

	private static final long serialVersionUID = -3368846668821527613L;
	
	/**
	 * 转让金额类型   00不限  01 1万元以下  02 1~5万元  03 5~10万元  04 10万元以上
	 */
	private String bondMoneyType;
	/**
	 * 剩余期限类型   00 不限  01 30天以下  02 31~90天  03 91~180天  04  180天以上
	 */
	private String remainDaysType;
	/**
	 * 预期年化类型   00不限  01 6%以下  02 6~10%  03 10~14%  04 14%以上
	 */
	private String aprType;
	/**
	 *状态集合
	 */
	private  String[]  statusSet;
	
	//数据库字段
	/**
	 * 还款方式
	 */
	private String repayStyle;
	/**
	 * 债权名称
	 */
	private String bondName;
	/**
	 * 项目ID
	 */
	private String	projectId;
	/**
	 * 投标ID
	 */
	private String	investId;
	/**
	 * 债权人
	 */
	private String	userId;
	/**
	 *折溢价率  
	 */
	private BigDecimal	bondApr;
	/**
	 * 债权金额
	 */
	private BigDecimal	bondMoney;
	/**
	 * 已售债权本金
	 */
	private BigDecimal	soldCapital;
	/**
	 * 债权状态:发布0， 转让完成3，自动撤回4，后台撤回5
	 */
	private String	status;
	/**
	 * 投资阶段
	 */
	private Integer stage;
	/**
	 * 债权有效时长
	 */
	private Integer	limitHours;
	/**
	 * 债权截止日期
	 */
	private Date	bondEndTime;
	/**
	 * 债权转让编号
	 */
	private String	bondNo;
	/**
	 * 开始期数
	 */
	private Integer	startPeriod;
	/**
	 * 添加时间
	 */
	private Date	createTime;
	/**
	 * 债权规则ID
	 */
	private String  ruleId;
	/**
	 * 债权起投金额
	 */
	private BigDecimal bondLowestMoney;
	/**
	 * 债权最高投资金额
	 */
	private BigDecimal bondMostMoney;
	/**
	 * 剩余期限（天）
	 */
	private int remainDays;
	/**
	 * 持有利息
	 */
	private BigDecimal soldInterest;
	/**
	 * 已支付手续费
	 */
	private BigDecimal  bondFee; 
	/**
	 * 转让成功时间
	 */
	private Date successTime;
	/**实际转让方式*/
	private String sellStyle;
	
	//扩展元素
	/**
	 * 项目名称
	 */
	private String projectName;
	/**
	 *原始借款标状态
	 */
	private String borrowStatus;
	/**
	 *原始标产品期限
	 */
	private int timeLimit;
	/**
	 * 原始标期限类型
	 */
	private int timeType;
	/**
	 *原资产预期年化(预期年化收益率)
	 */
	private BigDecimal apr;
	/**
	 * 可转让金额   计算得来 
	 */
	private BigDecimal remainMoney;
	/**
	 * 转让价格   计算得来 
	 */
	private BigDecimal bondPrice;
	/**
	 *本期回款日
	 */
    private Date nextRepaymentDate;
    /**
     * 手续费   相当于 bondFee 前端展示
     */
    private BigDecimal manageFee;
    /**
     * 已产生收益    相当于 soldInterest 前端展示
     */
    private BigDecimal happendInterest;
    /**
     * 实收金额
     */
    private BigDecimal receivedMoney;
    /**
     * 原始标待收总期数
     */
	private int totalPeriod;
	/**
	 * 原始标待收已还期数
	 */
	private int repayedPeriod;
	/**
	 * 原始标待收最后实际回款日
	 */
	private Date lastRepayTime;	
	/**
	 * 转让成功时间    相当于 successTime 前端展示
	 */
	private Date bondSuccessTime;
	/** 協議Id*/
	private String protocolId;
	

	private String userName;
	
	private String realName;
	/**
	 * 获取属性bondFee的值
	 * @return bondFee属性值
	 */
	public BigDecimal getBondFee() {
		return bondFee;
	}

	/**
	 * 设置属性bondFee的值
	 * @param  bondFee属性值
	 */
	public void setBondFee(final BigDecimal bondFee) {
		this.bondFee = bondFee;
	}
	
	/**
	 * 原始标名称
	 * @author QianPengZhan
	 * @date 2016年8月30日
	 * @return
	 */
	public String getProjectName() {
		return projectName;
	}
	
	/**
	 * 设置原始标名称
	 * @author QianPengZhan
	 * @date 2016年8月30日
	 * @param projectName
	 */
	public void setProjectName(final String projectName) {
		this.projectName = projectName;
	}
	
	/**
	 * 获取原始标利率
	 * @author QianPengZhan
	 * @date 2016年8月30日
	 * @return
	 */
	public BigDecimal getApr() {
		return apr;
	}
	
	/**
	 * 原始标利率
	 * @author QianPengZhan
	 * @date 2016年8月30日
	 * @param apr
	 */
	public void setApr(final BigDecimal apr) {
		this.apr = apr;
	}
	
	/**
	 * 获取剩余可投(必須的)
	 * @author QianPengZhan
	 * @date 2016年8月30日
	 * @return
	 */
	public BigDecimal getRemainMoney() {
		BigDecimal remainMoney = BigDecimal.ZERO;
		if(this.getBondNo() == null){
			remainMoney = this.remainMoney;
		}else{
			remainMoney = BigDecimal.valueOf(BondCache.getBondRemainAccount(this.getBondNo()));
		}
		return remainMoney;
	}
	
	/**
	 * 剩余债权
	 * @author QianPengZhan
	 * @date 2016年8月30日
	 * @param remainMoney
	 */
	public void setRemainMoney(final BigDecimal remainMoney) {
		this.remainMoney = remainMoney;
	}

	/**
	 * 下一期还款日
	 * @author QianPengZhan
	 * @date 2016年8月30日
	 * @param nextRepaymentDate
	 */
	public Date getNextRepaymentDate() {
		return nextRepaymentDate;
	}
	
	/**
	 * 下一期还款日
	 * @author QianPengZhan
	 * @date 2016年8月30日
	 * @param nextRepaymentDate
	 */
	public void setNextRepaymentDate(final Date nextRepaymentDate) {
		this.nextRepaymentDate = nextRepaymentDate;
	}
	
	/**
	 * 获取债权价格
	 * @author QianPengZhan
	 * @date 2016年8月30日
	 * @return
	 */
	public BigDecimal getBondPrice() {
		return getBondPriceMoney();
	}
	
	/**
	 * 设置债权价格
	 * @author QianPengZhan
	 * @date 2016年8月30日
	 * @param bondPrice
	 */
	public void setBondPrice(final BigDecimal bondPrice) {
		this.bondPrice = bondPrice;
	}

	/**
	 * 获取属性soldInterest的值
	 * @return soldInterest属性值
	 */
	public BigDecimal getSoldInterest() {
		return soldInterest;
	}

	/**
	 * 设置属性soldInterest的值
	 * @param  soldInterest属性值
	 */
	public void setSoldInterest(final BigDecimal soldInterest) {
		this.soldInterest = soldInterest;
	}
	
	/**
	 * 获取管理费
	 * @author QianPengZhan
	 * @date 2016年8月30日
	 * @return
	 */
	public BigDecimal getManageFee() {
		return BigDecimalUtils.defaultIfNull(manageFee);
	}
	
	/**
	 * 设置管理费
	 * @author QianPengZhan
	 * @date 2016年8月30日
	 * @param manageFee
	 */
	public void setManageFee(final BigDecimal manageFee) {
		this.manageFee = manageFee;
	}
	
	/**
	 * 持有收益
	 * @author QianPengZhan
	 * @date 2016年8月30日
	 * @return
	 */
	public BigDecimal getHappendInterest() {
		return happendInterest;
	}
	
	/**
	 * 设置持有收益
	 * @author QianPengZhan
	 * @date 2016年8月30日
	 * @param happendInterest
	 */
	public void setHappendInterest(final BigDecimal happendInterest) {
		this.happendInterest = happendInterest;
	}
	
	/**
	 * 实际支付金额 
	 * @author QianPengZhan
	 * @date 2016年8月30日
	 * @return
	 */
	public BigDecimal getReceivedMoney() {
		return getReceivedMoneyMethod();
	}
	
	/**
	 * 获取实际支付金额
	 * @author QianPengZhan
	 * @date 2016年8月30日
	 * @param receivedMoney
	 */
	public void setReceivedMoney(final BigDecimal receivedMoney) {
		this.receivedMoney = receivedMoney;
	}
	
	/**
	 * 获取属性borrowStatus的值
	 * @return borrowStatus属性值
	 */
	public String getBorrowStatus() {
		return borrowStatus;
	}

	/**
	 * 获取属性lastRepayTime的值
	 * @return lastRepayTime属性值
	 */
	public Date getLastRepayTime() {
		return lastRepayTime;
	}

	/**
	 * 设置属性lastRepayTime的值
	 * @param  lastRepayTime属性值
	 */
	public void setLastRepayTime(final Date lastRepayTime) {
		this.lastRepayTime = lastRepayTime;
	}

	/**
	 * 设置属性borrowStatus的值
	 * @param  borrowStatus属性值
	 */
	public void setBorrowStatus(final String borrowStatus) {
		this.borrowStatus = borrowStatus;
	}

	/**
	 * 获取属性timeLimit的值
	 * @return timeLimit属性值
	 */
	public int getTimeLimit() {
		return timeLimit;
	}

	/**
	 * 设置属性timeLimit的值
	 * @param  timeLimit属性值
	 */
	public void setTimeLimit(final int timeLimit) {
		this.timeLimit = timeLimit;
	}
	
	/**
	 * 获取属性timeType的值
	 * @return timeType属性值
	 */
	public int getTimeType() {
		return timeType;
	}

	/**
	 * 设置属性timeType的值
	 * @param  timeType属性值
	 */
	public void setTimeType(final int timeType) {
		this.timeType = timeType;
	}


	/**
	 * 获取属性bondMoneyType的值
	 * @return bondMoneyType属性值
	 */
	@JsonIgnore
	public String getBondMoneyType() {
		if (StringUtils.isBlank(bondMoneyType) && !NumberUtils.isDefault(getBondMoney())) {
			final List<DictData> list = DictUtils.list(DictConstant.BOND_MONEY_TYPE);
			if(CollectionUtils.isNotEmpty(list)){
				for (final DictData dictData : list) {
					if (ScriptEngineUtils.match(dictData.getExpression(), getBondMoney())) {
						bondMoneyType = dictData.getItemValue();
					}
				}
			}
		}
		return bondMoneyType;
	}

	/**
	 * 设置属性bondMoneyType的值
	 * @param  bondMoneyType属性值
	 */
	public void setBondMoneyType(final String bondMoneyType) {
		this.bondMoneyType = bondMoneyType;
	}

	/**
	 * 获取属性remainDaysType的值
	 * @return remainDaysType属性值
	 */
	@JsonIgnore
	public String getRemainDaysType() {
		if (StringUtils.isBlank(remainDaysType) && !NumberUtils.isDefault(getRemainDays())) {
			final List<DictData> list = DictUtils.list(DictConstant.REMAIN_DAYS_TYPE);
			if(CollectionUtils.isNotEmpty(list)){
				for (final DictData dictData : list) {
					if (ScriptEngineUtils.match(dictData.getExpression(), getRemainDays())) {
						remainDaysType =  dictData.getItemValue();
					}
				}
			}
		}
		return remainDaysType;
	}

	/**
	 * 设置属性remainDaysType的值
	 * @param  remainDaysType属性值
	 */
	public void setRemainDaysType(final String remainDaysType) {
		this.remainDaysType = remainDaysType;
	}

	/**
	 * 获取属性aprType的值
	 * @return aprType属性值
	 */
	@JsonIgnore
	public String getAprType() {
		if (StringUtils.isBlank(aprType) && !NumberUtils.isDefault(apr)) {
			final List<DictData> list = DictUtils.list(DictConstant.APR_TYPE);
			if(CollectionUtils.isNotEmpty(list)){
				for (final DictData dictData : list) {
					if (ScriptEngineUtils.match(dictData.getExpression(), apr)) {
						aprType = dictData.getItemValue();
					}
				}
			}
		}
		return aprType;
	}

	/**
	 * 设置属性aprType的值
	 * @param  aprType属性值
	 */
	public void setAprType(final String aprType) {
		this.aprType = aprType;
	}

	/**
	 * 获取属性totalPeriod的值
	 * @return totalPeriod属性值
	 */
	public int getTotalPeriod() {
		return totalPeriod;
	}

	/**
	 * 设置属性totalPeriod的值
	 * @param  totalPeriod属性值
	 */
	public void setTotalPeriod(final int totalPeriod) {
		this.totalPeriod = totalPeriod;
	}

	/**
	 * 获取属性repayedPeriod的值
	 * @return repayedPeriod属性值
	 */
	public int getRepayedPeriod() {
		return repayedPeriod;
	}

	/**
	 * 设置属性repayedPeriod的值
	 * @param  repayedPeriod属性值
	 */
	public void setRepayedPeriod(final int repayedPeriod) {
		this.repayedPeriod = repayedPeriod;
	}

	/**
	 * 获取属性repayStyle的值
	 * @return repayStyle属性值
	 */
	public String getRepayStyle() {
		return repayStyle;
	}

	/**
	 * 设置属性repayStyle的值
	 * @param  repayStyle属性值
	 */
	public void setRepayStyle(final String repayStyle) {
		this.repayStyle = repayStyle;
	}
	
	/**
	 * 复制属性
	 * @author QianPengZhan
	 * @date 2016年8月30日
	 * @return
	 */
	public Bond prototype(){
		final Bond bond = new Bond();
		BeanUtils.copyProperties(this, bond);
		return bond;
	}

	/**
	 * 获取属性statusSet的值
	 * @return statusSet属性值
	 */
	@JsonIgnore
	public String[] getStatusSet() {
		return statusSet;
	}

	/**
	 * 设置属性statusSet的值
	 * @param  statusSet属性值
	 */
	public void setStatusSet(final String[] statusSet) {
		this.statusSet = statusSet;
	}

	/**
	 * 获取属性bondName的值
	 * @return bondName属性值
	 */
	public String getBondName() {
		return bondName;
	}

	/**
	 * 设置属性bondName的值
	 * @param  bondName属性值
	 */
	public void setBondName(final String bondName) {
		this.bondName = bondName;
	}

	/**
	 * 获取属性projectId的值
	 * @return projectId属性值
	 */
	public String getProjectId() {
		return projectId;
	}

	/**
	 * 设置属性projectId的值
	 * @param  projectId属性值
	 */
	public void setProjectId(final String projectId) {
		this.projectId = projectId;
	}

	/**
	 * 获取属性investId的值
	 * @return investId属性值
	 */
	public String getInvestId() {
		return investId;
	}

	/**
	 * 设置属性investId的值
	 * @param  investId属性值
	 */
	public void setInvestId(final String investId) {
		this.investId = investId;
	}

	/**
	 * 获取属性userId的值
	 * @return userId属性值
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * 设置属性userId的值
	 * @param  userId属性值
	 */
	public void setUserId(final String userId) {
		this.userId = userId;
	}

	/**
	 * 获取属性bondApr的值
	 * @return bondApr属性值
	 */
	public BigDecimal getBondApr() {
		return bondApr;
	}

	/**
	 * 设置属性bondApr的值
	 * @param  bondApr属性值
	 */
	public void setBondApr(final BigDecimal bondApr) {
		this.bondApr = bondApr;
	}

	/**
	 * 获取属性bondMoney的值
	 * @return bondMoney属性值
	 */
	public BigDecimal getBondMoney() {
		return bondMoney;
	}

	/**
	 * 设置属性bondMoney的值
	 * @param  bondMoney属性值
	 */
	public void setBondMoney(final BigDecimal bondMoney) {
		this.bondMoney = bondMoney;
	}

	/**
	 * 获取属性soldCapital的值
	 * @return soldCapital属性值
	 */
	public BigDecimal getSoldCapital() {
		return soldCapital;
	}

	/**
	 * 设置属性soldCapital的值
	 * @param  soldCapital属性值
	 */
	public void setSoldCapital(final BigDecimal soldCapital) {
		this.soldCapital = soldCapital;
	}

	/**
	 * 获取属性status的值
	 * @return status属性值
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * 设置属性status的值
	 * @param  status属性值
	 */
	public void setStatus(final String status) {
		this.status = status;
	}

	/**
	 * 获取属性limitHours的值
	 * @return limitHours属性值
	 */
	public Integer getLimitHours() {
		return limitHours;
	}

	/**
	 * 设置属性limitHours的值
	 * @param  limitHours属性值
	 */
	public void setLimitHours(final Integer limitHours) {
		this.limitHours = limitHours;
	}

	/**
	 * 获取属性bondEndTime的值
	 * @return bondEndTime属性值
	 */
	public Date getBondEndTime() {
		return bondEndTime;
	}

	/**
	 * 设置属性bondEndTime的值
	 * @param  bondEndTime属性值
	 */
	public void setBondEndTime(final Date bondEndTime) {
		this.bondEndTime = bondEndTime;
	}

	/**
	 * 获取属性bondNo的值
	 * @return bondNo属性值
	 */
	public String getBondNo() {
		return bondNo;
	}

	/**
	 * 设置属性bondNo的值
	 * @param  bondNo属性值
	 */
	public void setBondNo(final String bondNo) {
		this.bondNo = bondNo;
	}

	/**
	 * 获取属性startPeriod的值
	 * @return startPeriod属性值
	 */
	public Integer getStartPeriod() {
		return startPeriod;
	}

	/**
	 * 设置属性startPeriod的值
	 * @param  startPeriod属性值
	 */
	public void setStartPeriod(final Integer startPeriod) {
		this.startPeriod = startPeriod;
	}

	/**
	 * 获取属性createTime的值
	 * @return createTime属性值
	 */
	public Date getCreateTime() {
		return createTime;
	}

	/**
	 * 设置属性createTime的值
	 * @param  createTime属性值
	 */
	public void setCreateTime(final Date createTime) {
		this.createTime = createTime;
	}

	/**
	 * 获取属性ruleId的值
	 * @return ruleId属性值
	 */
	public String getRuleId() {
		return ruleId;
	}

	/**
	 * 设置属性ruleId的值
	 * @param  ruleId属性值
	 */
	public void setRuleId(final String ruleId) {
		this.ruleId = ruleId;
	}

	/**
	 * 获取属性bondLowestMoney的值
	 * @return bondLowestMoney属性值
	 */
	public BigDecimal getBondLowestMoney() {
		return bondLowestMoney;
	}

	/**
	 * 设置属性bondLowestMoney的值
	 * @param  bondLowestMoney属性值
	 */
	public void setBondLowestMoney(final BigDecimal bondLowestMoney) {
		this.bondLowestMoney = bondLowestMoney;
	}

	/**
	 * 获取属性bondMostMoney的值
	 * @return bondMostMoney属性值
	 */
	public BigDecimal getBondMostMoney() {
		return bondMostMoney;
	}

	/**
	 * 设置属性bondMostMoney的值
	 * @param  bondMostMoney属性值
	 */
	public void setBondMostMoney(final BigDecimal bondMostMoney) {
		this.bondMostMoney = bondMostMoney;
	}

	/**
	 * 获取属性remainDays的值
	 * @return remainDays属性值
	 */
	public int getRemainDays() {
		return remainDays;
	}

	/**
	 * 设置属性remainDays的值
	 * @param  remainDays属性值
	 */
	public void setRemainDays(final int remainDays) {
		this.remainDays = remainDays;
	}
	
	/**
	 * 判断是否全部受让  返回true false
	 * @author QianPengZhan
	 * @date 2016年9月19日
	 * @return
	 */
	public boolean judgeIfBuyAll(){
		boolean isBuyAll = false;
		final BondRuleService bondRuleService = (BondRuleService)SpringContextHolder.getBean("bondRuleService");
		final BondRule bondRule = bondRuleService.getBondRule(this.getRuleId());
		if(BondRuleEnum.RULE_BUY_ALL.eq(bondRule.getBuyStyle())){
			isBuyAll = true;
		}
		return isBuyAll;
	}
	
	/**
	 * 根据债权金额和折溢价率计算转让的债权价格
	 * 转让价值 * 折溢价率的值  除以 100
	 * @author QianPengZhan
	 * @date 2016年9月22日
	 * @return
	 */
	public BigDecimal getBondPriceMoney(){
		BigDecimal bondPrice = BigDecimal.ZERO;
		if(this.bondMoney == null ||  this.bondApr == null){
			bondPrice = this.bondPrice;
		}else{
			final BigDecimal price = BigDecimalUtils.div(BigDecimalUtils.mul(this.bondMoney,this.bondApr),BigDecimal.valueOf(Constant.DOUBLE_ONE_HUNDRED),Constant.INT_TEN);
			bondPrice = BigDecimalUtils.round(BigDecimalUtils.add(this.bondMoney,price),Constant.INT_TWO);//债权价格
		}
		return bondPrice;
	}
	
	/**
	 * 计算实际支付金额
	 * @author QianPengZhan
	 * @date 2016年9月22日
	 * @return
	 */
	public BigDecimal getReceivedMoneyMethod(){
		BigDecimal receivedMoney = BigDecimal.ZERO;
		if(this.getBondPrice() == null){
			receivedMoney = this.receivedMoney;
		}else{
			receivedMoney =  BigDecimalUtils.sub(BigDecimalUtils.add(this.getBondPrice(),BigDecimalUtils.defaultIfNull(this.getSoldInterest())),
				BigDecimalUtils.defaultIfNull(this.getManageFee()));
		}
		return receivedMoney;
	}

	/**
	 * 获取属性successTime的值
	 * @return successTime属性值
	 */
	public Date getSuccessTime() {
		return successTime;
	}

	/**
	 * 设置属性successTime的值
	 * @param  successTime属性值
	 */
	public void setSuccessTime(Date successTime) {
		this.successTime = successTime;
	}

	/**
	 * 获取属性bondSuccessTime的值
	 * @return bondSuccessTime属性值
	 */
	public Date getBondSuccessTime() {
		return bondSuccessTime;
	}

	/**
	 * 设置属性bondSuccessTime的值
	 * @param  bondSuccessTime属性值
	 */
	public void setBondSuccessTime(Date bondSuccessTime) {
		this.bondSuccessTime = bondSuccessTime;
	}

	/**
	 * 获取属性sellStyle的值
	 * @return sellStyle属性值
	 */
	public String getSellStyle() {
		return sellStyle;
	}

	/**
	 * 设置属性sellStyle的值
	 * @param  sellStyle属性值
	 */
	public void setSellStyle(String sellStyle) {
		this.sellStyle = sellStyle;
	}

	/**
	 * 获取属性stage的值
	 * @return stage属性值
	 */
	public Integer getStage() {
		return stage;
	}

	/**
	 * 设置属性stage的值
	 * @param  stage属性值
	 */
	public void setStage(Integer stage) {
		this.stage = stage;
	}

	/**
	 * 获取属性protocolId的值
	 * @return protocolId属性值
	 */
	public String getProtocolId() {
		return protocolId;
	}

	/**
	 * 设置属性protocolId的值
	 * @param  protocolId属性值
	 */
	public void setProtocolId(String protocolId) {
		this.protocolId = protocolId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}	
}
