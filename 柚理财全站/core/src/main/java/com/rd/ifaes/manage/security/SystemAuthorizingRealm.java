package com.rd.ifaes.manage.security;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.PostConstruct;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.collect.Maps;
import com.rd.ifaes.common.dict.ExpireTime;
import com.rd.ifaes.common.exception.BussinessException;
import com.rd.ifaes.common.security.shiro.UsernamePasswordToken;
import com.rd.ifaes.common.util.EncodeUtils;
import com.rd.ifaes.common.util.PasswordHelper;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.core.core.constant.Constant;
import com.rd.ifaes.core.core.constant.ResourceConstant;
import com.rd.ifaes.core.core.util.CacheUtils;
import com.rd.ifaes.core.core.util.ConfigUtils;
import com.rd.ifaes.core.core.util.ResourceUtils;
import com.rd.ifaes.core.core.util.ValidateUtils;
import com.rd.ifaes.core.sys.domain.Operator;
import com.rd.ifaes.core.sys.service.OperatorService;

//@Service
public class SystemAuthorizingRealm extends AuthorizingRealm {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SystemAuthorizingRealm.class);
	
	@Autowired
	private OperatorService operatorService;
	
	/**
	 * 认证回调函数, 登录时调用
	 */
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) throws AuthenticationException {
		   UsernamePasswordToken token = (UsernamePasswordToken) authcToken;
		LOGGER.debug("login submit  username: {}" ,token.getUsername());
	    
		// 验证码校验
		String vCode = StringUtils.isNull(token.getCaptcha());
		if (vCode.isEmpty()) {
			throw new BussinessException(ResourceUtils.get(ResourceConstant.VALID_CODE_NOTEMPTY),
					BussinessException.TYPE_JSON);
		}

		if (!ValidateUtils.checkValidCode(vCode)) {
			throw new BussinessException(ResourceUtils.get(ResourceConstant.VALID_CODE_ERROR),
					BussinessException.TYPE_JSON);
		}
	    //账户校验
	    Operator user = operatorService.getByLoginName(token.getUsername());
	    if(user == null || (user.getDeleteFlag() != null && Operator.ROLE_EQ.equals(user.getDeleteFlag()))) {
            throw new UnknownAccountException();//没找到帐号
        }

        if(Operator.USER_STATUS_LOCKED.equals(user.getStatus())) {
            throw new LockedAccountException(); //帐号锁定
        }
        
        byte[] salt = EncodeUtils.decodeHex(user.getPwd().substring(0,16));
        return new SimpleAuthenticationInfo(
        		user, user.getPwd().substring(16),
        		ByteSource.Util.bytes(salt), getName());
	}
	/**
	 * 授权查询回调函数, 进行鉴权但缓存中无用户的授权信息时调用
	 */	
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection authcToken) {
		Operator user = (Operator)authcToken.getPrimaryPrincipal();		
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
	    info.setRoles(operatorService.findRoles(user.getUuid()));
	    info.setStringPermissions(operatorService.findPermissions(user.getUuid()));	    
	   	return info;
	}
	
	@Override
	public String getName() {
		return "systemAuthorizingRealm";
	}
	
    @Override
    public void clearCachedAuthorizationInfo(PrincipalCollection principals) {
        super.clearCachedAuthorizationInfo(principals);
    }

    @Override
    public void clearCachedAuthenticationInfo(PrincipalCollection principals) {
        super.clearCachedAuthenticationInfo(principals);
    }

    @Override
    public void clearCache(PrincipalCollection principals) {
        super.clearCache(principals);
    }

    public void clearAllCachedAuthorizationInfo() {
        getAuthorizationCache().clear();
    }

    public void clearAllCachedAuthenticationInfo() {
        getAuthenticationCache().clear();
    }

    public void clearAllCache() {
        clearAllCachedAuthenticationInfo();
        clearAllCachedAuthorizationInfo();
    }
	
	/**
	 * 设定密码校验的Hash算法与迭代次数
	 */
	@PostConstruct
	public void initCredentialsMatcher() {
		HashedCredentialsMatcher matcher = new RetryLimitHashedCredentialsMatcher(PasswordHelper.HASH_ALGORITHM);
		matcher.setHashIterations(PasswordHelper.HASH_INTERATIONS);
		setCredentialsMatcher(matcher);
	}
	
	 class RetryLimitHashedCredentialsMatcher extends HashedCredentialsMatcher {
		 
		 	public RetryLimitHashedCredentialsMatcher(String hashAlgorithmName) {
		      super(hashAlgorithmName);
		    }
		    @Override
		    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
		    	UsernamePasswordToken uptoken = (UsernamePasswordToken) token;
		    	String userName = uptoken.getUsername();
		        //retry count + 1
				@SuppressWarnings("unchecked")
				Map<String, AtomicInteger> loginFailMap =  (Map<String, AtomicInteger>)CacheUtils.get("loginFailMap", Map.class);
				if (loginFailMap==null){
					loginFailMap = Maps.newHashMap();
				}
				AtomicInteger loginFailNum = loginFailMap.get(userName);	
				if(loginFailNum==null){
					loginFailNum = new AtomicInteger(0);
				}
//				//if retry count > 5 throw
//		        if(loginFailNum.incrementAndGet() >= 3) {
//		            throw new ExcessiveAttemptsException("msg:您已连续3次输入密码错误，账户已被锁定");
//		        }
				int maxErrorCount = ConfigUtils.getInt(Constant.LOGIN_LOCK_NUMBER);//获取系统配置最大登录次数
		        loginFailMap.put(userName, loginFailNum);
		        CacheUtils.set("loginFailMap", loginFailMap, ExpireTime.ONE_DAY);
		        boolean matches = super.doCredentialsMatch(token, info);
		        if(matches) {
//		        	CacheUtils.del(userName);//原有的代码
		        	loginFailMap.remove(userName);//清掉最大登录次数
					CacheUtils.set("loginFailMap", loginFailMap, ExpireTime.ONE_DAY);
		        } else {//登陆失败
		        	loginFailNum.incrementAndGet();//失败次数+1
		        	 if(loginFailNum.get()>=maxErrorCount){
				        	Operator operator=operatorService.getByLoginName(userName);
				        	operator.setStatus(Operator.USER_STATUS_LOCKED);
				        	operator.setLockTime(new Date());
				        	operatorService.updateStatus(operator);
				        }
		        	loginFailMap.put(userName, loginFailNum);//把登录失败次数放到loginfailmap里面
		        	CacheUtils.set("loginFailMap", loginFailMap, ExpireTime.ONE_DAY);//把loginfailmap放到redis里面，并设置过期时间，即自动解冻时间
		        }
		        return matches;
		    }
		}
}
