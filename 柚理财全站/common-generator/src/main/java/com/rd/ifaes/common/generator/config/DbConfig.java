package com.rd.ifaes.common.generator.config;


/**
 * 数据库相关配置
 *@author lh
 *@since 2017-03-21
 *@version 3.0
 */
public class DbConfig {

    private String driverClass;
    private String username;
    private String password;
    private String url;


    public String getDriverClass() {
        return driverClass;
    }

    public void setDriverClass(String driverClass) {
        this.driverClass = driverClass;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "DbConfig{" +
                "driverClass='" + driverClass + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
