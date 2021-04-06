## 项目说明
认证授权


## 技术要点
一、Oauth2认证服务器配置；  
二、Oauth2资源服务器配置；  
三、与gateway整合；    


### oauth2认证授权应用策略
1. 公司内部产品  
   1.1. 认证服务器采用password + refresh_token模式，基于jwt的认证授权，加密方式为对称加密；  
   1.2. 网关服务负责转发请求，并对请求进行scope级别的鉴权；    
   1.3. 在微服务中配置资源服务器，负责解析jwt令牌和方法级别的鉴权；
2. 公司对外的开放接口  
   2.1. 认证授权服务器采用client_credentials + refresh_token模式，jwt令牌和非对称加密；  
   2.2. 网关服务负责统一校验token；
* 本项目为公司内部产品，暂不提供开放接口


### 一、Oauth2认证服务器配置
1. 相关依赖
```xml
      <dependency>
         <groupId>org.springframework.cloud</groupId>
         <artifactId>spring-cloud-starter-oauth2</artifactId>
      </dependency>
      <!-- Redis客户端Lettuce的连接池，spring-security需要redis管理令牌 -->
      <dependency>
         <groupId>org.apache.commons</groupId>
         <artifactId>commons-pool2</artifactId>
      </dependency>
```
2. 访问安全配置  
   2.1. 继承类`WebSecurityConfigurerAdapter` ，配置注解`@Configuration`和`@EnableWebSecurity`，参考：`AuthorizationServerConfig`   
   2.2. 自定义安全访问策略，参考：`AuthorizationServerSecurityConfig.configure(HttpSecurity http) `
   2.3. 配置密码策略，认证服务器配置会用到，参考：`AuthorizationServerSecurityConfig.getPasswordEncoder()`  
   2.4. 配置认证管理器，认证服务器配置会用到，参考：`AuthorizationServerSecurityConfig.authenticationManager()`
3. 自定义用户认证逻辑  
   3.1. 实现接口`UserDetailsService`，配置注解`@Service`，参考：`UserDetailsServiceImpl`  
   3.2. 重载`loadUserByUsername(String username)`方法，调用用户服务接口，实现基于业务的用户密码认证  
4. 认证服务器配置  
   4.1. 继承类`AuthorizationServerConfigurerAdapter` ，配置注解`@Configuration`和`@EnableAuthorizationServer`，参考：`AuthorizationServerConfig`  
   4.2. 客户端信息使用数据存储，创建Oauth2数据库`scaffold_oauth2`，并导入表和数据`scaffold_oauth2.sql`   
   4.3. 在`pom.xml`配置mysql驱动依赖，在`application.yml`配置数据源  
   4.4. 客户端详情存储方式配置，载入数据源，参考：`AuthorizationServerConfig.datasource`  
   4.5. 客户端详情存储方式配置，载入密码策略，参考：`AuthorizationServerConfig.passwordEncoder`   
   4.6. 客户端详情存储方式配置，参考：`AuthorizationServerConfig.configure(ClientDetailsServiceConfigurer clients)`  
   4.7. JWT令牌配置，配置JWT令牌转换器，参考：`AuthorizationServerConfig.jwtAccessTokenConverter()`    
   4.8. JWT令牌配置，创建JWT令牌增强器，并载入，参考：`JwtTokenEnhancer`、`AuthorizationServerConfig.jwtTokenEnhancer`  
   4.9. JWT令牌配置，载入认证管理器和用户详情，参考：`AuthorizationServerConfig.authenticationManager`、`AuthorizationServerConfig.userDetailsService`  
   4.10. JWT令牌配置，配置令牌访问端点，参考：`AuthorizationServerConfig.configure(AuthorizationServerEndpointsConfigurer endpoints)`  
   4.11. JWT令牌配置，配置令牌安全约束，参考：`AuthorizationServerConfig.configure(AuthorizationServerSecurityConfigurer security)`  
5. 统一返回配置  
5.1. 通过拦截Oauth2认证服务器返回信息，实现返回统一，参考：`AuthorizationServerResponseBodyAdvice`  


### 二、Oauth2资源服务器配置
* 配置前提  
需要每个微服务都配置资源服务，并为接口分配权限，参考微服务：service-company  
1. 相关依赖：  
```xml
      <dependency>
         <groupId>org.springframework.cloud</groupId>
         <artifactId>spring-cloud-starter-oauth2</artifactId>
      </dependency>
      <!-- Redis客户端Lettuce的连接池，spring-security需要redis管理令牌 -->
      <dependency>
         <groupId>org.apache.commons</groupId>
         <artifactId>commons-pool2</artifactId>
      </dependency>
      <!-- JWT令牌解析工具 -->
      <dependency>
         <groupId>com.nimbusds</groupId>
         <artifactId>nimbus-jose-jwt</artifactId>
      </dependency>
```   
2. 自定义认证异常，参考：`ResourceAuthExceptionEntryPoint`  
3. 资源服务器配置  
   3.1. 继承类`ResourceServerConfigurerAdapter` ，配置注解`@Configuration`、`@EnableAuthorizationServer`、`@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)`，参考：`ResourceServerConfig`  
   3.2. 配置JWT令牌转换器，参考：`ResourceServerConfig.jwtAccessTokenConverter()`  
   3.3. 配置令牌存储策略，参考：`ResourceServerConfig.tokenStore()`  
   3.4. 配置资源服务器安全访问策略，参考：`ResourceServerConfig.configure(HttpSecurity http)`  
   3.5. 配置资源服务器，参考：`ResourceServerConfig.configure(ResourceServerSecurityConfigurer resources)`  
4. 接口权限配置  
4.1. 先确认在资源服务器配置类使用了注解`@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)`  
4.2. 在controller类或方法上使用注解`@PreAuthorize("hasAnyAuthority('权限1','权限2')")`配置权限，参考：`CompanyController.getCompany()`    
4.3. 在controller类或方法上使用注解`@PreAuthorize("hasRole('角色')")`或`@Secured("ROLE_角色")`（注意这里要加上`ROLE_`）配置角色，参考：`CompanyController.getPrincipal`、`CompanyController.getUser`  
5. 通过JWT获取当前用户，参考：`CompanyController.getJwt()`  


### 三、与gateway整合
* gateway整合oauth2原则  
转发Oauth2所有请求，对业务请求进行scope级别的鉴权，以下内容均在server-gateway工程实现。  
1. 相关依赖  
```xml
      <!-- JWT令牌解析工具 -->
      <dependency>
         <groupId>com.nimbusds</groupId>
         <artifactId>nimbus-jose-jwt</artifactId>
      </dependency>
```   
2. 在`application.yml`中添加认证服务器路由  
```yaml
spring:
  cloud:
    gateway:
      discovery:
        locator:
          enabled: true                               # 是否开启基于服务发现的路由规则，该方式不会生成路由id，只能通过默认url访问，访问方式：http://localhost/服务名/api/**
          lower-case-service-id: true                 # 是否将服务名称转小写
        - id: server-security
          uri: lb://server-security
          predicates:
            - Path=/oauth/**
```
3. 新增全局过滤器，对客户端scope进行鉴权，参考：`AuthorizationGlobalFilter`  


### 问题：
* 在security工程中通过feign调用user服务失败
> 错误信息：RibbonLoadBalancerClient does not define or inherit an implementation of the resolved method abstract choose  
> 解决：经测试是springboot与springcloud版本不兼容导致，这里我们改为最后的稳定兼容版本(2.3.9.RELEASE+Hoxton.SR10)，问题解决。

* 微服务整合oauth2实现资源管理器，启动后，sharding-jdbc报错
> 错误信息：SQLFeatureNotSupportedException: isValid  
> 解决：经测试是spring版本兼容问题，需要重写`DataSourceHealthContributorAutoConfiguration`，详见`DataSourceHealthConfig`  
> 参考：https://blog.csdn.net/qq_33547169/article/details/106565788

* 待解决问题: 在Gateway解决统一鉴权  