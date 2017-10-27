package com.rd.ifaes.common.util.druid;

import java.security.PublicKey;
import java.util.Properties;

import com.alibaba.druid.filter.config.ConfigFilter;
import com.alibaba.druid.filter.config.ConfigTools;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;
/**
 *  druid原生数据库密码加密配置增强（不只password，url,username都加密）
 * @author FangJun
 *
 */
public class RdConfigFilter extends ConfigFilter {

	/**
	 * 解密 数据库信息配置(url,username,password)
	 */
    public void decrypt(DruidDataSource dataSource, Properties info) {

        try {
            final PublicKey publicKey = getPublicKey(dataSource.getConnectProperties(), info);
            setDbConfig(dataSource,info,DruidDataSourceFactory.PROP_URL,publicKey);
            setDbConfig(dataSource,info,DruidDataSourceFactory.PROP_USERNAME,publicKey);
            setDbConfig(dataSource,info,DruidDataSourceFactory.PROP_PASSWORD,publicKey);
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to decrypt.", e);
        }
    }
	/**
	 *  获取密文数据，解密后放回dataSource
	 * @param dataSource 数据源
	 * @param info   配置信息
	 * @param propKey  (url,username,password)三种之一
	 * @param publicKey  加密公钥
	 * @throws Exception 
	 */
    private   void   setDbConfig(final DruidDataSource dataSource, final Properties info, final String propKey,
    		final PublicKey publicKey) throws Exception{
    	String result=null;
        if (info != null) {
        	result = info.getProperty(propKey);
        }
        if (result == null || result.length() == 0) {
        	result = dataSource.getConnectProperties().getProperty(propKey);
        }

        if (result == null || result.length() == 0) {
        	if(DruidDataSourceFactory.PROP_URL.equals(propKey)){
        		result = dataSource.getUrl();
        	    String plainText = ConfigTools.decrypt(publicKey, result);
        	    if (info != null) {
                    info.setProperty(DruidDataSourceFactory.PROP_URL, plainText);
                } else {
                    dataSource.setUrl(plainText);
                }
        	}else if(DruidDataSourceFactory.PROP_USERNAME.equals(propKey)){
        		result = dataSource.getUsername();
        	    String plainText = ConfigTools.decrypt(publicKey, result);
        	    if (info != null) {
                    info.setProperty(DruidDataSourceFactory.PROP_USERNAME, plainText);
                } else {
                    dataSource.setUsername(plainText);
                }
        	}else if(DruidDataSourceFactory.PROP_PASSWORD.equals(propKey)){
        		result = dataSource.getPassword();
        	     String plainText = ConfigTools.decrypt(publicKey, result);
          	    if (info != null) {
                      info.setProperty(DruidDataSourceFactory.PROP_PASSWORD, plainText);
                  } else {
                      dataSource.setPassword(plainText);
                  }
        	}
        }
    }
}
