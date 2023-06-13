package com.hnkjxy.filter;

import cn.hutool.core.util.StrUtil;
import com.hnkjxy.component.TokenComponent;
import com.hnkjxy.data.ResponseCode;
import com.hnkjxy.service.impl.UserDetailServiceImpl;
import com.hnkjxy.utils.ResponseUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * @version: java version 17
 * @Author: Mr WzzZ
 * @description: 身份鉴定
 * @date: 2023-04-29 13:01
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenAuthenticationFilter implements WebFilter {
    private final TokenComponent tokenComponent;

    private final UserDetailServiceImpl userDetailService;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();

        String token = resolveToken(request);
        //判断token
        if (StrUtil.isBlank(token)) {
            return chain.filter(exchange);
        }

        //通过token获取authentication对象
        Authentication authentication = tokenComponent.getAuthentication(token);
        if (authentication == null) {
            return chain.filter(exchange);
        }
        //判断当前访问路径是否在权限中
        boolean success  = userDetailService.checkUserRoleResource(request.getURI().getPath(),authentication);
        if (!success) {
            return ResponseUtil.response(response,ResponseCode.NO_PERMISSION);
        }
        String userName = authentication.getName();
        ServerHttpRequest mutbaleReq = request.mutate().header("USER", URLEncoder.encode(userName, StandardCharsets.UTF_8)).build();
        ServerWebExchange mutableExchange = exchange.mutate().request(mutbaleReq).build();

        return chain.filter(mutableExchange).subscribeOn(Schedulers.boundedElastic())
                .contextWrite(ReactiveSecurityContextHolder.withAuthentication(authentication));
    }

    private String resolveToken (ServerHttpRequest request) {
        String token = request.getHeaders().getFirst("Authentication_Token");
        if (StrUtil.isNotBlank(token)) {
            return token;
        }
        return null;
    }
}
