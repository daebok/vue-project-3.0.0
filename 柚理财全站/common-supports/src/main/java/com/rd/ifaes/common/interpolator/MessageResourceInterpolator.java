package com.rd.ifaes.common.interpolator;
import java.util.Locale;

import javax.annotation.Resource;
import javax.validation.MessageInterpolator;

import org.springframework.binding.message.MessageBuilder;

import com.rd.ifaes.common.util.resource.MessageResource;

/**
 * 拦截Annotation验证信息
 * @author Robin
 *
 */
public class MessageResourceInterpolator implements MessageInterpolator {
    
    @Resource
    private MessageResource messageResource;
    
    public void setMessageResource(MessageResource messageResource) {
        this.messageResource = messageResource;
    }

    @Override
    public String interpolate(String messageTemplate, Context context) {
        return interpolate(messageTemplate,context,Locale.ENGLISH);
    }

    @Override
    public String interpolate(String messageTemplate, Context context, Locale locale) {
        
        String messageTemp;
        if(messageTemplate.startsWith("{") && messageTemplate.endsWith("}")) {
            messageTemp = messageTemplate.substring(1, messageTemplate.length() - 1);
        }
        else {
            return messageTemplate;
        }
        
        String[] params = (String[]) context.getConstraintDescriptor().getAttributes().get("params");
   	      
        MessageBuilder builder = new MessageBuilder().code(messageTemp);
        if (params != null) {
            builder = builder.args(params);
        }
        
        return builder.build().resolveMessage(messageResource, locale).getText();
    }

}