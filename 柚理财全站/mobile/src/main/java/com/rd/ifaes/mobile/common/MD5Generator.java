package com.rd.ifaes.mobile.common;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

public class MD5Generator {
	public String generateValue() {
		return generateValue(UUID.randomUUID().toString());
	}

	public String generateValue(String param) {
		StringBuffer hexString = new StringBuffer();
		try {
			MessageDigest algorithm = MessageDigest.getInstance("MD5");

			algorithm.reset();
			algorithm.update(param.getBytes());
			byte[] messageDigest = algorithm.digest();

			for (int i = 0; i < messageDigest.length; i++)
				hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		return hexString.toString();
	}
}
