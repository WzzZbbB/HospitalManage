package com.hnkjxy.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hnkjxy.entity.Button;
import com.hnkjxy.entity.Menu;
import com.hnkjxy.entity.Role;
import com.hnkjxy.entity.User;
import com.hnkjxy.mapper.ButtonMapper;
import com.hnkjxy.mapper.MenuMapper;
import com.hnkjxy.mapper.RoleMapper;
import com.hnkjxy.mapper.UserMapper;
import com.hnkjxy.utils.RedisUtil;
import jakarta.annotation.Resource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.ObjectUtils;
import org.springframework.util.PathMatcher;
import reactor.core.publisher.Mono;

import javax.naming.AuthenticationException;
import java.util.*;
import java.util.stream.Collectors;

import static com.hnkjxy.constant.RedisConstant.USER_ROLE;

/**
 * @version: java version 1.8
 * @Author: Mr Orange
 * @description:
 * @date: 2023-04-29 14:40
 */
@Service
public class MyUserDetailService implements ReactiveUserDetailsService , ReactiveUserDetailsPasswordService {
    @Resource
    private UserService userService;

    @Resource
    private RoleMapper roleMapper;

    @Resource
    private MenuMapper menuMapper;

    @Resource
    private RedisUtil redisUtil;

    @Resource
    private ButtonMapper buttonMapper;
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
        //查询角色信息
        List<Role> role = roleMapper.getRoleByUserId(user.getId());
        if (role == null) {
            return Mono.empty();
        }

        String URIS = redisUtil.getString(USER_ROLE + username);
        List<String> list = JSONUtil.toList(URIS, String.class);
        Set<String> roles = new HashSet<>(list);
        if (CollectionUtil.isEmpty(roles)) {
            Set<String> uris = role.stream().map(item -> item.getRoleName()).collect(Collectors.toSet());
            //查询路径信息
            Set<List<Menu>> menus = role.stream().map(item -> menuMapper.getMenuByRoleId(item.getRoleId())).collect(Collectors.toSet());
            menus.stream().forEach(item -> item.forEach(item2 -> uris.add("menu_"+item2.getMenuUri())));

            Set<List<Button>> buttons = role.stream().map(item -> buttonMapper.getBtnByRoleId(item.getRoleId())).collect(Collectors.toSet());
            buttons.stream().forEach(item -> item.forEach(item2 -> uris.add(item2.getBtnUri())));

            roles = uris;
        }

        //设置当前用户的授权信息
        user.setRoles(roles);
        UserDetails userDetails = org.springframework.security.core.userdetails.User.withUsername(username)
                .password(user.getPassword()).roles(roles.toArray(new String[]{}))
                .authorities(roles.toArray(new String[]{})).build();
        return Mono.just(userDetails);
    }

    public boolean checkUserRoleResource(String path, Authentication authentication) {
        boolean match = false;
        if (StrUtil.isBlank(path) || ObjectUtils.isEmpty(authentication)) {
            return match;
        }
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        PathMatcher pathMatcher = new AntPathMatcher();
        for (GrantedAuthority authority : authorities) {
            match = pathMatcher.match(authority.getAuthority(),path);
            if (match) {
                return match;
            }
        }
        return match;
    }

    public static String removePreFix(String str) {
        String preFix = "menu_";
        if (str.toLowerCase().matches("^"+preFix+".*")) {
            return str.substring(preFix.length());
        }
        return str;
    }
}
