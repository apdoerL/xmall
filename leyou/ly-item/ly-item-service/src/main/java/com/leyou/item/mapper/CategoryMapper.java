package com.leyou.item.mapper;

import com.leyou.pojo.Category;
import tk.mybatis.mapper.additional.idlist.IdListMapper;
import tk.mybatis.mapper.common.Mapper;

/**
 * @Description 通用mapper里包含了各种方法
 * IdListMapper<T,PK>里面包含用idlist查的方法
 * @Author apdoer
 * @Date 2019/3/19 16:32
 * @Version 1.0
 */
public interface CategoryMapper extends Mapper<Category>,IdListMapper<Category,Long> {

}
