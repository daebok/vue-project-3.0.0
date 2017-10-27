package com.rd.ifaes.manage.sys;

import com.rd.ifaes.common.orm.Page;
import com.rd.ifaes.common.util.HttpUtils;
import com.rd.ifaes.core.base.web.BaseController;
import com.rd.ifaes.core.core.constant.ConfigConstant;
import com.rd.ifaes.core.core.util.ConfigUtils;
import com.rd.ifaes.core.sys.constant.RefreshContextConstant;
import com.rd.ifaes.core.sys.domain.Config;
import com.rd.ifaes.core.sys.domain.Operator;

public abstract class SystemController extends BaseController {

	public Operator getUser(){
		Object user = getPrincipal();
		if(user == null) return null;
		return (Operator)user;
	}
	
	public String getUserOrgId(){
		Operator user = getUser();
		if(user == null) return null;
		return user.getOrgId();
	}

	public String getCurrUserName(){
		Operator user = getUser();
		if(user == null) return null;
		return user.getLoginName();
	}
	
	/**
	 * 取得总页数
	 * @param count
	 * @return
	 */
	protected int getTotalPage(int count){
		return ( count%Page.defaultExportPageSize)==0?count/Page.defaultExportPageSize: count/Page.defaultExportPageSize+1;
	}
	
	/**
	 * 清除前台s_config表的缓存信息
	 * @param entity
	 */
	public void doCleanWebCache(Config entity){
		LOGGER.info("清除前台缓存开始");
		String[] urls = ConfigUtils.getValue(ConfigConstant.WEB_REFRESH_URLS).split(",");
		if(urls == null || urls.length == 0){
			return;
		}
		String key = entity.getCode();
		boolean needRefresh = false;
		for (String keyPrefix : RefreshContextConstant.REFRESH_KEY_PREFIX) {
				if(key.startsWith(keyPrefix)){
					needRefresh = true;
					break;
				}
			}
		if(!needRefresh){
			return;
		}
		for (String uri : urls) {    		
			String[][] params = {{"key", entity.getCode()}};    
			String strUrl = uri.concat(RefreshContextConstant.REFRESH_CONTEXT_URL);    		  		
			String strResult = HttpUtils.postClient(strUrl, params);
			LOGGER.info("清除前台缓存，地址：{}，params：{}, 返回结果：{}", strUrl, params, strResult);  
		}    		
	}
	
}
