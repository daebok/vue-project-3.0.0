package com.rd.ifaes.core.column.service.impl;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.rd.ifaes.common.annotation.Cacheable;
import com.rd.ifaes.common.dict.DeleteFlagEnum;
import com.rd.ifaes.common.dict.ExpireTime;
import com.rd.ifaes.common.exception.BussinessException;
import com.rd.ifaes.common.orm.Page;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.core.base.service.BaseServiceImpl;
import com.rd.ifaes.core.column.domain.Section;
import com.rd.ifaes.core.column.mapper.SectionMapper;
import com.rd.ifaes.core.column.model.SectionModel;
import com.rd.ifaes.core.column.service.SectionService;
import com.rd.ifaes.core.core.constant.CacheConstant;
import com.rd.ifaes.core.core.constant.Constant;
import com.rd.ifaes.core.core.util.CacheUtils;


/**
 * ServiceImpl:SectionServiceImpl
 * @author WengYaWen
 * @version 3.0
 * @date 2016-7-21
 */
@Service("sectionService") 
public class SectionServiceImpl  extends BaseServiceImpl<SectionMapper, Section> implements SectionService{
	 /**查询栏目*/
	@Override
	public Page<Section> findListByPage(final Section model) {
		final Page<Section> page=model.getPage();
		model.setDeleteFlag(DeleteFlagEnum.NO.getValue());
		page.setRows(dao.findList(model));
		return page;
	}
	
	@Override
	public void update(Section entity) {
		super.update(entity);
		if(StringUtils.isNotBlank(entity.getSectionCode())){
			CacheUtils.del(CacheConstant.KEY_PREFIX_SECTION_CODE+entity.getSectionCode());			
		}
	}
	
	 /**获取所有的子节点*/
	@Override
	public List<SectionModel> getSectionTree(final String parentId) {
		
		//获取所有栏目
		final Section querySection = new Section();
		querySection.setDeleteFlag(DeleteFlagEnum.NO.getValue());
		final List<Section> sectionList = dao.findList(querySection);
		
		// 获取需要展开的所有id
		List<String> listExpanded = new ArrayList<>();
		if(StringUtils.isNotBlank(parentId)){
			listExpanded.add(parentId);
			getExpandedId(listExpanded,parentId);
		}
		
		final List<SectionModel> list = new ArrayList<SectionModel>();
		for(Section section : sectionList){
			 if(StringUtils.isBlank(section.getParentId())){  // 根节点
				 SectionModel rootModel = SectionModel.instance(section);
				 rootModel.setPid(section.getParentId());
				 for(String expandedId : listExpanded){
	    			if(expandedId.equals(section.getUuid())){
	    				rootModel.setExpanded(true);
	    			}
	    		}
				 //rootModel.setState(checked);
				 list.add(rootModel);
				 //向下遍历
				 childTreeNode(rootModel, section, sectionList,listExpanded);
			 }
		 }
    	return list;
	}
	
	/**获取所有的展开id集合*/
	private List<String> getExpandedId(List<String> listExpanded,final String id) {
		Section temp = dao.get(id);
		if(temp != null && temp.getParentId() != null){
			listExpanded.add(temp.getParentId());
			listExpanded = getExpandedId(listExpanded,temp.getParentId());
		}
		return listExpanded;
	}
	
	/**获取所有的子节点*/
	public List<SectionModel> getAllChildren(final String parentId,final List<String> listExpanded){
		final List<SectionModel> list=new ArrayList<SectionModel>();
		final Section temp=new Section();
		temp.setParentId(parentId);
		temp.setPage(null);
		final List<Section> children=this.findList(temp);
		for (final Section t : children){
			final SectionModel currModel=SectionModel.instance(t);
			 list.add(currModel);
			 currModel.setCode(t.getSectionCode());
			 final Section  current = new Section();
			 current.setParentId(t.getUuid());
			 current.setDeleteFlag(DeleteFlagEnum.NO.getValue());
			 current.setPage(null);
			 currModel.setPid(t.getParentId());
			 for(String expandedId : listExpanded){
    			if(expandedId.equals(t.getUuid())){
    				currModel.setExpanded(true);
    			}
			 }
			 if(this.getCount(current)>0){
				 currModel.setNodes(getAllChildren(t.getUuid(),listExpanded));
			 }
		}
		return list;
	 }
	 
	 /**根据栏目标识查询对象*/
	 @Override
	 @Cacheable(key=CacheConstant.KEY_PREFIX_SECTION_CODE+"{sectionCode}", expire = ExpireTime.ONE_DAY)
	 public Section getByCode(final String sectionCode){
		 if(StringUtils.isBlank(sectionCode)){
			 throw new BussinessException("栏目标识不能为空", BussinessException.TYPE_JSON);
		 }
		 final Section model = new Section();
		 model.setDeleteFlag(Constant.FLAG_NO);
		 model.setSectionCode(sectionCode);
		 return dao.getByCode(model);
	 }
	 
	 
	 /**
	  * 构建子树节点
	  * @return void
	  * @author xxb
	  * @date 2016年10月31日
	  */
	 public void childTreeNode(SectionModel parentModel, Section sec, List<Section> sectionList, List<String> listExpanded){
		 List<SectionModel> nodes = new LinkedList<SectionModel>();
		 parentModel.setNodes(nodes);
		 for(Section section : sectionList){
			 if(StringUtils.equals(section.getParentId(), sec.getUuid())){
				 SectionModel childModel = SectionModel.instance(section);
				 childModel.setPid(section.getParentId());
				 for(String expandedId : listExpanded){
	    			if(expandedId.equals(section.getUuid())){
	    				childModel.setExpanded(true);
	    			}
				 }
				 parentModel.getNodes().add(childModel);
				 childTreeNode(childModel, section, sectionList,listExpanded);
			 }
		 }
	 }
	 
	 /**
	 * 
	 * 获取子节点列表
	 * @param  
	 * @return List<Section>
	 * @author xxb
	 * @date 2016年11月1日
	 */
	 public List<Section> findChildSection(String parentId){
		 
		 	final Section querySection = new Section();
		    querySection.setDeleteFlag(DeleteFlagEnum.NO.getValue());
			final List<Section> sectionList = dao.findList(querySection);
			final List<Section> destList = new ArrayList<Section>();
			for(Section section : sectionList){
				if(StringUtils.equals(parentId, section.getUuid())){
					destList.add(section);
					break;
				}
			}
			childNode(parentId, sectionList, destList);
	    	return destList;
	 }
	 
	 
	 /**
	  * 构建子节点
	  * @return void
	  * @author xxb
	  * @date 2016年10月31日
	  */
	 public void childNode(String parentId, List<Section> sourceList, List<Section> destList){
		 for(Section section : sourceList){
			 if(StringUtils.equals(section.getParentId(), parentId)){
				 destList.add(section);
				 childNode(section.getUuid(), sourceList, destList);
			 }
		 }
	 }

	public Section getByCodeId(String sectionCode, String uuid) {
		final Section model = new Section();
		model.setDeleteFlag(Constant.FLAG_NO);
		model.setSectionCode(sectionCode);
		model.setUuid(uuid);
		return dao.getByCode(model);
	}
}