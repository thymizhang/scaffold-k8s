## 项目说明
微服务：项目服务（前台），不建议后台使用同一套微服务

## 技术要点  
一、Sharding-jdbc分库分表应用；  
二、分布式事务；  
三、Redis缓存应用；  
四、Spring-AMQP+RabbitMq消息应用；  

### 一、Sharding-jdbc分库分表应用
* 分库分表方案
  1. 项目服务数据量大，且增长迅速、访问高频，按公司id对项目的所有数据进行水平分库；
  2. 项目中的部分业务数据（如清单等），数据规模大，且访问高频，按项目id对业务数据进行水平分表；
  3. 项目描述信息数据量大，与项目信息进行垂直分表；
  4. 所有分库分表数据均需要读写分离。
* 相关依赖
```xml
            <dependency>
                <groupId>org.apache.shardingsphere</groupId>
                <artifactId>sharding-jdbc-spring-boot-starter</artifactId>
                <version>4.1.1</version>
            </dependency>
            <!-- 结合sharding-jdbc，输出sql日志，测试时使用 -->
            <dependency>
                <groupId>p6spy</groupId>
                <artifactId>p6spy</artifactId>
                <version>3.9.1</version>
            </dependency>
```

### 三、Redis缓存应用
* 相关依赖
```xml
            <!-- Redis客户端Lettuce的连接池 -->
            <dependency>
                <groupId>org.apache.commons</groupId>
                <artifactId>commons-pool2</artifactId>
                <version>2.9.0</version>
            </dependency>

            <dependency>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-starter-data-redis</artifactId>
            </dependency>
```
* yaml配置
```yaml
spring:
  redis:
    host: redis.gceasy.cc
    port: 6379
    timeout: 6000  # 连接超时时长（毫秒）
    lettuce:
      pool: # 注意: 使用连接池需要加入commons-pool2依赖
        max-active: 1000  # 连接池最大连接数（使用负值表示没有限制）
        max-idle: 10    # 连接池中的最大空闲连接
        min-idle: 5     # 连接池中的最小空闲连接
        max-wait: -1   # 连接池最大阻塞等待时间（使用负值表示没有限制）
```
* 通过配置类[`RedisConfig.java`](src/main/java/com/ylwq/scaffold/service/project/config/RedisConfig.java)解决中文序列化乱码问题
* 使用工具类`RedisUtil.java`简化开发
* 缓存应用基本策略
> 1. 必须设置key的有效期，热点数据可以长期有效；
> 2. 增删改数据时同步更新缓存；
> 3. 计数类数据（如：点赞等）定时同步数据到数据库；
* Redis缓存案例 [`ProjectRestController.java`](src/main/java/com/ylwq/scaffold/service/project/controller/ProjectRestController.java)

### 四、Spring-AMQP+RabbitMq应用
* 相关依赖
```xml
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-amqp</artifactId>
        </dependency>
```
* yaml配置
```yaml
spring:
  rabbitmq:
    host: rabbitmq.gceasy.cc
    port: 5672
    username: root
    password: 123456
```
* 创建消息生产者，通过`AmqpTemplate`或`RabbitTemplate`发送消息，代码参考：[`ProjectAmqpProducer`](src/main/java/com/ylwq/scaffold/service/project/amqp/ProjectAmqpSender.java)
* 创建消息消费者，通过`@RabbitListener`接收并处理消息，代码参考：[`ProjectAmqpConsumer`](src/main/java/com/ylwq/scaffold/service/project/amqp/ProjectAmqpReceiver.java)