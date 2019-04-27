package com.mooc.house.web.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class WebMvcContext extends WebMvcConfigurerAdapter {

    @Autowired
    private AuthActionInterceptor authActionInterceptor;

    @Autowired
    private AuthInterceptor authInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor).addPathPatterns("/**").excludePathPatterns("/static/**","/index","/accounts/register","/accounts/verify","/accounts/signin");
        registry.addInterceptor(authActionInterceptor).addPathPatterns("/house/toAdd").addPathPatterns("/accounts/profile").addPathPatterns("/accounts/profileSubmit").addPathPatterns("/house/bookmarked").addPathPatterns("/house/del").addPathPatterns("/house/ownlist").addPathPatterns("/house/add").addPathPatterns("/house/toAdd").addPathPatterns("/agency/agentMsg").addPathPatterns("/comment/leaveComment").addPathPatterns("/comment/leaveBlogComment");
        super.addInterceptors(registry);
    }
}