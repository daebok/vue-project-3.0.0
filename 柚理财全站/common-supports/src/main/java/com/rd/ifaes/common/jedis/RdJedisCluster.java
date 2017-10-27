package com.rd.ifaes.common.jedis;

import java.util.Set;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import com.rd.ifaes.common.util.StringUtils;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisClusterCommand;

public class RdJedisCluster extends JedisCluster {
		
	public RdJedisCluster(Set<HostAndPort> jedisClusterNode, int timeout,
			int maxRedirections, GenericObjectPoolConfig poolConfig) {
		super(jedisClusterNode, timeout, maxRedirections, poolConfig);
	}

	public Set<String> keys(final String pattern) {
	    return new JedisClusterCommand<Set<String>>(connectionHandler, maxRedirections) {
	      @Override
	      public Set<String> execute(Jedis connection) {
	        return connection.keys(pattern);
	      }
	    }.run(pattern);
	}
	
	public Set<byte[]> keys(final byte[] pattern) {
		String patternStr = StringUtils.toString(pattern);
	    return new JedisClusterCommand<Set<byte[]>>(connectionHandler, maxRedirections) {
	      @Override
	      public Set<byte[]> execute(Jedis connection) {
	        return connection.keys(pattern);
	      }
	    }.run(patternStr);
	}
	
	@Deprecated
	public Long clear(final String pattern){
		 return new JedisClusterCommand<Long>(connectionHandler, maxRedirections) {
		      @Override
		      public Long execute(Jedis connection) {
		    	 return connection.del(connection.keys(pattern).toArray(new String[]{}));
		      }
		    }.run(pattern);
	}

}
