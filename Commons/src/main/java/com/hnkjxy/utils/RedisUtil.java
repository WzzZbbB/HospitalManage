package com.hnkjxy.utils;

import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.hnkjxy.data.RedisData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import static com.hnkjxy.constant.RedisConstant.DOUBLE_KEY;
import static com.hnkjxy.constant.RedisConstant.LOCK_KEY;

/**
 * @version: java version 1.8
 * @Author: Mr Orange
 * @description:
 * @date: 2023-04-30 19:58
 */
@Component
public final class RedisUtil {
    @Autowired
    private StringRedisTemplate redisTemplate;

    //线程池
    private static final ThreadPoolExecutor CACHE_REBUILD_EXECUTOR =
            new ThreadPoolExecutor
                    (1,10,30,TimeUnit.SECONDS,new LinkedBlockingDeque<>(10));


    /**
     * 不带过期时间的存储
     * @author Mr WzzZ
     * @date 2023/4/30
     * @param key 存储的key
     * @param value 存储的value
     * @return void
     */
    public void set(String key, Object value) {
        redisTemplate.opsForValue().set(key, JSONUtil.toJsonStr(value));
    }

    /**
     * 带过期时间的存储
     * @author Mr OY
     * @date 2023/4/30
     * @param key 存储的key
     * @param value 存储的value
     * @param time 过期时间
     * @param timeUnit 时间单位
     * @return void
     */
    public void set(String key,Object value,Long time,TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(key,JSONUtil.toJsonStr(value),time, timeUnit);
    }

    /**
     * 功能描述 设置逻辑过期时间
     * @author Mr OY
     * @date 2023/4/22
     * @param key key
     * @param value value
     * @param time 逻辑过期时间
     * @param timeUnit 逻辑过期时间单位
     * @return void
     */
    public void setLogicalExpire(String key,Object value,Long time,TimeUnit timeUnit) {
        RedisData redisData = new RedisData();
        redisData.setData(value);
        redisData.setExpireTime(LocalDateTime.now().plusSeconds(timeUnit.toSeconds(time)));
        //写入Redis
        this.set(key,value);
    }

    /**
     * 功能描述 存入两个KEY一个会过期一个不会过期(仅用于重要数据，避免不必要的内存消耗)
     * @author Mr OY
     * @date 2023/4/22
     * @param key key
     * @param value value
     * @param time 设置过期时间
     * @param timeUnit 时间单位
     * @return void
     */
    public void setDoubleKey (String key,Object value,Long time,TimeUnit timeUnit) {
        this.set(key,value,time,timeUnit);
        this.set(DOUBLE_KEY+key,value);
    }

    /**
     * 功能描述 通过Key查询出并直接返回对象
     * @author Mr OY
     * @date 2023/4/22
     * @param key
     * @param tClass
     * @return T
     */
    public <T> T getObject(String key,Class<T> tClass) {
        String Json = redisTemplate.opsForValue().get(key);
        return JSONUtil.toBean(Json,tClass);
    }


    /**
     * 功能描述 通过key查询并返回该Json字符串
     * @author Mr OY
     * @date 2023/4/22
     * @param key
     * @return java.lang.String
     */
    public String getString(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    /**
     * 功能描述
     * @author Mr OY 缓存空对象解决缓存穿透问题
     * @date 2023/4/22
     * @param keyPrefix key前缀
     * @param id key后缀
     * @param type 泛型
     * @param dbFallback 泛型方法
     * @param time 过期时间设置（非逻辑过期）
     * @param unit 时间单位
     * @return R 缓存数据
     */
    public <R,ID> R queryWithPassThrough
    (String keyPrefix, ID id, Class<R> type, Function<ID,R> dbFallback, Long time, TimeUnit unit) {
        String key = keyPrefix+id;
        //从Redis查询缓存数据
        String json = getString(key);
        //判断数据是否存在
        if(StringUtils.isNotBlank(json)) {
            return JSONUtil.toBean(json,type);
        }
        //数据不存在 判断是否为空
        if (json != null) {
            //返回错误信息
            return null;
        }

        //为空 根据ID查询数据库
        R r = dbFallback.apply(id);
        //判断查询是否存在
        if (r == null) {
            //不存在 将空值写入Redis
            set(key,"",time,unit);
            //返回错误信息
            return null;
        }
        //存在 将数据写入redis
        set(key,r,time,unit);
        return r;
    }

    /**
     * 功能描述
     * @author Mr OY 逻辑过期解决短时间内大量key过期的问题（缓存雪崩、缓存击穿）
     * @date 2023/4/22
     * @param keyPrefix key前缀
     * @param id key后缀
     * @param type 泛型
     * @param dbFallback 泛型方法
     * @param time 设置新的逻辑过期时间
     * @param unit 时间单位
     * @return R 查询到的缓存数据
     */
    public <R,ID> R logicalExpiration
    (String keyPrefix,ID id,Class<R> type,Function<ID,R> dbFallback,Long time,TimeUnit unit){
        String key = keyPrefix+id;
        String json = getString(key);
        if (StrUtil.isNotBlank(json)) {
            //存在 直接返回
            return JSONUtil.toBean(json,type);
        }
        //不为NULL
        R r = null;
        RedisData redisData = JSONUtil.toBean(json, RedisData.class);
        r = JSONUtil.toBean((JSONObject) redisData.getData(), type);
        LocalDateTime expireTime = redisData.getExpireTime();

        //判断是否过期
        if(expireTime.isAfter(LocalDateTime.now())) {
            // 未过期，直接返回店铺信息
            return r;
        }

        //过期了，获取锁
        String lockKey = LOCK_KEY+keyPrefix+id;
        if (!tryLock(lockKey)) {
            //未获取到锁 直接返回旧数据
            return r;
        }
        //获取到锁
        CACHE_REBUILD_EXECUTOR.submit(() -> {
            try {
                R newR = dbFallback.apply(id);
                //重建缓存
                this.setLogicalExpire(key,newR,time,unit);
            }catch (Exception e){
                throw new RuntimeException(e);
            }finally {
                unLock(lockKey);
            }
        });
        //返回过期数据
        return r;
    }

    /**
     * 功能描述 通过双KEY解决短时间内大量KEY过期的问题
     * @author Mr OY
     * @date 2023/4/22
     * @param keyPrefix
     * @param id
     * @param type
     * @param dbFallback
     * @param time
     * @param unit
     * @return R
     */
    public <R,ID> R getDoubleKey (String keyPrefix,ID id,Class<R> type,Function<ID,R> dbFallback,Long time,TimeUnit unit) {
        String key = keyPrefix+id;
        String json = getString(key);
        if (StrUtil.isNotBlank(json)) {
            //命中缓存直接返回
            return JSONUtil.toBean(json,type);
        }
        //未命中
        if (json != null) {
            //返回错误信息
            return null;
        }
        String doubleKey = getString(DOUBLE_KEY + key);
        if (StrUtil.isBlank(doubleKey)) {
            //返回错误信息 并且开启异步更新缓存
            CACHE_REBUILD_EXECUTOR.submit(() -> {
                R r = dbFallback.apply(id);
                if (r != null) {
                    setDoubleKey(key,r,time,unit);
                }
            });
            //返回错误信息
            return null;
        }
        //返回旧key 并且开启异步更新缓存
        CACHE_REBUILD_EXECUTOR.submit(() -> {
            R r = dbFallback.apply(id);
            if (r != null) {
                setDoubleKey(key,r,time,unit);
            }
        });
        //返回旧key信息
        return JSONUtil.toBean(doubleKey,type);
    }

    /**
     * 功能描述 获取锁
     * @author Mr OY
     * @date 2023/4/22
     * @param key
     * @return boolean
     */
    private boolean tryLock(String key) {
        Boolean flag = redisTemplate.opsForValue().setIfAbsent(key,"1",10,TimeUnit.SECONDS);
        return BooleanUtil.isTrue(flag);
    }

    /**
     * 功能描述 释放锁
     * @author Mr OY
     * @date 2023/4/22
     * @param key
     * @return void
     */
    private void unLock(String key) {
        redisTemplate.delete(key);
    }
}
