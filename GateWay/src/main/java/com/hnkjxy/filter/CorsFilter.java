package com.hnkjxy.filter;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.cors.reactive.CorsUtils;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

/**
 * @version: java version 1.8
 * @Author: Mr Orange
 * @description:
 * @date: 2023-05-27 15:44
 */
@Configuration
public class CorsFilter implements WebFilter {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        if (CorsUtils.isCorsRequest(request)) {
            ServerHttpResponse response = exchange.getResponse();
            HttpHeaders headers = response.getHeaders();
            headers.set(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN,"*");
            headers.set(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS,"*");
            headers.set(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS,"*");
            headers.set(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS,"false");
            headers.set(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS,"*");
            headers.set(HttpHeaders.ACCESS_CONTROL_MAX_AGE,"3600");
            if (request.getMethod() == HttpMethod.OPTIONS) {
                response.setStatusCode(HttpStatus.OK);
                return Mono.empty();
            }
        }
        return chain.filter(exchange);
    }
}
