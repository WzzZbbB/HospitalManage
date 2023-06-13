package com.hnkjxy.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @version: java version 17
 * @Author: Mr WzzZ
 * @description:
 * @date: 2023-06-06 10:29
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Menu implements Serializable {
    /**
     * 菜单ID
     * @Author Mr WzzZ
     * @Date 2023/6/6
     */
    private Long menuId;
    /**
     * 菜单名称
     * @Author Mr WzzZ
     * @Date 2023/6/6
     */
    private String menuName;
    /**
     * 父级ID
     * @Author Mr WzzZ
     * @Date 2023/6/6
     */
    private Long parentId;
    /**
     * 菜单路径
     * @Author Mr WzzZ
     * @Date 2023/6/6
     */
    private String path;
    /**
     * 菜单类型（M 目录，C 菜单，F 按钮）
     * @Author Mr WzzZ
     * @Date 2023/6/6
     */
    private char menuType;
    /**
     * 是否显示标识
     * @Author Mr WzzZ
     * @Date 2023/6/6
     */
    private boolean visible;
    /**
     * 图标
     * @Author Mr WzzZ
     * @Date 2023/6/6
     */
    private String icon;
    /**
     * 创建者
     * @Author Mr WzzZ
     * @Date 2023/6/6
     */
    private String createBy;
    /**
     * 创建时间
     * @Author Mr WzzZ
     * @Date 2023/6/6
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "Asia/Shanghai")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime createTime;
    /**
     * 更新者
     * @Author Mr WzzZ
     * @Date 2023/6/6
     */
    private String updateBy;
    /**
     * 更新时间
     * @Author Mr WzzZ
     * @Date 2023/6/6
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "Asia/Shanghai")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime updateTime;
}
