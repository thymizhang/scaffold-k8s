package com.ylwq.scaffold.server.gateway.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

/**
 * gateway限流规则配置类，不推荐<br/>
 * 1. 以下bean对象不能同时使用，通过@Bean指定一个有效的限流规则；<br/>
 * 2. 使用限流过滤器时不能同时使用自定义过滤器；<br/>
 *
 * 不推荐原因：<br/>
 * 1. 需要额外使用redis实现令牌桶算法；
 * 2. 规则简单，不能处理复杂限流规则；
 * 3. 不能动态配置限流规则。
 *
 * @Author thymi
 * @Date 2021/2/1
 */
//@Configuration
@Slf4j
@Deprecated
public class KeyResolverConfig {

    /**
     * uri限流规则
     *
     * @return KeyResolver
     */
    public KeyResolver uriKeyResolver() {
        log.info("uri限流规则已配置 ... ");
        return exchange -> Mono.just(exchange.getRequest().getURI().getPath());
    }

    /**
     * 参数限流规则
     *
     * @return KeyResolver
     */
    public KeyResolver parameterKeyResolver() {
        log.info("参数限流规则已配置 ... ");
        return exchange -> Mono.just(exchange.getRequest().getQueryParams().getFirst("id"));
    }

    /**
     * ip限流规则
     *
     * @return KeyResolver
     */
    //@Bean
    public KeyResolver ipKeyResolver() {
        log.info("ip限流规则已配置 ... ");
        return exchange -> Mono.just(exchange.getRequest().getRemoteAddress().getHostName());
    }
}
