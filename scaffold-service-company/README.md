## 项目说明
微服务：公司服务

## 技术要点  
一、微服务对外接口定义原则；
二、微服务接口及服务间调用；  
三、在线文档配置之springfox；  
四、定时任务；

### 一、微服务对外接口定义原则
* 重点：将前端访问接口与微服务之间访问的接口分开编写
1. 前端访问接口定义：`xxxxController`；
2. 服务间访问接口定义：`xxxxServiceController`，并实现接口：`xxxxRestApi`；
3. 前端访问接口使用统一返回对象：`ResponseData`；
4. 服务间访问接口使用数据传输对象`DTO`，不建议使用实体对象`Entity`；
5. 服务调用接口使用统一命名规则：`xxxxFeign`，并继承接口`xxxxRestApi`；

### 二、微服务接口及服务间调用
实现步骤：
1. 在RestApi接口中定义`@RequestMapping`，这样在`RestController`（接口实现）和`RestFeign`（接口调用）中不需要再定义，可以提高开发效率；
```java
@RequestMapping("/user")
public interface UserRestApi {
    @GetMapping("/userInfo/{userId}")
    UserInfoDto getUserInfo(@PathVariable(value = "userId") String userId);
}
```
2. 微服务间互相调用通过Feign方式，Feign接口需要继承RestApi，并声明`@FeignClient`，name和url在配置文件中声明；
```java
@FeignClient(name = "${service.user.provider.name}", url = "${service.user.provider.url}")
public interface UserRestFeign extends UserRestApi {
}
```
3. 使用Feign的微服务，需要在启动类加上注解`@EnableFeignClients`；
```java
@SpringCloudApplication
@EnableFeignClients
public class CompanyApplication {
    public static void main(String[] args) {
        SpringApplication.run(CompanyApplication.class, args);
    }
}
```

### 三、在线文档配置之springfox
* springfox与springdoc
  springfox优势：应用广泛，与spring-cloud-gateway整合相对容易
  springdoc优势：配置简单，支持OAuth2

1. 相关依赖
```xml
        <dependency>
            <groupId>io.springfox</groupId>
            <artifactId>springfox-boot-starter</artifactId>
        </dependency>
```
2. 在线文档 启动/关闭 配置
```properties
# swagger-ui地址：http://localhost:port/swagger-ui/index.html
springfox.documentation.swagger-ui.enabled = true
```
3. 配置类，配置文档摘要等信息，参考：[`SpringfoxConfig.java`](src/main/java/com/ylwq/scaffold/service/company/config/SpringfoxConfig.java)


### 四、定时任务

1. 在启动类使用`@EnableScheduling`开启定时任务；
2. 在组件中的方法使用`@Scheduled`设置定时任务，参考：[`ScheduleJobs.java`](src/main/java/com/ylwq/scaffold/service/company/component/ScheduleJobs.java)；
```text
定时周期设置参考：
*/5 * * * *		每5分钟执行
5 * * * *		每小时的第5分钟执行一次
30 5 * * *		每天的 5:30 执行
30 7 8 * *		每月8号的7:30分执行
30 5 8 6 *		每年的6月8日5:30执行
30 6 * * 0		每星期日的6:30执行 [注：0表示星期天，1表示星期1，以此类推，也可以用英文来表示，sun表示星期天，mon表示星期一等]
30 3 10,20 * *	每月10号及20号的3：30执行 [注：“，”用来连接多个不连续的时段]
25 8-11 * * *	每天8-11点的第25分钟执行 [注：“-”用来连接连续的时段]
*/15 * * * *	每15分钟执行一次 [即每个小时的第0 15 30 45 60分钟执行 ]
30 6 */10 * *	每个月中，每隔10天6:30执行一次 [即每月的1、11、21、31日是的6：30执行一次]
```
3. 如何避免在集群时，定时任务重复执行？
* 思路：使用redis分布式锁:setnx命令

### 在线文档配置之springdoc
* springdoc介绍 
  springdoc是api文档标准OpenApi的非官方实现，另一个是springfox。springdoc帮助使用者将swagger3集成到SpringBoot中，springdoc-openapi库支持：
  > OpenAPI 3  
  > Spring-boot (v1 and v2)  
  > JSR-303，专门用于@NotNull、@Min、@Max和@Size。 Swagger-ui  
  > OAuth 2  
1. 在pom.xml加入`springdoc-openapi-ui`依赖；
```xml
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-ui</artifactId>
    <version>1.5.0</version>
</dependency>
```
2. 在`bootstrap.yml`或Nacos配置文件中配置springdoc；
```yaml
springdoc:
  api-docs:
    enabled: true #开启在线文档
    groups:
      enabled: true #启用分组方式显示各个微服务的API文档
    path: /docs #修改访问地址为：http://localhost:8083/docs/  默认地址：http://localhost:8083/v3/api-docs/
  swagger-ui:
    path: docs.html #修改SwaggerUI访问地址为：http://localhost:8083/docs.html  默认访问地址：http://localhost:8083/swagger-ui.html
```
3. 添加配置类[`OpenApiConfig`](src/main/java/com/ylwq/scaffold/service/company/config/OpenApiConfig.java)，定义描述、安全配置、配置分组等；
```java
/**
 * 在线文档SpringDoc配置类<br/>
 * 定义描述`@OpenAPIDefinition`：servers，请求服务地址配置，可以按不同的环境配置；tags，用来定义一些公共参数说明，比如：token或者其他自定义key；<br/>
 * 安全配置`@SecurityScheme`：JWT Token。也可以配置其他类型的鉴权，比如：basic；<br/>
 *
 */
@OpenAPIDefinition(
        info = @Info(
                title = "${spring.application.name}",
                version = "1.0.0",
                description = "SCAFFOLD-K8S"
        ),
        externalDocs = @ExternalDocumentation(description = "参考文档",
                url = "https://github.com/swagger-api/swagger-core/wiki/Swagger-2.X---Annotations"
        )
)
@SecurityScheme(name = HttpHeaders.AUTHORIZATION, type = SecuritySchemeType.HTTP, bearerFormat = "JWT", scheme = "bearer")
@Configuration
public class OpenApiConfig {

  /**
   * 分组配置
   *
   * @return GroupedOpenApi
   */
  @Bean
  public GroupedOpenApi storeOpenApi() {
    String[] packages = {"com.ylwq.scaffold.service.company", "com.ylwq.scaffold.service.user"};
    return GroupedOpenApi.builder().group("service").packagesToScan(packages)
            .build();
  }

}
```
4. 使用注解添加文档说明；
```java
// 以下是springfox和springdoc注解映射关系
@Api -> @Tag
@ApiIgnore -> @Parameter(hidden = true) or @Operation(hidden = true) or @Hidden
@ApiImplicitParam -> @Parameter
@ApiImplicitParams -> @Parameters
@ApiModel -> @Schema
@ApiModelProperty(hidden = true) -> @Schema(accessMode = READ_ONLY)
@ApiModelProperty -> @Schema
@ApiOperation(value = "foo", notes = "bar") -> @Operation(summary = "foo", description = "bar")
@ApiParam -> @Parameter
@ApiResponse(code = 404, message = "foo") -> @ApiResponse(responseCode = "404", description = "foo")
```
```java
// 方法标注举例
    @Operation(summary = "获取公司成员",
            parameters = {
                    @Parameter(name = "companyId", description = "公司id")
            },
            security = @SecurityRequirement(name = HttpHeaders.AUTHORIZATION))
```
5. 重要注解说明：
> @OpenAPIDefinition：在springboot之中只会生效一个，用于描述该服务的全局信息；  
> @Tag：可以加在类和方法上，用来在页面中显示某个接口属于某个controller；  
> @Operation：用来描述一些接口的信息；  
> @Parameter：用来描述一些传入参数的信息；  
> @Schema：可以注解实体类；
> @Hidden: 指定某个类不显示文档；  

参考：
1. [Springboot2.3 + Springdoc-openapi3整合笔记](https://blog.csdn.net/zxt521yt/article/details/110558598)