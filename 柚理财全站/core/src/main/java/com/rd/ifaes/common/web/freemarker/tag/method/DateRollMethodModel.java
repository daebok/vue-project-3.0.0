package com.rd.ifaes.common.web.freemarker.tag.method;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Component;

import com.rd.ifaes.common.util.DateUtils;

import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;

/**
 * @author lh
 * @date  2016-10-25
 * @version <b>Copyright (c)</b> 2012-51融都-版权所有<br/>
 *          Example: dateroll(date,year,month,day,format) date代表需要滚动的时间 year表示滚动年份，month表示滚动月份，day表示滚动天数，指DAY_OF_MONTH,
 *          format代表格式，如不填写，格式为"yyyy-MM-dd HH:mm:ss"
 */
@Component
public class DateRollMethodModel implements TemplateMethodModelEx {

	String dateStr = "", yStr = "", mStr = "", dStr = "", format = "";

	@SuppressWarnings("rawtypes")
	public Object exec(List args) throws TemplateModelException {
		long times = 0;
		int year = 0, mon = 0, day = 0;
		if (!parseArgs(args).equals("")) {
			return "Arguments error!";
		}
		times = Long.parseLong(dateStr);
		year = Integer.parseInt(yStr);
		mon = Integer.parseInt(mStr);
		day = Integer.parseInt(dStr);
		Date d = new Date(times * 1000);
		d = DateUtils.rollDate(d, year, mon, day);
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		String f = sdf.format(d);
		return f;
	}

	@SuppressWarnings("rawtypes")
	private String parseArgs(List args) {
		if (args.size() == 4) {
			dateStr = args.get(0) == null ? null : args.get(0).toString();
			yStr = args.get(1) == null ? null : args.get(1).toString();
			mStr = args.get(2) == null ? null : args.get(2).toString();
			dStr = args.get(3) == null ? null : args.get(3).toString();
			format = "yyyy-MM-dd HH:mm:ss";
		} else if (args.size() == 5) {
			dateStr = args.get(0) == null ? null : args.get(0).toString();
			yStr = args.get(1) == null ? null : args.get(1).toString();
			mStr = args.get(2) == null ? null : args.get(2).toString();
			dStr = args.get(3) == null ? null : args.get(3).toString();
			format = args.get(4) == null ? null : args.get(4).toString();
		} else {
			return "Arguments error!";
		}
		return "";
	}

}
