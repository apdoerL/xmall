package com.leyou.api;

import com.leyou.common.vo.PageResult;
import com.leyou.dto.GoodsParamDo;
import com.leyou.order.dto.CartDto;
import com.leyou.pojo.Sku;
import com.leyou.pojo.Spu;
import com.leyou.pojo.SpuDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 *@Description 向外提供被调用接口 有接口提供方维护接口
 *@author apdoer
 *@CreateDate 2019/3/22-22:17
 *@Version 1.0
 *===============================
**/
public interface GoodsApi {
    /**
     * 根据spuid查询spudetail
     * @param id
     * @return
     */
    @GetMapping("/spu/detail/{id}")
    SpuDetail querySpuDetailBySpuId(@PathVariable("id") Long id);

    /**
     * 根据spuid查询包含的所有sku
     * @param id
     * @return
     */
    @GetMapping("/sku/list")
    List<Sku> querySkusySkuId(@RequestParam("id") Long id);
    /**
     * 查询商品
     * @param
     * @return
     */
    @GetMapping("spu/page")
    PageResult<Spu> selSpuByPage(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "rows", defaultValue = "5") Integer rows,
            @RequestParam(value = "saleable", required = false) Boolean saleable,
            @RequestParam(value = "key", required = false) String key);


    /**
     * 根据spuid查询spu
     * @param id
     * @return
     */
    @GetMapping("/spu/{id}")
    Spu querySpuBySpuId(@PathVariable("id") Long id);

    /**
     *根据sku id的集合查询相关的sku
     * @param ids
     * @return
     */
    @GetMapping("/sku/list/ids")
    List<Sku> querySkusySkuIds(@RequestParam("ids") List<Long>ids);

    /**
     * 减库存   将传入的购物车中对应sku库存减少
     * @param cartDtos
     * @return
     */
    @PostMapping("stock/decrease")
    void decreaceStock(@RequestBody List<CartDto> cartDtos);
}
