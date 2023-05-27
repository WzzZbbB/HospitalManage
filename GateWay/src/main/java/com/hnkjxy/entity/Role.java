package com.hnkjxy.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.util.List;

/**
 * @version: java version 1.8
 * @Author: Mr Orange
 * @description:
 * @date: 2023-04-29 14:48
 */
@Data
public class Role {
    private Integer roleId;
    private String roleName;
}
