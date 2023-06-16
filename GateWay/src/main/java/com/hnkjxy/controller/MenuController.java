package com.hnkjxy.controller;

import cn.hutool.core.net.URLDecoder;
import com.hnkjxy.data.ResponseData;
import com.hnkjxy.entity.MenuVo;
import com.hnkjxy.entity.User;
import com.hnkjxy.service.MenuService;
import com.hnkjxy.utils.JsonUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
import java.util.List;


/**
 * @version: java version 17
 * @Author: Mr WzzZ
 * @description:
 * @date: 2023-06-15 16:52
 */
@RestController
@RequiredArgsConstructor
public class MenuController {
    private final MenuService menuService;

    /**
     * 根据用户名查找用户的菜单
     * @Author Mr WzzZ
     * @Date 2023/6/15
     * @Param
     * @return com.hnkjxy.data.ResponseData<java.util.List<com.hnkjxy.entity.MenuVo>>
     */
    @GetMapping("/getMenu")
    public ResponseData<List<MenuVo>> getMenu(ServerHttpRequest request){
        String userJson = request.getHeaders().getFirst("USER");
        User user = JsonUtils.serializable(URLDecoder.decode(userJson, StandardCharsets.UTF_8),User.class);
        return menuService.getCatalogueAndMenuByUserName(user);
    }

    @GetMapping("/test")
    public String test() {
        return "test";
    }
}
