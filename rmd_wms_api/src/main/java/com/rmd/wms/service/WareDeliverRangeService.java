package com.rmd.wms.service;

import java.util.List;
import java.util.Map;

import com.rmd.wms.bean.WareDeliverRange;

public interface WareDeliverRangeService {

    int deleteByPrimaryKey(Integer id);

    int insert(WareDeliverRange record);

    int insertSelective(WareDeliverRange record);

    WareDeliverRange selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(WareDeliverRange record);

    int updateByPrimaryKey(WareDeliverRange record);
    
    List<WareDeliverRange> selectAllByWhere(Map<String, Object> parmaMap);
    
    int insertBatch(List<WareDeliverRange> list);
}
