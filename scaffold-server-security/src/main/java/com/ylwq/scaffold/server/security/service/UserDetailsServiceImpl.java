package com.ylwq.scaffold.server.security.service;


import com.ylwq.scaffold.common.exception.UserErrorCode;
import com.ylwq.scaffold.server.security.feign.UserRestFeign;
import com.ylwq.scaffold.service.user.dto.UserLoginDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * 自定义Security用户认证逻辑<br/>
 * 覆盖UserDetailsService.loadUserByUsername方法，改写用户认证逻辑
 *
 * @Author thymi
 * @Date 2021/3/9
 */
@Service
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRestFeign userRestFeign;

    private final PasswordEncoder passwordEncoder;

    public UserDetailsServiceImpl(UserRestFeign userRestFeign, PasswordEncoder passwordEncoder) {
        this.userRestFeign = userRestFeign;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * 从用户服务中获取密码和权限，交给spring-security去验证<br/>
     * 技术要点：<br/>
     * 1 请求类型必须是POST；<br/>
     * 2 用户名和密码参数默认是：username和password，可以在SecurityConfig中指定；<br/>
     * 参考：UsernamePasswordAuthenticationFilter<br/>
     *
     * @param username 用户名
     * @return org.springframework.security.core.userdetails.User
     * @throws UsernameNotFoundException 找不到用户
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("自定义登录逻辑已执行");

        /* 查询用户 */
        UserLoginDto userLoginDto = userRestFeign.login(username);

        if (userLoginDto == null) {
            throw new UsernameNotFoundException(UserErrorCode.A0201.getErrorDesc());
        } else {
            // todo: 这里获取权限
            /* 将密码和权限取出，交给spring-security去验证，注意这里返回的是org.springframework.security.core.userdetails.User */
            /* AuthorityUtils.createAuthorityList包含了角色和权限，”ROLE_“前缀是角色 */
            return new User(username, userLoginDto.getPassword(), AuthorityUtils.createAuthorityList("admin","normal","ROLE_LEADER"));
        }
    }
}
