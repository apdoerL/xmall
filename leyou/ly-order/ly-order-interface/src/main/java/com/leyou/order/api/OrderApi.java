package com.leyou.order.api;

import com.leyou.order.pojo.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @Description DOTO
 * @Author Administrator
 * @Date 2019/3/3120:26
 * @Version 1.0
 */
public interface OrderApi {

    /**
     * 创建订单
     * @param seck
     * @param order
     * @return
     */
    @PostMapping
    List<Long> createOrder(@RequestParam("seck") String seck, @RequestBody @Valid Order order);


    /**
     * 修改订单状态
     * @param id
     * @param status
     * @return
     */
    @PutMapping("{id}/{status}")
    Boolean updateOrderStatus(@PathVariable("id") Long id, @PathVariable("status") Integer status);
}
