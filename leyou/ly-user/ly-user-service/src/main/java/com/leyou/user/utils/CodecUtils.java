package com.leyou.user.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


/**
 *@Description 密码加密工具类,用户微服务单独用,所以不放在common中
 *@author apdoer
 *@CreateDate 2019/3/26-20:07
 *@Version 1.0
 *===============================
**/
public class CodecUtils {

    public static String passwordBcryptEncode(String username,String password){

        return new BCryptPasswordEncoder().encode(username + password);
    }

    public static Boolean passwordConfirm(String rawPassword,String encodePassword){
        return new BCryptPasswordEncoder().matches(rawPassword,encodePassword);
    }
}
