package com.leyou.seckill.controller;

import com.leyou.auth.pojo.UserInfo;
import com.leyou.pojo.SeckillGoods;
import com.leyou.seckill.annotation.AccessLimit;
import com.leyou.seckill.interceptor.LoginInterceptor;
import com.leyou.seckill.service.SeckillService;
import com.leyou.seckill.vo.SeckillMessage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description DOTO
 * @Author Administrator
 * @Date 2019/3/3120:29
 * @Version 1.0
 */
@RestController
public class SeckillController implements InitializingBean {
    @Autowired
    private SeckillService seckillService;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    private static final String KEY_PREFIX = "leyou:seckill:stock";

    private Map<Long,Boolean> localOverMap = new HashMap<>();

    /**
     * 系统初始化,初始化秒杀数量
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        //查询可以秒杀的商品
        BoundHashOperations<String, Object, Object> operations = stringRedisTemplate.boundHashOps(KEY_PREFIX);
        if (operations.hasKey(KEY_PREFIX)){
            operations.entries().forEach((m,n)->localOverMap.put(Long.parseLong(m.toString()),false));
        }
    }

    /**
     * 秒杀商品
     * @param path 秒杀路径
     * @param seckillGoods 秒杀的商品
     * @return
     */
    public ResponseEntity<String> seckillOrder(@PathVariable("path")String path, @RequestBody SeckillGoods seckillGoods){
        String result = "排队中";
        //获取当前用户
        UserInfo userInfo = LoginInterceptor.getLoginUser();
        //1.验证路径
        boolean check = this.seckillService.checkSeckillPath(seckillGoods.getId(),userInfo.getId(),path);
        if (!check){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        // 2.内存标记,减少redis访问
        Boolean over = localOverMap.get(seckillGoods.getId());
        if (over){
            return ResponseEntity.ok(result);
        }
        //3.读取库存,减一后更新库存
        BoundHashOperations<String,Object,Object> hashOperations = this.stringRedisTemplate.boundHashOps(KEY_PREFIX);
        Long stock = hashOperations.increment(seckillGoods.getId().toString(), -1);
        //4.库存不足直接返回
        if (stock < 0){
            localOverMap.put(seckillGoods.getId(),true);
            return ResponseEntity.ok(result);
        }
        // 库存充足,请求入队
        //获取用户信息
        SeckillMessage seckillMessage = new SeckillMessage(userInfo,seckillGoods);

        //发送秒杀信息
        this.seckillService.sendMessage(seckillMessage);
        return ResponseEntity.ok(result);
    }


    /**
     * 创建秒杀路径
     * @param goodsId  秒杀商品的id
     * @return
     */
    @AccessLimit(seconds = 20,maxCount = 5,needLogin = true)
    @GetMapping("get_path/{goodsId}")
    public ResponseEntity<String>getSeckillPath(@PathVariable("goodsId") Long goodsId){
        UserInfo userInfo = LoginInterceptor.getLoginUser();
        //如果没有登录,返回为认证
        if (userInfo == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        //创建秒杀路径
        String path = this.seckillService.createPath(goodsId,userInfo.getId());
        if (StringUtils.isEmpty(path)){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(path);
    }

    /**
     * 根据userId查询秒杀订单号
     * @param userId
     * @return
     */
    @GetMapping("orderId")
    public ResponseEntity<Long> checkSeckillOrder(Long userId){
        Long result = this.seckillService.checkSeckillOrder(userId);
        if (result == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(result);

    }

}

