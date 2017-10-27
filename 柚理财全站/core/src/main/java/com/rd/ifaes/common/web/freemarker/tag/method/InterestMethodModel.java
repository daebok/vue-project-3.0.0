package com.rd.ifaes.common.web.freemarker.tag.method;

import java.math.BigDecimal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.rd.ifaes.common.util.BigDecimalUtils;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.core.interest.InterestCalculator;
import com.rd.ifaes.core.project.worker.ProjectWorker;

import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;

/**
 * 有三种方式: 　　
 * (1).在模板文件中注册,在模板中使用 　　<#assign interest= "com.rd.freemarker.InterestMethodModel"?new()> 　　<#assign lilv=interest(500,0.02,3,1,0)/> 　
 * (2).处理模板文件时注册
 * 	 Map<String,Object> root=new HashMap<String, Object>(); 
 * 	 root.put("interest", new InterestMethodModel()); Configuration config=new Configuration(); 
 * 	 TemplateModel model = createModel(wrapper, servletContext, request, response); 
 * 	 template.process(model, root); // 处理模版 
 * (3).Spring集成处理,自定义FreeMarkerConfigurer 
 * 	   继承FreeMarkerConfigurer 设置config.setSharedVariable("interest", new InterestMethodModel() ); 
 * 
 * @author lh
 * @version 3.0
 * @since 2016-10-25
 */
@Component
public class InterestMethodModel implements TemplateMethodModelEx {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(InterestMethodModel.class);

	public Object exec(@SuppressWarnings("rawtypes") List args) throws TemplateModelException {
		if (null != args && 5 == args.size()) {
			BigDecimal account = BigDecimal.ZERO, apr = BigDecimal.ZERO;//本金、利率
			int period = 0;//期限
			String repayStyle = "", timeType = "";//还款方式，期限类型（天标、月标）
			try {				
				account = BigDecimalUtils.valueOf(args.get(0).toString());
				apr = BigDecimalUtils.valueOf(args.get(1).toString());
				period = Integer.valueOf(args.get(2).toString());
				repayStyle = StringUtils.isNull(args.get(3).toString());
				timeType = StringUtils.isNull(args.get(4).toString());
			} catch (NumberFormatException e) {
				LOGGER.error(e.getMessage(), e);
			}
			InterestCalculator ic = ProjectWorker.doInterestCalculator(repayStyle, account, apr, BigDecimal.ZERO, timeType, period, BigDecimal.ZERO, null, null, 0);
			return ic.repayInterest();
		} else {
			return "Argument is null,or argument's length illegal.";
		}
	}
}
