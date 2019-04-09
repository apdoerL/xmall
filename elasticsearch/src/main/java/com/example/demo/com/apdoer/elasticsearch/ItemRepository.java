package com.example.demo.com.apdoer.elasticsearch;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

/**
 *@Description 类似于通用mapper
 *@author apdoer
 *@CreateDate 2019/3/22-17:33
 *@Version 1.0
 *===============================
**/
public interface ItemRepository extends ElasticsearchRepository<Item,Long> {

    List<Item> findByPriceBetween(Double start,Double end);
}
