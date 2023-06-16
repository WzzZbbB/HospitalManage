package com.hnkjxy.utils;

import com.alibaba.nacos.api.utils.StringUtils;
import com.hnkjxy.data.ResponseCode;
import com.hnkjxy.data.ResponseData;
import com.hnkjxy.exception.MyCustomException;

/**
 * @version: java version 17
 * @Author: Mr WzzZ
 * @description:
 * @date: 2023-06-17 1:10
 */
public final class ParamUtils {
    private final static ResponseData<Exception> RESPONSE_DATA = new ResponseData<>();
    /**
     * 字符串类型参数非空判断
     * param 要判断的字符串字段
     * msg 若为空要提示的文字
     */
    public static String nullString(String param, ResponseCode responseCode, String msg){
        if(StringUtils.isBlank(param)){
            RESPONSE_DATA.setCode(responseCode.getCode());
            RESPONSE_DATA.setMessage(StringUtils.isBlank(msg) ? responseCode.getMessage():msg);
            throw new MyCustomException(RESPONSE_DATA);
        }
        return param;
    }

    /**
     * 任意类型参数非空判断
     * param 要判断的任意字段
     * o 想返回的参数类型
     * msg 若为空要提示的文字
     */
    public static <T> T nullParam(Object param,Class<T> o, ResponseCode responseCode,String msg){
        RESPONSE_DATA.setCode(responseCode.getCode());
        RESPONSE_DATA.setMessage(StringUtils.isBlank(msg) ? responseCode.getMessage():msg);
        if(param == null){
            throw new MyCustomException(RESPONSE_DATA);
        }
        if(param instanceof String){
            if(StringUtils.isBlank(String.valueOf(param))){
                throw new MyCustomException(RESPONSE_DATA);
            }
        }
        return (T) param;
    }
}
