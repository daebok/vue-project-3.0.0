package com.rd.ifaes.common.security.shiro;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.SimpleSession;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Sets;
import com.rd.ifaes.common.dict.ExpireTime;
import com.rd.ifaes.common.entity.BaseEntity;
import com.rd.ifaes.common.util.CommonConstants;
import com.rd.ifaes.common.util.DateUtils;
import com.rd.ifaes.common.util.PropertiesUtils;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.common.web.ServletUtils;
import com.rd.ifaes.core.core.constant.Constant;
import com.rd.ifaes.core.core.util.CacheUtils;

/**
 * 自定义授权会话管理类
 */
public class JedisSessionDAO extends AbstractJedisSessionDAO{

	private static final Logger LOGGER = LoggerFactory.getLogger(JedisSessionDAO.class);

	@Override
	public void update(Session session) throws UnknownSessionException {
		if (session == null || session.getId() == null) {
			return;
		}

		HttpServletRequest request = ServletUtils.getRequest();
		if (request != null) {
			String uri = request.getServletPath();
			// 如果是静态文件，则不更新SESSION
			if (ServletUtils.isStaticFile(uri)) {
				return;
			}
			// 如果是视图文件，则不更新SESSION
			if (StringUtils.startsWith(uri, PropertiesUtils.getValue("web.view.prefix"))
					&& StringUtils.endsWith(uri, PropertiesUtils.getValue("web.view.suffix"))) {
				return;
			}
			// 手动控制不更新SESSION
			if (CommonConstants.NO.equals(request.getParameter("updateSession"))) {
				return;
			}
		}

		try {
			// 获取登录者编号
			PrincipalCollection pc = (PrincipalCollection) session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
			BaseEntity<?> operator = pc==null?null:(BaseEntity<?>)pc.getPrimaryPrincipal();//后台用户id
			BaseEntity<?> user = (BaseEntity<?>) session.getAttribute(Constant.SESSION_USER);//前台用户id
			
			String principalId = user != null ? user.getUuid() : StringUtils.EMPTY;
			principalId = operator != null ? operator.getUuid() : principalId;
			
			//防止频繁更新
			//if(StringUtils.isNotBlank(principalId) && CacheUtils.setnx(sessionKeyPrefix+":nx:"+principalId, "1", ExpireTime.TWO_SEC)){
				CacheUtils.hset(sessionKeyPrefix, session.getId().toString(),
						principalId + "|" + session.getTimeout() + "|" + session.getLastAccessTime().getTime());
				
				// 设置超期时间
				int timeoutSeconds = (int) (session.getTimeout() / 1000);
				CacheUtils.set(sessionKeyPrefix + session.getId(), session, timeoutSeconds);				
			//}

			LOGGER.debug("update {} {}", session.getId(), request != null ? request.getRequestURI() : "");
		} catch (Exception e) {
			LOGGER.error("update {} {}", session.getId(), request != null ? request.getRequestURI() : "", e);
		}
	}

	@Override
	public void delete(Session session) {
		if (session == null || session.getId() == null) {
			return;
		}

		try {
			CacheUtils.hdel(sessionKeyPrefix, session.getId().toString());
			CacheUtils.del(sessionKeyPrefix + session.getId());
			LOGGER.debug("delete {} ", session.getId());
		} catch (Exception e) {
			LOGGER.error("delete {} ", session.getId(), e);
		} 
	}

	@Override
	public Collection<Session> getActiveSessions() {
		return getActiveSessions(true);
	}

	/**
	 * 获取活动会话
	 * 
	 * @param includeLeave 是否包括离线（最后访问时间大于3分钟为离线会话）
	 * @return
	 */
	@Override
	public Collection<Session> getActiveSessions(boolean includeLeave) {
		return getActiveSessions(includeLeave, null, null);
	}

	/**
	 * 获取活动会话
	 * 
	 * @param includeLeave 是否包括离线（最后访问时间大于3分钟为离线会话）
	 * @param principal 根据登录者对象获取活动会话
	 * @param filterSession 不为空，则过滤掉（不包含）这个会话。
	 * @return
	 */
	@Override
	public Collection<Session> getActiveSessions(boolean includeLeave, Object principal, Session filterSession) {
		Set<Session> sessions = Sets.newHashSet();

		try {
			Map<String, String> map = CacheUtils.mget(sessionKeyPrefix, String.class);
			for (Map.Entry<String, String> e : map.entrySet()) {
				String value = e.getValue();
				if (StringUtils.isNotBlank(e.getKey()) && StringUtils.isNotBlank(value)) {

					String[] ss = StringUtils.split(value, "|");
					if (ss != null && ss.length == 3) {// jedis.exists(sessionKeyPrefix
														// + e.getKey())){
						// Session session =
						// (Session)JedisUtils.toObject(jedis.get(JedisUtils.getBytesKey(sessionKeyPrefix
						// + e.getKey())));
						SimpleSession session = new SimpleSession();
						session.setId(e.getKey());
						session.setAttribute("principalId", ss[0]);
						session.setTimeout(Long.valueOf(ss[1]));
						session.setLastAccessTime(new Date(Long.valueOf(ss[2])));
						try {
							// 验证SESSION
							session.validate();

							boolean isActiveSession = false;
							// 不包括离线并符合最后访问时间小于等于3分钟条件。
							if (includeLeave || DateUtils.pastMinutes(session.getLastAccessTime()) <= 3) {
								isActiveSession = true;
							}
							// 符合登陆者条件。
							if (principal != null) {
								PrincipalCollection pc = (PrincipalCollection) session
										.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
								if (principal.toString()
										.equals(pc != null ? pc.getPrimaryPrincipal().toString() : StringUtils.EMPTY)) {
									isActiveSession = true;
								}
							}
							// 过滤掉的SESSION
							if (filterSession != null && filterSession.getId().equals(session.getId())) {
								isActiveSession = false;
							}
							if (isActiveSession) {
								sessions.add(session);
							}

						}
						// SESSION验证失败
						catch (Exception e2) {
							CacheUtils.hdel(sessionKeyPrefix, e.getKey());
						}
					}
					// 存储的SESSION不符合规则
					else {
						CacheUtils.hdel(sessionKeyPrefix, e.getKey());
					}
				}
				// 存储的SESSION无Value
				else if (StringUtils.isNotBlank(e.getKey())) {
					CacheUtils.hdel(sessionKeyPrefix, e.getKey());
				}
			}
			LOGGER.debug("getActiveSessions size: {} ", sessions.size());
		} catch (Exception e) {
			LOGGER.error("getActiveSessions", e);
		}
		return sessions;
	}

	@Override
	protected Serializable doCreate(Session session) {
		HttpServletRequest request = ServletUtils.getRequest();
		if (request != null) {
			String uri = request.getServletPath();
			// 如果是静态文件，则不创建SESSION
			if (ServletUtils.isStaticFile(uri)) {
				return null;
			}
		}
		Serializable sessionId = this.generateSessionId(session);
		this.assignSessionId(session, sessionId);
		this.update(session);
		return sessionId;
	}

	@Override
	protected Session doReadSession(Serializable sessionId) {

		Session s = null;
		HttpServletRequest request = ServletUtils.getRequest();
		if (request != null) {
			String uri = request.getServletPath();
			// 如果是静态文件，则不获取SESSION
			if (ServletUtils.isStaticFile(uri)) {
				return null;
			}
			s = (Session) request.getAttribute("session_" + sessionId);
		}
		if (s != null) {
			return s;
		}

		Session session = null;
		try {
			session = CacheUtils.get(sessionKeyPrefix + sessionId, Session.class);
			LOGGER.debug("doReadSession {} {}", sessionId, request != null ? request.getRequestURI() : "");
		} catch (Exception e) {
			LOGGER.error("doReadSession {} {}", sessionId, request != null ? request.getRequestURI() : "", e);
		}

		if (request != null && session != null) {
			request.setAttribute("session_" + sessionId, session);
		}

		return session;
	}

	@Override
	public Session readSession(Serializable sessionId) throws UnknownSessionException {
		try {
			return super.readSession(sessionId);
		} catch (UnknownSessionException e) {
			return null;
		}
	}

}