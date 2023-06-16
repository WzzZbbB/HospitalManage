package com.hnkjxy.utils;

import com.hnkjxy.data.ResponseCode;
import com.hnkjxy.data.SuccessResponseCode;
import com.hnkjxy.data.ResponseData;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import reactor.core.publisher.Mono;

/**
 * @version: java version 17
 * @Author: Mr WzzZ
 * @description:
 * @date: 2023-06-13 15:28
 */
public class ResponseUtil {
    public static Mono<Void> response(ServerHttpResponse response, ResponseCode code) {
        //设置headers
        HttpHeaders headers = response.getHeaders();
        headers.add("Content-Type", MediaType.APPLICATION_JSON_UTF8_VALUE);

        //设置body
        ResponseData<Object> result = new ResponseData<>(code);
        DataBuffer wrap = response.bufferFactory().wrap(JsonUtils.deserializer(result).getBytes());
        return response.writeWith(Mono.just(wrap));
    }
}
