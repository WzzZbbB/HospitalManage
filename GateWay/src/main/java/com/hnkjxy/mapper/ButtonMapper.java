package com.hnkjxy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hnkjxy.entity.Button;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @version: java version 1.8
 * @Author: Mr Orange
 * @description:
 * @date: 2023-04-30 21:44
 */
@Mapper
public interface ButtonMapper extends BaseMapper<Button> {
    List<Button> getBtnByRoleId(Integer roleId);
}
