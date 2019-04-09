package com.leyou.search.pojo;

import com.leyou.common.vo.PageResult;
import com.leyou.pojo.Brand;
import com.leyou.pojo.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import sun.nio.cs.ext.IBM037;

import java.util.List;
import java.util.Map;

/**
 *@Description 在原来的分页返回对象上加了两个字段,category 和brand 只是搜索服务需要,所以不放在common中
 *@author apdoer
 *@CreateDate 2019/3/24-12:54
 *@Version 1.0
 *===============================
**/
@Data
public class SearchResult extends PageResult<Goods> {

    private List<Category> categories;//分类待选项

    private List<Brand> brands;//品牌待选项

    private List<Map<String,Object>> specs;//规格参数过滤条件 key :待选项


    /**
     * 一般要保留空参数构造,有些操作,是基于反射调用空参构造的
     *
     */
    public SearchResult() {
    }

    /**
     * 提供了这样的一个构造方法  需要分页的条件   当构造方法参数很多的时候,可以利用工厂模式
     * @param total
     * @param totalPage
     * @param items
     * @param categories
     * @param brands
     */
    public SearchResult(Long total, Integer totalPage, List<Goods> items, List<Category> categories, List<Brand> brands) {
        super(total, totalPage, items);
        this.categories = categories;
        this.brands = brands;
    }


    public SearchResult(Long total, Integer totalPage, List<Goods> items, List<Category> categories, List<Brand> brands, List<Map<String,Object>>specs) {
        super(total, totalPage, items);
        this.categories = categories;
        this.brands = brands;
        this.specs = specs;
    }
}