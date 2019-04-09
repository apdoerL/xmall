package com.leyou.order.pojo;


import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @Author: 98050
 * @Time: 2018-10-30 23:08
 * @Feature: 收货地址
 */
@Table(name = "tb_address")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;//用户id

    private String name;//收货人姓名

    private String phone;//手机号码

    private String zipCode;


    private String state;


    private String city;

    private String district;


    private String address;


    private Boolean defaultAddress;


    private String label;


}
