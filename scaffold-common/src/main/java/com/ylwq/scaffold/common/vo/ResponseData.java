package com.ylwq.scaffold.common.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 显示层统一返回对象，使用了枚举{@link ResultEnums ResultEnums}
 *
 * @Author thymi
 * @Date 2021/1/7
 */
@Data
@NoArgsConstructor
@Schema(name = "ResponseData", description = "统一返回对象")
public class ResponseData<T> implements Serializable {

    /**
     * 状态码
     */
    @Schema(description = "状态码", required = true)
    private String code;

    /**
     * 消息
     */
    @Schema(description = "消息")
    private String message;

    /**
     * 数据对象
     */
    @Schema(description = "数据对象")
    private T data;


    public ResponseData(String code, String msg, T data) {
        this.code = code;
        this.message = msg;
        this.data = data;
    }

    public ResponseData(String code, String msg) {
        this.code = code;
        this.message = msg;
    }

    public ResponseData(ResultEnums resultEnums) {
        this.code = resultEnums.getCode();
        this.message = resultEnums.getMessage();
    }

    public ResponseData(ResultEnums resultEnums, String msg) {
        this.code = resultEnums.getCode();
        this.message = msg;
    }

    public ResponseData(ResultEnums resultEnums, T data) {
        this.code = resultEnums.getCode();
        this.message = resultEnums.getMessage();
        this.data = data;
    }

}
