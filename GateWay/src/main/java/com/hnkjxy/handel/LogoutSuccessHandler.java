package com.hnkjxy.handel;

import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.logout.ServerLogoutSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @version: java version 1.8
 * @Author: Mr Orange
 * @description:
 * @date: 2023-05-01 18:20
 */
@Component
public class LogoutSuccessHandler implements ServerLogoutSuccessHandler {
    @Override
    public Mono<Void> onLogoutSuccess(WebFilterExchange exchange, Authentication authentication) {
        ServerWebExchange serverWebExchange = exchange.getExchange();
        ServerHttpResponse response = serverWebExchange.getResponse();
        DataBuffer wrap = response.bufferFactory().wrap("success".getBytes());
        return response.writeWith(Mono.just(wrap));
    }
}
