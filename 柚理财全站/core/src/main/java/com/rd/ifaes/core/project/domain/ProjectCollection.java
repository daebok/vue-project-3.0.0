package com.rd.ifaes.core.project.domain;

import java.math.BigDecimal;
import java.util.Date;

import com.rd.ifaes.common.entity.BaseEntity;
import com.rd.ifaes.common.util.BigDecimalUtils;
import com.rd.ifaes.common.util.DateUtils;
import com.rd.ifaes.core.core.constant.Constant;

/**
 * entity:ProjectCollection
 * 
 * @author FangJun
 * @version 3.0
 * @date 2016-7-14
 */
public class ProjectCollection extends BaseEntity<ProjectCollection> {
	
	private static final long serialVersionUID = 1L;
	
	private String	userId;		 /* 用户ID */ 
	private String	projectId;		 /* 项目ID */ 
	private String	investId;		 /* 投标ID */ 
	private Date	repayTime;		 /* 预计还款时间 */ 
	private Date	lastRepayTime;		 /* 已经还款时间 */ 
	private String	status;		 /* 还款状态  0未还款 1已还款 */ 
	private BigDecimal	payment;		 /* 预还金额(本金+利息+加息) */ 
	private BigDecimal	repayedAmount;		 /* 已还金额(本金+利息+加息) */ 
	private BigDecimal	capital;		 /* 本金 */ 
	private BigDecimal	interest;		 /* 利息 */ 
	private BigDecimal	raiseInterest;		 /* 加息利息 */ 
	private Integer	lateDays;		 /* 逾期天数 */ 
	private BigDecimal	lateInterest;		 /* 逾期利息 */ 
	private BigDecimal merchantLateInterest;/*给平台的逾期罚息*/ 
	private BigDecimal	manageFee;		 /* 利息管理费 */ 
	private String	collectionType;		 /* 待收类型  0普通待收;1转让人待收 2 受让人待收 3 已作废  */ 
	private Integer	period;		 /* 期数，从1开始 */ 
	private BigDecimal	bondCapital;		 /* 已成功转出本金 */ 
	private BigDecimal	bondInterest;		 /* 已成功转出利息 */ 
	private String	addIp;		 /* 添加ip */ 
	private Date	createTime;		 /* 添加时间 */ 
	private String 	parentId;  /*父ID*/
	
	/**
	 * 借款人管理
	 */
	private BigDecimal BorrowManageFee;
	//其他自定义属性
	private String repayMonth; //还款月份
	private String userName; // 用户名
	private String realName;// 投资人姓名
	private String projectName;// 借款名称
	private Integer periods;// 总期数
	private String startTime;// 开始时间（查询）
	private String endTime;// 结束时间（查询）
	String[] collectionTypeSet;//类型集合
	private BigDecimal freezeCapital;//变现冻结本金
	private BigDecimal freezeInterest;//变现冻结利息
	private String borrowFlag;/* 借款标标识(1是，借款标， 0否，即是理财产品） */
	private String repayTypeStr;//还款金额类型
	// Constructor
	public ProjectCollection() {
	}
	
	public ProjectCollection(String userId, String repayMonth) {
		super();
		this.userId = userId;
		this.repayMonth = repayMonth;
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

	public String getInvestId() {
		return investId;
	}

	public void setInvestId(String investId) {
		this.investId = investId;
	}

	public Date getRepayTime() {
		return repayTime;
	}

	public void setRepayTime(Date repayTime) {
		this.repayTime = repayTime;
	}

	public Date getLastRepayTime() {
		return lastRepayTime;
	}

	public void setLastRepayTime(Date lastRepayTime) {
		this.lastRepayTime = lastRepayTime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public BigDecimal getPayment() {
		return payment;
	}

	public void setPayment(BigDecimal payment) {
		this.payment = payment;
	}

	public BigDecimal getRepayedAmount() {
		return repayedAmount;
	}

	public void setRepayedAmount(BigDecimal repayedAmount) {
		this.repayedAmount = repayedAmount;
	}

	public BigDecimal getCapital() {
		return capital;
	}

	public void setCapital(BigDecimal capital) {
		this.capital = capital;
	}

	public BigDecimal getInterest() {
		return interest;
	}

	public void setInterest(BigDecimal interest) {
		this.interest = interest;
	}

	public BigDecimal getRaiseInterest() {
		return raiseInterest;
	}

	public void setRaiseInterest(BigDecimal raiseInterest) {
		this.raiseInterest = raiseInterest;
	}

	public Integer getLateDays() {
		return lateDays;
	}

	public void setLateDays(Integer lateDays) {
		this.lateDays = lateDays;
	}

	public BigDecimal getLateInterest() {
		return lateInterest;
	}

	public void setLateInterest(BigDecimal lateInterest) {
		this.lateInterest = lateInterest;
	}
	
	/**
	 * 获取属性merchantLateInterest的值
	 * @return merchantLateInterest属性值
	 */
	public BigDecimal getMerchantLateInterest() {
		return merchantLateInterest;
	}

	/**
	 * 设置属性merchantLateInterest的值
	 * @param  merchantLateInterest属性值
	 */
	public void setMerchantLateInterest(BigDecimal merchantLateInterest) {
		this.merchantLateInterest = merchantLateInterest;
	}

	public BigDecimal getManageFee() {
		return manageFee;
	}

	public void setManageFee(BigDecimal manageFee) {
		this.manageFee = manageFee;
	}

	public String getCollectionType() {
		return collectionType;
	}

	public void setCollectionType(String collectionType) {
		this.collectionType = collectionType;
	}

	public Integer getPeriod() {
		return period;
	}

	public void setPeriod(Integer period) {
		this.period = period;
	}

	public BigDecimal getBondCapital() {
		return bondCapital;
	}

	public void setBondCapital(BigDecimal bondCapital) {
		this.bondCapital = bondCapital;
	}

	public BigDecimal getBondInterest() {
		return bondInterest;
	}

	public void setBondInterest(BigDecimal bondInterest) {
		this.bondInterest = bondInterest;
	}

	public String getAddIp() {
		return addIp;
	}

	public void setAddIp(String addIp) {
		this.addIp = addIp;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getRepayMonth() {
		return repayMonth;
	}

	public void setRepayMonth(String repayMonth) {
		this.repayMonth = repayMonth;
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

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public void setPeriods(Integer periods) {
		this.periods = periods;
	}

	public Integer getPeriods() {
		return periods;
	}

	public String getPeriodsStr() {
		return period + "/" + periods;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	/**
	 * 获取属性parentId的值
	 * @return parentId属性值
	 */
	public String getParentId() {
		return parentId;
	}

	/**
	 * 设置属性parentId的值
	 * @param  parentId属性值
	 */
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	/**
	 * 获取属性collectionTypeSet的值
	 * @return collectionTypeSet属性值
	 */
	public String[] getCollectionTypeSet() {
		return collectionTypeSet;
	}

	/**
	 * 设置属性collectionTypeSet的值
	 * @param  collectionTypeSet属性值
	 */
	public void setCollectionTypeSet(String[] collectionTypeSet) {
		this.collectionTypeSet = collectionTypeSet;
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
	 * 获取属性repayTypeStr的值
	 * @return repayTypeStr属性值
	 */
	public String getRepayTypeStr() {
		return repayTypeStr;
	}

	/**
	 * 设置属性repayTypeStr的值
	 * @param  repayTypeStr属性值
	 */
	public void setRepayTypeStr(String repayTypeStr) {
		this.repayTypeStr = repayTypeStr;
	}

	public String getBorrowFlag() {
		return borrowFlag;
	}

	public void setBorrowFlag(String borrowFlag) {
		this.borrowFlag = borrowFlag;
	}
	
	public String getLastRepayTimeStr(){
		String lastRepayTimeStr = "";
		if(Constant.FLAG_YES.equals(getStatus())){ //已还
			if(getRepayedAmount()!=null && getLastRepayTime()!=null){
				lastRepayTimeStr = DateUtils.dateStr2(getLastRepayTime());
			}
		}else{ //未还
			if(getLateDays()!=null && getLateDays()>0){//已逾期
				lastRepayTimeStr="已逾期";
			}else{ 
				lastRepayTimeStr="未到期";
			}
		}
		return lastRepayTimeStr;
	}
	
	
	public String getRepayedAmountStr(){
		String repayedAmountStr = "";
		if(Constant.FLAG_YES.equals(getStatus())){ //已还
			if(getRepayedAmount()!=null){
				repayedAmountStr = String.valueOf(BigDecimalUtils.round(getRepayedAmount(), 2));
			}
		}else{ //未还
			if(getLateDays()!=null && getLateDays()>0){//已逾期
				repayedAmountStr="已逾期";
			}else{ 
				repayedAmountStr="未到期";
			}
		}
		return repayedAmountStr;
	}
	

	public BigDecimal getBorrowManageFee() {
		return BorrowManageFee;
	}

	public void setBorrowManageFee(BigDecimal borrowManageFee) {
		BorrowManageFee = borrowManageFee;
	}

	@Override
	public String toString() {
		return "ProjectCollection [userId=" + userId + ", projectId=" + projectId + ", investId=" + investId + ", repayTime="
				+ repayTime + ", lastRepayTime=" + lastRepayTime + ", status=" + status + ", payment=" + payment
				+ ", repayedAmount=" + repayedAmount + ", capital=" + capital + ", interest=" + interest + ", raiseInterest="
				+ raiseInterest + ", lateDays=" + lateDays + ", lateInterest=" + lateInterest + ", merchantLateInterest="
				+ merchantLateInterest + ", manageFee=" + manageFee + ", collectionType=" + collectionType + ", period=" + period
				+ ", bondCapital=" + bondCapital + ", bondInterest=" + bondInterest + ", addIp=" + addIp + ", createTime="
				+ createTime + ", parentId=" + parentId + ", BorrowManageFee=" + BorrowManageFee + "]";
	}

}
