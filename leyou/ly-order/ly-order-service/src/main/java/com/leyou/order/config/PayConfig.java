package com.leyou.order.config;

import com.github.wxpay.sdk.WXPayConfig;
import lombok.Data;

import java.io.InputStream;

/**
 *@Description 微信支付
 *@author apdoer
 *@CreateDate 2019/3/31-13:12
 *@Version 1.0
 *===============================
**/
@Data
public class PayConfig implements WXPayConfig {

    private String appID;//公共账号id
    private String mchID;//商户id
    private String key;//生成签名的密匙
    private int httpConnectTimeoutMs;//连接超时时长
    private int httpReadTimeoutMs;//读取超时时间
    private String notifyUrl ;//下单通知回调地址


    @Override
    public InputStream getCertStream() {
        return null;
    }
}
