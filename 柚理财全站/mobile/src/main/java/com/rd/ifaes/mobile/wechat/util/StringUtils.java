package com.rd.ifaes.mobile.wechat.util;

public class StringUtils
{
  public static boolean isBlank(String str)
  {
    return isNull(str).equals("");
  }
  public static String isNull(Object o) {
    if (o == null) {
      return "";
    }
    String str = "";
    if ((o instanceof String))
      str = (String)o;
    else {
      str = o.toString();
    }
    return str;
  }
}