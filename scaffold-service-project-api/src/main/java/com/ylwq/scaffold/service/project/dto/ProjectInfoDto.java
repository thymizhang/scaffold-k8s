package com.ylwq.scaffold.service.project.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 项目信息DTO
 *
 * @Author thymi
 * @Date 2021/1/7
 */
@Data
@Schema(description = "项目信息DTO")
public class ProjectInfoDto {
    /**
     * 项目id
     */
    @Schema(description = "项目id", required = true)
    Long id;

    /**
     * 公司id
     */
    @Schema(description = "公司id")
    Long companyId;

    /**
     * 项目名称
     */
    @Schema(description = "项目名称")
    String name;

    /**
     * 项目成员数
     */
    @Schema(description = "项目成员数")
    Integer userCount;
}
