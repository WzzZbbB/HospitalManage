package com.hnkjxy.filter;

import cn.hutool.core.util.StrUtil;
import com.hnkjxy.component.TokenComponent;
import com.hnkjxy.service.impl.UserDetailServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
public class JwtTokenAuthenticationFilter implements WebFilter {
    private final TokenComponent tokenComponent;

    private final UserDetailServiceImpl userDetailService;

    @Autowired
    public JwtTokenAuthenticationFilter(TokenComponent tokenComponent, UserDetailServiceImpl userDetailService) {
        this.tokenComponent = tokenComponent;
        this.userDetailService = userDetailService;
    }


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String token = resolveToken(request);
        //判断token
        if (StrUtil.isNotBlank(token)) {
            //通过token获取authentication对象
            Authentication authentication = tokenComponent.getAuthentication(token);
            if (authentication != null) {
                //判断当前访问路径是否在权限中
                boolean success = userDetailService.checkUserRoleResource(request.getURI().getPath(),authentication);
                if (!success) {
                    ServerHttpResponse response = exchange.getResponse();
                    response.setStatusCode(HttpStatus.FORBIDDEN);
                    return response.setComplete();
                }
                String user = authentication.getName();
                ServerHttpRequest mutbaleReq = request.mutate().header("USER", URLEncoder.encode(user, StandardCharsets.UTF_8)).build();
                ServerWebExchange mutableExchange = exchange.mutate().request(mutbaleReq).build();

                return chain.filter(mutableExchange).subscribeOn(Schedulers.boundedElastic())
                        .contextWrite(ReactiveSecurityContextHolder.withAuthentication(authentication));
            }
        }
        return chain.filter(exchange);
    }

    private String resolveToken (ServerHttpRequest request) {
        String token = request.getHeaders().getFirst("Authentication_Token");
        if (StrUtil.isNotBlank(token)) {
            return token;
        }
        return null;
    }
}
