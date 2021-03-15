package com.ylwq.scaffold.server.security.config;

import com.ylwq.scaffold.server.security.handler.LoginAccessDeniedHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Security配置类<br/>
 * 作用：<br/>
 * 1. 实现自定义登录页面；<br/>
 * 2. 设置访问授权规则；<br/>
 * <br/>
 * 访问授权匹配规则：<br/>
 * ? : 匹配一个字符<br/>
 * * : 匹配0个或多个字符<br/>
 * **: 匹配0个或多个目录<br/>
 *
 * @Author thymi
 * @Date 2021/3/12
 */
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * 注入密码生成及校验规则，注入后，启动日志中将不再提供随机密码，
     * 注意：数据库中记录的用户密码需要使用
     * 在{@link com.ylwq.scaffold.server.security.service.UserDetailsServiceImpl UserDetailsServiceImpl}中使用
     *
     * @return PasswordEncoder
     */
    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 注入自定义无授权异常
     */
    private final LoginAccessDeniedHandler loginAccessDeniedHandler;

    public SecurityConfig(LoginAccessDeniedHandler loginAccessDeniedHandler) {
        this.loginAccessDeniedHandler = loginAccessDeniedHandler;
    }

    /**
     * 重载configure，实现自定义登录页面
     *
     * @param http HttpSecurity
     * @throws Exception 异常
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        /* 登录表单提交 */
        http.formLogin()
                /* 自定义登录页面 */
                .loginPage("/login.html")
                /* 自定义用户名参数 */
                .usernameParameter("username")
                /* 自定义密码参数 */
                .passwordParameter("password")
                /* 登录成功后跳转 只接受POST请求 */
                /*.successForwardUrl("/toMain")*/
                /* 登录成功后跳转自定义url，非post请求，不能与successForwardUrl同时使用 */
                /*.successHandler(new LoginAuthenticationSuccessHandler("/main.html"))*/
                /* 登录失败后跳转 只接受POST请求 */
                /*.failureForwardUrl("/toError")*/
                /* 登录失败后跳转自定义url，非post请求，不能与failureForwardUrl同时使用 */
                /*.failureHandler(new LoginAuthenticationFailureHandler("/error.html"));*/
                /* 登录请求接口 只接受POST请求 不要改 */
                .loginProcessingUrl("/login");

        /* 退出登录 */
        http.logout()
                /* 退出登录后跳转的页面，配置该页面后，url不再带有?logout参数 */
                .logoutSuccessUrl("/login.html");


        /* 访问授权，设置顺序：antMatchers -> anyRequest */
        http.authorizeRequests()
                /* 放行login.html、error.html */
                .antMatchers("/login.html", "/error.html").permitAll()
                /* 放行image目录下所有文件 */
                .antMatchers("/image/**").permitAll()
                /* 放行所有目录下的png文件 */
                .antMatchers("/**/*.png").permitAll()
                /* 正则表达式，放行所有png文件，默认任意请求类型 */
                .regexMatchers(".+[.]png").permitAll()
                /* 正则表达式，放行test请求，请求类型：GET */
                /*.regexMatchers(HttpMethod.GET, "/test").permitAll()*/
                /* 只有admin或normal权限(严格区分大小写)，才能访问admin.html页面 */
                /*.antMatchers("/admin.html").hasAnyAuthority("admin", "normal")*/
                /* 只有LEADER角色(注意不要加上ROLE_前缀)，才能访问admin.html页面 */
                /*.antMatchers("/leader.html").hasRole("LEADER")*/
                /* 只有ip地址127.0.0.1可以访问，注意如果同时配置了角色，只能生效后配置的一个 */
                /*.antMatchers("/leader.html").hasIpAddress("127.0.0.1")*/
                /* 所有请求必须登录才能访问，anyRequest必须放在最后 */
                .anyRequest().authenticated();

        /* 自定义没有授权403异常处理 */
        http.exceptionHandling()
                .accessDeniedHandler(this.loginAccessDeniedHandler);

        /* 关闭csrf防护 */
        http.csrf().disable();
    }
}
