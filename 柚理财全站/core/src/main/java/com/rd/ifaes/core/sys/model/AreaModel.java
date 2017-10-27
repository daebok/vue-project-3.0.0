package com.rd.ifaes.core.sys.model;

import java.io.Serializable;
import java.util.List;

import com.rd.ifaes.core.sys.domain.Area;

public class AreaModel implements Serializable {

	private static final long serialVersionUID = -6818520737286211387L;
	/** 区域编码 */ 
	private String	value;		         
	/** 区域编码 */ 
	private String	label;		        
	/** 子集区域*/
	private  List<AreaModel> children;   
	
	/**
	 * 获取属性value的值
	 * @return value属性值
	 */
	public String getValue() {
		return value;
	}

	/**
	 * 设置属性value的值
	 * @param  value属性值
	 */
	public void setValue(final String value) {
		this.value = value;
	}

	/**
	 * 获取属性label的值
	 * @return label属性值
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * 设置属性label的值
	 * @param  label属性值
	 */
	public void setLabel(final String label) {
		this.label = label;
	}

	/**
	 * 获取属性children的值
	 * @return children属性值
	 */
	public List<AreaModel> getChildren() {
		return children;
	}

	/**
	 * 设置属性children的值
	 * @param  children属性值
	 */
	public void setChildren(final List<AreaModel> children) {
		this.children = children;
	}
	
	/**
	 * 
	 * 将area中与model相同的信息拷贝到model
	 * @author xhf
	 * @date 2016年7月29日
	 * @param area
	 * @return
	 */
	public static AreaModel instance(final Area area) {
		final AreaModel model = new AreaModel();
		model.setLabel(area.getAreaName());
		model.setValue(area.getAreaCode());
		return model;
	}

}
