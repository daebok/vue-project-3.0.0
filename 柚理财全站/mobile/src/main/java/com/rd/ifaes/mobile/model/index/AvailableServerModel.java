package com.rd.ifaes.mobile.model.index;

import java.util.List;

import com.rd.ifaes.core.sys.domain.DictData;

/**
 * 可用服务器模型
 * @author yoseflin
 */
public class AvailableServerModel {

	/**
	 * 服务器
	 */
	private String mobileServer;
	/**
	 * 每年天数
	 */
	private String daysOfYear;
	public String getDaysOfYear() {
		return daysOfYear;
	}

	public void setDaysOfYear(String daysOfYear) {
		this.daysOfYear = daysOfYear;
	}

	/**
	 * 收益方式
	 */
	private List<DictData> repayStyles;
	public List<DictData> getRepayStyles() {
		return repayStyles;
	}

	public void setRepayStyles(List<DictData> repayStyles) {
		this.repayStyles = repayStyles;
	}

	public String getMobileServer() {
		return mobileServer;
	}

	public void setMobileServer(String mobileServer) {
		this.mobileServer = mobileServer;
	}

	/**
	 * 图像资源服务器
	 */
	private String imageServer;

	//--------------------------华丽分割线----------------------------------

	public String getImageServer() {
		return imageServer;
	}

	public void setImageServer(String imageServer) {
		this.imageServer = imageServer;
	}
	
}
