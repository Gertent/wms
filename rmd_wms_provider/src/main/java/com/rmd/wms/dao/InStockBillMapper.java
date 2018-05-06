package com.rmd.wms.dao;

import java.util.List;
import java.util.Map;

import com.rmd.wms.bean.InStockBill;
import com.rmd.wms.bean.PurchaseInInfo;

public interface InStockBillMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(InStockBill record);

    int insertSelective(InStockBill record);

    InStockBill selectByPrimaryKey(Integer id);

    InStockBill selectByInStockNo(String inStockNo);

    int updateByPrimaryKeySelective(InStockBill record);

    int updateByPrimaryKey(InStockBill record);
    
    List<InStockBill> selectAllByWhere(Map<String, Object> parmaMap);
    
    int insertBatch(List<InStockBill> list);

    List<Map<String,Object>> selectAllByParmMap(Map<String,Object> parmaMap);
}