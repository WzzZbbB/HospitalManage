package com.hnkjxy.authorization;

import cn.hutool.json.JSONUtil;
import com.alibaba.nacos.api.remote.response.ResponseCode;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.ReactiveAuthorizationManager;
import org.springframework.security.config.annotation.web.configurers.UrlAuthorizationConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.server.authorization.AuthorizationContext;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.file.AccessDeniedException;
import java.util.Collection;

/**
 * @version: java version 17
 * @Author: Mr WzzZ
 * @description: 自定义权限鉴定
 * @date: 2023-04-29 14:53
 */
public class MyAuthorizationManager implements ReactiveAuthorizationManager<AuthorizationContext>{
    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Override
    public Mono<AuthorizationDecision> check(Mono<Authentication> authentication, AuthorizationContext object) {
        return authentication.map(auth -> {
            ServerWebExchange exchange = object.getExchange();
            ServerHttpRequest request = exchange.getRequest();
            Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
            for (GrantedAuthority authority : authorities) {
                String authorityAuthority = authority.getAuthority();
                String path = request.getURI().getPath();
                if (antPathMatcher.match(authorityAuthority,path)) {
                    return new AuthorizationDecision(true);
                }
            }
            return new AuthorizationDecision(false);
        }).defaultIfEmpty(new AuthorizationDecision(false));
    }

    @Override
    public Mono<Void> verify(Mono<Authentication> authentication, AuthorizationContext object) {
        return check(authentication,object)
                .filter(AuthorizationDecision::isGranted)
                .switchIfEmpty(Mono.defer(() -> {
                    String body = JSONUtil.toJsonStr(ResponseCode.FAIL);
                    return Mono.error(new AccessDeniedException(body));
                })).flatMap(d -> Mono.empty());
    }
}
