package com.hnkjxy.filter;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.hnkjxy.component.TokenComponent;
import com.hnkjxy.entity.User;
import com.hnkjxy.service.MyUserDetailService;
import com.hnkjxy.utils.RedisUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpHeaders;
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

import static com.hnkjxy.constant.RedisConstant.USER_TOKEN;

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

    private final MyUserDetailService userDetailService;


    public JwtTokenAuthenticationFilter(TokenComponent tokenComponent, MyUserDetailService userDetailService) {
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
                String username = authentication.getName();
                ServerHttpRequest mutbaleReq = request.mutate().header("X-USER", username).build();
                ServerWebExchange mutableExchange = exchange.mutate().request(mutbaleReq).build();

                return chain.filter(mutableExchange).subscribeOn(Schedulers.boundedElastic())
                        .contextWrite(ReactiveSecurityContextHolder.withAuthentication(authentication))
                        .doOnCancel(() -> log.info("取消"))
                        .doOnError((error) -> log.error("错误",error.getMessage()))
                        .doOnSuccess(aVoid -> log.info("Success"))
                        .log()
                        .doOnTerminate(() -> log.info("执行完成"));
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
