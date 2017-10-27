/**
* 互联网金融交易系统ifaes (Internet Finance  Assets  Exchange  System)
 * Copyright (c) 2016 杭州融都科技股份有限公司 版权所有
 */
package com.rd.ifaes.core.project.domain;

import java.math.BigDecimal;
import java.util.Date;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import com.rd.ifaes.common.annotation.DictType;
import com.rd.ifaes.common.constant.CbhbConstant;
import com.rd.ifaes.common.dict.CommonEnum;
import com.rd.ifaes.common.dict.LoanEnum;
import com.rd.ifaes.common.entity.BaseEntity;
import com.rd.ifaes.common.exception.BussinessException;
import com.rd.ifaes.common.util.BigDecimalUtils;
import com.rd.ifaes.common.util.DateUtils;
import com.rd.ifaes.common.util.HtmlUtils;
import com.rd.ifaes.common.util.NumberUtils;
import com.rd.ifaes.common.util.SpringContextHolder;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.core.core.constant.ConfigConstant;
import com.rd.ifaes.core.core.constant.Constant;
import com.rd.ifaes.core.core.constant.ProjectConstant;
import com.rd.ifaes.core.core.constant.ResourceConstant;
import com.rd.ifaes.core.core.util.ConfigUtils;
import com.rd.ifaes.core.core.util.ResourceUtils;
import com.rd.ifaes.core.interest.InterestCalculator;
import com.rd.ifaes.core.user.domain.User;
import com.rd.ifaes.core.user.domain.UserIdentify;
import com.rd.ifaes.core.user.model.UserFreezeModel;
import com.rd.ifaes.core.user.service.UserFreezeService;
import com.rd.ifaes.core.user.service.UserIdentifyService;
import com.rd.ifaes.core.user.service.UserService;

/**
 * entity:Project
 * 
 * @author FangJun
 * @version 3.0
 * @date 2016-6-22
 */
public abstract class ProjectEntity<T> extends BaseEntity<T> {

	private static final long serialVersionUID = 1L;

	protected String userId; /* 用户ID */
	protected String projectNo; /* 项目编号(YYYYMMDD+5位自增长数字) */
	protected String projectName; /* 项目名称 */
	@DictType(type="project_type_manage")
	protected String projectType; /* 标的类别 */
	protected BigDecimal raiseAccount; /* 筹集目标金额，大于截标后金额 */
	@NotNull(message="{"+ResourceConstant.PROJECT_ACCOUNT_NOTEMPTY+"}")
	protected BigDecimal account; /* 项目总额 */
	protected BigDecimal accountYes; /* 已投金额(募集金额) */
	@NotNull(message="{"+ResourceConstant.PROJECT_APR_NOTEMPTY+"}")
	protected BigDecimal apr; /* 年利率 */
	protected BigDecimal addApr; /* 加息 */
	@DictType(type="interestStyle")
	@NotEmpty(message="{"+ResourceConstant.PROJECT_INTEREST_STYLE_NOT_EMPTY+"}")
	protected String interestStyle; /* 计息方式: 1、成立计息 2、 T+N计息 */
	protected Integer interestStartDays; /* 起始计息天数(T+N计息方式 ) */
	protected Integer fixedRepayDay; /* 固定还款日 */
	@NotEmpty(message="{"+ResourceConstant.PROJECT_REPAY_STYLE_NOT_EMPTY+"}")
	@DictType(type="repayStyle")
	protected String repayStyle; /* 还款方式: 1月等额本息 2一次性还款 3 每月还息到期还本 4 等额本金 */
	@NotEmpty(message ="{"+ResourceConstant.PROJECT_TIME_TYPE_NOT_EMPTY+"}")
	protected String timeType; /* 借款期限类型： 0月标 1天标 */
	protected Integer timeLimit; /* 借款期限 */
	protected Date createTime; /* 添加时间 */
	protected Date publishVerifyTime;/*发布审核时间(或借款审核时间) */
	protected Date updateTime;/*更新时间 */
	protected Date stopTime; /* 下架时间 */
	protected Date showTime; /* 上架时间 */
	protected Date saleTime; /* 开售时间 */
	protected Integer raiseTimeLimit; /* 募集期:募集期时长，单位：天 */
	protected Date raiseEndTime; /* 募集结束时间 */
	protected Date reviewTime; /* 成立审核时间 */
	protected Date vouchVerifyTime; /* 担保审核时间 */
	protected BigDecimal borrowManageRate; /* 借款手续费率 */
	protected BigDecimal interestManageRate; /* 利息管理费率 */
	protected BigDecimal overdueFeeRate;/*逾期罚息利率*/
	protected String novice; /* 新手标标识： 1新手专享 0 普通 ，默认：0 */
	protected String choice; /* 是否精选 0：不是精选，1：精选 */
	protected String saleStyle; /* 销售规则 ：1 按金额 2 按份数，默认1 */
	@NotNull(message="{"+ResourceConstant.PROJECT_LOWEST_ACCOUNT_NOT_EMPTY+"}")
	protected BigDecimal lowestAccount; /* 最低起投金额 */
	protected BigDecimal mostAccount; /* 最高投资总额 */
	private BigDecimal stepAccount; /* 递增金额：整数、以元为单位 */
	protected Integer totalCopies; /* 总份数 */
	protected BigDecimal copyAccount; /* 每份金额 */
	protected Integer lowestCopies; /* 最低起投份数 */
	protected Integer mostCopies; /* 最高投资份数 */
	@NotBlank(message="{"+ResourceConstant.PROJECT_RISK_LEVEL_NOT_EMPTY+"}")
	protected String riskLevel; /* 产品风险等级 */
	
	protected String redenvelopeRuleId;	/*红包发放规则ID*/ 
	protected String rateCouponRuleId;	/*加息券发放规则ID*/ 	
	protected String redEnvelopeUseful; /* 红包可用标识: 1 红包可用 0 红包不可用 */
	protected BigDecimal redEnvelopeRate;/* 红包可用最大比例 */
	protected String additionalRateUseful; /* 加息劵可用标识:1可用 0不可用 */
	protected String projectTypeId; /* 理财分类:对应前台的理财产品分类，上架时候选择 */
	protected String protocolId;/*产品协议ID*/
	@DictType(type="borrowStatus")
	protected String status; /*  状态: 0 新增 1发布待审 2 发布审核成功(待售) 3发布审核失败4 募集中 5 满额待审（已售） 
                    6 成立审核成功（还款中）8 还款中87 逾期中（还款中）88坏账（还款中） 90部分还款（还款中） 9还款成功（已完成）91逾期还款（已完成）    */
	protected String isVouch; /* 是否担保( 1 担保，0 不担保） */
	protected String vouchId; /* 担保公司ID */
	protected String vouchStatus;/*担保用户审核状态*/
	protected String borrowFlag; /* 借款标标识(1是，借款标， 0否，即是理财产品） */
	protected String realizeFlag; /* 变现借款标识( 1是产品变现后发布借款，0 普通借款，默认0) */
	protected String deleteFlag; /* 0 未删除，1 已删除 */
	
	protected String	saleChannel;		 /* 上架渠道( 1 PC  2 APP 3 微信 ；多选使用拼接,如：1,2,3) */ 
	protected String	specificSale;		 /* 定向销售方式(0 不定向 1 定向密码 2 定向等级 3 定向邮箱域名，默认0) */ 
	protected String	specificSaleRule;		 /* 定向销售规则（对应规则内容,如：具体定向密码、定向邮箱域名） */ 
	
	protected BigDecimal	repayAmount;		 /* 预还本息 */ 
	protected BigDecimal	repayYesAccount;		 /* 实还本金 */ 
	protected BigDecimal	repayYesInterest;		 /* 实还利息 */ 
	protected String interestTime;	/*计息临界时间点(T+N计息，最长格式HH:MM:SS，暂到小时HH)*/
	protected Integer totalPeriod;/**项目总期数*/
	protected Integer repayedPeriod;/**项目已还期数*/
	protected Date nextRepayTime;/**本期回款时间*/
	protected Date lastRepayTime;/**最后一期回款时间*/
	protected Date realLastRepayTime;/**最后一期实际回款时间*/
	protected Integer stage;
	
	private String timeCondition;//项目期限条件：6个月以下（6）、6-12个月（4）、12个月以上（5）
	
	protected String  amountCondition;//项目金额条件：1万元以下 (1)1~5万(2) 5~10万(3)10万元以上(4)
	
	//审核意见
	private String verifyRemark;
	//审核人员
	private String operatorName;
	String riskLevelName;//风险等级名称
	
	/**
	 * 状态统计数量
	 */
	private int statusCount;
	
	// Constructor
	public ProjectEntity() {
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getProjectNo() {
		return projectNo;
	}

	public void setProjectNo(String projectNo) {
		this.projectNo = projectNo;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		if(projectName!=null){
			this.projectName = HtmlUtils.htmlUnescape(projectName);
		}
	}

	public BigDecimal getRaiseAccount() {
		return raiseAccount;
	}

	public void setRaiseAccount(BigDecimal raiseAccount) {
		this.raiseAccount = raiseAccount;
	}

	public BigDecimal getAccount() {
		return account;
	}

	public void setAccount(BigDecimal account) {
		this.account = account;
	}

	public BigDecimal getAccountYes() {
		return accountYes;
	}

	public void setAccountYes(BigDecimal accountYes) {
		this.accountYes = accountYes;
	}

	public BigDecimal getApr() {
		return apr;
	}

	public void setApr(BigDecimal apr) {
		this.apr = apr;
	}

	public BigDecimal getAddApr() {
		return addApr;
	}

	public void setAddApr(BigDecimal addApr) {
		this.addApr = addApr;
	}

	public String getInterestStyle() {
		return interestStyle;
	}

	public void setInterestStyle(String interestStyle) {
		this.interestStyle = interestStyle;
	}

	public Integer getInterestStartDays() {
		return interestStartDays;
	}

	public void setInterestStartDays(Integer interestStartDays) {
		this.interestStartDays = interestStartDays;
	}

	public Integer getFixedRepayDay() {
		return fixedRepayDay;
	}

	public void setFixedRepayDay(Integer fixedRepayDay) {
		this.fixedRepayDay = fixedRepayDay;
	}

	public String getRepayStyle() {
		return repayStyle;
	}
   /**
    * 还款方式: 1月等额本息 2一次性还款 3 每月还息到期还本 4 等额本金  
    * @author  FangJun
    * @date 2016年9月21日
    * @param repayStyle 还款方式
    */
	public void setRepayStyle(String repayStyle) {
		this.repayStyle = repayStyle;
	}

	public String getTimeType() {
		return timeType;
	}

	public void setTimeType(String timeType) {
		this.timeType = timeType;
	}

	public Integer getTimeLimit() {
		return timeLimit;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getTimeLimitStr() {
		if(LoanEnum.TIME_TYPE_MONTH.eq(timeType)){
			return timeLimit + "个月";
		}else{
			return timeLimit + "天";
		}
	}
	
	/**
	 * 期限（？天）（timeLimitSort）
	 * @return
	 */
	public Integer getTimeLimitSort() {
		if(LoanEnum.TIME_TYPE_MONTH.eq(timeType)){
			int daysOfMonth = ConfigUtils.getInt(ConfigConstant.DAYS_OF_MONTH);
			if(daysOfMonth == 0){
				daysOfMonth =InterestCalculator.DEFAULT_DAYS_OF_MONTH.intValue();
			}
			return timeLimit.intValue() * daysOfMonth;
		}
		return timeLimit;
	}

	public void setTimeLimit(Integer timeLimit) {
		this.timeLimit = timeLimit;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getStopTime() {
		return stopTime;
	}

	public void setStopTime(Date stopTime) {
		this.stopTime = stopTime;
	}

	public Date getShowTime() {
		return showTime;
	}

	public void setShowTime(Date showTime) {
		this.showTime = showTime;
	}

	public Date getSaleTime() {
		return saleTime;
	}

	public void setSaleTime(Date saleTime) {
		this.saleTime = saleTime;
	}

	public Integer getRaiseTimeLimit() {
		return raiseTimeLimit;
	}
	
	public void setRaiseTimeLimit(Integer raiseTimeLimit) {
		 this.raiseTimeLimit = raiseTimeLimit;
	}

	public Date getRaiseEndTime() {
		return raiseEndTime;
	}

	public void setRaiseEndTime(Date raiseEndTime) {
		this.raiseEndTime = raiseEndTime;
	}

	public Date getReviewTime() {
		return reviewTime;
	}

	public void setReviewTime(Date reviewTime) {
		this.reviewTime = reviewTime;
	}

	public BigDecimal getBorrowManageRate() {
		return borrowManageRate;
	}

	public void setBorrowManageRate(BigDecimal borrowManageRate) {
		this.borrowManageRate = borrowManageRate;
	}

	public BigDecimal getInterestManageRate() {
		return interestManageRate;
	}

	public void setInterestManageRate(BigDecimal interestManageRate) {
		this.interestManageRate = interestManageRate;
	}
 
	public String getNovice() {
		return novice;
	}

	public void setNovice(String novice) {
		this.novice = novice;
	}

	public String getChoice() {
		return choice;
	}

	public void setChoice(String choice) {
		this.choice = choice;
	}

	public String getSaleStyle() {
		return saleStyle;
	}

	public void setSaleStyle(String saleStyle) {
		this.saleStyle = saleStyle;
	}

	public BigDecimal getLowestAccount() {
		return lowestAccount;
	}

	public void setLowestAccount(BigDecimal lowestAccount) {
		this.lowestAccount = lowestAccount;
	}
	public BigDecimal getStepAccount() {
		return stepAccount;
	}

	public void setStepAccount(BigDecimal stepAccount) {
		this.stepAccount = stepAccount;
	}
	public BigDecimal getMostAccount() {
		return mostAccount;
	}

	public void setMostAccount(BigDecimal mostAccount) {
		this.mostAccount = mostAccount;
	}

	public Integer getTotalCopies() {
		return totalCopies;
	}

	public void setTotalCopies(Integer totalCopies) {
		this.totalCopies = totalCopies;
	}

	public BigDecimal getCopyAccount() {
		return copyAccount;
	}

	public void setCopyAccount(BigDecimal copyAccount) {
		this.copyAccount = copyAccount;
	}

	public Integer getLowestCopies() {
		return lowestCopies;
	}

	public void setLowestCopies(Integer lowestCopies) {
		this.lowestCopies = lowestCopies;
	}

	public Integer getMostCopies() {
		return mostCopies;
	}

	public void setMostCopies(Integer mostCopies) {
		this.mostCopies = mostCopies;
	}

	public String getRiskLevel() {
		return riskLevel;
	}

	public void setRiskLevel(String riskLevel) {
		this.riskLevel = riskLevel;
	}
	
	public String getRiskLevelName() {
		return riskLevelName;
	}

	public void setRiskLevelName(String riskLevelName) {
		this.riskLevelName = riskLevelName;
	}

	public String getRedEnvelopeUseful() {
		return redEnvelopeUseful;
	}

	public void setRedEnvelopeUseful(String redEnvelopeUseful) {
		   this.redEnvelopeUseful = redEnvelopeUseful;
	}
	/**
	 * 获取属性redEnvelopeRate的值
	 * @return redEnvelopeRate属性值
	 */
	public BigDecimal getRedEnvelopeRate() {
		return redEnvelopeRate;
	}
	/**
	 * 设置属性redEnvelopeRate的值
	 * @param  redEnvelopeRate属性值
	 */
	public void setRedEnvelopeRate(BigDecimal redEnvelopeRate) {
		this.redEnvelopeRate = redEnvelopeRate;
	}

	public String getAdditionalRateUseful() {
		return additionalRateUseful;
	}

	public void setAdditionalRateUseful(String additionalRateUseful) {
	     this.additionalRateUseful = additionalRateUseful;
	}

	public String getProjectTypeId() {
		return projectTypeId;
	}

	public void setProjectTypeId(String projectTypeId) {
		this.projectTypeId = projectTypeId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getIsVouch() {
		return isVouch;
	}

	public void setIsVouch(String isVouch) {
		this.isVouch = isVouch;
	}

	public String getVouchId() {
		return vouchId;
	}

	public void setVouchId(String vouchId) {
		this.vouchId = vouchId;
	}

	public String getBorrowFlag() {
		return borrowFlag;
	}

	public void setBorrowFlag(String borrowFlag) {
		this.borrowFlag = borrowFlag;
	}

	public String getRealizeFlag() {
		return realizeFlag;
	}

	public void setRealizeFlag(String realizeFlag) {
		this.realizeFlag = realizeFlag;
	}

	/**
	 * 基础数据校验
	 */
	public void validBase() {
		// 校验金额、利率、期限
		validAmountApr();
		// 销售方式校验（按份数、按金额）
		validSaleStyle();
		// 校验固定还款日
		validFixedRepayDay();
		// 还款方式信息校验
		validRepayStyle();
		// 校验借款人
		validBorrower();
	}

	/**
	 * 校验金额、利率 、期限
	 * @author  FangJun
	 * @date 2016年9月21日
	 */
	private void validAmountApr() {
		// 金额区间校验
		double borrowMinAccount = ConfigUtils.getDouble(ConfigConstant.BORROW_LOWEST_ACCOUNT);
		double borrowMaxAccount = ConfigUtils.getDouble(ConfigConstant.BORROW_MOST_ACCOUNT);
		if (borrowMinAccount > 0
				&& (borrowMinAccount > this.account.doubleValue() || borrowMaxAccount < this.account.doubleValue())) {
			String key = (LoanEnum.BORROW_FLAG_BORROW.eq(this.borrowFlag)) ? ResourceConstant.BORROW_ACCOUNT_RANGE_ERROR
					: ResourceConstant.PRODUCT_ACCOUNT_RANGE_ERROR;
			throw new BussinessException(ResourceUtils.get(key, borrowMinAccount, borrowMaxAccount));
		}
		// 金额必须为整数
		if (!BigDecimalUtils.modZero(account, BigDecimal.ONE)) {
			throw new BussinessException(ResourceUtils.get(ResourceConstant.PROJECT_ACCOUNT_MUSTINT));
		}

		// 年化利率区间校验
		double borrowMinApr = ConfigUtils.getDouble(ConfigConstant.BORROW_LOWEST_APR);
		double borrowMaxApr = ConfigUtils.getDouble(ConfigConstant.BORROW_MOST_APR);
		if (borrowMinApr > 0 && borrowMaxApr > 0
				&& (borrowMinApr > this.apr.doubleValue() || borrowMaxApr < this.apr.doubleValue())) {
			throw new BussinessException(ResourceUtils.get(ResourceConstant.PROJECT_APR_RANGE_ERROR, borrowMinApr, borrowMaxApr));

		}
		//年化利率最多保留两位小数
		if (!BigDecimalUtils.modZero(apr.multiply(BigDecimal.valueOf(100)), BigDecimal.ONE)) {
			throw new BussinessException(ResourceUtils.get(ResourceConstant.PROJECT_APR_REMAINTWODECIMAL));
		}
		
		int maxYear = ConfigUtils.getInt(ConfigConstant.PROJECT_TIME_LIMIT_MAX_YEAR);
		
		//借款期限校验、利息VS本金
		if (LoanEnum.TIME_TYPE_DAY.eq(this.timeType)){
			int daysOfYear = ConfigUtils.getInt(ConfigConstant.DAYS_OF_YEAR);
			int maxDays = daysOfYear * maxYear;
			if(timeLimit.intValue() > maxDays){
				throw new BussinessException(StringUtils.format(ResourceUtils.get(ResourceConstant.PROJECT_TIME_LIMIT_OUTOFDAYS), maxDays));
			}
			
			if(apr.doubleValue() * timeLimit.intValue()/daysOfYear >= Constant.INT_ONE_HUNDRED){
				throw new BussinessException(ResourceUtils.get(ResourceConstant.PROJECT_INTEREST_MUSTLT_CAPITAL));
			}			
			
		}else{
			int monthsOfYear = InterestCalculator.MONTHS_OF_FULL_YEAR.intValue();
			int maxMonths = maxYear * monthsOfYear;
			if(timeLimit.intValue() > maxMonths){
				throw new BussinessException(StringUtils.format(ResourceUtils.get(ResourceConstant.PROJECT_TIME_LIMIT_OUTOFMONTHS), maxMonths));
			}
			if(apr.doubleValue() * timeLimit.intValue()/monthsOfYear >= Constant.INT_ONE_HUNDRED){
				throw new BussinessException(ResourceUtils.get(ResourceConstant.PROJECT_INTEREST_MUSTLT_CAPITAL));
			}
			
		}		
		
	}

	/**
	 * 校验固定还款日
	 * @author  FangJun
	 * @date 2016年9月21日
	 */
	private void validFixedRepayDay() {
		// 固定还款日校验
		if (LoanEnum.STYLE_ONETIME_REPAYMENT.getValue().equals(this.repayStyle)) {
			this.fixedRepayDay = null;// 一次性还款无固定还款日
		}
		// 月均天数
		BigDecimal daysOfMonth = ConfigUtils.getBigDecimal(ConfigConstant.DAYS_OF_MONTH);
		if (daysOfMonth == null) {
			daysOfMonth = InterestCalculator.DEFAULT_DAYS_OF_MONTH;
		}
		if (!NumberUtils.isDefault(this.fixedRepayDay)
				&& (this.fixedRepayDay < 1 || this.fixedRepayDay > daysOfMonth.intValue())) {
			throw new BussinessException(
					ResourceUtils.get(ResourceConstant.PROJECT_FIXED_REPAY_DAY_GT_DAYSOFMONTH, daysOfMonth.intValue()));
		}
	}

	/**
	 * 借款人信息校验
	 * @author  FangJun
	 * @date 2016年9月21日
	 */
	private void validBorrower() {
		//校验借款人是否存在
		if(StringUtils.isNotBlank(userId)){
			//该用户是否存在
			UserService userService = SpringContextHolder.getBean("userService");
			if(userService.getCount(new User(userId)) <= 0){
				throw new BussinessException(ResourceUtils.get(ResourceConstant.PRODUCT_BORROWER_NOEXISTS));
			}
			
			//该用户是否进行实名认证
			UserIdentifyService userIdentifyService = SpringContextHolder.getBean("userIdentifyService");
			UserIdentify ui = new UserIdentify(userId);
			ui.setRealNameStatus(Constant.FLAG_YES);			
			if(userIdentifyService.getCount(ui) <= 0){
				throw new BussinessException(ResourceUtils.get(ResourceConstant.PRODUCT_BORROWER_NOAUTH));
			}	
			
			//用户借款冻结校验
			UserFreezeService userFreezeService = (UserFreezeService)SpringContextHolder.getBean("userFreezeService");
			if(userFreezeService.isFreezed(userId, UserFreezeModel.STATUS_USER_FREEZE_LOAN)){
				throw new BussinessException(ResourceUtils.get(ResourceConstant.PRODUCT_BORROWER_LOAN_FREEZE));
			}
		}else{
			throw new BussinessException(ResourceUtils.get(ResourceConstant.PRODUCT_BORROWER_NOEXISTS));
		}
	}

	/**
	 * 还款方式/计息方式校验
	 * @author  FangJun
	 * @date 2016年9月21日
	 */
	private void validRepayStyle() {
		
		if (LoanEnum.TIME_TYPE_DAY.eq(this.timeType) && !LoanEnum.STYLE_ONETIME_REPAYMENT.eq(repayStyle)) {
			// 天标只能选择一次性还款
			throw new BussinessException(ResourceUtils.get(ResourceConstant.PRODUCT_TIME_TYPE_DAY_ONETIME_REPAYMENT_ONLY));
		}
		// 期限数值错误（每季还息到期还本，必须为3的整数倍）
		if (LoanEnum.STYLE_QUARTER_INTEREST.eq(repayStyle) && this.timeLimit % Constant.INT_THREE != Constant.INT_ZERO) {
			throw new BussinessException(ResourceUtils.get(ResourceConstant.PROJECT_TIME_LIMIT_QUARTER_ERROR));
		}
		// 期限为1 (1天或1个月)，还款方式为一次性还款
		if (this.timeLimit == Constant.INT_ONE && !LoanEnum.STYLE_ONETIME_REPAYMENT.eq(repayStyle)) {
			throw new BussinessException(ResourceUtils.get(ResourceConstant.PRODUCT_TIME_LIMIT_ONETIME_REPAYMENT_ONLY));
		}
		// 除 每季还息到期还本、每月还息到期还本外，其他还款方式不能设置固定还款日
		if (!(LoanEnum.STYLE_QUARTER_INTEREST.eq(repayStyle) || LoanEnum.STYLE_MONTHLY_INTEREST.eq(repayStyle))) {
			this.fixedRepayDay = 0;
		}

		// T+N计息时起始计息天数不能为空
		if (LoanEnum.INTEREST_STYLE_TN.eq(interestStyle) ) {
			if( this.interestStartDays == null){
			   throw new BussinessException(ResourceUtils.get(ResourceConstant.PROJECT_INTERESTSTYLE_TNDAYS_NOTEMPTY));
			}
			if(this.interestStartDays < 0 || this.interestStartDays >Constant.INT_SEVEN){
				throw new BussinessException(ResourceUtils.get(ResourceConstant.PROJECT_INTERESTSTARTDAYS_RANGE_ERROR));
			}
		}
		
		if(this.interestStartDays!=null){
			// 月均天数
			BigDecimal daysOfMonth = ConfigUtils.getBigDecimal(ConfigConstant.DAYS_OF_MONTH);
			int borrowDays = timeLimit.intValue() * (LoanEnum.TIME_TYPE_MONTH.eq(timeType)?daysOfMonth.intValue():Constant.INT_ONE);
			if(interestStartDays.intValue() >= borrowDays){
				throw new BussinessException(ResourceUtils.get(ResourceConstant.PROJECT_INTERESTSTARTDAYS_MUSTLT_TIMELIMIT));
			}
		}
		
	}

	/**
	 * 销售方式校验（按份数、按金额）
	 * @author  FangJun
	 * @date 2016年9月21日
	 */
	private void validSaleStyle() {
		// 份数相关属性校验
		if (LoanEnum.SALE_STYLE_COPY.eq(this.saleStyle)) {
			if (this.copyAccount.doubleValue() <= 0) {
				throw new BussinessException(ResourceUtils.get(ResourceConstant.PRODUCT_COPY_ACCOUNT_LE_ZERO));
			}

			if (this.copyAccount.compareTo(this.account) > 0) {// 是否超过借款总额
				throw new BussinessException(ResourceUtils.get(ResourceConstant.PRODUCT_COPY_ACCOUNT_GT_ACCOUNT));
			}
			if (!BigDecimalUtils.modZero(this.account, this.copyAccount)) {// 能否被整除
				throw new BussinessException(ResourceUtils.get(ResourceConstant.PRODUCT_COPY_ACCOUNT_UNDIVISIBLE_BY_ACCOUNT));
			}
			this.totalCopies = BigDecimalUtils.div(this.account, this.copyAccount).intValue();

			if (this.lowestCopies > 0 && this.lowestCopies > this.totalCopies) {// 最低起投份数不能大于总份数
				throw new BussinessException(ResourceUtils.get(ResourceConstant.PRODUCT_LOWEST_COPIES_GT_TOTAL_COPIES));
			}
			if (this.mostCopies > 0 && this.mostCopies > this.totalCopies) {// 最高可投份数不能大于总份数
				throw new BussinessException(ResourceUtils.get(ResourceConstant.PRODUCT_MOST_COPIES_GT_TOTAL_COPIES));
			}
		} else {
			double borrowLowestInvestAccount = ConfigUtils.getDouble(ConfigConstant.BORROW_LOWEST_INVEST_ACCOUNT);
			// 最低起投金额校验
			if (this.lowestAccount.doubleValue() < borrowLowestInvestAccount) {
				throw new BussinessException(ResourceUtils.get(ResourceConstant.PROJECT_LOWEST_ACCOUNT_LT_ONE_ERROR,borrowLowestInvestAccount));
			}
			if (this.lowestAccount.doubleValue() > this.account.doubleValue()) {
				throw new BussinessException(ResourceUtils.get(ResourceConstant.PROJECT_LOWEST_ACCOUNT_GT_ACCOUNT_ERROR));
			}
			if (!NumberUtils.isDefault(this.mostAccount) && this.lowestAccount.doubleValue() > this.mostAccount.doubleValue()) {
				throw new BussinessException(ResourceUtils.get(ResourceConstant.PROJECT_LOWEST_ACCOUNT_GT_MOST_ACCOUNT_ERROR));
			}

			// 最高投资总额校验
			if (!NumberUtils.isDefault(this.mostAccount) && this.mostAccount.compareTo(this.account) > 0) {
				throw new BussinessException(ResourceUtils.get(ResourceConstant.PROJECT_MOST_ACCOUNT_GT_ACCOUNT_ERROR));
			}
			//递增金额必须为整数
			if(stepAccount!=null && !BigDecimalUtils.modZero(stepAccount, BigDecimal.ONE)){
				throw new BussinessException(ResourceUtils.get(ResourceConstant.PROJECT_STEPACCOUNT_MUSTINT));
			}
			//递增金额需小于等于最低起投金额
			if(stepAccount!=null && stepAccount.compareTo(lowestAccount)>0){
				throw new BussinessException(ResourceUtils.get(ResourceConstant.PROJECT_STEPACCOUNT_GT_LOWEST));
			}
			//最低起投金额和递增金额的总和不能超过借款总金额
			if(stepAccount != null && stepAccount.add(this.lowestAccount).compareTo(account) > 0){
				throw new BussinessException(ResourceUtils.get(LoanEnum.BORROW_FLAG_BORROW.eq(this.borrowFlag)?ResourceConstant.BORROW_STEPACCOUNT_MUSTLT_ACCOUNT:ResourceConstant.PRODUCT_STEPACCOUNT_MUSTLT_ACCOUNT));
			}
			//最低起投金额和递增金额的总和不能超过最高投资总额
			if(!NumberUtils.isDefault(this.mostAccount) && stepAccount != null && stepAccount.add(this.lowestAccount).compareTo(this.mostAccount) > 0){
				throw new BussinessException(ResourceUtils.get(ResourceConstant.PROJECT_STEPACCOUNT_MUSTLT_MOSTACCOUNT));
			}
		}
	}
	/**
	 * 定向销售信息校验
	 * @author  FangJun
	 * @date 2016年9月21日
	 */
	private  void  validSpecificSale(){
		//定向销售校验	
		 if(StringUtils.isNotBlank(this.getSpecificSale()) && !LoanEnum.SPECIFIC_SALE_NONE.eq(this.getSpecificSale())) {
			if(LoanEnum.SPECIFIC_SALE_PASSWORD.eq(this.getSpecificSale())  ){
				if(StringUtils.isBlank(this.getSpecificSaleRule())){
			    	throw new BussinessException(ResourceUtils.get(ResourceConstant.PROJECT_STEPACCOUNT_MUSTINT));
				}
				//校验定向密码（6位数字）
				if(this.getSpecificSaleRule().length()!=ProjectConstant.PROJECT_PASSWORD_LENGTH 
						|| !NumberUtils.isDigits(this.getSpecificSaleRule())){
					throw new BussinessException(ResourceUtils.get(ResourceConstant.PROJECT_PASS_FORMAT_ERROR));
				}
			}else if(LoanEnum.SPECIFIC_SALE_MAIL.eq(this.getSpecificSale())){
				if(StringUtils.isBlank(this.getSpecificSaleRule())){
			    	throw new BussinessException(ResourceUtils.get(ResourceConstant.PROJECT_SALE_MAIL_NOTEMPTY));
				}
				final String[] mailDomainArr=this.getSpecificSaleRule().split(",");
				for(String mailDomain : mailDomainArr){
					if(	!StringUtils.isEmailDomain(mailDomain)){
						throw new BussinessException(ResourceUtils.get(ResourceConstant.PROJECT_SALE_MAIL_FORMAT_ERROR));
					}
				}
			
			}else if(LoanEnum.SPECIFIC_SALE_VIP_LEVEL.eq(this.getSpecificSale())){
				if(StringUtils.isBlank(this.getSpecificSaleRule()))
			    	//throw new BussinessException(ResourceUtils.get(ResourceConstant.PROJECT_SALE_VIP_LEVEL_NOTEMPTY));
					this.setSpecificSaleRule(String.valueOf(Constant.INT_ONE));//如果定向发放规则为vip等级定向放，默认vip等级1及以上
				if( !NumberUtils.isDigits(this.getSpecificSaleRule())){
					throw new BussinessException(ResourceUtils.get(ResourceConstant.PROJECT_SALE_VIP_LEVEL_FORMAT_ERROR));
				}
			}else if(LoanEnum.SPECIFIC_SALE_USER.eq(this.getSpecificSale())){
				if(StringUtils.isBlank(this.getSpecificSaleRule())){
					this.setSpecificSaleRule(User.ACCOUNT_TYPE_YOU);//默认柚账户
				}
				if( !NumberUtils.isDigits(this.getSpecificSaleRule())){
					throw new BussinessException(ResourceUtils.get(ResourceConstant.PROJECT_SALE_VIP_LEVEL_FORMAT_ERROR));
				}
			}
	   }else{
		   this.setSpecificSaleRule(null);
	   }
	}
	
	/**
	 * 
	 * 前台借款参数校验
	 * @author ZhangBiao
	 * @date 2016年8月16日
	 */
	public void webValid() {
		// 校验金额、利率、期限
		validAmountApr();
		// 校验固定还款日
		validFixedRepayDay();
		// 还款方式信息校验
		validRepayStyle();
		// 校验借款人
		validBorrower();
	}

	/**
	 * 上架前基础数据校验
	 */
	public void validBeforeSale(){
		if(this.showTime == null ){
			throw new BussinessException(ResourceUtils.get(ResourceConstant.PROJECT_SHOW_TIME_NOT_EMPTY));
		}
		 Date now = new Date();
		if (LoanEnum.STATUS_RAISING.eq(this.getStatus()) && (now.after(this.getShowTime()))){
			throw new BussinessException(ResourceUtils.get(ResourceConstant.PROJECT_SHOW_EDIT_ERROR));
		}
		//所属类别校验
		if(StringUtils.isBlank(this.projectTypeId)){
			throw new BussinessException(ResourceUtils.get(ResourceConstant.PROJECT_PROJECT_TYPE_ID_NOT_EMPTY));
		}
		
		//借款：募集期校验
		if(LoanEnum.BORROW_FLAG_BORROW.getValue().equals(this.borrowFlag) || CbhbConstant.CBHB_TPP_NAME.equals(ConfigUtils.getTppName())){//借款
			
			int minDays = ProjectConstant.RAISETIMELIMIT_MINDAYS;
			int maxDays = ProjectConstant.RAISETIMELIMIT_MAXDAYS;
			
			if(this.raiseTimeLimit == null){
				throw new BussinessException(ResourceUtils.get(ResourceConstant.BORROW_RAISE_TIME_LIMIT_NOT_EMPTY));
			}
			if(this.raiseTimeLimit.intValue() < minDays || this.raiseTimeLimit.intValue() > maxDays){
				throw new BussinessException(ResourceUtils.get(ResourceConstant.BORROW_RAISE_TIME_LIMIT_RANGE_ERROR, minDays, maxDays));
			}			
		}
		
		//加息利率上限校验
		double borrowMaxAddApr =  ConfigUtils.getDouble(ConfigConstant.BORROW_MOST_ADD_APR);
		if(this.addApr!=null && borrowMaxAddApr > 0 && this.addApr.doubleValue() > borrowMaxAddApr){
			throw new BussinessException(ResourceUtils.get(ResourceConstant.PROJECT_ADD_APR_RANGE_ERROR, borrowMaxAddApr));
		}
		
		//加息利率和加息券不能叠加使用
		if(!NumberUtils.isDefault(this.addApr) && CommonEnum.YES.eq(this.additionalRateUseful)){
			throw new BussinessException(ResourceUtils.get(ResourceConstant.PROJECT_ADD_APR_ADDITIONALRATEUSEFUL_SUPERPOSITION));
		}
		
		//上架渠道校验
		if(StringUtils.isBlank(saleChannel)){
			throw new BussinessException(ResourceUtils.get(ResourceConstant.PROJECT_SALE_CHANNEL_NOT_EMPTY));
		}
		
		//上架时间/开售时间校验
		if(this.saleTime == null ){
			throw new BussinessException(ResourceUtils.get(ResourceConstant.PROJECT_SALE_TIME_NOT_EMPTY));
		}		
		if(this.showTime.compareTo(this.saleTime) > 0){
			throw new BussinessException(ResourceUtils.get(ResourceConstant.PROJECT_SHOW_TIME_GT_SALE_TIME));
		}
		//校验红包可用最大比例
		if(CommonEnum.YES.eq(this.getRedEnvelopeUseful()) && ( !BigDecimalUtils.validAmount(this.getRedEnvelopeRate()) 
				|| this.redEnvelopeRate.doubleValue() > Constant.DOUBLE_ONE_HUNDRED)){
			throw new BussinessException(ResourceUtils.get(ResourceConstant.PROJECT_RED_ENVELOPE_RATE_ERROR));   
		}
		
		//定向销售信息校验
		validSpecificSale();
	}
	
	/**
	 * 产品判断是否开启自动投资
	 * @author ZhangBiao
	 * @date 2016年8月5日
	 * @param project 产品
	 * @param novice 新手专享
	 * @param specificSale 定向密码
	 * @param date 上架时间
	 * @return
	 */
	public boolean checkOpenAuto(Project project){
		// 封装参数
		this.novice = project.getNovice();
		this.specificSale = project.getSpecificSale();
		this.saleTime = project.getSaleTime();
		// 开售时间时间且大于当前时间的产品+5分钟的
		if(this.saleTime.getTime() > (DateUtils.rollMinute(DateUtils.getNow(),Constant.INT_FIVE)).getTime()){
			return false;
		}
		return this.checkOpenAuto();
	}
	
	/**
	 * 产品判断是否开启自动投资
	 * @author ZhangBiao
	 * @date 2016年8月5日
	 * @param project 产品
	 * @return
	 */
	public boolean checkOpenAuto(){
		// 自动投资开关
		final String autoInvestStatus = ConfigUtils.getValue(ConfigConstant.AUTO_INVEST);
		// 获取自动投资可投比例
		final BigDecimal autoAmountMax = BigDecimalUtils.mul(ConfigUtils.getBigDecimal(ConfigConstant.AUTO_INVEST_MAX_APR),this.account); // 最大自动投资额度
		BigDecimal lowestAccount = this.lowestAccount;
		if(LoanEnum.SALE_STYLE_COPY.getValue().equals(this.saleStyle)){
			lowestAccount = BigDecimalUtils.mul(this.copyAccount,BigDecimal.valueOf(this.lowestCopies));
		}
		// 自动投标状态开启
		if(!Constant.FLAG_YES.equals(autoInvestStatus)){
			return false;
		}
		// 去除新手专享
		if(!LoanEnum.NOVICE_FLAG_NO.eq(this.novice)){
			return false;
		}
		// 去除定向密码
		if(!LoanEnum.SPECIFIC_SALE_NONE.eq(this.specificSale)){
			return false;
		}
		// 自动投标额度小于最低投资金额
		if(autoAmountMax.compareTo(lowestAccount) < Constant.INT_ZERO){
			return false;
		}
		// 产品的必须为非变现产品
		if(LoanEnum.BORROW_FLAG_BORROW.eq(this.borrowFlag) 
				|| (LoanEnum.BORROW_FLAG_PRODUCT.eq(this.borrowFlag)
				&& LoanEnum.REALIZE_FLAG_NORMAL.eq(this.realizeFlag))){
			return true;
		}
		return false;
	}
	
	public String getDeleteFlag() {
		return deleteFlag;
	}

	public void setDeleteFlag(String deleteFlag) {
		this.deleteFlag = deleteFlag;
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

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getSaleChannel() {
		return saleChannel;
	}

	public void setSaleChannel(String saleChannel) {
		this.saleChannel = saleChannel;
	}

	public String getSpecificSale() {
		return specificSale;
	}

	public void setSpecificSale(String specificSale) {
		this.specificSale = specificSale;
	}

	public String getSpecificSaleRule() {
		return specificSaleRule;
	}

	public void setSpecificSaleRule(String specificSaleRule) {
		this.specificSaleRule = specificSaleRule;
	}

	public Date getPublishVerifyTime() {
		return publishVerifyTime;
	}

	public void setPublishVerifyTime(Date publishVerifyTime) {
		this.publishVerifyTime = publishVerifyTime;
	}
	
	public BigDecimal getRepayAmount() {
		return repayAmount;
	}

	public void setRepayAmount(BigDecimal repayAmount) {
		this.repayAmount = repayAmount;
	}

	public BigDecimal getRepayYesAccount() {
		return repayYesAccount;
	}

	public void setRepayYesAccount(BigDecimal repayYesAccount) {
		this.repayYesAccount = repayYesAccount;
	}

	public BigDecimal getRepayYesInterest() {
		return repayYesInterest;
	}

	public void setRepayYesInterest(BigDecimal repayYesInterest) {
		this.repayYesInterest = repayYesInterest;
	}
	
	/**
	 * 应还总额
	 * @return
	 */
	public BigDecimal getRepayTotal() {
		if(repayAmount==null){
			return BigDecimal.ZERO;
		}
		return repayAmount;
	}
	
	/**
	 * 已还金额
	 * @return
	 */
	public BigDecimal getRepayYesTotal() {
		if(repayYesAccount==null){
			return BigDecimal.ZERO;
		}
		return repayYesAccount.add(repayYesInterest);
	}
	
	/**
	 * 未还金额
	 * @return
	 */
	public BigDecimal getNotRepayTotal() {
		if(repayAmount == null){
			return BigDecimal.ZERO;
		}else if(repayYesAccount == null){
			return repayAmount;
		}
		return repayAmount.subtract(repayYesAccount).subtract(repayYesInterest);
	}
	

	/**
	 * 取得募集时长
	 * @return
	 */
	public int getRaiseRealDays() {
		if(saleTime!=null && raiseEndTime!=null){
			return (int)DateUtils.getDistanceOfTwoDate(saleTime, raiseEndTime);			
		}
		if(saleTime!=null && stopTime!=null){
		  return (int)DateUtils.getDistanceOfTwoDate(saleTime, stopTime);         
		}
		return 0;
	}


	public String getVouchStatus() {
		return vouchStatus;
	}

	public void setVouchStatus(String vouchStatus) {
		this.vouchStatus = vouchStatus;
	}

	public String getInterestTime() {
		return  interestTime;
	}

	public void setInterestTime(String interestTime) {
		this.interestTime = interestTime;
	}

	public String getVerifyRemark() {
		return verifyRemark;
	}

	public void setVerifyRemark(String verifyRemark) {
		this.verifyRemark = verifyRemark;
	}

	/**
	 * 获取属性totalPeriod的值
	 * @return totalPeriod属性值
	 */
	public Integer getTotalPeriod() {
		return totalPeriod;
	}

	/**
	 * 设置属性totalPeriod的值
	 * @param  totalPeriod属性值
	 */
	public void setTotalPeriod(Integer totalPeriod) {
		this.totalPeriod = totalPeriod;
	}

	/**
	 * 获取属性repayedPeriod的值
	 * @return repayedPeriod属性值
	 */
	public Integer getRepayedPeriod() {
		return repayedPeriod;
	}

	/**
	 * 设置属性repayedPeriod的值
	 * @param  repayedPeriod属性值
	 */
	public void setRepayedPeriod(Integer repayedPeriod) {
		this.repayedPeriod = repayedPeriod;
	}

	/**
	 * 获取属性nextRepayTime的值
	 * @return nextRepayTime属性值
	 */
	public Date getNextRepayTime() {
		return nextRepayTime;
	}

	/**
	 * 设置属性nextRepayTime的值
	 * @param  nextRepayTime属性值
	 */
	public void setNextRepayTime(Date nextRepayTime) {
		this.nextRepayTime = nextRepayTime;
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
	 * 获取属性realLastRepayTime的值
	 * @return realLastRepayTime属性值
	 */
	public Date getRealLastRepayTime() {
		return realLastRepayTime;
	}

	/**
	 * 设置属性realLastRepayTime的值
	 * @param  realLastRepayTime属性值
	 */
	public void setRealLastRepayTime(Date realLastRepayTime) {
		this.realLastRepayTime = realLastRepayTime;
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
   * 根据已投金额和项目金额计算项目投资进度
   * @author  FangJun
   * @date 2016年8月24日
   * @return
   */
	public double getScales() {
		 double result=Constant.DOUBLE_ZERO;
		if(!LoanEnum.STATUS_NOT_ESTABLISH.eq(this.getStatus()) &&
				BigDecimalUtils.validAmount(account) && BigDecimalUtils.validAmount(accountYes)){
			result =BigDecimalUtils.div(accountYes.multiply(BigDecimal.valueOf(Constant.DOUBLE_ONE_HUNDRED)),account).doubleValue();	
                if(account.compareTo(accountYes) > 0  && result== Constant.DOUBLE_ONE_HUNDRED){
                	//对于剩余剩余可投，远小于总金额的0.01%，处理成99.99%
                	result=Constant.DOUBLE_FOUR_NINE_PERCENT;
                }
		}
		return result;
	}
	
	public String getOperatorName() {
		return operatorName;
	}

	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}

	public String getRedenvelopeRuleId() {
		return redenvelopeRuleId;
	}

	public void setRedenvelopeRuleId(String redenvelopeRuleId) {
		this.redenvelopeRuleId = redenvelopeRuleId;
	}

	public String getRateCouponRuleId() {
		return rateCouponRuleId;
	}

	public void setRateCouponRuleId(String rateCouponRuleId) {
		this.rateCouponRuleId = rateCouponRuleId;
	}

	public Date getVouchVerifyTime() {
		return vouchVerifyTime;
	}

	public void setVouchVerifyTime(Date vouchVerifyTime) {
		this.vouchVerifyTime = vouchVerifyTime;
	}

	public Integer getStage() {
		return stage;
	}

	public void setStage(Integer stage) {
		this.stage = stage;
	}

	/**
	 * 获取属性overdueFeeRate的值
	 * @return overdueFeeRate属性值
	 */
	public BigDecimal getOverdueFeeRate() {
		return overdueFeeRate;
	}

	/**
	 * 设置属性overdueFeeRate的值
	 * @param  overdueFeeRate属性值
	 */
	public void setOverdueFeeRate(BigDecimal overdueFeeRate) {
		this.overdueFeeRate = overdueFeeRate;
	}

	public BigDecimal getOverdueFeeRateMul() {
		return BigDecimalUtils.mul(BigDecimalUtils.defaultIfNull(overdueFeeRate),new BigDecimal(Constant.INT_ONE_HUNDRED));
	}
	
	/**
	 * 获取属性protocolId的值
	 * @return protocolId属性值
	 */
	public String getProtocolId() {
		return protocolId;
	}

	/**
	 * @return the 状态统计数量
	 */
	public int getStatusCount() {
		return statusCount;
	}

	/**
	 * @param 状态统计数量 the statusCount to set
	 */
	public void setStatusCount(int statusCount) {
		this.statusCount = statusCount;
	}

	/**
	 * 设置属性protocolId的值
	 * @param  protocolId属性值
	 */
	public void setProtocolId(String protocolId) {
		this.protocolId = protocolId;
	}
	/**
	 * 是否为即息理财（募集期为空或0，投资成功即放款）
	 * @return true or false
	 */
	public  boolean  isInterestFinancing(){
		if(NumberUtils.isDefault(this.raiseTimeLimit) && CommonEnum.NO.eq(this.borrowFlag) ){
			return true;
		}
		
		return false;
	}

	public String getProjectType() {
		return projectType;
	}

	public void setProjectType(String projectType) {
		this.projectType = projectType;
	}
	
	
}
