package com.rd.ifaes.core.sys.service;

import com.rd.ifaes.core.sys.domain.Resource;
import com.rd.ifaes.core.base.service.BaseService;

/**
 * Service Interface:ResourceService
 * @author lh
 * @version 3.0
 * @date 2016-7-27
 */
public interface ResourceService extends BaseService<Resource>{
	
	/**
	 * 
	 * @param name
	 * @return
	 */
	Resource getByName(String name, String language);

}