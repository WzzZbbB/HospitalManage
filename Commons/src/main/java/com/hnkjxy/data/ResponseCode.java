package com.hnkjxy.data;

/**
 * @version: java version 17
 * @Author: Mr WzzZ
 * @description:
 * @date: 2023-06-16 17:27
 */
public interface ResponseCode {
    /**
     * 获取状态码
     * @Author Mr WzzZ
     * @Date 2023/6/16
     */
    Integer getCode();

    /**
     * 获取信息
     * @Author Mr WzzZ
     * @Date 2023/6/16
     */
    String getMessage();

}
