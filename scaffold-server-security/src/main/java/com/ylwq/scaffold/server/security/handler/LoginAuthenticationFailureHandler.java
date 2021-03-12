package com.ylwq.scaffold.server.security.handler;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 自定义登录失败重定向，在SecurityConfig中调用
 * 解决failureForwardUrl不能跳转到静态页面的问题
 *
 * @Author thymi
 * @Date 2021/3/12
 */
public class LoginAuthenticationFailureHandler implements AuthenticationFailureHandler {

    /**
     * 登录失败跳转地址
     */
    private final String failUrl;

    public LoginAuthenticationFailureHandler(String failUrl) {
        this.failUrl = failUrl;
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException {
        response.sendRedirect(failUrl);
    }
}
