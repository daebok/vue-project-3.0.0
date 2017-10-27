package com.rd.ifaes.core.sys.model;

import com.rd.ifaes.common.entity.TreeEntity;
import com.rd.ifaes.core.sys.domain.Org;
/**
 * 组织机构 Model
 * @author lh
 * @since 2016年5月19日 
 * @version 3.0
 */
public class OrgModel extends TreeEntity<OrgModel> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public OrgModel() {
		super();
	}

	public OrgModel(String id, String text, int sort) {
		super.id = id;
		super.text = text;
		super.sort = sort;
	}

	public static OrgModel getInstance(Org org){
		return new OrgModel(org.getUuid(),org.getOrgName(),org.getSort());
	}
	
}
