package com.rd.ifaes.job;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;
import com.rd.ifaes.common.util.DateUtils;

/**
 * 
 * @author MyJob
 *
 */
//@PersistJobDataAfterExecution  
//@DisallowConcurrentExecution// 不允许并发执行  
public class MyJob extends QuartzJobBean{
	protected void execute() {
		System.out.println("\n\n\n\n\n\n\n\n\n===============================================");
		System.out.println(DateUtils.getDateTime() +  " " + Thread.currentThread().getName() + " 定时任务执行中…");
		System.out.println("===============================================\n\n\n\n\n\n\n\n\n");
		
	}

	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		execute();
		
	}
	
}
