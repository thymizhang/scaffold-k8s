package com.ylwq.scaffold.service.user;

import com.ylwq.scaffold.common.util.JvmUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 用户服务
 *
 * @Author thymi
 * @Date 2021/1/7
 */
@SpringBootApplication
public class UserApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserApplication.class, args);
        /* 启动查看虚拟机信息 */
        JvmUtil.getMemoryStatus();
    }
}
