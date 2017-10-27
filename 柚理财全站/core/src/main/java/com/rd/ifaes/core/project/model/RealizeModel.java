package com.rd.ifaes.core.project.model;

import org.springframework.beans.BeanUtils;

import com.rd.ifaes.core.project.domain.Realize;

public class RealizeModel extends Realize{

	private static final long serialVersionUID = 1L;
	
	private String userName;
	
	private String oldProjectName;

	/**
	 * 获取属性userName的值
	 * @return userName属性值
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * 设置属性userName的值
	 * @param  userName属性值
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * 获取属性oldProjectName的值
	 * @return oldProjectName属性值
	 */
	public String getOldProjectName() {
		return oldProjectName;
	}

	/**
	 * 设置属性oldProjectName的值
	 * @param  oldProjectName属性值
	 */
	public void setOldProjectName(String oldProjectName) {
		this.oldProjectName = oldProjectName;
	}

	public static RealizeModel instance(Realize realize) {
		RealizeModel realizeModel = new RealizeModel();
		BeanUtils.copyProperties(realize, realizeModel);
		return realizeModel;
	}	
	
}
