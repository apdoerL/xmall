package com.leyou.order.config;

import com.leyou.common.utils.IdWorker;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Description DOTO
 * @Author Administrator
 * @Date 2019/3/2910:25
 * @Version 1.0
 */
@Configuration
@EnableConfigurationProperties(IdWorkerProperties.class)
public class IdWorkerConfig {

    /**
     * 注册idworker
     * @param properties
     * @return
     */
    @Bean
    public IdWorker idWorker(IdWorkerProperties properties){
        return new IdWorker(properties.getWorkerId(),properties.getDataCenterId());
    }
}
