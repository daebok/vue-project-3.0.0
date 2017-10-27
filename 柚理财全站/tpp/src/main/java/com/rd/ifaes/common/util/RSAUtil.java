/**
 * 互联网金融交易系统ifaes (Internet Finance  Assets  Exchange  System)
 * Copyright (c) 2016 杭州融都科技股份有限公司 版权所有
 */
package com.rd.ifaes.common.util;

import java.security.interfaces.RSAPrivateKey;

import javax.crypto.Cipher;

/**
 * 渤海银行 私钥解密方法
 * @version 3.0
 * @author QianPengZhan
 * @date 2017年3月16日
 */
public class RSAUtil {
	
	/**
	 * 私钥解密
	 * @author QianPengZhan
	 * @date 2017年3月16日
	 * @param data
	 * @param privateKey
	 * @return
	 * @throws Exception
	 */
	public static String decryptByPrivateKey(String data, RSAPrivateKey privateKey)  throws Exception{
	    Cipher cipher = Cipher.getInstance("RSA");
	    cipher.init(2, privateKey);

	    int key_len = privateKey.getModulus().bitLength() / 8;
	    byte[] bytes = data.getBytes();
	    byte[] bcd = ASCII_To_BCD(bytes, bytes.length);
	    System.err.println(bcd.length);

	    String ming = "";
	    byte[][] arrays = splitArray(bcd, key_len);
	    for (byte[] arr : arrays) {
	      ming = ming + new String(cipher.doFinal(arr),"GBK");
	    }
	    return ming;
	}
	
	public static byte[] ASCII_To_BCD(byte[] ascii, int asc_len){
	  byte[] bcd = new byte[asc_len / 2];
	  int j = 0;
	  for (int i = 0; i < (asc_len + 1) / 2; i++) {
	    bcd[i] = asc_to_bcd(ascii[(j++)]);
	    bcd[i] = ((byte)((j >= asc_len ? 0 : asc_to_bcd(ascii[(j++)])) + (bcd[i] << 4)));
	  }
	  return bcd;
	}
	
	public static byte asc_to_bcd(byte asc){
	    byte bcd;
	    if ((asc >= 48) && (asc <= 57)) {
	      bcd = (byte)(asc - 48);
	    }else{
	      if ((asc >= 65) && (asc <= 70)) {
	        bcd = (byte)(asc - 65 + 10);
	      }else{
	        if ((asc >= 97) && (asc <= 102))
	          bcd = (byte)(asc - 97 + 10);
	        else
	          bcd = (byte)(asc - 48); 
	      }
	    }
	    return bcd;
	  }
	
	public static byte[][] splitArray(byte[] data, int len){
	    int x = data.length / len;
	    int y = data.length % len;
	    int z = 0;
	    if (y != 0) {
	      z = 1;
	    }
	    byte[][] arrays = new byte[x + z][];

	    for (int i = 0; i < x + z; i++) {
	      byte[] arr = new byte[len];
	      if ((i == x + z - 1) && (y != 0))
	        System.arraycopy(data, i * len, arr, 0, y);
	      else {
	        System.arraycopy(data, i * len, arr, 0, len);
	      }
	      arrays[i] = arr;
	    }
	    return arrays;
	  }
}
