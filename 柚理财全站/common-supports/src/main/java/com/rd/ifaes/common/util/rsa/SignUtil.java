package com.rd.ifaes.common.util.rsa;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * ras生成签名和验签的工具类
 * 官方网站：www.erongdu.com
 * 研发中心：rdc@erongdu.com
 * 未经授权不得进行修改、复制、出售及商业使用
 */
public class SignUtil {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SignUtil.class);

	/**
	 * 生成签名的方法,必须为utf-8格式
	 * @param str  签名明文字符串
	 * @param privateKey  私钥
	 * @return
	 */
	public static String subsign(final String str, final String privateKey){
		LOGGER.debug("签名明文：{}",str);
		LOGGER.debug("签名私钥：{}",privateKey);
		String signature = null;
		try {
			byte[] prikeybytes = RSAUtil.hexString2ByteArr(privateKey);
			// 构造PKCS8EncodedKeySpec对象
			PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(prikeybytes);
			// 指定的加密算法
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			// 取私钥匙对象
			PrivateKey privatekey = keyFactory.generatePrivate(pkcs8KeySpec);
			Signature instance = Signature.getInstance("SHA1withRSA");
			instance.initSign(privatekey);
			byte[] digest = str.getBytes("UTF-8");
			instance.update(digest);
			byte[] sign = instance.sign();
			signature = RSAUtil.byteArr2HexString(sign);
		} catch (Exception e) {
			LOGGER.error("签名参数sign："+signature, e);
		}
		LOGGER.debug("签名参数sign：{}",signature);
		return signature;
	}
	
	/**
	 * 校验签名参数,必须为utf-8格式
	 * @param str  签名明文
	 * @param sign  签名参数
	 * @param pubKey 公钥
	 * @return 返回true和false，true代表验签通过，false代表验签失败
	 */
	public static boolean checkSign(final String str, final String sign, final String pubKey){
		LOGGER.debug("验签明文：{}",str);
		LOGGER.debug("签名参数sign：{}",sign);
		LOGGER.debug("签名公钥：{}",pubKey);
		boolean flag = false;
		try {
			Signature instance = Signature.getInstance("SHA1withRSA");
			instance.initVerify(RSAUtil.generatePublicKeyFromDer(pubKey));
			byte[] digest = str.getBytes("UTF-8");
			instance.update(digest);
			flag = instance.verify(RSAUtil.hexString2ByteArr(sign));
		} catch (Exception e) {
			LOGGER.error(e.getMessage(),e);
		}
		return flag;
	}
	
}
