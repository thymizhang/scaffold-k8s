package com.ylwq.scaffold.server.security.config;

import com.ylwq.scaffold.server.security.domain.SecurityUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * JWT内容增强器，向令牌中增加更多信息
 * 如果不使用JWT内容增强器，可以在org.springframework.security.core.userdetails.User的username字段放入json数据来实现信息扩展，参考：UserDetailsServiceImpl
 *
 * @Author thymi
 * @Date 2021/3/30
 */
@Component
@Slf4j
public class JwtTokenEnhancer implements TokenEnhancer {

    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        log.info("JWT内容增强器已执行...");
        SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();
        /* 将用户id加入到jwt令牌中 */
        Map<String, Object> infoMap = new HashMap<>(0);
        if (securityUser != null && securityUser.getId() != null) {
            infoMap.put("user_id", securityUser.getId());
        }
        ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(infoMap);
        return accessToken;
    }

}
