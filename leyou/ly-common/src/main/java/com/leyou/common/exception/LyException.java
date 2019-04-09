package com.leyou.common.exception;

import com.leyou.common.enums.ExceptionEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @Description 自定义异常,可供专门捕获
 * @Author apdoer
 * @Date 2019/3/19 15:04
 * @Version 1.0
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class LyException extends RuntimeException{

    //异常枚举
    private ExceptionEnum exceptionEnum;

}