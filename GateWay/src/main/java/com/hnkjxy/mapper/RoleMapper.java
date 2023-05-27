package com.hnkjxy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hnkjxy.entity.Role;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @version: java version 1.8
 * @Author: Mr Orange
 * @description:
 * @date: 2023-04-29 15:26
 */
@Mapper
public interface RoleMapper extends BaseMapper<Role> {
    /**
     * 根据用户ID获取权限信息
     * @author Mr OY
     * @date 2023/5/2
     * @param userId
     * @return 获取权限
     */
    List<Role> getRoleByUserId(Integer userId);
}
