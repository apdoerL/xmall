package com.leyou.search.pojo;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;
import java.util.Set;

/**
 *@Description 商品索引
 *@author apdoer
 *@CreateDate 2019/3/22-21:03
 *@Version 1.0
 *===============================
**/
@Data
@Document(indexName = "goods",type = "docs",shards = 1,replicas = 0)
public class Goods {
    @Id
    private Long id; //spuid

    @Field(type = FieldType.Text,analyzer = "ik_max_word")
    private String msg;//所有需要被搜索的信息,包含标题,分类,甚至品牌 用户的搜索条件主要匹配这里面的 只有一个分词,可以提升效率

    @Field(type = FieldType.Keyword,index = false)//不分词,用来展示不做索引
    private String subTitle;//卖点

    private  Long brandId;//品牌id
    private Long cid1;//一级分类id
    private Long cid2;//二级分类id
    private Long cid3;//三级分类id

    private Date createTime;//spu创建时间

    private Set<Long> price;   //  ?list

    @Field(type = FieldType.Keyword,index = false)
    private  String skus;//sku的json结构 包含该spu下的skus信息,只是存到elasticsearch里,用来展示,不做索引
    private Map<String,Object> specs;//可搜索的规格参数,key是参数名,值是参数值

}
