package com.hnkjxy.controller;

import com.hnkjxy.data.Code;
import com.hnkjxy.data.ResponseCode;
import com.hnkjxy.data.ResponseData;
import com.hnkjxy.utils.VerificationCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static com.hnkjxy.constant.RedisConstant.VERIFY_IMG_CODE;

/**
 * @version: java version 1.8
 * @Author: Mr Orange
 * @description:
 * @date: 2023-04-29 21:21
 */
@RestController
public class GetVerificationCode {
    @Autowired
    private StringRedisTemplate redisTemplate;

    @GetMapping("/getVerifyCode")
    public ResponseData<Code> getVerifyCode() {
        try {
            Code code = VerificationCode.getVerificationCode();
            redisTemplate.opsForValue().set(VERIFY_IMG_CODE+code.getUuid(),code.getX().toString(),300L, TimeUnit.SECONDS);
            ResponseData<Code> response = new ResponseData<>();
            response.setData(code);
            response.setCode(200);
            return response;
        } catch (IOException e) {
            return new ResponseData<>(500,"验证码获取失败");
        }
    }

    @GetMapping("/hello")
    public String hello() {
       return "hello";
    }
}
