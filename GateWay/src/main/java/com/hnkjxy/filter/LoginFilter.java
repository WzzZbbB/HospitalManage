package com.hnkjxy.filter;

import com.hnkjxy.component.TokenComponent;
import com.hnkjxy.handel.AuthenticationFailHandel;
import com.hnkjxy.handel.SuccessLoginHandel;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.ReactiveAuthenticationManagerResolver;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.security.web.server.authentication.ServerAuthenticationFailureHandler;
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import javax.security.auth.login.LoginException;

/**
 * @version: java version 1.8
 * @Author: Mr Orange
 * @description:
 * @date: 2023-04-29 17:39
 */
public class LoginFilter extends AuthenticationWebFilter {

    private static final String METHOD = "post";
    private ServerAuthenticationSuccessHandler authenticationSuccessHandler = new SuccessLoginHandel(new TokenComponent());
    private ServerAuthenticationFailureHandler authenticationFailureHandler = new AuthenticationFailHandel();


    public LoginFilter(ReactiveAuthenticationManagerResolver<ServerWebExchange> authenticationManagerResolver) {
        super(authenticationManagerResolver);
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        if (!request.getMethod().equals(METHOD)) {

        }
        return super.filter(exchange, chain);
    }

    @Override
    protected Mono<Void> onAuthenticationSuccess(Authentication authentication, WebFilterExchange webFilterExchange) {
        return super.onAuthenticationSuccess(authentication, webFilterExchange);
    }

    @Override
    public void setSecurityContextRepository(ServerSecurityContextRepository securityContextRepository) {
        super.setSecurityContextRepository(securityContextRepository);
    }

    @Override
    public void setAuthenticationSuccessHandler(ServerAuthenticationSuccessHandler authenticationSuccessHandler) {
        super.setAuthenticationSuccessHandler(this.authenticationSuccessHandler);
    }

    @Override
    public void setServerAuthenticationConverter(ServerAuthenticationConverter authenticationConverter) {
        super.setServerAuthenticationConverter(authenticationConverter);
    }

    @Override
    public void setAuthenticationFailureHandler(ServerAuthenticationFailureHandler authenticationFailureHandler) {
        super.setAuthenticationFailureHandler(this.authenticationFailureHandler);
    }

    @Override
    public void setRequiresAuthenticationMatcher(ServerWebExchangeMatcher requiresAuthenticationMatcher) {
        super.setRequiresAuthenticationMatcher(requiresAuthenticationMatcher);
    }

/*    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        // 只允许POST请求
        if (!METHOD.equals(request.getMethod())) {
            throw new LoginException("只接受POST类型");
        }
        if (!request.getContentType().equalsIgnoreCase(MediaType.APPLICATION_JSON_VALUE)){
            throw new LoginException("请传入正确的数据格式");
        }

        try {
            Map<String,String> map = new ObjectMapper().readValue(request.getInputStream(), Map.class);
            String username = map.get(getUsernameParameter());
            String password = map.get(getPasswordParameter());
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username,password);
            return this.getAuthenticationManager().authenticate(token);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }*/
}
