package com.hnkjxy.handel;

import cn.hutool.json.JSONUtil;
import com.hnkjxy.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.authentication.*;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;

/**
 * @version: java version 1.8
 * @Author: Mr Orange
 * @description:
 * @date: 2023-04-29 17:39
 */
@Slf4j
public class CustomServerFormLoginAuthenticationConverter extends ServerFormLoginAuthenticationConverter {

    @Override
    public Mono<Authentication> convert(ServerWebExchange exchange) {
        ServerHttpRequest request = exchange.getRequest();
        if (!request.getMethod().matches("POST")){
            return Mono.error(new Throwable("请求类型错误！"));
        }
        if (!request.getHeaders().getFirst("Content-Type").equalsIgnoreCase(MediaType.APPLICATION_JSON_VALUE)){
            return Mono.error(new Throwable("请求参数错误！"));
        }
        StringBuffer paramBuffer = new StringBuffer();
        Flux<DataBuffer> body = request.getBody();
        body.subscribe(buffer -> {
            CharBuffer charBuffer = StandardCharsets.UTF_8.decode(buffer.asByteBuffer());
            log.info("request::charBuffer:{}",charBuffer);
            paramBuffer.append(charBuffer);
        });
        String param = paramBuffer.toString();
        return Mono.just(createAuthentication(param));
    }

    private UsernamePasswordAuthenticationToken createAuthentication(String item) {
        User user = JSONUtil.toBean(item, User.class);
        String username = user.getUsername();
        String password = user.getPassword();
        System.out.println(username);
        System.out.println(password);
        return UsernamePasswordAuthenticationToken.unauthenticated(username, password);
    }
}
