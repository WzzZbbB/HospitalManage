package com.hnkjxy.handel;

import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hnkjxy.component.TokenComponent;
import com.hnkjxy.data.ResponseCode;
import com.hnkjxy.data.ResponseData;
import com.hnkjxy.entity.AuthInfo;
import com.hnkjxy.utils.JsonUtils;
import com.hnkjxy.utils.RedisUtil;
import com.nimbusds.jose.shaded.json.JSONObject;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.WebFilterChainServerAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.hnkjxy.constant.JwtConstant.TOKEN_USER_INFO;
import static com.hnkjxy.constant.RedisConstant.USER_TOKEN;

/**
 * @version: java version 1.8
 * @Author: Mr Orange
 * @description:
 * @date: 2023-04-29 21:06
 */
@Component
@Slf4j
public class SuccessLoginHandel extends WebFilterChainServerAuthenticationSuccessHandler {
    private final TokenComponent tokenComponent;

    @Autowired
    private StringRedisTemplate redisTemplate;

    public SuccessLoginHandel(TokenComponent tokenComponent) {
        this.tokenComponent = tokenComponent;
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

        try {
            User user = (User) authentication.getPrincipal();
            com.hnkjxy.entity.User buildUser = buildUser(user);

            JSONObject jsonObject = new JSONObject();
            jsonObject.put(TOKEN_USER_INFO,buildUser);
            String token = tokenComponent.encode(jsonObject);

            buildUser.setRoles(buildUser.getRoles().stream().filter
                            (item -> item.toLowerCase().matches("^menu_.*")).collect(Collectors.toSet())
                    .stream().map(item -> item.substring(5)).collect(Collectors.toSet()));

            AuthInfo authInfo = AuthInfo.builder()
                    .access_token(token)
                    .jti(UUID.randomUUID().toString())
                    .expires_in(tokenComponent.getExpire())
                    .user_info(buildUser).build();
            dataBytes = objectMapper.writeValueAsBytes(authInfo);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            ResponseData<Object> res = new ResponseData<>(ResponseCode.UNAUTHORIZED_ERROR);
            dataBytes = JSONUtil.toJsonStr(res).getBytes();
        }
        DataBuffer wrap = response.bufferFactory().wrap(dataBytes);
        return response.writeWith(Mono.just(wrap));
    }

    private com.hnkjxy.entity.User buildUser(User user) {
        com.hnkjxy.entity.User authUser = new com.hnkjxy.entity.User();
        authUser.setUsername(user.getUsername());
        authUser.setEnabled(user.isEnabled());
        authUser.setAccountNonLocked(user.isAccountNonLocked());
        authUser.setAccountNonExpired(user.isAccountNonExpired());
        authUser.setCredentialsNonExpired(user.isCredentialsNonExpired());
        if (!CollectionUtils.isEmpty(user.getAuthorities())) {
            Set<String> collect = user.getAuthorities().stream().map(item -> item.getAuthority()).collect(Collectors.toSet());
            authUser.setRoles(collect);
        }
        return authUser;
    }

}
