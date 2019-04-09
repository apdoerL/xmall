package com.leyou.dto;

import lombok.Data;

/**
 * @Description 参数对象
 * @Author apdoer
 * @Date 2019/3/19 18:57
 * @Version 1.0
 */
@Data
public class ParamDo {
    private Integer page;//当前页
    private Integer rows;//数量
    private String key;//关键词
}
