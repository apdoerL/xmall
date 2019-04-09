package com.leyou.order.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 *@Description TODO
 *@author apdoer
 *@CreateDate 2019/3/28-21:25
 *@Version 1.0
 *===============================
**/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartDto {

    private Long skuId;//商品skuid

    private Integer num;//购买数量
}
