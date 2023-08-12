package com.ethan.mall.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.ethan.mall.common.Constant;
import com.ethan.mall.exception.EthanMailException;
import com.ethan.mall.exception.EthanMallExceptionEnum;
import com.ethan.mall.model.pojo.User;
import com.ethan.mall.service.UserService;
import javax.annotation.Resource;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.IOException;
import java.io.PrintWriter;

public class UserFilter implements Filter {

    @Resource
    private UserService userService;

    public static User currentUser = new User();

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest)servletRequest;
        String token = request.getHeader(Constant.JWT_TOKEN);
//        currentUser = (User)session.getAttribute(Constant.ETHAN_MALL_USER);
        if(token == null){
            PrintWriter out = new HttpServletResponseWrapper((HttpServletResponse) servletResponse).getWriter();
            out.write("{\n" +
                    "    \"status\": 10007,\n" +
                    "    \"msg\": \"NEED_JWT_TOKEN\",\n" +
                    "    \"data\": null\n" +
                    "}");
            out.flush();
            out.close();
            return;
        }
        Algorithm algorithm = Algorithm.HMAC256(Constant.JWT_KEY);//Constant.JWT_KEY可以是任何字符串
        JWTVerifier verifier = JWT.require(algorithm).build();
        try{
            DecodedJWT jwt = verifier.verify(token);
            currentUser.setId(jwt.getClaim(Constant.USER_ID).asInt());
            currentUser.setUsername(jwt.getClaim(Constant.USER_NAME).asString());
            currentUser.setRole(jwt.getClaim(Constant.USER_ROLE).asInt());
        } catch (TokenExpiredException e){
            //token过期，抛出异常
            throw new EthanMailException(EthanMallExceptionEnum.TOKEN_EXPIRED);
        } catch (JWTDecodeException e){
            //解码失败，抛出异常
            throw new EthanMailException(EthanMallExceptionEnum.TOKEN_WRONG);
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }
}
