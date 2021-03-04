package com.ylwq.scaffold.server.gateway;

import com.ylwq.scaffold.common.util.JvmUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 网关服务器
 *
 * @Author thymi
 * @Date 2021/1/13
 */
@SpringBootApplication
public class GatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
        /* 启动查看虚拟机信息 */
        JvmUtil.getMemoryStatus();
    }
}
