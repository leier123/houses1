package com.mooc.house.web.autoconfig;

import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.NoConnectionReuseStrategy;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass({HttpClient.class})
@EnableConfigurationProperties(HttpClientProperties.class)
public class HttpClientAutoConfig {

    private final HttpClientProperties properties;

    public HttpClientAutoConfig(HttpClientProperties properties) {
        this.properties = properties;
    }

    /**
     * HttpClient的定义
     * @return*/
    @Bean
    @ConditionalOnMissingBean
    public HttpClient httpClient(){
        RequestConfig requestConfig = RequestConfig.custom().
                setConnectionRequestTimeout(properties.getCommectTimeOut()).
                setSocketTimeout(properties.getSocketTimeOut()).build();
        HttpClient client = HttpClientBuilder.create().
                setDefaultRequestConfig(requestConfig).
                setUserAgent(properties.getAgent()).
                setMaxConnPerRoute(properties.getMaxConnperRoute())
                .setConnectionReuseStrategy(new NoConnectionReuseStrategy()).build();
        return client;
    }
}
