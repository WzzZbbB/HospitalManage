package com.hnkjxy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @version: java version 1.8
 * @Author: Mr Orange
 * @description:
 * @date: ${YEAR}-${MONTH}-${DAY} ${TIME}
 */
@SpringBootApplication
public class GateWayApplication {
    public static void main(String[] args) {
        SpringApplication.run(GateWayApplication.class,args);
    }
}