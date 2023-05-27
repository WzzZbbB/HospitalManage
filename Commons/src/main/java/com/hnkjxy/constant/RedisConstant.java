package com.hnkjxy.constant;

/**
 * @version: java version 1.8
 * @Author: Mr Orange
 * @description:
 * @date: 2023-04-30 20:36
 */
public interface RedisConstant {
    /**
     * 双KEY策略前缀
     */
    String DOUBLE_KEY = "double:";

    /**
     * 锁前缀
     */
    String LOCK_KEY = "lock:";

    /**
     * 用户TOKEN前缀
     */
    String USER_TOKEN = "user:token:";

    /**
     * 用户权限前缀
     */
    String USER_ROLE = "user:role:";

    /**
     * 图片验证码前缀
     */
    String VERIFY_IMG_CODE = "verify:img:code:";

    /**
     * 手机验证码前缀
     */
    String VERIFY_PHONE_CODE = "verify:img:code:";
}
