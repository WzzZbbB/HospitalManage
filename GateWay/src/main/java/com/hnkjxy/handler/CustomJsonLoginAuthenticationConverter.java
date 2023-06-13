package com.hnkjxy.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hnkjxy.exception.LoginException;
import com.hnkjxy.service.CustomAuthentication;
import com.hnkjxy.service.impl.TelephoneAuthentication;
import com.hnkjxy.service.impl.UserNamePwdAuthentication;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.core.Authentication;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

/**
 * @version: java version 17
 * @Author: Mr WzzZ
 * @description: 自定义Json登录认证
 * @date: 2023-04-29 17:39
 */
@Slf4j
public class CustomJsonLoginAuthenticationConverter {
    public static StringRedisTemplate redisTemplate = null;

    public static ObjectMapper mapper = new ObjectMapper();

    public static Function<ServerWebExchange, Mono<Authentication>> jsonBodyAuthenticationConverter() {
        return exchange -> exchange
                .getRequest()
                .getBody()
                .next()
                .flatMap(body -> {
                    try {
                        ServerHttpRequest request = exchange.getRequest();
                        if (!checkInit(request)) {
                            return Mono.error(new LoginException("登录错误"));
                        }
                        CustomAuthentication customAuthentication = setAuthenticationType(request);
                        Map<String,Object> map = mapper.readValue(body.asInputStream(), Map.class);
                        return Mono.just(customAuthentication.createAuthentication(map));
                    } catch (IOException e) {
                        return Mono.error(new LoginException("登录错误"));
                    }
                });
    }

    private static boolean checkInit(ServerHttpRequest request) {
        return Objects.requireNonNull(request.getHeaders().getFirst("Content-Type")).equalsIgnoreCase(MediaType.APPLICATION_JSON_VALUE);
    }

    private static CustomAuthentication setAuthenticationType(ServerHttpRequest request){
        String authenType = request.getHeaders().getFirst("Authentication-Type");
        return switch (Objects.requireNonNull(authenType)) {
            case "TP" -> new TelephoneAuthentication(redisTemplate);
            default -> new UserNamePwdAuthentication(redisTemplate);
        };
    }
}
