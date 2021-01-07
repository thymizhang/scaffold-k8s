package com.ylwq.scaffold.service.project.entity;

import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

/**
 * 项目信息实体类
 *
 * @Author thymi
 * @Date 2021/1/7
 */
@Data
public class ProjectInfo {
    /**
     * 项目id，由sharding-jdbc雪花算法生成，数据库不要配置自增长，详见配置文件
     */
    Long id;

    /**
     * 公司id
     */
    Long companyId;

    /**
     * 项目名称
     */
    String name;

    /**
     * 项目成员数
     */
    Integer userCount;

    /**
     * 创建者id
     */
    Long creatorId;

    /**
     * 逻辑删除
     */
    @TableLogic
    String isDeleted;

    /**
     * 创建时间
     */
    String createTime;

    /**
     * 更新时间
     */
    String updateTime;
}
