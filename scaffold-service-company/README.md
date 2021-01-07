## 项目说明
微服务：公司服务

## 技术要点  
一、微服务接口及服务间调用；  
二、在线文档配置，[springdoc-openapi](https://github.com/springdoc/springdoc-openapi) ；  
三、文件上传与下载；  

### 一、微服务接口及服务间调用
实现步骤：
1. 在RestApi接口中定义`@RequestMapping`，这样在`RestController`（接口实现）和`RestFeign`（接口调用）中不需要再定义，可以提高开发效率；
```java
@RequestMapping("/user")
public interface UserRestApi {
    @GetMapping("/userInfo/{userId}")
    UserInfoDto getUserInfo(@PathVariable(value = "userId") String userId);
}
```
2. 微服务间互相调用通过Feign方式，Feign接口需要继承RestApi，并声明`@FeignClient`；
```java
@FeignClient(value = "service-user")
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

### 二、在线文档配置
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
2. 在``bootstrap.yml`或Nacos配置文件中配置springdoc；
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
@OpenAPIDefinition(
        info = @Info(
                title = "${spring.application.name}",
                version = "1.0.0",
                description = "SCAFFOLD-K8S"
        )
)
@SecurityScheme(name = HttpHeaders.AUTHORIZATION, type = SecuritySchemeType.HTTP, bearerFormat = "JWT", scheme = "bearer")
@Configuration
public class OpenApiConfig {
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
5. 重要注解说明：
> @OpenAPIDefinition：在springboot之中只会生效一个，用于描述该服务的全局信息；  
> @Tag：可以加在类和方法上，用来在页面中显示某个接口属于某个controller；  
> @Operation：用来描述一些接口的信息；  
> @Parameter：用来描述一些传入参数的信息；  
> @Schema：可以注解实体类；
> @Hidden: 指定某个类不显示文档；  

参考：
1. [Springboot2.3 + Springdoc-openapi3整合笔记](https://blog.csdn.net/zxt521yt/article/details/110558598)