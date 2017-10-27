/**
* 互联网金融交易系统ifaes (Internet Finance  Assets  Exchange  System)
 * Copyright (c) 2016 杭州融都科技股份有限公司 版权所有
 */
package com.rd.ifaes.manage.sys;

import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rd.ifaes.common.exception.BussinessException;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.common.util.resource.LoanResource;
import com.rd.ifaes.core.cache.ProjectCache;
import com.rd.ifaes.core.core.constant.CacheConstant;
import com.rd.ifaes.core.core.constant.ResourceConstant;
import com.rd.ifaes.core.core.util.CacheUtils;
import com.rd.ifaes.core.core.util.ResourceUtils;
import com.rd.ifaes.core.project.domain.Project;
import com.rd.ifaes.core.project.service.ProjectService;

/**
 *  缓存清理
 * @version 3.0
 * @author FangJun
 * @date 2016年9月22日
 */
@Controller
public class CacheManageController extends SystemController {

	 private static final  Logger LOGGER = LoggerFactory.getLogger(CacheManageController.class);
	 
     @Resource
	 private  ProjectService  projectService;
     /**
      *  缓存管理首页
      * @author  FangJun
      * @date 2016年9月22日
      * @return
      */
 	@RequestMapping("/sys/cache/index")
 	public String  index(){
 		
 		return  "/sys/cache/index";
 	}
     /**
      * 项目缓存管理首页
      * @author  FangJun
      * @date 2016年9月22日
      * @return
      */
	@RequestMapping("/sys/cache/project")
	public String  project(){
		
		return  "/sys/cache/project";
	}
	/**
	 * 项目信息列表
	 * 
	 * @return
	 */
	@RequestMapping(value = "/sys/cache/projectQueryList", method = RequestMethod.POST)
	@ResponseBody
	public Object projectQueryList(Project project) {
		return projectService.findPage(project);
	}
	
	
	/**
	 * 产品列表缓存清除
	 * 
	 * @return
	 */
	@RequestMapping(value = "/sys/cache/clearCacheBatch", method = RequestMethod.POST)
	@ResponseBody
	public Object clearCacheBatch(String prefix) {
		if (StringUtils.isBlank(prefix)) {
			throw new BussinessException(ResourceUtils.get(ResourceConstant.GLOBAL_NO_RECORD));
		}
		LOGGER.debug("[Cache] clear  : clearCacheBatch---- prefix ===  {} !",prefix);
		   //满标，清理所有项目列表缓存
	  	CacheUtils.batchDel(prefix);
		return renderSuccessResult();
	}
	/**
	 * 产品列表缓存清除
	 * 
	 * @return
	 */
	@RequestMapping(value = "/sys/cache/get", method = RequestMethod.POST)
	@ResponseBody
	public Object getCache(String cacheKey) {
		if (StringUtils.isBlank(cacheKey)) {
			throw new BussinessException(ResourceUtils.get(ResourceConstant.GLOBAL_NO_RECORD));
		}
		LOGGER.debug("[Cache] get  : getCache---- cacheKey ===  {} !",cacheKey);
		   //满标，清理所有项目列表缓存
		final Map<String,Object> data=renderSuccessResult();
		Object obj=CacheUtils.getObj(cacheKey.trim());
	  	data.put(DATA_NAME,obj==null?CacheUtils.getStr(cacheKey.trim()):obj.toString());
		return data;
	}
	/**
	 * 产品列表缓存清除
	 * 
	 * @return
	 */
	@RequestMapping(value = "/sys/cache/clearProjectList", method = RequestMethod.POST)
	@ResponseBody
	public Object clearProjectList() {
		LOGGER.debug("[Cache] clear  : clearProjectList !");
		   //满标，清理所有项目列表缓存
	  	CacheUtils.batchDel(CacheConstant.KEY_PREFIX_PROJECT_LIST);
		return renderSuccessResult();
	}
	
    /**
     * 产品详情缓存清除
     * @author  FangJun
     * @date 2016年9月22日
     * @param projectId
     * @return
     */
	@RequestMapping(value = "/sys/cache/clearProjectDetail", method = RequestMethod.POST)
	@ResponseBody
	public Object clearProjectDetail(String projectId) {
		if (StringUtils.isBlank(projectId)) {
			throw new BussinessException(ResourceUtils.get(ResourceConstant.GLOBAL_NO_RECORD));
		}
		LOGGER.debug("[Cache] clear  : clearProjectDetail (projectId:{})!",projectId);
		//清理项目详情缓存
		 CacheUtils.del(CacheConstant.KEY_PREFIX_PROJECT_DETAIL+projectId);
		 CacheUtils.del(CacheConstant.KEY_PROJECT_UUID_PREFIX+projectId);
		return renderSuccessResult();
	}
	
	/**
	 *项目剩余可投金额清理
	 * @author  FangJun
	 * @date 2016年9月22日
	 * @param projectId
	 * @return
	 */
	@RequestMapping(value = "/sys/cache/clearRemainAccount", method = RequestMethod.POST)
	@ResponseBody
	public Object clearRemainAccount(String projectId) {
		if (StringUtils.isBlank(projectId)) {
			throw new BussinessException(ResourceUtils.get(LoanResource.PROJECT_ID_IS_NULL), BussinessException.TYPE_JSON);
		}
		LOGGER.debug("[Cache] clear  : clearRemainAccount (projectId:{})!", projectId);

		final Map<String, Object> data = renderSuccessResult();
		final Project project = projectService.get(projectId);
		if (project == null) {
			throw new BussinessException(ResourceUtils.get(LoanResource.PROJECT_NOT_EXISTS),
					BussinessException.TYPE_JSON);
		}
		// 清理项目详情缓存
		ProjectCache.setRemainAccount(project.getUuid(), project.getAccount().subtract(project.getAccountYes()).doubleValue());
		return data;
	}
	
}
