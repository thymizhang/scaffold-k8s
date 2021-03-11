package com.ylwq.scaffold.service.user.dto;

import lombok.Data;

/**
 * 用户登录DTO<br/>
 * 配合server-security安全校验使用
 *
 * @Author thymi
 * @Date 2021/3/9
 */
@Data
public class UserLoginDto {

    /**
     * 用户id
     */
    Long id;

    /**
     * 用户名
     */
    String userName;

    /**
     * 登录密码
     */
    String password;
}
