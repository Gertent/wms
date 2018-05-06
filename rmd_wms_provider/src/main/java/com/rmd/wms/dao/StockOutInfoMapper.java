package com.rmd.wms.dao;

import java.util.List;
import java.util.Map;

import com.rmd.wms.bean.StockOutInfo;

import java.util.List;

public interface StockOutInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(StockOutInfo record);

    int insertBatch(List<StockOutInfo> list);

    int insertSelective(StockOutInfo record);

    StockOutInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(StockOutInfo record);

    int updateByPrimaryKey(StockOutInfo record);

    List<StockOutInfo> selectByCriteria(StockOutInfo record);

    List<StockOutInfo> selectByOrderNo(String orderNo);

    int selectCountByOrderNo(String orderNo);

    List<StockOutInfo> selectAllByWhere(Map<String, Object> parmaMap);
}