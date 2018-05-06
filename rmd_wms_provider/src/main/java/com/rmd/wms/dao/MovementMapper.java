package com.rmd.wms.dao;

import java.util.List;
import java.util.Map;

import com.rmd.wms.bean.Movement;

public interface MovementMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Movement record);

    int insertSelective(Movement record);

    Movement selectByPrimaryKey(Integer id);

    List<Movement> selectByCriteria(Movement record);

    int updateByPrimaryKeySelective(Movement record);

    int updateByPrimaryKey(Movement record);
    
    List<Movement> selectAllByWhere(Map<String, Object> parmaMap);

    List<Map<String,Object>> selectAllByParmMap(Map<String,Object> parmaMap);
}