package com.hnkjxy.handler;

import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.hnkjxy.component.TokenComponent;
import com.hnkjxy.entity.AuthInfo;
import com.hnkjxy.entity.Menu;
import com.hnkjxy.utils.JsonUtils;
import com.nimbusds.jose.shaded.json.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.WebFilterChainServerAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.hnkjxy.constant.JwtConstant.*;
import static com.hnkjxy.constant.RedisConstant.USER_ROLE;

/**
 * @version: java version 17
 * @Author: Mr WzzZ
 * @description: 认证成功处理
 * @date: 2023-04-29 21:06
 */
@Component
@Slf4j
public class AuthenticationSuccessHandel extends WebFilterChainServerAuthenticationSuccessHandler {
    private final TokenComponent tokenComponent;

    private final StringRedisTemplate redisTemplate;

    @Autowired
    public AuthenticationSuccessHandel(TokenComponent tokenComponent, StringRedisTemplate redisTemplate) {
        this.tokenComponent = tokenComponent;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public Mono<Void> onAuthenticationSuccess(WebFilterExchange webFilterExchange, Authentication authentication) {
        ServerWebExchange exchange = webFilterExchange.getExchange();
        ServerHttpResponse response = exchange.getResponse();
        //设置handers
        HttpHeaders headers = response.getHeaders();
        headers.add("Content-Type", MediaType.APPLICATION_JSON_UTF8_VALUE);
        headers.add("Cache-Control","no-store,no-cache,must-revalidate,max-age-8");
        ObjectMapper objectMapper = new ObjectMapper();
        byte[] dataBytes;

        //获取User对象
        User user = (User) authentication.getPrincipal();

        //将UserName存入Token
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(TOKEN_USER_INFO_USERNAME,user.getUsername());
        String[] uris = AuthorityUtils.authorityListToSet(user.getAuthorities())
                .toArray(String[]::new);
        jsonObject.put(TOKEN_USER_INFO,uris);
        Map<String,String> tokenMap = tokenComponent.encode(jsonObject);

        //取出Token和TokenId
        String token = tokenMap.get("token");
        String jti = tokenMap.get("jti");

        //将当前生成的Token以JTI为键存入Redis
        redisTemplate.opsForValue().set(TOKEN+jti,token,7L, TimeUnit.DAYS);

        AuthInfo authInfo = AuthInfo.builder()
                .accessToken(token)
                .jti(jti).build();
        dataBytes = JsonUtils.deserializer(authInfo).getBytes();

        DataBuffer wrap = response.bufferFactory().wrap(dataBytes);
        return response.writeWith(Mono.just(wrap));
    }
}
