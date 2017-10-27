package com.rd.ifaes.core.project.domain;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.rd.ifaes.common.dict.CommonEnum;
import com.rd.ifaes.common.dict.DeleteFlagEnum;
import com.rd.ifaes.common.dict.LoanEnum;
import com.rd.ifaes.common.util.BigDecimalUtils;
import com.rd.ifaes.common.util.DateUtils;
import com.rd.ifaes.common.util.NumberUtils;
import com.rd.ifaes.core.core.constant.Constant;

/**
 * entity:Project
 * 
 * @author FangJun
 * @version 3.0
 * @date 2016-7-13
 */
public class Project extends ProjectEntity<Project> {
	
	private static final long serialVersionUID = 1L;
	
	//其他自定义属性
	private String realName;/*借款人真实姓名*/
	private String mobile;  /*借款人手机号*/
	
	//查询使用
	private String[] statusSet;//项目的状态
 
	private String statusStr; /*我的借款-列表使用 ： 项目状态*/
	
	private  String  projectTypeIdStr; /* 多个子类别UUID拼接字符串 */
		
	private String projectTypeName;//项目分类名称
	
	private String bondUseful; /* 是否可以债权转让 1 可以，0不可以 */
    private String realizeUseful; /* 是否可变现: 1 可变现 0 不可变现，默认 0 */
    
    private String  vipLevel; //VIP等级限制
    
    private BigDecimal costManage ;
	
	// Constructor
	public Project() {
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getStatusStr() {
		return statusStr;
	}

	public void setStatusStr(String statusStr) {
		this.statusStr = statusStr;
	}

	public Boolean getHasRepaymentProtocol() {
		Boolean hasRepaymentProtocol=false;
		if(LoanEnum.STATUS_REPAY_START.getValue().equals(getStatus())||LoanEnum.STATUS_REPAY_OVERDUE.getValue().equals(getStatus())||
				LoanEnum.STATUS_BAD_DEBT.getValue().equals(getStatus())|| LoanEnum.STATUS_REPAYED_SUCCESS.getValue().equals(getStatus())||
				LoanEnum.STATUS_REPAYED_LATE.getValue().equals(getStatus())){
			hasRepaymentProtocol = true;
		}
		return hasRepaymentProtocol;
	}

	@Override
	public void preInsert() {
		super.preInsert();
		this.deleteFlag = DeleteFlagEnum.NO.getValue();
		this.createTime = DateUtils.getNow();
		this.raiseAccount = NumberUtils.isDefault(this.raiseAccount) ? this.account : this.raiseAccount;
		this.accountYes = BigDecimal.ZERO;
		this.addApr = BigDecimal.ZERO;
		//转换计算起投金额、最高投资金额
		if (LoanEnum.SALE_STYLE_COPY.eq(this.saleStyle)) {
			if (this.getLowestCopies() == null || this.getLowestCopies() < Constant.INT_ONE) {
				this.lowestCopies = Constant.INT_ONE;
			}
			if (this.getMostCopies() == null || this.getMostCopies() < Constant.INT_ONE) {
				this.mostCopies = this.totalCopies;
			}
			this.lowestAccount = this.copyAccount.multiply(BigDecimal.valueOf(this.lowestCopies));
			this.mostAccount = this.copyAccount.multiply(BigDecimal.valueOf(this.mostCopies));
		}
		//起投设置
		if (!BigDecimalUtils.validAmount(this.lowestAccount)) {
			this.lowestAccount = BigDecimal.ONE;
		}
		//最高可投设置
		if (!BigDecimalUtils.validAmount(this.mostAccount)) {
			this.mostAccount = this.account;
		}
		//借款期限按月计算，添加总期数(初步预设置，不准确，上架设置固定还款日，可能是月份数+1;在成立审核时，再次校对)
		if(LoanEnum.TIME_TYPE_MONTH.eq(this.timeType) && ! LoanEnum.STYLE_ONETIME_REPAYMENT.eq(this.repayStyle)){ 
			 this.totalPeriod = this.timeLimit;
		}else{
			 this.totalPeriod = Constant.INT_ONE;
		}
		if(StringUtils.isBlank(this.getRiskLevel())){
			this.riskLevel = CommonEnum.YES.getValue();
		}
		this.setRepayYesAccount(BigDecimal.ZERO);
		this.setRepayYesInterest(BigDecimal.ZERO);
		this.setInterestStartDays(this.getInterestStartDays()==null?Constant.INT_ZERO:this.getInterestStartDays());
		if(StringUtils.isBlank(this.projectType)){
			projectType = LoanEnum.PROJECT_TYPE_PLEDGE.getValue();//默认质押
		}
	}
	
	@Override
	public void preUpdate() {
		super.preUpdate();
		this.raiseAccount = NumberUtils.isDefault(this.raiseAccount) ? this.account
				: this.raiseAccount;
		this.updateTime = DateUtils.getNow();
		if (!BigDecimalUtils.validAmount(this.mostAccount)) {
			this.mostAccount = this.account;
		}
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

	@JsonIgnore
	public String[] getStatusSet() {
		return statusSet;
	}

	public void setStatusSet(String[] statusSet) {
		this.statusSet = statusSet;
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

	/**
	 * 销售预计结束时间
	 * @author  FangJun
	 * @date 2016年8月31日
	 * @return
	 */
	public Date getSaleEndTime() {
		 if(this.getRaiseTimeLimit()==null ||  this.getRaiseTimeLimit() < 1){
			 return null;
		 }
		return DateUtils.rollDay(this.getSaleTime(), this.getRaiseTimeLimit());
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
	
	public String getVipLevel() {
		return vipLevel;
	}

	public void setVipLevel(String vipLevel) {
		this.vipLevel = vipLevel;
	}
	
	public BigDecimal getCostManage() {
		return costManage;
	}

	public void setCostManage(BigDecimal costManage) {
		this.costManage = costManage;
	}

	@Override
	public String toString() {
		return "Project [" + "uuid=" + uuid + ", userId=" + userId + ", projectNo=" + projectNo + ", projectName=" + projectName + ", raiseAccount=" + raiseAccount + ", account=" + account + ", accountYes=" + accountYes  + ", apr=" + apr + ", addApr=" + addApr + ", interestStyle=" + interestStyle + ", interestStartDays=" + interestStartDays + ", repayStyle=" + repayStyle + ", timeType=" + timeType + ", timeLimit=" + timeLimit + ", createTime=" + createTime + ", stopTime=" + stopTime + ", showTime=" + showTime + ", saleTime=" + saleTime + ", raiseTimeLimit=" + raiseTimeLimit + ", raiseEndTime=" + raiseEndTime + ", reviewTime=" + reviewTime + ", borrowManageRate=" + borrowManageRate + ", interestManageRate=" + interestManageRate  + ", novice=" + novice + ", choice=" + choice + ", saleStyle=" + saleStyle + ", lowestAccount=" + lowestAccount + ", mostAccount=" + mostAccount + ", totalCopies=" + totalCopies + ", copyAccount=" + copyAccount + ", lowestCopies=" + lowestCopies + ", mostCopies=" + mostCopies + ", riskLevel=" + riskLevel + ", redEnvelopeUseful=" + redEnvelopeUseful + ", additionalRateUseful=" + additionalRateUseful + ", productTypeId=" + projectTypeId + ", status=" + status + ", isVouch=" + isVouch + ", vouchId=" + vouchId + ", borrowFlag=" + borrowFlag + ", realizeFlag=" + realizeFlag +  "]";
	}
}
