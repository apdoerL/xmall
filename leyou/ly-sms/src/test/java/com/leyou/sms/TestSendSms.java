package com.leyou.sms;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

/**
 *@Description 短信发送测试
 *@author apdoer
 *@CreateDate 2019/3/26-14:20
 *@Version 1.0
 *===============================
**/
@RunWith(SpringRunner.class)
@SpringBootTest
public class TestSendSms {

    @Autowired
    private AmqpTemplate amqpTemplate;

    @Test
    public void testSend() throws InterruptedException {
        Map<String,String> msg = new HashMap<>();
        msg.put("phone","18797667599");
//        msg.put("phone","13227385359");
        msg.put("code","54321");

        //发送 第一个参数是交换机,第二个参数是routingkey
        amqpTemplate.convertAndSend("ly.sms.exchange","sms.verify.code",msg);

        Thread.sleep(10000L);//睡10秒,在rabbitmq可视化界面15672看到
    }
}
