package com.ylwq.scaffold.service.project.handler;

import com.ylwq.scaffold.common.exception.ClientException;
import com.ylwq.scaffold.common.exception.SystemException;
import com.ylwq.scaffold.common.exception.UserException;
import com.ylwq.scaffold.common.vo.ResponseData;
import com.ylwq.scaffold.common.vo.ResultEnums;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理类，每个微服务都需要配置
 * <p>
 * OpenApi会解析@RestControllerAdvice注解的类，在response中会显示这些异常定义：400/404/500
 *
 * @Author thymi
 * @Date 2021/1/7
 */
@RestControllerAdvice
public class GlobalControllerExceptionHandler {

    @ExceptionHandler(UserException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseData handleUserException(UserException e) {
        e.printStackTrace();
        return new ResponseData(ResultEnums.FAILD, e.getErrorDesc());
    }

    @ExceptionHandler(SystemException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseData handleSystemException(SystemException e) {
        e.printStackTrace();
        return new ResponseData(ResultEnums.FAILD, e.getErrorDesc());
    }

    @ExceptionHandler(ClientException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseData handleClientException(ClientException e) {
        e.printStackTrace();
        return new ResponseData(ResultEnums.FAILD, e.getErrorDesc());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseData handleException(Exception e) {
        e.printStackTrace();
        return new ResponseData(ResultEnums.ERROR, e.getMessage());
    }

}
