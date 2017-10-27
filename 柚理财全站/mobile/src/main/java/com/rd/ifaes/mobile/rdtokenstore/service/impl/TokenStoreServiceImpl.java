package com.rd.ifaes.mobile.rdtokenstore.service.impl;

import java.util.HashMap;

import org.springframework.stereotype.Service;

import com.rd.ifaes.core.base.service.BaseServiceImpl;
import com.rd.ifaes.mobile.rdtokenstore.domain.TokenStore;
import com.rd.ifaes.mobile.rdtokenstore.mapper.TokenStoreMapper;
import com.rd.ifaes.mobile.rdtokenstore.service.TokenStoreService;

/**
 * ServiceImpl:RdTokenStoreServiceImpl
 * @author lgx
 * @version 3.0
 * @date 2016-10-10
 */
@Service("rdTokenStoreService") 
public class TokenStoreServiceImpl  extends BaseServiceImpl<TokenStoreMapper, TokenStore> implements TokenStoreService{
	@Override
	public TokenStore getRefreshTokenStore(String userId ,String refreshToken) {
		HashMap<String, String> map=new HashMap<String, String>();
		map.put("bindingId", userId);
		map.put("refreshToken", refreshToken);
		return dao.getRefreshTokenStore(map);
	}
	
	@Override
	public void removeOldToken(String bindingId) {
		dao.removeOldToken(bindingId);
	}
	
	@Override
	public TokenStore getOauthTokenStore(String oauth_token) {
		return dao.getOauthTokenStore(oauth_token);
	}
	
	@Override
	public TokenStore getOauthTokenStoreByUserId(String bindingId,
			String clientId,String oauthToken) {
		HashMap<String, String> map=new HashMap<String, String>();
		map.put("bindingId", bindingId);
		map.put("clientId", clientId);
		map.put("oauthToken", oauthToken);
		return dao.getOauthTokenStoreByUserId(map);
	}
}