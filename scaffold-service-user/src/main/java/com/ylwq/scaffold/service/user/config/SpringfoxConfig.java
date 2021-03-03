package com.ylwq.scaffold.service.user.config;

import io.swagger.annotations.ApiOperation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.*;
import springfox.documentation.schema.ScalarType;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ParameterType;
import springfox.documentation.service.RequestParameter;
import springfox.documentation.service.Response;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.ArrayList;
import java.util.List;

/**
 * Springfox(swagger3)在线文档配置类
 *
 * @Author thymi
 * @Date 2021/3/2
 */
@Configuration
public class SpringfoxConfig implements WebMvcConfigurer {

    @Bean
    public Docket createRestApi() {
        /* 返回文档摘要信息 */
        return new Docket(DocumentationType.OAS_30)
                .apiInfo(apiInfo())
                .select()
                /* 只显示ApiOperation标注的api，同时避免显示basic-error-controller */
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                //.apis(RequestHandlerSelectors.withMethodAnnotation(Operation.class))
                .paths(PathSelectors.any())
                .build();
        //.globalRequestParameters(getGlobalRequestParameters())
        //.globalResponses(HttpMethod.GET, getGlobalResponseMessage())
        //.globalResponses(HttpMethod.POST, getGlobalResponseMessage());
    }


    /**
     * 生成接口信息，包括标题、联系人等
     *
     * @return
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("scaffold-k8s接口文档")
                .build();
    }

    /**
     * 生成全局参数，自动为每个请求增加的参数（可选）
     *
     * @return
     */
    private List<RequestParameter> getGlobalRequestParameters() {
        List<RequestParameter> parameters = new ArrayList<>();
        parameters.add(new RequestParameterBuilder()
                .name("token")
                .description("请求令牌")
                .required(true)
                .in(ParameterType.QUERY)
                .query(q -> q.model(m -> m.scalarModel(ScalarType.STRING)))
                .required(false)
                .build());
        return parameters;
    }

    /**
     * 生成通用响应信息
     *
     * @return
     */
    private List<Response> getGlobalResponseMessage() {
        List<Response> responseList = new ArrayList<>();
        responseList.add(new ResponseBuilder().code("404").description("找不到资源").build());
        return responseList;
    }
}
