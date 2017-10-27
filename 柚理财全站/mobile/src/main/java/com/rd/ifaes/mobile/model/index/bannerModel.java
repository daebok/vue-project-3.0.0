/**
 * banner模型
 */
package com.rd.ifaes.mobile.model.index;

/**
 * @author yoseflin
 * banner模型
 */
public class bannerModel {

	/** 
	 * 图片路径 
	 */
	private String picPath;
	/** 
	 * 链接 
	 */
	private String url;
	public String getPicPath() {
		return picPath;
	}
	public void setPicPath(String picPath) {
		this.picPath = picPath;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	/** 
	 * 标题 
	 */
	private String title;
	
	
}
