package  com.rd.ifaes.mobile.rdtokenstore.mapper;

import java.util.HashMap;

import com.rd.ifaes.core.base.mapper.BaseMapper;
import com.rd.ifaes.mobile.rdtokenstore.domain.TokenStore;

public interface TokenStoreMapper extends BaseMapper<TokenStore>{
	/**
	 * 根据refresh_token获取RdTokenStore
	 * 
	 * @param refresh_token
	 * @return
	 */
	public TokenStore getRefreshTokenStore(HashMap<String, String> map);

	/**
	 * 根据id删除旧的token
	 * 
	 * @param entity
	 */
	public void removeOldToken(TokenStore paramRdTokenStore);

	/**
	 * 根据bindingId删除旧的token
	 */
	public void removeOldToken(String bindingId);

	/**
	 * 根据oauth_token获取RdTokenStore
	 * 
	 * @param oauth_token
	 * @return
	 */
	public TokenStore getOauthTokenStore(String oauthToken);

	/**
	 * 根据user_id获取RdTokenStore
	 * 
	 * @param oauth_token
	 * @return
	 */
	public TokenStore getOauthTokenStoreByUserId(HashMap<String, String> map);

	/**
	 * 添加token记录
	 * 
	 * @param entity
	 */
	public void add(TokenStore paramRdTokenStore);
}
