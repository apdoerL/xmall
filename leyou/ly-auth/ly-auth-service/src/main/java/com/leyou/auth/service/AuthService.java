package com.leyou.auth.service;

import com.leyou.auth.client.UserClient;
import com.leyou.auth.config.JwtProperties;
import com.leyou.auth.pojo.UserInfo;
import com.leyou.auth.utils.JwtUtils;
import com.leyou.common.enums.ExceptionEnum;
import com.leyou.common.exception.LyException;
import com.leyou.user.pojo.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

/**
 *@Description TODO
 *@author apdoer
 *@CreateDate 2019/3/26-22:48
 *@Version 1.0
 *===============================
**/
@Slf4j
@Service
@EnableConfigurationProperties(JwtProperties.class)
public class AuthService {

    @Autowired
    private UserClient userClient;

    @Autowired
    private JwtProperties prop;

    /**
     * 登录
     * @param username
     * @param password
     * @return
     */
    public String login(String username, String password) {
        try {
            //校验用户名和密码
            User user = userClient.queryByUsernameAndPassword(username, password);
        if (user==null){
            throw new LyException(ExceptionEnum.INVALID_USERNAME_PASSWORD);
        }
            //生成token
            String token = JwtUtils.generateToken(new UserInfo(user.getId(), username), prop.getPrivateKey(), prop.getExpire());
            return token;
        } catch (Exception e) {
            //记录下日志
            log.info("【授权中心】 用户名或密码有误,用户名称:{}",username,e);
            throw new LyException(ExceptionEnum.INVALID_USERNAME_PASSWORD);
        }
    }
}
