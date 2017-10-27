package com.rd.ifaes.core.core.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPRow;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.rd.ifaes.common.util.SpringContextHolder;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.core.core.constant.Constant;

public class PdfHelper {
	/**
	 * 日志
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(PdfHelper.class);
	
	private Document document;
	private BaseFont bfChinese;
	private PdfWriter writer;
	private Font font;

	public PdfHelper(String path) {
		document = new Document();
		try {
			writer = PdfWriter.getInstance(document, new FileOutputStream(path));// 建立一个PdfWriter对象
			document.open();
			bfChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);// 设置中文字体
			font = new Font(bfChinese, 12, Font.NORMAL);// 设置字体大小
		} catch (DocumentException de) {
			LOGGER.error("pdf的de错误：{}",de.toString());
		} catch (IOException ioe) {
			LOGGER.error("pdf的ioe错误：{}",ioe.toString());
		}
	}
	
	/**
	 * 获取PDF分页的页数
	 * @author QianPengZhan
	 * @date 2016年10月10日
	 * @return
	 */
	public int getPageNumber(){
		if(document==null){
			return Constant.INT_ZERO;
		}
		return document.getPageNumber();
	}
	
	/**
	 * PDF工具类 初始化
	 * @author QianPengZhan
	 * @date 2016年10月10日
	 * @param path
	 * @return
	 */
	public static PdfHelper instance(String path) {
		return new PdfHelper(path);
	}
	
	/**
	 * 关闭文件流
	 * @author QianPengZhan
	 * @date 2016年10月10日
	 */
	public void exportPdf() {
		document.close();
	}
	
	/**
	 * 将元素list添加到pdf中
	 * @author QianPengZhan
	 * @date 2016年10月10日
	 * @param list
	 * @throws DocumentException
	 */
	public void addHtmlList(List<Element> list) throws DocumentException {
		for (int k = 0; k <list.size(); k++) {
			Element e = list.get(k);
			if(isChinese(e.toString())){
				final String eValue = e.toString().replace("[", StringUtils.EMPTY)
						.replace("]", StringUtils.EMPTY).replace(",", StringUtils.EMPTY);
				Paragraph paragraph;
				Font bondFont = new Font(bfChinese, 16, Font.BOLD);// 设置字体大小
				if(k==0){
					paragraph = new Paragraph(eValue,bondFont);
					paragraph.setAlignment(Element.ALIGN_CENTER);
				}else{
					paragraph = new Paragraph(eValue,font);
				}
				document.add(paragraph);
			}else if(e instanceof PdfPTable){
				document.add(this.getPdfpTable(e));
			}else{
				document.add(e);
			}
			
		}
	}
	
	/**
	 * 若元素为table的时候处理
	 * @author QianPengZhan
	 * @date 2016年10月11日
	 * @param e
	 * @return
	 */
	private PdfPTable getPdfpTable(final Element e){
		final PdfPTable pdfTable = (PdfPTable)e;
		if(pdfTable == null){
			return pdfTable;
		}else{
			List<PdfPRow> rowList = pdfTable.getRows();
			float totalCellWidth= 500 ;
			int num = pdfTable.getNumberOfColumns();
			float cellWidth =  totalCellWidth / num;
			float[] widths = new float[num];
			for (int i = 0; i < widths.length; i++) {
				widths[i] = cellWidth;
			}
			PdfPTable table = new PdfPTable(widths);
			table.setSpacingBefore(10f);// 设置表格上面空白宽度
			table.setSpacingBefore(10f);
			table.setTotalWidth(totalCellWidth);// 设置表格的宽度
			table.setLockedWidth(true);// 设置表格的宽度固定
			PdfPCell cell = null;
			if(CollectionUtils.isNotEmpty(rowList)){
				for(PdfPRow pdfProw :rowList){
					for (int j = 0; j < pdfProw.getCells().length; j++) {
						List<Element> eList = pdfProw.getCells()[j].getColumn().getCompositeElements();
						String value = StringUtils.EMPTY;
						if(CollectionUtils.isNotEmpty(eList)){
							for(Element ee :eList){
								value = ee.toString().replace("[", StringUtils.EMPTY)
										.replace("]", StringUtils.EMPTY).replace(",", StringUtils.EMPTY);
							}
						}
						Paragraph cellPara = new Paragraph(value, font);
						cellPara.setAlignment(Element.ALIGN_CENTER);
						cell = new PdfPCell(cellPara);
						table.addCell(cell);
					}
				}
			}
			return table;
		}
	}
	
	/**
	 * 判斷字節是否是中文
	 * @author QianPengZhan
	 * @date 2016年10月10日
	 * @param c
	 * @return
	 */
	private static final boolean isChinese(char c) {
 		Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
 		if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
 				|| ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
 				|| ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
 				|| ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
 				|| ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
 				|| ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
 			return true;
 		}
 		return false;
 	}
	
	/**
	 * 判斷字符串是否是中文
	 * @author QianPengZhan
	 * @date 2016年10月10日
	 * @param strName
	 * @return
	 */
	public static final boolean isChinese(String strName) {
 		char[] ch = strName.toCharArray();
 		for (int i = 0; i < ch.length; i++) {
 			char c = ch[i];
 			if (isChinese(c)) {
 				return true;
 			}
 		}
 		return false;
 	}
	
	/**
	 * 文件形式获取html模板然后输出模板生成pdf
	 * @author QianPengZhan
	 * @date 2016年10月10日
	 * @param htmlPath
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public void createPdf(final String htmlPath) throws FileNotFoundException, IOException{
		 XMLWorkerHelper.getInstance().parseXHtml(writer, document,
	                new FileInputStream(htmlPath), Charset.forName("UTF-8"));
	}
	
	/**
	 * 生成html
	 * @author QianPengZhan
	 * @date 2016年10月10日
	 * @param inPdfName
	 */
	public File createHtml(final String inPdfName,final String out){
		final File htmlFile = new File(inPdfName.replace(".pdf", ".html"));// 新建一个文件对象
		FileWriter fileWriter;
		try {
			fileWriter = new FileWriter(htmlFile);// 新建一个FileWriter
			fileWriter.write(out);// 将字符串写入到指定的路径下的文件中
			fileWriter.close();
		} catch (IOException e) {
			LOGGER.error("生成html出错,{}", e.getMessage(), e);
		}
		return htmlFile;
	}

	/*public void appendImage(final String pdfPath) {
		try {
			PdfReader reader = new PdfReader(pdfPath);  
			PdfStamper stamp = new PdfStamper(reader, new FileOutputStream());  
			Image image = Image.getInstance(IOUtils.toByteArray(new FileInputStream(SpringContextHolder.class.getClassLoader().getResource("/").getPath() + "protocolpdf/jyylc.png")));
		} catch (Exception e) {
			LOGGER.error("pdf添加水印出错,{}", e.getMessage(), e);
		}  
		
		
	}*/
	
	
}
