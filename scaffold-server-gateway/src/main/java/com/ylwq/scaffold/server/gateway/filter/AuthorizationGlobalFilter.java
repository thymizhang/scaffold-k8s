package com.ylwq.scaffold.server.gateway.filter;

import cn.hutool.json.JSONObject;
import com.nimbusds.jose.JWSObject;
import com.ylwq.scaffold.common.util.JsonUtil;
import com.ylwq.scaffold.common.util.ResponseDataUtil;
import com.ylwq.scaffold.common.vo.ResponseData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.text.ParseException;
import java.util.List;

/**
 * 网关全局过滤器，实现scope级别的鉴权
 *
 * @Author thymi
 * @Date 2021/4/6
 */
@Component
@Slf4j
public class AuthorizationGlobalFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();
        String oauthPath = "/oauth";
        ServerHttpResponse response = exchange.getResponse();
        /* 响应头 */
        response.getHeaders().add("Content-Type", "application/json;charset=UTF-8");
        /* 对非oauth2请求，进行token校验 */
        if (!path.startsWith(oauthPath)) {
            HttpHeaders headers = exchange.getRequest().getHeaders();
            List<String> authorization = headers.get("Authorization");
            /* 获取jwt令牌 */
            String token = authorization.get(0);
            if(token == null){
                /* 响应状态码 */
                response.setStatusCode(HttpStatus.UNAUTHORIZED);
                /* 响应内容 */
                ResponseData responseData = ResponseDataUtil.buildUnAuthorized();
                String message = JsonUtil.objectToJson(responseData);
                DataBuffer buffer = response.bufferFactory().wrap(message.getBytes());
                return response.writeWith(Mono.just(buffer));
            }
            String realToken = token.replace("Bearer ", "");
            String userStr = "";
            JWSObject jwsObject = null;
            try {
                jwsObject = JWSObject.parse(realToken);
                userStr = jwsObject.getPayload().toString();
                JSONObject jsonObject = new JSONObject(userStr);
                /* 获取jwt中的scope信息 */
                List<String> scopes = jsonObject.getJSONArray("scope").toList(String.class);
                log.info("全局过滤器：");
                scopes.forEach(System.out::println);

                if(path.startsWith("/api/user") && scopes.contains("scaffold")){
                    return chain.filter(exchange);
                }

                if(path.startsWith("/api/company") && scopes.contains("scaffold")){
                    return chain.filter(exchange);
                }

                if(path.startsWith("/api/project") && scopes.contains("scaffold")){
                    return chain.filter(exchange);
                }

                /* scope校验失败，返回403 */
                response.setStatusCode(HttpStatus.FORBIDDEN);
                /* 响应内容 */
                ResponseData responseData = ResponseDataUtil.buildForbidden();
                String message = JsonUtil.objectToJson(responseData);
                DataBuffer buffer = response.bufferFactory().wrap(message.getBytes());
                return response.writeWith(Mono.just(buffer));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 0;
    }

}
