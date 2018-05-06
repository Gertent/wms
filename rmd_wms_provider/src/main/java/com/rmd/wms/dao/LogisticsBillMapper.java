package com.rmd.wms.dao;

import com.rmd.wms.bean.LogisticsBill;

public interface LogisticsBillMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(LogisticsBill record);

    int insertSelective(LogisticsBill record);

    LogisticsBill selectByPrimaryKey(Integer id);
    
    LogisticsBill selectByOrderNo(String orderNo);

    int updateByPrimaryKeySelective(LogisticsBill record);

    int updateByPrimaryKey(LogisticsBill record);
}