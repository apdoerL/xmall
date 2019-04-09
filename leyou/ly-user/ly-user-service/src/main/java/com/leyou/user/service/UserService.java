package com.leyou.user.service;

import com.leyou.common.enums.ExceptionEnum;
import com.leyou.common.exception.LyException;
import com.leyou.common.utils.NumberUtils;
import com.leyou.user.mapper.UserMapper;
import com.leyou.user.pojo.User;
import com.leyou.user.utils.MD5Encode;
import org.apache.commons.lang.StringUtils;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 *@Description TODO
 *@author apdoer
 *@CreateDate 2019/3/26-16:19
 *@Version 1.0
 *===============================
**/
@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private AmqpTemplate amqpTemplate;
    @Autowired
    private StringRedisTemplate redisTemplate;

    //验证码保存到redis中key的前缀
    private static final String KEY_PREFIX = "user:valid:phone:";
    /**
     * 参数校验
     * @param data
     * @param type
     * @return
     */
    public Boolean checkData(String data, Integer type) {
        User record = new User();
        //判断数据类型
        switch (type){
            case 1:
                record.setUsername(data);
                break;
            case 2:
                record.setPhone(data);
                break;
            default:
                throw new LyException(ExceptionEnum.INVALID_USER_DATA_TYPE);
        }
        //if true return true这种写法太low了
        return userMapper.selectCount(record)==0;//数据库没有就可用
    }


    /**
     * 发送短信获取验证码
     * @param phone
     */
    public void getValidCode(String phone) {
        //生成保存到redis中的key
        String key = KEY_PREFIX+phone;
        // TODO 生成验证码
        String code = NumberUtils.generateCode(6);
        Map<String,String> msg = new HashMap<>();
        msg.put("phone",phone);
        msg.put("code",code);
        //发送验证码到rabbitmq消息队列  这里应该抽取成变量放到配置文件中
        amqpTemplate.convertAndSend("ly.sms.exchange","sms.verify.code",msg);

        //保存验证码  5分钟有效 5分钟也要抽取出来
        redisTemplate.opsForValue().set(key,code,5,TimeUnit.MINUTES);
    }

    /**
     * 用户注册
     * @param user
     * @param code 验证码
     */
    public void register(User user, String code) {
        //验证码验证
        String cacheCode = redisTemplate.opsForValue().get(KEY_PREFIX + user.getPhone());
        if(!StringUtils.equals(code,cacheCode)){
            throw new LyException(ExceptionEnum.INVALID_VALID_CODE);
        }
        //加密密码
        String salt = MD5Encode.generateSalt();
        user.setSalt(salt);
        user.setPassword(MD5Encode.encryptPassword(user.getUsername(),user.getPassword(),salt));

        //写入数据库
        user.setCreated(new Date());
        userMapper.insert(user);
    }

    /**
     * 根据用户名密码查询用户
     * @param username
     * @param password
     * @return
     */
    public User queryByUsernameAndPassword(String username, String password) {
        User record = new User();
        record.setUsername(username);//根据用户名查 因为数据库里username加了索引,登录操作频繁,查出来再比对密码
        //用查询一个的方法
        User user = userMapper.selectOne(record);
        if (user ==null){
            throw new LyException(ExceptionEnum.INVALID_USERNAME_PASSWORD);
        }

        //检验密码
        if(!StringUtils.equals(MD5Encode.encryptPassword(username,password,user.getSalt()),user.getPassword())){
            throw new LyException(ExceptionEnum.INVALID_USERNAME_PASSWORD);
        }
        return user;
    }
}
