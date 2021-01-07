package com.ylwq.scaffold.common.exception;

/**
 * 自定义异常基类
 *
 * @Author thymi
 * @Date 2021/1/7
 */
public abstract class BaseException extends RuntimeException implements ErrorCode {
    public BaseException() {
        super();
    }

    public BaseException(String message) {
        super(message);
    }

    public BaseException(Throwable cause) {
        super(cause);
    }

    public BaseException(String message, Throwable cause) {
        super(message, cause);
    }
}
