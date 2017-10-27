package com.rd.ifaes.core.sys.service;

import java.util.List;

import com.rd.ifaes.core.base.service.BaseService;
import com.rd.ifaes.core.sys.domain.Operation;

/**
 * Service Interface:OperationService
 * @author lh
 * @version 3.0
 * @date 2016-6-2
 */
public interface OperationService extends BaseService<Operation>{

	/**
	 * 通过menuId查找按钮
	 * @param menuId
	 * @return
	 */
	List<Operation> findByMenuId(String menuId);

}