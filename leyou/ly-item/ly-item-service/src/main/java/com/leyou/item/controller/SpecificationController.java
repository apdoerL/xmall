package com.leyou.item.controller;

import com.leyou.common.enums.ExceptionEnum;
import com.leyou.common.exception.LyException;
import com.leyou.item.service.SpecificationService;
import com.leyou.pojo.SpecGroup;
import com.leyou.pojo.SpecParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 *@Description 商品规格参数/组
 *@author apdoer
 *@CreateDate 2019/3/20-19:48
 *@Version 1.0
 *===============================
**/
@RestController
@RequestMapping("/spec")
public class SpecificationController {
    @Autowired
    private SpecificationService specificationService;

    /**
     * 根据分类id查询商品规格参数组
     * @param cid
     * @return
     */
    @GetMapping("/groups/{cid}")
        public ResponseEntity<List<SpecGroup>> selSpecGroupsByCid(@PathVariable Long cid){
        List<SpecGroup> specGroups = specificationService.selSpecGroupsByCid(cid);
        if (CollectionUtils.isEmpty(specGroups)){
            throw new LyException(ExceptionEnum.SPEC_GROUP_NOT_FOUND);
        }
        return ResponseEntity.ok(specGroups);
    }

    /**
     * 查询商品规格  可以根据商品组id.也可以根据分类id,或者搜索条件
     * @param gid
     * @param cid
     * @param searching
     * @return
     */
    @GetMapping("/params")
    public ResponseEntity<List<SpecParam>> selSpecParamsByParams(
            @RequestParam(value = "gid",required = false) Long gid,
            @RequestParam(value = "cid",required = false) Long cid,
            @RequestParam(value = "searching",required = false) Boolean searching){
        List<SpecParam> specParams = specificationService.selSpecParamsByParams(gid,cid,searching);
        if (CollectionUtils.isEmpty(specParams)){
            throw new LyException(ExceptionEnum.SPEC_PARAM_NOT_FOUND);
        }
        return ResponseEntity.ok(specParams);
    }

    /**
     * 新增商品规格参数
     * @param specParam
     * @return
     */
    @PostMapping("/param")
    public ResponseEntity<Void> saveSpecParam(SpecParam specParam){
        specificationService.saveSpecParam(specParam);
        return null;
    }

    /**
     * 根据分类查询规格组及组内参数
     * @param cid
     * @return
     */
    @GetMapping("spec/group")
    public ResponseEntity<List<SpecGroup>> selGroupByCid(@RequestParam("cid") Long cid){
        return ResponseEntity.ok(specificationService.queryListByCid(cid));
    }
}
