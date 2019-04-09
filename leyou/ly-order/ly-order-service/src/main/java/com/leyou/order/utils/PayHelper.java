package com.leyou.order.utils;

import com.github.wxpay.sdk.WXPay;
import static com.github.wxpay.sdk.WXPayConstants.*;//jdk5里面新加的,静态引入另一个类,可以直接使用该类的静态常量

import com.leyou.common.enums.ExceptionEnum;
import com.leyou.common.exception.LyException;
import com.leyou.order.config.PayConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

/**
 *@Description wx支付工具类
 *@author apdoer
 *@CreateDate 2019/3/31-13:48
 *@Version 1.0
 *===============================
**/
@Slf4j
public class PayHelper {
    @Autowired
    private WXPay wxPay;
    @Autowired
    private PayConfig payConfig;

    /**
     * 创建预交易订单,返回支付链接
     * @param orderId
     * @param totalPay
     * @param desc
     * @return
     */
    public String createOrder(Long orderId,Long totalPay,String desc){
        try {
            Map<String,String> data = new HashMap<>();
            //商品描述
            data.put("body",desc);
            //订单号
            data.put("out_trade_no",orderId.toString());
            //金额.单位是分
            data.put("total_fee",totalPay.toString());
            //调用微信支付的终端ip
            data.put("spbill_create_ip","127.0.0.1");
            //回调地址
            data.put("notify_url",payConfig.getNotifyUrl());
            // 交易类型为扫码支付
            data.put("trade_type","NATIVE");

            //利用wxpay工具,完成下单
            Map<String, String> result = wxPay.unifiedOrder(data);

            //判断通信标识
            String return_code = result.get("code_url");
            if (FAIL.equals(return_code)){
                //通信失败
                log.error("【微信下单】 通信失败,失败原因:{}",result.get("return_msg"));
                throw new LyException(ExceptionEnum.WX_PAY_ORDER_FAIL);
            }

            //判断通信标识
            String result_code = result.get("result_code");
            if (FAIL.equals(result_code)){
                //通信失败
                log.error("【微信下单】 业务败,错误码:{},错误原因:{}",result.get("err_code"),result.get("err_code_des"));
                throw new LyException(ExceptionEnum.WX_PAY_ORDER_FAIL);
            }

            //下单成功.获取支付链接
            String url = result.get("code_url");
            return url;
        }catch (Exception e){
            log.error("【微信下单】 创建预交易订单失败",e);
            return null;
        }
    }


}
