package com.example.democommon.configuraction;

import com.example.democommon.Filter.LoginFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

import javax.servlet.Filter;

/**
 * filter中的redis无法注入
 */

@Configuration
public class FilterConfig {
    @Bean
    public FilterRegistrationBean FilterRegistrationBean2(){
        FilterRegistrationBean<Filter> filterFilterRegistrationBean = new FilterRegistrationBean<>();
        //将filter注入进去
        filterFilterRegistrationBean.setFilter(new LoginFilter());
        //需要过滤的请求
        filterFilterRegistrationBean.addUrlPatterns("/api/*/*");
        //设置filter的等级
        filterFilterRegistrationBean.setOrder(Ordered.LOWEST_PRECEDENCE);
        //设置filter的名字
        filterFilterRegistrationBean.setName("loginFilter");
        return filterFilterRegistrationBean;
    }
}
