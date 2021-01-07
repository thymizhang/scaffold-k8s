package com.ylwq.scaffold.common.cache;

import lombok.Getter;

/**
 * 缓存枚举
 *
 * @Author thymi
 * @Date 2021/1/7
 */
public enum CacheEnum {

    /* 默认30天有效期 */
    DEFAULT("service", 2592000),
    /* 公司项目 */
    COMPANY_PROJECTS("companyId", 60);

    /**
     * Key前缀
     */
    @Getter
    private String keyPrefix;

    /**
     * 缓存失效时间，单位：秒
     */
    @Getter
    private long expire;

    /**
     * 构造
     *
     * @param keyPrefix Key前缀
     * @param expire    缓存失效时间（单位：秒）
     */
    CacheEnum(String keyPrefix, long expire) {
        this.keyPrefix = keyPrefix;
        this.expire = expire;
    }
}
