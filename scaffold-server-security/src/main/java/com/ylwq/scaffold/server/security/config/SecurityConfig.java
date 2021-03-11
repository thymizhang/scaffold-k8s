package com.ylwq.scaffold.server.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Security配置类
 *
 * @Author thymi
 * @Date 2021/3/9
 */
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * 注入密码生成及校验规则
     * 在{@link com.ylwq.scaffold.server.security.service.UserDetailsServiceImpl UserDetailsServiceImpl}中使用
     *
     * @return PasswordEncoder
     */
    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        /* 表单提交 */
//        http.formLogin()
//                /* 自定义登录页面 */
//                .loginPage("/login.html")
//                /* 登录请求 */
//                .loginProcessingUrl("/login")
//                /* 登录成功后跳转 只接受POST请求 */
//                .successForwardUrl("/main");
//
//        http.authorizeRequests()
//                /* 放行login.html */
//                .antMatchers("/login.html").permitAll()
//                /* 所有请求必须登录才能访问 */
//                .anyRequest().authenticated();
//
//        http.csrf().disable();
//    }
}
