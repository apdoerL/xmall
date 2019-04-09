package com.leyou.pojo;

import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @Description 商品分类 类目
 * @Author apdoer
 * @Date 2019/3/19 16:25
 * @Version 1.0
 */
@Table(name="tb_category")
@Data
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Long parentId;

    private Boolean isParent;//注意这个的get和set方法需要手动修改

    private Integer sort;//排序指数,越小越靠前
}
