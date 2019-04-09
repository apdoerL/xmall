package com.leyou.search.client;

import com.leyou.api.GoodsApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 *@Description 调用item服务的商品接口
 *@author apdoer
 *@CreateDate 2019/3/22-22:07
 *@Version 1.0
 *===============================
**/
@FeignClient("item-service")
public interface GoodsClient extends GoodsApi {
}
