package com.leyou.api;

import com.leyou.pojo.Brand;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @Description DOTO
 * @Author Administrator
 * @Date 2019/3/2222:21
 * @Version 1.0
 */
public interface BrandApi {
    /**
     * 根据spuid查询品牌
     * @param id
     * @return
     */
    @GetMapping("brand/{id}")
    Brand queryBrandById(@PathVariable("id") Long id);

    /**
     * 根据id集合批量查询brand品牌
     * @param ids
     * @return
     */
    @GetMapping("brand/brands")
    List<Brand> queryBrandByIds(@RequestParam("ids")List<Long>ids);
}
