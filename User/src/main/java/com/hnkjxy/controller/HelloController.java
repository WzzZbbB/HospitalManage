package com.hnkjxy.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * @version: java version 1.8
 * @Author: Mr Orange
 * @description:
 * @date: 2023-04-29 21:01
 */
@RestController
public class HelloController {
    @GetMapping("/user/hello")
    public String hello(HttpServletRequest request) {
        try {
            System.out.println(URLDecoder.decode(request.getHeader("USER"),"utf-8"));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        return "Hello";
    }
}
