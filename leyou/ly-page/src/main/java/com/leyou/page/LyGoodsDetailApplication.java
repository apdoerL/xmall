package com.leyou.page;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 *@Description 商品详情页启动器
 *@author apdoer
 *@CreateDate 2019/3/24-20:29
 *@Version 1.0
 *===============================
**/
@SpringBootApplication
@EnableFeignClients//feign远程服务调用
@EnableDiscoveryClient//开启发现eureka注册中心
public class LyGoodsDetailApplication {
    public static void main(String[] args) {
        SpringApplication.run(LyGoodsDetailApplication.class);
    }
}
