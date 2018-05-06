package com.rmd.wms.dao;

import java.util.List;
import java.util.Map;

import com.rmd.wms.bean.DeliveryRange;

public interface DeliveryRangeMapper {
    int deleteByPrimaryKey(Integer id);

    int deleteByCriteria(DeliveryRange record);

    int insert(DeliveryRange record);

    int insertSelective(DeliveryRange record);

    DeliveryRange selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(DeliveryRange record);

    int updateByPrimaryKey(DeliveryRange record);
    
    List<DeliveryRange> selectAllByWhere(Map<String, Object> parmaMap);

    List<DeliveryRange> selectByCriteria(DeliveryRange record);

    int insertBatch(List<DeliveryRange> list);
}