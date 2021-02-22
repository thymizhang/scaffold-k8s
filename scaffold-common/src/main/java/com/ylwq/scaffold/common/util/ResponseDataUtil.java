package com.ylwq.scaffold.common.util;

import com.ylwq.scaffold.common.vo.ResponseData;
import com.ylwq.scaffold.common.vo.ResultEnums;

/**
 * 数据返回工具类
 * 对{@link ResponseData ResponseData}进行了封装
 *
 * @Author thymi
 * @Date 2021/1/7
 */
public class ResponseDataUtil {

    /**
     * 带实体的统一返回
     *
     * @param data 数据对象
     * @param <T>  数据类
     * @return {@link ResponseData ResponseData}
     */
    public static <T> ResponseData buildSuccess(T data) {
        return new ResponseData(ResultEnums.SUCCESS, data);
    }

    /**
     * 返回默认请求成功信息200
     *
     * @return {@link ResponseData ResponseData}
     */
    public static ResponseData buildSuccess() {
        return new ResponseData(ResultEnums.SUCCESS);
    }

    /**
     * 返回默认请求失败信息400
     *
     * @return {@link ResponseData ResponseData}
     */
    public static ResponseData buildFaild() {
        return new ResponseData(ResultEnums.FAILD);
    }

    /**
     * 返回请求失败信息400
     *
     * @param msg 自定义信息
     * @return {@link ResponseData ResponseData}
     */
    public static ResponseData buildFaild(String msg) {
        return new ResponseData(ResultEnums.FAILD, msg);
    }

    /**
     * 返回默认认证失败信息401
     *
     * @return {@link ResponseData ResponseData}
     */
    public static ResponseData buildUnAuthorized() {
        return new ResponseData(ResultEnums.UNAUTHORIZED);
    }

    /**
     * 返回服务器繁忙信息429
     *
     * @return {@link ResponseData ResponseData}
     */
    public static ResponseData buildTooManyRequests() {
        return new ResponseData(ResultEnums.TOO_MANY_REQUESTS);
    }

    /**
     * 返回默认系统异常信息500
     *
     * @return {@link ResponseData ResponseData}
     */
    public static ResponseData buildError() {
        return new ResponseData(ResultEnums.ERROR);
    }

    /**
     * 返回系统异常信息500
     *
     * @param msg 自定义信息
     * @return {@link ResponseData ResponseData}
     */
    public static ResponseData buildError(String msg) {
        return new ResponseData(ResultEnums.ERROR, msg);
    }
}
