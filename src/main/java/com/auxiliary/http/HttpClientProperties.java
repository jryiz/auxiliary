package com.auxiliary.http;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @auther ucmed Wenjun Choi
 * @create 2017/8/4
 * 用@ConfigurationProperties必须使用set方法才能注入
 */
@Component
@ConfigurationProperties(prefix = "httpclient.config")
public class HttpClientProperties {
    //最大连接数
    private Integer maxTotal;
    //客户端和服务器建立连接的timeout
    private Integer connectTimeout;
    //服务器返回超时时间
    private Integer socketTimeout;
    //最大路由连接
    private Integer defaultMaxPerRoute;
    //从连接池获取连接的timeout
    private Integer connectionRequestTimeout;

    public Integer getMaxTotal() {
        return maxTotal;
    }

    public void setMaxTotal(Integer maxTotal) {
        this.maxTotal = maxTotal;
    }

    public Integer getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(Integer connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public Integer getSocketTimeout() {
        return socketTimeout;
    }

    public void setSocketTimeout(Integer socketTimeout) {
        this.socketTimeout = socketTimeout;
    }

    public Integer getDefaultMaxPerRoute() {
        return defaultMaxPerRoute;
    }

    public void setDefaultMaxPerRoute(Integer defaultMaxPerRoute) {
        this.defaultMaxPerRoute = defaultMaxPerRoute;
    }

    public Integer getConnectionRequestTimeout() {
        return connectionRequestTimeout;
    }

    public void setConnectionRequestTimeout(Integer connectionRequestTimeout) {
        this.connectionRequestTimeout = connectionRequestTimeout;
    }
}
