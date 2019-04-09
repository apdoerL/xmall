package com.leyou.cart.pojo;

import lombok.Data;

/**
 *@Description 购物车信息
 *@author apdoer
 *@CreateDate 2019/3/28-9:45
 *@Version 1.0
 *===============================
**/
@Data
public class Cart {
    private Long skuId;//商品skuid
    private String title;//
    private String image;
    private Long price;//加入购物车的价格
    private Integer num;//购买数量
    private String ownSpec;//规格参数
}
