package com.leyou.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 *@Description 鉴权中心启动器
 *@author apdoer
 *@CreateDate 2019/3/26-22:05
 *@Version 1.0
 *===============================
**/
@SpringBootApplication
@EnableDiscoveryClient//从注册中心拉取服务
@EnableFeignClients//开启feign服务调用
public class LyAuthApplication {
    public static void main(String[] args) {
        SpringApplication.run(LyAuthApplication.class);
    }
}
