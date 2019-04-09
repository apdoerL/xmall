package com.leyou.order.dto;

import lombok.Data;


/**
 *@Description TODO
 *@author apdoer
 *@CreateDate 2019/3/29-10:05
 *@Version 1.0
 *===============================
**/
@Data
public class AddressDto {
    private Long id;
    private String name;//收件人姓名
    private String phone;//电话
    private String state;//省份
    private String city;//城市
    private String district;//街道
    private String address;//具体地址
    private String zipCode;//邮编
    private Boolean isDefault;//
}
