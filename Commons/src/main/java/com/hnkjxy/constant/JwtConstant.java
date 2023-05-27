package com.hnkjxy.constant;

/**
 * @version: java version 1.8
 * @Author: Mr Orange
 * @description:
 * @date: 2023-04-30 14:06
 */
public interface JwtConstant {
    String TOKEN = "Authorization";
    String TOKEN_PREFIX="Bearer ";
    String TOKEN_USER_INFO = "user_info";
    String TOKEN_USER_INFO_USERNAME = "username";
    String HEADER_USER = "X-USER";
    String HEADER_ORG = "X-ORG";

    /**
     * 重置密码邮件验证码缓存前缀
     */
    String REST_PASSWORD_EMAIL_CODE_CACHE_PREFIX = "PWD_REST_CODE_";
    /**
     * 绑定邮箱验证码缓存前缀
     */
    String BIND_EMAIL_CHECK_CODE_PREFIX = "BIND_EMAIL_CODE_";
}
