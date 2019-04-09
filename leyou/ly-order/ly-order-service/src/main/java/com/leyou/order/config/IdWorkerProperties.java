package com.leyou.order.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 *@Description TODO
 *@author apdoer
 *@CreateDate 2019/3/29-10:22
 *@Version 1.0
 *===============================
**/
@Data
@ConfigurationProperties(prefix = "ly.worker")
public class IdWorkerProperties {

    private Long workerId;//当前机器id

    private Long dataCenterId;//序列号
}
