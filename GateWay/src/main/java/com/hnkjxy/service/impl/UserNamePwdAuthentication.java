package com.hnkjxy.service.impl;

import cn.hutool.json.JSONUtil;
import com.hnkjxy.service.CustomAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Map;

import static com.hnkjxy.constant.RedisConstant.VERIFY_IMG_CODE;

/**
 * @version: java version 1.8
 * @Author: Mr Orange
 * @description:
 * @date: 2023-05-27 14:08
 */
public class UserNamePwdAuthentication implements CustomAuthentication {

    private final StringRedisTemplate redisTemplate;

    public UserNamePwdAuthentication(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public UsernamePasswordAuthenticationToken createAuthentication(String item) {
        Map<String,Object> map = JSONUtil.toBean(item, Map.class);
        String username = map.get("username").toString();
        String password = map.get("password").toString();
        String uuid = map.get("uuid").toString();
        String code = map.get("code").toString();
        String rightCode = redisTemplate.opsForValue().get(VERIFY_IMG_CODE + uuid);
        if (null == uuid || null == code || !code.equals(rightCode)) {
            return UsernamePasswordAuthenticationToken.unauthenticated(uuid, password);
        }
        return UsernamePasswordAuthenticationToken.unauthenticated(username, password);
    }
}
