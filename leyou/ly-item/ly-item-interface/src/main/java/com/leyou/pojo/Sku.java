package com.leyou.pojo;

import com.sun.org.apache.xpath.internal.operations.Bool;
import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.math.BigDecimal;
import java.util.Date;

/**
 *@Description Sku 商品库存单位,精确到具体的商品
 *@author apdoer
 *@CreateDate 2019/3/21-17:26
 *@Version 1.0
 *===============================
**/
@Data
@Table(name = "tb_sku")
public class Sku {
    @Id
    @KeySql(useGeneratedKeys = true)
    private Long id;
    private Long spuId;//对应的spuid
    private String title;
    private String images;
    private Long price;
    private String ownSpec;//商品特殊规格的键值对
    private String indexes;
    private Boolean enable;//是否有效
    private Date createTime;
    private Date lastUpdateTime;

    @Transient
    private Integer stock;//库存

    //前端传过来是一个大的json格式,有stock字段,所以后端,但是数据库表没有该字段

}
