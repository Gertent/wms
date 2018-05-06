package com.rmd.wms.dao;

import java.util.List;
import java.util.Map;

import com.rmd.wms.bean.GroundingBill;
import com.rmd.wms.bean.InStockBill;

public interface GroundingBillMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(GroundingBill record);

    int insertSelective(GroundingBill record);

    GroundingBill selectByPrimaryKey(Integer id);

    GroundingBill selectByInStockNo(String inStockNo);

    int updateByPrimaryKeySelective(GroundingBill record);

    int updateByPrimaryKey(GroundingBill record);
    
    List<GroundingBill> selectAllByWhere(Map<String, Object> parmaMap);
    
    GroundingBill selectByGroundingNo(String groundingNo);
    
    int insertBatch( List<GroundingBill> list);

    List<Map<String,Object>> selectAllByParmMap(Map<String,Object> parmaMap);
    
}