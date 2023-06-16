package com.hnkjxy.data;

import lombok.NonNull;

import java.io.Serializable;

/**
 * @version: java version 1.8
 * @Author: Mr Orange
 * @description:
 * @date: 2023-04-30 14:26
 */
public class ResponseData<T> implements Serializable {
    /**
     * 返回码
     */
    private Integer code;

    /**
     * 返回描述
     */
    private String message;

    private T data;

    public ResponseData() {
    }

    public ResponseData(@NonNull ResponseCode responseCode) {
        this(responseCode.getCode(), responseCode.getMessage());
    }

    public ResponseData(@NonNull ResponseCode responseCode, T data) {
        this(responseCode.getCode(), responseCode.getMessage(), data);
    }

    public ResponseData(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public ResponseData(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

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

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public static <T> ResponseData<T> success() {
        return new ResponseData<>(SuccessResponseCode.SUCCESS);
    }

    public static <T> ResponseData<T> success(SuccessResponseCode successResponseCode, T data) {
        return new ResponseData<>(successResponseCode,data);
    }

    public static <T> ResponseData<T> success(Integer code,String message,T data){
        return new ResponseData<>(code,message,data);
    }

    public static <T> ResponseData<T> failure(ErrorResponseCode responseCode) {
        ResponseData<T> response = new ResponseData<>(responseCode);
        response.setData(null);
        return response;
    }

    public static <T> ResponseData<T> failure(Integer code,String message,T data){
        return new ResponseData<>(code,message,data);
    }
}
