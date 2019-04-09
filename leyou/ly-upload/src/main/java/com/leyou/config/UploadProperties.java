package com.leyou.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * @Description uploadservice 配置文件读取
 * @Author apdoer
 * @Date 2019/3/19 23:20
 * @Version 1.0
 */
@ConfigurationProperties(prefix = "ly.upload")
@Data
public class UploadProperties {
    private String baseUrl;

    private List<String>allowTypes;
}
