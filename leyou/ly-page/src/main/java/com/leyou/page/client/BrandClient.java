package com.leyou.page.client;

import com.leyou.api.BrandApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 *@Description 调用brandapi服务
 *@author apdoer
 *@CreateDate 2019/3/22-22:22
 *@Version 1.0
 *===============================
**/
@FeignClient("item-service")
public interface BrandClient extends BrandApi {
}
