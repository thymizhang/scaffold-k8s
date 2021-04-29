package com.ylwq.scaffold.service.user.config;

import com.ylwq.scaffold.service.user.component.ResourceAuthExceptionEntryPoint;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

/**
 * Oauth2资源服务器配置
 *
 * @Author thymi
 * @Date 2021/3/31
 */
@Configuration
@EnableResourceServer
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    @Value("${spring.application.name}")
    private String serverName;

    private final ResourceAuthExceptionEntryPoint resourceAuthExceptionEntryPoint;

    public ResourceServerConfig(ResourceAuthExceptionEntryPoint resourceAuthExceptionEntryPoint) {
        this.resourceAuthExceptionEntryPoint = resourceAuthExceptionEntryPoint;
    }

    /**
     * jwt令牌转换器，将普通令牌转换为jwt令牌
     * 使用场景：有了jwt令牌，资源服务器就不用每次去认证服务器校验令牌
     * 认证服务器：转换器需要配置到AuthorizationServerConfig.configure(AuthorizationServerEndpointsConfigurer endpoints)
     * 资源服务器：转换器需要配置到ResourceServerConfig.tokenStore()
     *
     * @return JwtAccessTokenConverter
     */
    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
        /* 对称密钥，必须与认证服务器相同  */
        jwtAccessTokenConverter.setSigningKey("scaffold_jwt_key");
        return jwtAccessTokenConverter;
    }

    /**
     * 令牌存储策略
     *
     * @return TokenStore
     */
    @Bean
    public TokenStore tokenStore() {
        return new JwtTokenStore(jwtAccessTokenConverter());
    }

    /**
     * 资源服务器安全策略配置
     *
     * @param http http
     * @throws Exception 异常
     */
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                /* 放行测试接口和健康检查接口 */
                .antMatchers("/api/user/3", "/actuator/**").permitAll()
                .antMatchers("/api/user/**").access("#oauth2.hasAnyScope('scaffold','scaffold_admin')")
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .csrf().disable();
    }

    /**
     * 资源服务器配置
     *
     * @param resources 资源对象
     */
    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        resources
                .resourceId(this.serverName)
                .tokenStore(this.tokenStore())
                /* 自定义认证异常：没有携带token、过期 */
                .authenticationEntryPoint(this.resourceAuthExceptionEntryPoint)
                .stateless(true);
    }

}
