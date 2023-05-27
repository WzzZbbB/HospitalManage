package com.hnkjxy.data;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @version: java version 1.8
 * @Author: Mr WzzZ
 * @description: Redis逻辑过期时间
 * @date: 2023-04-30 20:34
 */
@Data
public class RedisData {
    private LocalDateTime expireTime;
    private Object data;

    public LocalDateTime getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(LocalDateTime expireTime) {
        this.expireTime = expireTime;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
