package com.rd.ifaes.core.sys.mapper;

import java.util.List;

import com.rd.ifaes.core.base.mapper.BaseMapper;
import com.rd.ifaes.core.sys.domain.Org;

/**
 * Dao Interface:OrgMapper
 * @author lh
 * @version 3.0
 * @date 2016-5-31
 */
public interface OrgMapper extends BaseMapper<Org> {

	/**
	 * 取得组织结构树
	 * @param id
	 * @return
	 */
	List<Org> getOrgTree(String id);
	
	/**
	 * 获得本级组织机构
	 * @param id
	 * @return
	 */
	List<Org> getCurrOrg(String id);
	
	int validHasChildren(String [] parentIds);
}