package com.hnkjxy.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hnkjxy.entity.User;
import com.hnkjxy.mapper.UserMapper;
import com.hnkjxy.service.UserService;
import org.springframework.stereotype.Service;

/**
 * @version: java version 1.8
 * @Author: Mr Orange
 * @description:
 * @date: 2023-04-29 15:28
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
