package com.ylwq.scaffold.service.company;

import com.ylwq.scaffold.common.util.JvmUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 公司服务
 *
 * @Author thymi
 * @Date 2021/1/7
 */
@SpringBootApplication
@EnableFeignClients
@EnableScheduling
public class CompanyApplication {
    public static void main(String[] args) {
        SpringApplication.run(CompanyApplication.class, args);
        /* 启动查看虚拟机信息 */
        JvmUtil.getMemoryStatus();
    }
}
