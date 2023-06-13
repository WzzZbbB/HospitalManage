package com.hnkjxy.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

/**
 * @version: java version 1.8
 * @Author: Mr Orange
 * @description:
 * @date: 2023-04-29 14:48
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Builder
public class Role {
    /**
     * 角色ID
     * @Author Mr WzzZ
     * @Date 2023/6/6
     */
    private Long roleId;
    /**
     * 角色名称
     * @Author Mr WzzZ
     * @Date 2023/6/6
     */
    private String roleName;
    /**
     * 角色状态（0正常，1停用）
     * @Author Mr WzzZ
     * @Date 2023/6/6
     */
    private boolean status;
    /**
     * 删除标志（0存在，1删除）
     * @Author Mr WzzZ
     * @Date 2023/6/6
     */
    private boolean delFlag;
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
    private LocalDateTime updateTime;
}
