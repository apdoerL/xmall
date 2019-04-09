package com.leyou.pojo;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

/**
 *@Description 规格参数组
 *@author apdoer
 *@CreateDate 2019/3/20-19:30
 *@Version 1.0
 *===============================
**/
@Data
@Table(name = "tb_spec_group")
public class SpecGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long cid;//类目
    private String name;
    @Transient//不属于实体类
    private List<SpecParam>params;//包含的规格参数模板
}
