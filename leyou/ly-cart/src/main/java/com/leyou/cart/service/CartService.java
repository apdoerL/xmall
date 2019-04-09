package com.leyou.cart.service;

import com.leyou.auth.pojo.UserInfo;
import com.leyou.cart.interceptor.UserInterceptor;
import com.leyou.cart.pojo.Cart;
import com.leyou.common.enums.ExceptionEnum;
import com.leyou.common.exception.LyException;
import com.leyou.common.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 *@Description TODO
 *@author apdoer
 *@CreateDate 2019/3/28-9:50
 *@Version 1.0
 *===============================
**/
public class CartService {
    @Autowired
    private StringRedisTemplate redisTemplate;

    private static final String KEY_PREFIX = "cart:uer:id:";
    /**
     * 加入购物车
     * @param cart
     */
    public void addCart(Cart cart) {
        //判断当前购物车是否存在
        UserInfo user = UserInterceptor.getUser();
        //判断当前商品是否存在
        String key = KEY_PREFIX + user.getId();
        String hashKey = cart.getSkuId().toString();
//        redisTemplate.opsForHash()          这个方法每次都需要传key hash 可以用bound方法
        BoundHashOperations<String, Object, Object> operation = redisTemplate.boundHashOps(key);
        // 记录需要添加的商品的数量
        Integer num = cart.getNum();
        if (operation.hasKey(hashKey)) {
            //从redis中取出的购物车数据,redis存进去和取出来的都是String
            String cartJson = operation.get(hashKey).toString();
            // 覆盖传入的cart
            cart = JsonUtils.parse(cartJson, Cart.class);
            cart.setNum(cart.getNum()+num);//缓存的数量加传入的数量
        }
        //再存入redis中
        operation.put(hashKey,JsonUtils.serialize(cart));
    }

    /**
     * 查询购物车中数据
     * @return
     */
    public List<Cart> queryCart() {
        //获取登录用户
        UserInfo user = UserInterceptor.getUser();
        String key = KEY_PREFIX + user.getId();
        if (!redisTemplate.hasKey(key)){
            throw new LyException(ExceptionEnum.CART_NOT_FOUND);
        }
        //获取登录用户的所有购物车信息
        BoundHashOperations<String, Object, Object> operations = redisTemplate.boundHashOps(key);
        //values 方法获取map的所有value
        List<Cart> carts = operations.values().stream().map(o -> JsonUtils.parse(o.toString(), Cart.class)).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(carts)){
            throw new LyException(ExceptionEnum.CART_NOT_FOUND);
        }
        return carts;
    }


    /**
     * 修改购物车的商品数量
     * @param skuId
     * @param num
     */
    public void updateCart(Long skuId, Integer num) {
        //获取当前用户
        UserInfo user = UserInterceptor.getUser();
        String key = KEY_PREFIX + user.getId();//外层键
        String hashKey = skuId.toString();//内层键
        //虽然理论上肯定是存在的,还是判断一下
        if (!redisTemplate.hasKey(key)){
            throw new LyException(ExceptionEnum.CART_NOT_FOUND);
        }
        BoundHashOperations<String, Object, Object> operations = redisTemplate.boundHashOps(key);
        //redis 存储取出始终是要转换成String的
        Cart cart = JsonUtils.parse(operations.get(hashKey).toString(), Cart.class);
        cart.setNum(num);
        //放回redis中
        operations.put(hashKey,JsonUtils.serialize(cart));
    }

    /**
     * 根据skuid删除购物车中指定sku
     * @param skuId
     */
    public void deleteCart(Long skuId) {
        //获取当前用户
        UserInfo user = UserInterceptor.getUser();
        String key = KEY_PREFIX + user.getId();//外层键
        String hashKey = skuId.toString();//内层键
        //删除redis中对应内容
        redisTemplate.opsForHash().delete(key,hashKey);//删除的时候这个方法比绑定又要好些
    }
}
