package com.rd.ifaes.core.operate.domain;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 红包规则设置pojo
 * 
 * @version 3.0
 * @author wyw
 * @date 2016-7-28
 */
public class RedenvelopeRule extends BaseRule<RedenvelopeRule> {
	/** 序列号 */
	private static final long serialVersionUID = 5267151048382454074L;
	/** 红包发放方式：1 固定金额、2固定比例、3金额满返、4比例满返 */
	private String grantType;
	/** 发放最大金额（发放类型为固定比例、比例满返的时候，设置发放红包最大值） */
	private BigDecimal grantMax;
	/** 发放最小金额（发放类型为固定比例、比例满返的时候，设置发放红包最小值） */
	private BigDecimal grantMin;
	/** 固定比例（固定比例，红包发放方式为固定比率时设定） */
	private BigDecimal grantRate;
	/**红包适用投资期限*/
	private int redInvestExpireTime;
	// 其他自定义属性
	/** 规则明细 */
	private List<RedenvelopeRuleDetail> detailList;
	/** 活动名称 */
	private String activityName;
	
	/** Constructor */
	public RedenvelopeRule() {
		super();
	}

	/** full Constructor */
	public RedenvelopeRule(final String uuid, final String activityCode, final String ruleName, final String status,
			final Integer totalNum, final Integer lssueNum, final Date createTime, final Date updateTime,
			final String deleteFlag, final String grantType, final String grantUrl, final Date grantStartTime,
			final Date grantEndTime, final BigDecimal grantMax, final BigDecimal grantMin, final BigDecimal grantRate,
			final String useProjectType, final String useValidDay, final Date useExpireTime) {
		super();
		setUuid(uuid);
		this.activityCode = activityCode;
		this.ruleName = ruleName;
		this.status = status;
		this.totalNum = totalNum;
		this.lssueNum = lssueNum;
		this.createTime = createTime;
		this.updateTime = updateTime;
		this.deleteFlag = deleteFlag;
		this.grantType = grantType;
		this.grantUrl = grantUrl;
		this.grantStartTime = grantStartTime;
		this.grantEndTime = grantEndTime;
		this.grantMax = grantMax;
		this.grantMin = grantMin;
		this.grantRate = grantRate;
		this.useProjectType = useProjectType;
		this.useValidDay = useValidDay;
		this.useExpireTime = useExpireTime;
	}
	/** 获取 发放类型 */
	public String getGrantType() {
		return grantType;
	}
	/** 设置 发放类型 */
	public void setGrantType(final String grantType) {
		this.grantType = grantType;
	}
	/** 获取 发放最大值 */
	public BigDecimal getGrantMax() {
		return grantMax;
	}
	/** 设置 发放最大值 */
	public void setGrantMax(final BigDecimal grantMax) {
		this.grantMax = grantMax;
	}
	/** 获取 发放最小值 */
	public BigDecimal getGrantMin() {
		return grantMin;
	}
	/** 设置 发放最小值 */
	public void setGrantMin(final BigDecimal grantMin) {
		this.grantMin = grantMin;
	}
	/** 获取 发放比例 */
	public BigDecimal getGrantRate() {
		return grantRate;
	}
	/** 设置 发放比例 */
	public void setGrantRate(final BigDecimal grantRate) {
		this.grantRate = grantRate;
	}
	/** 获取 规则明细 */
	public List<RedenvelopeRuleDetail> getDetailList() {
		return detailList;
	}
	/** 设置 规则明细 */
	public void setDetailList(final List<RedenvelopeRuleDetail> detailList) {
		this.detailList = detailList;
	}
	/** 获取 活动名称 */
	public String getActivityName() {
		return activityName;
	}
	/** 设置 活动名称 */
	public void setActivityName(final String activityName) {
		this.activityName = activityName;
	}

	/** toString */
	@Override
	public String toString() {
		return "RedenvelopeRule [" + "uuid=" + uuid + ", activityCode=" + activityCode + ", ruleName=" + ruleName + ", status="
				+ status + ", totalNum=" + totalNum + ", lssueNum=" + lssueNum + ", createTime=" + createTime + ", updateTime="
				+ updateTime + ", deleteFlag=" + deleteFlag + ", grantType=" + grantType + ", grantUrl=" + grantUrl
				+ ", grantStartTime=" + grantStartTime + ", grantEndTime=" + grantEndTime + ", grantMax=" + grantMax
				+ ", grantMin=" + grantMin + ", grantRate=" + grantRate + ", useProjectType=" + useProjectType + ", useValidDay="
				+ useValidDay + ", useExpireTime=" + useExpireTime + "]";
	}

	/**
	 * 获得红包投资期限
	 * @return
	 */
	public int getRedInvestExpireTime() {
		return redInvestExpireTime;
	}
	/**
	 * 设置红包投资期限
	 * @param redInvestExpireTime
	 */
	public void setRedInvestExpireTime(int redInvestExpireTime) {
		this.redInvestExpireTime = redInvestExpireTime;
	}
}
