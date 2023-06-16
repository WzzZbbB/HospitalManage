package com.hnkjxy.handler;

import com.hnkjxy.utils.ResponseUtil;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.ServerAuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import static com.hnkjxy.data.ErrorResponseCode.UNAUTHORIZED_ERROR;

/**
 * @version: java version 17
 * @Author: Mr WzzZ
 * @description: 认证失败处理
 * @date: 2023-04-30 14:32
 */
@Component
public class AuthenticationFailHandel implements ServerAuthenticationFailureHandler {
    @Override
    public Mono<Void> onAuthenticationFailure(WebFilterExchange webFilterExchange, AuthenticationException exception) {
        ServerWebExchange exchange = webFilterExchange.getExchange();
        ServerHttpResponse response = exchange.getResponse();
        return ResponseUtil.response(response, UNAUTHORIZED_ERROR);
    }

}
