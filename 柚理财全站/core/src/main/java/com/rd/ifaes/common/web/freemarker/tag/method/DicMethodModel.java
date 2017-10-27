package com.rd.ifaes.common.web.freemarker.tag.method;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.core.sys.domain.DictData;
import com.rd.ifaes.core.sys.service.DictDataService;

import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;

/**
 * 根据ID获取证件类型 或者获取证件列表(用户类型等)
 * 
 * @author lh 
 * @version 3.0
 * @since 2016-10-25
 */
@Component
public class DicMethodModel implements TemplateMethodModelEx {
	
	@Resource
	private DictDataService dictDataService;

	@SuppressWarnings("rawtypes")
	@Override
	public Object exec(List arg) throws TemplateModelException {
		String nid = "", type = "", value = "";
		if (arg != null && arg.size() == 3) {
			nid = StringUtils.isNull(arg.get(0));
			type = StringUtils.isNull(arg.get(1));
			value = StringUtils.isNull(arg.get(2));
		} else {
			return "Illegal arguments";
		}
		DictData l = null;
		if ("id".equals(type)) {
			l = dictDataService.get(value);
		} else {
			l = dictDataService.get(nid, value);
		}
		return l;
	}

}
