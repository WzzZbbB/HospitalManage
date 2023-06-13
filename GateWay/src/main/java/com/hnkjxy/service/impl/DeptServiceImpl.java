package com.hnkjxy.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hnkjxy.entity.Dept;
import com.hnkjxy.mapper.DeptMapper;
import com.hnkjxy.service.DeptService;
import org.springframework.stereotype.Service;

/**
 * @version: java version 17
 * @Author: Mr WzzZ
 * @description:
 * @date: 2023-06-06 10:36
 */
@Service
public class DeptServiceImpl extends ServiceImpl<DeptMapper, Dept> implements DeptService {
}
