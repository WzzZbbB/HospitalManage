package com.hnkjxy.handel;

import cn.hutool.json.JSONUtil;
import com.hnkjxy.service.CustomAuthentication;
import com.hnkjxy.service.impl.TelephoneAuthentication;
import com.hnkjxy.service.impl.UserNamePwdAuthentication;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.authentication.*;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

/**
 * @version: java version 1.8
 * @Author: Mr Orange
 * @description:
 * @date: 2023-04-29 17:39
 */
@Slf4j
@Component
public class CustomServerFormLoginAuthenticationConverter extends ServerFormLoginAuthenticationConverter {
    @Autowired
    private final StringRedisTemplate redisTemplate;

    public CustomServerFormLoginAuthenticationConverter(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public Mono<Authentication> convert(ServerWebExchange exchange) {
        ServerHttpRequest request = exchange.getRequest();
        if (("/auth/login").equalsIgnoreCase(request.getURI().getPath())) {
            if (!request.getMethod().matches("POST")){
                return Mono.empty();
            }
            if (!request.getHeaders().getFirst("Content-Type").equalsIgnoreCase(MediaType.APPLICATION_JSON_VALUE)){
                return Mono.empty();
            }
        }else {
            return Mono.empty();
        }
        StringBuffer paramBuffer = new StringBuffer();
        Flux<DataBuffer> body = request.getBody();
        body.subscribe(buffer -> {
            CharBuffer charBuffer = StandardCharsets.UTF_8.decode(buffer.asByteBuffer());
            paramBuffer.append(charBuffer);
        });
        CustomAuthentication customAuthentication = new UserNamePwdAuthentication(redisTemplate);
        String param = paramBuffer.toString();
        if (("TP").equals(request.getHeaders().getFirst("Authentication-Type"))) {
            customAuthentication = new TelephoneAuthentication(redisTemplate);
        }
        return Mono.just(customAuthentication.createAuthentication(param));
    }
}
