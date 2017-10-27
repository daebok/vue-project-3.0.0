package com.rd.ifaes.common.security.shiro;

import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;
/**
 * jedis会话dao抽象类
 * @author lh
 *
 */
public abstract class AbstractJedisSessionDAO extends AbstractSessionDAO implements SessionDAO {
	
	protected String sessionKeyPrefix = "shiro_session_";

	public String getSessionKeyPrefix() {
		return sessionKeyPrefix;
	}

	public void setSessionKeyPrefix(String sessionKeyPrefix) {
		this.sessionKeyPrefix = sessionKeyPrefix;
	}
	

}
