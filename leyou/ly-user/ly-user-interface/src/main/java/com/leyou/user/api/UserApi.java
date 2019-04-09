package com.leyou.user.api;

import com.leyou.user.pojo.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *@Description 用户服务向外提供调用的api
 *@author apdoer
 *@CreateDate 2019/3/26-23:23
 *@Version 1.0
 *===============================
**/
public interface UserApi {

    /**
     * 根据用户名密码查询用户
     * @param username
     * @param password
     * @return
     */
    @GetMapping("/query")
    User queryByUsernameAndPassword(@RequestParam("username") String username
            , @RequestParam("password") String password);
}
