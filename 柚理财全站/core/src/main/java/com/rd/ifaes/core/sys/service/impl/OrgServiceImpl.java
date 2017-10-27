package com.rd.ifaes.core.sys.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.core.base.service.BaseServiceImpl;
import com.rd.ifaes.core.sys.domain.Org;
import com.rd.ifaes.core.sys.mapper.OrgMapper;
import com.rd.ifaes.core.sys.model.OrgModel;
import com.rd.ifaes.core.sys.service.OrgService;

/**
 * ServiceImpl:OrgServiceImpl
 * @author lh
 * @version 3.0
 * @date 2016-5-31
 */
@Service("orgService") 
public class OrgServiceImpl  extends BaseServiceImpl<OrgMapper, Org> implements OrgService{
	
	
	
	@Override
	@Transactional(readOnly = false)
	public void save(Org entity) {
		preInsUpd(entity);
		super.save(entity);
	}
	
	@Override
	@Transactional(readOnly = false)
	public void update(Org entity) {
		preInsUpd(entity);
		super.update(entity);
	}
	
	@Override
	@Transactional(readOnly = false)
	public void deleteBatch(String[] ids) {
		if(validHasChildren(ids) > 0){
			return ;
		}
		super.deleteBatch(ids);
	}
	
	@Override
	public int validHasChildren(String[] parentIds) {
		return dao.validHasChildren(parentIds);
	}
	
	/**
     * 
     * @param entity
     */
    private void preInsUpd(Org entity){
    	if(StringUtils.isNotBlank(entity.getParentId())){
    		Org parent = get(entity.getParentId());
    		String parentIds = StringUtils.isBlank(parent.getParentIds()) ? parent.getUuid() : 
    				( parent.getParentIds().concat(":").concat(parent.getUuid()) );
    		entity.setParentIds(parentIds);
    		entity.setOrgLevel((StringUtils.isBlank(entity.getParentIds())?0:entity.getParentIds().split(":").length));
    	}else{
    		entity.setOrgLevel(0);
    	}
    }
	
	@Override
	public List<OrgModel> getOrgTree(String parentId) {
		List<OrgModel> rootModel = new ArrayList<OrgModel>();
    	List<Org> rootOrg = dao.getCurrOrg(null);
    	
    	// 获取需要展开的所有id
		List<String> listExpanded = new ArrayList<>();
		if(StringUtils.isNotBlank(parentId)){
			listExpanded.add(parentId);
			getExpandedId(listExpanded,parentId);
		}
    	
    	for (Org org : rootOrg) {
    		OrgModel currModel = OrgModel.getInstance(org);
    		currModel.setNodes(getAllChildren(org.getUuid(),listExpanded));	//添加子节点
    		currModel.setPid(org.getParentId());
			for(String expandedId : listExpanded){
    			if(expandedId.equals(org.getUuid())){
    				currModel.setExpanded(true);
    			}
    		}
    		rootModel.add(currModel);	//添加父节点
    	}
    	return rootModel;
	}
	
	/**获取所有的展开id集合*/
	private List<String> getExpandedId(List<String> listExpanded,final String id) {
		Org temp = dao.get(id);
		if(temp != null && temp.getParentId() != null){
			listExpanded.add(temp.getParentId());
			listExpanded = getExpandedId(listExpanded,temp.getParentId());
		}
		return listExpanded;
	}
	
    public List<OrgModel> getAllChildren(String parentId, List<String> listExpanded){
    	List<OrgModel> list = new ArrayList<OrgModel>();
    	Org model = new Org();
    	model.setParentId(parentId);
    	model.setPage(null);
    	List<Org> children = findList(model);
    	for (Org organization : children) {
    		OrgModel currModel = OrgModel.getInstance(organization);
			list.add(currModel);
			Org current = new Org();
			current.setParentId(organization.getUuid());
			current.setPage(null);
			currModel.setPid(organization.getParentId());
			for(String expandedId : listExpanded){
    			if(expandedId.equals(organization.getUuid())){
    				currModel.setExpanded(true);
    			}
    		}
			if(getCount(current) > 0){
				currModel.setNodes(getAllChildren(organization.getUuid(),listExpanded));
			}
		}
    	return list;
    }
}