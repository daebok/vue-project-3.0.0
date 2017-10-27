/**
* 互联网金融交易系统ifaes (Internet Finance  Assets  Exchange  System)
 * Copyright (c) 2016 杭州融都科技股份有限公司 版权所有
 */
package com.rd.ifaes.core.tpp.model.cbhb;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rd.ifaes.common.exception.BussinessException;
import com.rd.ifaes.common.resource.CbhbResource;
import com.rd.ifaes.common.util.BigDecimalUtils;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.core.core.constant.Constant;
import com.rd.ifaes.core.core.util.ResourceUtils;
import com.rd.ifaes.core.tpp.exception.CbhbException;
import com.rd.ifaes.core.tpp.model.cbhb.xml.XmlQueryMerchantAccts;

/**
 * 帮助类
 * @version 3.0
 * @author QianPengZhan
 * @date 2017年2月22日
 */
public class CbhbHelper {
	
	/**
	 * 日志记录器
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(CbhbHelper.class);
	
	
	/**
	 * 提交 不判断状态码是否是000000（成功）
	 * @author QianPengZhan
	 * @date 2017年2月22日
	 * @param model
	 * @return
	 */
	public  static CbhbBaseModel doSubmit(CbhbBaseModel model){
		try {
			model.submit(model);
		} catch (Exception e) {
			LOGGER.error("doSubmit提交异常：{}",e.toString());
		}
		//2、返回码处理
		if(!model.checkReturn()){
			LOGGER.error("返回非成功：{}",model.getRespDesc());
		}
		return model;
	}
	
	
	/**
	 * 提交  状态码必须是000000（成功）
	 * @author QianPengZhan
	 * @date 2017年2月22日
	 * @param model
	 * @return
	 */
	public  static CbhbBaseModel doSubmitWithValidCode(CbhbBaseModel model){
		//1、提交
		try {
			model.submit(model);
		} catch (Exception e) {
			LOGGER.error("提交异常：{}",e.toString());
			throw new CbhbException(ResourceUtils.get(CbhbResource.CBHB_RESOURCE_SUBMIT_ERROR),BussinessException.TYPE_JSON);
		}
		//2、返回码处理
		if(!model.checkReturn()){
			LOGGER.error("返回非成功：{}",model.getRespDesc());
			throw new CbhbException(model.getRespDesc(),BussinessException.TYPE_JSON);
		}
		return model;
	}
	
	/**
	 * 渤海银行 -- 回调金额转为金额再除以100 保留2位有效数字
	 * @author QianPengZhan
	 * @date 2017年3月1日
	 * @return
	 */
	public static BigDecimal getBigDecimalMoneyLow(final String value){
		BigDecimal result = StringUtils.isBlank(value)?BigDecimal.ZERO:BigDecimalUtils.valueOf(value);
		return BigDecimalUtils.div(result, BigDecimal.valueOf(Constant.DOUBLE_ONE_HUNDRED),Constant.INT_TWO);
	}
	
	/**
	 * 渤海银行 -- 请求金额保留2位有效数字乘以100转为字符串
	 * @author QianPengZhan
	 * @date 2017年3月1日
	 * @return
	 */
	public static String getBigDecimalMoneyUpper(final String value){
		BigDecimal temp = StringUtils.isBlank(value)?BigDecimal.ZERO:BigDecimalUtils.valueOf(value);
		BigDecimal result = BigDecimalUtils.decimal(temp.multiply(BigDecimal.valueOf(Constant.DOUBLE_ONE_HUNDRED)),Constant.INT_ZERO);
		return result.toString();
	}
	
	/**
	 * 渤海银行 -- 请求金额保留2位有效数字除以100转为字符串
	 * @author QianPengZhan
	 * @date 2017年3月15日
	 * @param value
	 * @return
	 */
	public static String getStringLower(final String value){
		BigDecimal temp = StringUtils.isBlank(value)?BigDecimal.ZERO:BigDecimalUtils.valueOf(value);
		BigDecimal result = BigDecimalUtils.decimal(temp.divide(BigDecimal.valueOf(Constant.DOUBLE_ONE_HUNDRED)),Constant.INT_TWO);
		return result.toString();
	}
	
	/**
	 * 渤海银行 -- 去除小数点
	 * @author ZhangBiao
	 * @date 2017年3月1日
	 * @param value
	 * @return
	 */
	public static String checkNumber(final String value){
		int num = value.indexOf('.');
		return value.substring(0,num);
	}
	
	
	/**
	 * 用于单个<viin>的xml解析
	 * @param xmlStr
	 * @return
	 */
	@SuppressWarnings({ "rawtypes" })
	public static final List<Map<String,Object>> xmlToMap(String xmlStr){
		//处理xmlString 
		int length = xmlStr.lastIndexOf("</viin>");
		xmlStr = xmlStr.substring(0, length+7);
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		try {
			//1、将字符串转换为xml的文件元素
			Document document = DocumentHelper.parseText(xmlStr);
			//2、获取根元素
			Element rootElement = document.getRootElement();
			LOGGER.info("rootElememt-name:"+rootElement.getName());
			 for ( Iterator i = rootElement.elementIterator(); i.hasNext(); ) {
		            Element element = (Element) i.next();
		           LOGGER.info(element.asXML());
		            List eList =  element.elements();
		            Map<String,Object> map = new HashMap<String, Object>();
		            for (int j = 0; j < eList.size(); j++) {
						Element e = (Element) eList.get(j);
						LOGGER.info("name:{},value:{}",e.getName(), e.getTextTrim());
						map.put(e.getName(), e.getTextTrim());
		            }
		            list.add(map);
			 }
		} catch (DocumentException e) {
			throw  new CbhbException(e.toString(),BussinessException.TYPE_JSON);
		}
		return list;
	}
	
	/**
	 * 将商户余额查询接口中的xmlString
	 * @author QianPengZhan
	 * @date 2017年3月6日
	 * @param xmlStr
	 * @return
	 */
	public static final List<XmlQueryMerchantAccts> xmlToQueryMerchantAccts(String xmlString){
		List<Map<String,Object>> list = xmlToMap(xmlString);
		List<XmlQueryMerchantAccts> objectList = new ArrayList<XmlQueryMerchantAccts>();
		if(CollectionUtils.isNotEmpty(list)){
			for (int i = 0; i < list.size(); i++) {
				XmlQueryMerchantAccts xmqa = new XmlQueryMerchantAccts();
				Map<String,Object> map  = list.get(i);
				xmqa.setAcTyp(StringUtils.isNull(map.get("ac_typ")));
				xmqa.setAvlBal(getBigDecimalMoneyLow(StringUtils.isNull(map.get("avl_bal"))).toString());
				xmqa.setActBal(getBigDecimalMoneyLow(StringUtils.isNull(map.get("act_bal"))).toString());
				xmqa.setFrzBal(getBigDecimalMoneyLow(StringUtils.isNull(map.get("frz_bal"))).toString());
				objectList.add(xmqa);
			}
		}
		return objectList;
	}
	
	
	/**
	 * xmlString 进行  base64位 转码
	 * @param resp
	 * @return
	 */
	@SuppressWarnings("restriction")
	public static final String base64Encode(String xmlString){
		try{    	    	
	    	byte[] b = null;   
	         try {  
	                b = xmlString.getBytes("GBK");                  
	            } catch (UnsupportedEncodingException e) {  
	                e.printStackTrace();  
	            }  
	            if (b != null) {  
	            	xmlString = new sun.misc.BASE64Encoder().encode(b).replaceAll("\\s*","");  
	            } 
	     }catch (Exception e) {
	           LOGGER.error("转码异常:" + e.getMessage());
	     }
		return  xmlString;
	}
	
	/**
	 * base64 解码  
	 * @param string
	 * @return xmlString
	 */
	@SuppressWarnings("restriction")
	public static final String  base64Decode(String string){
		sun.misc.BASE64Decoder decoder = new sun.misc.BASE64Decoder();
		byte[] c = null;
		String result = StringUtils.EMPTY;
		try {
			c = decoder.decodeBuffer(string);
			result = new String(c,"GBK");
			LOGGER.info(result);
		} catch (IOException e) {
			throw new CbhbException(ResourceUtils.get(CbhbResource.CBHB_RESOURCE_ERROR_URLDECODE),BussinessException.TYPE_JSON);
		}
		return result;
	}
	
	public static void main(String[] args) {
		String ttt= "PD94bWwgdmVyc2lvbj0iMS4wIiBlbmNvZGluZz0iR0IyMzEyIj8+CiAgPHZpaW4+CiAgICA8cnM+CiAgICAgIDxjYXBfY29yZz5JQ0JDPC9jYXBfY29yZz4KICAgICAgPGNhcF9jcmRfbm8+NjIxNjE4NDQwMzEyNDYxNDYyOTwvY2FwX2NyZF9ubz4KICAgICAgPG1ibF9ubz4xMzg1NjkxMTM5NDwvbWJsX25vPgogICAgICA8dXNyX25tPseuxfTVuTwvdXNyX25tPgogICAgPC9ycz4KICAgIDxycz4KICAgICAgPGNhcF9jb3JnPkNNQjwvY2FwX2Nvcmc+CiAgICAgIDxjYXBfY3JkX25vPjYyMjU4ODU4NjY3ODA5MDk8L2NhcF9jcmRfbm8+CiAgICAgIDxtYmxfbm8+MTM4NTY5MTEzOTQ8L21ibF9ubz4KICAgICAgPHVzcl9ubT7HrsX01bk8L3Vzcl9ubT4KICAgIDwvcnM+CiAgPC92aWluPgo";
		System.out.println(base64Decode(ttt));
	}
}
