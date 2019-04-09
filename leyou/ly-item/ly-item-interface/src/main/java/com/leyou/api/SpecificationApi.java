package com.leyou.api;

import com.leyou.pojo.SpecGroup;
import com.leyou.pojo.SpecParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 *@Description 向外提供的SpecificationApi接口
 *@author apdoer
 *@CreateDate 2019/3/22-22:25
 *@Version 1.0
 *===============================
**/
public interface SpecificationApi {
    /**
     * 根据分类id查询商品规格参数组
     * @param cid
     * @return
     */
    @GetMapping("/spec/groups/{cid}")
    List<SpecGroup> selSpecGroupsByCid(@PathVariable("cid") Long cid);

    /**
     * 查询商品规格  可以根据商品组id.也可以根据分类id,或者是否作为搜索条件
     * @param gid
     * @param cid
     * @param searching
     * @return
     */
    @GetMapping("spec/params")
    List<SpecParam> selSpecParamsByParams(
            @RequestParam(value = "gid",required = false) Long gid,
            @RequestParam(value = "cid",required = false) Long cid,
            @RequestParam(value = "searching",required = false) Boolean searching);

    /**
     * 根据cid查询规格参数组及组下所有参数
     * @param cid
     * @return
     */
    @GetMapping("spec/group")
    List<SpecGroup> selGroupByCid(@RequestParam("cid") Long cid);
}
