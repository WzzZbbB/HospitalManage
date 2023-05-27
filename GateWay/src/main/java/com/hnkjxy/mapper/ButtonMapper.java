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
    /**
     * 根据权限ID获取具体按钮URI
     * @author Mr WzzZ
     * @date 2023/5/2
     * @param roleId 权限Id
     * @return 能访问的按钮URI
     */
    List<Button> getBtnByRoleId(Integer roleId);
}
