package com.rd.ifaes.core.project.mapper;

import java.util.List;

import com.rd.ifaes.core.base.mapper.BaseMapper;
import com.rd.ifaes.core.project.domain.Product;

/**
 * Dao Interface:ProductMapper
 * @author FangJun
 * @version 3.0
 * @date 2016-6-20
 */
public interface ProductMapper extends BaseMapper<Product> {

	/**
	 * 根据产品id获取可变现状态
	 * @author ZhangBiao
	 * @date 2016年10月19日
	 * @param projectId
	 * @return
	 */
	String getRealizeUsefulByProjectId(String projectId);
	/**
	 * 产品列表(后台查询)
	 * @param model 查询条件
	 * @return 产品列表
	 */
	List<Product> productQuery(Product model);
}