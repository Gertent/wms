package com.rmd.wms.dao;

import com.rmd.wms.bean.Warehouse;

import java.util.List;
import java.util.Map;

public interface WarehouseMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Warehouse record);

    int insertSelective(Warehouse record);

    Warehouse selectByPrimaryKey(Integer id);

    Warehouse selectByWareCode(String code);

    int updateByPrimaryKeySelective(Warehouse record);

    int updateByPrimaryKey(Warehouse record);

    List<Warehouse> selectByCriteria(Warehouse record);
    
    List<Warehouse> selectByParam(Map<String, Object> paraMap);

    List<Warehouse> selectByUserId(Integer userId);

	int selectByName(Warehouse warehouse);

	int selectByCode(Warehouse warehouse);
}