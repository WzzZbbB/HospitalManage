package com.hnkjxy.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hnkjxy.entity.Menu;
import com.hnkjxy.entity.MenuVo;
import com.hnkjxy.entity.User;
import com.hnkjxy.mapper.MenuMapper;
import com.hnkjxy.mapper.RoleMapper;
import com.hnkjxy.service.UserService;
import com.hnkjxy.utils.JsonUtils;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
import org.springframework.util.*;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.hnkjxy.constant.RedisConstant.USER_ROLE;
import static com.hnkjxy.utils.MenusUtil.initMenu;

/**
 * @version: java version 17
 * @Author: Mr WzzZ
 * @description: 自定义用户信息认证
 * @date: 2023-04-29 14:40
 */
@Service
@RequiredArgsConstructor
@RefreshScope
public class UserDetailServiceImpl implements ReactiveUserDetailsService , ReactiveUserDetailsPasswordService {
    private final UserService userService;
    private final MenuMapper menuMapper;
    private final StringRedisTemplate redisTemplate;
    @Value("${wzzz.url.white}")
    private String[] excludeAuthPages;

    @Override
    public Mono<UserDetails> updatePassword(UserDetails user, String newPassword) {
        String username = user.getUsername();
        User one = userService.getOne(new LambdaQueryWrapper<User>().eq(User::getUsername, username));
        one.setPassword(newPassword);
        boolean b = userService.saveOrUpdate(one);
        if (b) {
            ((User)user).setPassword(newPassword);
        }
        return Mono.just(user);
    }

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        //根据用户名查询当前用户
        User user = userService.getOne(new LambdaQueryWrapper<User>().eq(User::getUsername, username));
        if (ObjectUtils.isEmpty(user)) {
            return Mono.empty();
        }
        String[] menus = queryMenu(username);
        UserDetails userDetails = org.springframework.security.core.userdetails.User.withUsername(username)
                .password(user.getPassword())
                .authorities(menus).build();
        return Mono.just(userDetails);
    }

    /**
     * 根据用户名查询菜单信息
     * @Author Mr WzzZ
     * @Date 2023/6/8
     * @Param userName 用户名
     */
    public String[] queryMenu(String userName) {
        List<MenuVo> menus = JSONUtil.toList(redisTemplate.opsForValue().get(USER_ROLE + userName), MenuVo.class);
        if (menus.isEmpty()) {
            //查询部门的菜单和目录信息
            List<Menu> catalogueAndMenus = menuMapper.getCatalogueAndMenuByUserName(userName);
            //查询角色的按钮信息
            List<Menu> buttons = menuMapper.getButtonByUserName(userName);
            catalogueAndMenus.addAll(buttons);
            List<MenuVo> menuVos = initMenu(catalogueAndMenus, 0L, "");
            redisTemplate.opsForValue().set(USER_ROLE+userName,JsonUtils.deserializer(menuVos),1L,TimeUnit.DAYS);
            return initUri(menuVos, new ArrayList<>()).toArray(String[]::new);
        }
        return initUri(menus, new ArrayList<>()).toArray(String[]::new);
    }


    /**
     * 将数结构的MenuVo 转换成 最底层拼接好路径的list
     * @Author Mr WzzZ
     * @Date 2023/6/13
     * @Param menuVos 数结构Menu
     * @Param list 拼接好的集合
     * @return java.util.List<java.lang.String>
     */
    public static List<String> initUri(List<MenuVo> menuVos,List<String> list) {
        for (int i = 0; i < menuVos.size(); i++) {
            if (menuVos.get(i).getChildren() == null) {
                list.add(menuVos.get(i).getUrl());
                continue;
            }
            initUri(menuVos.get(i).getChildren(),list);
        }
        return list;
    }

    /**
     * 判断当前用户是否有权限访问该路径
     * @Author Mr WzzZ
     * @Date 2023/6/13
     * @Param path 当前访问的路径
     * @Param authentication 当前用户的认证信息
     * @return boolean
     */
    public boolean checkUserRoleResource(String path, Authentication authentication) {
        PathMatcher pathMatcher = new AntPathMatcher();
        List<String> whiteUri = Arrays.stream(excludeAuthPages).filter(item -> pathMatcher.match(path, item)).toList();
        if (CollectionUtil.isNotEmpty(whiteUri)) {
            return true;
        }
        if (StrUtil.isBlank(path) || ObjectUtils.isEmpty(authentication)) {
            return false;
        }
        Set<String> uriSet = AuthorityUtils.authorityListToSet(authentication.getAuthorities());
        List<String> list = uriSet.stream().filter(item -> pathMatcher.match(item, path)).toList();
        return !list.isEmpty();
    }

}
