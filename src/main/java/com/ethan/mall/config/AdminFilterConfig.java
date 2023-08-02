package com.ethan.mall.config;


import com.ethan.mall.filter.AdminFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 描述 Admin过滤器的配置
 */
@Configuration
public class AdminFilterConfig {

    //把filter定义出来
    @Bean
    public AdminFilter adminFilter(){
        return new AdminFilter();
    }

    //filter放到过滤器的链路中去,bean名不能重复
    @Bean(name = "adminFilterConf")
    public FilterRegistrationBean adminFilterConfig(){
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        //设置adminFilter过滤器
        filterRegistrationBean.setFilter(adminFilter());
        //设置过滤器所要拦截的URL
        filterRegistrationBean.addUrlPatterns("/admin/category/*");
        filterRegistrationBean.addUrlPatterns("/admin/product/*");
        filterRegistrationBean.addUrlPatterns("/admin/order/*");
        //设置过滤器名字
        filterRegistrationBean.setName("adminFilterConfig");
        return filterRegistrationBean;
    }
}
