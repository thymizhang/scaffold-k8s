package com.ylwq.scaffold.service.project.entity;

import lombok.Data;

/**
 * 项目预算清单实体类
 *
 * @Author thymi
 * @Date 2021/1/7
 */
@Data
public class ProjectBudget {
    /**
     * 清单id，由sharding-jdbc雪花算法生成，数据库不要配置自增长，详见配置文件
     */
    Long id;

    /**
     * 公司id
     */
    Long companyId;

    /**
     * 项目id
     */
    Long projectId;

    /**
     * 上级清单id
     */
    Long parentId;

    /**
     * 清单名称
     */
    String name;

    /**
     * 清单特征描述
     */
    String spec;

    /**
     * 清单单位
     */
    String unit;

    /**
     * 备注
     */
    String remark;
}
