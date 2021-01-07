package com.ylwq.scaffold.service.user.config;

import com.ylwq.scaffold.common.config.MyBatisPlusBaseConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * MyBatisPlus配置类
 * <p>
 * 如果这里不配置@MapperScan，会找不到mapper，启动失败
 *
 * @Author thymi
 * @Date 2021/1/7
 */
@Configuration
@MapperScan("com.ylwq.scaffold.service.user.dao")
public class MyBatisPlusConfig extends MyBatisPlusBaseConfig {

}
