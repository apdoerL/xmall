package com.leyou.seckill.client;

import com.leyou.order.api.OrderApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @Description DOTO
 * @Author Administrator
 * @Date 2019/3/3120:25
 * @Version 1.0
 */
@FeignClient("order-service"          /**,configuration = OrderConfig.class**/)
public interface OrderClient extends OrderApi {
}
