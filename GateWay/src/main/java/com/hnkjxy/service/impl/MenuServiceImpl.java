package com.hnkjxy.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hnkjxy.entity.Menu;
import com.hnkjxy.mapper.MenuMapper;
import com.hnkjxy.service.MenuService;
import org.springframework.stereotype.Service;

/**
 * @version: java version 17
 * @Author: Mr WzzZ
 * @description:
 * @date: 2023-06-06 10:37
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {
}
