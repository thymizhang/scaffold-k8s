package com.ylwq.scaffold.service.user.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 用户信息DTO<br/>
 * 技术要点：DTO的属性类型需要与ENTITY的属性类型保持一致，方便信息复制。
 *
 * @Author thymi
 * @Date 2021/1/7
 */
@Data
@ApiModel(value = "UserInfoDto", description = "用户信息DTO")
public class UserInfoDto {
    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户id", required = true)
    Long id;

    /**
     * 公司id
     */
    @ApiModelProperty("公司id")
    Long companyId;

    /**
     * 用户名
     */
    @ApiModelProperty("用户名")
    String userName;

    /**
     * 用户头像
     */
    @ApiModelProperty("用户头像")
    String imageHead;

    /**
     * 手机号码
     */
    @ApiModelProperty("手机号码")
    String phone;
}
