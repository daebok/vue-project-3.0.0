package com.rd.ifaes.core.project.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import com.google.common.collect.Maps;
import com.rd.ifaes.common.constant.CommonConstant;
import com.rd.ifaes.common.dict.InvestEnum;
import com.rd.ifaes.common.dict.LoanEnum;
import com.rd.ifaes.common.dict.ProjectCollectionEnum;
import com.rd.ifaes.common.entity.BaseEntity;
import com.rd.ifaes.common.orm.Page;
import com.rd.ifaes.common.util.BigDecimalUtils;
import com.rd.ifaes.common.util.DateUtils;
import com.rd.ifaes.common.util.SpringContextHolder;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.core.bond.service.BondService;
import com.rd.ifaes.core.core.constant.Constant;
import com.rd.ifaes.core.project.domain.Project;
import com.rd.ifaes.core.project.domain.ProjectCollection;
import com.rd.ifaes.core.project.domain.ProjectInvest;
import com.rd.ifaes.core.project.service.ProjectCollectionService;
import com.rd.ifaes.core.project.service.ProjectService;

public class ProjectInvestModel extends BaseEntity<ProjectInvestModel>{
	/**
	 * 日志
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(ProjectInvestModel	.class);

	private static final long serialVersionUID = 1L;
	
	private String	userId;		 /* 用户ID */ 
	private String	projectId;		 /* 项目ID */ 
	private String	status;		 /* 投资状态 0 待支付，1成功，2失败 */ 
	private BigDecimal	money;		 /* 投标金额 */ 
	private BigDecimal	amount;		 /* 有效投资金额 */ 
	private BigDecimal	realAmount;		 /* 用户真实资金 */ 
	private BigDecimal	payment;		 /* 预收本息 */ 
	private BigDecimal	interest;		 /* 预期利息 */ 
	private BigDecimal	repayedAmount;		 /* 已还金额 */ 
	private BigDecimal	repayedInterest;		 /* 已还利息 */ 
	private BigDecimal	waitAmount;		 /* 待还总额 */ 
	private BigDecimal	waitInterest;		 /* 待还利息 */ 
	private BigDecimal	waitRaiseInterest;		 /* 待收加息利息 */ 
	private String	investType;		 /* 投资方式  0手动投资;1自动投资 */ 
	private String	investChannel;		 /* 投资渠道 1 PC  2 APP 3 微信  */ 
	private String	investOrderNo;		 /* 投标时候的订单号 */ 
	private String	freezeNo;		 /* 投标冻结流水号 */ 
	private String	userFirstInvest;		 /* 用户首次投资，0：非首投，1：首投 */ 
	private String	realizeFlag;		 /* 变现标识 */ 
	private Date	createTime;		 /* 添加时间 */ 
	private String	addIp;		 /* ip地址 */ 
	private String	bondFlag;		 /* 债权标识*/ 
	private String	investNo;		 /* 投资流水号 */ 
	private String	investDate;		 /* 投资日期(UFX) */ 
	private String originInvestId;   /* 原始投资ID*/
	private String investStyle;/*投资类型方式: 0通投资,1转让投资,2受让投资*/
	private BigDecimal realizeAmount;   /* 已变现本息 */
	private BigDecimal realizeInterest; /* 已变现利息*/
	private BigDecimal freezeInterest;  /* 变现冻结利息 */
	private Date	interestDate;		 /* 起息时间 */ 
	private Date	endDate;		 /* 结束时间 */ 
	private String userName; // 用户账号
	private String realName;// 真实姓名
	private String mobile; // 手机号码
	private Integer periods;// 期数
	private String exceptStatus;// 除外的状态
	private  String borrowFlag;//借款标识
	private BigDecimal raiseInterest;//加息利息
	private   String	pwd;		 /* 用户输入 定向密码  */ 
	private Integer bondMaxTurn;//借贷转让次数
	private  Boolean pwdHasVerify=false; /* 否校验过定向密码*/
	/**
	 * 借款标  还款方式
	 */
	private String repayStyle;
	/**
	 * 借款标状态值   
	 */
	private String projectStatus;
	/**
	 * 项目名称
	 */
	private String projectName;
	
	/**
	 * 剩余期限
	 */
	private int remainDays;//剩余期限
	
	/**
	 * 成立审核时间
	 */
	private Date reviewTime;
	
	/**
	 * 计息方式
	 */
	private String interestStyle;
	
	/**
	 * 起始计息天数(T+N计息方式 )
	 */
	private String interestStartDays;
	
	/**
	 * 下一次还款日
	 */
	private String nextRepayDate;
	
	/**
	 * 起息日
	 */
	private String repayStartDate;
	
	/**
	 * 结束日
	 */
	private String repayEndDate;
	
	/**
	 * 原借款标预期年化
	 */
	private BigDecimal apr;
	/**
	 * 起息持有天数
	 */
	private Integer holdDays;
	/**
	 * 本期还款剩余天数 0
	 */
	private Integer periodRemainDays;
	/**
	 * 下期还款时间
	 */
	private Date nextRepayTime;
	/**
	 * 最后还款时间
	 */
	private Date lastRepayTime;
	/**
	 * 最小变现金额
	 */
	private BigDecimal sellAmountMin;
	/**
	 * 红包ID字符串
	 */
	private  String redEnvelopeIds;
	/**
	 * 加息劵ID
	 */
	private  String rateCouponId;
	/**
	 *加息劵加息利率 （页面传值，计算预计利息使用）
	 */
	private BigDecimal upApr;
	
	/** 是否可以转让*/
	private String bondUseful;
	
	/** 是否已经选择了不在乎风险提示，继续投资*/
	private int isSelectedTip;
	
	private String	sellStyle;		 /* 变现金额方式:0 部分变现;1全额变现,默认0 */ 
	
	/** 短信验证码*/
	private String smsCode;
	
	/**
	 * 对账结果  0-其他异常
				1-投资冻结成功
				2-投资冻结失败
				3-投资撤销解冻成功
				4-投资撤销解冻失败
				5-投资部分冻结
				6-放款解冻

	 */
	private String chkResult;
	
	
	private String futureTime;
	
	/**
	 * 获取属性isSelectedTip的值
	 * @return isSelectedTip属性值
	 */
	public int getIsSelectedTip() {
		return isSelectedTip;
	}


	/**
	 * 设置属性isSelectedTip的值
	 * @param  isSelectedTip属性值
	 */
	public void setIsSelectedTip(int isSelectedTip) {
		this.isSelectedTip = isSelectedTip;
	}


	/**
	 * 获取属性bondUseful的值
	 * @return bondUseful属性值
	 */
	public String getBondUseful() {
		return bondUseful;
	}


	/**
	 * 设置属性bondUseful的值
	 * @param  bondUseful属性值
	 */
	public void setBondUseful(final String bondUseful) {
		this.bondUseful = bondUseful;
	}


	/**
	 * 获取属性holdDays的值
	 * @return holdDays属性值
	 */
	public Integer getHoldDays() {
		return holdDays;
	}
	

	/**
	 * 设置属性holdDays的值
	 * @param  holdDays属性值
	 */
	public void setHoldDays(Integer holdDays) {
		this.holdDays = holdDays;
	}

	/**
	 * 获取属性periodRemainDays的值
	 * @return periodRemainDays属性值
	 */
	public Integer getPeriodRemainDays() {
		return periodRemainDays;
	}

	/**
	 * 设置属性periodRemainDays的值
	 * @param  periodRemainDays属性值
	 */
	public void setPeriodRemainDays(Integer periodRemainDays) {
		this.periodRemainDays = periodRemainDays;
	}

	/**
	 * @return the apr
	 */
	public BigDecimal getApr() {
		return apr;
	}

	/**
	 * @param apr the apr to set
	 */
	public void setApr(BigDecimal apr) {
		this.apr = apr;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getNextRepayDate() {
		return nextRepayDate;
	}

	public void setNextRepayDate(String nextRepayDate) {
		this.nextRepayDate = nextRepayDate;
	}

	public String getRepayStartDate() {
		return repayStartDate;
	}

	public void setRepayStartDate(String repayStartDate) {
		this.repayStartDate = repayStartDate;
	}

	public String getRepayEndDate() {
		return repayEndDate;
	}

	public void setRepayEndDate(String repayEndDate) {
		this.repayEndDate = repayEndDate;
	}

	/**
	 * 获取属性remainDays的值
	 * @return remainDays属性值
	 */
	public int getRemainDays() {
		return remainDays;
	}

	/**
	 * 获取属性projectStatus的值
	 * @return projectStatus属性值
	 */
	public String getProjectStatus() {
		return projectStatus;
	}

	/**
	 * 设置属性projectStatus的值
	 * @param  projectStatus属性值
	 */
	public void setProjectStatus(String projectStatus) {
		this.projectStatus = projectStatus;
	}

	/**
	 * 设置属性remainDays的值
	 * @param  remainDays属性值
	 */
	public void setRemainDays(int remainDays) {
		this.remainDays = remainDays;
	}

	/**
	 * 获取属性redEnvelopeIds的值
	 * @return redEnvelopeIds属性值
	 */
	public String getRedEnvelopeIds() {
		return redEnvelopeIds;
	}

	/**
	 * 设置属性redEnvelopeIds的值
	 * @param  redEnvelopeIds属性值
	 */
	public void setRedEnvelopeIds(String redEnvelopeIds) {
		this.redEnvelopeIds = redEnvelopeIds;
	}

	/**
	 * 获取属性rateCouponId的值
	 * @return rateCouponId属性值
	 */
	public String getRateCouponId() {
		return rateCouponId;
	}

	/**
	 * 设置属性rateCouponId的值
	 * @param  rateCouponId属性值
	 */
	public void setRateCouponId(String rateCouponId) {
		this.rateCouponId = rateCouponId;
	}
	
	/**
	 * 获取属性upApr的值
	 * @return upApr属性值
	 */
	public BigDecimal getUpApr() {
		return upApr;
	}

	/**
	 * 设置属性upApr的值
	 * @param  upApr属性值
	 */
	public void setUpApr(BigDecimal upApr) {
		this.upApr = upApr;
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
	public void setUserId(String userId) {
		this.userId = userId;
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
	public void setProjectId(String projectId) {
		this.projectId = projectId;
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
	 * 获取属性money的值
	 * @return money属性值
	 */
	public BigDecimal getMoney() {
		return money;
	}

	/**
	 * 设置属性money的值
	 * @param  money属性值
	 */
	public void setMoney(BigDecimal money) {
		this.money = money;
	}

	/**
	 * 获取属性amount的值
	 * @return amount属性值
	 */
	public BigDecimal getAmount() {
		return amount;
	}

	/**
	 * 设置属性amount的值
	 * @param  amount属性值
	 */
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	/**
	 * 获取属性realAmount的值
	 * @return realAmount属性值
	 */
	public BigDecimal getRealAmount() {
		return realAmount;
	}

	/**
	 * 设置属性realAmount的值
	 * @param  realAmount属性值
	 */
	public void setRealAmount(BigDecimal realAmount) {
		this.realAmount = realAmount;
	}

	/**
	 * 获取属性payment的值
	 * @return payment属性值
	 */
	public BigDecimal getPayment() {
		return payment;
	}

	/**
	 * 设置属性payment的值
	 * @param  payment属性值
	 */
	public void setPayment(BigDecimal payment) {
		this.payment = payment;
	}

	/**
	 * 获取属性interest的值
	 * @return interest属性值
	 */
	public BigDecimal getInterest() {
		return interest;
	}

	/**
	 * 设置属性interest的值
	 * @param  interest属性值
	 */
	public void setInterest(BigDecimal interest) {
		this.interest = interest;
	}

	/**
	 * 获取属性repayedAmount的值
	 * @return repayedAmount属性值
	 */
	public BigDecimal getRepayedAmount() {
		return repayedAmount;
	}

	/**
	 * 设置属性repayedAmount的值
	 * @param  repayedAmount属性值
	 */
	public void setRepayedAmount(BigDecimal repayedAmount) {
		this.repayedAmount = repayedAmount;
	}

	/**
	 * 获取属性repayedInterest的值
	 * @return repayedInterest属性值
	 */
	public BigDecimal getRepayedInterest() {
		return repayedInterest;
	}

	/**
	 * 设置属性repayedInterest的值
	 * @param  repayedInterest属性值
	 */
	public void setRepayedInterest(BigDecimal repayedInterest) {
		this.repayedInterest = repayedInterest;
	}

	/**
	 * 获取属性reviewTime的值
	 * @return reviewTime属性值
	 */
	public Date getReviewTime() {
		return reviewTime;
	}


	/**
	 * 设置属性reviewTime的值
	 * @param  reviewTime属性值
	 */
	public void setReviewTime(Date reviewTime) {
		this.reviewTime = reviewTime;
	}


	/**
	 * 获取属性waitAmount的值
	 * @return waitAmount属性值
	 */
	public BigDecimal getWaitAmount() {
		return waitAmount;
	}

	/**
	 * 设置属性waitAmount的值
	 * @param  waitAmount属性值
	 */
	public void setWaitAmount(BigDecimal waitAmount) {
		this.waitAmount = waitAmount;
	}

	/**
	 * 获取属性waitInterest的值
	 * @return waitInterest属性值
	 */
	public BigDecimal getWaitInterest() {
		return waitInterest;
	}

	/**
	 * 设置属性waitInterest的值
	 * @param  waitInterest属性值
	 */
	public void setWaitInterest(BigDecimal waitInterest) {
		this.waitInterest = waitInterest;
	}

	/**
	 * 获取属性investType的值
	 * @return investType属性值
	 */
	public String getInvestType() {
		return investType;
	}

	/**
	 * 设置属性investType的值
	 * @param  investType属性值
	 */
	public void setInvestType(String investType) {
		this.investType = investType;
	}

	/**
	 * 获取属性investChannel的值
	 * @return investChannel属性值
	 */
	public String getInvestChannel() {
		return investChannel;
	}

	/**
	 * 设置属性investChannel的值
	 * @param  investChannel属性值
	 */
	public void setInvestChannel(String investChannel) {
		this.investChannel = investChannel;
	}

	/**
	 * 获取属性investOrderNo的值
	 * @return investOrderNo属性值
	 */
	public String getInvestOrderNo() {
		return investOrderNo;
	}

	/**
	 * 设置属性investOrderNo的值
	 * @param  investOrderNo属性值
	 */
	public void setInvestOrderNo(String investOrderNo) {
		this.investOrderNo = investOrderNo;
	}

	/**
	 * 获取属性freezeNo的值
	 * @return freezeNo属性值
	 */
	public String getFreezeNo() {
		return freezeNo;
	}

	/**
	 * 设置属性freezeNo的值
	 * @param  freezeNo属性值
	 */
	public void setFreezeNo(String freezeNo) {
		this.freezeNo = freezeNo;
	}

	/**
	 * 获取属性userFirstInvest的值
	 * @return userFirstInvest属性值
	 */
	public String getUserFirstInvest() {
		return userFirstInvest;
	}

	/**
	 * 设置属性userFirstInvest的值
	 * @param  userFirstInvest属性值
	 */
	public void setUserFirstInvest(String userFirstInvest) {
		this.userFirstInvest = userFirstInvest;
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
	 * 获取属性addIp的值
	 * @return addIp属性值
	 */
	public String getAddIp() {
		return addIp;
	}

	/**
	 * 设置属性addIp的值
	 * @param  addIp属性值
	 */
	public void setAddIp(String addIp) {
		this.addIp = addIp;
	}

	/**
	 * 获取属性bondFlag的值
	 * @return bondFlag属性值
	 */
	public String getBondFlag() {
		return bondFlag;
	}

	/**
	 * 设置属性bondFlag的值
	 * @param  bondFlag属性值
	 */
	public void setBondFlag(String bondFlag) {
		this.bondFlag = bondFlag;
	}

	/**
	 * 获取属性investNo的值
	 * @return investNo属性值
	 */
	public String getInvestNo() {
		return investNo;
	}

	/**
	 * 设置属性investNo的值
	 * @param  investNo属性值
	 */
	public void setInvestNo(String investNo) {
		this.investNo = investNo;
	}

	/**
	 * 获取属性investDate的值
	 * @return investDate属性值
	 */
	public String getInvestDate() {
		return investDate;
	}

	/**
	 * 设置属性investDate的值
	 * @param  investDate属性值
	 */
	public void setInvestDate(String investDate) {
		this.investDate = investDate;
	}

	/**
	 * 获取属性originInvestId的值
	 * @return originInvestId属性值
	 */
	public String getOriginInvestId() {
		return originInvestId;
	}

	/**
	 * 设置属性originInvestId的值
	 * @param  originInvestId属性值
	 */
	public void setOriginInvestId(String originInvestId) {
		this.originInvestId = originInvestId;
	}

	/**
	 * 获取属性investStyle的值
	 * @return investStyle属性值
	 */
	public String getInvestStyle() {
		return investStyle;
	}

	/**
	 * 设置属性investStyle的值
	 * @param  investStyle属性值
	 */
	public void setInvestStyle(String investStyle) {
		this.investStyle = investStyle;
	}

	/**
	 * 获取属性realizeAmount的值
	 * @return realizeAmount属性值
	 */
	public BigDecimal getRealizeAmount() {
		return realizeAmount;
	}

	/**
	 * 设置属性realizeAmount的值
	 * @param  realizeAmount属性值
	 */
	public void setRealizeAmount(BigDecimal realizeAmount) {
		this.realizeAmount = realizeAmount;
	}

	/**
	 * 获取属性realizeInterest的值
	 * @return realizeInterest属性值
	 */
	public BigDecimal getRealizeInterest() {
		return realizeInterest;
	}

	/**
	 * 设置属性realizeInterest的值
	 * @param  realizeInterest属性值
	 */
	public void setRealizeInterest(BigDecimal realizeInterest) {
		this.realizeInterest = realizeInterest;
	}

	/**
	 * 获取属性freezeInterest的值
	 * @return freezeInterest属性值
	 */
	public BigDecimal getFreezeInterest() {
		return freezeInterest;
	}

	/**
	 * 设置属性freezeInterest的值
	 * @param  freezeInterest属性值
	 */
	public void setFreezeInterest(BigDecimal freezeInterest) {
		this.freezeInterest = freezeInterest;
	}

	/**
	 * 获取属性interestDate的值
	 * @return interestDate属性值
	 */
	public Date getInterestDate() {
		return interestDate;
	}

	/**
	 * 设置属性interestDate的值
	 * @param  interestDate属性值
	 */
	public void setInterestDate(Date interestDate) {
		this.interestDate = interestDate;
	}

	/**
	 * 获取属性endDate的值
	 * @return endDate属性值
	 */
	public Date getEndDate() {
		return endDate;
	}

	/**
	 * 设置属性endDate的值
	 * @param  endDate属性值
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	/**
	 * 获取属性userName的值
	 * @return userName属性值
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * 设置属性userName的值
	 * @param  userName属性值
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * 获取属性realName的值
	 * @return realName属性值
	 */
	public String getRealName() {
		return realName;
	}

	/**
	 * 设置属性realName的值
	 * @param  realName属性值
	 */
	public void setRealName(String realName) {
		this.realName = realName;
	}

	/**
	 * 获取属性mobile的值
	 * @return mobile属性值
	 */
	public String getMobile() {
		return mobile;
	}

	/**
	 * 设置属性mobile的值
	 * @param  mobile属性值
	 */
	public void setMobile(String mobile) {
		this.mobile = mobile;
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
	 * 获取属性exceptStatus的值
	 * @return exceptStatus属性值
	 */
	public String getExceptStatus() {
		return exceptStatus;
	}

	/**
	 * 设置属性exceptStatus的值
	 * @param  exceptStatus属性值
	 */
	public void setExceptStatus(String exceptStatus) {
		this.exceptStatus = exceptStatus;
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
	 * 获取用户输入的定向密码
	 * @return  用户输入的定向密码
	 */
	public String getPwd() {
		return pwd;
	}

	/**
	 * 设置用户输入的定向密码
	 * @param  用户输入的定向密码
	 */
	public void setPwd(String pwd) {
		this.pwd = pwd;
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
	 * 获取属性sellAmountMin的值
	 * @return sellAmountMin属性值
	 */
	public BigDecimal getSellAmountMin() {
		return sellAmountMin;
	}
	/**
	 * 设置属性sellAmountMin的值
	 * @param  sellAmountMin属性值
	 */
	public void setSellAmountMin(BigDecimal sellAmountMin) {
		this.sellAmountMin = sellAmountMin;
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
	 * 获取属性interestStartDays的值
	 * @return interestStartDays属性值
	 */
	public String getInterestStartDays() {
		return interestStartDays;
	}


	/**
	 * 设置属性interestStartDays的值
	 * @param  interestStartDays属性值
	 */
	public void setInterestStartDays(String interestStartDays) {
		this.interestStartDays = interestStartDays;
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


	public ProjectInvest prototype() {
		ProjectInvest invest = new ProjectInvest();
		BeanUtils.copyProperties(this, invest);
		return invest;
	}
	

	/**
	 *  获取计息时间 
	 * @author QianPengZhan
	 * @date 2016年9月22日
	 * @return
	 */
	public Date getInterestTime(){
		Date interestTime = DateUtils.getNow();
		if(this.getReviewTime() != null && this.getInterestStyle() != null){
			interestTime = this.getReviewTime(); 
			if(LoanEnum.INTEREST_STYLE_EV.eq(this.getInterestStyle())){
				interestTime = this.getReviewTime();
			}else if(LoanEnum.INTEREST_STYLE_TN.eq(this.getInterestStyle())){//T+N
				interestTime = DateUtils.rollDay(this.getReviewTime(), Integer.parseInt(interestStartDays));
			}
		}
		return interestTime;
	}
	
	/**
	 * 获取实时剩余期限（当前时间到最后的还款日中间的时间天数）
	 * @author QianPengZhan
	 * @date 2016年9月22日
	 * @return
	 */
	public int getProjectRealRemainDays(){
		return  (int)DateUtils.daysBetween(DateUtils.getNow(), this.getLastRepayTime()==null?DateUtils.getNow():this.getLastRepayTime());
	}
	
	/**
	 * 获取总期限（计息日到最后的还款日中间的时间天数）
	 * @author QianPengZhan
	 * @date 2016年9月22日
	 * @return
	 */
	public int getProjectTotalRemainDays(){
		return  (int)DateUtils.daysBetween(getInterestTime(),this.getLastRepayTime()==null?DateUtils.getNow():this.getLastRepayTime());
	}
	
	/**
	 * 获取可转让列表的某些信息
	 * @author QianPengZhan
	 * @date 2016年8月12日
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public Map<String,Object> getAbleBondData(ProjectInvestModel model){
		Map<String,Object> map = Maps.newHashMap();
		
		//1、按照期数倒序排序来查询未还款的待收   可以获得第一期的未还待收  和 最后一期待收
		ProjectCollection pcModel = new ProjectCollection();
		pcModel.setUserId(model.getUserId());//用户ID
		pcModel.setInvestId(model.getUuid());//投资记录ID
		pcModel.setProjectId(model.getProjectId());//项目ID
		pcModel.setStatus(ProjectCollectionEnum.STATUS_NOT_REPAY.getValue());//未还
		Page<ProjectCollection> page = new Page<ProjectCollection>();
		page.setSort(InvestEnum.INVEST_PAGE_SORT_PERIOD.getValue());
		page.setOrder(Constant.ASC);
		pcModel.setPage(page);
		ProjectCollectionService projectCollectionService = (ProjectCollectionService)SpringContextHolder.getBean("projectCollectionService");
		List<ProjectCollection> pcList = projectCollectionService.findPcByPIdAndUserId(pcModel);
		
		//2、初始化需要获取的值
		final Date createTime = model.getCreateTime();//交易登记时间
		final Integer createTimeString = createTime.getHours();
		int interestStartDays = Constant.INT_ZERO;//T+N的N天
		Date repayTime = DateUtils.getNow();//最后一笔待收还款时间
		Date nowTime = DateUtils.getNow();//当前时间
		Date interestTime = DateUtils.getNow();//计息时间  如果一期未还就是标的成立审核时间
		Date nextRepayDate = DateUtils.getNow();//本期还款时间
		Integer holdDay = Constant.INT_ZERO;//持有天数  默认值为0
		Integer remainDay = Constant.INT_ZERO;//剩余期限  默认值为0
		Integer total =Constant.INT_ZERO;//总天数
		BigDecimal totalCollection = BigDecimal.ZERO;//未还款的总待收利息
		Integer startPeriod = Constant.INT_ZERO;//债权的开始期数
		Integer periodDay = Constant.INT_ZERO;//债权的开始期数
		BigDecimal holdDayCollection = BigDecimal.ZERO;//持有天数收益
		BigDecimal interestManageRate = BigDecimal.ZERO;//利息管理费率
		Integer periodTotalDay = Constant.INT_ZERO;//债权的本期总天数
		String addTime = StringUtils.EMPTY;//交易登记日时间分界点(T+N计息，最长格式HH:MM:SS，暂到小时HH)
		//3、根据待收信息计算需要的值
		if (CollectionUtils.isNotEmpty(pcList)) {
			
			//a)获取利息管理费率    T+N的N推迟计息时间点
			ProjectService projectService = (ProjectService)SpringContextHolder.getBean("projectService");
			Project project = projectService.get(model.getProjectId());
			interestManageRate =project.getInterestManageRate();
			interestStartDays = project.getInterestStartDays();
			addTime = StringUtils.isNull(project.getInterestTime());
			final Integer addTimeString = Integer.parseInt(StringUtils.EMPTY.equals(addTime)?"0":addTime);
			//b)该笔投资记录对应的所有未还款的待收利息和    分期的取未还的第一期的利息
			

			//c)获取本期待收  	第一期即为本期待收
			ProjectCollection fisrt = pcList.get(Constant.INT_ZERO);
			boolean firstBl = true;
			for(ProjectCollection pc : pcList){
				totalCollection = totalCollection.add(BigDecimalUtils.defaultIfNull(pc.getInterest()));
				//第一个出现未还即为本期待收
				if (ProjectCollectionEnum.STATUS_NOT_REPAY.getValue().equals(pc.getStatus()) && firstBl) {
					fisrt = pc;
					firstBl = false;
				}
			}
			
			startPeriod = fisrt.getPeriod();
			
			//d) 获取最后一期待收 
			ProjectCollection last = pcList.get(pcList.size()-Constant.INT_ONE);
			
			//e)获取本期应还款时间  ：即为第一期的应还款时间
			nextRepayDate = fisrt.getRepayTime();
			LOGGER.info("本期应还款时间：{}",nextRepayDate);
			
			//f)获取应还款时间 ：该笔标的最终应还款时间为最后一期的应还款时间
			repayTime = last.getRepayTime();
			LOGGER.info("应还款时间：{}",repayTime);
			
			final ProjectCollection beforePc = projectCollectionService.getProjectCollectionByInvestIdAndPeriod(fisrt.getInvestId(), fisrt.getPeriod()-Constant.INT_ONE);
			final Date lastRepayTime = beforePc == null?DateUtils.rollMon(fisrt.getRepayTime(),Constant.INT_ONE_NEGA):beforePc.getRepayTime();//上一期的还款时间
			LOGGER.info("上一期的还款时间：{}",lastRepayTime);
			
			//g) 获取计息时间   ：如果期数是第一期  那么计息时间就是成立审核时间
			if(fisrt.getPeriod() == Constant.INT_ONE){//如果是第一期
				if(LoanEnum.INTEREST_STYLE_EV.eq(project.getInterestStyle())){
					interestTime = project.getReviewTime();
				}else if(LoanEnum.INTEREST_STYLE_TN.eq(project.getInterestStyle())){//T+N 计息  取交易时间来判断
					if(createTimeString > addTimeString){//交易时间 再 设置值 之后  为 T+N+1
						interestTime = DateUtils.rollDay(model.getCreateTime(), interestStartDays+1);
					}else{
						interestTime = DateUtils.rollDay(model.getCreateTime(), interestStartDays);
					}
					LOGGER.info("计息时间【第一期】：{},{},{}",interestTime,createTimeString,addTimeString);
				}
			}else{//已经还了几期的  那么本期就是未还的第一期 
				LOGGER.info("计息时间【非第一期】：{}",interestTime);
				interestTime = lastRepayTime;//第一期的还款时间作为计息时间
			}
			
			//h)计算距离本期到期日天数    当前到本期还款时间的距离
			periodDay = (int)DateUtils.daysBetween(nowTime, nextRepayDate);
			
			//i)计算持有天数
			int tempHoldDay  = (int)DateUtils.daysBetween(interestTime, nowTime);
			holdDay = tempHoldDay < Constant.INT_ZERO ? Constant.INT_ZERO : tempHoldDay;
			LOGGER.info("计算持有天数：{}",holdDay);
			
			//j)计算总天数  一次性还款是直接总的  分期的都是按照第一期的来算
			total = (int)DateUtils.daysBetween(interestTime, repayTime);
			LOGGER.info("计算总天数：{}",total);
			
			//k) 计算本期的总天数
			periodTotalDay = (int)DateUtils.daysBetween(interestTime, nextRepayDate);
			LOGGER.info("本期的总天数：{}",periodTotalDay);
			
			BigDecimal caluCollection = BigDecimal.ZERO;
			int caluTotalIntDay = Constant.INT_ZERO;
			if(LoanEnum.STYLE_AVERAGE_CAPITAL_REPAYMENT.eq(project.getRepayStyle()) || LoanEnum.STYLE_INSTALLMENT_REPAYMENT.eq(project.getRepayStyle())){
				caluTotalIntDay = periodTotalDay;//等额本息或者等额本金的时候  计算当前的总天数
				caluCollection = fisrt.getInterest();//等额本息或者等额本金的时候  计算持有利息 用  本期的待收利息去计算
			}else{
				caluTotalIntDay = total;//一次性还款方式、每月还息到期还本、每季还息到期还本    总天数
				caluCollection = totalCollection;//一次性还款方式、每月还息到期还本、每季还息到期还本   用总的待收利息去计算  都是减掉N天的时间
			}
			BigDecimal caluTotalDay = BigDecimal.valueOf(caluTotalIntDay);
			holdDayCollection = BigDecimalUtils.div(BigDecimalUtils.mul(caluCollection,BigDecimalUtils.valueOf(holdDay.toString())),caluTotalDay,Constant.INT_TEN);
					
			//l)计算剩余天数
			remainDay = total - holdDay;
			LOGGER.info("-------------------计算债权的相关参数-------------start-=----------");
			LOGGER.info("-------------------利息管理费率：数据库中获取：{}--------------=----------",interestManageRate);
			LOGGER.info("-------------债权持有待收利息=总待收利息(等额本息或者等额本金为本期的待收利息)*持有天数/总天数(等额本息或者等额本金为本期总天数)={}*{}/{}={}-----------",caluCollection,holdDay,caluTotalDay,holdDayCollection);
			LOGGER.info("-------------------计算债权的相关参数-------------end-=----------");
		}
		//4、传递参数
		map.put("interestManageRate", interestManageRate);
		map.put("holdDayCollection", holdDayCollection);
		map.put("totalCollection", totalCollection);
		map.put("periodDay", periodDay);
		map.put("total", total);
		map.put("holdDay", holdDay);
		map.put("remainDay", remainDay);
		map.put("startPeriod", startPeriod);
		map.put("nextRepayDate", nextRepayDate);
		map.put("repayTime", repayTime );
		return map;
	}
	
	/**
	 * 判断转让次数是否够了  
	 * @author QianPengZhan
	 * @return
	 */
	public boolean judge(){
		boolean flag = false;
		final int ableCounts = this.getBondMaxTurn();//默认为1,单次转让，0为无限次转让  设置的次数
		int realCounts = Constant.INT_ZERO ;//计算得来  默认是1
		if(Constant.INT_ZERO == ableCounts){//无限次转让  不受限制
			flag = false;
		}else{
			BondService bondService = (BondService)SpringContextHolder.getBean("bondService");
			realCounts = bondService.calculateCounts(this.getUuid(),realCounts);
			if(realCounts >= ableCounts){
				flag = true;
			}
		}
		LOGGER.info("设置的转让次数：{},实际转让次数：{},是否过滤：{}",ableCounts,realCounts,flag);
		return flag;
	}
	

	/**
	 * 获取属性pwdHasVerify的值
	 * @return pwdHasVerify属性值
	 */
	public Boolean getPwdHasVerify() {
		return pwdHasVerify;
	}


	/**
	 * 设置属性pwdHasVerify的值
	 * @param  pwdHasVerify属性值
	 */
	public void setPwdHasVerify(Boolean pwdHasVerify) {
		this.pwdHasVerify = pwdHasVerify;
	}


	/**
	 * 获取属性raiseInterest的值
	 * @return raiseInterest属性值
	 */
	public BigDecimal getRaiseInterest() {
		return raiseInterest;
	}


	/**
	 * 设置属性raiseInterest的值
	 * @param  raiseInterest属性值
	 */
	public void setRaiseInterest(BigDecimal raiseInterest) {
		this.raiseInterest = raiseInterest;
	}


	/**
	 * @return the bondMaxTurn
	 */
	public Integer getBondMaxTurn() {
		return bondMaxTurn;
	}


	/**
	 * @param bondMaxTurn the bondMaxTurn to set
	 */
	public void setBondMaxTurn(Integer bondMaxTurn) {
		this.bondMaxTurn = bondMaxTurn;
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
	 * 获取属性waitRaiseInterest的值
	 * @return waitRaiseInterest属性值
	 */
	public BigDecimal getWaitRaiseInterest() {
		return waitRaiseInterest;
	}


	/**
	 * 设置属性waitRaiseInterest的值
	 * @param  waitRaiseInterest属性值
	 */
	public void setWaitRaiseInterest(BigDecimal waitRaiseInterest) {
		this.waitRaiseInterest = waitRaiseInterest;
	}


	public String getSmsCode() {
		return smsCode;
	}


	public void setSmsCode(String smsCode) {
		this.smsCode = smsCode;
	}


	/**
	 * 获取属性chkResult的值
	 * @return chkResult属性值
	 */
	public String getChkResult() {
		String result= StringUtils.EMPTY;
		if("0".equals(this.chkResult)){
			result = "其他异常";
		}else if("1".equals(this.chkResult)){
			result = "投资冻结成功";
		}else if("2".equals(this.chkResult)){
			result = "投资冻结失败";
		}else if("3".equals(this.chkResult)){
			result = "投资撤销解冻成功";
		}else if("4".equals(this.chkResult)){
			result = "投资撤销解冻失败";
		}else if("5".equals(this.chkResult)){
			result = "投资部分冻结";
		}else if("6".equals(this.chkResult)){
			result = "放款解冻";
		}     
		return result;
	}


	/**
	 * 设置属性chkResult的值
	 * @param  chkResult属性值
	 */
	public void setChkResult(String chkResult) {
		this.chkResult = chkResult;
	}

	/**
	 * 设置属性futureTime的值
	 * @param  futureTime属性值
	 */
	public void setFutureTime(String futureTime) {
		this.futureTime = futureTime;
	}

	/**
	 * 获取属性futureTime的值
	 * @return futureTime属性值
	 */
	public String getFutureTime() {
		return futureTime;
	}

	/**
	 * 获取属性futureTime的值
	 * @return futureTime属性值
	 */
	public void dealFutureTime() {
		Date futureTime=null; //默认全部
		final String dateTypeTemp = getDateType();
		if(StringUtils.isNotBlank(dateTypeTemp)){
			final Date now = DateUtils.getNow();
			if(CommonConstant.DATE_TYPE_WEEK.equals(dateTypeTemp)){  //未来7天
				futureTime = DateUtils.rollDay(now, 7);
			} else if(CommonConstant.DATE_TYPE_MONTH.equals(dateTypeTemp)){   //未来1月
				futureTime = DateUtils.rollMon(now, 1);
			} else if(CommonConstant.DATE_TYPE_QUARTER.equals(dateTypeTemp)){ //未来3月
				futureTime = DateUtils.rollMon(now, 3);
			} else if(CommonConstant.DATE_TYPE_YEAR.equals(dateTypeTemp)){    //未来1年
				futureTime = DateUtils.rollYear(now, 1);
			}
		}
		this.setFutureTime(futureTime==null? null:DateUtils.getDateEnd(futureTime));
	}
	
}
