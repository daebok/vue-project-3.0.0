package com.rd.ifaes.common.web.freemarker.tag.directive;

import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.rd.ifaes.common.dict.CommonEnum;
import com.rd.ifaes.core.column.domain.Section;
import com.rd.ifaes.core.column.service.SectionService;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

/**
 * 各种下拉框的html输出
 * 
 * @author lh 
 * @usage: <@site name="pid" value=1></@site>
 * @param name 表单名字,String类型,不能为空
 * @param id 表单的id,String类型，可以为空
 * @param clazz 表单的class,String类型，可以为空
 * @param pid 选择地区的父编码,Number类型
 * @param value 表单的默认值，String类型。
 * @return 返回拼装出来的html字符串
 */
@Component
public class SiteDirectiveModel implements TemplateDirectiveModel {

	private static Logger logger = LoggerFactory.getLogger(SiteDirectiveModel.class);

	private static final String NAME = "name";
	private static final String ID = "id";
	private static final String CLASS = "class";
	private static final String PID = "pid";
	private static final String DEFUALT = "default";

	@Resource
	private SectionService sectionService;

	public SiteDirectiveModel() {
		super();
	}

	@SuppressWarnings({ "rawtypes", "unused" })
	@Override
	public void execute(Environment env, Map map, TemplateModel[] loopVars, TemplateDirectiveBody body)
		throws TemplateException, IOException {
		Iterator it = map.entrySet().iterator();
		String name = "", id = "", clazz = "", value = "", pid = "", defaultValue = "";
		while (it.hasNext()) {
			Map.Entry entry = (Entry) it.next();
			String paramName = entry.getKey().toString();
			TemplateModel paramValue = (TemplateModel) entry.getValue();
			logger.debug("name:" + paramName);
			logger.debug("r:" + paramValue);
			if (paramName.equals(NAME)) {
				name = paramValue.toString();
			} else if (paramName.equals(ID)) {
				id = paramValue.toString();
			} else if (paramName.equals(CLASS)) {
				clazz = paramValue.toString();
			} else if (paramName.equals(PID)) {
				pid = paramValue.toString();
			} else if (paramName.equals(DEFUALT)) {
				defaultValue = paramValue.toString();
			}
		}
		String result = html(name, defaultValue);
		Writer out = env.getOut();
		out.write(result);
	}

	/**
	 * @return 返回拼装出来的html字符串
	 */
	private String html(String name, String value) {
		Section model = new Section();
		model.setDeleteFlag(CommonEnum.NO.getValue());
		List<Section> list = sectionService.findList(model);
		StringBuffer sb = new StringBuffer();
		sb.append("<select name=\"").append(name).append("\">");
		sb.append("<option value=\"\">请选择</option>");
		for (Section s : list) {
			sb.append(option(s.getSectionCode(), s.getSectionName(), value));
		}
		sb.append("</select>");
		return sb.toString();
	}

	private String option(String code, String name, String value) {
		StringBuffer sb = new StringBuffer();
		sb.append("<option value=\"").append(code).append("\"");
		if (value.equals(code)) {
			sb.append(" selected=\"selected\" ");
		}
		sb.append(">").append(name).append("</option>");
		return sb.toString();
	}

}
