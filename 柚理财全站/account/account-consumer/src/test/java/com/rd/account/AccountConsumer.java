//package com.rd.account;
//
//
//import java.math.BigDecimal;
//import java.util.List;
//
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//
//import com.rd.account.domain.Account;
//import com.rd.account.service.AccountService;
//
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations={"classpath:spring-consumer.xml"})
//public class AccountConsumer {
// 
//	
//	private static Logger logger = LoggerFactory.getLogger(AccountConsumer.class);
//
//	//@Reference
//	@Autowired
//	private AccountService accountService;
//	
//	
//	
//	@Test
//	public void testRegister(){
//		String userId = "lihua";
//		String accountCode = "001";
//		String accountType = null;
////		double money = 0d;
////		String tradeNo = null;
//		String parentCode = null;
//		
//		//RpcContext.getContext().setAttachment("index", "1"); // 隐式传参，后面的远程调用都会隐式将这些参数发送到服务器端，类似cookie，用于框架集成，不建议常规业务使用
//		
//		try {
//			accountService.register(userId, accountCode, accountType, parentCode);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		
//	}
//	
//	@Test
//	public void testRecharge(){
//		String userId = "lihua";
//		String accountCode = "001";
//		String accountType = null;
//		BigDecimal money = new BigDecimal(100);
//		String tradeNo = System.currentTimeMillis()+"";
////		String parentCode = null;
//	
//		try {
//			accountService.recharge(userId, accountCode, accountType, money, tradeNo);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//	
//	@Test
//	public void testCash(){
//		String userId = "lihua";
//		String accountCode = "001";
//		String accountType = null;
//		BigDecimal money = new BigDecimal(1000);
//		String tradeNo = System.currentTimeMillis()+"";
////		String parentCode = null;
//	
//		try {
//			accountService.cash(userId, accountCode, accountType, money, tradeNo);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//	
//	@Test
//	public void testFreeze(){
//		String userId = "lihua";
//		String accountCode = "001";
//		String accountType = null;
//		BigDecimal money = new BigDecimal(100);
//		String tradeNo = System.currentTimeMillis()+"";
////		String parentCode = null;
//	
//		try {
//			accountService.freeze(userId, accountCode, accountType, money, tradeNo);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//	
//	
//	
//	
//	
//	
//	@Test
//	public void testAccountCount(){
//		List<Account> list = accountService.findList(null);
//	    for (Account ac : list) {
//	     	logger.info(ac.toString());			
//		}
//		logger.info("total:{}",accountService.getCount(null));
//	}
//	
//	
////    public static void main(String[] args) {
////        //测试常规服务
////        @SuppressWarnings("resource")
////        ApplicationContext  context = new ClassPathXmlApplicationContext("classpath:spring-consumer.xml");
////        AccountService accountService = context.getBean(AccountService.class);
////		
////        List<Account> list = accountService.findAll();
////        for (Account ac : list) {
////        	logger.info(ac.toString());			
////		}
////        logger.info("total:{}",accountService.getCount(null));
////    }
//    
//}