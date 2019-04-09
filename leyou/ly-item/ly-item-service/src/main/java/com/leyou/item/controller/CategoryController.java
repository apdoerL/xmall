package com.leyou.item.controller;

import com.leyou.item.service.CategoryService;
import com.leyou.pojo.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Description 商品分类
 * @Author apdoer
 * @Date 2019/3/19 16:34
 * @Version 1.0
 */
@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    /**
     * 根据父类目查所有类目
     * @param pid 父类目
     * @return
     */
    @GetMapping("/list")
    public ResponseEntity<List<Category>> getCategoryByPid(@RequestParam("pid") Long pid){
        return ResponseEntity.ok(categoryService.getCategoryByPid(pid));
    }

    /**
     * 根据spuids查询分类
     * @param ids
     * @return
     */
    @GetMapping("/list/ids")
    public ResponseEntity<List<Category>> queryCategoryByIds(@RequestParam List<Long> ids){
        return ResponseEntity.ok(categoryService.selCategoryByIds(ids));
    }
}
