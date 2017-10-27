package com.rd.ifaes.core.project.service;

import com.rd.ifaes.core.base.service.BaseService;
import com.rd.ifaes.core.project.domain.Product;

/**
 * Service Interface:ProductService
 * @author FangJun
 * @version 3.0
 * @date 2016-6-20
 */
public interface ProductService extends BaseService<Product>{
 
	/**
	 * 发布审核
	 * @param product 产品
	 */
	void publishVerify(Product product);
	/**
	 * 产品上架
	 * @param product 产品
	 * @return
	 */
	void productSaleEdit(Product product);
	
	/**
	 * 产品下架
	 * @param product 产品信息（含ID、下架原因）
	 * @return
	 */
	void productStop(Product product);
	/**
	 * 产品成立审核 
	 * @param product 产品
	 * @return
	 */
	void productEstablishVerify(Product product);
	
	/**
	 * 查询原始产品信息(缓存)
	 * @author fxl
	 * @date 2016年8月25日
	 * @param uuid
	 * @return
	 */
	String getOrgProduct(String projectId);
	
	/**
	 * 后台分页查询产品列表
	 * @author ZhangBiao
	 * @date 2016年10月19日
	 * @param product
	 * @return
	 */
	Object findManagePage(Product product);
	
	/**
	 * 根据产品id获取可变现状态
	 * @author ZhangBiao
	 * @date 2016年10月19日
	 * @param uuid
	 * @return
	 */
	String getRealizeUsefulByProjectId(String projectId);
}