package com.rmd.wms.dao;

import com.rmd.wms.bean.Goods;

@Deprecated
public interface GoodsMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Goods record);

    int insertSelective(Goods record);

    Goods selectByPrimaryKey(Integer id);

    Goods selectByGoodsCode(String code);

    int updateByPrimaryKeySelective(Goods record);

    int updateByPrimaryKey(Goods record);
}