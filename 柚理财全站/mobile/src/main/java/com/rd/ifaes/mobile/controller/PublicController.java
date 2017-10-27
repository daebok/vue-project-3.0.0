package com.rd.ifaes.mobile.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.core.base.web.BaseController;
import com.rd.ifaes.core.core.util.DictUtils;
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
}
