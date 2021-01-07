package com.ylwq.scaffold.common.exception;

import lombok.Getter;

/**
 * C类异常，第三方异常
 * <p>
 * 表示错误来源于第三方服务，比如 CDN 服务出错，消息投递超时等问题
 *
 * @Author thymi
 * @Date 2021/1/7
 */
public class ClientException extends BaseException {

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

    /**
     * 第三方异常构造
     *
     * @param errorCode 系统错误枚举{@link ClientErrorCode ClientErrorCode}
     */
    public ClientException(ClientErrorCode errorCode) {
        super(errorCode.toString());
        this.errorCode = errorCode.getErrorCode();
        this.errorDesc = errorCode.getErrorDesc();
    }

    public ClientException(String errorCode, String errorMsg) {
        super(errorCode + " : " + errorMsg);
    }

    public ClientException(Throwable cause) {
        super(cause);
    }

    public ClientException(String errorCode, String errorMsg, Throwable cause) {
        super(errorCode + " : " + errorMsg, cause);
    }
}
