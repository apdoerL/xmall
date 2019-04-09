package com.leyou.seckill.vo;

import com.leyou.auth.pojo.UserInfo;
import com.leyou.pojo.SeckillGoods;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *@Description TODO
 *@author apdoer
 *@CreateDate 2019/3/31-19:52
 *@Version 1.0
 *===============================
**/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SeckillMessage {
    private UserInfo userInfo;//用户信息
    private SeckillGoods seckillGoods;//秒杀商品
}
