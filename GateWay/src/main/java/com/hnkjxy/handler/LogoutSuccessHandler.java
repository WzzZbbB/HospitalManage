package com.hnkjxy.handler;

import com.hnkjxy.data.SuccessResponseCode;
import com.hnkjxy.utils.ResponseUtil;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.logout.ServerLogoutSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @version: java version 17
 * @Author: Mr WzzZ
 * @description: 退出成功处理
 * @date: 2023-05-01 18:20
 */
@Component
public class LogoutSuccessHandler implements ServerLogoutSuccessHandler {
    @Override
    public Mono<Void> onLogoutSuccess(WebFilterExchange exchange, Authentication authentication) {
        ServerWebExchange serverWebExchange = exchange.getExchange();
        ServerHttpResponse response = serverWebExchange.getResponse();
        return ResponseUtil.response(response, SuccessResponseCode.SUCCESS);
    }
}
