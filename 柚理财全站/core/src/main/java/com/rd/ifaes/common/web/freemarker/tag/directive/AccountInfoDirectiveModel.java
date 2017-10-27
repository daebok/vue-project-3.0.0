package com.rd.ifaes.common.web.freemarker.tag.directive;

import java.io.IOException;
import java.io.Writer;
import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.rd.account.domain.Account;
import com.rd.account.model.AccountQueryModel;
import com.rd.account.service.AccountService;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.core.core.constant.Constant;
import com.rd.ifaes.core.core.util.ConfigUtils;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

/**
 *融都新版我的资产展示
 * @author lh
 *usage: <@accountInfo  id="2" ></@account>
 *@param id 用户主键Id，String类型。
 *@return  返回拼装出来的html字符串
 */
@Component
public class AccountInfoDirectiveModel implements TemplateDirectiveModel{
	
	private static Logger logger = LoggerFactory.getLogger(AccountInfoDirectiveModel.class);
	
    private static final String ID = "id";
    
    @Resource
    private AccountService accountService;
    
	@SuppressWarnings("rawtypes")
	@Override
	public void execute(Environment environment, Map map,
			TemplateModel[] atemplatemodel,
			TemplateDirectiveBody templatedirectivebody)
			throws TemplateException, IOException {
		
		Iterator it = map.entrySet().iterator();
		String id = "";
        while (it.hasNext()) {
            Map.Entry entry = (Entry) it.next();
            String paramName = entry.getKey().toString();
            TemplateModel paramValue = (TemplateModel) entry.getValue();
            logger.debug("name:"+paramName);
            logger.debug("r:"+paramValue);
            if (paramName.equals(ID)) {
            	id = paramValue.toString();
            }
        }
        String result=html(id);
        Writer out = environment.getOut();
        out.write(result);
	}
	
	
	private String html(String id){
		StringBuffer sb=new StringBuffer();
		if(StringUtils.isNotBlank(id)){
			Account account = accountService.getByUserId(new AccountQueryModel(id,ConfigUtils.getValue(Constant.ACCOUNT_CODE)));
			DecimalFormat df = new DecimalFormat("0.00");//""00.00"小数点后面的0的个数表示小数点的个数
			String sTotal = df.format(account.getTotal());
			sb.append(sTotal);
		}
		return sb.toString();
	}
	
}
