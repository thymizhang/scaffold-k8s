package com.ylwq.scaffold.server.gateway.config;

import com.alibaba.csp.sentinel.adapter.gateway.common.SentinelGatewayConstants;
import com.alibaba.csp.sentinel.adapter.gateway.common.api.ApiDefinition;
import com.alibaba.csp.sentinel.adapter.gateway.common.api.ApiPathPredicateItem;
import com.alibaba.csp.sentinel.adapter.gateway.common.api.GatewayApiDefinitionManager;
import com.alibaba.csp.sentinel.adapter.gateway.common.rule.GatewayFlowRule;
import com.alibaba.csp.sentinel.adapter.gateway.common.rule.GatewayParamFlowItem;
import com.alibaba.csp.sentinel.adapter.gateway.common.rule.GatewayRuleManager;
import com.alibaba.csp.sentinel.adapter.gateway.sc.SentinelGatewayFilter;
import com.alibaba.csp.sentinel.adapter.gateway.sc.callback.BlockRequestHandler;
import com.alibaba.csp.sentinel.adapter.gateway.sc.callback.GatewayCallbackManager;
import com.alibaba.csp.sentinel.adapter.gateway.sc.exception.SentinelGatewayBlockExceptionHandler;
import com.ylwq.scaffold.common.util.ResponseDataUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.config.GatewayProperties;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.reactive.result.view.ViewResolver;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 基于sentinel的gateway限流、降级、熔断配置<br/>
 * <br/>
 * 两种资源限流配置方法介绍：<br/>
 * 一、面向路由的限流<br/>
 * 1. 先在yml配置文件中配置网关路由routes；<br/>
 * 2. 对每个routes单独配置限流规则（getGatewayRulesForRoute）或对所有routes统一配置限流规则（initUnifiedGatewayRulesForRoute）<br/>
 * 二、面向API分组的限流<br/>
 * 1. 定义API分组（initCustomizedApis）；<br/>
 * 2. 为API分组添加限流规则（getGatewayRulesForApis）；<br/>
 * 注：以上两种方式可以同时使用，API分组限流可以作为路由限流的补充。<br/>
 * <br/>
 * 使用场景举例：<br/>
 * 1. 频繁提交写操作：比如注册 -> ip限流；<br/>
 * 2. 恶意数据抓取 -> ip限流；<br/>
 * 3. 热门链接导致压力过大 -> url限流；<br/>
 * 4. 系统频繁报错 -> 降级；<br/>
 * <br/>
 * 问题：ip限流测试未通过。
 *
 * @Author thymi
 * @Date 2021/2/24
 */
@Configuration
@Slf4j
public class GatewaySentinelConfig {

    private final GatewayProperties gatewayProperties;

    private final List<ViewResolver> viewResolvers;

    private final ServerCodecConfigurer serverCodecConfigurer;

    /**
     * 这里提示viewResolvers参数报错，其实没有问题，是idea的问题
     *
     * @param viewResolvers         viewResolvers
     * @param serverCodecConfigurer serverCodecConfigurer
     */
    public GatewaySentinelConfig(List<ViewResolver> viewResolvers, ServerCodecConfigurer serverCodecConfigurer, GatewayProperties gatewayProperties) {
        this.viewResolvers = viewResolvers;
        this.serverCodecConfigurer = serverCodecConfigurer;
        this.gatewayProperties = gatewayProperties;
    }

    /**
     * 通过block来触发限流规则
     *
     * @return SentinelGatewayBlockExceptionHandler
     */
    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public SentinelGatewayBlockExceptionHandler sentinelGatewayBlockExceptionHandler() {
        // Register the block exception handler for Spring Cloud Gateway.
        return new SentinelGatewayBlockExceptionHandler(viewResolvers, serverCodecConfigurer);
    }

    @Bean
    @Order(-1)
    public GlobalFilter sentinelGatewayFilter() {
        return new SentinelGatewayFilter();
    }

    /**
     * Spring容器初始化时执行，存在多个相同规则时，后加载的规则会覆盖之前的规则
     */
    @PostConstruct
    public void doInit() {
        /* 加载自定义API分组 */
        initCustomizedApis();
        /* 加载限流规则 */
        initGatewayRules();
        /* 加载自定义限流异常处理器 */
        initBlockHandler();
    }

    private void initGatewayRules() {
        Set<GatewayFlowRule> rules = new HashSet<>();
        /* 添加面向路由的限流规则 */
        Set<GatewayFlowRule> gatewayRulesForRoute = getGatewayRulesForRoute();
        for (GatewayFlowRule rule : gatewayRulesForRoute) {
            rules.add(rule);
        }

        /* 添加面向路由的统一限流规则 */
        // todo: getUnifiedGatewayRulesForRoute

        /* 添加面向API分组的限流规则 */
        Set<GatewayFlowRule> gatewayRulesForApis = getGatewayRulesForApis();
        for (GatewayFlowRule rule : gatewayRulesForApis) {
            rules.add(rule);
        }
        /* 加载限流规则 */
        GatewayRuleManager.loadRules(rules);
    }

    /**
     * 面向路由的限流规则<br/>
     * 前提：需要配置文件中为微服务配置网关路由，并且不使用gateway默认的限流过滤器RequestRateLimiter<br/>
     *
     * @return 限流规则
     */
    private Set<GatewayFlowRule> getGatewayRulesForRoute() {
        Set<GatewayFlowRule> rules = new HashSet<>();
        rules.add(new GatewayFlowRule("service-user")
                /* 限流资源模式：路由id */
                .setResourceMode(SentinelGatewayConstants.RESOURCE_MODE_ROUTE_ID)
                /* 限流阈值 */
                .setCount(4)
                /* 统计时间窗口，单位：秒，默认：1秒 */
                .setIntervalSec(30)
        );
        log.info("sentinel：面向路由的限流规则已加载");
        return rules;
    }

    /**
     * 面向路由的统一限流规则<br/>
     * 前提：需要配置文件中为微服务配置网关路由，并且不使用gateway默认的限流过滤器RequestRateLimiter<br/>
     * 这里为每个服务设置了统一的限流规则，如果需要为每个服务单独设置限流，参考initGatewayRulesForRoute方法<br/>
     * 使用场景：通常根据微服务整体负载能力设置限流阈值，在压力过大时保护服务不因过载崩溃<br/>
     *
     * @return 限流规则
     */
    private Set<GatewayFlowRule> getUnifiedGatewayRulesForRoute() {
        List<RouteDefinition> routes = gatewayProperties.getRoutes();
        Set<GatewayFlowRule> rules = new HashSet<>();
        for (RouteDefinition route : routes) {
            rules.add(new GatewayFlowRule(route.getId())
                    /* 限流资源模式：路由id */
                    .setResourceMode(SentinelGatewayConstants.RESOURCE_MODE_ROUTE_ID)
                    /* 限流阈值 */
                    .setCount(10)
                    /* 统计时间窗口，单位：秒，默认：1秒 */
                    .setIntervalSec(30)
                    /* 对ip限流 */
                    .setParamItem(new GatewayParamFlowItem()
                            .setParseStrategy(SentinelGatewayConstants.PARAM_PARSE_STRATEGY_CLIENT_IP)
                    )
            );
        }
        log.info("sentinel：面向路由的统一限流规则已加载");
        return rules;
    }

    /**
     * 自定义API分组<br/>
     * 作用：对资源进行更细粒度的分配限流，这种方式在控制台看不到限流规则
     */
    private void initCustomizedApis() {
        Set<ApiDefinition> definitions = new HashSet<>();
        /* 自定义API分组1 */
        ApiDefinition api1 = new ApiDefinition("customized_api1")
                /* 一个分组可添加多个匹配规则 */
                .setPredicateItems(new HashSet<>() {{
                    /* 匹配规则1 */
                    add(new ApiPathPredicateItem().setPattern("/api/user/1"));
                    /* 匹配规则2 */
                    add(new ApiPathPredicateItem().setPattern("/api/user/2"));
                }});
        /* 自定义API分组2 */
        ApiDefinition api2 = new ApiDefinition("customized_api2")
                .setPredicateItems(new HashSet<>() {{
                    add(new ApiPathPredicateItem().setPattern("/api/company/**")
                            /* 匹配规则：根据前缀模糊匹配，默认匹配规则：URL_MATCH_STRATEGY_EXACT（精确匹配） */
                            .setMatchStrategy(SentinelGatewayConstants.URL_MATCH_STRATEGY_PREFIX));
                }});
        /* 自定义API分组3 */
        ApiDefinition api3 = new ApiDefinition("customized_api3")
                .setPredicateItems(new HashSet<>() {{
                    add(new ApiPathPredicateItem().setPattern("/api/project/company/**")
                            .setMatchStrategy(SentinelGatewayConstants.URL_MATCH_STRATEGY_PREFIX));
                }});

        definitions.add(api1);
        definitions.add(api2);
        definitions.add(api3);
        log.info("sentinel：API分组已加载");
        GatewayApiDefinitionManager.loadApiDefinitions(definitions);
    }

    /**
     * 面向API分组的限流规则<br/>
     * 限流因子说明：<br/>
     * 以客户端IP作为限流因子 PARAM_PARSE_STRATEGY_CLIENT_IP = 0;<br/>
     * 以客户端HOST作为限流因子 PARAM_PARSE_STRATEGY_HOST = 1;<br/>
     * 以客户端HEADER参数作为限流因子 PARAM_PARSE_STRATEGY_HEADER = 2;<br/>
     * 以客户端请求参数作为限流因子 PARAM_PARSE_STRATEGY_URL_PARAM = 3;<br/>
     * 以客户端请求Cookie作为限流因子 PARAM_PARSE_STRATEGY_COOKIE = 4;<br/>
     *
     * @return 限流规则
     */
    private Set<GatewayFlowRule> getGatewayRulesForApis() {
        Set<GatewayFlowRule> rules = new HashSet<>();
        rules.add(new GatewayFlowRule("customized_api1")
                /* 限流模式：API分组，默认：RESOURCE_MODE_ROUTE_ID（路由id）*/
                .setResourceMode(SentinelGatewayConstants.RESOURCE_MODE_CUSTOM_API_NAME)
                /* 限流阈值 */
                .setCount(10)
                /* 统计时间窗口，单位：秒，默认：1秒 */
                .setIntervalSec(15)
        );
        /* 以客户端IP作为限流因子 */
        rules.add(new GatewayFlowRule("customized_api2")
                .setResourceMode(SentinelGatewayConstants.RESOURCE_MODE_CUSTOM_API_NAME)
                .setCount(2)
                .setIntervalSec(15)
                .setParamItem(new GatewayParamFlowItem().setParseStrategy(SentinelGatewayConstants.PARAM_PARSE_STRATEGY_CLIENT_IP))
        );
        rules.add(new GatewayFlowRule("customized_api3")
                .setResourceMode(SentinelGatewayConstants.RESOURCE_MODE_CUSTOM_API_NAME)
                .setCount(2)
                .setIntervalSec(15)
                /* 以客户端请求参数作为限流因子，不设置ParamItem，默认为url限流 */
                .setParamItem(new GatewayParamFlowItem()
                        .setParseStrategy(SentinelGatewayConstants.PARAM_PARSE_STRATEGY_URL_PARAM)
                        .setFieldName("id"))
        );
        log.info("sentinel：面向API分组的限流规则已加载");
        return rules;
    }


    /**
     * 自定义限流异常处理器<br/>
     * 作用：定义限流后的返回信息，否则系统默认返回：Blocked by Sentinel: ParamFlowException
     */
    private void initBlockHandler() {
        BlockRequestHandler blockRequestHandler = (serverWebExchange, throwable) -> {
            log.info(throwable.getClass().toString());
            // todo: 当熔断发生时，可针对性添加处理逻辑

            return ServerResponse.status(HttpStatus.TOO_MANY_REQUESTS).
                    contentType(MediaType.APPLICATION_JSON).
                    body(BodyInserters.fromValue(ResponseDataUtil.buildTooManyRequests()));
        };
        log.info("sentinel：自定义限流异常处理器已加载");
        GatewayCallbackManager.setBlockHandler(blockRequestHandler);
    }

}
