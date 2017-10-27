package com.rd.ifaes.common.util;

import java.io.IOException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 页面表单解密工具类
 * <p>
 * JS版RSA加密请参考：https://github.com/travist/jsencrypt 
 * </p>
 * @author lh
 * @since 2017-05-16
 * @version 3.0
 *
 */
public class FormDecryptUtils {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(FormDecryptUtils.class);

	//private static final String BASE64PUBKEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCfU3jYhR8QzD+0x7SG/gGuQ9w3JOlJJ2CNbN8eAdiDKCPZ9gektP52tZE31txQ5NQraqW13+un16jJTs/iumQVw2n9L5ZmXCDZueNWrOwaaTA6vHs9KD9SgEVM6+7ml0NJpRBeBEr58Dtjzh51f6YDVn0nJeJ6pr0GdWXi3kNqMQIDAQAB";
	private static final String BASE64PRIKEY = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAJ9TeNiFHxDMP7THtIb+Aa5D3Dck6UknYI1s3x4B2IMoI9n2B6S0/na1kTfW3FDk1CtqpbXf66fXqMlOz+K6ZBXDaf0vlmZcINm541as7BppMDq8ez0oP1KARUzr7uaXQ0mlEF4ESvnwO2POHnV/pgNWfScl4nqmvQZ1ZeLeQ2oxAgMBAAECgYAXUQjzbu/v7mQ4Wa2Sv+ORFD9LFqzJVujraY5xfsWn1B0DDd1qfk5rIwFAkcImWIawX+gmaMG9C3OZGl6UCMESrr+0E7cQceyoz3vHuY2uRuJZq7yfnQyJKmPZ5kf+tY4T9DgfgYvt/J+xz9X5IGQYvUiLpZd9iXLrQYHqoehJbQJBAOOzxbIfJTlF5EU2dKDzM9uKdKeDiZvt4k6tXHM107fstk1rz/NA78IyorRs/uj9hPH82WIwfC9XNMwBhK6+0BMCQQCzIFj6HL0XuNqZoKcTb2O45eYpudIXl2Q2DPKu9pkrmh5fFnSqNhEa2fO/PaYktJTiL0vdm/hNX3KTEr2xdI0rAkEAkwJV+RIyri95mVX3JpLeQDe76Qr7pTiIi9NRhPCTqIOjj4iz0ZFzOiYG9gYI7dQAKVvd3Y8AHnBnHe89ArUfEQJBAIIDaJGhal5dfc0kHiCtKOR7eaOvjB4zdDkHDN6Rfnt3UbQSyHsC40dqCtE0HfNmXuoNCjO/kWoXbUHyyFyVDCECQD7TBTc9JDQ1fJItyL/QrqmXH4ixOjaHS5ZC0eeWxkHdlsTbzibonqk6YC9R0628DB+MYkCdi4rouvvS7V8f50o=";
	private static final String KEY_ALGORITHM = "RSA";
	
	/** 安全服务提供者 */
	private static final Provider PROVIDER = new BouncyCastleProvider();
	
	/**
	 * 解密
	 * @param text
	 *            Base64编码字符串
	 * @return 解密后的数据
	 */
	public static String decrypt(String text) {
		byte[] data = null;
		try {
			Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding", PROVIDER);
			//cipher.init(Cipher.DECRYPT_MODE, RSAUtils.getKeyPair().getPrivate());
			cipher.init(Cipher.DECRYPT_MODE, getPrivateKey(BASE64PRIKEY));
			data = cipher.doFinal(Base64.decodeBase64(text));
		} catch (Exception e) {
			LOGGER.error(e.getMessage(),e);
			return null;
		}
		return data != null ? new String(data) : null;
	}
	
	/**
	 * 解密
	 * @param text
	 *            Base64编码字符串
	 * @return 解密后的数据
	 */
	public static String decrypt(String text, String privateKey) {
		byte[] data = null;
		try {
			Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding", PROVIDER);
			//cipher.init(Cipher.DECRYPT_MODE, RSAUtils.getKeyPair().getPrivate());
			cipher.init(Cipher.DECRYPT_MODE, getPrivateKey(privateKey));
			data = cipher.doFinal(Base64.decodeBase64(text));
		} catch (Exception e) {
			LOGGER.error(e.getMessage(),e);
			return null;
		}
		return data != null ? new String(data) : null;
	}
	
	/**
	 * 取得公钥对象
	 * @param key	公钥key
	 * @return
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 */
	public static PublicKey getPublicKey(String key){  
		// 解密由base64编码的公钥     
        byte[] keyBytes = Base64.decodeBase64(key.getBytes());     
        // 构造X509EncodedKeySpec对象     
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        try{
        	// KEY_ALGORITHM 指定的加密算法     
        	KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);         	
        	return keyFactory.generatePublic(keySpec);          	
        } catch (NoSuchAlgorithmException|InvalidKeySpecException e) {
			LOGGER.error(e.getMessage(),e);
			return null;
		}
    }
	/**
	 * 取得私钥对象
	 * @param key	私钥key
	 * @return
	 * @throws Exception
	 */
	public static PrivateKey getPrivateKey(String key){
		byte[] keyBytes = Base64.decodeBase64(key);
		PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
		KeyFactory keyFactory;
		try {
			keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
			return keyFactory.generatePrivate(keySpec);
		} catch (NoSuchAlgorithmException|InvalidKeySpecException e) {
			LOGGER.error(e.getMessage(),e);
			return null;
		}
	}
	
	/**
	 * 取得公钥key(base64加密)
	 * @return
	 */
	public static String getPublicKey(){
		try {
			return new String(Base64.encodeBase64(RSAUtils.getKeyPair().getPublic().getEncoded()));
		} catch (ClassNotFoundException | IOException e) {
			LOGGER.error(e.getMessage(),e);
			return null;
		}
	}
	
	/**
	 * 取得私钥key(base64加密)
	 * @return
	 */
	public static String getPrivateKey(){
		try {
			return new String(Base64.encodeBase64(RSAUtils.getKeyPair().getPrivate().getEncoded()));
		} catch (ClassNotFoundException | IOException e) {
			LOGGER.error(e.getMessage(),e);
			return null;
		}
	}

}
