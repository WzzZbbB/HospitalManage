package com.hnkjxy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hnkjxy.entity.Role;
import com.hnkjxy.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @version: java version 1.8
 * @Author: Mr Orange
 * @description:
 * @date: 2023-04-29 15:26
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
