package com.leyou.item.service;

import com.leyou.common.enums.ExceptionEnum;
import com.leyou.common.exception.LyException;
import com.leyou.item.mapper.SpecGroupMapper;
import com.leyou.item.mapper.SpecParamMapper;
import com.leyou.pojo.SpecGroup;
import com.leyou.pojo.SpecParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *@Description 商品规格
 *@author apdoer
 *@CreateDate 2019/3/20-19:48
 *@Version 1.0
 *===============================
**/
@Service
public class SpecificationService {
    @Autowired
    private SpecGroupMapper specGroupMapper;
    @Autowired
    private SpecParamMapper specParamMapper;
    /**
     * 查询所有的商品规格参数模板组
     * @param cid
     * @return
     */
    public List<SpecGroup> selSpecGroupsByCid(Long cid) {
        SpecGroup specGroup = new SpecGroup();
        specGroup.setCid(cid);
        return specGroupMapper.select(specGroup);
    }

    /**
     * 查询所有的商品规格参数
     * @param gid
     * @return
     */
    public List<SpecParam> selSpecParamsByParams(Long gid,Long cid,Boolean searching) {
        SpecParam specParam = new SpecParam();
        specParam.setGroupId(gid);
        specParam.setCid(cid);
        specParam.setSearching(searching);
        return specParamMapper.select(specParam);
    }

    /**
     * 新增商品规格参数
     * @param specParam
     */
    public void saveSpecParam(SpecParam specParam) {

    }

    /**
     * 根据cid 查询参数组及组内所有参数
     * @param cid
     * @return
     */
    public List<SpecGroup> queryListByCid(Long cid) {
        //查询规格组
        List<SpecGroup> groups = selSpecGroupsByCid(cid);
        //查询当前分类下的所有规格参数
        List<SpecParam> specParams = selSpecParamsByParams(null, cid, null);
        //先把规格参数变成map,key是规格参数组的id,value是组下所有参数
        Map<Long,List<SpecParam>> map = new HashMap<>();
        for (SpecParam param : specParams) {
            if (!map.containsKey(param.getGroupId())){
                //如果map中不存在该组,创建改组
                map.put(param.getGroupId(),new ArrayList<>());
            }
            //获取该list并设值  这时候已经存在该list,直接设值
            map.get(param.getGroupId()).add(param);
        }

        //把规格参数设置到对应的规格参数组中去
        for (SpecGroup group : groups) {
            group.setParams(map.get(group.getId()));
        }
        return groups;
    }
}
