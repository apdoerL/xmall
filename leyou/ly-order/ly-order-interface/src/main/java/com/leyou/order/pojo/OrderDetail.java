package com.leyou.order.pojo;

import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @Author: 98050
 * @Time: 2018-10-27 16:34
 * @Feature: 订单详情信息实体类
 */
@Table(name = "tb_order_detail")
@Data
public class OrderDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long orderId;//订单id

    private Long skuId;//商品id

    private Integer num;//商品购买数量

    private String title;//商品标题

    private Long price;//商品单价

    private String ownSpec;//商品规格数据

    private String image;//图片


}
