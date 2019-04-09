package com.leyou.item.controller;

import com.leyou.common.enums.ExceptionEnum;
import com.leyou.common.exception.LyException;
import com.leyou.common.vo.PageResult;
import com.leyou.dto.BrandParamDo;
import com.leyou.item.service.BrandService;
import com.leyou.pojo.Brand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Description 品牌
 * @Author apdoer
 * @Date 2019/3/19 19:15
 * @Version 1.0
 */
@RestController
@RequestMapping("/brand")
public class BrandController {
    @Autowired
    private BrandService brandService;

    /**
     * 按条件查询品牌
     * @param brandParamDo
     * @return
     */
    @GetMapping("/page")
    public ResponseEntity<PageResult<Brand>> selBrands(BrandParamDo brandParamDo){
        PageResult<Brand> brandPageResult = brandService.selBrands(brandParamDo);
        if (brandPageResult==null || brandPageResult.getItems().size()==0){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(brandPageResult);
    }

    /**
     * 新增品牌
     * @param brand
     * @param cids
     * 无返回值的ResponseEntity里面写Void 大写的,"类"
     * @return
     */
    @PostMapping
    public ResponseEntity<Void> saveBrand(Brand brand,@RequestParam("cids") List<Long>cids){
        brandService.saveBrand(brand,cids);
        //新增成功就是Created 里面就是新增成功,如果有返回体就是body,没有就build行了
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 根据分类id查询 该分类下的所有品牌
     * @param cid
     * @return
     */
    @GetMapping("/cid/{cid}")
    public ResponseEntity<List<Brand>> selBrandsByCid(@PathVariable Long cid){
        return ResponseEntity.ok(brandService.selBrandsByCid(cid));
    }

    /**
     * 根据spuid查询品牌
     * @param id
     * @return
     */
    @GetMapping("{id}")
    public ResponseEntity<Brand> queryBrandById(@PathVariable("id") Long id){
        return ResponseEntity.ok(brandService.selBrandById(id));
    }
    /**
     * 根据id集合批量查询brand品牌
     * @param ids
     * @return
     */
    @GetMapping("brands")
    public ResponseEntity<List<Brand>> queryBrandByIds(@RequestParam("ids")List<Long>ids){
        return ResponseEntity.ok(brandService.queryBrandsByIds(ids));
    }
}
