package com.hnkjxy.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hnkjxy.authorization.MyAuthorizationManager;
import com.hnkjxy.component.TokenComponent;
import com.hnkjxy.filter.JwtTokenAuthenticationFilter;
import com.hnkjxy.handler.*;
import com.hnkjxy.service.impl.UserDetailServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.authentication.logout.LogoutWebFilter;
import org.springframework.security.web.server.authentication.logout.SecurityContextServerLogoutHandler;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.security.web.server.context.WebSessionServerSecurityContextRepository;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers;
import reactor.core.publisher.Mono;

/**
 * @version: java version 17
 * @Author: Mr WzzZ
 * @description: Security配置
 * @date: 2023-05-27 21:41
 */
@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {
    @Value("${wzzz.url.white}")
    private String[] excludeAuthPages;

    @Resource
    UserDetailServiceImpl userDetailServiceImpl;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private TokenComponent tokenComponent;

    /**
     * 定义密码加密规则
     * @author Mr WzzZ
     * @date 2023/5/28
     * @return org.springframework.security.crypto.password.PasswordEncoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    /**
     * 定义Security存储Bean
     * @author Mr WzzZ
     * @date 2023/5/28
     * @return org.springframework.security.web.server.context.ServerSecurityContextRepository
     */
    @Bean
    public ServerSecurityContextRepository securityContextRepository() {
        WebSessionServerSecurityContextRepository securityContextRepository =
                new WebSessionServerSecurityContextRepository();
        securityContextRepository.setSpringSecurityContextAttrName("wzzz-security-context");
        return securityContextRepository;
    }

    /**
     * 定义认证管理器，并指定UserDetailService
     * @author Mr OY
     * @date 2023/5/28
     * @return org.springframework.security.authentication.ReactiveAuthenticationManager
     */
    @Bean
    public ReactiveAuthenticationManager authenticationManager() {
        UserDetailsRepositoryReactiveAuthenticationManager authenticationManager =
                new UserDetailsRepositoryReactiveAuthenticationManager(userDetailServiceImpl);
        authenticationManager.setPasswordEncoder(passwordEncoder());
        return authenticationManager;
    }

    /**
     * Security主体配置
     * @author Mr WzzZ
     * @date 2023/5/28
     * @return org.springframework.security.web.server.SecurityWebFilterChain
     */
    @Bean
    public SecurityWebFilterChain filterChain (ServerHttpSecurity http) throws Exception {
        http.csrf().disable()
                .httpBasic().disable()
                .formLogin().disable() //关闭默认的form表单登录
                .logout().disable() //关闭默认的退出行为
                .securityContextRepository(securityContextRepository()) //指定SecurityContext
                //权限设定
                .authorizeExchange()
                //白名单放行
                .pathMatchers(excludeAuthPages).permitAll()
                //OPTIONS请求放行
                .pathMatchers(HttpMethod.OPTIONS).permitAll()
                //其他请求通过自定义鉴权
                .anyExchange().access(new MyAuthorizationManager())
                .and()
                .exceptionHandling()
                //定义未登录的返回
                .authenticationEntryPoint(new CustomHttpBasicServerAuthenticationEntryPoint())
                //定义鉴权失败
                .accessDeniedHandler((swe,e) -> {
                    swe.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
                    return swe.getResponse().writeWith(Mono.just(new DefaultDataBufferFactory().wrap("未获取到权限！".getBytes())));
                });
        //将自定义的拦截器添加到过滤器链
        //自定义认证拦截器
        http.addFilterAt(authenticationWebFilter(),SecurityWebFiltersOrder.AUTHENTICATION);
        //判断是否携带TOKEN拦截器
        http.addFilterAt(new JwtTokenAuthenticationFilter(tokenComponent, userDetailServiceImpl), SecurityWebFiltersOrder.HTTP_BASIC);
        //退出登录拦截器
        http.addFilterAt(logoutWebFilter(), SecurityWebFiltersOrder.LOGOUT);
        return http.build();
    }

    /**
     * 定义退出拦截器
     * @author Mr WzzZ
     * @date 2023/5/28
     * @return org.springframework.security.web.server.authentication.logout.LogoutWebFilter
     */
    private LogoutWebFilter logoutWebFilter() {
        LogoutWebFilter logoutWebFilter = new LogoutWebFilter();

        SecurityContextServerLogoutHandler logoutHandler = new SecurityContextServerLogoutHandler();
        logoutHandler.setSecurityContextRepository(securityContextRepository());

        logoutWebFilter.setLogoutSuccessHandler(new LogoutSuccessHandler());
        logoutWebFilter.setLogoutHandler(logoutHandler);
        logoutWebFilter.setRequiresLogoutMatcher(
                ServerWebExchangeMatchers.pathMatchers(HttpMethod.GET, "/logout")
        );

        return logoutWebFilter;
    }

    /**
     * 定义认证拦截器
     * @author Mr WzzZ
     * @date 2023/5/28
     * @return org.springframework.security.web.server.authentication.AuthenticationWebFilter
     */
    private AuthenticationWebFilter authenticationWebFilter() {
        CustomJsonLoginAuthenticationConverter.redisTemplate = redisTemplate;
        CustomJsonLoginAuthenticationConverter.mapper = mapper;
        AuthenticationWebFilter filter = new AuthenticationWebFilter(authenticationManager());
        filter.setSecurityContextRepository(securityContextRepository());
        filter.setAuthenticationConverter(CustomJsonLoginAuthenticationConverter.jsonBodyAuthenticationConverter());
        filter.setAuthenticationSuccessHandler(new AuthenticationSuccessHandel(tokenComponent));
        filter.setAuthenticationFailureHandler(new AuthenticationFailHandel());
        filter.setRequiresAuthenticationMatcher(
                ServerWebExchangeMatchers.pathMatchers(HttpMethod.POST, "/auth/login"));
        return filter;
    }

}
