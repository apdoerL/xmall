package com.leyou.common.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 *@Description 自定义异常枚举,定义成常量不如定义成枚举  枚举:固定实例个数的类
 *@author apdoer
 *@CreateDate 2019/3/19-15:10
 *@Version 1.0
 *===============================
**/
@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum ExceptionEnum {

    //枚举  每一个都是一个具体的实例  private static final exceptionEnum = new ExceptionEnum(code,msg);的简写
    //枚举之间用分号隔开,
    PRICE_CONNOT_BE_NULL(400,"价格不能为空"),
    CATEGORY_NOT_FOUND(404,"商品分类没查到"),
    BRANDS_NOT_FOUND(404,"品牌信息未查到"),
    BRAND_SAVE_ERROR(500,"新增品牌失败"),
    FILE_UPLOAD_ERROR(500,"文件上传失败"),
    INVALID_FILE_TYPE(400,"文件类型无效"),
    SPEC_GROUP_NOT_FOUND(404,"商品规格参数组未查到"),
    SPEC_PARAM_NOT_FOUND(404,"商品规格参数未查到"),
    GOODS_NOT_FOUND(404,"商品不存在"),
    GOODS_SAVE_ERROR(500,"商品新增失败"),
    SPU_DETAIL_NOT_FOUND(404,"商品详情未查到"),
    GOODS_STOCK_EMPTY(404,"商品库存不足"),
    GOODS_UPDATE_ERROR(500,"商品修改失败"),
    GOODS_ID_CAN_NOT_BE_EMPTY(400,"商品id不能为空"),
    GOODS_SKU_NOT_FOUND(404,"商品SKU未查到"),
    INVALID_USER_DATA_TYPE(400,"用户数据无效"),
    INVALID_VALID_CODE(400,"无效的验证码"),
    USER_NOT_FOUND(404,"用户不存在"),
    INVALID_USERNAME_PASSWORD(400,"无效的用户名或密码"),
    CREATE_TOKEN_ERROR(500,"用户凭证生成失败"),
    UNAUTHORIZED(403,"权限不足"),
    CART_NOT_FOUND(404,"购物车不存在"),
    CREATE_ORDER_ERROR(500,"创建订单失败"),
    STOCK_NOT_ENOUGH(500,"库存不足"),
    ORDER_NOT_FOUND(404,"订单不存在"),
    ORDER_DETAIL_NOT_FOUND(404,"订单详情不存在"),
    ORDER_STATUS_NOT_FOUND(404,"订单状态不存在"),
    WX_PAY_ORDER_FAIL(500,"微信下单失败"),
    ORDER_STATUS_ERROR(500,"订单状态异常")



    //注意格式下面的分号
    ;
    private Integer code;//状态码
    private String msg;//异常信息

}
