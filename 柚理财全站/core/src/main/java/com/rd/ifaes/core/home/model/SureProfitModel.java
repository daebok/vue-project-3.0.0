/**
* 互联网金融交易系统ifaes (Internet Finance  Assets  Exchange  System)
 * Copyright (c) 2016 杭州融都科技股份有限公司 版权所有
 */
package com.rd.ifaes.core.home.model;

import java.io.Serializable;

/**
 *  首页稳赚系列数据
 * @version 3.0
 * @author FangJun
 * @date 2016年11月9日
 */
public class SureProfitModel implements Serializable {
	private static final long serialVersionUID = -9219537045769228865L;
	/**
	 * 项目类别UUID (理财频道下一级前2个)
	 */
	private String typeId;
	/**
	 * 项目类别名称
	 */
	private String typeName;
	/**
	 * 项目类别特性
	 */
	private String features;
	/**
	 *  年利率区间值（最小值-最大值）
	 */
    private String apr;
     /**
      * 最小年利率
      */
    private Double  minApr;
    /**
     * 最大年利率
     */
    private Double maxApr;

	/**
	 * 获取项目类别UUID
	 * @return 项目类别UUID
	 */
	public String getTypeId() {
		return typeId;
	}

	/**
	 * 设置项目类别UUID
	 * @param  项目类别UUID
	 */
	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}

	/**
	 * 获取属性typeName值
	 * @return typeName属性值
	 */
	public String getTypeName() {
		return typeName;
	}

	/**
	 * 设置属性typeName值
	 * @param  typeName属性值
	 */
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	/**
	 * 获取项目类别特性
	 * @return 项目类别特性
	 */
	public String getFeatures() {
		return features;
	}

	/**
	 * 设置项目类别特性
	 * @param  项目类别特性
	 */
	public void setFeatures(String features) {
		this.features = features;
	}

	/**
	 * 获取 年利率区间值
	 * @return  年利率区间值
	 */
	public String getApr() {
		StringBuilder builder=new StringBuilder();
		if(minApr!=null && maxApr!=null && !minApr.equals(maxApr)){
			builder.append(minApr).append("-").append(maxApr);
		}else{
			builder.append(minApr);
		}
		return builder.toString();	
	}
 
	/**
	 * 获取最小年利率
	 * @return 最小年利率
	 */
	public Double getMinApr() {
		return minApr;
	}

	/**
	 * 设置最小年利率
	 * @param  最小年利率
	 */
	public void setMinApr(Double minApr) {
		this.minApr = minApr;
	}

	/**
	 * 获取最大年利率
	 * @return 最大年利率
	 */
	public Double getMaxApr() {
		return maxApr;
	}

	/**
	 * 设置最大年利率
	 * @param  最大年利率
	 */
	public void setMaxApr(Double maxApr) {
		this.maxApr = maxApr;
	}
                 
}
