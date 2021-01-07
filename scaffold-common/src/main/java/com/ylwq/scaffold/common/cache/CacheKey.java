package com.ylwq.scaffold.common.cache;

/**
 * 缓存Key接口
 *
 * @Author thymi
 * @Date 2021/1/7
 */
public interface CacheKey {

    /**
     * 获取完整缓存Key
     *
     * @param cacheEnum 缓存枚举，指定缓存key前缀和key失效时间
     * @param postfix   缓存后缀
     * @return 缓存Key
     */
    public String getCacheKey(CacheEnum cacheEnum, String postfix);
}
