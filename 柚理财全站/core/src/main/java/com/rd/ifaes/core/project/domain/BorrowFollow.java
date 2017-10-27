package com.rd.ifaes.core.project.domain;

import com.rd.ifaes.common.entity.BaseEntity;

/**
 * 
 * 贷后跟踪
 * @version 3.0
 * @author wyw
 * @date 2016-8-17
 */
public class BorrowFollow extends BaseEntity<BorrowFollow> {
	
	private static final long serialVersionUID = 1L;
	
	private String	projectId;		 /* 项目id */ 
	private String	fundsCondition;		 /* 资金使用情况 */ 
	private String	operateCondition;		 /* 借款人经营情况 */ 
	private String	financeCondition;		 /* 借款人财务情况 */ 
	private String	repaymentAbility;		 /* 借款还款能力 */ 
	private String[] picPath;//跟进上传图片

	//其他自定义属性
	

	// Constructor
	public BorrowFollow() {
	}

	/**
	 * full Constructor
	 */
	public BorrowFollow(String uuid, String projectId, String fundsCondition, String operateCondition, String financeCondition, String repaymentAbility) {
		setUuid(uuid);
		this.projectId = projectId;
		this.fundsCondition = fundsCondition;
		this.operateCondition = operateCondition;
		this.financeCondition = financeCondition;
		this.repaymentAbility = repaymentAbility;
	}

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	public String getFundsCondition() {
		return fundsCondition;
	}

	public void setFundsCondition(String fundsCondition) {
		this.fundsCondition = fundsCondition;
	}

	public String getOperateCondition() {
		return operateCondition;
	}

	public void setOperateCondition(String operateCondition) {
		this.operateCondition = operateCondition;
	}

	public String getFinanceCondition() {
		return financeCondition;
	}

	public void setFinanceCondition(String financeCondition) {
		this.financeCondition = financeCondition;
	}

	public String getRepaymentAbility() {
		return repaymentAbility;
	}

	public void setRepaymentAbility(String repaymentAbility) {
		this.repaymentAbility = repaymentAbility;
	}
	
	public String[] getPicPath() {
		return picPath;
	}

	public void setPicPath(String[] picPath) {
		this.picPath = picPath;
	}

	@Override
	public String toString() {
		return "BorrowFollow [" + "uuid=" + uuid + ", projectId=" + projectId + ", fundsCondition=" + fundsCondition + ", operateCondition=" + operateCondition + ", financeCondition=" + financeCondition + ", repaymentAbility=" + repaymentAbility +  "]";
	}
}
