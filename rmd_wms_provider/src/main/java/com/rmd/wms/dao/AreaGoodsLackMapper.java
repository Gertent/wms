package com.rmd.wms.dao;

import com.rmd.wms.bean.AreaGoodsLack;

@Deprecated
public interface AreaGoodsLackMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(AreaGoodsLack record);

    int insertSelective(AreaGoodsLack record);

    AreaGoodsLack selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(AreaGoodsLack record);

    int updateByPrimaryKey(AreaGoodsLack record);
}