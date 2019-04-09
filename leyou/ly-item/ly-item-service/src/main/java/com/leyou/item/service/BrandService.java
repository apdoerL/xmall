package com.leyou.item.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.leyou.common.enums.ExceptionEnum;
import com.leyou.common.exception.LyException;
import com.leyou.common.vo.PageResult;
import com.leyou.dto.BrandParamDo;
import com.leyou.item.mapper.BrandMapper;
import com.leyou.pojo.Brand;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @Description 品牌
 * @Author apdoer
 * @Date 2019/3/19 19:14
 * @Version 1.0
 */
@Service
public class BrandService {
    @Autowired
    private BrandMapper brandMapper;


    /**
     * 按条件查询品牌
     * @param brandParamDo
     * @return
     */
    public PageResult<Brand> selBrands(BrandParamDo brandParamDo) {
        //利用分页助手,开始分页
        PageHelper.startPage(brandParamDo.getPage(),brandParamDo.getRows());
        //过滤
        Example example = new Example(Brand.class);
        if (StringUtils.isNotBlank(brandParamDo.getKey())){
            example.createCriteria()
                    .andLike("name","%"+brandParamDo.getKey()+"%")
                    .orEqualTo("letter",brandParamDo.getKey());
        }
        //排序
        if (StringUtils.isNotBlank(brandParamDo.getSortBy())){
            //desc和asc前面的空格很重要
            String orderByClause = brandParamDo.getSortBy()+(brandParamDo.isDesc()?" DESC":" ASC");
            example.setOrderByClause(orderByClause);
        }
        //查询
        List<Brand> brands = brandMapper.selectByExample(example);
        //service中的异常通用异常不能处理
        if (CollectionUtils.isEmpty(brands)){
            throw new LyException(ExceptionEnum.BRANDS_NOT_FOUND);
        }
        //解析分页结果
        PageInfo<Brand> brandPageInfo = new PageInfo<>(brands);
        return new PageResult<>(brandPageInfo.getTotal(),brands);
    }

    /**
     * 新增品牌
     * @param brand
     * @param cids
     */
    @Transactional //添加事务
    public void saveBrand(Brand brand, List<Long> cids) {
        brand.setId(null);//为了安全
        int count = brandMapper.insert(brand);
        if (count!=1){
            throw new LyException(ExceptionEnum.BRAND_SAVE_ERROR);
        }
        //新增关联表
        for (Long cid : cids) {
            Integer count1 = brandMapper.insertCategoryByBrand(cid, brand.getId());//上面新增完会自动回显,所以可以get到
            if (count1!=1){
                throw new LyException(ExceptionEnum.BRAND_SAVE_ERROR);
            }
        }
    }

    /**
     * 根据id查询品牌
     * @param id
     * @return
     */
    public Brand selBrandById(Long id){
        Brand brand = brandMapper.selectByPrimaryKey(id);
        if (brand==null){
            throw new LyException(ExceptionEnum.BRANDS_NOT_FOUND);
        }
        return brand;
    }


    /**
     * 根据;类目id查询该分类下的所有品牌
     * @param cid
     * @return
     */
    public List<Brand> selBrandsByCid(Long cid) {
        List<Brand> brands = brandMapper.selBrandsByCid(cid);
        if(CollectionUtils.isEmpty(brands)){
            throw new LyException(ExceptionEnum.BRANDS_NOT_FOUND);
        }
        return brands;
    }

    /**
     * 根据id集合批量查询brand品牌
     * @param ids
     * @return
     */
    public List<Brand> queryBrandsByIds(List<Long> ids) {
        List<Brand> brands = brandMapper.selectByIdList(ids);
        if (CollectionUtils.isEmpty(brands)){
            throw new LyException(ExceptionEnum.BRANDS_NOT_FOUND);
        }
        return brands;
    }
}
