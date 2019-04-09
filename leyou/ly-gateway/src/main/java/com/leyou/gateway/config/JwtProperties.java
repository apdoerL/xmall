package com.leyou.gateway.config;

import com.leyou.auth.utils.RsaUtils;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.annotation.PostConstruct;
import java.io.File;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 *@Description jwt配置文件
 *@author apdoer
 *@CreateDate 2019/3/26-22:30
 *@Version 1.0
 *===============================
**/
@ConfigurationProperties(prefix = "ly.jwt") //这样配置就可以读取application.yml中的属性
@Data
public class JwtProperties {

    private String pubKeyPath;

    private String cookieName;

    private PublicKey publicKey;//公钥
    //对象一旦实例化后,就应该读取公钥和私钥

    /**
     * 实例化公钥私钥,如果不存在,项目启动不了,所以try起来没多大意义
     * @throws Exception
     */
    @PostConstruct//在对象实例化后执行,这个时候上面的几个变量都执行了
    public void init() throws Exception {
        //读取公钥和私钥
        this.publicKey = RsaUtils.getPublicKey(pubKeyPath);
    }
}
