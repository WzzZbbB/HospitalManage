package com.hnkjxy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hnkjxy.entity.Menu;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @version: java version 17
 * @Author: Mr WzzZ
 * @description:
 * @date: 2023-06-06 10:35
 */
@Mapper
public interface MenuMapper extends BaseMapper<Menu> {
    List<Menu> getCatalogueAndMenuByUserName(String userName);

    List<Menu> getButtonByUserName(String userName);
}
