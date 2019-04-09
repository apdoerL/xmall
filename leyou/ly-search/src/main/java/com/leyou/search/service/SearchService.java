package com.leyou.search.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.leyou.common.enums.ExceptionEnum;
import com.leyou.common.exception.LyException;
import com.leyou.common.utils.JsonUtils;
import com.leyou.common.utils.NumberUtils;
import com.leyou.common.vo.PageResult;
import com.leyou.pojo.*;
import com.leyou.search.client.BrandClient;
import com.leyou.search.client.CategoryClient;
import com.leyou.search.client.GoodsClient;
import com.leyou.search.client.SpecificationClient;
import com.leyou.search.pojo.Goods;
import com.leyou.search.pojo.SearchRequest;
import com.leyou.search.pojo.SearchResult;
import com.leyou.search.repository.GoodSRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.LongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilter;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 *@Description 索引服务
 *@author apdoer
 *@CreateDate 2019/3/22-22:45
 *@Version 1.0
 *===============================
**/
@Service
@Slf4j
public class SearchService {
    @Autowired
    private GoodsClient goodsClient;
    @Autowired
    private CategoryClient categoryClient;
    @Autowired
    private BrandClient brandClient;
    @Autowired
    private SpecificationClient specificationClient;
    @Autowired
    private GoodSRepository repository;
    @Autowired
    private ElasticsearchTemplate template;

    /**
     * 创建索引对象
     * @param spu
     * @return
     */
    public Goods BuildGoods(Spu spu){
        //获取搜索字段 所有搜索条件
        String msg = buildMsg(spu);
        // 获取该spu下面所有sku的价格集合
        Set<Long> skusPriceList = getSkusPriceList(spu);
        //获取该spu下所有sku的数据json格式
        String skusJson = getSkusJson(spu);
        //获取所有可搜索的规格参数
        Map<String, Object> specs = getSpecs(spu);
        //构建Goods索引对象
        Goods goods = new Goods();
        goods.setBrandId(spu.getBrandId());
        goods.setCid1(spu.getCid1());
        goods.setCid2(spu.getCid2());
        goods.setCid3(spu.getCid3());
        goods.setCreateTime(spu.getCreateTime());
        goods.setId(spu.getId());
        goods.setMsg(msg);//  搜索字段,包含标题,分类,品牌,规格等,
        goods.setPrice(skusPriceList);//该spu下所有sku的价格集合
        goods.setSkus(skusJson);// 该spu下所有sku的数据json格式
        goods.setSpecs(specs);//所有可搜索的规格参数
        goods.setSubTitle(spu.getSubTitle());
        return goods;
    }

    /**
     * 获取所有可搜索的规格参数
     * @param spu
     * @return
     */
    private Map<String,Object> getSpecs(Spu spu) {
        //查询商品所属的规格参数 查询能用来搜索的规格参数 这里组id设为null,通过三级id查,作为搜索条件的
        List<SpecParam> specParams = specificationClient.selSpecParamsByParams(null, spu.getCid3(), true);
        if (CollectionUtils.isEmpty(specParams)){
            throw new LyException(ExceptionEnum.SPEC_PARAM_NOT_FOUND);
        }
        //查询商品详情
        SpuDetail spuDetail = goodsClient.querySpuDetailBySpuId(spu.getId());
        //获取通用规格参数 这里都是json格式的字符串,不好把值取出来,所以转成map,当然用vo也是可以的
        Map<Long, String> genericSpec = JsonUtils.parseMap(spuDetail.getGenericSpec(), Long.class, String.class);
        //获取特有规格参数
        Map<Long, List<String>> specialSpec = JsonUtils.nativeRead(spuDetail.getSpecialSpec(), new TypeReference<Map<Long, List<String>>>() {
        });
        //包含所有的规格参数,key是规格参数的name(specParams).值是规格参数的值(generic,specialSpec)
        Map<String,Object> specs = new HashMap<>();
        for (SpecParam specParam : specParams) {
            String key = specParam.getName();
            Object value = "";
            //判断是否是通用规格参数
            if (specParam.getGeneric()){
                value = genericSpec.get(specParam.getId());
                //在查询条件中数字类型是分段存在的,不是单个匹对,这里就需要数据库设计匹配es,存入数据的时候存段,不能存具体的值
                if (specParam.getNumeric()){
                    value = chooseSegment(value.toString(),specParam);//如果是数字,就处理成段
                }
            }else {
                value = specialSpec.get(specParam.getId());
            }
            //存入map
            specs.put(key,value);
        }
        return specs;
    }


    /**
     * 将数据分段
     * @param value
     * @param p
     * @return
     */
    private String chooseSegment(String value, SpecParam p) {
        double val = NumberUtils.toDouble(value);
        String result = "其它";
        // 保存数值段
        for (String segment : p.getSegments().split(",")) {
            String[] segs = segment.split("-");
            // 获取数值范围
            double begin = NumberUtils.toDouble(segs[0]);
            double end = Double.MAX_VALUE;
            if(segs.length == 2){
                end = NumberUtils.toDouble(segs[1]);
            }
            // 判断是否在范围内
            if(val >= begin && val < end){
                if(segs.length == 1){
                    result = segs[0] + p.getUnit() + "以上";
                }else if(begin == 0){
                    result = segs[1] + p.getUnit() + "以下";
                }else{
                    result = segment + p.getUnit();
                }
                break;
            }
        }
        return result;
    }


    /**
     * 获取该spu下面所有的sku数据的json格式
     * @param spu
     * @return
     */
    private String getSkusJson(Spu spu) {
        //查询所有sku
        List<Sku> skus = goodsClient.querySkusySkuId(spu.getId());
        if (CollectionUtils.isEmpty(skus)){
            throw new LyException(ExceptionEnum.GOODS_SKU_NOT_FOUND);
        }
        //对sku进行处理
        List<Map<String,Object>> skuList = new ArrayList<>();
        for (Sku sku : skus) {
            HashMap<String, Object> map = new HashMap<>();
            //在json中对象和map的存储是一样的 map可以看做可变对象 或者写一个vo对象包含id,title,price.image四个属性
            map.put("id",sku.getId());
            map.put("title",sku.getTitle());
            map.put("price",sku.getPrice());
            map.put("image",StringUtils.substringBefore(sku.getImages(),","));
            skuList.add(map);
        }
        return JsonUtils.serialize(skuList);
    }

    /**
     * 获取该spu下面的所有sku价格集合
     * @param spu
     * @return
     */
    private Set<Long> getSkusPriceList(Spu spu) {
        //查询所有sku
        List<Sku> skus = goodsClient.querySkusySkuId(spu.getId());
        if (CollectionUtils.isEmpty(skus)){
            throw new LyException(ExceptionEnum.GOODS_SKU_NOT_FOUND);
        }
        //
        Set<Long> priceSet = skus.stream().map(Sku::getPrice).collect(Collectors.toSet());
        return priceSet;
    }

    /**
     * 获取所有搜索条件 用于分词的内容,包含分类,标题,品牌
     * @param spu
     * @return
     */
    private String buildMsg(Spu spu) {
        //查询分类 只需要分类 只需要分类的名称
        List<Category> categories = categoryClient.queryCategoryByIds(Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3()));
        if (CollectionUtils.isEmpty(categories)){
            throw new LyException(ExceptionEnum.CATEGORY_NOT_FOUND);
        }
        List<String> CategoryNames = categories.stream().map(Category::getName).collect(Collectors.toList());

        //查询品牌
        Brand brand = brandClient.queryBrandById(spu.getBrandId());
        if (brand==null){
            throw new LyException(ExceptionEnum.BRANDS_NOT_FOUND);
        }
        String brandName = brand.getName();
        //查询标题
        String title = spu.getTitle();
        return StringUtils.join(CategoryNames,"")+brandName+title;
    }

    /**
     * 全局搜索功能
     * @param request
     * @return
     */
    public PageResult<Goods> search(SearchRequest request) {
        Integer page = request.getPage();
        Integer size = request.getSize();
        //创建查询构建器
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();

        //0.结果过滤
        queryBuilder.withSourceFilter(new FetchSourceFilter(new String[]{"id","subTitle","skus"},null));
        //1.分页
        queryBuilder.withPageable(PageRequest.of(page,size));
        //2.搜索条件
        QueryBuilder basicQuery = QueryBuilders.matchQuery("msg",request.getKey());
        if (request.getFilter()!=null){
            basicQuery = buildBasicQuery(request);//基本查询条件
        }
        queryBuilder.withQuery(basicQuery);

        //3. 聚合分类和品牌的信息
        //3.1聚合分类
        String categoryAggName = "category_agg";//聚合名
        queryBuilder.addAggregation(AggregationBuilders.terms(categoryAggName).field("cid3"));//对cid3进行聚合,三级类目
        //3.2品牌聚合
        String brandAggName = "brand_agg";
        queryBuilder.addAggregation(AggregationBuilders.terms(brandAggName).field("brandId"));//聚合品牌id

        //4.查询
        AggregatedPage<Goods> result = template.queryForPage(queryBuilder.build(), Goods.class);


        Page<Goods> goods = repository.search(queryBuilder.build());
        //5.解析结果
        //5.1解析分页结果
        long total = goods.getTotalElements();
        int totalPage = goods.getTotalPages();
        List<Goods> goodsList = goods.getContent();
        //5.2解析聚合结果
        Aggregations aggregations = result.getAggregations();
        //处理分类和品牌聚合结果
        List<Category> categories = parseCategoryAgg(aggregations.get(categoryAggName));
        List<Brand> brands = parseBrandAgg(aggregations.get(brandAggName));

        //6 完成规格参数聚合
        List<Map<String,Object>>specs = null;
        //6.1判断商品分类不为空且数量为1,进行规格参数聚合
        if (categories!=null && categories.size()==1){
            specs = buildSpecificationAgg(categories.get(0).getId(), basicQuery);
        }
        return new SearchResult(total, totalPage,goodsList,categories,brands,specs);
    }

    /**
     * 构建基本查询条件  做查询过滤必须用bool,不然导致文本域中boost值受影响
     * @param request
     * @return
     */
    private QueryBuilder buildBasicQuery(SearchRequest request) {
        //创建布尔查询
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        //查询条件
        boolQueryBuilder.must(QueryBuilders.matchQuery("msg",request.getKey()));
        //过滤条件
        Map<String, String> filter = request.getFilter();
            for (Map.Entry<String, String> entry : filter.entrySet()) {
                String key = entry.getKey();
                //处理key  品牌分类是一种,其他是一种
                if (!"cid3".equals(key) && !"brandId".equals(key)){
                    key = "specs."+key+".keyword";
                }
                boolQueryBuilder.filter(QueryBuilders.termQuery(key,entry.getValue()));
            }

        return boolQueryBuilder;
    }

    /**
     * 进行商品规格参数聚合
     * @param id
     * @param basicQuery
     * @return
     */
    private List<Map<String, Object>> buildSpecificationAgg(Long id, QueryBuilder basicQuery) {
        List<Map<String, Object>> specs = new ArrayList<>();
        //1.查询需要聚合的规格参数
        List<SpecParam> specParams = specificationClient.selSpecParamsByParams(null, id, true);
        //2.完成聚合
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        // 2.1 带上查询条件
        queryBuilder.withQuery(basicQuery);
        // 2.2 聚合
        for (SpecParam param:specParams){
            String name = param.getName();
            queryBuilder.addAggregation(AggregationBuilders.terms(name).field("specs."+name+".keyword"));
        }
        //3.获取结果
        AggregatedPage<Goods> result = template.queryForPage(queryBuilder.build(), Goods.class);
        //4.解析结果
        Aggregations aggs = result.getAggregations();
        for (SpecParam param : specParams) {
            String name = param.getName();
            StringTerms terms = aggs.get(name);//这里已经确定是String类型,所以直接用Stringteerms
            List<String> options = terms.getBuckets()
                    .stream().map(bucket -> bucket.getKeyAsString()).collect(Collectors.toList());
            //准备map
            Map<String,Object> map = new HashMap<>();
            map.put("k",name);
            map.put("options",options);
            specs.add(map);
        }
        return specs;
    }

    /**
     * 解析分类聚合结果
     * @param terms
     * @return
     */
    private List<Category> parseCategoryAgg(LongTerms terms) {
        //获取
        try {
            List<Long> cid3s = terms.getBuckets()
                    .stream().map(b -> b.getKeyAsNumber().longValue()).collect(Collectors.toList());
        List<Category> categories = categoryClient.queryCategoryByIds(cid3s);
        return categories;
        }catch (Exception e){
            log.error("【搜索服务异常】"+e);
            return null;
        }
    }

    /**
     * 解析品牌聚合结果
     * @param terms
     * @return
     */
    private List<Brand> parseBrandAgg(LongTerms terms) {
        try {
            List<Long> brandIds = terms.getBuckets()
                    .stream().map(b -> b.getKeyAsNumber().longValue()).collect(Collectors.toList());
            return brandClient.queryBrandByIds(brandIds);
        }catch (Exception e){
            log.error("【搜索服务异常】"+e);
            return null;
        }
    }

    /**
     * 商品新增或者修改时更新索引
     *              这里可能产生异常,但是spring里默认了ack,出现异常自己会回滚
     * @param spuId
     */
    public void createOrUpdateIndex(Long spuId) {
        Spu spu = goodsClient.querySpuBySpuId(spuId);
        //构建goods对象
        Goods goods = BuildGoods(spu);
        //存入索引库
        repository.save(goods);
    }


    /**
     * 商品删除时更新索引
     * @param spuId
     */
    public void deleteIndex(Long spuId) {
        //处理消息,对索引库进行更新和
        repository.deleteById(spuId);
    }
}
