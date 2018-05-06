package com.rmd.wms.dao;

import com.rmd.wms.bean.LogisticsFreightTemplate;

import java.util.List;
import java.util.Map;

public interface LogisticsFreightTemplateMapper {
    int deleteByPrimaryKey(Integer id);

    int deleteByCriteria(LogisticsFreightTemplate record);

    int insert(LogisticsFreightTemplate record);

    int insertSelective(LogisticsFreightTemplate record);

    LogisticsFreightTemplate selectByPrimaryKey(Integer id);

    List<LogisticsFreightTemplate> selectByCriteria(Map<String,Object> map);

    int updateByPrimaryKeySelective(LogisticsFreightTemplate record);

    int updateByPrimaryKey(LogisticsFreightTemplate record);
}