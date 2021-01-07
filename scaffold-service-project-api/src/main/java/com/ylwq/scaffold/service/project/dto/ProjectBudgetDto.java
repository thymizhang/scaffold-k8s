package com.ylwq.scaffold.service.project.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 项目预算清单DTO
 *
 * @Author thymi
 * @Date 2021/1/7
 */
@Data
@Schema(description = "项目预算清单DTO")
public class ProjectBudgetDto {

    /**
     * 清单id
     */
    @Schema(description = "清单id", required = true)
    Long id;

    /**
     * 上级清单id
     */
    @Schema(description = "上级清单id")
    Long parentId;

    /**
     * 清单名称
     */
    @Schema(description = "清单名称")
    String name;

    /**
     * 清单特征描述
     */
    @Schema(description = "清单特征描述")
    String spec;

    /**
     * 清单单位
     */
    @Schema(description = "清单单位")
    String unit;

    /**
     * 备注
     */
    @Schema(description = "备注")
    String remark;
}
