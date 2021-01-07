package com.ylwq.scaffold.service.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 服务套餐DTO
 *
 * @Author thymi
 * @Date 2021/1/7
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ComboDto {

    String id;

    /**
     * 套餐成员数量
     */
    String memberSize;

    /**
     * 套餐项目数量
     */
    String projectSize;

    /**
     * 套餐数据空间
     */
    String dataSize;
}
