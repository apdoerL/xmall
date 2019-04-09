package com.leyou.order.controller;

import com.leyou.order.dto.OrderDto;
import com.leyou.order.pojo.Order;
import com.leyou.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.annotation.Repeatable;

/**
 *@Description TODO
 *@author apdoer
 *@CreateDate 2019/3/28-21:31
 *@Version 1.0
 *===============================
**/
@RestController
@RequestMapping("order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    /**
     * 订单生成   返回订单编号
     * @param orderDto
     * @return
     */
    @PostMapping
    public ResponseEntity<Long> createOrder(@RequestBody OrderDto orderDto){
        //创建订单
        return ResponseEntity.ok(orderService.createOrder(orderDto));
    }

    /**
     * 根据订单id 查询订单
     * @param orderId
     * @return
     */
    @GetMapping("{id}")
    public ResponseEntity<Order> queryOrderById(@PathVariable("id")Long orderId){
        return ResponseEntity.ok(orderService.queryOrderById(orderId));
    }

    /**
     * 创建支付链接,返回微信生成的支付链接
     * @param orderId
     * @return
     */
    @GetMapping("/url/{id}")
    public ResponseEntity<String> createPayUrl(@PathVariable("id") Long orderId){
        return ResponseEntity.ok(orderService.createPayUrl(orderId));
    }
}
