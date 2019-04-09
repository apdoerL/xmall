package com.leyou.order.client;


import com.leyou.api.GoodsApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 *@Description TODO
 *@author apdoer
 *@CreateDate 2019/3/29-11:16
 *@Version 1.0
 *===============================
**/
@FeignClient("item-service")
public interface GoodsClient extends GoodsApi {
}
