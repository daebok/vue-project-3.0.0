package com.rd.ifaes.core.core.sign.cfca.connector;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rd.ifaes.core.core.constant.ConfigConstant;
import com.rd.ifaes.core.core.sign.cfca.constant.MIMEType;
import com.rd.ifaes.core.core.sign.cfca.constant.Request;
import com.rd.ifaes.core.core.sign.cfca.constant.SystemConst;
import com.rd.ifaes.core.core.sign.cfca.util.CommonUtil;
import com.rd.ifaes.core.core.util.ConfigUtils;

/**
 * 
 * @author jxx
 *
 */
public class HttpConnector {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(HttpConnector.class);

    public static final String JKS_PATH = HttpConnector.class.getResource("client_rdkj.jks").getPath();
    public static final String JKS_PWD = ConfigUtils.getValue(ConfigConstant.CFC_PASS);
    public static final String ALIAS = "server";

	private String url = ConfigUtils.getValue(ConfigConstant.CFC_URL);
    public static final int connectTimeout = 3000;
    public static final int readTimeout = 10000;
    private String channel = "Test";
    private String keyStorePath = JKS_PATH;
    private String keyStorePassword = JKS_PWD;
    private String trustStorePath = JKS_PATH;
    private String trustStorePassword = JKS_PWD;

    private HttpClient httpClient;

    /**
     * 初始化
     */
    public void init() {
        httpClient = new HttpClient();
        httpClient.config.connectTimeout = connectTimeout;
        httpClient.config.readTimeout = readTimeout;
        httpClient.httpConfig.userAgent = "TrustSign FEP";
        httpClient.httpConfig.contentType = MIMEType.FORM;
        httpClient.httpConfig.accept = MIMEType.JSON;
        try {
            httpClient.initSSL(keyStorePath, keyStorePassword.toCharArray(), trustStorePath, trustStorePassword.toCharArray());
        } catch (Exception e) {
        	LOGGER.warn(e.getMessage(), e);
        }
        if (!url.endsWith("/")) {
            url += "/";
        }
    }

    /**
     * 
     * @param uri
     * @param data
     * @param signature
     * @return
     */
    public String post(String uri, String data, String signature) {
        return deal(uri, "POST", prepare(data, signature, null));
    }

    public String post(String uri, String data, String signature, Map<String, String> map) {
        return deal(uri, "POST", prepare(data, signature, map));
    }
    
    public String post(String uri, String data, String signature, File file) {
        return deal(uri, "POST", data, file, signature);
    }

    /**
     * 
     */
    public byte[] getFile(String uri) {
        HttpURLConnection connection = null;
        try {
            connection = httpClient.connect(url + uri, "GET");
            int responseCode = httpClient.send(connection, null);
            LOGGER.debug("responseCode:{}", responseCode);

            return httpClient.receive(connection);
        } catch (Exception e) {
        	LOGGER.warn(e.getMessage(), e);
            return new byte[0];
        } finally {
            httpClient.disconnect(connection);
        }
    }

    private String prepare(String data, String signature, Map<String, String> map) {
        try {
            StringBuilder request = new StringBuilder();
            request.append(Request.CHANNEL).append("=").append(URLEncoder.encode(channel, SystemConst.DEFAULT_CHARSET));
            if (CommonUtil.isNotEmpty(data)) {
                request.append("&").append(Request.DATA).append("=").append(URLEncoder.encode(data, SystemConst.DEFAULT_CHARSET));
            }
            if (CommonUtil.isNotEmpty(signature)) {
                request.append("&").append(Request.SIGNATURE).append("=").append(URLEncoder.encode(signature, SystemConst.DEFAULT_CHARSET));
            }
            if (CommonUtil.isNotEmpty(map)) {
                for (Entry<String, String> pair : map.entrySet()) {
                    request.append("&").append(pair.getKey()).append("=")
                            .append(pair.getValue() == null ? "" : URLEncoder.encode(pair.getValue(), SystemConst.DEFAULT_CHARSET));
                }
            }
            return request.toString();
        } catch (UnsupportedEncodingException e) {
        	LOGGER.warn(e.getMessage(), e);
            return null;
        }
    }

    private String deal(String uri, String method, String request) {
        HttpURLConnection connection = null;
        try {
            connection = httpClient.connect(url + uri, method);
            int responseCode = httpClient.send(connection, request == null ? null : CommonUtil.getBytes(request));
            LOGGER.debug("responseCode:{}", responseCode);
            return CommonUtil.getString(httpClient.receive(connection));
        } catch (Exception e) {
        	LOGGER.warn(e.getMessage(), e);
            return e.getMessage();
        } finally {
            httpClient.disconnect(connection);
        }
    }

    private String deal(String uri, String method, String request, File file, String signature) {
        HttpURLConnection connection = null;
        try {
            connection = httpClient.connect(url + uri, method);
            LOGGER.debug("url,{},uri", url , uri);
            LOGGER.debug("method,{}",method);
            LOGGER.debug("request,{}",request);
            int responseCode = httpClient.send(connection, request == null ? null : CommonUtil.getBytes(request), file, signature);
            LOGGER.debug("responseCode:{}", responseCode);
            return CommonUtil.getString(httpClient.receive(connection));
        } catch (Exception e) {
            LOGGER.warn(e.getMessage(), e);
            return e.getMessage();
        } finally {
            httpClient.disconnect(connection);
        }
    }
}
