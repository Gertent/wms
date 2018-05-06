package com.rmd.wms.dao;

import java.util.List;
import java.util.Map;

import com.rmd.wms.bean.DeliveryBill;

public interface DeliveryBillMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(DeliveryBill record);

    int insertSelective(DeliveryBill record);

    DeliveryBill selectByPrimaryKey(Integer id);

    List<DeliveryBill> selectByPrimaryKeys(List<Integer> list);

    DeliveryBill selectByDeliveryNo(String deliveryNo);

    int updateByPrimaryKeySelective(DeliveryBill record);

    int updateByPrimaryKey(DeliveryBill record);
    
    List<DeliveryBill> selectAllByWhere(Map<String, Object> parmaMap);

    List<DeliveryBill> selectByUser(DeliveryBill record);
}