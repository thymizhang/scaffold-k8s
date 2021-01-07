package com.ylwq.scaffold.service.company.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 公司信息实体类
 *
 * @Author thymi
 * @Date 2021/1/7
 */
@Data
@TableName("company_info")
public class CompanyInfo {
    /**
     * 公司id
     */
    @TableId(type = IdType.AUTO)
    Long id;

    /**
     * 公司名称
     */
    String name;

    /**
     * 公司成员数
     */
    String userCount;

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
