package com.hnkjxy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hnkjxy.entity.Menu;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @version: java version 1.8
 * @Author: Mr Orange
 * @description:
 * @date: 2023-04-29 15:27
 */
@Mapper
public interface MenuMapper extends BaseMapper<Menu> {
    List<Menu> getMenuByRoleId(Integer roleId);
}
