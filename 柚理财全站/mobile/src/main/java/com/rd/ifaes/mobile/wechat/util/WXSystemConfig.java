package com.rd.ifaes.mobile.wechat.util;

import java.io.Serializable;
import java.util.Date;

public class WXSystemConfig
  implements Serializable
{
  private int id;
  private String token;
  private String value;
  private int expires;
  private boolean flag;
  private int times;
  private Date addTime;
  private Date lastUpdateTime;

  public int getId()
  {
    return this.id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getToken() {
    return this.token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public String getValue() {
    return this.value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public int getExpires() {
    return this.expires;
  }

  public void setExpires(int expires) {
    this.expires = expires;
  }

  public boolean isFlag() {
    return this.flag;
  }

  public void setFlag(boolean flag) {
    this.flag = flag;
  }

  public int getTimes() {
    return this.times;
  }

  public void setTimes(int times) {
    this.times = times;
  }

  public Date getAddTime() {
    return this.addTime;
  }

  public void setAddTime(Date addTime) {
    this.addTime = addTime;
  }

  public Date getLastUpdateTime() {
    return this.lastUpdateTime;
  }

  public void setLastUpdateTime(Date lastUpdateTime) {
    this.lastUpdateTime = lastUpdateTime;
  }
}