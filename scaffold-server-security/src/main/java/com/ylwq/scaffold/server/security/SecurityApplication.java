package com.ylwq.scaffold.server.security;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 认证授权服务器
 *
 * @Author thymi
 * @Date 2021/3/8
 */
@SpringBootApplication
@EnableFeignClients
public class SecurityApplication {
    public static void main(String[] args) {
        SpringApplication.run(SecurityApplication.class, args);
    }
}
