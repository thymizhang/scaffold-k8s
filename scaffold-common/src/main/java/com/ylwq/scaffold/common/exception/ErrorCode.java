package com.ylwq.scaffold.common.exception;

/**
 * 错误码接口
 *
 * @Author thymi
 * @Date 2021/1/7
 */
public interface ErrorCode {
    /**
     * 获取错误码
     *
     * @return 错误码
     */
    String getErrorCode();

    /**
     * 获取错误码信息
     *
     * @return 错误码
     */
    String getErrorDesc();
}
