package com.rd.ifaes.mobile.model.extra;

import java.util.Date;

public class AppArticleModel {
	/** 
	 * 标题
	 */
	private String title;
	/** 
	 * 简介
	 */
	private String remark;
	/** 
	 * 点击量 
	 */
	private Integer clicks;
	/** 
	 * 添加时间 
	 */
	private Date createTime;
	/** 
	 * uuid 
	 */
	private String uuid;
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	/** 
	 * 图片路径 
	 */
	private String picPath;
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Integer getClicks() {
		return clicks;
	}
	public void setClicks(Integer clicks) {
		this.clicks = clicks;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getPicPath() {
		return picPath;
	}
	public void setPicPath(String picPath) {
		this.picPath = picPath;
	}
}
