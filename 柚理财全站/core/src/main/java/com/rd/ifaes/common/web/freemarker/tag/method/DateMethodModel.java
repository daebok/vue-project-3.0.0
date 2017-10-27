package com.rd.ifaes.common.web.freemarker.tag.method;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.rd.ifaes.common.util.DateUtils;
import com.rd.ifaes.common.util.StringUtils;

import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;

/**
 * @author lh
 * @since 2016-10-25
 * @version 3.0
 */
@Component
public class DateMethodModel implements TemplateMethodModelEx {

	private static final Logger LOGGER = LoggerFactory.getLogger(DateMethodModel.class);
	
	@SuppressWarnings({ "rawtypes", "unused" })
	public Object exec(List args) throws TemplateModelException {
		String arg1 = "";
		String format = "yyyy-MM-dd HH:mm:ss";
		String s = "";
		if (null == args) {
			return "";
		} else {
			if (1 == args.size()) {
				if (args.get(0) == null || args.get(0).toString().equals("")) {
					return "";
				} else if (args.get(0).toString().equals("now")) {
					return System.currentTimeMillis() / 1000 + "";
				} else {

				}
			} else if (2 == args.size()) {
				if (args.get(1) != null && !args.get(1).toString().equals("")) {
					format = (String) args.get(1);
				}
			} else if (3 == args.size()) {
				if (args.get(2) != null && args.get(2).toString().equals("vip")) {
					return vip((String) args.get(0), (String) args.get(1));
				}
			}
			if (format.equals("age")) {
				return getAge(args.get(0).toString());
			}
			long times = 0;
			try {
				times = Long.parseLong(args.get(0).toString());
			} catch (NumberFormatException e) {
				LOGGER.error(e.getMessage(), e);
				times = 0;
			}
			Date d = new Date(times * 1000);
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			try {
				s = sdf.format(d);
			} catch (Exception e) {
				s = "ERROR！";
			}
			return s;
		}
	}

	@SuppressWarnings("rawtypes")
	private String getAge(String age) throws TemplateModelException {
		Map map = DateUtils.getApartTime(age, DateUtils.dateStr2(DateUtils.getNow()));
		if (map == null) {
			return "-1";
		}
		return map.get("YEAR") + "";
	}

	@SuppressWarnings("rawtypes")
	private String vip(String begin, String end) {
		Map map = DateUtils.getApartTime(DateUtils.dateStr2(new Date((StringUtils.toLong(begin) * 1000))),
				DateUtils.dateStr2(new Date(StringUtils.toLong(end) * 1000)));
		return "还有" + map.get("YEAR") + "年" + map.get("MONTH") + "月" + map.get("DAY") + "天 到期";
	}
}
