package com.leyou.api;


import com.leyou.pojo.Category;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 *@Description categoryApi
 *@author apdoer
 *@CreateDate 2019/3/22-22:20
 *@Version 1.0
 *===============================
**/
@RequestMapping("category")
public interface CategoryApi {

    /**
     * 根据id查询商品分类
     * @param ids
     */
    @GetMapping("list/ids")
    List<Category> queryCategoryByIds(@RequestParam("ids") List<Long> ids);

}
