package com.rd.ifaes.common.web.freemarker.tag.method;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

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
public class AttestationTypeNameModel implements TemplateMethodModelEx {
	
	@Resource
	private DictDataService dictDataService;
	
	@SuppressWarnings("rawtypes")
	@Override
	public Object exec(List arg0) throws TemplateModelException {
		
		if (arg0 != null && arg0.size() > 1) {
			if (arg0.get(0)!= null && arg0.get(0).toString().equals("list")) {
				if (arg0.get(1)!=null && arg0.get(1).toString().equals("usertype")) {
					return null;
					// return userDao.getAllUserType();
				}
			}
			if (arg0.get(1)!=null && arg0.get(1).toString().equals("name")) {
				DictData dic = dictDataService.get(arg0.get(0).toString());
				if (dic != null) {
					return dic.getItemName();
				}
				return "";
			}
			if (arg0.get(1)!=null && arg0.get(1).toString().equals("area")) {
				// return dictDao.getAreaByPid(arg0.get(0).toString());
				return null;
			}
			if (arg0.get(1)!=null && arg0.get(1).toString().equals("usertype")) {
				return null;
				// return userDao.getUserTypeByid(arg0.get(0).toString());
			}
			if (arg0.get(1) != null && arg0.get(1).toString().equals("account_type")) {
				String value = arg0.get(0).toString();				
				return dictDataService.get(arg0.get(1).toString(), value);
			}
		}
		return "-";
	}

}
