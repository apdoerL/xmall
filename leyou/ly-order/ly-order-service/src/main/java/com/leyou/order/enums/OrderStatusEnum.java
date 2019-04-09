package com.leyou.order.enums;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public enum OrderStatusEnum {
    UNPAY(1,"未付款"),
    PAYED(2,"已付款"),
    DELIVERED(3,"已发货,未确认"),
    SUCCESS(4,"已确认,未评价"),
    CLOSED(5,"已关闭,交易失败"),
    RATED(6,"已评价")

    ;
    private Integer code;
    private String description;

    /**
     * 既能获取状态码,又能有提示
     * @return
     */
    public Integer value(){
        return this.code;
    }

}
