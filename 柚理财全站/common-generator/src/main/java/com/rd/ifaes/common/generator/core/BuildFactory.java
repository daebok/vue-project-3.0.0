package com.rd.ifaes.common.generator.core;



import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.rd.ifaes.common.generator.Builder;
import com.rd.ifaes.common.generator.config.SetupConfig;
import com.rd.ifaes.common.generator.jdbc.AbstractDaoSupport;
import com.rd.ifaes.common.generator.util.MyUtils;
import com.rd.ifaes.common.generator.util.StringUtil;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * @author lh@erongdu.com
 */
public class BuildFactory {
	private static  Map<String, Map<String, Object>> CACHE = new HashMap<String, Map<String, Object>>();
	private static SetupConfig config = SetupConfig.getInstance();
	private static AbstractDaoSupport dao = AbstractDaoSupport.getInstance();
	
	/**
	 * 配置属性
	 */
	private static Configuration cfg = new Configuration();

	/**
	 * 这里我设置模板的根目录
	 * @param dirPath 目录路径
	 */
	public static void setLoadingDir(String dirPath) {
		try {
			cfg.setDirectoryForTemplateLoading(new File(dirPath));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 根据模板生成文件
	 * 
	 * @param templateFile 
	 * @param obj 
	 * @param outFile 
	 */
	public void build(String templateFile, Object obj, String outFile) {
		try {
			Template t = cfg.getTemplate(templateFile);
			Writer out = new OutputStreamWriter(new FileOutputStream(outFile), "utf-8");
			t.process(obj, out);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TemplateException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * POJO数据准备
	 * @param tableName 
	 * @return Map 
	 */
	public Map<String, Object> getParams(String tableName, String packagePath) {
		if (CACHE.containsKey(tableName)) {
			Map<String, Object> map = CACHE.get(tableName);
			map.put("model_package", MyUtils.buildModelPackage(tableName));
			map.put("package_path", packagePath);
			return map;
		}
		// 数据准备,可以是Map,List或者是实体
		String className = StringUtil.className(tableName.replace(config.getIgnorePrefix(), ""));
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("package_path", packagePath);
		map.put("model_package", MyUtils.buildModelPackage(tableName));
		map.put("table_name", tableName);
		map.put("class_name",  className);//StringUtil.className(tableName);
		List<Column> columns = dao.queryColumns(tableName);
		map.put("table_column", columns);
		map.put("hasDateColumn", Column.typeContains(columns, "Date"));	
		map.put("project", config.getProject());
		map.put("author", config.getAuthor());
		map.put("sysDate", new Date());
		CACHE.put(tableName, map);
		return map;
	}
	
}
