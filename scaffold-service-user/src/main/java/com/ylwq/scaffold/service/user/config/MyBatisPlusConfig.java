package com.ylwq.scaffold.service.user.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
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
public class MyBatisPlusConfig {

    /**
     * mybatis plus分页插件配置，3.4.0及以上版本<br/>
     * 参考：https://mybatis.plus/guide/interceptor.html#mybatisplusinterceptor
     *
     * @return MybatisPlusInterceptor
     */
    @Bean
    public MybatisPlusInterceptor paginationInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return interceptor;
    }

}
