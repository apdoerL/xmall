package com.leyou.page.service;

import com.leyou.page.client.BrandClient;
import com.leyou.page.client.CategoryClient;
import com.leyou.page.client.GoodsClient;
import com.leyou.page.client.SpecificationClient;
import com.leyou.pojo.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.thymeleaf.ThymeleafAutoConfiguration;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *@Description 商品详情
 *@author apdoer
 *@CreateDate 2019/3/24-21:51
 *@Version 1.0
 *===============================
**/
@Service
@Slf4j
public class PageService {
    @Autowired
    private GoodsClient goodsClient;
    @Autowired
    private CategoryClient categoryClient;
    @Autowired
    private BrandClient brandClient;
    @Autowired
    private SpecificationClient specificationClient;
    @Autowired
    TemplateEngine templateEngine;


    public Map<String ,Object> loadModel(Long spuId){
        Map<String,Object> model = new HashMap<>();
        //查询spu及下面所有的sku和spudetail
        Spu spu = goodsClient.querySpuBySpuId(spuId);
        //sku
        List<Sku> skus = spu.getSkus();
        //spudetail
        SpuDetail spuDetail = spu.getSpuDetail();
        //查询brand
        Brand brand = brandClient.queryBrandById(spu.getBrandId());

        //查询商品分类
        List<Category> categories = categoryClient
                .queryCategoryByIds(Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3()));
        //查询规格参数及下面的所有参数
        List<SpecGroup> specGroups = specificationClient.selSpecGroupsByCid(spu.getCid3());
        //装填模型数据
        model.put("title",spu.getTitle());
        model.put("subTitle",spu.getSubTitle());
        model.put("skus",skus);
        model.put("detail",spuDetail);
        model.put("brand",brand);
        model.put("categories",categories);
        model.put("specs",specGroups);
        return model;
    }


    /**
     * 创建静态页
     * @param spuId
     */
    public void createHtml(Long spuId){
        //上下文
        Context context = new Context();
        context.setVariables(loadModel(spuId));
        //输出流
        File dest = new File("G:/my/", spuId + ".html");
        //如果目标文件已经存在,先删除
        if(dest.exists()){
            dest.delete();
        }
        try {
            PrintWriter writer = new PrintWriter(dest,"utf-8");
            templateEngine.process("item",context,writer);
        } catch (FileNotFoundException e) {
            log.error("生成静态也异常"+e);
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            log.error("生成静态也异常"+e);
            e.printStackTrace();
        }
        //生成html

    }


    /**
     * 商品删除时删除静态页
     * @param spuId
     */
    public void deleteHtml(Long spuId) {
        File dest = new File("G:/my/", spuId + ".html");
        if (dest.exists()){
            dest.delete();
        }
    }
}
