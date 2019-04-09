package com.leyou.search.repository;

import com.leyou.common.vo.PageResult;
import com.leyou.dto.GoodsParamDo;
import com.leyou.pojo.Spu;
import com.leyou.search.client.GoodsClient;
import com.leyou.search.pojo.Goods;
import com.leyou.search.service.SearchService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.stream.Collectors;


@RunWith(SpringRunner.class)
@SpringBootTest
public class GoodsRepositoryTest {
    @Autowired
    private GoodSRepository goodsRepository;

    @Autowired
    private ElasticsearchTemplate template;

    @Autowired
    private GoodsClient goodsClient;

    @Autowired
    private SearchService searchService;

    @Test
    public void testCreateIndex() {
        template.createIndex(Goods.class);
        template.putMapping(Goods.class);
    }
    @Test
    public void print(){
        System.out.println("123");
    }

    @Test
    public void loadData() {
        int page = 1;
        int size = 0;
        int rows = 100;
        do {
            // 查询spu信息
            PageResult<Spu> result = goodsClient.selSpuByPage(page, rows, true, null);
            List<Spu> spuList = result.getItems();
            // 构建成goods
            List<Goods> goodList = spuList.stream()
                    .map(searchService::BuildGoods).collect(Collectors.toList());
            size = spuList.size();
            this.goodsRepository.saveAll(goodList);
            page++;
        } while (size == 100);
    }
}