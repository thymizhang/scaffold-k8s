package com.ylwq.scaffold.server.security.controller;

import com.ylwq.scaffold.common.util.ResponseDataUtil;
import com.ylwq.scaffold.common.vo.ResponseData;
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

    /**
     * 获取Oauth2授权码
     *
     * @param code 授权码
     * @return
     */
    @GetMapping("/authorizeCode")
    public ResponseData<String> getAuthorizeCode(String code) {
        return ResponseDataUtil.buildSuccess(code);
    }

    @Secured("ROLE_LEADER")
    @GetMapping("/api/test")
    public String test() {
        return "test";
    }

    @PreAuthorize("hasAuthority('admin')")
    @GetMapping("/api/pre")
    public String preAuthorize() {
        return "方法执行前判断权限@PreAuthorize";
    }
}
