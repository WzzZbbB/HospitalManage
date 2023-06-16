package com.hnkjxy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hnkjxy.data.ResponseData;
import com.hnkjxy.entity.Menu;
import com.hnkjxy.entity.MenuVo;
import com.hnkjxy.entity.User;
import org.springframework.validation.BindingResult;

import java.util.List;

/**
 * @version: java version 17
 * @Author: Mr WzzZ
 * @description:
 * @date: 2023-06-06 10:36
 */
public interface MenuService extends IService<Menu> {
    ResponseData<List<MenuVo>> getCatalogueAndMenuByUserName(User user);
}
