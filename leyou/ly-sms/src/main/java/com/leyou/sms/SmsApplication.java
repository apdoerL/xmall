package com.leyou.sms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 *@Description 短信微服务启动器
 *@author apdoer
 *@CreateDate 2019/3/26-10:01
 *@Version 1.0
 *===============================
**/
@SpringBootApplication
public class SmsApplication {
    public static void main(String[] args) {
        SpringApplication.run(SmsApplication.class,args);
    }
}
