package com.rd.ifaes.core.sys.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.rd.ifaes.core.base.mapper.BaseMapper;
import com.rd.ifaes.core.sys.domain.Operation;

/**
 * Dao Interface:OperationMapper
 * @author lh
 * @version 3.0
 * @date 2016-6-2
 */
public interface OperationMapper extends BaseMapper<Operation> {
	
	/**
	 * 取得菜单下的操作
	 * @param menuId
	 * @return
	 */
	List<Operation> findListByMenuId(@Param("menuId") String menuId, @Param("operatorId") String operatorId);

}