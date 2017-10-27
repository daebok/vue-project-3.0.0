package com.rd.ifaes.core.core.util;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.rd.ifaes.common.util.SpringContextHolder;
import com.rd.ifaes.core.sys.domain.DictData;
import com.rd.ifaes.core.sys.service.DictDataService;

/**
 * 
 * @author Administrator
 * @version 3.0
 * @since 2016-7-2
 *
 */
public class DictUtils {

	private static DictDataService dictDataService = SpringContextHolder.getBean("dictDataService");
	
	/**
	 * 取字典项集合
	 * @param dictType
	 * @return
	 */
	public static List<DictData> list(String dictType){
		return dictDataService.findListByDictType(dictType);
	}
	
	/**
	 * 取字典项
	 * @param dictType
	 * @param itemValue
	 * @return
	 */
	public static DictData getData(String dictType, String itemValue){
		return dictDataService.get(dictType, itemValue);
	}
	
	public static String getItemName(String dictType, String itemValue){
		String itemName="";
		if(StringUtils.isNotBlank(dictType) && StringUtils.isNotBlank(itemValue) ){
			DictData dictData = getData(dictType, itemValue);
			if(dictData!=null){
				itemName = dictData.getItemName();
			}
		}
		return itemName;
	}
}
