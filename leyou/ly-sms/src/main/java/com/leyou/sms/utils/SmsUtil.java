package com.leyou.sms.utils;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.leyou.sms.config.SmsProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 *@Description 阿里提供的demo抽取成的工具类
 *@author apdoer
 *@CreateDate 2019/3/26-10:09
 *@Version 1.0
 *===============================
**/
@EnableConfigurationProperties(SmsProperties.class)
@Component
@Slf4j
public class SmsUtil {
    @Autowired
    private SmsProperties prop;//注入配置文件

    @Autowired
    private StringRedisTemplate template;

    //产品名称:云通信短信API产品,开发者无需替换
    static final String product = "Dysmsapi";
    //产品域名,开发者无需替换
    static final String domain = "dysmsapi.aliyuncs.com";
    //定义一个key的前缀(为了和redis中其他数据产生干扰) 这个也可以写到配置文件
    private static final String KEY_PREFIX = "sms:phone:";
    //定义一个时间长度
    private static final Long SMS_MIN_INTERVAL_IN_MILLS = 60*1000L;

    /**
     * 发送短信
     * @param phone 接收者手机号
     * @param signName  签名
     * @param templateCode   短信模板
     * @param templateParam  短信模板参数
     * @return
     * @throws ClientException
     */
    public SendSmsResponse sendSms(String phone,String signName,String templateCode,String templateParam)  {
        // TODO 手机号码限流
        String key = KEY_PREFIX+phone;
        //读取时间
        String lastTime = template.opsForValue().get(key);//上一次的时间
        if (StringUtils.isNotBlank(lastTime)){
            Long last = Long.valueOf(lastTime);
            if (System.currentTimeMillis()-last < SMS_MIN_INTERVAL_IN_MILLS){
                log.info("【短信服务】 发送短信频率过高,被拦截,手机号码:{}",phone);
                //跟上次发送小于一分钟,直接return
                return null;
            }
        }

        try {
            //可自助调整超时时间
            System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
            System.setProperty("sun.net.client.defaultReadTimeout", "10000");

            //初始化acsClient,暂不支持region化
            IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", prop.getAccessKeyId(), prop.getAccessKeySecret());
            DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
            IAcsClient acsClient = new DefaultAcsClient(profile);

            //组装请求对象-具体描述见控制台-文档部分内容
            SendSmsRequest request = new SendSmsRequest();
            request.setMethod(MethodType.POST);
            //必填:待发送手机号
            request.setPhoneNumbers(phone);
            //必填:短信签名-可在短信控制台中找到
            request.setSignName(signName);
            //必填:短信模板-可在短信控制台中找到
            request.setTemplateCode(templateCode);
            //可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
            request.setTemplateParam(templateParam);

            //选填-上行短信扩展码(无特殊需求用户请忽略此字段)
            //request.setSmsUpExtendCode("90997");

            //可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者
            request.setOutId("123456");

            //hint 此处可能会抛出异常，注意catch
            SendSmsResponse resp = acsClient.getAcsResponse(request);

            if (!"Ok".equals(resp.getCode())) {
                log.info("发送短信手机号" + phone);
                log.info("发送短信状态：{}", resp.getCode());
                log.info("发送短信消息：{}", resp.getMessage());
            }

            // TODO 短信发送成功后 写入redis 同时指定生存时间为1分钟
            template.opsForValue().set(phone,String.valueOf(System.currentTimeMillis()),1,TimeUnit.MINUTES);

            return resp;
        }catch (Exception e){
            log.info("短信服务,发送短信失败,phone:{},原因:{}",e);
            return null;
        }
    }
}
