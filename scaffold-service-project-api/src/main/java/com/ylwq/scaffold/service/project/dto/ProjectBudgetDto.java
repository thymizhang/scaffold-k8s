package com.ylwq.scaffold.service.project.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 项目预算清单DTO
 *
 * @Author thymi
 * @Date 2021/1/7
 */
@Data
@ApiModel(value = "ProjectBudgetDto", description = "项目预算清单DTO")
public class ProjectBudgetDto {

    /**
     * 清单id
     */
    @ApiModelProperty(value = "清单id", required = true)
    Long id;

    /**
     * 上级清单id
     */
    @ApiModelProperty("上级清单id")
    Long parentId;

    /**
     * 清单名称
     */
    @ApiModelProperty("清单名称")
    String name;

    /**
     * 清单特征描述
     */
    @ApiModelProperty("清单特征描述")
    String spec;

    /**
     * 清单单位
     */
    @ApiModelProperty("清单单位")
    String unit;

    /**
     * 备注
     */
    @ApiModelProperty("备注")
    String remark;
}
