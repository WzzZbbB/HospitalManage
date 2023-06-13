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
    private String accessToken;
    private Integer expiresIn;
    private String jti;
}
