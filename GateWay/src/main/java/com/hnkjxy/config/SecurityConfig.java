package com.hnkjxy.config;

import com.hnkjxy.authorization.MyAuthorizationManager;
import com.hnkjxy.component.TokenComponent;
import com.hnkjxy.filter.JwtTokenAuthenticationFilter;
import com.hnkjxy.handel.AuthenticationFailHandel;
import com.hnkjxy.handel.CustomHttpBasicServerAuthenticationEntryPoint;
import com.hnkjxy.handel.LogoutSuccessHandler;
import com.hnkjxy.handel.SuccessLoginHandel;
import com.hnkjxy.service.MyUserDetailService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;
import reactor.core.publisher.Mono;

/**
 * @version: java version 1.8
 * @Author: Mr Orange
 * @description:
 * @date: 2023-04-29 17:36
 */
@EnableWebFluxSecurity
@Configuration
public class SecurityConfig {
    @Value("${wzzz.url.white}")
    private String[] excludeAuthPages;

    @Resource
    MyUserDetailService myUserDetailService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public ReactiveAuthenticationManager reactiveAuthenticationManager(MyUserDetailService userDetailsService) {
        UserDetailsRepositoryReactiveAuthenticationManager authenticationManager = new UserDetailsRepositoryReactiveAuthenticationManager(userDetailsService);
        authenticationManager.setPasswordEncoder(passwordEncoder());
        return authenticationManager;
    }

    @Bean
    public SecurityWebFilterChain filterChain (ServerHttpSecurity http,
                                               ReactiveAuthenticationManager authenticationManager,
                                               SuccessLoginHandel successLoginHandel,
                                               AuthenticationFailHandel failHandel,
                                               TokenComponent tokenComponent,
                                               LogoutSuccessHandler logoutSuccessHandler
                                                ) throws Exception {
        http.csrf(ServerHttpSecurity.CsrfSpec::disable)
                .httpBasic(Customizer.withDefaults())
                .authenticationManager(authenticationManager)
                .exceptionHandling()
                //定义未登录的返回
                .authenticationEntryPoint(new CustomHttpBasicServerAuthenticationEntryPoint())
                .accessDeniedHandler((swe,e) -> {
                    swe.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
                    return swe.getResponse().writeWith(Mono.just(new DefaultDataBufferFactory().wrap("未获取到权限！".getBytes())));
                })
                .and()
                .securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
                .authorizeExchange()
                .pathMatchers(excludeAuthPages).permitAll()
                .pathMatchers(HttpMethod.OPTIONS).permitAll()
                .anyExchange().access(new MyAuthorizationManager())
                .and()
                .formLogin()
                .loginPage("/auth/login")
                .authenticationSuccessHandler(successLoginHandel)
                .authenticationFailureHandler(failHandel)
                .and()
                .logout()
                .logoutUrl("/auth/logout")
                .logoutSuccessHandler(logoutSuccessHandler);
        http.addFilterAt(new JwtTokenAuthenticationFilter(tokenComponent,myUserDetailService), SecurityWebFiltersOrder.HTTP_BASIC);
        return http.build();
    }
}
