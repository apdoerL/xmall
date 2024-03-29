package com.leyou.cart.controller;

import com.leyou.cart.pojo.Cart;
import com.leyou.cart.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;

/**
 *@Description TODO
 *@author apdoer
 *@CreateDate 2019/3/28-9:48
 *@Version 1.0
 *===============================
**/
@RestController
public class CartController {
    @Autowired
    private CartService cartService;
    /**
     * 新增购物车,不需要返回值
     * @param cart
     * @return
     */
    @PostMapping()
    public ResponseEntity<Void> addCart(@RequestBody Cart cart){
        cartService.addCart(cart);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 获取购物车信息
     * @return
     */
    @GetMapping("list")
    public ResponseEntity<List<Cart>> queryCart(){
        return ResponseEntity.ok(cartService.queryCart());
    }

    /**
     * 更新购物车信息
     * @param skuId
     * @param num 修改后的购买数量
     * @return
     */
    @PutMapping
    public ResponseEntity<Void> updateCart(@RequestParam ("id")Long skuId,@RequestParam("num")Integer num){
        cartService.updateCart(skuId,num);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


    /**
     * 删除购物车,通过skuid操作,和清空购物车有区别
     * @param skuId
     * @return
     */
    @DeleteMapping("/{skuId}")
    public ResponseEntity<Void> deleteCart(@PathVariable("skuId") Long skuId){
        cartService.deleteCart(skuId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
