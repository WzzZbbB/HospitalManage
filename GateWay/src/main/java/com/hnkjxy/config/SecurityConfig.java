package com.hnkjxy.config;

import com.hnkjxy.authorization.MyAuthorizationManager;
import com.hnkjxy.component.TokenComponent;
import com.hnkjxy.filter.CorsFilter;
import com.hnkjxy.filter.JwtTokenAuthenticationFilter;
import com.hnkjxy.handel.CustomServerFormLoginAuthenticationConverter;
import com.hnkjxy.filter.ServerHttpReqFilter;
import com.hnkjxy.handel.AuthenticationFailHandel;
import com.hnkjxy.handel.CustomHttpBasicServerAuthenticationEntryPoint;
import com.hnkjxy.handel.LogoutSuccessHandler;
import com.hnkjxy.handel.SuccessLoginHandel;
import com.hnkjxy.service.MyUserDetailService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.server.WebFilter;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.Iterator;

/**
 * @version: java version 17
 * @Author: Mr WzzZ
 * @description: Security配置
 * @date: 2023-04-29 17:36
 */
@EnableWebFluxSecurity
@Configuration
public class SecurityConfig {
    @Value("${wzzz.url.white}")
    private String[] excludeAuthPages;

    @Resource
    MyUserDetailService myUserDetailService;

    @Autowired
    private ServerCodecConfigurer serverCodecConfigurer ;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private AuthenticationFailHandel failHandel;

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
                                               TokenComponent tokenComponent,
                                               LogoutSuccessHandler logoutSuccessHandler
                                                ) throws Exception {
        http.csrf(ServerHttpSecurity.CsrfSpec::disable)
                .httpBasic(Customizer.withDefaults())
                .authenticationManager(authenticationManager)
                .exceptionHandling()
                //定义未登录的返回
                .authenticationEntryPoint(new CustomHttpBasicServerAuthenticationEntryPoint())
                //定义鉴权失败
                .accessDeniedHandler((swe,e) -> {
                    swe.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
                    return swe.getResponse().writeWith(Mono.just(new DefaultDataBufferFactory().wrap("未获取到权限！".getBytes())));
                })
                .and()
                //定义Security默认的存储信息
                .securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
                //权限设定
                .authorizeExchange()
                //白名单放行
                .pathMatchers(excludeAuthPages).permitAll()
                //OPTIONS请求放行
                .pathMatchers(HttpMethod.OPTIONS).permitAll()
                //其他请求通过自定义鉴权
                .anyExchange().access(new MyAuthorizationManager())
                .and()
                //开启表单登录
                .formLogin()
                //登录请求
                .loginPage("/auth/login")
                //认证成功的处理
                .authenticationSuccessHandler(successLoginHandel)
                //认证失败的处理
                .authenticationFailureHandler(failHandel)
                .and()
                .logout()
                //退出登录的请求
                .logoutUrl("/auth/logout")
                //退出登录成功的处理
                .logoutSuccessHandler(logoutSuccessHandler)
                .and()
                .cors().configurationSource(configurationSource());
        //将自定义的拦截器添加到过滤器链
        http.addFilterAt(new CorsFilter(), SecurityWebFiltersOrder.SECURITY_CONTEXT_SERVER_WEB_EXCHANGE);
        http.addFilterAt(new JwtTokenAuthenticationFilter(tokenComponent,myUserDetailService), SecurityWebFiltersOrder.HTTP_BASIC);
        http.addFilterAt(new ServerHttpReqFilter(),SecurityWebFiltersOrder.FIRST);
        SecurityWebFilterChain chain = http.build();
        //自定义实现AuthenticationConverter改变Form表单获取参数的方式
        Iterator<WebFilter> webFilterIterator = chain.getWebFilters().toIterable().iterator();
        while (webFilterIterator.hasNext()) {
            WebFilter filter = webFilterIterator.next();
            if (filter instanceof  AuthenticationWebFilter) {
                AuthenticationWebFilter webFilter = (AuthenticationWebFilter) filter;
                webFilter.setServerAuthenticationConverter(new CustomServerFormLoginAuthenticationConverter(redisTemplate));
            }
        }
        return chain;
    }

   CorsConfigurationSource configurationSource() {
       CorsConfiguration corsConfiguration = new CorsConfiguration();
       corsConfiguration.setAllowedOrigins(Arrays.asList("*"));
       corsConfiguration.setAllowedHeaders(Arrays.asList("*"));
       corsConfiguration.setAllowedMethods(Arrays.asList("*"));
       corsConfiguration.setMaxAge(3600L);
       corsConfiguration.setAllowedOrigins(Arrays.asList("*"));
       UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
       urlBasedCorsConfigurationSource.registerCorsConfiguration("/**",corsConfiguration);
       return urlBasedCorsConfigurationSource;
   }
}
