package com.ylwq.scaffold.server.security.handler;

import com.ylwq.scaffold.common.util.ResponseDataUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * 认证服务器统一返回封装器
 * 将security的默认返回格式封装为业务系统统一格式ResponseData
 *
 * @Author thymi
 * @Date 2021/3/29
 */
@Slf4j
@ControllerAdvice
public class AuthorizationServerResponseBodyAdvice implements ResponseBodyAdvice {

    @Override
    public boolean supports(MethodParameter methodParameter, Class aClass) {
        /* 此处返回true，表示对任何handler的responsebody都调用beforeBodyWrite方法，如果有特殊方法不使用可以考虑使用注解等方式过滤 */
        return true;
    }

    /**
     * 统一返回处理，对Controller的所有返回结果进行处理
     *
     * @param body               是controller方法中返回的值，对其进行修改后再return
     * @param methodParameter    methodParameter
     * @param mediaType          mediaType
     * @param aClass             aClass
     * @param serverHttpRequest  serverHttpRequest
     * @param serverHttpResponse serverHttpResponse
     * @return 统一返回对象ResponseData
     */
    @Override
    public Object beforeBodyWrite(Object body, MethodParameter methodParameter, MediaType mediaType, Class aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        String errorKey = "error";

        /* 获取令牌，转换格式 */
        if (body instanceof DefaultOAuth2AccessToken) {
            DefaultOAuth2AccessToken token = (DefaultOAuth2AccessToken) body;
            Map<String, String> tokenInfo = new HashMap<>(3);
            tokenInfo.put("expiresIn", String.valueOf(token.getExpiresIn()));
            tokenInfo.put("refresh_token", token.getRefreshToken().getValue());
            tokenInfo.put("token", token.getValue());
            return ResponseDataUtil.buildSuccess(tokenInfo);
        }

        /* 认证异常处理 */
        if (body instanceof OAuth2Exception) {
            if (body.toString().contains(OAuth2Exception.INVALID_TOKEN)) {
                return ResponseDataUtil.buildUnAuthorized("无效的token", body);
            } else if (body.toString().contains(OAuth2Exception.UNSUPPORTED_GRANT_TYPE)) {
                return ResponseDataUtil.buildUnAuthorized("不支持的认证方式", body);
            } else if (body.toString().contains(OAuth2Exception.INVALID_GRANT)) {
                return ResponseDataUtil.buildUnAuthorized("用户名或密码错误", body);
            } else if (body.toString().contains(OAuth2Exception.ACCESS_DENIED)) {
                return ResponseDataUtil.buildForbidden(body);
            }
            return ResponseDataUtil.buildUnAuthorized(body);
        }

        /* 其他异常统一处理 */
        if (body.toString().contains(errorKey)) {
            return ResponseDataUtil.buildError(body);
        }

        return ResponseDataUtil.buildSuccess(body);
    }
}
