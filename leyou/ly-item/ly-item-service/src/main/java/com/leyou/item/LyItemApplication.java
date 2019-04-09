package com.leyou.item;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @Description 商品服务启动
 * @Author apdoer
 * @Date 2019/3/19 13:56
 * @Version 1.0
 */
@SpringBootApplication
@EnableDiscoveryClient//发现服务
@MapperScan("com.leyou.item.mapper")//添加扫描包
public class LyItemApplication {
    public static void main(String[] args) {
        SpringApplication.run(LyItemApplication.class);
    }
}
