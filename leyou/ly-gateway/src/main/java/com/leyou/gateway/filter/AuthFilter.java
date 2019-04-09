package com.leyou.gateway.filter;

import com.leyou.auth.pojo.UserInfo;
import com.leyou.auth.utils.JwtUtils;
import com.leyou.common.utils.CookieUtils;
import com.leyou.gateway.config.FilterProperties;
import com.leyou.gateway.config.JwtProperties;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.context.annotation.FilterType;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 *@Description 拦截请求统一放在网关
 *@author apdoer
 *@CreateDate 2019/3/27-20:01
 *@Version 1.0
 *===============================
**/
@Component
@EnableConfigurationProperties({JwtProperties.class,FilterProperties.class})//允许读取这两个配置文件的内容
public class AuthFilter extends ZuulFilter {
    @Autowired
    private JwtProperties jwtProperties;

    private FilterProperties filterProperties;


    /**
     * 过滤器的类型,一共有四种过滤器,
     *              前置
     *              后置
     * @return
     */
    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE;//前置拦截
    }

    /**
     * 执行顺序   定义在系统内置的前置过滤器的前面   -1
     * @return
     */
    @Override
    public int filterOrder() {
        return FilterConstants.PRE_DECORATION_FILTER_ORDER-1;
    }

    /**
     * 是否过滤
     * @return
     */
    @Override
    public boolean shouldFilter() {
        //获取上下文
        RequestContext ctx = RequestContext.getCurrentContext();
        //获取请求
        HttpServletRequest request = ctx.getRequest();
        //获取请求路径
        String uri = request.getRequestURI();
        //判断是否在允许的范围内
        return !isAllowPath(uri);  //这里true是过滤,false是放行
    }

    /**
     * 判断请求路径是否在允许的范围内
     * @param uri
     * @return
     */
    private Boolean isAllowPath(String uri) {
        for (String allowPath : filterProperties.getAllowPaths()) {
            if (uri.startsWith(allowPath)){
                return true;
            }
        }
        return false;
    }

    /**
     * 具体的过滤的逻辑
     * @return
     * @throws ZuulException
     */
    @Override
    public Object run() throws ZuulException {
        //获取上下文
        RequestContext ctx = RequestContext.getCurrentContext();
        //获取request
        HttpServletRequest request = ctx.getRequest();
        //获取cookie中的token
        //Cookie[] cookies = request.getCookies();
        String token = CookieUtils.getCookieValue(request, jwtProperties.getCookieName());
        //解析token
        try {
            UserInfo userInfo = JwtUtils.getInfoFromToken(token, jwtProperties.getPublicKey());
            // TODO 校验权限
        } catch (Exception e) {
            // 解析token失败,未登录,拦截
            ctx.setSendZuulResponse(false);
            // 返回状态码
            ctx.setResponseStatusCode(403);
        }
        return null;
    }
}
