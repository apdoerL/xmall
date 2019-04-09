package com.leyou.item.mapper;

import com.leyou.pojo.Stock;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import tk.mybatis.mapper.additional.idlist.IdListMapper;
import tk.mybatis.mapper.additional.insert.InsertListMapper;
import tk.mybatis.mapper.common.Mapper;


/**
 *@Description 库存 必须导入tk.mybatis.mapper.additional.insert.InsertListMapper这个包
 *@author apdoer
 *@CreateDate 2019/3/21-17:48
 *@Version 1.0
 *===============================
**/
public interface StockMapper extends Mapper<Stock>, IdListMapper<Stock, Long>, InsertListMapper<Stock> {


    @Update("update tb_stock set stock=stock-#{num} where sku_id=#{id} and stock>=#{num}")
    Integer decreaceStock(@Param("id") Long skuId, @Param("num") Integer num);
}
