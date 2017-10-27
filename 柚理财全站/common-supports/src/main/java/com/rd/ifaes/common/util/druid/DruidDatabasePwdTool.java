package com.rd.ifaes.common.util.druid;

import com.alibaba.druid.filter.config.ConfigTools;
/**
 * 参考：https://github.com/alibaba/druid/wiki/%E4%BD%BF%E7%94%A8ConfigFilter
 * druid 数据库密码加密工具
 *        java -cp druid-1.0.16.jar com.alibaba.druid.filter.config.ConfigTools you_password
 *   
 *  输出的password,publicKey 分别贴到config.properties的jdbc.password 和 druid.publicKey
 *  确保 druid配置了ConfigFilter : 
 *  <pre>
 *    <bean id="configFilter" class="com.alibaba.druid.filter.config.ConfigFilter" />  
 *    <bean id="dataSource" class="com.alibaba.druid.pool.DruidDataSource" init-method="init" destroy-method="close"> 
 *        <property name="proxyFilters">  
 *              <list>  
 *                  <ref bean="configFilter" />  
 *             </list>  
 *        </property> 
 *     </bean>
 * </pre>
 * @author FangJun
 *
 */
public class DruidDatabasePwdTool {

	public static void main(String[] args)throws Exception {
		 
		String url =  args[0];// "jdbc:mysql://172.16.90.30:3306/ifaes?useUnicode=true&characterEncoding=utf-8&allowMultiQueries=true";
		String username=args[1]; //"dev";
		 String password =args[2];// "dev";
        String[] arr = ConfigTools.genKeyPair(1024);
        System.out.println("privateKey:\t" + arr[0]);
        System.out.println("publicKey:\t" + arr[1]);
        System.out.println("url:\t" +ConfigTools. encrypt(arr[0], url));
        System.out.println("username:\t" +ConfigTools. encrypt(arr[0], username));
        System.out.println("password:\t" +ConfigTools. encrypt(arr[0], password));
	}

}
