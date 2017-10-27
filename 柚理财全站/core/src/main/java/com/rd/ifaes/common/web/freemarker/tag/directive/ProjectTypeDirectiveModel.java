/**
* 互联网金融交易系统ifaes (Internet Finance  Assets  Exchange  System)
 * Copyright (c) 2016 杭州融都科技股份有限公司 版权所有
 */
package com.rd.ifaes.common.web.freemarker.tag.directive;

import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.core.project.domain.ProjectType;
import com.rd.ifaes.core.project.service.ProjectTypeService;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

/**
 * projectType 的ul li 的菜单栏  freemarker
 * 返回拼装出来的html字符串
 * @version 3.0manma
 * @author QianPengZhan
 * @date 2016年10月11日  
 */
@Component
public class ProjectTypeDirectiveModel implements TemplateDirectiveModel{
	/**
	 * 日志
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(ProjectTypeDirectiveModel.class);
	/**
	 * ProjectTypeService业务
	 */
	@Autowired
	private transient ProjectTypeService projectTypeService;
	
	/** class*/
	private String className;
	/** projectTypeId*/
	private String projectTypeId;
	/**额外的标题*/
	private String extraName;
	/**额外的链接*/
	private String extraUrl;
	
	@SuppressWarnings("rawtypes")
	@Override
	public void execute(Environment env, Map params, TemplateModel[] loopVars,
			TemplateDirectiveBody body) throws TemplateException, IOException {
		className = params.get("class").toString();
		if (StringUtils.isBlank(className)) {
			return;
		}
		projectTypeId = params.get("projectTypeId").toString();
		if (StringUtils.isBlank(projectTypeId)) {
			return;
		}
		extraName = StringUtils.isNull(params.get("extraName")).toString();
		extraUrl =  StringUtils.isNull(params.get("extraUrl")).toString();
		final List<ProjectType>  list = projectTypeService.findChilds(projectTypeId);
		Writer out = env.getOut();
		StringBuilder buffer = new StringBuilder();
		buffer.append(getArrayData(list));
		try {
			out.write(buffer.toString());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
	}

	
	/**
	 *	拼接
	 * @author QianPengZhan
	 * @date 2016年10月11日
	 * @param list
	 * @return
	 */
	private String getArrayData(final List<ProjectType> list) {
		if (list != null && !list.isEmpty()) {
			 StringBuilder buffer = new StringBuilder();
			 buffer.append("\t\t\t\t\t<ul  class='").append(this.className).append("'>\n");
			 for (ProjectType projectType : list) {
				 buffer.append("\t\t\t\t\t\t<li><a href='/invest/index.html?projectTypeId=").append(projectType.getUuid())
				 .append("'>").append(projectType.getTypeName()).append("</a></li>\n");
			 }
			 if(StringUtils.isNotBlank(extraName)){
				 if(extraName.contains(",")){
					 String[] extraNameArray = extraName.split(",");
					 for (int i = 0; i < extraNameArray.length; i++) {
						 if(extraUrl.contains(",")){
							 String[] extraUrlArray = extraUrl.split(",");
							 buffer.append("\t\t\t\t\t\t<li><a href='").append(extraUrlArray[i]).append("'>").append(extraNameArray[i]).append("</a></li>\n");
						 }else{
							 buffer.append("\t\t\t\t\t\t<li><a href='").append(extraUrl).append("'>").append(extraNameArray[i]).append("</a></li>\n");
						 }
					}
				 }else{
					 buffer.append("\t\t\t\t\t\t<li><a href='").append(extraUrl).append("'>").append(extraName).append("</a></li>\n");
				 }
			 }
			 buffer.append("\t\t\t\t\t</ul>\n");
	         return buffer.toString();
		}
		return "";
	}
	
	
	
	
}
