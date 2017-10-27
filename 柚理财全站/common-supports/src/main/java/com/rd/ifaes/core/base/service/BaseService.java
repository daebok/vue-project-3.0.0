package com.rd.ifaes.core.base.service;

import java.util.List;

import com.rd.ifaes.common.orm.Page;



/**
 * Service基类
 * @author lh
 * @version 3.0
 * @date 2016年5月9日
 */
public interface BaseService<T> {
	
	/**
	 * 插入数据
	 * @param entity
	 * @return 
	 */
	void insert(T entity);
	
	/**
	 * 批量插入
	 * @param list
	 */
	void insertBatch(List<T> list);
		
	/**
	 * 删除数据
	 * @param id
	 * @return 
	 */
	void delete(String id);
	
	/**
	 * 批量删除
	 * @param ids
	 * @return
	 */
	void deleteBatch(String [] ids);
	
	/**
	 * 逻辑删除
	 * @param id
	 * @return 
	 */
	void deleteLogic(String id);
	
	/**
	 * 更新数据
	 * @param entity
	 * @return 
	 */
	void update(T entity);
	
	/**
	 * 批量更新
	 * @param list
	 */
	void updateBatch(List<T> list);
	
	/**
	 * 查询所有数据列表
	 * @param model
	 * @return
	 */
	List<T> findAll();
	
	/**
	 * 查询数据列表，如果需要分页，请设置分页对象，如：model.setPage(new Page<T>());
	 * @param model
	 * @return
	 */
	List<T> findList(T model);
	
	/**
	 * 查询数据记录
	 * @param model
	 * @return
	 */
	int getCount(T model);
	/**
	 * 获取单条数据
	 * @param id
	 * @return
	 */
	T get(String id);
	
	/**
	 * 保存数据
	 * @param entity
	 * @return
	 */
	void save(T entity);
	
	/**
	 * 查询分页数据
	 * @param entity
	 * @return
	 */
	Page<T> findPage(T entity);
	
}
