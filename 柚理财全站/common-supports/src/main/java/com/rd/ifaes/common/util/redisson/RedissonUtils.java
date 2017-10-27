package com.rd.ifaes.common.util.redisson;

import java.io.IOException;

import org.redisson.Config;
import org.redisson.Redisson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rd.ifaes.common.util.PropertiesUtils;
import com.rd.ifaes.common.util.StringUtils;

/**
 * redisson分布式锁 工具类
 * @author lh
 * @version 3.0
 * @since 2016-7-23
 */

public final class RedissonUtils {
	
	
	private static final Logger LOGGER = LoggerFactory.getLogger(RedissonUtils.class);
	
	private static RedissonUtils instance;
	private Redisson redisson;

	private RedissonUtils() throws IOException {
		init();
	}

	public static synchronized RedissonUtils getInstance() {
		if (instance == null) {
			try {
				instance = new RedissonUtils();					
			} catch (IOException e) {
				LOGGER.warn(e.getMessage(), e);
			}
		}
		return instance;
	}

	public Redisson getClient() {
		return redisson;
	}

	public void init() throws IOException {		
		String nodes = PropertiesUtils.getValue("spring.redis.cluster.nodes");
		if(StringUtils.isNotBlank(nodes)){
			String[] hosts = nodes.split(",");
			redisson = getClusterClient(hosts);//
		}else{
			String hosts = PropertiesUtils.getValue("redis.host")+":"+ PropertiesUtils.getValue("redis.port");			
			redisson = getSingleClient(hosts);// 单机
		}
	}

	public Redisson getSingleClient(String host) {
		Config config = new Config();
		config.useSingleServer().setAddress(host).setConnectionPoolSize(1000);
		String password = PropertiesUtils.getValue("redis.password");
		if(StringUtils.isNotBlank(password)){
			config.useSingleServer().setPassword(password);
		}
		
		return (Redisson)Redisson.create(config);
	}

	// Master/Slave servers connection:
	public Redisson getMasterSlaveClient(String add, String hosts) {
		Config config = new Config();
		return (Redisson)Redisson.create(config);
	}

	// Sentinel servers connection:
	public Redisson getSentinelClient(String masterName, String hosts) {
		Config config = new Config();
		return (Redisson)Redisson.create(config);
	}

	// Cluster nodes connections:
	public Redisson getClusterClient(String ... hosts) {
		Config config = new Config();
		config.useClusterServers().setScanInterval(2000) // sets cluster state
				.addNodeAddress(hosts).setMasterConnectionPoolSize(10000)
				.setSlaveConnectionPoolSize(10000);

		return (Redisson)Redisson.create(config);
	}
}