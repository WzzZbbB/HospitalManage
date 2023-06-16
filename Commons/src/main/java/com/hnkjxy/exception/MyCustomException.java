package com.hnkjxy.exception;

import com.hnkjxy.data.ResponseData;

/**
 * @version: java version 17
 * @Author: Mr WzzZ
 * @description:
 * @date: 2023-06-17 1:12
 */
public class MyCustomException extends RuntimeException{
    private ResponseData<Exception> responseData;
    public MyCustomException(ResponseData<Exception> responseData) {
        this.responseData = responseData;
    }

    public ResponseData<Exception> getResponseData() {
        return responseData;
    }

    public void setResponseData(ResponseData<Exception> responseData) {
        this.responseData = responseData;
    }
}
