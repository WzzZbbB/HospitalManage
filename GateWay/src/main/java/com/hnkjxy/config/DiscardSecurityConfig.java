package com.hnkjxy.config;

/**
 * @version: java version 17
 * @Author: Mr WzzZ
 * @description: Security配置
 * @date: 2023-04-29 17:36
 **/

//@EnableWebFluxSecurity
//@Configuration
public class DiscardSecurityConfig {
/*    @Value("${wzzz.url.white}")
    private String[] excludeAuthPages;

    @Resource
    UserDetailServiceImpl myUserDetailService;

    @Autowired
    private ServerCodecConfigurer serverCodecConfigurer ;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private AuthenticationFailHandel failHandel;

    @Autowired
    private ObjectMapper mapper;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public ReactiveAuthenticationManager reactiveAuthenticationManager(UserDetailServiceImpl userDetailsService) {
        UserDetailsRepositoryReactiveAuthenticationManager authenticationManager = new UserDetailsRepositoryReactiveAuthenticationManager(userDetailsService);
        authenticationManager.setPasswordEncoder(passwordEncoder());
        return authenticationManager;
    }

    @Bean
    public SecurityWebFilterChain filterChain (ServerHttpSecurity http,
                                               ReactiveAuthenticationManager authenticationManager,
                                               AuthenticationSuccessHandel successLoginHandel,
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
                webFilter.setServerAuthenticationConverter(new CustomJsonLoginAuthenticationConverter(redisTemplate));
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

    @Bean
    public ServerSecurityContextRepository securityContextRepository() {
        WebSessionServerSecurityContextRepository securityContextRepository =
                new WebSessionServerSecurityContextRepository();
        securityContextRepository.setSpringSecurityContextAttrName("wzzz-security-context");
        return securityContextRepository;
    }

    @Bean
    public ReactiveAuthenticationManager authenticationManager() {
        UserDetailsRepositoryReactiveAuthenticationManager authenticationManager =
                new UserDetailsRepositoryReactiveAuthenticationManager(myUserDetailService);
        authenticationManager.setPasswordEncoder(passwordEncoder());
        return authenticationManager;
    }

    private AuthenticationWebFilter authenticationWebFilter() {
        AuthenticationWebFilter filter = new AuthenticationWebFilter(authenticationManager());

        filter.setSecurityContextRepository(securityContextRepository());
        filter.setAuthenticationConverter(jsonBodyAuthenticationConverter());
        filter.setAuthenticationSuccessHandler(new RedirectServerAuthenticationSuccessHandler("/home"));
        filter.setAuthenticationFailureHandler(
                new ServerAuthenticationEntryPointFailureHandler(
                        new RedirectServerAuthenticationEntryPoint("/authentication-failure")
                )
        );
        filter.setRequiresAuthenticationMatcher(
                ServerWebExchangeMatchers.pathMatchers(HttpMethod.POST, "/signin")
        );

        return filter;
    }

    private Function<ServerWebExchange, Mono<Authentication>> jsonBodyAuthenticationConverter() {
        return exchange -> exchange
                .getRequest()
                .getBody()
                .next()
                .flatMap(body -> {
                    try {
                        UserController.SignInForm signInForm =
                                mapper.readValue(body.asInputStream(), UserController.SignInForm.class);

                        return Mono.just(
                                new UsernamePasswordAuthenticationToken(
                                        signInForm.getUsername(),
                                        signInForm.getPassword()
                                )
                        );
                    } catch (IOException e) {
                        return Mono.error(new LangDopeException("Error while parsing credentials"));
                    }
                });
    }*/
}
