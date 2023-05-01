package com.hnkjxy.entity;

import lombok.Builder;
import lombok.Data;

/**
 * @version: java version 1.8
 * @Author: Mr Orange
 * @description:
 * @date: 2023-04-30 14:20
 */
@Data
@Builder
public class AuthInfo {
    private String access_token;
    private User user_info;
    private Integer expires_in;
    private String jti;
}
