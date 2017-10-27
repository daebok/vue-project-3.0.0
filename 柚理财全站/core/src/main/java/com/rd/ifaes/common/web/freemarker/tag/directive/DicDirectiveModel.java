package com.rd.ifaes.common.web.freemarker.tag.directive;

import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.core.sys.domain.DictData;
import com.rd.ifaes.core.sys.service.DictDataService;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

/**
 * 各种下拉框的html输出
 * 
 * @author fuxingxing usage: <@linkage name="use" id="use" class="test" default="36"  nid="borrow_use"  noselect="不限"
 *         disabled="disabled" type="value"></@linkage>
 * @param name 表单名字,String类型,不能为空
 * @param id 表单的id,String类型，可以为空
 * @param clazz 表单的class,String类型，可以为空
 * @param nid 下拉框的类型,String类型，比如借款用途为borrow_use，对应数据库中的nid
 * @param default 表单的默认值，String类型。
 * @param disabled 表单是否可选，String类型。
 * @param  noselect="不限"
 * @param type 表单的值是否由linkage的Value决定,是则type='value'。
 * @return 返回拼装出来的html字符串
 */
@Component
public class DicDirectiveModel implements TemplateDirectiveModel {
	
	private static final String PLANTEXT_TYPE_TEXT = "plantext";
	private static final String PLANTEXT_TYPE_HREF = "href";
	private static final String PLANTEXT_TYPE_SELECT = "select";
	private static final String PLANTEXT_TYPE_RADIO = "radio";
	private static final String PLANTEXT_TYPE_CHECKBOX = "checkbox";
	

	private static final String NAME = "name";
	private static final String ID = "id";
	private static final String CLASS = "class";
	private static final String DEFAULT = "default";
	private static final String NID = "nid";
	private static final String DISABLED = "disabled";
	private static final String TYPE = "type";
	private static final String NOSELECT = "noselect";
	private static final String PLAINTEXT = "plantext";
	private static final String NOSHOW = "noshow";//下拉框中 这个name对应的值 不做显示
	private static final String ALLSELECT = "allselect";  //复选框默认全选
	
	@Autowired
	private DictDataService dictDataService;
	

	public DicDirectiveModel() {
		super();
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body)
		throws TemplateException, IOException {
		Iterator it = params.entrySet().iterator();
		String name = "", id = "", clazz = "", defaultvalue = "", nid = "", disabled = "", type = "", noselect = "",noshow="",allselect="";
		String plantext = "select";
		while (it.hasNext()) {
			Map.Entry entry = (Entry) it.next();
			String paramName = entry.getKey().toString();
			TemplateModel paramValue = (TemplateModel) entry.getValue();
			if (paramName.equals(PLAINTEXT)) {
				plantext = paramValue.toString();
			}
			if (paramName.equals(NAME)) {
				name = paramValue.toString();
			} else if (paramName.equals(ID)) {
				id = paramValue.toString();
			} else if (paramName.equals(CLASS)) {
				clazz = paramValue.toString();
			} else if (paramName.equals(DEFAULT)) {
				defaultvalue = paramValue.toString();
			} else if (paramName.equals(NID)) {
				nid = paramValue.toString();
			} else if (paramName.equals(DISABLED)) {
				disabled = paramValue.toString();
			} else if (paramName.equals(NOSELECT)) {
				noselect = paramValue.toString();
			} else if (paramName.equals(TYPE)) {
				type = StringUtils.isNull( paramValue.toString());
			} else if(paramName.equals(NOSHOW)){
				noshow = StringUtils.isNull( paramValue.toString());
			} else if(paramName.equals(ALLSELECT)){
				allselect = StringUtils.isNull( paramValue.toString());
			}
		}
		String result = "";
		if (plantext.equals(PLANTEXT_TYPE_TEXT)) {
			result = plaintext(name, id, clazz, defaultvalue, nid, disabled, type, noselect);
		}else if(plantext.equals(PLANTEXT_TYPE_HREF)){
			result = hrefHtml(name, id, clazz, defaultvalue,  nid, disabled, type, noselect);
		} else if(plantext.equals(PLANTEXT_TYPE_RADIO)){
			result = html(name, id, clazz, defaultvalue, nid, disabled, type, noselect,noshow);
		}else if(plantext.equals(PLANTEXT_TYPE_CHECKBOX)){
			result = checkboxHtml(name, id, clazz, defaultvalue, nid, noselect, noshow, allselect);
		}else if(plantext.equals(PLANTEXT_TYPE_SELECT)){
			result = html(name, id, clazz, defaultvalue, nid, disabled, type, noselect,noshow);
		}
		Writer out = env.getOut();
		out.write(result);
	}

	/**
	 * @param name 表单名字,不能为空
	 * @param id 表单的id，可以为空
	 * @param clazz 表单的class，可以为空
	 * @return 返回拼装出来的html字符串
	 */
	private String html(String name, String id, String clazz, String defaultvalue,  String nid,
			String disabled, String type, String noselect,String noshow) {
		List<DictData> list = null;
		if (StringUtils.isNotBlank(nid)) {
			list = dictDataService.findListByDictType(nid);
		} else {
			return "";
		}

		StringBuffer sb = new StringBuffer();
		sb.append("<select name=\"").append(name).append("\" autocomplete=\"off\"");
		if (!id.equals("")) {
			sb.append(" id=\"").append(id).append("\"");
		}
		if (!clazz.equals("")) {
			sb.append(" class=\"").append(clazz).append("\"");
		}
		if (!disabled.equals("")) {
			sb.append(" disabled=\"").append(disabled).append("\"");
		}
		sb.append(">");
		// 没有选择处理
		if (!noselect.equals("")) {
			sb.append("<option value=\"\">" + noselect + "</option>");
		}
		for (DictData l : list) {
			//	String value = String.valueOf(l.getId());
			String value = String.valueOf(l.getItemValue());
			if(noshow.endsWith(value)){//此key值的对应的value不做显示
				continue;
			}
			if ("value".equals(type)) {
				value = l.getItemValue();
			} else if ("label".equals(type)) {
				value = l.getItemName();
			}
			sb.append("<option value=\"").append(value).append("\"");
			if (!defaultvalue.equals("") && defaultvalue.equals(value)) {
				sb.append(" selected=\"selected\" ");
			}
			sb.append(">").append(l.getItemName()).append("</option>");
		}
		sb.append("</select>");
		return sb.toString();
	}

	private String plaintext(String name, String id, String clazz, String defaultvalue,String nid,
			String disabled, String type, String noselect) {
		if(StringUtils.isBlank(defaultvalue)){
			return "";
		}
		DictData l = null;
		if (StringUtils.isNotBlank(nid)) {
			l = dictDataService.get(nid, defaultvalue);
		} else {
			l = dictDataService.get(defaultvalue);
		}
		if (l == null) {
			return "";
		}
		String ret = "";
		if (type.equals("value")) {
			ret = l.getItemValue();
		} else {
			ret = l.getItemName();
		}
		return ret;
	}
	
	/**
	 * @param name 表单名字,不能为空
	 * @param id 表单的id，可以为空
	 * @param clazz 表单的class，可以为空
	 * @return 返回拼装出来的html字符串
	 */
	private String hrefHtml(String name, String id, String clazz, String defaultvalue, String nid,
			String disabled, String type, String noselect) {
		List<DictData> list = null;
		if (StringUtils.isNotBlank(nid)) {
			list = dictDataService.findListByDictType(nid);
		}else{
			return "";
		}
		StringBuffer sb = new StringBuffer();
		
		// 没有选择处理
		if (!noselect.equals("")) {
			sb.append("<a href=\"javascript:;\"  data-val=\"0\">" + noselect + "</a>");
		}
		for (DictData l : list) {
			String value = String.valueOf(l.getUuid());
			if ("value".equals(type)) {
				value = l.getItemValue();
			} else if ("name".equals(type)) {
				value = l.getItemName();
			}
			sb.append("<a href=\"javascript:;\"  data-val=\"").append(value).append("\"");
			sb.append(">").append(l.getItemName()).append("</a>");
		}
		return sb.toString();
	}
	
	/**
	 * @param name 表单名字,不能为空
	 * @param id 表单的id，可以为空
	 * @param clazz 表单的class，可以为空
	 * @return 返回拼装出来的html字符串
	 */
	private String checkboxHtml(String name, String id, String clazz, String defaultvalue, String nid,
			String noselect, String noshow, String allselect) {
		List<DictData> list = null;
		if (StringUtils.isNotBlank(nid)) {
			list = dictDataService.findListByDictType(nid);
		} else {
			return "";
		}

		StringBuffer sb = new StringBuffer();
		// 如果全选则显示全选按钮
		if (ALLSELECT.equals(allselect)) {
			sb.append("<label class=\"checkbox inline\">");
			sb.append("<input type=\"checkbox\" value=\"\" checked>不限");
			sb.append("</label>");
		}
		for (DictData l : list) {
			String value = String.valueOf(l.getItemValue());
			if(noshow.endsWith(value)){//此key值的对应的value不做显示
				continue;
			}
			sb.append("<label class=\"checkbox inline\">");
			sb.append("<input type=\"checkbox\" name=\""+name+"\" value=\""+value+"\" ");
			if ((!defaultvalue.equals("") && defaultvalue.equals(value))||ALLSELECT.equals(allselect)) {
				sb.append(" checked ");
			}
			sb.append(">").append(l.getItemName());
			sb.append("</label>");
		}
		return sb.toString();
	}
}
