package com.leyou;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * @Description 注册中心启动器
 * @Author apdoer
 * @Date 2019/3/1822:23
 * @Version 1.0
 */
@SpringBootApplication
@EnableEurekaServer//启用eureka注册中心
public class LyRegistry {
    public static void main(String[] args) {
        SpringApplication.run(LyRegistry.class);
    }
}
