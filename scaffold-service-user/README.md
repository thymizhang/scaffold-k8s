## 项目说明
微服务：用户服务（前台），不建议后台使用同一套微服务

## 技术要点
* 服务注册与发现、配置中心均采用Nacos，这里不作说明，请参考[官方文档](https://nacos.io/zh-cn/docs/what-is-nacos.html)  
一、 微服务配置及配置读取；
二、 Json数据的null值替换；  
三、 Mybatis-plus应用；  
  
### 一、微服务配置及配置读取
1. spring-cloud微服务默认配置文件为`bootstrap.yml`或`bootstrap.properties`；
2. bootstrap文件中至少要配置`spring.application.name`属性，指定服务名称；
3. 如果使用nacos作为配置中心，数据库相关配置（mysql、mybatis、sharding-jdbc）必须配置在主文件中（以服务名称命名的文件），避免配置失效；
4. 如果使用nacos作为配置中心，配置文件按功能单独命名，避免混淆；
* 配置信息读取方法
1. 通过@Value("${xxx.xxx}")可以获取任何一个配置文件中的属性值；
```java
    @Value("${server.port}")
    private String port;
```
2. 使用Nacos的Java SDK读取，代码如下；
```java
    String serverAddr = "nacos.gceasy.cc:8848";
    String dataId = "combo.properties";
    String group = "SCAFFOLD";
    String nameSpace = "SCAFFOLD-DEV";
    Properties properties = new Properties();
    properties.put(PropertyKeyConst.SERVER_ADDR, serverAddr);
    properties.put(PropertyKeyConst.NAMESPACE,nameSpace);
    ConfigService configService = NacosFactory.createConfigService(properties);
    String content = configService.getConfig(dataId, group, 5000);
    System.out.println("读取的配置信息：" + content);
```

### 二、Json数据的null值替换
* 为什么要替换null？
  > 将null替换为空字符串，有利于前端快速开发，不用再为频繁处理null值耗费时间
1. 将Controller返回给前端Json数据中的null统一替换为空字符串，案例：[JsonConfig.java](src/main/java/com/ylwq/scaffold/service/user/config/JsonConfig.java)；
2. 在手动Json格式转换过程中，将Json数据中的null统一替换为空字符串，案例：[JsonUtil.java](../scaffold-common/src/main/java/com/ylwq/scaffold/common/util/JsonUtil.java);

### 三、Mybatis-plus应用 [官方文档](https://mybatis.plus/guide/)
1. Mybatis-plus配置，案例：[MyBatisPlusConfig.java](src/main/java/com/ylwq/scaffold/service/user/config/MyBatisPlusConfig.java)；
2. 实体对象与数据库表映射，案例：[UserInfo.java](src/main/java/com/ylwq/scaffold/service/user/entity/UserInfo.java)；
3. 创建DAO，案例：[UserInfoMapper.java](src/main/java/com/ylwq/scaffold/service/user/dao/UserInfoMapper.java)；
4. 创建Service，案例：[UserInfoService.java](src/main/java/com/ylwq/scaffold/service/user/service/UserInfoService.java)；
5. 实现Service，案例：[UserInfoServiceImpl.java](src/main/java/com/ylwq/scaffold/service/user/service/impl/UserInfoServiceImpl.java)；

