package com.leyou.search.client;


import com.leyou.api.CategoryApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 *@Description 调用接口服务的client
 *@author apdoer
 *@CreateDate 2019/3/22-21:59
 *@Version 1.0
 *===============================
**/
@FeignClient("item-service")//指定要调用的服务
public interface CategoryClient extends CategoryApi {

}
