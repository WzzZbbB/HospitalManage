package com.hnkjxy.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hnkjxy.data.SuccessResponseCode;
import com.hnkjxy.data.ResponseData;
import com.hnkjxy.entity.Menu;
import com.hnkjxy.entity.MenuVo;
import com.hnkjxy.entity.User;
import com.hnkjxy.mapper.MenuMapper;
import com.hnkjxy.service.MenuService;
import com.hnkjxy.utils.ParamUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.hnkjxy.data.ErrorResponseCode.NO_PERMISSION;
import static com.hnkjxy.utils.MenusUtil.initMenu;

/**
 * @version: java version 17
 * @Author: Mr WzzZ
 * @description:
 * @date: 2023-06-06 10:37
 */
@Service
@RequiredArgsConstructor
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {
    private final MenuMapper menuMapper;
    @Override
    public ResponseData<List<MenuVo>> getCatalogueAndMenuByUserName(User user) {
        User validataUser = ParamUtils.nullParam(user, User.class, NO_PERMISSION, "请先获取权限！");
        List<Menu> menus = menuMapper.getCatalogueAndMenuByUserName(validataUser.getUsername());
        List<MenuVo> menuVos = initMenu(menus, 0L, "");
        return ResponseData.success(SuccessResponseCode.SUCCESS,menuVos);
    }
}
