package com.rd.ifaes.core.project.mapper;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.rd.ifaes.core.base.mapper.BaseMapper;
import com.rd.ifaes.core.project.domain.Realize;

/**
 * Dao Interface:RealizeMapper
 * @author FangJun
 * @version 3.0
 * @date 2016-7-13
 */
public interface RealizeMapper extends BaseMapper<Realize> {
	
	/**
	 * 获取变现列表
	 * @author fxl
	 * @date 2016年8月01日
	 * @param entity
	 * @return
	 */
	List<Realize> findRealizeList(Realize entity);
	
	/**
	 * 获取变现前台列表
	 * @author fxl
	 * @date 2016年8月01日
	 * @param entity
	 * @return
	 */
	List<Realize> findWebRealizeList(Realize entity);
	/**
	 * 查询变现实体
	 * @author fxl
	 * @date 2016年8月01日
	 * @param uuid
	 * @return
	 */
	Realize findRealize(String uuid);
	
	/**
	 * 获取当日还款的变现产品
	 * @author fxl
	 * @date 2016年8月31日
	 * @param nowTime
	 * @return
	 */
	List<Realize> findRealizeRepay(@Param("nowTime")Date nowTime);
	
	/**
	 * 获取已变现条数
	 * @author fxl
	 * @date 2016年11月29日
	 * @param investId
	 * @return
	 */
	int countSuccessRealize(@Param("investId")String investId);
}