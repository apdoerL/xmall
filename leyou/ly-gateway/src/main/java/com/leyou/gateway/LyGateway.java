package com.leyou.gateway;


import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

/**
 * @Description 网关启动器
 * @Author apdoer
 * @Date 2019/3/1913:05
 * @Version 1.0
 */
@SpringCloudApplication//springcloudapplication这个注解包含springbootapplication,enablediscoveryclient和enablecircuitBreaker三个注解
@EnableZuulProxy
public class LyGateway {

    public static void main(String[] args) {
        SpringApplication.run(LyGateway.class);
    }
}
