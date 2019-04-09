package com.leyou.seckill.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *@Description 接口限流注解
 *@author apdoer
 *@CreateDate 2019/3/31-20:40
 *@Version 1.0
 *===============================
**/
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface AccessLimit {
    int seconds();//限流时间
    int maxCount();//最大请求次数
    boolean needLogin() default true;//是否需要登录.默认true
}
