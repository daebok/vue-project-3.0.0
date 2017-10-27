package  com.rd.ifaes.mobile.rdtokenstore.service;

import com.rd.ifaes.core.base.service.BaseService;
import com.rd.ifaes.mobile.rdtokenstore.domain.TokenStore;

public interface TokenStoreService extends BaseService<TokenStore>{
	/**
	 * 根据refresh_token获取RdTokenStore
	 * 
	 * @param refresh_token
	 * @return
	 */
	public TokenStore getRefreshTokenStore(String userId ,String refreshToken);

	/**
	 * 根据id删除旧的token
	 * 
	 * @param entity
	 */
	public void removeOldToken(String bindingId);

	/**
	 * 根据oauth_token获取RdTokenStore
	 * 
	 * @param oauth_token
	 * @return
	 */
	public TokenStore getOauthTokenStore(String paramString);

	/**
	 * 根据user_id获取RdTokenStore
	 * 
	 * @param oauth_token
	 * @return
	 */
	public TokenStore getOauthTokenStoreByUserId(String userId , String clientType,String oauthToken);
}
