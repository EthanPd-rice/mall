package com.ethan.mall.config;


import com.ethan.mall.filter.AdminFilter;
import com.ethan.mall.filter.UserFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 描述 User过滤器的配置
 */
@Configuration
public class UserFilterConfig {

    //把filter定义出来
    @Bean
    public UserFilter userFilter(){
        return new UserFilter();
    }

    //filter放到过滤器的链路中去,bean名不能重复
    @Bean(name = "userFilterConf")
    public FilterRegistrationBean userFilterConfig(){
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        //设置adminFilter过滤器
        filterRegistrationBean.setFilter(userFilter());
        //设置过滤器所要拦截的URL
        filterRegistrationBean.addUrlPatterns("/cart/*");
        filterRegistrationBean.addUrlPatterns("/order/*");
        filterRegistrationBean.addUrlPatterns("/user/update");
        //设置过滤器名字
        filterRegistrationBean.setName("userFilterConfig");
        return filterRegistrationBean;
    }
}
