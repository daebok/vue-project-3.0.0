package com.rd.ifaes.manage.sys;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rd.ifaes.common.util.excel.ExportUtil;
import com.rd.ifaes.core.core.util.CacheUtils;
/**
 * 导出工具类
 * @author lh
 * @version 3.0
 * @since 2016-11-18
 *
 */
@Controller
public class ExportController extends SystemController {

	@RequestMapping(value = "/sys/export/progress")
	@ResponseBody
	public Object progress(String exportUrl){
		Map<String, Object> result = new HashMap<>();
		result.put("result", true);
		int end = exportUrl.indexOf('?')==-1?exportUrl.length():exportUrl.indexOf('?');
		result.put("msg", CacheUtils.getDouble(ExportUtil.makeCacheKey(exportUrl.substring(0, end), getUserId())));
		return result;
	}
	
}
