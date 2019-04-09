package com.leyou.pojo;

import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 *@Description Spu的详情
 *@author apdoer
 *@CreateDate 2019/3/20-22:13
 *@Version 1.0
 *===============================
**/
@Data
@Table(name = "tb_spu_detail")
public class SpuDetail {
    @Id
    private Long spuId;//对应的spuid  跟对应的spu绑定,所以没有不自动加主键
    
    private String description;//商品描述
    private String specialSpec;//商品特殊规格的名称及可选模板值
    private String genericSpec;//商品的全局规格属性
    private String packingList;//商品的包装清单
    private String afterService;//商品的售后服务
}
