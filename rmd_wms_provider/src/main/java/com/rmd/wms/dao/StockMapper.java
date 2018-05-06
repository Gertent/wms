package com.rmd.wms.dao;

import com.rmd.wms.bean.Stock;
import com.rmd.wms.bean.vo.StockInfoVo;

import java.util.List;
import java.util.Map;

public interface StockMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Stock record);

    int insertSelective(Stock record);

    int insertBatch(List<Stock> list);

    Stock selectByPrimaryKey(Integer id);

    Stock selectUnionPrimaryKey(Stock record);

    List<StockInfoVo> selectWareStockByCriteria(StockInfoVo record);
    
    List<StockInfoVo> selectWareStockByParmMap(Map<String, Object> map);

    int updateByPrimaryKeySelective(Stock record);

    int updateByPrimaryKey(Stock record);

    int updateBatchByPrimaryKey(List<Stock> list);
}