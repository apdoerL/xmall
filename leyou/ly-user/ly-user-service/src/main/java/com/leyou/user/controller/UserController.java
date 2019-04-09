package com.leyou.user.controller;

import com.leyou.common.exception.LyException;
import com.leyou.user.pojo.User;
import com.leyou.user.service.UserService;
import io.micronaut.http.annotation.Get;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.stream.Collectors;

/**
 * @Description DOTO
 * @Author Administrator
 * @Date 2019/3/2616:12
 * @Version 1.0
 */
@RestController
public class UserController {
    @Autowired
    private UserService userService;

    /**
     * 参数校验 根据接口文档
     *          路径 /check/{data}/{type}
     *          参数 data(String)  type(Integer)
     *          参数均无默认值
     * @param data
     * @param type
     * @return
     */
    @GetMapping("/check/{data}/{type}")
    public ResponseEntity<Boolean> checkData(@PathVariable("data") String data, @PathVariable("type") Integer type){
        //因为是get请求返回ok
        return ResponseEntity.ok(userService.checkData(data,type));
    }

    /**
     * 发送短信获取验证码
     * @param phone 手机号
     * @return
     */
    @PostMapping("code")
    public ResponseEntity<Void> getValidCode(@RequestParam("phone") String phone){
        userService.getValidCode(phone);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();//返回204 nocontent无返回值
    }

    /**
     * 用户注册
     * @param user  @Valid对user进行后端校验,如果不需要springmvc对验证异常返回,在验证的参数后面紧接BindingResult result
     * @param code
     * @return
     */
    @PostMapping("register")
    public ResponseEntity<Void> register(@Valid User user, BindingResult result, @RequestParam("code") String code){
        // TODO 自定义验证验证条件有误  错误消息用 | 隔开
        if (result.hasErrors()) {
            throw new RuntimeException(result.getFieldErrors()
                    .stream().map(e->e.getDefaultMessage()).collect(Collectors.joining("|")));
        }
        userService.register(user,code);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 根据用户名密码查询用户
     * @param username
     * @param password
     * @return
     */
    @GetMapping("/query")
    public ResponseEntity<User> queryByUsernameAndPassword(@RequestParam("username") String username
            ,@RequestParam("password") String password){
        return ResponseEntity.ok(userService.queryByUsernameAndPassword(username,password));
    }
}
