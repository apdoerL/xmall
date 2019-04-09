package com.leyou.gateway.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 *@Description TODO
 *@author apdoer
 *@CreateDate 2019/3/27-20:26
 *@Version 1.0
 *===============================
**/
@Data
@ConfigurationProperties("ly.filter")
public class FilterProperties {

    //zuul网关过滤器允许放行的路径
    private List<String> allowPaths;
}
