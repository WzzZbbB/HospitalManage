package com.hnkjxy.entity;

import lombok.Data;

import java.util.List;

/**
 * @version: java version 17
 * @Author: Mr WzzZ
 * @description:
 * @date: 2023-06-12 15:07
 */
@Data
public class MenuVo {

    private String name;

    private String url;

    private String icon;

    private List<MenuVo> children;
}
