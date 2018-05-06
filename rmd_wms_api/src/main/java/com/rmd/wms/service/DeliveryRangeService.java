package com.rmd.wms.service;

import java.util.List;
import java.util.Map;

import com.rmd.wms.bean.DeliveryRange;

public interface DeliveryRangeService {

    int deleteByPrimaryKey(Integer id);

    /**
     * 根据条件删除，慎用
     * @param record
     * @return
     * */
    int deleteByCriteria(DeliveryRange record);

    int insert(DeliveryRange record);

    int insertSelective(DeliveryRange record);

    DeliveryRange selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(DeliveryRange record);

    int updateByPrimaryKey(DeliveryRange record);
    
    List<DeliveryRange> selectAllByWhere(Map<String, Object> parmaMap);
    
    int insertBatch(List<DeliveryRange> list);
}
