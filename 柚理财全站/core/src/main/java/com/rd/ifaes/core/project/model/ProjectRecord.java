/**
* 互联网金融交易系统ifaes (Internet Finance  Assets  Exchange  System)
 * Copyright (c) 2016 杭州融都科技股份有限公司 版权所有
 */
package com.rd.ifaes.core.project.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.rd.ifaes.common.entity.BaseEntity;

/**
 *  理财频道 - 产品列表 
 * @version 3.0
 * @author FangJun
 * @date 2016年8月19日
 */
public class ProjectRecord extends BaseEntity<ProjectRecord> {
 
	private static final long serialVersionUID = -6475486320723735381L;

	private String userId; /* 用户ID */
	private String projectNo; /* 项目编号(YYYYMMDD+5位自增长数字) */
	private String projectName; /* 项目名称 */
	private BigDecimal raiseAccount; /* 筹集目标金额，大于截标后金额 */
	private BigDecimal account; /* 项目总额 */
	private BigDecimal accountYes; /* 已投金额(募集金额) */
	private BigDecimal apr; /* 年利率 */
	private BigDecimal addApr; /* 加息 */
	private String interestStyle; /* 计息方式: 1、成立计息 2、 T+N计息 */
	private Integer fixedRepayDay; /* 固定还款日 */
	private String repayStyle; /* 还款方式: 1月等额本息 2一次性还款 3 每月还息到期还本 4 等额本金 */
	private String timeType; /* 借款期限类型： 0月标 1天标 */
	private Integer timeLimit; /* 借款期限 */
	private Date createTime; /* 添加时间 */
	private Date showTime; /* 上架时间 */
	private Date saleTime; /* 开售时间 */
	private Integer raiseTimeLimit; /* 募集期:募集期时长，单位：天 */
	private Date raiseEndTime; /* 募集结束时间 */
	private String novice; /* 新手标标识： 1新手专享 0 普通 ，默认：0 */
	private String choice; /* 是否精选 0：不是精选，1：精选 */
	private String saleStyle; /* 销售规则 ：1 按金额 2 按份数，默认1 */
	private BigDecimal lowestAccount; /* 最低起投金额 */
	private BigDecimal mostAccount; /* 最高投资总额 */
	private BigDecimal stepAccount; /* 递增金额：整数、以元为单位 */
	private Integer totalCopies; /* 总份数 */
	private BigDecimal copyAccount; /* 每份金额 */
	private Integer lowestCopies; /* 最低起投份数 */
	private Integer mostCopies; /* 最高投资份数 */
	private String riskLevel; /* 产品风险等级 */
	private String redEnvelopeUseful; /* 红包可用标识: 1 红包可用 0 红包不可用 */
	private String additionalRateUseful; /* 加息劵可用标识:1可用 0不可用 */
	private String projectTypeId; /* 理财分类:对应前台的理财产品分类，上架时候选择 */
	private String status; /*  状态: 0 新增 1发布待审 2 发布审核成功(待售) 3发布审核失败4 募集中 5 满额待审（已售） 
	      6 成立审核成功（还款中）8 还款中87 逾期中（还款中）88坏账（还款中） 90部分还款（还款中） 9还款成功（已完成）91逾期还款（已完成）    */
	private String isVouch; /* 是否担保( 1 担保，0 不担保） */
	private String borrowFlag; /* 借款标标识(1是，借款标， 0否，即是理财产品） */
	private String realizeFlag; /* 变现借款标识( 1是产品变现后发布借款，0 普通借款，默认0) */
	private String	saleChannel;		 /* 上架渠道( 1 PC  2 APP 3 微信 ；多选使用拼接,如：1,2,3) */ 
	private String	specificSale;		 /* 定向销售方式(0 不定向 1 定向密码 2 定向等级 3 定向邮箱域名，默认0) */ 
	private String	specificSaleRule;/* 列表只显示定向等级 （VIP等级） */ 
	
	private  Date lastRepayTime; //还款完成的项目，最后还款日
	//产品特殊属性
	private Integer lockTimeLimit; /* 锁定期 */
	private Integer saleTimeLimit; /* 开放期 */
	
	// 其他自定义属性
	private String timeCondition;//项目期限条件：1个月以下（1）1-3个月（2） 3-6个月（3）、6-12个月（4）、12个月以上（5）
	private String  amountCondition;//项目金额条件：1万元以下 (1)1~5万(2) 5~10万(3)10万元以上(4)
    private  String  projectTypeIdStr; /* 多个子类别UUID拼接字符串 */
    private Double remainAccount;/*剩余可投金额*/
    private String projectTypeName;//项目分类名称
    
    private String bondUseful; /* 是否可以债权转让 1 可以，0不可以 */
    private String realizeUseful; /* 是否可变现: 1 可变现 0 不可变现，默认 0 */
    private List<String> specificSaleStatus;//定向销售状态
	private String userAccountType;//用户账号类型  1，柚账户，2京账户
	/**
	 * 还款总期数
	 */
	private Integer  periods;
	/**
	 * 已还期数
	 */
	private Integer  repayedPeriods;
	/**
	 * 是否已预约 1 是 0 否
	 */
	private Integer  bespeak=0;
	
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
	public void setUserId(String userId) {
		this.userId = userId;
	}
	/**
	 * 获取属性projectNo的值
	 * @return projectNo属性值
	 */
	public String getProjectNo() {
		return projectNo;
	}
	/**
	 * 设置属性projectNo的值
	 * @param  projectNo属性值
	 */
	public void setProjectNo(String projectNo) {
		this.projectNo = projectNo;
	}
	/**
	 * 获取属性projectName的值
	 * @return projectName属性值
	 */
	public String getProjectName() {
		return projectName;
	}
	/**
	 * 设置属性projectName的值
	 * @param  projectName属性值
	 */
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	/**
	 * 获取属性raiseAccount的值
	 * @return raiseAccount属性值
	 */
	public BigDecimal getRaiseAccount() {
		return raiseAccount;
	}
	/**
	 * 设置属性raiseAccount的值
	 * @param  raiseAccount属性值
	 */
	public void setRaiseAccount(BigDecimal raiseAccount) {
		this.raiseAccount = raiseAccount;
	}
	/**
	 * 获取属性account的值
	 * @return account属性值
	 */
	public BigDecimal getAccount() {
		return account;
	}
	/**
	 * 设置属性account的值
	 * @param  account属性值
	 */
	public void setAccount(BigDecimal account) {
		this.account = account;
	}
	/**
	 * 获取属性accountYes的值
	 * @return accountYes属性值
	 */
	public BigDecimal getAccountYes() {
		return accountYes;
	}
	/**
	 * 设置属性accountYes的值
	 * @param  accountYes属性值
	 */
	public void setAccountYes(BigDecimal accountYes) {
		this.accountYes = accountYes;
	}
	/**
	 * 获取属性apr的值
	 * @return apr属性值
	 */
	public BigDecimal getApr() {
		return apr;
	}
	/**
	 * 设置属性apr的值
	 * @param  apr属性值
	 */
	public void setApr(BigDecimal apr) {
		this.apr = apr;
	}
	/**
	 * 获取属性addApr的值
	 * @return addApr属性值
	 */
	public BigDecimal getAddApr() {
		return addApr;
	}
	/**
	 * 设置属性addApr的值
	 * @param  addApr属性值
	 */
	public void setAddApr(BigDecimal addApr) {
		this.addApr = addApr;
	}
	/**
	 * 获取属性interestStyle的值
	 * @return interestStyle属性值
	 */
	public String getInterestStyle() {
		return interestStyle;
	}
	/**
	 * 设置属性interestStyle的值
	 * @param  interestStyle属性值
	 */
	public void setInterestStyle(String interestStyle) {
		this.interestStyle = interestStyle;
	}
 
	/**
	 * 获取属性fixedRepayDay的值
	 * @return fixedRepayDay属性值
	 */
	public Integer getFixedRepayDay() {
		return fixedRepayDay;
	}
	/**
	 * 设置属性fixedRepayDay的值
	 * @param  fixedRepayDay属性值
	 */
	public void setFixedRepayDay(Integer fixedRepayDay) {
		this.fixedRepayDay = fixedRepayDay;
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
	public void setRepayStyle(String repayStyle) {
		this.repayStyle = repayStyle;
	}
	/**
	 * 获取属性timeType的值
	 * @return timeType属性值
	 */
	public String getTimeType() {
		return timeType;
	}
	/**
	 * 设置属性timeType的值
	 * @param  timeType属性值
	 */
	public void setTimeType(String timeType) {
		this.timeType = timeType;
	}
	/**
	 * 获取属性timeLimit的值
	 * @return timeLimit属性值
	 */
	public Integer getTimeLimit() {
		return timeLimit;
	}
	/**
	 * 设置属性timeLimit的值
	 * @param  timeLimit属性值
	 */
	public void setTimeLimit(Integer timeLimit) {
		this.timeLimit = timeLimit;
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
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	/**
	 * 获取属性showTime的值
	 * @return showTime属性值
	 */
	public Date getShowTime() {
		return showTime;
	}
	/**
	 * 设置属性showTime的值
	 * @param  showTime属性值
	 */
	public void setShowTime(Date showTime) {
		this.showTime = showTime;
	}
	/**
	 * 获取属性saleTime的值
	 * @return saleTime属性值
	 */
	public Date getSaleTime() {
		return saleTime;
	}
	/**
	 * 设置属性saleTime的值
	 * @param  saleTime属性值
	 */
	public void setSaleTime(Date saleTime) {
		this.saleTime = saleTime;
	}
	/**
	 * 获取属性raiseTimeLimit的值
	 * @return raiseTimeLimit属性值
	 */
	public Integer getRaiseTimeLimit() {
		return raiseTimeLimit;
	}
	/**
	 * 设置属性raiseTimeLimit的值
	 * @param  raiseTimeLimit属性值
	 */
	public void setRaiseTimeLimit(Integer raiseTimeLimit) {
		this.raiseTimeLimit = raiseTimeLimit;
	}
	/**
	 * 获取属性raiseEndTime的值
	 * @return raiseEndTime属性值
	 */
	public Date getRaiseEndTime() {
		return raiseEndTime;
	}
	/**
	 * 设置属性raiseEndTime的值
	 * @param  raiseEndTime属性值
	 */
	public void setRaiseEndTime(Date raiseEndTime) {
		this.raiseEndTime = raiseEndTime;
	}
	/**
	 * 获取属性novice的值
	 * @return novice属性值
	 */
	public String getNovice() {
		return novice;
	}
	/**
	 * 设置属性novice的值
	 * @param  novice属性值
	 */
	public void setNovice(String novice) {
		this.novice = novice;
	}
	/**
	 * 获取属性choice的值
	 * @return choice属性值
	 */
	public String getChoice() {
		return choice;
	}
	/**
	 * 设置属性choice的值
	 * @param  choice属性值
	 */
	public void setChoice(String choice) {
		this.choice = choice;
	}
	/**
	 * 获取属性saleStyle的值
	 * @return saleStyle属性值
	 */
	public String getSaleStyle() {
		return saleStyle;
	}
	/**
	 * 设置属性saleStyle的值
	 * @param  saleStyle属性值
	 */
	public void setSaleStyle(String saleStyle) {
		this.saleStyle = saleStyle;
	}
	/**
	 * 获取属性lowestAccount的值
	 * @return lowestAccount属性值
	 */
	public BigDecimal getLowestAccount() {
		return lowestAccount;
	}
	/**
	 * 设置属性lowestAccount的值
	 * @param  lowestAccount属性值
	 */
	public void setLowestAccount(BigDecimal lowestAccount) {
		this.lowestAccount = lowestAccount;
	}
	/**
	 * 获取属性mostAccount的值
	 * @return mostAccount属性值
	 */
	public BigDecimal getMostAccount() {
		return mostAccount;
	}
	/**
	 * 设置属性mostAccount的值
	 * @param  mostAccount属性值
	 */
	public void setMostAccount(BigDecimal mostAccount) {
		this.mostAccount = mostAccount;
	}
	/**
	 * 获取属性stepAccount的值
	 * @return stepAccount属性值
	 */
	public BigDecimal getStepAccount() {
		return stepAccount;
	}
	/**
	 * 设置属性stepAccount的值
	 * @param  stepAccount属性值
	 */
	public void setStepAccount(BigDecimal stepAccount) {
		this.stepAccount = stepAccount;
	}
	/**
	 * 获取属性totalCopies的值
	 * @return totalCopies属性值
	 */
	public Integer getTotalCopies() {
		return totalCopies;
	}
	/**
	 * 设置属性totalCopies的值
	 * @param  totalCopies属性值
	 */
	public void setTotalCopies(Integer totalCopies) {
		this.totalCopies = totalCopies;
	}
	/**
	 * 获取属性copyAccount的值
	 * @return copyAccount属性值
	 */
	public BigDecimal getCopyAccount() {
		return copyAccount;
	}
	/**
	 * 设置属性copyAccount的值
	 * @param  copyAccount属性值
	 */
	public void setCopyAccount(BigDecimal copyAccount) {
		this.copyAccount = copyAccount;
	}
	/**
	 * 获取属性lowestCopies的值
	 * @return lowestCopies属性值
	 */
	public Integer getLowestCopies() {
		return lowestCopies;
	}
	/**
	 * 设置属性lowestCopies的值
	 * @param  lowestCopies属性值
	 */
	public void setLowestCopies(Integer lowestCopies) {
		this.lowestCopies = lowestCopies;
	}
	/**
	 * 获取属性mostCopies的值
	 * @return mostCopies属性值
	 */
	public Integer getMostCopies() {
		return mostCopies;
	}
	/**
	 * 设置属性mostCopies的值
	 * @param  mostCopies属性值
	 */
	public void setMostCopies(Integer mostCopies) {
		this.mostCopies = mostCopies;
	}
	/**
	 * 获取属性riskLevel的值
	 * @return riskLevel属性值
	 */
	public String getRiskLevel() {
		return riskLevel;
	}
	/**
	 * 设置属性riskLevel的值
	 * @param  riskLevel属性值
	 */
	public void setRiskLevel(String riskLevel) {
		this.riskLevel = riskLevel;
	}
	/**
	 * 获取属性redEnvelopeUseful的值
	 * @return redEnvelopeUseful属性值
	 */
	public String getRedEnvelopeUseful() {
		return redEnvelopeUseful;
	}
	/**
	 * 设置属性redEnvelopeUseful的值
	 * @param  redEnvelopeUseful属性值
	 */
	public void setRedEnvelopeUseful(String redEnvelopeUseful) {
		this.redEnvelopeUseful = redEnvelopeUseful;
	}
	/**
	 * 获取属性additionalRateUseful的值
	 * @return additionalRateUseful属性值
	 */
	public String getAdditionalRateUseful() {
		return additionalRateUseful;
	}
	/**
	 * 设置属性additionalRateUseful的值
	 * @param  additionalRateUseful属性值
	 */
	public void setAdditionalRateUseful(String additionalRateUseful) {
		this.additionalRateUseful = additionalRateUseful;
	}
	/**
	 * 获取属性projectTypeId的值
	 * @return projectTypeId属性值
	 */
	public String getProjectTypeId() {
		return projectTypeId;
	}
	/**
	 * 设置属性projectTypeId的值
	 * @param  projectTypeId属性值
	 */
	public void setProjectTypeId(String projectTypeId) {
		this.projectTypeId = projectTypeId;
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
	public void setStatus(String status) {
		this.status = status;
	}
	/**
	 * 获取属性isVouch的值
	 * @return isVouch属性值
	 */
	public String getIsVouch() {
		return isVouch;
	}
	/**
	 * 设置属性isVouch的值
	 * @param  isVouch属性值
	 */
	public void setIsVouch(String isVouch) {
		this.isVouch = isVouch;
	}
	/**
	 * 获取属性borrowFlag的值
	 * @return borrowFlag属性值
	 */
	public String getBorrowFlag() {
		return borrowFlag;
	}
	/**
	 * 设置属性borrowFlag的值
	 * @param  borrowFlag属性值
	 */
	public void setBorrowFlag(String borrowFlag) {
		this.borrowFlag = borrowFlag;
	}
	/**
	 * 获取属性realizeFlag的值
	 * @return realizeFlag属性值
	 */
	public String getRealizeFlag() {
		return realizeFlag;
	}
	/**
	 * 设置属性realizeFlag的值
	 * @param  realizeFlag属性值
	 */
	public void setRealizeFlag(String realizeFlag) {
		this.realizeFlag = realizeFlag;
	}
	/**
	 * 获取属性saleChannel的值
	 * @return saleChannel属性值
	 */
	public String getSaleChannel() {
		return saleChannel;
	}
	/**
	 * 设置属性saleChannel的值
	 * @param  saleChannel属性值
	 */
	public void setSaleChannel(String saleChannel) {
		this.saleChannel = saleChannel;
	}
	/**
	 * 获取属性specificSale的值
	 * @return specificSale属性值
	 */
	public String getSpecificSale() {
		return specificSale;
	}
	/**
	 * 设置属性specificSale的值
	 * @param  specificSale属性值
	 */
	public void setSpecificSale(String specificSale) {
		this.specificSale = specificSale;
	}
	 
	/**
	 * 获取属性timeCondition的值
	 * @return timeCondition属性值
	 */
	public String getTimeCondition() {
		return timeCondition;
	}
	/**
	 * 设置属性timeCondition的值
	 * @param  timeCondition属性值
	 */
	public void setTimeCondition(String timeCondition) {
		this.timeCondition = timeCondition;
	}
	/**
	 * 获取属性amountCondition的值
	 * @return amountCondition属性值
	 */
	public String getAmountCondition() {
		return amountCondition;
	}
	/**
	 * 设置属性amountCondition的值
	 * @param  amountCondition属性值
	 */
	public void setAmountCondition(String amountCondition) {
		this.amountCondition = amountCondition;
	}
	/**
	 * 获取属性periods的值
	 * @return periods属性值
	 */
	public Integer getPeriods() {
		return periods;
	}
	/**
	 * 设置属性periods的值
	 * @param  periods属性值
	 */
	public void setPeriods(Integer periods) {
		this.periods = periods;
	}
	/**
	 * 获取属性repayedPeriods的值
	 * @return repayedPeriods属性值
	 */
	public Integer getRepayedPeriods() {
		return repayedPeriods;
	}
	/**
	 * 设置属性repayedPeriods的值
	 * @param  repayedPeriods属性值
	 */
	public void setRepayedPeriods(Integer repayedPeriods) {
		this.repayedPeriods = repayedPeriods;
	}
	/**
	 * 获取属性bespeak的值
	 * @return bespeak属性值
	 */
	public Integer getBespeak() {
		return bespeak;
	}
	/**
	 * 设置属性bespeak的值
	 * @param  bespeak属性值
	 */
	public void setBespeak(Integer bespeak) {
		this.bespeak = bespeak;
	}
	/**
	 * 获取属性projectTypeIdStr的值
	 * @return projectTypeIdStr属性值
	 */
	@JsonIgnore
	public String getProjectTypeIdStr() {
		return projectTypeIdStr;
	}

	/**
	 * 设置属性projectTypeIdStr的值
	 * @param  projectTypeIdStr属性值
	 */
	public void setProjectTypeIdStr(String projectTypeIdStr) {
		this.projectTypeIdStr = projectTypeIdStr;
	}
	/**
	 * 获取属性lockTimeLimit的值
	 * @return lockTimeLimit属性值
	 */
	public Integer getLockTimeLimit() {
		return lockTimeLimit;
	}
	/**
	 * 设置属性lockTimeLimit的值
	 * @param  lockTimeLimit属性值
	 */
	public void setLockTimeLimit(Integer lockTimeLimit) {
		this.lockTimeLimit = lockTimeLimit;
	}
	/**
	 * 获取属性saleTimeLimit的值
	 * @return saleTimeLimit属性值
	 */
	public Integer getSaleTimeLimit() {
		return saleTimeLimit;
	}
	/**
	 * 设置属性saleTimeLimit的值
	 * @param  saleTimeLimit属性值
	 */
	public void setSaleTimeLimit(Integer saleTimeLimit) {
		this.saleTimeLimit = saleTimeLimit;
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
	public void setLastRepayTime(Date lastRepayTime) {
		this.lastRepayTime = lastRepayTime;
	}
	/**
	 * 获取属性remainAccount的值
	 * @return remainAccount属性值
	 */
	public Double getRemainAccount() {
		return remainAccount;
	}
	/**
	 * 设置属性remainAccount的值
	 * @param  remainAccount属性值
	 */
	public void setRemainAccount(Double remainAccount) {
		this.remainAccount = remainAccount;
	}
	/**
	 * 获取项目分类名称 
	 * @return 项目分类名称 
	 */
	public String getProjectTypeName() {
		return projectTypeName;
	}
	/**
	 * 设置项目分类名称
	 * @param  项目分类名称
	 */
	public void setProjectTypeName(String projectTypeName) {
		this.projectTypeName = projectTypeName;
	}
	public String getBondUseful() {
		return bondUseful;
	}
	public void setBondUseful(String bondUseful) {
		this.bondUseful = bondUseful;
	}
	public String getRealizeUseful() {
		return realizeUseful;
	}
	public void setRealizeUseful(String realizeUseful) {
		this.realizeUseful = realizeUseful;
	}
	public String getSpecificSaleRule() {
		return specificSaleRule;
	}
	public void setSpecificSaleRule(String specificSaleRule) {
		this.specificSaleRule = specificSaleRule;
	}
	public String getUserAccountType() {
		return userAccountType;
	}
	public void setUserAccountType(String userAccountType) {
		this.userAccountType = userAccountType;
	}
	public List<String> getSpecificSaleStatus() {
		return specificSaleStatus;
	}
	public void setSpecificSaleStatus(List<String> specificSaleStatus) {
		this.specificSaleStatus = specificSaleStatus;
	}
	
}
