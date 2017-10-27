package com.rd.ifaes.core.sys.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.rd.ifaes.core.sys.service.AreaService;
import com.rd.ifaes.core.sys.mapper.AreaMapper;
import com.rd.ifaes.core.sys.model.AreaModel;
import com.rd.ifaes.core.sys.domain.Area;
import com.rd.ifaes.core.base.service.*;

import org.springframework.stereotype.Service;

/**
 * 地区处理类
 * @author xhf
 * @version 3.0
 * @date 2016-7-29
 */
@Service("areaService") 
public class AreaServiceImpl  extends BaseServiceImpl<AreaMapper, Area> implements AreaService{
	
	/**
	 * 根据areaCode查找对象
	 */
    @Override
    public AreaModel findByAreaCode(final String areaCode){
    	AreaModel model = null;
    	final List<Area> list = dao.findList(new Area(areaCode, null));
    	if(!list.isEmpty()){
    		model = AreaModel.instance(list.get(0));
    	}
    	return model;
    }
	
    /**
     * 根据parentCode查找子对象集合
     */
    @Override
    public List<AreaModel> findByParentCode(final String parentCode){
    	final List<AreaModel> modelList = new ArrayList<AreaModel>();
    	final List<Area> areaList = dao.findList(new Area(null, parentCode));
    	for (final Area area : areaList) {
    		modelList.add(AreaModel.instance(area));
		}
    	
    	return modelList;
    }

    /**
     * 根据code获得地名
     */
	@Override
	public String getNameByCode(final String areaCode) {
		return dao.getNameByCode(areaCode);
	}
    
}