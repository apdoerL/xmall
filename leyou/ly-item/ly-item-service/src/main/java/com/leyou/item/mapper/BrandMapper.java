package com.leyou.item.mapper;

import com.leyou.common.mapper.BaseMapper;
import com.leyou.pojo.Brand;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @Description 品牌
 * @Author apdoer
 * @Date 2019/3/19 19:11
 * @Version 1.0
 */
public interface BrandMapper extends BaseMapper<Brand> {

    /**
     * 新增类目表和品牌表的关系表
     * @param cid
     * @param bid
     * @return
     */
    @Insert("INSERT INTO tb_category_brand (category_id, brand_id) VALUES (#{cid},#{bid})")
    Integer insertCategoryByBrand(@Param("cid") Long cid, @Param("bid") Long bid);

    /**
     * 根据类目id查询该类目下所有的品牌
     * @param cid
     * @return
     */
    @Select("select b.* from tb_category_brand cb inner join tb_brand b on b.id= cb.brand_id where cb.category_id=#{cid}")
    List<Brand> selBrandsByCid(@Param("cid") Long cid);

}
