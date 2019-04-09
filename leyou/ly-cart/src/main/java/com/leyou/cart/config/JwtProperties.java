package com.leyou.cart.config;

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

    private String secret;
    private String pubKeyPath;
    private String priKeyPath;
    private Integer expire;//过期时间

    private String cookieName;
    private Integer cookieMaxAge;

    private PublicKey publicKey;//公钥
    private PrivateKey privateKey;//私钥
    //对象一旦实例化后,就应该读取公钥和私钥

    /**
     * 实例化公钥私钥,如果不存在,项目启动不了,所有try起来没多大意义
     * @throws Exception
     */
    @PostConstruct//在对象实例化后执行,这个时候上面的几个变量都执行了
    public void init() throws Exception {
        //公钥和私钥如果不存在,先生成
        File pubPath = new File(pubKeyPath);
        File priPath = new File(priKeyPath);
        if (!pubPath.exists() || !priPath.exists()){
            RsaUtils.generateKey(pubKeyPath,priKeyPath,secret);
        }
        //读取公钥和私钥
        this.privateKey = RsaUtils.getPrivateKey(priKeyPath);
        this.publicKey = RsaUtils.getPublicKey(pubKeyPath);
    }
}
