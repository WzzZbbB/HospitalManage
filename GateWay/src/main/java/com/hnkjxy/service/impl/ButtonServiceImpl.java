package com.hnkjxy.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hnkjxy.entity.Button;
import com.hnkjxy.mapper.ButtonMapper;
import com.hnkjxy.service.ButtonService;
import org.springframework.stereotype.Service;

/**
 * @version: java version 1.8
 * @Author: Mr Orange
 * @description:
 * @date: 2023-04-30 21:45
 */
@Service
public class ButtonServiceImpl extends ServiceImpl<ButtonMapper, Button> implements ButtonService {
}
