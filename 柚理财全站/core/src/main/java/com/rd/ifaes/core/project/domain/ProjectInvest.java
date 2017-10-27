package com.rd.ifaes.core.project.domain;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

import org.springframework.beans.BeanUtils;

import com.rd.ifaes.common.annotation.DictType;
import com.rd.ifaes.common.dict.InvestEnum;
import com.rd.ifaes.common.entity.BaseEntity;
import com.rd.ifaes.common.util.DateUtils;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.core.project.model.ProjectInvestModel;
import com.rd.ifaes.core.tpp.model.ufx.UfxAutoInvestModel;

/**
 * entity:ProjectInvest
 * 
 * @author FangJun
 * @version 3.0
 * @date 2016-7-18
 */
public class ProjectInvest extends BaseEntity<ProjectInvest> {
	
	private static final long serialVersionUID = 1L;
	
	private String	userId;		 /* 用户ID */ 
	private String	projectId;		 /* 项目ID */ 
	@DictType(type="investStatus")
	private String	status;		 /* 投资状态 0 待支付，1成功，2失败 3退款处理中 4 投资作废（债权）  5 超时取消 6订单失效,被再次支付 */ 
	private BigDecimal	money;		 /* 投标金额 */ 
	private BigDecimal	amount;		 /* 有效投资金额 */ 
	private BigDecimal	realAmount;		 /* 用户真实资金 */ 
	private BigDecimal	payment;		 /* 预收本息 */ 
	private BigDecimal	interest;		 /* 预期利息 */ 
	private BigDecimal	raiseInterest;		 /* 加息利息 */ 
	private BigDecimal	repayedAmount;		 /* 已还金额 */ 
	private BigDecimal	repayedInterest;		 /* 已还利息 */ 
	private BigDecimal	waitAmount;		 /* 待还总额 */ 
	private BigDecimal	waitInterest;		 /* 待还利息(含加息) */ 
	private BigDecimal waitRaiseInterest; /* 待还加息*/
	@DictType(type="investType")
	private String	investType;		 /* 投资方式  0手动投资;1自动投资 */ 
	@DictType(type="saleChannel")
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
	private BigDecimal freezeCapital;  /* 变现冻结本金 */
	private BigDecimal freezeInterest;  /* 变现冻结利息 */
	private String realizeId;   /*首次变现ID*/
	private Date	interestDate;		 /* 起息时间 */ 
	private Date	endDate;		 /* 结束时间 */ 
	
	private String authCode;//江西银行投标成功授权码

	//其他自定义属性
	private String userName; // 用户账号
	private String realName;// 真实姓名
	private String mobile; // 手机号码
	private String projectName;// 借款名称
	private Integer periods;// 期数
	private String projectStatus;//项目状态，供查询使用
	private String[] projectStatusSet;//项目状态，供查询使用
	private String exceptStatus;// 除外的状态	
	private  String borrowFlag;//借款标识
	private String[] investStyleSet;//投资类型集合
	private String[] statusSet;//投资状态集合
	// Constructor
	public ProjectInvest() {
	}

	public ProjectInvest(UfxAutoInvestModel autoInvestModel, BigDecimal amount) {
		this.investOrderNo = autoInvestModel.getOrderNo();
		this.status = InvestEnum.STATUS_SUCCESS.getValue();
		this.createTime = DateUtils.getNow();
		this.money = amount;
		this.realAmount = amount;
		this.amount = amount;
		this.investChannel = InvestEnum.INVEST_CHANNEL_PC.getValue();
		this.investType = InvestEnum.INVEST_TYPE_AUTO.getValue();
		this.realizeFlag = InvestEnum.REALIZE_FLAG_NORMAL.getValue();
		this.bondFlag = InvestEnum.BOND_FLAG_NORMAL.getValue();
		this.investNo = autoInvestModel.getInvestNo();
		this.investDate = autoInvestModel.getInvestDate();
		this.freezeNo = autoInvestModel.getFreezeNo();
		this.realizeAmount = BigDecimal.ZERO;
		this.realizeInterest = BigDecimal.ZERO;
		this.freezeInterest = BigDecimal.ZERO;
		this.payment = BigDecimal.ZERO;
		this.interest = BigDecimal.ZERO;
		this.repayedAmount = BigDecimal.ZERO;
		this.repayedInterest = BigDecimal.ZERO;
		this.waitAmount = BigDecimal.ZERO;
		this.waitInterest = BigDecimal.ZERO;
		this.investStyle = InvestEnum.INVEST_STYLE_NORMAL.getValue();
	}
	
	public ProjectInvest(Map<String, Object> map, BigDecimal amount) {
		String orderId = StringUtils.isNull(map.get("orderId"));
		this.authCode = StringUtils.isNull(map.get("authCode"));
		this.investOrderNo = orderId ;
		this.status = InvestEnum.STATUS_SUCCESS.getValue();
		this.createTime = DateUtils.getNow();
		this.money = amount;
		this.realAmount = (BigDecimal) map.get("realAmount");
		this.amount = amount;
		this.investChannel = InvestEnum.INVEST_CHANNEL_PC.getValue();
		this.investType = InvestEnum.INVEST_TYPE_AUTO.getValue();
		this.realizeFlag = InvestEnum.REALIZE_FLAG_NORMAL.getValue();
		this.bondFlag = InvestEnum.BOND_FLAG_NORMAL.getValue();
		this.investNo = orderId;
		this.investDate = DateUtils.dateStr7(DateUtils.getNow());
		this.freezeNo = orderId;
		this.realizeAmount = BigDecimal.ZERO;
		this.realizeInterest = BigDecimal.ZERO;
		this.freezeCapital = BigDecimal.ZERO;
		this.freezeInterest = BigDecimal.ZERO;
		this.payment = BigDecimal.ZERO;
		this.interest = BigDecimal.ZERO;
		this.repayedAmount = BigDecimal.ZERO;
		this.repayedInterest = BigDecimal.ZERO;
		this.waitAmount = BigDecimal.ZERO;
		this.waitInterest = BigDecimal.ZERO;
		this.investStyle = InvestEnum.INVEST_STYLE_NORMAL.getValue();
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public BigDecimal getMoney() {
		return money;
	}

	public void setMoney(BigDecimal money) {
		this.money = money;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public BigDecimal getRealAmount() {
		return realAmount;
	}

	public void setRealAmount(BigDecimal realAmount) {
		this.realAmount = realAmount;
	}

	public BigDecimal getPayment() {
		return payment;
	}

	public void setPayment(BigDecimal payment) {
		this.payment = payment;
	}

	public BigDecimal getInterest() {
		return interest;
	}

	public void setInterest(BigDecimal interest) {
		this.interest = interest;
	}

	public BigDecimal getRaiseInterest() {
		return raiseInterest == null ? BigDecimal.ZERO : raiseInterest;
	}

	public void setRaiseInterest(BigDecimal raiseInterest) {
		this.raiseInterest = raiseInterest;
	}

	public BigDecimal getRepayedAmount() {
		return repayedAmount;
	}

	public void setRepayedAmount(BigDecimal repayedAmount) {
		this.repayedAmount = repayedAmount;
	}

	public BigDecimal getRepayedInterest() {
		return repayedInterest;
	}

	public void setRepayedInterest(BigDecimal repayedInterest) {
		this.repayedInterest = repayedInterest;
	}

	public BigDecimal getWaitAmount() {
		return waitAmount;
	}

	public void setWaitAmount(BigDecimal waitAmount) {
		this.waitAmount = waitAmount;
	}

	public BigDecimal getWaitInterest() {
		return waitInterest;
	}

	public void setWaitInterest(BigDecimal waitInterest) {
		this.waitInterest = waitInterest;
	}

	public String getInvestType() {
		return investType;
	}

	public void setInvestType(String investType) {
		this.investType = investType;
	}

	public String getInvestOrderNo() {
		return investOrderNo;
	}

	public void setInvestOrderNo(String investOrderNo) {
		this.investOrderNo = investOrderNo;
	}

	public String getFreezeNo() {
		return freezeNo;
	}

	public void setFreezeNo(String freezeNo) {
		this.freezeNo = freezeNo;
	}

	public String getUserFirstInvest() {
		return userFirstInvest;
	}

	public void setUserFirstInvest(String userFirstInvest) {
		this.userFirstInvest = userFirstInvest;
	}

	public String getRealizeFlag() {
		return realizeFlag;
	}

	public void setRealizeFlag(String realizeFlag) {
		this.realizeFlag = realizeFlag;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getAddIp() {
		return addIp;
	}

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

	public String getInvestNo() {
		return investNo;
	}

	public void setInvestNo(String investNo) {
		this.investNo = investNo;
	}

	public String getInvestDate() {
		return investDate;
	}

	public void setInvestDate(String investDate) {
		this.investDate = investDate;
	}
	
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getExceptStatus() {
		return exceptStatus;
	}

	public void setExceptStatus(String exceptStatus) {
		this.exceptStatus = exceptStatus;
	}
	
	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public Integer getPeriods() {
		return periods;
	}

	public void setPeriods(Integer periods) {
		this.periods = periods;
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

	public BigDecimal getCollectionTotal() {
		BigDecimal total = amount;
		if(interest != null){
			total = total.add(interest);
		}
		if(raiseInterest!=null){
			total = total.add(raiseInterest);
		}
		return total;
	}

	/**
	 * 复制本身的属性
	 * @author QianPengZhan
	 * @date 2016年8月29日
	 * @return
	 */
	public ProjectInvest instance(){
		ProjectInvest projectInvest = new ProjectInvest();
		BeanUtils.copyProperties(this, projectInvest);
		return projectInvest;
	}
	
	/**
	 * 复制本身属性给Model
	 * @author QianPengZhan
	 * @date 2016年8月29日
	 * @return
	 */
	public ProjectInvestModel prototype() {
		ProjectInvestModel projectInvestModel = new ProjectInvestModel();
		BeanUtils.copyProperties(this, projectInvestModel);
		return projectInvestModel;
	} 
	/**
	 * 获取投资渠道值
	 * @return 投资渠道
	 */
	public String getInvestChannel() {
		return investChannel;
	}

	/**
	 * 设置投资渠道
	 * @param 投资渠道
	 */
	public void setInvestChannel(String investChannel) {
		this.investChannel = investChannel;
	}

	@Override
	public String toString() {
		return "ProjectInvest [" + "uuid=" + uuid + ", userId=" + userId + ", projectId=" + projectId + ", status=" + status + ", money=" + money + ", amount=" + amount + ", realAmount=" + realAmount + ", payment=" + payment + ", interest=" + interest + ", repayedAmount=" + repayedAmount + ", repayedInterest=" + repayedInterest + ", waitAmount=" + waitAmount + ", waitInterest=" + waitInterest + ", investType=" + investType + ", investOrderNo=" + investOrderNo + ", freezeNo=" + freezeNo + ", userFirstInvest=" + userFirstInvest + ", realizeFlag=" + realizeFlag + ", createTime=" + createTime + ", addIp=" + addIp + ", bondFlag=" + bondFlag + ", investNo=" + investNo + ", investDate=" + investDate +  "]";
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

	public String getProjectStatus() {
		return projectStatus;
	}

	public void setProjectStatus(String projectStatus) {
		this.projectStatus = projectStatus;
	}

	/**
	 * 获取属性investStyleSet的值
	 * @return investStyleSet属性值
	 */
	public String[] getInvestStyleSet() {
		return investStyleSet;
	}

	/**
	 * 设置属性investStyleSet的值
	 * @param  investStyleSet属性值
	 */
	public void setInvestStyleSet(String[] investStyleSet) {
		this.investStyleSet = investStyleSet;
	}

	/**
	 * 获取属性projectStatusSet的值
	 * @return projectStatusSet属性值
	 */
	public String[] getProjectStatusSet() {
		return projectStatusSet;
	}

	/**
	 * 设置属性projectStatusSet的值
	 * @param  projectStatusSet属性值
	 */
	public void setProjectStatusSet(String[] projectStatusSet) {
		this.projectStatusSet = projectStatusSet;
	}

	/**
	 * 获取属性statusSet的值
	 * @return statusSet属性值
	 */
	public String[] getStatusSet() {
		return statusSet;
	}

	/**
	 * 设置属性statusSet的值
	 * @param  statusSet属性值
	 */
	public void setStatusSet(String[] statusSet) {
		this.statusSet = statusSet;
	}

	/**
	 * 获取属性freezeCapital的值
	 * @return freezeCapital属性值
	 */
	public BigDecimal getFreezeCapital() {
		return freezeCapital;
	}

	/**
	 * 设置属性freezeCapital的值
	 * @param  freezeCapital属性值
	 */
	public void setFreezeCapital(BigDecimal freezeCapital) {
		this.freezeCapital = freezeCapital;
	}

	/**
	 * 获取属性waitRealInterest的值
	 * @return waitRealInterest属性值
	 */
	public BigDecimal getWaitRaiseInterest() {
		return waitRaiseInterest;
	}

	/**
	 * 设置属性waitRealInterest的值
	 * @param  waitRealInterest属性值
	 */
	public void setWaitRaiseInterest(BigDecimal waitRaiseInterest) {
		this.waitRaiseInterest = waitRaiseInterest;
	}

	/**
	 * 获取属性realizeId的值
	 * @return realizeId属性值
	 */
	public String getRealizeId() {
		return realizeId;
	}

	/**
	 * 设置属性realizeId的值
	 * @param  realizeId属性值
	 */
	public void setRealizeId(String realizeId) {
		this.realizeId = realizeId;
	}

	public String getAuthCode() {
		return authCode;
	}

	public void setAuthCode(String authCode) {
		this.authCode = authCode;
	}
}
