## 项目说明

服务网关

## 技术要点

一、spring-cloud-gateway配置；
二、服务限流sentinel集成；
三、在线文档springfox集成；
四、统一认证集成；
五、统一访问日志记录；


### 一、spring-cloud-gateway配置

1. 相关依赖

```xml
        <!-- spring-gateway依赖 -->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-gateway</artifactId>
        </dependency>
```

2. 路由-断言配置  
   说明：每个断言`predicates`，匹配一个`uri`，将对应的路径转发到后端的微服务，如果微服务已经集群，`uri`使用`lb://[服务名]`

```yaml
spring:
  cloud:
    gateway:
      routes:
        - id: service-user                            # 路由id
          uri: lb://service-user                      # 目标uri
          predicates:                                 # 断言（判断条件）
            - Path=/api/user/**
```

3. 路由-过滤器配置（不推荐）
   说明：过滤器有很多种，这里重点说明全局过滤器和限流过滤器  
   3.1. 全局过滤器  
   通过自定义全局过滤器实现统一鉴权，代码参考[`CustomerGlobalFilter.java`](src/main/java/com/ylwq/scaffold/server/gateway/filter/CustomerGlobalFilter.java)   
   3.2. 限流过滤器  
   ① 限流过滤器声明；

```yaml
spring:
  cloud:
    gateway:
      routes:
        - id: service-user                            # 路由id
          uri: lb://service-user                      # 目标uri
          predicates:                                 # 断言（判断条件）
            - Path=/api/user/**
          filters:
            - name: RequestRateLimiter                # 配置限流过滤器，限流规则通过KeyResolverConfig注入，如果使用sentinel限流，需要取消该过滤器
              args:
                key-resolver: "#{@ipKeyResolver}"     # 使用SpEL表达式引用bean对象，对应于KeyResolverConfig中的bean对象方法ipKeyResolver
                redis-rate-limiter.replenishRate: 1   # 令牌桶每秒填充平均速率
                redis-rate-limiter.burstCapacity: 2   # 令牌桶容量
```

② 由于限流过滤器使用令牌桶算法，需要添加redis依赖；

```xml
        <!-- 网关过滤器RequestRateLimiterGatewayFilterFactory的令牌桶算法需要redis -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-redis-reactive</artifactId>
        </dependency>
        <!-- 新版本的redis使用lettuce连接池，lettuce需要依赖commons-pool2-->
        <dependency>
            <groupId>org.apache.commons</groupId>
            <artifactId>commons-pool2</artifactId>
        </dependency>
```

```yaml
spring:
   redis:  # 网关限流过滤器的令牌桶算法需要使用redis缓存
      host: redis.gceasy.cc
      port: 6379
      database: 0
      timeout: 30000
      lettuce:
         pool:  # redis连接池配置
            max-active: 1000  # 最大可用连接数（默认为8，负数表示无限）
            max-idle: 10      # 最大空闲连接数（默认为8，负数表示无限）
            min-idle: 5       # 最小空闲连接数（默认为0，该值只有为正数才有用）
            max-wait: -1      # 从连接池中获取连接最大等待时间（默认为-1，单位为毫秒，负数表示无限）
```

③ 配置限流规则；
代码参考[`KeyResolverConfig.java`](src/main/java/com/ylwq/scaffold/server/gateway/config/KeyResolverConfig.java)

4. 网关高可用
> 1 使用不同端口启动多个gateway；  
> 2 利用nginx实现网关集群，在nginx.conf中配置；
> ```text
> upstream localhost {
>   server 192.168.213.140:9000;
>   server 192.168.213.140:9001;
> }
> 
> server {
>   listen      80;
>   server_name localhost;
> 
>   location / {
>       proxy_pass  http://localhost
>   }
> }
> ```
> 说明：如果是kubernetes环境，使用其默认负载均衡规则
> 


### 二、服务限流sentinel限流集成

* 为什么要使用sentinel限流？
> sentinel支持动态配置限流规则，且无代码侵入。gateway默认的限流规则不够灵活，规则不能组合。

* 使用sentinel注意事项
> 1 需要配置sentinel-dashboard控制台，[下载地址](https://github.com/alibaba/Sentinel/releases)；   
> 2 不要配置gateway默认的限流规则`RequestRateLimiter`；  
> 3 gateway需要配置网关路由；  

1. 相关依赖
```xml
        <dependency>
            <groupId>com.alibaba.csp</groupId>
            <artifactId>sentinel-spring-cloud-gateway-adapter</artifactId>
        </dependency>
```
2. 配置文件
```yml
spring:
  cloud:
    sentinel:
      eager: true                          # 取消Sentinel控制台懒加载，即项目启动即连接
      filter:
        enabled: false                     # 如果在网关流控控制台上看到了url资源，就是此项没有设为false
      transport:
        dashboard: sentinel.gceasy.cc:9090 # 如果需要可视化查看限流，可配置Sentinel DashBoard地址
```
3. 整合配置类：[`GatewaySentinelConfig.java`](/src/main/java/com/ylwq/scaffold/server/gateway/config/GatewaySentinelConfig.java)
4. 限流规则配置：GatewaySentinelConfig.getGatewayRulesForRoute（网关路由限流）和GatewaySentinelConfig.getGatewayRulesForApis（API分组限流）
5. 自定义限流异常处理器：GatewaySentinelConfig.initBlockHandler


### 三、在线文档springfox集成
* 前提：所有微服务都需要配置springfox3.0.0
* 待解决问题：1 设置全局token；2 gateway集群时，请求host被替换
1. 相关依赖
```xml
        <dependency>
            <groupId>io.springfox</groupId>
            <artifactId>springfox-boot-starter</artifactId>
        </dependency>
```   
2. 通过配置文件开启文档
```yaml
springfox:
  documentation:
    swagger-ui:
      enabled: true     # 开启swagger3文档显示
```
3. 通过swagger分布式聚合文档配置类来实现文档聚合，参考：[`SwaggerProvider.java`](/src/main/java/com/ylwq/scaffold/server/gateway/config/SwaggerProvider.java)
4. 注意事项
> 如果使用nginx搭建了网关高可用，nginx的反向代理配置中，一定要使用正确的host，案例：`upstream localhost`  


### 四、统一认证集成
* 思路：由于gateway使用的时webflux引擎，深度整合security改造量巨大，所以这里我们采用全局过滤器进行sope级别的鉴权。如果需要深度整合，建议采用zuul网关。
* 方案：在网关全局过滤器中解析JWT，对微服务sope进行鉴权，参见：[`AuthorizationGlobalFilter.java`](/src/main/java/com/ylwq/scaffold/server/gateway/filter/AuthorizationGlobalFilter.java)



### 问题

* 启动报错：`Failed to introspect Class [org.springdoc.webmvc.ui.SwaggerWelcome] from ClassLoader`

> 原因：与`springdoc-openapi-ui`依赖冲突  
> 解决：在pom文件中限制`springdoc-openapi-ui`依赖范围
>
> ```xml
>     <dependency>
>         <groupId>org.springdoc</groupId>
>         <artifactId>springdoc-openapi-ui</artifactId>
>         <scope>test</scope>
>     </dependency>
> ```