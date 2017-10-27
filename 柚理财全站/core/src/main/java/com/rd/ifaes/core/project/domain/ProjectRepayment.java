package com.rd.ifaes.core.project.domain;

import java.math.BigDecimal;
import java.util.Date;

import com.rd.ifaes.common.annotation.DictType;
import com.rd.ifaes.common.dict.DictTypeEnum;
import com.rd.ifaes.common.dict.LoanEnum;
import com.rd.ifaes.common.dict.RepaymentEnum;
import com.rd.ifaes.common.entity.BaseEntity;
import com.rd.ifaes.common.util.BigDecimalUtils;
import com.rd.ifaes.common.util.DateUtils;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.core.core.constant.Constant;
import com.rd.ifaes.core.core.util.DictUtils;

/**
 * entity:ProjectRepayment
 * 
 * @author FangJun
 * @version 3.0
 * @date 2016-7-14
 */
public class ProjectRepayment extends BaseEntity<ProjectRepayment> {
	
	private static final long serialVersionUID = 1L;
	
	private String	userId;		 /* 用户ID */ 
	private String	projectId;		 /* 项目ID */ 
	private String	investId;		 /* 投标ID，普通定期投资不适用， 计息理财等使用 */ 
	private Date	repayTime;		 /* 预计还款时间 */ 
	private Date	realRepayTime;		 /* 实际还款时间 */ 
	private BigDecimal	payment;		 /* 预还金额 */ 
	private BigDecimal	payedAmount;		 /* 已还金额 */ 
	private BigDecimal	capital;		 /* 本金 */ 
	private BigDecimal	interest;		 /* 利息 */
	@DictType(type="repayStatus")
	private String	status;		 /* 还款状态  0未还；1已还  */ 
	private String	repayType;		 /* 还款类型（1正常还款 2 垫付  3 已还垫付 4 逾期还款） */ 
	private Integer	period;		 /* 期数 */ 
	private Integer	lateDays;		 /* 逾期天数 */ 
	private BigDecimal	lateInterest;		 /* 逾期利息 */ 
	private BigDecimal merchantLateInterest;/*给平台的逾期罚息*/ 
	private String	repayUserId;		 /* 实际还款者 */ 
	private String	repayTradeNo;		 /* 第三方还款流水号 */ 
	private Date	createTime;		 /* 添加时间 */ 
	private Integer periods;// 总期数
	private BigDecimal borrowManageFee;/* 借款人手续费 */
	
	//其他自定义属性
	private String projectName; // 项目名称
	private String projectNo; // 项目编号
	private int timeLimit;      //借款期限(月标)
	private int isRepayentStatus;  //是否是还款状态
	private String projectTypeName;//项目分类名称
	
	private String userName; // 用户名
	private String realName;// 投资人姓名
	private String mobile ; //手机号
	
	private String statusType;     //状态类型      00:全部   01：待还   02：逾期待还  03：担保垫付
	private String repayTypeAdvance;  //担保垫付
	
	private String isVouch; /* 是否担保( 1 担保，0 不担保） */
	private String vouchId; /* 担保公司ID */
	private String vouchStatus;/*担保用户审核状态*/
	
	private String isOverdue; /* 是否逾期( 1 逾期，0 未逾期） */
	private String urgeRepayCount ;/* 催收次数 */
	
	private String projectStatus; /* 项目状态 */
	 
	private BigDecimal overdueFeeRate;/*逾期罚息利率*/
	private String borrowFlag; /* 借款标标识(1是，借款标， 0否，即是理财产品） */
	
	@Override
	public String getDateTypeTime(){
		Date dateTypeTime=null; //默认全部
		String dateTypeTemp = getDateType();
		String status = getStatus();
		
		String result = "";
		if(StringUtils.isNotBlank(dateTypeTemp)){
			Date now = DateUtils.getNow();
			if(Constant.FLAG_NO.equals(status)){ //未还
				if(Constant.DATE_TYPE_WEEK.equals(dateTypeTemp)){  //近7天
					dateTypeTime = DateUtils.rollDay(now, 7);
				} else if(Constant.DATE_TYPE_MONTH.equals(dateTypeTemp)){   //近1月
					dateTypeTime = DateUtils.rollMon(now, 1);
				} else if(Constant.DATE_TYPE_QUARTER.equals(dateTypeTemp)){ //近3月
					dateTypeTime = DateUtils.rollMon(now, 3);
				} else if(Constant.DATE_TYPE_YEAR.equals(dateTypeTemp)){    //近1年
					dateTypeTime = DateUtils.rollYear(now, 1);
				}
				if(dateTypeTime != null){
					result = DateUtils.getDateEnd(dateTypeTime);
				}
				
			}else{ //已还
				if(Constant.DATE_TYPE_WEEK.equals(dateTypeTemp)){  //近7天
					dateTypeTime = DateUtils.rollDay(now, -7);
				} else if(Constant.DATE_TYPE_MONTH.equals(dateTypeTemp)){   //近1月
					dateTypeTime = DateUtils.rollMon(now, -1);
				} else if(Constant.DATE_TYPE_QUARTER.equals(dateTypeTemp)){ //近3月
					dateTypeTime = DateUtils.rollMon(now, -3);
				} else if(Constant.DATE_TYPE_YEAR.equals(dateTypeTemp)){    //近1年
					dateTypeTime = DateUtils.rollYear(now, -1);
				}
				if(dateTypeTime != null){
					result = DateUtils.getDateStart(dateTypeTime);					
				}
			}
		}
		return result;
	}
	
	// Constructor
	public ProjectRepayment() {
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

	public String getIsOverdue() {
		return isOverdue;
	}

	public void setIsOverdue(String isOverdue) {
		this.isOverdue = isOverdue;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
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

	public Date getRealRepayTime() {
		return realRepayTime;
	}

	public void setRealRepayTime(Date realRepayTime) {
		this.realRepayTime = realRepayTime;
	}
	
	/**
	 * 
	 * 还款计划-实际还款日期
	 * @author xhf
	 * @date 2016年8月3日
	 * @return
	 */
	public String getRealRepayTimeStr() {
		String realRepayTimeStr="";
		if(Constant.FLAG_YES.equals(getStatus())){ //已还
			if(getRealRepayTime()!=null){
				realRepayTimeStr = DateUtils.dateStr2(getRealRepayTime());
			}
			
		}else{ //未还
			if(getLateDays()!=null && getLateDays()>0){//已逾期
				realRepayTimeStr="已逾期";
			}else{ 
				realRepayTimeStr="未到期";
			}
		}
		return realRepayTimeStr;
	}
	
	/**
	 * 
	 * 还款计划-实际还款金额
	 * @author xhf
	 * @date 2016年8月3日
	 * @return
	 */
	public String getPayedAmountStr() {
		String payedAmountStr = "";
		if(Constant.FLAG_YES.equals(getStatus())){ //已还
			if(getPayedAmount()!=null){
				payedAmountStr = String.valueOf(BigDecimalUtils.round(getPayedAmount(), 2));
			}
		}else{ //未还
			if(getLateDays()!=null && getLateDays()>0){//已逾期
				payedAmountStr="已逾期";
			}else{ 
				payedAmountStr="未到期";
			}
		}
		return payedAmountStr;
	}
	
	/**
	 * 
	 * 还款计划-类型
	 * @author xhf
	 * @date 2016年8月3日
	 * @return
	 */
	public String getRepaymentTypeStr(){
		String repaymentTypeStr = "利息";
		if(getCapital()!=null && getCapital().compareTo(BigDecimal.ZERO)>0){
			repaymentTypeStr = repaymentTypeStr+"+本金";
		}
		if((getLateInterest()!=null && getLateInterest().compareTo(BigDecimal.ZERO)>0)
				|| (getMerchantLateInterest()!=null && getMerchantLateInterest().compareTo(BigDecimal.ZERO)>0) ){
			repaymentTypeStr = repaymentTypeStr+"+罚息";
		}
		return repaymentTypeStr;
	}
	
	public String getStatusStr(){
		if(this.projectStatus!=null && LoanEnum.STATUS_BAD_DEBT.eq(this.projectStatus) 
				&& RepaymentEnum.STATUS_NOT_REPAY.eq(this.status)){
			return LoanEnum.STATUS_BAD_DEBT.getName();
		}else{
			return DictUtils.getItemName(DictTypeEnum.REPAY_STATUS.getValue(), getStatus());
		}
	}
	
	/**
	 * 
	 * 获取下拉状态类型
	 * @author xhf
	 * @date 2016年8月3日
	 * @return
	 */
	public String getStatusTypeStr() {
		String statusTypeStr="";
		if(LoanEnum.REPAY_TYPE_ADVANCE.getValue().equals(getRepayType())){ //垫付
			statusTypeStr = "担保垫付";
			
		} else if(RepaymentEnum.STATUS_NOT_REPAY.eq(getStatus()) || RepaymentEnum.STATUS_NOT_NEED_REPAY.eq(getStatus())){ //待还
			if(getLateDays()!=null && getLateDays()>0){
				statusTypeStr = "逾期待还";
			}else{
				statusTypeStr = "待还";
			}
		}
		return statusTypeStr;
	}
   /**
    * 还款计划-字段-是否逾期
    * @author  FangJun
    * @date 2016年8月30日
    * @return 是否逾期  文字说明
    */
	public String getLateStr(){
		String lateStr="";
		if(lateDays!=null && lateDays >0 ){
			lateStr="是";
		}else if(Constant.FLAG_NO.equals(getStatus())&&
				this.repayTime!=null && this.repayTime.after(DateUtils.getNow())){
	        lateStr="未到期";
		}else{
	    	lateStr="否";
		}
		
		return lateStr;
	}
	
	public BigDecimal getPayment() {
		return payment;
	}

	public void setPayment(BigDecimal payment) {
		this.payment = payment;
	}

	public BigDecimal getPayedAmount() {
		return payedAmount;
	}

	public void setPayedAmount(BigDecimal payedAmount) {
		this.payedAmount = payedAmount;
	}

	public BigDecimal getCapital() {
		return capital;
	}

	public void setCapital(BigDecimal capital) {
		this.capital = capital;
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

	public String getVouchStatus() {
		return vouchStatus;
	}

	public void setVouchStatus(String vouchStatus) {
		this.vouchStatus = vouchStatus;
	}

	public BigDecimal getInterest() {
		return interest;
	}

	public void setInterest(BigDecimal interest) {
		this.interest = interest;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getRepayType() {
		return repayType;
	}
	
	public String getRepayTypeStr() {
		if(RepaymentEnum.REPAY_TYPE_GUARANTEE.eq(getRepayType())){
			return RepaymentEnum.STATUS_REPAY_NO.getName();
		}else if(RepaymentEnum.REPAY_TYPE_REPAYED_GUARANTEE.eq(getRepayType())){
			return RepaymentEnum.STATUS_REPAY_YES.getName();
		}
		return "";
	}

	public void setRepayType(String repayType) {
		this.repayType = repayType;
	}

	public Integer getPeriod() {
		return period;
	}
	
	/**
	 * 当期/总期数
	 * @return
	 */
	public String getPeriodStr() {
		return period+"/"+periods;
	}

	public void setPeriod(Integer period) {
		this.period = period;
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

	public String getRepayUserId() {
		return repayUserId;
	}

	public void setRepayUserId(String repayUserId) {
		this.repayUserId = repayUserId;
	}

	public String getRepayTradeNo() {
		return repayTradeNo;
	}

	public void setRepayTradeNo(String repayTradeNo) {
		this.repayTradeNo = repayTradeNo;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public int getTimeLimit() {
		return timeLimit;
	}

	public void setTimeLimit(int timeLimit) {
		this.timeLimit = timeLimit;
	}

	public int getIsRepayentStatus() {
		return isRepayentStatus;
	}

	public void setIsRepayentStatus(int isRepayentStatus) {
		this.isRepayentStatus = isRepayentStatus;
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
	
	public Integer getPeriods() {
		return periods;
	}

	
	/**
	 * periodsStr
	 * @return
	 */
	public String getPeriodsStr() {
		return period + "/" + periods;
	}

	public void setPeriods(Integer periods) {
		this.periods = periods;
	}

	public String getStatusType() {
		return statusType;
	}

	public void setStatusType(String statusType) {
		this.statusType = statusType;
	}

	public String getRepayTypeAdvance() {
		return repayTypeAdvance;
	}

	public void setRepayTypeAdvance(String repayTypeAdvance) {
		this.repayTypeAdvance = repayTypeAdvance;
	}

	public String getProjectStatus() {
		return projectStatus;
	}

	public void setProjectStatus(String projectStatus) {
		this.projectStatus = projectStatus;
	}

	public String getUrgeRepayCount() {
		return urgeRepayCount;
	}

	public void setUrgeRepayCount(String urgeRepayCount) {
		this.urgeRepayCount = urgeRepayCount;
	}

	public String getProjectNo() {
		return projectNo;
	}

	public void setProjectNo(String projectNo) {
		this.projectNo = projectNo;
	}
	
	public BigDecimal getLateInterestSum(){
		BigDecimal lateInterestSum = BigDecimal.ZERO;
		if(this.lateInterest!=null){
			lateInterestSum = lateInterestSum.add(this.lateInterest);
		}
		if(this.merchantLateInterest!=null){
			lateInterestSum = lateInterestSum.add(this.merchantLateInterest);
		}
		return lateInterestSum;
	}
	
	/**
	 * 获取属性enRepaymentMoney的值
	 * @return enRepaymentMoney属性值
	 */
	public BigDecimal getEnRepaymentMoney() {
		return this.payment;
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

	public String getProjectTypeName() {
		return projectTypeName;
	}

	public void setProjectTypeName(String projectTypeName) {
		this.projectTypeName = projectTypeName;
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
	

	public BigDecimal getBorrowManageFee() {
		return borrowManageFee;
	}

	public void setBorrowManageFee(BigDecimal borrowManageFee) {
		this.borrowManageFee = borrowManageFee;
	}

	@Override
	public String toString() {
		return "ProjectRepayment [userId=" + userId + ", projectId=" + projectId + ", investId=" + investId + ", repayTime="
				+ repayTime + ", realRepayTime=" + realRepayTime + ", payment=" + payment + ", payedAmount=" + payedAmount
				+ ", capital=" + capital + ", interest=" + interest + ", status=" + status + ", repayType=" + repayType
				+ ", period=" + period + ", lateDays=" + lateDays + ", lateInterest=" + lateInterest + ", merchantLateInterest="
				+ merchantLateInterest + ", repayUserId=" + repayUserId + ", repayTradeNo=" + repayTradeNo + ", createTime="
				+ createTime + ", periods=" + periods + ", borrowManageFee=" + borrowManageFee + "]";
	}

}
