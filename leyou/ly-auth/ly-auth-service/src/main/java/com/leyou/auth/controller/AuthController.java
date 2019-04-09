package com.leyou.auth.controller;

import com.leyou.auth.config.JwtProperties;
import com.leyou.auth.pojo.UserInfo;
import com.leyou.auth.service.AuthService;
import com.leyou.auth.utils.JwtUtils;
import com.leyou.common.enums.ExceptionEnum;
import com.leyou.common.exception.LyException;
import com.leyou.common.utils.CookieUtils;
import com.leyou.user.pojo.User;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *@Description TODO
 *@author apdoer
 *@CreateDate 2019/3/26-22:48
 *@Version 1.0
 *===============================
**/
@RestController
public class AuthController {
    @Autowired
    private AuthService authService;

    @Value("${ly.jwt.cookieName}")
    private String cookieName;

    @Autowired
    private JwtProperties properties;
    /**
     * 登录  鉴权
     * @param username
     * @param password
     * @return  token
     */
    @PostMapping("login")
    public ResponseEntity<Void> login(
            @RequestParam("username")String username, @RequestParam("password") String password, HttpServletRequest request, HttpServletResponse response){
        //登录
        String token = authService.login(username,password);

        //写入cookie 每次请求时携带,用流写回给页面没有任何意义 httpOnly,禁止js操作cookie
        CookieUtils.newBuilder(response).httpOnly().request(request).build(cookieName,token);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * 校验用户登状态 这个动作在每个页面开始都会被执行,
     *              所以还要刷新token,重新写入
     * @param token
     * @return
     */
    @GetMapping("verify")
    public ResponseEntity<UserInfo> verify(@CookieValue("LY_TOKEN")String token,HttpServletRequest request, HttpServletResponse response){
        if (StringUtils.isEmpty(token)){
            throw new LyException(ExceptionEnum.UNAUTHORIZED);
        }
        try {
            //根据公钥解析token
            UserInfo userInfo = JwtUtils.getInfoFromToken(token, properties.getPublicKey());
            //刷新token
            String newToken = JwtUtils.generateToken(userInfo, properties.getPrivateKey(), properties.getExpire());
            CookieUtils.newBuilder(response).httpOnly().request(request).build(cookieName,newToken);
            //返回用户信息
            return ResponseEntity.ok(userInfo);
        } catch (Exception e) {
            //token过期或者token无效
            throw new LyException(ExceptionEnum.UNAUTHORIZED);
        }
    }
}
