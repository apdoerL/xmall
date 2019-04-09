package com.leyou.common.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Description 分页查询视图对象
 * @Author apdoer
 * @Date 2019/3/19 19:35
 * @Version 1.0
 */
@Data
public class PageResult<T>{

    private Long total;//总条数
    private Integer totalPage;//总页数
    private List<T> items;//当前页数据

    public PageResult() {
    }

    public PageResult(Long total, List<T>items){
        this.total = total;
        this.items = items;
    }

    public PageResult(Long total, Integer totalPage, List<T> items) {
        this.total = total;
        this.totalPage = totalPage;
        this.items = items;
    }
}
