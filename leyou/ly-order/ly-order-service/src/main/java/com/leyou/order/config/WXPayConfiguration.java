package com.leyou.order.config;


import com.github.wxpay.sdk.WXPay;
import com.github.wxpay.sdk.WXPayConstants;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Description 微信支付
 * @Author Administrator
 * @Date 2019/3/3113:41
 * @Version 1.0
 */
@Configuration
public class WXPayConfiguration {


    @Bean
    @ConfigurationProperties(prefix = "ly.pay") //这个方法加上方法上也可以注入进来
    public PayConfig payConfig(){
        return new PayConfig();
    }

    @Bean
    public WXPay wxPay(PayConfig payConfig){
        return new WXPay(payConfig,WXPayConstants.SignType.HMACSHA256);
    }
}
