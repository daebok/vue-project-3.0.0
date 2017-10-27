package com.rd.ifaes.common.web.freemarker.tag.directive;

import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.rd.ifaes.core.sys.domain.DictData;
import com.rd.ifaes.core.sys.service.DictDataService;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

/**
 * 数据字典格式化自定义标签 在freemarker配置XML中定义为：
 * 
 * <pre>
 *        <entry key="dictFormatter" value-ref="dictFormatterDirective" />
 * </pre>
 * 
 * 那么在页面使用例子：
 * 
 * <pre>
 *    <script type="text/javascript"> 
 *         <@dictFormatter type="sex" />
 *         ........     
 *           //jqgrid colModel formatter值为自定义标签中的type+"Formatter"
 *          {name:'sex',label:'性别',sortable:false,formatter:sexFormatter}  
 *           
 *  </script>
 * </pre>
 * 
 * @author FangJun
 * 
 */
@Component
public class DictFormatterDirective implements TemplateDirectiveModel {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(DictFormatterDirective.class);
	@Autowired
	private DictDataService dictDataService;

	private String type;

	@Override
	public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body)
			throws TemplateException, IOException {
		type = params.get("type").toString();
		if (StringUtils.isBlank(type)) {
			return;
		}
		// 查询字段指定分类所有条目
		List<DictData> list = dictDataService.findListByDictType(type);
		Writer out = env.getOut();
		StringBuilder buffer = new StringBuilder();
		buffer.append(getArrayData(list));
		buffer.append(createFormatterFunction());
		try {
			out.write(buffer.toString());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}

	}

	private String getArrayData(List<DictData> list) {
		if (list != null && !list.isEmpty()) {
			StringBuilder buffer = new StringBuilder(list.size() <= 1 ? 16 : list.size() * 10);
			String  objectName = type + "Data_";
            	 buffer.append("var " + objectName + " ={ \n");
            	 for (DictData dict : list) {
						buffer.append("\t\"").append(dict.getItemValue()).append("\" : \"").append(dict.getItemName())
								.append("\",\n");
					}
            	 buffer.deleteCharAt(buffer.length()-2);
            	 buffer.append("\n  } ; \n");
             return buffer.toString();
		}
		return "";
	}

	private String createFormatterFunction() {
		StringBuilder buffer = new StringBuilder(50);
		String objectName = type + "Data_";
		buffer.append("function ").append(type).append("Formatter(cellvalue,options,rowData){\n");
		// 默认值为空字符串
		buffer.append("\t if(cellvalue == null){\n");
		buffer.append("\t\t return \"&nbsp;\"\n");
		buffer.append("\t }\n");
		buffer.append("\t var label= ").append(objectName).append("[cellvalue];\n");
		buffer.append("\t if(!label) { \n");
		buffer.append("\t\t return cellvalue ; \n");
		buffer.append("\t }  \n");
		buffer.append("\t return label;\n ");
		buffer.append("}\n");
		return buffer.toString();
	}
}
