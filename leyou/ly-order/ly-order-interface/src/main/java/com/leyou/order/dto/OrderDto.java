package com.leyou.order.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 *@Description 页面传来的参数对象
 *@author apdoer
 *@CreateDate 2019/3/28-21:22
 *@Version 1.0
 *===============================
**/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {
    @NotNull
    private Long addressId;//收货人地址id
    @NotNull
    private Integer paymentType;//收款类型
    @NotNull
    private List<CartDto> carts;//订单详情
}
