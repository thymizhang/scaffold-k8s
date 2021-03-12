package com.ylwq.scaffold.server.security.handler;

import com.ylwq.scaffold.common.util.JsonUtil;
import com.ylwq.scaffold.common.util.ResponseDataUtil;
import com.ylwq.scaffold.common.vo.ResponseData;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 授权异常（403）自定义，当用户没有权限访问资源时返回
 *
 * @Author thymi
 * @Date 2021/3/12
 */
@Component
public class LoginAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException, ServletException {
        /* 响应头 */
        response.setHeader("Content-Type", "application/json;charset=UTF-8");
        /* 响应状态码 */
        response.setStatus(HttpStatus.FORBIDDEN.value());
        /* 响应内容，json格式 */
        ResponseData responseData = ResponseDataUtil.buildForbidden();
        String message = JsonUtil.objectToJson(responseData);
        PrintWriter printWriter = response.getWriter();
        printWriter.write(message);
        printWriter.flush();
        printWriter.close();
    }
}
