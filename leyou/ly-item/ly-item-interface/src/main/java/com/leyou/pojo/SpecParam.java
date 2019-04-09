package com.leyou.pojo;

import lombok.Data;

import javax.persistence.*;

/**
 *@Description 商品参数模板
 *@author apdoer
 *@CreateDate 2019/3/20-19:33
 *@Version 1.0
 *===============================
**/
@Data
@Table(name="tb_spec_param")
public class SpecParam {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long cid;//商品类目
    private Long groupId;//商品模板参数组id
    private String name;

    @Column(name = "`numeric`")//numeric在sql中是关键字需要转义
    private Boolean numeric;//是否是数字
    private String unit;//如果是数字,就要填单位,比如手机重量152g
    private Boolean generic;
    private Boolean searching;//是否作为搜索条件
    private String segments;//如果需要搜索,添加范围 比如售价的价格1000-2000

}
