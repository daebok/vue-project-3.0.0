package com.rd.ifaes.common.util;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

/**
 * 渤海 APP 加密解密工具类
 * 
 * @author qpz
 *
 */
public class CbhbAppUtil {
	
	private static final String AESTYPE = "AES/ECB/PKCS5Padding";
	
	/**
	 * APP 加密明文方法
	 * 
	 * @param keyStr
	 * @param plainText
	 * @return
	 */
	public static String AES_Encrypt(String keyStr, String plainText) {
		byte[] encrypt = null;
		try {
			Key key = generateKey(keyStr);
			Cipher cipher = Cipher.getInstance(AESTYPE);
			cipher.init(Cipher.ENCRYPT_MODE, key);
			encrypt = cipher.doFinal(plainText.getBytes());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new String(Base64.encodeBase64(encrypt));
	}
	
	/**
	 * 
	 * @param key
	 * @return
	 * @throws Exception
	 */
	private static Key generateKey(String key) throws Exception {
		try {
			SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), "AES");
			return keySpec;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	
	/**
	 * 解密
	 * @param keyStr
	 * @param encryptData
	 * @return
	 */
	public static String AES_Decrypt(String keyStr, String encryptData) {
	        byte[] decrypt = null; 
	        try{ 
	            Key key = generateKey(keyStr); 
	            Cipher cipher = Cipher.getInstance(AESTYPE); 
	            cipher.init(Cipher.DECRYPT_MODE, key); 
	            decrypt = cipher.doFinal(Base64.decodeBase64(encryptData.getBytes())); 
	        }catch(Exception e){ 
	            e.printStackTrace(); 
	        } 
	        return new String(decrypt).trim(); 
	    }

}
