package com.leyou.common.vo;

import com.leyou.common.enums.ExceptionEnum;
import lombok.Data;

/**
 *@Description restful风格返回给前端的vo对象
 *@author apdoer
 *@CreateDate 2019/3/19-15:16
 *@Version 1.0
 *===============================
**/
@Data //写在类上的注解.为该类提供读写属性,此外还提供了equals,hascode,tostring方法 ,还包含Getter/Setter
public class ExceptionResult {

    private Integer status;//状态码
    private String message;//返回信息
    private Long timeStamp;//时间戳

    /**
     * 提供一个构造方法,传入自定义异常枚举,返回exceptionResult 返回给前端的vo
     * @param exceptionEnum
     */
    public ExceptionResult (ExceptionEnum exceptionEnum){
        this.status = exceptionEnum.getCode();
        this.message = exceptionEnum.getMsg();
        this.timeStamp = System.currentTimeMillis();
    }

}
