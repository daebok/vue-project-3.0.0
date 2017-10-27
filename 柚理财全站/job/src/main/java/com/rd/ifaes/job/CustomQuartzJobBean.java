package com.rd.ifaes.job;

import java.lang.reflect.Method;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;
import com.rd.ifaes.common.util.SpringContextHolder;

/**
 * 支持job实现的自定义,将多个定时任务集中到统一的业务类中
 * @author xxb
 * @date 2016年12月30日
 */
//@Component
public class CustomQuartzJobBean extends QuartzJobBean{

    private String targetObject;  //目标名称
    private String targetMethod;  //目标方法
    
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        try {
            Object otargetObject = SpringContextHolder.getBean(targetObject);
            Method method = otargetObject.getClass().getMethod(targetMethod, new Class[] {});
            method.invoke(otargetObject, new Object[] {});
        } catch (Exception e) {
            throw new JobExecutionException(e);
        }
    }


    public void setTargetObject(String targetObject) {
        this.targetObject = targetObject;
    }

    public void setTargetMethod(String targetMethod) {
        this.targetMethod = targetMethod;
    }

}
