package com.rmd.wms.dao;

import java.util.List;
import java.util.Map;

import com.rmd.wms.bean.PurchaseBill;
import com.rmd.wms.bean.PurchaseInInfo;

public interface PurchaseInInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(PurchaseInInfo record);

    int insertBatch(List<PurchaseInInfo> list);

    int insertSelective(PurchaseInInfo record);

    PurchaseInInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PurchaseInInfo record);

    int updateBatchByPrimaryKey(List<PurchaseInInfo> list);

    int updateByPrimaryKey(PurchaseInInfo record);
    
    List<PurchaseInInfo> selectAllByWhere(Map<String, Object> parmaMap);

    int selectCountByInStockNo(String InStockNo);

    int selectWaitCountByPurchaseNo(String purchaseNo);

    List<Map> selectAllByParmMap(Map<String, Object> parmaMap);
}