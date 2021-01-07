package com.ylwq.scaffold.service.project.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.apache.http.HttpHeaders;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 在线文档SpringDoc配置类<br/>
 * 定义描述`@OpenAPIDefinition`：servers，请求服务地址配置，可以按不同的环境配置；tags，用来定义一些公共参数说明，比如：token或者其他自定义key；<br/>
 * 安全配置`@SecurityScheme`：JWT Token。也可以配置其他类型的鉴权，比如：basic；<br/>
 *
 * @Author thymi
 * @Date 2021/1/7
 */
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

    /**
     * 分组配置
     *
     * @return GroupedOpenApi
     */
    @Bean
    public GroupedOpenApi storeOpenApi() {
        String[] packages = {"com.ylwq.scaffold.service.project"};
        return GroupedOpenApi.builder().group("service").packagesToScan(packages)
                .build();
    }

}
