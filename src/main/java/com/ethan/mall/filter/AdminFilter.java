package com.ethan.mall.filter;

import com.ethan.mall.common.ApiRestResponse;
import com.ethan.mall.common.Constant;
import com.ethan.mall.exception.EthanMallExceptionEnum;
import com.ethan.mall.model.pojo.User;
import com.ethan.mall.service.UserService;

import javax.annotation.Resource;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

/**
 *  管理员校验过滤器
 */
public class AdminFilter implements Filter {
    @Resource
    private UserService userService;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        //获取session
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpSession session = request.getSession();

        User currentUser = (User) session.getAttribute(Constant.ETHAN_MALL_USER);
        //判断用户是否登录状态
        if(currentUser == null){
        //此方法返回值为void，无法使用ApiRestResponse类型返回
        //return ApiRestResponse.error(EthanMallExceptionEnum.NEED_LOGIN);
            PrintWriter out = new HttpServletResponseWrapper((HttpServletResponse) servletResponse).getWriter();
            //输出内容从postman拷贝比较快
            out.write("{\n" +
                    "    \"status\": 10007,\n" +
                    "    \"msg\": \"NEED_LOGIN\",\n" +
                    "    \"data\": null\n" +
                    "}");
            out.flush();
            out.close();
            return;//方法执行到此结束，不执行未来的过滤器调用，也不会进去controller层
        }
        //判断用户是否是管理员
        boolean adminRole = userService.checkAdminRole(currentUser);
        if(adminRole){
            //校验通过，继续执行下一个过滤器
            filterChain.doFilter(servletRequest, servletResponse);
        }else{
            PrintWriter out = new HttpServletResponseWrapper((HttpServletResponse) servletResponse).getWriter();
            //输出内容从postman拷贝比较快
            out.write("{\n" +
                    "    \"status\": 10009,\n" +
                    "    \"msg\": \"NEED_ADMIN\",\n" +
                    "    \"data\": null\n" +
                    "}");
            out.flush();
            out.close();
            return;//加与不加都可以，方法到此结束
        }
    }

    @Override
    public void destroy() {

    }
}
