package com.ylwq.scaffold.server.security.config;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

/**
 * Oauth2认证服务器配置类<br/>
 * 注意：本项目客户端只能使用密码模式
 * <p>
 * 使用Postman获取令牌请求配置：
 * POST请求->http://localhost:8080/oauth/token
 * Body->x-www-form-urlencoded->client_id:scaffold_web,client_secret:111111,grant_type:password,username:刘一,password:111111
 * <p>
 * 使用Postman刷新令牌请求配置：
 * POST请求->http://localhost:8080/oauth/token
 * Authorization->Basic Auth->Username:scaffold_web,Password:111111
 * Body->x-www-form-urlencoded->grant_type:password,refresh_token:刷新令牌
 *
 * @Author thymi
 * @Date 2021/3/30
 */
@Configuration
@AllArgsConstructor
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    /**
     * 数据源，用于存储客户端信息，需要创建数据库scaffold_oauth2
     */
    private final DataSource dataSource;

    /**
     * 密码策略，通过AuthorizationServerSecurityConfig注入
     */
    private final PasswordEncoder passwordEncoder;

    /**
     * JWT内容增强器，令牌配置时使用
     */
    private final JwtTokenEnhancer jwtTokenEnhancer;

    /**
     * 认证管理器，通过AuthorizationServerSecurityConfig注入，令牌配置时使用
     */
    private final AuthenticationManager authenticationManager;

    /**
     * 用户详情，参考：UserDetailsServiceImpl，令牌配置时使用
     */
    private final UserDetailsService userDetailsService;

    /**
     * jwt令牌转换器，将普通令牌转换为jwt令牌
     * 使用场景：jwt自带用户信息，资源服务器就不用每次去认证服务器校验令牌
     * 认证授权服务器：转换器需要配置到AuthorizationServerEndpointsConfigurer
     * 资源服务器：转换器需要配置到TokenStore，再将TokenStore配置到ResourceServerConfig.ResourceServerSecurityConfigurer
     *
     * @return JwtAccessTokenConverter
     */
    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
        /* 对称密钥，必须与资源服务器相同 */
        jwtAccessTokenConverter.setSigningKey("scaffold_jwt_key");
        return jwtAccessTokenConverter;
    }

    /**
     * 客户端详情存储方式
     *
     * @param clients clients
     * @throws Exception 异常
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        /* 配置客户端为数据库存储 */
        JdbcClientDetailsService jdbcClientDetailsService = new JdbcClientDetailsService(dataSource);
        jdbcClientDetailsService.setPasswordEncoder(passwordEncoder);
        clients.withClientDetails(jdbcClientDetailsService);
    }

    /**
     * 配置令牌访问端点
     *
     * @param endpoints 访问端点
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        /* 配置JWT内容增强器 */
        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        List<TokenEnhancer> delegates = new ArrayList<>();
        delegates.add(this.jwtTokenEnhancer);
        delegates.add(this.jwtAccessTokenConverter());
        tokenEnhancerChain.setTokenEnhancers(delegates);

        endpoints
                .authenticationManager(this.authenticationManager)
                .userDetailsService(this.userDetailsService)
                .tokenEnhancer(tokenEnhancerChain)
                .allowedTokenEndpointRequestMethods(HttpMethod.POST);
    }

    /**
     * 配置令牌安全约束
     *
     * @param security 安全约束
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) {
        security
                /* 对/oauth/token_key 公开不拦截 */
                .tokenKeyAccess("permitAll()")
                /* 对/oauth/check_token 公开不拦截, 防止资源服务器提交的token检查请求被拦截，适用于授权码模式 */
                .checkTokenAccess("permitAll()")
                .allowFormAuthenticationForClients();
    }

}
