package com.ylwq.scaffold.common.exception;

import lombok.Getter;

/**
 * A类异常：用户异常
 * <p>
 * 表示错误来源于用户，比如参数错误，用户安装版本过低，用户支付超时等问题
 *
 * @Author thymi
 * @Date 2021/1/7
 */
public class UserException extends BaseException {

    /**
     * 错误码
     */
    @Getter
    private String errorCode;

    /**
     * 错误码对应的外部描述信息
     */
    @Getter
    private String errorDesc;

    public UserException() {
        super();
    }

    /**
     * 用户异常构造
     *
     * @param errorCode 用户错误枚举{@link UserErrorCode UserErrorCode}
     */
    public UserException(UserErrorCode errorCode) {
        super(errorCode.toString());
        this.errorCode = errorCode.getErrorCode();
        this.errorDesc = errorCode.getErrorDesc();
    }

    public UserException(String message) {
        super(message);
    }

    public UserException(Throwable cause) {
        super(cause);
    }

    public UserException(String message, Throwable cause) {
        super(message, cause);
    }
}
