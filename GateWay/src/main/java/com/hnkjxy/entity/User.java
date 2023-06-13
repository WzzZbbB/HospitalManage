package com.hnkjxy.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.expression.spel.ast.Literal;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * @version: java version 1.8
 * @Author: Mr Orange
 * @description:
 * @date: 2023-04-29 14:46
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private Integer id;
    private String username;
    private String password;
    private Boolean enabled;
    private Boolean accountNonExpired;
    private Boolean accountNonLocked;
    private Boolean credentialsNonExpired;
    @TableField(exist = false)
    private List<Menu> menus;

    public void setRoles(List<Menu> roles) {
        this.menus = roles;
    }

    public User(String username, String password, List<Menu> menus) {
        this.username = username;
        this.password = password;
        this.menus = menus;
        this.enabled = true;
        this.accountNonExpired = false;
        this.credentialsNonExpired = false;
        this.accountNonLocked = false;
    }


    public List<Menu> getRoles() {
        return this.menus;
    }


    public boolean isAccountNonExpired() {
        return false;
    }

    public boolean isAccountNonLocked() {
        return false;
    }

    public boolean isCredentialsNonExpired() {
        return false;
    }

    public boolean isEnabled() {
        return false;
    }
}
