package com.leyou.cart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 *@Description 购物车微服务,购物车基本不需要被引用,所以不需要拆分,购物车基本只对redis操作
 *@author apdoer
 *@CreateDate 2019/3/27-23:05
 *@Version 1.0
 *===============================
**/
@SpringBootApplication
@EnableDiscoveryClient
public class LyCartApplication {
    public static void main(String[] args) {
        SpringApplication.run(LyCartApplication.class);
    }
}
