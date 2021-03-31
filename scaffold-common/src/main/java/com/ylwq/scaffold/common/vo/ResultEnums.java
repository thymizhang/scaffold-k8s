package com.ylwq.scaffold.common.vo;

import org.springframework.http.HttpStatus;

/**
 * 数据返回信息枚举<br/>
 * 状态码规则：<br/>
 * 采用HttpStatus状态码：200（OK,请求成功）、400（BAD_REQUEST，请求失败，ErrorCode:A类）、500（INTERNAL_SERVER_ERROR，系统异常，ErrorCode:B类C类）<br/>
 *
 * @Author thymi
 * @Date 2021/1/7
 */
public enum ResultEnums {
    /**
     * 状态枚举
     */
    /* 200 */
    SUCCESS(String.valueOf(HttpStatus.OK.value()), "请求成功"),
    /* 400 */
    FAILD(String.valueOf(HttpStatus.BAD_REQUEST.value()), "请求失败"),
    /* 401 */
    UNAUTHORIZED(String.valueOf(HttpStatus.UNAUTHORIZED.value()),"认证失败"),
    /* 403 */
    FORBIDDEN(String.valueOf(HttpStatus.FORBIDDEN.value()),"访问未授权"),
    /* 429 */
    TOO_MANY_REQUESTS(String.valueOf(HttpStatus.TOO_MANY_REQUESTS.value()),"服务器繁忙"),
    /* 500 */
    ERROR(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), "系统异常");

    /**
     * 状态码
     */
    private String code;

    /**
     * 消息
     */
    private String message;

    ResultEnums(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
