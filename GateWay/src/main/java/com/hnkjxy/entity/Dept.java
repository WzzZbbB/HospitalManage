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
 * @date: 2023-06-06 10:25
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Dept implements Serializable {
    /**
     * 部门ID
     * @Author Mr WzzZ
     * @Date 2023/6/6
     */
    private Long deptId;
    /**
     * 父级部门ID
     * @Author Mr WzzZ
     * @Date 2023/6/6
     */
    private Long parentId;
    /**
     * 部门名称
     * @Author Mr WzzZ
     * @Date 2023/6/6
     */
    private String deptName;
    /**
     * 部门状态
     * @Author Mr WzzZ
     * @Date 2023/6/6
     */
    private boolean status;
    /**
     * 部门删除标志
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
