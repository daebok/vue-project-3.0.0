package com.rd.ifaes.supports.spring;

import static org.springframework.util.Assert.isTrue;
import static org.springframework.util.Assert.notNull;
import static org.springframework.util.StringUtils.split;

import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisNode;

import com.rd.ifaes.common.exception.BussinessException;
import com.rd.ifaes.common.util.StringUtils;
/**
 * <p>RedisCluster配置</p>
 * <p>用于简化spring-data-redis配置</p>
 * @author lh
 * @version 3.0
 * @since 2017-4-5
 *
 */
public class RedisClusterConfig extends RedisClusterConfiguration{

	public RedisClusterConfig(String nodes, Integer maxRedirects) {
		super();
		initNodes(nodes);
		setMaxRedirects(maxRedirects);
	}
	
	private void initNodes(String nodes){
		if(StringUtils.isBlank(nodes)){
			throw new BussinessException("nodes can not be empty!");
		}
		String[] hostAndPorts = nodes.split(",");
		for (String hostAndPort : hostAndPorts) {
			addClusterNode(readHostAndPortFromString(hostAndPort));
		}
	}
	
	private RedisNode readHostAndPortFromString(String hostAndPort) {
		String[] args = split(hostAndPort, ":");
		notNull(args, "HostAndPort need to be seperated by  ':'.");
		isTrue(args.length == 2, "Host and Port String needs to specified as host:port");
		return new RedisNode(args[0], Integer.valueOf(args[1]).intValue());
	}
	
}
