package com.leyou.item.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.leyou.common.enums.ExceptionEnum;
import com.leyou.common.exception.LyException;
import com.leyou.common.vo.PageResult;
import com.leyou.dto.GoodsParamDo;
import com.leyou.item.mapper.SkuMapper;
import com.leyou.item.mapper.SpuDetailMapper;
import com.leyou.item.mapper.SpuMapper;

import com.leyou.item.mapper.StockMapper;
import com.leyou.order.dto.CartDto;
import com.leyou.pojo.*;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.apache.commons.lang.StringUtils;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import javax.persistence.Transient;
import java.time.Year;
import java.util.*;
import java.util.stream.Collectors;

/**
 *@Description 商品
 *@author apdoer
 *@CreateDate 2019/3/20-22:16
 *@Version 1.0
 *===============================
**/
@Service
public class GoodsService {
    @Autowired
    private SpuMapper spuMapper;
    @Autowired
    private SpuDetailMapper spuDetailMapper;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private BrandService brandService;
    @Autowired
    private SkuMapper skuMapper;
    @Autowired
    private StockMapper stockMapper;
    @Autowired
    private AmqpTemplate amqpTemplate;

    /**
     * 按条件分页查询所有商品
     * @param
     * @return
     */
    public PageResult<Spu> selSpuByPage(Integer page, Integer rows, Boolean saleable,String key) {
        //分页
        PageHelper.startPage(page,rows);
        //过滤
        Example example = new Example(Spu.class);
        //搜索字段过滤
        if (StringUtils.isNotBlank(key)){
            example.createCriteria().andLike("title","%"+key+"%");
        }
        //上下架过滤
        if (saleable!=null){
            example.createCriteria().andEqualTo("saleable",saleable);
        }
        //默认排序
        example.setOrderByClause("last_update_time DESC");
        //查询
        List<Spu> spus = spuMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(spus)){
            throw new LyException(ExceptionEnum.GOODS_NOT_FOUND);
        }
        //处理categoryname 和brand name
        loadCategoryAndBrandName(spus);
        PageInfo<Spu> pageInfo = new PageInfo<>(spus);
        return new PageResult<>(pageInfo.getTotal(), spus);
    }

    private void loadCategoryAndBrandName(List<Spu> spus){
        for (Spu spu : spus) {
            //处理分类名称 这里要查询 一级/二级/三级
//            categoryService.selCategoryByIds(Arrays.asList(spu.getCid1(),spu.getCid2(),spu.getCid3()))
//                    .stream().map(category -> category.getName()).collect(Collectors.toList());
            //上面等价于下面的写法
            List<String> names = categoryService.selCategoryByIds(Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3()))
                    .stream().map(Category::getName).collect(Collectors.toList());
            spu.setCname(StringUtils.join(names, "/"));//转成xx/xx/xx的格式
            //处理品牌分类
            spu.setBname(brandService.selBrandById(spu.getBrandId()).getName());
        }
    }

    /**
     * 新增商品
     * @param spu
     */
    @Transactional//增加事务
    public void saveGoods(Spu spu) {
        //新增spu
        saveSpu(spu);
        //新增spudetail
        saveSpuDetail(spu);
        //新增sku和库存
        saveSkuAndStock(spu);

    }

    /**
     * 新增sku
     * @param spu
     */
    @Transactional
    void saveSkuAndStock(Spu spu) {
        List<Sku> skus = spu.getSkus();
        //定义一个库存的集合,等下批量新增
        List<Stock>stocks = new ArrayList<>();
        for (Sku sku : skus) {
            sku.setCreateTime(new Date());
            sku.setSpuId(spu.getId());
            sku.setLastUpdateTime(sku.getCreateTime());
            //skuid 是自增长的所有不设
            int count = skuMapper.insert(sku);
            if (count!=1){
                throw new LyException(ExceptionEnum.GOODS_SAVE_ERROR);
            }
            //秒杀不在新增里
            Stock stock = new Stock();
            stock.setSkuId(sku.getId());
            stock.setStock(sku.getStock());
            stocks.add(stock);
        }
        //批量新增   批量新增不返回id,批量新增sku还要再拿出id 反而麻烦
        int count = stockMapper.insertList(stocks);
        //这里返回值是所有操作成功的和(count!=stocks.size())
        if (count!=stocks.size()){
            throw new LyException(ExceptionEnum.GOODS_SAVE_ERROR);
        }
    }



    /**
     * 新增spuDetail
     * @param spu
     */
    private void saveSpuDetail(Spu spu) {
        SpuDetail spuDetail = spu.getSpuDetail();
        spuDetail.setSpuId(spu.getId());
        int count = spuDetailMapper.insert(spuDetail);
        if (count!=1){
            throw new LyException(ExceptionEnum.GOODS_SAVE_ERROR);
        }
        // TODO 发送mq消息
        amqpTemplate.convertAndSend("item.update",spu.getId());
    }

    /**
     * 新增spu
     * @param spu
     */
    private void saveSpu(Spu spu) {
        //设置页面没有传过来的参数
        spu.setId(null);
        spu.setCreateTime(new Date());
        spu.setLastUpdateTime(spu.getCreateTime());
        spu.setSaleable(true);//是否上架
        spu.setValid(true);//是否有效
        int count = spuMapper.insert(spu);
        if (count!=1){
            throw new LyException(ExceptionEnum.GOODS_SAVE_ERROR);
        }
        // TODO 发送mq消息
        amqpTemplate.convertAndSend("item.insert",spu.getId());
    }

    /**
     * 根据 spuid 查询spudetail
     * @param id
     * @return
     */
    public SpuDetail querySpuDetailById(Long id) {
        SpuDetail spuDetail = spuDetailMapper.selectByPrimaryKey(id);
        if (spuDetail==null){
            throw new LyException(ExceptionEnum.SPU_DETAIL_NOT_FOUND);
        }
        return spuDetail;
    }

    /**
     * 根据spuid查询相关的所有sku,包含库存
     * @param spuId
     * @return
     */
    public List<Sku> querySkusySpuId(Long spuId) {
        //查询sku
        Sku sku = new Sku();
        sku.setSpuId(spuId);
        List<Sku> skus = skuMapper.select(sku);
        if (CollectionUtils.isEmpty(skus)){
            throw new LyException(ExceptionEnum.GOODS_NOT_FOUND);
        }
        //查询库存
        List<Long> ids = skus.stream().map(Sku::getId).collect(Collectors.toList());
        List<Stock> stocks = stockMapper.selectByIdList(ids);
        if (CollectionUtils.isEmpty(stocks)){
            throw new LyException(ExceptionEnum.GOODS_STOCK_EMPTY);
        }
        //把stock变成一个map,key是skuid 值是stock
        Map<Long, Integer> map = stocks.stream().collect(Collectors.toMap(Stock::getSkuId, Stock::getStock));
        skus.forEach(s->s.setStock(map.get(s.getId())));

        return skus;
    }


    /**
     * 修该商品
     * @param spu
     */
    @Transactional
    public void updateGoods(Spu spu) {
        if (spu.getId()==null){
            throw new LyException(ExceptionEnum.GOODS_ID_CAN_NOT_BE_EMPTY);
        }
        Sku sku = new Sku();
        sku.setSpuId(spu.getId());
        List<Sku> skus = skuMapper.select(sku);
        //如果不为空
        if (!CollectionUtils.isEmpty(skus)){
            //获取所有的skuid
            List<Long> ids = skus.stream().map(Sku::getId).collect(Collectors.toList());
            //删除sku 和stock
            skuMapper.deleteByIdList(ids);
            stockMapper.selectByIdList(ids);
        }
        //修改spu
        spu.setCreateTime(null);
        spu.setSaleable(null);//是否上架
        spu.setValid(null);//是否有效
        spu.setLastUpdateTime(new Date());
        int count = spuMapper.updateByPrimaryKeySelective(spu);
        if (count!=1){
            throw new LyException(ExceptionEnum.GOODS_UPDATE_ERROR);
        }
        //修改spuDetail
        count = spuDetailMapper.updateByPrimaryKeySelective(spu.getSpuDetail());
        if (count!=1){
            throw new LyException(ExceptionEnum.GOODS_UPDATE_ERROR);
        }
        //新增sku和stock
        saveSkuAndStock(spu);

        // TODO 发送mq消息
        amqpTemplate.convertAndSend("item.update",spu.getId());
    }

    /**
     * 根据spuid查询spu spudetail 下面的sku
     * @param id
     * @return
     */
    public Spu querySkuById(Long id) {
        //查询spu
        Spu spu = spuMapper.selectByPrimaryKey(id);
        if (spu==null){
            throw new LyException(ExceptionEnum.GOODS_NOT_FOUND);
        }
        //查询sku
        spu.setSkus(querySkusySpuId(id));
        //查询detail
        spu.setSpuDetail(querySpuDetailById(id));
        return spu;
    }

    /**
     * 通过sku id列表查询对应的sku 这里是为购物车查的,所以把库存也查出来
     * @param ids
     * @return
     */
    public List<Sku> querySkusySkuIds(List<Long> ids) {
        List<Sku> skus = skuMapper.selectByIdList(ids);
        if (CollectionUtils.isEmpty(skus)) {
            new LyException(ExceptionEnum.GOODS_SKU_NOT_FOUND);
        }

        //查询库存
        List<Stock> stocks = stockMapper.selectByIdList(ids);
        if (CollectionUtils.isEmpty(stocks)){
            throw new LyException(ExceptionEnum.GOODS_STOCK_EMPTY);
        }
        //把stock变成一个map,key是skuid 值是stock
        Map<Long, Integer> map = stocks.stream().collect(Collectors.toMap(Stock::getSkuId, Stock::getStock));
        skus.forEach(s->s.setStock(map.get(s.getId())));
        return skus;
    }

    /**
     * 根据传入购物车sku  减少相应的库存
     * 这里涉及到分布式事务 利用乐观锁,在数据库加条件
     * TODO 有问题:购物车有一件失败,整个购物车都失败
     * @param cartDtos
     */
    @Transactional
    public void decreaceStock(List<CartDto> cartDtos) {
        for (CartDto cartDto : cartDtos) {
            Integer count = stockMapper.decreaceStock(cartDto.getSkuId(), cartDto.getNum());
            if (count!=1){
                throw new LyException(ExceptionEnum.STOCK_NOT_ENOUGH);
            }
        }
    }
}
