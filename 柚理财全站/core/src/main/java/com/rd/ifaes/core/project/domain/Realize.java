package com.rd.ifaes.core.project.domain;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.beans.BeanUtils;

import com.rd.ifaes.common.dict.LoanEnum;
import com.rd.ifaes.common.exception.BussinessException;
import com.rd.ifaes.common.security.shiro.IdGen;
import com.rd.ifaes.common.util.DateUtils;
import com.rd.ifaes.common.util.NumberUtils;
import com.rd.ifaes.common.util.SpringContextHolder;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.core.core.constant.ResourceConstant;
import com.rd.ifaes.core.core.util.ResourceUtils;
import com.rd.ifaes.core.user.model.UserFreezeModel;
import com.rd.ifaes.core.user.service.UserFreezeService;

/**
 * 变现实体
 * @version 3.0
 * @author fxl
 * @date 2016年8月1日
 */
public class Realize extends ProjectEntity<Realize> {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * 项目ID
	 */
	private String	projectId;		 /* 项目ID */ 
	/**
	 * 投标ID
	 */
	private String	investId;		 /* 投标ID */ 
	/**
	 * 被变现项目ID
	 */
	private String	oldProjectId;		 /* 变现项目ID */ 
	/**
	 * 原始项目ID
	 */
	private String	originalProjectId;		 /* 原始项目ID(由于多次变现的存在,因此这里存入最原始项目ID) */ 
	/**
	 * 变现期限
	 */
	private Integer	deadline;		 /* 变现期数(发布变现时原标的还款期数) */ 
	/**
	 * 变现规则ID
	 */
	private String	ruleId;		 /* 变现规则ID */ 
	/**
	 * 原产品本息
	 */
	private BigDecimal	realizeAmount;
	/**
	 * 原产品利息
	 */
	private BigDecimal	realizeInterest;
	
	//其他自定义属性
	/**
	 * 状态集合
	 */
	private String[] statusSet;//状态集合
	/**
	 * 变现金额查询条件
	 */
	private String realizeAmountCondition;
	/**
	 * 变现天数查询条件
	 */
	private String realizeDayCondition;
	/**
	 * 变现利率查询条件
	 */
	private String realizeAprCondition;
	/**
	 *  剩余可投金额
     */
	private Double remainAccount;
	// Constructor
	public Realize() {
	}
	
	/**
	 * full Constructor
	 */
	public Realize(String projectId, String investId, String oldProjectId, String originalProjectId, Integer deadline, String ruleId, BigDecimal realizeAmount, BigDecimal realizeInterest) {
		this.projectId = projectId;
		this.investId = investId;
		this.oldProjectId = oldProjectId;
		this.originalProjectId = originalProjectId;
		this.deadline = deadline;
		this.ruleId = ruleId;
		this.realizeAmount = realizeAmount;
		this.realizeInterest = realizeInterest;
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
	public void setInvestId(String investId) {
		this.investId = investId;
	}

	/**
	 * 获取属性oldProjectId的值
	 * @return oldProjectId属性值
	 */
	public String getOldProjectId() {
		return oldProjectId;
	}

	/**
	 * 设置属性oldProjectId的值
	 * @param  oldProjectId属性值
	 */
	public void setOldProjectId(String oldProjectId) {
		this.oldProjectId = oldProjectId;
	}

	/**
	 * 获取属性originalProjectId的值
	 * @return originalProjectId属性值
	 */
	public String getOriginalProjectId() {
		return originalProjectId;
	}

	/**
	 * 设置属性originalProjectId的值
	 * @param  originalProjectId属性值
	 */
	public void setOriginalProjectId(String originalProjectId) {
		this.originalProjectId = originalProjectId;
	}

	/**
	 * 获取属性deadline的值
	 * @return deadline属性值
	 */
	public Integer getDeadline() {
		return deadline;
	}

	/**
	 * 设置属性deadline的值
	 * @param  deadline属性值
	 */
	public void setDeadline(Integer deadline) {
		this.deadline = deadline;
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
	public void setRuleId(String ruleId) {
		this.ruleId = ruleId;
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
	 * 获取属性realizeAmountCondition的值
	 * @return realizeAmountCondition属性值
	 */
	public String getRealizeAmountCondition() {
		return realizeAmountCondition;
	}

	/**
	 * 设置属性realizeAmountCondition的值
	 * @param  realizeAmountCondition属性值
	 */
	public void setRealizeAmountCondition(String realizeAmountCondition) {
		this.realizeAmountCondition = realizeAmountCondition;
	}

	/**
	 * 获取属性realizeDayCondition的值
	 * @return realizeDayCondition属性值
	 */
	public String getRealizeDayCondition() {
		return realizeDayCondition;
	}

	/**
	 * 设置属性realizeDayCondition的值
	 * @param  realizeDayCondition属性值
	 */
	public void setRealizeDayCondition(String realizeDayCondition) {
		this.realizeDayCondition = realizeDayCondition;
	}

	/**
	 * 获取属性realizeAprCondition的值
	 * @return realizeAprCondition属性值
	 */
	public String getRealizeAprCondition() {
		return realizeAprCondition;
	}

	/**
	 * 设置属性realizeAprCondition的值
	 * @param  realizeAprCondition属性值
	 */
	public void setRealizeAprCondition(String realizeAprCondition) {
		this.realizeAprCondition = realizeAprCondition;
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

	@Override
	public void preInsert(){
		this.uuid =StringUtils.isBlank(this.uuid)?IdGen.uuid():this.uuid;
		Date now = DateUtils.getNow();
		this.createTime = now;
		this.showTime = now;
		this.saleTime = now;
	}
	
	/**
	 * 校验基本属性
	 * @author fxl
	 * @date 2016年8月01日
	 * @param rule
	 */
	public void validBase(RealizeRule rule) {
		// 年化利率区间校验
		double minApr = rule.getRealizeRateMin().doubleValue();
		double maxApr = rule.getRealizeRateMax().doubleValue();
		if (minApr > 0 && maxApr > 0
				&& (minApr > this.apr.doubleValue() || maxApr < this.apr.doubleValue())) {
			throw new BussinessException(ResourceUtils.get(ResourceConstant.PROJECT_APR_RANGE_ERROR, minApr, maxApr));
		}
		// 最低起投金额校验
		if (this.lowestAccount.doubleValue() < 1) {
			throw new BussinessException(ResourceUtils.get(ResourceConstant.PROJECT_LOWEST_ACCOUNT_LT_ONE_ERROR,1));
		}
		if (this.lowestAccount.doubleValue() > this.account.doubleValue()) {
			throw new BussinessException(ResourceUtils.get(ResourceConstant.PROJECT_LOWEST_ACCOUNT_GT_ACCOUNT_ERROR));
		}
		if (!NumberUtils.isDefault(this.mostAccount) && this.lowestAccount.doubleValue() > this.mostAccount.doubleValue()) {
			String key = (LoanEnum.BORROW_FLAG_BORROW.eq(this.borrowFlag)) ? ResourceConstant.BORROW_ACCOUNT_RANGE_ERROR
					: ResourceConstant.PRODUCT_ACCOUNT_RANGE_ERROR;
			throw new BussinessException(ResourceUtils.get(key, mostAccount, lowestAccount));
		}

		// 最高投资总额校验
		if (!NumberUtils.isDefault(this.mostAccount) && this.mostAccount.compareTo(this.account) > 0) {
			throw new BussinessException(ResourceUtils.get(ResourceConstant.PROJECT_MOST_ACCOUNT_GT_ACCOUNT_ERROR));
		}
		// 判断是否冻结该功能
		final UserFreezeService userFreezeService = (UserFreezeService)SpringContextHolder.getBean("userFreezeService");
		final boolean isFreezed = userFreezeService.isFreezed(userId, UserFreezeModel.STATUS_USER_FREEZE_REALIZE);
		if(isFreezed){
			throw new BussinessException(ResourceUtils.get(ResourceConstant.REALIZE_IS_FREEZE), BussinessException.TYPE_CLOSE);
		}
	}
	
	public Project prototype() {
		Project project = new Project();
		BeanUtils.copyProperties(this, project);
		return project;
	}
	
	public static Realize instance(Project project) {
		Realize realize = new Realize();
		BeanUtils.copyProperties(project, realize);
		return realize;
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

	@Override
	public String toString() {
		return "Realize [" + "projectId=" + projectId + ", investId=" + investId + ", oldProjectId=" + oldProjectId
				+ ", originalProjectId=" + originalProjectId + ", deadline=" + deadline + ", ruleId=" + ruleId
				+ ", realizeAmount=" + realizeAmount + ", realizeInterest=" + realizeInterest + "]";
	}
}
