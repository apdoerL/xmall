package com.leyou.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;
import java.util.List;
import java.util.function.LongFunction;

/**
 *@Description Spu standard product unit 标准产品单位,一组具有相同属性的商品集
 *@author apdoer
 *@CreateDate 2019/3/20-21:52
 *@Version 1.0
 *===============================
**/
@Data
@Table(name = "tb_spu")
public class Spu {
    @Id
    @KeySql(useGeneratedKeys=true)
    private Long id;
    private Long brandId;//品牌
    private Long cid1;//一级类目
    private Long cid2;//二级类目
    private Long cid3;//三级类目
    private String title;//标题
    private String subTitle;//子标题
    private Boolean saleable;//是否上架
    private Boolean valid;//是否有效,逻辑删除用
    private Date createTime;//创建时间

    @JsonIgnore//返回页面时忽略此字段
    private Date lastUpdateTime;//最后修改时间

    //本来是要写一个Vo的,为了不对对象转来转去,就不用了
    @Transient
    private String cname; //分类
    @Transient
    private String bname; //品牌

    @Transient
    private List<Sku> skus;//做sku新增的时候传过来数据包含一组sku 数据库无关

    @Transient
    private SpuDetail spuDetail;//做sku新增的时候数据包含spudetail 数据库无关
}
