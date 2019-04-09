package com.leyou.dto;

import lombok.Data;

/**
 * @Description
 * @Author Administrator
 * @Date 2019/3/1919:04
 * @Version 1.0
 */
@Data
public class BrandParamDo extends ParamDo {
    private String sortBy;//排序
    private boolean desc;//排序方式
}
