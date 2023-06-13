package com.hnkjxy.data;

/**
 * @version: java version 1.8
 * @Author: Mr Orange
 * @description:
 * @date: 2023-04-30 14:25
 */
public enum ResponseCode {
    /**
     * 成功
     */
    SUCCESS(200, "成功."),
    /**
     * 失败
     */
    ERROR(500, "失败."),
    /**
     * 参数错误
     */
    PARA_ERROR(1002, "参数错误."),
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
    NO_PERMISSION(5000, "权限不足"),

    /**
     * 认证失败
     */
    UNAUTHORIZED_ERROR(4000, "认证失败");


    ResponseCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    private Integer code;

    private String message;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
