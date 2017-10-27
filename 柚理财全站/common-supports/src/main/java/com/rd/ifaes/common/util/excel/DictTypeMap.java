package com.rd.ifaes.common.util.excel;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Maps;
import com.rd.ifaes.common.annotation.DictType;
import com.rd.ifaes.common.util.SpringContextHolder;
import com.rd.ifaes.core.sys.domain.DictData;
import com.rd.ifaes.core.sys.service.DictDataService;

/**
 * 字典类型集合-供导出使用
 * @author lh
 * @version 3.0
 * @since 2016-11-3
 */
public class DictTypeMap {
	
	private static DictDataService dictDataService = (DictDataService)SpringContextHolder.getBean("dictDataService");
	
	/**
	 * 根据@DictType注解获取字典对应的显示值
	 * 
	 * @param obj
	 * @return
	 */
	public static Map<String, String> getDictShowValues(Object obj) {
		Map<String, String> dictKvs = Maps.newHashMap();
		if (obj != null) {
			// 得到类对象
			Class<?> clazz = obj.getClass();
			do{
				// 得到类中的所有属性集合
				Field[] fs = clazz.getDeclaredFields();
				for (Field field : fs) {
					DictType dictTypeAnnot = field.getAnnotation(DictType.class);
					getDictValuesByAnnot(dictKvs, field, dictTypeAnnot);
				}
				clazz = clazz.getSuperclass();
			}while(clazz != Object.class);
		}
		return dictKvs;
	}

	/**
	 * getDictValuesByAnnot
	 * @param dictKvs
	 * @param field
	 * @param dictTypeAnnot
	 */
	private static void getDictValuesByAnnot(Map<String, String> dictKvs, Field field, DictType dictTypeAnnot) {
		if (dictTypeAnnot != null) {
			String colName = field.getName();
			String type = dictTypeAnnot.type();
			dictKvs.put(colName, type);
			List<DictData> list = dictDataService.findListByDictType(type);
			for (DictData code : list) {
				String key = colName + ":" + code.getItemValue();
				dictKvs.put(key, code.getItemName());
			}
		}
	}

}
