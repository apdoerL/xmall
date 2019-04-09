package com.leyou.pojo;

import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 *@Description 库存
 *@author apdoer
 *@CreateDate 2019/3/21-17:37
 *@Version 1.0
 *===============================
**/
@Data
@Table(name = "tb_stock")
public class Stock {
    @Id
    private Long skuId;//具体的商品id 确定了不能自增
    private Integer seckillStock;//可秒杀库存
    private Integer seckillTotal;//已秒杀数量
    private Integer stock;//库存
}
