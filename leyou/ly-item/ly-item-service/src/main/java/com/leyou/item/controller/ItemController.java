package com.leyou.item.controller;

import com.leyou.item.service.ItemService;
import com.leyou.pojo.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description 商品控制器
 * @Author apdoer
 * @Date 2019/3/1914:54
 * @Version 1.0
 */
@RestController
@RequestMapping("/item")
public class ItemController {

    @Autowired
    private ItemService itemService;


    public ResponseEntity<Item> saveItem(Item item){
        if (item==null){
            //抛出自定义异常
        }
        item = itemService.saveItem(item);
        return null;
    }
}
