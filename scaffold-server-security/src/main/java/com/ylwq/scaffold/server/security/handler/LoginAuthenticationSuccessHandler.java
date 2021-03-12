package com.ylwq.scaffold.server.security.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 自定义登录成功重定向，在SecurityConfig中调用
 * 解决successForwardUrl不能跳转到静态页面的问题
 *
 * @Author thymi
 * @Date 2021/3/12
 */
@Slf4j
public class LoginAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    /**
     * 登录成功跳转地址
     */
    private final String successUrl;

    public LoginAuthenticationSuccessHandler(String successUrl) {
        this.successUrl = successUrl;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        User user = (User) authentication.getPrincipal();
        log.info("已登录用户：" + user.getUsername());
        log.info("已登录用户权限：" + user.getAuthorities());
        response.sendRedirect(successUrl);
    }
}
