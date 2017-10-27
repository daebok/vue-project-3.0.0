package com.rd.ifaes.core.operate.domain;

import java.util.Date;

import javax.validation.constraints.Size;

import com.rd.ifaes.common.dict.DeleteFlagEnum;
import com.rd.ifaes.common.dict.OperateEnum;
import com.rd.ifaes.common.entity.BaseEntity;
import com.rd.ifaes.common.exception.BussinessException;
import com.rd.ifaes.common.util.DateUtils;
import com.rd.ifaes.core.core.constant.ResourceConstant;
import com.rd.ifaes.core.core.util.ResourceUtils;
import com.rd.ifaes.core.operate.model.RedenvelopeRuleModel;

/**
 * 发放规则基类
 * 
 * @version 3.0
 * @author wyw
 * @date 2016-7-29
 */
public class BaseRule<T> extends BaseEntity<T> {
	/** 序列号 */
	private static final long serialVersionUID = 9219271763444401297L;
	/** 项目分类id值，多个项目分类以英文,分割 */
	protected String useProjectType;
	/** 有效天数 */
	protected String useValidDay;
	/** 到期时间 */
	protected Date useExpireTime;
	/** 活动方案唯一标识 */
	protected String activityCode;
	/** 规则名称 */
	@Size(min = 2, max = 15, message = "{" + ResourceConstant.RULE_NAME_ERROR + "}")
	protected String ruleName;
	/** 状态： 0禁用 1启用 */
	protected String status;
	/** 发放的总数 */
	protected Integer totalNum;
	/** 已经发放数量 */
	protected Integer lssueNum;
	/** 创建日期 */
	protected Date createTime;
	/** 最后修改日期 */
	protected Date updateTime;
	/** 逻辑删除标识 0正常 1删除 */
	protected String deleteFlag;
	/** 发放的url */
	protected String grantUrl;
	/** 发放开始时间 */
	protected Date grantStartTime;
	/** 发放结束时间 */
	protected Date grantEndTime;

	/** 发放项目类别id，多个用逗号分隔 */
	protected String grantProjectType;

	/** 规则是否可用（1/可用；默认不限），仅供查询使用 */
	private String ruleEnable;

	/**
	 * 检查规则是否满足发放条件
	 * 
	 * @author ywt
	 * @date 2016-11-05
	 * @return
	 */
	public void validRule() {
		// 验证是否删除 或则禁用
		if (OperateEnum.STATUS_FORBIDDEN.eq(this.status)
				|| DeleteFlagEnum.YES.eq(this.deleteFlag)) {
			throw new BussinessException(ResourceUtils.get(ResourceConstant.RULE_IS_BANED));
		}
		Date now=DateUtils.getNow();
		// 验证发放时间
		if (!((this.grantEndTime==null || now.before(this.grantEndTime))&&now.after(this.grantStartTime==null?this.createTime:this.grantStartTime))) {
			throw new BussinessException(ResourceUtils.get(ResourceConstant.RULE_OVER_GRANT_TIME));
		}
		// 校验发放数量
		if (this.totalNum!=null && this.lssueNum >= this.totalNum) {
			throw new BussinessException(ResourceUtils.get(ResourceConstant.REMAINING_RULE_NUM_INSUFFICIENT));
		}
	}


	/** 持久化前执行的操作 */
	@Override
	public void preInsert() {
		super.preInsert();
		this.deleteFlag = DeleteFlagEnum.NO.getValue();
		this.createTime = new Date((long)(System.currentTimeMillis()/1000) *1000);
		this.status = RedenvelopeRuleModel.STATUS_SERVICE;
		this.lssueNum = 0;
		this.updateTime = DateUtils.getNow();
	}

	/** 获取 项目类别 */
	public String getUseProjectType() {
		return useProjectType;
	}

	/** 设置 项目类别 */
	public void setUseProjectType(final String useProjectType) {
		this.useProjectType = useProjectType;
	}

	/** 获取 有效期天数 */
	public String getUseValidDay() {
		return useValidDay;
	}

	/** 设置 有效期天数 */
	public void setUseValidDay(final String useValidDay) {
		this.useValidDay = useValidDay;
	}

	/** 获取使用过期时间 */
	public Date getUseExpireTime() {
		return useExpireTime;
	}

	/** 设置 使用过期时间 */
	public void setUseExpireTime(final Date useExpireTime) {
		this.useExpireTime = useExpireTime;
	}

	/** 获取 规则唯一标示 */
	public String getActivityCode() {
		return activityCode;
	}

	/** 设置 规则唯一标示 */
	public void setActivityCode(final String activityCode) {
		this.activityCode = activityCode;
	}

	/** 获取 规则名称 */
	public String getRuleName() {
		return ruleName;
	}

	/** 设置 规则名称 */
	public void setRuleName(final String ruleName) {
		this.ruleName = ruleName;
	}

	/** 获取 状态 */
	public String getStatus() {
		return status;
	}

	/** 设置 状态 */
	public void setStatus(final String status) {
		this.status = status;
	}

	/** 获取 总的发放数量 */
	public Integer getTotalNum() {
		return totalNum;
	}

	/** 设置 总的发放数量 */
	public void setTotalNum(final Integer totalNum) {
		this.totalNum = totalNum;
	}

	/** 获取 已经发放数量 */
	public Integer getLssueNum() {
		return lssueNum;
	}

	/** 设置 已经发放数量 */
	public void setLssueNum(final Integer lssueNum) {
		this.lssueNum = lssueNum;
	}

	/** 获取 创建日期 */
	public Date getCreateTime() {
		return createTime;
	}

	/** 设置 创建日期 */
	public void setCreateTime(final Date createTime) {
		this.createTime = createTime;
	}

	/** 获取 更新时间 */
	public Date getUpdateTime() {
		return updateTime;
	}

	/** 设置 更新时间 */
	public void setUpdateTime(final Date updateTime) {
		this.updateTime = updateTime;
	}

	/** 获取删除标示 */
	public String getDeleteFlag() {
		return deleteFlag;
	}

	/** 设置删除标示 */
	public void setDeleteFlag(final String deleteFlag) {
		this.deleteFlag = deleteFlag;
	}

	/** 获取 发放url */
	public String getGrantUrl() {
		return grantUrl;
	}

	/** 设置 发放url */
	public void setGrantUrl(final String grantUrl) {
		this.grantUrl = grantUrl;
	}

	/** 获取 发放开始时间 */
	public Date getGrantStartTime() {
		return grantStartTime;
	}

	/** 设置 发放开始时间 */
	public void setGrantStartTime(final Date grantStartTime) {
		this.grantStartTime = grantStartTime;
	}

	/** 获取 发放结束时间 */
	public Date getGrantEndTime() {
		return grantEndTime;
	}

	/** 设置 发放结束时间 */
	public void setGrantEndTime(final Date grantEndTime) {
		this.grantEndTime = grantEndTime;
	}

	public String getGrantProjectType() {
		return grantProjectType;
	}

	public void setGrantProjectType(String grantProjectType) {
		this.grantProjectType = grantProjectType;
	}

	public String getRuleEnable() {
		return ruleEnable;
	}

	public void setRuleEnable(String ruleEnable) {
		this.ruleEnable = ruleEnable;
	}

}
