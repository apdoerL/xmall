package com.leyou.item.service;

import com.leyou.item.mapper.CategoryMapper;
import com.leyou.common.enums.ExceptionEnum;
import com.leyou.common.exception.LyException;
import com.leyou.pojo.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @Description 类目
 * @Author apdoer
 * @Date 2019/3/1916:33
 * @Version 1.0
 */
@Service
public class CategoryService {
    @Autowired
    private CategoryMapper categoryMapper;

    /**
     * 根据pid查询所有类目
     * @param pid
     * @return
     */
    public List<Category> getCategoryByPid(Long pid) {
        Category category = new Category();
        category.setParentId(pid);
        //用该对象中的非空参数作为条件去查询
        List<Category> list = categoryMapper.select(category);
        if (CollectionUtils.isEmpty(list)){
            throw new LyException(ExceptionEnum.CATEGORY_NOT_FOUND);
        }
        return list;
    }

    /**
     * 根据spuid列表查询类目
     * @param ids
     * @return
     */
    public List<Category> selCategoryByIds(List<Long> ids){
        List<Category> categories = categoryMapper.selectByIdList(ids);
        if (CollectionUtils.isEmpty(categories)){
            throw new LyException(ExceptionEnum.CATEGORY_NOT_FOUND);
        }
        return categories;
    }
}
