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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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

    private static CustomAuthentication customAuthentication;

    public static void setCustomAuthentication(CustomAuthentication customAuthentication) {
        CustomJsonLoginAuthenticationConverter.customAuthentication = customAuthentication;
    }

    /**
     * 自定义 JSON 登录身份验证转换器
     * @Author Mr WzzZ
     * @Date 2023/6/16
     * @return java.util.function.Function<org.springframework.web.server.ServerWebExchange,reactor.core.publisher.Mono<org.springframework.security.core.Authentication>>
     */
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
                        Map<String,Object> map = mapper.readValue(body.asInputStream(), Map.class);
                        return Mono.just(setAuthenticationType(request,map));
                    } catch (IOException e) {
                        return Mono.error(new LoginException("登录错误"));
                    }
                });
    }

    /**
     * 判断请求是否符合要求
     * @Author Mr WzzZ
     * @Date 2023/6/16
     * @Param Request请求
     * @return boolean
     */
    private static boolean checkInit(ServerHttpRequest request) {
        return Objects.requireNonNull(request.getHeaders().getFirst("Content-Type")).equalsIgnoreCase(MediaType.APPLICATION_JSON_VALUE);
    }


    /**
     * 通过Request中的请求头，判断所属的登录类型，并构造出Authentication对象
     * @Author Mr WzzZ
     * @Date 2023/6/16
     * @Param Request请求、Map参数
     * @return org.springframework.security.authentication.UsernamePasswordAuthenticationToken
     */
    private static UsernamePasswordAuthenticationToken setAuthenticationType(ServerHttpRequest request, Map<String, Object> map){
        String authenType = request.getHeaders().getFirst("Authentication-Type");
        switch (Objects.requireNonNull(authenType)) {
            case "TP" -> setCustomAuthentication(new TelephoneAuthentication(redisTemplate));
            default -> setCustomAuthentication(new UserNamePwdAuthentication(redisTemplate));
        };
        return customAuthentication.createAuthentication(map);
    }
}
