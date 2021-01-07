package com.ylwq.scaffold.common.exception;

import lombok.Getter;

/**
 * B类异常：系统异常
 * <p>
 * 表示错误来源于当前系统，往往是业务逻辑出错，或程序健壮性差等问题
 *
 * @Author thymi
 * @Date 2021/1/7
 */
public class SystemException extends BaseException {

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

    public SystemException() {
        super();
    }

    /**
     * 系统异常构造
     *
     * @param errorCode 系统错误枚举{@link SystemErrorCode SystemErrorCode}
     */
    public SystemException(SystemErrorCode errorCode) {
        super(errorCode.toString());
        this.errorCode = errorCode.getErrorCode();
        this.errorDesc = errorCode.getErrorDesc();
    }

    public SystemException(String message) {
        super(message);
    }

    public SystemException(Throwable cause) {
        super(cause);
    }

    public SystemException(String message, Throwable cause) {
        super(message, cause);
    }
}
