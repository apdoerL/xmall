package com.leyou.pojo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Description 商品
 * @Author apdoer
 * @Date 2019/3/1915:34
 * @Version 1.0
 */
@Data
public class Item {
    private Integer id;
    private String name;
    private Long price;

}
