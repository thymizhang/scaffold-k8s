package com.ylwq.scaffold.server.security.domain;

import com.ylwq.scaffold.service.user.dto.UserLoginDto;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Oauth2安全用户对象，用于JWT令牌扩展用户信息
 * 使用步骤：
 * 1. 在UserDetailsServiceImpl中转换用户服务返回的信息；
 * 2. 在JwtTokenEnhancer中，增加扩展的用户信息，本项目扩展了用户id信息
 *
 * @Author thymi
 * @Date 2021/3/31
 */
@Data
public class SecurityUser implements UserDetails {

    /**
     * 用户ID
     */
    private Long id;
    /**
     * 用户名
     */
    private String username;
    /**
     * 用户密码
     */
    private String password;

    /**
     * 权限数据
     */
    private Collection<SimpleGrantedAuthority> authorities;

    public SecurityUser(UserLoginDto userLoginDto) {
        this.id = userLoginDto.getId();
        this.username = userLoginDto.getUserName();
        this.password = userLoginDto.getPassword();
        if(userLoginDto.getPermission() != null || userLoginDto.getPermission().trim() != "") {
            this.authorities = Stream.of(userLoginDto.getPermission().split(",")).map(SimpleGrantedAuthority::new).collect(Collectors.toList());
        }
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
