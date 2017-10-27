package com.rd.ifaes.web.controller;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Maps;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.core.base.web.BaseController;
import com.rd.ifaes.core.core.constant.ResourceConstant;
import com.rd.ifaes.core.core.util.CacheUtils;
import com.rd.ifaes.core.core.util.DictUtils;
import com.rd.ifaes.core.core.util.ResourceUtils;
import com.rd.ifaes.core.sys.domain.DictData;

@Controller
public class PublicController extends BaseController {
	
	/**
	 * 获得下拉框选择
	 */
	@RequestMapping(value = "/public/getSelectList")
	@ResponseBody
	public Object getLogList(final String dictType){
		List<DictData> dictDataList = null;
		if(!StringUtils.isBlank(dictType)){
			dictDataList = DictUtils.list(dictType);
		}
		return dictDataList;
	}
	
	/* 返回系统处理的信息
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/public/getResult")
	@ResponseBody
	public Object getResult(String resultFlag) throws Exception{
		String result = "";
		if(StringUtils.isBlank(resultFlag)){
			result=ResourceUtils.get(ResourceConstant.GLOBAL_MSG_FAIL);
		}else{
			result = CacheUtils.getStr(resultFlag);
			if(StringUtils.isNotBlank(result)){ //取出数据就删除缓存
				CacheUtils.del(resultFlag);
			}
		}
		Map<String,String> map = Maps.newHashMap();
		map.put("msg_data", result);
		return map;
	}
}
