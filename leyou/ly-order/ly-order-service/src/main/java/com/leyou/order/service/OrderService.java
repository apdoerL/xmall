package com.leyou.order.service;

import com.leyou.api.GoodsApi;
import com.leyou.auth.pojo.UserInfo;
import com.leyou.common.enums.ExceptionEnum;
import com.leyou.common.exception.LyException;
import com.leyou.common.utils.IdWorker;
import com.leyou.order.client.AddressClient;
import com.leyou.order.client.GoodsClient;
import com.leyou.order.dto.AddressDto;
import com.leyou.order.dto.CartDto;
import com.leyou.order.dto.OrderDto;
import com.leyou.order.enums.OrderStatusEnum;
import com.leyou.order.interceptor.UserInterceptor;
import com.leyou.order.mapper.OrderDetailMapper;
import com.leyou.order.mapper.OrderMapper;
import com.leyou.order.mapper.OrderStatusMapper;
import com.leyou.order.pojo.Order;
import com.leyou.order.pojo.OrderDetail;
import com.leyou.order.pojo.OrderStatus;
import com.leyou.order.utils.PayHelper;
import com.leyou.pojo.Sku;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.stream.Collectors;

import static com.github.wxpay.sdk.WXPayConstants.FAIL;

/**
 *@Description TODO
 *@author apdoer
 *@CreateDate 2019/3/28-21:31
 *@Version 1.0
 *===============================
**/
@Service
@Slf4j
public class OrderService {
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderDetailMapper detailMapper;
    @Autowired
    private OrderStatusMapper statusMapper;
    @Autowired
    private IdWorker idWorker;
    @Autowired
    private GoodsClient goodsClient;
    @Autowired
    private PayHelper payHelper;

    /**
     * 生成订单
     * @param orderDto
     * @return
     */
    @Transactional
    public Long createOrder(OrderDto orderDto) {
        //1.TODO 新增订单
        Order order = new Order();
        //1.1订单编号.基本信息
        long orderId = idWorker.nextId();
        order.setOrderId(orderId);
        order.setCreateTime(new Date());
        order.setPaymentType(orderDto.getPaymentType());

        //1.2用户信息
        UserInfo userInfo = UserInterceptor.getUser();
        order.setUserId(userInfo.getId());
        order.setBuyerNick(userInfo.getUsername());
        order.setBuyerRate(false);//是否评价

        //1.3收货人地址  orderDto只有收货人地址id,应该是根据id去查询该用户的
        AddressDto addressDto = AddressClient.findById(orderDto.getAddressId());
        order.setReceiver(addressDto.getName());
        order.setReceiverAddress(addressDto.getAddress());
        order.setReceiverCity(addressDto.getCity());
        order.setReceiverDistrict(addressDto.getDistrict());
        order.setReceiverMobile(addressDto.getPhone());
        order.setReceiverState(addressDto.getState());
        order.setReceiverZip(addressDto.getZipCode());

        //1.4金额 传过来的orderDto中包含了购物车Dto的集合 所以要根据购物车dto中的skuid查询对应的sku
//        List<CartDto> cartDtos = orderDto.getCarts();  这样获取不方便后面处理,这里转成一个<skuid,num>的map
        Map<Long, Integer> map = orderDto.getCarts().stream().collect(Collectors.toMap(CartDto::getSkuId, CartDto::getNum));
        Set<Long> skuids = map.keySet();//获取购物车中所有的skuid
        List<Sku> skus = goodsClient.querySkusySkuIds(new ArrayList<>(skuids));//购物车中所有的sku  集合中可以接受另一个集合,把其中的元素一一接受

        //准备orderDetail集合
        List<OrderDetail>details = new ArrayList<>();

        Long totalPay = 0L;//总金额
        for (Sku sku : skus) {
            totalPay += sku.getPrice() * map.get(sku.getId());
            //封装orderdetail
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setImage(StringUtils.substringBefore(sku.getImages(),","));
            orderDetail.setNum(map.get(sku.getId()));
            orderDetail.setOrderId(orderId);
            orderDetail.setOwnSpec(sku.getOwnSpec());
            orderDetail.setPrice(sku.getPrice());
            orderDetail.setSkuId(sku.getId());
            orderDetail.setTitle(sku.getTitle());
            details.add(orderDetail);
        }
        order.setTotalPay(totalPay);//总价格
        order.setActualPay(totalPay + order.getPostFee() - 0);// 0 是促销或者打折的减免

        // 1.5 order 写入数据库
        int count = orderMapper.insertSelective(order);
        if (count!=1){
            log.error("【创建订单】 创建订单失败,orderId:{}",orderId);
            throw new LyException(ExceptionEnum.CREATE_ORDER_ERROR);
        }
        //2. TODO 新增订单详情
        count = detailMapper.insertList(details);
        if (count!=details.size()){
            log.error("【创建订单】 创建订单失败,orderId:{}",orderId);
            throw new LyException(ExceptionEnum.CREATE_ORDER_ERROR);
        }

        //3. TODO 新增订单状态
        OrderStatus status = new OrderStatus();
        status.setCreateTime(order.getCreateTime());
        status.setOrderId(orderId);
        status.setStatus(OrderStatusEnum.UNPAY.value());
        count = statusMapper.insertSelective(status);
        if (count!=details.size()){
            log.error("【创建订单】 创建订单失败,orderId:{}",orderId);
            throw new LyException(ExceptionEnum.CREATE_ORDER_ERROR);
        }

        //4. TODO 更新库存
        goodsClient.decreaceStock(orderDto.getCarts());

        return orderId;
    }

    /**
     * 根据orderId 查询order
     * @param orderId
     * @return
     */
    public Order queryOrderById(Long orderId) {
        //查询订单
        Order order = orderMapper.selectByPrimaryKey(orderId);
        if (order==null){
            throw new LyException(ExceptionEnum.ORDER_NOT_FOUND);
        }

        //查询订单详情
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setOrderId(orderId);
        List<OrderDetail> orderDetails = detailMapper.select(orderDetail);
        if (CollectionUtils.isEmpty(orderDetails)){
            throw new LyException(ExceptionEnum.ORDER_DETAIL_NOT_FOUND);
        }
        order.setOrderDetails(orderDetails);

        //查询订单状态
        OrderStatus orderStatus = statusMapper.selectByPrimaryKey(orderId);
        if (orderStatus==null){
            throw new LyException(ExceptionEnum.ORDER_STATUS_NOT_FOUND);
        }
        order.setOrderStatus(orderStatus);
        return order;
    }

    /**
     * 创建支付链接,返回wx生成的支付链接
     * @param orderId
     * @return
     */
    public String createPayUrl(Long orderId) {
        //查询订单
        Order order = queryOrderById(orderId);

        //判断订单状态
        Integer status = order.getOrderStatus().getStatus();
        if (!status.equals(OrderStatusEnum.UNPAY.value())){
            //订单状态异常
            throw new LyException(ExceptionEnum.ORDER_STATUS_ERROR);
        }

        //订单金额
        Long actualPay = order.getActualPay();
        //商品描述
        OrderDetail orderDetail = order.getOrderDetails().get(0);
        String desc = orderDetail.getTitle();
        return payHelper.createOrder(orderId,actualPay,desc);
    }

    /**
     * 处理回调,处理微信返回支付结果后的操作
     * @param result
     */
    public void handleNotify(Map<String, String> result) {
        //判断通信标识
        String return_code = result.get("code_url");
        if (FAIL.equals(return_code)){
            //通信失败
            log.error("【微信下单】 通信失败,失败原因:{}",result.get("return_msg"));
            throw new LyException(ExceptionEnum.WX_PAY_ORDER_FAIL);
        }

        //判断通信标识
        String result_code = result.get("result_code");
        if (FAIL.equals(result_code)){
            //通信失败
            log.error("【微信下单】 业务败,错误码:{},错误原因:{}",result.get("err_code"),result.get("err_code_des"));
            throw new LyException(ExceptionEnum.WX_PAY_ORDER_FAIL);
        }
    }
}
