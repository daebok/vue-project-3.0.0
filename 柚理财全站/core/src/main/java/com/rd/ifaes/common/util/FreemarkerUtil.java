package com.rd.ifaes.common.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rd.ifaes.common.web.freemarker.tag.directive.AccountInfoDirectiveModel;
import com.rd.ifaes.common.web.freemarker.tag.directive.DicDirectiveModel;
import com.rd.ifaes.common.web.freemarker.tag.directive.SiteDirectiveModel;
import com.rd.ifaes.common.web.freemarker.tag.method.AttestationTypeNameModel;
import com.rd.ifaes.common.web.freemarker.tag.method.DateMethodModel;
import com.rd.ifaes.common.web.freemarker.tag.method.DateRollMethodModel;
import com.rd.ifaes.common.web.freemarker.tag.method.DicMethodModel;
import com.rd.ifaes.common.web.freemarker.tag.method.InterestMethodModel;
import com.rd.ifaes.common.web.freemarker.tag.method.ParserDoubleMethodModel;
import com.rd.ifaes.common.web.freemarker.tag.method.ParserLongMethodModel;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

/**
 * 工具类-freemarker的模板处理
 * @author lh
 * @version 3.0
 * @date 2016年4月28日
 */
public class FreemarkerUtil {

	private static final String CHARSET_UTF8= "UTF-8";
	private static Configuration config =  new Configuration();
	private static final Logger LOGGER = LoggerFactory.getLogger(FreemarkerUtil.class);
	
	private FreemarkerUtil() {
	}
	
	/**
	 * 模板内容渲染
	 * @param s
	 * @param data
	 * @return
	 * @throws IOException
	 * @throws TemplateException
	 */
	public static String renderTemplate(String s, Map<String, Object> data) {
		initSharedVariables();
		// 执行插值，并输出到指定的输出流中
		StringWriter w = new StringWriter();
		Template t = null;
		try {
			t = new Template(null, new StringReader(s), config);
			t.process(data, w);
		} catch (IOException | TemplateException e1) {
			LOGGER.error(e1.getMessage(), e1);
		}
		return w.getBuffer().toString();
	}
	
	/**
	 * 模板内容渲染
	 * @param templatePath
	 * @param data
	 * @return
	 * @throws IOException
	 * @throws TemplateException
	 */
	public static String renderFileTemplate(String templatePath, Map<String, Object> data) throws IOException, TemplateException {
		// 执行插值，并输出到指定的输出流中
		StringWriter w = new StringWriter();
		getTemplate(templatePath).process(data, w);
		return w.getBuffer().toString();
	}
	
	/**
	 * 模板内容渲染
	 * @param templatePath
	 * @param data
	 * @param savePath
	 * @throws IOException
	 * @throws TemplateException
	 */
	public static void renderFileTemplate(String templatePath, Map<String, Object> data, String savePath)throws IOException, TemplateException {
		FileOutputStream fos = null;
		Writer out = null;
		try {
			fos = new FileOutputStream(new File(savePath));
			OutputStreamWriter osw = new OutputStreamWriter(fos, CHARSET_UTF8);
			out = new BufferedWriter(osw);
			getTemplate(templatePath).process(data,out);
			
		} finally{
			if(out!=null){
				out.close();
			}
			if(fos!=null){
				fos.close();
			}
		}
	}
	
	/**
	 * 模板内容渲染
	 * @param templatePath
	 * @return
	 * @throws IOException
	 */
	private static Template getTemplate(String templatePath) throws IOException{
		Configuration cfg = config;
		String path = templatePath.substring(0, templatePath.lastIndexOf(File.separator));
		String name = templatePath.substring(templatePath.lastIndexOf(File.separator) + 1 );
		// 加载模板文件
		cfg.setClassForTemplateLoading(FreemarkerUtil.class, path);
		// 设置对象包装器
		cfg.setObjectWrapper(new DefaultObjectWrapper());
		// 设置异常处理器
		cfg.setTemplateExceptionHandler(TemplateExceptionHandler.IGNORE_HANDLER);
		return cfg.getTemplate(name);
	}
	
	private static void initSharedVariables(){
		if(config.getSharedVariable("dateformat") == null){
			config.setSharedVariable("dateformat", SpringContextHolder.getBean(DateMethodModel.class));
			config.setSharedVariable("dateroll", SpringContextHolder.getBean(DateRollMethodModel.class));
			config.setSharedVariable("interest", SpringContextHolder.getBean(InterestMethodModel.class));
			// 新增自定义标签
			config.setSharedVariable("linkage", SpringContextHolder.getBean(DicDirectiveModel.class));
			
			//cfg.setSharedVariable("attestation", new AttestationDirectiveModel());
			config.setSharedVariable("Typet", SpringContextHolder.getBean(AttestationTypeNameModel.class));
			config.setSharedVariable("siteDirect", SpringContextHolder.getBean(SiteDirectiveModel.class));
			
			config.setSharedVariable("getLinkage", SpringContextHolder.getBean(DicMethodModel.class));
			config.setSharedVariable("parseDouble", SpringContextHolder.getBean(ParserDoubleMethodModel.class));
			config.setSharedVariable("parseLong", SpringContextHolder.getBean(ParserLongMethodModel.class));
			config.setSharedVariable("accountType", SpringContextHolder.getBean(AttestationTypeNameModel.class));
			config.setSharedVariable("accountInfo", SpringContextHolder.getBean(AccountInfoDirectiveModel.class));			
		}
		config.setDefaultEncoding(CHARSET_UTF8);
	}

}
