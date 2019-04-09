package com.leyou.pojo;

import lombok.Data;

/**
 *@Description 秒杀商品
 *@author apdoer
 *@CreateDate 2019/3/31-19:52
 *@Version 1.0
 *===============================
**/
@Data
public class SeckillGoods extends Sku{
    private Boolean isSeckill;//是否是秒杀商品
}
