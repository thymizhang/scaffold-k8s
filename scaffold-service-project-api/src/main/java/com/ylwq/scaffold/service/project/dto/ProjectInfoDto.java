package com.ylwq.scaffold.service.project.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

/**
 * 项目信息DTO
 *
 * @Author thymi
 * @Date 2021/1/7
 */
@Data
@ApiModel(value = "ProjectInfoDto", description = "项目信息DTO")
public class ProjectInfoDto {
    /**
     * 项目id
     */
    @ApiModelProperty(value = "项目id", required = true)
    Long id;

    /**
     * 公司id
     */
    @ApiModelProperty(value = "公司id", required = true)
    Long companyId;

    /**
     * 项目名称
     */
    @ApiModelProperty("项目名称")
    @NotBlank
    String name;

    /**
     * 项目成员数
     */
    @ApiModelProperty("项目成员数")
    @Min(0)
    Integer userCount;
}
