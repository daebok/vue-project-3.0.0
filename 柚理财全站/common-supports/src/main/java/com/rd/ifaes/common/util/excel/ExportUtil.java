package com.rd.ifaes.common.util.excel;

import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.PropertyUtilsBean;
import org.apache.commons.collections.CollectionUtils;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rd.ifaes.common.dict.ExpireTime;
import com.rd.ifaes.common.entity.BaseEntity;
import com.rd.ifaes.common.exception.BussinessException;
import com.rd.ifaes.common.orm.Page;
import com.rd.ifaes.common.util.BigDecimalUtils;
import com.rd.ifaes.common.util.DateUtils;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.core.core.util.CacheUtils;

/**
 * excel 导出工具类
 * @author lh
 * @version 3.0
 * @since 2016-11-8
 *
 */
@SuppressWarnings("rawtypes")
public class ExportUtil {
	private static final Logger LOGGER = LoggerFactory.getLogger(ExportUtil.class);
	
	private static final int sheetMaxCount = 1000000;//单个sheet最多写入行数
	private static final int cacheUpdateCount = 100;//每多少条记录更新一次缓存进度
	
	public static String  makeCacheKey(String url,String cacheKey){
		if(StringUtils.isBlank(url)){
			throw new IllegalArgumentException("url can not be null!");
		}
		if(StringUtils.isBlank(cacheKey)){
			throw new IllegalArgumentException("cacheKey can not be null!");
		}
		return url.replaceFirst("/", "").replaceAll("/", ":").replace(".html", "").concat(cacheKey);
	}
    @SuppressWarnings("unchecked")
	public static<T extends BaseEntity > void exportExcel(ExportModel<T>  exportModel,
			    final HttpServletRequest request, final HttpServletResponse response) throws IOException {
		if(exportModel == null ){
			throw new IllegalArgumentException("export model can not be null!");
		}
		final String cacheKey = makeCacheKey(request.getRequestURI(), exportModel.getCacheKey());
		CacheUtils.setDouble(cacheKey, 0d, ExpireTime.FIVE_MIN);
		final T model=exportModel.getModel();
		 Page<T> page=model.getPage();
	     if(page==null){
		 	page=new Page<T>();
		 	model.setPage(page);
		 }
		page.setExport(true);
		page.setPageSize(Page.defaultExportPageSize);
		int count = exportModel.getTotal(model);
		page.setCount(count);
		 
		Workbook wb =  ExportUtil.createWorkbook();
		if(count == 0){
			Sheet sheet = wb.createSheet(model.getExportTitle() + "_1");
			sheet = wb.getSheetAt(0); 
			sheet.setDisplayGridlines(false);// 设置表标题是否有表格边框
			createHeader(sheet, model.getExportTitle(), model.getHearders());	
		}
		for (int i = 0; i <page.getTotalPage(); i++) {
			page.setPage(i+1);
			page.setRows(null);
			ExportUtil.exportExcel(cacheKey,count,model.getExportTitle(), model.getHearders(), 
					model.getFields(), i * Page.defaultExportPageSize, wb,exportModel.getPageRecords(model));
		}
		
		ExportUtil.responseWorkbook(model.getExportTitle(), wb, request, response);
		
		CacheUtils.setDouble(cacheKey, 100d, ExpireTime.FIVE_SEC);
	}
	
	private static   void exportExcel(String cacheKey,int count, String title, String[] headers,
		    	String[] fields, int startRow, Workbook wb, List<Object>  data) throws IOException {
		if(headers == null || headers.length == 0){
			throw new BussinessException("export headers can not be be null");
		}
		Sheet sheet = null;
		if(CollectionUtils.isEmpty(data)){
			if(startRow == 0){
				sheet = wb.createSheet(title + "_1");
				sheet = wb.getSheetAt(0); 
				sheet.setDisplayGridlines(false);// 设置表标题是否有表格边框
				createHeader(sheet, title, headers);
			}
			return ;
		}
		startRow = startRow>0?startRow+2:startRow;
		int index = startRow, pageRowNo = startRow, columnCount = headers.length; // 行号、页码、列数
		
		//字典类型
		Map<String, String> dictKvs = DictTypeMap.getDictShowValues(data.get(0));
		for (Object obj : data) {
			int sheetIndex = index/sheetMaxCount;
			if (index % sheetMaxCount == 0) {
				sheet = wb.createSheet(title + "_" + (sheetIndex + 1));
				sheet = wb.getSheetAt(sheetIndex); 
				sheet.setDisplayGridlines(false);// 设置表标题是否有表格边框
				pageRowNo = 2; 
				createHeader(sheet, title, headers);				
			}else{
				sheet = wb.getSheetAt(sheetIndex); 
			}
			if(index % cacheUpdateCount ==0){
			 	CacheUtils.setDouble(cacheKey, BigDecimalUtils.decimal(BigDecimal.valueOf(index*100d/count), 0).doubleValue(), ExpireTime.FIVE_MIN);
			}
			index++;
			@SuppressWarnings("unchecked")
			Map<String, Object> map = obj instanceof Map ? (Map<String, Object>) obj : beanToMap(obj);	
			Row nRow = sheet.createRow(pageRowNo++); // 新建行对象	
			for (int j = 0; j < columnCount; j++) {
				Cell cell = nRow.createCell(j);
				String field = fields[j];
				Object val = null;
				if(dictKvs != null && dictKvs.containsKey(field)){
					Object valObj = map.get(field);
					val = dictKvs.get(field+":"+StringUtils.isNull(valObj));
					if(val==null){
						val = StringUtils.isNull(valObj);	
					}					
				}else{
					val = map.get(field);
				}
				setCellValue(sheet, cell, val);
			}			
		}
		
	}
	
	/**
	 * responseWorkbook
	 * @param title
	 * @param wb
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	private static void responseWorkbook(String title, Workbook wb,HttpServletRequest request, HttpServletResponse response)throws IOException{
		String sFileName = title + ".xlsx";
		// 火狐浏览器导出excel乱码
		String agent = request.getHeader("User-Agent");
		// 判断是否火狐浏览器
		boolean isFirefox = agent != null && agent.contains("Firefox");
		if (isFirefox) {
			sFileName = new String(sFileName.getBytes("UTF-8"), "ISO-8859-1");
		} else {
			sFileName = URLEncoder.encode(sFileName, "UTF8");
		}
		response.setHeader("Content-Disposition", "attachment; filename=".concat(sFileName));
		response.setHeader("Connection", "close");
		response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");  
		//response.setHeader("Content-Type", "application/vnd.ms-excel");
		wb.write(response.getOutputStream());
	}
	/**
	 * 设置单元格的值
	 * @param cell
	 * @param cellVal
	 */
	private static void setCellValue(Sheet sheet, Cell cell, Object cellVal){
		if(cellVal == null || String.class.equals(cellVal.getClass())){
			cell.setCellValue(StringUtils.isHref(cellVal));
		}else if(Integer.class.equals(cellVal.getClass()) || int.class.equals(cellVal.getClass())){
			cell.setCellValue(Integer.valueOf(cellVal.toString()));
		}else if(Long.class.equals(cellVal.getClass()) || long.class.equals(cellVal.getClass())){
			cell.setCellValue(Integer.valueOf(cellVal.toString()));
		}else if(Double.class.equals(cellVal.getClass()) || double.class.equals(cellVal.getClass())){
			cell.setCellValue(Double.valueOf(cellVal.toString()));
		}else if(Float.class.equals(cellVal.getClass()) || float.class.equals(cellVal.getClass())){
			cell.setCellValue(Float.valueOf(cellVal.toString()));
		}else if(BigDecimal.class.equals(cellVal.getClass())){
			cell.setCellValue(BigDecimalUtils.round(cellVal.toString()).doubleValue());
		}else if(Date.class.equals(cellVal.getClass())){
			cell.setCellValue(DateUtils.formatDateTime((Date)cellVal));
		}else if(Timestamp.class.equals(cellVal.getClass())){
			cell.setCellValue(DateUtils.formatDateTime((Timestamp)cellVal));
		}else{
			cell.setCellValue(StringUtils.isNull(cellVal));
		}
		cell.setCellStyle(sheet.getWorkbook().getCellStyleAt(3));
	}
	
	/**
	 * JavaBean转Map
	 * 
	 * @param obj
	 * @return
	 */
	private static Map<String, Object> beanToMap(Object obj) {
		Map<String, Object> params = new HashMap<>(0);
		try {
			PropertyUtilsBean propertyUtilsBean = new PropertyUtilsBean();
			PropertyDescriptor[] descriptors = propertyUtilsBean.getPropertyDescriptors(obj);
			for (int i = 0; i < descriptors.length; i++) {
				String name = descriptors[i].getName();
				if (!StringUtils.equals(name, "class")) {
					params.put(name, propertyUtilsBean.getNestedProperty(obj, name));
				}
			}
		} catch (Exception e) {
			LOGGER.error("URLDecoder fail :", e);
		}
		return params;
	}
	
	/**
	 * 创建表头
	 * @param sheet
	 * @param headers
	 */
	private static void createHeader(Sheet sheet, String title, String[] headers){
		
		//设置标题
		Row tRow = sheet.createRow(0);
		Cell hc = tRow.createCell(0);
		hc.setCellValue(new XSSFRichTextString(title));
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, headers.length - 1));// 合并标题行：起始行号，终止行号， 起始列号，终止列号
		hc.setCellStyle(sheet.getWorkbook().getCellStyleAt(1));
		
		//设置表头
		Row nRow = sheet.createRow(1);
		for (int i = 0; i < headers.length; i++) {
			Cell cell =	nRow.createCell(i);
			cell.setCellValue(headers[i]);
			cell.setCellStyle(sheet.getWorkbook().getCellStyleAt(2));
		}
	}
	
	
	/**
	 * 创建Workbook
	 * @return
	 */
	private static Workbook createWorkbook(){
		Workbook wb = new SXSSFWorkbook(100);
		CellStyle hcs = wb.createCellStyle();
		hcs.setBorderBottom(BorderStyle.THIN);
		hcs.setBorderLeft(BorderStyle.THIN);
		hcs.setBorderRight(BorderStyle.THIN);
		hcs.setBorderTop(BorderStyle.THIN);
		hcs.setAlignment(HorizontalAlignment.CENTER);
		Font hfont = wb.createFont();
		hfont.setFontName("宋体");
		hfont.setFontHeightInPoints((short) 16);// 设置字体大小
		hfont.setBold(true);// 加粗
		hcs.setFont(hfont);
		
		CellStyle tcs = wb.createCellStyle();
		tcs.setBorderBottom(BorderStyle.THIN);
		tcs.setBorderLeft(BorderStyle.THIN);
		tcs.setBorderRight(BorderStyle.THIN);
		tcs.setBorderTop(BorderStyle.THIN);
		Font tfont = wb.createFont();
		tfont.setFontName("宋体");
		tfont.setFontHeightInPoints((short) 12);// 设置字体大小
		tfont.setBold(true);// 加粗
		tcs.setFont(tfont);
		
		CellStyle cs = wb.createCellStyle();
		cs.setBorderBottom(BorderStyle.THIN);
		cs.setBorderLeft(BorderStyle.THIN);
		cs.setBorderRight(BorderStyle.THIN);
		cs.setBorderTop(BorderStyle.THIN);
		Font font = wb.createFont();
		font.setFontName("宋体");
		font.setFontHeightInPoints((short) 12);// 设置字体大小
		
		return wb;
	}

}
