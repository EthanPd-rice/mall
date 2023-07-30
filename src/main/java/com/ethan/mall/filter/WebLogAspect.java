package com.ethan.mall.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

/**
 * 打印请求和响应信息
 */
@Aspect
@Component
public class WebLogAspect {
    //log类来记录日志，final一旦修饰不可以改变
    private final Logger log =  LoggerFactory.getLogger(WebLogAspect.class);
    //配置拦截点
    @Pointcut("execution(public * com.ethan.mall.controller.*.*(..))")
    public void webLog(){

    }
    @Before("webLog()")
    public void doBefore(JoinPoint joinPoint){
        //收到请求，记录请求内容
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        log.info("URL:"+request.getRequestURL().toString());
        log.info("HTTP_METHOD:"+request.getMethod());
        log.info("IP:"+request.getRemoteAddr());
        log.info("CLASS_METHOD:"+joinPoint.getSignature().getDeclaringTypeName()+"."+joinPoint.getSignature().getName());
        log.info("ARGS:"+ Arrays.toString(joinPoint.getArgs()));
    }
    //返回类型为res，下面方法参数，拦截的点是上面配置的webLog()
    @AfterReturning(returning = "res",pointcut = "webLog()")
    public void doAfterReturning(Object res) throws JsonProcessingException {
        //处理完请求，返回内容
        //要把对象转成json格式，用new ObjectMapper().writeValueAsString(res)
        log.info("RESPONSE:"+ new ObjectMapper().writeValueAsString(res));
    }
}
