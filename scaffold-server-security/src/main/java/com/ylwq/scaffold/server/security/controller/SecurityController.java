package com.ylwq.scaffold.server.security.controller;

import com.ylwq.scaffold.server.security.feign.UserRestFeign;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author thymi
 * @Date 2021/3/9
 */
@RestController
public class SecurityController {

    final
    UserRestFeign userRestFeign;

    public SecurityController(UserRestFeign userRestFeign) {
        this.userRestFeign = userRestFeign;
    }

    @PostMapping("/toMain")
    public String main() {
        return "登录成功";
    }

    @PostMapping("/toError")
    public String error() {
        return "登录失败";
    }

    @Secured("ROLE_LEADER")
    @GetMapping("/test")
    public String test() {
        return "test";
    }

    @PreAuthorize("hasAuthority('admin')")
    @GetMapping("/pre")
    public String preAuthorize() {
        return "方法执行前判断权限@PreAuthorize";
    }
}
