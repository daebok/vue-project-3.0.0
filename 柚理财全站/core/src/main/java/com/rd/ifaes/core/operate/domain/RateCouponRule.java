package com.rd.ifaes.core.operate.domain;
import java.util.Date;
import java.util.List;
/**
 * 加息券规则设置pojo
 * @version 3.0
 * @author wyw
 * @date 2016-7-28
 */
public class RateCouponRule extends BaseRule<RateCouponRule> {
	
	/** 序列号 */
	private static final long serialVersionUID = 4582119555907800973L;
	/** 加息券发放方式：1 固定金额、2金额满返 */
	private String	grantType;	
	
	//其他自定义属性
	/** 规则明细 */
	private List<RateCouponRuleDetail> detailList;
	/** 活动名称 */
	private String activityName;
	
    /** empty  Constructor*/
	public RateCouponRule() {
		super();
	}
	/** full Constructor*/
	public RateCouponRule(final String uuid, final String activityCode, final String ruleName,
			final String status, final Integer totalNum, final Integer lssueNum, 
			final Date createTime,final  Date updateTime, final String deleteFlag, 
			final String grantType, final String grantUrl, final Date grantStartTime, 
			final Date grantEndTime, final String useProjectType, final String useValidDay,
			final Date useExpireTime) {
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
		this.useProjectType = useProjectType;
		this.useValidDay = useValidDay;
		this.useExpireTime = useExpireTime;
	}
	/** 获取 发放方式 */
	public String getGrantType() {
		return grantType;
	}
	/** 设置 发放方式 */
	public void setGrantType(final String grantType) {
		this.grantType = grantType;
	}
	/** 获取 规则明细 */
	public List<RateCouponRuleDetail> getDetailList() {
		return detailList;
	}
	/** 设置 规则明细 */
	public void setDetailList(final List<RateCouponRuleDetail> detailList) {
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
		return "RatecouponRule [" + "uuid=" + uuid + ", activityCode=" + activityCode + ", ruleName=" + ruleName + ", status=" + status + ", totalNum=" + totalNum + ", lssueNum=" + lssueNum + ", createTime=" + createTime + ", updateTime=" + updateTime + ", deleteFlag=" + deleteFlag + ", grantType=" + grantType + ", grantUrl=" + grantUrl + ", grantStartTime=" + grantStartTime + ", grantEndTime=" + grantEndTime + ", useProjectType=" + useProjectType + ", useValidDay=" + useValidDay + ", useExpireTime=" + useExpireTime +  "]";
	}
}
