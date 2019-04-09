package com.leyou.search.mq;

import com.leyou.search.service.SearchService;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.xml.ws.soap.Addressing;

/**
 *@Description 监听商品微服务的消息
 *@author apdoer
 *@CreateDate 2019/3/25-20:31
 *@Version 1.0
 *===============================
**/
@Component
public class ItemListener {
    @Autowired
    private SearchService searchService;

    /**
     * 监听新增和修改,新增和修改在elasticsearch里是一样的,所以一起处理
     *  默认durable 持久化
     * @param spuId
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "search.item.insertOrUpdate",durable = "true"),//search服务下的商品新增或者修改的队列,可随意
            exchange = @Exchange(name = "ly.item.exchange",type = ExchangeTypes.TOPIC),//和接收方的指定交换机保持一致
            key = {"item.insert","item.update"}
    ))
    public void listenInsertOrUpdate(Long spuId){
        if(spuId==null){
            return ;
        }
        //处理消息,对索引库进行新增和修改
        searchService.createOrUpdateIndex(spuId);
    }

    /**
     * 监听新增和修改,新增和修改在elasticsearch里是一样的,所以一起处理
     * 默认持久化
     * @param spuId
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "search.item.delete.queue",durable = "true"),//
            exchange = @Exchange(name = "ly.item.exchange",type = ExchangeTypes.TOPIC),//和接收方的指定交换机保持一致
            key = {"item.delete"}
    ))
    public void listenDelete(Long spuId){
        if(spuId==null){
            return ;
        }
        //处理消息,对索引库进行新增和修改
        searchService.deleteIndex(spuId);
    }
}
