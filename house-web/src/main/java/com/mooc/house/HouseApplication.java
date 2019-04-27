package com.mooc.house;

import com.mooc.house.web.Filter.LogFilter;
import com.mooc.house.web.autoconfig.EnableHttpClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@EnableHttpClient
@EnableAsync
public class HouseApplication {

	public static void main(String[] args) {
		SpringApplication.run(HouseApplication.class, args);
	}

	/**
	 * 1.构造filter
	 * 2.配置拦截urlPattern
	 * 3.利用FilterRegistrationBean进行包装
	 * @return
	 */
	@Bean
	public FilterRegistrationBean logFilter(){
		FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
		filterRegistrationBean.setFilter(new LogFilter());
		List<String> urList = new ArrayList<String>();
		urList.add("*");
		filterRegistrationBean.setUrlPatterns(urList);
		return filterRegistrationBean;
		}
		}
