package com.rd.ifaes.mobile.wechat.util;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;

public class WeixinUtils
{
  public static Map AccessToken = null;
  public static Map JSApiTicket = null;
  public static WXSystemConfig wxSystemConfig = null;
  private static final Logger logger = Logger.getLogger(WeixinUtils.class);

  private static Map<String, Object> getAccessToken(String code) {
		String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";
		url = url.replace("APPID", CommonUtils.APPID).replace("SECRET",
				CommonUtils.APPSECRET).replace("CODE", code);
		logger.info("getAccessToken======" + url);
		return (Map) JSON.parse(WeixinHttpUtil.httpRequest(url, "GET", null));
	}
  
  private static Map<String, Object> getAccessToken()
  {
    String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
    url = url.replace("APPID", CommonUtils.APPID).replace("APPSECRET", CommonUtils.APPSECRET);
    logger.info("getAccessToken======" + url);
    return (Map)JSON.parse(WeixinHttpUtil.httpRequest(url, "GET", null));
  }

  private static Map<String, Object> getJSApiTicket(String accessToken) {
    String url = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=ACCESS_TOKEN&type=jsapi";
    url = url.replace("ACCESS_TOKEN", accessToken);
    logger.info("getJSApiTicket======" + url);
    return (Map)JSON.parse(WeixinHttpUtil.httpRequest(url, "GET", null));
  }

  public static String loadAccessToken() {
    String token = "ACCESS_TOKEN";

    String accessToken = "";

    if (AccessToken == null)
    {
      Map map = getAccessToken();

      accessToken = StringUtils.isNull(map.get("access_token"));

      if (!StringUtils.isBlank(accessToken))
      {
        int expires_in = Integer.valueOf(map.get("expires_in").toString()).intValue();

        wxSystemConfig = new WXSystemConfig();
        wxSystemConfig.setFlag(true);
        wxSystemConfig.setAddTime(new Date());
        wxSystemConfig.setLastUpdateTime(new Date());
        wxSystemConfig.setTimes(2000);
        wxSystemConfig.setToken("ACCESS_TOKEN");
        wxSystemConfig.setValue(accessToken);
        wxSystemConfig.setExpires(expires_in);

        AccessToken = new HashMap();
        AccessToken.put("bean", wxSystemConfig);
        logger.info(accessToken);
      } else {
        logger.info("access_token获取失败");
      }
    } else { WXSystemConfig wxSystemConfig = (WXSystemConfig)AccessToken.get("bean");
      Date lastUpdateTime = wxSystemConfig.getLastUpdateTime();
      Date date = new Date();
      int expires = wxSystemConfig.getExpires();

      if (date.getTime() - lastUpdateTime.getTime() >= expires * 1000)
      {
        Map map = getAccessToken();

        accessToken = StringUtils.isNull(map.get("access_token"));

        if (!StringUtils.isBlank(accessToken))
        {
          int expires_in = Integer.valueOf(map.get("expires_in").toString()).intValue();
          wxSystemConfig.setLastUpdateTime(new Date());
          wxSystemConfig.setTimes(wxSystemConfig.getTimes() - 1);
          wxSystemConfig.setValue(accessToken);
          wxSystemConfig.setExpires(expires_in);
          logger.info(accessToken);

          AccessToken.put("bean", wxSystemConfig);
        } else {
          logger.info("access_token获取失败");
        }
      } else { accessToken = wxSystemConfig.getValue(); }

    }
    return accessToken;
  }

  public static String loadJSApiTicket()
  {
    String token = "JSAPI_TICKET";
    String ticket = null;

    if (JSApiTicket == null) {
      wxSystemConfig = new WXSystemConfig();

      String accessToken = loadAccessToken();

      Map map = getJSApiTicket(accessToken);

      ticket = StringUtils.isNull(map.get("ticket"));

      if (!StringUtils.isBlank(ticket))
      {
        int expires_in = Integer.valueOf(map.get("expires_in").toString()).intValue();

        wxSystemConfig = new WXSystemConfig();
        wxSystemConfig.setFlag(true);
        wxSystemConfig.setAddTime(new Date());
        wxSystemConfig.setLastUpdateTime(new Date());
        wxSystemConfig.setTimes(2000);
        wxSystemConfig.setToken("JSAPI_TICKET");
        wxSystemConfig.setValue(ticket);
        wxSystemConfig.setExpires(expires_in);

        JSApiTicket = new HashMap();
        JSApiTicket.put("bean", wxSystemConfig);
      } else {
        logger.info("jsapi_ticket获取失败");
      }
    } else { Date lastUpdateTime = wxSystemConfig.getLastUpdateTime();
      Date date = new Date();
      int expires = wxSystemConfig.getExpires();

      if (date.getTime() - lastUpdateTime.getTime() >= expires * 1000) {
        String accessToken = loadAccessToken();

        Map map = getJSApiTicket(accessToken);

        ticket = StringUtils.isNull(map.get("ticket"));

        if (!StringUtils.isBlank(ticket))
        {
          int expires_in = Integer.valueOf(map.get("expires_in").toString()).intValue();
          wxSystemConfig.setLastUpdateTime(new Date());
          wxSystemConfig.setTimes(wxSystemConfig.getTimes() - 1);
          wxSystemConfig.setValue(ticket);
          wxSystemConfig.setExpires(expires_in);

          JSApiTicket.put("bean", wxSystemConfig);
        } else {
          logger.info("jsapi_ticket获取失败");
        }
      } else { ticket = wxSystemConfig.getValue(); }

    }
    return ticket;
  }
  
  public static String loadOpenId(String code) {
		Map map1=getAccessToken(code);
		String openid=StringUtils.isNull(map1.get("openid"));
		return openid;
	}
}