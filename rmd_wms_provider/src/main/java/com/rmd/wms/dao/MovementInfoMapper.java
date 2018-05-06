package com.rmd.wms.dao;

import com.rmd.wms.bean.MovementInfo;

import java.util.List;
import java.util.Map;

public interface MovementInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(MovementInfo record);

    int insertBatch(List<MovementInfo> list);

    int insertSelective(MovementInfo record);

    MovementInfo selectByPrimaryKey(Integer id);

    List<MovementInfo> selectByCriteria(MovementInfo record);

    int updateByPrimaryKeySelective(MovementInfo record);

    int updateByPrimaryKey(MovementInfo record);

    int updateBatchByPrimaryKey(List<MovementInfo> list);
    
    List<MovementInfo> selectAllByWhere(Map<String, Object> parmaMap);
}