package com.leyou.auth.client;

import com.leyou.user.api.UserApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 *@Description 通过feign调用用户服务的接口
 *@author apdoer
 *@CreateDate 2019/3/26-23:26
 *@Version 1.0
 *===============================
**/
@FeignClient("user-service")
public interface UserClient extends UserApi {
}
