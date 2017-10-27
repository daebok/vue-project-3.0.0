package com.rd.ifaes.core.protocol.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.WritableDirectElement;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.itextpdf.tool.xml.ElementHandler;
import com.itextpdf.tool.xml.Writable;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.itextpdf.tool.xml.pipeline.WritableElement;
import com.rd.ifaes.core.core.util.PdfHelper;
import com.rd.ifaes.core.protocol.util.bond.BondProtocol;
import com.rd.ifaes.core.protocol.util.bond.BondProtocolBean;
import com.rd.ifaes.core.protocol.util.invest.AbstractProtocolBean;
import com.rd.ifaes.core.protocol.util.invest.InvestProtocol;

/**
 * 
 *  
 * 协议基类
 *  
 */
public class ProtocolHelper {
	private static final Logger LOGGER = LoggerFactory.getLogger(ProtocolHelper.class);

	public static void init() {
		Properties prop = new Properties();
		InputStream in = ProtocolHelper.class.getResourceAsStream("protocol.properties");
		try {
			prop.load(in);
			Set<Object> keySet = prop.keySet();
			LOGGER.debug("系统指定初始化protocolBean:.......");
			for (Object object : keySet) {
				String key = object.toString();
				String value = prop.getProperty(key).trim();
				Class.forName(value).newInstance();
			}
		} catch (Exception e) {
			LOGGER.error("初始化系统指定ProtocolBean报错..", e);
		}
	}

	/**
	 * 根据key获取，逻辑bean
	 * 
	 * @param key
	 * @return
	 */
	public static AbstractProtocolBean doProtocol(String key) {
		AbstractProtocolBean bean = new InvestProtocol();
		return bean;
	}
	
	/**
	 * 根据key获取，逻辑bean
	 * 
	 * @param key
	 * @return
	 */
	public static BondProtocolBean doBondProtocol(String key) {
		BondProtocolBean bean = new BondProtocol();
		return bean;
	}
	
	/**
	 * 
	 * @param str
	 * @param pdf
	 * @return
	 * @throws IOException
	 * @throws DocumentException
	 */
	public static String templateHtml(String str, PdfHelper pdf) throws IOException, DocumentException {
		final List<Element> pdfeleList = new ArrayList<Element>();
		ElementHandler elemH = new ElementHandler() {
			public void add(final Writable w) {
				if (w instanceof WritableElement) {
					pdfeleList.addAll(((WritableElement) w).elements());
				}
			}
		};
		ByteArrayInputStream isr = new ByteArrayInputStream(str.getBytes("UTF-8"));
		XMLWorkerHelper.getInstance().parseXHtml(elemH, isr, Charset.forName("UTF-8"));
		List<Element> list = new ArrayList<Element>();
		for (Element ele : pdfeleList) {
			if (ele instanceof LineSeparator || ele instanceof WritableDirectElement) {
				continue;
			}
			list.add(ele);
		}
		pdf.addHtmlList(list);
		return "";
	}
	
	/**
	 * 文件输出html然后生成PDF
	 * @author QianPengZhan
	 * @date 2016年10月10日
	 * @param inPdfName
	 * @param out
	 * @param pdfHelper
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static void fileHtml(final String inPdfName,final String out,final PdfHelper pdfHelper) throws FileNotFoundException, IOException{
		//创建临时html文件
		File htmlFile = pdfHelper.createHtml(inPdfName, out);
		//将html生成PDF
		pdfHelper.createPdf(inPdfName.replace(".pdf", ".html"));
		//删除旧的html
		if(htmlFile.exists()){
			boolean result = htmlFile.delete();
			LOGGER.info("删除html结果：{}", result);
		}else{
			LOGGER.info("html已删除或不存在");
		}
	}
	
}
