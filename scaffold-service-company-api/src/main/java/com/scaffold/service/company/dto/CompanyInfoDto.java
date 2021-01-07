package com.scaffold.service.company.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 公司信息DTO
 *
 * @Author thymi
 * @Date 2021/1/7
 */
@Data
@Schema(description = "公司信息DTO")
public class CompanyInfoDto {

    /**
     * 公司id
     */
    Long id;

    /**
     * 公司名称
     */
    String name;

    /**
     * 公司成员数量
     */
    String userCount;

    /**
     * 创建者id
     */
    Long creatorId;

    /**
     * 公司成员
     */
    List users;
}
