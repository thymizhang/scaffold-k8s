package com.ylwq.scaffold.server.security.controller;

import com.ylwq.scaffold.common.util.ResponseDataUtil;
import com.ylwq.scaffold.common.vo.ResponseData;
import com.ylwq.scaffold.server.security.feign.UserRestFeign;
import com.ylwq.scaffold.service.user.dto.UserLoginDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    @GetMapping("/loginUser/{userName}")
    public ResponseData loginUser(@PathVariable("userName") String userName){
        UserLoginDto userLoginDto = userRestFeign.login(userName);
        return ResponseDataUtil.buildSuccess(userLoginDto);
    }

/*    @PostMapping("/login")
    public String login(){
        return "redirect:main.html";
    }*/

    @PostMapping("/main")
    public String main(){
        return "redirect:main.html";
    }
}
