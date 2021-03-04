package com.ylwq.scaffold.server.gateway.filter;

import com.ylwq.scaffold.common.util.JsonUtil;
import com.ylwq.scaffold.common.util.ResponseDataUtil;
import com.ylwq.scaffold.common.vo.ResponseData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * 自定义全局过滤器<br/>
 * 使用场景：统一鉴权
 *
 * @Author thymi
 * @Date 2021/1/29
 */
//@Component
@Slf4j
public class CustomerGlobalFilter implements GlobalFilter, Ordered {

    /**
     * 过滤规则
     *
     * @param exchange
     * @param chain
     * @return
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        /* 模拟统一鉴权，token方式 */
        List<String> token = exchange.getRequest().getHeaders().get("token");
        if (token != null) {
            log.info("token：" + token.get(0));
        } else {
            log.info("没有获取到token");
            ServerHttpResponse response = exchange.getResponse();
            /* 响应头 */
            response.getHeaders().add("Content-Type", "application/json;charset=UTF-8");
            /* 响应状态码 */
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            /* 响应内容 */
            ResponseData responseData = ResponseDataUtil.buildUnAuthorized();
            String message = JsonUtil.objectToJson(responseData);
            DataBuffer buffer = response.bufferFactory().wrap(message.getBytes());
            return response.writeWith(Mono.just(buffer));
        }

        return chain.filter(exchange);
    }

    /**
     * 过滤器执行优先级，值越小优先级越高
     *
     * @return
     */
    @Override
    public int getOrder() {
        return 0;
    }
}
