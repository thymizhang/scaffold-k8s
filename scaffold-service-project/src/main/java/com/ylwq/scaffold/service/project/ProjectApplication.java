package com.ylwq.scaffold.service.project;

import com.ylwq.scaffold.common.util.JvmUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 项目服务
 *
 * @Author thymi
 * @Date 2021/1/7
 */
@SpringBootApplication
@EnableFeignClients
public class ProjectApplication {
    public static void main(String[] args) {
        SpringApplication.run(ProjectApplication.class, args);
        /* 启动查看虚拟机信息 */
        JvmUtil.getMemoryStatus();
    }
}
