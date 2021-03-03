package com.scaffold.service.company.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 公司信息DTO
 *
 * @Author thymi
 * @Date 2021/1/7
 */
@Data
@ApiModel(value = "CompanyInfoDto",description = "公司信息DTO")
public class CompanyInfoDto {

    /**
     * 公司id
     */
    @ApiModelProperty("公司id")
    Long id;

    /**
     * 公司名称
     */
    @ApiModelProperty("公司名称")
    String name;

    /**
     * 公司成员数量
     */
    @ApiModelProperty("公司成员数量")
    String userCount;

    /**
     * 创建者id
     */
    @ApiModelProperty("创建者id")
    Long creatorId;

    /**
     * 公司成员
     */
    @ApiModelProperty("公司成员")
    List users;
}
