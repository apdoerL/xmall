package com.leyou.seckill.client;

import com.leyou.api.GoodsApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @Description DOTO
 * @Author Administrator
 * @Date 2019/3/3120:24
 * @Version 1.0
 */
@FeignClient(value = "item-service")
public interface GoodsClient extends GoodsApi {
}
