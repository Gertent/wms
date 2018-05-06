package com.rmd.wms.dao;

import com.rmd.wms.bean.LogisticsCompany;

import java.util.List;
import java.util.Map;

public interface LogisticsCompanyMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(LogisticsCompany record);

    int insertSelective(LogisticsCompany record);

    LogisticsCompany selectByPrimaryKey(Integer id);

    List<LogisticsCompany> selectByCriteria(LogisticsCompany record);

    List<LogisticsCompany> selectByProvCodeAndWeight(Map<String, Object> parmaMap);

    int updateByPrimaryKeySelective(LogisticsCompany record);

    int updateByPrimaryKey(LogisticsCompany record);
    
    List<LogisticsCompany> selectAllByWhere(Map<String, Object> parmaMap);
    
    int selectMaxId();
    
}