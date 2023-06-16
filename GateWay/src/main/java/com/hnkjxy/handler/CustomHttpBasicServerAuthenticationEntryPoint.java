package com.hnkjxy.handler;

import com.hnkjxy.utils.ResponseUtil;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.authentication.HttpBasicServerAuthenticationEntryPoint;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import static com.hnkjxy.data.ErrorResponseCode.UNAUTHORIZED_ERROR;

/**
 * @version: java version 17
 * @Author: Mr WzzZ
 * @description: 自定义未认证访问受限URI
 * @date: 2023-04-29 21:55
 */

public class CustomHttpBasicServerAuthenticationEntryPoint extends HttpBasicServerAuthenticationEntryPoint {

    @Override
    public Mono<Void> commence(ServerWebExchange exchange, AuthenticationException ex) {
        ServerHttpResponse response = exchange.getResponse();
        return ResponseUtil.response(response, UNAUTHORIZED_ERROR);
    }

}
