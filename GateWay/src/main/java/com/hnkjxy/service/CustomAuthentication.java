package com.hnkjxy.service;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.util.Map;

/**
 * @version: java version 17
 * @Author: Mr WzzZ
 * @description: 自定义登陆方式
 * @date: 2023-05-27 14:07
 */
public interface CustomAuthentication {
    /**
     * 构建Authentication对象
     * @Author Mr WzzZ
     * @Date 2023/6/16
     * @Param 请求体
     * @return org.springframework.security.authentication.UsernamePasswordAuthenticationToken
     */
    UsernamePasswordAuthenticationToken createAuthentication(Map<String,Object> map);
}
