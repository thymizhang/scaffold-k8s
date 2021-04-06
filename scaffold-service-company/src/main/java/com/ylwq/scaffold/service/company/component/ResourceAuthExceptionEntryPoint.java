package com.ylwq.scaffold.service.company.component;

import com.ylwq.scaffold.common.util.JsonUtil;
import com.ylwq.scaffold.common.util.ResponseDataUtil;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Oauth2资源服务器认证异常（401）自定义，token无效或过期
 *
 * @Author thymi
 * @Date 2021/3/29
 */
@Component
public class ResourceAuthExceptionEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        /* 响应头 */
        response.setHeader("Content-Type", "application/json;charset=UTF-8");
        /* 响应状态码 */
        response.setStatus(HttpStatus.UNAUTHORIZED.value());

        String message = JsonUtil.objectToJson(ResponseDataUtil.buildUnAuthorized("无效的token", authException.getMessage()));

        PrintWriter printWriter = response.getWriter();
        printWriter.write(message);
        printWriter.flush();
        printWriter.close();
    }

}

