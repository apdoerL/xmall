package com.leyou.common.mapper;


import tk.mybatis.mapper.additional.idlist.IdListMapper;
import tk.mybatis.mapper.additional.insert.InsertListMapper;//必须用这个新增
import tk.mybatis.mapper.annotation.RegisterMapper;
import tk.mybatis.mapper.common.Mapper;

/**
 *@Description 自定义通用mapper,继承了多个通用mapper,拥有的方法更多
 *@author apdoer
 *@CreateDate 2019/3/21-18:45
 *@Version 1.0
 *===============================
**/
@RegisterMapper//自定义mapper生效
public interface BaseMapper<T> extends Mapper<T> ,IdListMapper<T,Long>,InsertListMapper<T> {
}
