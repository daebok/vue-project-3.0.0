package com.rd.ifaes.common.util.rsa;



import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPublicKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.x509.RSAPublicKeyStructure;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rd.ifaes.common.exception.BussinessException;
import com.rd.ifaes.common.util.code.BASE64Encoder;
import com.rd.util.base64.RdBase64Util;

/**
 * rsa签名辅助工具类
 * 官方网站：www.erongdu.com
 * 研发中心：rdc@erongdu.com
 * 未经授权不得进行修改、复制、出售及商业使用
 */
@SuppressWarnings("deprecation")
public class RSAUtil {

	private static final Logger LOGGER = LoggerFactory.getLogger(RSAUtil.class);
	private RSAUtil() {	}
	
	/**
	 * x509格式公钥转换为Der格式
	 *
	 * @param x509PublicKey x509格式公钥字符串
	 * @return Der格式公钥字符串
	 */
	public static String getRsaPublicKeyDerFromX509(String x509PublicKey) {
		ASN1InputStream aIn = null;
		try {
			aIn = new ASN1InputStream(hexString2ByteArr(x509PublicKey));
			SubjectPublicKeyInfo info = SubjectPublicKeyInfo.getInstance(aIn.readObject());
			RSAPublicKeyStructure struct = RSAPublicKeyStructure.getInstance(info.getPublicKey());
			return byteArr2HexString(struct.getEncoded());
		} catch (IOException e) {
			LOGGER.warn(e.getMessage(),e);
		}finally{
			if (aIn != null){
				try {
					aIn.close();
				} catch (IOException e) {
					LOGGER.warn(e.getMessage(),e);
				}
			}
		}
		return null;
	}

	/**
	 * 从Der格式公钥生成公钥
	 *
	 * @param publicKeyDer Der格式公钥字符串
	 * @return 公钥
	 */
	public static PublicKey generatePublicKeyFromDer(String publicKeyDer) {
		ASN1InputStream aIn = null;
		try {
			aIn = new ASN1InputStream(hexString2ByteArr(publicKeyDer));
			RSAPublicKeyStructure pStruct = RSAPublicKeyStructure.getInstance(aIn.readObject());
			RSAPublicKeySpec spec = new RSAPublicKeySpec(pStruct.getModulus(), pStruct.getPublicExponent());
			KeyFactory kf = KeyFactory.getInstance("RSA");
			return kf.generatePublic(spec);
		} catch (Exception e) {
			LOGGER.warn(e.getMessage(),e);
		}finally{
			if (aIn != null){
				try {
					aIn.close();
				} catch (IOException e) {
					LOGGER.warn(e.getMessage(),e);
				}
			}
		}
		return null;
	}
	
	/**
	 * 生成私钥文件
	 * @param privateKeyStr
	 * @return
	 */
	public static PrivateKey generatePrivateKey(String privateKeyStr) {
		try {
			byte[] buffer = RSAUtil.hexString2ByteArr(privateKeyStr);
			PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(buffer);
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			PrivateKey key = keyFactory.generatePrivate(keySpec);
			return key;
		} catch (Exception e) {
			LOGGER.warn(e.getMessage(),e);
			return null;
		}
	}
	
	/**
	 * 私钥加密
	 * @param privateKeyStr 私钥字符串
	 * @param str 加密原串
	 * @return
	 * @throws Exception
	 */
	public static String encrypt(String privateKeyStr, String str) throws BussinessException {
		PrivateKey privateKey = RSAUtil.generatePrivateKey(privateKeyStr);
		if (privateKey == null) {
			throw new BussinessException("加密私钥为空, 请设置");
		}
		Cipher cipher = null;
		try {
			// 使用默认RSA
			cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.ENCRYPT_MODE, privateKey);
			byte[] output = cipher.doFinal(str.getBytes(Charset.forName("UTF-8")));
			// byte数组base64编码后存在非法字符，所以需要再base64编码一次
			return RdBase64Util.base64Encode(RdBase64Util.base64Encode(output));
		} catch (NoSuchAlgorithmException e) {
			throw new BussinessException("无此加密算法");
		} catch (NoSuchPaddingException e) {
			LOGGER.warn(e.getMessage(),e);
			return null;
		} catch (InvalidKeyException e) {
			throw new BussinessException("加密私钥非法,请检查");
		} catch (IllegalBlockSizeException e) {
			throw new BussinessException("明文长度非法");
		} catch (BadPaddingException e) {
			throw new BussinessException("明文数据已损坏");
		}
	}
	
	/**
	 * 公钥解密
	 * @param publicKeyStr 公钥字符串
	 * @param encryptStr 密文
	 * @return
	 * @throws Exception
	 */
	public static String decrypt(final String publicKeyStr,final String encryptStr) throws BussinessException {
		// 因为密文为base64编码后数据，所以需要先base64解码
		String tempEncryptStr = encryptStr;
		tempEncryptStr = RdBase64Util.base64Decode(tempEncryptStr);
		PublicKey publicKey = RSAUtil.generatePublicKeyFromDer(publicKeyStr);
		if (publicKey == null) {
			throw new BussinessException("解密公钥为空, 请设置");
		}
		Cipher cipher = null;
		try {
			// 使用默认RSA
			cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.DECRYPT_MODE, publicKey);
			byte[] output = cipher.doFinal(RdBase64Util.base64DecodeToArray(tempEncryptStr));
			return new String(output);
		} catch (NoSuchAlgorithmException e) {
			throw new BussinessException("无此解密算法");
		} catch (NoSuchPaddingException e) {
			LOGGER.warn(e.getMessage(),e);
			return null;
		} catch (InvalidKeyException e) {
			throw new BussinessException("解密公钥非法,请检查");
		} catch (IllegalBlockSizeException e) {
			throw new BussinessException("密文长度非法");
		} catch (BadPaddingException e) {
			throw new BussinessException("密文数据已损坏");
		}
	}

	/**
	 * 字节数组转换为十六进制字符串
	 *
	 * @param byteArr 字节数组
	 * @return 十六进制字符串
	 */
	public static String byteArr2HexString(byte[] byteArr) {
		if (byteArr == null) {
			return "null";
		}
		StringBuilder sb = new StringBuilder();

		for (int k = 0; k < byteArr.length; k++) {
			if ((byteArr[k] & 0xFF) < 16) {
				sb.append("0");
			}
			sb.append(Integer.toString(byteArr[k] & 0xFF, 16));
		}
		return sb.toString();
	}

	/**
	 * 十六进制字符串转换为字节数组
	 *
	 * @param hexString 十六进制字符串
	 * @return 字节数组
	 */
	public static byte[] hexString2ByteArr(String hexString) {
		if ((hexString == null) || (hexString.length() % 2 != 0)) {
			return new byte[0];
		}

		byte[] dest = new byte[hexString.length() / 2];

		for (int i = 0; i < dest.length; i++) {
			String val = hexString.substring(2 * i, 2 * i + 2);
			dest[i] = (byte) Integer.parseInt(val, 16);
		}
		return dest;
	}
	
	/**
	 * 根据公钥文件读取验签公钥
	 * @param keyPath
	 * @return
	 */
	public static String getPublicKeyStr(String keyPath) {
		String key = "";
		try {
			CertificateFactory cf = CertificateFactory.getInstance("X.509");
			InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(keyPath);
			// 生成一个证书对象并使用从输入流 inStream 中读取的数据对它进行初始化。
			Certificate c = cf.generateCertificate(inputStream);
			PublicKey publicKey = c.getPublicKey();
			key = new BASE64Encoder().encodeBuffer(publicKey.getEncoded());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(),e);
		}
		return key;
	}
}
