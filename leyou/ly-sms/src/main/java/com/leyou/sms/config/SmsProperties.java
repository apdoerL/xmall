package com.leyou.sms.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 *@Description TODO
 *@author apdoer
 *@CreateDate 2019/3/26-10:05
 *@Version 1.0
 *===============================
**/
@ConfigurationProperties(prefix = "ly.sms")//这样会读取配置文件中ly.sms开头的所有信息
@Data
public class SmsProperties {

    String accessKeyId;

    String accessKeySecret;

    String signName;

    String verifyCodeTemplate;

}
