package com.rmd.wms.dao;

import com.rmd.wms.bean.LocationGoodsBindIn;

import java.util.List;
import java.util.Map;

public interface LocationGoodsBindInMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(LocationGoodsBindIn record);

    int insertBatch(List<LocationGoodsBindIn> list);

    int insertSelective(LocationGoodsBindIn record);

    LocationGoodsBindIn selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(LocationGoodsBindIn record);

    int updateByPrimaryKey(LocationGoodsBindIn record);
    
    List<LocationGoodsBindIn> selectAllByWhere(Map<String, Object> parmaMap);
}