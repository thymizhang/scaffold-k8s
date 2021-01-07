package com.ylwq.scaffold.service.project.cache;

import com.google.common.collect.Lists;
import com.ylwq.scaffold.common.cache.BaseCache;
import com.ylwq.scaffold.common.cache.CacheEnum;
import com.ylwq.scaffold.common.cache.CacheKey;
import com.ylwq.scaffold.common.util.BeansUtil;
import com.ylwq.scaffold.common.util.JsonUtil;
import com.ylwq.scaffold.common.util.RedisUtil;
import com.ylwq.scaffold.service.project.dto.ProjectInfoDto;
import com.ylwq.scaffold.service.project.entity.ProjectInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 项目服务缓存
 *
 * @Author thymi
 * @Date 2021/1/7
 */
@Component
@Slf4j
public class ProjectCache extends BaseCache implements CacheKey {

    final
    RedisUtil redisUtil;

    final
    RedisTemplate redisTemplate;

    public ProjectCache(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.redisUtil = new RedisUtil(redisTemplate);
    }

    /**
     * 缓存公司项目，缓存对象为DTO
     *
     * @param companyId    公司id
     * @param projectInfos 公司项目集合
     * @return boolean
     */
    public boolean pushCompanyProjects(String companyId, List<ProjectInfo> projectInfos) {
        try {
            String key = getCacheKey(CacheEnum.COMPANY_PROJECTS, companyId);
            List<ProjectInfoDto> companyProject = BeansUtil.copyList(projectInfos, ProjectInfoDto.class);
            this.redisUtil.set(key, JsonUtil.objectToJson(companyProject));
            this.redisUtil.expire(key, CacheEnum.COMPANY_PROJECTS.getExpire());
            log.info("向缓存添加或更新Key：" + key + " ，缓存时间:" + CacheEnum.COMPANY_PROJECTS.getExpire() + "秒。");
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 从缓存获取公司项目
     *
     * @param companyId 公司id
     * @return 公司项目集合，没有项目返回空集合
     */
    public List<ProjectInfoDto> pullCompanyProjects(String companyId) {
        String key = getCacheKey(CacheEnum.COMPANY_PROJECTS, companyId);
        Object object = this.redisUtil.get(key);
        if (object != null) {
            /* 有效期策略，自动延长key的有效期 */
            this.redisUtil.expire(key, CacheEnum.COMPANY_PROJECTS.getExpire());
            log.info("从缓存取出数据：" + object.toString());
            log.info("更新Key：" + key + "的缓存时间为" + CacheEnum.COMPANY_PROJECTS.getExpire() + "秒。");
            return (List<ProjectInfoDto>) JsonUtil.jsonToObject(object.toString(), List.class);
        }
        return Lists.newArrayList();
    }

    @Override
    public String getCacheKey(CacheEnum cacheEnum, String postfix) {
        String key = cacheEnum.getKeyPrefix() + ":" + postfix;
        return key;
    }
}
