package com.rd.ifaes.core.operate.domain;

import java.math.BigDecimal;
import java.util.Date;

import com.rd.ifaes.common.entity.BaseEntity;
import com.rd.ifaes.common.util.DateUtils;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.core.core.constant.Constant;
import com.rd.ifaes.core.project.domain.Project;

/**
 * 优惠券基类 加息券 红包
 * @version 3.0
 * @author wyw
 * @date 2016-7-29
 */
public class BaseUserVoucher<T> extends BaseEntity<T> {
	/** 序列号 */
	protected static final long serialVersionUID = 2255765157016260199L;
	/** 用户id值 */
	protected String userId;
	/** 规则id */
	protected String ruleId;
	/** 规则名称 */
	protected String ruleName;
	/** 活动名称 */
	protected String activityName;
	/** 使用时间 */
	protected Date useTime;
	/** 发放时间 */
	protected Date createTime;
	/** 投标id值 */
	protected String tenderId;
	/** 状态：0未使用,1已使用，2已过期 3已作废 */
	protected String status;
	/** 有效天数 */
	protected String useValidDay;
	/** 使用到期时间 */
	protected Date useExpireTime;
	/** 最低使用投资额度 */
	protected BigDecimal useTenderMoney;
	/** 使用产品分类 */
	protected String useProjectType;
	/** 使用产品分类名称 */
	protected String useProjectTypeName;
	//数据库查询条件
	/**此次投资金额*/
	private BigDecimal tenderMoney;
	/**投资项目的类型id*/
	private String projectTypeId;
	/**使用时间查询类型  null:条件查询*/
	private String dateUseType;
	/**使用开始时间*/
	private String useStartTime;
	/**使用的截止时间*/
	private String useEndTime;
	
	/**
	 * 优惠券是否可用(校验投资最低金额以及产品类型)
	 * @author ywt
	 * @date 2016-10-14
	 * @param project
	 * @param tenderMoney
	 * @return
	 */
	public boolean isAvailable(final Project project, final BigDecimal tenderMoney) {
		boolean result = true;
		//校验使用状态
		if (!Constant.STATUS_NO_USE.equals(this.status)) {
			result = false;
		}else if (this.useExpireTime!=null && DateUtils.getNow().after(this.useExpireTime)) {
			//校验过期时间
			result = false;
		}else if (this.useTenderMoney!=null && tenderMoney.doubleValue()< this.useTenderMoney.doubleValue()) {
			//校验最低投资可用金额
			result = false;
		} else if (this.useProjectType!=null && !this.useProjectType.contains(project.getProjectTypeId())) {
			//检验项目分类是否满足
			result = false;
		}
		return result;
	}
	/** 获取 用户id*/
	public String getUserId() {
		return userId;
	}
	/** 设置 用户id*/
	public void setUserId(final String userId) {
		this.userId = userId;
	}
	/** 获取规则名称*/
	public String getRuleName() {
		return ruleName;
	}
	/** 设置 规则名称*/
	public void setRuleName(final String ruleName) {
		this.ruleName = ruleName;
	}
	/** 获取 活动名称*/
	public String getActivityName() {
		return activityName;
	}
	/** 设置 活动名称*/
	public void setActivityName(final String activityName) {
		this.activityName = activityName;
	}
	/** 获取 使用时间*/
	public Date getUseTime() {
		return useTime;
	}
	/** 设置 使用时间*/
	public void setUseTime(final Date useTime) {
		this.useTime = useTime;
	}
	/** 获取 发放时间*/
	public Date getCreateTime() {
		return createTime;
	}
	/** 设置 发放时间*/
	public void setCreateTime(final Date createTime) {
		this.createTime = createTime;
	}
	/** 获取 投资记录id*/
	public String getTenderId() {
		return tenderId;
	}
	/** 设置 投资记录id*/
	public void setTenderId(final String tenderId) {
		this.tenderId = tenderId;
	}
	/** 获取 状态*/
	public String getStatus() {
		return status;
	}
	/** 设置 状态*/
	public void setStatus(final String status) {
		this.status = status;
	}
	/** 获取 有效期天数*/
	public String getUseValidDay() {
		return useValidDay;
	}
	/** 设置 有效期天数*/
	public void setUseValidDay(final String useValidDay) {
		this.useValidDay = useValidDay;
	}
	/** 获取 过期时间*/
	public Date getUseExpireTime() {
		return useExpireTime;
	}
	/** 设置 过期时间*/
	public void setUseExpireTime(final Date useExpireTime) {
		this.useExpireTime = useExpireTime;
	}
	/** 获取 最低投资金额*/
	public BigDecimal getUseTenderMoney() {
		return useTenderMoney;
	}
	/** 设置 最低投资金额*/
	public void setUseTenderMoney(final BigDecimal useTenderMoney) {
		this.useTenderMoney = useTenderMoney;
	}
	/** 获取 项目分类*/
	public String getUseProjectType() {
		return useProjectType;
	}
	/** 设置 项目分类*/
	public void setUseProjectType(String useProjectType) {
		this.useProjectType = useProjectType;
	}
	public BigDecimal getTenderMoney() {
		return tenderMoney;
	}
	public void setTenderMoney(BigDecimal tenderMoney) {
		this.tenderMoney = tenderMoney;
	}
	public String getProjectTypeId() {
		return projectTypeId;
	}
	public void setProjectTypeId(String projectTypeId) {
		this.projectTypeId = projectTypeId;
	}
	public String getRuleId() {
		return ruleId;
	}
	public void setRuleId(String ruleId) {
		this.ruleId = ruleId;
	}
	/**
	 * 获取属性dateUseType的值
	 * @return dateUseType属性值
	 */
	public String getDateUseType() {
		return dateUseType;
	}

	/**
	 * 设置属性dateUseType的值
	 * @param  dateUseType属性值
	 */
	public void setDateUseType(String dateUseType) {
		this.dateUseType = dateUseType;
	}

	/**
	 * 获取属性useStartTime的值
	 * @return useStartTime属性值
	 */
	public String getUseStartTime() {
		return useStartTime;
	}

	/**
	 * 设置属性useStartTime的值
	 * @param  useStartTime属性值
	 */
	public void setUseStartTime(String useStartTime) {
		this.useStartTime = useStartTime;
	}

	/**
	 * 获取属性useEndTime的值
	 * @return useEndTime属性值
	 */
	public String getUseEndTime() {
		String temp = useEndTime;
		if(StringUtils.isNotBlank(temp)){
			temp = DateUtils.getDateEnd(DateUtils.parseDate(temp));
		}
		return temp;
	}

	/**
	 * 设置属性useEndTime的值
	 * @param  useEndTime属性值
	 */
	public void setUseEndTime(String useEndTime) {
		this.useEndTime = useEndTime;
	}
	/**
	 * 获取属性useProjectTypeName的值
	 * @return useProjectTypeName属性值
	 */
	public String getUseProjectTypeName() {
		return useProjectTypeName;
	}
	/**
	 * 设置属性useProjectTypeName的值
	 * @param  useProjectTypeName属性值
	 */
	public void setUseProjectTypeName(String useProjectTypeName) {
		this.useProjectTypeName = useProjectTypeName;
	}
	
}
