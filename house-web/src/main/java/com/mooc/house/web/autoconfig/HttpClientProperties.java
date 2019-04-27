package com.mooc.house.web.autoconfig;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring.httpclient")
public class HttpClientProperties {

    private Integer commectTimeOut = 1000;

    private Integer socketTimeOut=10*1000;

    private String agent = "agent";

    private Integer maxConnperRoute = 10;

    private Integer MaxConnTotaol = 50;

    public Integer getCommectTimeOut() {
        return commectTimeOut;
    }

    public void setCommectTimeOut(Integer commectTimeOut) {
        this.commectTimeOut = commectTimeOut;
    }

    public Integer getSocketTimeOut() {
        return socketTimeOut;
    }

    public void setSocketTimeOut(Integer socketTimeOut) {
        this.socketTimeOut = socketTimeOut;
    }

    public String getAgent() {
        return agent;
    }

    public void setAgent(String agent) {
        this.agent = agent;
    }

    public Integer getMaxConnperRoute() {
        return maxConnperRoute;
    }

    public void setMaxConnperRoute(Integer maxConnperRoute) {
        this.maxConnperRoute = maxConnperRoute;
    }

    public Integer getMaxConnTotaol() {
        return MaxConnTotaol;
    }

    public void setMaxConnTotaol(Integer maxConnTotaol) {
        MaxConnTotaol = maxConnTotaol;
    }
}
