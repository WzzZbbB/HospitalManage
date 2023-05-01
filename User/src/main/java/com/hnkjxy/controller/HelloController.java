package com.hnkjxy.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @version: java version 1.8
 * @Author: Mr Orange
 * @description:
 * @date: 2023-04-29 21:01
 */
@RestController
public class HelloController {
    @GetMapping("/user/hello")
    public String hello() {
        return "Hello";
    }
}
