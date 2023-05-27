package com.hnkjxy.service;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

/**
 * @version: java version 1.8
 * @Author: Mr Orange
 * @description:
 * @date: 2023-05-27 14:07
 */
public interface CustomAuthentication {
    UsernamePasswordAuthenticationToken createAuthentication(String item);
}
