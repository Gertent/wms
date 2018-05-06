package com.rmd.wms.dao;

import com.rmd.wms.bean.CheckInfo;

import java.util.List;
import java.util.Map;

public interface CheckInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CheckInfo record);

    int insertBatch(List<CheckInfo> list);

    int insertSelective(CheckInfo record);

    CheckInfo selectByPrimaryKey(Integer id);

    List<CheckInfo> selectByIds(List<Integer> list);

    List<CheckInfo> selectByCheckNo(String checkNo);

    int updateByPrimaryKeySelective(CheckInfo record);

    int updateByPrimaryKey(CheckInfo record);

    int updateBatchByPrimaryKeySelective(List<CheckInfo> list);
    
    List<CheckInfo> selectByCriteria(Map<String, Object> map);
}