package com.ylwq.scaffold.server.gateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

import java.util.*;

/**
 * swagger分布式聚合文档配置类<br/>
 * gateway整合swagger
 *
 * @Author thymi
 * @Date 2021/3/3
 */
@Component
@Primary
public class SwaggerProvider implements SwaggerResourcesProvider {

    /**
     * swagger3默认的url后缀
     */
    private static final String OAS_30_URL = "/v3/api-docs";

    private final RouteLocator routeLocator;

    /**
     * 网关应用名称
     */
    @Value("${spring.application.name}")
    private String self;

    public SwaggerProvider(RouteLocator routeLocator) {
        this.routeLocator = routeLocator;
    }

    @Override
    public List<SwaggerResource> get() {
        List<SwaggerResource> resources = new ArrayList<>();
        //List<String> routeHosts = new ArrayList<>();
        Set<String> routeHosts = new HashSet<>();
        routeLocator.getRoutes()
                .filter(route -> route.getUri().getHost() != null)
                .filter(route -> Objects.equals(route.getUri().getScheme(), "lb"))
                .subscribe(route -> routeHosts.add(route.getUri().getHost()));

        /* 过滤掉网关服务器，如果服务器名更新这里也需要同步更新 */
        routeHosts.remove("server-gateway");
        /* 过滤掉认证服务器，如果服务器名更新这里也需要同步更新 */
        routeHosts.remove("server-security");

        /* 记录已经添加过的微服务，存在同一个微服务注册了多个服务在注册中心上 */
        Set<String> serverUrls = new HashSet<>();
        routeHosts.forEach(instance -> {
            /* 拼接url ，请求swagger的url */
            String url = "/" + instance.toLowerCase() + OAS_30_URL;
            if (!serverUrls.contains(url)) {
                serverUrls.add(url);
                SwaggerResource swaggerResource = new SwaggerResource();
                swaggerResource.setUrl(url);
                swaggerResource.setName(instance);
                swaggerResource.setSwaggerVersion("3.0.3");
                resources.add(swaggerResource);
            }
        });
        return resources;
    }
}
