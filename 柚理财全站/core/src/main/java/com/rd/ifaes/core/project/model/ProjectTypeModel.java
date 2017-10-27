package com.rd.ifaes.core.project.model;

import com.rd.ifaes.common.entity.TreeEntity;
import com.rd.ifaes.core.project.domain.ProjectType;

public class ProjectTypeModel extends TreeEntity<ProjectTypeModel> {
	private static final long serialVersionUID = -3337318346482795670L;

	public ProjectTypeModel() {
		super();
	}

	public ProjectTypeModel(String id, String text, int sort) {
		super.id = id;
		super.text = text;
		super.sort = sort;
	}
	public static ProjectTypeModel getInstance(ProjectType type){
		return new ProjectTypeModel(type.getUuid(),type.getTypeName(),type.getSort());
	}
}
