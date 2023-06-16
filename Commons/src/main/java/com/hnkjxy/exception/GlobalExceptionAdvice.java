package com.hnkjxy.exception;

import com.hnkjxy.data.ResponseData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLException;
import static com.hnkjxy.data.ErrorResponseCode.*;

/**
 * @version: java version 17
 * @Author: Mr WzzZ
 * @description:
 * @date: 2023-06-16 15:15
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionAdvice {
    /**
     * 异常处理
     * @Author Mr WzzZ
     * @Date 2023/6/16
     */
    @ExceptionHandler(Exception.class)
    public ResponseData<Exception> exceptionHandler(Exception e) {
        log.error("GlobalExceptionHandler.exceptionHandler , 异常信息:",e);
        return ResponseData.failure(ERROR);
    }

    /**
     * SQL异常处理
     * @Author Mr WzzZ
     * @Date 2023/6/17
     */
    @ExceptionHandler(SQLException.class)
    public ResponseData<Exception> handleSqlException(SQLException e) {
        log.error("服务运行SQLException异常",e);
        return ResponseData.failure(SQL_EXCEPTION);
    }

    /**
     * 处理所有RequestBody注解参数验证异常
     * @Author Mr WzzZ
     * @Date 2023/6/16
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseData<Exception> handlerMethodArgValidException(MethodArgumentNotValidException e) {
        log.error(e.getMessage());
        return ResponseData.failure(PARAMETER_ANNOTATION_NOT_MATCH);
    }

    /**
     * 处理数据验证异常
     * @Author Mr WzzZ
     * @Date 2023/6/16
     */
    @ExceptionHandler(BindException.class)
    public ResponseData<Exception> handleBindException(BindException ex) {
        FieldError fieldError = ex.getBindingResult().getFieldError();
        log.error("必填校验异常:{}({})", fieldError.getDefaultMessage(),fieldError.getField());
        return ResponseData.failure(PARAMETER_NOT_MATCH_RULE);
    }

    /**
     * 自定义异常处理
     * @Author Mr WzzZ
     * @Date 2023/6/17
     */
    @ExceptionHandler(MyCustomException.class)
    public ResponseData<Exception> handleMyCustomException(MyCustomException e) {
        log.error("异常为：",e);
        return e.getResponseData();
    }
}
