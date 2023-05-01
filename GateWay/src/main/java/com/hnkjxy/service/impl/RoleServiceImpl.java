package com.hnkjxy.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hnkjxy.mapper.RoleMapper;
import com.hnkjxy.service.RoleService;
import com.hnkjxy.entity.Role;
import org.springframework.stereotype.Service;

/**
 * @version: java version 1.8
 * @Author: Mr Orange
 * @description:
 * @date: 2023-04-29 15:29
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {
}
