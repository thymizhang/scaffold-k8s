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
 * 自定义Security用户认证逻辑
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
     * 从业务数据库中找到user, 并与UserDetails进行对接
     *
     * @param userName 用户名
     * @return org.springframework.security.core.userdetails.User
     * @throws UsernameNotFoundException 找不到用户
     */
    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        log.info("执行自定义登录");

        /* 查询用户 */
        UserLoginDto userLoginDto = userRestFeign.login(userName);

        if (userLoginDto == null) {
            throw new UsernameNotFoundException(UserErrorCode.A0201.getErrorDesc());
        }

        if (userLoginDto != null) {
            // todo: 这里获取权限
            /* 注意这里返回的是org.springframework.security.core.userdetails.User */
            return new User(userName, userLoginDto.getPassword(), AuthorityUtils.createAuthorityList("admin,normal"));
        } else {
            /* 如果查不到用户, 返回null, 由provider来抛出异常 */
            return null;
        }
    }
}
