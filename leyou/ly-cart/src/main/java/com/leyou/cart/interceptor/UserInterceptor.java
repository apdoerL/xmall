package com.leyou.cart.interceptor;

import com.leyou.auth.pojo.UserInfo;
import com.leyou.auth.utils.JwtUtils;
import com.leyou.cart.config.JwtProperties;
import com.leyou.common.utils.CookieUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *@Description TODO
 *@author apdoer
 *@CreateDate 2019/3/27-23:19
 *@Version 1.0
 *===============================
**/
@EnableConfigurationProperties(JwtProperties.class)
@Slf4j
public class UserInterceptor implements HandlerInterceptor {

    private JwtProperties jwtProperties;//这个就通过构造注入

    //使用线程域传递对象
    private static final ThreadLocal<UserInfo> tl = new ThreadLocal<>();

    public UserInterceptor(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    }

    /**
     * 前置拦截 在请求到达之前,把token解析出来
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //获取cookie中的token
        String token = CookieUtils.getCookieValue(request, jwtProperties.getCookieName());
        try {
            //解析token
            UserInfo userInfo = JwtUtils.getInfoFromToken(token, jwtProperties.getPublicKey());
            //传递user
            tl.set(userInfo);//key就是线程
            //放行
            return true;
        }catch (Exception e){
            log.error("【购物车服务】 解析用户身份失败",e);
            return false;
        }
    }

    /**
     * 视图渲染完执行
     * @param request
     * @param response
     * @param handler
     * @param ex
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        //从线程变量中删除
        tl.remove();
    }

    /**
     * 获取购物车用户
     * @return
     */
    public static UserInfo getUser(){
        return tl.get();
    }
}
