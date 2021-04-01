package com.ylwq.scaffold.service.user.handler;

import com.ylwq.scaffold.common.exception.ClientException;
import com.ylwq.scaffold.common.exception.SystemException;
import com.ylwq.scaffold.common.exception.UserException;
import com.ylwq.scaffold.common.util.ResponseDataUtil;
import com.ylwq.scaffold.common.vo.ResponseData;
import com.ylwq.scaffold.common.vo.ResultEnums;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 全局异常处理类，每个微服务都需要配置
 * <p>
 * OpenApi会解析@RestControllerAdvice注解的类，在response中会显示这些异常定义：400/401/403/500
 *
 * @Author thymi
 * @Date 2021/3/31
 */
@RestControllerAdvice
public class GlobalControllerExceptionHandler {

    @ExceptionHandler(UserException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseData handleUserException(UserException e) {
        e.printStackTrace();
        return ResponseDataUtil.buildFaild(e.getErrorDesc());
    }

    @ExceptionHandler(SystemException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseData handleSystemException(SystemException e) {
        e.printStackTrace();
        return ResponseDataUtil.buildFaild(e.getErrorDesc());
    }

    @ExceptionHandler(ClientException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseData handleClientException(ClientException e) {
        e.printStackTrace();
        return ResponseDataUtil.buildFaild(e.getErrorDesc());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseData handleException(Exception e) {
        e.printStackTrace();
        return ResponseDataUtil.buildError(ResultEnums.ERROR.getMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseData handleAccessDeniedException(AccessDeniedException e) {
        return ResponseDataUtil.buildForbidden();
    }

    /**
     * 参数校验异常处理1：处理form data方式调用接口校验失败抛出的异常
     *
     * @param e BindException
     * @return 参数校验信息
     */
    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseData bindExceptionHandler(BindException e) {
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        /* 如果同一个参数有多个校验结果，只输出一个校验结果 */
        Map<String, String> collect = fieldErrors.stream()
                .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage, (key1, key2) -> key2));
        return ResponseDataUtil.buildFaild(collect);
    }

    /**
     * 参数校验异常处理2：处理json请求体调用接口校验失败抛出的异常
     *
     * @param e MethodArgumentNotValidException
     * @return 错误信息
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseData methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        Map<String, String> collect = e.getBindingResult().getFieldErrors().stream().collect(
                Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage, (key1, key2) -> key2));
        return ResponseDataUtil.buildFaild(collect);
    }

    /**
     * 参数校验异常处理3：处理单个参数校验失败抛出的异常
     *
     * @param e ConstraintViolationException
     * @return 错误信息
     */
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseData constraintViolationExceptionHandler(ConstraintViolationException e) {
        Set<ConstraintViolation<?>> constraintViolations = e.getConstraintViolations();
        List<String> collect = constraintViolations.stream()
                .map(o -> o.getMessage())
                .collect(Collectors.toList());
        return ResponseDataUtil.buildFaild(collect);
    }
}
