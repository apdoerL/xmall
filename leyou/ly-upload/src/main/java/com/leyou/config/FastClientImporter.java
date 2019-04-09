package com.leyou.config;

import com.github.tobato.fastdfs.FdfsClientConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableMBeanExport;
import org.springframework.context.annotation.Import;
import org.springframework.jmx.support.RegistrationPolicy;

/**
 * @Description fastdfs配置
 * @Author apdoer
 * @Date 2019/3/1922:43
 * @Version 1.0
 */
@Configuration
@Import(FdfsClientConfig.class)//引入fdfs客户端
// 解决jmx重复注册bean的问题
@EnableMBeanExport(registration = RegistrationPolicy.IGNORE_EXISTING)
public class FastClientImporter {

}


