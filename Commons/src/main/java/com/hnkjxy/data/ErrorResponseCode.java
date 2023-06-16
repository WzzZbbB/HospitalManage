package com.hnkjxy.data;

import com.hnkjxy.data.ResponseCode;

/**
 * @version: java version 1.8
 * @Author: Mr WzzZ
 * @description: 错误返回状态码和信息
 * @date: 2023-04-30 14:28
 */
public enum ErrorResponseCode implements ResponseCode {
    /**
     * 系统内部错误
     */
    SYSTEM_INTERNAL_ERROR(5000, "系统内部错误！"),
    /**
     * 无效参数
     */
    INVALID_PARAMETER(5100, "无效参数！"),
    /**
     * 服务不存在
     */
    SERVICE_NOT_FOUND(5200, "Service Not Found！"),
    /**
     * 参数过长
     */
    PARAMETER_MAX_LENGTH(5400, "参数超出指定范围！"),
    /**
     * 参数过短
     */
    PARAMETER_MIN_LENGTH(5500, "参数过短！"),
    /**
     * 参数出错
     */
    PARAMETER_ANNOTATION_NOT_MATCH(5600, "参数错误！"),
    /**
     * 参数验证失败
     */
    PARAMETER_NOT_MATCH_RULE(5700, "参数验证失败！"),
    /**
     * 请求方法出错
     */
    METHOD_NOT_SUPPORTED(5800, "请求方法出错！"),
    /**
     * 不支持的content类型
     */
    CONTENT_TYPE_NOT_SUPPORT(5900, "类型错误！"),
    /**
     * json格式化出错
     */
    JSON_FORMAT_ERROR(6000, "格式化错误！"),
    /**
     * 远程调用出错
     */
    CALL_REMOTE_ERROR(6100, "远程调用错误！"),
    /**
     * 服务运行SQLException异常
     */
    SQL_EXCEPTION(6200, "数据库异常！"),
    /**
     * 客户端异常 给调用者 app,移动端调用
     */
    CLIENT_EXCEPTION(6300, "客户端异常！"),
    /**
     * 服务端异常, 微服务服务端产生的异常
     */
    SERVER_EXCEPTION(6400, "服务异常！"),
    /**
     * 授权失败 禁止访问
     */
    ACCESS_DENIED(6500, "禁止访问！"),

    /**
     * 失败
     */
    ERROR(500, "失败！"),
    /**
     * 接口调用失败
     */
    FACADE_ERROR(2000, "接口调用失败"),
    /**
     * 业务处理失败
     */
    BUSINESS_ERROR(3000, "业务处理失败"),

    /**
     * 认证失败
     */
    NO_PERMISSION(4300, "权限不足"),

    /**
     * 认证失败
     */
    UNAUTHORIZED_ERROR(4000, "认证失败");

    private Integer errorCode;
    private String errorMessage;

    ErrorResponseCode(Integer errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    @Override
    public Integer getCode() {
        return errorCode;
    }

    @Override
    public String getMessage() {
        return errorMessage;
    }



}
