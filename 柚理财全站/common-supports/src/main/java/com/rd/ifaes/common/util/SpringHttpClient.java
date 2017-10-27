package com.rd.ifaes.common.util;

import java.util.ArrayList;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;

public class SpringHttpClient {
	protected static final Logger LOGGER = LoggerFactory.getLogger(SpringHttpClient.class);

	@SuppressWarnings({ "rawtypes", "serial", "unchecked" })
	public static Map<String, Object> post(String clientURL, Map<String, Object> reqMap) {

		RestTemplate restTemplate = new RestTemplate(new ArrayList<HttpMessageConverter<?>>() {
			{
				add(new FastJsonHttpMessageConverter());
			}
		});
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("Accept-Charset", "UTF-8");
		headers.set("contentType", "application/json");
		try {
			trustAllHttpsCertificates();
		} catch (Exception e) {
			e.printStackTrace();
		}
		HttpsURLConnection.setDefaultHostnameVerifier(hv);

		HttpEntity entity = new HttpEntity(reqMap, headers);
		LOGGER.info("\r\n请求银行端流水号：{}{}{}", reqMap.get("txDate"), reqMap.get("txTime"), reqMap.get("seqNo"));
		LOGGER.info("\r\n(P2P-->即信端)请求信息：\r\n{}", JSON.toJSON(reqMap).toString().replace(",", ",\r\n"));

		LOGGER.info("\r\n(P2P-->即信端)发送请求至：\r\n{}", clientURL);

		// 请求到即信端
		ResponseEntity response = restTemplate.exchange(clientURL, HttpMethod.POST, entity, Map.class);

		// 响应报文
		LOGGER.info("\r\n(即信端-->P2P)响应报文：\r\n{}", response.getBody().toString().replace(",", ",\r\n"));
		Map responseMap = (Map) response.getBody();
		return responseMap;
	}
	
	@SuppressWarnings({ "rawtypes", "serial", "unchecked" })
	public static Map<String, Object> download(String clientURL, Map<String, Object> reqMap) {

		RestTemplate restTemplate = new RestTemplate(new ArrayList<HttpMessageConverter<?>>() {
			{
				add(new FastJsonHttpMessageConverter());
			}
		});
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("Accept-Charset", "UTF-8");
		headers.set("contentType", "application/json");
		try {
			trustAllHttpsCertificates();
		} catch (Exception e) {
			e.printStackTrace();
		}
		HttpsURLConnection.setDefaultHostnameVerifier(hv);

		HttpEntity entity = new HttpEntity(JSON.toJSON(reqMap), headers);//此处不一样
		LOGGER.info("\r\n请求银行端流水号：{}{}{}", reqMap.get("txDate"), reqMap.get("txTime"), reqMap.get("seqNo"));
		LOGGER.info("\r\n(P2P-->即信端)请求信息：\r\n{}", JSON.toJSON(reqMap).toString().replace(",", ",\r\n"));

		LOGGER.info("\r\n(P2P-->即信端)发送请求至：\r\n{}", clientURL);

		// 请求到即信端
		ResponseEntity response = restTemplate.exchange(clientURL, HttpMethod.POST, entity, Map.class);

		// 响应报文
		LOGGER.info("\r\n(即信端-->P2P)响应报文：\r\n{}", response.getBody().toString().replace(",", ",\r\n"));
		Map responseMap = (Map) response.getBody();
		return responseMap;
	}

	static HostnameVerifier hv = new HostnameVerifier() {
		public boolean verify(String urlHostName, SSLSession session) {
			LOGGER.info("Warning: URL Host: " + urlHostName + " vs. " + session.getPeerHost());
			return true;
		}
	};

	private static void trustAllHttpsCertificates() throws Exception {
		javax.net.ssl.TrustManager[] trustAllCerts = new javax.net.ssl.TrustManager[1];
		javax.net.ssl.TrustManager tm = new miTM();
		trustAllCerts[0] = tm;
		javax.net.ssl.SSLContext sc = javax.net.ssl.SSLContext.getInstance("SSL");
		sc.init(null, trustAllCerts, null);
		javax.net.ssl.HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
	}

	static class miTM implements javax.net.ssl.TrustManager, javax.net.ssl.X509TrustManager {
		public java.security.cert.X509Certificate[] getAcceptedIssuers() {
			return null;
		}

		public boolean isServerTrusted(java.security.cert.X509Certificate[] certs) {
			return true;
		}

		public boolean isClientTrusted(java.security.cert.X509Certificate[] certs) {
			return true;
		}

		public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType)
				throws java.security.cert.CertificateException {
			return;
		}

		public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType)
				throws java.security.cert.CertificateException {
			return;
		}

	}
}
