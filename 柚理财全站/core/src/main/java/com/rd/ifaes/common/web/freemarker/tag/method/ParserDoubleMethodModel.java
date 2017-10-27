package com.rd.ifaes.common.web.freemarker.tag.method;

import java.util.List;

import org.springframework.stereotype.Component;

import com.rd.ifaes.common.util.BigDecimalUtils;
import com.rd.ifaes.common.util.StringUtils;

import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;

/**
 * 转为double类型
 * @author lh
 * @version 3.0
 * @since 2016-10-25
 */
@Component
public class ParserDoubleMethodModel implements TemplateMethodModelEx {

	@SuppressWarnings("rawtypes")
	public Object exec(List args) throws TemplateModelException {
		if (args == null || args.size() < 1)
			return 0;
		Object str = args.get(0);
		String tmp = StringUtils.isNull(str).replaceAll(",", "");
		return BigDecimalUtils.round(tmp);
	}
}
