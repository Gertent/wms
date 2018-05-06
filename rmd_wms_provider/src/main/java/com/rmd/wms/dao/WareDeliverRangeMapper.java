package com.rmd.wms.dao;

import com.rmd.wms.bean.WareDeliverRange;

import java.util.List;
import java.util.Map;

public interface WareDeliverRangeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(WareDeliverRange record);

    int insertSelective(WareDeliverRange record);

    WareDeliverRange selectByPrimaryKey(Integer id);

    List<WareDeliverRange> selectByCriteria(WareDeliverRange record);
    
    List<WareDeliverRange> selectAllByWhere(Map<String, Object> parmaMap);

    int updateByPrimaryKeySelective(WareDeliverRange record);

    int updateByPrimaryKey(WareDeliverRange record);
    
    int insertBatch(List<WareDeliverRange> list);
}