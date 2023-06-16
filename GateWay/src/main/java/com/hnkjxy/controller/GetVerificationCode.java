package com.hnkjxy.controller;

import com.hnkjxy.data.VerifyImgCode;
import com.hnkjxy.data.ResponseData;
import com.hnkjxy.service.MenuService;
import com.hnkjxy.utils.VerificationCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static com.hnkjxy.constant.RedisConstant.VERIFY_IMG_CODE;

/**
 * @version: java version 17
 * @Author: Mr Orange
 * @description:
 * @date: 2023-04-29 21:21
 */
@RestController
@RequiredArgsConstructor
public class GetVerificationCode {

    private final StringRedisTemplate redisTemplate;
    private final MenuService menuService;

    /**
     * 获取图片验证码
     * @Author Mr WzzZ
     * @Date 2023/6/15
     * @return com.hnkjxy.data.ResponseData<com.hnkjxy.data.VerifyImgCode>
     */
    @GetMapping("/getVerifyCode")
    public ResponseData<VerifyImgCode> getVerifyCode() {
        try {
            VerifyImgCode verifyImgCode = VerificationCode.getVerificationCode();
            redisTemplate.opsForValue().set(VERIFY_IMG_CODE+ verifyImgCode.getUuid(), verifyImgCode.getX().toString(),300L, TimeUnit.SECONDS);
            ResponseData<VerifyImgCode> response = new ResponseData<>();
            response.setData(verifyImgCode);
            response.setCode(200);
            return response;
        } catch (IOException e) {
            return new ResponseData<>(3000,"验证码获取失败");
        }
    }

    @GetMapping("/hello")
    public String hello() {
       return "hello";
    }
}
