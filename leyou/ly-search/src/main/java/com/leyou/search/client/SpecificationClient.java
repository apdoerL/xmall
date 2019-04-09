package com.leyou.search.client;

import com.leyou.api.SpecificationApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 *@Description 调用item-servic服务的SpecificationApi
 *@author apdoer
 *@CreateDate 2019/3/22-22:26
 *@Version 1.0
 *===============================
**/
@FeignClient("item-service")
public interface SpecificationClient extends SpecificationApi {
}
