package com.ylwq.scaffold.service.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户信息DTO<br/>
 * 技术要点：DTO的属性类型需要与ENTITY的属性类型保持一致，方便信息复制。
 *
 * @Author thymi
 * @Date 2021/1/7
 */
@Data
@Schema(description = "用户信息DTO")
public class UserInfoDto {
    /**
     * 用户id
     */
    @Schema(description = "用户id", required = true)
    Long id;

    /**
     * 公司id
     */
    @Schema(description = "公司id")
    Long companyId;

    /**
     * 用户名
     */
    @Schema(description = "用户名")
    String userName;

    /**
     * 用户头像
     */
    @Schema(description = "用户头像")
    String imageHead;

    /**
     * 手机号码
     */
    @Schema(description = "手机号码")
    String phone;

}
