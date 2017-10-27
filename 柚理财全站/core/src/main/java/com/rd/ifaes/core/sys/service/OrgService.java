package com.rd.ifaes.core.sys.service;

import java.util.List;

import com.rd.ifaes.core.base.service.BaseService;
import com.rd.ifaes.core.sys.domain.Org;
import com.rd.ifaes.core.sys.model.OrgModel;

/**
 * Service Interface:OrgService
 * @author lh
 * @version 3.0
 * @date 2016-5-31
 */
public interface OrgService extends BaseService<Org>{

	/**
	 * 取得组织结构树
	 * @param id
	 * @return
	 */
	List<OrgModel> getOrgTree(final String uuid);
	
	/**
	 * 判断是否有子节点
	 * @param parentIds
	 * @return
	 */
	int validHasChildren(final String[] parentIds);
}