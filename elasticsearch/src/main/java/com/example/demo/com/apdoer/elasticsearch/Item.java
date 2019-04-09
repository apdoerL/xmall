package com.example.demo.com.apdoer.elasticsearch;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(indexName = "item",type = "docs",shards = 1,replicas = 1)
public class Item {

    @Field(type = FieldType.Long)
    @Id
    Long id;

    @Field(type = FieldType.Text,analyzer = "ik_smart")
    String title; //标题

    @Field(type =FieldType.Keyword)//不用分词
    String category;// 分类
    @Field(type = FieldType.Keyword)
    String brand; // 品牌
    @Field(type = FieldType.Double)
    Double price; // 价格

    @Field(type = FieldType.Keyword,index = false)//不用分词,不用索引
    String images; // 图片地址
}