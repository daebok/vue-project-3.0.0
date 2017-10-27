package com.rd.ifaes.mobile.wechat.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.URL;
import java.security.SecureRandom;
import java.util.Random;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WeixinHttpUtil
{
  private static Logger log = LoggerFactory.getLogger(WeixinHttpUtil.class);

  public static String getUrl(String url){
      String result = null;
      try {
          // 根据地址获取请求
          HttpGet request = new HttpGet(url);
          // 获取当前客户端对象
          HttpClient httpClient = new DefaultHttpClient();
          // 通过请求对象获取响应对象
          HttpResponse response = httpClient.execute(request);
          
          // 判断网络连接状态码是否正常(0--200都数正常)
          if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
        	  log.info( (String) response.getParams().getParameter("code")+"--------------------------------------");
        	  log.info( (String) response.getParams().getParameter("code")+"--------------------------------------");
        	  log.info( (String) response.getParams().getParameter("code")+"--------------------------------------");
              result= EntityUtils.toString(response.getEntity());
          } 
      } catch (Exception e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
      }
      return result;
  }
  
  public static String httpRequest(String requestUrl, String requestMethod, String outputStr)
  {
    String resp = null;
    StringBuffer buffer = new StringBuffer();
    try
    {
      TrustManager[] tm = { new MyX509TrustManager() };
      SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
      sslContext.init(null, tm, new SecureRandom());

      SSLSocketFactory ssf = sslContext.getSocketFactory();

      URL url = new URL(requestUrl);
      HttpsURLConnection httpUrlConn = (HttpsURLConnection)url.openConnection();
      httpUrlConn.setSSLSocketFactory(ssf);

      httpUrlConn.setDoOutput(true);
      httpUrlConn.setDoInput(true);
      httpUrlConn.setUseCaches(false);

      httpUrlConn.setRequestMethod(requestMethod);

      if ("GET".equalsIgnoreCase(requestMethod)) {
        httpUrlConn.connect();
      }

      if (outputStr != null) {
        OutputStream outputStream = httpUrlConn.getOutputStream();
       
        outputStream.write(outputStr.getBytes("UTF-8"));
        outputStream.close();
      }
      InputStream inputStream = httpUrlConn.getInputStream();
      InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
      BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

      String str = null;
      while ((str = bufferedReader.readLine()) != null) {
        buffer.append(str);
      }
      bufferedReader.close();
      inputStreamReader.close();

      inputStream.close();
      httpUrlConn.disconnect();
      resp = buffer.toString();
    } catch (ConnectException ce) {
      log.error("Weixin server connection timed out.");
    } catch (Exception e) {
      log.error("https request error:{}", e);
    }
    return resp;
  }

  public static String getRandomStr()
  {
    return getRandomStr(16);
  }

  public static String getRandomStr(int length)
  {
    String base = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    Random random = new Random();
    StringBuffer sb = new StringBuffer();
    for (int i = 0; i < length; i++) {
      int number = random.nextInt(base.length());
      sb.append(base.charAt(number));
    }
    return sb.toString();
  }
}