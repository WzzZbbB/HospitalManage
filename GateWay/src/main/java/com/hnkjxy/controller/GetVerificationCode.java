package com.hnkjxy.controller;

import com.hnkjxy.data.Code;
import com.hnkjxy.data.ResponseCode;
import com.hnkjxy.data.ResponseData;
import com.hnkjxy.entity.Menu;
import com.hnkjxy.entity.MenuVo;
import com.hnkjxy.service.MenuService;
import com.hnkjxy.utils.VerificationCode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriTemplate;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.hnkjxy.constant.RedisConstant.VERIFY_IMG_CODE;

/**
 * @version: java version 1.8
 * @Author: Mr Orange
 * @description:
 * @date: 2023-04-29 21:21
 */
@RestController
@RequiredArgsConstructor
public class GetVerificationCode {

    private final StringRedisTemplate redisTemplate;
    private final MenuService menuService;

    @GetMapping("/getVerifyCode")
    public ResponseData<Code> getVerifyCode() {
        try {
            Code code = VerificationCode.getVerificationCode();
            redisTemplate.opsForValue().set(VERIFY_IMG_CODE+code.getUuid(),code.getX().toString(),300L, TimeUnit.SECONDS);
            ResponseData<Code> response = new ResponseData<>();
            response.setData(code);
            response.setCode(200);
            return response;
        } catch (IOException e) {
            return new ResponseData<>(500,"验证码获取失败");
        }
    }

    @GetMapping("/hello")
    public String hello() {
       return "hello";
    }

    @GetMapping("/test")
    public ResponseData<List<MenuVo>> test(){
        //获取菜单
        List<Menu> list = this.menuService.lambdaQuery()
                .in(Menu::getMenuType, "M", "C")
                .list();
        return new ResponseData<>(ResponseCode.SUCCESS,this.get(list,0L,""));
    }

    public List<MenuVo> get(List<Menu> menus, Long parentId,String path){
        return menus.stream()
                .filter(item -> item.getParentId().equals(parentId))
                .map(item -> {
                    MenuVo menuVo = new MenuVo();
                    menuVo.setName(item.getMenuName());
                    String url = "";
                    if(StringUtils.hasText(path)){
                        url= path + item.getPath();
                    }else{
                        url = item.getPath();
                    }
                    menuVo.setUrl(url);
                    menuVo.setIcon(item.getIcon());
                    List<MenuVo> menuVos1 = get(menus, item.getMenuId(),menuVo.getUrl());
                    menuVo.setChildren(menuVos1.isEmpty()?null:menuVos1);
                    return menuVo;
                }).toList();
    }
}
