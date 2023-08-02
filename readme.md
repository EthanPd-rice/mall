# 一、项目介绍以及初始化

## 1.功能模块的介绍

<img src="D:\document\Typora\image\image-20230728151748956.png" alt="image-20230728151748956" style="zoom:33%;" />

### （1）前台

<img src="D:\document\Typora\image\image-20230728151912822.png" alt="image-20230728151912822" style="zoom: 50%;" />

<img src="D:\document\Typora\image\image-20230728154643590.png" alt="image-20230728154643590" style="zoom: 50%;" />

### （2）后台

<img src="D:\document\Typora\image\image-20230728154757819.png" alt="image-20230728154757819" style="zoom: 50%;" />

<img src="D:\document\Typora\image\image-20230728154924622.png" alt="image-20230728154924622" style="zoom: 50%;" />

## 2.项目开发所需工具准备

### （1）插件

Maven Helper

Free MyBatis plugin

## 3.数据库设计

- 表设计
- 技术选型、思路
- 新建项目，整合MyBatis，跑通接口
- log4j2日志组件
- 使用AOP统一处理Web请求日志

### （1）表设计

### （2）技术选型

<img src="D:\document\Typora\image\image-20230728183052258.png" alt="image-20230728183052258" style="zoom:33%;" />

<img src="D:\document\Typora\image\image-20230728183433008.png" alt="image-20230728183433008" style="zoom:33%;" />

## 4.数据库逆向

### （1）新建项目

“Spring Initializr”->"Next"->将web添加进来

项目创建完后，修改spring-boot-starter-parent依赖的版本为2.2.1.RELEASE

### （2）在pom.xml排除junit-vintage-engine

在spring-boot-starter-test依赖上排除junit-vintage-engine

```
<exclusions>
    <exclusion>
        <groupId>org.junit.vintage</groupId>
        <artifactId>junit-vintage-engine</artifactId>
    </exclusion>
</exclusions>
```

<img src="D:\document\Typora\image\image-20230728192407430.png" alt="image-20230728192407430" style="zoom: 50%;" />

### （3）在pom.xml添加数据库依赖

```
<dependency>
    <groupId>org.mybatis.spring.boot</groupId>
    <artifactId>mybatis-spring-boot-starter</artifactId>
    <version>1.3.2</version>
</dependency>
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
</dependency>
```

### （4）在pom.xml添加插件（直接copy）

```
<plugin>
    <groupId>org.mybatis.generator</groupId>
    <artifactId>mybatis-generator-maven-plugin</artifactId>
    <version>1.3.7</version>
    <configuration>
        <verbose>true</verbose>
        <overwrite>true</overwrite>
    </configuration>
</plugin>
```

### （5）在resource目录下，新增generatorConfig.xml配置文件

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE generatorConfiguration
  PUBLIC "-//mybatis.org//DTD MyBatis Generator Configuration 1.0//EN"
  "http://mybatis.org/dtd/mybatis-generator-config_1_0.dtd">
<generatorConfiguration>
  <!-- 配置文件，放在resource目录下即可 -->
  <!--数据库驱动个人配置-->
  <classPathEntry
    location="D:\Java\local-repo\mysql\mysql-connector-java\8.0.21\mysql-connector-java-8.0.21.jar"/>
  <context id="MysqlTables" targetRuntime="MyBatis3">
    <property name="autoDelimitKeywords" value="true"/>
    <!--可以使用``包括字段名，避免字段名与sql保留字冲突报错-->
    <property name="beginningDelimiter" value="`"/>
    <property name="endingDelimiter" value="`"/>
    <!-- optional，旨在创建class时，对注释进行控制 -->
    <commentGenerator>
      <property name="suppressDate" value="true"/>
      <property name="suppressAllComments" value="true"/>
    </commentGenerator>
    <!--数据库链接地址账号密码-->
    <jdbcConnection driverClass="com.mysql.cj.jdbc.Driver"
      connectionURL="jdbc:mysql://127.0.0.1:3306/ethan_mall?useSSL=false&amp;useUnicode=true&amp;characterEncoding=UTF-8&amp;serverTimezone=Asia/Shanghai&amp;allowPublicKeyRetrieval=true"
      userId="root"
      password="abc123456">
      <property name="nullCatalogMeansCurrent" value="true"/>
    </jdbcConnection>
    <!-- 非必需，类型处理器，在数据库类型和java类型之间的转换控制-->
    <javaTypeResolver>
      <property name="forceBigDecimals" value="false"/>
    </javaTypeResolver>
    <!--生成Model类存放位置-->
    <javaModelGenerator targetPackage="com.ethan.mall.model.pojo"
      targetProject="src/main/java">
      <!-- 是否允许子包，即targetPackage.schemaName.tableName -->
      <property name="enableSubPackages" value="true"/>
      <!-- 是否对类CHAR类型的列的数据进行trim操作 -->
      <property name="trimStrings" value="true"/>
      <!-- 建立的Model对象是否 不可改变  即生成的Model对象不会有 setter方法，只有构造方法 -->
      <property name="immutable" value="false"/>
    </javaModelGenerator>
    <!--生成mapper映射文件存放位置-->
    <sqlMapGenerator targetPackage="mappers" targetProject="src/main/resources">
      <property name="enableSubPackages" value="true"/>
    </sqlMapGenerator>
    <!--生成Dao类存放位置-->
    <javaClientGenerator type="XMLMAPPER" targetPackage="com.ethan.mall.model.dao"
      targetProject="src/main/java">
      <property name="enableSubPackages" value="true"/>
    </javaClientGenerator>
    <!--生成对应表及类名-->
    <table schema="root" tableName="ethan_mall_cart" domainObjectName="Cart"
      enableCountByExample="false"
      enableUpdateByExample="false" enableDeleteByExample="false" enableSelectByExample="false"
      selectByExampleQueryId="false">
    </table>
    <table tableName="ethan_mall_category" domainObjectName="Category" enableCountByExample="false"
      enableUpdateByExample="false" enableDeleteByExample="false" enableSelectByExample="false"
      selectByExampleQueryId="false">
    </table>
    <table tableName="ethan_mall_order" domainObjectName="Order" enableCountByExample="false"
      enableUpdateByExample="false" enableDeleteByExample="false" enableSelectByExample="false"
      selectByExampleQueryId="false">
    </table>
    <table tableName="ethan_mall_order_item" domainObjectName="OrderItem"
      enableCountByExample="false"
      enableUpdateByExample="false" enableDeleteByExample="false" enableSelectByExample="false"
      selectByExampleQueryId="false">
    </table>
    <table tableName="ethan_mall_product" domainObjectName="Product" enableCountByExample="false"
      enableUpdateByExample="false" enableDeleteByExample="false" enableSelectByExample="false"
      selectByExampleQueryId="false">
    </table>
    <table tableName="ethan_mall_user" domainObjectName="User" enableCountByExample="false"
      enableUpdateByExample="false" enableDeleteByExample="false" enableSelectByExample="false"
      selectByExampleQueryId="false">
    </table>
  </context>
</generatorConfiguration>
```

### （6）导入数据库文件

### （7）用插件导入配置

<img src="D:\document\Typora\image\image-20230728195646517.png" alt="image-20230728195646517" style="zoom:50%;" />

（8）free mybatis plugin可以在mapper和xml文件中跳转，有绿色箭头，也会提示是否有对应的xml方法

<img src="D:\document\Typora\image\image-20230728200019167.png" alt="image-20230728200019167" style="zoom: 67%;" />

<img src="D:\document\Typora\image\image-20230728200106430.png" alt="image-20230728200106430" style="zoom: 50%;" />

## 5.数据库配置

### （1）若直接启动服务，会报错，需要对数据库进行配置

![image-20230728201833384](D:\document\Typora\image\image-20230728201833384.png)

在application.properties配置数据库连接

```
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.datasource.url=jdbc:mysql://localhost:3306/ethan_mall?serverTimezone=UTC&useUnicode=true&characterEncoding=utf-8&useSSL=true
spring.datasource.username=root
spring.datasource.password=abc123456
```

### （2）若没指定mapper位置，则会报错

![image-20230729094716490](D:\document\Typora\image\image-20230729094716490.png)

在MallApplication添加

```
@MapperScan(basePackages = "com.ethan.mall.model.dao")
```

如下所示

```
@SpringBootApplication
@MapperScan(basePackages = "com.ethan.mall.model.dao")
public class MallApplication {
    public static void main(String[] args) {
        SpringApplication.run(MallApplication.class, args);
    }
}
```

### （3）找不到mapper

![image-20230729102146950](D:\document\Typora\image\image-20230729102146950.png)

在application.properties添加

```
mybatis.mapper-locations=classpath:mappers/*.xml
```

![image-20230729102255120](D:\document\Typora\image\image-20230729102255120.png)

## 6.配置log4j2日志

### （1）日志级别：error,warn,info,debug,trace

### （2）在pom排除Logback依赖

![image-20230729105403167](D:\document\Typora\image\image-20230729105403167.png)

### （2）导入依赖后导入log4j2.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<Configuration status="fatal">
  <Properties>
    <!--windows要替换掉日志的地址，
	linux默认是用户目录 例：value="${sys:user.home}/logs-->
    <Property name="baseDir" value="${sys:user.home}/logs"/>
  </Properties>

  <Appenders>
    <Console name="Console" target="SYSTEM_OUT">
      <!--控制台只输出level及以上级别的信息（onMatch），其他的直接拒绝（onMismatch） -->
      <ThresholdFilter level="info" onMatch="ACCEPT"
        onMismatch="DENY"/>
      <PatternLayout
        pattern="[%d{MM:dd HH:mm:ss.SSS}] [%level] [%logger{36}] - %msg%n"/>
    </Console>

    <!--debug级别日志文件输出-->
    <RollingFile name="debug_appender" fileName="${baseDir}/debug.log"
      filePattern="${baseDir}/debug_%i.log.%d{yyyy-MM-dd}">
      <!-- 过滤器 -->
      <Filters>
        <!-- 限制日志级别在debug及以上在info以下 -->
        <ThresholdFilter level="debug"/>
        <ThresholdFilter level="info" onMatch="DENY" onMismatch="NEUTRAL"/>
      </Filters>
      <!-- 日志格式 -->
      <PatternLayout pattern="[%d{HH:mm:ss:SSS}] [%p] - %l - %m%n"/>
      <!-- 策略 -->
      <Policies>
        <!-- 每隔一天转存 -->
        <TimeBasedTriggeringPolicy interval="1" modulate="true"/>
        <!-- 文件大小 -->
        <SizeBasedTriggeringPolicy size="100 MB"/>
      </Policies>
    </RollingFile>

    <!-- info级别日志文件输出 -->
    <RollingFile name="info_appender" fileName="${baseDir}/info.log"
      filePattern="${baseDir}/info_%i.log.%d{yyyy-MM-dd}">
      <!-- 过滤器 -->
      <Filters>
        <!-- 限制日志级别在info及以上在error以下 -->
        <ThresholdFilter level="info"/>
        <ThresholdFilter level="error" onMatch="DENY" onMismatch="NEUTRAL"/>
      </Filters>
      <!-- 日志格式 -->
      <PatternLayout pattern="[%d{HH:mm:ss:SSS}] [%p] - %l - %m%n"/>
      <!-- 策略 -->
      <Policies>
        <!-- 每隔一天转存 -->
        <TimeBasedTriggeringPolicy interval="1" modulate="true"/>
        <!-- 文件大小 -->
        <SizeBasedTriggeringPolicy size="100 MB"/>
      </Policies>
    </RollingFile>

    <!-- error级别日志文件输出 -->
    <RollingFile name="error_appender" fileName="${baseDir}/error.log"
      filePattern="${baseDir}/error_%i.log.%d{yyyy-MM-dd}">
      <!-- 过滤器 -->
      <Filters>
        <!-- 限制日志级别在error及以上 -->
        <ThresholdFilter level="error"/>
      </Filters>
      <!-- 日志格式 -->
      <PatternLayout pattern="[%d{HH:mm:ss:SSS}] [%p] - %l - %m%n"/>
      <Policies>
        <!-- 每隔一天转存 -->
        <TimeBasedTriggeringPolicy interval="1" modulate="true"/>
        <!-- 文件大小 -->
        <SizeBasedTriggeringPolicy size="100 MB"/>
      </Policies>
    </RollingFile>
  </Appenders>
  <Loggers>
    <Root level="debug">
      <AppenderRef ref="Console"/>
      <AppenderRef ref="debug_appender"/>
      <AppenderRef ref="info_appender"/>
      <AppenderRef ref="error_appender"/>
    </Root>

  </Loggers>
</Configuration>
```

## 7.AOP统一处理Web请求日志

为什么需要AOP统一处理Web请求日志？

用filter把每个请求打印出来，可以提高开发的效率，方便问题的排查

### （1）在pom.xml添加依赖

```
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-aop</artifactId>
</dependency>
```

### （2）在com.ethan.mall新建filter目录，新建WebLogAspect类拦截请求

```
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
```

```
[07:29 15:08:31.969] [INFO] [com.ethan.mall.filter.WebLogAspect] - URL:http://127.0.0.1:8080/test
[07:29 15:08:31.969] [INFO] [com.ethan.mall.filter.WebLogAspect] - HTTP_METHOD:GET
[07:29 15:08:31.969] [INFO] [com.ethan.mall.filter.WebLogAspect] - IP:127.0.0.1
[07:29 15:08:31.969] [INFO] [com.ethan.mall.filter.WebLogAspect] - CLASS_METHOD:com.ethan.mall.controller.UserController.personalPage
[07:29 15:08:31.970] [INFO] [com.ethan.mall.filter.WebLogAspect] - ARGS:[1]
[07:29 15:08:31.981] [INFO] [com.ethan.mall.filter.WebLogAspect] - RESPONSE:{"id":1,"username":"1","password":"1","personalizedSignature":"3","role":1,"createTime":1576463853000,"updateTime":1581273672000}
```

# 二、用户模块开发

<img src="D:\document\Typora\image\image-20230729151452175.png" alt="image-20230729151452175" style="zoom: 50%;" />

<img src="D:\document\Typora\image\image-20230729151629657.png" alt="image-20230729151629657" style="zoom:50%;" />

## 1.枚举的使用

由下例子得

输出OrderEnum.PAID时，实际是调用了toString()的方法

也可通过OrderEnum.DELVERED.getState()枚举值去调用getState和getStateInfo方法

还可将枚举值赋值给枚举类型的变量orderEnum,通过变量对方法进行调用



由此也可得出”PAID其实是枚举类的对象“

```java
package com.ethan.mall;


public enum OrderEnum {
    PAID(0,"已支付，未发货"),DELVERED(1,"已支付，已发货"),RECELIED(2,"已收货");

    private int state;
    private String stateInfo;

    OrderEnum(){

    }

    OrderEnum(int state,String stateInfo){
        this.state = state;
        this.stateInfo = stateInfo;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getStateInfo() {
        return stateInfo;
    }

    public void setStateInfo(String stateInfo) {
        this.stateInfo = stateInfo;
    }

    @Override
    public String toString() {
        return "orderEnum{" +
                "state=" + state +
                ", stateInfo='" + stateInfo + '\'' +
                '}';
    }

    public static void main(String[] args) {
        //输出PAID对应的状态
        System.out.println("PAID的状态"+OrderEnum.PAID);
        //获取DELVERED的状态码和状态信息并输出
        int state = OrderEnum.DELVERED.getState();
        String stateInfo = OrderEnum.DELVERED.getStateInfo();
        System.out.println("DELVERED的状态码："+state+",DELVERED的状态信息："+stateInfo);
        
        
        OrderEnum orderEnum = OrderEnum.RECELIED;
        System.out.println("RECELIED的状态信息是："+orderEnum.getStateInfo());
    }
}
```

![image-20230729154006848](D:\document\Typora\image\image-20230729154006848.png)

## 2.API统一返回对象

<img src="D:\document\Typora\image\image-20230729164726999.png" alt="image-20230729164726999" style="zoom:50%;" />

（1）在com.ethan.mall新建EthanMallExceptionEnum枚举类

自己写error不利于维护，新建枚举类，把常见错误收拢在一起，之后找到枚举值，一调用就可以

```java
package com.ethan.mall.exception;

/**
 * 描述：      异常枚举
 */
public enum EthanMallExceptionEnum {
    NEED_USER_NAME(10001,"用户名不能为空");
    /**
     * 异常码
     */
    Integer code;
    /**
     * 异常信息
     */
    String msg;

    EthanMallExceptionEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
```

（2）在com.ethan.mall新建common目录，新建ApiRestResponse

```java
package com.ethan.mall.common;

import com.ethan.mall.exception.EthanMallExceptionEnum;

public class ApiRestResponse<T> {
    private Integer status;
    private String msg;
    //使用泛型，data返回可能是用户对象,也可能是购物车对象
    private T data;
    //定义常量
    private static final int OK_CODE = 10000;
    private static final String OK_MSG = "SUCCESS";

    public ApiRestResponse(Integer status, String msg, T data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    public ApiRestResponse(Integer status, String msg) {
        this.status = status;
        this.msg = msg;
    }
    //请求成功，使用默认构造方法
    public ApiRestResponse() {
        this(OK_CODE,OK_MSG);
    }

    //返回通用响应对象-成功时
    public static <T> ApiRestResponse<T> success(){
        //建立一个成功的返回值，含OK_CODE = 10000，OK_MSG = "SUCCESS"
        return new ApiRestResponse<>();
    }
    //请求成功时，带上一个返回值
    public static <T> ApiRestResponse<T> success(T result){
        ApiRestResponse<T> response = new ApiRestResponse<>();
        //response不仅携带了OK_CODE与OK_MSG，还携带data
        response.setData(result);
        return response;
    }

    //返回通用响应对象-失败时
    //自己写error不利于维护，新建枚举类，把常见错误收拢在一起，之后找到枚举值，一调用就可以
    public static <T> ApiRestResponse<T> error(Integer code,String msg){
        return new ApiRestResponse<>(code,msg);
    }
    public static <T> ApiRestResponse<T> error(EthanMallExceptionEnum ex){
        return new ApiRestResponse<>(ex.getCode(),ex.getMsg());
    }


    @Override
    public String toString() {
        return "ApiRestResponse{" +
                "status=" + status +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public static int getOkCode() {
        return OK_CODE;
    }

    public static String getOkMsg() {
        return OK_MSG;
    }
}
```

## 3.注册接口开发

（1）"username==null" 和 "StringUtils.isEmpty(username)"

if(username==null) 在判断时会忽略字符串为""的情况，同时不易阅读

if(StringUtils.isEmpty(username))在判断是会判断null与字符串为""的情况，更加满足

![image-20230729180721014](D:\document\Typora\image\image-20230729180721014.png)

（2）在com.ethan.mall.exception.EthanMallExceptionEnum添加密码错误的枚举

```
public enum EthanMallExceptionEnum {
    NEED_USER_NAME(10001,"用户名不能为空")
    ,NEED_PASSWORD(10002,"密码不能为空")
    ,PASSWORD_TOO_SHORT(10003,"密码位数不能小于8");
    ...
}
```

（3）在com.ethan.mall.controller.UserController

对用户名和密码做空校验->设置密码长度不能少于8位

```java
@Controller
public class UserController {
    @Resource
    private UserService userService;
    ...
    @PostMapping("/register")
    @ResponseBody
    public ApiRestResponse register(
            @RequestParam("userName") String userName,
            @RequestParam("password") String password){
        //对用户名和密码做空校验
        if(StringUtils.isEmpty(userName)){
            return ApiRestResponse.error(EthanMallExceptionEnum.NEED_USER_NAME);
        }
        if(StringUtils.isEmpty(password)){
            return ApiRestResponse.error(EthanMallExceptionEnum.NEED_PASSWORD);
        }
        //密码长度不能少于8位
        if(password.length()<8){
            return ApiRestResponse.error(EthanMallExceptionEnum.PASSWORD_TOO_SHORT);
        }
    
        ....
        
    }
}
```

（4）在com.ethan.mall.service.UserService接口中添加注册方法

```java
public interface UserService {
    ...
    void register(String username,String password);
}
```

（5）在com.ethan.mall.service.impl.UserServiceImpl实现类中添加注册方法

```
@Service
public class UserServiceImpl implements UserService {
    @Resource
    private UserMapper userMapper;
	...
    @Override
    public void register(String username, String password) {
        ...
    }
}
```

（6）需要先在UserMapper接口中添加查询用户名的方法

```
public interface UserMapper {
	...
    User selectByName(String userName);
}
```

没有到对应的xml对selectByName方法进行描述，在resources/mappers.UserMapper.xml添加

```XML
  <select id="selectByName" parameterType="java.lang.String" resultMap="BaseResultMap">
    select
    <include refid="Base_Column_List"/>
    from ethan_mall_user
    where username = #{userName,jdbcType=VARCHAR}
  </select>
```

（7）在com.ethan.mall.service.impl.UserServiceImpl实现类完善注册方法

```
@Service
public class UserServiceImpl implements UserService {
    @Resource
    private UserMapper userMapper;
	...
    @Override
    public void register(String username, String password) {
        User result = userMapper.selectByName(username);
        //本应根据result是否为null，进行下一步判断，但这是controller的工作，这时候可以抛出自定义异常的方法解决。
        ...
    }
}
```

## 4.自定义异常类

### （1）新建自定义异常类，在exception目录下新建EthanMailException类继承Exception。

```java
public class EthanMailException extends Exception {
    private final Integer code;
    private final String message;

    public EthanMailException(Integer code,String message){
        this.code = code;
        this.message = message;
    }

    public EthanMailException(EthanMallExceptionEnum exceptionEnum){
        this(exceptionEnum.code,exceptionEnum.msg);
    }

    public Integer getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
```

### （2）补充EthanMallExceptionEnum"不允许重名，注册失败" 与 "插入失败，请重试"的错误信息

```
public enum EthanMallExceptionEnum {
    NEED_USER_NAME(10001,"用户名不能为空")
    ,NEED_PASSWORD(10002,"密码不能为空")
    ,PASSWORD_TOO_SHORT(10003,"密码位数不能小于8")
    ,NAME_EXISTED(10004,"不允许重名，注册失败")
    ,INSERT_FAILED(10005,"插入失败，请重试");
    ....
```

### （3）在com.ethan.mall.service.impl.UserServiceImpl实现类完善注册方法

```java
@Service
public class UserServiceImpl implements UserService {
    @Resource
    private UserMapper userMapper;
    @Override
    public void register(String username, String password) throws EthanMailException {
        User result = userMapper.selectByName(username);
        //本应根据result是否为null，进行下一步判断并返回，但这是controller的工作，不可以在service实现类做响应success或fail
        //这时候可以抛出自定义异常的方法解决,将异常跑出来给controller
        if(result != null){
            throw new EthanMailException(EthanMallExceptionEnum.NAME_EXISTED);
        }
        //没有重名信息，写进数据库
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        //使用userMapper.insert的话，要全部都有值，只录入用户名密码要用insertSelective，xml有对应方法
        int count = userMapper.insertSelective(user);
        if(count == 0){
            throw new EthanMailException(EthanMallExceptionEnum.INSERT_FAILED);
        }
    }
}
```

### （4）在com.ethan.mall.controller.UserController补充完

```java
@Controller
public class UserController {
    @Resource
    private UserService userService;
    @GetMapping("/test")
    @ResponseBody
    public User personalPage(@RequestParam Integer id){
        return userService.getUser(id);
    }
    @PostMapping("/register")
    @ResponseBody
    public ApiRestResponse register(
            @RequestParam("userName") String userName,
            @RequestParam("password") String password) throws EthanMailException {
        //对用户名和密码做空校验
        if(StringUtils.isEmpty(userName)){
            return ApiRestResponse.error(EthanMallExceptionEnum.NEED_USER_NAME);
        }
        if(StringUtils.isEmpty(password)){
            return ApiRestResponse.error(EthanMallExceptionEnum.NEED_PASSWORD);
        }
        //密码长度不能少于8位
        if(password.length()<8){
            return ApiRestResponse.error(EthanMallExceptionEnum.PASSWORD_TOO_SHORT);
        }
        userService.register(userName, password);
        return ApiRestResponse.success();
    }
}
```

<img src="D:\document\Typora\image\image-20230730124406522.png" alt="image-20230730124406522" style="zoom:33%;" />

## 5.GlobalExceptionHandler编写

一些不必要的错误信息，容易让黑客攻击，这时候需要进行统一处理异常

![image-20230730125411364](D:\document\Typora\image\image-20230730125411364.png)

在com.ethan.mail.exceptiom.EthanMallExceptionEnum补充错误信息

```
public enum EthanMallExceptionEnum {
    NEED_USER_NAME(10001,"用户名不能为空")
    ,NEED_PASSWORD(10002,"密码不能为空")
    ,PASSWORD_TOO_SHORT(10003,"密码位数不能小于8")
    ,NAME_EXISTED(10004,"不允许重名，注册失败")
    ,INSERT_FAILED(10005,"插入失败，请重试")
    ,SYSTEM_ERROR(20000,"系统异常");
    ...
}
```

在exception目录下编写GlobalExceptionHandler

```java
//@ControllerAdvice 是一个注解，用于标记一个类是全局控制器异常处理器。
@ControllerAdvice
public class GlobalExceptionHandler {
    private final Logger log = LoggerFactory.getLogger("GlobalExceptionHandler.class");
    //Spring将根据 @ExceptionHandler 注解来捕获并处理异常，从而提供一个统一的异常处理机制。
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Object handleException(Exception e){
        log.error("Default Exception:",e);
        return ApiRestResponse.error(EthanMallExceptionEnum.SYSTEM_ERROR);
    }

    @ExceptionHandler(EthanMailException.class)
    @ResponseBody
    public Object handleEthanMailException(EthanMailException e){
        log.error("EthanMailException:",e);
        return ApiRestResponse.error(e.getCode(),e.getMessage());
    }

}
```

![image-20230730133731037](D:\document\Typora\image\image-20230730133731037.png)

![image-20230730133737064](D:\document\Typora\image\image-20230730133737064.png)

## 6.Java异常体系

<img src="D:\document\Typora\image\image-20230730134551163.png" alt="image-20230730134551163" style="zoom: 50%;" />

Error:一般是人为代码上无法解决的：内存不足，堆栈溢出等

Exception:

RuntimeException：运行时异常，如空指针异常，数学除以0的异常等。无法提前感知，遇到了才能处理，不能对其try catch进行异常捕获或将其抛出去

其他Exception：程序会要求我们使用try catch进行异常捕获或将其抛出去。和RuntimeException的区别在于能不能try catch或将其抛出去。

## 7.对密码进行md5保护

（1）直接进行md5加密的话，破解网站很容易破解

使用加盐值的方式，进行更加高级的加密

在com.ethan.mall.common上新建Constant类

```java
public class Constant {
    //盐值不能太简单，复杂点
    public static final String SALT = "SJsdsSK.SAD/.S28371][S]./";
}
```

（2）在com.ethan.mall.util上新建MD5Utils类

```java
package com.ethan.mall.util;

import com.ethan.mall.common.Constant;
import org.apache.tomcat.util.codec.binary.Base64;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Utils {
    public static String getMD5Str(String strValue) throws NoSuchAlgorithmException {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        return Base64.encodeBase64String(md5.digest((strValue+Constant.SALT).getBytes()));
    }

    public static void main(String[] args) {
        String md5=null;
        try {
            md5 = getMD5Str("1234");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        System.out.println(md5);
    }
}
```

（3）修改UserServiceImpl设置密码的格式

```java
    try {
        user.setPassword(MD5Utils.getMD5Str(password));
    } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
    }
```

```java
@Service
public class UserServiceImpl implements UserService {
    @Resource
    private UserMapper userMapper;
    @Override
    public User getUser(Integer id) {
        return userMapper.selectByPrimaryKey(id);
    }

    @Override
    public void register(String username, String password) throws EthanMailException {
        User result = userMapper.selectByName(username);
        //本应根据result是否为null，进行下一步判断并返回，但这是controller的工作，不可以在service实现类做响应success或fail
        //这时候可以抛出自定义异常的方法解决,将异常跑出来给controller
        if(result != null){
            throw new EthanMailException(EthanMallExceptionEnum.NAME_EXISTED);
        }
        //没有重名信息，写进数据库
        User user = new User();
        user.setUsername(username);
        try {
            user.setPassword(MD5Utils.getMD5Str(password));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        //使用userMapper.insert的话，要全部都有值，只录入用户名密码要用insertSelective，xml有对应方法
        int count = userMapper.insertSelective(user);
        if(count == 0){
            throw new EthanMailException(EthanMallExceptionEnum.INSERT_FAILED);
        }
    }
}
```

## 8.登录接口开发

（1）登录接口

需要利用session把登录状态保存，把用户信息保存到服务端

（2）在UserMapper添加selectLogin方法

```
public interface UserMapper {
    ...
    User selectLogin(@Param("userName") String userName, @Param("password") String password);
}
```

（3）在UserMapper.xml添加

```xml
<select id="selectLogin" parameterType="map" resultMap="BaseResultMap">
  select
  <include refid="Base_Column_List"></include>
  from ethan_mall_user
  where username = #{userName,jdbcType=VARCHAR}
  and password = #{password,jdbcType=VARCHAR}
</select>
```

（4）在EthanMallExceptionEnum添加WRONG_PASSWORD(10006,"密码错误")

（5）在UserServiceImpl直接填写login方法

```java
@Override
public User login(String userName,String password) throws EthanMailException {
    String md5Password = null;
    try {
        md5Password = MD5Utils.getMD5Str(password);
    } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
    }
    User user = userMapper.selectLogin(userName,md5Password);
    if(user == null){
        throw new EthanMailException(EthanMallExceptionEnum.WRONG_PASSWORD);
    }
    return user;
}
```

按下图方式，会将方法填写到UserService接口中。

![image-20230730163640452](D:\document\Typora\image\image-20230730163640452.png)

（6）在com.ethan.mall.common目录下Constant类新增key常量ETHAN_MALL_USER

```
public class Constant {
	//key常量
    public static final String ETHAN_MALL_USER = "ethan_mall_user";
    //盐值不能太简单，复杂点
    public static final String SALT = "SJsdsSK.SAD/.S28371][S]./";
}
```

（7）在com.ethan.mall.controller下补充UserController

```java
@PostMapping("/login")
@ResponseBody
public ApiRestResponse login(
        @RequestParam("userName") String userName
        , @RequestParam("password") String password
        , HttpSession session) throws EthanMailException {
    if(StringUtils.isEmpty(userName)){
        return ApiRestResponse.error(EthanMallExceptionEnum.NEED_USER_NAME);
    }
    if(StringUtils.isEmpty(password)){
        return ApiRestResponse.error(EthanMallExceptionEnum.NEED_PASSWORD);
    }
    User user = userService.login(userName,password);
    //安全考虑，不返回用户密码
    user.setPassword(null);
    //在Constant类设置一个Key,把user对象放到key为ETHAN_MALL_USER常量里去
    session.setAttribute(Constant.ETHAN_MALL_USER,user);
    return ApiRestResponse.success(user);
}
```

## 9.个性签名更新接口开发

（1）在com.ethan.mall.exception.EthanMallExceptionEnum更新错误枚举

```
,NEED_LOGIN(10007,"用户未登录")
,UPDATE_FAILED(10008,"更新失败")
```

（2）在com.ethan.mall.service.ipml的UserServiceImpl实现类添加更新个性签名的方法

```java
@Override
public void updateInformation(User user) throws EthanMailException {
    int updateCount = userMapper.updateByPrimaryKeySelective(user);
    if(updateCount > 1){
        throw new EthanMailException(EthanMallExceptionEnum.UPDATE_FAILED);
    }
}
```

<img src="D:\document\Typora\image\image-20230730182634608.png" alt="image-20230730182634608" style="zoom: 67%;" />

（3）在com.ethan.mall.controller.UserController添加updateUserInfo方法

```java
@PostMapping("/user/update")
@ResponseBody
public ApiRestResponse updateUserInfo(HttpSession session,@RequestParam String signature) throws EthanMailException {
    User currentUser = (User)session.getAttribute(Constant.ETHAN_MALL_USER);
    if(currentUser == null){
        return ApiRestResponse.error(EthanMallExceptionEnum.NEED_LOGIN);
    }
    User user = new User();
    user.setId(currentUser.getId());
    user.setPersonalizedSignature(signature);
    userService.updateInformation(user);
    return ApiRestResponse.success();
}
```

测试无误

![image-20230730184122155](D:\document\Typora\image\image-20230730184122155.png)

## 10.退出登录接口开发

（1）在com.ethan.mall.controller.UserController添加logout方法

```java
@PostMapping("/user/logout")
@ResponseBody
public ApiRestResponse logout(HttpSession session){
    session.removeAttribute(Constant.ETHAN_MALL_USER);
    return ApiRestResponse.success();
}
```

![image-20230730184556339](D:\document\Typora\image\image-20230730184556339.png)

## 11.管理员登录接口开发

（1）在UserServiceImpl层对身份进行检查

```java
@Override
public boolean checkAdminRole(User user){
    //1是普通用户，2是管理员
    return user.getRole().equals(2);
}
```

（2）在UserController添加adminLogin方法

a.提高编码效率的方式

![image-20230730185415106](D:\document\Typora\image\image-20230730185415106.png)

![image-20230730185424440](D:\document\Typora\image\image-20230730185424440.png)

b.在EthanMallExceptionEnum添加错误枚举

```
,NEED_ADMIN(10009,"无管理员权限")
```

c.在UserController添加adminLogin方法

```java
@PostMapping("/adminLogin")
@ResponseBody
public ApiRestResponse adminLogin(
        @RequestParam("userName") String userName
        , @RequestParam("password") String password
        , HttpSession session) throws EthanMailException {
    if(StringUtils.isEmpty(userName)){
        return ApiRestResponse.error(EthanMallExceptionEnum.NEED_USER_NAME);
    }
    if(StringUtils.isEmpty(password)){
        return ApiRestResponse.error(EthanMallExceptionEnum.NEED_PASSWORD);
    }
    User user = userService.login(userName,password);
    //相比普通用户登录，管理员登录多了一层校验，user表中role为2是管理员，1是普通用户
    if (userService.checkAdminRole(user)) {
        //安全考虑，不返回用户密码
        user.setPassword(null);
        //在Constant类设置一个Key,把user对象放到key为ETHAN_MALL_USER常量里去
        session.setAttribute(Constant.ETHAN_MALL_USER,user);
        return ApiRestResponse.success(user);
    }else{
        return ApiRestResponse.error(EthanMallExceptionEnum.NEED_ADMIN);
    }

}
```

## 12.总结用户模块

### （1）重难点：

统一响应对象

登录状态保持

统一异常处理

### （2）常见错误

响应对象不规范

异常不统一处理

# 三、商品分类管理模块

## 1.开发添加分类接口

（1） 在请求时，请求参数太多，在添加或删除时会比较麻烦，建议封装

在com.ethan.mall.mode新建request目录，在目录下新建AddCategoryReq类

之所以不使用pojo目录下的Category类是因为：

- 各司其职，pojo(Plain Old Java Object)是简单java对象，而request是用来给请求用的
- 避免黑客攻击，一些不需要用到的字段不让黑客通过此接口访问到。

```java（）
package com.ethan.mall.model.request;

public class AddCategoryReq {
    private String name;
    private Integer type;
    private Integer parentId;
    private Integer orderNum;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public Integer getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }
}
```

（2）在EthanMallExceptionEnum枚举类添加错误枚举

```
,PARA_NOT_NULL(10010,"参数不能为空")
```

（3）在CategoryMapper接口新增selectByName方法

```java
public interface CategoryMapper {
    ...
    Category selectByName(String name);
}
```

（4）在CategoryMapper.xml添加selectByName方法

```xml
<select id="selectByName" parameterType="java.lang.String" resultMap="BaseResultMap">
  select
  <include refid="Base_Column_List"/>
  from ethan_mall_category
  where name = #{name,jdbcType=VARCHAR}
</select>
```

（5）在EthanMallExceptionEnum修改异常枚举

```
,NAME_EXISTED(10004,"不允许重名，注册失败")
```

修改成通用点的

```
,NAME_EXISTED(10004,"不允许重名")
```

（6）修改异常的继承类Exception->RuntimeException

这样不需要每次都要处理或将异常抛出

在EthanMailException修改继承类

![image-20230730214351714](D:\document\Typora\image\image-20230730214351714.png)

改成

![image-20230730214416271](D:\document\Typora\image\image-20230730214416271.png)

（7）在EthanMallExceptionEnum枚举类添加错误枚举

```
,CREATE_FAILED(10011,"新增失败")
```

（8）新建CategoryServiceImpl实现类

BeanUtils.copyProperties( , )把相同参数拷贝过去

```java
@Service
public class CategoryServiceImpl implements CategoryService {
    @Resource
    private CategoryMapper categoryMapper;
    @Override
    public void add(AddCategoryReq addCategoryReq){
        Category category = new Category();
        //BeanUtils.copyProperties(addCategoryReq,category)
        //把addCategoryReq和category对象的相同参数拷贝过去，无需频繁的get与set操作
        BeanUtils.copyProperties(addCategoryReq,category);
        Category categoryOld = categoryMapper.selectByName(addCategoryReq.getName());
        //检查要新增的目录名是否已有相同名字
        if(categoryOld != null){
            throw new EthanMailException(EthanMallExceptionEnum.NAME_EXISTED);
        }
        int count = categoryMapper.insertSelective(category);
        //若更新条数为0，抛出创建失败的错误
        if(count == 0){
            throw new EthanMailException(EthanMallExceptionEnum.CREATE_FAILED);
        }
    }
}
```

（9）测试接口

修改CategoryController中请求参数的传参方式Params变成Body

![image-20230730220339790](D:\document\Typora\image\image-20230730220339790.png)

![image-20230730220915443](D:\document\Typora\image\image-20230730220915443.png)

## 2.@Valid注解优雅校验入参

<img src="D:\document\Typora\image\image-20230730221304154.png" alt="image-20230730221304154" style="zoom: 50%;" />

@Valid开关，

@NotNull、@Max(value)、@Size(max,min)分别起作用

异常参数 MethodArgumentNotValidException

（1）注释掉com.ethan.mall.CategoryController的校验部分，在请求参数前加上@Valid注释

![image-20230731104526716](D:\document\Typora\image\image-20230731104526716.png)

（2）在com.ethan.mall.model.request.AddCategoryReq添加注释

```java
public class AddCategoryReq {
    //若直接注释@NotNull，异常提示不会显示哪个字段不能为null，可以加message属性自定义提示
    @Size(min = 2,max = 3)
    @NotNull(message = "name不能为null")
    private String name;
    @Max(3)
    @NotNull(message = "type不能为null")
    private Integer type;
    @NotNull(message = "parentId不能为null")
    private Integer parentId;
    @NotNull(message = "orderNum不能为null")
    private Integer orderNum;
	...
}
```

![image-20230731105414145](D:\document\Typora\image\image-20230731105414145.png)

前台只会显示系统异常，不会写详情错误信息，后台会有详情信息

```
org.springframework.web.bind.MethodArgumentNotValidException: Validation failed for argument [1] in public com.ethan.mall.common.ApiRestResponse com.ethan.mall.controller.CategoryController.addCategory(javax.servlet.http.HttpSession,com.ethan.mall.model.request.AddCategoryReq): [Field error in object 'addCategoryReq' on field 'name': rejected value [null]; codes [NotNull.addCategoryReq.name,NotNull.name,NotNull.java.lang.String,NotNull]; arguments [org.springframework.context.support.DefaultMessageSourceResolvable: codes [addCategoryReq.name,name]; arguments []; default message [name]]; default message [name不能为null]]
```

（3）为更好的使用体验，也便于前后端工作联调顺利，在GlobalExceptionHandler统一处理异常

```java
//@ControllerAdvice 是一个注解，用于标记一个类是全局控制器异常处理器。
@ControllerAdvice
public class GlobalExceptionHandler {
    private final Logger log = LoggerFactory.getLogger("GlobalExceptionHandler.class");
    //Spring将根据 @ExceptionHandler 注解来捕获并处理异常，从而提供一个统一的异常处理机制。
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Object handleException(Exception e){
        log.error("Default Exception:",e);
        return ApiRestResponse.error(EthanMallExceptionEnum.SYSTEM_ERROR);
    }

    @ExceptionHandler(EthanMailException.class)
    @ResponseBody
    public Object handleEthanMailException(EthanMailException e){
        log.error("EthanMailException:",e);
        return ApiRestResponse.error(e.getCode(),e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ApiRestResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException e){
        log.error("MethodArgumentNotValidException:",e);
        return handleBindingResult(e.getBindingResult());
    }

    public ApiRestResponse handleBindingResult(BindingResult result){
        //把异常处理为对外暴露的提示
        List<String> list = new ArrayList<>();
        if(result.hasErrors()){
            List<ObjectError> allErrors = result.getAllErrors();
            for(ObjectError objectError:allErrors){
                String message = objectError.getDefaultMessage();
                list.add(message);
            }
        }
        if (list.size() == 0){
            return ApiRestResponse.error(EthanMallExceptionEnum.REQUEST_PARAM_ERROR);
        }
        return ApiRestResponse.error(EthanMallExceptionEnum.REQUEST_PARAM_ERROR.getCode(),list.toString());
    }

}
```

## 3.自动生成api文档

（1）pom.xml添加依赖

```xml
<dependency>
    <groupId>io.springfox</groupId>
    <artifactId>springfox-swagger2</artifactId>
    <version>2.9.2</version>
</dependency>
<dependency>
    <groupId>io.springfox</groupId>
    <artifactId>springfox-swagger-ui</artifactId>
    <version>2.9.2</version>
</dependency>
```

（2）在MallApplication添加@EnableSwagger2，自动生成api文档

```java
@SpringBootApplication
@MapperScan(basePackages = "com.ethan.mall.model.dao")
@EnableSwagger2
public class MallApplication {
    public static void main(String[] args) {
        SpringApplication.run(MallApplication.class, args);
    }
}
```

（2）在com.ethan.mall新建config目录，新建SpringFoxConfig类

以下是固定的模板

```
package com.ethan.mall.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SpringFoxConfig {

    //访问http://localhost:8083/swagger-ui.html可以看到API文档
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Ethan生鲜超市")//自定义
                .description("")
                .termsOfServiceUrl("")
                .build();
    }
}
```

（3）在com.ethan.mall新建config目录，新建EthanMallWebMvcConfig类，实现WebMvcConfigurer接口

重写addResourceHandlers方法

```java
package com.ethan.mall.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class EthanMallWebMvcConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html").addResourceLocations(
                "classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations(
                "classpath:/META-INF/resources/webjars/");
    }
}
```

（4）（1）-（3）已完成配置，接下来只需访问http://localhost:8080/swagger-ui.html

如果不加`@ApiOperation`注解，接口文档中将不会显示对应的简要描述，这可能使得其他团队成员或开发者难以快速了解接口的用途。

加了就会在页面上显示我们的备注

例在CategoryController的addCategory上添加@ApiOperation("后台添加目录")注解

```java
package com.ethan.mall.controller;

import com.ethan.mall.common.ApiRestResponse;
import com.ethan.mall.common.Constant;
import com.ethan.mall.exception.EthanMallExceptionEnum;
import com.ethan.mall.model.pojo.User;
import com.ethan.mall.model.request.AddCategoryReq;
import com.ethan.mall.service.CategoryService;
import com.ethan.mall.service.UserService;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
public class CategoryController {
    @Resource
    private UserService userService;
    @Resource
    private CategoryService categoryService;

    @ApiOperation("后台添加目录")
    @PostMapping("/admin/category/add")
    @ResponseBody
    public ApiRestResponse addCategory(HttpSession session, @Valid @RequestBody AddCategoryReq addCategoryReq){
        //若有参数是空的，则提示参数不能为空
//        if(addCategoryReq.getName() == null || addCategoryReq.getOrderNum() == null
//                || addCategoryReq.getParentId() == null || addCategoryReq.getOrderNum() == null){
//            return ApiRestResponse.error(EthanMallExceptionEnum.PARA_NOT_NULL);
//        }
        User currentUser = (User) session.getAttribute(Constant.ETHAN_MALL_USER);
        //判断用户是否登录状态
        if(currentUser == null){
            return ApiRestResponse.error(EthanMallExceptionEnum.NEED_LOGIN);
        }
        //判断用户是否是管理员
        boolean adminRole = userService.checkAdminRole(currentUser);
        if(adminRole){
            categoryService.add(addCategoryReq);
            return ApiRestResponse.success();
        }else{
            return ApiRestResponse.error(EthanMallExceptionEnum.NEED_ADMIN);
        }
    }
}
```



![image-20230731133747737](D:\document\Typora\image\image-20230731133747737.png)

## 4.更新目录接口

（1）创建UpdateCategoryReq

```java
package com.ethan.mall.model.request;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class UpdateCategoryReq {
    //若直接注释@NotNull，异常提示不会显示哪个字段不能为null，可以加message属性自定义提示
    @NotNull(message = "id不能为null")
    private Integer id;
    @Size(min = 2,max = 5)
    private String name;
    @Max(3)
    private Integer type;
    private Integer parentId;
    private Integer orderNum;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public Integer getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }
}
```

2.CategoryServiceImpl添加update方法

```java
@Override
public void update(Category updatecCategory){
    if(updatecCategory.getName() != null){
        Category categoryOld = categoryMapper.selectByName(updatecCategory.getName());
        if(categoryOld != null && !categoryOld.getId().equals(updatecCategory.getId())){
            throw new EthanMailException(EthanMallExceptionEnum.NAME_EXISTED);
        }
    }

    int count = categoryMapper.updateByPrimaryKeySelective(updatecCategory);
    if(count == 0 ){
        throw new EthanMailException(EthanMallExceptionEnum.UPDATE_FAILED);
    }
}
```

3.CategoryController添加updateCategory方法

```java
@ApiOperation("后台更新目录")
@PostMapping("/admin/category/update")
@ResponseBody
public ApiRestResponse updateCategory(@Valid @RequestBody UpdateCategoryReq updateCategoryReq,HttpSession session){
    User currentUser = (User) session.getAttribute(Constant.ETHAN_MALL_USER);
    //判断用户是否登录状态
    if(currentUser == null){
        return ApiRestResponse.error(EthanMallExceptionEnum.NEED_LOGIN);
    }
    //判断用户是否是管理员
    boolean adminRole = userService.checkAdminRole(currentUser);
    if(adminRole){
        Category category = new Category();
        BeanUtils.copyProperties(updateCategoryReq,category);
        categoryService.update(category);
        return ApiRestResponse.success();
    }else{
        return ApiRestResponse.error(EthanMallExceptionEnum.NEED_ADMIN);
    }
}
```

![image-20230731161355874](D:\document\Typora\image\image-20230731161355874.png)

## 5.统一校验管理员身份

考虑到每次都需要校验管理员身份，若要修改校验管理员的相关代码，不统一的话是极其不方便的

于是，创建一个过滤器，统一进行管理员的身份校验

（2）在com.ethan.mail.filter新建AdminFilter过滤实现Filter接口

```java
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
        //此方法返回值为void，无法使用ApiRestResponse类型返回,使用其他方法输出错误信息
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
```

（3）把过滤器AdminFilter配置到com.ethan.mall.config.AdminFilterConfig类上去

```java
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
```

## 6.删除目录接口

（1）在CategoryServiceImpl新建delete方法

```java
@Override
public void delete(Integer id){
    Category categoryOld = categoryMapper.selectByPrimaryKey(id);
    if(categoryOld == null){
        throw new EthanMailException(EthanMallExceptionEnum.DELETE_FAILED);
    }
    int count = categoryMapper.deleteByPrimaryKey(id);
    if(count == 0){
        throw new EthanMailException(EthanMallExceptionEnum.DELETE_FAILED);
    }
}
```

（2）在CategoryController新建deleteCategory方法

```java
@ApiOperation("后台删除目录")
@PostMapping("/admin/category/delete")
@ResponseBody
public ApiRestResponse deleteCategory(@RequestParam Integer id){
    categoryService.delete(id);
    return  ApiRestResponse.success();
}
```

## 7.后台查询商品分类列表

-----------------------后端------------------------

（1）pom.xml导入分页依赖

```xml
<dependency>
    <groupId>com.github.pagehelper</groupId>
    <artifactId>pagehelper-spring-boot-starter</artifactId>
    <version>1.2.13</version>
</dependency>
```

（2）在com.ethan.mall.service.impl的CategoryServiceImpl实现类添加listForAdmin方法

```java
//pageInfo包括页号，页空间等信息
@Override
public PageInfo listForAdmin(Integer pageNum,Integer pageSize){
    PageHelper.startPage(pageNum,pageSize,"type,order_num");
    List<Category> categoryList = categoryMapper.selectList();
    PageInfo pageInfo = new PageInfo(categoryList);
    return pageInfo;
}
```

（3）在com.ethan.mall.controller新增方法

```
    @ApiOperation("后台目录列表")
    @PostMapping("/admin/category/list")
    @ResponseBody
    public ApiRestResponse listCategoryForAdmin(@RequestParam Integer pageNum,@RequestParam Integer pageSize){
        PageInfo pageInfo = categoryService.listForAdmin(pageNum, pageSize);
        return ApiRestResponse.success(pageInfo);
    }
```

![image-20230801101558857](D:\document\Typora\image\image-20230801101558857.png)

------------------------------------前端列表数据------------------------------------------------

（2）在com.ethan.mall.model.vo目录新建CategoryVO类

```java
package com.ethan.mall.model.vo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CategoryVO {
    private Integer id;

    private String name;

    private Integer type;

    private Integer parentId;

    private Integer orderNum;

    private Date createTime;

    private Date updateTime;

    private List<CategoryVO> childCategory = new ArrayList<>();

    public List<CategoryVO> getChildCategory() {
        return childCategory;
    }

    public void setChildCategory(List<CategoryVO> childCategory) {
        this.childCategory = childCategory;
    }
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public Integer getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
```

（3）再CategoryMapper.xml修改selectCategoriesByParentId方法

```
<select id="selectCategoriesByParentId" resultMap="BaseResultMap" parameterType="java.lang.Integer">
  select
  <include refid="Base_Column_List"></include>
  from ethan_mall_category
  where parent_id = #{parentId}
</select>
```

（4）CategoryMapper接口添加selectCategoriesByParentId方法

```
List<Category> selectCategoriesByParentId(Integer parentId);
```

（5）CategoryServiceImpl类添加listCategoryForCustomer（返回数据）与recursivelyFindCategories（处理数据）方法，

```java
@Override
public List<CategoryVO> listCategoryForCustomer(){
    ArrayList<CategoryVO> categoryVOList = new ArrayList<>();
    recursivelyFindCategories(categoryVOList,0);
    return categoryVOList;

}

public void recursivelyFindCategories(List<CategoryVO> categoryVOList,Integer parentId){
    //递归获取所有子类别，并组合成为一个“目录树“
    List<Category> categoryList = categoryMapper.selectCategoriesByParentId(parentId);
    if(!CollectionUtils.isEmpty(categoryList)){
        for(int i = 0; i < categoryList.size();i++){
            Category category = categoryList.get(i);
            CategoryVO categoryVO = new CategoryVO();
            BeanUtils.copyProperties(category,categoryVO);
            categoryVOList.add(categoryVO);
            recursivelyFindCategories(categoryVO.getChildCategory(),categoryVO.getId());
        }
    }
}
```

（6）CategoryController添加listCategoryForCustomer方法

```
@ApiOperation("前台目录列表")
@PostMapping("/category/list")
@ResponseBody
public ApiRestResponse listCategoryForCustomer(){
    List<CategoryVO> categoryVOS = categoryService.listCategoryForCustomer();
    return ApiRestResponse.success(categoryVOS);
}
```

## 8.利用Redis缓存加速取得目录列表

（1）pom.xml加入Redis依赖

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-redis</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-cache</artifactId>
</dependency>
```

（2）在application.properties添加redis配置

```
spring.redis.host=localhost
spring.redis.port=6379
spring.redis.password=
```

（3）在MallApplication加注解打开缓存功能

```
@EnableCaching //打开缓存功能
```

```
@SpringBootApplication
@MapperScan(basePackages = "com.ethan.mall.model.dao")
@EnableSwagger2
@EnableCaching //打开缓存功能
public class MallApplication {
    public static void main(String[] args) {
        SpringApplication.run(MallApplication.class, args);
    }
}
```

（4）给希望缓存的方法加一个注解，在CategoryServiceImpl添加

```
@Cacheable(value = "listCategoryForCustomer")//import org.springframework.cache.annotation.Cacheable;
```

```
@Override
@Cacheable(value = "listCategoryForCustomer")//import org.springframework.cache.annotation.Cacheable;
public List<CategoryVO> listCategoryForCustomer(){
    ArrayList<CategoryVO> categoryVOList = new ArrayList<>();
    recursivelyFindCategories(categoryVOList,0);
    return categoryVOList;

}

public void recursivelyFindCategories(List<CategoryVO> categoryVOList,Integer parentId){
    //递归获取所有子类别，并组合成为一个“目录树“
    List<Category> categoryList = categoryMapper.selectCategoriesByParentId(parentId);
    if(!CollectionUtils.isEmpty(categoryList)){
        for(int i = 0; i < categoryList.size();i++){
            Category category = categoryList.get(i);
            CategoryVO categoryVO = new CategoryVO();
            BeanUtils.copyProperties(category,categoryVO);
            categoryVOList.add(categoryVO);
            recursivelyFindCategories(categoryVO.getChildCategory(),categoryVO.getId());
        }
    }
}
```

（5）在com.ethan.mall.config新建CachingConfig类，对redis进行配置（主要配置超时时间）

```
package com.ethan.mall.config;


import java.time.Duration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;

/**
 * 描述：     缓存的配置类
 */
@Configuration
@EnableCaching
public class CachingConfig {

    @Bean
    public RedisCacheManager redisCacheManager(RedisConnectionFactory connectionFactory) {

        RedisCacheWriter redisCacheWriter = RedisCacheWriter
                .lockingRedisCacheWriter(connectionFactory);
        RedisCacheConfiguration cacheConfiguration = RedisCacheConfiguration.defaultCacheConfig();
        cacheConfiguration = cacheConfiguration.entryTtl(Duration.ofSeconds(30));

        RedisCacheManager redisCacheManager = new RedisCacheManager(redisCacheWriter,
                cacheConfiguration);
        return redisCacheManager;
    }
}
```

（6）按以上步骤会出现序列化失败，需要让CategoryVO继承Serializable序列化。

<img src="D:\document\Typora\image\image-20230802091842423.png" alt="image-20230802091842423" style="zoom: 67%;" />

10.总结商品分类模块

重难点：

- 参数校验
- Swagger
- 统一鉴权（filter）
- Redis整合
- 调试功能

常见错误：

- 参数手动校验
- 项目不用缓存
- 不善用调试

# 四、商品模块开发

## 1.添加商品接口与图片上传功能开发

（1）新建AddProductReq请求类

```java
package com.ethan.mall.model.request;


import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class AddProductReq {

    @NotNull(message = "商品名称不能为null")
    private String name;

    @NotNull(message = "商品图片不能为null")
    private String image;

    private String detail;

    @NotNull(message = "商品分类不能为null")
    private Integer categoryId;

    @NotNull(message = "商品价格不能为null")
    @Min(value = 1, message = "价格不能小于1分")
    private Integer price;

    @NotNull(message = "商品库存不能为null")
    @Max(value = 10000, message = "库存不能大于10000")
    private Integer stock;

    private Integer status;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
```

（2）在ProductMapper新建selectByName方法

```
<select id="selectByName" parameterType="java.lang.String" resultMap="BaseResultMap">
  select
  <include refid="Base_Column_List"/>
  from
  ethan_mall_product
  where name = #{name}
</select>
```

（3）·新建ProductServiceImpl

```
/**
 * 描述：商品服务实现类
 */
@Service
public class ProdectServiceImpl implements ProductService {

    @Resource
    private ProductMapper productMapper;

    @Override
    public void add(AddProductReq addProductReq){
        Product product = new Product();
        BeanUtils.copyProperties(addProductReq,product);
        Product productOld = productMapper.selectByName(addProductReq.getName());
        if(productOld != null){
            throw new EthanMailException(EthanMallExceptionEnum.NAME_EXISTED);
        }
        int count = productMapper.insertSelective(product);
        if(count == 0){
            throw new EthanMailException(EthanMallExceptionEnum.CREATE_FAILED);
        }
    }
}
```

.（4）新建ProductAdminController类

```
@Controller
public class ProductAdminController {

    @Resource
    private ProductService productService;

    @PostMapping("admin/product/add")
    public ApiRestResponse addProduct(@Valid @RequestBody AddProductReq addProductReq){
        productService.add(addProductReq);
        return ApiRestResponse.success();
    }
}
```

（5）图片上传

文件名UUID

通用唯一识别码（Universally Unique Identifier）

防止重名、防止爬图

生成规则：日期和时间、MAC地址、HashCode、随机数

（6）文件上传地址配置

a.到Constant配置常量FILE_UPLOAD_DIR

```
public class Constant {
	...
    @Value("${file.upload.dir}")
    public static String FILE_UPLOAD_DIR;
}
```

b.在application.properties添加配置信息

```
#上传文件的路径，根据部署情况，自行修改
file.upload.dir=D:/Java/code/Ethan-mall-image/
```

（7）到EthanMallExceptionEnum异常枚举类添加文件夹创建失败的错误

```
,MKDIR_FAILED(10014,"文件夹创建失败")
,UPLOAD_FAILED(10015,"图片上传失败")
```

（8）在ProductAdminController添加upload方法与getHost方法

```java
	@PostMapping("admin/upload/file")
    @ResponseBody
    public ApiRestResponse upload(HttpServletRequest httpServletRequest, @RequestParam("file")MultipartFile file){
        String fileName = file.getOriginalFilename();
        String suffixName = fileName.substring(fileName.lastIndexOf("."));
        //生成文件名称UUID
        UUID uuid = UUID.randomUUID();
        String newFileName = uuid.toString() + suffixName;
        File fileDirectory = new File(Constant.FILE_UPLOAD_DIR);
        File destFile = new File(Constant.FILE_UPLOAD_DIR + newFileName);
        if(!fileDirectory.exists()){
            if(!fileDirectory.mkdir()){
                throw new EthanMailException(EthanMallExceptionEnum.MKDIR_FAILED);
            }
        }
        //
        try {
            file.transferTo(destFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //StringBuffer+""变String格式
        try {
            return ApiRestResponse.success(getHost(new URI(httpServletRequest.getRequestURL()+""))+"/image/"+newFileName);
        } catch (URISyntaxException e) {
            return ApiRestResponse.error(EthanMallExceptionEnum.UPLOAD_FAILED);
        }
    }

    private URI getHost(URI uri){
        URI effectiveURI;
        try {
            effectiveURI = new URI(uri.getScheme(),uri.getUserInfo(),uri.getHost(),uri.getPort(),null,null,null);
        } catch (URISyntaxException e) {
            effectiveURI = null;
        }
        return effectiveURI;
    }

}
```

（9）测试添加商品

{"name":"新鲜泥猴桃","categoryId":5,"price":1000,"stock":10,"status":1,"detail":"新西兰黄心，黄金奇异果","image":"http://127.0.0.1:8083/image/6037baf8-5261-4560-be5a-32b8ee3823cf.png"}

![image-20230801174127897](D:\document\Typora\image\image-20230801174127897.png)

（10）测试图片上传

a.直接上传前台报系统异常，后台提示空指针

![image-20230801174151638](D:\document\Typora\image\image-20230801174151638.png)

![image-20230801174223670](D:\document\Typora\image\image-20230801174223670.png)

b.静态用普通方式加注解，不会将配置加载，需要在set方法加注解，同时类前要加@Component注解识别Value注解

<img src="D:\document\Typora\image\image-20230801174326563.png" alt="image-20230801174326563" style="zoom: 67%;" />

​																										↓

![image-20230801180247962](D:\document\Typora\image\image-20230801180247962.png)

![image-20230801180310776](D:\document\Typora\image\image-20230801180310776.png)

c.添加后，图片打不开，需配置资源映射

![image-20230801180350280](D:\document\Typora\image\image-20230801180350280.png)

上传图片后回显

配置SpringBootWebMvcConfig->静态资源到本地目录的映射->演示打开图片

```
        //凡是以images打头的都会转发到addResourceLocations的地址
        registry.addResourceHandler("/images/**").addResourceLocations("file:"+ Constant.FILE_UPLOAD_DIR);
```

到SpringBootWebMvcConfig配置

```java
/**
 * 配置地址映射
 */
@Configuration
public class EthanMallWebMvcConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //凡是以images打头的都会转发到addResourceLocations的地址
        registry.addResourceHandler("/images/**").addResourceLocations("file:"+ Constant.FILE_UPLOAD_DIR);
        registry.addResourceHandler("swagger-ui.html").addResourceLocations(
                "classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations(
                "classpath:/META-INF/resources/webjars/");
    }
}
```

重启，再次上传，可以打开看到图片

## 2.更新与删除商品接口

（1）新增UpdateCategoryReq类

```java
package com.ethan.mall.model.request;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class UpdateCategoryReq {
    //若直接注释@NotNull，异常提示不会显示哪个字段不能为null，可以加message属性自定义提示
    @NotNull(message = "id不能为null")
    private Integer id;
    @Size(min = 2,max = 5)
    private String name;
    @Max(3)
    private Integer type;
    private Integer parentId;
    private Integer orderNum;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public Integer getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }
}
```

（2）在ProdectServiceImpl添加update与delete方法

```
/**
 * 更新商品信息
 * @param updateProduct 要更新的Product对象
 */
@Override
public void update(Product updateProduct){
    Product productOld = productMapper.selectByName(updateProduct.getName());
    if(productOld != null && !updateProduct.getId().equals(productOld.getId())){
        throw new EthanMailException(EthanMallExceptionEnum.NAME_EXISTED);
    }
    int count = productMapper.updateByPrimaryKeySelective(updateProduct);
    if(count == 0 ){
        throw new EthanMailException(EthanMallExceptionEnum.UPDATE_FAILED);
    }
}


/**
 * 删除商品信息
 * @param id 要删除的商品id
 */
@Override
public void delete(Integer id){
    Product productOld = productMapper.selectByPrimaryKey(id);
    if(productOld == null){
        throw new EthanMailException(EthanMallExceptionEnum.DELETE_FAILED);
    }
    int count = productMapper.deleteByPrimaryKey(id);
    if(count == 0){
        throw new EthanMailException(EthanMallExceptionEnum.DELETE_FAILED);
    }

}
```

（3）在ProductAdminController新增updateProduct和deleteProduct方法

```
@PostMapping("/admin/product/update")
@ResponseBody
    public ApiRestResponse updateProduct(@Valid @RequestBody UpdateProductReq updateProductReq){
        Product product = new Product();
        BeanUtils.copyProperties(updateProductReq,product);
        productService.update(product);
        return ApiRestResponse.success();
    }

@PostMapping("/admin/product/delete")
@ResponseBody
public ApiRestResponse deleteProduct(@RequestParam("id") Integer id){
    productService.delete(id);
    return ApiRestResponse.success();
}
```

（4）测试

a.更新功能

![image-20230801225622022](D:\document\Typora\image\image-20230801225622022.png)

b.删除功能

![image-20230801225824611](D:\document\Typora\image\image-20230801225824611.png)

## 3.批量上下架商品接口开发

（1）在ProdectServiceImpl新增batchUpdateSellStatus方法

```
@Override
public void batchUpdateSellStatus(Integer[] ids, Integer sellStatus){
    productMapper.batchUpdateSellStatus(ids,sellStatus);
}
```

（2）在ProductMapper添加batchUpdateSellStatus方法

```
int batchUpdateSellStatus(@Param("ids") Integer[] ids,@Param("sellStatus") Integer sellStatus);
```

（3）在ProductMapper.xml添加batchUpdateSellStatus方法

```
<update id="batchUpdateSellStatus">
  update ethan_mall_product
  set status = #{sellStatus}
  where id in
  <foreach collection="ids" close=")" item="id" open="(" separator=",">
  #{id}
  </foreach>
</update>
```

（4）在ProductAdminController添加batchUpdateSellStatus方法

```
@ApiOperation("后台批量上下架接口")
@PostMapping("/admin/product/batchUpdateSellStatus")
@ResponseBody
public ApiRestResponse batchUpdateSellStatus(@RequestParam Integer[] ids,@RequestParam Integer sellStatus){
    productService.batchUpdateSellStatus(ids,sellStatus);
    return ApiRestResponse.success();
}
```

![image-20230802104144973](D:\document\Typora\image\image-20230802104144973.png)

## 4.查看后台商品列表

（1）在ProductMapper添加selectListForAdmin方法

```java
List<Product> selectListForAdmin();
```

（2）在ProductMapper新建selectListForAdmin方法

```xml
<select id="selectListForAdmin" resultMap="BaseResultMap">
  select
  <include refid="Base_Column_List"/>
  from
  ethan_mall_product
  order by update_time desc
</select>
```

（3）在ProdectServiceImpl添加方法listProductForAdmin

```java
@Override
public PageInfo listProductForAdmin(@RequestParam Integer pageNum, @RequestParam Integer pageSize){
    PageHelper.startPage(pageNum,pageSize);
    List<Product> products = productMapper.selectListForAdmin();
    PageInfo pageInfo = new PageInfo(products);
    return pageInfo;
}
```

（4）在ProductAdminController新建listProductForAdmin方法

```java
@ApiOperation("后台商品列表")
@GetMapping("/admin/product/list")
@ResponseBody
public ApiRestResponse listProductForAdmin(@RequestParam  Integer pageNum,@RequestParam Integer pageSize){
    PageInfo pageInfo = productService.listProductForAdmin(pageNum,pageSize);
    return ApiRestResponse.success(pageInfo);
}
```

## 5.前台商品详情

（1）在ProdectServiceImpl新增detail方法

```
@Override
public Product detail(@RequestParam Integer id){
    Product product = productMapper.selectByPrimaryKey(id);
    return product;
}
```

（2）在ProductController新增detail方法

```
@ApiOperation("商品详情")
@GetMapping("/product/detail")
@ResponseBody
public ApiRestResponse detail(@RequestParam Integer id){
    Product product = productService.detail(id);
    return ApiRestResponse.success(product);
}
```

## 6.前台商品列表

（1）搜索功能

![image-20230802145816425](D:\document\Typora\image\image-20230802145816425.png)

（2）新建请求ProductListReq类

有关键字、目录id，排序方式，页码，页长信息。

```java
package com.ethan.mall.model.request;

public class ProductListReq {
    private String keyword;

    private Integer categoryId;

    private String orderBy;

    private Integer pageNum = 1;

    private Integer pageSize = 10;

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
}
```

（2）com.ethan.mall.model新建query目录，在此目录下新建排序类ProductListQuery

类中包含关键字、目录id列表

```JAVA
package com.ethan.mall.model.query;

import java.util.List;

public class ProductListQuery {

    private String keyword;

    private List<Integer> categoryIds;


    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public List<Integer> getCategoryIds() {
        return categoryIds;
    }

    public void setCategoryIds(List<Integer> categoryIds) {
        this.categoryIds = categoryIds;
    }
}
```

（3）修改CategoryServiceImpl中的listCategoryForCustom，

改成需传入Integer parentId，同时在对应的引用与接口处也需要修改
方便之后查询相关信息的时候可以多次引用

![image-20230802153409229](D:\document\Typora\image\image-20230802153409229.png)

（3）在Constant新建一个排序set集合，让排序规则在掌控之中

```java
public interface  ProductListOrderBy{
    Set<String> PRICE_ASC_DESC = Sets.newHashSet("price desc","price asc");
}
```

（4）在ProductMapper新建方法selectList

```java
List<Product> selectList(@Param("query")ProductListQuery query);
```

（5）在ProductMapper新建selectList方法

```xml
  <select id="selectList" resultMap="BaseResultMap" parameterType="com.ethan.mall.model.query.ProductListQuery">
    select
    <include refid="Base_Column_List"/>
    from
    ethan_mall_product
    <where>
        <if test="query.keyword != null">
            and name like #{query.keyword}
        </if>
        <if test="query.categoryIds != null">
            and category_id in
            <foreach collection="query.categoryIds" close=")" item="item" open="(" separator=",">
                #{item}
            </foreach>
        </if>
        and status = 1
    </where>
    order by update_time desc
  </select>
```

（6）ProductServiceImpl中添加list和getCategoryIds方法

```java
@Override
public PageInfo list(ProductListReq productListReq){
    //构建Query对象
    ProductListQuery productListQuery = new ProductListQuery();

    //搜索处理
    if(!StringUtils.isEmpty(productListReq.getKeyword())){
        String keyword = new StringBuilder().append("%").append(productListReq.getKeyword()).append("%").toString();
        productListQuery.setKeyword(keyword);
    }

    //目录处理，若查某个目录下的商品，不仅查出该目录下的，还要把所有子目录的所有商品都查出来，所以要拿到一个目录id的List
    if(productListReq.getCategoryId() != null){
        //使用CategoryService中的接口提取id集合
        List<CategoryVO> categoryVOList = categoryService.listCategoryForCustomer(productListReq.getCategoryId());
        ArrayList<Integer> categoryIds = new ArrayList<>();
        categoryIds.add(productListReq.getCategoryId());
        getCategoryIds(categoryVOList,categoryIds);
        productListQuery.setCategoryIds(categoryIds);
    }

    //排序，为安全性，提前设置好可以排序什么
    String orderBy = productListReq.getOrderBy();
    if (Constant.ProductListOrderBy.PRICE_ASC_DESC.contains(orderBy)) {
        //传的排序方式支持排序
        PageHelper.startPage(productListReq.getPageNum(),productListReq.getPageSize(),orderBy);
    }else{
        //传的排序方式不支持排序，按默认输出
        PageHelper.startPage(productListReq.getPageNum(),productListReq.getPageSize());
    }
    List<Product> productList = productMapper.selectList(productListQuery);
    PageInfo pageInfo = new PageInfo(productList);
    return pageInfo;
}
//categoryVOList树状结构下面的所有id要拿到
private void getCategoryIds(List<CategoryVO> categoryVOList,ArrayList<Integer> categoryIds){
    for(int i = 0;i < categoryVOList.size() ; i++){
        CategoryVO categoryVO = categoryVOList.get(i);
        if(categoryVO != null){
            categoryIds.add(categoryVO.getId());
            getCategoryIds(categoryVO.getChildCategory(),categoryIds);
        }
    }
}
```

（7）ProductController新增list类

```
@ApiOperation("前台商品列表")
@GetMapping("product/list")
public ApiRestResponse list(ProductListReq productListReq){
    PageInfo pageInfo = productService.list(productListReq);
    return ApiRestResponse.success(pageInfo);
}
```

## 7.总结商品模块

重难点：商品的搜索、排序、目录查询

常见错误：更新和新增放在同一个接口、排序字段不用枚举