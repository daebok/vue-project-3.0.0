package com.rd.ifaes.core.project.model;

import com.rd.ifaes.core.project.domain.Buyback;

public class BuybackModel extends Buyback{

	private static final long serialVersionUID = 1L;
	
	/**
	 * 项目名称
	 */
	private String projectName;

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	
	
}
