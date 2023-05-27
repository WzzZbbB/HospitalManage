package com.hnkjxy.service.impl;

import cn.hutool.json.JSONUtil;
import com.hnkjxy.service.CustomAuthentication;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.util.Map;

import static com.hnkjxy.constant.RedisConstant.VERIFY_PHONE_CODE;

/**
 * @version: java version 17
 * @Author: Mr WzzZ
 * @description: 自定义手机号登录认证
 * @date: 2023-05-27 14:33
 */
public class TelephoneAuthentication implements CustomAuthentication {
    private final StringRedisTemplate redisTemplate;

    public TelephoneAuthentication(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }
    @Override
    public UsernamePasswordAuthenticationToken createAuthentication(String item) {
        Map<String,String> map = JSONUtil.toBean(item, Map.class);
        String telephone = map.get("phone");
        String code = map.get("code");
        String rightCode = redisTemplate.opsForValue().get(VERIFY_PHONE_CODE + telephone);
        if (!code.equals(rightCode)) {
            return null;
        }
        return UsernamePasswordAuthenticationToken.unauthenticated(telephone,"asdvgask%##%^asdjva");
    }
}
