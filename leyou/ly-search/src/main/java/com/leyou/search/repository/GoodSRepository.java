package com.leyou.search.repository;

import com.leyou.search.pojo.Goods;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 *@Description 通过这个对elasticsearch操作
 *@author apdoer
 *@CreateDate 2019/3/22-22:30
 *@Version 1.0
 *===============================
**/
public interface GoodSRepository extends ElasticsearchRepository<Goods,Long> {
}
