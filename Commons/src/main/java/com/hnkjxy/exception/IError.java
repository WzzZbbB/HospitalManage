package com.hnkjxy.exception;

/**
 * @version: java version 1.8
 * @Author: Mr Orange
 * @description:
 * @date: 2023-04-30 14:28
 */
public interface IError {
    /**
     * 获取nameSpace
     *
     * @return nameSpace
     */
    String getNameSpace();

    /**
     * 获取错误码

     * @return 错误码
     */
    String getErrorCode();

    /**
     * 获取错误信息

     * @return 错误信息
     */
    String getErrorMessage();
}
