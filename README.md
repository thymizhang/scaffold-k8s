# scaffold-k8s

## 编写目的

帮助公司新来的小伙伴快速上手

## 项目介绍

1. scaffold-common : 公共工程，抽象和实现各类工具，技术要点：自定义异常和错误码，统一返回状态码规则，全局异常处理；
2. scaffold-server-security : 认证授权，基于Spring-Security-Oauth2认证授权框架、JWT无状态令牌实现；
3. scaffold-server-gateway : 服务网关，技术要点：网关路由配置、SpringFox在线文档聚合、Sentinel限流与熔断、基于JWT的统一鉴权；
4. scaffold-service-user : 用户微服务，技术要点：服务配置、Nacos配置中心应用、Json数据处理、Mybatis-plus应用、参数校验及全局捕获；
5. scaffold-service-company : 公司微服务，技术要点：微服务接口定义原则、服务间调用、SpringFox在线文档配置、定时任务；
6. scaffold-service-project : 项目微服务，技术要点：Sharding-jdbc分库分表应用、Redis缓存应用、Spring-AMQP+RabbitMq消息应用、分布式事务；
7. scaffold-service-xxxx-api : 微服务接口，面向其他微服务提供业务依赖；
* 注意：server和service工程均可独立打包部署，每个微服务需要独立配置数据库；

## 应用分层

在《Java开发手册》中，阿里巴巴技术团队对应用分层的建议如下图：  
![应用分层](resource/image/application_layer.png)  
本架构面向中小型项目，采用四层架构，即Web层、Rest层、Service层、Dao层

1. 调用链说明：controller->rest->service->dao；
2. Web（controller）层：为前端提供接口，返回统一对象{@link ResponseData ResponseData}；
3. Rest（api）层：为controller层和其他微服务提供接口，参数校验，业务逻辑实现，返回DTO对象；
4. Service层：为rest层提供实体类操作接口，封装dao，丰富操作，返回Entity对象；
5. Dao（mapper）层：为service层提供实体类基本CRUD操作，自定义查询，返回Entity对象；

## 开发环境

- 操作系统: Window10 20H2
- 开发工具: Idea 2021.1
- java环境: JDK11
- 本地k8s环境: minikube 1.18.1
- 公网k8s环境：Aliyun ACK

## 运行环境及部署

* 运行环境

1. JDK 11.0.8
2. MySQL 8.0.21
3. Redis 6.2.3
4. RabbitMQ 3.8.16
5. Nacos 2.0.1
6. Sentinel 1.8.1

* 命令行启动(windows环境下需要指定编码)

```
java -Dport=8080 -Dfile.encoding=UTF-8 -jar xxx.jar --spring.profiles.active=dev
```

* 部署项目到kubernetes环境  
包括本地k8s环境（minikube）部署和阿里云ACK部署，详情参考[k8s部署](resource/k8s/README.md)


## 数据库
* 手动创建以下数据库，并运行对应脚本
1. scaffold_user : scaffold_user.sql
2. scaffold_company : scaffold_company.sql
3. scaffold_project_0 : scaffold_project.sql
4. scaffold_project_1 : scaffold_project.sql
5. scaffold_oauth2 : scaffold_oauth2.sql  

## 打包说明

1. 子模块打包找不到依赖时，先maven install父工程；
2. spring-boot应用中，当有多个类包含main方法时，需要指定一个mainClass；
```xml
<build>
    <plugins>
        <plugin>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-maven-plugin</artifactId>
            <configuration>
                <mainClass>com.ylwq.scaffold.service.user.UserApplication</mainClass>
            </configuration>
        </plugin>
    </plugins>
</build>
```
3. spring-boot应用中，既要打可执行包又需要非可执行包的方式；

```xml

<build>
    <plugins>
        <plugin>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-maven-plugin</artifactId>
            <configuration>
                <classifier>exec</classifier>
            </configuration>
        </plugin>
    </plugins>
</build>
```


## 版本操作

* 项目全局版本更新

> 命令：`mvn versions:set -DnewVersion=版本号`  
> 说明：会产生一个旧版本备份文件`pom.xml.versionsBackup`

* 项目全局版本回滚

> 命令：`mvn versions:revert`  
> 说明：恢复`pom.xml.versionsBackup`文件中的版本，回滚完成后自动删除`pom.xml.versionsBackup`文件

* 项目全局版本确认

> 命令：`mvn versions:commit`  
> 说明：确认后会自动删除`pom.xml.versionsBackup`文件

## 问题

* 程序包XXX不存在  
> 在Reload all maven projects之后，File -> Invalidate Caches...  
> 参考：https://baijiahao.baidu.com/s?id=1666135264347817292&wfr=spider&for=pc  

* 项目中maven依赖报红的处理

> 1. 使用idea自带功能：File -> Invalidate Caches / Restart...   
> 2. 如果还有依赖报红，在pom.xml文件中修改报红依赖的版本，重载，再改回原来的版本，再重载 

* 使用lombok的@Slf4j，但log没有info的方法

> 解决：需要spring-boot依赖
> ```xml
>         <dependency>
>              <groupId>org.springframework.boot</groupId>
>              <artifactId>spring-boot-starter</artifactId>
>          </dependency>
> ```

* 微服务启动报错误：`Annotation @EnableCircuitBreaker found, but there are no implementations`

> 解决：添加依赖，注意：spring-cloud2020版本后，可以取消该依赖
> ```xml
>         <dependency>
>             <groupId>org.springframework.cloud</groupId>
>             <artifactId>spring-cloud-starter-netflix-hystrix</artifactId>
>         </dependency>
> ```

* 同样配置信息下，bootstrap.yml与bootstrap.properties差异

> 1. mybatis-plus在控制输出信息时：yml配置比properties完整，yml配置输出信息包含数据源信息；

* 微服务启动告警：`Spring Cloud LoadBalancer is currently working with the default cache. You can switch to using Caffeine cache, by adding it to the classpath.`

> 解决：添加Caffeine依赖
> ```xml
>         <dependency>
>             <groupId>com.github.ben-manes.caffeine</groupId>
>             <artifactId>caffeine</artifactId>
>         </dependency>
> ```

* 镜像构建报错：`no main manifest attribute, in application.jar`  

> 解决：在打包插件中声明execution
> ```xml
>                <plugin>
>                    <!-- 打可执行包插件声明，包括jar、war、docker镜像等，微服务子模块必须要声明该插件 -->
>                    <groupId>org.springframework.boot</groupId>
>                    <artifactId>spring-boot-maven-plugin</artifactId>
>                    <version>2.3.9.RELEASE</version>
>                    <configuration>
>                        <layers>
>                            <enabled>true</enabled>
>                        </layers>
>                    </configuration>
>                    <executions>
>                        <execution>
>                            <goals>
>                                <goal>repackage</goal>
>                            </goals>
>                        </execution>
>                    </executions>
>                </plugin>
> ```


## 升级日志

* [2020.01] springboot升级到2.4.2，springcloud升级到2020.0.1，nacos升级到2.2.5.RELEASE，mybatisplus升级到3.4.2

1. 启动类的注解`@SpringCloudApplication`不再推荐使用，回到`@SpringBootApplication`；
2. 启动类无需添加`@EnableDiscoveryClient`，只要有依赖即可服务发现；
3. 不再需要依赖`spring-cloud-starter-netflix-hystrix`，`Annotation @EnableCircuitBreaker found`报错消失；
4. 需要依赖`spring-cloud-starter-bootstrap`，方可使用`bootstrap.yml`或`bootstrap.properties`配置文件；
5. 配置文件加载顺序变化：`bootstrap.yml`优先级最高；
6. 由于依赖`spring-cloud-alibaba-dependencies`不再维护，需要使用nacos、sentinel、seata时，单独引入依赖；
7. feign不再使用`ribbon`作为负载均衡，改为`loadbalancer`，但会与nacos服务发现冲突，需要新增和修改依赖：

```xml

<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-loadbalancer</artifactId>
</dependency>
<dependency>
    <groupId>com.alibaba.cloud</groupId>
    <artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
    <!-- 由于feign使用了spring-cloud-loadbalancer作为负载均衡，这里要排除ribbon -->
    <exclusions>
        <exclusion>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-ribbon</artifactId>
        </exclusion>
    </exclusions>
</dependency>
```

## 参考

1. [Java开发手册-嵩山版](resource/pdf/Java开发手册-嵩山版.pdf)
1. [Spring Cloud 微服务与 K8S 的协作之道](https://blog.csdn.net/weixin_44388301/article/details/99575907?utm_medium=distribute.pc_relevant.none-task-blog-BlogCommendFromBaidu-5.control&depth_1-utm_source=distribute.pc_relevant.none-task-blog-BlogCommendFromBaidu-5.control)
2. [关于领域模型转换的那些事儿](https://www.jianshu.com/p/a7f56a9b9c33)
3. [ShardingSphere 4.x Sharding-JDBC 用户手册之YAML配置手册](https://my.oschina.net/u/3777515/blog/4450623)
