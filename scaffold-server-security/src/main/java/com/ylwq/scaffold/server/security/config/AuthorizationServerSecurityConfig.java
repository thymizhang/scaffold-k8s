package com.ylwq.scaffold.server.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Oauth2认证服务器访问安全配置类
 *
 * @Author thymi
 * @Date 2021/3/30
 */
@Configuration
@EnableWebSecurity
public class AuthorizationServerSecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * 注入密码生成及校验规则，这里使用BCrypt规则，注入后，启动日志中将不再提供随机密码
     * 注意：数据库中记录的用户密码需要使用同样的加密规则
     * 在{@link com.ylwq.scaffold.server.security.service.UserDetailsServiceImpl UserDetailsServiceImpl}和{@link AuthorizationServerConfig AuthorizationServerConfig}中使用
     *
     * @return PasswordEncoder
     */
    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    /**
     * 重载configure，自定义访问安全配置
     *
     * @param http HttpSecurity
     * @throws Exception 异常
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.formLogin()
                .and()
                /* 允许登出 */
                .logout()
                .and()
                .authorizeRequests()
                /* 授权服务器，需要允许所有的访问 */
                .anyRequest().authenticated()
                .and()
                .csrf().disable();
    }
}
