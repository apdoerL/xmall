package com.leyou.item.controller;

import com.leyou.common.vo.PageResult;
import com.leyou.dto.GoodsParamDo;
import com.leyou.item.service.GoodsService;
import com.leyou.order.dto.CartDto;
import com.leyou.pojo.Sku;
import com.leyou.pojo.Spu;
import com.leyou.pojo.SpuDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 *@Description 商品
 *@author apdoer
 *@CreateDate 2019/3/20-22:18
 *@Version 1.0
 *===============================
**/
@RestController
public class GoodsController {
    @Autowired
    private GoodsService goodsService;

    /**
     * 查询商品
     * @param
     * @return
     */
    @GetMapping("spu/page")
    public ResponseEntity<PageResult<Spu>> selSpuByPage(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "rows", defaultValue = "5") Integer rows,
            @RequestParam(value = "saleable", required = false) Boolean saleable,
            @RequestParam(value = "key", required = false) String key){
        return ResponseEntity.ok(goodsService.selSpuByPage(page,rows,saleable,key));
    }

    /**
     * 新增商品
     * @param spu
     * @return
     */
    @PostMapping("/goods")
    public ResponseEntity<Void> saveGoods(@RequestBody Spu spu){
        goodsService.saveGoods(spu);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 修改商品
     * @param spu
     * @return
     */
    @PutMapping("/goods")
    public ResponseEntity<Void> updateGoods(@RequestBody Spu spu){
        goodsService.updateGoods(spu);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


    /**
     * 根据spuid查询spudetail
     * @param id
     * @return
     */
    @GetMapping("/spu/detail/{id}")
    public ResponseEntity<SpuDetail> querySpuDetailBySpuId(@PathVariable Long id){
        return ResponseEntity.ok(goodsService.querySpuDetailById(id));
    }

    /**
     * 根据spuid查询包含的所有sku
     * @param id
     * @return
     */
    @GetMapping("/sku/list")
    public ResponseEntity<List<Sku>> querySkusySkuId(@RequestParam("id") Long id){
        return ResponseEntity.ok(goodsService.querySkusySpuId(id));
    }

    /**
     *根据sku id的集合查询相关的sku
     * @param ids
     * @return
     */
    @GetMapping("/sku/list/ids")
    public ResponseEntity<List<Sku>> querySkusySkuIds(@RequestParam("ids") List<Long>ids){
        return ResponseEntity.ok(goodsService.querySkusySkuIds(ids));
    }


    /**
     * 根据spuid查询spu spudetail,sku
     * @param id
     * @return
     */
    @GetMapping("/spu/{id}")
    public ResponseEntity<Spu> querySpuBySpuId(@PathVariable("id") Long id){
        return ResponseEntity.ok(goodsService.querySkuById(id));
    }

    /**
     * 减库存   将传入的购物车中对应sku库存减少
     * @param cartDtos
     * @return
     */
    @PostMapping("stock/decrease")
    public ResponseEntity<Void> decreaceStock(@RequestBody List<CartDto> cartDtos){
        goodsService.decreaceStock(cartDtos);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
