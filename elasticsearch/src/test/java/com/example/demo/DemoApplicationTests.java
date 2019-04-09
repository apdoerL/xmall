package com.example.demo;

import com.example.demo.com.apdoer.elasticsearch.Item;
import com.example.demo.com.apdoer.elasticsearch.ItemRepository;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilter;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoApplicationTests {

	@Autowired
	ElasticsearchTemplate template;

	@Autowired
	private ItemRepository repository;

	@Test
	public void contextLoads() {
		//创建索引库,会根据Item类的@Document注解信息来创建
		template.createIndex(Item.class);

		//配置映射关系,会根据Item类中的id,field等字段来自动完成映射
		template.putMapping(Item.class);
	}

	@Test
	public void testinsert(){
		List<Item> items = new ArrayList<>();
		items.add(new Item(4L, "华为META10", " 手机", "华为", 2299.00, "http://image.leyou.com/3.jpg"));
		items.add(new Item(5L, "华为META10", " 手机", "华为", 2299.00, "http://image.leyou.com/3.jpg"));
		items.add(new Item(6L, "华为META10", " 手机", "华为", 2299.00, "http://image.leyou.com/3.jpg"));
		repository.saveAll(items);
	}

	@Test
	public void testfind(){
		Iterable<Item> items = repository.findAll();
		for (Item item : items) {
			System.out.println(item);
		}
	}

	@Test
	public void testdel(){
		repository.deleteAll();
	}

	@Test
	public void testFindByPrice(){
		List<Item> list = repository.findByPriceBetween(2000.00, 4000.00);
		for (Item item : list) {
			//soutv可以直接 System.out.println("item = " + item);这样的效果
			System.out.println(item);
		}
	}

	/**
	 * 复杂查询
	 */
	@Test
	public void testFindByPage(){
		//创建查询构建器
		NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
		//结果过滤
		nativeSearchQueryBuilder.withSourceFilter(new FetchSourceFilter(new String[]{"id","title","price"},null));
		//添加查询条件
		nativeSearchQueryBuilder.withQuery(QueryBuilders.matchQuery("price",4499));
		//排序
		nativeSearchQueryBuilder.withSort(SortBuilders.fieldSort("price").order(SortOrder.DESC));
		//分页
		nativeSearchQueryBuilder.withPageable(PageRequest.of(0,2));//这里的分页从0开始的,不是从1开始
		//查询
		Page<Item> items = repository.search(nativeSearchQueryBuilder.build());
		for (Item item : items) {
			System.out.println(item);
		}
	}

	/**
	 * 聚合
	 */
	@Test
	public void testAgg(){
		//创建查询构建器
		NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
		String aggName = "popularBrand";
		//聚合
		queryBuilder.addAggregation(AggregationBuilders.terms(aggName).field("brand"));
		//查询并返回待聚合结果
		AggregatedPage<Item> items = template.queryForPage(queryBuilder.build(), Item.class);

		//解析聚合
		Aggregations aggregations = items.getAggregations();

		//获取指定名称的聚合
		StringTerms terms = aggregations.get(aggName);
		//获取桶
		List<StringTerms.Bucket> buckets = terms.getBuckets();

		for (StringTerms.Bucket bucket : buckets) {
			System.out.println("key="+bucket.getKey());
			System.out.println("docCount="+bucket.getDocCount());
		}
	}

}
