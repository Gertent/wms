package com.rmd.wms.dao;

import com.rmd.wms.bean.OrderLifecircle;

public interface OrderLifecircleMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OrderLifecircle record);

    int insertSelective(OrderLifecircle record);

    OrderLifecircle selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OrderLifecircle record);

    int updateByPrimaryKey(OrderLifecircle record);
}