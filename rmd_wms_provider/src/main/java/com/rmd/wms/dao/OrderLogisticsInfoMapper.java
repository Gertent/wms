package com.rmd.wms.dao;

import com.rmd.wms.bean.OrderLogisticsInfo;

public interface OrderLogisticsInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OrderLogisticsInfo record);

    int insertSelective(OrderLogisticsInfo record);

    OrderLogisticsInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OrderLogisticsInfo record);

    int updateByPrimaryKey(OrderLogisticsInfo record);
    
    OrderLogisticsInfo selectByOrderNo(String orderNo);
}