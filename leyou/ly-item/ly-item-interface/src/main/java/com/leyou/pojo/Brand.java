package com.leyou.pojo;

import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @Description 品牌
 * @Author apdoer
 * @Date 2019/3/19 19:12
 * @Version 1.0
 */
@Data
@Table(name="tb_brand")
public class Brand {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String image;//品牌图片
    private Character letter;//
}
