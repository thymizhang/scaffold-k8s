package com.ylwq.scaffold.server.security.service;


import com.ylwq.scaffold.common.exception.UserErrorCode;
import com.ylwq.scaffold.server.security.domain.SecurityUser;
import com.ylwq.scaffold.server.security.feign.UserRestFeign;
import com.ylwq.scaffold.service.user.dto.UserLoginDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * 自定义Security用户认证逻辑<br/>
 * 覆盖UserDetailsService.loadUserByUsername方法，改写用户认证逻辑
 *
 * @Author thymi
 * @Date 2021/3/30
 */
@Service
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRestFeign userRestFeign;

    public UserDetailsServiceImpl(UserRestFeign userRestFeign) {
        this.userRestFeign = userRestFeign;
    }

    /**
     * 从用户服务中获取密码和权限，交给spring-security去验证<br/>
     * 技术要点：<br/>
     * 1 请求类型必须是POST；<br/>
     * 2 用户名和密码参数默认是：username和password，可以在AuthorizationServerSecurityConfig中指定；<br/>
     * 参考：UsernamePasswordAuthenticationFilter<br/>
     *
     * @param username 用户名
     * @return org.springframework.security.core.userdetails.User
     * @throws UsernameNotFoundException 找不到用户
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("自定义认证逻辑已执行...");
        /* 查询用户 */
        UserLoginDto userLoginDto = userRestFeign.login(username);

        if (userLoginDto == null) {
            throw new UsernameNotFoundException(UserErrorCode.A0201.getErrorDesc());
        } else {
            return new SecurityUser(userLoginDto);
        }
    }
}
