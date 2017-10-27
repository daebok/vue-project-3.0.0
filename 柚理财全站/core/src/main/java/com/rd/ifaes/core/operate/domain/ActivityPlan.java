package com.rd.ifaes.core.operate.domain;

import java.util.Date;

import com.rd.ifaes.common.entity.BaseEntity;

/**
 * 活动方案pojo
 * @version 3.0
 * @author wyw
 * @date 2016-7-28
 */
public class ActivityPlan extends BaseEntity<ActivityPlan> {
	/** 序列号 */
	private static final long serialVersionUID = -4559962117212259324L;
	/** 活动方案名称 */
	private String activityName;
	/** 活动方案唯一标识 */
	private String activityCode;
	/** 排序字段 */
	private Integer sort;
	/** 状态 ：0禁用 1启用 */
	private String status;
	/** 创建日期 */
	private Date createTime;

	// 其他自定义属性
	/** 状态 启用 */
	public static final String STATUS_SERVICE = "1";
	/** 状态 禁用 */
	public static final String STATUS_FORBIDDEN = "0";

	/** Constructor */
	public ActivityPlan() {
		super();
	}

	/**
	 * full Constructor
	 */
	public ActivityPlan(final String uuid, final String activityName, final String activityCode, final Integer sort,
			final String status, final Date createTime) {
		super();
		setUuid(uuid);
		this.activityName = activityName;
		this.activityCode = activityCode;
		this.sort = sort;
		this.status = status;
		this.createTime = createTime;
	}

	/** 获取 活动名称值 */
	public String getActivityName() {
		return activityName;
	}

	/** 设置 活动名称值 */
	public void setActivityName(final String activityName) {
		this.activityName = activityName;
	}

	/** 获取 活动唯一标识值 */
	public String getActivityCode() {
		return activityCode;
	}

	/** 设置 活动唯一标识值 */
	public void setActivityCode(final String activityCode) {
		this.activityCode = activityCode;
	}

	/** 获取 排序值 */
	public Integer getSort() {
		return sort;
	}

	/** 设置 排序值 */
	public void setSort(final Integer sort) {
		this.sort = sort;
	}

	/** 获取 状态 */
	public String getStatus() {
		return status;
	}

	/** 设置 状态 */
	public void setStatus(final String status) {
		this.status = status;
	}

	/** 获取 创建日期 */
	public Date getCreateTime() {
		return createTime;
	}

	/** 设置 创建日期 */
	public void setCreateTime(final Date createTime) {
		this.createTime = createTime;
	}

	/** toString */
	@Override
	public String toString() {
		return "ActivityPlan [" + "uuid=" + uuid + ", activityName=" + activityName + ", activityCode=" + activityCode
				+ ", sort=" + sort + ", status=" + status + ", createTime=" + createTime + "]";
	}
}
