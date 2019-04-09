package com.leyou.page.mq;

import com.leyou.page.service.PageService;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *@Description 商品增删改时静态页的数据同步问题
 *@author apdoer
 *@CreateDate 2019/3/25-20:58
 *@Version 1.0
 *===============================
**/
@Component
public class PageListener {
    @Autowired
    private PageService pageService;

    /**
     * 商品新增或者修改静态页数据的同步
     * @param spuId
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "page.item.insert.queue",durable = "true"),
            exchange = @Exchange(name = "ly.page.change",type = ExchangeTypes.TOPIC),
            key = {"item.insert","item.update"} //低于elasticsearch来说.做的都是一样
    ))
    public void listenInsertOrUpdate(Long spuId){
        if (spuId==null){
            return;
        }
        //处理消息,创建静态页
        pageService.createHtml(spuId);
    }


    /**
     * 商品删除时同步静态页数据
     * @param spuId
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "page.item.delete.queue",durable = "true"),
            exchange = @Exchange(name = "ly.page.change",type = ExchangeTypes.TOPIC),
            key = {"page.delete"}

    ))
    public void listenDelete(Long spuId){
        if (spuId==null){
            return;
        }
        //处理消息,删除创建页
        pageService.deleteHtml(spuId);
    }
}
