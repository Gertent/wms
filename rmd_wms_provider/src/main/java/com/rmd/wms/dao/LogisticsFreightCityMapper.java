package com.rmd.wms.dao;

import com.rmd.wms.bean.LogisticsFreightCity;

import java.util.List;
import java.util.Map;

public interface LogisticsFreightCityMapper {
    int deleteByPrimaryKey(Integer id);

    int deleteByCriteria(LogisticsFreightCity record);

    int insert(LogisticsFreightCity record);

    int insertSelective(LogisticsFreightCity record);

    LogisticsFreightCity selectByPrimaryKey(Integer id);

    List<LogisticsFreightCity> selectByCriteria(Map<String,Object> map);

    int updateByPrimaryKeySelective(LogisticsFreightCity record);

    int updateByPrimaryKey(LogisticsFreightCity record);

    int insertBatch(List<LogisticsFreightCity> list);
}