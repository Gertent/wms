package com.rmd.wms.service;

import java.util.List;
import java.util.Map;

import com.rmd.wms.bean.LocationGoodsBindIn;

public interface LocationGoodsBindInService {
    
    int deleteByPrimaryKey(Integer id);

    int insert(LocationGoodsBindIn record);

    int insertSelective(LocationGoodsBindIn record);

    LocationGoodsBindIn selectByPrimaryKey(Integer id);

    List<LocationGoodsBindIn> listLocationGoodsBindIns(Map<String, Object> param);

    int updateByPrimaryKeySelective(LocationGoodsBindIn record);

    int updateByPrimaryKey(LocationGoodsBindIn record);
	


}
