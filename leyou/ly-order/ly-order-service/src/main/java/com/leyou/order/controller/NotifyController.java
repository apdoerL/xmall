package com.leyou.order.controller;

import com.leyou.order.pojo.Order;
import com.leyou.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 *@Description 提供给wx的外网访问接口,利用内网穿透,拦截器放行这个控制器
 *@author apdoer
 *@CreateDate 2019/3/31-15:03
 *@Version 1.0
 *===============================
**/
@RestController
@RequestMapping("notify")
public class NotifyController {
    @Autowired
    private OrderService orderService;


    /**
     * 接收到微信支付的返回信息,返回给微信的响应,如果没有响应,微信会在3个多小时内不断重试,仍无结果,认为失败
     *              微信发送和接受都用的xml格式
     *              所以用map和jackson包中的转换器转换
     * @param result
     * @return
     */
    @GetMapping(value = "pay",produces = "application/xml")
    public Map<String,String> returnStatusToWx(@RequestBody Map<String,String>result){
        //处理回调
        orderService.handleNotify(result);

        Map<String,String> msg = new HashMap<>();
        msg.put("return_code","SUCCESS");
        msg.put("return_msg","OK");
        return msg;
    }



}
