package com.leyou.common.advice;

import com.leyou.common.enums.ExceptionEnum;
import com.leyou.common.exception.LyException;
import com.leyou.common.vo.ExceptionResult;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * @Description 通用异常通知
 * @Author apdoer
 * @Date 2019/3/19 15:00
 * @Version 1.0
 */
@ControllerAdvice
//拦截所有加了controller注解的
public class CommonExceptionAdvice {

    /**
     * 异常处理,拦截加了controller中的抛出的lycontroller 这个方法会作用在添加了@Requestmappingz注解的方法上
     * @param exception
     * @return
     */
    @ExceptionHandler(LyException.class)
    public ResponseEntity<ExceptionResult> handleException(LyException exception){
        ExceptionEnum exceptionEnum = exception.getExceptionEnum();
        //抛出异常就返回这个ResponseEntity.没有异常就返回controller自己的的那个
        return ResponseEntity.status(exceptionEnum.getCode()).body(new ExceptionResult(exceptionEnum));
    }
}
