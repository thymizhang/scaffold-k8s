package com.ylwq.scaffold.common.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;

/**
 * MyBatisPlus配置基础类<br/>
 * 包括分页等插件，所有使用MyBatisPlus的微服务都需要继承该类。
 *
 * @Author thymi
 * @Date 2021/1/7
 */
public class MyBatisPlusBaseConfig {
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
