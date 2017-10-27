package com.rd.ifaes.common.util.excel;

import java.util.List;

import com.rd.ifaes.common.entity.BaseEntity;

/**
 *  Excel导出需要信息抽象接口
 * 
 * @author FangJun
 *
 */
@SuppressWarnings("rawtypes")
public interface  ExportModel<T extends BaseEntity > {
	 
	/**
	 * 下载进度缓存标识（目前建议使用用户ID)
	 * @return 下载进度缓存标识
	 */
	String getCacheKey();
	
     /**
      * 查询条件信息 
      * @return   查询条件信息 
      */
	 T getModel();
	
	/**
	 * 导出数据总条数
    * @param   entity 含有查询条件和页数
	 * @return 数据总条数
	 */
	  int  getTotal(T entity);
   /**
    * 查询指定页的数据
    * @param   entity 含有查询条件和页数
    * @return 页面数据
    */
	  List getPageRecords(T entity);
}
