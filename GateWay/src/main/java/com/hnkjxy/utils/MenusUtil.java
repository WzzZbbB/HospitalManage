package com.hnkjxy.utils;

import com.hnkjxy.entity.Menu;
import com.hnkjxy.entity.MenuVo;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @version: java version 17
 * @Author: Mr WzzZ
 * @description:
 * @date: 2023-06-13 14:28
 */
public class MenusUtil {
    /**
     * 将Menu转换成树结构的MenuVO
     * @Author Mr WzzZ
     * @Date 2023/6/13
     * @Param menus 查询出的Menu
     * @Param parentId 父ID
     * @Param path 拼接的路径
     * @return java.util.List<com.hnkjxy.entity.MenuVo>
     */
    public static List<MenuVo> initMenu(List<Menu> menus, Long parentId, String path){
        return menus.stream()
                .filter(item -> item.getParentId().equals(parentId))
                .map(item -> {
                    MenuVo menuVo = new MenuVo();
                    menuVo.setName(item.getMenuName());
                    String url = "";
                    if(StringUtils.hasText(path)){
                        url= path + item.getPath();
                    }else{
                        url = item.getPath();
                    }
                    menuVo.setUrl(url);
                    menuVo.setIcon(item.getIcon());
                    List<MenuVo> menuVos1 = initMenu(menus, item.getMenuId(),menuVo.getUrl());
                    menuVo.setChildren(menuVos1.isEmpty()?null:menuVos1);
                    return menuVo;
                }).toList();
    }
}
